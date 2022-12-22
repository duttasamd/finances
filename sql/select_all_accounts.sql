SELECT 'current', uuid, name, number, currency, bank, bank_identifier, current_amount, is_primary, current_amount, '', '', '' FROM financesdb.current_account
UNION
SELECT 'fixed', uuid, name, number, currency, bank, bank_identifier, current_amount, 0, amount, rate_of_interest, date_of_deposit, date_of_maturity FROM financesdb.fixed_deposit_account
UNION
SELECT 'trading', uuid, name, number, currency, '', '', current_amount, 0, '', '', '', ''  FROM financesdb.trading_account