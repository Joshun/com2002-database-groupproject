package com2002.team021;

import java.sql.*;

public class Treatment {
	
	private String name;
	private int cost;
	private String coveredBy;
	
	public Treatment (String treatmentName) throws SQLException {
		try {
			Treatment dbTreatment = new Query().getTreatment(treatmentName);
			
			this.name = dbTreatment.getName();
			this.cost = dbTreatment.getCost();
			this.coveredBy = dbTreatment.getCoveredBy();
			
		} catch (SQLException e) {
			throw new SQLException("Couldnt instantiate treatment\n" + e);
			
		}
	}
	
	public Treatment (String name, int cost, String coveredBy) {
		this.name = name;
		this.cost = cost;
		this.coveredBy = coveredBy;
		
	}
	
	public Treatment (String name, int cost) {
		this(name, cost, null);
		
	}
	
	public String getName() { return this.name; }
	public int getCost() { return this.cost; }
	public String getCoveredBy() { return this.coveredBy; }
	
	public String toString() {
		return this.name + " (£" + (this.cost/100) + ")";
	}

	@Override
	public boolean equals(Object t2) {
		Treatment t2Casted = (Treatment) t2;
		return this.name.equals(t2Casted.getName()) && this.cost == t2Casted.getCost();
	}
	
	public static void main (String args[]) {
		try {
			System.out.println(new Treatment("Cleaning", 50));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
