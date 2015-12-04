package com2002.team021;

import static com2002.team021.config.SQL.*;

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
	
	public Address getPatientAddress (int patientID) {
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
			
		} catch (Exception e) {
			System.out.println(e);
			
		}// trycat
		
		return null;
		
	}// getPatientAddress
	
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
	
	public static void main (String args[]) {
		
		try {
			System.out.println(new Query().getPatient(1));
			
		} catch (Exception e) {
			System.out.println(e);
			
		}
		
	}
	
}
