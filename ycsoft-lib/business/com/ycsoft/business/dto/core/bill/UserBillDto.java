package com.ycsoft.business.dto.core.bill;

import java.io.Serializable;

public class UserBillDto implements Serializable {
	private java.lang.String prod_name;

    private java.lang.String prod_type;

    private java.lang.Integer fee;

    private java.lang.String fee_time;

	public java.lang.String getProd_name() {
		return prod_name;
	}

	public void setProd_name(java.lang.String prodName) {
		prod_name = prodName;
	}

	public java.lang.String getProd_type() {
		return prod_type;
	}

	public void setProd_type(java.lang.String prodType) {
		prod_type = prodType;
	}

	public java.lang.Integer getFee() {
		return fee;
	}

	public void setFee(java.lang.Integer fee) {
		this.fee = fee;
	}

	public java.lang.String getFee_time() {
		return fee_time;
	}

	public void setFee_time(java.lang.String feeTime) {
		fee_time = feeTime;
	}
    
    
}
