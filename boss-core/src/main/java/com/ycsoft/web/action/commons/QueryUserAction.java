
package com.ycsoft.web.action.commons;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.prod.CProdInvalidTariff;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.service.IQueryUserService;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.core.Pager;

/**
 * @author sheng
 * May 17, 2010 3:00:09 PM
 */
public class QueryUserAction extends BaseAction{

	private static final long serialVersionUID = -5695004822004229484L;
	private IQueryUserService queryUserService;
	private String custId;//客户编号
	private String custStatus;//客户状态
	private String userId;//用户编号
	private String userType;
	private String prod_name;//产品名称
	private String prod_id;

	private String prodSn;
	private String custNo;
	private String userStatus;
	private String promotion_sn;
	private String promotion_id;
	
	private String promFeeId;
	private String orderSn;
	
	/**
	 * 根据客户Id查询对应用户信息
	 * @return
	 * @throws Exception
	 */
	public String queryUser() throws Exception{
		List<UserDto> userList = null;
		if (StatusConstants.INVALID.equals(custStatus))
			userList = queryUserService.queryUserHis(custId);
		else
			userList = queryUserService.queryUser(custId);
		getRoot().setRecords(userList);
		return JSON_RECORDS;
	}

	/**
	 * 根据客户查询所有产品信息
	 * @return
	 * @throws Exception
	 */
	public String queryAllProd() throws Exception{
		Map<String,List<CProdDto>> prods =null;
		if (StatusConstants.INVALID.equals(custStatus))
			prods = queryUserService.queryAllProdHis(custId);
		else 
			prods = queryUserService.queryAllProd(custId);
		getRoot().setOthers(prods);
		return JSON_OTHER;
	}
	
	/**
	 * 根据编号数组查询产品信息
	 * @return
	 * @throws Exception
	 */
	public String queryProdByIds() throws Exception{
		String type = request.getParameter("type");
		String ids ="";
		if(type.equals("CUST")){
			ids = custId;
		}else{
			ids = userId;
		}
		getRoot().setRecords(queryUserService.queryProdByIds(ids.split(","),type,prod_id));
		return JSON_RECORDS;
	}
	public String queryBaseProdByIds() throws Exception{
		String type = request.getParameter("type");
		String ids ="";
		if(type.equals("CUST")){
			ids = custId;
		}else{
			ids = userId;
		}
		getRoot().setRecords(queryUserService.queryBaseProdByIds(ids.split(","),type));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询用户产品资源
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public String queryUserProdRes() throws Exception{
		List<PRes> pResList = queryUserService.queryUserProdRes(prodSn);
		getRoot().setRecords(pResList);
		return JSON_RECORDS;
	}

	/**
	 * 根据用户Id 查询用户异动信息
	 */
	public String queryUserPropChange() throws Exception{
		List<CUserPropChange> propChangeList = queryUserService.queryUserPropChange(userId,userType);
		getRoot().setRecords(propChangeList);
		return JSON_RECORDS;
	}
	
	/**
	 * 根据订单号查询订单金额明细
	 * @return
	 * @throws Exception
	 */
	public String queryOrderFeeDetail() throws Exception {
		getRoot().setPage(queryUserService.queryOrderFeeDetail(orderSn, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 根据用户查询促销信息
	 */
	public String queryUserPromotion() throws Exception{
		getRoot().setRecords(queryUserService.queryUserPromotion(userId));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据用户查询促销信息
	 */
	public String queryPromotionCanCancel() throws Exception{
		getRoot().setRecords(queryUserService.queryPromotionCanCancel(userId,prod_id));
		return JSON_RECORDS;
	}
	
	public String queryPromotionProdBySn() throws Exception {
		getRoot().setRecords(queryUserService.queryPromotionProdBySn(promotion_sn,promotion_id));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据用户Id 查询用户受理记录
	 */
	public String queryUserDoneCode() throws Exception{
		List<CDoneCode> doneCodeList = queryUserService.queryUserDoneCode(userId);
		getRoot().setRecords(doneCodeList);
		return JSON_RECORDS;
	}

	/**
	 * 查询当前产品的状态异动信息
	 * @param prodId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public String querProdPropChange() throws Exception{
//		getRoot().setRecords(queryUserService.querProdPropChange(prodSn));
		Pager<CProdPropChange> changes = queryUserService.querProdPropChange(prodSn,start,limit);
		getRoot().setPage(changes);
		return JSON_PAGE;
	}

	/**
	 * 查询当前产品的资费变更信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public String tariffProdChange() throws Exception {
		Pager<CProdInvalidTariff> prodChangeList = queryUserService.queryTariffChange(prodSn,start,limit);
		getRoot().setPage(prodChangeList);
		return JSON_PAGE;
	}

	/**
	 * 根据资费ID 查询产品资费信息
	 * @return
	 * @throws Exception
	 */
	public String queryUserProdTariff() throws Exception{
		String tariffId = request.getParameter("tariffId");
		PProdTariff prodTariff = queryUserService.queryProdTariffById(tariffId);
		List list = new ArrayList();
		list.add(prodTariff);
		getRoot().setRecords(list);
		return JSON_RECORDS;
	}
	
	/**
	 * 用户排斥资 
	 * @return
	 * @throws Exception
	 */
	public String queryRejectRes() throws Exception {
		getRoot().setRecords(queryUserService.queryRejectRes(userId, custId));
		return JSON_RECORDS;
	}
	
	/**
	 * 用户未排斥资 
	 * @return
	 * @throws Exception
	 */
	public String queryUnRejectRes() throws Exception {
		getRoot().setRecords(queryUserService.queryUnRejectRes(userId, custId));
		return JSON_RECORDS;
	}
	
	public String queryDynResByProdSn() throws Exception {
		getRoot().setSimpleObj(queryUserService.queryDynResByProdSn(prodSn));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询本地区适用套餐
	 * @return
	 * @throws Exception
	 */
	public String querySelectablePromPay() throws Exception{
		getRoot().setRecords(queryUserService.querySelectablePromPay(custId,optr));
		return JSON_RECORDS;
	}
	
	/**
	 * 查看该产品属进行的套餐缴费列
	 * @return
	 * @throws Exception
	 */
	public String querySelectPromFee() throws Exception{
		getRoot().setRecords(queryUserService.querySelectPromFee(userId,prodSn));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询客户适用该套餐的用户信息以及套餐树
	 * @return
	 * @throws Exception
	 */
	public String querySelectUserProm() throws Exception{
		getRoot().setSimpleObj(queryUserService.querySelectUserProm(custId, promFeeId));
		return JSON;
	}
	
	public String queryAtvProds() throws Exception{
		getRoot().setRecords(queryUserService.queryAtvProds());
		return JSON_RECORDS;
	}
	
	public String queryUserByCustNoAndStatus() throws Exception {
		getRoot().setRecords(queryUserService.queryUserByCustNoAndStatus(custNo,userStatus));
		return JSON_RECORDS;
	}
	
	public String queryZteBandRes() throws Exception {
		getRoot().setRecords(queryUserService.queryZteBandRes());
		return JSON_RECORDS;
	}

	public IQueryUserService getQueryUserService() {
		return queryUserService;
	}

	public void setQueryUserService(IQueryUserService queryUserService) {
		this.queryUserService = queryUserService;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public String getProdSn() {
		return prodSn;
	}

	public void setProdSn(String prodSn) {
		this.prodSn = prodSn;
	}

	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
	}

	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

	public void setPromFeeId(String promFeeId) {
		this.promFeeId = promFeeId;
	}
	
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

}
