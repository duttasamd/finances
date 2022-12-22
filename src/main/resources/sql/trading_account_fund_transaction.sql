CREATE TABLE `financesdb`.`trading_account_fund_transaction` (
  `uuid` VARCHAR(36) NOT NULL,
  `trading_account_uuid` VARCHAR(36) NOT NULL,
  `event_uuid` VARCHAR(36) NOT NULL,
  `type` ENUM('PAY_IN', 'PAY_OUT') NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`uuid`)
);
