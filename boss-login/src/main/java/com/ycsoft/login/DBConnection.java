package com.ycsoft.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnection {
	private static ComboPooledDataSource cpds;
	private static String user;
	private static String password;
	private static String url; 
	static{
		try {
			url = LoadPropertie.getInstance().getProperty("URL");

			user = LoadPropertie.getInstance().getProperty(
					"DBUSER");
			password = LoadPropertie.getInstance().getProperty(
					"DBPASSWORD","1","");
			String maxIdleTime = LoadPropertie.getInstance()
					.getProperty("MAXIDLETIME");
			String maxPoolSize = LoadPropertie.getInstance()
					.getProperty("MAXPOOLSIZE");
			String minPoolSize = LoadPropertie.getInstance()
					.getProperty("MINPOOLSIZE");
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass("oracle.jdbc.driver.OracleDriver");
			// loads the jdbc driver
			cpds.setJdbcUrl(url);
			cpds.setUser(user);
			cpds.setPassword(password);
			cpds.setMaxStatements(20);
			cpds.setMaxIdleTime(Integer.parseInt(maxIdleTime));
			cpds.setMaxPoolSize(Integer.parseInt(maxPoolSize));
			cpds.setMinPoolSize(Integer.parseInt(minPoolSize));
			cpds.setTestConnectionOnCheckin(false);
			cpds.setTestConnectionOnCheckout(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException  {
		try {

			if (LoadPropertie.getInstance().getProperty("CONNECTTYPE").equals(
					"2")) {
				Connection conn = cpds.getConnection();
				return conn;
			} else {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
				Connection conn = DriverManager.getConnection(url, user,
						password);
				return conn;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
    public static void closeRsStConn(ResultSet rs,Statement st,Connection conn){
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
		}
		try {
			if (st != null)
				st.close();
		} catch (Exception e) {
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
		}
	}


}
