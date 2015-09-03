package com.ycsoft.beans.core.prod;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;

public class CProdOrderFollowPay extends CProdOrderDto {

	//缴费面板使用的资费列表
	private List<PProdTariffDisct> tariffList;
	
	//能否缴费（有套餐子订单则不能缴费）： true 能缴费
	private Boolean canFollowPay;
	//当前资费是否有效 :true 有效
	private Boolean currentTariffStatus;
	
	// 套餐内容组的用户选择情况和产品组选择情况
	private List<PackageGroupUser> groupSelected;
	
	private BusiFeeDto busiFee;

	public BusiFeeDto getBusiFee() {
		return busiFee;
	}

	public void setBusiFee(BusiFeeDto busiFee) {
		this.busiFee = busiFee;
	}

	public List<PackageGroupUser> getGroupSelected() {
		return groupSelected;
	}

	public void setGroupSelected(List<PackageGroupUser> groupSelected) {
		this.groupSelected = groupSelected;
	}

	public Boolean getCanFollowPay() {
		return canFollowPay;
	}

	public void setCanFollowPay(Boolean canFollowPay) {
		this.canFollowPay = canFollowPay;
	}

	public Boolean getCurrentTariffStatus() {
		return currentTariffStatus;
	}

	public void setCurrentTariffStatus(Boolean currentTariffStatus) {
		this.currentTariffStatus = currentTariffStatus;
	}

	public List<PProdTariffDisct> getTariffList() {
		return tariffList;
	}

	public void setTariffList(List<PProdTariffDisct> tariffList) {
		this.tariffList = tariffList;
	}
	
}
