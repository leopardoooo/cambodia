package com.ycsoft.sysmanager.component.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.config.TRuleDefineCounty;
import com.ycsoft.beans.config.TRuleEdit;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDataRightType;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TRuleDefineCountyDao;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.config.TRuleEditDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.config.RuleDefineDto;
import com.ycsoft.sysmanager.dto.config.RuleEditDto;
import com.ycsoft.sysmanager.dto.config.VewRuleObjDto;
import com.ycsoft.sysmanager.dto.config.VewRulePropDto;

@Component
public class RuleComponent extends  BaseComponent {

	private TRuleDefineDao tRuleDefineDao;
	private TRuleEditDao tRuleEditDao;
	private SCountyDao sCountyDao;
	private TAddressDao tAddressDao;
	private TRuleDefineCountyDao tRuleDefineCountyDao;
	
	/**
	 * 明细规则配置
	 * @param rule
	 * @throws Exception
	 */
	public String updateDetailRule(RuleDefineDto ruleDto) throws Exception {
		TRuleDefine rule = new TRuleDefine();
		BeanUtils.copyProperties(ruleDto, rule);
		String ruleId = rule.getRule_id();
		if(StringHelper.isEmpty(ruleId)){
			ruleId = gRuleId();
			rule.setRule_id(ruleId);
			rule.setCfg_type(SystemConstants.RULE_CFG_TYPE_DETAIL);
			rule.setRule_type(SystemConstants.RULE_TYPE_DATA);
			tRuleDefineDao.save(rule);
		}else{
			tRuleDefineDao.update(rule);
			tRuleDefineCountyDao.delete(rule.getRule_id());
		}
		saveRuleCounty(ruleDto.getCounty_id().split(","), rule.getRule_id());
		return ruleId;
	}
	
	/**
	 * 根据用户选择的数据类型，动态查询数据
	 * @param tableName
	 * @param resultColumn
	 * @param selectColumn
	 * @return
	 * @throws Exception
	 */
	public List queryDynamicData(String tableName,String resultColumn,String selectColumn) throws Exception {
		return sDataRightTypeDao.queryDynamicData(tableName, resultColumn, selectColumn);
	}
	
	/**
	 * 查询所有数据类型
	 * @return
	 * @throws Exception
	 */
	public List<SDataRightType> queryAllSDataType() throws Exception {
		return sDataRightTypeDao.queryDataRight();
	}
	
	/**
	 * 保存或修改手工规则
	 * @param rule
	 * @throws Exception
	 */
	public String updateHandRule(RuleDefineDto ruleDto) throws Exception {
		TRuleDefine rule = new TRuleDefine();
		BeanUtils.copyProperties(ruleDto, rule);
		String ruleId = rule.getRule_id();
		if(StringHelper.isEmpty(ruleId)){
			ruleId = gRuleId();
			rule.setRule_id(ruleId);
			rule.setCfg_type(SystemConstants.RULE_CFG_TYPE_HAND);//手工
			tRuleDefineDao.save(rule);
		}else{
			tRuleDefineDao.update(rule);
			tRuleDefineCountyDao.delete(ruleId);
		}
		saveRuleCounty(ruleDto.getCounty_id().split(","), rule.getRule_id());
		return ruleId;
	}
	
	private void saveRuleCounty(String[] countyIdArr,String ruleId) throws Exception {
		List<TRuleDefineCounty> rcList = new ArrayList<TRuleDefineCounty>();
		for (int i = 0, len = countyIdArr.length; i < len; i++) {
			TRuleDefineCounty  ruleCounty = new TRuleDefineCounty();
			ruleCounty.setCounty_id(countyIdArr[i]);
			ruleCounty.setRule_id(ruleId);
			rcList.add(ruleCounty);
		}
		tRuleDefineCountyDao.save(rcList.toArray(new TRuleDefineCounty[rcList.size()]));
	}

	/**
	 * 保存或修改条件规则
	 * @param rule
	 * @param ruleEdits
	 * @throws Exception
	 */
	public String updateRuleAndRuleEdit(RuleDefineDto ruleDto,List<TRuleEdit> ruleEdits) throws Exception {
		TRuleDefine rule = new TRuleDefine();
		BeanUtils.copyProperties(ruleDto, rule);
		String ruleId = rule.getRule_id();
		if(StringHelper.isEmpty(ruleId)){
			ruleId = gRuleId();
			for(TRuleEdit ruleEdit : ruleEdits){
				ruleEdit.setRule_id(ruleId);
			}
			rule.setRule_id(ruleId);
			rule.setCfg_type(SystemConstants.RULE_CFG_TYPE_COND);
			tRuleDefineDao.save(rule);
		}else{
			tRuleDefineDao.update(rule);
			tRuleEditDao.deleteByRuleId(ruleId);
			tRuleDefineCountyDao.delete(ruleId);
		}
		tRuleEditDao.save(ruleEdits.toArray(new TRuleEdit[ruleEdits.size()]));
		saveRuleCounty(ruleDto.getCounty_id().split(","), rule.getRule_id());
		return ruleId;
	}
	
