package com2002.team021.config;

public class SQL {
	
	public static final String DB_HOST = "stusql.dcs.shef.ac.uk";
	public static final String DB_USER = "team021";
	public static final String DB_PASS = "c2832352";
	public static final String DB_NAME = "team021";
	
	public static final String DB_CONNECTION_STRING =
		"jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASS;
	
}
