package com.ycsoft.sysmanager.web.action.config;

import java.util.List;

import com.ycsoft.beans.system.SBulletin;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.config.SendMsgComponent;
import com.ycsoft.sysmanager.component.prod.ProdComponent;

public class BulletinAction  extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5566630604653385489L;

	private String query;
	private String records;
	private SBulletin bulletin;
	private String doneId;
	private String bullCountyIds;
	private SendMsgComponent sendMsgComponent;
	
	public String getCountyTree() throws Exception{
		String[] type = {"COUNTY","BULLETION"};
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)sendMsgComponent.getCountyTree(optr,type,query));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	public String getDeptTree() throws Exception{
		String[] type = {"COUNTY","BULLETION"};
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)sendMsgComponent.getDeptTree(optr,type,query));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	public String queryBulletin()throws Exception{
		getRoot().setPage( sendMsgComponent.queryBulletin(start, limit,query,optr.getCounty_id()) );
		return JSON_PAGE;  
	}
	
	public String saveBulletin() throws Exception{ 
		getRoot().setSuccess(sendMsgComponent.saveBulletin(bulletin, optr,bullCountyIds));
		return JSON;
	}
	
	
	
	/**
	 * 启用禁用公告
	 */
	public String changeBulletin() throws Exception{
			getRoot().setSuccess(sendMsgComponent.changeBulletin(query,doneId));
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






	public String getRecords() {
		return records;
	}






	public void setRecords(String records) {
		this.records = records;
	}






	public SBulletin getBulletin() {
		return bulletin;
	}






	public void setBulletin(SBulletin bulletin) {
		this.bulletin = bulletin;
	}






	public String getDoneId() {
		return doneId;
	}






	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}
	public String getBullCountyIds() {
		return bullCountyIds;
	}
	public void setBullCountyIds(String bullCountyIds) {
		this.bullCountyIds = bullCountyIds;
	}
}
