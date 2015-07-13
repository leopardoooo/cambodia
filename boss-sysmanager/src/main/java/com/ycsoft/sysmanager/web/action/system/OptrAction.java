package com.ycsoft.sysmanager.web.action.system;


import java.util.List;

import org.springframework.stereotype.Controller;

import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.system.SystemComponent;
import com.ycsoft.sysmanager.dto.system.SOptrDto;

/**
 *
 * @author sheng
 * Mar 23, 2010 5:44:08 PM
 */

@Controller
public class OptrAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -7971930916829919870L;
	private SystemComponent systemComponent;
	private SOptrDto newoptr;
	private String query;//查询条件
	private String pid;//查询条件 树节点值
	private String optr_id;
	private String doneId;
	private String[] roleIds;
	private String[] resourceIds;
	private String optrRoleList;
	private boolean clear;
	private String pwd;
	/**
	 * 根据条件查询操作员的信息，并进行分页
	 */
	public String queryOptrs()throws Exception{
		getRoot().setPage( systemComponent.queryOptrs(start, limit,query,pid,optr.getCounty_id()) );
		return JSON_PAGE;  
	}
	public String queryDepts() throws Exception{
			getRoot().setRecords(systemComponent.queryDepts(query,optr.getCounty_id()));
		return JSON_RECORDS;
	}
	public String queryOptrRole() throws Exception{
		getRoot().setRecords(systemComponent.queryOptrRole(query));
	return JSON_RECORDS;
	}
	
	public String chickLoginName() throws Exception{
		getRoot().setSuccess(systemComponent.validLoginName(query));
	return JSON;
	}
	public String getSubSystemByOptrId() throws Exception{
		getRoot().setRecords(systemComponent.getSubSystemByOptrId(optr.getOptr_id())); 
		return JSON_RECORDS;
	}
	public String updateOptrData() throws Exception{
		getRoot().setSuccess(systemComponent.updateOptrData(optr.getOptr_id(),pwd,query));
		return JSON;
	}
	/**
	 *操作员与权限的关系树
	 */
	@SuppressWarnings("unchecked")
	public String ResourceToOptrTree() throws Exception{
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)systemComponent.ResourceToOptrTree(optr_id,optr));
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	/**
	 *增加或减少权限TO操作员 
	 */
	public String saveResourceToOptrs() throws Exception {
		getRoot().setSuccess(systemComponent.saveResourceToOptrs(resourceIds,optr_id));
		return JSON;
	}
	/**
	 * 添加操作员
	 */
	public String save() throws Exception{
		//登陆名是否存在
		if(StringHelper.isNotEmpty(newoptr.getOptr_id())){
			getRoot().setSuccess(systemComponent.updateOptr(newoptr,optrRoleList));
		}else{
		    getRoot().setSuccess(systemComponent.saveOptr(newoptr,optrRoleList));
		}
		return JSON;
	}
	public String deteleOptrRole() throws Exception{
			getRoot().setSuccess(systemComponent.deteleOptrRole(doneId,optr_id));
		return JSON;
	}
	
	public String copyOptr() throws Exception {
		systemComponent.copyOptr(newoptr);
		return JSON;
	}
	
	public String changeDept() throws Exception {
		String optrId = request.getParameter("optrId");
		String newDeptId = request.getParameter("deptId");
		systemComponent.changeDept(optrId, newDeptId);
		return JSON;
	}
	
	public String queryDeptByCountyId() throws Exception{
		String countyId = request.getParameter("countyId");
		getRoot().setRecords(systemComponent.queryDepts(null,countyId));
	return JSON_RECORDS;
}
	
	public String queryOptrByCountyId() throws Exception {
		String countyId = request.getParameter("countyId");
		getRoot().setRecords(systemComponent.queryOptrByCountyId(countyId));
		return JSON_RECORDS;
	}
	
	/**
	 * 注销启用操作员
	 */
	public String updateOptrStatus() throws Exception{
		if(StringHelper.isNotEmpty(optr_id)){
			getRoot().setSuccess(systemComponent.updateOptrStatus(optr_id,doneId));
		}
		return JSON;
	}

	
	/**
	 * 验证登录名是否可用
	 * @return
	 * @throws Exception
	 */
	public String validLoginName() throws Exception{
		getRoot().setSuccess( systemComponent.validLoginName( optr.getLogin_name()));
		return JSON;
	}

	public void setSystemComponent(SystemComponent systemComponent) {
		this.systemComponent = systemComponent;
	}


	public SystemComponent getSystemComponent() {
		return systemComponent;
	}
	public SOptrDto getNewoptr() {
		return newoptr;
	}
	public void setNewoptr(SOptrDto newoptr) {
		this.newoptr = newoptr;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	public boolean isClear() {
		return clear;
	}
	public void setClear(boolean clear) {
		this.clear = clear;
	}
	public String getOptrRoleList() {
		return optrRoleList;
	}
	public void setOptrRoleList(String optrRoleList) {
		this.optrRoleList = optrRoleList;
	}
	public String getDoneId() {
		return doneId;
	}
	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}
	public String[] getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(String[] resourceIds) {
		this.resourceIds = resourceIds;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}



}
