-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: stusql.dcs.shef.ac.uk
-- Generation Time: Dec 13, 2015 at 01:38 AM
-- Server version: 5.5.44-MariaDB
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `team021`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `houseNumber` varchar(200) NOT NULL,
  `streetName` varchar(200) NOT NULL,
  `district` varchar(200) NOT NULL,
  `city` varchar(200) NOT NULL,
  `postcode` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`houseNumber`, `streetName`, `district`, `city`, `postcode`) VALUES
('0', 'house road', 'cow close', 'sheffield', 's104sd'),
('13', 'elm close', 'kidsgrove', 'stoke-on-trent', 'st74hr'),
('14', 'elm close', 'kidsgrove', 'stoke-on-trent', 'st74hr'),
('16', 'aif', 'onoince', 'oin', 'iosnf');

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `start` bigint(20) NOT NULL,
  `end` bigint(20) NOT NULL,
  `patient` int(11) NOT NULL,
  `practitioner` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`start`, `end`, `patient`, `practitioner`) VALUES
(1449763208559, 1449766748559, 1, 'Dentist'),
(1449765621000, 1449766761000, 1, 'Dentist'),
(1449824402309, 1449825542309, 9, 'Hygienist');

-- --------------------------------------------------------

--
-- Table structure for table `healthcarePlans`
--

CREATE TABLE `healthcarePlans` (
  `name` varchar(200) NOT NULL,
  `cost` int(11) NOT NULL,
  `checkUps` int(11) NOT NULL,
  `hygieneVisits` int(11) NOT NULL,
  `repairs` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `healthcarePlans`
--

INSERT INTO `healthcarePlans` (`name`, `cost`, `checkUps`, `hygieneVisits`, `repairs`) VALUES
('Dental Repair Plan', 3600, 2, 2, 2),
('Maintenance Plan', 1500, 2, 2, 0),
('NHS Free Plan', 0, 2, 2, 6),
('Oral Health Plan', 2100, 2, 4, 0);

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `forename` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `dob` bigint(20) NOT NULL,
  `phone` int(11) NOT NULL,
  `houseNumber` varchar(200) NOT NULL,
  `postcode` varchar(8) NOT NULL,
  `subscription` varchar(200) DEFAULT NULL,
  `checkupsTaken` int(11) DEFAULT NULL,
  `hygeineVisitsTaken` int(11) DEFAULT NULL,
  `repairsTaken` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`id`, `forename`, `surname`, `dob`, `phone`, `houseNumber`, `postcode`, `subscription`, `checkupsTaken`, `hygeineVisitsTaken`, `repairsTaken`) VALUES
(1, 'rob', 'ede', 1448974665, 6982634, '14', 'st74hr', NULL, NULL, NULL, NULL),
(7, 'Rob', 'Ede', 790387740000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(8, 'Rob', 'Ede', 790387740000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(9, 'Rob', 'Ede', 790387740000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(10, 'Rob', 'Ede', 790387740000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(11, 'Rob', 'Ede', 790387740000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(12, 'Rob', 'Ede', 790387740000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(13, 'a', 'c', 1449878400000, 1, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(14, 'a', 'c', 1449878400000, 1, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(15, 'c', 'b', 1449878400000, 1, '14', 'st74hr', 'Dental Repair Plan', NULL, NULL, NULL),
(16, 'test', 'patient', 1420070400000, 3, '14', 'st74hr', 'NHS Free Plan', NULL, NULL, NULL),
(17, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(18, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(19, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'Dental Repair Plan', NULL, NULL, NULL),
(20, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(21, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'NHS Free Plan', NULL, NULL, NULL),
(22, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(23, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', NULL, NULL, NULL, NULL),
(24, 'Mr', 'Knobede', 1330819200000, 5365893, '14', 'st74hr', NULL, NULL, NULL, NULL),
(25, 'Mr', 'Knobede', 1330819200000, 5365893, '14', 'st74hr', NULL, NULL, NULL, NULL),
(26, 'Mr', 'Knobede', 1330819200000, 5365893, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(27, 'Mr', 'Knobede', 1330819200000, 5365893, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(28, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'NHS Free Plan', NULL, NULL, NULL),
(29, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'Oral Health Plan', NULL, NULL, NULL),
(30, 'Rob', 'Ede', 790387200000, 554342, '14', 'st74hr', 'Dental Repair Plan', NULL, NULL, NULL),
(31, 'Bob', 'Jones', -430794000000, 8340235, '14', 'st74hr', 'Maintenance Plan', NULL, NULL, NULL),
(32, 'Moritz', 'ede', 1378800000, 6982634, '14', 'st74hr', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `practitioners`
--

CREATE TABLE `practitioners` (
  `name` varchar(200) NOT NULL,
  `role` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `practitioners`
--

INSERT INTO `practitioners` (`name`, `role`) VALUES
('Phil Green', 'Dentist'),
('John Cena', 'Hygienist');

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `start` bigint(20) NOT NULL,
  `practitioner` varchar(200) NOT NULL,
  `treatmentName` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `treatments`
--

CREATE TABLE `treatments` (
  `name` varchar(200) NOT NULL,
  `cost` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `treatments`
--

INSERT INTO `treatments` (`name`, `cost`) VALUES
('Checkup', 40),
('Cleaning', 70);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`houseNumber`,`postcode`);

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`start`,`practitioner`),
  ADD KEY `appointmentPractitioner` (`practitioner`),
  ADD KEY `appointmentPatient` (`patient`) USING BTREE;

--
-- Indexes for table `healthcarePlans`
--
ALTER TABLE `healthcarePlans`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `patientAddress` (`houseNumber`,`postcode`),
  ADD KEY `subscription` (`subscription`);

--
-- Indexes for table `practitioners`
--
ALTER TABLE `practitioners`
  ADD PRIMARY KEY (`role`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`start`,`practitioner`,`treatmentName`),
  ADD KEY `practitioner` (`practitioner`),
  ADD KEY `start` (`start`),
  ADD KEY `treatmentName` (`treatmentName`);

--
-- Indexes for table `treatments`
--
ALTER TABLE `treatments`
  ADD PRIMARY KEY (`name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `appointmentPatient` FOREIGN KEY (`patient`) REFERENCES `patients` (`id`),
  ADD CONSTRAINT `appointmentPractitioner` FOREIGN KEY (`practitioner`) REFERENCES `practitioners` (`role`);

--
-- Constraints for table `patients`
--
ALTER TABLE `patients`
  ADD CONSTRAINT `patientAddress` FOREIGN KEY (`houseNumber`,`postcode`) REFERENCES `addresses` (`houseNumber`, `postcode`),
  ADD CONSTRAINT `patientSubscription` FOREIGN KEY (`subscription`) REFERENCES `healthcarePlans` (`name`);

--
-- Constraints for table `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `sessionAppointment` FOREIGN KEY (`start`,`practitioner`) REFERENCES `appointments` (`start`, `practitioner`),
  ADD CONSTRAINT `sessionTreatment` FOREIGN KEY (`treatmentName`) REFERENCES `treatments` (`name`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
