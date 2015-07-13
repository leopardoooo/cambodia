package com.ycsoft.sysmanager.web.action.resource;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.ycsoft.beans.core.voucher.CVoucherType;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.business.dto.core.voucher.VoucherDto;
import com.ycsoft.business.dto.core.voucher.VoucherProcureDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.resource.VoucherComponent;

@Controller
public class VoucherAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2586734253971755576L;
	private VoucherComponent voucherComponent;
	private MemoryComponent memoryComponent;
	
	private VoucherDto voucherDto;
	private CVoucherType vtype;
	private VoucherProcureDto voucherProcureDto;
	private Integer start;
	private Integer limit;
	private Integer doneCode;
	private String voucherId;
	private String query;
	
	public String queryMulitVoucher() throws Exception {
		getRoot().setPage(voucherComponent.queryMulitVoucher(voucherDto, start, limit));
		return JSON_PAGE;
	}
	
	public String queryVoucherTypes() throws Exception{
		List<CVoucherType> records = voucherComponent.queryVoucherTypes();
		getRoot().setRecords(records);
		return JSON_RECORDS;
	}
	
	public String editVoucherType() throws Exception{
		voucherComponent.editVoucherType(vtype);
		memoryComponent.addDictSignal("");
		return JSON_SUCCESS;
	}
	
	public String updateVoucherStatus() throws Exception {
		voucherComponent.updateVoucherStatus(voucherId);
		return JSON;
	}
	
	public String updateVoucherProcureStatus() throws Exception {
		voucherComponent.updateVoucherProcureStatus(voucherId.split(","), optr.getCounty_id());
		return JSON;
	}
	
	public String queryProcureByDoneCode() throws Exception {
		getRoot().setPage(voucherComponent.queryProcureByDoneCode(doneCode, start, limit));
		return JSON_PAGE;
	}
	
	public String queryVoucherProcure() throws Exception {
		getRoot().setPage(voucherComponent.queryVoucherProcure(query, optr.getCounty_id(), start, limit));
		return JSON_PAGE;
	}
	
	public String saveVoucherProcure() throws Exception {
		getRoot().setSimpleObj(
				voucherComponent.saveVoucherProcure(voucherProcureDto, optr));
		return JSON;
	}
	
	public String queryFgsByDeptId() throws Exception {
		getRoot().setRecords(voucherComponent.queryFgsByDeptId(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	
	public String saveVoucher() throws Exception {
		boolean flag = voucherComponent.saveVoucher(voucherDto, optr);
		getRoot().setSimpleObj(flag);
		return JSON;
	}
	
	public String queryVoucher() throws Exception {
		getRoot().setPage(voucherComponent.queryVoucher(query, start, limit, optr.getCounty_id()));
		return JSON_PAGE;
	}

	public void setVoucherComponent(VoucherComponent voucherComponent) {
		this.voucherComponent = voucherComponent;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setDoneCode(Integer doneCode) {
		this.doneCode = doneCode;
	}

	public VoucherDto getVoucherDto() {
		return voucherDto;
	}

	public void setVoucherDto(VoucherDto voucherDto) {
		this.voucherDto = voucherDto;
	}

	public VoucherProcureDto getVoucherProcureDto() {
		return voucherProcureDto;
	}

	public void setVoucherProcureDto(VoucherProcureDto voucherProcureDto) {
		this.voucherProcureDto = voucherProcureDto;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public CVoucherType getVtype() {
		return vtype;
	}

	public void setVtype(CVoucherType vtype) {
		this.vtype = vtype;
	}

	public void setMemoryComponent(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
	}
}
