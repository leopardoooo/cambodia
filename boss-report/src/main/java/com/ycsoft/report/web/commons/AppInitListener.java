package com.ycsoft.report.web.commons;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.db.ConnContainer;

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
	
	
	/**
	 * 实现容器初始化的函数
	 */
	public void contextInitialized(ServletContextEvent sce) {
		
		this.initComponent(sce.getServletContext());
		configuration(sce.getServletContext());
		LoggerHelper.info(this.getClass(),"Report容器初始化...");
	}
	
	/**
	 * 配置文件参数
	 */
	private void configuration(ServletContext sc){
		 
	}

	/**
	 * 初始化查询组建，从Spring 管理的Bean中获取
	 * @param sc
	 * @throws Exception
	 */
	private void initComponent(ServletContext sc){
		try {
			SystemConfig.init(sc,WebApplicationContextUtils.getWebApplicationContext(sc));
					
		} catch (Exception e) {
			LoggerHelper.error(AppInitListener.class, "report_init_error ", e);
		} 		
	}
	
	/**
	 * 实现容器销毁函数
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		LoggerHelper.info(this.getClass(),"Report容器正在销毁....");
		ConnContainer.clossContainer();
	}
}
