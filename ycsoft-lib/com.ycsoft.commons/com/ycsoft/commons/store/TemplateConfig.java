package com.ycsoft.commons.store;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TTemplate;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.config.TemplateUpdatePorpDto;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;

/**
 * 模板配置
 *
 * @author liujiaqi
 *
 */
public class TemplateConfig {
	private static TTemplateDao tTemplateDao;
	public enum Template {
		/** 计费 */
		BILLING,
		/** 配置 */
		CONFIG,
		/** 单据 */
		DOC,
		/** 费用 */
		FEE,
		/** 发票 */
		INVOICE,
		/** 工单 */
		TASK,
		/** 信息修改 */
		UPDPROP,
		/**是否必须基本包**/
		NEED_BASE_PROD,
		/**分公司账户是否启用**/
		GENERAL_ACCT_FLAG,
		/**基本产品免费天数**/
		BASE_PROD_FREE_DAY
	};

	/**
	 *
	 */
	private static Map<Template, Map<String, TTemplate>> configMap = new HashMap<Template, Map<String, TTemplate>>();


	/**
	 *地区模板对应表
	 * key county_id
	 * value template_id
	 */
	public static Map<String, String> configCounty = new HashMap<String, String>();
	/**
	 * @param template
	 *            模板类型
	 * @param countyId
	 *            地区
	 * @return
	 */
	public static TTemplate loadConfig(Template template,
			String countyId) throws ComponentException {
		Map<String, TTemplate> templates = configMap.get(template);
		if (templates == null) {
			throw new ComponentException("找不到对应的模板配置"+template.name());
		}
		TTemplate t = templates.get(countyId);
		if (t == null)
			throw new ComponentException("地区" + countyId + "找不到对应的模板配置"
					+ template.name());
		return t;
	}

	/**
	 * 设置数据加载Dao
	 * @param dao
	 */
	public static void loadData(TTemplateDao dao) throws Exception {
		tTemplateDao = dao;
		loadData();
	}

	public static void loadData() throws Exception{
		if (tTemplateDao!=null){
			configMap.put(Template.CONFIG, allConfigTemplate());
			configMap.put(Template.UPDPROP, allUpdatePropTemplate());
			//TODO 其他模板加载
		}
	}

	/**
	 * 所有的属性修改模板
	 * @return
	 */
	private static Map<String, TTemplate> allUpdatePropTemplate() throws Exception {
		Map<String, TTemplate> result =new HashMap<String, TTemplate> ();
		List<TUpdateCfg> allUpdateProp = tTemplateDao.queryAllUpdateProp();
		Map<String, List<TUpdateCfg>> allUpdatePropMap =
			CollectionHelper.converToMap(allUpdateProp, "county_id");
		Iterator<String> it = allUpdatePropMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			List<TUpdateCfg> l =allUpdatePropMap.get(key);
			TemplateUpdatePorpDto dto = new TemplateUpdatePorpDto();
			dto.setUpdateList(l);
			dto.setUpdateMap(CollectionHelper.converToMap(l, "busi_code"));
			result.put(key, dto);
		}
		return result;
	}

	/**
	 * 所有的配置模板
	 * @return
	 */
	private static Map<String, TTemplate> allConfigTemplate() throws Exception {
		Map<String, TTemplate> result =new HashMap<String, TTemplate> ();
		List<TConfigTemplate> allConfig = tTemplateDao.queryAllConfig();
		Map<String, List<TConfigTemplate>> allConfigMap =
			CollectionHelper.converToMap(allConfig, "county_id");
		Iterator<String> it = allConfigMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			List<TConfigTemplate> l =allConfigMap.get(key);
			TemplateConfigDto dto = new TemplateConfigDto();
			dto.setConfigList(l);
			dto.setConfigMap(CollectionHelper.converToMapSingle(l, "config_name"));
			result.put(key, dto);
		}
		return result;
	}



}
