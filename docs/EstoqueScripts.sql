-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `estoque` DEFAULT CHARACTER SET utf8 ;
USE `estoque` ;

-- -----------------------------------------------------
-- Table `mydb`.`Fornecedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Fornecedor` (
  `Codigo` INT NOT NULL,
  `CNPJ` VARCHAR(18) NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Telefone` VARCHAR(20) NULL,
  `Email` VARCHAR(60) NULL,
  PRIMARY KEY (`Codigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Produto` (
  `Codigo` INT NOT NULL,
  `Descricao` VARCHAR(45) NOT NULL,
  `Qtde` INT NULL,
  `CustoMedio` DECIMAL(8,2) NULL,
  PRIMARY KEY (`Codigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Cliente` (
  `Codigo` INT NOT NULL,
  `CPF` VARCHAR(14) NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Celular` VARCHAR(20) NULL,
  `Email` VARCHAR(60) NULL,
  PRIMARY KEY (`Codigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Entrada`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Entrada` (
  `Codigo` INT NOT NULL,
  `Data` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Qtde` INT NULL,
  `Valor` DECIMAL(8,2) NULL,
  `Fornecedor` INT NOT NULL,
  `Produto` INT NOT NULL,
  PRIMARY KEY (`Codigo`),
  INDEX `fk_Entrada_Fornecedor_idx` (`Fornecedor` ASC) VISIBLE,
  INDEX `fk_Entrada_Produto1_idx` (`Produto` ASC) VISIBLE,
  CONSTRAINT `fk_Entrada_Fornecedor`
    FOREIGN KEY (`Fornecedor`)
    REFERENCES `Fornecedor` (`Codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Entrada_Produto1`
    FOREIGN KEY (`Produto`)
    REFERENCES `Produto` (`Codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Saida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Saida` (
  `Codigo` INT NOT NULL,
  `Data` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '\n',
  `Qtde` INT NULL,
  `Valor` DECIMAL(8,2) NULL,
  `Produto` INT NOT NULL,
  `Cliente` INT NOT NULL,
  PRIMARY KEY (`Codigo`),
  INDEX `fk_Saida_Produto1_idx` (`Produto` ASC) VISIBLE,
  INDEX `fk_Saida_Cliente1_idx` (`Cliente` ASC) VISIBLE,
  CONSTRAINT `fk_Saida_Produto1`
    FOREIGN KEY (`Produto`)
    REFERENCES `Produto` (`Codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Saida_Cliente1`
    FOREIGN KEY (`Cliente`)
    REFERENCES `Cliente` (`Codigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
