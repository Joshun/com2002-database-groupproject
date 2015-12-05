package com2002.team021;

import java.sql.*;

public class HealthcarePlan {
	
	private String name;
	private int cost;
	private int checkUps;
	private int hygieneVisits;
	private int repairs;
	
	public HealthcarePlan (String name, int cost, int checkUps, int hygieneVisits, int repairs) {
		this.name = name;
		this.cost = cost;
		this.checkUps = checkUps;
		this.hygieneVisits = hygieneVisits;
		this.repairs = repairs;
		
	}
	
	public HealthcarePlan (String name) throws SQLException {
		try {
			HealthcarePlan dbHealthcarePlan = new Query().getHealthcarePlan(name);
			
			this.name = dbHealthcarePlan.getName();
			this.cost = dbHealthcarePlan.getCost();
			this.checkUps = dbHealthcarePlan.getCheckUps();
			this.hygieneVisits = dbHealthcarePlan.getHygieneVisits();
			this.repairs = dbHealthcarePlan.getRepairs();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		
	}
	
	// getters
	public String getName () { return this.name; }
	public int getCost () { return this.cost; }
	public int getCheckUps () { return this.checkUps; }
	public int getHygieneVisits () { return this.hygieneVisits; }
	public int getRepairs () { return this.repairs; }
	
	public String toString () {
		return this.getName() + this.getCost() + this.getCheckUps() + this.getHygieneVisits() + this.getRepairs();
	}
	
	public static void main (String args[]) {
		
		try {
			System.out.println( new HealthcarePlan("NHSFreePlan") );
		} catch (SQLException e) {
			System.out.println("SQL error " + e);
		}
		
	}
	
}
