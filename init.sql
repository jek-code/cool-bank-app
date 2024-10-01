CREATE SCHEMA `banking-app-schema`;

USE `banking-app-schema`;

SET FOREIGN_KEY_CHECKS = 0;


CREATE TABLE `bank_accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `birthday` DATETIME DEFAULT NULL,
  `balance` DOUBLE DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_id` int NOT NULL,
  `type` int NOT NULL,
  `date_created` DATETIME DEFAULT NULL,
  `sum` DOUBLE DEFAULT NULL,
  `order_id` varchar(45),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES bank_accounts (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



SET FOREIGN_KEY_CHECKS = 1;