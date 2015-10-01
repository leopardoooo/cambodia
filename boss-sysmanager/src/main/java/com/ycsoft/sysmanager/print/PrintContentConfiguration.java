package com.ycsoft.sysmanager.print;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ycsoft.beans.config.TBusiConfirm;
import com.ycsoft.commons.helper.FileHelper;

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

}
