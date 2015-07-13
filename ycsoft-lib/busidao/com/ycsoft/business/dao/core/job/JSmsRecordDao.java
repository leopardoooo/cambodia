/**
 * JSmsRecordDao.java	2013/05/07
 */
 
package com.ycsoft.business.dao.core.job; 


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.job.JSmsRecord;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JSmsRecordDao -> J_SMS_RECORD table's operator
 */
@Component
public class JSmsRecordDao extends BaseEntityDao<JSmsRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 296611083102597453L;

	/**
	 * default empty constructor
	 */
	public JSmsRecordDao() {}
	
	public List<JSmsRecord> queryReminderCustRecord(String countyId) throws Exception {
		String sql = "select distinct c.cust_id,cl.mobile,c.county_id,c.area_id"
			+ " from c_cust c,c_cust_linkman cl,c_prod cp"
			+ " where c.cust_id=cp.cust_id and c.cust_id=cl.cust_id"
			+ " and  not exists (select 1 from j_sms_record s"
			+ " where s.cust_id=c.cust_id and send_date between (sysdate-1) and sysdate) " 
			+ " and cp.notice_date between (sysdate-1) and sysdate"
			+ " and cl.mobile is not null"
			+ " and c.county_id=? and cl.county_id=? and cp.county_id=?";
		return this.createQuery(sql, countyId, countyId, countyId).list();
	}
}