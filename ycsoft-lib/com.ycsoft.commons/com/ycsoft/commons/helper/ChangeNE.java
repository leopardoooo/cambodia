package com.ycsoft.commons.helper;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ycsoft.commons.constants.SystemConstants;

public class ChangeNE  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1420766298301004096L;

	public static Connection getOracleConnection() throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@18.0.0.1:19115:boss";
		String username = "busi";
		String password = "123busi";
		Class.forName(driver); 
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}
	
	public static void main(String[] args) throws Exception {
		Connection conn = getOracleConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("select * from t_address where addr_id not in (select data_id from t_spell ) ");
			rs =stmt.executeQuery();
			String sql =  "insert into t_spell (data_id, data_type, full_sepll, seq_sepll) values (?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			stmt.clearBatch();
			 while (rs.next()) {
					stmt.setString(1,rs.getString("addr_id"));
					stmt.setString(2,SystemConstants.DATA_TYPE_ADDRESS);
					stmt.setString(3,CnToSpell.getPinYin(rs.getString("addr_name")));
					stmt.setString(4,CnToSpell.getPinYinHeadChar(rs.getString("addr_name")));
					stmt.addBatch();					
				 }
			int[] updateCounts = stmt.executeBatch();
			System.out.println(updateCounts.length);
			conn.commit();
		} catch (BatchUpdateException b) {
			System.out.println("SQLException: " + b.getMessage());
			System.out.println("SQLState: " + b.getSQLState());
			System.out.println("Message: " + b.getMessage());
			System.out.println("Vendor error code: " + b.getErrorCode());
			System.out.print("Update counts: ");
			int[] updateCounts = b.getUpdateCounts();
			for (int i = 0; i < updateCounts.length; i++) {
				System.out.print(updateCounts[i] + " ");
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("Message: " + ex.getMessage());
			System.out.println("Vendor error code: " + ex.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception: " + e.getMessage());
		} finally {
			if (conn != null)
				conn.close();
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
		}
	}

}
