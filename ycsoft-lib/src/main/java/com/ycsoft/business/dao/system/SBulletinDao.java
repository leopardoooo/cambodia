/**
 * TBulletinDao.java	2010/11/26
 */
 
package com.ycsoft.business.dao.system; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SBulletin;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SBulletinDto;


/**
 * SBulletinDao -> S_BULLETIN table's operator
 */
@Component
public class SBulletinDao extends BaseEntityDao<SBulletin> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8766537619543807446L;

	/**
	 * default empty constructor
	 */
	public SBulletinDao() {}
	
	public int doUpdate(SBulletin bul) throws Exception{
		String sql = "update s_bulletin " +
				" set BULLETIN_TITLE = ?, " +
//				" CREATE_DATE = ?, " +
				" BULLETIN_CONTENT= ?, " +
//				" BULLETIN_PUBLISHER= ?, " +
//				" STATUS= ?, " +
//				" OPTR_ID= ?, " +
				" EFF_DATE= ?, " +
				" EXP_DATE= ? " +
				" where bulletin_id = ?";
		Connection conn = this.getConnection();
		PreparedStatement pst = null;
		int result = 0;
		try{
			pst = conn.prepareStatement(sql);
			pst.setString(1, bul.getBulletin_title());//BULLETIN_TITLE
			
			C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor(); 
			OracleConnection oracleConn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(pst.getConnection());
			CLOB clob = oracle.sql.CLOB.createTemporary(oracleConn, false, oracle.sql.CLOB.DURATION_SESSION);
			clob.putString(1,bul.getBulletin_content());
			pst.setClob(2, clob);//BULLETIN_CONTENT
			pst.setDate(3, new java.sql.Date(bul.getEff_date().getTime()));//EFF_DATE
			pst.setDate(4, new java.sql.Date(bul.getExp_date().getTime()));//EXP_DATE
			pst.setString(5, bul.getBulletin_id());
			result = pst.executeUpdate();
		}catch (SQLException ex) {
			JdbcUtils.closeStatement(pst);
			pst = null;
			DataSourceUtils.releaseConnection(conn, getDataSource());
			conn = null;
			throw ex;
		}finally {
			JdbcUtils.closeStatement(pst);
			DataSourceUtils.releaseConnection(conn, getDataSource());
		}
		return result;
	}
	
	public int doSave(SBulletin bulletin) throws Exception{
		//  SEQ_S_BULLETIN.Nextval,bulletin_content,create_date,bulletin_title,
		// status,exp_date,optr_id,bulletin_publisher,bulletin_id,eff_date
		String sql = "INSERT INTO S_BULLETIN (bulletin_id,bulletin_content,create_date,bulletin_title," +
				"status,exp_date,optr_id,bulletin_publisher,eff_date) " +
				"values (?,?,sysdate,?,?,?,?,?,?)";
		Connection conn = this.getConnection();
		PreparedStatement pst = null;
		int result = 0;
		try{
			pst = conn.prepareStatement(sql);
			C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor(); 
			OracleConnection oracleConn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(pst.getConnection());
			pst.setString(1, bulletin.getBulletin_id());
			CLOB clob = oracle.sql.CLOB.createTemporary(oracleConn, false, oracle.sql.CLOB.DURATION_SESSION);
			clob.putString(1,bulletin.getBulletin_content());
			pst.setClob(2, clob);//bulletin_content
			pst.setString(3, bulletin.getBulletin_title());//bulletin_title
			pst.setString(4, bulletin.getStatus());//status
			pst.setDate(5, new java.sql.Date(bulletin.getExp_date().getTime()));//exp_date
			pst.setString(6, bulletin.getOptr_id());//optr_id
			pst.setString(7, bulletin.getBulletin_publisher());//bulletin_publisher
			pst.setDate(8, new java.sql.Date(bulletin.getEff_date().getTime()));//eff_date
			result = pst.executeUpdate();
		}catch (SQLException ex) {
			JdbcUtils.closeStatement(pst);
			pst = null;
			DataSourceUtils.releaseConnection(conn, getDataSource());
			conn = null;
			throw ex;
		}finally {
			JdbcUtils.closeStatement(pst);
			DataSourceUtils.releaseConnection(conn, getDataSource());
		}
		return result;
	}


	public Pager<SBulletinDto> queryByOptrId(Integer start,Integer limit,String optrId,String deptId) throws JDBCException {
		String sql = "  SELECT a.*, c.check_date FROM s_bulletin a, s_bulletin_check c , s_bulletin_county sbc "+
			" WHERE a.bulletin_id = c.bulletin_id(+) and a.bulletin_id = sbc.bulletin_id "+
			" and c.optr_id(+) =?  AND a.status =? "+
			" AND to_date(to_char(sysdate, 'yyyymmdd hh24:mi:ss'), 'yyyymmdd hh24:mi:ss') >= a.eff_date "+
			" AND sbc.dept_id = ?   order by a.eff_date desc ";
		return createQuery(SBulletinDto.class, sql, optrId,StatusConstants.ACTIVE, deptId).setLimit(limit).setStart(start).page();
	}

	public SBulletinDto queryUnCheckByOptrId(String optrId)
			throws JDBCException {
		String sql = "SELECT * FROM s_bulletin a , s_bulletin_county sbc WHERE a.bulletin_id = sbc.bulletin_id and "
				+ " a.status=? AND to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') BETWEEN  a.eff_date AND a.exp_date "
				+ " and sbc.dept_id =(SELECT o.dept_id FROM s_optr o WHERE o.optr_id =?) "
				+ " AND NOT EXISTS (SELECT 1 FROM s_bulletin_check c WHERE c.bulletin_id=a.bulletin_id and ? = c.optr_id )";
		return createQuery(SBulletinDto.class, sql, StatusConstants.ACTIVE,optrId,optrId).first();
	}

	public void checkBulletin(String bulletinId, String optrId)
			throws JDBCException {
		String sql = "insert into s_bulletin_check (bulletin_id, optr_id, check_date) values(?,?,sysdate)";
		executeUpdate(sql, bulletinId, optrId);
	}
	public Pager<SBulletin> query(Integer start , Integer limit , String keyword,String countyId )throws Exception{
		String sql = " select a.* from s_bulletin a where 1=1 ";
		if(StringUtils.isNotEmpty(countyId)){
			sql +=" and (SELECT b.county_id FROM s_optr b WHERE a.optr_id=b.optr_id) ='"+countyId+"' ";
		}
		if(StringHelper.isNotEmpty(keyword)){
			sql += " and ( a.bulletin_title like '%"+keyword+"%' or a.bulletin_content like '%"+keyword+"%' or a.bulletin_publisher like '%"+keyword+"%' ) ";
		}
		sql += " order by a.eff_date desc";
		return createQuery(SBulletin.class,sql ).setLimit(limit).setStart(start).page();
	}
	public int updateBulletin(String bulletinId,String statusId) throws Exception {
		String sql ="update s_bulletin set status=? where bulletin_id=?";
		return executeUpdate(sql, statusId,bulletinId);
	}
	/**
	 * 更新公告内容，并删除营业员查看信息
	 */
	public int updateBulletinByWorkTask(String bulletinId,String bulletinText) throws JDBCException{
		String sql1="delete s_bulletin_check where bulletin_id=? ";
		 executeUpdate(sql1,bulletinId);
		
		String sql="update s_bulletin set bulletin_content=? where bulletin_id=?";
		return executeUpdate(sql, bulletinText,bulletinId);
	}
}
