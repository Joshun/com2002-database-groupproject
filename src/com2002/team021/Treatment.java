package com2002.team021;

import java.sql.*;

public class Treatment {
	
	private String name;
	private int cost;
	
	public Treatment (String treatmentName) throws SQLException {
		try {
			Treatment dbTreatment = new Query().getTreatment(treatmentName);
			
			this.name = dbTreatment.getName();
			this.cost = dbTreatment.getCost();
			
		} catch (SQLException e) {
			throw new SQLException(e);
			
		}
	}
	
	public Treatment (String name, int cost) {
		this.name = name;
		this.cost = cost;
		
	}
	
	public String getName() { return this.name; }
	public int getCost() { return this.cost; }
	
	public String toString() {
		return this.name + " (£" + this.cost + ")";
	}

	public boolean equals(Treatment t2) {
		return this.name.equals(t2.getName()) && this.cost == t2.getCost();
	}
	
	public static void main (String args[]) {
		try {
			System.out.println(new Treatment("Cleaning", 50));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
