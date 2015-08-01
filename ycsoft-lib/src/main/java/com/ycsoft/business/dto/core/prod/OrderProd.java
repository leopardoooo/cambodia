package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 订购产品提交的数据bean
 * @author new
 *
 */
public class OrderProd implements Serializable{
	private String cust_id;
	private String user_id;
	// 套餐内容组的用户选择情况和产品组选择情况
	private List<PackageGroupUser> groupSelected;
	private String prod_id;
	//资费栏目的ID （特殊）可能是折扣(tariff_id+'_'+disct_id)
	private String tariff_id;
	//上期订购SN
	private String last_order_sn;
	//订购月数
	private Integer order_months;
	// 实际支付金额（小计金额）
	private Integer pay_fee;
	//转移支付金额 (后台要验证是否一致，不一致要报错)
	private Integer transfer_fee;
	//开始计费日
	private Date eff_date;
	//失效日期
	private Date exp_date;
	
	public Date getExp_date() {
		return exp_date;
	}
	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public List<PackageGroupUser> getGroupSelected() {
		return groupSelected;
	}
	public void setGroupSelected(List<PackageGroupUser> groupSelected) {
		this.groupSelected = groupSelected;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}
	public String getLast_order_sn() {
		return last_order_sn;
	}
	public void setLast_order_sn(String last_order_sn) {
		this.last_order_sn = last_order_sn;
	}
	public Integer getOrder_months() {
		return order_months;
	}
	public void setOrder_months(Integer order_months) {
		this.order_months = order_months;
	}
	public Integer getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(Integer pay_fee) {
		this.pay_fee = pay_fee;
	}
	public Integer getTransfer_fee() {
		return transfer_fee;
	}
	public void setTransfer_fee(Integer transfer_fee) {
		this.transfer_fee = transfer_fee;
	}
	public Date getEff_date() {
		return eff_date;
	}
	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}
	
}
