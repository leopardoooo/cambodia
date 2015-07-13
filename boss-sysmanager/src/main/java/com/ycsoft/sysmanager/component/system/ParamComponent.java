package com.ycsoft.sysmanager.component.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTabDefine;
import com.ycsoft.beans.system.SItemDefine;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TBusiCodeDao;
import com.ycsoft.business.dao.config.TTabDefineDao;
import com.ycsoft.business.dao.system.SItemDefineDao;
import com.ycsoft.business.dao.system.SItemvalueDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

@Component
public class ParamComponent extends BaseComponent {
	private TBusiCodeDao tBusiCodeDao;
	private SItemvalueDao sItemvalueDao;
	private TAddressDao tAddressDao;
	private SItemDefineDao sItemDefineDao;
	private TTabDefineDao tTabDefineDao;

	/**
	 * 查询参数定义
	 * @return
	 * @throws Exception
	 */
	public List<SItemDefine> queryItemDefines(String query) throws JDBCException {
		return sItemDefineDao.findAllDefines(query);
	}
	
	/**
	 * 查询参数定义
	 * @return
	 * @throws Exception
	 */
	public SItemDefine queryItemDefineForChangeInfo(String item_key) throws JDBCException {
		return sItemDefineDao.findByKey(item_key);
	}

	/**
	 * 字段名参数查询
	 * @param start
	 * @param limit
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public Pager<TTabDefine> queryTabDefine(Integer start, Integer limit,String query) throws Exception {
		return tTabDefineDao.queryTabDefine(start, limit,query);
	}
	
	/**
	 * 查询参数配置
	 * @return
	 * @throws Exception
	 */
	public List<SItemvalue> queryItemValues(String itemKey) throws JDBCException {
		return sItemvalueDao.queryByKey(itemKey);
	}

	/**
	 * 保存一条参数定义
	 * @return
	 * @throws JDBCException
	 * @throws Exception
	 */
	public void saveItemDefine(SItemDefine itemDefine) throws JDBCException {
		if(sItemDefineDao.findByKey(itemDefine.getItem_key()) != null){
			sItemDefineDao.update(itemDefine);
		}else{
			sItemDefineDao.save(itemDefine);
		}
	}
	
	
	/**
	 * 保存修改字段名配置
	 * @param tabDefine
	 * @param oldTableName
	 * @param oldColumnName
	 * @throws Exception
	 */
	public void saveTabDefine(TTabDefine tabDefine,String oldTableName,String oldColumnName) throws Exception {
		if(StringHelper.isNotEmpty(oldTableName)&& StringHelper.isNotEmpty(oldColumnName)){//修改
			deleteTabDefine(oldTableName,oldColumnName);//失效原先的配置
			tabDefine.setOld_data(oldTableName+","+oldColumnName);//新配置记录原先的配置
		}else{
			TTabDefine dto =  tTabDefineDao.queryTab(tabDefine.getTable_name(),tabDefine.getColumn_name());
			if(dto!=null){
				throw new ComponentException("已经存在该表名与字段名的配置! ");
			}
		}
		tabDefine.setStatus(StatusConstants.ACTIVE);
		tTabDefineDao.save(tabDefine); //新增修改后配置
	}

	/**
	 * 删除字段名配置
	 * @param oldTableName
	 * @param oldColumnName
	 * @throws Exception
	 */
	public void deleteTabDefine(String oldTableName,String oldColumnName) throws Exception {
			tTabDefineDao.updateInvalid(oldTableName,oldColumnName);//失效原先的配置
	}
	
	/**
	 * 删除一条参数定义
	 * @param itemKey
	 * @throws JDBCException
	 */
	public void deleteItemDefine(String itemKey) throws JDBCException{
		sItemDefineDao.remove(itemKey);
	}

	public void saveItemValues(List<SItemvalue> valueList,String itemKey) throws JDBCException {
		sItemvalueDao.deleteByKey(itemKey);
		if(valueList != null && valueList.size() > 0){
			sItemvalueDao.save(valueList.toArray(new SItemvalue[valueList.size()]));
		}
	}

	public List<SItemvalue> findAllKey() throws JDBCException{
		return sItemvalueDao.findAllKey();
	}

	public List<TAddressDto> findAllAddress(String qName,String county_id) throws Exception {
		return tAddressDao.queryActiveAddrByName(qName, county_id, " 1=1 ");
	}

	public TBusiCodeDao getTBusiCodeDao() {
		return tBusiCodeDao;
	}

	public void setTBusiCodeDao(TBusiCodeDao busiCodeDao) {
		tBusiCodeDao = busiCodeDao;
	}

	public SItemvalueDao getSItemvalueDao() {
		return sItemvalueDao;
	}

	public void setSItemvalueDao(SItemvalueDao itemvalueDao) {
		sItemvalueDao = itemvalueDao;
	}

	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}

	public SItemDefineDao getSItemDefineDao() {
		return sItemDefineDao;
	}

	public void setSItemDefineDao(SItemDefineDao itemDefineDao) {
		sItemDefineDao = itemDefineDao;
	}
	
	public TTabDefineDao getTTabDefineDao() {
		return tTabDefineDao;
	}

	public void setTTabDefineDao(TTabDefineDao tabDefineDao) {
		tTabDefineDao = tabDefineDao;
	}
}
