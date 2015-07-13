package com.ycsoft.report.query.daq;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.db.ConnContainer;

/**
 * 数据库提取数据
 */
public class DBAcquisition implements DataReader {

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String database =null;
	private String sql=null;
	
	public DBAcquisition(String sql,String database){
		this.sql=sql;
		this.database=database;
	}
	
	public void close() throws ReportException {
		try {
			if (rs != null){
				rs.close();
				rs=null;
			}
		} catch (Exception e) {
		}
		try {
			if (stmt != null){
				stmt.close();
				stmt=null;
			}
		} catch (Exception e) {
		}
		try {
			if (conn != null){
				conn.close();
				conn=null;
			}
		} catch (Exception e) {
		}
	}

	public Object getObject(int i) throws ReportException {
		try {
			return rs.getObject(i);
		} catch (SQLException e) {
			throw new ReportException(e);
		}
	}

	public String getString(int i) throws ReportException {
		try {
			return rs.getString(i);
		} catch (SQLException e) {
			throw new ReportException(e);
		}
	}

	public boolean next() throws ReportException {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new ReportException(e);
		}
	}

	public void open() throws ReportException {
		try {
			conn = ConnContainer.getConn(database);
			stmt = conn.createStatement();
			stmt.setFetchSize(1000);
			LoggerHelper.debug(this.getClass(),sql);
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new ReportException(e,e.getSQLState());
		}
	}


}
