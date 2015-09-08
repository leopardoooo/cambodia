package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TDeviceChangeReason;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TDeviceChangeReasonDao -> T_DEVICE_CHANGE_REASON table's operator
 */
@Component
public class TDeviceChangeReasonDao extends BaseEntityDao<TDeviceChangeReason> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1753252428031671015L;

	/**
	 * default empty constructor
	 */
	public TDeviceChangeReasonDao() {}
	
	public TDeviceChangeReason queryChangeReasonByType(String reasonType) throws Exception {
		return this.createQuery("select * from T_DEVICE_CHANGE_REASON where reason_type=?", reasonType).first();
	}

}
