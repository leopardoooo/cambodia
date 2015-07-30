package com.ycsoft.business.component.resource;


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.system.SParamDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.daos.core.JDBCException;
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

	public List<TAddressDto> queryAddrByName(String q) throws Exception{
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(), DataRight.NEW_CUST_ADDRESS.toString());
		} catch (Exception e) {
			dataRight = " 1=1 ";
		}
		List<TAddressDto> list = tAddressDao.queryActiveAddrByName(q, getOptr().getCounty_id(),null, dataRight);
		return list;
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
}
