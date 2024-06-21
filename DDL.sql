CREATE DATABASE `habits_tracker`;
USE `habits_tracker`;

CREATE TABLE `user` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `verified` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `habits_tracker`.`email_verification` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(45) NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC) VISIBLE,
  CONSTRAINT `user_email_verification`
    FOREIGN KEY (`idUser`)
    REFERENCES `habits_tracker`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE `habits_tracker`.`habit_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `user_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_habit_category_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_habit_category`
    FOREIGN KEY (`user_id`)
    REFERENCES `habits_tracker`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE `habits_tracker`.`habit` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` TEXT NULL,
  `habit_trigger` TEXT NULL,
  `category_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_habit_idx` (`category_id` ASC) VISIBLE,
  INDEX `user_habit_idx1` (`user_id` ASC) VISIBLE,
  CONSTRAINT `category`
    FOREIGN KEY (`category_id`)
    REFERENCES `habits_tracker`.`habit_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_habit`
    FOREIGN KEY (`user_id`)
    REFERENCES `habits_tracker`.`user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);