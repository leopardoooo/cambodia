/**
 *
 */
package com.ycsoft.commons.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ycsoft.commons.helper.LoggerHelper;

/**
 * @author YC-SOFT
 *
 */
public class SysConfig  {
	private static Properties config = null;

	private static Properties getConfig() {
		loadData();
		return config;
	}

	/**
	 * 获取配置信息
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return getConfig().getProperty(key);
	}

	protected static void loadData() {
		if (config == null)
			synchronized (SysConfig.class) {
				if (config == null) {
					InputStream in = null;
					config = new Properties();
					try {
						in = ClassLoader
								.getSystemResourceAsStream("config.properties");
						config.load(in);
					} catch (IOException e) {
						LoggerHelper.error(SysConfig.class, "配置装载异常");
					} finally {
						if (in != null)
							try {
								in.close();
							} catch (IOException e) {
							}
					}
				}
			}
	}

	/**
	 * 重新装载数据
	 */
	public static void reset(){
		config = null;
		loadData();
	}

}
