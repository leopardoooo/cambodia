package com.ycsoft.business.cache;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ycsoft.beans.config.TBusiConfirm;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.StringHelper;

/**
 * 打印内容的配置文件解析
 *
 * @author hh
 */
public class PrintContentConfiguration {

	/**
	 * 初始化打印内容配置的路径
	 */
	private static final String PRINT_XML_PATH = "WEB-INF"+File.separator+"print";

	/**
	 * 存储所有的模板，以文件名作为KEY
	 */
	private static Map<String, String> allPrintTemplate = new HashMap<String, String>();
	
	/**
	 * 业务确认单打印配置,结构为
	 * {
	 * 	county_id:{
	 * 		busi_code:{}
	 * 	}
	 * }
	 */
	private static Map<String, Map<String, TBusiConfirm>> busiConfirmPrintCfg = new HashMap<String, Map<String, TBusiConfirm>>();

	private PrintContentConfiguration() {}
 
	/**
	 * 开始解析所有的文件,便装载到内存
	 */
	public static void configure(String root) throws IOException {
		File dir = new File(root + PRINT_XML_PATH);
		File [] files = dir.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".xml");
			}
		});
		//读取文件
		String key = "" ,value = "";
		for (File f : files) {
			key = f.getName();
			//将XML文件的注意和head去掉。
			value = FileHelper.readFile(f)
							.replaceAll("<!--[\\s\\S]*-->", "");
			allPrintTemplate.put( key , value);
		}
	}

	/**
	 * 返回
	 * @param fileName 文件名包括后缀
	 * @return
	 */
	public static String getTemplate(String fileName){
		if( null == fileName){
			return fileName;
		}
		return allPrintTemplate.get(fileName);
	}

	/**
	 *  添加一个打印模板
	 * @param key 文件名
	 * @param content 文件内容
	 */
	public static void put(String key , String content){
		allPrintTemplate.put(key, content);
	}
	
	/**
	 * 取指定分公司指定业务的业务模版配置.
	 * @param busiCode
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public static TBusiConfirm getBusiConfirm(String busiCode,String countyId) throws Exception{
		if(StringHelper.isEmpty(busiCode) || StringHelper.isEmpty(countyId)){
			throw new IllegalArgumentException("取业务确认单配置时两个参数都不能为空");
		}
		Map<String, TBusiConfirm> map = busiConfirmPrintCfg.get(countyId);
		if(null == map || map.isEmpty()){
			return null;
		}
		return map.get(busiCode);
	}

	public static Map<String, Map<String, TBusiConfirm>> getBusiConfirmPrintCfg() {
		return busiConfirmPrintCfg;
	}

	public static void setBusiConfirmPrintCfg(
			Map<String, Map<String, TBusiConfirm>> busiConfirmPrintCfg) {
		PrintContentConfiguration.busiConfirmPrintCfg = busiConfirmPrintCfg;
	}
	
	public static void addBusiConfirmPrintCfg(String countyId,Map<String, TBusiConfirm> map) {
		busiConfirmPrintCfg.put(countyId, map);
	}
}
