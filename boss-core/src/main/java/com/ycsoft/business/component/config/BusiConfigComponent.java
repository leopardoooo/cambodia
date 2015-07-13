package com.ycsoft.business.component.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.beans.config.TBandCheckType;
import com.ycsoft.beans.config.TBusiCode;
import com.ycsoft.beans.config.TBusiDocTemplatefile;
import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TNetType;
import com.ycsoft.beans.config.TOpenTemp;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.config.TTerminalAmount;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.beans.core.job.JOdscntRecord;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSubSystem;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TAcctFeeTypeDao;
import com.ycsoft.business.dao.config.TBandCheckTypeDao;
import com.ycsoft.business.dao.config.TBusiCodeDao;
import com.ycsoft.business.dao.config.TBusiDocDao;
import com.ycsoft.business.dao.config.TBusiDocTemplatefileDao;
import com.ycsoft.business.dao.config.TBusiFeeDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeDao;
import com.ycsoft.business.dao.config.TNetTypeDao;
import com.ycsoft.business.dao.config.TOpenTempDao;
import com.ycsoft.business.dao.config.TPublicAcctitemDao;
import com.ycsoft.business.dao.config.TTerminalAmountDao;
import com.ycsoft.business.dao.core.job.JOdscntRecordDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SSubSystemDao;
import com.ycsoft.business.dto.config.BusiDocDto;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.config.TemplateUpdatePorpDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.commons.store.TemplateConfig.Template;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.system.SDeptDto;
/**
 *
 * @author YC-SOFT
 *
 */
@Component
public class BusiConfigComponent extends BaseBusiComponent {

	private TBusiDocDao tBusiDocDao;
	private TBusiDocTemplatefileDao tBusiDocTemplatefileDao;
	private TBusiCodeDao tBusiCodeDao;
	private TPublicAcctitemDao tPublicAcctitemDao;
	private TDeviceBuyModeDao tDeviceBuyModeDao;
	private TBandCheckTypeDao tBandCheckTypeDao;
	private TNetTypeDao tNetTypeDao;
	private TAcctFeeTypeDao tAcctFeeTypeDao;
	private SDeptDao sDeptDao;
	private SSubSystemDao sSubSystemDao;
	private TOpenTempDao tOpenTempDao;
	private TTerminalAmountDao tTerminalAmountDao;
	private TBusiFeeDao tBusiFeeDao;
	private JOdscntRecordDao jOdscntRecordDao;
	
	/**
	 * 指定业务代码和模板返回可用的单据
	 * @return
	 * @throws Exception
	 */
	public List<BusiDocDto> queryDoc() throws Exception {
		return tBusiDocDao.queryByBusiCodeTemplate(queryTemplateId(SystemConstants.TEMPLATE_TYPE_DOC));
	}

	/**
	 * 根据业务编号和模版编号获取该业务可以修改的客户属性
	 * @param busiCode 业务编号
	 * @param templateId 模版编号
	 * @return
	 * @throws Exception
	 */
	public List<TUpdateCfg> queryCanUpdateField(String busiCode) throws Exception{
		TemplateUpdatePorpDto updateConfig = (TemplateUpdatePorpDto) TemplateConfig
				.loadConfig(Template.UPDPROP, getOptr().getCounty_id());
		return updateConfig.get(busiCode);
	}


	/**
	 * 根据单据类型编号 获得单据实体
	 * @param docType
	 * @return
	 * @throws JDBCException
	 */
	public TBusiDocTemplatefile queryBusiDoc(String docType) throws Exception {
		return tBusiDocTemplatefileDao.queryByDocType(docType,getOptr().getCounty_id());
	}

	/**
	 * 根据单据类型编号 获得单据实体
	 * @param docType
	 * @return
	 * @throws JDBCException
	 */
	public TBusiCode queryBusiCode(String busiCode) throws JDBCException {
		return tBusiCodeDao.findByKey(busiCode);
	}



	/**
	 * 查找系统配置的公用账目信息
	 * @return
	 * @throws Exception
	 */
	public List<TPublicAcctitem> qureyAcctitem() throws Exception{
		return tPublicAcctitemDao.findAll();
	}

	public TPublicAcctitem qureayPublicAcctitem() throws Exception{
		List<TPublicAcctitem> acctitemList= qureyAcctitem();
		for (TPublicAcctitem item:acctitemList){
			if (item.getAcctitem_type().equals(SystemConstants.ACCTITEM_PUBLIC)){
				return item;
			}

		}
		return null;
	}

	/**
	 * 查询所有子系统定义
	 * @return
	 * @throws Exception
	 */
	public List<SSubSystem> queryAllSubSystem(SOptr optr) throws Exception {
		return sSubSystemDao.queryAllSubSystem(optr);
	}

	/**
	 * 根据子系统id 返回对应的url，找不到返回空字符
	 * @param sysId
	 * @return
	 */
	public SSubSystem querySubSystem(String sysId) throws JDBCException {
		return sSubSystemDao.findByKey(sysId);
	}

	/**
	 * 根据编号查找设备销售方式信息
	 * @return
	 */

	public TDeviceBuyMode queryBuyMode(String buyMode) throws Exception{
		return tDeviceBuyModeDao.findByKey(buyMode);
	}

	/**
	 * 查询宽带绑定类型配置
	 * @return
	 */
	public List<TBandCheckType> queryBandCheckType() throws JDBCException {
		return tBandCheckTypeDao.findAll();
	}

