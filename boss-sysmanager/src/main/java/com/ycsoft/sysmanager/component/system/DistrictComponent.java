package com.ycsoft.sysmanager.component.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.system.SArea;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.resource.device.RDepotDefineDao;
import com.ycsoft.business.dao.system.SAreaDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.dto.system.SAreaDto;
import com.ycsoft.sysmanager.dto.system.SCountyDto;


/**
 * 区域、县市、营业厅的组件封装
 *
 * @author hh
 * @data Mar 31, 2010 2:03:39 PM
 */
@Component
public class DistrictComponent extends BaseComponent {

	private SCountyDao sCountyDao ;
	private SDeptDao sDeptDao ;
	private SAreaDao sAreaDao;
	private RDepotDefineDao rDepotDefineDao;


	public List<SAreaDto> queryCounties(String templateId, SOptr optr) throws Exception{
		List<SAreaDto> areaDtoList = new ArrayList<SAreaDto>();
		Map<String, List<SCountyDto>> countyMap = null;
		List<SArea> areaList = null;
		String countyId = optr.getCounty_id();
		if(SystemConstants.COUNTY_ALL.equals(countyId)){
			countyMap = CollectionHelper.converToMap(sCountyDao.queryCounties(templateId,optr.getCounty_id(),null), "area_id");
			areaList = sAreaDao.findAll();
		}else{
			String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
			List<SCountyDto> countyList = sCountyDao.queryCounties(templateId,optr.getCounty_id(),dataRight);
			countyMap = CollectionHelper.converToMap(countyList, "area_id");
			
			List<String> areaIdList = CollectionHelper.converValueToList(countyList, "area_id");
			areaList = sAreaDao.getAreaById(areaIdList.toArray(new String[areaIdList.size()]));
		}
		
		for(SArea sa : areaList){
			SAreaDto areaDto = new SAreaDto();
			BeanUtils.copyProperties(sa, areaDto);
			areaDto.setCountyList(countyMap.get(areaDto.getArea_id()));
			areaDtoList.add(areaDto);
		}
		return areaDtoList;
	}

