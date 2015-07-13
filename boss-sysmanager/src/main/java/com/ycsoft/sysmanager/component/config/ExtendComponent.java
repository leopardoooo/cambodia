package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.beans.config.TExtend;
import com.ycsoft.beans.config.TExtendAttribute;
import com.ycsoft.beans.config.TExtendGroup;
import com.ycsoft.business.dao.config.TExtendAttributeDao;
import com.ycsoft.business.dao.config.TExtendDao;
import com.ycsoft.business.dao.config.TExtendGroupDao;
import com.ycsoft.business.dto.config.ExtendTableAttributeDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.config.ExtendTableDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


@Component
public class ExtendComponent extends BaseComponent {

	private TExtendAttributeDao tExtendAttributeDao;
	private TExtendDao tExtendDao;
	private TExtendGroupDao tExtendGroupDao;

	public String gAttributeId() throws JDBCException{
		return tExtendAttributeDao.findSequence().toString();
	}

	public String gExtTableId() throws JDBCException{
		return tExtendDao.findSequence().toString();
	}

	/**
	 * 返回菜单树
	 * @return
	 * @throws JDBCException
	 */
	public List<TreeDto> queryExtensionTree() throws JDBCException{
		return tExtendDao.queryExtensionTree();
	}

	/**
	 * 根据设备类型及对应的值，查询对应扩展表或扩展业务的分组信息,封装成SItemvalue返回
	 * @param extendType 分组类型
	 * @param extendValue 扩展表名或业务code
	 * @return
	 * @throws JDBCException
	 */
	public List<TExtendGroup> queryGroups(String extensionId) throws JDBCException{
		return tExtendGroupDao.queryGroups(extensionId);
	}



	/**
	 * Description: 查询扩展表的字段信息
	 * @param tablename
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableDto> queryTableExtendAttribute(String extensionId,String groupId) throws JDBCException{
		return tExtendAttributeDao.queryExtensionAttr(extensionId, groupId);
	}

	/**
	 * 根据busi_code查询扩展字段定义信息
	 * @param busiCode
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableDto> queryBusiExtendAttribute(String extensionId,String groupId) throws JDBCException{
		return tExtendAttributeDao.queryExtensionAttr(extensionId, groupId);
	}

	/**
	 * 保存扩展属性
	 * @param list
	 * @param extend_table_pk
	 * @param extend_type
	 * @throws JDBCException
	 */
	public void saveAttribute(List<ExtendTableDto> extendList,String extendType,String extendValue) throws JDBCException{

		TExtend tExtend = tExtendDao.findByExtendName(extendType, extendValue);
		
		
		if(null == tExtend){//如果不存在扩展对象
			tExtend = new TExtend();
			if(extendType.equals(SystemConstants.EXDT_ATTR_TYPE_TAB)){
				//从user_tab_comments中获取表的描述
				TExtend te = tExtendDao.findExtendName(extendValue);
				if(StringHelper.isEmpty(te.getExtend_name())){
					tExtend.setExtend_name(SystemConstants.EXT_TAB_EXTENDNAME);
				}else{
					tExtend.setExtend_name(te.getExtend_name());
				}
				tExtend.setExtend_table(extendValue);
				tExtend.setExtend_type(extendType);
			}else if(extendType.equals(SystemConstants.EXT_ATTR_TYPE_BUSI)){
				tExtend.setExtend_name(MemoryDict.getDictName(DictKey.BUSI_CODE, extendValue));
				tExtend.setExtend_type(extendType);
				tExtend.setExtend_table(SystemConstants.EXT_C_DONE_CODE);
				tExtend.setBusi_code(extendValue);
			}
			tExtend.setExtend_id(gExtTableId());
			tExtendDao.save(tExtend);

		}
		String extendId = tExtend.getExtend_id();
		
		//批量保存或修改TExtendAttribute
		List<TExtendAttribute> attrsForUpdate = new ArrayList<TExtendAttribute>();
		List<TExtendAttribute> attrsForSave = new ArrayList<TExtendAttribute>();
		for(ExtendTableDto ed : extendList){
			if(StringHelper.isNotEmpty(ed.getAttribute_id())){//如果存在
				TExtendAttribute tea = new TExtendAttribute();
				tea.setExtend_id(extendId);
				tea.setAttribute_id(ed.getAttribute_id());
				tea.setAttribute_name(ed.getAttribute_name());
				tea.setAttribute_order(ed.getAttribute_order());
				tea.setCol_name(ed.getCol_name());
				tea.setGroup_id(ed.getGroup_id());
				tea.setInput_type(ed.getInput_type());
				tea.setIs_null(ed.getIs_null());
				tea.setIs_show(ed.getIs_show());
				tea.setDefault_value(ed.getDefault_value());
				tea.setParam_name(ed.getParam_name());
				attrsForUpdate.add(tea);
			}else{
				TExtendAttribute tea = new TExtendAttribute();
				tea.setExtend_id(extendId);
				tea.setAttribute_id(gAttributeId());
				tea.setAttribute_name(ed.getAttribute_name());
				tea.setAttribute_order(ed.getAttribute_order());
				tea.setCol_name(ed.getCol_name());
				tea.setGroup_id(ed.getGroup_id());
				tea.setInput_type(ed.getInput_type());
				tea.setIs_null(ed.getIs_null());
				tea.setIs_show(ed.getIs_show());
				tea.setDefault_value(ed.getDefault_value());
				tea.setParam_name(ed.getParam_name());
				attrsForSave.add(tea);
			}
		}
		tExtendAttributeDao.save(attrsForSave.toArray(new TExtendAttribute[attrsForSave.size()]));
		tExtendAttributeDao.update(attrsForUpdate.toArray(new TExtendAttribute[attrsForUpdate.size()]));
	}


	/**
	 * 根据扩展表attribute_id删除该扩展表的字段定义信息
	 * @param attribute_id
	 * @throws JDBCException
	 */
	public void deleteAttribute(String attribute_id) throws JDBCException{
		tExtendAttributeDao.deleteAttribute(attribute_id);
	}

	public List<TExtendAttribute> queryColumns(String table_name) throws JDBCException{
		return tExtendAttributeDao.queryColumns(table_name);
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

	public TExtendAttributeDao getTExtendAttributeDao() {
		return tExtendAttributeDao;
	}

	public void setTExtendAttributeDao(TExtendAttributeDao extendAttributeDao) {
		tExtendAttributeDao = extendAttributeDao;
	}

	public TExtendDao getTExtendDao() {
		return tExtendDao;
	}

	public void setTExtendDao(TExtendDao extendDao) {
		tExtendDao = extendDao;
	}

	public TExtendGroupDao getTExtendGroupDao() {
		return tExtendGroupDao;
	}

	public void setTExtendGroupDao(TExtendGroupDao extendGroupDao) {
		tExtendGroupDao = extendGroupDao;
	}
}
