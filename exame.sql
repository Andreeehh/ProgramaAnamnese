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
-- Estrutura da tabela `exame`
--

CREATE TABLE `exame` (
  `idAnamnese` int(11) NOT NULL,
  `id_Paciente` int(11) NOT NULL,
  `tipoExame` varchar(40) NOT NULL,
  `data` varchar(10) NOT NULL,
  `apAcidente` varchar(100) DEFAULT NULL,
  `apCiclo` varchar(100) DEFAULT NULL,
  `apHep` varchar(100) DEFAULT NULL,
  `apMaleita` varchar(100) DEFAULT NULL,
  `apMenarca` varchar(100) DEFAULT NULL,
  `apNefrit` varchar(100) DEFAULT NULL,
  `apOpera` varchar(100) DEFAULT NULL,
  `apOutras` varchar(100) DEFAULT NULL,
  `apParasita` varchar(100) DEFAULT NULL,
  `apPartos` varchar(100) DEFAULT NULL,
  `apPleuriz` varchar(100) DEFAULT NULL,
  `apReum` varchar(100) DEFAULT NULL,
  `apTbc` varchar(100) DEFAULT NULL,
  `apVenereas` varchar(100) DEFAULT NULL,
  `ap` varchar(100) DEFAULT NULL,
  `apHpma` varchar(100) DEFAULT NULL,
  `fumo` varchar(50) DEFAULT NULL,
  `alcool` varchar(50) DEFAULT NULL,
  `toxicos` varchar(50) DEFAULT NULL,
  `hrSono` varchar(50) DEFAULT NULL,
  `alimentacao` varchar(50) DEFAULT NULL,
  `peso` varchar(10) DEFAULT NULL,
  `altura` varchar(10) DEFAULT NULL,
  `PA` varchar(50) DEFAULT NULL,
  `pulso` varchar(10) DEFAULT NULL,
  `mucosas` varchar(50) DEFAULT NULL,
  `garganta` varchar(50) DEFAULT NULL,
  `tegumentos` varchar(50) DEFAULT NULL,
  `ganglios` varchar(50) DEFAULT NULL,
  `dentes` varchar(50) DEFAULT NULL,
  `olhos` varchar(50) DEFAULT NULL,
  `semPerEsq` varchar(10) DEFAULT NULL,
  `semPerDir` varchar(10) DEFAULT NULL,
  `semLonEsq` varchar(10) DEFAULT NULL,
  `semLonDir` varchar(10) DEFAULT NULL,
  `comPerEsq` varchar(10) DEFAULT NULL,
  `comPerDir` varchar(10) DEFAULT NULL,
  `comLonEsq` varchar(10) DEFAULT NULL,
  `comLonDir` varchar(10) DEFAULT NULL,
  `cores` varchar(50) DEFAULT NULL,
  `ouvidos` varchar(50) DEFAULT NULL,
  `apResp` varchar(50) DEFAULT NULL,
  `coracao` varchar(50) DEFAULT NULL,
  `varizes` varchar(50) DEFAULT NULL,
  `circCol` varchar(50) DEFAULT NULL,
  `abdomen` varchar(50) DEFAULT NULL,
  `cicatrizes` varchar(50) DEFAULT NULL,
  `aneis` varchar(50) DEFAULT NULL,
  `outrosAchados` varchar(50) DEFAULT NULL,
  `apGen` varchar(50) DEFAULT NULL,
  `apOst` varchar(50) DEFAULT NULL,
  `sisNerv` varchar(50) DEFAULT NULL,
  `roemberg` varchar(50) DEFAULT NULL,
  `psiquismo` varchar(50) DEFAULT NULL,
  `sistEndo` varchar(50) DEFAULT NULL,
  `antivari` varchar(50) DEFAULT NULL,
  `antite` varchar(50) DEFAULT NULL,
  `outrasVacinas` varchar(50) DEFAULT NULL,
  `apto` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `exame`
--

INSERT INTO `exame` (`idAnamnese`, `id_Paciente`, `tipoExame`, `data`, `apAcidente`, `apCiclo`, `apHep`, `apMaleita`, `apMenarca`, `apNefrit`, `apOpera`, `apOutras`, `apParasita`, `apPartos`, `apPleuriz`, `apReum`, `apTbc`, `apVenereas`, `ap`, `apHpma`, `fumo`, `alcool`, `toxicos`, `hrSono`, `alimentacao`, `peso`, `altura`, `PA`, `pulso`, `mucosas`, `garganta`, `tegumentos`, `ganglios`, `dentes`, `olhos`, `semPerEsq`, `semPerDir`, `semLonEsq`, `semLonDir`, `comPerEsq`, `comPerDir`, `comLonEsq`, `comLonDir`, `cores`, `ouvidos`, `apResp`, `coracao`, `varizes`, `circCol`, `abdomen`, `cicatrizes`, `aneis`, `outrosAchados`, `apGen`, `apOst`, `sisNerv`, `roemberg`, `psiquismo`, `sistEndo`, `antivari`, `antite`, `outrasVacinas`, `apto`) VALUES
(1, 1, 'de Mudança de cargo/função', '28/06/2021', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'Não', 'Não', 'Não', '8h', 'Normal', '81kg', '1,81', 'teste', '100', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', '0', '0', '0', '0', '0', '0', '0', '0', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'teste', 'APTO'),
(2, 3, 'Periódico', '28/06/2021', 'Não', '', 'Não', 'Não', '', 'Não', 'Não', '', 'Não', '', 'Não', 'Não', 'Não', 'Não', '', '', 'Não', 'Não', 'Não', '', 'Normal', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'APTO'),
(3, 3, 'Demissional', '29/06/2021', 'Não', '', 'Não', 'Não', '', 'Não', 'Não', '', 'Não', '', 'Não', 'Não', 'Não', 'Não', 'nada', 'nada', 'Não', 'Não', 'Não', '5h', 'Normal', '100kg', '1.60', 'nada', '60', '', 'nada', 'nada', 'nada', 'nada', 'nada', '2', '2', '2', '2', '2', '2', '2', '2', 'nada', 'nada', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'APTO'),
(4, 1, 'Periódico', '29/06/2021', 'Não', '', 'Não', 'Não', '', 'Não', 'Não', '', 'Não', '', 'Não', 'Não', 'Não', 'Não', '', '', 'Não', 'Não', 'Não', '6h', 'Normal', '1.81', '81', 'n', '100', 'n', 'n', 'n', 'n', 'n', 'n', '0', '0', '0', '0', '0', '0', '0', '0', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'APTO');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `exame`
--
ALTER TABLE `exame`
  ADD PRIMARY KEY (`idAnamnese`),
  ADD KEY `id_Paciente` (`id_Paciente`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `exame`
--
ALTER TABLE `exame`
  MODIFY `idAnamnese` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `exame`
--
ALTER TABLE `exame`
  ADD CONSTRAINT `exame_ibfk_1` FOREIGN KEY (`id_Paciente`) REFERENCES `paciente` (`idPaciente`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
