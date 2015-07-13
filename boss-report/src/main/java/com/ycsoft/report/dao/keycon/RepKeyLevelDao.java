/**
 * RepKeyLevelDao.java	2010/06/21
 */
 
package com.ycsoft.report.dao.keycon; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepKeyLevel;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;


/**
 * RepKeyLevelDao -> REP_KEY_LEVEL table's operator
 */
@Component
public class RepKeyLevelDao extends BaseEntityDao<RepKeyLevel> {
	
	/**
	 * default empty constructor
	 */
	public RepKeyLevelDao() {}
	
	/**
	 * 查询规则定义语句
	 * 返回List<QueryKeyValue(rule_id id,rule_sql name,data_type pid)>
	 * @param datarighttype
	 * @return
	 * @throws ReportException
	 */
	public List<QueryKeyValue> queryRuleSql(String datarighttype) throws ReportException{
		try {
	
			String sql=StringHelper.append("select distinct ru.rule_id id,",
				"'select '||ty.result_column||' id,'||ty.select_column||' name from '||ty.table_name||' where '||ru.rule_str name,",
				"ru.data_type pid",
				" from s_role r,t_rule_define ru,s_data_right_type ty ",
				" where r.role_type='DATA' and r.rule_id=ru.rule_id and r.data_right_type=ru.data_type ",
				" and ty.data_right_type=r.data_right_type and ty.table_name is not null and ru.data_type in (?) ").toString();
		
			return this.createQuery(QueryKeyValue.class, sql, datarighttype).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	/*
	 * 查询报表标准数据权限定义
	 */
	public Integer queryRepRole(String optr_id) throws ReportException{
		try {
			String sql="select s.* from s_role a,s_optr_role b,s_itemvalue s where a.role_id=b.role_id " +
						" and b.optr_id=? and a.data_right_type='REPORT' and a.data_right_level=s.item_value and s.item_key='SYS_LEVEL'";
			SItemvalue vo = this.createQuery(SItemvalue.class, sql, optr_id).first();
			if(vo==null)
				return null;
			else
				return vo.getItem_idx();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 查询自定义数据权限
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	public List<SRole> querySDatarightRole(String optr_id) throws ReportException{
		String sql="select a.* from s_role a,s_optr_role b where a.role_id=b.role_id and b.optr_id=? "
			+" and a.role_type='DATA'  and a.role_id is not null";
		try {
			return this.createQuery(SRole.class, sql, optr_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}

}
