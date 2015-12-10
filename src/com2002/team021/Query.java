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
	
	public boolean updateAppointment (Appointment appointment, Appointment old) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM appointments WHERE date = ? AND startTime = ? AND practitioner = ?";
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getDate().getTime());
			stmt.setLong(2, appointment.getStartTime().getTime());
			stmt.setString(3, appointment.getPractitioner().getRole());
			rs = stmt.executeQuery();
			
			rs.first();
			
			if (rs.getInt("count") > 0) {
				System.out.println("update");
				updateExistingAppointment(appointment, old);
				return true;
				
			} else {
				System.out.println("add");
				addAppointment(appointment);
				return true;
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update appointment " + appointment + "\n" + e);
			
		}// trycat
		
	}
	
	
	public boolean updateExistingAppointment (Appointment appointment, Appointment old) throws SQLException {
		String query = "UPDATE appointments SET date = ?, startTime = ?, endTime = ?, patient = ?, practitioner = ? WHERE date = ? AND startTime = ? AND practitioner = ?;";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getDate().getTime());
			stmt.setLong(2, appointment.getStartTime().getTime());
			stmt.setLong(3, appointment.getEndTime().getTime());
			stmt.setInt(4, appointment.getPatient().getId());
			stmt.setString(5, appointment.getPractitioner().getRole());
			
			stmt.setLong(6, old.getDate().getTime());
			stmt.setLong(7, old.getStartTime().getTime());
			stmt.setString(8, old.getPractitioner().getRole());
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException("couldnt update existing appointment " + appointment + "\n" + e);
			
		}
		
		return success > 0;
		
	}
	
	public boolean addAppointment (Appointment appointment) throws SQLException {
		String query = "INSERT INTO appointments VALUES (?, ?, ?, ?, ?);";
		boolean success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getDate().getTime());
			stmt.setLong(2, appointment.getStartTime().getTime());
			stmt.setLong(3, appointment.getEndTime().getTime());
			stmt.setInt(4, appointment.getPatient().getId());
			stmt.setString(5, appointment.getPractitioner().getRole());
			success = stmt.execute();
			
			
		} catch (SQLException e) {
			throw new SQLException("couldnt add appointment" + appointment + "\n" + e);
			
		}// trycat
		
		return success;
		
	}
	
	public ArrayList<Appointment> getAppointmentsOnDay (Date day) throws SQLException {
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		
		try {
			for (Appointment a : new Query().getAppointments()) {
				if (a.getDate().equals(day)) filtered.add(a);
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get appointments on day" + day + "\n" + e);
		}// trycat
		
		return filtered;
		
	}
	
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
			throw new SQLException("Couldn't find patient: " + patientID + "\n" + e);
			
		}// trycat
		
	}// getPatient()
	
	public ArrayList<Patient> getPatients () throws SQLException {
		String query = "SELECT * FROM patients ORDER BY id DESC;";
		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				patients.add(new Patient(
					rs.getInt("id"),
					rs.getString("forename"),
					rs.getString("surname"),
					rs.getLong("dob"),
					rs.getInt("phone"),
					rs.getString("houseNumber"),
					rs.getString("postcode"),
					rs.getString("subscription")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get patients\n" + e);
			
		}// trycat
		
		return patients;
		
	}
	
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
			throw new SQLException("Couldn't find patient address: " + patientID + "\n" + e);
			
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
			throw new SQLException("Couldn't find address: " + houseNumber + " " + postcode + "\n" + e);
			
		}// trycat
		
	}// getAddress()
	
	public ArrayList<Address> getAddresses() throws SQLException {
		String query = "SELECT * FROM addresses ORDER BY postcode ASC;";
		ArrayList<Address> addresses = new ArrayList<Address>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				addresses.add(new Address(
					rs.getString("houseNumber"),
					rs.getString("streetName"),
					rs.getString("district"),
					rs.getString("city"),
					rs.getString("postcode")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("Couldn't find addresses: \n" + e);
			
		}// trycat
		return addresses;
		
	}// getAddresses()
	
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
				null
			);
			
		} catch (SQLException e) {
			throw new SQLException("Couldn't find appointment in database: " + date + " " + startTime + " " + practitioner, e);
			
		}// trycat
		
	}// getAppointment()
	
	public ArrayList<Appointment> getAppointments() throws SQLException {
		String query = "SELECT * FROM appointments ORDER BY date ASC, startTime ASC;";
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				appointments.add(new Appointment(
				rs.getLong("date"),
				rs.getLong("startTime"),
				rs.getLong("endTime"),
				new Patient(rs.getInt("patient")),
				new Practitioner(rs.getString("practitioner")),
				new ArrayList<Treatment>()
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("Couldn't find appointments: \n" + e);
			
		}// trycat
		
		return appointments;
		
	}// getAppointments
	
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
			throw new SQLException("couldnt find practitioner", e);
			
		}// trycat
		
	}// getPractitioner()
	
	public ArrayList<Practitioner> getPractitioners() throws SQLException {
		String query = "SELECT * FROM practitioners ORDER BY name ASC;";
		ArrayList<Practitioner> practitioners = new ArrayList<Practitioner>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				practitioners.add(new Practitioner(
					rs.getString("name"),
					rs.getString("role")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get practitioners", e);
			
		}// trycat
		
		return practitioners;
		
	}//getAppointments
	
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
			throw new SQLException("couldnt find treatment: " + treatmentName, e);
			
		}// trycat
		
	} // getTreatment
	
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
			throw new SQLException("Couldn't find HealthcarePlan: " + planName + "\n" + e);
			
		}// trycat
		
	}// get HealthcarePlan
	
	public ArrayList<HealthcarePlan> getHealthcarePlans() throws SQLException {
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
			throw new SQLException("Couldn't find healthcarePlans: \n" + e);
			
		}// trycat
		
		return plans;
		
	}// getHleathCarePlans
	
	public ArrayList<Treatment> getTreatments() throws SQLException {
		String query = "SELECT * FROM treatments ORDER BY name ASC;";
		ArrayList<Treatment> treatments = new ArrayList<Treatment>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				treatments.add(new Treatment(
					rs.getString("name"),
					rs.getInt("cost")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get treatments", e);
			
		}// trycat
		
		return treatments;
		
	}// getTreatments
	
	public ArrayList<Treatment> getAppointmentTreatments (Appointment appointment) throws SQLException {
		String query = "SELECT * FROM sessions WHERE date = ? AND startTime = ? AND practitioner = ?";
		ArrayList<Treatment> treatments = new ArrayList<Treatment>();
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getDate().getTime());
			stmt.setLong(2, appointment.getStartTime().getTime());
			stmt.setString(3, appointment.getPractitioner().getRole());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				treatments.add(new Treatment(
					rs.getString("treatmentName")
				));
				
			}
			
		} catch (SQLException e) {
			System.out.println(appointment);
			throw new SQLException("Could not find treatments for appointment: " + "\n" + e);
			
		}// trycat
		
		return treatments;
		
	}// getAppointmentTreatments
	
	public static void main (String args[]) {
		
		try {
			Patient pa = new Patient(1);
			Practitioner pr = new Practitioner("Dentist");
			
			Appointment a = new Appointment(new Date(0), new Date(250), new Date(350), pa, pr, null);
			
			System.out.println(new Query().updateAppointment(a, new Query().getAppointments().get(0)));
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
}
