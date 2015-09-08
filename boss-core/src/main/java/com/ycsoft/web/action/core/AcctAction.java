/**
 *
 */
package com.ycsoft.web.action.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemThresholdProp;
import com.ycsoft.beans.core.acct.CGeneralAcct;
import com.ycsoft.beans.core.acct.CGeneralContract;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.acct.QueryAcctitemThresholdDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.business.service.IAcctService;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;
/**
 * @author YC-SOFT
 *
 */
@Controller
public class AcctAction extends BaseBusiAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 5887684390458311969L;

	private IAcctService acctService;

	private String custId;
	private String custStatus;
	private String acctItemId;
	private String acctId;
	private String userId;
	private int fee;
	private int shouldPay;
	private int realPay;
	private String invaliddate;
	private String prodSn;
	private String remark;
	private String reason;
	private Integer doneCode;

	private Date invalidDate;
	private Date beginDate;
	private int months;
	private String prodId;
	private String tariffId;
	
	
	private CGeneralAcct generalAcct;
	private CGeneralContract generalContract;
	private Integer credentialStartNo;
	private Integer credentialEndNo;
	private Integer credentialAmount;
	private Integer presentAmount;
	private String contractId;
	private String gAcctId;
	private Integer changedAmount;
	//搜索关键字
	private String query;
	
	private String payFeesData;//所有缴费数据
	
	private String promFeeId;
	private int promFee;
	private String prodArrStr;
	private String preOpenTime;
	
	private String thresholdListStr;
	private String[] acctIds;
	private QueryAcctitemThresholdDto queryAcctitemThresholdDto;
	private String promFeeSn;
	
	private CFeePayDto pay;
	
	public CFeePayDto getPay() {
		return pay;
	}

	public void setPay(CFeePayDto pay) {
		this.pay = pay;
	}

	public String saveTrans() throws Exception{
		String sourceAcctId = request.getParameter("sourceAcctId");
		String orderAcctId = request.getParameter("orderAcctId");
		String sourceAcctItemId = request.getParameter("sourceAcctItemId");
		String orderAcctItemId = request.getParameter("orderAcctItemId");

		acctService.saveTrans(sourceAcctId, sourceAcctItemId, orderAcctId, orderAcctItemId, fee);
		return JSON;
	}

	public String saveRefund() throws Exception{
		acctService.saveRefund(acctId, acctItemId, prodSn, userId, fee,invaliddate,promFeeSn);
		return JSON;
	}

	/**
	 * 单居民缴费
	 * @return
	 * @throws Exception
	 */
	public String payFees() throws Exception{
		String payFeesData = request.getParameter("payFeesData");

		//cfee缴费业务记录日志，用于串数据分析
		LoggerHelper.info("CFEE", AcctAction.class,"业务前:"+payFeesData);
		
		Type type = new TypeToken<List<PayDto>>(){}.getType();
		Gson gson = new Gson();
		List<PayDto> payList = gson.fromJson(payFeesData, type);

		acctService.savePrePay(payList);
		LoggerHelper.info("CFEE", AcctAction.class,"业务后:"+gson.toJson(payList));
		return JSON;
	}
	

	/**
	 * 批量缴费,根据到期日计算应缴的费用数目.
	 * @return
	 * @throws Exception
	 */
	public String calcBatchPayFees() throws Exception{
		String payFeesData = request.getParameter("payFeesData");
		
		List<PayDto> payList = new ArrayList<PayDto>();
		Type type = new TypeToken<List<PayDto>>(){}.getType();
		Gson gson = new Gson();
		payList = gson.fromJson(payFeesData, type);
		
		payList = acctService.calcBatchPayFees(payList,invalidDate,custId);
		Map<String, PayDto> others = CollectionHelper.converToMapSingle(payList, "acct_id","acctitem_id");
//		getRoot().setRecords(payList);
		getRoot().setOthers(others);
//		return JSON_RECORDS;
		return JSON_OTHER;
	}
	
	
	/**
	 * 非居民批量缴费 或单位缴费
	 * @return
	 * @throws Exception
	 */
	public String payBatchFees() throws Exception{
		String payFeesData = request.getParameter("payFeesData");
		
		List<PayDto> payList = new ArrayList<PayDto>();
		Type type = new TypeToken<List<PayDto>>(){}.getType();
		Gson gson = new Gson();
		payList = gson.fromJson(payFeesData, type);
		int size = payList.size();
		int avgShouldPay = shouldPay/size;
		int avgRealPay = realPay/size;
		int avgPresent = avgShouldPay - avgRealPay;
		
		int lastPay = avgRealPay + (realPay - avgRealPay*size);
		int lastPresent = avgShouldPay - lastPay;
		
		payList.get(0).setFee(lastPay);
		payList.get(0).setPresent_fee(lastPresent);
		for (int i=1;i<payList.size() ;i++){
			PayDto pay = payList.get(i);
			pay.setFee(avgRealPay);
			pay.setPresent_fee(avgPresent);
		}
		
		acctService.savePrePay(payList);
		return JSON;
	}
	
	/**
	 * 取消套餐缴费.
	 * @return
	 * @throws Exception
	 */
	public String cancelPromFee() throws Exception{
		acctService.cancelPromFee(promFeeSn,reason);
		return JSON;
	}
	
	/**
	 * 取消套餐缴费.
	 * @return
	 * @throws Exception
	 */
	public String queryPromAcctItemInactive() throws Exception{
		getRoot().setRecords(acctService.queryPromAcctItemInactive(doneCode,custId,SystemConstants.BOOLEAN_TRUE.equals(query)));
		return JSON_RECORDS;
	}
	
	/**
	 * 套餐缴费.
	 * @return
	 * @throws Exception
	 */
	public String promPayFee() throws Exception{
		List<PromFeeProdDto> prodList = new ArrayList<PromFeeProdDto>();
		Type type = new TypeToken<List<PromFeeProdDto>>(){}.getType();
		Gson gson = new Gson();
		prodList = gson.fromJson(prodArrStr, type);
		
		acctService.savePromPayFee(promFeeId,promFee,prodList,preOpenTime);
		return JSON;
	}
	
	/**
	 * 模拟费补收
	 * @param prodId
	 * @param tariffId
	 * @param beginDate
	 * @param months
	 * @param invalidDate
	 * @param fee
	 * @return
	 * @throws Exception
	 */
	public String payAtvFee() throws Exception{
		acctService.savePay(prodId, tariffId, fee, beginDate, invalidDate);
		return JSON;
	}

	
	/**
	 * 调账
	 * @return
	 * @throws Exception
	 */
	public String acctAdjust() throws Exception{
		String adjust_fee = request.getParameter("adjust_fee");
		boolean nagitive = false;
		if(StringHelper.isNotEmpty(adjust_fee) && adjust_fee.indexOf("-") ==0){
			nagitive = true;
			adjust_fee = adjust_fee.substring(1);
		}
		int adjustFee = Integer.parseInt(adjust_fee);
		if(nagitive){
			adjustFee = 0-adjustFee;
		}
		String fee_type = request.getParameter("fee_type");
		String reason = request.getParameter("reason");
		acctService.saveAdjust(acctId, acctItemId, prodSn, adjustFee, fee_type, reason);
		return JSON;
	}
	
	public String acctFree() throws Exception {
		int adjustFee = Integer.parseInt(request.getParameter("adjust_fee"));
		String fee_type = request.getParameter("fee_type");
		acctService.saveAcctFree(acctId, acctItemId, prodSn, adjustFee, fee_type);
		return JSON;
	}
	
	
	/**
	 * 单位缴费，查询可以缴费的产品
	 * @return
	 * @throws Exception
	 */
	public String querySelectableProds() throws Exception{
		getRoot().setRecords(acctService.querySelectableProds(custId.split(",")));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询单位下指定客户主终端基本包的
	 * @return
	 */
	public String queryBaseProdAcct() throws Exception{
		getRoot().setRecords(acctService.queryBaseProdAcctItems(custId.split(","),prodId));
		return JSON_RECORDS;
	}
	/**
	 * 查询客户账户信息
	 * @return
	 * @throws Exception
	 */
	public String queryAcctByCustId() throws Exception{
		if(!StatusConstants.INVALID.equals(custStatus))
			getRoot().setRecords(acctService.queryAcctByCustId(custId));
		else 
			getRoot().setRecords(new ArrayList());
		return JSON_RECORDS;
	}
	/**
	 * 查询客户的公用账目
	 * @return
	 * @throws Exception
	 */
	public String queryPublicAcctitem() throws Exception{
		getRoot().setRecords(acctService.queryPublicAcctItemByCustId(custId));
		return JSON_RECORDS;
	}

	/**
	 * 查询账目下余额明细
	 * @return
	 * @throws Exception
	 */
	public String queryAcctitemActive() throws Exception{
		getRoot().setRecords(acctService.queryActive(acctId,acctItemId));
		return JSON_RECORDS;
	}

	/**
	 * 查询账目下异动明细
	 * @return
	 * @throws Exception
	 */
	public String queryAcctitemChange() throws Exception{
//		getRoot().setRecords(acctService.queryAcctitemChange(acctId,acctItemId));
		Pager<CAcctAcctitemChange> pager = acctService.queryAcctitemChange(acctId,acctItemId,start,limit);
		getRoot().setPage(pager);
		return JSON_PAGE;
	}

	/**
	 * 查询账目下冻结明细
	 * @return
	 * @throws Exception
	 */
	public String queryAcctitemInactive() throws Exception{
		getRoot().setRecords(acctService.queryAcctitemInactive(acctId,acctItemId));
		return JSON_RECORDS;
	}
	
	/**
	 * 查找上月和当月的有效套餐缴费
	 * @return
	 * @throws Exception
	 */
	public String queryIsPromFee() throws Exception{
		getRoot().setRecords(acctService.queryIsPromFee(userId,prodId));
		return JSON_RECORDS;
	}

//	/**
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public String queryAcctitemOrder() throws Exception{
//		getRoot().setRecords(acctService.queryAcctitemOrder(acctId,acctItemId));
//		return JSON_RECORDS;
//	}

	/**
	 * 查询账目下阈值明细
	 * @return
	 * @throws Exception
	 */
	public String queryAcctitemThreshold() throws Exception{
		getRoot().setRecords(acctService.queryAcctitemThreshold(acctId,acctItemId));
		return JSON_RECORDS;
	}
	
	public String updateThreshold() throws Exception {
		Type type = new TypeToken<List<CAcctAcctitemThresholdProp>>(){}.getType();
		List<CAcctAcctitemThresholdProp> list = new Gson().fromJson(thresholdListStr, type);
		acctService.updateThreshold(list);
		return JSON;
	}
	
	public String queryThresholdByAcctId() throws Exception {
		getRoot().setRecords(acctService.queryThresholdByAcctId(custId, acctIds));
		return JSON_RECORDS;
	}
	
	public String queryThresholdByCustId() throws Exception {
		getRoot().setRecords(acctService.queryThresholdByCustId(queryAcctitemThresholdDto,custId,acctIds));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询账目调账信息
	 * @param acctId
	 * @param acctItemId
	 * @return
	 * @throws JDBCException 
	 */
	public String queryAcctitemAdjust() throws Exception{
		getRoot().setRecords(acctService.queryAcctitemAdjust(acctId,acctItemId));
		return JSON_RECORDS;
	}
	
	public String editAdjustReason() throws Exception {
		acctService.editAdjustReason(doneCode, reason);
		return JSON_SUCCESS;
	}

	/**
	 * 零资费缴费或修改到期日
	 * @return
	 * @throws Exception
	 */
	public String editExpDate() throws Exception{
		acctService.savePay(prodSn, fee, months, beginDate, invalidDate);
		return JSON;
	}
	
	/**
	 * 单位批量修改到期日
	 * @return
	 * @throws Exception
	 */
	public String batchEditExpDate() throws Exception{
		String payFeesData = request.getParameter("payFeesData");
		
		List<PayDto> payList = new ArrayList<PayDto>();
		Type type = new TypeToken<List<PayDto>>(){}.getType();
		Gson gson = new Gson();
		payList = gson.fromJson(payFeesData, type);
		
		String newExpDate = request.getParameter("newExpDate");
		
		acctService.saveBatchEditExpDate(payList, newExpDate);
		return JSON;
	}
	
	/**
	 * 作废赠送.
	 * @return
	 * @throws Exception
	 */
	public String cancelFree() throws Exception{
		int fee = Integer.parseInt(request.getParameter("fee"));
		acctService.cancelFree(acctId, acctItemId, prodSn, fee);
		return JSON;
	}
	
	/**
	 * 查询分公司账户
	 * @return
	 * @throws Exception
	 */
	public String queryCompanyAcct() throws Exception{
		getRoot().setRecords(acctService.queryCompanyAcct());
		return JSON_RECORDS;
	}
	
	public String editCompanyAcct() throws Exception{
		String generalAcctListStr = request.getParameter("generalAcctListStr");
		
		List<CGeneralAcct> generalAcctList = new ArrayList<CGeneralAcct>();
		Type type = new TypeToken<List<CGeneralAcct>>(){}.getType();
		Gson gson = new Gson();
		generalAcctList = gson.fromJson(generalAcctListStr, type);
		
		acctService.editCompanyAcct(generalAcctList);
		return JSON;
	}
	
	public String queryCompanyWithOutAcct() throws Exception{
		getRoot().setRecords(acctService.queryCompanyWithOutAcct());
		return JSON_RECORDS;
	}
	
	/**
	 * 保存分公司账户
	 * @return
	 * @throws Exception
	 */
	public String saveGeneralAcct() throws Exception{
		acctService.saveGeneralAcct(generalAcct);
		return JSON;
	}
	
	/**
	 * 保存预收款或工程款
	 * @return
	 * @throws Exception
	 */
	public String saveGeneralContract() throws Exception{
		acctService.saveGeneralContract(generalContract, pay,optr,credentialStartNo,credentialEndNo,credentialAmount,presentAmount);
		return JSON;
	}
	
	public String payUnBusiFee() throws Exception{
		acctService.savePayUnBusiFee(contractId,fee);
		return JSON;
	}
	
	public String refundUnBusiFee() throws Exception{
		acctService.saveRefundUnBusiFee(generalContract);
		return JSON;
	}
	
	/**
	 * 修改预收款或工程款合同金额
	 * @return
	 * @throws Exception
	 */
	public String editGeneralContract() throws Exception{
		acctService.editGeneralContract(generalContract);
		return JSON;
	}
	
	/**
	 * 作废合同
	 * @return
	 * @throws Exception
	 */
	public String saveRemoveContract() throws Exception{
		acctService.saveRemoveContract(contractId);
		return JSON;
	}
	
	/**
	 * 添加合同凭据
	 * @return
	 * @throws Exception
	 */
	public String addCredential() throws Exception{
		acctService.addCredential(contractId,credentialStartNo,credentialEndNo,credentialAmount);
		return JSON;
	}
	
	public String queryGeneralContracts() throws Exception{
		getRoot().setPage(acctService.queryGeneralContracts(start,limit,query));
		return JSON_PAGE;
	}
	
	/**
	 * 查询合同凭据信息
	 * @return
	 * @throws Exception
	 */
	public String queryCredential() throws Exception{
		getRoot().setPage(acctService.queryCredential(contractId,start,limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询合同付款明细
	 * @return
	 * @throws Exception
	 */
	public String queryPayInfo() throws Exception{
		getRoot().setPage(acctService.queryPayInfo(contractId,start,limit));
		return JSON_PAGE;
	}
	
	
	/**
	 * 查询非营业费
	 * @return
	 * @throws Exception
	 */
	public String queryUnBusiFee() throws Exception{
		getRoot().setRecords(acctService.queryUnBusiFee());
		return JSON_RECORDS;
	}
	
	
	/**
	 * 查询客户账户作废信息
	 * @return
	 * @throws Exception
	 */
	public String queryAcctitemInvalidByCustId() throws Exception{
			getRoot().setRecords(acctService.queryAcctitemInvalidByCustId(custId));
		return JSON_RECORDS;
	}
	
	public String saveRefundInvlid() throws Exception{
		String fee_type = request.getParameter("fee_type");
		acctService.saveRefundInvlid(acctId, acctItemId,fee_type, fee);
		return JSON;
	}
	
	/**
	 * 作废账单
	 * @return
	 * @throws Exception
	 */
	public String saveCancelBill() throws Exception{
		String[] billSns = request.getParameterValues("billSns");
		acctService.saveCancelBill(billSns);
		return JSON;
	}
	
	/**
	 * 查询账目预约和被预约记录
	 * @return
	 * @throws Exception
	 */
	public String queryAllAcctitemOrder() throws Exception{
		getRoot().setRecords(acctService.queryAllByAcctitemId(acctId, acctItemId));
		return JSON_RECORDS;
	}
	
	public String queryAcctitemThresholdProp() throws Exception {
		getRoot().setRecords(acctService.queryAcctitemThresholdProp(acctId, acctItemId));
		return JSON_RECORDS;
	}
	
	public String dezsRefund() throws Exception{
		String feeType = request.getParameter("feeType");
		acctService.dezsRefund(acctId,acctItemId,feeType,fee);
		return JSON;
	}
	
	/**
	 * 作废冻结金额
	 * @return
	 * @throws Exception
	 */
	public String clearInactiveAmount() throws Exception {
		acctService.clearInactiveAmount(promFeeSn,acctId, acctItemId,fee,realPay);
		return JSON;
	}
	
	public IAcctService getAcctService() {
		return acctService;
	}

	public void setAcctService(IAcctService acctService) {
		this.acctService = acctService;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public void setAcctItemId(String acctItemId) {
		this.acctItemId = acctItemId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getPayFeesData() {
		return payFeesData;
	}

	public void setPayFeesData(String payFeesData) {
		this.payFeesData = payFeesData;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public void setProdSn(String prodSn) {
		this.prodSn = prodSn;
	}


	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	public void setInvaliddate(String invaliddate) {
		this.invaliddate = invaliddate;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public void setTariffId(String tariffId) {
		this.tariffId = tariffId;
	}

	public int getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(int shouldPay) {
		this.shouldPay = shouldPay;
	}

	public int getRealPay() {
		return realPay;
	}

	public void setRealPay(int realPay) {
		this.realPay = realPay;
	}

	public CGeneralAcct getGeneralAcct() {
		return generalAcct;
	}

	public void setGeneralAcct(CGeneralAcct generalAcct) {
		this.generalAcct = generalAcct;
	}

	public CGeneralContract getGeneralContract() {
		return generalContract;
	}

	public void setGeneralContract(CGeneralContract generalContract) {
		this.generalContract = generalContract;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public void setGAcctId(String acctId) {
		gAcctId = acctId;
	}

	public void setChangedAmount(Integer changedAmount) {
		this.changedAmount = changedAmount;
	}

	public void setCredentialStartNo(Integer credentialStartNo) {
		this.credentialStartNo = credentialStartNo;
	}

	public void setCredentialEndNo(Integer credentialEndNo) {
		this.credentialEndNo = credentialEndNo;
	}

	public void setCredentialAmount(Integer credentialAmount) {
		this.credentialAmount = credentialAmount;
	}

	public void setPresentAmount(Integer presentAmount) {
		this.presentAmount = presentAmount;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setThresholdListStr(String thresholdListStr) {
		this.thresholdListStr = thresholdListStr;
	}

	public void setAcctIds(String[] acctIds) {
		this.acctIds = acctIds;
	}

	public QueryAcctitemThresholdDto getQueryAcctitemThresholdDto() {
		return queryAcctitemThresholdDto;
	}

	public void setQueryAcctitemThresholdDto(
			QueryAcctitemThresholdDto queryAcctitemThresholdDto) {
		this.queryAcctitemThresholdDto = queryAcctitemThresholdDto;
	}

	public void setPromFeeId(String promFeeId) {
		this.promFeeId = promFeeId;
	}

	public void setPromFee(int promFee) {
		this.promFee = promFee;
	}

	public void setProdArrStr(String prodArrStr) {
		this.prodArrStr = prodArrStr;
	}

	public void setDoneCode(Integer doneCode) {
		this.doneCode = doneCode;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setPreOpenTime(String preOpenTime) {
		this.preOpenTime = preOpenTime;
	}

	public void setPromFeeSn(String promFeeSn) {
		this.promFeeSn = promFeeSn;
	}

}
