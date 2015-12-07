package com2002.team021;

import static com2002.team021.config.SQL.*;

import java.util.ArrayList;
import java.sql.*;

public class Query {

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public Query () {
		try {
			Class.forName(DB_DRIVER);
			 con = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASS);

		} catch (Exception e) {
			System.out.println(e);

		}// trycat

	}// constructor

	public Patient getPatient (int patientID) throws SQLException {
		String query = "SELECT * FROM patients WHERE id = ? LIMIT 1;";

		try {
			stmt = con.prepareStatement(query);
			stmt.setInt(1, patientID);
			rs = stmt.executeQuery();
			rs.first();

			return new Patient(
				rs.getInt("id"),
				rs.getString("forename"),
				rs.getString("surname"),
				rs.getInt("dob"),
				rs.getInt("phone"),
				rs.getString("houseNumber"),
				rs.getString("postcode"),
				rs.getString("subscription")
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}// getPatient()

	public Address getPatientAddress (int patientID) throws SQLException {
		String query = "SELECT * FROM addresses WHERE (houseNumber, postcode) IN (SELECT houseNumber, postcode FROM patients WHERE id = ?) LIMIT 1;";

		try {
			stmt = con.prepareStatement(query);
			stmt.setInt(1, patientID);
			rs = stmt.executeQuery();
			rs.first();

			return new Address(
				rs.getString("houseNumber"),
				rs.getString("streetName"),
				rs.getString("district"),
				rs.getString("city"),
				rs.getString("postcode")
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}// getPatientAddress()

	public Address getAddress (String houseNumber, String postcode) throws SQLException {
		String query = "SELECT * FROM addresses WHERE houseNumber = ? AND postcode = ? LIMIT 1";

		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, houseNumber);
			stmt.setString(2, postcode);
			rs = stmt.executeQuery();
			rs.first();

			return new Address(
				rs.getString("houseNumber"),
				rs.getString("streetName"),
				rs.getString("district"),
				rs.getString("city"),
				rs.getString("postcode")
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}// getAddress()

	public Appointment getAppointment(long date, long startTime, String practitioner) throws SQLException {
		String query = "SELECT * FROM appointments WHERE date = ? AND startTime = ? AND practitioner = ? LIMIT 1";

		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, date);
			stmt.setLong(2, startTime);
			stmt.setString(3, practitioner);
			rs = stmt.executeQuery();
			rs.first();

			return new Appointment(
				rs.getLong("date"),
				rs.getLong("startTime"),
				rs.getLong("endTime"),
				new Patient(rs.getInt("patient")),
				new Practitioner(rs.getString("practitioner")),
				new ArrayList<Treatment>()
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}// getAppointment()

	public Practitioner getPractitioner(String practitionerRole) throws SQLException {
		String query = "SELECT * FROM practitioners WHERE role = ? LIMIT 1;";

		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, practitionerRole);
			rs = stmt.executeQuery();
			rs.first();

			return new Practitioner(
				rs.getString("name"),
				rs.getString("role")
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}// getPractitioner()

	public Treatment getTreatment(String treatmentName) throws SQLException {
		String query = "SELECT * FROM treatments WHERE name = ? LIMIT 1;";

		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, treatmentName);
			rs = stmt.executeQuery();
			rs.first();

			return new Treatment(
				rs.getString("name"),
				rs.getInt("cost")
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}

	public HealthcarePlan getHealthcarePlan (String planName) throws SQLException {
		String query = "SELECT * FROM healthcarePlans WHERE name = ? LIMIT 1;";

		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, planName);
			rs = stmt.executeQuery();
			rs.first();

			return new HealthcarePlan(
				rs.getString("name"),
				rs.getInt("cost"),
				rs.getInt("checkUps"),
				rs.getInt("hygieneVisits"),
				rs.getInt("repairs")
			);

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

	}

	public ArrayList<HealthcarePlan> getHealthcarePlans () throws SQLException {
		String query = "SELECT * FROM healthcarePlans ORDER BY cost ASC;";
		ArrayList<HealthcarePlan> plans = new ArrayList<HealthcarePlan>();

		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				plans.add(new HealthcarePlan(
					rs.getString("name"),
					rs.getInt("cost"),
					rs.getInt("checkUps"),
					rs.getInt("hygieneVisits"),
					rs.getInt("repairs")
				));

			}

		} catch (SQLException e) {
			throw new SQLException(e);

		}// trycat

		return plans;

	}

	public static void main (String args[]) {

		try {
			System.out.println(new Query().getPatient(1));

		} catch (Exception e) {
			System.out.println(e);

		}

	}

}
