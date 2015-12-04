package com2002.team021;
import java.sql.*;

public class Practitioner {

	private String name;
	private String role;

	public Practitioner (String practitionerRole) throws SQLException {

		try {
			Practitioner dbPractitioner = new Query().getPractitioner(practitionerRole);

			this.role = dbPractitioner.getRole();
			this.name = dbPractitioner.getName();

		} catch (SQLException e) {
			throw new SQLException(e);

		}

	}

	public Practitioner (String name, String role) {

		this.name = name;
		this.role = role;

	}

	public String getName() {
		return this.name;
	}

	public String getRole() {
		return this.role;
	}

	public String toString () {
		return this.role + " " + this.name;
	}

	public static void main (String args[]) {
		try {
			System.out.println (new Practitioner("Dr. Knobede", "Dentist"));
		} catch (Exception e) {

		}
	}

}
