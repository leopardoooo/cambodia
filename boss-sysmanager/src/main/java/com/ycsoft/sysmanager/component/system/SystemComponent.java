package com.ycsoft.sysmanager.component.system;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.beans.system.SDeptBusicode;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SOptrResource;
import com.ycsoft.beans.system.SOptrRole;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.system.SRoleResource;
import com.ycsoft.beans.system.SSubSystem;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceDao;
import com.ycsoft.business.dao.system.SAreaDao;
import com.ycsoft.business.dao.system.SDeptAddrDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SOptrResourceDao;
import com.ycsoft.business.dao.system.SOptrRoleDao;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dao.system.SRoleCountyDao;
import com.ycsoft.business.dao.system.SSubSystemDao;
import com.ycsoft.business.dao.system.SdeptBusicodeDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.system.OptrDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.dto.system.SDeptDto;
import com.ycsoft.sysmanager.dto.system.SOptrDto;
import com.ycsoft.sysmanager.dto.system.SRoleDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

/**
 * 系统管理组件
 * 
 * @author sheng Mar 24, 2010 11:28:07 AM
 */
@Component
public class SystemComponent extends BaseSystemComponent {

	private SAreaDao sAreaDao;
	private TAddressDao tAddressDao;
	private SDeptAddrDao sDeptAddrDao;

	private SResourceDao sResourceDao;
	private SOptrRoleDao sOptrRoleDao;
	private SOptrResourceDao sOptrResourceDao;

	private PProdDao pProdDao;
	private SSubSystemDao sSubSystemDao;

	private RInvoiceDao rInvoiceDao;
	private RDeviceDao rDeviceDao;
	private SRoleCountyDao sRoleCountyDao;
	
	private SdeptBusicodeDao sdeptBusicodeDao;
	
	/**
	 * 地区的所有仓库
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<RDepotDefine> queryDepot(String countyId) throws Exception {
		return sDeptDao.queryDepot(countyId);
	}

	/**
	 * 查询所有子系统信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SSubSystem> queryAllSubSystem(SOptr optr) throws Exception {
		return sSubSystemDao.queryAllSubSystem(optr);
	}

	// -----操作员管理-------------------------------------------------------------------------------------------------

	/**
	 *操作员与权限关系树
	 */
	public List<TreeDto> ResourceToOptrTree(String optrId, SOptr optr)
			throws Exception {
		List<TreeDto> resources = null;
		List<TreeDto> valueList = null;
		if (optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
			resources = sOptrResourceDao.getResourceByOptr(optr.getOptr_id());
		} else {
			resources = sOptrResourceDao.getResourceByCounty(optr.getOptr_id());
		}
		if (StringHelper.isNotEmpty(optrId)) {
			valueList = sOptrResourceDao.getResourceByCounty(optrId);
		}
		for (int i = 0; i < resources.size(); i++) {
			resources.get(i).setChecked(false);
			if (valueList != null && !"null".equals(valueList)) {
				for (int j = 0; j < valueList.size(); j++) {
					if (resources.get(i).getId().equals(
							valueList.get(j).getId())
							&& valueList.get(j).getAttr().equals("true")) {
						resources.get(i).setChecked(true);
					}
				}
			}
		}
		return resources;
	}

	/**
	 * 查询操作员信息
	 */
	public Pager<SOptrDto> queryOptrs(Integer start, Integer limit,
			String keyword, String pid, String countyId) throws Exception {
		return sOptrDao.query(start, limit, keyword, pid, null);
	}

	public List<SDept> queryDepts(String query, String countyId)
			throws JDBCException {
		if (StringHelper.isNotEmpty(query)) {
			return sDeptDao.queryByCountyId(query);
		} else {
			return sDeptDao.queryByCountyId(countyId);
		}
	}
	
	/**
	 * 根据deptid查询dept信息.
	 * @param deptId
	 * @return
	 * @throws ComponentException
	 */
	public SDept queryDeptInfoForSysChange(String deptId) throws ComponentException{
		SDept dept = null;
		try{
			dept = sDeptDao.findByKey(deptId);
		}catch (Exception e) {
			throw new ComponentException(e);
		}
		return dept;
	}

	public List<SRole> queryRoleForAssign(SOptr optr, String subSystemId,
			String dataType) throws Exception {
		String dataRight = this.queryDataRightCon(optr, DataRight.ROLE.toString());
		List<SRole> list = sRoleDao.queryRoleToUse(subSystemId, dataType,
				optr.getCounty_id(), dataRight);
		return list;
	}

	public List<SRoleDto> queryOptrRole(String optrId) throws Exception {
		List<SRoleDto> list = sOptrRoleDao.queryOptrRole(optrId);
		return list;
	}

