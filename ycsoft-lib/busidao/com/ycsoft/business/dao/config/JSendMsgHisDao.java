/**
 * JSendMsgHisDao.java	2010/11/21
 */
 
package com.ycsoft.business.dao.config; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.JSendMsgHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JSendMsgHisDao -> J_SEND_MSG_HIS table's operator
 */
@Component
public class JSendMsgHisDao extends BaseEntityDao<JSendMsgHis> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5998385760628033731L;

	/**
	 * default empty constructor
	 */
	public JSendMsgHisDao() {}

}
