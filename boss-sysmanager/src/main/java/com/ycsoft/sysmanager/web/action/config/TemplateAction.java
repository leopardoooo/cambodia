package com.ycsoft.sysmanager.web.action.config;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.beans.config.TBusiCodeDoc;
import com.ycsoft.beans.config.TBusiFeeStd;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.beans.config.TOpenTemp;
import com.ycsoft.beans.config.TProdStatusRent;
import com.ycsoft.beans.config.TStbFilled;
import com.ycsoft.beans.config.TTemplate;
import com.ycsoft.beans.config.TTemplateColumn;
import com.ycsoft.beans.config.TTemplateCounty;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.beans.task.TBusiCodeTask;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.component.config.TemplateComponent;
import com.ycsoft.sysmanager.dto.config.TemplateTreeDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

/**
 * @Description:
 * @author  eagle
 * @date Apr 1, 2010 10:09:33 AM
 */
public class TemplateAction extends BaseAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 3930307472679076748L;
	private TemplateComponent templateComponent;

	private String templateId;
	private String templateType;
	private String templateList;
	private String countyIds;
	private String templateName;
	private String copyTemplateId;
	private TTemplate template;
	
	private String feeStdId;
	private String deviceBuyMode;
	private String deviceType;
	private String feeId;
	
	private TBusiFeeStd busiFeeStd;
	private String deviceModelListStr;
	
	private String configName;
	private String query;
	
	private String feeColumnStr; 
	private String[] columnIds;
	private String[] optrIds;
	private String type;

	/**
	 * 查询模板菜单树
	 * @return
	 * @throws JDBCException
	 */
	public String queryTemplateTree() throws Exception{
		List treeList = templateComponent.queryTemplateTree();
		TemplateTreeDto template = new TemplateTreeDto();
		template.setTreeList(TreeBuilder.createTree(treeList));
		String changeCountyIds = "";
		try {
			changeCountyIds = templateComponent.queryChangeCounty();
		} catch (Exception e) {
		}
		if(StringHelper.isNotEmpty(changeCountyIds)){
			template.setChangeCountyIds(changeCountyIds.substring(changeCountyIds.indexOf("(")+1, changeCountyIds.indexOf(")")));
		}
		getRoot().setSimpleObj(template);
		return JSON_SIMPLEOBJ;
	}
	
	public String queryFeeTemplateTree() throws Exception {
		List<TreeDto> list = templateComponent.queryFeeTemplateTree(optr);
		List feeTreeList = new ArrayList();
		for(Object obj : list){
			TreeDto tree = (TreeDto) obj;
			if(tree.getAttr().indexOf("FEE_type") == 0){
				feeTreeList.add(tree);
			}
		}
		String pId = ((TreeDto)feeTreeList.get(0)).getId();
		for(Object obj : list){
			TreeDto tree = (TreeDto) obj;
			if(tree.getPid().equals(pId)){
				feeTreeList.add(tree);
			}
		}
		getRoot().setRecords(TreeBuilder.createTree(feeTreeList));
		return JSON_RECORDS;
	}
	
	public String queryTemplateOptr() throws Exception {
		List list = templateComponent.queryTemplateOptr(columnIds, type);
		getRoot().setRecords(TreeBuilder.createTreeCheck(list));
		return JSON_RECORDS;
	}
	
	public String saveTemplateToOptrs() throws Exception {
		templateComponent.saveTemplateToOptrs(columnIds, optrIds);
		return JSON;
	}

	/**
	 * 业务信息
	 * @return
	 * @throws Exception
	 */
	public String querybusi() throws Exception{
		getRoot().setRecords(templateComponent.querybusi()) ;
		return JSON_RECORDS;
	}


