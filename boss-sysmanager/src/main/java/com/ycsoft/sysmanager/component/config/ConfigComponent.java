package com.ycsoft.sysmanager.component.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.beans.system.SAgent;
import com.ycsoft.beans.system.SDataTranslation;
import com.ycsoft.business.dao.config.TBusiFeeStdDao;
import com.ycsoft.business.dao.config.TProvinceDao;
import com.ycsoft.business.dao.config.TServerOttauthProdDao;
import com.ycsoft.business.dao.config.TTemplateCountyDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.prod.PSpkgDao;
import com.ycsoft.business.dao.prod.PSpkgOpenbusifeeDao;
import com.ycsoft.business.dao.prod.PSpkgOpenuserDao;
import com.ycsoft.business.dao.resource.device.RDeviceFeeDao;
import com.ycsoft.business.dao.system.SAgentDao;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.service.externalImpl.IOttServiceExternal;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Component
public class ConfigComponent extends BaseComponent {

	@Autowired
	private TProvinceDao tProvinceDao;
	@Autowired
	private SAgentDao sAgentDao;
	@Autowired
	private PSpkgDao pSpkgDao;
	@Autowired
	private PSpkgOpenuserDao pSpkgOpenuserDao;
	@Autowired
	private PSpkgOpenbusifeeDao pSpkgOpenbusifeeDao;
	@Autowired
	private TTemplateCountyDao tTemplateCountyDao;
	@Autowired
	private RDeviceFeeDao rDeviceFeeDao;
	@Autowired
	private TBusiFeeStdDao tBusiFeeStdDao;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private TServerOttauthProdDao tServerOttauthProdDao;
	
	public List<TServerOttauthProd> queryAllOttAuth() throws Exception {
		return tServerOttauthProdDao.findAll();
	}
	
	public Pager<TServerOttauthProd> queryOttAuth(String query, Integer start, Integer limit) throws Exception {
		return tServerOttauthProdDao.queryOttAuth(query, start, limit);
	}
	
	public void saveOttAuth(TServerOttauthProd ottAuth, String type) throws Exception {
		if(tServerOttauthProdDao.countByFeeId(ottAuth.getId(), ottAuth.getFee_id()) > 0){
			throw new ComponentException("资费ID【"+ottAuth.getFee_id()+"】已存在");
		}
		if(type.equals("save")){
			if(tServerOttauthProdDao.findByKey(ottAuth.getId()) != null){
				throw new ComponentException("产品ID【"+ottAuth.getId()+"】已存在");
			}
			ottAuth.setStatus("0");		//默认待审核
			tServerOttauthProdDao.save(ottAuth);
		}else if(type.equals("update")){
			tServerOttauthProdDao.update(ottAuth);
		}
		
	}
	
	public Pager<PSpkg> querySpkg(String query, Integer start, Integer limit) throws Exception {
		Pager<PSpkg> page= pSpkgDao.querySpkg(query, start, limit);
		for(PSpkg ps:  page.getRecords()){
			String prodNameAll="";
			for(String prodName:pSpkgDao.queryProdName(ps.getSpkg_sn())){
				prodNameAll=prodNameAll+" "+prodName;
			}
			ps.setProd_name(prodNameAll);
		}
		return page;
	}
	
