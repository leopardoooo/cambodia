package com.ycsoft.business.component.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExtend;
import com.ycsoft.beans.config.TExtendAttribute;
import com.ycsoft.beans.core.common.ExtCDoneCode;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TExtendAttributeDao;
import com.ycsoft.business.dao.config.TExtendDao;
import com.ycsoft.business.dao.core.common.ExtCDoneCodeDao;
import com.ycsoft.business.dto.config.ExtAttrFormDto;
import com.ycsoft.business.dto.config.ExtendAttributeDto;
import com.ycsoft.business.dto.config.ExtendTableAttributeDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.ListHelper;

/**
 * @author YC-SOFT
 *
 */
@Component
public class ExtTableComponent extends BaseBusiComponent{

	private TExtendAttributeDao tExtendAttributeDao;
	private TExtendDao tExtendDao;
	private ExtCDoneCodeDao extCDoneCodeDao;

	/**
	 * 删除扩展表
	 * @param extTabName 扩展表名
	 * @param pkColumn 主键字段名
	 * @param pkValues 主键值
	 */
	public void deleteData(String extTabName,String pkColumn, Serializable [] pkValues)
			throws JDBCException, ComponentException {
		//TODO 删除扩展信息
		tExtendAttributeDao.deleteToExtTable(extTabName, pkColumn, pkValues);
	}

	/**
	 * 查询扩展表属性，通过表名
	 * @param extend_table
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableAttributeDto> findTabExtAttr(String groupId ,String extend_table) throws JDBCException {
		return findTabExtAttr(groupId, extend_table, null);
	}

	/**
	 * 查询业务扩展信息
	 */
	public List<ExtendAttributeDto> findBusiExtAttr()throws Exception{
		return tExtendAttributeDao.queryBusiExtAttr();
	}
	public List<TExtendAttribute> findBusiExtAttr(String busiCode)
			throws Exception {
		return tExtendAttributeDao.queryBusiExtAttr(busiCode);
	}

	/**
	 * 查询扩展表的属性
	 * @param extend_table
	 * @param primaryKey_value
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableAttributeDto> findTabExtAttr(String groupId, String extend_table,
			String primaryKey_value) throws JDBCException {
		Map<String, Object> value = null;
		List<ExtendTableAttributeDto> attributes = tExtendAttributeDao
				.findExtTableArrtible(groupId, extend_table);
		//主键值不为NULL,则设置显示值
		if(StringHelper.isNotEmpty(primaryKey_value)){
			value = queryInfo(extend_table, primaryKey_value);
		}
		for (ExtendTableAttributeDto a : attributes) {
			//设置编辑值
			if (value != null){
				Object v = value.get(a.getCol_name().toLowerCase());
				if( null != v){
					a.setEdit_value(v.toString());
				}
				if(a.getInput_type().equals( SystemConstants.EXT_TAB_INPUTTYPE_COMBO)){
					a.setDisplay_text(MemoryDict.getDictName(a.getParam_name(), a
							.getEdit_value()));
				}else{
					a.setDisplay_text(a.getEdit_value());
				}
			}
		}
		return attributes;
	}

	/**
	 *  保存业务扩展信息
	 */
	public void saveBusiAttr(Integer doneCode ,ExtCDoneCode [] exts)throws Exception{
		if(null == exts || exts.length == 0) return ;
		for(ExtCDoneCode e: exts){
			e.setDone_code(doneCode);
		}
		extCDoneCodeDao.save(exts);
	}

	/**
	 * 保存到扩展表
	 * @param extend_table
	 * @param data
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public void saveOrUpdate(ExtAttrFormDto form)
			throws JDBCException, ComponentException {
		if(null == form || null == form.getExtAttrs() || 0 == form.getExtAttrs().size()) return ;
		if(StringHelper.isEmpty(form.getPkColumn()) || form.getPkValue() == null){
			throw new ComponentException("没有设置扩展信息的主键字段或主键值!");
		}
		Map<String,String> map = form.getExtAttrs();
		ListHelper.clearNullElement(map);
		//判断在扩展表中是否存在记录，如果存在则做更新操作，否则保存操作
		if(tExtendAttributeDao.existExtTable(form) && map.size() > 0){
			tExtendAttributeDao.updateToExtTable(form);
		}else{
			map.put(form.getPkColumn() , form.getPkValue());
			tExtendAttributeDao.insterToExtTable(form.getExtendTable(), map);
		}
	}

	/**
	 * 查询扩展表
	 *
	 * @param extend_table 扩展表名
	 * @param primaryKey_value 扩展表主键值
	 * @return
	 * @throws JDBCException
	 */
	public Map<String, Object> queryInfo(String extend_table,
			String primaryKey_value) throws JDBCException {
		if (StringHelper.isNotEmpty(primaryKey_value)){
			TExtend tExtend = new TExtend();
			tExtend.setExtend_table(extend_table);
			List<TExtend> lst = tExtendDao.findByEntity(tExtend);
			if(lst.size() == 0){
				return null;
			}else{
				tExtend = lst.get(0);
			}
			tExtend.setExtend_table(extend_table);
			return tExtendAttributeDao.findExtTable(extend_table, primaryKey_value,
					tExtend.getExtend_table_pk());
		}else{
			return null;
		}
	}

	/**
	 * @param extendAttributeDao
	 */
	public void setTExtendAttributeDao(TExtendAttributeDao extendAttributeDao) {
		tExtendAttributeDao = extendAttributeDao;
	}

	/**
	 * @param extendDao
	 */
	public void setTExtendDao(TExtendDao extendDao) {
		tExtendDao = extendDao;
	}

	public void setExtCDoneCodeDao(ExtCDoneCodeDao extCDoneCodeDao) {
		this.extCDoneCodeDao = extCDoneCodeDao;
	}
}