/*****************************业务费用****************************/
	/**
	 * @Description:查询业务费用类型
	 * @return
	 * @throws Exception
	 */
	public String queryBusiFeeForStdCfg() throws Exception{
		getRoot().setRecords(templateComponent.queryBusiFeeForStdCfg(templateId, countyIds)) ;
		return JSON_RECORDS;
	}

	/**
	 * 查询业务类型模板
	 */
	public String queryFeeTpls() throws Exception{
		getRoot().setSimpleObj(templateComponent.queryByTemplateId(templateId));
		return JSON_SIMPLEOBJ;
	}
	
	public String queryFeeTemplateColumn() throws Exception {
		getRoot().setRecords(templateComponent.queryFeeTemplateColumn(templateId, query));
		return JSON_RECORDS;
	}
	
	public String updateFeeColumn() throws Exception {
		List<TTemplateColumn> columnList = new ArrayList<TTemplateColumn>();
		if(StringHelper.isNotEmpty(feeColumnStr)){
			Type type = new TypeToken<List<TTemplateColumn>>(){}.getType();
			Gson gson = new Gson();
			columnList = gson.fromJson(feeColumnStr,type);
		}
		templateComponent.updateColumn(columnList);
		return JSON_SUCCESS;
	}
	
	/**
	 * 查找费用标准可以选择的设备型号
	 * @param templateId
	 * @param deviceBuyMode
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public String qeuryDeviceModelForStdCfg() throws Exception{
		getRoot().setRecords(templateComponent.qeuryDeviceModelForStdCfg(templateId, feeStdId, deviceBuyMode, deviceType,feeId,countyIds.split(",")));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存或修改一条收费标准
	 * @param entity
	 * @throws Exception
	 */
	public String saveFeeConfig() throws Exception {
		//记录异动
		TBusiFeeStd oldbean=templateComponent.queryTBusiFeeStd(busiFeeStd.getTemplate_id(), busiFeeStd.getFee_std_id());
		
		templateComponent.saveFeeConfig(busiFeeStd,deviceModelListStr);
		//记录异动
		TBusiFeeStd newbean=templateComponent.queryTBusiFeeStd(busiFeeStd.getTemplate_id(), busiFeeStd.getFee_std_id());
		templateComponent.saveTemplateBsuiFeeChange(busiFeeStd.getTemplate_id(), oldbean, newbean);
		return JSON;
	}
	
	/**
	 * 根据费用标准ID删除记录
	 * @param feeStdId
	 * @throws JDBCException
	 */
	public String deleteFeeConfig() throws Exception{
		//记录异动
		TBusiFeeStd oldbean=templateComponent.queryTBusiFeeStd(templateComponent.queryTBusiFeeStd(feeStdId).getTemplate_id(),feeStdId);
		
		templateComponent.deleteFeeConfig(feeStdId);
		
		templateComponent.saveTemplateBsuiFeeChange(oldbean.getTemplate_id(), oldbean, null);
		return JSON;
	}
	
	public String saveFeeTpls() throws Exception{
		//记录异动
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		
		templateComponent.saveFeeTpls(templateId, templateType, countyIds.split(","));
		
		List<TTemplateCounty>  newcountys= templateComponent.queryTemplateCountys(templateId);
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		return JSON;
	}

