package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFollowPay;
import com.ycsoft.beans.core.prod.CancelUserDto;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdEdit;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;

public interface IOrderService extends IBaseService{
	public OrderProdPanel queryOrderableProd(String busi_code,String cust_id,String user_id,String filter_order_sn ) throws Exception;

	public PackageGroupPanel queryPackageGroupPanel(String cust_id,String prod_id,String last_order_sn) throws Exception;
	
	public List<CProdOrderDto> queryTransferFee(OrderProd orderProd,String busi_code) throws Exception;
	
	public Map<String,List<CProdOrderDto>> queryCustEffOrder(String cust_id,String loadType) throws Exception;
	
	
	public List<String> saveOrderProdList(String busi_code,OrderProd...orderProds) throws Exception;
	//public String saveOrderProd(OrderProd orderProd,String busi_code) throws Exception;
	
	
	public void saveCancelProd(String[] orderSns,Integer cancelFee,Integer refundFee, String acctBalanceType)throws Exception;;
	
	public List<CProdOrderDto> queryCancelFeeByCancelOrder(String busi_code,String cust_id,String order_sn)throws Exception;

	public List<CProdOrderDto> queryLogoffUserProd(String busi_code, String user_id) throws Exception;

	public List<CProdOrderFollowPay> queryFollowPayOrderDto(String custId) throws Exception;

	public void savePublicRecharge(String pay_type, Integer fee, String receipt_id)throws Exception;

	public void savePublicRefund(Integer fee)throws Exception;

	public OrderProdEdit queryOrderToEdit(String orderSn)throws Exception;
	
	public void saveOrderEdit(OrderProd orderProd) throws Exception;
	public void savePayOtherFee()throws Exception;
	public Map<String, Object> queryLogoffUserProdList(String custId, List<String> userIdList) throws Exception;

}