	/**
	 * 获取区域和区域对应的County，及County下对应的部门，
	 * 按照TreeNode结构排列
	 *
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> getDistricts(String model,SOptr optr)throws Exception{
		List<SItemvalue> areas = new ArrayList<SItemvalue>();
		String countyId = optr.getCounty_id();
		String areaId = optr.getArea_id();
		List<SCounty> countys = null;
		if (areaId.equals(SystemConstants.AREA_ALL)) {
			countys = sCountyDao.findAll();
			areas = MemoryDict.getDicts(DictKey.AREA);
		} else {
			countys = sCountyDao.getCountyById(countyId);
			String areaName = MemoryDict.getDictName(DictKey.AREA, areaId);
			areas.add(new SItemvalue(areaName,areaId));
		}

		List<TreeNode> at = TreeBuilder.convertToNodes( areas, "item_value", "item_name");
		List<TreeNode> ct = TreeBuilder.convertToNodes( countys, "county_id", "county_name");

		List<SDept> depts = null ;
		List<RDepotDefine> depots = null;
		List<TreeNode> dt = null ;
		boolean isDept = false;
		boolean isDepot = false;
		//判断是否需要部门
		if(model.endsWith("dept")){
			if (areaId.equals(SystemConstants.AREA_ALL))
				depts = sDeptDao.findAll();
			else
				depts = sDeptDao.queryByCountyId(countyId);
			dt = TreeBuilder.convertToNodes( depts, "dept_id", "dept_name");
			isDept = true;
		}
		if(isDept){
			//将县市对应的部门添加至县市的子节点中
			setCounty2Dept(countys, depts, ct, dt);
		}else if(isDepot){
			setCounty2Depot(countys, depots, ct, dt);
		} else {
			//如果没有部门，则所有的county均为子节点
			for(int i=0;i< countys.size() ;i++){
				ct.get(i).setLeaf(true);
				ct.get(i).setIs_leaf("T");
			}
		}
		//将区域对应的县市添加至区域的子节点中
		setArea2County(areas, countys, at, ct);
		return at;
	}

	/**
	 * 获取区域和区域对应的County，及County下对应的部门，
	 * 按照TreeNode结构排列
	 *
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> getDistrictTree(SOptr optr)throws Exception{
		List<SItemvalue> areas = new ArrayList<SItemvalue>();
		String areaId = optr.getArea_id();
		List<SCounty> countys = null;
		if (areaId.equals(SystemConstants.AREA_ALL)) {
			countys = sCountyDao.findAll();
			areas = MemoryDict.getDicts(DictKey.AREA);
		} else {
			countys = sCountyDao.getCountyByAreaId(areaId);
			String areaName = MemoryDict.getDictName(DictKey.AREA, areaId);
			areas.add(new SItemvalue(areaName,areaId));
		}

		List<TreeNode> at = TreeBuilder.convertToNodes( areas, "item_value", "item_name");
		List<TreeNode> ct = TreeBuilder.convertToNodes( countys, "county_id", "county_name");
			//如果没有部门，则所有的county均为子节点
			for(int i=0;i< countys.size() ;i++){
				ct.get(i).setLeaf(true);
			}
		//将区域对应的县市添加至区域的子节点中
		setArea2County(areas, countys, at, ct);
		return at;
	}
	
	/**
	 * 设置地区仓库的节点关系
	 */
	private void setCounty2Depot(List<SCounty> countys,List<RDepotDefine> depots,
			List<TreeNode> ct,List<TreeNode> dt){
		for(int i=0;i< countys.size() ;i++){
			for(int j=0;j < depots.size();j++){
				if(countys.get(i).getCounty_id().equals( depots.get(j).getCounty_id())){
					dt.get(j).setLeaf( true );
					ct.get(i).getChildren().add( dt.get(j));
				}
			}
			ct.get(i).setIs_leaf("F");
			if(ct.get(i).getChildren().size() == 0){
				ct.get(i).setLeaf( true);
				//ct.get(i).setIs_leaf( SystemConstants.BOOLEAN_FALSE );
			}
		}
	}

	/**
	 * 设置地区部门的节点关系
	 */
	private void setCounty2Dept(List<SCounty> countys,List<SDept> depts,
						List<TreeNode> ct, List<TreeNode> dt){
		for(int i=0;i< countys.size() ;i++){
			for(int j=0;j < depts.size();j++){
				if(countys.get(i).getCounty_id().equals( depts.get(j).getCounty_id())){
					dt.get(j).setLeaf( true );
					ct.get(i).setLeaf( true );
					ct.get(i).getChildren().add( dt.get(j));
				}
			}
			ct.get(i).setIs_leaf("F");
			if(ct.get(i).getChildren().size() == 0){
				ct.get(i).setLeaf( true);
				//ct.get(i).setIs_leaf( SystemConstants.BOOLEAN_FALSE );
			}
		}
	}
	/**
	 * 设置区域和县市的节点关系
	 */
	private void setArea2County(List<SItemvalue> areas, List<SCounty> countys,
								List<TreeNode> at,List<TreeNode> ct){
		Map<String, String> others = new HashMap<String, String>();
		for(int i=0;i< areas.size() ;i++){
			for(int j=0;j < countys.size();j++){
				if(areas.get(i).getItem_value().equals( countys.get(j).getArea_id())){
					at.get(i).getChildren().add( ct.get(j));
					others.put("att", "county");
					ct.get(j).setOthers(others);
				}
			}
		}
	}

	public SAreaDao getSAreaDao() {
		return sAreaDao;
	}

	public void setSAreaDao(SAreaDao areaDao) {
		sAreaDao = areaDao;
	}


	public SCountyDao getSCountyDao() {
		return sCountyDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public SDeptDao getSDeptDao() {
		return sDeptDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public void setRDepotDefineDao(RDepotDefineDao depotDefineDao) {
		rDepotDefineDao = depotDefineDao;
	}
}