/**
 * 
 */
package com.ycsoft.web.action.core;

import org.springframework.stereotype.Controller;

import com.ycsoft.business.service.impl.AcctService;
import com.ycsoft.business.service.impl.CustService;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

@Controller
public class BankAction extends BaseBusiAction {
	
	private String acctPayType;//支付方式
	
	private String custId; //客户编号
	private String optionType;
	private AcctService acctService;
	private CustService custService;

	/**
	 * 暂停卡扣
	 * @return
	 * @throws Exception
	 */
	public String bankStop()throws Exception{
		custService.saveBankStop();
		return JSON;
	}
	
	/**
	 * 恢复卡扣
	 * @return
	 * @throws Exception
	 */
	public String bankResume()throws Exception{
		custService.saveBankResume();
		return JSON;
	}
	
	/**
	 * 取消签约
	 * @return
	 * @throws Exception
	 */
	public String cancelSign() throws Exception{
		acctService.saveRemoveSignBank(acctPayType,DateHelper.now());
		return JSON;
	}


	public String getAcctPayType() {
		return acctPayType;
	}

	public void setAcctPayType(String acctPayType) {
		this.acctPayType = acctPayType;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	public void setAcctService(AcctService acctService) {
		this.acctService = acctService;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	/**
	 * @param custService the custService to set
	 */
	public void setCustService(CustService custService) {
		this.custService = custService;
	}

 
	
}
