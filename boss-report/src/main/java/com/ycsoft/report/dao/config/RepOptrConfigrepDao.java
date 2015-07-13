package com.ycsoft.report.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepOptrConfigrep;
import com.ycsoft.report.commons.ReportConstants;

@Component
public class RepOptrConfigrepDao extends BaseEntityDao<RepOptrConfigrep> {

	public RepOptrConfigrepDao(){}
	
	/**
	 * 查询操作员是否有编辑报表权限
	 * @param optr_id
	 * @return
	 * @throws JDBCException
	 */
	public boolean queryEditOptr(String optr_id) throws JDBCException{
		String sql="select * from busi.rep_optr_configrep t " +
				"where t.optr_id=? and t.optr_type=? ";
		if(this.createQuery(sql, optr_id,ReportConstants.OPTRCONFIG_OPTR).first()!=null){
			return true;
		}
		return false;
	}
}
