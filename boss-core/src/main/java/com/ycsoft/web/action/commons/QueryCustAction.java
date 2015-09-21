package com.ycsoft.web.action.commons;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.business.dto.core.cust.CustDeviceDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.service.IDocService;
import com.ycsoft.business.service.IDoneCodeService;
import com.ycsoft.business.service.IQueryCustService;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;


/**
 * 处理首页的查询请求。
 *
 * @author hh
 * @date Feb 8, 2010 7:10:35 PM
 */
@Controller
public class QueryCustAction extends BaseBusiAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 4365667203669261928L;
	private IQueryCustService queryCustService;
	private IDocService docService;
	private IDoneCodeService doneCodeService;
	private String search_type ;
	private String search_value ;
	private String unitName;//单位名称
	private String address;
	private String residentCustId;//居民客户编号
	private String custId;//客户编号
	private String custStatus;//客户状态
	private String addrId;//地址编号
	private String query;
	private CCust cust;
	private String custType;
	
	private String beginOptrateDate;
	private String endOptrateDate;
	private String beginAcctDate;
	private String endAcctDate;
	private String optrId;
	private String feeType;
	private String deptId;
	private String cust_no;
	private String beginInvoice;
	private String endInvoice;
	private String countyId;
	private String mnCustName;
	private String promFeeSn;
	private QueryFeeInfo queryFeeInfo;
	private String paySn;
	
	/**
	 * 所有账期
	 * @return
	 * @throws Exception
	 */
	public String queryAllBillingCycleCfg() throws Exception {
		String query = request.getParameter("query");
		getRoot().setPage(queryCustService.queryAllBillingCycleCfg(query, start, limit));
		return JSON_PAGE;
	}
	
	public String queryBillPrint() throws Exception {
		String billingCycleId = request.getParameter("billingCycleId");
		getRoot().setSimpleObj(queryCustService.queryBillPrint(custId, billingCycleId));
		return JSON_SIMPLEOBJ;
	}
	
	/**套餐账单
	 * @return
	 * @throws Exception
	 */
	public String queryPromPrint() throws Exception {
		getRoot().setSimpleObj(queryCustService.queryPromPrint(custId, promFeeSn));
		return JSON_SIMPLEOBJ;
	}

	public String queryPromFeeByCust() throws Exception {
		getRoot().setPage(queryCustService.queryPromFeeByCust(custId, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 根据客户编号查询套餐信息
	 * @return
	 * @throws Exception
	 */
	public String queryCustPromFee() throws Exception{
		List<CPromFee> promFeeList = queryCustService.queryCustPromFee(custId);
		getRoot().setRecords(promFeeList);
		return JSON_RECORDS;
	}
	
	
	public String queryProvince() throws Exception{
		List<TProvince> list = queryCustService.queryProvince();
		getRoot().setRecords(list);
		return JSON_RECORDS;
	}
	
	/**
	 * 查询当前操作员，上线后未打印发票的客户编号
	 * @return
	 * @throws Exception
	 */
	public String queryUnPrintCustByOptr() throws Exception {
		getRoot().setSimpleObj(queryCustService.queryUnPrintCustByOptr());
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询当前操作员 调账可退后 未退款的客户编号
	 * @return
	 * @throws Exception
	 */
	public String queryUnRefundByOptr() throws Exception {
		getRoot().setSimpleObj(queryCustService.queryUnRefundByOptr());
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询客户的所有发票信息
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public String queryInvoiceByCustId() throws Exception{
		getRoot().setRecords(docService.queryInvoiceByCustId(custId));
		return JSON_RECORDS;
	}

	/**
	 * 查询客户的工单
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public String queryTaskByCustId()throws Exception{
		getRoot().setRecords(docService.queryTaskByCustId(custId));
		return JSON_RECORDS;
	}

	/**
	 * 查询客户的单据 （不包括发票）
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public String queryDocByCustId() throws Exception {
		getRoot().setRecords(docService.queryDocByCustId(custId));
		return JSON_RECORDS;
	}
	
	
	/**
	 * 查询业务确认单.
	 * @return
	 * @throws Exception
	 */
	public String queryBusiConfirmDocByCustId() throws Exception {
		getRoot().setRecords(docService.queryBusiConfirmDocByCustId(custId));
		return JSON_RECORDS;
	}

	/**
	 * 查询客户处理函数
	 * @return
	 * @throws Exception
	 */
	public String searchCust()throws Exception{
		Pager<CCust> p = null;
		if(SystemConstants.MULTIPLE.equals(search_type)){//多条件搜索
			p = queryCustService.complexSearchCust(cust , start,limit);
		}else{
			p = queryCustService.searchCust(search_type, search_value , start,limit);
		}
		getRoot().setPage( p );
		return JSON_PAGE;
	}
	
	/**
	 * 查询可迁移客户.
	 * @return
	 * @throws Exception
	 */
	public String searchTransportableCust()throws Exception{
		Pager<CCust> p = queryCustService.searchTransportableCust(search_value , start,limit);
		getRoot().setPage( p );
		return JSON_PAGE;
	}
	
	/**
	 * 查询可迁移客户.
	 * @return
	 * @throws Exception
	 */
	public String queryWhetherCustOwnfee()throws Exception{
		getRoot().setOthers(queryCustService.queryCustWithOweFeeInfo(custId));
		return JSON_OTHER;
	}
	
	public String searchNonresCust() throws Exception {
		if(cust == null) cust = new CCust();
		cust.setCust_type(SystemConstants.CUST_TYPE_NONRESIDENT);
		if(StringHelper.isNotEmpty(query)){
			cust.setCust_name(query);
		}
		getRoot().setPage( queryCustService.complexSearchCust(cust , start,limit) );
		return JSON_PAGE;
	}
	
	public String searchResidentCust() throws Exception{
		getRoot().setRecords(queryCustService.searchResidentCust(cust));
		return JSON_RECORDS;
	}
	
	public String searchCustById() throws Exception{
		getRoot().setSimpleObj(queryCustService.searchCustById(custId));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 根据单位名称和地址模糊查找对应的单位客户
	 * @return
	 * @throws Exception
	 */
	public String queryUnitByNameAndAddr() throws Exception{
		List<CCust> custList = queryCustService.queryUnitByNameAndAddr(unitName,address);
		getRoot().setRecords(custList);
		return JSON_RECORDS;
	}
	
	/**
	 * 根据模拟大客户名称和地址模糊查找对应的模拟大客户
	 * @return
	 * @throws Exception
	 */
	public String queryMnCustByNameAndAddr() throws Exception{
		List<CCust> custList = queryCustService.queryMnCustByNameAndAddr(mnCustName,address);
		getRoot().setRecords(custList);
		return JSON_RECORDS;
	}


	/**
	 * 查询客户预存费用
	 * @return
	 * @throws Exception
	 */
	public String queryAcctPayFee() throws Exception{
		if (StatusConstants.INVALID.equals(custStatus))
			getRoot().setPage(queryCustService.queryAcctPayFeeHis(residentCustId, queryFeeInfo,start,limit));
		else 
			getRoot().setPage(queryCustService.queryAcctPayFee(residentCustId, queryFeeInfo,start,limit));
		return JSON_PAGE;
	}
	
	/**
	 * 多条件查询客户预存费用
	 * @return
	 * @throws Exception
	 */
	public String queryBatchAcctPayFee() throws Exception {
		
		getRoot().setRecords(
				queryCustService.queryBatchAcctPayFee(beginOptrateDate,
						endOptrateDate, beginAcctDate, endAcctDate, optrId,
						feeType, deptId,cust_no,beginInvoice,endInvoice,countyId));
		return JSON_RECORDS;
	}
	
	/**
	 * 多条件查询客户业务费用
	 * @return
	 * @throws Exception
	 */
	public String queryBatchBusiPayFee() throws Exception {
		if(StringHelper.isEmpty(endOptrateDate))
			endOptrateDate = beginOptrateDate;
		
		getRoot().setRecords(
				queryCustService.queryBatchBusiPayFee(beginOptrateDate,
						endOptrateDate, beginAcctDate, endAcctDate, optrId,
						feeType, deptId,cust_no,beginInvoice,endInvoice,countyId));
		return JSON_RECORDS;
	}

	/**
	 * 查询客户业务费用（受理费+设备销售）
	 * @return
	 * @throws Exception
	 */
	public String queryBusiPayFee() throws Exception{
		getRoot().setPage(queryCustService.queryBusiPayFee(residentCustId, queryFeeInfo, start, limit));
		return JSON_PAGE;
	}

	
	/**
	 * 查询订单
	 * @return
	 * @throws Exception
	 */
	public String queryFeePay() throws Exception{
		getRoot().setPage(queryCustService.queryFeePay(residentCustId,queryFeeInfo, start, limit));
		return JSON_PAGE;
	}
	
	public String queryFeePayDetail() throws Exception{
		getRoot().setRecords(queryCustService.queryFeePayDetail(paySn));
		return JSON_RECORDS;
	}
	

	/**
	 * 根据居民客户的编号查找客户对应的单位信息
	 * @return
	 * @throws Exception
	 */
	public String queryCustUnit() throws Exception{
		List<CCust> custList = queryCustService.queryResidentUnit(residentCustId);
		getRoot().setRecords(custList);
		return JSON_RECORDS;
	}

	/**
	 * 根据居民客户的编号查找单位客户对应的客户信息
	 * @return
	 * @throws Exception
	 */
	public String queryUnitMember() throws Exception{
		List<CCust> custList = queryCustService.queryUnitMember(custId);
		getRoot().setRecords(custList);
		return JSON_RECORDS;
	}

	/**
	 * 根据客户id查找客户属性异动记录
	 * @return
	 * @throws Exception
	 */
	public String queryCustPropChange() throws Exception{
		List<CCustPropChange> propChangeList = queryCustService.queryCustPropChange(custId,custType);
		getRoot().setRecords(propChangeList);
		return JSON_RECORDS;
	}

	/**
	 * 根据客户编号查找办理过的业务流水
	 * @return
	 * @throws Exception
	 */
	public String queryCustDoneCode() throws Exception{
		getRoot().setPage(doneCodeService.queryByCustId(custId, queryFeeInfo,start,limit));
		return JSON_PAGE;
	}

	/**
	 * 查询客户的设备信息
	 * @return
	 * @throws Exception
	 */
	public String queryCustDevices()throws Exception{
		if(StringHelper.isEmpty(custId)){
			return JSON_RECORDS;
		}
		List<CustDeviceDto> custDevices = queryCustService.queryCustDevices(custId);
		getRoot().setRecords(custDevices);
		return JSON_RECORDS;
	}

	/**
	 * 查询客户套餐
	 * @return
	 * @throws Exception
	 */
	public String queryPackages()throws Exception{
		if(StringHelper.isEmpty(custId)){
			return JSON_RECORDS;
		}
		List<CProdDto> prodList = queryCustService.queryCustPackage(custId);
		getRoot().setRecords(prodList);
		return JSON_RECORDS;
	}

	public String getOweFee()throws Exception{
		if(StringHelper.isEmpty(custId)){
			return JSON;
		}
		CAcctAcctitem acct = queryCustService.getOweFee(custId);
		getRoot().setSimpleObj(acct);
		return JSON;
	}


	
	/** 
	 * 查询Ca指令
	 * @return
	 * @throws Exception
	 */
	public String queryCaCommand() throws Exception{
		String type = request.getParameter("type");
		getRoot().setPage(queryCustService.queryCaCommand(type,custId,start,limit));
		return JSON_PAGE;
	}
	
	/** 
	 * 查询vod指令
	 * @return
	 * @throws Exception
	 */
	public String queryVodCommand() throws Exception{
		String type = request.getParameter("type");
		getRoot().setPage(queryCustService.queryVodCommand(type,custId,start,limit));
		return JSON_PAGE;
	}

	public String queryBandCommand() throws Exception{
		getRoot().setPage(queryCustService.queryBandCommand(custId,start,limit));
		return JSON_PAGE;
	}
	
	
	/**
	 * 根据地址编号返回客户数量
	 * @return
	 * @throws Exception
	 */
	public String queryAddressAll() throws Exception {
		getRoot().setSimpleObj(queryCustService.queryAddressAll(addrId, optr));
		return JSON;
	}
	
	/**
	 * 根据条件查询客户信息
	 * @return
	 * @throws Exception
	 */
	public String queryAddrList() throws Exception {
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit = Integer.parseInt(request.getParameter("limit"));
		if(StringHelper.isNotEmpty(custId)){
			//通过一个或多个cust_id查询客户信息
			getRoot().setPage(queryCustService.queryCustAddrByCustIds(custId.split(","),optr,start,limit));
		}else{
			//通过cust的小区，号，楼，单元来查询客户信息
			getRoot().setPage(queryCustService.queryCustAddress(cust,optr,start,limit));
		}
		return JSON_PAGE;
	}
	
	/**
	 * 根据操作员数据规则查询重要客户数量
	 * @return
	 * @throws Exception
	 */
	public String queryImportanceCustNum() throws Exception {
		getRoot().setSimpleObj(queryCustService.queryImportanceCustNum());
		return JSON;
	}
	
	/**
	 * 根据操作员数据规则分页查询重要客户
	 * @return
	 * @throws Exception
	 */
	public String queryImportanceCust()throws Exception{
		getRoot().setPage( queryCustService.queryImportanceCust(start,limit) );
		return JSON_PAGE;
	}
	
	/**
	 * 查询当月账单
	 * @return
	 * @throws Exception
	 */
	public String queryMonthBill() throws Exception{
		//11月28号暂时不更新
//		getRoot().setPage(queryCustService.queryMonthNextBill(custId, queryFeeInfo, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询欠费账单
	 * @return
	 * @throws Exception
	 */
	public String queryCustBill() throws Exception{
		//11月28号暂时不更新
		getRoot().setPage(queryCustService.queryCustBill(custId, queryFeeInfo, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询账单销帐记录.
	 * @return
	 * @throws Exception
	 */
	public String queryCustBillWriteOff() throws Exception{
		getRoot().setRecords(queryCustService.queryCustBillWriteOff(query));
		return JSON_RECORDS;
	}
	
	public String getSearch_type() {
		return search_type;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	public String getSearch_value() {
		return search_value;
	}

	public void setSearch_value(String search_value) {
		this.search_value = search_value;
	}

	public void setQueryCustService(IQueryCustService queryCustService) {
		this.queryCustService = queryCustService;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getResidentCustId() {
		return residentCustId;
	}

	public void setResidentCustId(String residentCustId) {
		this.residentCustId = residentCustId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public void setDocService(IDocService docService) {
		this.docService = docService;
	}

	public void setDoneCodeService(IDoneCodeService doneCodeService) {
		this.doneCodeService = doneCodeService;
	}

	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CCust getCust() {
		return cust;
	}

	public void setCust(CCust cust) {
		this.cust = cust;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public void setBeginOptrateDate(String beginOptrateDate) {
		this.beginOptrateDate = beginOptrateDate;
	}

	public void setEndOptrateDate(String endOptrateDate) {
		this.endOptrateDate = endOptrateDate;
	}

	public void setBeginAcctDate(String beginAcctDate) {
		this.beginAcctDate = beginAcctDate;
	}

	public void setEndAcctDate(String endAcctDate) {
		this.endAcctDate = endAcctDate;
	}

	public void setOptrId(String optrId) {
		this.optrId = optrId;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	public void setBeginInvoice(String beginInvoice) {
		this.beginInvoice = beginInvoice;
	}

	public void setEndInvoice(String endInvoice) {
		this.endInvoice = endInvoice;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public void setMnCustName(String mnCustName) {
		this.mnCustName = mnCustName;
	}

	public void setPromFeeSn(String promFeeSn) {
		this.promFeeSn = promFeeSn;
	}

	public QueryFeeInfo getQueryFeeInfo() {
		return queryFeeInfo;
	}

	public void setQueryFeeInfo(QueryFeeInfo queryFeeInfo) {
		this.queryFeeInfo = queryFeeInfo;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}
	
}
