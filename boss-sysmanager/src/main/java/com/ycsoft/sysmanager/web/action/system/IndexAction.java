package com.ycsoft.sysmanager.web.action.system;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.sysmanager.component.system.IndexComponent;


public class IndexAction extends BaseAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -4183471848212000275L;

	private IndexComponent indexComponent;
	private MemoryComponent memoryComponent;
	

	private String subSystemId;
	private String doneId;
	private String deptId;
	private String countyId;
	private String areaId;
	private String query;

	public String getDoneId() {
		return doneId;
	}

	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}

	public String queryMenus() throws Exception{
		List<SResource> lst = indexComponent.queryMenus(subSystemId,optr.getOptr_id());
		getRoot().setRecords(lst);
		return JSON_RECORDS;
	}
	
	public String reloadConfig() throws Exception{
		memoryComponent.addDictSignal("");
		memoryComponent.addTemplateSignal("");
		return JSON;
	}
	
	public String getTruleByDataType() throws Exception{
		getRoot().setRecords(indexComponent.getTruleByDataType(doneId,optr.getCounty_id()));
		return JSON_RECORDS;
	}
	public String queryBusiRule() throws Exception{
		getRoot().setRecords(indexComponent.findBusiRule(optr.getCounty_id()));
		return JSON_RECORDS;
	}

	public String queryRentRule() throws Exception{
		getRoot().setRecords(indexComponent.findRentRule(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	public String queryUseFeeRule() throws Exception{
		getRoot().setRecords(indexComponent.findUseFeeRule(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	public String queryBillRule() throws Exception{
		getRoot().setRecords(indexComponent.findBillRule(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	
	public String queryLogs() throws Exception{
		getRoot().setPage(indexComponent.queryLogs(query, optr.getCounty_id(), start, limit));
		return JSON_PAGE;
	}
	

	/**
	 * 系统菜单
	 */
	public String loadTreeMenus() throws Exception {
		List lst = indexComponent.loadTreeMenus(subSystemId);
		if (lst.size() > 0) {
			getRoot().setSimpleObj(TreeBuilder.createTree(lst,false).get(0));
		}
		return "json-children";
	}

	/**
	 * 查询营业厅树(分权限)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryDeptTree() throws Exception{
		List list = indexComponent.queryDeptTree(optr);
		getRoot().setRecords(TreeBuilder.createTree(list));
		return JSON_RECORDS;
	}
	
	public String changeDept() throws Exception{
		HttpSession session = getSession();
		SOptr optr = JsonHelper.toObject(session.getAttribute(
				Environment.USER_IN_SESSION_NAME).toString(), SOptr.class);
		optr.setDept_id(deptId);
		optr.setCounty_id(countyId);
		optr.setArea_id(areaId);
		session.setAttribute(Environment.USER_IN_SESSION_NAME, JsonHelper.fromObject(optr));

		return JSON;
	}
	

	/**
	 * 查询在线用户的操作记录
	 * @return
	 * @throws Exception
	 */
	public String queryOnelineUserBusi() throws Exception{
		getRoot().setRecords(indexComponent.queryOnelineUserBusi(query));
		return JSON_RECORDS;
	}
	
	public void setIndexComponent(IndexComponent indexComponent) {
		this.indexComponent = indexComponent;
	}


	public void setSubSystemId(String subSystemId) {
		this.subSystemId = subSystemId;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	/**
	 * @param memoryComponent the memoryComponent to set
	 */
	public void setMemoryComponent(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
	}
	
	
}
