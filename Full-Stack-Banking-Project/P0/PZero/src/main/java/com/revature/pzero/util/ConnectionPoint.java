package com.revature.pzero.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoint {

	private static final String USERNAME = System.getenv("PZERO_DB_USERNAME");
	private static final String PASSWORD = System.getenv("PZERO_DB_PASSWORD");
	private static final String URL = System.getenv("PZERO_DB_URL");
	private static Connection conn;
	
	public static Connection getConnection() {
		
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
		
	}
	
}