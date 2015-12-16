package com2002.team021;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class Appointment {
	
	private Date start;
	private Date end;
	private Patient patient;
	private Practitioner practitioner;
	private ArrayList<Treatment> treatments;
	private int amountDue;
	
	public Appointment (Date start, Date end, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments, int amountDue) {
		this.start = start;
		this.end = end;
		this.patient = patient;
		this.practitioner = practitioner;
		this.treatments = treatments;
		this.amountDue = amountDue;
		
	}
	
	public Appointment (long start, long end, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments, int amountDue) {
		this(new Date(start), new Date(end), patient, practitioner, treatments, amountDue);
		
	}
	
	public Appointment (Date start, Date end, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments) {
		this(start, end, patient, practitioner, treatments, 0);
		
	}
	
	public Appointment (long start, long end, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments) {
		this(new Date(start), new Date(end), patient, practitioner, treatments, 0);
		
	}
	
	public Appointment (Date start, Practitioner practitioner) throws SQLException {
		try {
			Appointment dbAppointment = new Query().getAppointment(start.getTime(), practitioner.getRole());
			
			this.start = dbAppointment.getStart();
			this.end = dbAppointment.getEnd();
			this.patient = dbAppointment.getPatient();
			this.practitioner = dbAppointment.getPractitioner();
			this.treatments = dbAppointment.getTreatments();
			this.amountDue = dbAppointment.getAmountDue();
			
		} catch (SQLException e) {
			throw new SQLException("Couldnt find treatment: " + start.getTime() + " " + practitioner.getRole() + "\n" + e);
		}
		
	}
	
	public Date getStart () { return this.start; }
	public Date getEnd () { return this.end; }
	public Patient getPatient () { return this.patient; }
	public Practitioner getPractitioner () { return this.practitioner; }
	public int getAmountDue () { return this.amountDue; }
	
	public void setTreatments (ArrayList<Treatment> treatments) { this.treatments = treatments; }
	
	public ArrayList<Treatment> getTreatments() throws SQLException {
		if (this.treatments == null) {
			try {
				this.treatments = new Query().getAppointmentTreatments(this);
				return this.treatments;
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException("Could not find treatments for this appointment", e);
			}
		}
		
		return this.treatments;
		
	}
	
	public void setStart(Date start) { this.start = start; }
	public void setEnd(Date end) { this.end = end; }
	public void setStart(long start) { this.start = new Date(start); }
	public void setEnd(long end) { this.end = new Date(end); }
	public void setAmountDue(int amountDue) { this.amountDue = amountDue; }
	
	public String toString () {
		String str = "";
		str += this.start + " - ";
		str += this.practitioner;
		
		return str;
		
	}
	
	public static void main (String args[]) {
		
		try {
			System.out.println(
				new Appointment(new Date(0), new Practitioner("Dentist")).getTreatments()
			);
		} catch (Exception e){
			System.out.println(e);
		} 
		
	}
	
}
