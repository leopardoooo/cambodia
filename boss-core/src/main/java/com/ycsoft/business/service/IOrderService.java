package com.ycsoft.business.service;

import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;

public interface IOrderService extends IBaseService{
	public OrderProdPanel queryOrderableProd(
			String optr_type,String cust_id,String user_id,String filter_order_sn ) throws Exception;

}
