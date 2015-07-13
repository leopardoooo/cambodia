/**
 * RepSqlDao.java	2010/06/21
 */

package com.ycsoft.report.dao.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepSql;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;

/**
 * RepSqlDao -> REP_SQL table's operator
 */
@Component
public class RepSqlDao extends BaseEntityDao<RepSql> {

	/**
	 * default empty constructor
	 */
	public RepSqlDao() {
	}

	/**
	 * 保存Sql
	 * 
	 * @param repSql
	 * @throws JDBCException
	 */
	public void saveSql(RepSql repSql) throws ReportException {

		PreparedStatement pst = null;
		try {
			pst =this.getConnection().prepareStatement(
							"INSERT INTO REP_SQL(REP_ID,SQL_INDEX,QUERY_SQL) VALUES(?,?,?)");

			String querysql = repSql.getQuery_sql();

			int index = querysql.length() / ReportConstants.SQLBASELENGTH;

			for (int i = 0; i <= index; i++) {
				if (i == index) {
					pst.setString(1, repSql.getRep_id());
					pst.setInt(2, i);
				    String ttt=querysql.substring(i
							* ReportConstants.SQLBASELENGTH);
					pst.setString(3, ttt);
					
					pst.addBatch();
				} else {
					pst.setString(1, repSql.getRep_id());
					pst.setInt(2, i);
					String aaa=querysql.substring(i
							* ReportConstants.SQLBASELENGTH, (i + 1)
							* ReportConstants.SQLBASELENGTH);
					pst.setString(3, aaa);
					pst.addBatch();
				}
			}
			pst.executeBatch();

		} catch (SQLException e) {
			throw new ReportException(e);
		}finally{
			if(pst!=null)
				try {
					pst.close();
				} catch (Exception e) {}
		}

//		try {
//			String querysql = repSql.getQuery_sql();
//			int index = querysql.length() / ReportConstants.SQLBASELENGTH;
//			for (int i = 0; i <= index; i++) {
//				RepSql o=new RepSql();
//				if (i == index) {
//					o.setRep_id(repSql.getRep_id());
//					o.setSql_index(i);
//					o.setQuery_sql(querysql.substring(i
//							* ReportConstants.SQLBASELENGTH));			
//					this.save(o);
//				} else {
//					o.setRep_id(repSql.getRep_id());
//					o.setSql_index(i);
//					o.setQuery_sql(querysql.substring(i
//							* ReportConstants.SQLBASELENGTH, (i + 1)
//							* ReportConstants.SQLBASELENGTH));
//					this.save(o);
//				}
//			}
//		} catch (JDBCException e) {
//			throw new ReportException(e);
//		}
	}

	public String getSql(String rep_id) throws JDBCException {
		String sql = "select * from rep_sql where rep_id=? order by sql_index";
		List<RepSql> list = this.findList(sql, rep_id);
		StringBuilder sb = new StringBuilder();
		for (RepSql repsql : list)
			sb.append(repsql.getQuery_sql());
		String sql_contect=sb.toString();
		return sql_contect;
	}

	/**
	 * 删除SQL定义
	 * 
	 * @param rep_id
	 * @throws JDBCException
	 */
	public void delete(String rep_id) throws JDBCException {
		this.executeUpdate("delete from rep_sql where rep_id=?", rep_id);
	}
}
