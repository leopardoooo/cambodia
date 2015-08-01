package com.ycsoft.business.service;

import java.util.List;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;

public interface IOrderService extends IBaseService{
	public OrderProdPanel queryOrderableProd(String busi_code,String cust_id,String user_id,String filter_order_sn ) throws Exception;

	public PackageGroupPanel queryPackageGroupPanel(String cust_id,String prod_id,String last_order_sn) throws Exception;
	
	public List<CProdOrder> queryTransferFee(OrderProd orderProd,String busi_code) throws Exception;
	
	
	public String saveOrderProd(OrderProd orderProd,String busi_code) throws Exception;
}
