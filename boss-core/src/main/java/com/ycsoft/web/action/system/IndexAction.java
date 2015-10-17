package com.ycsoft.web.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSubSystem;
import com.ycsoft.business.dto.system.LoginDto;
import com.ycsoft.business.dto.system.MenuButtonDto;
import com.ycsoft.business.service.IIndexService;
import com.ycsoft.business.service.IQueryCfgService;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 *
 * <p>首页控制器</p>
 * <uL>
 * 	<li>加载侧边Resource Tree Node</li>
 * </ul>
 * @author hh
 * @date Dec 29, 2009 3:39:54 PM
 */
public class IndexAction extends BaseBusiAction{

	/**
	 *
	 */
	private static final long serialVersionUID = 1386539297590065075L;
	private IIndexService indexService;
	private IQueryCfgService queryCfgService;

	private String loginName ;
	private String pwd;
	private String sub_system_id;
	private String deptId;
	private String countyId;
	private String areaId;
	private String query;
	private String optrId;
	
	private String voucherId;


	/**
	 * @return the optrId
	 */
	public String getOptrId() {
		return optrId;
	}

	/**
	 * @param optrId the optrId to set
	 */
	public void setOptrId(String optrId) {
		this.optrId = optrId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取营业子系统功能资源
	 * @return
	 */
	public String indexInit()throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();

		List<MenuButtonDto> menuButtonList = indexService.findResource();
		map.put("resources", menuButtonList);
		map.put("busiCfgData", queryCfgService.queryBusiCfgData());
		map.put("cfgData", queryCfgService.queryCfgData());
		map.put("depts", queryCfgService.queryDepts());
		map.put("deptBusiCode", queryCfgService.queryDeptBusiCode(optr.getDept_id()));
		map.put("deptAddress", queryCfgService.queryDeptAddress(optr.getDept_id()));
		Map<String, MenuButtonDto> busiTask = CollectionHelper.converToMapSingle(menuButtonList, "handler");
		map.put("busiTask", busiTask);//查询可以跳转的业务

		HttpSession session = getSession();
		map.put("optr", JsonHelper.toObject(session.getAttribute(
				Environment.USER_IN_SESSION_NAME).toString(), SOptr.class));

		map.put("subsystem", queryCfgService.queryAllSubSystem(optr));//所有子系统信息
		map.put("busiOptrId", session.getAttribute(Environment.CURRENT_BUSI_OPTR_ID));
		getRoot().setOthers( map );
		return JSON_OTHER;
	}
	
	/**
	 * 查询当前操作员的公告信息
	 * @return
	 * @throws Exception
	 */
	public String queryBulletin()throws Exception{
		getRoot().setPage(indexService.queryBulletinByOptrId(start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询当前操作员的公告信息
	 * @return
	 * @throws Exception
	 */
	public String queryUnCheckBulletin()throws Exception{
		if(optr != null){
			getRoot().setSimpleObj(indexService.queryUnCheckByOptrId(optr.getOptr_id()));
		}
		return JSON_SIMPLEOBJ;
	}
	
	public String checkBulletin()throws Exception{
		String bulletinId = request.getParameter("bulletin_id");
		indexService.checkBulletin(bulletinId,optr.getOptr_id());
		return JSON;
	}

	/**
	 * 查询营业厅树(分权限)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryDeptTree() throws Exception{
		List list = queryCfgService.queryDeptTree();
		getRoot().setRecords(TreeBuilder.createTree(list));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询营业厅数(不分权限)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryOtherDeptTree() throws Exception{
		List list = queryCfgService.queryOtherDeptTree();
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
	
	public String queryOptrByCountyId() throws Exception {
		getRoot().setRecords(indexService.queryBusiOptr());
		return JSON_RECORDS;
	}
	
	public String getSubSystemByOptrId() throws Exception{
		getRoot().setRecords(indexService.querySubSystemByOptrId(optr.getOptr_id())); 
		return JSON_RECORDS;
	}
	public String updateOptrData() throws Exception{
		getRoot().setSuccess(indexService.updateOptrData(optr.getOptr_id(),pwd,query));
		return JSON;
	}
	
	public String reloadConfig() throws Exception{
		queryCfgService.reloadMemoryData();
		return JSON;
	}
	
	public String reloadPrintData() throws Exception{
		queryCfgService.reloadPrintData();
		return JSON;
	}
	
	public String queryVoucherById() throws Exception {
		getRoot().setSimpleObj(queryCfgService.queryVoucherById(voucherId));
		return JSON_SIMPLEOBJ;
	}

	public IIndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IIndexService indexService) {
		this.indexService = indexService;
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

	public void setQueryCfgService(IQueryCfgService queryCfgService) {
		this.queryCfgService = queryCfgService;
	}

	/**
	 * @param countyId the countyId to set
	 */
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

}
