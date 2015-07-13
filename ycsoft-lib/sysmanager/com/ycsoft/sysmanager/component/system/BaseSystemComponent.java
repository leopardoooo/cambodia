package com.ycsoft.sysmanager.component.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SOptrResource;
import com.ycsoft.beans.system.SOptrRole;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.system.SRoleResource;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SRoleResourceDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.dto.system.SOptrDto;
/**
 * 系统管理基础组件(报表会调用)
 * @author sheng
 * Oct 19, 2010 9:29:00 PM
 */
public class BaseSystemComponent extends BaseComponent{
	protected SCountyDao sCountyDao;
	protected SRoleResourceDao sRoleResourceDao;
	protected SDeptDao sDeptDao;
	protected MemoryComponent memoryComponent;

	/**
	 * 保存角色资源表
	 * @param sRoleResourceDao
	 */
	public boolean saveRoleResource(String[] roleIds,String res_id) throws Exception{
		return sRoleResourceDao.saveRoleResource(roleIds, res_id);
	}


	/**
	 * 删除角色资源表
	 * @param sRoleResourceDao
	 */
	public boolean removeRoleResource(String res_id) throws Exception{
		return sRoleResourceDao.removeRoleResource(res_id);
	}
	/**
	 * 报表资源分配角色
	 */
	public List<TreeNode> getRepRole(String resource_id) throws Exception {
		List<SRole> roles = sRoleDao.queryRoleBySystemId("7");
		List<SRoleResource> roles2= sRoleResourceDao.getRoleResource(resource_id);
		List<TreeNode> rt = TreeBuilder.convertToNodes( roles, "role_id", "role_name");
		//将县市对应的部门添加至县市的子节点中
	
		for(int j=0;j < roles.size();j++){
			rt.get(j).setLeaf( true );
			rt.get(j).setChecked(false);
			for(SRoleResource o:roles2){
				if (o.getRole_id().equals(rt.get(j).getId())){
					rt.get(j).setChecked(true);
					break;
				}
			}
		}
		return rt;
	}
	/**
	 * 菜单资源分配给角色
	 * @param resource_id
	 */
	public List<TreeNode> resource2Role(String resource_id) throws Exception {
		List<SItemvalue> areas = MemoryDict.getDicts(DictKey.AREA);
		List<SCounty> countys = sCountyDao.findAll();
		List<SRole> roles = sRoleDao.findAll();
		List<SRoleResource> roles2= sRoleResourceDao.getRoleResource(resource_id);

		List<TreeNode> at = TreeBuilder.convertToNodes( areas, "item_value", "item_name");
		List<TreeNode> ct = TreeBuilder.convertToNodes( countys, "county_id", "county_name");
		List<TreeNode> rt = TreeBuilder.convertToNodes( roles, "role_id", "role_name");

		//将县市对应的部门添加至县市的子节点中
		for(int i=0;i< countys.size() ;i++){
			for(int j=0;j < roles.size();j++){
				if(countys.get(i).getCounty_id().equals( roles.get(j).getCounty_id())){
					rt.get(j).setLeaf( true );
					rt.get(j).setChecked(false);
					for(SRoleResource o:roles2){
						if (o.getRole_id().equals(rt.get(j).getId())){
							rt.get(j).setChecked(true);
							break;
						}
					}
					ct.get(i).setChecked(false);
					ct.get(i).getChildren().add( rt.get(j));
				}
			}
			if(ct.get(i).getChildren().size() == 0){
				ct.get(i).setLeaf( false);
				ct.get(i).setChecked(false);
				ct.get(i).setIs_leaf( SystemConstants.BOOLEAN_FALSE );
			}
		}
		//将区域对应的县市添加至区域的子节点中
		for(int i=0;i< areas.size() ;i++){
			for(int j=0;j < countys.size();j++){
				if(areas.get(i).getItem_value().equals( countys.get(j).getArea_id())){
					at.get(i).getChildren().add( ct.get(j));
					at.get(i).setChecked(false);
				}
			}
			if(ct.get(i).getChildren().size() == 0){
				ct.get(i).setLeaf( false);
				ct.get(i).setChecked(false);
				ct.get(i).setIs_leaf( SystemConstants.BOOLEAN_FALSE );
			}
		}
		return at;
	}
	
	/**
	 * 验证操作员是否已经存在
	 */
	protected boolean validLoginName(String login_name) throws Exception {
		return !sOptrDao.isOptrToken(login_name);
	}
	
