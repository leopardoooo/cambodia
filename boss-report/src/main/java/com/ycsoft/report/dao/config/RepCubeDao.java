package com.ycsoft.report.dao.config;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

@Component
public class RepCubeDao extends BaseEntityDao<RepCube> {
	
	public List<RepCube> queryRepCube(String rep_id) throws ReportException{
		String sql="select * from rep_cube where rep_id=?";
		try {
			return this.createQuery(sql, rep_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	public void deleteRepCube(String rep_id) throws ReportException{
		String sql="delete from rep_cube where rep_id=?";
		try {
			this.executeUpdate(sql, rep_id);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 检查cube和维度的数据源是否一致
	 * @return
	 * @throws ReportException 
	 */
	public List<RepDefine> checkCubeDimDatabase() throws ReportException{
		try{
		String sql="select d.rep_id,d.rep_name,dim.id rep_info,dim.name remark from rep_cube c,rep_define d,rep_dimension dim " +
				" where c.rep_id=d.rep_id and c.column_type<>'measure' and c.column_define=dim.id and d.database<>dim.database";
		return this.createQuery(RepDefine.class, sql).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
		
	}
	
}
