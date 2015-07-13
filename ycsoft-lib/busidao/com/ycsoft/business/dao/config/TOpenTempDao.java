/**
 * TOpenTempDao.java	2010/10/12
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TOpenTemp;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;



/**
 * TOpenTempDao -> T_OPEN_TEMP table's operator
 */
@Component
public class TOpenTempDao extends BaseEntityDao<TOpenTemp> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5261517951138902778L;

	/**
	 * default empty constructor
	 */
	public TOpenTempDao() {}

	/**
	 * @param county_id
	 * @return
	 */
	public TOpenTemp queryByCountyId(String userType, String countyId) throws JDBCException{
		String sql = "SELECT tot.* FROM T_OPEN_TEMP tot, t_template tt, t_template_county ttc "+
		 " where tt.template_id = ttc.template_id "+
		 "    and tot.template_id = tt.template_id "+
		 "    and tt.template_type = 'OPEN_TEMP' "+
		 "	  and tot.user_type=?"+
		 "    and ttc.county_id = ? ";
		List<TOpenTemp> l = this.createQuery(sql, userType, countyId).list();
		if (l == null || l.size()==0)
			return null;
		else
			return l.get(0);
	}
	public List<TOpenTemp> queryOpenTemps(String templateId) throws JDBCException{
		String sql = "select * from t_open_temp t where t.template_id=?";
		return createQuery(TOpenTemp.class, sql, templateId).list();
	}

	public void deleteByTplId(String templateId) throws JDBCException {
		String sql = "delete from t_open_temp t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

}
