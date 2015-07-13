package com.ycsoft.business.dto.core.voucher;

import com.ycsoft.beans.core.voucher.CVoucher;

public class VoucherDto extends CVoucher {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3462533697414749020L;
	private String start_voucher_id;
	private String end_voucher_id;
	private String start_invalid_time;
	private String end_invalid_time;
	private String start_used_time;
	private String end_used_time;
	private String start_create_time;
	private String end_create_time;

	private String procure_no;
	private String procure_dept;
	private String procurer;
	
	public String getStart_voucher_id() {
		return start_voucher_id;
	}
	public void setStart_voucher_id(String start_voucher_id) {
		this.start_voucher_id = start_voucher_id;
	}
	public String getEnd_voucher_id() {
		return end_voucher_id;
	}
	public void setEnd_voucher_id(String end_voucher_id) {
		this.end_voucher_id = end_voucher_id;
	}
	public String getStart_invalid_time() {
		return start_invalid_time;
	}
	public void setStart_invalid_time(String start_invalid_time) {
		this.start_invalid_time = start_invalid_time;
	}
	public String getEnd_invalid_time() {
		return end_invalid_time;
	}
	public void setEnd_invalid_time(String end_invalid_time) {
		this.end_invalid_time = end_invalid_time;
	}
	public String getStart_used_time() {
		return start_used_time;
	}
	public void setStart_used_time(String start_used_time) {
		this.start_used_time = start_used_time;
	}
	public String getEnd_used_time() {
		return end_used_time;
	}
	public void setEnd_used_time(String end_used_time) {
		this.end_used_time = end_used_time;
	}
	public String getStart_create_time() {
		return start_create_time;
	}
	public void setStart_create_time(String start_create_time) {
		this.start_create_time = start_create_time;
	}
	public String getEnd_create_time() {
		return end_create_time;
	}
	public void setEnd_create_time(String end_create_time) {
		this.end_create_time = end_create_time;
	}
	public String getProcure_no() {
		return procure_no;
	}
	public void setProcure_no(String procure_no) {
		this.procure_no = procure_no;
	}
	public String getProcure_dept() {
		return procure_dept;
	}
	public void setProcure_dept(String procure_dept) {
		this.procure_dept = procure_dept;
	}
	public String getProcurer() {
		return procurer;
	}
	public void setProcurer(String procurer) {
		this.procurer = procurer;
	}
}
