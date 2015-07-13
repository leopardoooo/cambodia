package com.ycsoft.business.dto.core.cust;

import java.io.Serializable;

public class CustGeneralInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8884493806041514277L;
	private String cust_id;
	private String cust_no;
	private String cust_name;
	private int totalUserAmount;		//用户总数
	private int activeUserAmount;		//正常用户数
	private int stopUserAmount;			//报停用户数
	private int atvUserAmount;			//模拟用户数
	private int dtvUserAmount;			//数字用户数
	private int bandUserAmount;			//宽带用户数
	private int totalProdAmount;		//产品总数
	private int activeProdAmount;		//正常产品数
	private int oweStopProdAmount;		//欠费停产品数
	private int oweUnStopProdAmount;	//欠费未停产品数
	private int totalBalance;			//总余额
	private int totalOwn;				//总欠费
	private int balance;				//积分
	private int hisBalance;				//历史积分
	
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public int getTotalUserAmount() {
		return totalUserAmount;
	}
	public void setTotalUserAmount(int totalUserAmount) {
		this.totalUserAmount = totalUserAmount;
	}
	public int getActiveUserAmount() {
		return activeUserAmount;
	}
	public void setActiveUserAmount(int activeUserAmount) {
		this.activeUserAmount = activeUserAmount;
	}
	public int getStopUserAmount() {
		return stopUserAmount;
	}
	public void setStopUserAmount(int stopUserAmount) {
		this.stopUserAmount = stopUserAmount;
	}
	public int getAtvUserAmount() {
		return atvUserAmount;
	}
	public void setAtvUserAmount(int atvUserAmount) {
		this.atvUserAmount = atvUserAmount;
	}
	public int getDtvUserAmount() {
		return dtvUserAmount;
	}
	public void setDtvUserAmount(int dtvUserAmount) {
		this.dtvUserAmount = dtvUserAmount;
	}
	public int getBandUserAmount() {
		return bandUserAmount;
	}
	public void setBandUserAmount(int bandUserAmount) {
		this.bandUserAmount = bandUserAmount;
	}
	public int getTotalProdAmount() {
		return totalProdAmount;
	}
	public void setTotalProdAmount(int totalProdAmount) {
		this.totalProdAmount = totalProdAmount;
	}
	public int getActiveProdAmount() {
		return activeProdAmount;
	}
	public void setActiveProdAmount(int activeProdAmount) {
		this.activeProdAmount = activeProdAmount;
	}
	public int getOweStopProdAmount() {
		return oweStopProdAmount;
	}
	public void setOweStopProdAmount(int oweStopProdAmount) {
		this.oweStopProdAmount = oweStopProdAmount;
	}
	public int getOweUnStopProdAmount() {
		return oweUnStopProdAmount;
	}
	public void setOweUnStopProdAmount(int oweUnStopProdAmount) {
		this.oweUnStopProdAmount = oweUnStopProdAmount;
	}
	public int getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(int totalBalance) {
		this.totalBalance = totalBalance;
	}
	public int getTotalOwn() {
		return totalOwn;
	}
	public void setTotalOwn(int totalOwn) {
		this.totalOwn = totalOwn;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getHisBalance() {
		return hisBalance;
	}
	public void setHisBalance(int hisBalance) {
		this.hisBalance = hisBalance;
	}
}
