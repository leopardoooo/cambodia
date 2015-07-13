/**
 * TGripLogDao.java	2010/11/25
 */

package com.ycsoft.business.dao.config;


import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TGripLog;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TGripLogDao -> T_GRIP_LOG table's operator
 */
@Component
public class TGripLogDao extends BaseEntityDao<TGripLog> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1399415876323695922L;

	public TGripLogDao() {
	}

}
