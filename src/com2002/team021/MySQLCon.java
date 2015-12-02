package com2002.team021;

import com2002.team021.config.SQL;

import java.sql.*;

class MySQLCon {
	
	public static void main(String args[]) {
		
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection(SQL.DB_URL);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM patients;");
			
			while (rs.next()) {
				System.out.println(rs.getObject(1) + " " +
					rs.getObject(2) + "\t" +
					rs.getObject(3) + "\t" + 
					rs.getObject(4) + "\t" +
					rs.getObject(5) + "\t" +
					rs.getObject(6) + "\t" +
					rs.getObject(7) + "\t" +
					rs.getObject(8)
				);
			}
			
			con.close();
			
		} catch (Exception e) {
			System.out.println(e);
			
		}// trycat
		
	}// main
	
}// class
