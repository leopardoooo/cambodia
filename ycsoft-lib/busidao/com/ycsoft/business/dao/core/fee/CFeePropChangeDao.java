/**
 * CFeePropChangeDao.java	2011/03/09
 */
 
package com.ycsoft.business.dao.core.fee; 

import org.springframework.stereotype.Component;
import com.ycsoft.beans.core.fee.CFeePropChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CFeePropChangeDao -> C_FEE_PROP_CHANGE table's operator
 */
@Component
public class CFeePropChangeDao extends BaseEntityDao<CFeePropChange> {
	
	/**
	 * default empty constructor
	 */
	public CFeePropChangeDao() {}

}
