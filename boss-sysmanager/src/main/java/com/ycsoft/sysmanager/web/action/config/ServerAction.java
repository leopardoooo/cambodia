package com.ycsoft.sysmanager.web.action.config;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.bill.BCreditAddressStop;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.business.dto.config.ChangeValueDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.config.ResComponent;
import com.ycsoft.sysmanager.component.prod.ProdComponent;

public class ServerAction  extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5566630604653385489L;

	private String query;
	private String doneId;
	private String countyIds;
	private TServer server;
	private ResComponent resComponent;
	private ProdComponent prodComponent;
	
 	public String getCountyTree() throws Exception{
		String[] type = {"COUNTY","SERVER"};
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	public String queryServer()throws Exception{
		getRoot().setRecords( resComponent.queryServer(optr.getCounty_id()) );
		return JSON_RECORDS;  
	}
	
	
	public String queryStopCount()throws Exception{
		getRoot().setRecords( resComponent.queryStopCount() );
		return JSON_RECORDS; 
	}
	
	public String saveStopCount()throws Exception{
		String changeList = request.getParameter("changeList");
		Type type = new TypeToken<List<BCreditAddressStop>>(){}.getType();
		List<BCreditAddressStop> changeValueList = new Gson().fromJson(changeList, type);
		resComponent.saveStopCount(changeValueList);
		getRoot().setSuccess(true);
		return JSON_SUCCESS; 
	}
	
	public String saveServer() throws Exception{ 
		resComponent.saveServer(server, optr,countyIds);
		return JSON;
	}

	public ResComponent getResComponent() {
		return resComponent;
	}


	public void setResComponent(ResComponent resComponent) {
		this.resComponent = resComponent;
	}


	public String getQuery() {
		return query;
	}



	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getDoneId() {
		return doneId;
	}

	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}
	
	public String getCountyIds() {
		return countyIds;
	}
	public void setCountyIds(String countyIds) {
		this.countyIds = countyIds;
	}
	public TServer getServer() {
		return server;
	}

	public void setServer(TServer server) {
		this.server = server;
	}

}
