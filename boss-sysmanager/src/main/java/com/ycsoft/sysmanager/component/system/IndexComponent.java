package com.ycsoft.sysmanager.component.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SLog;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dto.core.cust.DoneCodeDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.config.VewRuleDto;
import com.ycsoft.sysmanager.dto.system.SDeptDto;
import com.ycsoft.sysmanager.dto.system.SResourceDto;

/**
 *
 * 首页显示的管理器
 * @author hh
 * @date Dec 29, 2009 4:02:49 PM
 */
@Component
public class IndexComponent extends BaseComponent{

	private SResourceDao sResourceDao;
	private TRuleDefineDao tRuleDefineDao;
	private SCountyDao sCountyDao;
	private SDeptDao sDeptDao;
	/**
	 * @param resourceDao the sResourceDao to set
	 */
	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}
	

	public List<SResource> queryMenus(String subSystemId,String optrId) throws Exception {
		return sResourceDao.findResource(optrId,subSystemId) ;
	}

	public List<SResourceDto> loadTreeMenus(String subSystemId) throws Exception {
		return sResourceDao.queryResources(subSystemId, SystemConstants.ROLE_TYPE_MENU);
	}

	/**
	 * 查询部门树
	 * @return
	 * @throws Exception
	 */
	public List<SDeptDto> queryDeptTree(SOptr optr) throws Exception{
		List<SCounty> countys = querySwitchCounty(optr);
		
		String countyIds = "";
		for(SCounty county : countys){
			String countyId = county.getCounty_id();
			if(SystemConstants.COUNTY_ALL.equals(countyId)){
				countyIds = SystemConstants.COUNTY_ALL;
				break;
			}else{
				countyIds = StringHelper.append(countyIds,countyId,",");
			}
		}
		
		List<SDeptDto> depts = sDeptDao.queryAllYYT(countyIds.split(","));
		
		return depts;
	}
	
	public List<SCounty> querySwitchCounty(SOptr optr) throws Exception{
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		return this.sCountyDao.querySwitchCounty(dataRight);
	}

	// ----------资费规则---
	public List<VewRuleDto> findBusiRule(String countyId) throws Exception {
		return tRuleDefineDao.findRuleViewDictByType("BUSI",countyId);
	}

	public List<VewRuleDto> findRentRule(String countyId) throws Exception {
		return tRuleDefineDao.findRuleViewDictByType("RENT_RULE",countyId);
	}

	public List<VewRuleDto> findUseFeeRule(String countyId) throws Exception {
		return tRuleDefineDao.findRuleViewDictByType("USEFEE",countyId);
	}

	public List<VewRuleDto> findBillRule(String countyId) throws Exception {
		return tRuleDefineDao.findRuleViewDictByType("BILL",countyId);
	}
	/**
	 * 根据数据类型查询业务规则
	 */
	public List<VewRuleDto> getTruleByDataType(String doneId,String countyId) throws Exception {
		return tRuleDefineDao.getTruleByDataType(doneId,countyId);
	}
	
	/**
	 * 查询地区操作记录
	 * @param query
	 * @param countyId
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<SLog> queryLogs(String query,String countyId,Integer start,Integer limit) throws Exception{
		return sLogDao.queryLogs(query,countyId,start,limit);
	}

	public List<DoneCodeDto> queryOnelineUserBusi(String optrId) throws Exception {
		return cDoneCodeDao.queryOnelineUserBusi(optrId);
	}
	

	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}

	
	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}
}
