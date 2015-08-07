/**
 * CProdPropChange.java	2010/07/13
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * CProdPropChange -> C_PROD_PROP_CHANGE mapping
 */
@POJO(tn = "C_PROD_STATUS_CHANGE", sn = "", pk = "")
public class CProdStatusChange  implements Serializable {
	private Integer done_code;
	private String order_sn;
	private String status;
	private Date status_date;
	public Integer getDone_code() {
		return done_code;
	}
	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStatus_date() {
		return status_date;
	}
	public void setStatus_date(Date status_date) {
		this.status_date = status_date;
	}
	 
	

}