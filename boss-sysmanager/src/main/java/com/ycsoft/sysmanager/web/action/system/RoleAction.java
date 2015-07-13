package com.ycsoft.sysmanager.web.action.system;



import java.util.List;

import org.springframework.stereotype.Controller;

import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.system.SystemComponent;
import com.ycsoft.sysmanager.dto.system.SRoleDto;

/**
 * 角色管理
 * @author sheng
 * Mar 29, 2010 10:02:15 AM
 */
@Controller
public class RoleAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 5508516213704572382L;
	private SystemComponent systemComponent;
	private SRoleDto role;
	private String [] resIds;
	private String [] optrIds ;
	private String[] countyIds;
	private String role_id;
	private String role_type;
	private String sub_system_id;
	private String data_right_type;
	private String data_right_level;
	private String rule_id;
	private String query;
	private String doneId;
	private String recordList;
	private boolean clear ;

	/**
	 * 查询角色
	 */
	public String queryRoles() throws Exception{
		getRoot().setPage(systemComponent.queryRoles(start, limit, query,optr));
		return JSON_PAGE;
	}
	
	public String queryRoleByObj() throws Exception {
		getRoot().setRecords(systemComponent.queryRoleForAssign(optr,role.getSub_system_id(),role.getData_right_type()));
		return JSON_RECORDS;
	}

	public String getResBySystemId() throws Exception{
		getRoot().setRecords(systemComponent.getResBySystemId(doneId,role_id,optr));
		return JSON_RECORDS;
	}
	
	public String findRoleResource() throws Exception{
		getRoot().setRecords(systemComponent.findRoleResource(doneId,role_id));
		return JSON_RECORDS;
	}
	
	/**
	 *操作员树
	 */
	@SuppressWarnings("unchecked")
	public String goToOptrTree() throws Exception{
		SRoleDto dto = new SRoleDto();
		dto.setRole_id(role_id);
		dto.setSub_system_id(sub_system_id);
		dto.setRole_type(role_type);
		dto.setData_right_type(data_right_type);
		dto.setRule_id(rule_id);
		dto.setData_right_level(data_right_level);
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)systemComponent.goToOptrTree(dto,optr));
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	/**
	* 角色分配给多个操作员
	 */
	public String saveRoleToOptrs() throws Exception {
		//TODO 需要记录变更
		getRoot().setSuccess(systemComponent.saveRoleToOptrs(role_id,optrIds,optr));
		return JSON;
	}
	
	/**
	 * 保存角色
	 */
	public String save() throws Exception{
		//TODO 需要记录变更
		if(StringHelper.isNotEmpty(role.getRole_id())){
			systemComponent.updateRole(role,resIds,countyIds);
		}else{
			role.setCreator(optr.getLogin_name());
			role.setCreate_time(DateHelper.now());
			role.setCounty_id(optr.getCounty_id());
			role.setArea_id(optr.getArea_id());
			role.setOptr_id(optr.getOptr_id());
			systemComponent.saveRole(role,resIds,countyIds);
		}
		return JSON;
	}

	/**
	 * 删除角色
	 */
	public String delete() throws Exception{
		if(StringHelper.isNotEmpty(role_id)){
			SRoleDto role = new SRoleDto();
			role.setRole_id(role_id);
			getRoot().setSuccess(systemComponent.deleteRole(role));
		}else{
			getRoot().setSuccess(false);
		}

		return JSON;
	}
	public SRoleDto getRole() {
		return role;
	}

	public void setRole(SRoleDto role) {
		this.role = role;
	}

	public SystemComponent getSystemComponent() {
		return systemComponent;
	}
	public void setSystemComponent(SystemComponent systemComponent) {
		this.systemComponent = systemComponent;
	}
	public String[] getOptrIds() {
		return optrIds;
	}

	public void setOptrIds(String[] optrIds) {
		this.optrIds = optrIds;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}
	public String getDoneId() {
		return doneId;
	}
	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}
	public String[] getResIds() {
		return resIds;
	}
	public void setResIds(String[] resIds) {
		this.resIds = resIds;
	}

	public String getRecordList() {
		return recordList;
	}

	public void setRecordList(String recordList) {
		this.recordList = recordList;
	}

	public String getRole_type() {
		return role_type;
	}

	public void setRole_type(String roleType) {
		role_type = roleType;
	}

	public String getSub_system_id() {
		return sub_system_id;
	}

	public void setSub_system_id(String subSystemId) {
		sub_system_id = subSystemId;
	}

	public String getData_right_type() {
		return data_right_type;
	}

	public void setData_right_type(String dataRightType) {
		data_right_type = dataRightType;
	}

	public String getData_right_level() {
		return data_right_level;
	}

	public void setData_right_level(String dataRightLevel) {
		data_right_level = dataRightLevel;
	}

	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String ruleId) {
		rule_id = ruleId;
	}
	public String[] getCountyIds() {
		return countyIds;
	}

	public void setCountyIds(String[] countyIds) {
		this.countyIds = countyIds;
	}
}