	protected SOptrDto setSaveOptrInfo(SOptrDto newOptr, SOptr currentOptr) throws Exception {
		
		SOptrDto optr = new SOptrDto();
		BeanUtils.copyProperties(newOptr, optr);
		SDept dept = new SDept();
		if (StringHelper.isNotEmpty(optr.getDept_id())) {
			dept = sDeptDao.findByKey(optr.getDept_id());
		}
		if (dept != null) {
			optr.setCounty_id(dept.getCounty_id());
			optr.setOld_county_id(dept.getCounty_id());
			optr.setArea_id(dept.getArea_id());
		} else {
			throw new ComponentException("该部门不允许建操作员!");
		}
		if (!validLoginName(optr.getLogin_name())) {
			throw new ComponentException("工号重复!");
		}
		optr.setPassword(MD5.EncodePassword(optr.getPassword()));
		optr.setOptr_id(sOptrDao.findSequence().toString());
		optr.setCreate_time(DateHelper.now());
		optr.setStatus(StatusConstants.ACTIVE);
		optr.setCreator(currentOptr.getLogin_name());
		
		//复制操作员默认登录系统
		if(StringHelper.isNotEmpty(newOptr.getCopy_optr_id())){
			SOptr copyOptr = sOptrDao.findByKey(newOptr.getCopy_optr_id());
			optr.setLogin_sys_id(copyOptr.getLogin_sys_id());
		}
		return optr;
	}
	
	/**
	 * 记录新增操作员异动
	 * @param doneCode
	 * @param newOptr				前台传值 操作员
	 * @param saveOptr				新建保存操作员
	 * @param createOptrId			当前操作员
	 * @param optrRoleList			新建操作员权限
	 * @param optrResourceList		新建操作员资源
	 * @throws Exception
	 */
	protected void createSaveOptrChange(int doneCode, SOptrDto newOptr, SOptr saveOptr,
			String createOptrId, List<SOptrRole> optrRoleList,
			List<SOptrResource> optrResourceList) throws Exception {
		Date createTime = new Date();
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		if(newOptr != null){
			SSysChange optrChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), doneCode,
					saveOptr.getOptr_id(), saveOptr.getOptr_name(), "新增操作员", 
					BeanHelper.beanchange(null, newOptr), createOptrId, createTime);
			changes.add(optrChange);
		}
		