/*****************************业务单据****************************/
	/**
	* @Description:查询单据类型
	* @return
	* @throws Exception
	 */
	public String querydoc()throws Exception {
		getRoot().setRecords(templateComponent.querydoc());
		return JSON_RECORDS;
	}

	/**
	 * 根据模板ID查询业务单据配置
	 * @author eagle
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public String queryDocTpls() throws JDBCException{
		getRoot().setRecords(templateComponent.queryDocTpls(templateId));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存业务单据配置
	 * @param templateId
	 * @param templateType
	 * @param docList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveDocTpls() throws Exception{
		//记录异动
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		List<TBusiCodeDoc> oldList= templateComponent.queryDocTpls(templateId);
		
		List<TBusiCodeDoc> docList = new ArrayList<TBusiCodeDoc>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TBusiCodeDoc>>(){}.getType();
			Gson gson = new Gson();
			docList = gson.fromJson(templateList,type);
		}
		templateComponent.saveDocTpls(templateId, templateType, docList, countyIds.split(","));
		
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		List<TBusiCodeDoc> newList= templateComponent.queryDocTpls(templateId);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "busi_name","doc_name");
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		
		return JSON;
	}

/*****************************业务工单****************************/
	/**
	 * 根据模板ID查询业务工单配置
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public String queryTaskTpls() throws Exception{
		getRoot().setRecords(templateComponent.queryTaskTpls(templateId));
		return JSON_RECORDS;
	}

	/**
	 * 查询工单类型
	 * @return
	 * @throws Exception
	 */
	public String querytask() throws Exception{
		getRoot().setRecords(templateComponent.querytask()) ;
		return JSON_RECORDS;
	}
	
	/**
	 * 保存业务工单配置
	 * @param templateId
	 * @param templateType
	 * @param taskList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveTaskTpls() throws Exception{
		List<TBusiCodeTask> oldList=templateComponent.queryTaskTpls(templateId);
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		
		List<TBusiCodeTask> taskList = new ArrayList<TBusiCodeTask>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TBusiCodeTask>>(){}.getType();
			Gson gson = new Gson();
			taskList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveTaskTpls(templateId, templateType, taskList, countyIds.split(","));
		//记录异动
		List<TBusiCodeTask> newList=templateComponent.queryTaskTpls(templateId);
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "busi_name","detail_type_name");
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		
		return JSON;
	}

/*****************************信息修改****************************/

	/**
	 * 查询跟信息修改相关的业务
	 * @return
	 * @throws Exception
	 */
	public String queryBusiForUpdate() throws Exception{
		getRoot().setRecords(templateComponent.queryBusiForUpdate());
		return JSON_RECORDS;
	}
	
	/**
	 * 查询字段名
	 */
	public String queryFields() throws Exception{
		getRoot().setRecords(templateComponent.queryFields());
		return JSON_RECORDS;
	}
	/**
	 * 查询信息修改模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public String queryUpdCfgTpls() throws Exception{
		getRoot().setRecords(templateComponent.queryUpdCfgTpls(templateId));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存信息修改模板数据
	 * @param templateId
	 * @param templateType
	 * @param invoiceList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveUpdCfgTpls() throws Exception{
		//记录异动
		List<TUpdateCfg> oldList=templateComponent.queryUpdCfgTpls(templateId);
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		
		List<TUpdateCfg> updList = new ArrayList<TUpdateCfg>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TUpdateCfg>>(){}.getType();
			Gson gson = new Gson();
			updList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveUpdCfgTpls(templateId, templateType, updList, countyIds.split(","));
		//记录异动
		List<TUpdateCfg> newList=templateComponent.queryUpdCfgTpls(templateId);
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "busi_name","remark");
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		
		return JSON;
	}

/****************************计费****************************/
	/**
	 * 查询产品状态
	 * @return
	 * @throws JDBCException
	 */
	public String queryProdStatus() throws JDBCException{
		getRoot().setRecords(templateComponent.queryProdStatus());
		return JSON_RECORDS;
	}
	/**
	 * 查询计费模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public String queryBillTpls() throws Exception{
		getRoot().setRecords(templateComponent.queryBillTpls(templateId));
		return JSON_RECORDS;
	}
	
	public String queryTemplateCountyById() throws Exception {
		getRoot().setRecords(templateComponent.queryTemplateCountys(templateId));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存计费模板数据
	 * @param templateId
	 * @param templateType
	 * @param billList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveBillTpls() throws Exception{
		//记录异动
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		List<TProdStatusRent> oldList=templateComponent.queryBillTpls(templateId);
		
		List<TProdStatusRent> billList = new ArrayList<TProdStatusRent>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TProdStatusRent>>(){}.getType();
			Gson gson = new Gson();
			billList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveBillTpls(templateId, templateType, billList, countyIds.split(","));
		
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		List<TProdStatusRent> newList=templateComponent.queryBillTpls(templateId);
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "status_desc","is_cal_rent_text");
		
		return JSON;
	}

/****************************发票打印****************************/
	/**
	 * 查询发票打印项
	 */
	public String queryPrintItem() throws JDBCException{
		getRoot().setRecords(templateComponent.queryPrintItem());
		return JSON_RECORDS;
	}

	/**
	 * 查询发票打印种类
	 * @return
	 * @throws JDBCException
	 */
	public String queryInvoiceType() throws JDBCException{
		getRoot().setRecords(templateComponent.queryInvoiceType());
		return JSON_RECORDS;
	}

	/**
	 * 查询发票打印模板数据
	 * @return
	 * @throws JDBCException
	 */
	public String queryInvoiceTpls() throws JDBCException{
		getRoot().setRecords(templateComponent.queryInvoiceTpls(templateId));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存发票打印模板数据
	 * @param templateId
	 * @param templateType
	 * @param invoiceList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveInvoiceTpls() throws Exception{
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		List<TInvoicePrintitem> oldList=templateComponent.queryInvoiceTpls(templateId);
		//记录异动
		List<TInvoicePrintitem> invoiceList = new ArrayList<TInvoicePrintitem>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TInvoicePrintitem>>(){}.getType();
			Gson gson = new Gson();
			invoiceList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveInvoiceTpls(templateId, templateType, invoiceList, countyIds.split(","));
		
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		List<TInvoicePrintitem> newList=templateComponent.queryInvoiceTpls(templateId);
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "printitem_name","doc_name");
		return JSON;
	}

