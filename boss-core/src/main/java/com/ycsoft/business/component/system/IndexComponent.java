package com.ycsoft.business.component.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.beans.system.SDeptBusicode;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.system.SBulletinDao;
import com.ycsoft.business.dao.system.SDeptAddrDao;
import com.ycsoft.business.dao.system.SItemvalueDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dao.system.SOptrRoleDao;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dao.system.SdeptBusicodeDao;
import com.ycsoft.business.dto.system.OptrDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SBulletinDto;
import com.ycsoft.sysmanager.dto.system.SRoleDto;

/**
 *
 * 首页显示的管理器
 *
 * @author hh
 * @date Dec 29, 2009 4:02:49 PM
 */
@Component
public class IndexComponent extends BaseBusiComponent {

	private SResourceDao sResourceDao;
	private SOptrDao sOptrDao;
	private SOptrRoleDao sOptrRoleDao;
	private SBulletinDao sBulletinDao;
	private SdeptBusicodeDao sdeptBusicodeDao;
	private SDeptAddrDao sDeptAddrDao;
	
	public List<OptrDto> queryBusiOptr() throws Exception {
		return sOptrDao.queryBusiOptrByCountyId(getOptr().getCounty_id());
//		String dataRight ="";
//		try {
//			dataRight = this.queryDataRightCon(getOptr(), DataRight.BUSI_OPTR.toString());
//		} catch (Exception e) {
//			return null;
//		}
//		if (DataRightLevel.DEPT.toString().equals(dataRight)){
//			return sOptrDao.queryOptrByDept(getOptr().getDept_id());
//		} else if(DataRightLevel.COUNTY.toString().equals(dataRight)) {
//			return sOptrDao.queryOptrByCountyId(getOptr().getCounty_id());
//		} else if(DataRightLevel.OPTR.toString().equals(dataRight)) {
//			return sOptrDao.queryOptrByOptr(getOptr().getOptr_id());
//		}else if(DataRightLevel.ALL.toString().equals(dataRight)) {
//			return sOptrDao.queryOptrByAll();
//		}else{
//			return null;
//		}
	}

	/**
	 * 检查指定的操作员是否存在
	 *
	 * @param optr
	 */
	public SOptr checkOptrExists(String loginName, String password) throws Exception {
		SOptr _o = sOptrDao.isExists(loginName,password);
		if (null == _o) {
			return null;
		}
		return _o;
	}
	
	
	public boolean updateOptrData(String optrId,String password,String subSystemId) throws Exception{
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
	
	public List<SRoleDto> querySubSystemByOptrId(String optrId) throws Exception {
		List<SRoleDto> list = sOptrRoleDao.queryOptrRole(optrId);
		if (list.size()>0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				boolean ck = false;
				if (StringHelper.isEmpty(list.get(i).getSub_system_id())) {
					ck = true;
				}
				if (ck) {
					list.remove(i);
				}
			}
		}else{
			throw new ComponentException("操作员的配置存在问题，角色中不存在子系统!");
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public Pager<SBulletinDto> queryBulletinByOptrId(Integer start,Integer limit,SOptr optr) throws JDBCException {
		Pager<SBulletinDto> pager = sBulletinDao.queryByOptrId(start, limit,optr.getOptr_id(),optr.getDept_id());
		List<String> ids = new ArrayList<String>();
		List<SBulletinDto> result = new ArrayList<SBulletinDto>();
		List<SBulletinDto> records = pager.getRecords();
		for(SBulletinDto bul :records){
			if(!ids.contains(bul.getBulletin_id())){
				ids.add(bul.getBulletin_id());
				result.add(bul);
			}
		}
		pager.setRecords(result);
		return pager;
	}
	/**
	 * 
	 * @return
	 */
	public SBulletinDto queryUnCheckByOptrId(String optrId) throws JDBCException {
		return sBulletinDao.queryUnCheckByOptrId(optrId);
	}

	/**
	 * 加载资源菜单 busi子系统
	 *
	 * @return
	 */
	public List<SResource> findResource() throws Exception {
		return sResourceDao.findResource(getOptr().getOptr_id(),
				SystemConstants.SUB_SYSTEM_BUSI);
	}
	
	/**
	 * @param acctDate
	 * @param optrId
	 */
	public void checkUserCount(String acctDate, String addrIds) throws Exception {
		sOptrDao.checkUserCount(acctDate,addrIds,getOptr().getOptr_id(),getOptr().getDept_id());
	}
	
	
	/**
	 * @param acctDate
	 * @param optrId
	 */
	public void checkDeviceCount(String acctDate, String optrId,String deptId) {
		sOptrDao.checkDeviceCount(acctDate,optrId,deptId);
	}


	
	/**
	 * @param bulletinId
	 * @return
	 */
	public void checkBulletin(String bulletinId,String optrId) throws JDBCException {
		sBulletinDao.checkBulletin(bulletinId, optrId);
	}
	
	/**
	 * @param dept_id
	 * @return
	 */
	public List<SDeptBusicode> queryDeptBusiCode(String dept_id) throws Exception{
		return sdeptBusicodeDao.queryDeptBusiCodes(dept_id);
	}
	
	public List<SDeptAddr> queryDeptAddress(String county_id) throws Exception{
		return sDeptAddrDao.queryDeptAddressByCountyId(county_id);
	}

	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}

	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}

	public SOptrDao getSOptrDao() {
		return sOptrDao;
	}

	public void setSOptrDao(SOptrDao optrDao) {
		sOptrDao = optrDao;
	}

	/**
	 * @param itemvalueDao the sItemvalueDao to set
	 */
	public void setSItemvalueDao(SItemvalueDao itemvalueDao) {
		sItemvalueDao = itemvalueDao;
	}


	public SOptrRoleDao getSOptrRoleDao() {
		return sOptrRoleDao;
	}


	public void setSOptrRoleDao(SOptrRoleDao optrRoleDao) {
		sOptrRoleDao = optrRoleDao;
	}


	/**
	 * @param bulletinDao the sBulletinDao to set
	 */
	public void setSBulletinDao(SBulletinDao bulletinDao) {
		sBulletinDao = bulletinDao;
	}
	
	public void setSdeptBusicodeDao(SdeptBusicodeDao sdeptBusicodeDao) {
		this.sdeptBusicodeDao = sdeptBusicodeDao;
	}

	public void setSDeptAddrDao(SDeptAddrDao sDeptAddrDao) {
		this.sDeptAddrDao = sDeptAddrDao;
	}
}
