package com.ycsoft.report.query.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.query.ResultSetExtractor;

public class QuerySqlImpl implements QuerySql {

	/**
	 * 返回值为合计项字符串
	 * 
	 */
	public Map<String, String> testSQL(String sql, String database)
			throws ReportException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnContainer.getConn(database);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			StringBuilder totals = new StringBuilder();
			StringBuilder groups = new StringBuilder();
			Map<String, String> columnnamemap = new HashMap<String, String>();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				String ColumnName = rsmd.getColumnName(i + 1).trim();
				if ("NUMBER".equals(rsmd.getColumnTypeName(i + 1))
						|| "INTEGER".equals(rsmd.getColumnTypeName(i + 1))
						||"INT".equals(rsmd.getColumnTypeName(i + 1))) {
					totals.append(ColumnName).append(",");
				}
				groups.append(ColumnName).append(",");

				if (ColumnName.indexOf(',') > -1)
					throw new ReportException("列名含有',':" + ColumnName, sql);
				if (columnnamemap.containsKey(rsmd.getColumnName(i + 1))) {
					throw new ReportException("列名重复:" + ColumnName, sql);
				} else {
					columnnamemap.put(ColumnName, "");
				}
			}
			String total_list = totals.toString();
			String group_list = groups.toString();
			Map<String, String> testmap = new HashMap<String, String>();
			if (total_list != null && total_list.length() > 0) {
				testmap.put("total", total_list.substring(0, total_list
						.length() - 1));
			}
			if (group_list != null && group_list.length() > 0) {
				testmap.put("group", group_list.substring(0, group_list
						.length() - 1));
			}
			return testmap;
		} catch (SQLException e) {
			throw new ReportException(e.getMessage(), e, sql);
		} catch (ReportException e) {
			throw e;
		} catch (Exception e) {
			throw new ReportException("System_Error:" + e.getMessage(), e, sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public List<String> getColumnNameList(String sql, String database)
			throws ReportException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnContainer.getConn(database);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			List<String> list = new ArrayList<String>();

			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				String ColumnName = rsmd.getColumnName(i + 1).trim();
				list.add(ColumnName);
			}

			return list;
		} catch (SQLException e) {
			throw new ReportException(e.getMessage(), e, sql);
		} catch (Exception e) {
			throw new ReportException("System_Error:" + e.getMessage(), e, sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public <T> T getColumnRSMD(String sql, String database, ResultSetExtractor<T> rse)
			throws ReportException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnContainer.getConn(database);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return rse.extractData(rs);
		} catch (SQLException e) {
			throw new ReportException(e.getMessage(), e, sql);
		} catch (Exception e) {
			throw new ReportException("System_Error:" + e.getMessage(), e, sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public String translateTemplateKey(String sql) {
		for(String key:SystemConfig.getTemplateKeyList())
			sql=sql.replaceAll(key, SystemConfig.getTemplateKeyMap().get(key));
		return sql;
	}
}