	/**
	 * 查询宽带网络类型配置
	 * @return
	 */
	public List<TNetType> queryNetType() throws JDBCException {
		return tNetTypeDao.findAll();
	}

	public TAcctFeeType queryAcctFeeType(String feeType) throws JDBCException{
		return tAcctFeeTypeDao.findByKey(feeType);
	}

	/**
	 *
	 * @return
	 */
	public List<SDept> queryDepts() throws Exception {
		return sDeptDao.queryByCountyId(getOptr().getCounty_id());
	}
	
	/**
	 * 查询部门树
	 * @return
	 * @throws Exception
	 */
	public List<SDeptDto> queryDeptTree() throws Exception{
		List<SCounty> countys = querySwitchCounty();
		
		String countyIds = "";
		for(SCounty county : countys){
			String countyId = county.getCounty_id();
			if(SystemConstants.COUNTY_ALL.equals(countyId)){
				countyIds = SystemConstants.COUNTY_ALL;
				break;
			}else{
				countyIds = StringHelper.append(countyIds,countyId,",");
			}
		}
		
		List<SDeptDto> depts = sDeptDao.queryAllYYT(countyIds.split(","));
		
		return depts;
	}
	
	/**
	 * 查询部门树
	 * @return
	 * @throws Exception
	 */
	public List<SDeptDto> queryOtherDeptTree() throws Exception{
		List<SDeptDto> depts = sDeptDao.queryYYT(getOptr().getDept_id());
		return depts;
	}

	/**
	 * 查找临时授权配置
	 * @return
	 * @throws JDBCException
	 */
	public TOpenTemp queryOpenTempCfg(String userType) throws Exception {
		return tOpenTempDao.queryByCountyId(userType, getOptr().getCounty_id());
	}

	/**
	 * 查询终端限制数量
	 * @return
	 */
	public List<TTerminalAmount> queryTerminalAmount() throws Exception {
		return tTerminalAmountDao.queryByTemplateId(queryTemplateId(SystemConstants.TEMPLATE_TYPE_TERMINAL_AMOUNT));
	}
	
	/**
	 * 查询配置类型模板数据
	 * @return
	 * @throws Exception
	 */
	public TemplateConfigDto queryTemplateConfig() throws Exception{
		TemplateConfigDto dto = (TemplateConfigDto) TemplateConfig.loadConfig(
				Template.CONFIG, getOptr().getCounty_id());
		return dto;
	}
	
	/**
	 * 查找折旧费id
	 */
	public String queryZjFeeId()throws Exception {
		return this.tBusiFeeDao.queryZjFeeId();
	}
	
	/**
	 * 查找租赁费
	 */
	public TBusiFee queryZlFeeById()throws Exception {
		return this.tBusiFeeDao.findByKey(SystemConstants.LEASE_FEE_ID); 
	}
	
	public List<JOdscntRecord> queryRecordByDeptId() throws Exception {
		return jOdscntRecordDao.queryRecordByDeptId(getOptr().getDept_id());
	}
	/**
	 * @param deptDao the sDeptDao to set
	 */
	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public void setTAcctFeeTypeDao(TAcctFeeTypeDao acctFeeTypeDao) {
		tAcctFeeTypeDao = acctFeeTypeDao;
	}

	public void setTBusiDocDao(TBusiDocDao tBusiDocDao) {
		this.tBusiDocDao = tBusiDocDao;
	}

	public void setTPublicAcctitemDao(TPublicAcctitemDao publicAcctitemDao) {
		tPublicAcctitemDao = publicAcctitemDao;
	}

	public void setTDeviceBuyModeDao(TDeviceBuyModeDao deviceBuyModeDao) {
		tDeviceBuyModeDao = deviceBuyModeDao;
	}

	/**
	 * @param bandCheckTypeDao the tBandCheckTypeDao to set
	 */
	public void setTBandCheckTypeDao(TBandCheckTypeDao bandCheckTypeDao) {
		tBandCheckTypeDao = bandCheckTypeDao;
	}

	/**
	 * @param netTypeDao the tNetTypeDao to set
	 */
	public void setTNetTypeDao(TNetTypeDao netTypeDao) {
		tNetTypeDao = netTypeDao;
	}

	public void setTBusiCodeDao(TBusiCodeDao busiCodeDao) {
		tBusiCodeDao = busiCodeDao;
	}

	/**
	 * @param subSystemDao the sSubSystemDao to set
	 */
	public void setSSubSystemDao(SSubSystemDao subSystemDao) {
		sSubSystemDao = subSystemDao;
	}

	public void setTOpenTempDao(TOpenTempDao openTempDao) {
		tOpenTempDao = openTempDao;
	}

	/**
	 * @param terminalAmountDao the tTerminalAmountDao to set
	 */
	public void setTTerminalAmountDao(TTerminalAmountDao terminalAmountDao) {
		tTerminalAmountDao = terminalAmountDao;
	}

	public void setTBusiFeeDao(TBusiFeeDao busiFeeDao) {
		tBusiFeeDao = busiFeeDao;
	}

	/**
	 * @param busiDocTemplatefileDao the tBusiDocTemplatefileDao to set
	 */
	public void setTBusiDocTemplatefileDao(
			TBusiDocTemplatefileDao busiDocTemplatefileDao) {
		tBusiDocTemplatefileDao = busiDocTemplatefileDao;
	}

	public void setJOdscntRecordDao(JOdscntRecordDao odscntRecordDao) {
		jOdscntRecordDao = odscntRecordDao;
	}

}
