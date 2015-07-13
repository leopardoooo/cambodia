package com.ycsoft.sysmanager.web.action.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ycsoft.beans.config.TRuleEdit;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.config.RuleComponent;
import com.ycsoft.sysmanager.component.system.ParamComponent;
import com.ycsoft.sysmanager.dto.config.RuleDefineDto;
import com.ycsoft.sysmanager.dto.config.RuleEditDto;

public class RuleAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -4755713479110498142L;

	private RuleComponent ruleComponent;
	private ParamComponent paramComponent;
	private RuleDefineDto rule;
	private String ruleId;
	private String comboQueryText;
	private String model_name;
	private String tableName;
	private String resultColumn;
	private String selectColumn;
	private String dataType;
	private String query;
	
	/**
	 * 根据数据类型查询对应规则属性
	 * @return
	 * @throws Exception
	 */
	public String queryRulePropByDataRightType() throws Exception {
		getRoot().setRecords(ruleComponent.queryRulePropByDataRightType(dataType));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据用户选择的数据类型，动态查询数据
	 * @return
	 * @throws Exception
	 */
	public String queryDynamicData() throws Exception {
		getRoot().setRecords(ruleComponent.queryDynamicData(tableName, resultColumn, selectColumn));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询所有数据类型
	 * @return
	 * @throws Exception
	 */
	public String queryAllSDataType() throws Exception {
		getRoot().setRecords(ruleComponent.queryAllSDataType());
		return JSON_RECORDS;
	}

	/**
	 * 地址树查询
	 * @return
	 * @throws Exception
	 */
	public String findAllAddress() throws Exception {
		List list = paramComponent.findAllAddress(comboQueryText,optr.getCounty_id());
		List<TreeNode> addTree = TreeBuilder.createTree(list);
		getRoot().setRecords(addTree);
		return JSON_RECORDS;
	}
	
	/**
	 * 明细规则配置
	 * @return
	 * @throws Exception
	 */
	public String updateDetailRule() throws Exception {
		RuleEditDto former = ruleComponent.queryRuleForCollectChangeInfo(rule);
		String ruleid = ruleComponent.updateDetailRule(rule);
		rule.setRule_id(ruleid);
		RuleEditDto newOne = ruleComponent.queryRuleForCollectChangeInfo(rule);
		saveSysChange(former,newOne);
		return JSON;
	}
	
	/**
	 * 手工规则配置
	 * @return
	 * @throws Exception
	 */
	public String updateHandRule() throws Exception {
		
		RuleEditDto former = ruleComponent.queryRuleForCollectChangeInfo(rule);
		rule.setRule_id(ruleComponent.updateHandRule(rule));
		RuleEditDto newOne = ruleComponent.queryRuleForCollectChangeInfo(rule);
		saveSysChange(former,newOne);
		return JSON;
	}

	/**
	 * 保存或修改条件规则
	 * @return
	 * @throws Exception
	 */
	public String updateRuleAndRuleEdit() throws Exception {
		String ruleInfo = request.getParameter("ruleEditDto");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		RuleEditDto ruleEditDto = gson.fromJson(ruleInfo, RuleEditDto.class);
		//规则对象
		RuleDefineDto rule = ruleEditDto.getRule();
		//规则编辑对象
		List<TRuleEdit> ruleEdits = ruleEditDto.getRuleEdits();
		
		RuleEditDto former = ruleComponent.queryRuleForCollectChangeInfo(rule);
		rule.setRule_id(ruleComponent.updateRuleAndRuleEdit(rule, ruleEdits));
		RuleEditDto theNewOne = ruleComponent.queryRuleForCollectChangeInfo(rule);
		
		saveSysChange(former,theNewOne);
		
		return JSON_SUCCESS;
	}
	
	private void saveSysChange(RuleEditDto former, RuleEditDto theNewOne) throws Exception{
		if(former ==null && theNewOne == null){
			throw new ActionException("比较的两个对象都为空！");
		}
		if(former ==null && theNewOne !=null){
			former = new RuleEditDto();
		}else if(theNewOne ==null && former !=null){
			theNewOne = new RuleEditDto();
		}
		RuleDefineDto oldRuleObj = former.getRule();
		if(null !=oldRuleObj){
			oldRuleObj.setOptr_id(MemoryDict.getDictName(DictKey.OPTR, oldRuleObj.getOptr_id()));
		}
		RuleDefineDto newRuleObj = theNewOne.getRule();
		if(null !=newRuleObj){
			String optrId = newRuleObj.getOptr_id();
			newRuleObj.setOptr_id(MemoryDict.getDictName(DictKey.OPTR, optrId));
		}
		
		List<SSysChange> changes = new ArrayList<SSysChange>();
		String [] basicChangeFields = new String [] {"rule_id","rule_name","remark","rule_type","data_type","cfg_type","pre_billing_rule","optr_id","eff_date","exp_date"};
		String basicChange = BeanHelper.beanchange(oldRuleObj, newRuleObj,basicChangeFields);
		//两个肯定最多只有一个为空,因此下面的方法可行
		String key = theNewOne ==null ? oldRuleObj.getRule_id() : theNewOne.getRule().getRule_id();
		String keyDesc = theNewOne ==null ? oldRuleObj.getRule_name() : theNewOne.getRule().getRule_name();
		
		String optrId = getOptr().getOptr_id();
		
		if(!StringHelper.isEmpty(basicChange)){
			//两个肯定最多只有一个为空,因此下面的方法可行
			SSysChange change = new SSysChange(SysChangeType.RULE.toString(),
					ruleComponent.getDoneCOde(), key, keyDesc, "规则基本定义", basicChange, optrId, new Date());
			changes.add(change);
		}
		
		String detailChange = BeanHelper.beanchange(oldRuleObj, newRuleObj,"rule_str");
		if(!StringHelper.isEmpty(detailChange)){
			//两个肯定最多只有一个为空,因此下面的方法可行
			SSysChange change = new SSysChange(SysChangeType.RULE.toString(),
					ruleComponent.getDoneCOde(), key, keyDesc, "规则详细内容", detailChange, optrId, new Date());
			changes.add(change);
		}
		
		String countyChange = BeanHelper.beanchange(oldRuleObj, newRuleObj,"county_name");
		if(!StringHelper.isEmpty(countyChange)){
			//两个肯定最多只有一个为空,因此下面的方法可行
			SSysChange change = new SSysChange(SysChangeType.RULE.toString(),
					ruleComponent.getDoneCOde(), key, keyDesc, "规则适用分公司", countyChange, optrId, new Date());
			changes.add(change);
		}
		
		if(!changes.isEmpty()){
			ruleComponent.getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
		}
		
	}
	
	public String queryRuleEditByRuleId() throws Exception{
		getRoot().setRecords(ruleComponent.queryRuleEditByRuleId(ruleId));
		return JSON_RECORDS;
	}

	/**
	 * 规则属性
	 * @return
	 * @throws Exception
	 */
	public String queryRuleProp() throws Exception {
		getRoot().setRecords(ruleComponent.queryRuleProp(comboQueryText));
//		getRoot().setRecords(tRuleComponent.queryRuleProp(model_name));
		return JSON_RECORDS;
	}

	/**
	 * 规则对应的 引用对象
	 * @param rule_id 规则id
	 * @return
	 * @throws Exception
	 */
	public String queryRuleObjByRuleId() throws Exception{
		getRoot().setRecords(ruleComponent.queryRuleObjByRuleId(ruleId));
		return JSON_RECORDS;
	}

	public String getTruleByRuleId() throws Exception{
		getRoot().setSimpleObj(ruleComponent.getTruleByRuleId(ruleId));
		return JSON_SIMPLEOBJ;
	}

	public String queryAllRules() throws Exception {
		getRoot().setPage(ruleComponent.queryAllRules(query,dataType,optr.getCounty_id(),start,limit));
		return JSON_PAGE;
	}
	
	public String queryRuleCounty() throws Exception {
		getRoot().setRecords(ruleComponent.queryRuleCounty(optr));
		return JSON_RECORDS;
	}

	public void setTRuleComponent(RuleComponent ruleComponent) {
		ruleComponent = ruleComponent;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public void setComboQueryText(String comboQueryText) {
		this.comboQueryText = comboQueryText;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public void setParamComponent(ParamComponent paramComponent) {
		this.paramComponent = paramComponent;
	}

	public RuleDefineDto getRule() {
		return rule;
	}

	public void setRule(RuleDefineDto rule) {
		this.rule = rule;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setResultColumn(String resultColumn) {
		this.resultColumn = resultColumn;
	}

	public void setSelectColumn(String selectColumn) {
		this.selectColumn = selectColumn;
	}

	public void setRuleComponent(RuleComponent ruleComponent) {
		this.ruleComponent = ruleComponent;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}