	public List<SRoleDto> getSubSystemByOptrId(String optrId) throws Exception {
		List<SRoleDto> list = sOptrRoleDao.queryOptrRole(optrId);
		if (list.size() > 0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				boolean ck = false;
				if (StringHelper.isEmpty(list.get(i).getSub_system_id())) {
					ck = true;
				}
				if (ck) {
					list.remove(i);
				}
			}
		} else {
			throw new ComponentException("操作员的配置存在问题，角色中不存在子系统!");
		}
		return list;
	}

	/**
	 * 保存操作员信息
	 */
	public boolean saveOptr(SOptrDto newOptr, String rolelist)
			throws Exception {
		int sues = -1;
		int doneCode = getDoneCOde();
		SOptr optr = setSaveOptrInfo(newOptr, WebOptr.getOptr());
		sues = sOptrDao.save(optr)[0];

		if (sues >= 0 || sues == -2) {
			
			List<SOptrRole> optrRoleList = new ArrayList<SOptrRole>();
			if (StringHelper.isNotEmpty(rolelist)) {
				Type type = new TypeToken<List<SOptrRole>>() {}.getType();
				List<SOptrRole> list = new Gson().fromJson(rolelist, type);
				SOptrRole optrRole = null;
				for (SOptrRole dto : list) {
					optrRole = new SOptrRole(optr.getOptr_id(), dto.getRole_id());
					optrRole.setRole_name(dto.getRole_name());
					optrRoleList.add(optrRole);
				}
			}
			
			SOptr newoptr = new SOptr();
			newoptr.setLogin_name(newOptr.getLogin_name());
			newoptr.setStatus(StatusConstants.ACTIVE);
			newoptr = sOptrDao.findByEntity(newoptr).get(0);//一定不能报错.根据login_name且保证唯一性

			if (optrRoleList.size() > 0){
				sOptrRoleDao.save(optrRoleList.toArray(new SOptrRole[optrRoleList.size()]));
			}
			
			this.createSaveOptrChange(doneCode, newOptr, newoptr, 
					WebOptr.getOptr().getOptr_id(), optrRoleList, null);
			return true;
		}
		return false;
	}

	public boolean updateOptr(SOptrDto newOptr, String rolelist)
			throws Exception {
		int doneCode = getDoneCOde();
		String optrId = newOptr.getOptr_id();
		
		List<SOptr> optrList = sOptrDao.getSameOptrById(optrId);
		
		for(SOptr optrDto :optrList){
			
			SOptr oldOptr = sOptrDao.findByKey(optrDto.getOptr_id());
			List<SRoleDto> oldRoles = queryOptrRole(optrDto.getOptr_id());
			
			SOptrRole optrRole = new SOptrRole();
			SOptrDto optr = new SOptrDto();
			newOptr.setDept_id(null);
			BeanUtils.copyProperties(newOptr, optr);
			optr.setOptr_id(optrDto.getOptr_id());
			List<SOptrRole> optrRoleList = new ArrayList<SOptrRole>();
			if (optr.getPassword().trim().equals("")) {
				optr.setPassword(null);
			} else {
				optr.setPassword(MD5.EncodePassword(optr.getPassword()));
			}
			sOptrDao.update(optr);
			
			if (StringHelper.isNotEmpty(rolelist)) {
				Type type = new TypeToken<List<SOptrRole>>() {}.getType();
				List<SOptrRole> list = new Gson().fromJson(rolelist, type);
				deteleOptrRoleByOptr(optrDto.getOptr_id());
				for (SOptrRole dto : list) {
					optrRole = new SOptrRole(optrDto.getOptr_id(), dto.getRole_id());
					optrRole.setRole_name(dto.getRole_name());
					optrRoleList.add(optrRole);
				}
			}
			// 配置角色
			if (optrRoleList.size() > 0 && optrDto.getStatus().equals(StatusConstants.ACTIVE)) {
				SOptrRole[] array = new SOptrRole[optrRoleList.size()];
				sOptrRoleDao.save(optrRoleList.toArray(array));
			}
			
			//开始记录异动
			SOptr newoptr = sOptrDao.findByKey(optrDto.getOptr_id());
			
			this.createUpdateOptrChange(doneCode, oldOptr, newoptr, WebOptr.getOptr().getOptr_id(), 
					oldRoles, optrRoleList, null, null);
				
		}
		return true;
	}
	
	/**
	 * 复制操作员，包括资源、权限
	 * @param newoptr
	 * @param optr
	 * @throws Exception
	 */
	public void copyOptr(SOptrDto newOptrDto) throws Exception {
		int sues = -1;
		int doneCode = getDoneCOde();
		//新增操作员复制
		if(StringHelper.isEmpty(newOptrDto.getOptr_id())){
			SOptr optr = setSaveOptrInfo(newOptrDto, WebOptr.getOptr());
			sues = sOptrDao.save(optr)[0];
			
			if(sues >= 0 || sues == -2){
				List<SOptrRole> optrRoleList = sOptrRoleDao.copyOptrRole(newOptrDto.getCopy_optr_id(), optr.getOptr_id());
				List<SOptrResource> optrResList = sOptrResourceDao.copyOptrResource(newOptrDto.getCopy_optr_id(), optr.getOptr_id());
				
				this.createSaveOptrChange(doneCode, newOptrDto, optr, 
						WebOptr.getOptr().getOptr_id(), optrRoleList, optrResList);
			}
		} else {
			SOptr oldOptr = sOptrDao.findByKey(newOptrDto.getOptr_id());
			List<SRoleDto> oldRoleList = queryOptrRole(oldOptr.getOptr_id());
			List<SOptrResource> oldResList = sOptrResourceDao.queryByOptr(oldOptr.getOptr_id());
			
			SOptr optr = new SOptr();
			BeanUtils.copyProperties(newOptrDto, optr);
			if (optr.getPassword().trim().equals("")) {
				optr.setPassword(null);
			} else {
				optr.setPassword(MD5.EncodePassword(optr.getPassword()));
			}
			//复制操作员默认登录系统
			if(StringHelper.isNotEmpty(newOptrDto.getCopy_optr_id())){
				SOptr copyOptr = sOptrDao.findByKey(newOptrDto.getCopy_optr_id());
				optr.setLogin_sys_id(copyOptr.getLogin_sys_id());
			}
			sues = sOptrDao.update(optr)[0];
			
			SOptr newOptr = sOptrDao.findByKey(newOptrDto.getOptr_id());
			List<SRoleDto> newRoleList = queryOptrRole(newOptrDto.getCopy_optr_id());	//复制操作员权限
			List<SOptrResource> newResList = sOptrResourceDao.queryByOptr(newOptrDto.getCopy_optr_id());//复制操作员资源
			
			String optrId = newOptr.getOptr_id();
			if(newRoleList.size() > 0 ){
				//复制新权限
				List<SOptrRole> addRoleList = new ArrayList<SOptrRole>();
				for(SRoleDto newRole : newRoleList){
					boolean flag = true;
					String roleId = newRole.getRole_id();
					for(SRoleDto oldRole : oldRoleList){
						if(roleId.equals(oldRole.getRole_id())){
							flag = false;
							break;
						}
					}
					if(flag){
						SOptrRole optrRole = new SOptrRole();
						optrRole.setOptr_id(optrId);
						optrRole.setRole_id(roleId);
						addRoleList.add(optrRole);
					}
				}
				sOptrRoleDao.save(addRoleList.toArray(new SOptrRole[addRoleList.size()]));
			}
			
			if(newResList.size() > 0){
				List<SOptrResource> addResList = new ArrayList<SOptrResource>();
				for(SOptrResource newRes : newResList){
					boolean flag = true;
					String resId = newRes.getRes_id();
					int moreOrLess = newRes.getMore_or_less();
					for(SOptrResource oldRes : oldResList){
						if(resId.equals(oldRes.getRes_id())){
							flag = false;
							break;
						}
					}
					if(flag){
						SOptrResource optrRes = new SOptrResource();
						optrRes.setOptr_id(optrId);
						optrRes.setRes_id(resId);
						optrRes.setMore_or_less(moreOrLess);
						addResList.add(optrRes);
					}
				}
				sOptrResourceDao.save(addResList.toArray(new SOptrResource[addResList.size()]));
			}
			
			this.createUpdateOptrChange(doneCode, oldOptr, newOptr, WebOptr.getOptr().getOptr_id(), 
					oldRoleList, newRoleList, oldResList, newResList);
		}
	}

	public void changeDept(String optrId, String newDeptId) throws Exception {
		int doneCode = getDoneCOde();
		SOptr optr = sOptrDao.findByKey(optrId);
		String oldOptrId = optr.getOptr_id();
		if(optr.getDept_id().equals(newDeptId)){
			throw new ComponentException("相同部门不需要更换!");
		}
		
		SOptr newSOptr = new SOptr();
		BeanUtils.copyProperties(optr, newSOptr);
		newSOptr.setStatus(StatusConstants.INVALID);
		sOptrDao.update(newSOptr);
		
		//记录原操作员资料修改 状态变更
		createUpdateOptrChange(doneCode, optr, newSOptr, WebOptr.getOptr().getOptr_id(), null, null, null, null);
		
		//该操作员在新部门是否有失效记录
		//如果有，不新建，直接启用
		SOptr invalidOptr = sOptrDao.queryInvalidOptr(newDeptId, optr.getLogin_name());
		SOptrDto newOptr = new SOptrDto();
		if(invalidOptr != null){
			BeanUtils.copyProperties(invalidOptr, newOptr);
			newOptr.setStatus(StatusConstants.ACTIVE);
			newOptr.setPassword(optr.getPassword());
			sOptrDao.update(newOptr);
			//记录操作员资料修改记录
			createUpdateOptrChange(doneCode, invalidOptr, newOptr, WebOptr.getOptr().getOptr_id(), null, null, null, null);
		}else{
			SOptrDto newOptrDto = new SOptrDto();
			BeanUtils.copyProperties(optr, newOptrDto);
			newOptrDto.setDept_id(newDeptId);
			newOptrDto.setStatus(StatusConstants.ACTIVE);
			newOptr = setSaveOptrInfo(newOptrDto, WebOptr.getOptr());
			newOptr.setPassword(optr.getPassword());
			sOptrDao.save(newOptr);
			
			//记录操作员资料修改记录
			createSaveOptrChange(doneCode, newOptr, newOptr, WebOptr.getOptr().getOptr_id(), null, null);
		}
		
		//新操作员 复制 原操作员 权限、资源
		List<SOptrRole> newOptrRole = sOptrRoleDao.copyOptrRole(oldOptrId, newOptr.getOptr_id());
		List<SOptrResource> newOptrResource =sOptrResourceDao.copyOptrResource(oldOptrId, newOptr.getOptr_id());
		//记录新操作员 角色、资源 异动记录
		createUpdateOptrChange(doneCode, newOptr, newOptr, WebOptr.getOptr().getOptr_id(), null, newOptrRole, null, newOptrResource);
		
		//原操作员角色、资源
		List<SRoleDto> oldRoleList = sOptrRoleDao.queryOptrRole(oldOptrId);
		List<SOptrResource> oldResList = sOptrResourceDao.queryByOptr(oldOptrId);
		//记录原操作员 角色、资源 异动记录
		createUpdateOptrChange(doneCode, optr, optr, WebOptr.getOptr().getOptr_id(), oldRoleList, null, oldResList, null);
		
		//删除已失效操作员 权限、资源
		sOptrRoleDao.deteleOptrRoleByOptr(oldOptrId);
		sOptrResourceDao.delete(oldOptrId);
		
	}
	
	/**
	 * 验证操作员是否已经存在
	 */
	public boolean validLoginName(String login_name) throws Exception {
		return !sOptrDao.isOptrToken(login_name);
	}

	/**
	 * 修改操作员密码
	 */
	public boolean updateOptrData(String optrId, String password,
			String subSystemId) throws Exception {
		List<SOptr> optrList = sOptrDao.getSameOptrById(optrId);
		for(SOptr optrDto :optrList){
			SOptr soptr = new SOptr();
			soptr.setOptr_id(optrDto.getOptr_id());
			int sues = -1;
			if (StringHelper.isNotEmpty(password)) {
				soptr.setPassword(MD5.EncodePassword(password));
			}
			if (StringHelper.isNotEmpty(subSystemId)) {
				soptr.setLogin_sys_id(subSystemId);
			}
			sues = sOptrDao.update(soptr)[0];
			if (sues>=0 || sues == -2){
				continue;
			}else{
				return false;
			}
		}
		return true ;
	}

	/**
	 * 操作员注销启用
	 */
	public boolean updateOptrStatus(String optr_id, String statusId)
			throws Exception {
		
		SOptr newOptr = sOptrDao.findByKey(optr_id);
		String status = StatusConstants.ACTIVE;
		
		if (statusId.equals(StatusConstants.ACTIVE)) {
			status = StatusConstants.INVALID;
		}
		if (statusId.equals(StatusConstants.INVALID)) {
			status = StatusConstants.ACTIVE;
			if (!validLoginName(newOptr.getLogin_name())) {
				throw new ComponentException("工号重复!");
			}
		}
		
		boolean updateSuccess = sOptrDao.updateOptrStatus(optr_id, status) > 0;
		if(updateSuccess){
			SOptr oldOptr = new SOptr();
			oldOptr.setStatus(statusId);
			
			newOptr.setStatus(status);
			
			SSysChange optrChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), getDoneCOde(),
					optr_id, newOptr.getOptr_name(), "操作员基本资料修改", BeanHelper.beanchange(oldOptr, newOptr, "status"), WebOptr.getOptr().getOptr_id(), new Date());
			sSysChangeDao.save(optrChange);
		}
		return updateSuccess;
	}

	/**
	 * 删除角色与操作员关系( by Optr_id)
	 */
	public boolean deteleOptrRoleByOptr(String optr_id) throws Exception {
		return sOptrRoleDao.deteleOptrRoleByOptr(optr_id);
	}

	/**
	 * 保存角色操作员关系(单个Optr_id 对应多个Role_id)
	 */
	public boolean saveOptrRoleByRoles(String[] role_id, String optr_id)
			throws Exception {
		return sOptrRoleDao.saveOptrRoleByRoles(role_id, optr_id);
	}

	// ------------------机构管理----------------------------------------------------------------------------------

	/**
	 * 根据操作员ID查询部门
	 * 
	 * @param countyId
	 * @return
	 * @throws Exception 
	 */
	public List<SDeptDto> queryDeptByCountyId(SOptr optr) throws Exception {
		List<SDeptDto> sdList = sDeptDao.queryDeptByCountyId(optr.getDept_id());
		String[] deptArr = CollectionHelper.converValueToArray(sdList,"dept_id");
		List<TAddressDto> taList = tAddressDao.queryDeptAddr(deptArr);
		Map<String, List<TAddressDto>> taMap = CollectionHelper.converToMap(taList, "dept_id");
		for(SDeptDto dto: sdList){
			List<TAddressDto> adList = taMap.get(dto.getDept_id());
			if(CollectionHelper.isNotEmpty(adList)){
				String address_name_src = "";
				String address_id_src = "";
				for(TAddressDto ta : adList){
					address_name_src+=ta.getAddr_name()+",";
					address_id_src += ta.getAddr_id()+",";
				}
				dto.setAddress_name_src(StringHelper.delEndChar(address_name_src, 1));
				dto.setAddress_id_src(StringHelper.delEndChar(address_id_src, 1));
			}
			
		}
		return sdList;
	}
	
	/**
	 * 根据部门编号查找区域树
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<TreeDto> queryAdrToDeptTree(String deptId) throws Exception {
		List<TreeDto> valueList = null;
		SDept dept = sDeptDao.findByKey(deptId);
		List<TreeDto> addrList = tAddressDao.getAddrByCountyId(dept.getCounty_id());
		if (StringHelper.isNotEmpty(deptId)) {
			valueList = tAddressDao.getAddrByDeptd(deptId);
		}
		for (int i = 0; i < addrList.size(); i++) {
			addrList.get(i).setChecked(false);
			if (valueList != null && !"null".equals(valueList)) {
				for (int j = 0; j < valueList.size(); j++) {
					if (addrList.get(i).getId().equals(valueList.get(j).getId())) {
						addrList.get(i).setChecked(true);
					}
				}
			}
		}
		return addrList;
	}
	
	/**
	 * 查询可绑定到部门的address.包括已经能够被本部门绑定的.
	 * @param countyId
	 * @param deptId 
	 * @return
	 * @throws Exception
	 */
	public List<TAddress> queryBindableAddr(String countyId, String deptId) throws Exception {
		return tAddressDao.queryBindableAddr(countyId,deptId);
	}
	
	public List<OptrDto> queryOptrByCountyId(String countyId) throws Exception {
		return sOptrDao.queryOptrByCountyId(countyId);
	}

	/**
	 * 保存机构信息
	 */
	public void saveDept(SDept dept, SOptr optr) throws Exception {
		dept.setDept_id(sDeptDao.findSequence().toString());
		dept.setCreator(optr.getLogin_name());
		dept.setCreate_time(new Date());
		dept.setStatus(StatusConstants.ACTIVE);

		// 将分公司的area,county赋值给部门
		SDept pDept = sDeptDao.findByKey(dept.getDept_pid());
		dept.setArea_id(pDept.getArea_id());
		dept.setCounty_id(pDept.getCounty_id());

		sDeptDao.save(dept);
	}

	/**
	 * 更新机构信息
	 */
	public void updateDept(SDept dept) throws Exception {
		sDeptDao.update(dept);
//		memoryComponent.addDictSignal(DictKey.OPTR.toString());
	}

	/**
	 * 删除机构
	 */
	public String deleteDept(String deptId) throws Exception {
		List<SOptr> optrList = sDeptDao.queryOptrByDeptId(deptId);
		if (optrList.size() > 0) {
			return "{'success':false,'msg':'该部门下面还有员工，暂不能删除。'}";
		} else {
			boolean flag = rInvoiceDao.isExistsInvoiceByDepotId(deptId);
			if (flag) {
				return "{'success':false,'msg':'该部门下有发票，暂不能删除。'}";
			}
			flag = rDeviceDao.isExistsDeviceByDepotId(deptId);
			if (flag) {
				return "{'success':false,'msg':'该部门下有设备，暂不能删除。'}";
			}

			SDept dept = sDeptDao.findByKey(deptId);
			dept.setStatus(StatusConstants.INVALID);
			sDeptDao.update(dept);
			return "{'success':true}";
		}
	}
	
	/**
	 * 保存小区、业务对应关系.
	 * @param bindType 
	 * @param optr 
	 * @return
	 * @throws Exception
	 */
	public void saveBusiCodesToDept(String[] busiCodes, String deptId, String bindType) throws Exception{
		List<SDeptBusicode> list = new ArrayList<SDeptBusicode>();
		for(String busiCode:busiCodes){
			if(StringHelper.isEmpty(busiCode)){
				continue;
			}
			SDeptBusicode db = new SDeptBusicode();
			db.setDept_id(deptId);
			db.setBind_type(bindType);
			db.setBusi_code(busiCode);
			list.add(db);
		}
		sdeptBusicodeDao.deleteBySdeptId(deptId);
		if(list.size()>0){
			sdeptBusicodeDao.save(list.toArray( new SDeptBusicode [list.size()]));
		}
		memoryComponent.addDictSignal(DictKey.DEPT_BUSICODE.toString());
	}
	
	/**
	 * 查询小区、业务对应关系.
	 * @param optrId 
	 * @return
	 * @throws Exception
	 */
	public List<SDeptBusicode> queryDeptBusiCodes(String deptId) throws Exception{
		return sdeptBusicodeDao.queryDeptBusiCodes(deptId);
	}
	
	
	/**
	 * 更新部门下关联的区域
	 * @param addrIds
	 * @param deptId
	 * @throws Exception
	 */
	public void saveAddrToDept(String[] addrIds,String deptId) throws Exception {
		//原先的区域信息
		List<SDeptAddr> oldList = sDeptAddrDao.getAddrByDept(deptId);
		List<String> addrIdList = new ArrayList<String>();
		if(addrIds != null){
			addrIdList = new ArrayList<String>(Arrays.asList(addrIds));
			for(int i=oldList.size()-1;i>=0;i--){
				if(addrIdList.contains(oldList.get(i).getAddr_id())){
					addrIdList.remove(oldList.get(i).getAddr_id());
					oldList.remove(i);
				}
			}
		}
		//删除取消关联的区域
		if(oldList.size()>0){
			sDeptAddrDao.removeByDeptId(CollectionHelper.converValueToArray(oldList,"addr_id"),deptId);
		}
		
		//保存新增关联的区域
		SDept dept = sDeptDao.findByKey(deptId);
		List<SDeptAddr> sList = new ArrayList<SDeptAddr>();
		for(String id :addrIdList){
			SDeptAddr depAddr = new SDeptAddr();
			depAddr.setCounty_id(dept.getCounty_id());
			depAddr.setAddr_id(id);
			depAddr.setDept_id(deptId);
			depAddr.setTree_level(2);
			sList.add(depAddr);
		}
		sDeptAddrDao.save(sList.toArray(new SDeptAddr[sList.size()]));
	}
	
	
	/**
	 * 更新部门下关联的区域
	 * @param addrIds
	 * @param deptId
	 * @throws Exception
	 */
	public void saveAddr2Dept(String[] addrIds,String deptId) throws Exception {
		//原先的区域信息
		List<SDeptAddr> oldList = sDeptAddrDao.getAddrByDept(deptId);
		List<String> addrIdList = new ArrayList<String>();
		if(addrIds != null){
			addrIdList = new ArrayList<String>(Arrays.asList(addrIds));
			for(int i=oldList.size()-1;i>=0;i--){
				if(addrIdList.contains(oldList.get(i).getAddr_id())){
					addrIdList.remove(oldList.get(i).getAddr_id());
					oldList.remove(i);
				}
			}
		}
		//删除取消关联的区域
		if(oldList.size()>0){
			sDeptAddrDao.removeByDeptId(CollectionHelper.converValueToArray(oldList,"addr_id"),deptId);
		}
		
		//保存新增关联的区域
		SDept dept = sDeptDao.findByKey(deptId);
		List<SDeptAddr> sList = new ArrayList<SDeptAddr>();
		for(String id :addrIdList){
			SDeptAddr depAddr = new SDeptAddr();
			depAddr.setCounty_id(dept.getCounty_id());
			depAddr.setAddr_id(id);
			depAddr.setDept_id(deptId);
			depAddr.setTree_level(2);
			sList.add(depAddr);
		}
		sDeptAddrDao.save(sList.toArray(new SDeptAddr[sList.size()]));
	}
	
	

	// -----------------角色管理-----------------------------------------------------------------------------------

	/**
	 *地区操作员树
	 */
	public List<TreeDto> goToOptrTree(SRoleDto role, SOptr optr)
			throws Exception {
		List<TreeDto> countys = null;
		List<TreeDto> valueList = null;
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY
				.toString());
		countys = sCountyDao.getCountyTreeAndOptr(role, optr, dataRight);
		if (StringHelper.isNotEmpty(role.getRole_id())) {
			valueList = sOptrDao.getOptrByRoleId(role.getRole_id());
		}
		for (int i = 0; i < countys.size(); i++) {
			countys.get(i).setChecked(false);
			if (valueList != null && !"null".equals(valueList)) {
				for (int j = 0; j < valueList.size(); j++) {
					if (countys.get(i).getId().equals(valueList.get(j).getId())) {
						countys.get(i).setChecked(true);
					}
				}
			}
		}
		return countys;
	}

	public boolean saveRoleToOptrs(String roleId, String[] optrIds, SOptr optr)
			throws Exception {
		if (StringHelper.isEmpty(roleId)) {
			return false;
		}
		SRole role = sRoleDao.findByKey(roleId);
		String countyDataRight = this.queryDataRightCon(optr,
				DataRight.CHANGE_COUNTY.toString());
		if (optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
			countyDataRight = SystemConstants.DEFAULT_DATA_RIGHT;
		}

		List<SOptrRole> oldRoleOptrList = sOptrRoleDao.getOptrRole(roleId,null);
		Map<String, SOptrRole> oldRoleOptrMap = CollectionHelper.converToMapSingle(oldRoleOptrList, "optr_id");
		Map<String, List<SOptrRole>> oldOptrRoleMap = new HashMap<String, List<SOptrRole>>();//以optr_id为键记录当前有的角色集合
		for(SOptrRole sr:oldRoleOptrList){
			oldOptrRoleMap.put(sr.getOptr_id(), sOptrRoleDao.getOptrRole(null,sr.getOptr_id()));
		}
		for(String optrid:optrIds){
			if(!oldOptrRoleMap.containsKey(optrid)){
				oldOptrRoleMap.put(optrid, sOptrRoleDao.getOptrRole(null,optrid));
			}
		}
		
		sOptrRoleDao.deteleOptrRoleByRole(roleId, countyDataRight);
		if (optrIds != null) {
			// 删除操作员同种类型的权限
			sOptrRoleDao.deteleOptrAllByRoleCounty(optrIds, role);
			sOptrRoleDao.saveOptrRoleByOptrs(optrIds, roleId);
		}
		
		List<SOptrRole> newRoleOptrList = sOptrRoleDao.getOptrRole(roleId,null);
		Map<String, SOptrRole> newRoleOptrMap = CollectionHelper.converToMapSingle(newRoleOptrList, "optr_id");
		
		Map<String, List<SOptrRole>> newOptrRoleMap = new HashMap<String, List<SOptrRole>>();//以optr_id为键记录当前有的角色集合
		for(SOptrRole sr:newRoleOptrList){
			newOptrRoleMap.put(sr.getOptr_id(), sOptrRoleDao.getOptrRole(null,sr.getOptr_id()));
		}
		for(SOptrRole sr:oldRoleOptrList){
			if(!newOptrRoleMap.containsKey(sr.getOptr_id())){
				newOptrRoleMap.put(sr.getOptr_id(), sOptrRoleDao.getOptrRole(null,sr.getOptr_id()));
			}
		}
		
		//记录异动		
		String listChange = BeanHelper.listchange(oldRoleOptrList, newRoleOptrList, "optr_id","optr_name");
		
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		if(StringHelper.isNotEmpty(listChange)){
			int doneCOde = getDoneCOde();
			
				Map<String, SOptrRole> temp = new HashMap<String, SOptrRole>();
				
				temp.putAll(oldRoleOptrMap);
				temp.putAll(newRoleOptrMap);
				
				Map<String, SOptrRole> changedOptrMap = new HashMap<String, SOptrRole>();
				
				for(Entry<String, SOptrRole> entry:temp.entrySet()){
					String key = entry.getKey();
					//新旧都有的,说明没变化
					if(oldRoleOptrMap.containsKey(key) && newRoleOptrMap.containsKey(key)){
						continue;
					}
					SOptrRole value = entry.getValue();
					changedOptrMap.put(key, value);
					
					List<SOptrRole> oldOptrRoleList = oldOptrRoleMap.get(key);
					
					List<SOptrRole> newOptrRoleList = newOptrRoleMap.get(key);
					
					String optrRoleChangeInfo = BeanHelper.listchange(oldOptrRoleList, newOptrRoleList, "role_name", null).replaceFirst("role_name", "role_id");
					
					SSysChange optrChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCOde, 
							key, value.getOptr_name(), "角色管理分配操作员", optrRoleChangeInfo,
							optr.getOptr_id(), new Date());
					changes.add(optrChange);
				}
				
				
				sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		}
		return true;
	}

	/**
	 *增加或减少权限TO操作员
	 */
	public boolean saveResourceToOptrs(String[] resourceIds, String optrId)
			throws Exception {
		
		/**
		 * 两个异动，一个是功能禁用，一个是启用  
		 */
		List<SOptrResource> oldOptrResList = sOptrResourceDao.queryByOptr(optrId);
		Map<String, List<SOptrResource>> oldOptrResMap = CollectionHelper.converToMap(oldOptrResList, "more_or_less");
		
		List<SOptrResource> oldResourceList = sOptrResourceDao.getResourceByRole(optrId);
		List<String> newlist = null;
		if (resourceIds != null && resourceIds.length > 0) {
			newlist = new ArrayList<String>(Arrays.asList(resourceIds));
		}
		List<SOptrResource> toOldList = new ArrayList<SOptrResource>();
		List<SOptrResource> toNewList = new ArrayList<SOptrResource>();
		if (newlist != null && newlist.size() > 0) {
			for (int i = newlist.size() - 1; i >= 0; i--) {
				if (!"null".equals(oldResourceList) && oldResourceList != null) {
					boolean ck = false;
					for (int k = oldResourceList.size() - 1; k >= 0; k--) {
						if (newlist.get(i).toString().equals(
								oldResourceList.get(k).getRes_id())) {
							oldResourceList.remove(k);
							ck = true;
						}
					}
					if (ck) {
						newlist.remove(i);
					}
				}
			}
			for (String dto : newlist) {
				SOptrResource dao = new SOptrResource();
				dao.setRes_id(dto.toString());
				dao.setOptr_id(optrId);
				dao.setMore_or_less(1);
				toNewList.add(dao);
			}
		}
		for (SOptrResource dto : oldResourceList) {
			SOptrResource dao = new SOptrResource();
			dao.setRes_id(dto.getRes_id());
			dao.setOptr_id(optrId);
			dao.setMore_or_less(0);
			toOldList.add(dao);
		}
		// 删除该操作员所定义的功能
		sOptrResourceDao.delete(optrId);
		// 保存禁用的功能
		sOptrResourceDao.save(toOldList.toArray(new SOptrResource[toOldList.size()]));
		// 保存使用的功能
		sOptrResourceDao.save(toNewList.toArray(new SOptrResource[toNewList.size()]));
		
		//记录异动
		List<SOptrResource> newOptrResList = sOptrResourceDao.queryByOptr(optrId);
		Map<String, List<SOptrResource>> newOptrResMap = CollectionHelper.converToMap(newOptrResList, "more_or_less");
		
		List<SOptrResource> oldAddList = oldOptrResMap.get("1");
		List<SOptrResource> newAddList = newOptrResMap.get("1");
		
		int doneCOde = getDoneCOde();
		Date createTime = new Date();
		
		SOptr theModifiedOptr = sOptrDao.findByKey(optrId); 
		
		if(oldAddList != null || newAddList != null ){	
			String listchange = BeanHelper.listchange(oldAddList, newAddList, "res_name", null);
			
			if(StringHelper.isNotEmpty(listchange)){
				SSysChange addChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCOde, 
						optrId, theModifiedOptr.getOptr_name(), "操作员管理授予菜单资源", listchange.replaceFirst("res_name", "res_id"),
						WebOptr.getOptr().getOptr_id(), createTime);
				sSysChangeDao.save(addChange);
			}
		}

		
		List<SOptrResource> oldRemoveList = oldOptrResMap.get("0");
		List<SOptrResource> newRemoveList = newOptrResMap.get("0");
		
		if(oldRemoveList != null || newRemoveList != null ){
			String listchange = BeanHelper.listchange(oldRemoveList, newRemoveList, "res_name", null);
			if(StringHelper.isNotEmpty(listchange)){
				SSysChange removeChange = new SSysChange(SysChangeType.OPTRCONFIG.toString(),doneCOde, 
						optrId, theModifiedOptr.getOptr_name(), "操作员管理禁用菜单资源", listchange.replaceFirst("res_name", "res_id"),
						WebOptr.getOptr().getOptr_id(), createTime);
				sSysChangeDao.save(removeChange);
			}
			
			
		}
		
		return true;
	}

	/**
	 * 查询角色信息
	 */
	public Pager<SRole> queryRoles(Integer start, Integer limit,
			String keyword, SOptr optr) throws Exception {
		String roleDataRight = this.queryDataRightCon(optr, DataRight.ROLE
				.toString());
		List<SItemvalue> list = new ArrayList<SItemvalue>();
		List<TRuleDefine> rulelist = new ArrayList<TRuleDefine>();
		String[] arry = null;
		if (StringHelper.isNotEmpty(keyword)) {
			list = sItemvalueDao.findValueByName(keyword.toUpperCase());
			rulelist = sRoleDao.getRule(keyword);
			if (rulelist.size() > 0) {
				arry = new String[rulelist.size()];
				;
				for (int i = 0; i < rulelist.size(); i++) {
					arry[i] = rulelist.get(i).getRule_id();
				}
			}
		}
		String countyDataRight = this.queryDataRightCon(optr,
				DataRight.CHANGE_COUNTY.toString());
		return sRoleDao.queryAll(start, limit, list, arry, keyword, optr
				.getCounty_id(), countyDataRight, roleDataRight);
	}

	/**
	 * 根据子系统编号查询系统功能除开角色编号role_id下的系统功能
	 */
	public List<SRoleDto> getResBySystemId(String doneId, String roleId,
			SOptr optr) throws Exception {
		List<SRoleDto> allList = new ArrayList<SRoleDto>();
		List<SRoleDto> roleList = new ArrayList<SRoleDto>();
		if (optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
			allList = sRoleResourceDao.getResBySystemId(doneId);
		} else {
			allList = sRoleResourceDao
					.getResByoptrId(doneId, optr.getOptr_id());
		}

		if (StringHelper.isNotEmpty(roleId)) {
			roleList = findRoleResource(doneId, roleId);
			if (!"null".equals(roleList) && roleList != null) {
				for (int i = allList.size() - 1; i >= 0; i--) {
					boolean ck = false;
					for (int k = roleList.size() - 1; k >= 0; k--) {
						if (allList.get(i).getRes_id().equals(
								roleList.get(k).getRes_id())) {
							roleList.remove(k);
							ck = true;
						}
					}
					if (ck) {
						allList.remove(i);
					}
				}
			}
		}
		return allList;
	}

	/**
	 * 根据角色编号role_id和子系统编号查询系统功能
	 */
	public List<SRoleDto> findRoleResource(String doneId, String roleId)
			throws Exception {
		return sRoleResourceDao.findRoleResource(doneId, roleId);
	}

	/**
	 * 保存角色信息
	 */
	public void saveRole(SRole role, String[] resIds, String[] countyIds)
			throws Exception {
		int doneCOde = getDoneCOde();
		Date createTime = new Date();
		
		role.setRole_id(sRoleDao.findSequence().toString());
		// 配置适用地区
		sRoleDao.save(role);
		saveRoleCountyId(WebOptr.getOptr(),role.getRole_id(), countyIds,doneCOde);
		if (!"null".equals(resIds) && resIds != null) {
			sRoleResourceDao.addRoleRes(resIds, role.getRole_id());
		}
		SRole newRole = sRoleDao.findByKey(role.getRole_id());
		
		SSysChange roleChange  = new SSysChange(SysChangeType.ROLE.toString(), doneCOde ,
				role.getRole_id(), newRole.getRole_name(), "角色基本配置异动", BeanHelper.beanchange(null, newRole), WebOptr.getOptr().getOptr_id(), createTime);
		
		sSysChangeDao.save(roleChange);
		
		if (!"null".equals(resIds) && resIds != null) {
			List<SResource> resources = sResourceDao.queryByResIds(resIds);
			String content = BeanHelper.listchange(null, resources, "res_name", null).replaceFirst("res_name", "res_id");
			SSysChange resChange  = new SSysChange(SysChangeType.ROLE.toString(), doneCOde ,
					role.getRole_id(), newRole.getRole_name(), "角色资源异动", content, WebOptr.getOptr().getOptr_id(), createTime);
			sSysChangeDao.save(resChange);
		}
	}

	private void saveRoleCountyId(SOptr operator, String roleId, String[] countyIds,int doneCode) throws Exception {
		List<TreeDto> oldRoleCountyById = sRoleCountyDao.getRoleCountyById(roleId);
		sRoleCountyDao.deletebyRoleId(roleId);
		if (null != countyIds && countyIds.length > 0) {
			sRoleCountyDao.saveRoleCountyId(roleId, countyIds);
		}
		
		List<TreeDto> newRoleCountyById = sRoleCountyDao.getRoleCountyById(roleId);
		
		String listchange = BeanHelper.listchange(oldRoleCountyById, newRoleCountyById,"id", null);
		
		if(StringHelper.isNotEmpty(listchange)){
			String roleName = sRoleDao.findByKey(roleId).getRole_name();
			SSysChange roleChange = new SSysChange(SysChangeType.ROLE.toString(),doneCode, 
					roleId, roleName, "角色基本配置适用地区异动", 
							listchange, operator.getOptr_id(),
					new Date());
			
			sSysChangeDao.save(roleChange);
		}
		
	}

	/**
	 * 更新角色信息
	 */
	public void updateRole(SRole role, String[] resIds, String[] countyIds)
			throws Exception {
		SRole oldRole = sRoleDao.findByKey(role.getRole_id());
		List<OptrBase> ors = sSysChangeDao.getOptrRole(role.getRole_id());
		
		List<SRoleResource> oldRes = sRoleResourceDao.findRoleResource(role.getRole_id());
		
		Date now = new Date();
		int doneCOde = getDoneCOde();
		
		sRoleDao.update(role);
		// 配置适用地区
		saveRoleCountyId(WebOptr.getOptr(),role.getRole_id(), countyIds,doneCOde);
		if (!"null".equals(resIds) && resIds != null) {
			sRoleResourceDao.deleteRoleRes(role.getRole_id());
			sRoleResourceDao.addRoleRes(resIds, role.getRole_id());
		} else {
			sRoleResourceDao.deleteRoleRes(role.getRole_id());
		}
		
		
		SRole newRole = sRoleDao.findByKey(role.getRole_id());
		List<SRoleResource> newRes = sRoleResourceDao.findRoleResource(role.getRole_id());
		
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		String roleChangeInfo = BeanHelper.beanchange(oldRole, newRole);
		
		//角色基本配置的变更
		if(StringHelper.isNotEmpty(roleChangeInfo)){
			SSysChange roleChange  = new SSysChange(SysChangeType.ROLE.toString(), doneCOde ,
					role.getRole_id(), newRole.getRole_name(), "角色基本配置异动", roleChangeInfo, WebOptr.getOptr().getOptr_id(), now);
			changes.add(roleChange);
		}
		
		String resChangeInfo = BeanHelper.listchange(oldRes, newRes, "res_name", null);///DictKey.RESOURCE
		
		if(StringHelper.isNotEmpty(resChangeInfo)){
			//记录角色本身的资源变更记录
			SSysChange roleResChange  = new SSysChange(SysChangeType.ROLE.toString(), doneCOde ,
					role.getRole_id(), role.getRole_name(), "角色资源配置异动", resChangeInfo.replaceFirst("res_name", "res_id"), 
					WebOptr.getOptr().getOptr_id(), now);
			changes.add(roleResChange);
			// s_optr_role 给有该角色的操作员 插入异动记录
			for(OptrBase orz:ors){
				SSysChange optrChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), doneCOde ,
						orz.getOptr_id(), orz.getOptr_name(), "角色资源配置异动", resChangeInfo.replaceFirst("res_name", "res_id"), 
						WebOptr.getOptr().getOptr_id(), now);
				changes.add(optrChange);
			}
		}
		
		sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
	}

	/**
	 * 删除角色信息
	 * @param operator 
	 */
	public boolean deleteRole(SRole role) throws Exception {
		List<SOptr> optrList = sOptrDao.getOptrRole(role.getRole_id());
		String info = "";
		if (optrList.size() > 0) {
			for (SOptr optr : optrList) {
				info += optr.getLogin_name() + ",";
			}
			info = StringHelper.delEndChar(info, 1);
			throw new ComponentException("该角色已经分配给操作员【" + info + "】无法删除!");
		}
		SRole oldRole = sRoleDao.findByKey(role.getRole_id());
		
		int sues = sRoleDao.remove(role.getRole_id())[0];

		if (sues >= 0 || sues == -2) {
			//查看 resIds
			SSysChange roleChange  = new SSysChange(SysChangeType.ROLE.toString(), getDoneCOde() ,
					role.getRole_id(), oldRole.getRole_name(), "角色基本配置异动", BeanHelper.beanchange(oldRole, null), WebOptr.getOptr().getOptr_id(), new Date());
			sSysChangeDao.save(roleChange);
			return true;
		}
		return false;
	}

	/**
	 * 保存角色编号与操作员关系(单个Role_id 对应多个Optr_id)
	 */
	public boolean saveOptrRoleByOptrs(String[] optr_id, String role_id)
			throws Exception {
		return sOptrRoleDao.saveOptrRoleByOptrs(optr_id, role_id);
	}

	/**
	 * (根据Role_id,optr) 删除角色与操作员关系
	 * @param operator 
	 * 
	 * @throws Exception
	 */
	public boolean deteleOptrRole(String roleId, String optrId)
			throws Exception {
		List<SRoleDto> oldRoles = queryOptrRole(optrId);
		boolean deteleOptrRole = sOptrRoleDao.deteleOptrRole(roleId, optrId);
		if(deteleOptrRole){
			SOptr theOptr = sOptrDao.findByKey(optrId);
			List<SRoleDto> newRoles = queryOptrRole(optrId);
			String listchange = BeanHelper.listchange(oldRoles, newRoles, "role_name", null).replaceFirst("role_name", "role_id");
			SSysChange roleChange  = new SSysChange(SysChangeType.OPTRCONFIG.toString(), getDoneCOde() ,
					optrId, theOptr.getOptr_name(), "操作员管理角色清单", listchange, WebOptr.getOptr().getOptr_id(), new Date());
			
			sSysChangeDao.save(roleChange);
		}
		
		
		
		return deteleOptrRole;
	}

	// ---------菜单资源管理----------------------------------------------------------------------------
	/**
	 * 查询菜单资源信息
	 */
	public Pager<SResource> queryResources(Integer start, Integer limit,
			String keyword, String pid) throws Exception {
		return sResourceDao.query(start, limit, keyword, pid);
	}

	/**
	 * 添加资源信息
	 * 
	 * @param resource
	 */
	public boolean saveResource(SResource resource) throws Exception {
		resource.setRes_id(sResourceDao.getResourceID());
		resource.setRes_status(StatusConstants.ACTIVE);
		int sues = sResourceDao.save(resource)[0];
		if (sues >= 0 || sues == -2) {
			return true;
		}
		return false;
	}

	/**
	 * 更新菜单资源
	 * 
	 * @param resource
	 */
	public boolean updateResource(SResource resource) throws Exception {
		int sues = sResourceDao.update(resource)[0];
		if (sues >= 0 || sues == -2) {
			return true;
		}
		return false;
	}

	/**
	 * 删除菜单资源
	 * 
	 * @param resource
	 */
	public boolean deleteResource(SResource resource) throws Exception {
		resource.setRes_status(StatusConstants.INVALID);
		int sues = sResourceDao.update(resource)[0];
		if (sues >= 0 || sues == -2) {
			return true;
		}
		return false;
	}

	public SDeptDao getSDeptDao() {
		return sDeptDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}
	
	public void setSdeptBusicodeDao(SdeptBusicodeDao dao) {
		sdeptBusicodeDao = dao;
	}

	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}

	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}

	public SOptrRoleDao getSOptrRoleDao() {
		return sOptrRoleDao;
	}

	public void setSOptrRoleDao(SOptrRoleDao optrRoleDao) {
		sOptrRoleDao = optrRoleDao;
	}

	public SAreaDao getSAreaDao() {
		return sAreaDao;
	}

	public void setSAreaDao(SAreaDao areaDao) {
		sAreaDao = areaDao;
	}

	public void setPProdDao(PProdDao prodDao) {
		pProdDao = prodDao;
	}

	public PProdDao getPProdDao() {
		return pProdDao;
	}

	public void setSSubSystemDao(SSubSystemDao subSystemDao) {
		sSubSystemDao = subSystemDao;
	}

	public SOptrResourceDao getSOptrResourceDao() {
		return sOptrResourceDao;
	}

	public void setSOptrResourceDao(SOptrResourceDao optrResourceDao) {
		this.sOptrResourceDao = optrResourceDao;
	}

	public void setRInvoiceDao(RInvoiceDao invoiceDao) {
		rInvoiceDao = invoiceDao;
	}

	public void setRDeviceDao(RDeviceDao deviceDao) {
		rDeviceDao = deviceDao;
	}

	public void setSRoleCountyDao(SRoleCountyDao roleCountyDao) {
		sRoleCountyDao = roleCountyDao;
	}
	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}

	public void setSDeptAddrDao(SDeptAddrDao deptAddrDao) {
		sDeptAddrDao = deptAddrDao;
	}
}
