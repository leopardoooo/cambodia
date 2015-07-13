package com.ycsoft.web.action.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.service.IUserProdService;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;
/**
 * @author YC-SOFT
 *
 */
@Controller
public class ProdAction extends BaseBusiAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -9088995947189418132L;
	private IUserProdService userProdService;
	private String userId;
	private String userType;
	private String servType;
	private String prodId;
	private String custId;
	private String tariffId;
	private String prodSn;
	private String countyId;
	private Date preOpenDate;
	private Date feeDate;

	private String publicAcctitemType;//公用账目适用类型
	
	/**
	 * 变更产品预开通日期.
	 * @return
	 * @throws Exception
	 */
	public String updatePreOpenDate() throws Exception{
		userProdService.updateProdPreOpenDate(prodSn,countyId,preOpenDate,feeDate);
		return JSON_SUCCESS;
	}
	
	
	/**
	 * 一体机授权
	 * @return
	 * @throws Exception
	 */
	public String saveBusiCmdCard() throws Exception{
		String cardId = request.getParameter("cardId");
		userProdService.saveBusiCmdCard(cardId);
		return JSON;
	}
	
	/** 
	 * 查询Ca指令
	 * @return
	 * @throws Exception
	 */
	public String queryCaCommand() throws Exception{
		String cardId = request.getParameter("cardId");
		getRoot().setPage(userProdService.queryCaCommandByCardId(cardId,start,limit));
		return JSON_PAGE;
	}
	
	/**
	 * 变更产品 公用账目适用类型.
	 * @return
	 * @throws Exception
	 */
	public String updatePublicAcctItemType() throws Exception{
		userProdService.updatePublicAcctItemType(prodSn,countyId,publicAcctitemType);
		return JSON_SUCCESS;
	}

	/**
	 * 查询多个资费的折扣信息
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public String queryTariffByTariffIds() throws Exception{
		String[] tariffId = JsonHelper.toObject(request.getParameter("tariffIds"),String[].class);
		String[] userId = JsonHelper.toObject(request.getParameter("userIds"),String[].class);
		List<ProdTariffDto> list =userProdService.queryTariffByTariffIds(tariffId,userId,custId);
		getRoot().setRecords(list);
		return JSON_RECORDS;
	}
	
	public String queryBatchTariffByTariffId(String[] tariffIds) throws Exception {
		String[] tariffId = JsonHelper.toObject(request.getParameter("tariffIds"),String[].class);
		getRoot().setRecords(userProdService.queryBatchTariffByTariffId(tariffId));
		return JSON_RECORDS;
	}
	
	
	/**
	 * 根据到期日计算费用
	 * @return
	 * @throws Exception
	 */
	public String getFeeByInvalidDate()throws Exception{
//		int balance = Integer.parseInt(request.getParameter("balance"));
//		int oweFee = Integer.parseInt(request.getParameter("oweFee"));
//		int realFee = Integer.parseInt(request.getParameter("realFee"));
//		int rent = Integer.parseInt(request.getParameter("rent"));
//		String rentType = request.getParameter("rentType");
		Date invalidDate = DateHelper.parseDate(request.getParameter("invaidDate"), "yyyy-MM-dd");
		String prodSn = request.getParameter("prodSn");
//		Date beginFeeDate = DateHelper.parseDate(request.getParameter("beginFeeDate"), "yyyy-MM-dd");
		long fee = userProdService.getFeeByInvalidDate(prodSn, invalidDate);
		getRoot().setSimpleObj(fee);
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 根据费用计算到期日
	 * @return
	 * @throws Exception
	 */
	public String getInvalidDateByFee()throws Exception{
		
		String prodSn = request.getParameter("prodSn");
		int fee = Integer.parseInt(request.getParameter("fee"));
		Date invaidDate = userProdService.getInvalidDateByFee(prodSn, fee);
		Map<String, String > map = new HashMap<String, String>();
		map.put("invalidDate", DateHelper.format(invaidDate, "yyyy-MM-dd"));
		getRoot().setOthers(map);
		return JSON_OTHER;
	}
	

	/**
	 * 重算到期日.
	 * @return
	 * @throws Exception
	 */
	public String reCalcInvalidDate()throws Exception{
		
		String prodSn = request.getParameter("prodSn");
		Date invaidDate = userProdService.reCalcInvalidDate(prodSn);
		Map<String, String > map = new HashMap<String, String>();
		map.put("invalidDate", DateHelper.format(invaidDate, "yyyy-MM-dd"));
		getRoot().setOthers(map);
		return JSON_OTHER;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryProdTree() throws Exception {
		List<TreeNode> prodtree ;
		if (StringHelper.isNotEmpty(userId)){
			prodtree = TreeBuilder.createTree((List) userProdService
				.queryCanOrderProd(userId.split(","), userType,servType));
		} else {
			prodtree = TreeBuilder.createTree((List) userProdService
					.queryCanOrderPkg(custId));
		}

		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	public String queryCanOrderBandProd() throws Exception {
		getRoot().setRecords(userProdService.queryCanOrderBandProd(userId));
		return JSON_RECORDS;
	}
	
	public String queryBatchProdTree() throws Exception {
		List<TreeNode> prodTree = TreeBuilder.createTree((List)userProdService.queryBatchCanOrderProd(userType));
		getRoot().setRecords(prodTree);
		return JSON_RECORDS;
	}

	/**
	 * 查询产品包的子产品
	 * @return
	 * @throws Exception
	 */
	public String querySubProds() throws Exception{
		getRoot().setRecords(userProdService.querySubProds(prodId));
		return JSON_RECORDS;
	}
	/**
	 * 查询产品资源
	 * @return
	 * @throws Exception
	 */
	public String queryProdRes() throws Exception{
		getRoot().setRecords(userProdService.queryProdRes(prodId));
		return JSON_RECORDS;
	}

	/**
	 * 查询产品资费
	 * @return
	 * @throws Exception
	 */
	public String queryProdTariff() throws Exception{
		getRoot().setRecords(userProdService.queryProdTariff(userId.split(","),prodId,tariffId));
		return JSON_RECORDS;
	}
	
	/**
	 * 修改免费终端，查询基本产品资费
	 * @return
	 * @throws Exception
	 */
	public String queryFreeTariff() throws Exception{
		getRoot().setRecords(userProdService.queryFreeTariff(userId.split(","),userType,prodId,tariffId));
		return JSON_RECORDS;
	}
	
	public String queryEditProdTariff() throws Exception {
		getRoot().setOthers(userProdService.queryEditProdTariff(userId.split(","), prodId, tariffId));
		return JSON_OTHER;
	}
	
	public String queryBatchProdTariff() throws Exception {
		getRoot().setRecords(userProdService.queryBatchProdTariff(prodId));
		return JSON_RECORDS;
	}
	
	/**
	 * 第二终端转副机的时候基本包可以选择的资费
	 * @return
	 * @throws Exception
	 */
	public String queryTariffForEzdToFzd() throws Exception{
		getRoot().setRecords(userProdService.queryTariffForEzdToFzd(custId, userId, prodId, tariffId));
		return JSON_RECORDS;
	}


	public String queryAllProdTariff() throws Exception{
		getRoot().setRecords(userProdService.queryAllProdTariff(userId.split(","),prodId,tariffId));
		return JSON_RECORDS;
	}
	
	
	/**
	 * @param userProdService
	 *            the userProdService to set
	 */
	public void setUserProdService(IUserProdService userProdService) {
		this.userProdService = userProdService;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return
	 */
	public String getProdId() {
		return prodId;
	}

	/**
	 * @param prodId
	 */
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	/**
	 * @return the servType
	 */
	public String getServType() {
		return servType;
	}

	/**
	 * @param servType the servType to set
	 */
	public void setServType(String servType) {
		this.servType = servType;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * @param tariffId the tariffId to set
	 */
	public void setTariffId(String tariffId) {
		this.tariffId = tariffId;
	}

	public Date getPreOpenDate() {
		return preOpenDate;
	}

	public void setPreOpenDate(Date preOpenDate) {
		this.preOpenDate = preOpenDate;
	}

	public String getProdSn() {
		return prodSn;
	}

	public void setProdSn(String prodSn) {
		this.prodSn = prodSn;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}
	public String getPublicAcctitemType() {
		return publicAcctitemType;
	}

	public void setPublicAcctitemType(String publicAcctitemType) {
		this.publicAcctitemType = publicAcctitemType;
	}
}
