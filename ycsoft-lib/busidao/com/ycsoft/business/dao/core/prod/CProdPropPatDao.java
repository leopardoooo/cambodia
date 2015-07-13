/**
 * CProdPropChangeDao.java	2010/07/13
 */

package com.ycsoft.business.dao.core.prod;


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdPropPat;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CProdPropChangeDao -> C_PROD_PROP_CHANGE table's operator
 */
@Component
public class CProdPropPatDao extends BaseEntityDao<CProdPropPat> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4710329294236197489L;

	/**
	 * default empty constructor
	 */
	public CProdPropPatDao() {}
	
	/**
	 * 按客户查产品的手工变更账目级别有效记录
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CProdPropPat> queryPatsByCustId(String custId) throws JDBCException{
		String sql="select * from c_prod_prop_pat where cust_id=? and status=? ";
		return this.createQuery(sql, custId,StatusConstants.ACTIVE).list();
	} 
	
}
