CREATE TABLE `financesdb`.`expenditure` (
  `uuid` VARCHAR(36) NOT NULL,
  `eventUuid` VARCHAR(36) NOT NULL,
  `type` ENUM('RESTAURANT', 'GROCERIES', 'INSURANCE', 'TRANSPORT', 'UTILITY', 'PURCHASE', 'MISCELLANEOUS') NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `comment` VARCHAR(200) NULL DEFAULT NULL,
  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `currency` ENUM('INR', 'EUR') NOT NULL DEFAULT 'EUR',
  PRIMARY KEY (`uuid`));
