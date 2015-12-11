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
			
		}
		
	}// constructor
	
	public boolean updatePatient (Patient patient) throws SQLException {
		
		return false;
		
	}
	
	public boolean updateAppointment (Appointment appointment, Appointment old) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM appointments WHERE date = ? AND startTime = ? AND practitioner = ?";
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, old.getDate().getTime());
			stmt.setLong(2, old.getStartTime().getTime());
			stmt.setString(3, old.getPractitioner().getRole());
			rs = stmt.executeQuery();
			
			rs.first();
			this.con.close();
			
			if (rs.getInt("count") > 0) {
				return updateExistingAppointment(appointment, old);
				
			} else {
				return addAppointment(appointment);
				
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt update appointment " + appointment + "\n" + e);
			
		}
		
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
			throw new SQLException("couldnt update existing appointment " + appointment + "\n" + e);
			
		}
		
		try {
			updateSessionTreatments(appointment);
		} catch (SQLException e) {
			throw new SQLException("couldnt existing appointment session\n" + e);
		}
		
		return success > 0;
		
	}
	
	public boolean addAppointment (Appointment appointment) throws SQLException {
		String query = "INSERT INTO appointments VALUES (?, ?, ?, ?, ?);";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getDate().getTime());
			stmt.setLong(2, appointment.getStartTime().getTime());
			stmt.setLong(3, appointment.getEndTime().getTime());
			stmt.setInt(4, appointment.getPatient().getId());
			stmt.setString(5, appointment.getPractitioner().getRole());
			success = stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt add appointment" + appointment + "\n" + e);
			
		}
		
		try {
			updateSessionTreatments(appointment);
		} catch (SQLException e) {
			throw new SQLException("Couldnt add appointment session\n" + e);
		}
		
		return success > 0;
		
	}
	
	public boolean updateSessionTreatments (Appointment appointment) throws SQLException {
		
		try {
			String query = "DELETE FROM sessions WHERE date = ? AND startTime = ? AND practitioner = ?;";
			
			stmt = con.prepareStatement(query);
			stmt.setLong(1, appointment.getDate().getTime());
			stmt.setLong(2, appointment.getStartTime().getTime());
			stmt.setString(3, appointment.getPractitioner().getRole());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("couldnt delete existing treatments from " + appointment + "\n" + e);
			
		}
		System.out.println(appointment);
		for (Treatment t : appointment.getTreatments()) {
			String query = "INSERT INTO sessions VALUES (?, ?, ?, ?);";
			try {
				stmt = con.prepareStatement(query);
				stmt.setLong(1, appointment.getDate().getTime());
				stmt.setLong(2, appointment.getStartTime().getTime());
				stmt.setString(3, appointment.getPractitioner().getRole());
				stmt.setString(4, t.getName());
				stmt.executeUpdate();
				
			} catch (SQLException e) {
				throw new SQLException("couldnt insert treatment " + t + "\n" + e);
			}
			
		}
		
		return true;
	}
	
	public ArrayList<Appointment> getAppointmentsOnDay (Date day) throws SQLException {
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		
		try {
			for (Appointment a : new Query().getAppointments()) {
				if (a.getDate().equals(day)) filtered.add(a);
			}
			
		} catch (SQLException e) {
			throw new SQLException("couldnt get appointments on day" + day + "\n" + e);
		}
		
		return filtered;
		
	}
	
	public boolean addPatient(Patient patient) throws SQLException {
		String query = "INSERT INTO patients (forename, surname, dob, phone, houseNumber, postcode, subscription)  VALUES (?, ?, ?, ?, ?, ?, ?);";
		int success;
		
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, patient.getForename());
			stmt.setString(2, patient.getSurname());
			stmt.setLong(3, patient.getDob());
			stmt.setInt(4, patient.getPhone());
			stmt.setString(5, patient.getHouseNumber());
			stmt.setString(6, patient.getPostcode());
			if (patient.getSubscription() == null)
				stmt.setNull(7, java.sql.Types.VARCHAR);
				else {
					stmt.setString(7, patient.getSubscription().getName());
				}
				
			success = stmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
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
			
		}
		
		return treatments;
		
	}// getAppointmentTreatments
	
	public static void main (String args[]) {
		
		try {
			Patient pa = new Patient(1);
			Practitioner pr = new Practitioner("Dentist");
			Treatment tr1 = new Treatment("Cleaning");
			Treatment tr2 = new Treatment("Checkup");
			ArrayList<Treatment> trs = new ArrayList<Treatment>();
			trs.add(tr1);
			trs.add(tr2);
			Appointment a = new Appointment(new Date(0), new Date(250), new Date(350), pa, pr, trs);
			
			System.out.println(new Query().updateAppointment(a, a));
			
			Patient rob = new Patient("Rob", "Ede", 213, 74961309, "14", "st74hr", null);
			
			new Query().addPatient(rob);
			// System.out.println(new Query().upda());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
}
