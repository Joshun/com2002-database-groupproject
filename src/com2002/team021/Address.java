package com2002.team021;

import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Address {
	
	private String houseNumber;
	private String streetName;
	private String district;
	private String city;
	private String postcode;
	
	public Address (String houseNumber, String postcode) throws SQLException {
		try {
			Address q = new Query().getAddress(houseNumber, postcode);
			
			this.houseNumber = q.getHouseNumber();
			this.streetName = q.getStreetName();
			this.district = q.getDistrict();
			this.city = q.getCity();
			this.postcode = q.getPostcode();
			
			
		} catch (SQLException e) {
			throw new SQLException("Address not in database.", e);
			
		}
		
	}
	
	public Address (String houseNumber, String streetName, String district, String city, String postcode) {
		this.houseNumber = houseNumber;
		this.streetName = streetName;
		this.district = district;
		this.city = city;
		this.postcode = postcode;
		
	}
	
	public String getHouseNumber() { return this.houseNumber; }
	public String getStreetName() { return this.streetName; }
	public String getDistrict() { return this.district; }
	public String getCity() { return this.city; }
	public String getPostcode() { return this.postcode; }
	
	public String toString () {
		return this.houseNumber + ", " + this.streetName + ", " + this.district + ", " + this.city + ", " + this.postcode+ "\n";
	}
	
	public static boolean isValidPostcode (String pc) {
		String pattern = "([a-z]{1,2}\\d{1,2})\\s?(\\d{1,2}[a-z]{2})";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(pc.trim().toLowerCase());
		return m.matches();
	}
	
	public static void main (String args[]) {
		
		try {
			// Address address = new Address("14", "st74hr");
			
			System.out.println(isValidPostcode("st73 2ds"));
			
		} catch (Exception e) {
			System.out.println("Couln't find address. " + e);
			
		}
		
	}
	
}
