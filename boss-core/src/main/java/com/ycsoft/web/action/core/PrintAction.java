/*
 * @(#) PrintAction.java 1.0.0 Aug 4, 2011 3:50:27 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ycsoft.web.action.core;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.ycsoft.business.cache.PrintContentConfiguration;
import com.ycsoft.business.service.impl.DocService;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;



/**
 * 打印控制器 
 *
 * @author allex
 * @since 1.0
 */
@Controller
public class PrintAction extends BaseBusiAction{

	private String cust_id;
	private DocService docService;
	
	private String custId;
	private String[] doneCodes;
	private String docSn;
	private String type;
	private String done_date;

//	public String queryUnFaxPrint()throws Exception{
//		int count = docService.queryUnFaxPrint();
//		getRoot().setSimpleObj(count);
//		return JSON_SIMPLEOBJ;
//	}
	
	/**
	 * 查询业务受理单
	 * @return
	 * @throws Exception
	 */
	public String queryDoc()throws Exception{
		Assert.notNull(cust_id);
		Map<String, Object> data = null;
		if(StringHelper.isEmpty(docSn)){
			data = docService.queryServiceDoc(cust_id);
		}else{
			data = docService.queryServiceRepeatDoc(cust_id, docSn);
		}
		getRoot().setOthers(data);
		return JSON_OTHER;
	}
	
	public String saveDoc() throws Exception {
		docService.saveDoc(custId,docSn, doneCodes);
		return JSON_SUCCESS;
	}
	/**
	 * 重装模板
	 * @return
	 * @throws Exception
	 */
	public String reloadTemplate()throws Exception{
		String root = request.getSession().getServletContext().getRealPath("/");
		PrintContentConfiguration.configure(root);
		return JSON_SUCCESS;
	}
	
//	public String queryPrintBatchAtv() throws Exception {
//		getRoot().setRecords(docService.queryPrintBatchAtv(done_date));
//		return JSON_RECORDS;
//	}
//	
//	public String queryRepeatBatchAtv() throws Exception {
//		getRoot().setRecords(docService.queryRepeatBatchAtv(Integer.parseInt(docSn),type));
//		return JSON_RECORDS;
//	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public void setDocService(DocService docService) {
		this.docService = docService;
	}
	
	public void setCustId(String custId) {
		this.custId = custId;
	}

	public void setDoneCodes(String[] doneCodes) {
		this.doneCodes = doneCodes;
	}

	public void setDocSn(String docSn) {
		this.docSn = docSn;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDone_date(String done_date) {
		this.done_date = done_date;
	}
	
}
