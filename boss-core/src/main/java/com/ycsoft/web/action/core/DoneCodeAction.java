/**
 *
 */
package com.ycsoft.web.action.core;

import org.springframework.stereotype.Controller;

import com.ycsoft.business.service.IDoneCodeService;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * @author YC-SOFT
 *
 */
@Controller
public class DoneCodeAction extends BaseBusiAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -8077193777258641953L;

	private IDoneCodeService doneCodeService;

	private Integer doneCode;
	private String busiCode;
	private String custId;

	/**
	 * 根据流水号获取业务明细
	 * @return
	 * @throws Exception
	 */
	public String getGridDate() throws Exception {
		getRoot().setPage(doneCodeService.getGridDate(doneCode, custId, start, limit));
		return JSON_PAGE;
	}
	/**
	 * 业务回退
	 * @return
	 * @throws Exception
	 */
	public String cancelDoneCode() throws Exception{
		doneCodeService.cancelDoneCode(doneCode);
		return JSON;
	}
	
	public String queryEditFee() throws Exception{
		getRoot().setRecords(doneCodeService.queryEditFee(custId,doneCode, busiCode));
		return JSON_RECORDS;
	}

	public Integer getDoneCode() {
		return doneCode;
	}



	public void setDoneCode(Integer doneCode) {
		this.doneCode = doneCode;
	}



	public IDoneCodeService getDoneCodeService() {
		return doneCodeService;
	}

	public void setDoneCodeService(IDoneCodeService doneCodeService) {
		this.doneCodeService = doneCodeService;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	
}
