package com.ycsoft.beans.core.prod;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "C_PROD_ORDER_FEE_OUT", sn = "", pk = "")
public class CProdOrderFeeOut extends BusiBase implements Serializable {
	
	private String order_fee_sn;
	private String output_type;
	private String output_sn;
	private Integer pre_fee;
	private Integer output_fee;
	private Integer fee;
	private String remark;

	//额外属性
	private String fee_type;
	private String order_sn;
	private String output_type_text;


	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getPre_fee() {
		return pre_fee;
	}

	public void setPre_fee(Integer pre_fee) {
		this.pre_fee = pre_fee;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOutput_type_text() {
		return output_type_text;
	}

	public String getOutput_type() {
		return output_type;
	}

	public void setOutput_type(String output_type) {
		this.output_type = output_type;
		this.output_type_text = MemoryDict.getDictName(DictKey.ORDER_FEE_TYPE, output_type);
	}

	public String getOutput_sn() {
		return output_sn;
	}

	public void setOutput_sn(String output_sn) {
		this.output_sn = output_sn;
	}

	public String getOrder_fee_sn() {
		return order_fee_sn;
	}
	
	
	public void setOrder_fee_sn(String order_fee_sn) {
		this.order_fee_sn = order_fee_sn;
	}
	
	
	
	public Integer getOutput_fee() {
		return output_fee;
	}
	public void setOutput_fee(Integer output_fee) {
		this.output_fee = output_fee;
	}
	
}
