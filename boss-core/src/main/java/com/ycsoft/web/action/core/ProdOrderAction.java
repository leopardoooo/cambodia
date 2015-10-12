package com.ycsoft.web.action.core;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.prod.CancelUserDto;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.service.IOrderService;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.pojo.Root;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

@Controller
public class ProdOrderAction extends BaseBusiAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3614724278247375954L;


	private IOrderService orderService;
	
	
	private String user_id;
	private String filter_order_sn;
	private String busi_code;
	private String cust_id;
	private String last_order_sn;
	private String prod_id;
	private String orderProd;
	
	private Integer cancelFee;
	private String[] orderSns;
	private String order_sn;
	private Integer refundFee;
	
	private String payFeesData;
	private String acct_id;
	private String acctitem_id;
	private String pay_type;
	private String receipt_id;
	private Integer fee;
	//客户订单清单加载类型: ALL(所有),EFF(有效订单),不填(取有效订单，无有效则取最近一条订单)
	private String loadType;
	
	/**
	 * 查询订单编辑的初始化数据
	 * @return
	 * @throws Exception
	 */
	public String queryOrderToEdit()throws Exception{
		getRoot().setSimpleObj(orderService.queryOrderToEdit(order_sn));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 保存订单修改
	 * @param orderProd
	 * @return
	 * @throws Exception
	 */
	public String saveOrderEdit() throws Exception{
		OrderProd o=JsonHelper.toObject(orderProd, OrderProd.class);
		orderService.saveOrderEdit(o);
		return JSON_SUCCESS;
	}
	
	/**
	 * 退订界面数据初始化查询
	 * @return
	 * @throws Exception 
	 */
	public String queryCancelOrderAndFee() throws Exception{
		getRoot().setRecords(orderService.queryCancelFeeByCancelOrder(busi_code, cust_id, order_sn));
		return JSON_RECORDS;
	}
	
	/**
	 * 用户销户产品数据初始化
	 * @return
	 * @throws Exception
	 */
	public String queryLogoffUserProd() throws Exception{
		getRoot().setRecords(orderService.queryLogoffUserProd(busi_code,user_id));
		return JSON_RECORDS;
	}
	
	private File file;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String queryBatchLogoffUserProd() throws Exception {
		List<String> userIdList = new ArrayList<String>();
		if(file != null){
			userIdList = FileHelper.fileToArray(file);
		}
		Root root = getProxyRoot();
		try {
			Map<String, Object> map = orderService.queryLogoffUserProdList(cust_id, userIdList);
			root.setSimpleObj(map);
			root.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			root.setSimpleObj(e.getMessage());
			root.setSuccess(false);
		}
		
		return AJAX_UPLOAD;
	}
	
	/**
	 * 退订产品(高级和普通退订)
	 * @return
	 * @throws Exception 
	 */
	public String cancelProd() throws Exception{
		String acctBalanceType = request.getParameter("acctBalanceType");
		orderService.saveCancelProd(orderSns, cancelFee,refundFee,acctBalanceType);
		return JSON_SUCCESS;
	}
	/**
	 * 取消当天已支付订单
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String cancelTodayOrder() throws Exception{
		//orderService.saveCancelTodayOrder(order_sn, cancelFee,refundFee);
		return JSON_SUCCESS;
	}
	
	public String loadProdList() throws Exception{
		getRoot().setSimpleObj(orderService.queryOrderableProd(busi_code,cust_id,user_id, filter_order_sn));
		return JSON_SIMPLEOBJ;
	}
	
	
	public String loadPackageUserSelect() throws Exception{
		getRoot().setSimpleObj(orderService.queryPackageGroupPanel(cust_id, prod_id, last_order_sn));
		return JSON_SIMPLEOBJ;
	}
	
	public String loadTransferFee() throws Exception{
		OrderProd order=JsonHelper.toObject(orderProd, OrderProd.class);
		getRoot().setRecords(orderService.queryTransferFee(order, busi_code));
		return JSON_RECORDS;
	}
	
	public String saveOrderProd()throws Exception{
		OrderProd order=JsonHelper.toObject(orderProd, OrderProd.class);
		orderService.saveOrderProdList(busi_code,order);
		return JSON_SUCCESS;
	}
	
	/**
	 * 保存缴费
	 * @return
	 * @throws Exception
	 */
	public String savePayFee() throws Exception{
		List<OrderProd> orderList = new ArrayList<OrderProd>();
		if(StringHelper.isNotEmpty(payFeesData)){
			Type type = new TypeToken<List<OrderProd>>(){}.getType();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			orderList = gson.fromJson(payFeesData,type);
		}
		
		orderService.saveOrderProdList(busi_code,orderList.toArray(new OrderProd[orderList.size()]));
		return JSON_SUCCESS;
	}
	
	public String queryCustEffOrder() throws Exception{
		getRoot().setSimpleObj(orderService.queryCustEffOrder(cust_id,loadType));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 缴费界面初始化
	 * @return
	 * @throws Exception
	 */
	public String queryFollowPayOrderDto() throws Exception{
		getRoot().setRecords(orderService.queryFollowPayOrderDto(cust_id));
		return JSON_RECORDS;
	}
	
	public String savePublicRecharge() throws Exception{
		orderService.savePublicRecharge(pay_type,fee,receipt_id);
		return JSON;
	}
	
	public String savePayOtherFee() throws Exception{
		orderService.savePayOtherFee();
		return JSON;
	} 
	
	
	public String savePublicRefund() throws Exception{
		orderService.savePublicRefund(fee);
		return JSON;
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


	public void setLast_order_sn(String last_order_sn) {
		this.last_order_sn = last_order_sn;
	}


	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}


	public void setOrderProd(String orderProd) {
		this.orderProd = orderProd;
	}
	public Integer getCancelFee() {
		return cancelFee;
	}
	public void setCancelFee(Integer cancelFee) {
		this.cancelFee = cancelFee;
	}
	public String[] getOrderSns() {
		return orderSns;
	}
	public void setOrderSns(String[] orderSns) {
		this.orderSns = orderSns;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getPayFeesData() {
		return payFeesData;
	}

	public void setPayFeesData(String payFeesData) {
		this.payFeesData = payFeesData;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public void setReceipt_id(String receipt_id) {
		this.receipt_id = receipt_id;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}


	
	
	
}
