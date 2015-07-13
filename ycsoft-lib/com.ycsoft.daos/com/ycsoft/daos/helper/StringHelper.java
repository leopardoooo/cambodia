package com.ycsoft.daos.helper;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 提供对字符串处理的类，扩展了apache StringUtils
 * </p>
 *
 */
public class StringHelper extends org.apache.commons.lang.StringUtils {
	private static Pattern p = Pattern.compile("\\{\\d+\\}");
	/**
	 * <p>
	 * 根据参数的类型,解析含有占位符的字符串，
	 * 字符类型按:'参数'的格式替换 数字类型不加任何字符直接替换
	 * </p>
	 *
	 * @param sql
	 *            需要格式化的字符串
	 * @param params
	 *            要替换的可变参数
	 * @return 格式化后的字符串
	 */
	public static String format(String source, Object... params) {
		return parses(source,"'",params);
	}
	/**
	 * <p>
	 *  忽略参数的类型，按照给定的startChar,endChar替换字符串
	 * </p>
	 *
	 * @param source
	 *            源字符串
	 * @param start
	 *            参数首位需要加的字符
	 * @param end
	 *            参数后面需要添加的字符
	 * @param params
	 *            根据占位符给定的参数
	 * @return
	 */
	public static String formatIgnoreType(String source, Object... params) {
		return parses(source,"",params);
	}

	/**
	 * <p> 功能：格式化带有'{0}'占位符的字符串 </p>
	 * @param source 源字符串
	 * @param split 添加在目标匹配的字符串开始和结尾的字符
	 * @param params 可变的参数，根据{0}的个数传入相应的参数值
	 * @return 格式化后的字符串
	 */
	private static String parses(String source,String split,Object... params){
		Matcher m = p.matcher(source);
		int i = 0;
		int endIndex = 0;
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			String p = "''";
			if (params[i] != null){
				if (params[i] instanceof String || params[i] instanceof Date
						|| params[i] instanceof Character)
					p =split+params[i++].toString() + split;
				else
					p =params[i++].toString();
				endIndex = m.end();
			}
			m.appendReplacement(buf,p);
		}
		if (endIndex > 0)buf.append(source.substring(endIndex)).toString();
		else return source;
		return buf.toString();
	}
}
