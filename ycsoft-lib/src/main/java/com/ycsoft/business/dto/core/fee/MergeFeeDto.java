package com.ycsoft.business.dto.core.fee;

import java.util.Date;

/**
 * 合并费用dto
 */
public class MergeFeeDto {

	private String fee_text ;
	private String fee_sn ;
	private String printitem_id ;
	private String printitem_name;
	private Integer real_pay ;
	private Date create_time ;

	public String getFee_text() {
		return fee_text;
	}
	public void setFee_text(String fee_text) {
		this.fee_text = fee_text;
	}
	public String getFee_sn() {
		return fee_sn;
	}
	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}
	public String getPrintitem_id() {
		return printitem_id;
	}
	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}
	public String getPrintitem_name() {
		return printitem_name;
	}
	public void setPrintitem_name(String printitem_name) {
		this.printitem_name = printitem_name;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
