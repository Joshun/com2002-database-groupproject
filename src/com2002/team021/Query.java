package com2002.team021;

import static com2002.team021.config.SQL.*;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class Query {
	
	private Connection con = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	public Query () {
		try {
			Class.forName(DB_DRIVER);
			con = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASS);
			
		} catch (Exception e) {
			System.out.println(e);
			
		}
		
	}
	
	public boolean updatePatient (Patient patient) throws SQLException {
		if (patient.getId() == 0) {
			return addPatient(patient);
		} else {
			return updateExistingPatient(patient);
		}
		
	}
	
	public boolean updateExistingPatient (Patient patient) throws SQLException {
		String query = "UPDATE patients SET forename = ?, surname = ?, dob = ?, phone = ?, phone = ?, houseNumber = ?, postcode = ?, WHERE;";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, patient.getForename());
			stmt.setString(2, patient.getSurname());
			stmt.setLong(3, patient.getDob().getTime());
			stmt.setInt(4, patient.getPhone());
			stmt.setString(5, patient.getHouseNumber());
			stmt.setString(6, patient.getPostcode());
			success = stmt.executeUpdate();
			
			return success > 0;
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update existing Healthcare plan " + patient + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
	}
	
	public boolean addAddress (Address address) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM addresses WHERE houseNumber = ? AND postcode = ?;";
		
		String postcode = address.getPostcode().toLowerCase().replace(" ", "");
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, address.getHouseNumber());
			stmt.setString(2, postcode);
			rs = stmt.executeQuery();
			
			rs.first();
			
			if (rs.getInt("count") > 0) {
				System.out.println("address already exists");
				return false;
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt find address " + address + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
		query = "INSERT INTO addresses VALUES (?, ?, ?, ?, ?);";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, address.getHouseNumber());
			stmt.setString(2, address.getStreetName());
			stmt.setString(3, address.getDistrict());
			stmt.setString(4, address.getCity());
			stmt.setString(5, address.getPostcode());
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt add address " + address + "\n" + e);
			
		} finally {
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
		return success > 0;
		
	}
	
	public boolean updateHealthcarePlan (HealthcarePlan hcp) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM healthcarePlans WHERE name = ?;";
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, hcp.getName());
			rs = stmt.executeQuery();
			
			rs.first();
			
			if (rs.getInt("count") > 0) {
				return updateExistingHealthcarePlan(hcp);
			} else {
				return addHealthcarePlan(hcp);
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update appointment " + hcp + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
	}
	
	public boolean addHealthcarePlan (HealthcarePlan hcp) throws SQLException {
		String query = "INSERT INTO healthcarePlans VALUES (?, ?, ?, ?, ?);";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, hcp.getName());
			stmt.setInt(2, hcp.getCost());
			stmt.setInt(3, hcp.getCheckUps());
			stmt.setInt(4, hcp.getHygieneVisits());
			stmt.setInt(5, hcp.getRepairs());
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt add HealthcarePlan" + hcp + "\n" + e);
			
		} finally {
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
		return success > 0;
	}
	
	public boolean updateExistingHealthcarePlan (HealthcarePlan hcp) throws SQLException {
		String query = "UPDATE healthcarePlans SET cost = ?, checkUps = ?, hygieneVisits = ?, repairs = ? WHERE name = ?;";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setInt(1, hcp.getCost());
			stmt.setInt(2, hcp.getCheckUps());
			stmt.setInt(3, hcp.getHygieneVisits());
			stmt.setInt(4, hcp.getRepairs());
			stmt.setString(5, hcp.getName());
			success = stmt.executeUpdate();
			
			return success > 0;
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update existing Healthcare plan " + hcp + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
	public boolean updateAppointment (Appointment appointment, Appointment old) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM appointments WHERE start = ? AND practitioner = ?;";
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, old.getStart().getTime());
			stmt.setString(2, old.getPractitioner().getRole());
			rs = stmt.executeQuery();
			
			rs.first();
			
			if (rs.getInt("count") > 0) {
				return updateExistingAppointment(appointment, old);
			} else {
				return addAppointment(appointment);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("couldnt update appointment " + appointment + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
	}
	
	
	public boolean updateExistingAppointment (Appointment appointment, Appointment old) throws SQLException {
		String query = "UPDATE appointments SET start = ?, end = ?, patient = ?, practitioner = ? WHERE start = ? AND practitioner = ?;";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setLong(2, appointment.getEnd().getTime());
			stmt.setInt(3, appointment.getPatient().getId());
			stmt.setString(4, appointment.getPractitioner().getRole());
			
			stmt.setLong(5, old.getStart().getTime());
			stmt.setString(6, old.getPractitioner().getRole());
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update existing appointment " + appointment + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
		try {
			updateSessionTreatments(appointment);
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update existing appointment session\n" + e);
			
		} finally {
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return success > 0;
		
	}
	
	public boolean addAppointment (Appointment appointment) throws SQLException {
		String query = "INSERT INTO appointments VALUES (?, ?, ?, ?);";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setLong(2, appointment.getEnd().getTime());
			stmt.setInt(3, appointment.getPatient().getId());
			stmt.setString(4, appointment.getPractitioner().getRole());
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt add appointment" + appointment + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
		try {
			updateSessionTreatments(appointment);
			
		} catch (SQLException e) {
			throw new SQLException("Couldnt add appointment session\n" + e);
		} finally {
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return success > 0;
		
	}
	
	public boolean updateSessionTreatments (Appointment appointment) throws SQLException {
		
		try {
			String query = "DELETE FROM sessions WHERE start = ? AND practitioner = ?;";
			
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setString(2, appointment.getPractitioner().getRole());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt delete existing treatments from " + appointment + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		System.out.println("successfully deleted all treatments for appointment");
		
		try {
		
			for (Treatment t : appointment.getTreatments()) {
				String query = "INSERT INTO sessions VALUES (?, ?, ?);";
				stmt = con.prepareStatement(query);
				stmt.setLong(1, appointment.getStart().getTime());
				stmt.setString(2, appointment.getPractitioner().getRole());
				stmt.setString(3, t.getName());
				stmt.executeUpdate();
				
			}
			
		
		} catch (SQLException e) {
			throw new SQLException("Could insert one of the treatments\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return true;
	}
	
	public boolean deleteAppointment (Appointment appointment) throws SQLException {
		
		try {
			String query = "DELETE FROM sessions WHERE start = ? AND practitioner = ?;";
			
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setString(2, appointment.getPractitioner().getRole());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt delete existing treatments from " + appointment + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		System.out.println("successfully deleted all treatments for appointment");
		
		try {
			String query = "DELETE FROM appointments WHERE start = ? AND practitioner = ?;";
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setString(2, appointment.getPractitioner().getRole());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("Could insert one of the treatments\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return true;
	}
	
	public ArrayList<Appointment> getAppointmentsOnDay (Date day) throws SQLException {
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		ArrayList<Appointment> all;
		
		try {
			all = new Query().getAppointments();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get all appointments\n" + e);
			
		}
		
		for (Appointment a : all) {
			if (a.getStart().getDate() == day.getDate() &&
				a.getStart().getMonth() == day.getMonth() &&
				a.getStart().getYear() == day.getYear()
			) {
				filtered.add(a);
			}
		}
		
		
		return filtered;
		
	}
	
	public ArrayList<Appointment> getPractitionerAppointmentsOnDay (Date d, Practitioner p) throws SQLException {
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		ArrayList<Appointment> all;
		
		try {
			all = new Query().getAppointmentsOnDay(d);
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get appointments on d" + d + "\n" + e);
			
		}
		
		for (Appointment a : all) {
			if (a.getPractitioner().getRole().equals(p.getRole())) filtered.add(a);
		}
		
		return filtered;
		
	}
	
	public boolean addPatient(Patient patient) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM patients WHERE title = ? AND forename = ? AND surname = ? AND dob = ? AND phone = ? AND houseNumber = ? AND postcode = ?;";
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, patient.getTitle());
			stmt.setString(2, patient.getForename());
			stmt.setString(3, patient.getSurname());
			stmt.setLong(4, patient.getDob().getTime());
			stmt.setInt(5, patient.getPhone());
			stmt.setString(6, patient.getHouseNumber());
			stmt.setString(7, patient.getPostcode());
			ResultSet rs = stmt.executeQuery();
			rs.first();
			
			if (rs.getInt("count") > 0) {
				System.out.println("patient already exists");
				return false;
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt count patients: " + "\n" + e);
		
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
		query = "INSERT INTO patients (title, forename, surname, dob, phone, houseNumber, postcode, subscription)  VALUES (?, ?, ?, ?, ?, ?, ?);";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, patient.getTitle());
			stmt.setString(2, patient.getForename());
			stmt.setString(3, patient.getSurname());
			stmt.setLong(4, patient.getDob().getTime());
			stmt.setInt(5, patient.getPhone());
			stmt.setString(6, patient.getHouseNumber());
			stmt.setString(7, patient.getPostcode());
			if (patient.getSubscription() == null) {
				stmt.setNull(8, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(8, patient.getSubscription().getName());
			}
				
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt insert patient " + patient + "\n" + e);
		
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return success > 0;
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
				rs.getString("title"),
				rs.getString("forename"),
				rs.getString("surname"),
				new Date(rs.getLong("dob")),
				rs.getInt("phone"),
				rs.getString("houseNumber"),
				rs.getString("postcode"),
				rs.getString("subscription"),
				rs.getInt("checkupsTaken"),
				rs.getInt("hygeineVisitsTaken"),
				rs.getInt("repairsTaken")
			);
			
		} catch (SQLException e) {
			throw new SQLException("Couldn't find patient: " + patientID + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
	public ArrayList<Patient> getPatients () throws SQLException {
		String query = "SELECT * FROM patients ORDER BY id DESC;";
		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				patients.add(new Patient(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getString("forename"),
					rs.getString("surname"),
					new Date(rs.getLong("dob")),
					rs.getInt("phone"),
					rs.getString("houseNumber"),
					rs.getString("postcode"),
					rs.getString("subscription")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get patients\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return addresses;
		
	}
	
	public Appointment getAppointment(long start, String practitioner) throws SQLException {
		String query = "SELECT * FROM appointments WHERE start = ? AND practitioner = ? LIMIT 1";
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, start);
			stmt.setString(2, practitioner);
			rs = stmt.executeQuery();
			rs.first();
			
			return new Appointment(
				rs.getLong("start"),
				rs.getLong("end"),
				new Patient(rs.getInt("patient")),
				new Practitioner(rs.getString("practitioner")),
				new Query().getAppointmentTreatments(rs.getLong("start"), rs.getString("practitioner")),
				rs.getInt("amountDue")
			);
			
		} catch (SQLException e) {
			throw new SQLException("Couldn't find appointment in database: " + start + " " + practitioner, e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
	public ArrayList<Appointment> getAppointments() throws SQLException {
		String query = "SELECT * FROM appointments ORDER BY start ASC;";
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		
		try {
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				appointments.add(new Appointment(
				rs.getLong("start"),
				rs.getLong("end"),
				new Patient(rs.getInt("patient")),
				new Practitioner(rs.getString("practitioner")),
				new Query().getAppointmentTreatments(rs.getLong("start"), rs.getString("practitioner")),
				rs.getInt("amountDue")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("Couldn't find appointments: \n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return appointments;
		
	}
	
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
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
			e.printStackTrace();
			throw new SQLException("couldnt get practitioners", e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return practitioners;
		
	}
	
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
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
			throw new SQLException("Couldn't find HealthcarePlan: " + planName + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return plans;
		
	}
	
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
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return treatments;
		
	}
	
	public ArrayList<Treatment> getAppointmentTreatments (Appointment appointment) throws SQLException {
		String query = "SELECT * FROM sessions WHERE start = ? AND practitioner = ?;";
		ArrayList<Treatment> treatments = new ArrayList<Treatment>();
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setString(2, appointment.getPractitioner().getRole());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				treatments.add(new Treatment(
					rs.getString("treatmentName")
				));
				
			}
			
		} catch (SQLException e) {
			System.out.println(appointment);
			throw new SQLException("Could not find treatments for appointment: " + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return treatments;
		
	}
	
	public ArrayList<Treatment> getAppointmentTreatments (long start, String practitioner) throws SQLException {
		String query = "SELECT * FROM sessions WHERE start = ? AND practitioner = ?;";
		ArrayList<Treatment> treatments = new ArrayList<Treatment>();
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, start);
			stmt.setString(2, practitioner);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				treatments.add(new Treatment(
					rs.getString("treatmentName")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("Could not find treatments for appointment: " + new Date(start) + " - " + practitioner + "\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return treatments;
		
	}
	
	public static void main (String args[]) {
		
		try {
			Patient pa = new Patient(34);
			Practitioner pr = new Practitioner("Dentist");
			Treatment tr1 = new Treatment("Hygiene");
			Treatment tr2 = new Treatment("Checkup");
			ArrayList<Treatment> trs = new ArrayList<Treatment>();
			trs.add(tr1);
			trs.add(tr2);
			Appointment a = new Appointment(new Date(250), new Date(350), pa, pr, trs);
			
			// new Query().updateAppointment(a, a);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");
			Patient rob = new Patient("Mr", "Rob", "Ede", sdf.parse("18/9/1995"), 554342, "14", "st74hr", null);
			
			// HealthcarePlan hcp = new HealthcarePlan("New One", 456, 2, 3, 4);
			
			Practitioner prac = new Practitioner("Dentist");
			Date date = new Date(1449763208559L);
			
			Address add = new Address("13", "elm close", "kidsgrove", "stoke-on-trent", "st74hr");
			
			// System.out.println(
				// new Query().updateHealthcarePlan(hcp);
				new Query().getPractitioners();
				new Query().addPatient(pa);
				new Query().getTreatments();
				new Query().getPractitioners();
				new Query().getPractitionerAppointmentsOnDay(date, prac);
			// );
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		
	}
	
}