//		if(saveOptr != null){
//			//操作成功刷新数据字典
//			List<SItemvalue> datalist = new ArrayList<SItemvalue>();
//			SItemvalue val = new SItemvalue(saveOptr.getOptr_name(), saveOptr.getOptr_id());
//			val.setItem_key(DictKey.OPTR.toString());
//			datalist.add(val);
//			
//			MemoryDict.addData(datalist );
//		}
		memoryComponent.addDictSignal(DictKey.OPTR.toString());
		
		if (optrRoleList != null && optrRoleList.size() > 0){
			String listchange = BeanHelper.listchange(null,optrRoleList,"role_name",null).replaceFirst("role_name", "role_id");
			SSysChange roleChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), doneCode,
					saveOptr.getOptr_id(), saveOptr.getOptr_name(), "操作员管理角色清单", 
					listchange, createOptrId, createTime);
			changes.add(roleChange);
		}
			
		if(optrResourceList!=null && optrResourceList.size() > 0 ){
			Map<String, List<SOptrResource>> newOptrResMap = CollectionHelper.converToMap(optrResourceList, "more_or_less");
			
			List<SOptrResource> newAddList = newOptrResMap.get("1");
			if(newAddList != null){
				String listchange = BeanHelper.listchange(null, newAddList, "res_name", null);
				if(StringHelper.isNotEmpty(listchange)){
					SSysChange resChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCode, 
							saveOptr.getOptr_id(), saveOptr.getOptr_name(), "操作员管理授予菜单资源", 
							listchange.replaceFirst("res_name", "res_id"),createOptrId, createTime);
					changes.add(resChange);
				}
			}
			
			List<SOptrResource> newRemoveList = newOptrResMap.get("0");
			if(newRemoveList != null){
				String listchange = BeanHelper.listchange(null, newRemoveList, "res_name", null);
				if(StringHelper.isNotEmpty(listchange)){
					SSysChange resChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCode, 
							saveOptr.getOptr_id(), saveOptr.getOptr_name(), "操作员管理禁用菜单资源", 
							listchange.replaceFirst("res_name", "res_id"),createOptrId, createTime);
					changes.add(resChange);
				}
			}
			
		}
		
		if(changes.size() > 0)
			sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
	}
	
	/**
	 * 记录修改操作员异动
	 * <p>若操作员SOptr信息没变更，oldOptr和newOptr 都传新操作员对象
	 * @param doneCode
	 * @param oldOptr				旧操作员
	 * @param newOptr				新操作员
	 * @param createOptrId			当前操作员ID
	 * @param oldOptrRoleList		旧操作员权限
	 * @param newRoleList			新操作员权限
	 * @param oldOptrResourceList	旧操作员资源
	 * @param newOptrResourceList	新操作员资源
	 * @throws Exception
	 */
	protected void createUpdateOptrChange(int doneCode,SOptr oldOptr, SOptr newOptr,String createOptrId, 
			List<?> oldOptrRoleList, List<?> newRoleList,
			List<SOptrResource> oldOptrResourceList,List<SOptrResource> newOptrResourceList) throws Exception {
		
		Date createTime = new Date();
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		if(oldOptrRoleList == null && newRoleList != null){
			oldOptrRoleList = new ArrayList();
		}else if(oldOptrRoleList != null && newRoleList == null){
			newRoleList = new ArrayList();
		}
		
		if(oldOptrRoleList != null && newRoleList != null){
			String listchange = BeanHelper.listchange(
					CollectionHelper.converValueToList(oldOptrRoleList, "role_name"), 
					CollectionHelper.converValueToList(newRoleList, "role_name"), null, null);
			
			if(StringHelper.isNotEmpty(listchange)){
				SSysChange roleChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), doneCode,
						newOptr.getOptr_id(), newOptr.getOptr_name(), "操作员管理角色清单", "role_id:"+listchange, 
						createOptrId, createTime);
				
				changes.add(roleChange);
			}
		}
		
		if(oldOptr != null && newOptr != null){
			String beanchange = BeanHelper.beanchange(oldOptr,newOptr);
			if(StringHelper.isNotEmpty(beanchange)){
				SSysChange optrChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), doneCode,
						newOptr.getOptr_id(), newOptr.getOptr_name(), "操作员基本资料修改", beanchange, 
						createOptrId, createTime);
				changes.add(optrChange);
			}
		}
		
		if(oldOptrResourceList == null && newOptrResourceList != null){
			oldOptrResourceList = new ArrayList<SOptrResource>();
		}else if(oldOptrResourceList != null && newOptrResourceList == null){
			newOptrResourceList = new ArrayList<SOptrResource>();
		}
		
		if(oldOptrResourceList!=null && newOptrResourceList != null ){
			Map<String, List<SOptrResource>> newOptrResMap = CollectionHelper.converToMap(newOptrResourceList, "more_or_less");
			Map<String, List<SOptrResource>> oldOptrResMap = CollectionHelper.converToMap(oldOptrResourceList, "more_or_less");
			
			List<SOptrResource> newAddList = newOptrResMap.get("1");
			List<SOptrResource> oldAddList = oldOptrResMap.get("1");
			if(newAddList != null || oldAddList != null){
				String listchange = BeanHelper.listchange(oldAddList, newAddList, "res_name", null);
				if(StringHelper.isNotEmpty(listchange)){
					SSysChange resChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCode, 
							newOptr.getOptr_id(), newOptr.getOptr_name(), "操作员管理授予菜单资源", 
							listchange.replaceFirst("res_name", "res_id"),createOptrId, createTime);
					changes.add(resChange);
				}
			}
			
			List<SOptrResource> newRemoveList = newOptrResMap.get("0");
			List<SOptrResource> oldRemoveList = oldOptrResMap.get("0");
			if(newRemoveList != null || oldRemoveList != null){
				String listchange = BeanHelper.listchange(oldRemoveList, newRemoveList, "res_name", null);
				if(StringHelper.isNotEmpty(listchange)){
					SSysChange resChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCode, 
							newOptr.getOptr_id(), newOptr.getOptr_name(), "操作员管理禁用菜单资源", 
							listchange.replaceFirst("res_name", "res_id"),createOptrId, createTime);
					changes.add(resChange);
				}
			}
			
		}
		
		if(changes.size() > 0)
			sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
	}
	
	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}
	public void setSRoleResourceDao(SRoleResourceDao roleResourceDao) {
		sRoleResourceDao = roleResourceDao;
	}


	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}


	/**
	 * @param memoryComponent the memoryComponent to set
	 */
	public void setMemoryComponent(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
	}
}
