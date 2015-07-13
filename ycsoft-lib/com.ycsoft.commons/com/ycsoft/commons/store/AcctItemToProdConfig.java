package com.ycsoft.commons.store;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TAcctitemToProd;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TTemplate;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.business.dao.config.TAcctitemToProdDao;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.config.TemplateUpdatePorpDto;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.DaoException;
import com.ycsoft.commons.helper.CollectionHelper;

/**
 * 产品可使用的账目配置
 *
 */
public class AcctItemToProdConfig {
	private static TAcctitemToProdDao tAcctitemToProdDao;
	
	private static Map<String,List<TAcctitemToProd>> configMap = new HashMap<String,List<TAcctitemToProd>>();

	
	public static List<TAcctitemToProd> loadConfig(String prod_id) throws ComponentException{
		List<TAcctitemToProd> list=configMap.get(prod_id);
		if(list==null){
			throw new ComponentException("t_acctitem_to_prod产品("+prod_id+")找不到可使用的账目配置");
		}
		return list;
	}

	/**
	 * 设置数据加载Dao
	 * @param dao
	 */
	public static void loadData(TAcctitemToProdDao dao) throws Exception {
		tAcctitemToProdDao = dao;
		loadData();
	}

	public static void loadData() throws Exception{
		if (tAcctitemToProdDao!=null){
			configMap=CollectionHelper.converToMap(tAcctitemToProdDao.findAll(), "prod_id");
		}
	}
}
