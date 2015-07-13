package com.ycsoft.report.query;

import org.springframework.context.ApplicationContext;

import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.commons.SystemConfig;
/**
 * 重载报表系统缓存
 */
public class ReloadSystemMemory extends Thread {

	private ApplicationContext ac=null;
	public ReloadSystemMemory(ApplicationContext ac){
		this.ac=ac;
	}
	/**
	 * 每隔半个小时更新内存
	 */
	public void run(){
		try {
			Thread.sleep(30* 60 * 1000);
			SystemConfig.initMemory(ac);
		} catch (Exception e) {
			LoggerHelper.error(this.getClass(),e.getMessage(),e);
		}
	}
}
