package com.samratdutta.finances.service;

import com.samratdutta.finances.model.Account;
import com.samratdutta.finances.model.CurrentAccountTransaction;
import com.samratdutta.finances.model.Event;
import com.samratdutta.finances.model.Expenditure;
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
import java.util.UUID;

@Slf4j
@Service
public class ExpenditureService {
    @Autowired
    private DataSource dataSource;
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
}
