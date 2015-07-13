/**
/**
 * RCard.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "R_DEVICE_RECLAIM", sn = "", pk = "DONE_CODE,DEVICE_ID")
public class RDeviceReclaim extends BusiBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3336532867700938716L;

	private String device_id;
	private String depot_id;
	private String status;
	private Date confirm_time;
	private String confirm_optr;
	private String cust_id;
	private String reclaim_reason;
	private String is_history;
	private String buy_mode;
	
	private String reclaim_reason_text;
	private String status_text;
	private String depot_text;
	private String confirm_optr_text;
	
	/**
	 * default empty constructor
	 */
	public RDeviceReclaim() {
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String deviceId) {
		device_id = deviceId;
	}

	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depotId) {
		depot_id = depotId;
		depot_text = MemoryDict.getDictName(DictKey.DEPOT, depotId);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	public Date getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(Date confirmTime) {
		confirm_time = confirmTime;
	}

	public String getConfirm_optr() {
		return confirm_optr;
	}

	public void setConfirm_optr(String confirmOptr) {
		confirm_optr = confirmOptr;
		confirm_optr_text = MemoryDict.getDictName(DictKey.OPTR, confirmOptr);
	}

	public String getDepot_text() {
		return depot_text;
	}

	public String getConfirm_optr_text() {
		return confirm_optr_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	/**
	 * @return the reclaim_reason
	 */
	public String getReclaim_reason() {
		return reclaim_reason;
	}

	/**
	 * @param reclaim_reason the reclaim_reason to set
	 */
	public void setReclaim_reason(String reclaim_reason) {
		this.reclaim_reason = reclaim_reason;
		reclaim_reason_text = MemoryDict.getDictName(DictKey.RECLAIM_REASON, reclaim_reason);
		if(StringHelper.isEmpty(reclaim_reason_text)){
			reclaim_reason_text = MemoryDict.getDictName(DictKey.CHANGE_REASON, reclaim_reason);
		}
	}

	public void setReclaim_reason_text(String reclaim_reason_text) {
		this.reclaim_reason_text = reclaim_reason_text;
	}

	public String getReclaim_reason_text() {
//		reclaim_reason_text = MemoryDict.getDictName(DictKey.RECLAIM_REASON, reclaim_reason);
//		if(StringHelper.isEmpty(reclaim_reason_text)){
//			reclaim_reason_text = MemoryDict.getDictName(DictKey.CHANGE_REASON, reclaim_reason);
//		}
		return reclaim_reason_text;
	}

	public String getIs_history() {
		return is_history;
	}

	public void setIs_history(String is_history) {
		this.is_history = is_history;
	}

	public String getBuy_mode() {
		return buy_mode;
	}

	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
	}

}