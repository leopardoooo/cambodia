package com.ycsoft.business.component.resource;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.beans.core.cust.CCustAddrNote;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TDistrictDao;
import com.ycsoft.business.dao.core.cust.CCustAddrDao;
import com.ycsoft.business.dao.system.SDeptAddrDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SParamDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.commons.constants.DataRight;
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
	
	
	public List<TAddressDto> queryAddrByLike(String name,String pId) throws Exception{
		
		if(StringHelper.isNotEmpty(pId)){
			return tAddressDao.queryAddrById(pId);
		}
		
		List<SDeptAddr> sList = sDeptAddrDao.getAddrByDept(getOptr().getDept_id());
		String[] addrIds = null;
		if(sList.size()>0){
			addrIds = CollectionHelper.converValueToArray(sList, "addr_id");
		}else{
			SDept dept= sDeptDao.findByKey(getOptr().getDept_id());
			if(StringHelper.isNotEmpty(dept.getAgent_id())){
				throw new ComponentException(ErrorCode.DeptAddrIsNull,dept.getDept_name());
			}
			//tAddressDao.queryAddrByAllowPids(levelId, addrPid)
			String[] pids={SystemConstants.ADDRESS_ROOT_ID};
			addrIds= CollectionHelper.converValueToArray(tAddressDao.queryAddrByAllowPids(SystemConstants.ADDR_TREE_LEVEL_ONE,pids),"addr_id");
		}
		if(StringHelper.isEmpty(name)){
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
				districtName = districtName+t.getDistrict_name();
			}
		}
		
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
