package com.ycsoft.business.dao.core.acct;

/**
 * CGeneralAcctDao.java	2011/01/24
 */

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CGeneralAcct;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;



/**
 * CGeneralAcctDao -> C_GENERAL_ACCT table's operator
 */
@Component
public class CGeneralAcctDao extends BaseEntityDao<CGeneralAcct> {
	

	/**
	 * default empty constructor
	 */
	public CGeneralAcctDao() {}
	
	
	/**
	 * 查询分公司账户
	 * @return
	 * @throws Exception
	 */
	public List<CGeneralAcct> queryCompanyAcct() throws Exception{
		String sql = "select c.*,S.DEPT_NAME COUNTY_NAME from c_general_acct c,s_dept s " +
				" where c.type=? AND C.COUNTY_ID= S.DEPT_ID ";
		return createQuery(CGeneralAcct.class,sql, SystemConstants.General_ACCT_TYPE_COMPANY).list();
	}
	
	/**
	 * 根据地区编号查询分公司账户
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public CGeneralAcct queryCompanyAcctByCountyId(String countyId) throws Exception{
		String sql = "select c.* from c_general_acct c where c.county_id = ? and c.type = ?";
		return createQuery(CGeneralAcct.class,sql, countyId,SystemConstants.General_ACCT_TYPE_COMPANY).first();
	}


	/**
	 * 查询没有分公司账户的分公司
	 * @return
	 * @throws JDBCException 
	 * @throws Exception
	 */
	public List<SDept> queryCompanyWithOutAcct() throws JDBCException {
		String sql = "select * from s_dept s where s.dept_type=? and s.dept_id not in(select county_id from c_general_acct c where c.type = 'COMPANY')";
		return createQuery(SDept.class, sql, SystemConstants.DEPT_TYPE_FGS).list();
	}

}
