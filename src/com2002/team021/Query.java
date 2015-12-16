package com2002.team021;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static com2002.team021.config.SQL.*;

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
		String query = "UPDATE patients SET title = ?, forename = ?, surname = ?, dob = ?, phone = ?, houseNumber = ?, postcode = ?, subscription = ?, checkupsTaken = ?, hygeineVisitsTaken = ?, repairsTaken = ? WHERE id = ?;";
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
			stmt.setInt(9, patient.getCheckUps());
			stmt.setInt(10, patient.getHygieneVisits());
			stmt.setInt(11, patient.getRepairs());
			stmt.setInt(12, patient.getId());
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
	
	public boolean updateAppointment (Appointment appointment) throws SQLException, AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException {
		System.out.println("updateAppointment(Appointment appointment) method may produce unexpected results");
		System.out.println("updateAppointment(Appointment appointment) please make sure appointment start time ahs not changed");
		try {
			return new Query().updateAppointment(appointment, appointment);
		} catch (SQLException e) {
			throw new SQLException("could not update appointment (using updateAppointment(appointment)) \n" + e);
		} catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
			throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException(e);
		}
		
	}
	
	
	public boolean isAppointmentCollision (Appointment appointment) throws SQLException, AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException {
		ArrayList<Appointment> all;
		Appointment a = appointment;
		
		try {
			all = new Query().getAppointmentsByPractitioner(appointment.getPractitioner());
		} catch (SQLException e) {
			throw new SQLException("Couldnt get all appointment to check collisions with\n" + e);
		}
		
		// check duplicate starts
		for (Appointment t : all) {
			System.out.println(t);
			if (t.getStart().getTime() == a.getStart().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Duplicate start");
			}
		}
		
		// check duplicate ends
		for (Appointment t : all) {
			if (t.getEnd().getTime() == a.getEnd().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Duplicate end");
			}
		}
		
		// check start not within another appointment
		for (Appointment t : all) {
			if (a.getStart().getTime() < t.getEnd().getTime() && a.getStart().getTime() > t.getStart().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Start is within another appointment");
			}
		}
		int i = 0;
		// check end not within another appointment
		for (Appointment t : all) {
			System.out.println(i++);
			if (a.getEnd().getTime() < t.getEnd().getTime() && a.getEnd().getTime() > t.getStart().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("End is within another appointment");
			}
		}
		i = 0;
		// check entire overlaps
		for (Appointment t : all) {
			System.out.println(i++);
			System.out.println(a.getStart());
			System.out.println(t.getStart());
			System.out.println(t.getEnd());
			System.out.println(a.getEnd());
			System.out.println("");
			if (a.getStart().getTime() < t.getStart().getTime() && t.getEnd().getTime() < a.getEnd().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Appointment entirely overlaps another");
			}
		}
		
		return false;
	}
	
	public boolean isAppointmentCollision (Appointment appointment, Appointment minus) throws SQLException, AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException {
		ArrayList<Appointment> all;
		Appointment a = appointment;
		
		try {
			all = new Query().getAppointmentsByPractitioner(appointment.getPractitioner(), minus);
		} catch (SQLException e) {
			throw new SQLException("Couldnt get all appointment to check collisions with\n" + e);
		}
		
		// check duplicate starts
		for (Appointment t : all) {
			System.out.println(t);
			if (t.getStart().getTime() == a.getStart().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Duplicate start");
			}
		}
		
		// check duplicate ends
		for (Appointment t : all) {
			if (t.getEnd().getTime() == a.getEnd().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Duplicate end");
			}
		}
		
		// check start not within another appointment
		for (Appointment t : all) {
			if (a.getStart().getTime() < t.getEnd().getTime() && a.getStart().getTime() > t.getStart().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Start is within another appointment");
			}
		}
		int i = 0;
		// check end not within another appointment
		for (Appointment t : all) {
			System.out.println(i++);
			if (a.getEnd().getTime() < t.getEnd().getTime() && a.getEnd().getTime() > t.getStart().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("End is within another appointment");
			}
		}
		i = 0;
		// check entire overlaps
		for (Appointment t : all) {
			System.out.println(i++);
			System.out.println(a.getStart());
			System.out.println(t.getStart());
			System.out.println(t.getEnd());
			System.out.println(a.getEnd());
			System.out.println("");
			if (a.getStart().getTime() < t.getStart().getTime() && t.getEnd().getTime() < a.getEnd().getTime()) {
				throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException("Appointment entirely overlaps another");
			}
		}
		
		return false;
	}
	
	public boolean updateAppointment (Appointment appointment, Appointment old) throws SQLException, AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException {
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
			throw new SQLException("couldnt update appointment " + appointment + "\n" + e);
			
		} catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
			throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException(e);
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
		}
		
	}
	
	private int sumTreatments(ArrayList<Treatment> ts) {
		int total = 0;
		for (Treatment t : ts) {
			total += t.getCost();
		}
		
		return total;
	}
	
	public boolean updatePatientHCP (int id, int checkUps, int hygieneVisits, int repairs) throws SQLException {
		String query = "UPDATE patients SET checkupsTaken = ?, hygeineVisitsTaken = ?, repairsTaken = ? WHERE id = ?;";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setInt(1, checkUps);
			stmt.setInt(2, hygieneVisits);
			stmt.setInt(3, repairs);
			stmt.setInt(4, id);
			success = stmt.executeUpdate();
			
			return success > 0;
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update existing Healthcare plan " + id + "\n" + e);
			
		} finally {
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
	}
	
	public int calculateCost (Appointment appointment) throws SQLException {
		Patient p = appointment.getPatient();
		HealthcarePlan hcp = p.getHealthcarePlan();
		ArrayList<Treatment> treatments = null;
		int cost = 0;
		
		System.out.println(cost);
		try {
			treatments = new Query().getAppointmentTreatments(appointment);
		} catch (SQLException e) {
			throw new SQLException("couldnt find treatments to calculate cost:\n" + e);
		}
		System.out.println(treatments);
		System.out.println(cost);
		
		if (p.getHealthcarePlan() == null) {
			System.out.println("patient does not have a hcp");
			cost = sumTreatments(treatments);
			System.out.println(cost);
			
		} else {
			System.out.println(cost);
			for (Treatment t : treatments) {
				System.out.println(t.getCoveredBy());
				System.out.println(cost);
				if (t.getCoveredBy().equals("checkUps")) {
					if (!p.incrementCheckUps()) {
						System.out.println("patient has used all checkUps");
						cost += t.getCost();
					}
					System.out.println(cost);
					
				} else if (t.getCoveredBy().equals("hygieneVisits")) {
					if (!p.incrementHygieneVisits()) {
						System.out.println("patient has used all hygieneVisits");
						cost += t.getCost();
					}
					System.out.println(cost);
					
				} else if (t.getCoveredBy().equals("repairs")) {
					if (!p.incrementRepairs()) {
						System.out.println("patient has used all repairs");
						cost += t.getCost();
					}
					System.out.println(cost);
					
				} else {
					System.out.println("treatment " + t + " not covered by HCP");
					cost += t.getCost();
					
				}
				
			}
			
		}
		System.out.println("amount due " + cost);
		appointment.setAmountDue(cost);
		
		try {
			System.out.println(new Query().updateAppointment(appointment, appointment));
		} catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
			System.out.println("in calculate cost => it really should have you know...");
		}
		
		return cost;
		
	}
	
	
	public boolean updateExistingAppointment (Appointment appointment, Appointment old) throws SQLException, AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException {
		String query = "UPDATE appointments SET start = ?, end = ?, patient = ?, practitioner = ?, amountDue = ? WHERE start = ? AND practitioner = ?;";
		int success;
		boolean collision;
		
		try {
			if (!isAppointmentCollision(appointment, old)) collision = false;
		} catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
			throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException(e);
		}
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setLong(2, appointment.getEnd().getTime());
			stmt.setInt(3, appointment.getPatient().getId());
			stmt.setString(4, appointment.getPractitioner().getRole());
			stmt.setInt(5, appointment.getAmountDue());
			
			stmt.setLong(6, old.getStart().getTime());
			stmt.setString(7, old.getPractitioner().getRole());
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
	
	public boolean addAppointment (Appointment appointment) throws SQLException, AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException {
		String query = "INSERT INTO appointments VALUES (?, ?, ?, ?, ?);";
		int success;
		boolean collision;
		
		if (appointment.getStart().getTime() > appointment.getEnd().getTime()) {
			throw new SQLException("Start time should be sooner than end time");
		}
		
		try {
			if (!isAppointmentCollision(appointment)) collision = false;
		} catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
			throw new AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException(e);
		}
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getStart().getTime());
			stmt.setLong(2, appointment.getEnd().getTime());
			stmt.setInt(3, appointment.getPatient().getId());
			stmt.setString(4, appointment.getPractitioner().getRole());
			stmt.setInt(5, appointment.getAmountDue());
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
	
	public ArrayList<Appointment> getAppointmentsByPractitioner (Practitioner prac) throws SQLException {
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		ArrayList<Appointment> all;
		
		try {
			all = new Query().getAppointments();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get all appointments\n" + e);
		}
		
		for (Appointment a : all) {
			if (a.getPractitioner().getRole().equals(prac.getRole())) {
				filtered.add(a);
			}
		}
		
		return filtered;
		
	}
	
	public ArrayList<Appointment> getAppointmentsByPractitioner (Practitioner prac, Appointment minus) throws SQLException {
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		ArrayList<Appointment> all;
		
		try {
			all = new Query().getAppointmentsByPractitioner(prac);
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get all appointments\n" + e);
		}
		
		for (Appointment a : all) {
			if (a.getStart().getTime() != minus.getStart().getTime() &&
				a.getPractitioner().equals(minus.getPractitioner())) {
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
		
		query = "INSERT INTO patients (title, forename, surname, dob, phone, houseNumber, postcode, subscription)  VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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
					rs.getString("subscription"),
					rs.getInt("checkupsTaken"),
					rs.getInt("hygeineVisitsTaken"),
					rs.getInt("repairsTaken")
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
	
	public ArrayList<Patient> getPatientsByName (String fn, String sn) throws SQLException {
		String query = "SELECT * FROM patients WHERE forename LIKE ? AND surname LIKE ? ORDER BY CASE WHEN forename = ? AND surname = ? THEN 0 WHEN forename LIKE ? AND surname = ? THEN 1 WHEN forename LIKE ? AND surname LIKE ? THEN 2 WHEN forename LIKE ? AND surname LIKE ? THEN 3 ELSE 4 END, id DESC;";
		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + fn + "%");
			stmt.setString(2, "%" + sn + "%");
			stmt.setString(3, fn);
			stmt.setString(4, sn);
			stmt.setString(5, fn + "%");
			stmt.setString(6, sn);
			stmt.setString(7, fn + "%");
			stmt.setString(8, sn + "%");
			stmt.setString(9, "%" + fn + "%");
			stmt.setString(10, "%" + sn + "%");
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
					rs.getString("subscription"),
					rs.getInt("checkupsTaken"),
					rs.getInt("hygeineVisitsTaken"),
					rs.getInt("repairsTaken")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt search patients\n" + e);
			
		} finally {
			try { if (rs != null && !rs.isClosed()) rs.close(); } catch (SQLException e) { throw new SQLException("Couldnt close result set"); };
			try { if (!stmt.isClosed()) stmt.close(); } catch (SQLException e) { throw new SQLException("Couldnt close statement"); };
			try { if (!con.isClosed()) con.close(); } catch (SQLException e) { throw new SQLException("Couldnt close connection"); };
		}
		
		return patients;
		
	}
	
	public ArrayList<Patient> getPatientsByName (String name) throws SQLException {
		String fn = null;
		String sn = null;
		
		Pattern p = Pattern.compile("(.*)\\s(\\w+)$");
		Matcher m = p.matcher(name);
		if (m.find()) {
			fn = m.group(1);
			sn = m.group(2);
			return getPatientsByName(fn, sn);
		}
		
		String query = "SELECT * FROM patients WHERE surname LIKE ? UNION SELECT *	 FROM patients WHERE forename LIKE ? ORDER BY CASE WHEN surname = ? THEN 0 WHEN surname LIKE ? THEN 1 WHEN surname LIKE ? THEN 2 WHEN forename = ? THEN 3 WHEN forename LIKE ? THEN 4 WHEN forename LIKE ? THEN 5 ELSE 7 END, id DESC;";
		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + name + "%");
			stmt.setString(2, "%" + name + "%");
			stmt.setString(3, name);
			stmt.setString(4, name + "%");
			stmt.setString(5, "%" + name + "%");
			stmt.setString(6, name);
			stmt.setString(7, name + "%");
			stmt.setString(8, "%" + name + "%");
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
					rs.getString("subscription"),
					rs.getInt("checkupsTaken"),
					rs.getInt("hygeineVisitsTaken"),
					rs.getInt("repairsTaken")
				));
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt search patients\n" + e);
			
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
	
	public ArrayList<Address> getAddressByPostcode(String postcode) throws SQLException {
		String query = "SELECT * FROM addresses WHERE postcode = ? ORDER BY houseNumber ASC;";
		ArrayList<Address> addresses = new ArrayList<Address>();
		postcode = postcode.toLowerCase().replace(" ", "");
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, postcode);
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
				rs.getInt("cost"),
				rs.getString("coveredBy")
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
					rs.getInt("cost"),
					rs.getString("coveredBy")
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
		String query = "SELECT * FROM appointments NATURAL JOIN (sessions JOIN treatments ON (treatments.name = sessions.treatmentName)) WHERE start = ? AND practitioner = ?;";
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
			Treatment tr2 = new Treatment("White Composite Resin Filling");
			ArrayList<Treatment> trs = new ArrayList<Treatment>();
			trs.add(tr1);
			trs.add(tr2);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");
			Patient rob = new Patient("Mr", "Rob", "Ede", sdf.parse("18/9/1995"), 554342, "14", "st74hr", null);
			
			// HealthcarePlan hcp = new HealthcarePlan("New One", 456, 2, 3, 4);
			
			Practitioner prac = new Practitioner("Dentist");
			Date date = new Date(1449763208559L);
			
			Address add = new Address("13", "elm close", "kidsgrove", "stoke-on-trent", "st74hr");
			
			Appointment ap = new Query().getAppointments().get(0);
			// ap.setTreatments(trs);
			// System.out.println(new Query().updateAppointment(ap, ap));
			
			// ap.setStart(ap.getStart().getTime() - 1000);
			// ap.setEnd(ap.getEnd().getTime() + 1000);
			
			System.out.println(
				// new Query().updateHealthcarePlan(hcp)
				// new Query().getPractitioners()
				// new Query().addPatient(pa)
				// new Query().getTreatments()
				// new Query().getPractitioners()
				// new Query().getPractitionerAppointmentsOnDay(date, prac)
				// new Query().getPatientsByName("rob")
				// new Query().calculateCost(ap)
				// new Query().getAddressByPostcode("st74hr")
				// new Query().addAppointment(ap)
			);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
		
	}
	
}
