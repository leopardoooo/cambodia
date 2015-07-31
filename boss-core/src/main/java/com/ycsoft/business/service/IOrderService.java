package com.ycsoft.business.service;

import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;

public interface IOrderService extends IBaseService{
	public OrderProdPanel queryOrderableProd(String user_id,String filter_order_sn ) throws Exception;

	public PackageGroupPanel queryPackageGroupPanel(String cust_id,String prod_id,String last_order_sn) throws Exception;
}
