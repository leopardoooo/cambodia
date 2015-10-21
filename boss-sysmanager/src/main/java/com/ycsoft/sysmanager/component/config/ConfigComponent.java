package com.ycsoft.sysmanager.component.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.system.SAgent;
import com.ycsoft.beans.system.SDataTranslation;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.business.dao.config.TProvinceDao;
import com.ycsoft.business.dao.system.SAgentDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;

@Component
public class ConfigComponent extends BaseComponent {

	@Autowired
	private TProvinceDao tProvinceDao;
	@Autowired
	private SAgentDao sAgentDao;
	@Autowired
	private MemoryComponent memoryComponent;
	
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