package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.service.IOrderService;
import com.ycsoft.commons.constants.SystemConstants;

public class OrderService extends BaseBusiService implements IOrderService{
	private PProdDao pProdDao;
	private PProdTariffDao pProdTariffDao;
	private PProdTariffDisctDao pProdTariffDisctDao;

	@Override
	public OrderProdPanel queryOrderableProd(String optr_type, String cust_id, String user_id,
			String filter_order_sn) throws Exception {
		return null;
	}
	
	private OrderProdPanel queryUserOrderableProd(String user_Id) throws Exception{
		OrderProdPanel panel = new OrderProdPanel();
		CUser user = userComponent.queryUserById(user_Id);
		if (user == null)
			return null;
		pProdDao.queryCanOrderUserProd(user.getUser_type(), user.getCounty_id(), user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
		return panel;
		
	}
	
	private OrderProdPanel queryOrderableGoon(String filter_order_sn) throws Exception{
		return null;
	}
	
	private  Map<PProd, List<PProdTariffDisct>> queryOrderableUp(String filter_order_sn) throws Exception{
		return null;
	}
	
	private List<PProdTariffDisct> queryTariffList(CUser user,PProd prod) throws Exception{
		List<PProdTariffDisct> tariffList = new ArrayList<>();
		List<ProdTariffDto> ptList = pProdTariffDao.queryProdTariff(prod.getProd_id(),  user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
		return tariffList;
		
	}

}
