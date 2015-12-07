package com2002.team021;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class Appointment {

	private Date startTime;
	private Date endTime;
	private Patient patient;
	private Practitioner practitioner;
	private ArrayList<Treatment> treatments;

	public Appointment (Date date, Date startTime, Date endTime, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.patient = patient;
		this.practitioner = practitioner;
		this.treatments = treatments;

	}

	public Appointment (long date, long startTime, long endTime, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments) {
		this(new Date(0), new Date(startTime*1000), new Date(endTime), patient, practitioner, treatments);

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

	public ArrayList<Treatment> getTreatments() {
		return this.treatments;
	}

	public String toString () {
		String str = "";
		str += this.startTime + " - ";
		str += this.endTime + " - ";
		str += this.patient + " - ";
		str += this.practitioner + " - ";
		str += this.treatments;

		return str;

	}

	public static void main (String args[]) {

		/* try {
			System.out.println(new Appointment());
		} catch (Exception e){

		} */

	}

}
