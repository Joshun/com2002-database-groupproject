-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: stusql.dcs.shef.ac.uk
-- Generation Time: Dec 03, 2015 at 03:32 PM
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
CREATE DATABASE IF NOT EXISTS `team021`;
USE `team021`;

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `houseNumber` int(5) NOT NULL,
  `streetName` varchar(200) NOT NULL,
  `district` varchar(200) NOT NULL,
  `city` varchar(200) NOT NULL,
  `postcode` varchar(8) NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `date` int(12) NOT NULL,
  `startTime` int(12) NOT NULL,
  `endTime` int(12) NOT NULL,
  `patient` int(11) NOT NULL,
  `practitioner` varchar(200) NOT NULL,
  `treatment` varchar(200) NOT NULL
);

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
);

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `forename` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `dob` int(12) NOT NULL,
  `phone` int(13) NOT NULL,
  `houseNumber` int(5) NOT NULL,
  `postcode` varchar(8) NOT NULL,
  `subscription` varchar(200) DEFAULT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `practitioners`
--

CREATE TABLE `practitioners` (
  `name` int(50) NOT NULL,
  `role` varchar(200) NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `treatments`
--

CREATE TABLE `treatments` (
  `name` varchar(200) NOT NULL,
  `cost` int(11) NOT NULL
);

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
  ADD PRIMARY KEY (`date`,`startTime`),
  ADD KEY `appointmentPractitioner` (`practitioner`),
  ADD KEY `appointmentTreatment` (`treatment`),
  ADD KEY `appointmentPatient` (`patient`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `appointmentTreatment` FOREIGN KEY (`treatment`) REFERENCES `treatments` (`name`),
  ADD CONSTRAINT `appointmentPatient` FOREIGN KEY (`patient`) REFERENCES `patients` (`id`),
  ADD CONSTRAINT `appointmentPractitioner` FOREIGN KEY (`practitioner`) REFERENCES `practitioners` (`role`);

--
-- Constraints for table `patients`
--
ALTER TABLE `patients`
  ADD CONSTRAINT `patientSubscription` FOREIGN KEY (`subscription`) REFERENCES `healthcarePlans` (`name`),
  ADD CONSTRAINT `patientAddress` FOREIGN KEY (`houseNumber`,`postcode`) REFERENCES `addresses` (`houseNumber`, `postcode`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
