package com2002.team021;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class Appointment {
	
	private Date date;
	private Date startTime;
	private Date endTime;
	private Patient patient;
	private Practitioner practitioner;
	private ArrayList<Treatment> treatments;
	
	public Appointment (Date date, Date startTime, Date endTime, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.patient = patient;
		this.practitioner = practitioner;
		this.treatments = treatments;
		
	}
	
	public Appointment (long date, long startTime, long endTime, Patient patient, Practitioner practitioner, ArrayList<Treatment> treatments) {
		this(new Date(date), new Date(startTime), new Date(endTime), patient, practitioner, treatments);
		
	}
	
	public Appointment (Date date, Date startTime, Practitioner practitioner) throws SQLException {
		try {
			Appointment dbAppointment = new Query().getAppointment(date.getTime(), startTime.getTime(), practitioner.getRole());
			
			this.date = dbAppointment.getDate();
			this.startTime = dbAppointment.getStartTime();
			this.endTime = dbAppointment.getEndTime();
			this.patient = dbAppointment.getPatient();
			this.practitioner = dbAppointment.getPractitioner();
			this.treatments = dbAppointment.getTreatments();
			
		} catch (SQLException e) {
			throw new SQLException("Couldnt find treatment: " + date.getTime() + " " + startTime.getTime() + " " + practitioner.getRole() + "\n" + e);
		}
		
	}
	
	public Date getDate () { return this.date; }
	public Date getStartTime () { return this.startTime; }
	public Date getEndTime () { return this.endTime; }
	public Patient getPatient () { return this.patient; }
	public Practitioner getPractitioner () { return this.practitioner; }
	
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
	
	public void setTreatments (ArrayList<Treatment> treatments) { this.treatments = treatments; }
	
	public String toString () {
		String str = "";
		str += this.startTime + " - ";
		str += this.endTime + " - ";
		str += this.patient + " - ";
		str += this.practitioner + " - ";
		str += this.treatments;
		
		return str;
		
	}
	
	public static void main (String args[]) {
		
		try {
			System.out.println(
				new Appointment(new Date(0), new Date(0), new Practitioner("Dentist")).getTreatments()
			);
		} catch (Exception e){
			System.out.println(e);
		} 
		
	}
	
}
