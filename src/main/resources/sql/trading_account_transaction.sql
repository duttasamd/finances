CREATE TABLE `financesdb`.`trading_account_transaction` (
  `uuid` VARCHAR(36) NOT NULL,
  `event_uuid` VARCHAR(36) NOT NULL,
  `trading_account_uuid` VARCHAR(36) NOT NULL,
  `security_uuid` VARCHAR(36) NOT NULL,
  `quantity` DECIMAL(10,3) NOT NULL,
  `trade_price` DECIMAL(10,3) NOT NULL,
  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`uuid`));
