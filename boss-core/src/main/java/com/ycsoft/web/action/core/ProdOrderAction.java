package com.ycsoft.web.action.core;

import org.springframework.stereotype.Controller;

import com.ycsoft.business.service.IOrderService;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

@Controller
public class ProdOrderAction extends BaseBusiAction {
	private String user_id;
	private String filter_order_sn;
	private String busi_code;
	private String cust_id;
	
	private IOrderService orderService;
	
	public String loadProdList() throws Exception{
		getRoot().setSimpleObj(orderService.queryOrderableProd(busi_code,cust_id,user_id, filter_order_sn));
		return JSON_SIMPLEOBJ;
	}
	
	public String queryProdOrderInit(){
		
		return JSON_OTHER;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	

	public String getFilter_order_sn() {
		return filter_order_sn;
	}

	public void setFilter_order_sn(String filter_order_sn) {
		this.filter_order_sn = filter_order_sn;
	}

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	
	
	
	
}
