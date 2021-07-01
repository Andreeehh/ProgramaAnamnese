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
-- Estrutura da tabela `aso`
--

CREATE TABLE `aso` (
  `idAso` int(11) NOT NULL,
  `id_Paciente` int(11) NOT NULL,
  `id_Empresa` int(11) NOT NULL,
  `tipoExame` varchar(50) NOT NULL,
  `fisCalor` int(11) DEFAULT NULL,
  `fisFrio` int(11) DEFAULT NULL,
  `fisRuido` int(11) DEFAULT NULL,
  `fisUmid` int(11) DEFAULT NULL,
  `fisRad` int(11) DEFAULT NULL,
  `fisOutros` int(11) DEFAULT NULL,
  `quiGases` int(11) DEFAULT NULL,
  `quiPoeiras` int(11) DEFAULT NULL,
  `quiTintas` int(11) DEFAULT NULL,
  `quiSolventes` int(11) DEFAULT NULL,
  `quiProd` int(11) DEFAULT NULL,
  `quiOutros` int(11) DEFAULT NULL,
  `bioMicro` int(11) DEFAULT NULL,
  `bioOutros` int(11) DEFAULT NULL,
  `ergoMovPeso` int(11) DEFAULT NULL,
  `ergoPost` int(11) DEFAULT NULL,
  `ergoEsf` int(11) DEFAULT NULL,
  `ergoTrabSentado` int(11) DEFAULT NULL,
  `ergoTrabPe` int(11) DEFAULT NULL,
  `ergoMovRep` int(11) DEFAULT NULL,
  `ergoOutros` int(11) DEFAULT NULL,
  `ausenciaRisc` int(11) DEFAULT NULL,
  `exCli` date DEFAULT NULL,
  `exAud` date DEFAULT NULL,
  `exEspi` date DEFAULT NULL,
  `exECG` date DEFAULT NULL,
  `exHemo` date DEFAULT NULL,
  `exChagas` date DEFAULT NULL,
  `exGlice` date DEFAULT NULL,
  `exRX` date DEFAULT NULL,
  `exAcui` date DEFAULT NULL,
  `exCopropar` date DEFAULT NULL,
  `exCoprocult` date DEFAULT NULL,
  `exVDRL` date DEFAULT NULL,
  `exOutras1` date DEFAULT NULL,
  `exOutras2` date DEFAULT NULL,
  `exOutras3` date DEFAULT NULL,
  `exOutras1Texto` varchar(20) DEFAULT NULL,
  `exOutras2Texto` varchar(20) DEFAULT NULL,
  `exOutras3Texto` varchar(20) DEFAULT NULL,
  `conclusao` varchar(50) NOT NULL,
  `restricoes` varchar(50) DEFAULT NULL,
  `data` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `aso`
--

INSERT INTO `aso` (`idAso`, `id_Paciente`, `id_Empresa`, `tipoExame`, `fisCalor`, `fisFrio`, `fisRuido`, `fisUmid`, `fisRad`, `fisOutros`, `quiGases`, `quiPoeiras`, `quiTintas`, `quiSolventes`, `quiProd`, `quiOutros`, `bioMicro`, `bioOutros`, `ergoMovPeso`, `ergoPost`, `ergoEsf`, `ergoTrabSentado`, `ergoTrabPe`, `ergoMovRep`, `ergoOutros`, `ausenciaRisc`, `exCli`, `exAud`, `exEspi`, `exECG`, `exHemo`, `exChagas`, `exGlice`, `exRX`, `exAcui`, `exCopropar`, `exCoprocult`, `exVDRL`, `exOutras1`, `exOutras2`, `exOutras3`, `exOutras1Texto`, `exOutras2Texto`, `exOutras3Texto`, `conclusao`, `restricoes`, `data`) VALUES
(1, 1, 1, 'Admissional', 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, '0010-08-20', '0006-03-16', '0006-03-17', '0006-03-20', '0006-03-19', '0006-03-22', '0006-03-24', '0006-03-27', '0006-03-28', '0006-04-06', '0006-04-05', '0006-03-18', '0006-03-21', '0006-03-23', '0006-03-26', 'teste1', 'teste2', 'teste3', 'Apto', '', '2021-07-01'),
(2, 1, 1, 'Parto', 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, '0006-06-13', '1991-01-01', '1993-01-01', '1996-01-01', '1995-01-01', '1993-01-01', '1911-01-01', '1992-01-01', NULL, '1991-01-01', '1992-01-01', '1993-01-01', '1993-01-01', '1995-01-01', '1994-01-01', '01011911', '01011994', '01011991', 'Apto', '', '2021-06-01'),
(3, 1, 1, 'Admissional', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '0006-06-16', '1994-01-01', '1996-01-01', '1992-01-01', '1991-01-01', '1995-01-01', '1997-01-01', '1993-01-01', '1991-01-01', '1993-01-01', '1995-01-01', '1997-01-01', '1994-01-01', '1996-01-01', '1992-01-01', '', '', '', 'Apto', '', '2021-05-01');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `aso`
--
ALTER TABLE `aso`
  ADD PRIMARY KEY (`idAso`),
  ADD KEY `id_Paciente` (`id_Paciente`),
  ADD KEY `id_Empresa` (`id_Empresa`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `aso`
--
ALTER TABLE `aso`
  MODIFY `idAso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `aso`
--
ALTER TABLE `aso`
  ADD CONSTRAINT `aso_ibfk_1` FOREIGN KEY (`id_Paciente`) REFERENCES `paciente` (`idPaciente`),
  ADD CONSTRAINT `aso_ibfk_2` FOREIGN KEY (`id_Empresa`) REFERENCES `empresa` (`idEmpresa`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