	/**
	 * 根据数据类型查询对应规则属性
	 * @param dataType
	 * @return
	 * @throws Exception
	 */
	public List<VewRulePropDto> queryRulePropByDataRightType(String dataType) throws Exception {
		return tRuleDefineDao.queryRulePropByDataRightType(dataType);
	}

	/**
	 * 根据ruleid 查询要编辑的规则信息
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	public List<TRuleEdit> queryRuleEditByRuleId(String ruleId) throws Exception {
		List<TRuleEdit> list = tRuleEditDao.queryRuleEditByRuleId(ruleId);
		/**
		 * bug: 0001254: 条件规则修改后值乱了
		 * 原因是在存入的时候, t_rule_edit表prop_name属性直接存储字面值
		 * 解决办法,重新查询下该属性.
		 * 不必更新,如果下次再查询,这里又会重新查询一次.
		 */
		for(TRuleEdit edit:list){
			String data_type = edit.getData_type();
			String prop_id = edit.getProp_id();
			String prop_value = edit.getProp_value();
			if(StringHelper.isEmpty(prop_value)){
				continue;
			}
			if(data_type.equalsIgnoreCase("F")){//functions类型,地址
				if(prop_id.equalsIgnoreCase("inAddr")){//铁定地址
					TAddress address = tAddressDao.findByKey(prop_value);
					if(null == address){
						throw new ComponentException("获取地址数据出错！");
					}
					edit.setProp_value_text("("+address.getAddr_name()+")");
				}
			}else if(data_type.equalsIgnoreCase("S")){
				edit.setProp_value_text(MemoryDict.getDictName(edit.getParam_name(), prop_value));
			}
		}
		return list;
	}

	/**
	 * 规则属性
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> queryRuleProp(String prop_name) throws Exception {
		List list = tRuleDefineDao.queryRuleProp(prop_name);
		return TreeBuilder.createTree( list );
	}

	/**
	 * 规则对应的 引用对象
	 * @param rule_id 规则id
	 * @return
	 * @throws Exception
	 */
	public List<VewRuleObjDto> queryRuleObjByRuleId(String ruleId) throws Exception{
		return tRuleDefineDao.queryRuleObjByRuleId(ruleId);
	}

	public TRuleDefine getTruleByRuleId(String ruleId) throws Exception{
		String sql = "select * from  T_RULE_DEFINE  where rule_id = ?  ";
		return tRuleDefineDao.getTruleByRuleId(ruleId);
	}
	
	public String gRuleId() throws Exception {
		return tRuleDefineDao.findSequence().toString();
	}

	public Pager<RuleDefineDto> queryAllRules(String query,String dataType,String countyId,Integer start,Integer limit) throws Exception {
		return tRuleDefineDao.queryAllRule(query,dataType,countyId,start,limit);
	}
	
	public List<TRuleDefine> queryPromotionRuleByCountyId(String countyId) throws Exception {
		return tRuleDefineDao.queryRuleByCountyId(countyId);
	}
	
	public List<SCounty> queryRuleCounty(SOptr optr) throws Exception {
		if(SystemConstants.COUNTY_ALL.equals(optr.getCounty_id())){
			return sCountyDao.findAll();
		}else {
			String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
			return sCountyDao.queryCountyByDataRight(dataRight);
		}
	}

	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}

	public void setTRuleEditDao(TRuleEditDao ruleEditDao) {
		tRuleEditDao = ruleEditDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}
	
	public void setTAddressDao(TAddressDao tAddressDao) {
		this.tAddressDao = tAddressDao;
	}

	public void setTRuleDefineCountyDao(TRuleDefineCountyDao ruleDefineCountyDao) {
		tRuleDefineCountyDao = ruleDefineCountyDao;
	}

	/**
	 * 为记录变更查询数据.
	 * @param rule
	 * @return
	 * @throws ComponentException
	 */
	public RuleEditDto queryRuleForCollectChangeInfo(RuleDefineDto rule1) throws ComponentException{
		RuleEditDto result = new RuleEditDto();
		if(null == rule1 || StringHelper.isEmpty(rule1.getRule_id())){
			return null;
		}
		RuleDefineDto rule = new RuleDefineDto();
		rule.setRule_id(rule1.getRule_id());
		try{
			TRuleDefine trule = tRuleDefineDao.findByKey(rule.getRule_id());
			BeanUtils.copyProperties(trule, rule);
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("rule_id", rule.getRule_id());
			List<TRuleDefineCounty> ruleCounty = tRuleDefineCountyDao.findByMap(params);
			if(ruleCounty.size() > 0){
				List<String> countys = CollectionHelper.converValueToList(ruleCounty, "county_id");
				String countyId = "";
				for(String c:countys){
					countyId += c +",";
				}
				if(countyId.length()>0)
					countyId=countyId.substring(0,countyId.length()-1);
				rule.setCounty_id(countyId);
			}
			result.setRule(rule);
			List<TRuleEdit> ruleEdits = queryRuleEditByRuleId(rule.getRule_id());
			result.setRuleEdits(ruleEdits);
		}catch (Exception e) {
			throw new ComponentException(e);
		}
		return result;
	}
}
