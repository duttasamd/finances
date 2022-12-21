CREATE TABLE `financesdb`.`fixed_deposit_account` (
  `uuid` VARCHAR(36) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `number` VARCHAR(128) NOT NULL,
  `bank` VARCHAR(128) NULL DEFAULT NULL,
  `bank_identifier` VARCHAR(45) NULL DEFAULT NULL,
  `amount` DECIMAL(10,2) NULL DEFAULT 0,
  `currency` ENUM('EUR', 'INR') NULL DEFAULT 'EUR',
  `rate_of_interest` DECIMAL(10,2) NULL DEFAULT 0,
  `date_of_deposit` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_of_maturity` TIMESTAMP NOT NULL,
PRIMARY KEY (`uuid`));
