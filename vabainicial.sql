-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 01-Jul-2021 às 20:03
-- Versão do servidor: 10.6.0-MariaDB
-- PHP Version: 5.6.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `anamnese`
--

-- --------------------------------------------------------

--
-- Structure for view `vabainicial`
--

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vabainicial`  AS  select `t1`.`Nome` AS `Nome`,`t1`.`dataNasc` AS `dataNasc`,`t1`.`Sexo` AS `Sexo`,`t1`.`Cor` AS `Cor`,`t1`.`estadoCivil` AS `estadoCivil`,`t1`.`RG` AS `RG`,`t1`.`Endereco` AS `endereco`,`t1`.`Cargo` AS `cargo`,`t1`.`mae` AS `mae`,`t1`.`pai` AS `pai`,`t1`.`naturalidade` AS `naturalidade`,`t1`.`Cidade` AS `Cidade`,`t1`.`idPaciente` AS `idPaciente`,`t2`.`CNPJ` AS `CNPJ` from (`paciente` `t1` join `empresa` `t2` on(`t1`.`id_empresa` = `t2`.`idEmpresa`)) ;

--
-- VIEW  `vabainicial`
-- Data: Nenhum
--

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
