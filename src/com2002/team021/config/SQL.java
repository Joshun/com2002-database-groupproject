package com2002.team021.config;

public final class SQL {
	
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_NAME = "team021";
	public static final String DB_USER = "team021";
	public static final String DB_PASS = "c2832352";
	public static final String DB_HOST = "jdbc:mysql://stusql.dcs.shef.ac.uk/" + DB_NAME;
	
	public static final String DB_URL = DB_HOST +  "?user=" + DB_USER + "&password=" + DB_PASS;
	
	private SQL () {
		throw new AssertionError();
	}
	
}

