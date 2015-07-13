package com.ycsoft.sysmanager.web.action.system;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TTabDefine;
import com.ycsoft.beans.system.SItemDefine;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.system.ParamComponent;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Controller
public class ParamAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 824517072041831504L;

	private ParamComponent paramComponent;

	//搜索关键字
	private String query;
	private String itemKey;
	private SItemDefine itemDefine;
	private String records;
	private TTabDefine tabDefine;
	private String old_table_name;
	private String old_column_name;
	
	
	/**
	 * 查询参数定义
	 * @return
	 * @throws Exception
	 */
	public String queryItemDefines() throws Exception{
		getRoot().setRecords(paramComponent.queryItemDefines(query));
		return JSON_RECORDS;
	}
	/**
	 * 字段名参数查询
	 * @return
	 * @throws Exception
	 */
	public String queryTabDefine() throws Exception{
		getRoot().setPage(paramComponent.queryTabDefine(start, limit,query));
		return JSON_PAGE;
	}
	public String saveTabDefine() throws Exception{
		paramComponent.saveTabDefine(tabDefine,old_table_name,old_column_name);
		return JSON;
	}
	
	public String deleteTabDefine() throws Exception{
		paramComponent.deleteTabDefine(old_table_name,old_column_name);
		return JSON;
	}
	
	/**
	 * 查询参数配置
	 * @return
	 * @throws Exception
	 */
	public String queryItemValues() throws Exception{
		getRoot().setRecords(paramComponent.queryItemValues(itemKey));
		return JSON_RECORDS;
	}

	/**
	 * 保存或修改参数定义
	 * @return
	 * @throws Exception
	 */
	public String saveItemDefine() throws Exception{
		SItemDefine oldItem = paramComponent.queryItemDefineForChangeInfo(itemDefine.getItem_key());
		paramComponent.saveItemDefine(itemDefine);
		SItemDefine newItem = paramComponent.queryItemDefineForChangeInfo(itemDefine.getItem_key());
		saveChanges(oldItem, newItem, null, null,1);
		return JSON;
	}

	/**
	 * 删除一条参数定义记录
	 * @return
	 * @throws Exception
	 */
	public String deleteItemDefine() throws Exception{
		paramComponent.deleteItemDefine(itemKey);
		return JSON;
	}

	/**
	 * 保存参数配置
	 * @return
	 * @throws Exception
	 */
	public String saveItemValues() throws Exception{
		List<SItemvalue> valueList = null;
		if(StringHelper.isNotEmpty(records)){
			Type type = new TypeToken<List<SItemvalue>>(){}.getType();
			Gson gson = new Gson();
			valueList = gson.fromJson(records, type);
		}
		List<SItemvalue> oldList = paramComponent.queryItemValues(itemKey);
		paramComponent.saveItemValues(valueList,itemKey);
		List<SItemvalue> newList = paramComponent.queryItemValues(itemKey);
		SItemDefine newItem = paramComponent.queryItemDefineForChangeInfo(itemKey);
		saveChanges(null,newItem,oldList,newList,2);
		return JSON;
	}

	private void saveChanges(SItemDefine oldItem, SItemDefine newItem,List<SItemvalue> oldList, List<SItemvalue> newList,int type) throws ActionException{
		boolean valueChangeFlag = oldItem ==null && newItem ==null;
		boolean defineChangeFlag = CollectionHelper.isEmpty(oldList) && CollectionHelper.isEmpty(newList);
		if(valueChangeFlag && defineChangeFlag){
			throw new ActionException("记录系统参数变更时候,方法接受的参数信息有误!");
		}
		
		String key = newItem == null ? oldItem.getItem_key():newItem.getItem_key();;
		String keyDesc = newItem == null ? oldItem.getItem_desc() :newItem.getItem_desc();
		String changeDesc = type ==1 ? "系统参数定义" : "系统参数值定义";
		String content = null;
		
		try{
			if(type==1){
				content = BeanHelper.beanchange(oldItem, newItem);
			}else{
				content = BeanHelper.listchange(oldList, newList,new String[]{"item_value","item_name","item_idx","show_county_text"});
			}
			if(StringHelper.isNotEmpty(content)){
				SSysChange change = new SSysChange(SysChangeType.SYS_PARAM.toString(),
						paramComponent.getDoneCOde(), key, keyDesc, changeDesc,
						content, WebOptr.getOptr().getOptr_id(), new Date());
				
				paramComponent.getSSysChangeDao().save(change);
			}
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		
	}
	
	public ParamComponent getParamComponent() {
		return paramComponent;
	}

	public void setParamComponent(ParamComponent paramComponent) {
		this.paramComponent = paramComponent;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}


	public SItemDefine getItemDefine() {
		return itemDefine;
	}

	public void setItemDefine(SItemDefine itemDefine) {
		this.itemDefine = itemDefine;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getOld_table_name() {
		return old_table_name;
	}
	public void setOld_table_name(String oldTableName) {
		old_table_name = oldTableName;
	}
	public String getOld_column_name() {
		return old_column_name;
	}
	public void setOld_column_name(String oldColumnName) {
		old_column_name = oldColumnName;
	}
	public TTabDefine getTabDefine() {
		return tabDefine;
	}
	public void setTabDefine(TTabDefine tabDefine) {
		this.tabDefine = tabDefine;
	}

}
