package com.ycsoft.report.web.action.system;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.commons.tree.RepTreeBuilder;
import com.ycsoft.report.commons.tree.RepTreeNode;
import com.ycsoft.report.component.system.IndexComponent;

/**
 *
 * <p>首页控制器</p>
 * <uL>
 * 	<li>加载侧边Resource Tree Node</li>
 * </ul>
 * @author hh
 * @date Dec 29, 2009 3:39:54 PM
 */
public class IndexAction extends BaseAction{

	/**
	 *
	 */
	private static final long serialVersionUID = 1386539297590065075L;

	private IndexComponent indexComponent;
	
	
	private String loginName ;
	private String pwd;
	private String sub_system_id;
	private String deptId;
	private String countyId;
	private String areaId;
	private String query;
	private String optrId;

	public String login() throws Exception {
		ServletContext pokerWeb= getSession().getServletContext().getContext("/poker");
		if(pokerWeb!=null&&StringHelper.isNotEmpty(sub_system_id)){
			Object userloginname=pokerWeb.getAttribute(sub_system_id);
			if(userloginname!=null){
				SOptr optr=indexComponent.queryOptrByloginname(userloginname.toString());
				if(optr!=null){
					this.getSession().setAttribute(Environment.USER_IN_SESSION_NAME,JsonHelper.fromObject(optr));
					return "pokerstart";
				}
			}
		}
		return "pokererror";
	}
	/**
	 * 查询所有子系统定义信息
	 * @return
	 * @throws Exception
	 */
	public String queryAllSubSystem() throws Exception {
		getRoot().setRecords(indexComponent.queryAllSubSystem(optr));
		return JSON_RECORDS;
	}

	public String getSubSystemByOptrId() throws Exception{
		getRoot().setRecords(indexComponent.getSubSystemByOptrId(optr.getOptr_id())); 
		return JSON_RECORDS;
	}
	
	public String updateOptrData() throws Exception{
		getRoot().setSuccess(indexComponent.updateOptrData(optr.getOptr_id(),pwd,query));
		return JSON;
	}
	
	public String updateMemory()throws Exception{
		getRoot().setSimpleObj( SystemConfig.initMemory(
				WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext())));
		return JSON_SIMPLEOBJ ;
	}
	/**
	 * 加载侧边Resource Tree Node
	 */
	public String loadTreeNodes()throws Exception{
		List lst = indexComponent.loadTabResource(optr,sub_system_id);
		if(lst.size() > 0){
			getRoot().setSimpleObj( TreeBuilder.createTree( lst ).get(0).getChildren());
		}
		//getRoot().setSimpleObj(indexService.loadTreeNodes(optr.getOptr_id(),sub_system_id));
		return JSON_SIMPLEOBJ ;
	}
	/**
	 * 查询资源Resource Tree Node
	 */
	public String queryTreeNodes()throws Exception{
		/**
		 * 数据权限初始化
		 */
		indexComponent.initDataRole(this.getOptr(),this.getSession());
		/**
		 * 菜单权限查询
		 * 报表菜单清单
		 */
		List<RepTreeNode> lst = RepTreeBuilder.createTree((List) indexComponent.loadRepTabResource(optr,sub_system_id));
		if(lst.size()>0)
			getRoot().setSimpleObj(lst.get(0).getChildren());
		else
			getRoot().setSimpleObj(new ArrayList());
		return JSON_SIMPLEOBJ ;
	}
	
	public String queryIDTreeNodes()throws Exception{
		
		List<TreeNode> lst = TreeBuilder.createTree((List) indexComponent.loadTabResourceID(optr,"7"));
		getRoot().setRecords(lst);
		return JSON_RECORDS ;
	}
	
	/**
	 * 查询报表中res_type为NODE的资源信息
	 * @return
	 * @throws Exception
	 */
	public String queryResourcesByResType() throws Exception {
		getRoot().setRecords(TreeBuilder.createTree((List)indexComponent.queryResourcesByResType(optr.getOptr_id(), "7", "NODE")));
		return JSON_RECORDS;
	}
	
	/**
	 * 侧边选项卡资源
	 */
	public String queryResource()throws Exception{
		return null;
	}



	public IndexComponent getIndexComponent() {
		return indexComponent;
	}

	public void setIndexComponent(IndexComponent indexComponent) {
		this.indexComponent = indexComponent;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSub_system_id() {
		return sub_system_id;
	}

	public void setSub_system_id(String sub_system_id) {
		this.sub_system_id = sub_system_id;
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getOptrId() {
		return optrId;
	}

	public void setOptrId(String optrId) {
		this.optrId = optrId;
	}

}
