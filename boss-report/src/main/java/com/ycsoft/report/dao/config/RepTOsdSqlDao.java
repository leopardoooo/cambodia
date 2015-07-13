package com.ycsoft.report.dao.config;

import java.sql.PreparedStatement;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TOsdSql;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
@Component
public class RepTOsdSqlDao extends BaseEntityDao<TOsdSql> {
	public RepTOsdSqlDao(){}
	
	public void saveRepTOsdSql(TOsdSql o) throws ReportException{
		PreparedStatement pst = null;
		try {
			pst =this.getConnection().prepareStatement(
						"INSERT INTO T_OSD_SQL(query_id,title,sql_content,status,optr_id,create_time) VALUES(?,?,?,?,?,sysdate)");
			pst.setString(1, o.getQuery_id());
			pst.setString(2, o.getTitle());
			pst.setString(3, o.getSql_content());
			pst.setString(4, o.getStatus());
			pst.setString(5, o.getOptr_id());
			pst.addBatch();
			pst.executeBatch();
		}catch(Exception e){
			throw new ReportException(e);
		}finally{
			try{
			if(pst!=null)
				pst.close();
			}catch(Exception e){}
		}
	}
}