/****************************配置****************************/
	/**
	 * 查询配置种类
	 * @return
	 * @throws JDBCException
	 */
//	public String queryConfigs() throws JDBCException{
//		getRoot().setRecords(templateComponent.queryConfigs());
//		return JSON_RECORDS;
//	}

	/**
	 * 查询配置模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public String queryConfigTpls() throws JDBCException{
		getRoot().setRecords(templateComponent.queryConfigTpls(templateId));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存配置模板数据
	 * @param templateId
	 * @param templateType
	 * @param configList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveConfigTpls() throws Exception{
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		List<TConfigTemplate> oldList=templateComponent.queryConfigTpls(templateId);
		//TODO 记录异动
		List<TConfigTemplate> configList = new ArrayList<TConfigTemplate>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TConfigTemplate>>(){}.getType();
			Gson gson = new Gson();
			configList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveConfigTpls(templateId, templateType, configList, countyIds.split(","),optr);
		
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		List<TConfigTemplate> newList=templateComponent.queryConfigTpls(templateId);
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "remark","config_value_text");
		return JSON;
	}

/****************************临时授权****************************/
	public String queryOpenTemps() throws JDBCException{
		getRoot().setRecords(templateComponent.queryOpenTemps(templateId));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存临时授权配置
	 * @param templateId
	 * @param templateType
	 * @param openList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveOpenTemps() throws Exception{
		// 记录异动
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		List<TOpenTemp>  oldList=templateComponent.queryOpenTemps(templateId);
		
		
		List<TOpenTemp> openList = new ArrayList<TOpenTemp>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TOpenTemp>>(){}.getType();
			Gson gson = new Gson();
			openList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveOpenTemps(templateId, templateType, openList, countyIds.split(","));
		
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		List<TOpenTemp>  newList=templateComponent.queryOpenTemps(templateId);
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "user_type","cycle","times","days");
		
		return JSON;
	}

