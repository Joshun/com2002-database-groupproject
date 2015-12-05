package com2002.team021;

import java.sql.*;

public class Patient {

	private int id;
	private String forename;
	private String surname;
	private int dob;
	private int phone;
	private Address address;
	private HealthcarePlan subscription;

	public Patient (int patientID) throws SQLException {
		try {
			Patient dbPatient = new Query().getPatient(patientID);

			this.id = dbPatient.getId();
			this.forename = dbPatient.getForename();
			this.surname = dbPatient.getSurname();
			this.dob = dbPatient.getDob();
			this.phone = dbPatient.getPhone();
			this.address = dbPatient.getAddress();
			this.subscription = dbPatient.getSubscription();

		} catch (SQLException e) {
			throw new SQLException(e);

		}

	}

	public Patient (int id, String forename, String surname, int dob, int phone, String houseNumber, String postcode, String subscription) throws SQLException {
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.dob = dob;
		this.phone = phone;
		
		try {
			this.address = new Address(houseNumber, postcode);
			if (subscription != null) this.subscription = new HealthcarePlan(subscription);
			
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}
	
	// getters
	public int getId () { return this.id; }
	public String getForename () { return this.forename; }
	public String getSurname () { return this.surname; }
	public int getDob () { return this.dob; }
	public int getPhone () { return this.phone; }
	public String getHouseNumber () { return this.address.getHouseNumber(); }
	public String getPostcode () { return this.address.getPostcode(); }
	public HealthcarePlan getSubscription () { return this.subscription; }

	public Address getAddress () throws SQLException {
		try {
			return new Query().getPatientAddress(this.id);
			
		} catch (SQLException e) {
			throw new SQLException(e);
			
		}
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
			System.out.println("Couldn't find patient");
			e.printStackTrace();

		}
	}

}
