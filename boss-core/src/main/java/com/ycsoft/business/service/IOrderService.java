package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;

public interface IOrderService extends IBaseService{
	public OrderProdPanel queryOrderableProd(String busi_code,String cust_id,String user_id,String filter_order_sn ) throws Exception;

	public PackageGroupPanel queryPackageGroupPanel(String cust_id,String prod_id,String last_order_sn) throws Exception;
	
	public List<CProdOrderDto> queryTransferFee(OrderProd orderProd,String busi_code) throws Exception;
	
	public Map<String,List<CProdOrderDto>> queryCustEffOrder(String cust_id) throws Exception;
	
	public String saveOrderProd(OrderProd orderProd,String busi_code) throws Exception;
	
	
	public void saveCancelProd(String[] orderSns,Integer cancelFee)throws Exception;;
	public void saveCancelTodayOrder(String orderSn,Integer cancelFee)throws Exception;;
	public List<CProdOrderDto> queryCancelFeeByCancelOrder(String busi_code,String cust_id,String order_sn)throws Exception;

	public List<CProdOrderDto> queryLogoffUserProd(String busi_code, String user_id) throws Exception;
}
