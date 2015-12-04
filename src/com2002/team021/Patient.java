package com2002.team021;

import java.sql.*;

public class Patient {
	
	private int id;
	private String forename;
	private String surname;
	private int dob;
	private int phone;
	private String houseNumber;
	private String postcode;
	private String subscription;
	
	public Patient (int patientID) throws SQLException {
		
		try {
			Patient dbPatient = new Query().getPatient(patientID);
			
			this.id = dbPatient.getId();
			this.forename = dbPatient.getForename();
			this.surname = dbPatient.getSurname();
			this.dob = dbPatient.getDob();
			this.phone = dbPatient.getPhone();
			this.houseNumber = dbPatient.getHouseNumber();
			this.postcode = dbPatient.getPostcode();
			this.subscription = dbPatient.getSubscription();
			
		} catch (SQLException e) {
			throw new SQLException(e);
			
		}
		
	}
	
	public Patient (int id, String forename, String surname, int dob, int phone, String houseNumber, String postcode, String subscription) {
		
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.dob = dob;
		this.phone = phone;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.subscription = subscription;
		
	}
	
	public int getId () { return this.id; }
	public String getForename () { return this.forename; }
	public String getSurname () { return this.surname; }
	public int getDob () { return this.dob; }
	public int getPhone () { return this.phone; }
	public String getHouseNumber () { return this.houseNumber; }
	public String getPostcode () { return this.postcode; }
	public String getSubscription () { return this.subscription; }
	
	public Address getAddress () {
		return new Query().getPatientAddress(this.id);
		
	}
	
	public String toString () {
		return this.id + ": " + this.forename + " " + this.surname;
	}
	
	public static void main (String args[]) {
		
		try {
			Patient patient = new Patient(1);
			
			System.out.println(patient);
			System.out.println(patient.getAddress());
			
		} catch (SQLException e) {
			System.out.println("Couldn't find patient." + e);
			
		}
	}
	
}
