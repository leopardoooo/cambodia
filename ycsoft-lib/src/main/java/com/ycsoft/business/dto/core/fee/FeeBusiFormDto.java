package com.ycsoft.business.dto.core.fee;

import com.ycsoft.beans.core.fee.CFeeBusi;
import com.ycsoft.commons.pojo.DTO;

/**
 * 封装前台杂费信息
 *
 * @author hh
 * @date Mar 10, 2010 10:27:00 AM
 */
public class FeeBusiFormDto implements DTO<CFeeBusi> {

	private String fee_type;
	private String fee_id ;
	private Integer count ;
	private String value_id ;
	private Integer should_pay ; //应收
	private Integer real_pay ;	 //实收
	private Integer buy_num;
	private String addr_id;
	private String fee_std_id;

	/**
	 * IP费的收费时间段 
	 * 格式： 20150601-20150701
	 * 存储：c_fee.disct_info
	 */
	private String disct_info;

	/**
	 * 将当前的DTO转换为CFeeBusi对象返回
	 */
	public CFeeBusi transform() {
		CFeeBusi tFee = new CFeeBusi();
		tFee.setFee_id( getFee_id());
		tFee.setCount( getCount());
		tFee.setShould_pay( getShould_pay());
		tFee.setReal_pay( getReal_pay());
		return tFee;
	}
	
	public String getFee_std_id() {
		return fee_std_id;
	}


	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}
	
	public String getDisct_info() {
		return disct_info;
	}


	public void setDisct_info(String disct_info) {
		this.disct_info = disct_info;
	}


	public String getFee_type() {
		return fee_type;
	}


	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}


	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getValue_id() {
		return value_id;
	}
	public void setValue_id(String value_id) {
		this.value_id = value_id;
	}
	public Integer getShould_pay() {
		return should_pay;
	}
	public void setShould_pay(Integer should_pay) {
		this.should_pay = should_pay;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}


	public Integer getBuy_num() {
		return buy_num;
	}


	public void setBuy_num(Integer buy_num) {
		this.buy_num = buy_num;
	}

	public String getAddr_id() {
		return addr_id;
	}


	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}
}
