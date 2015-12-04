package com2002.team021;

import java.util.Date;
import java.sql.*;

public class Appointment {

	private Date startTime;
	private Date endTime;
	private Patient patient;
	private Practitioner practitioner;
	private Treatment treatment;

	public Appointment () throws SQLException {
		int startTime = 1448974665;
		int endTime = 1448975665;
		Date start = new Date((long)startTime*1000);
		Date end = new Date((long)endTime);

		try {
			Patient patient = new Patient(1);
			Practitioner practitioner = new Practitioner("Dentist");
			Treatment treatment = new Treatment("CheckUp");
		} catch (SQLException e) {
			throw new SQLException(e);
		}

		this.startTime = start;
		this.endTime = end;
		this.patient = patient;
		this.practitioner = practitioner;
		this.treatment = treatment;

	}

	public Appointment (Date startTime, Date endTime, Patient patient, Practitioner practitioner, Treatment treatment) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.patient = patient;
		this.practitioner = practitioner;
		this.treatment = treatment;

	}

	public Appointment (int startTime, int endTime, Patient patient, Practitioner practitioner, Treatment treatment) {
		this(new Date((long)startTime*1000), new Date((long)endTime), patient, practitioner, treatment);

	}

	// public Date getDate () {
		// return this.date;
	// }

	public Date getStartTime () {
		return this.startTime;
	}

	public Date getEndTime () {
		return this.endTime;
	}

	public Patient getPatient () {
		return this.patient;
	}

	public Practitioner getPractitioner () {
		return this.practitioner;
	}

	public Treatment getTreatment () {
		return this.treatment;
	}

	public String toString () {
		String str = "";
		str += this.startTime + " - ";
		str += this.endTime + " - ";
		str += this.patient + " - ";
		str += this.practitioner + " - ";
		str += this.treatment;

		return str;

	}

	public static void main (String args[]) {

		try {
			System.out.println(new Appointment());
		} catch (Exception e){

		}

	}

}
