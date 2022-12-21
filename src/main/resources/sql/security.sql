CREATE TABLE `security` (
  `uuid` VARCHAR(36) NOT NULL,
  `symbol` varchar(45) NOT NULL,
  `name` varchar(128) NOT NULL,
  `type` enum('STOCK','BOND','ETF','CD','CFD') DEFAULT NULL,
  PRIMARY KEY (`uuid`)
);
