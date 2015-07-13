package com.ycsoft.sysmanager.web.action.config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.config.ExtendComponent;
import com.ycsoft.sysmanager.component.system.ParamComponent;
import com.ycsoft.sysmanager.dto.config.ExtendTableDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;


@SuppressWarnings("serial")
@Controller
public class ExtendTableAction extends BaseAction {
	private ExtendComponent extendComponent;
	private ParamComponent paramComponent;
	private String records;
	private String extendType;
	private String extendValue;
	private String delAttributeIds;
	private String groups;
	private String groupId;
	private String extensionId;
	
	private String tabName;
	private String pkValue;
	


	/**
	 * 查询扩展属性菜单树
	 * @return
	 * @throws JDBCException
	 */
	@SuppressWarnings("unchecked")
	public String queryExtensionTree() throws JDBCException{
		List list = extendComponent.queryExtensionTree();
		getRoot().setRecords(TreeBuilder.createTree(list));
		return JSON_RECORDS;
	}


	/**
	 * 初始化输入类型数据
	 * @return
	 */
	public String queryInputtype(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.EXT_INPUT_TYPE));
		return JSON_RECORDS;
	}

	/**
	 * 查询下拉框参数
	 * @return
	 * @throws JDBCException
	 */
	public String queryParams() throws JDBCException{
		getRoot().setRecords(paramComponent.findAllKey());
		return JSON_RECORDS;
	}


	/**
	 * 查询扩展字段定义信息
	 * @return
	 * @throws JDBCException
	 */
	public String queryAttribute() throws JDBCException{
		if(StringHelper.isNotEmpty(extendType)){
			if(extendType.equals(SystemConstants.EXDT_ATTR_TYPE_TAB)){
				getRoot().setRecords(extendComponent.queryTableExtendAttribute(extensionId,groupId));
			}else if(extendType.equals(SystemConstants.EXT_ATTR_TYPE_BUSI)){
				getRoot().setRecords(extendComponent.queryBusiExtendAttribute(extensionId,groupId));
			}
		}

		return JSON_RECORDS;
	}

	/**
	 * 保存扩展字段定义信息
	 * @return
	 * @throws JDBCException
	 */
	public String saveAttribute() throws Exception{
		List<ExtendTableDto> extendList = new ArrayList<ExtendTableDto>();
		if(StringHelper.isNotEmpty(records)){
			Type type = new TypeToken<List<ExtendTableDto>>(){}.getType();
			Gson gson = new Gson();
			extendList = gson.fromJson(records,type);
		}
		
		List<ExtendTableDto> oldList = new ArrayList<ExtendTableDto>();
		if(extendType.equals(SystemConstants.EXDT_ATTR_TYPE_TAB)){
			oldList = extendComponent.queryTableExtendAttribute(extensionId,groupId);
		}else if(extendType.equals(SystemConstants.EXT_ATTR_TYPE_BUSI)){
			oldList = extendComponent.queryBusiExtendAttribute(extensionId,groupId);
		}
		
		extendComponent.saveAttribute(extendList,  extendType,extendValue);
		
		List<ExtendTableDto> newList = new ArrayList<ExtendTableDto>();
		if(extendType.equals(SystemConstants.EXDT_ATTR_TYPE_TAB)){
			newList = extendComponent.queryTableExtendAttribute(extensionId,groupId);
		}else if(extendType.equals(SystemConstants.EXT_ATTR_TYPE_BUSI)){
			newList = extendComponent.queryBusiExtendAttribute(extensionId,groupId);
		}
		
		saveChanges(oldList,newList);
		
		return JSON;
	}

	private void saveChanges(List<ExtendTableDto> oldList,List<ExtendTableDto> newList) throws ActionException{
		String optrId = WebOptr.getOptr().getOptr_id();
		Date createTime = new Date();
		String key = extendValue;
		String keyDesc = null;
		String content = null;
		String changeDesc = "扩展属性配置";
		String changeType = SysChangeType.EXT_CFG.toString();
		
		if(CollectionHelper.isNotEmpty(oldList)){
			for(ExtendTableDto dto:oldList){
				String columnName = dto.getCol_name();
				if(StringHelper.isNotEmpty(columnName)){
					dto.setCol_name(columnName.replace("str", "扩展字段"));
				}
			}
		}
		
		if(CollectionHelper.isNotEmpty(newList)){
			for(ExtendTableDto dto:newList){
				String columnName = dto.getCol_name();
				if(StringHelper.isNotEmpty(columnName)){
					dto.setCol_name(columnName.replace("str", "扩展字段"));
				}
			}
		}
		String preAppend = null;
		try{
			Integer doneCode = extendComponent.getDoneCOde();
			
			if(StringHelper.isEmpty(extendType)){
				throw new ActionException("记录扩展属性异动出现错误,未能获取的扩展类型 !");
			}
			
			if(extendType.equals(SystemConstants.EXDT_ATTR_TYPE_TAB)){
				if(extendValue.equalsIgnoreCase("C_CUST")){
					keyDesc = "客户扩展";
				}else if(extendValue.equalsIgnoreCase("C_USER")){
					keyDesc = "用户扩展";
				}else if(extendValue.equalsIgnoreCase("P_PROD")){
					keyDesc = "产品扩展";
				}else{
					throw new ActionException("记录扩展属性异动出现错误,获取的扩展类型 "+ extendType + " 未知!");
				}
				
				String[] displayFields = new String[] {"col_name","attribute_name","attribute_order","is_null_text","is_show_text","input_type_text","param_name_text","group_name"};
				preAppend = "字段名_ 显示名_位置_允许空_允许显示_输入类型_下来框参数值_组名";
				content = BeanHelper.listchange(oldList, newList, displayFields);
				
				
			}else if(extendType.equals(SystemConstants.EXT_ATTR_TYPE_BUSI)){
				keyDesc = MemoryDict.getDictName(DictKey.BUSI_CODE, extendValue);
				
				String[] displayFields = new String[] {"attribute_name","attribute_order","is_null_text","is_show_text","input_type_text","param_name_text"};
				preAppend = "显示名_位置_允许空_允许显示_输入类型_下来框参数值";
				content = BeanHelper.listchange(oldList, newList, displayFields);
			}
			if(StringHelper.isEmpty(content)){
				return;
			}
			content = preAppend + ":\n" + content;
			SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
			
			extendComponent.getSSysChangeDao().save(change);
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}


	/**
	 * 查询扩展信息
	 * @return
	 * @throws Exception
	 */
	public String findTabExtAttr() throws Exception{
		getRoot().setRecords(extendComponent.findTabExtAttr(groupId, tabName, pkValue));
		return JSON_RECORDS;
	}

	public ExtendComponent getExtendComponent() {
		return extendComponent;
	}

	public void setExtendComponent(ExtendComponent extendComponent) {
		this.extendComponent = extendComponent;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}


	public String getExtendType() {
		return extendType;
	}

	public void setExtendType(String extendType) {
		this.extendType = extendType;
	}

	public ParamComponent getParamComponent() {
		return paramComponent;
	}

	public void setParamComponent(ParamComponent paramComponent) {
		this.paramComponent = paramComponent;
	}

	public String getExtendValue() {
		return extendValue;
	}

	public void setExtendValue(String extendValue) {
		this.extendValue = extendValue;
	}

	public String getDelAttributeIds() {
		return delAttributeIds;
	}

	public void setDelAttributeIds(String delAttributeIds) {
		this.delAttributeIds = delAttributeIds;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}


	public void setTabName(String tabName) {
		this.tabName = tabName;
	}


	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}


}
