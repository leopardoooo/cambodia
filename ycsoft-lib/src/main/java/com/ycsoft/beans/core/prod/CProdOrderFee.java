package com.ycsoft.beans.core.prod;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "C_PROD_ORDER_FEE", sn = "SEQ_ORDER_FEE_SN", pk = "ORDER_FEE_SN")
public class CProdOrderFee extends BusiBase implements Serializable {
	private String order_fee_sn;
	private String order_sn;
	private String input_type;
	private String input_sn;
	private String fee_type;
	private Integer input_fee;
	private Integer writeoff_fee;
	private String output_type;
	private String output_sn;
	private Integer output_fee;
	//额外信息 产品名称
	private String prod_name;
	
	private String fee_type_text;
	private String input_type_text;
	private String output_type_text;
	
	private String input_prod_id;
	private String input_prod_name;
	private String output_prod_id;
	private String output_prod_name;
	
	public String getFee_type_text() {
		return fee_type_text;
	}
	
	public String getInput_type_text() {
		return input_type_text;
	}

	public String getOutput_type_text() {
		return output_type_text;
	}

	public String getInput_prod_id() {
		return input_prod_id;
	}

	public void setInput_prod_id(String input_prod_id) {
		this.input_prod_id = input_prod_id;
	}

	public String getInput_prod_name() {
		return input_prod_name;
	}

	public void setInput_prod_name(String input_prod_name) {
		this.input_prod_name = input_prod_name;
	}

	public String getOutput_prod_id() {
		return output_prod_id;
	}

	public void setOutput_prod_id(String output_prod_id) {
		this.output_prod_id = output_prod_id;
	}

	public String getOutput_prod_name() {
		return output_prod_name;
	}

	public void setOutput_prod_name(String output_prod_name) {
		this.output_prod_name = output_prod_name;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public String getInput_type() {
		return input_type;
	}

	public void setInput_type(String input_type) {
		this.input_type = input_type;
		this.input_type_text = MemoryDict.getDictName(DictKey.ORDER_FEE_TYPE, input_type);
	}

	public String getInput_sn() {
		return input_sn;
	}

	public void setInput_sn(String input_sn) {
		this.input_sn = input_sn;
	}

	public Integer getInput_fee() {
		return input_fee;
	}

	public void setInput_fee(Integer input_fee) {
		this.input_fee = input_fee;
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
	
	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
		this.fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
	}

	public void setOrder_fee_sn(String order_fee_sn) {
		this.order_fee_sn = order_fee_sn;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	
	public Integer getWriteoff_fee() {
		return writeoff_fee;
	}
	public void setWriteoff_fee(Integer writeoff_fee) {
		this.writeoff_fee = writeoff_fee;
	}
	public Integer getOutput_fee() {
		return output_fee;
	}
	public void setOutput_fee(Integer output_fee) {
		this.output_fee = output_fee;
	}
	
}
