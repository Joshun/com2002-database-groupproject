package com2002.team021;

import java.sql.SQLException;

public class Patient {
	
	private boolean inDB = false;
	private int id;
	private String forename;
	private String surname;
	private long dob;
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
			this.inDB = true;
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get patient: " + patientID + "\n" + e);
			
		}
		
	}

	public Patient (String forename, String surname, long dob, int phone, String houseNumber, String postcode, String subscription) throws SQLException {
		this.forename = forename;
		this.surname = surname;
		this.dob = dob;
		this.phone = phone;
		this.inDB = false;
		
		try {
			this.address = new Address(houseNumber, postcode);
			
		} catch (SQLException e) {
			throw new SQLException("Address not in database." + houseNumber + "" + postcode, e);
		}
		
		try {
			if (subscription != null) {
				this.subscription = new HealthcarePlan(subscription);
			} else {
				this.subscription = null;
			}
			
		} catch (SQLException e) {
			throw new SQLException("Subscription not in database: " + subscription, e);
		}
		
	}
	
	public Patient (int id, String forename, String surname, long dob, int phone, String houseNumber, String postcode, String subscription) throws SQLException {
		this.id = id;
		this.forename = forename;
		this.surname = surname;
		this.dob = dob;
		this.phone = phone;
		this.inDB = true;
		
		try {
			this.address = new Address(houseNumber, postcode);
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get address: " + houseNumber + ", " + postcode + "\n" + e);
		}
		
		try {
			if (subscription != null) this.subscription = new HealthcarePlan(subscription);
			
		} catch (SQLException e) {
			throw new SQLException("Subscription not in database: " + subscription, e);
		}
	}
	
	// getters
	public int getId () { return this.id; }
	public String getForename () { return this.forename; }
	public String getSurname () { return this.surname; }
	public long getDob () { return this.dob; }
	public int getPhone () { return this.phone; }
	public HealthcarePlan getSubscription () { return this.subscription; }
	public boolean getInDB () { return this.inDB; }
	
	public String getHouseNumber () { return this.address.getHouseNumber(); }
	public String getPostcode () { return this.address.getPostcode(); }
	
	public String getName () {
		return this.getForename() + " " + this.getSurname();
	}
	
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