	public Map querySpkgInfoBySpkgId(String spId) throws Exception {
		List<PSpkgOpenuser> spkeUserList = pSpkgOpenuserDao.querySpkgUserBySpkgId(spId);
		List<PSpkgOpenbusifee> spkgFeeList = pSpkgOpenbusifeeDao.querySpkgOpenFeeBySpId(spId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", spkeUserList);
		map.put("busifee", spkgFeeList);
		return map;
	}
	
	public void saveSpkg(PSpkg spkg) throws Exception {
		if(pSpkgDao.countBySpkgSn(spkg.getSp_id(), spkg.getSpkg_sn()) > 0){
			throw new ComponentException("客户协议编号【"+spkg.getSpkg_sn()+"】已存在");
		}
		if(StringHelper.isEmpty(spkg.getSp_id())){
			spkg.setSp_id(pSpkgDao.findSequence().toString());
			spkg.setStatus(StatusConstants.IDLE);
			spkg.setOptr_id(WebOptr.getOptr().getOptr_id());
			pSpkgDao.save(spkg);
		}else{
			pSpkgDao.update(spkg);
		}
	}
	
	public void saveSpkgUser(PSpkgOpenuser spkgUser) throws Exception {
		if(StringHelper.isEmpty(spkgUser.getId())){
			spkgUser.setId(pSpkgOpenuserDao.findSequence("SEQ_AGENT_ID").toString());
			spkgUser.setStatus(StatusConstants.IDLE);
			spkgUser.setStatus_date(new Date());
			spkgUser.setOptr_id(WebOptr.getOptr().getOptr_id());
			pSpkgOpenuserDao.save(spkgUser);
		}else{
			spkgUser.setStatus_date(new Date());
			pSpkgOpenuserDao.update(spkgUser);
		}
	}
	
	public void saveSpkgBusiFee(PSpkgOpenbusifee spkgBusiFee) throws Exception {
		if(StringHelper.isEmpty(spkgBusiFee.getId())){
			spkgBusiFee.setId(pSpkgOpenuserDao.findSequence("SEQ_AGENT_ID").toString());
			spkgBusiFee.setStatus(StatusConstants.IDLE);
			spkgBusiFee.setStatus_date(new Date());
			spkgBusiFee.setOptr_id(WebOptr.getOptr().getOptr_id());
			pSpkgOpenbusifeeDao.save(spkgBusiFee);
		}else{
			spkgBusiFee.setStatus_date(new Date());
			pSpkgOpenbusifeeDao.update(spkgBusiFee);
		}
	}
	
	public void updateSpkgStatus(String spId, String newStatus) throws Exception {
		PSpkg spkg = pSpkgDao.findByKey(spId);
		if(spkg == null)
			throw new ComponentException("协议数据不存在!");
		
		if(newStatus.equals(StatusConstants.CONFIRM)){
			spkg.setStatus(newStatus);
			spkg.setConfirm_optr_id(WebOptr.getOptr().getOptr_id());
			spkg.setConfirm_date(new Date());
			pSpkgDao.update(spkg);
		}else if(newStatus.equals(StatusConstants.INVALID)){
			spkg.setStatus(StatusConstants.IDLE);
			pSpkgDao.update(spkg);
			cCustDao.clearSpkgSn(spkg.getSpkg_sn());
		}
	}
	
	public void updateSpkgUserStatus(String id, String newStatus) throws Exception {
		PSpkgOpenuser spkgUser = pSpkgOpenuserDao.findByKey(id);
		if(spkgUser == null)
			throw new ComponentException("协议用户数据不存在!");
		if(!spkgUser.getStatus().equals(newStatus)){
			spkgUser.setStatus(newStatus);
			spkgUser.setStatus_date(new Date());
			pSpkgOpenuserDao.update(spkgUser);
		}
	}
	
	public void updateSpkgBusiFeeStatus(String id, String newStatus) throws Exception {
		PSpkgOpenbusifee spkgFee = pSpkgOpenbusifeeDao.findByKey(id);
		if(spkgFee == null)
			throw new ComponentException("协议用户杂费数据不存在!");
		if(!spkgFee.getStatus().equals(newStatus)){
			spkgFee.setStatus(newStatus);
			spkgFee.setStatus_date(new Date());
			pSpkgOpenbusifeeDao.update(spkgFee);
		}
	}
	
	public void deleteSpkgUser(String id) throws Exception {
		PSpkgOpenuser spkgUser = pSpkgOpenuserDao.findByKey(id);
		if(spkgUser == null)
			throw new ComponentException("协议用户数据不存在!");
		pSpkgOpenuserDao.remove(id);
	}
	
	public void deleteSpkgBusiFee(String id) throws Exception {
		PSpkgOpenbusifee spkgFee = pSpkgOpenbusifeeDao.findByKey(id);
		if(spkgFee == null)
			throw new ComponentException("协议用户杂费数据不存在!");
		pSpkgOpenbusifeeDao.remove(id);
	}
	
	public List<RDeviceFee> queryDeviceFee(String deviceModel, String buyMode) throws Exception {
		String templateId = tTemplateCountyDao.getTemplateIdByCounty(WebOptr.getOptr().getCounty_id(), SystemConstants.TEMPLATE_TYPE_FEE);
		return rDeviceFeeDao.queryFee(null, deviceModel, buyMode, templateId);
	}
	
	public List<BusiFeeDto> queryBulkUserBusiFee() throws Exception {
		String templateId = tTemplateCountyDao.getTemplateIdByCounty(WebOptr.getOptr().getCounty_id(), SystemConstants.TEMPLATE_TYPE_FEE);
		return tBusiFeeStdDao.queryBusiFeeStdByBusiCode(templateId, BusiCodeConstants.OTHER_FEE);
	}
	
	public List<TProvince> queryProvince() throws Exception {
		return tProvinceDao.findAll();
	}
	
	public void saveProvince(List<TProvince> provinceList) throws Exception {
		//省定义配置没有新增，只有数据修改
		for(TProvince province : provinceList){
			TProvince tprov = tProvinceDao.findByKey(province.getId());
			
			if(tprov == null)
				throw new ComponentException("省定义不存在，行重新查询再编辑");

			if(tProvinceDao.countProvinceByName(province.getId(), province.getName()) > 0)
				throw new ComponentException("省【"+province.getName()+"】已存在");
			
			if( StringHelper.isNotEmpty(province.getCust_code()) && !province.getCust_code().equals(tprov.getCust_code()) ){
				if(tProvinceDao.countProvinceByCustCode(province.getId(), province.getCust_code()) > 0)
					throw new ComponentException("客户编号前缀【"+province.getCust_code()+"】已存在");
				try {
					tProvinceDao.createCustNameSeq(province.getCust_code());
				} catch (Exception e) {
					e.printStackTrace();
					throw new ComponentException("根据该客户编号前缀创建的客户编号序列已存在，请更换前缀");
				}
			}
		}
		tProvinceDao.update(provinceList.toArray(new TProvince[provinceList.size()]));
	}
	
	public List<SAgent> queryAllAgent() throws Exception {
		return sAgentDao.findAll();
	}
	
	public Pager<SAgent> queryAgent(String query, Integer start, Integer limit) throws Exception {
		return sAgentDao.queryAgent(query, start, limit);
	}
	
	public void saveAgent(SAgent agent) throws Exception {
		if(StringHelper.isEmpty(agent.getId())){
			agent.setId(sAgentDao.findSequence().toString());
			sAgentDao.save(agent);
		}else{
			sAgentDao.update(agent);
		}
	}
	
	public Pager<SDataTranslation> queryDataTranslation(String query, Integer start, Integer limit) throws Exception {
		return sDataTranslationDao.queryDataTranslation(query, start, limit);
	}
	
	public void saveDataTranslation(List<SDataTranslation> dataList) throws Exception {
		for(SDataTranslation data : dataList){
			if(sDataTranslationDao.countByDataCn(data.getId(), data.getData_cn()) > 0){
				throw new ComponentException("中文【"+data.getData_cn()+"】已存在");
			}
			if(StringHelper.isEmpty(data.getId())){
				data.setId(sDataTranslationDao.findSequence().toString());
				sDataTranslationDao.save(data);
			}else{
				sDataTranslationDao.update(data);
			}
		}
	}
	
	public void deleteDataTranslation(String[] ids) throws Exception {
		sDataTranslationDao.remove(ids);
	}
}