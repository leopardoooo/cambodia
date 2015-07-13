package com.ycsoft.sysmanager.web.action.system;

import org.springframework.stereotype.Controller;

import com.ycsoft.beans.system.SResource;
import com.ycsoft.beans.system.SRoleResource;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.sysmanager.component.system.SystemComponent;

/**
 * 资源管理
 * @author sheng
 * Mar 29, 2010 10:03:35 AM
 */
@Controller
public class ResourceAction extends BaseAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -8812138587558276973L;

	private SystemComponent systemComponent;
	private SResource resource;
	private SRoleResource sRoleResource;
	private String role_id;
	private String res_id;
	private boolean clear ;
	private String[] roleIds;
	private String query;
	private String pid;

	/**
	 * 查询所有子系统信息
	 * @return
	 * @throws Exception
	 */
	public String queryAllSubSystem() throws Exception {
		getRoot().setRecords(systemComponent.queryAllSubSystem(optr));
		return JSON_RECORDS;
	}

	/**
	 * 查询菜单资源信息
	 */
	public String queryResources() throws Exception{
		getRoot().setPage(systemComponent.queryResources(start, limit, query,pid));
		return JSON_PAGE;
	}

	/**
	 * 保存菜单资源信息
	 */
	public String save() throws Exception {
		if(StringHelper.isNotEmpty(resource.getRes_id())){
			getRoot().setSuccess(systemComponent.updateResource(resource));
		}else{
			getRoot().setSuccess(systemComponent.saveResource(resource));
		}
		return JSON;
	}

	/**
	 * 删除菜单资源信息
	 */
	public String delete() throws Exception{
		if(StringHelper.isNotEmpty(res_id)){
			SResource resource= new SResource();
			resource.setRes_id(res_id);
			getRoot().setSuccess(systemComponent.deleteResource(resource));
		}else{
			getRoot().setSuccess(false);
		}
		return JSON;
	}

	/**
	 * 菜单分配角色关系
	 */
	public String getResource2Role() throws Exception{
		getRoot().setRecords(systemComponent.resource2Role(res_id));
		return JSON_RECORDS;
	}
	/**
	 * 菜单分配角色保存
	 */
	public String saveResource2Role() throws Exception{
		if (StringHelper.isNotEmpty(res_id)){
			getRoot().setSuccess(systemComponent.removeRoleResource(res_id));//如果原来分配的有角色，先删除角色，在保存修改的
			//标记是否全部未选中
			if(true != clear ){
				getRoot().setSuccess(systemComponent.saveRoleResource(roleIds,res_id));
			}
		}else{
			getRoot().setSuccess(false);
		}
		return JSON;
	}



	public SystemComponent getSystemComponent() {
		return systemComponent;
	}
	public void setSystemComponent(SystemComponent systemComponent) {
		this.systemComponent = systemComponent;
	}
	public SResource getResource() {
		return resource;
	}
	public void setResource(SResource resource) {
		this.resource = resource;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
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

	public SRoleResource getSRoleResource() {
		return sRoleResource;
	}

	public void setSRoleResource(SRoleResource roleResource) {
		sRoleResource = roleResource;
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


}
