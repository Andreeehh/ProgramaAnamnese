-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 01-Jul-2021 às 20:02
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
-- Estrutura da tabela `paciente`
--

CREATE TABLE `paciente` (
  `idPaciente` int(12) NOT NULL,
  `Nome` varchar(80) NOT NULL,
  `dataNasc` varchar(15) NOT NULL,
  `Sexo` varchar(1) NOT NULL,
  `Cor` varchar(15) DEFAULT NULL,
  `estadoCivil` varchar(15) NOT NULL,
  `RG` varchar(11) NOT NULL,
  `Endereco` varchar(150) NOT NULL,
  `Cidade` varchar(15) NOT NULL,
  `Cargo` varchar(20) NOT NULL,
  `pai` varchar(100) DEFAULT NULL,
  `mae` varchar(100) DEFAULT NULL,
  `naturalidade` varchar(25) NOT NULL,
  `id_empresa` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `paciente`
--

INSERT INTO `paciente` (`idPaciente`, `Nome`, `dataNasc`, `Sexo`, `Cor`, `estadoCivil`, `RG`, `Endereco`, `Cidade`, `Cargo`, `pai`, `mae`, `naturalidade`, `id_empresa`) VALUES
(1, 'André', '13/08/1995', 'M', 'Branco', 'Solteiro', '484455588', '9 de julho 992', 'Iacanga', 'Estagiário', 'Beto', 'Lu', 'Brasileiro', 1),
(3, 'Roberto', '23/10/1961', 'M', 'Branco', 'Solteiro', '1111', '9 de julho 992', 'Iacanga', 'Médico', 'Haroldo', 'Sylvia', 'Brasileiro', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `paciente`
--
ALTER TABLE `paciente`
  ADD PRIMARY KEY (`idPaciente`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `paciente`
--
ALTER TABLE `paciente`
  MODIFY `idPaciente` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `paciente`
--
ALTER TABLE `paciente`
  ADD CONSTRAINT `paciente_ibfk_1` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`idEmpresa`),
  ADD CONSTRAINT `paciente_ibfk_2` FOREIGN KEY (`id_empresa`) REFERENCES `empresa` (`idEmpresa`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
