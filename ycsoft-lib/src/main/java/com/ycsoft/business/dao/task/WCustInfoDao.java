/**
 * WCustInfoDao.java	2013/08/23
 */
 
package com.ycsoft.business.dao.task; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WCustInfo;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * WCustInfoDao -> W_CUST_INFO table's operator
 */
@Component
public class WCustInfoDao extends BaseEntityDao<WCustInfo> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -217262071851597870L;

	/**
	 * default empty constructor
	 */
	public WCustInfoDao() {}

	public void updateTask(String task_id, String taskCustName, String tel) throws Exception{
		String sql = "UPDATE w_cust_info t SET t.task_cust_name=?,t.tel = ?  where t.work_id=?";
		this.executeUpdate(sql, taskCustName,tel,task_id);
	}

}
