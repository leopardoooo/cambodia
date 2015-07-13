package com.ycsoft.login;

import java.util.Properties;

import com.ycsoft.commons.helper.DES;

public class LoadPropertie {
	final static String PROP_FILE_NAME = "servers.properties";
	private static LoadPropertie loadProp = null;
	private Properties prop = null;
	private final static String separator = System
			.getProperty("file.separator");

	public LoadPropertie(String filePath) {
		// String filePath = new java.io.File("").getAbsolutePath();
		// String filePath = System.getProperty(
		// "user.dir")+separator+"WebRoot";

		if (!filePath.endsWith(separator)) {
			filePath += separator;
		}
		filePath += "WEB-INF" + separator + "classes" + separator
				+ PROP_FILE_NAME;
		try {
			java.io.InputStream is = new java.io.FileInputStream(filePath);
			prop = new Properties();
			prop.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LoadPropertie getInstance(String webPath) throws Exception {
		if (loadProp == null) {
			synchronized (LoadPropertie.class) {
				if (loadProp == null) {
					loadProp = new LoadPropertie(webPath);
				}
			}
		}
		return loadProp;
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public static LoadPropertie getInstance() throws Exception {
		return getInstance("");
	}
	
	/**
	 * 
	 * @param key			属性
	 * @param type			加密或解密，0 加密，1 解密
	 * @param passwordKey	密匙key
	 * @return
	 */
	public String getProperty(String key, String type, String passwordKey) {
		String value = getProperty(key);
		DES des = new DES(passwordKey);
		if(type.equals("1")){
			try {
				value = des.getDesString(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(type.equals("0")){
			value = des.getEncString(value);
		}
		return value;
	}

}
