package com.ycsoft.business.component.resource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.core.cust.CCustAddrNote;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TDistrictDao;
import com.ycsoft.business.dao.config.TProvinceDao;
import com.ycsoft.business.dao.core.cust.CCustAddrDao;
import com.ycsoft.business.dao.system.SDeptAddrDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SParamDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.config.TAddressSysDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
/**
 * 简单资源操作：安装地址、ip地址等
 *
 * @author pyb
 *
 * Mar 11, 2010
 *
 */
@Component
public class SimpleComponent extends BaseBusiComponent {
	private TAddressDao tAddressDao;
	private SParamDao sParamDao;
	private TDistrictDao tDistrictDao;
	private SDeptAddrDao sDeptAddrDao;
	@Autowired
	private SDeptDao sDeptDao;
	@Autowired
	private CCustAddrDao cCustAddrDao;
	@Autowired
	private TProvinceDao tProvinceDao;

	public List<TAddressDto> queryAddrByName(String q,String pId) throws Exception{
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(), DataRight.NEW_CUST_ADDRESS.toString());
		} catch (Exception e) {
			dataRight = " 1=1 ";
		}		
		List<TAddressDto> list = tAddressDao.queryActiveAddrByName(q,pId, getOptr().getCounty_id(),null, dataRight);
		return list;
	}
	
	
	public List<TAddressDto> queryAddrByLike(String name,String pId, String editId) throws Exception{
		
		if(StringHelper.isNotEmpty(pId)){
			return tAddressDao.queryAddrById(pId);
		}
		
		List<SDeptAddr> sList = sDeptAddrDao.getAddrByDept(getOptr().getDept_id());
		String[] addrIds = null;
		if(sList.size()>0){
			addrIds = CollectionHelper.converValueToArray(sList, "addr_id");
		}else{
//			SDept dept= sDeptDao.findByKey(getOptr().getDept_id());
//			if(StringHelper.isNotEmpty(dept.getAgent_id())){
//				throw new ComponentException(ErrorCode.DeptAddrIsNull,dept.getDept_name());
//			}
			//tAddressDao.queryAddrByAllowPids(levelId, addrPid)
			String[] pids={SystemConstants.ADDRESS_ROOT_ID};
			addrIds= CollectionHelper.converValueToArray(tAddressDao.queryAddrByAllowPids(SystemConstants.ADDR_TREE_LEVEL_ONE,pids),"addr_id");
		}
		if(StringHelper.isEmpty(name)){
			if(StringHelper.isNotEmpty(editId)){
				TAddress editAddr = tAddressDao.findByKey(editId);
				if(editAddr!= null && SystemConstants.ADDR_TREE_LEVEL_THREE.equals(editAddr.getTree_level().toString())){
					List<TAddressDto> threeAddrList = tAddressDao.queryAddrById(editAddr.getAddr_pid());
					TAddress twoAddr = tAddressDao.findByKey(editAddr.getAddr_pid());
					List<String> addrArrs =  Arrays.asList(addrIds);
					if(addrArrs.contains(twoAddr.getAddr_pid())){
						List<TAddressDto> oneAddrList = tAddressDao.queryAddrByIds(addrIds);
						List<TAddressDto> twoAddrList = tAddressDao.queryAddrById(twoAddr.getAddr_pid());
						if(twoAddrList.size()>0 && threeAddrList.size()>0){
							oneAddrList.addAll(twoAddrList);
							oneAddrList.addAll(threeAddrList);
							return oneAddrList;
						}
					}
				}
				
			}
			return tAddressDao.queryAddrByIds(addrIds);
		}else{
			
		}
		name = name.toLowerCase();
		name = name.replaceAll(" ", "");
		List<TAddressDto> list=tAddressDao.queryAddrTreeByLvOneAndName(addrIds,name);
		
		if(list.size()>2000){
			throw new ComponentException(ErrorCode.DataNumTooMuch);
		}
		return list;
		

	}
	

	public Map<String , Object> queryCustAddrName(String addrId)throws Exception{
		TAddress addr = tAddressDao.findByKey(addrId);
		TAddress paddr = tAddressDao.findByKey(addr.getAddr_pid());
		List<TDistrict> districtList = tDistrictDao.queryDistrictListById(addr.getDistrict_id());
		if(districtList.size() == 0 || (districtList.size()==1 && districtList.get(0).getDistrict_level()==0)){
			throw new ComponentException(ErrorCode.CustDistrictIsNull,addr.getAddr_name());
		}
		String addrName = "No."+addr.getAddr_name()+",St."+paddr.getAddr_name();
		String districtName = "";
		for(TDistrict t : districtList){
			if(t.getDistrict_level() != 0){
				districtName = districtName+t.getDistrict_name()+",";
			}
		}
		districtName = StringHelper.isNotEmpty(districtName)? StringHelper.delEndChar(districtName, 1):"";
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("netType", addr.getNet_type()); //小区网络
		map.put("districtName", districtName); //行政区域
		map.put("addrName", addrName+ (StringHelper.isNotEmpty(districtName)?","+districtName:"")); //详细地址
		return map;
	}
	
	
	/**
	 * 查询单个小区信息.
	 * @param addrId
	 * @return
	 */
	public TAddress querySingleAddress(String addrId) throws Exception{
		return tAddressDao.findByKey(addrId);
	}
	
	/**
	 * @return
	 */
	public List<TAddressDto> queryAddrDistrict() throws Exception{
		return tAddressDao.queryAddrDistrict(getOptr().getCounty_id());
	}
	
	/**
	 * 查询区域下的小区
	 * @param addrPid
	 * @return
	 */
	public List<TAddressDto> queryAddrCommunity(String addrPid) throws JDBCException {
		return tAddressDao.queryAddrCommunity(addrPid);
	}

	public String queryParamValue(String name) throws JDBCException{
		return sParamDao.queryValue(name).getParam_value();
	}

	public void saveParamValue(String name, Integer value) throws JDBCException {
		sParamDao.saveParamValue(name,value);
	}
	
	
	public List queryAddressTree(String name, String pId, SOptr optr) throws Exception{
		List<TAddressSysDto> list = new ArrayList<TAddressSysDto>();
		if(StringHelper.isNotEmpty(pId)){
			list = tAddressDao.queryAllAddrById(pId);
		}else{
		
			List<SDeptAddr> sList = sDeptAddrDao.getAddrByDept(optr.getDept_id());
			String[] addrIds = null;
			if(sList.size()>0){
				addrIds = CollectionHelper.converValueToArray(sList, "addr_id");
			}else{
//				SDept dept= sDeptDao.findByKey(optr.getDept_id());
//				if(StringHelper.isNotEmpty(dept.getAgent_id())){
//					throw new ComponentException(ErrorCode.DeptAddrIsNull,dept.getDept_name());
//				}
				//tAddressDao.queryAddrByAllowPids(levelId, addrPid)
				String[] pids={SystemConstants.ADDRESS_ROOT_ID};
				addrIds= CollectionHelper.converValueToArray(tAddressDao.queryAllAddrByPids(SystemConstants.ADDR_TREE_LEVEL_ONE,pids),"addr_id");
			}
			if(StringHelper.isEmpty(name)){
				list = tAddressDao.queryAllAddrByIds(null);
			}else{
				name = name.toLowerCase();
				name = name.replaceAll(" ", "");
				list=tAddressDao.queryAddrSysTreeByLvOneAndName(addrIds,name);
				
			}
		}
		
		List<TDistrict> districtList = tDistrictDao.findAll();
		Map<String,TDistrict> map = CollectionHelper.converToMapSingle(districtList, "district_id");
		for(TAddressSysDto dto:list){
			TDistrict t = map.get(dto.getDistrict_id());
			if(t != null && StringHelper.isNotEmpty(t.getDistrict_name())){
				dto.setDistrict_name(t.getDistrict_name());
			}
		}
		if(list.size()>2000){
			throw new ComponentException(ErrorCode.DataNumTooMuch);
		}
		return list;
	}
	
	public List<TProvince> queryProvince() throws Exception{
		return tProvinceDao.queryProvince();
	}
	
	public List queryDistrictByPid(String pId) throws Exception{
		if(StringHelper.isEmpty(pId)){
			throw new ComponentException(ErrorCode.ParamIsNull);
		}
		return tDistrictDao.queryDistrictListByPid(pId);
	}
	
	private String getNextAddrId() throws JDBCException{
		return tAddressDao.findSequence(SequenceConstants.SEQ_ADDR_ID).toString();
	}
	
	public void updateAddressStatus(String addrId, String status) throws Exception{
		TAddress addr = tAddressDao.findByKey(addrId);
		if(addr.getTree_level()==0){
			throw new ComponentException("本级不能启用禁用操作");
		}
		if(status.equals(StatusConstants.ACTIVE)){
			if(StringHelper.isNotEmpty(addr.getAddr_pid())){
				TAddress _p = tAddressDao.findByKey(addr.getAddr_pid());
				if(!_p.getStatus().equals(status)){
					throw new ComponentException("上级状态是禁用的，不能启用本级");
				}
			}
		}else{
			List<TAddress> list = tAddressDao.queryByPidStatus(addrId,status);
			if(list.size()>0){
				throw new ComponentException("下级状态是正常的，不能禁用本级");
			}
		}
		addr.setStatus(status);
		tAddressDao.update(addr);
	}
	
	public void editAddress(TAddressSysDto addr) throws JDBCException{
		TAddress  newAddr = new TAddress();
		newAddr.setAddr_id(addr.getAddr_id());
		newAddr.setAddr_name(addr.getAddr_name());
		newAddr.setNet_type(addr.getNet_type());
		newAddr.setSort_num(addr.getSort_num());
		newAddr.setDistrict_id(addr.getDistrict_id());
		tAddressDao.update(newAddr);
	}

	/**
	 * 增加地区
	 * @param treeLevel 
	 * @return
	 * @throws Exception
	 */
	public TAddress saveAddress(TAddressSysDto addr,String type) throws Exception{
		TAddress  newAddr = new TAddress();
		newAddr.setAddr_pid(addr.getAddr_pid());
		newAddr.setArea_id(addr.getArea_id());
		newAddr.setTree_level(addr.getTree_level());
		newAddr.setCounty_id(addr.getCounty_id());
		newAddr.setAddr_name(addr.getAddr_name());
		newAddr.setAddr_id(getNextAddrId());
		newAddr.setNet_type(addr.getNet_type());
		newAddr.setDistrict_id(addr.getDistrict_id());
		newAddr.setIs_leaf(SystemConstants.BOOLEAN_TRUE);
		float  b = (float) 0.00;
		if("leveladd".equals(type)){//新增平级算排序值
			TAddress lastAddr = tAddressDao.findByKey(addr.getAddr_last_id());
			TAddress nextAddr = tAddressDao.querySortNumByNextId(lastAddr.getAddr_pid(), lastAddr.getSort_num());
			if(nextAddr == null){
				b = lastAddr.getSort_num()+1000;
			}else{
				b  = (float)(Math.round((lastAddr.getSort_num()+nextAddr.getSort_num())/2*100))/100;
			}
		}else if("add".equals(type)){//新增下级算排序值，默认最大值+1000
			String maxSortNum =	tAddressDao.queryMaxSortNumByPid(addr.getAddr_pid());
			b = Float.parseFloat(maxSortNum)  + 1000;
		}

		newAddr.setSort_num(b);
		tAddressDao.save(newAddr);
		
		
		//修改父节点is_leaf为F
		updateAddress(addr.getAddr_pid(), SystemConstants.BOOLEAN_FALSE);
		
		return newAddr;
	}

	/**
	 * 修改地址is_leaf属性
	 * @param addrId
	 * @param isLeaf
	 * @throws JDBCException
	 */
	private void updateAddress(String addrId,String isLeaf) throws JDBCException{
		TAddress addr = tAddressDao.findByKey(addrId);
		addr.setIs_leaf(isLeaf);
		tAddressDao.update(addr);
	}

	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}
	/**
	 * @param paramDao the sParamDao to set
	 */
	public void setSParamDao(SParamDao paramDao) {
		sParamDao = paramDao;
	}


	public void setTDistrictDao(TDistrictDao districtDao) {
		this.tDistrictDao = districtDao;
	}


	public void setSDeptAddrDao(SDeptAddrDao deptAddrDao) {
		this.sDeptAddrDao = deptAddrDao;
	}


	public Pager<CCustAddrNote> queryNoteCust(String addrId, Integer start, Integer limit) throws Exception{
		return cCustAddrDao.queryNoteCust(addrId,start,limit);
	}



}
