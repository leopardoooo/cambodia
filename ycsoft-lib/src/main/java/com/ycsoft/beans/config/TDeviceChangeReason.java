package com.ycsoft.beans.config;

import java.io.Serializable;
import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.daos.config.POJO;

/**
 * TDeviceChangeReason -> T_DEVICE_CHANGE_REASON mapping
 */
@POJO(tn = "T_DEVICE_CHANGE_REASON", sn = "", pk = "")
public class TDeviceChangeReason extends OptrBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2178010561448577862L;
	private String reason_type;
	private String reason_text;
	private String is_charge;
	private String is_reclaim;
	private String is_lost;
	
	public String getReason_type() {
		return reason_type;
	}
	public void setReason_type(String reason_type) {
		this.reason_type = reason_type;
	}
	public String getReason_text() {
		return reason_text;
	}
	public void setReason_text(String reason_text) {
		this.reason_text = reason_text;
	}
	public String getIs_charge() {
		return is_charge;
	}
	public void setIs_charge(String is_charge) {
		this.is_charge = is_charge;
	}
	public String getIs_reclaim() {
		return is_reclaim;
	}
	public void setIs_reclaim(String is_reclaim) {
		this.is_reclaim = is_reclaim;
	}
	public String getIs_lost() {
		return is_lost;
	}
	public void setIs_lost(String is_lost) {
		this.is_lost = is_lost;
	}
	
}
