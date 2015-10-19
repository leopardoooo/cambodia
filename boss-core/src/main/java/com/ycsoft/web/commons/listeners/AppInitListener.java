package com.ycsoft.web.commons.listeners;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycsoft.beans.config.TBusiConfirm;
import com.ycsoft.business.cache.PrintContentConfiguration;
import com.ycsoft.business.dao.config.TBusiConfirmDao;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.system.SDataTranslationDao;
import com.ycsoft.business.dao.system.SItemvalueDao;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.MemoryPrintData;
import com.ycsoft.commons.store.TemplateConfig;

/**
 * 容器启动的监听器,完成系统初始化工作。 包括配置文件、系统参数等初始化。
 *
 * @author hh
 * @date Mar 12, 2010 9:23:35 AM
 */
public class AppInitListener implements javax.servlet.ServletContextListener {


	/**
	 * 构造监听器
	 */
	public AppInitListener(){}

	private static WebApplicationContext wc = null;

	/**
	 * 实现容器初始化的函数
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		try{
			configuration(sc);
			initComponent(sc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}

	/**
	 * 配置文件参数
	 */
	private void configuration(ServletContext sc)throws Exception{
		//打印模板的解析
		PrintContentConfiguration.configure(sc.getRealPath("/"));
	}

	/**
	 * 初始化查询组建，从Spring 管理的Bean中获取
	 * @param sc
	 * @throws Exception
	 */
	private void initComponent(ServletContext sc) throws Exception{
		wc = WebApplicationContextUtils.getWebApplicationContext(sc);
		SItemvalueDao sItemvalueDao = wc.getBean(SItemvalueDao.class);
		SDataTranslationDao sDataTranslationDao = wc.getBean(SDataTranslationDao.class);
		TTemplateDao tTemplateDao =wc.getBean(TTemplateDao.class);
		CFeeDao cFeeDao = wc.getBean(CFeeDao.class);
		if (sItemvalueDao == null || tTemplateDao == null || cFeeDao == null)
			throw new Exception("初始化错误，DAO未注入");
		
		TemplateConfig.loadData(tTemplateDao);
		
		MemoryDict.setupData(sItemvalueDao.findAllViewDict(), sDataTranslationDao.findAll());
		//MemoryPrintData.loadData(cFeeDao.queryUnPrintFee());
		
		TBusiConfirmDao tBusiConfirmDao =wc.getBean(TBusiConfirmDao.class);
		List<TBusiConfirm> all = tBusiConfirmDao.findAll();
		Map<String, List<TBusiConfirm>> mappedByBusiCode = CollectionHelper.converToMap(all, "county_id");
		for(String busiCode:mappedByBusiCode.keySet()){
			PrintContentConfiguration.addBusiConfirmPrintCfg(busiCode, CollectionHelper.converToMapSingle(mappedByBusiCode.get(busiCode), "busi_code"));
		}
	}

	/**
	 * 实现容器销毁函数
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("容器正在销毁....");
	}

	/**
	 * @return the wc
	 */
	public static WebApplicationContext getWc() {
		return wc;
	}
}
