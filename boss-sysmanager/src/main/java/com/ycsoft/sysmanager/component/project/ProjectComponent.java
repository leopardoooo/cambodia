package com.ycsoft.sysmanager.component.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.project.RProject;
import com.ycsoft.beans.project.RProjectAddr;
import com.ycsoft.beans.project.RProjectCounty;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.project.RProjectAddrDao;
import com.ycsoft.business.dao.project.RProjectCountyDao;
import com.ycsoft.business.dao.project.RProjectDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.project.QueryProject;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;

@Component
public class ProjectComponent extends BaseComponent {

	private RProjectDao rProjectDao;
	private RProjectAddrDao rProjectAddrDao;
	private RProjectCountyDao rProjectCountyDao;
	private TAddressDao tAddressDao;
	
	public Pager<RProject> queryProject(QueryProject queryProject,String countyId,Integer start,Integer limit) throws Exception {
		return rProjectDao.queryProject(queryProject,countyId,start,limit);
	}
	
	public String saveProject(RProject project) throws Exception {
		String result = "";
		String projectId = project.getProject_id();
		if(StringHelper.isEmpty(projectId)){
			project.setProject_id(rProjectDao.findSequence().toString());
			rProjectDao.save(project);
		}else{
			RProject p = rProjectDao.findByKey(project.getProject_id());
			//未修改项目编号，无需检查
			if(p.getProject_number().equals(project.getProject_number())){
				project.setChange_date(new Date());
				rProjectDao.update(project);
			}else{
				//修改项目时，是否有小区关联
				List<RProjectAddr> paList = rProjectAddrDao.queryAddrByProjectId(project.getProject_id());
				List<RProject> list = null;
				
				//有小区关联，检查项目编号顺序码是否有重复
				if(paList != null && paList.size() > 0){
					List<String> addrIdList = new ArrayList<String>();
					for(RProjectAddr pa : paList){
						addrIdList.add(pa.getAddr_id());
					}
					list = rProjectDao.checkProjectOrderCode(project
							.getProject_id(), project.getProject_number(), addrIdList.toArray(new String[addrIdList.size()]));
					//没有重复的，修改项目信息
					if(list == null || list.size() == 0){
						project.setChange_date(new Date());
						rProjectDao.update(project);
					}else{
						//有重复的，给予提示
						for(RProject rp : list){
							result += rp.getAddr_name()+",";
						}
						result = result.substring(0,result.length()-1);
					}
				}else{
					//没有小区关联，直接修改
					project.setChange_date(new Date());
					rProjectDao.update(project);
				}
			}
		}
		return result;
	}
	
	/**
	 * 项目小区关联
	 * @param projectId
	 * @param addrIds
	 * @throws Exception
	 */
	public String saveLinkedProject(String projectId, String addrIds) throws Exception {
		String[] addrIdArr = addrIds.split(",");
		RProject project = rProjectDao.findByKey(projectId);
		List<RProject> list = rProjectDao.checkProjectOrderCode(project
				.getProject_id(), project.getProject_number(), addrIdArr);
		String result = "";
		if(list == null || list.size() == 0){
			rProjectAddrDao.deleteByProjectId(projectId);
			if(StringHelper.isNotEmpty(addrIds)){
				List<RProjectAddr> paList = new ArrayList<RProjectAddr>();
				for(String addrId : addrIdArr){
					RProjectAddr pa = new RProjectAddr();
					pa.setProject_id(projectId);
					pa.setAddr_id(addrId);
					paList.add(pa);
				}
				rProjectAddrDao.save(paList.toArray(new RProjectAddr[paList.size()]));
			}
		}else{
			for(RProject p : list){
				result += p.getAddr_name()+",";
			}
			result = result.substring(0,result.length()-1);
		}
		
		return result;
	}
	
	public List<RProjectAddr> queryAddrByProjectId(String projectId) throws Exception {
		return rProjectAddrDao.queryAddrByProjectId(projectId);
	}
	
	public void isInvalid(String projectId,String isValid) throws Exception {
		rProjectDao.isInvalid(projectId, isValid);
	}
	
	public List<TAddressDto> queryAddrDistrict(String countyId) throws Exception {
		return tAddressDao.queryAddrDistrict(countyId);
	}
	
	public List<RProjectCounty> queryByCountyId(String countyId) throws Exception {
		return rProjectCountyDao.queryByCountyId(countyId);
	}
	
	/**
	 * 检查项目编号是否存在
	 * @param projectNumber
	 * @return false 不存在，true 存在
	 * @throws Exception
	 */
	public boolean checkProjectNumber(String projectNumber) throws Exception {
		return rProjectDao.checkProjectNumber(projectNumber);
	}
	
	public List<TAddressDto> queryAddrCommunity(String addrPid) throws Exception {
		return tAddressDao.queryAddrCommunity(addrPid);
	}

	public void setRProjectAddrDao(RProjectAddrDao projectAddrDao) {
		rProjectAddrDao = projectAddrDao;
	}

	public void setRProjectCountyDao(RProjectCountyDao projectCountyDao) {
		rProjectCountyDao = projectCountyDao;
	}

	public void setRProjectDao(RProjectDao projectDao) {
		rProjectDao = projectDao;
	}

	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}
}
