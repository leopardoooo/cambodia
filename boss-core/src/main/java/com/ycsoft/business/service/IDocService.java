/**
 *
 */
package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TPrintitem;
import com.ycsoft.beans.core.common.CDoneCodeInfo;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.MergeFeeFormDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.dto.core.print.DocDto;
import com.ycsoft.business.dto.core.print.InvoiceFromDto;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;

/**
 * @author liujiaqi
 *
 */
public interface IDocService extends IBaseService{


	/**
	 * 查询客户的所有发票信息
	 * @param custId
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws JDBCException
	 */
	public List<CInvoiceDto> queryInvoiceByCustId(String custId) throws Exception;


	/**
	 * 查询客户的工单
	 * @param custId
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws JDBCException
	 */
	public List<TaskQueryWorkDto> queryTaskByCustId(String custId)throws Exception;

	/**
	 * 查询客户的单据 （不包括发票）
	 * @param custId
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws JDBCException
	 */
	public List<CDoneCodeInfo> queryDocByCustId(String custId)throws Exception;
	
	/**
	 * 查询业务确认单.
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<CDoc> queryBusiConfirmDocByCustId(String custId)throws Exception;

	/**
	 * 查询打印内容，包括打印数据和模板
	 * @param custId 客户编号
	 * @param CDoc 需要打印的记录
	 * @return
	 * @throws Exception
	 */

	public Map<String, ?> queryPrintContent(String custId, CDoc cDoc, String suffix, String invoiceId, String invoiceCode)throws Exception;

	/**
	 * 保存合并打印项
	 * @param lst 封装的参数集合
	 */
	public void savePrintItem(List<MergeFeeFormDto> lst) throws Exception;

	/**
	 * 修改发票号,机打
	 * @throws Exception
	 */
	public void saveChangeInvoice(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String docSn) throws Exception;
	
	/**
	 * 修改手工票的发票.
	 * @param oldInvoice
	 * @param newInvoice
	 * @param fee_sn
	 * @throws Exception
	 */
	public void saveManuallyEditMInvoice(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String fee_sn) throws Exception;
	

	/**
	 * 查询打印确认单，doneCode为空，返回客户未打印的所有确认单
	 * @param custId
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public List<CDoneCodeInfo> queryPrintConfig(String custId, String[] doneCode)throws Exception;

	/**
	 * 根据发票id查询发票详细信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceByInvoiceId(String invoiceId,String invoiceBookId) 
		throws Exception;
	
	/**
	 * 根据发票号查询发票信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> queryInvoiceById(String invoiceId) throws Exception ;

	/**
	 * 保存发票信息
	 * 对新发票做使用标记
	 * 对原发票做作废标记
	 * @param invoiceId 原发票号
	 * @param invoiceCode 原发票代码
	 * @param invoices 新发票信息
	 */
	public boolean saveInvoice(String invoiceId, String invoiceCode, List<InvoiceFromDto> invoices) throws Exception;

	/**
		预付费打印
	 * @param feeSn
	 * @return
	 * @throws JDBCException
	 * @throws Exception 
	 */
	public List<DocDto> queryUnPrintUnitPre(String feeSn) throws  Exception;

	public List<DocDto> queryFeeSnAll(String[] feeSn) throws  Exception;
	/**
	 *  查询打印内容，包括打印数据和模板
	 * @param custId
	 * @param doneCode
	 * @return
	 * @throws Exception 
	 */
	public Map<String, ?> queryPrintContent(String custId, String[] doneCode,CDoc doc) throws  Exception;


	public Map<String, ?> queryConfigPrintContent(String custId,String[] doneCode)throws  Exception;


	public List<DocDto> queryUnPrintInvoice(String custId) throws Exception;
	
	//银行转账打印查询
	public List<DocDto> queryYHZZPrintInvoice(String custId) throws Exception;
	
	/**
	 * 单位客户需要打印记录
	 * @param unitCustId
	 * @return
	 * @throws Exception
	 */
	public List<DocDto> queryUnitUnPrintInvoice(String unitCustId) throws Exception;


	/**
	 * 查询重打的发票信息
	 * @param invoice_id
	 * @param invoice_code
	 * @return
	 * @throws Exception
	 */
	public CInvoiceDto queryReprintInvoice(String invoice_id, String invoice_code)throws Exception;


	public void saveConfigPrint(String[] doneCode)throws Exception;


	/**
	 * 修改发票号 ，手工
	 * @param fees
	 * @throws Exception
	 */
	public void saveChangeInvoice(List<FeeDto> fees) throws Exception;


	public List<PrintItemDto> queryPrintItemByDoc(String docSn, String custType, String invoiceId, String invoiceCode) throws Exception;
	
	public List<PrintItemDto> queryPrintItemByDoneCode(String doneCode) throws Exception;

	/**
	 * 根据打印编号查询
	 * @param printitemId
	 * @return
	 * @throws Exception
	 */
	public List<TPrintitem> queryPrintItemById(String printitemId) throws Exception;
	
	/**
	 * 修改打印项
	 * @param printitem
	 * @throws Exception
	 */
	public void editPrintitem(String printitemId,String printitemMame) throws Exception;


	/**
	 * @param docType
	 * @param doneCode
	 * @param custId
	 * @param docItems
	 * @return
	 */
	public String saveDocItemManual(String docType, String doneCode,
			String custId, String[] docItems)  throws Exception;


	/**
	 * 修改发票状态.
	 * @param invoice_id
	 * @param invoice_code
	 */
	public void editInvoiceStatus(String invoice_id, String invoice_code,String newStatus) throws Exception;
}