/****************************机顶盒灌装***************************/
	/**
	 * 查询服务类型为DTV的资源
	 * @return
	 * @throws JDBCException
	 */
	public String queryDtvRes() throws Exception {
		getRoot().setRecords(templateComponent.queryDtvRes()) ;
		return JSON_RECORDS;
	}

	/**
	 * 根据模板ID查询机顶盒灌装数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public String queryStbFilleds() throws Exception {
		getRoot().setRecords(templateComponent.queryStbFilleds(templateId)) ;
		return JSON_RECORDS;
	}
	
	/**
	 * 保存机顶盒灌装配置
	 * @param templateId
	 * @param templateType
	 * @param stbList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public String saveStbFilled() throws Exception {
		//记录异动
		List<TTemplateCounty> oldcountys= templateComponent.queryTemplateCountys(templateId);
		List<TStbFilled> oldList=templateComponent.queryStbFilleds(templateId);
		
		List<TStbFilled> stbList = new ArrayList<TStbFilled>();
		if(StringHelper.isNotEmpty(templateList)){
			Type type = new TypeToken<List<TStbFilled>>(){}.getType();
			Gson gson = new Gson();
			stbList = gson.fromJson(templateList,type);
		}
		
		templateComponent.saveStbFilled(templateId, templateType, stbList, countyIds.split(","));
		
		List<TTemplateCounty> newcountys= templateComponent.queryTemplateCountys(templateId);
		List<TStbFilled> newList=templateComponent.queryStbFilleds(templateId);
		templateComponent.saveTemplateCountyChange(templateId, oldcountys, newcountys);
		templateComponent.saveTemplateContentChange(templateId, oldList, newList, "res_name","months");
		
		return JSON;
	}
	

	public String createTemplate() throws Exception{
		//TODO 记录异动
		getRoot().setSimpleObj(templateComponent.createTemplate(templateType, templateName, copyTemplateId,optr.getOptr_id()));
		return JSON;
	}

	public String deleteTemplate() throws Exception{
		templateComponent.deleteTemplate(templateId);
		return JSON;
	}

	public String editTemplate() throws Exception{
		templateComponent.editTemplate(templateId,templateName);
		return JSON;
	}

	/**
	 * 根据模板类型查找模板
	 * @param templateType
	 * @return
	 * @throws Exception
	 */
	public String queryTplsByType() throws Exception{
		getRoot().setRecords(templateComponent.queryTplsByType(templateType,optr));
		return JSON_RECORDS;
	}

	/**
	 * 根据配置名称和地区查配置
	 * @param configName
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public String queryConfigByConfigName() throws Exception{
		getRoot().setSimpleObj(templateComponent.queryConfigByConfigName(configName, optr.getCounty_id()));
		return JSON_SIMPLEOBJ;
	}

	public TemplateComponent getTemplateComponent() {
		return templateComponent;
	}


	public void setTemplateComponent(TemplateComponent templateComponent) {
		this.templateComponent = templateComponent;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}


	public String getTemplateType() {
		return templateType;
	}


	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getTemplateList() {
		return templateList;
	}


	public void setTemplateList(String templateList) {
		this.templateList = templateList;
	}

	public String getCountyIds() {
		return countyIds;
	}

	public void setCountyIds(String countyIds) {
		this.countyIds = countyIds;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


	public String getCopyTemplateId() {
		return copyTemplateId;
	}


	public void setCopyTemplateId(String copyTemplateId) {
		this.copyTemplateId = copyTemplateId;
	}

	public TTemplate getTemplate() {
		return template;
	}

	public void setTemplate(TTemplate template) {
		this.template = template;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getFeeStdId() {
		return feeStdId;
	}

	public String getDeviceBuyMode() {
		return deviceBuyMode;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setFeeStdId(String feeStdId) {
		this.feeStdId = feeStdId;
	}

	public void setDeviceBuyMode(String deviceBuyMode) {
		this.deviceBuyMode = deviceBuyMode;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public TBusiFeeStd getBusiFeeStd() {
		return busiFeeStd;
	}

	public void setBusiFeeStd(TBusiFeeStd busiFeeStd) {
		this.busiFeeStd = busiFeeStd;
	}

	public void setDeviceModelListStr(String deviceModelListStr) {
		this.deviceModelListStr = deviceModelListStr;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setFeeColumnStr(String feeColumnStr) {
		this.feeColumnStr = feeColumnStr;
	}

	public void setColumnIds(String[] columnIds) {
		this.columnIds = columnIds;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOptrIds(String[] optrIds) {
		this.optrIds = optrIds;
	}

}
