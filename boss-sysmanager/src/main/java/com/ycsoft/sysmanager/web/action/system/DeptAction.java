package com.ycsoft.sysmanager.web.action.system;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SDeptBusicode;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.system.SystemComponent;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Controller
public class DeptAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -2672335551430477892L;
	private SystemComponent systemComponent;
	private SDept dept ;
	private String deptId;
	private String query;
	private String pid;
	private String countyId;
	private String[] addrIds;
	private String[] busiCodes;
	private String bindType;

	/**
	 * 部门对应的仓库
	 * @return
	 * @throws Exception
	 */
	public String queryDepot() throws Exception {
		getRoot().setRecords(systemComponent.queryDepot(countyId));
		return JSON_RECORDS;
	}

	/**
	 * 根据操作员ID查询部门
	 * @param countyId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String queryDeptByCountyId() throws Exception{
		List list = systemComponent.queryDeptByCountyId(optr);
		getRoot().setRecords(TreeBuilder.createTree(list));
		return JSON_RECORDS;
	}
	
	@SuppressWarnings("unchecked")
	public String queryAdrToDeptTree() throws Exception{
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)systemComponent.queryAdrToDeptTree(deptId));
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	/**
	 * 删除机构信息
	 */
	public String removeDept() throws Exception{
		SDept oldDept = systemComponent.queryDeptInfoForSysChange(deptId);
		getRoot().setSimpleObj(systemComponent.deleteDept(deptId));
		SDept newDept = systemComponent.queryDeptInfoForSysChange(deptId);
		saveChanges(oldDept,newDept);
		return JSON;
	}

	/**
	 * 添加修改机构信息
	 */
	public String saveDept() throws Exception{
		SDept oldDept = systemComponent.queryDeptInfoForSysChange(dept.getDept_id());
		if(StringHelper.isNotEmpty(dept.getDept_id())){
			systemComponent.updateDept(dept);
		}else{
			systemComponent.saveDept(dept,optr);
		}
		SDept newDept = systemComponent.queryDeptInfoForSysChange(dept.getDept_id());
		saveChanges(oldDept,newDept);
		getRoot().setSimpleObj(dept);
		return JSON;
	}
	
	private void saveChanges(SDept oldDept, SDept newDept) throws ActionException{
		if(oldDept ==null && newDept ==null){
			throw new ActionException("保存部门异动信息的时候参数有误!");
		}
		try{
			String content = BeanHelper.beanchange(oldDept, newDept);
			String key = newDept ==null ? oldDept.getDept_id():newDept.getDept_id();
			String keyDesc = newDept ==null ? oldDept.getDept_name():newDept.getDept_name();
			String changeDesc = "部门异动";
			if(StringHelper.isNotEmpty(content)){
				SSysChange change = new SSysChange(SysChangeType.DEPT.toString(), systemComponent.getDoneCOde(), 
						key,keyDesc, changeDesc, content, WebOptr.getOptr().getOptr_id(), new Date());
				systemComponent.getSSysChangeDao().save(change);
			}
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}
	
	public String saveAddrToDept() throws Exception {
		systemComponent.saveAddrToDept(addrIds,deptId);
		getRoot().setSimpleObj(deptId);
		return JSON;
	}
	
	/**
	 * 溧阳.
	 * @return
	 * @throws Exception
	 */
	public String saveAddr2Dept() throws Exception {
		if(addrIds  == null){
			addrIds = new String [0];
		}
		List<String> list = new ArrayList<String>();
		for(String str:addrIds){
			if(StringHelper.isNotEmpty(str)){
				list.add(str);
			}
		}
		//通过上面方法过滤掉传过来空参数.
		systemComponent.saveAddr2Dept(list.toArray(new String[list.size()]),deptId);
		getRoot().setSimpleObj(deptId);
		return JSON;
	}
	
	public String queryBindableAddr() throws Exception{
		List<TAddress> records = systemComponent.queryBindableAddr(countyId,deptId);
		getRoot().setRecords(records);
		return JSON_RECORDS;
	}
	
	/**
	 * 保存小区、业务对应关系.
	 * @return
	 * @throws Exception
	 */
	public String saveBusiCodesToDept() throws Exception {
		systemComponent.saveBusiCodesToDept(busiCodes,deptId,bindType);
		getRoot().setSimpleObj(true);
		return JSON;
	}
	
	/**
	 * 查询小区、业务对应关系.
	 * @return
	 * @throws Exception
	 */
	public String queryDeptBusiCodes() throws Exception {
		List<SDeptBusicode> records = systemComponent.queryDeptBusiCodes(deptId);
		getRoot().setRecords(records);
		return JSON_RECORDS;
	}

	public SystemComponent getSystemComponent() {
		return systemComponent;
	}

	public void setSystemComponent(SystemComponent systemComponent) {
		this.systemComponent = systemComponent;
	}

	public SDept getDept() {
		return dept;
	}

	public void setDept(SDept dept) {
		this.dept = dept;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public void setAddrIds(String[] addrIds) {
		this.addrIds = addrIds;
	}

	public String[] getBusiCodes() {
		return busiCodes;
	}

	public void setBusiCodes(String[] busiCodes) {
		this.busiCodes = busiCodes;
	}

	public String getBindType() {
		return bindType;
	}

	public void setBindType(String bindType) {
		this.bindType = bindType;
	}
}
