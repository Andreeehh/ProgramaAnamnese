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
-- Structure for view `pesquisaanamnese`
--

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pesquisaanamnese`  AS  select `t1`.`Nome` AS `Nome`,`t1`.`dataNasc` AS `dataNasc`,`t1`.`Sexo` AS `Sexo`,`t1`.`Cor` AS `Cor`,`t1`.`estadoCivil` AS `estadoCivil`,`t1`.`RG` AS `RG`,`t1`.`endereco` AS `endereco`,`t1`.`cargo` AS `cargo`,`t1`.`mae` AS `mae`,`t1`.`pai` AS `pai`,`t1`.`naturalidade` AS `naturalidade`,`t1`.`Cidade` AS `Cidade`,`t1`.`idPaciente` AS `idPaciente`,`t1`.`CNPJ` AS `CNPJ`,`t2`.`idAnamnese` AS `idAnamnese`,`t2`.`id_Paciente` AS `id_Paciente`,`t2`.`tipoExame` AS `tipoExame`,`t2`.`data` AS `data`,`t2`.`apAcidente` AS `apAcidente`,`t2`.`apCiclo` AS `apCiclo`,`t2`.`apHep` AS `apHep`,`t2`.`apMaleita` AS `apMaleita`,`t2`.`apMenarca` AS `apMenarca`,`t2`.`apNefrit` AS `apNefrit`,`t2`.`apOpera` AS `apOpera`,`t2`.`apOutras` AS `apOutras`,`t2`.`apParasita` AS `apParasita`,`t2`.`apPartos` AS `apPartos`,`t2`.`apPleuriz` AS `apPleuriz`,`t2`.`apReum` AS `apReum`,`t2`.`apTbc` AS `apTbc`,`t2`.`apVenereas` AS `apVenereas`,`t2`.`ap` AS `ap`,`t2`.`apHpma` AS `apHpma`,`t2`.`fumo` AS `fumo`,`t2`.`alcool` AS `alcool`,`t2`.`toxicos` AS `toxicos`,`t2`.`hrSono` AS `hrSono`,`t2`.`alimentacao` AS `alimentacao`,`t2`.`peso` AS `peso`,`t2`.`altura` AS `altura`,`t2`.`PA` AS `PA`,`t2`.`pulso` AS `pulso`,`t2`.`mucosas` AS `mucosas`,`t2`.`garganta` AS `garganta`,`t2`.`tegumentos` AS `tegumentos`,`t2`.`ganglios` AS `ganglios`,`t2`.`dentes` AS `dentes`,`t2`.`semPerEsq` AS `semPerEsq`,`t2`.`semPerDir` AS `semPerDir`,`t2`.`semLonEsq` AS `semLonEsq`,`t2`.`semLonDir` AS `semLonDir`,`t2`.`comPerEsq` AS `comPerEsq`,`t2`.`comPerDir` AS `comPerDir`,`t2`.`comLonEsq` AS `comLonEsq`,`t2`.`comLonDir` AS `comLonDir`,`t2`.`cores` AS `cores`,`t2`.`ouvidos` AS `ouvidos`,`t2`.`apResp` AS `apResp`,`t2`.`coracao` AS `coracao`,`t2`.`varizes` AS `varizes`,`t2`.`circCol` AS `circCol`,`t2`.`abdomen` AS `abdomen`,`t2`.`cicatrizes` AS `cicatrizes`,`t2`.`aneis` AS `aneis`,`t2`.`outrosAchados` AS `outrosAchados`,`t2`.`apGen` AS `apGen`,`t2`.`apOst` AS `apOst`,`t2`.`sisNerv` AS `sisNerv`,`t2`.`roemberg` AS `roemberg`,`t2`.`psiquismo` AS `psiquismo`,`t2`.`sistEndo` AS `sistEndo`,`t2`.`antivari` AS `antivari`,`t2`.`antite` AS `antite`,`t2`.`outrasVacinas` AS `outrasVacinas`,`t2`.`apto` AS `apto` from (`vabainicial` `t1` join `exame` `t2` on(`t1`.`idPaciente` = `t2`.`id_Paciente`)) ;

--
-- VIEW  `pesquisaanamnese`
-- Data: Nenhum
--

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
