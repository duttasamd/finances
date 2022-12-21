CREATE TABLE IF NOT EXISTS `financesdb`.`fixed_deposit_account` (
  `uuid` VARCHAR(36) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `number` VARCHAR(128) NOT NULL,
  `bank` VARCHAR(128) NULL DEFAULT NULL,
  `bank_identifier` VARCHAR(45) NULL DEFAULT NULL,
  `current_amount` DECIMAL(10,2) NULL DEFAULT 0,
  `currency` ENUM('EUR', 'INR') NULL DEFAULT 'EUR',
  'is_primary' TINYINT(1) NULL DEFAULT 0,
PRIMARY KEY (`uuid`));
