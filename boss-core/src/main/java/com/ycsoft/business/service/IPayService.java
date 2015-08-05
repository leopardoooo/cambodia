/**
 *
 */
package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeUnitpre;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.MergeFeeDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.commons.exception.ServicesException;


/**
 *
 * 营业业务保存服务
 * @author YC-SOFT
 *
 */
public interface IPayService extends IBaseService{

	public Integer queryUnPaySum(String cust_id) throws Exception;
	
	public List<FeeDto> queryUnPayDetail(String cust_id)throws Exception;
	
	public void savePay(String cust_id) throws Exception;
	
	public Integer queryExchage() throws ServicesException;
	/**
	 * 根据用户类型查询一次性费用信息
	 * @param feeType 预收费
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryUnitpreBusiFee() throws Exception;

	/**
	 * 预付款
	 * @param dto
	 * @param optr
	 * @throws Exception
	 */
	public void saveFeeUnitpre(CFeeUnitpre cFeeUnitpre,SOptr optr) throws Exception;

	/**
	 * 查询同一地区类的所有预付款
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CFeeUnitpre> queryFeeUnitpre(String countyId) throws Exception;

	/**
	 * 保存支付信息
	 * @param pay 支付信息
	 * @param feeSn 支付的费用项
	 */
	public void savePay(CFeePayDto pay, String[] feeSn)throws Exception;

	/**
	 * 冲正缴费记录
	 * @param feeSn		费用编号
	 * @param feeType	费用类型
	 * @throws Exception
	 */
	public void saveCancelFee(String feeSn)throws Exception;

	/**
	 * 修改业务收费
	 * @param doneCode
	 * @param feeList  FeeBusiFormDto:fee_type,fee_id,real_pay必须有值
	 * @throws Exception
	 */
	public void editFee(int doneCode,List<FeeBusiFormDto> feeList)throws Exception;

	/**
	 * 修改流水备注
	 * @param doneCode
	 * @param remark
	 * @throws Exception
	 */
	public void editRemark(int doneCode,String remark) throws Exception;
	
	/**
	 * 保存支付信息并合并发票项
	 * @param pay 支付信息
	 * @param feeSn 支付的费用项
	 */
	public void savePayAndMerge(CFeePayDto pay, String[] feeSn)throws Exception;

	/**
	 * 根据费用编号及费用类型查询费用项信息
	 * @param feeSn
	 * @param feeType
	 * @return
	 * @throws Exception
	 */
	public CFee queryFeeInfo(String feeSn) throws Exception ;

	/**
	 * 查询指定客户下未支付的费用项
	 * @throws Excetpion
	 */
	public List<FeeDto> queryUnPayFees(String custId)throws Exception;

	/**
	 * 查询指定客户下未合并的费用项
	 * @param custId 客户编号
	 */
	public List<MergeFeeDto> queryUnMergeFees(String custId)throws Exception ;

	/**
	 * 根据客户编号，查询客户为支付的费用记录数
	 * @param custId
	 * @return
	 */
	public Map<String, Object> queryFeeView(String custId)throws Exception;


	/**
	 * 验证发票
	 * @param invoiceCode
	 * @param invoiceId
	 * @param invoiceMode
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> checkInvoice(String invoiceId,String docType,String invoiceMode)throws Exception;


	/**
	 * 根据发票号码和Id查询相应记录
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public List<CFee> queryFeeByInvoice(String invoiceCode, String invoiceId,String custId)throws Exception;
	
	/**
	 * 退押金
	 * @param feeSn
	 * @return
	 */
	public void saveDepositUnPay(String feeSn) throws Exception;

	/**
	 * 修改发票出票方式
	 * @param feeSn
	 * @param invoiceMode
	 * @param invoice_code 
	 * @param invoice_book_id 
	 * @param invoice_id 
	 * @throws Exception
	 */
	public void editInvoiceMode(String feeSn, String invoiceMode,CInvoiceDto oldInvoice,CInvoiceDto newInvoice,Integer realPay) throws Exception;

	public void editAcctDate(String feeSn, String newAcctDate, String oldAcctDate, String contractId, String leftAmount) throws Exception;
	
	/**
	 * 批量修改账目日期
	 * @param feeSns
	 * @param acctDate
	 * @throws Exception
	 */
	public void batchEditAcctDate(List<CFee> feeList, String acctDate) throws Exception;
	

	public void queryValidFeeList(List<PayDto> feeList) throws Exception;
	/**
	 * 银行缴费日终扎账查询缴费信息
	 * @param startTransCode
	 * @param endTransCode
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CFee> queryFeeByBankTransCode(String startTransCode, String endTransCode,String countyId) throws Exception;
 
	/**
	 * 修改业务员
	 * @param feeSn
	 * @param busiOptrId
	 * @return
	 * @throws Exception
	 */
	public CFee editBusiOptr(String feeSn,String busiOptrId,String oldBusiOptrId) throws Exception ;

	/**
	 * 移动结帐
	 * @param payList
	 */
	public int saveCheckMobileBill(List<PayDto> payList) throws Exception;

	/**
	 * @param feeSn
	 * @param newInvoiceFee
	 * @param remark
	 */
	public void editInvoiceFee(String feeSn, int newInvoiceFee, String remark) throws Exception;
	
	/**
	 * 收费清单更换发票
	 * @param custId
	 * @param feelistId
	 * @param feelistCode
	 * @param feelistBookId
	 * @param invoiceId
	 * @param invoiceCode
	 * @param remark
	 * @throws Exception
	 */
	public void changeFeelistInvoice(String custId, String feelistId, String feelistCode,
			String feelistBookId, String invoiceId, String invoiceCode, String remark) throws Exception;
	
	/**
	 * 正式发票作废
	 * @param doneCode
	 * @throws Exception
	 */
	public void invalidFeeListInvoice(Integer doneCode) throws Exception;

	/**
	 * @param fee_sn
	 */
	public void savePrintStatus(String fee_sn) throws Exception;

	/**
	 * @param fee_sn
	 */
	public void saveCancelPrintStatus(String fee_sn) throws Exception;

	/**
	 * @param invoice_id
	 * @param invoice_code
	 * @param invoice_book_id
	 */
	public void invalidInvoice(String invoice_id, String invoice_code,
			String invoice_book_id)throws Exception;
}
