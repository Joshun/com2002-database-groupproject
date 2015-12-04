package com2002.team021;

import java.sql.*;

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
			throw new SQLException(e);
			
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
	public String getPostcode() { return this.postcodushe; }
	
	public String toString () {
		return this.houseNumber + " " + this.streetName + "\n" + this.district + "\n" + this.city + "\n" + this.postcode;
	}
	
	public static void main (String args[]) {
		
		try {
			Address address = new Address("14", "st74hr");
			
			System.out.println(address);
			
		} catch (Exception e) {
			System.out.println("Couln't find address. " + e);
			
		}
		
	}
	
}
