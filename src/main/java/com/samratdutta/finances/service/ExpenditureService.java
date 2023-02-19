package com.samratdutta.finances.service;

import com.samratdutta.finances.helper.exception.NotFoundException;
import com.samratdutta.finances.model.*;
import com.samratdutta.finances.model.dto.ExpenditureDTO;
import com.samratdutta.finances.model.dto.ExpenditureSummary;
import com.samratdutta.finances.repository.AccountRepository;
import com.samratdutta.finances.repository.EventRepository;
import com.samratdutta.finances.repository.ExpenditureRepository;
import com.samratdutta.finances.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Slf4j
@Service
public class ExpenditureService {
    @Autowired
    private DataSource dataSource;
    @Autowired
    BudgetService budgetService;

    public UUID buy(UUID currentAccountUuid, Expenditure expenditure) {
        UUID eventUUID = UUID.randomUUID();
        var event = Event.builder()
                .uuid(eventUUID)
                .type(Event.Type.EXPENDITURE)
                .build();

        var currentAccountTransaction = CurrentAccountTransaction.builder()
                .uuid(UUID.randomUUID())
                .currentAccountUuid(currentAccountUuid)
                .eventUuid(eventUUID)
                .amount(-expenditure.getAmount()) // NEGATIVE OF THE AMOUNT
                .build();

        expenditure.setEventUuid(eventUUID);
        if(expenditure.getComment() == null) {
            expenditure.setComment("-");
        }

        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.beginTransaction()) {
            var eventRepository = new EventRepository(connection);
            var accountRepository = new AccountRepository(connection);
            var expenditureRepository = new ExpenditureRepository(connection);
            var transactionRepository = new TransactionRepository(connection);

            eventRepository.add(event);

            Account currentAccount = accountRepository.getAccount(Account.Type.CURRENT, currentAccountUuid);
            accountRepository.adjustCurrentAmount(currentAccount, expenditure.getAmount(), false);

            expenditure.setUuid(UUID.randomUUID());
            expenditure.setCurrency(currentAccount.getCurrency());

            transactionRepository.add(currentAccountTransaction);
            expenditureRepository.add(expenditure);

            connection.commit();
        }

        return eventUUID;
    }

    public Map<Expenditure.Type, List<ExpenditureDTO>> list(int year, int month, Budget.Type type) {
        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.open()) {
            var expenditureRepository = new ExpenditureRepository(connection);
            List<ExpenditureDTO> expenditures = expenditureRepository.list(year, month, type);

            LOGGER.info("{} {}", expenditures.size(), expenditures);

            return expenditures.stream().collect(groupingBy(ExpenditureDTO::getType));
        }
    }

    public ExpenditureSummary getExpenditureSummary(int year, int month, Budget.Type budgetType) {
        List<BudgetEntry> budgetEntries = budgetService.list(year, month);
        Map<Expenditure.Type, List<ExpenditureDTO>> expenditureMap = list(year, month, budgetType);

        List<Expenditure.Type> fixedTypes = Arrays.asList(Expenditure.Type.UTILITY,
                Expenditure.Type.INSURANCE,
                Expenditure.Type.SUBSCRIPTION,
                Expenditure.Type.TRANSPORT);

        double budget = 0;
        double spent = 0;
        double remainingFixed = 0;

        for (BudgetEntry budgetEntry : budgetEntries) {
            budget += budgetEntry.getAmount();

            List<ExpenditureDTO> expenditures = expenditureMap.get(budgetEntry.getType());
            double expenditure = expenditures != null ? expenditures.stream().mapToDouble(ExpenditureDTO::getAmount).sum() : 0;

            spent += expenditure;

            if(fixedTypes.contains(budgetEntry.getType())) {
                remainingFixed += budgetEntry.getAmount() - expenditure;
            }
        }

        remainingFixed = remainingFixed >= 0 ? remainingFixed : 0;

        var expenditureSummary = new ExpenditureSummary();
        expenditureSummary.setBudget(budget);
        expenditureSummary.setVariableSpent(spent);
        expenditureSummary.setRemainingFixed(remainingFixed);

        return expenditureSummary;
    }

    public boolean removeExpenditure(UUID uuid) throws NotFoundException {
        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.beginTransaction()) {
            var eventRepository = new EventRepository(connection);
            var accountRepository = new AccountRepository(connection);
            var expenditureRepository = new ExpenditureRepository(connection);
            var transactionRepository = new TransactionRepository(connection);

            var expenditure = expenditureRepository.get(uuid);
            if(expenditure == null) {
                throw new NotFoundException("Expenditure Not Found", uuid);
            }
             var transactionList
                    = transactionRepository.getCurrentAccountTransactionList(expenditure.getEventUuid());

            for (var transaction:transactionList) {
                CurrentAccount currentAccount = (CurrentAccount) accountRepository.getAccount(Account.Type.CURRENT, transaction.getCurrentAccountUuid());
                accountRepository.adjustCurrentAmount(currentAccount, Math.abs(transaction.getAmount()), true);
                transactionRepository.remove(transaction);
                expenditureRepository.remove(uuid);
            }

            eventRepository.remove(expenditure.getUuid());
            connection.commit();
        }
        return true;
    }
}
