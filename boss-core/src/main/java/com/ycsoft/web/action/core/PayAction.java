package com.ycsoft.web.action.core;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeUnitpre;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.MergeFeeFormDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.dto.core.print.InvoiceFromDto;
import com.ycsoft.business.service.IDocService;
import com.ycsoft.business.service.IPayService;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * 处理支付的功能
 */
@Controller
public class PayAction extends BaseBusiAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -1936997336680925576L;
	private IPayService payService;
	private IDocService docService;

	private CFeePayDto pay ;
	private String[] feeSn ;
	private String[] doneCode ;
	private String doneCodes ;
	private String custId;
	private CDoc doc;
	private String docSn;
	private CFeeUnitpre feeUnitpre;

	private String fee_sn;
	private String feeBusiListStr;
	private int donecode;
	
	private String fee_type;
	private boolean onlyShowInfo=false;
	
	private int newInvoiceFee;
	private String remark;


	private String invoice_code;
	private String invoice_id;
	private String invoice_book_id;
	private String invoice_mode;
	private String doc_type;
	private Integer realPay;
	
	private CInvoiceDto oldInvoice;
	private CInvoiceDto newInvoice;

	private String printType;
	
	private File files;
	
	private String printitemId;
	private String printitemName;
	
	private String payFeesData;//所有缴费数据
	
	//合同款修改账目日期
	private String contractId;
	private String leftAmount;
	
	private String status;
	
	private String cust_id;
	
	private String paySn;
	
	private String[] invoiceIds;
	
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	/**
	 * 查询未支付业务总额
	 * Map<String,Integer>: {"FEE":"未支付总额","CNT":"未支付业务数"}
	 * 当未支付业务数>0时，未支付总额可能=0。
	 * @return
	 * @throws Exception
	 */
	public String queryUnPaySum() throws Exception{
		getRoot().setOthers(payService.queryUnPaySum(cust_id));
		return JSON_OTHER;
	}
	/**
	 * 查询待支付的费用清单和当前汇率
	 * 显示 费用编号 fee_sn,业务名称busi_name,费用名称fee_text,数量(当count不为空，显示count否则显示begin_date(yyyymmdd)+“-”+prod_invalid_date),
	 * 操作员 optr_name,操作时间create_time,金额 real_pay,订单号 prod_sn,X按钮(当prod_sn不为空时显示)
	 * @return
	 * @throws Exception
	 */
	public String queryUnPayDetail() throws Exception{
		//费用信息
		getRoot().setRecords(payService.queryUnPayDetail(cust_id));
		Map<String, Integer> feeMap = payService.queryUnPaySum(cust_id);
		feeMap.put("EXC", payService.queryExchage());
		//汇率
		getRoot().setSimpleObj(feeMap);
		return JSON;
	}
	
	public String queryBaseFeeData() throws Exception {
		//汇率
		getRoot().setSimpleObj(payService.queryExchage());
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 保存支付
	 * @return
	 * @throws Exception
	 */
	public String savePayNew() throws Exception{
		payService.savePay(pay,feeSn);
		return JSON_SUCCESS;
	}
	
	/**
	 * 查询支付记录的取消信息(发票信息)
	 * @param paySn
	 * @throws Exception 
	 */
	public String queryPayToCancel() throws Exception{
		getRoot().setSimpleObj(payService.queryPayToCancel(paySn));
		return JSON_SIMPLEOBJ;
		
	}
	/**
	 * 回退支付记录（含处理缴费记录、发票、订单支付状态和订单费用明细）
	 */
	public String canclePay()throws Exception{
		payService.saveCanclePay(paySn, invoiceIds);
		return JSON_SUCCESS;
	}
	
	/**
	 * 取消一个费用
	 * 当onlyShowInfo=true只返回提示，但不执行取消
	 * fee_type = c_fee.fee_type
	 * @return
	 * @throws Exception
	 */
	public String cancelUnPayFee() throws Exception{
		getRoot().setSimpleObj(payService.saveCancelUnPayFee(fee_sn,fee_type,onlyShowInfo));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 保存确认单打印
	 * @return
	 * @throws Exception
	 */
	public String saveConfigPrint() throws Exception{
		docService.saveConfigPrint(doneCode);
		return JSON;
	}

	/**
	 *  查询预收费信息
	 * @return
	 * @throws Exception
	 */
	public String queryUnitpreBusiFee() throws Exception {
		getRoot().setRecords(payService.queryUnitpreBusiFee());
		return JSON_RECORDS;
	}

	/**
	 * 预付款
	 * @return
	 * @throws Exception
	 */
	public String saveFeeUnitpre() throws Exception {
		payService.saveFeeUnitpre(feeUnitpre, optr);
		return JSON;
	}

	/**
	 * 查询同一地区类的所有预付款
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public String queryFeeUnitpre() throws Exception {
		getRoot().setRecords(payService.queryFeeUnitpre(optr.getCounty_id()));
		return JSON_RECORDS;
	}


	/**
	 * 校验发票是否可用
	 * @return
	 * @throws Exception
	 */
	public String checkInvoice()throws Exception{
		getRoot().setRecords(payService.checkInvoice(invoice_id,doc_type,invoice_mode));
		return JSON_RECORDS;
	}
	
	public String queryInvoice()throws Exception{
		getRoot().setRecords(payService.queryFeeByInvoice(invoice_code,invoice_id,custId));
		return JSON_RECORDS;
	}
	
	/**
	 * 修改发票状态.
	 * @return
	 * @throws Exception
	 */
	public String editInvoiceStatus()throws Exception{
		docService.editInvoiceStatus(invoice_id,invoice_code,status);
		return JSON_SUCCESS;
	}

	/**
	 * 修改发票
	 */
	public String saveChangeInvoice() throws Exception {
		String feesData = request.getParameter("feedata");
		
		if (feesData == null){
			docService.saveChangeInvoice(oldInvoice,newInvoice,docSn);
		}else{
			Type type = new TypeToken<List<FeeDto>>(){}.getType();
			Gson gson = new Gson();
			List<FeeDto> fees = gson.fromJson(feesData, type);
			docService.saveChangeInvoice(fees);
		}
			
		return JSON_SUCCESS;
	}
	
	/**
	 * 修改发票
	 */
	public String manuallyEditMInvoice() throws Exception {
		docService.saveManuallyEditMInvoice(oldInvoice,newInvoice,fee_sn);
		return JSON_SUCCESS;
	}

	/**
	 * 冲正
	 */
	public String saveCancelFee() throws Exception {
		payService.saveCancelFee(fee_sn);
		return JSON_SUCCESS;
	}

	/**
	 * 修改费用
	 */
	public String editFee() throws Exception {
		List<FeeBusiFormDto> busiFeeList = null;
		if(StringHelper.isNotEmpty(feeBusiListStr)){
			Type t = new TypeToken<List<FeeBusiFormDto>>(){}.getType();
			busiFeeList = JsonHelper.gson.fromJson( feeBusiListStr , t);
		}
		payService.editFee(donecode, busiFeeList);
		return JSON_SUCCESS;
	}
	
	public String changeFeelistInvoice() throws Exception {
		String feelistId = request.getParameter("feelist_id");
		String feelistCode = request.getParameter("feelist_code");
		String feelistBookId = request.getParameter("feelist_book_id");
		payService.changeFeelistInvoice(custId, feelistId, feelistCode,
				feelistBookId, invoice_id, invoice_code, remark);
		return JSON_SUCCESS;
	}
	
	public String invalidFeeListInvoice() throws Exception {
		payService.invalidFeeListInvoice(donecode);
		return JSON;
	}
	
	/**
	 * 单据页面中添加发票作废按钮.
	 * 只有集团用户发票开错，牵扯到大批的用户时.
	 * @return
	 * @throws Exception
	 */
	public String invalidInvoice() throws Exception {
		payService.invalidInvoice(invoice_id,invoice_code,invoice_book_id);
		return JSON;
	}
	
	/**
	 * 修改备注
	 */
	public String editRemark() throws Exception {
		payService.editRemark(donecode, remark);
		return JSON_SUCCESS;
	}

	/**
	 * 查询需要支付的费用项
	 */
	public String queryUnPay()throws Exception{
		if(null == custId){
			throw new ActionException("客户编号不能为空");
		}
		getRoot().setRecords( payService.queryUnPayFees(custId) );
		return JSON_RECORDS ;
	}

	/**
	 * 保存支付信息
	 * @throws Exception
	 */
	public String savePay()throws Exception{
		if(null == request.getParameter("notMerge")){
			payService.savePayAndMerge(pay, feeSn);
		}else{
			payService.savePay(pay, feeSn);
		}
		return JSON;
	}

	/**
	 * 根据前台传递的feeSn
	 * @throws Excpetion
	 */
	public String queryFeeInfo()throws Exception{
		String feeSn = request.getParameter("feeSn");
		if(null == feeSn){
			throw new ActionException("费用项编号或类型不能为空!");
		}
		getRoot().setSimpleObj(payService.queryFeeInfo(feeSn));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 指定客户编号。查询客户下未合并的费用项
	 * @return
	 * @throws Exception
	 */
	public String queryUnMergeFees()throws Exception{
		if(null != custId) {
			getRoot().setRecords(payService.queryUnMergeFees(custId));
		}
		return JSON_RECORDS;
	}

	/**
	 * 保存合并的费用
	 * @return
	 * @throws Exception
	 */
	public String saveMergeFee()throws Exception{
		String mergeStr = request.getParameter("merges");
		Type t = new TypeToken<List<MergeFeeFormDto>>(){}.getType();
		List<MergeFeeFormDto> lst = JsonHelper.gson.fromJson( mergeStr , t);
		docService.savePrintItem(lst);

		return JSON;
	}

	/**
	 * 查询打印记录
	 * @return
	 * @throws Exception
	 */
	public String queryPrintRecord() throws Exception {
		if ("feesn".equals(printType))
			//非营业费
			getRoot().setRecords(docService.queryUnPrintUnitPre(fee_sn));
		else if ("through".equals(printType)) {
			getRoot().setRecords(docService.queryUnPrintInvoice(custId));
		}else if ("unit".equals(printType)) {
			getRoot().setRecords(docService.queryUnitUnPrintInvoice(custId));
		}else if("feesnAll".equals(printType)){//充值卡费
			getRoot().setRecords(docService.queryFeeSnAll(fee_sn.split(",")));
		}else if ("bank".equals(printType)){//银行打印
			getRoot().setRecords(docService.queryYHZZPrintInvoice(custId));
		} else {
			//重打
			List<CInvoiceDto> list = new ArrayList<CInvoiceDto>();
			list.add(docService.queryReprintInvoice(invoice_id, invoice_code));
			getRoot().setRecords(list);
		}
		return JSON_RECORDS;
	}
	
	//保存手工设置发票打印内容
	public String saveDocItemManual() throws Exception {
		String docType = request.getParameter("docType");
		String doneCode = request.getParameter("doneCode");
		String custId = request.getParameter("custId");
		String[] docItems = request.getParameterValues("docItems");
		String doc_sn = docService.saveDocItemManual(docType,doneCode,custId,docItems); 
		getRoot().setSimpleObj(doc_sn);
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询确认单记录
	 * @return
	 * @throws Exception
	 */
	public String queryConfigPrintRecord()throws Exception{
		String[] ds = null;
		if (StringHelper.isNotEmpty(doneCodes))
			ds = doneCodes.split(",");
		getRoot().setRecords( docService.queryPrintConfig(custId,ds));
		return JSON_RECORDS;
	}
	
	public String queryPrintItem()throws Exception{
		String custType = request.getParameter("custType");
		String invoiceId = request.getParameter("invoiceId");
		String invoiceCode = request.getParameter("invoiceCode");
		getRoot().setRecords( docService.queryPrintItemByDoc(docSn,custType,invoiceId, invoiceCode));
		return JSON_RECORDS;
	}
	
	public String queryUnPrintItem() throws Exception{
		String doneCode = request.getParameter("done_code");
		getRoot().setRecords( docService.queryPrintItemByDoneCode(doneCode));
		return JSON_RECORDS;
	}

	/**
	 * 获取打印数据，包括打印内容及模板数据（发票）
	 * @return
	 * @throws Exception
	 */
	public String initPrint()throws Exception{
		if(null == custId || null == doc){
			throw new ActionException("客户编号或单据记录不能为空!");
		}
		String invoiceId = request.getParameter("invoiceId");
		String invoiceCode = request.getParameter("invoiceCode");
		getRoot().setOthers(docService.queryPrintContent(custId, doc, getPrintSuffix(), invoiceId, invoiceCode));
		return JSON_OTHER;
	}
	/**
	 * 获取打印数据，包括打印内容及模板数据（发票）
	 * @return
	 * @throws Exception
	 */
	public String initConfigPrint()throws Exception{
		if(null == custId || null == doneCode){
			throw new ActionException("客户编号或单据记录不能为空!");
		}
		getRoot().setOthers(docService.queryConfigPrintContent(custId,doneCode));
		return JSON_OTHER;
	}
	
	/**
	 * 获取打印数据，包括打印内容及模板数据（单据）
	 * @return
	 * @throws Exception
	 */
	public String printDoc()throws Exception{
		if(null == custId || null == doneCode){
			throw new ActionException("客户编号或单据记录不能为空!");
		}
		getRoot().setOthers(docService.queryPrintContent(custId,doc, getPrintSuffix(), null, null));
		return JSON_OTHER;
	}
	
	private String getPrintSuffix(){
		String suffix = request.getParameter("suffix");
		return StringUtils.isEmpty(suffix) ? "" : suffix + "-";
	}

	/**
	 * 冲正
	 * @return
	 * @throws Exception
	 */
	public String reversePay()throws Exception{
		String sn = request.getParameter("sn");
		String type = request.getParameter("type");
		if(null == sn ||null==type ){
			throw new ActionException("费用编号不能为空!");
		}

		payService.saveCancelFee(sn);
		return JSON_SUCCESS;
	}
	
	/**
	 * 根据发票id查询发票详细信息
	 * @return
	 * @throws Exception
	 */
	public String queryInvoiceByInvoiceId() throws Exception {
		getRoot().setSimpleObj(docService.queryInvoiceByInvoiceId(invoice_id,invoice_code));
		return JSON_SIMPLEOBJ;
	}
	
	public String queryInvoiceById() throws Exception {
		getRoot().setRecords(docService.queryInvoiceById(invoice_id));
		return JSON_RECORDS;
	}

	/**
	 * 保存发票信息
	 * @return
	 * @throws Exception
	 */
	public String saveInvoiceInfo()throws Exception{
		String param = request.getParameter("invoiceInfo");
		Type t = new TypeToken<List<InvoiceFromDto>>(){}.getType();
		List<InvoiceFromDto> invoices = JsonHelper.gson.fromJson( param , t);
		getRoot().setSimpleObj(docService.saveInvoice(invoice_id,invoice_code,invoices));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 退押金
	 * @return
	 * @throws Exception
	 */
	public String saveDepositUnPay()throws Exception{
		String feeSn = request.getParameter("feeSn");
		if (null == feeSn) {
			throw new ActionException("费用编号不能为空!");
		}
		payService.saveDepositUnPay(feeSn);
		return JSON_SUCCESS;
	}

	/**
	 * 刷新前台费用信息。
	 * @return
	 * @throws Exception
	 */
	public String feeView()throws Exception{
		if(null == custId ){
			throw new ActionException("客户编号不能为空!");
		}
		Map<String,Object> map = payService.queryFeeView(custId);
		getRoot().setSimpleObj( map );
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 修改出票方式
	 */
	public String editInvoiceMode() throws Exception{
		String feeSn = request.getParameter("feeSn");
		payService.editInvoiceMode(feeSn,invoice_mode,oldInvoice,newInvoice,realPay);
		return JSON;
	}
	
	/**
	 * 定额发票调账
	 * @return
	 * @throws Exception
	 */
	public String editInvoiceFee() throws Exception{
		payService.editInvoiceFee(fee_sn,newInvoiceFee,remark);
		return JSON;
	}
	
	/**
	 * 修改账务日期
	 * @return
	 * @throws Exception
	 */
	public String editAcctDate() throws Exception{
		String feeSn = request.getParameter("feeSn");
		String newAcctDate = request.getParameter("acctDate");
		String oldAcctDate = request.getParameter("oldAcctDate");
		
		payService.editAcctDate(feeSn,newAcctDate,oldAcctDate,contractId,leftAmount);
		return JSON;
	}
	
	/**
	 * 批量修改账务日期
	 * @return
	 * @throws Exception
	 */
	public String batchEditAcctDate() throws Exception{
		String acctDate = request.getParameter("acctDate");
		String feeListStr = request.getParameter("feeList");
		Type t = new TypeToken<List<CFee>>(){}.getType();
		List<CFee> feeList = JsonHelper.gson.fromJson( feeListStr , t);
		payService.batchEditAcctDate(feeList,acctDate);
		return JSON;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadFeeExcel() throws Exception{
		String[] colName = new String[]{"cust_no","user_id","prod_sn","cust_name","cust_addr","user_type",
				"prod_name","fee","present_fee","invoice_id","invoice_code"};
		List<PayDto> feeList = FileHelper.fileToBean(files, colName, PayDto.class);
		
		try{
			payService.queryValidFeeList(feeList);
		}catch(Exception e){
			String msg = e.getMessage();
			return retrunNone(msg);
		}
		
		
		return returnList(feeList);
	}
	
	/**
	 * 加载移动账单
	 * @return
	 * @throws Exception
	 */
	public String loadMobileBillExcel() throws Exception{
		String[] colName = new String[]{"cust_no","cust_name","user_id","acctitem_id",
				"prod_name","fee","done_code","create_time","invoice_id","invoice_code"};
		List<PayDto> billList = FileHelper.fileToBean(files, colName, PayDto.class);
		
		try{
			payService.queryValidFeeList(billList);
		}catch(Exception e){
			String msg = e.getMessage();
			return retrunNone(msg);
		}
		
		
		return returnList(billList);
	}
	
	/**
	 * 移动结账
	 * @return
	 * @throws Exception
	 */
	public String checkMobileBill() throws Exception{
		String payFeesData = request.getParameter("payFeesData");

		Type type = new TypeToken<List<PayDto>>(){}.getType();
		Gson gson = new Gson();
		List<PayDto> payList = gson.fromJson(payFeesData, type);
		
		getRoot().setSimpleObj(payService.saveCheckMobileBill(payList));
		
		return JSON;
	}
	
	/**
	 * 查询打印项
	 * @return
	 * @throws Exception
	 */
	public String queryPrintitem() throws Exception{
		getRoot().setRecords(docService.queryPrintItemById(printitemId));
		return JSON_RECORDS;
	}
	
	/**
	 * 修改打印项
	 * @return
	 * @throws Exception
	 */
	public String eidtPrintitem() throws Exception{
		docService.editPrintitem(printitemId,printitemName);
		return JSON;
	}
	
	public String editBusiOptr() throws Exception {
		String busi_optr_id = request.getParameter("busi_optr_id");
		String old_busi_optr_id = request.getParameter("old_busi_optr_id");
		getRoot().setSimpleObj(payService.editBusiOptr(fee_sn, busi_optr_id,old_busi_optr_id));
		request.getSession().setAttribute(Environment.CURRENT_BUSI_OPTR_ID, busi_optr_id);
		return JSON_SIMPLEOBJ;
	}


	
	/**
	 * 打印标记
	 * @return
	 * @throws Exception
	 */
	public String savePrintStatus() throws Exception {
		payService.savePrintStatus(fee_sn);
		return JSON;
	}

	/**
	 * 取消打印标记
	 * @return status
	 * @throws Exception
	 */
	public String saveCancelPrintStatus() throws Exception {
		payService.saveCancelPrintStatus(fee_sn);
		return JSON;
	}
	

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}


	public CFeePayDto getPay() {
		return pay;
	}

	public void setPay(CFeePayDto pay) {
		this.pay = pay;
	}

	public String[] getFeeSn() {
		return feeSn;
	}

	public void setFeeSn(String[] feeSn) {
		this.feeSn = feeSn;
	}

	public CDoc getDoc() {
		return doc;
	}

	public void setDoc(CDoc doc) {
		this.doc = doc;
	}


	public void setPayService(IPayService payService) {
		this.payService = payService;
	}

	public void setDocService(IDocService docService) {
		this.docService = docService;
	}

	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}

	public void setFee_type(String fee_type){
		this.fee_type=fee_type;
	}

	/**
	 * @return the invoice_code
	 */
	public String getInvoice_code() {
		return invoice_code;
	}

	/**
	 * @param invoice_code the invoice_code to set
	 */
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	/**
	 * @return the invoice_id
	 */
	public String getInvoice_id() {
		return invoice_id;
	}

	/**
	 * @param invoice_id the invoice_id to set
	 */
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	/**
	 * @param invoice_mode the invoice_mode to set
	 */
	public void setInvoice_mode(String invoice_mode) {
		this.invoice_mode = invoice_mode;
	}


	/**
	 * @return the feeUnitpre
	 */
	public CFeeUnitpre getFeeUnitpre() {
		return feeUnitpre;
	}

	/**
	 * @param feeUnitpre the feeUnitpre to set
	 */
	public void setFeeUnitpre(CFeeUnitpre feeUnitpre) {
		this.feeUnitpre = feeUnitpre;
	}


	/**
	 * @return the doneCode
	 */
	public String[] getDoneCode() {
		return doneCode;
	}

	/**
	 * @param doneCode the doneCode to set
	 */
	public void setDoneCode(String[] doneCode) {
		this.doneCode = doneCode;
	}

	public void setFeeBusiListStr(String feeBusiListStr) {
		this.feeBusiListStr = feeBusiListStr;
	}

	public void setDonecode(int donecode) {
		this.donecode = donecode;
	}

	/**
	 * @return the printType
	 */
	public String getPrintType() {
		return printType;
	}

	/**
	 * @param printType the printType to set
	 */
	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public CInvoiceDto getOldInvoice() {
		return oldInvoice;
	}

	public void setOldInvoice(CInvoiceDto oldInvoice) {
		this.oldInvoice = oldInvoice;
	}

	public CInvoiceDto getNewInvoice() {
		return newInvoice;
	}

	public void setNewInvoice(CInvoiceDto newInvoice) {
		this.newInvoice = newInvoice;
	}



	/**
	 * @return the doneCodes
	 */
	public String getDoneCodes() {
		return doneCodes;
	}

	/**
	 * @param doneCodes the doneCodes to set
	 */
	public void setDoneCodes(String doneCodes) {
		this.doneCodes = doneCodes;
	}

	/**
	 * @return the docSn
	 */
	public String getDocSn() {
		return docSn;
	}

	/**
	 * @param docSn the docSn to set
	 */
	public void setDocSn(String docSn) {
		this.docSn = docSn;
	}

	/**
	 * @param invoice_book_id the invoice_book_id to set
	 */
	public void setInvoice_book_id(String invoice_book_id) {
		this.invoice_book_id = invoice_book_id;
	}

	/**
	 * @param realPay the realPay to set
	 */
	public void setRealPay(Integer realPay) {
		this.realPay = realPay;
	}

	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}

	public void setPrintitemId(String printitemId) {
		this.printitemId = printitemId;
	}

	public void setPrintitemName(String printitemName) {
		this.printitemName = printitemName;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	public void setNewInvoiceFee(int newInvoiceFee) {
		this.newInvoiceFee = newInvoiceFee;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getLeftAmount() {
		return leftAmount;
	}

	public void setLeftAmount(String leftAmount) {
		this.leftAmount = leftAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public void setOnlyShowInfo(boolean onlyShowInfo) {
		this.onlyShowInfo = onlyShowInfo;
	}
	public void setPayFeesData(String payFeesData) {
		this.payFeesData = payFeesData;
	}
	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}
	public String[] getInvoiceIds() {
		return invoiceIds;
	}
	public void setInvoiceIds(String[] invoiceIds) {
		this.invoiceIds = invoiceIds;
	}
	
}
