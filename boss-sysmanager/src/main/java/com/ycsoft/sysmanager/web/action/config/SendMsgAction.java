package com.ycsoft.sysmanager.web.action.config;

import java.util.List;

import com.ycsoft.beans.config.JSendMsg;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.config.SendMsgComponent;
import com.ycsoft.sysmanager.component.prod.ProdComponent;

public class SendMsgAction  extends BaseAction {
	private String query;
	private String comboQueryText;
	private String records;
	private String addrRecord;
	private JSendMsg sendMsg;
	private String doneId;
	private String countyId;
	private String areaId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8528671698233165436L;
	
	private SendMsgComponent sendMsgComponent;
	private ProdComponent prodComponent;

	/**
	 * 地市树
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getCountyTree() throws Exception{
		String[] type= {"CHOOSE"};
		//TODO 待确认，是否根据操作员的地区权限来
		List<TreeNode> prodtree = TreeBuilder.createTree((List)prodComponent.getCountyTree(optr,type,null));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	
	public String queryMsg()throws Exception{ 
		getRoot().setPage( sendMsgComponent.queryMsg(start, limit,query,optr.getCounty_id(),SystemConstants.TASK_CODE_CJ));
		return JSON_PAGE;  
	}

	public String queryStop()throws Exception{
		getRoot().setPage( sendMsgComponent.queryMsg(start, limit,query,optr.getCounty_id(),SystemConstants.TASK_CODE_TJ));
		return JSON_PAGE;  
	}
	
	public String queryMsgByJob() throws Exception{
		getRoot().setSimpleObj(sendMsgComponent.queryMsgByJob(doneId));
		return JSON_SIMPLEOBJ;
	}
	
	public String getTerminalType(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.TERMINAL_TYPE));
		return JSON_RECORDS;
	}
	public String getCustType(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.CUST_TYPE));
		return JSON_RECORDS;
	}
	public String getCustClass(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.CUST_CLASS));
		return JSON_RECORDS;
	}
	public String getCustColony(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.CUST_COLONY));
		return JSON_RECORDS;
	}
	public String getStopType(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.STOP_TYPE));
		return JSON_RECORDS;
	}
	public String getSendType(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.JOB_SEND_TYPE));
		return JSON_RECORDS;
	}
	
	
	public String getAddrByName() throws Exception{
		String county="";
		if(StringHelper.isNotEmpty(countyId)){
			county = countyId;
		}else{
			county = optr.getCounty_id();
		}
		List addrs = sendMsgComponent.getAddrByName(comboQueryText,county);
		getRoot().setRecords(TreeBuilder.createTree(addrs));
		return JSON_RECORDS;
	}
	
	public String getUnitAll() throws Exception{
		String county="";
		if(StringHelper.isNotEmpty(countyId)){
			county = countyId;
		}else{
			county = optr.getCounty_id();
		}
		getRoot().setRecords(sendMsgComponent.getUnitAll(county));
		return JSON_RECORDS;
	}
	
	public String saveMsg() throws Exception {
		String county="",area = "";
		if(StringHelper.isNotEmpty(countyId)&&StringHelper.isNotEmpty(areaId)){
			county = countyId;
			area = areaId.split("-")[0];
		}else{
			county = optr.getCounty_id();
			area = optr.getArea_id();
		}
		sendMsgComponent.saveMsg(addrRecord,records,county,area, optr,sendMsg);
		return JSON;
	}
	
	public String deleteMsg() throws Exception{
			getRoot().setSuccess(sendMsgComponent.deleteMsg(doneId));
		return JSON;
	}
	
	
	public SendMsgComponent getSendMsgComponent() {
		return sendMsgComponent;
	}

	public void setSendMsgComponent(SendMsgComponent sendMsgComponent) {
		this.sendMsgComponent = sendMsgComponent;
	}

	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public String getComboQueryText() {
		return comboQueryText;
	}

	public void setComboQueryText(String comboQueryText) {
		this.comboQueryText = comboQueryText;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public JSendMsg getSendMsg() {
		return sendMsg;
	}

	public void setSendMsg(JSendMsg sendMsg) {
		this.sendMsg = sendMsg;
	}

	public String getDoneId() {
		return doneId;
	}

	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}

	public String getAddrRecord() {
		return addrRecord;
	}

	public void setAddrRecord(String addrRecord) {
		this.addrRecord = addrRecord;
	}


	public ProdComponent getProdComponent() {
		return prodComponent;
	}


	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}


	public String getCountyId() {
		return countyId;
	}


	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}


	public String getAreaId() {
		return areaId;
	}


	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}






}
