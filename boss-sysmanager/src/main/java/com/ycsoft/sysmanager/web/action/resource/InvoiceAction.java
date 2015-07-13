package com.ycsoft.sysmanager.web.action.resource;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.InvoiceOptrType;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.component.resource.InvoiceComponent;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;

/**
 *
 * @author danjp
 *
 */
public class InvoiceAction extends BaseAction {
	private static final long serialVersionUID = -3473932107774460726L;
	private InvoiceComponent invoiceComponent;

	private InvoiceDto invoiceDto;

	private String startInvoiceId;
	private String endInvoiceId;

	private String startInvoiceBook;
	private String endInvoiceBook;
	private String optrType;
	private String transDepotId;
	private String status;
	private String invoiceCode;

	private String invoiceList;
	
	private String deptId;
	private String invoiceType;
	private String optrId;
	private Integer amount;
	
	public String queryQuotaInvoiceTransDepot() throws Exception {
		getRoot().setRecords(invoiceComponent.queryQuotaInvoiceTransDepot(optr));
		return JSON_RECORDS;
	}
	
	public String queryTransDepot() throws Exception {
		getRoot().setRecords(invoiceComponent.queryTransDepot(optr));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据发票号查询发票
	 * @param invoiceId
	 * @return
	 * @throws JDBCException 
	 */
	public String queryInvoiceById() throws Exception {
		String invoiceId = request.getParameter("invoiceId");
		getRoot().setRecords(invoiceComponent.queryInvoiceByCountyId(invoiceId,optr.getCounty_id()));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询要操作的发票信息
	 * @return
	 * @throws Exception
	 */
	public String queryInoivce()throws Exception {
		if(StringHelper.isEmpty(endInvoiceBook))
			endInvoiceBook = startInvoiceBook;
		if(StringHelper.isEmpty(endInvoiceId))
			endInvoiceId = startInvoiceId;
		
		List<RInvoice>  invoiceList = null;
		if (optrType.equals(InvoiceOptrType.EDITSTATUS.toString())){
			invoiceList = invoiceComponent.queryInvoiceForEdit(optrType, optr,
					startInvoiceBook, endInvoiceBook, startInvoiceId,
					endInvoiceId, status, invoiceType,invoiceCode);
		} else {
			invoiceList = invoiceComponent.queryInvoiceForOptr(optrType, optr,
					startInvoiceBook, endInvoiceBook, startInvoiceId,
					endInvoiceId,invoiceCode,optrId);
		}
		getRoot().setRecords(invoiceList);
		return JSON_RECORDS;
	}
	
	/**
	 * 根据发票id查询发票详细信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public String queryInvoiceByInvoiceId() throws Exception {
		String invoiceId = request.getParameter("invoiceId");
		String invoiceBookId = request.getParameter("invoiceBookId");
		String invoiceCode = request.getParameter("invoiceCode");
		getRoot().setSimpleObj(invoiceComponent.queryInvoiceByInvoiceId(invoiceId,invoiceBookId,invoiceCode));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 多条件查询发票及客户信息
	 * @param invoiceDto
	 * @return
	 * @throws Exception
	 */
	public String queryMulitInvoice() throws Exception {
		getRoot().setPage(invoiceComponent.queryMulitInvoice(invoiceDto,optr, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询当前仓库及以下子仓库
	 * @return
	 * @throws Exception
	 */
	public String queryChildInvoiceDepot() throws Exception {
		List list = invoiceComponent.queryChildInvoiceDepot(optr);
		getRoot().setRecords(TreeBuilder.createTree(list));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存发票操作信息
	 * @return
	 * @throws Exception
	 */
	public String saveInoivceOptr() throws Exception {
		Type type = new TypeToken<List<RInvoice>>(){}.getType();
		List<RInvoice> list = new Gson().fromJson(invoiceList, type);
		if (list != null && list.size()>0){
			if(optrType.equals(InvoiceOptrType.CHECK.toString())){
				this.invoiceComponent.saveCheck(optr, list);
			} else if(optrType.equals(InvoiceOptrType.CANCELCHECK.toString())){
				this.invoiceComponent.saveCancelCheck(optr, list);
			} else if(optrType.equals(InvoiceOptrType.CLOSE.toString())){
				this.invoiceComponent.saveClose(optr, list);
			} else if(optrType.equals(InvoiceOptrType.CANCELCLOSE.toString())){
				this.invoiceComponent.saveCancelClose(optr, list);
			} else if (optrType.equals(InvoiceOptrType.TRANS.toString()) 
					|| optrType.equals(InvoiceOptrType.QUOTA_TRANS.toString())
					|| optrType.equals(InvoiceOptrType.CANCEL_RECEIVE.toString())
					|| optrType.equals(InvoiceOptrType.RECEIVE.toString())) {
				this.invoiceComponent.saveTrans(transDepotId, optrId,optrType, optr, list);
			} else if(optrType.equals(InvoiceOptrType.EDITSTATUS.toString())){
				this.invoiceComponent.saveEditStatus( optr, list,status,invoiceType);
			} else if(optrType.equals(InvoiceOptrType.REFUND.toString())){
				this.invoiceComponent.saveRefund( optr, list,StatusConstants.INVALID);
			} else if(optrType.equals(InvoiceOptrType.QUOTA_LOSS.toString())){
				//定额发票挂失
				this.invoiceComponent.saveQutaLoss(optr, list,
						InvoiceOptrType.QUOTA_LOSS.toString(),
						SystemConstants.BOOLEAN_TRUE);
			} else if(optrType.equals(InvoiceOptrType.QUOTA_CANCEL_LOSS.toString())){
				//定额发票取消挂失
				this.invoiceComponent.saveQutaLoss(optr, list,
						InvoiceOptrType.QUOTA_CANCEL_LOSS.toString(),
						SystemConstants.BOOLEAN_FALSE);
			} else if(optrType.equals(InvoiceOptrType.QUOTA_ADJUST.toString())){
				this.invoiceComponent.saveQutaAdjust(optr, list, amount);
			}
		}
		return JSON;
	}

	/**
	 * 检查当前发票号码段是否存在
	 * @return
	 * @throws Exception
	 */
	public String checkInvoic() throws Exception {
		getRoot().setSuccess(invoiceComponent.checkInvoic(startInvoiceId,endInvoiceId,optr));
		return JSON;
	}

	/**
	 * 发票录入
	 * @return
	 * @throws Exception
	 */
	public String saveInvoiceInput() throws Exception {
		invoiceComponent.saveInvoiceInput(invoiceDto, optr);
		return JSON;
	}
	
	public String getByDeptId() throws Exception {
		getRoot().setRecords(invoiceComponent.getByDeptId(deptId));
		return JSON_RECORDS;
	}

	public void setInvoiceComponent(InvoiceComponent invoiceComponent) {
		this.invoiceComponent = invoiceComponent;
	}

	public InvoiceDto getInvoiceDto() {
		return invoiceDto;
	}

	public void setInvoiceDto(InvoiceDto invoiceDto) {
		this.invoiceDto = invoiceDto;
	}

	public String getStartInvoiceId() {
		return startInvoiceId;
	}

	public void setStartInvoiceId(String startInvoiceId) {
		this.startInvoiceId = startInvoiceId;
	}

	public String getEndInvoiceId() {
		return endInvoiceId;
	}

	public void setEndInvoiceId(String endInvoiceId) {
		this.endInvoiceId = endInvoiceId;
	}

	public String getStartInvoiceBook() {
		return startInvoiceBook;
	}

	public void setStartInvoiceBook(String startInvoiceBook) {
		this.startInvoiceBook = startInvoiceBook;
	}

	public String getEndInvoiceBook() {
		return endInvoiceBook;
	}

	public void setEndInvoiceBook(String endInvoiceBook) {
		this.endInvoiceBook = endInvoiceBook;
	}

	public String getOptrType() {
		return optrType;
	}

	public void setOptrType(String optrType) {
		this.optrType = optrType;
	}

	public String getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(String invoiceList) {
		this.invoiceList = invoiceList;
	}

	public String getTransDepotId() {
		return transDepotId;
	}

	public void setTransDepotId(String transDepotId) {
		this.transDepotId = transDepotId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setOptrId(String optrId) {
		this.optrId = optrId;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
}
