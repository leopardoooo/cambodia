package com.yaochen.boss.commons;

import org.slf4j.LoggerFactory;

/**
 * 日志输出工具类
 * @author Tom
 */
public final class LoggerUtil {

	public static void PrintInfo(Class clazz, String title, String msg){
		LoggerFactory.getLogger(clazz).info(title + ", " + msg);
	}
	
}
