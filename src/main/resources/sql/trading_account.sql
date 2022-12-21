CREATE TABLE `financesdb`.`trading_account` (
  `uuid` VARCHAR(36) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `number` VARCHAR(128) NOT NULL,
  `currency` ENUM('EUR', 'INR') NULL DEFAULT 'EUR'
PRIMARY KEY (`uuid`));