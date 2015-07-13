package com.ycsoft.report.test.other;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.report.db.ConnContainer;

public class TestConKill extends Thread {

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs=null;
	private List<String> tmp=new ArrayList<String>();

	public TestConKill(String database) throws Exception {

		try {
			conn = ConnContainer.getConn(database);
			stmt = conn.createStatement();

		

		} catch (Exception e) {
			throw e;
		}

	}

	public void ConKill() throws SQLException {
		if (stmt != null){
			stmt.cancel();
			System.err.println("stmt is killed."+tmp.size());			
		}
		else
			System.err.println("ConKill stmt is null .can not kill");
		if(rs!=null){
			rs.close();
			rs=null;
			System.err.println("rs is closed."+tmp.size());
		}else
				System.err.println("rs is null");

	}

	public void close() {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			 rs = stmt
					.executeQuery("select  cc.cust_id,cu.user_id,cc.cust_name,cu.user_type,cc.addr_id,cc.address,cc.open_time,"
							+ " c.done_code,c.done_date,c.acct_id"
							+ " from busi.c_cust cc,busi.c_user cu,C_ACCT_ACCTITEM_CHANGE c where cc.cust_id=c.cust_id"
							+ " and c.acct_id=cu.acct_id"
							+ " order by cc.cust_id,cu.user_id,cc.cust_name,cu.user_type,cc.addr_id,cc.address,cc.open_time,"
							+ " c.done_code,c.done_date,c.acct_id,cu.stb_id,cu.card_id,cu.modem_mac");
			while(rs!=null&&rs.next()){
				if(rs!=null)
				tmp.add(rs.getString(1));
				
			}
			System.out.println("ResultSet over."+tmp.size());
			if(rs==null)
				System.out.println("rs is null");
			else {
				System.out.println("rs is not null");
			}
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			e.printStackTrace();
		}
		close();
	}

}
