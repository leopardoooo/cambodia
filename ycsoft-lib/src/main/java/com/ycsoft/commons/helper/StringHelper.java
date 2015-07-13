package com.ycsoft.commons.helper;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <code>java.lang.String</code>辅助类, 对字符串的一系列函数。
 *
 * @author hh
 * @date Dec 3, 2009 1:40:01 PM
 */
public class StringHelper {

	/**
	 * 判断字符串是否为空
	 * @return boolean 空返回true 不为空返回lase
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断字符不为NULL，""，null值
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}


	/**
	 * 从开始位置，删除指定个数的字符串
	 * @return
	 */
	public static String delStartChar(String src ,int len){
		if(null != src && src.length() > 0){
			src = src.substring( len );
		}
		return src;
	}
	/**
	 * 从结束位置，删除指定个数的字符串
	 * @param src 源字符串
	 * @param len 要删除的长度
	 * @return
	 */
	public static String delEndChar(String src , int len){
		if(null != src && src.length() > 0){
			src = src.substring( 0 , src.length() - len );
		}
		return src;
	}

	/**
	 * 将给定的params按顺序拼接起来
	 * @param params 需要拼接的参数
	 * @return
	 */
	public static String append(Serializable ... params){
		StringBuilder sb = new StringBuilder(100);
		for (Serializable s : params) {
			sb.append( s );
		}
		return sb.toString();
	}
	
	/**
	 * 如果是null或者空字符串,返回默认值，否则返回字符串本身.
	 * @param source	原字符串.
	 * @param defaultValue	默认值.
	 * @return
	 */
	public static String makeSureNotEmpty(String source ,String defaultValue){
		return isNotEmpty(source) ? source : defaultValue;
	}
	
	/**
	 * 如果是null或者空字符串,返回""，否则返回字符串本身.
	 * @param source
	 * @return
	 */
	public static String makeSureNotEmpty(String source){
		return isNotEmpty(source) ? source : "";
	}
	
	/**
	 * 清空左右两边空格
	 * @param cs
	 * @return
	 */
	public static String lrTrim(CharSequence cs) {
        if (null == cs)
            return null;
        if (cs instanceof String)
            return ((String) cs).trim();
        int length = cs.length();
        if (length == 0)
            return cs.toString();
        int l = 0;
        int last = length - 1;
        int r = last;
        for (; l < length; l++) {
            if (!Character.isWhitespace(cs.charAt(l)))
                break;
        }
        for (; r > l; r--) {
            if (!Character.isWhitespace(cs.charAt(r)))
                break;
        }
        if (l > r)
            return "";
        else if (l == 0 && r == last)
            return cs.toString();
        return cs.subSequence(l, r + 1).toString();
    }
	
	/**
	 * 字符串左边补0
	 * @param str 字符串
	 * @param num 补0后长度
	 * @return
	 */
	public static String leftWithZero(String str,int num){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<num-str.length();i++){
			sb.append("0");
		}
		return sb.append(str).toString();
	}
	
	/**
	 * 是否數字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 用分隔符将字符串数组连接成字符串
	 * 
	 * @param args
	 *            字符串数组
	 * @param sep
	 *            分隔符
	 * @return
	 */
	public static final String join(String[] args, String sep) {
		StringBuilder buf = new StringBuilder(256);
		int j = args.length - 1;
		for (int i = 0; i < j; i++) {
			buf.append(args[i]).append(sep);
		}
		buf.append(args[j]);
		return buf.toString();
	}
	
    /**
     * 清除特殊字符
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public   static   String StringFilter(String   str)   throws   PatternSyntaxException   {      
        // 只允许字母和数字        
        // String   regEx  =  "[^a-zA-Z0-9]";                      
        // 清除掉所有特殊字符   
	  String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";   
	  Pattern   p   =   Pattern.compile(regEx);      
	  Matcher   m   =   p.matcher(str);      
	  return   m.replaceAll("").trim();      
	}
    
    /**
     * 比较两个字符串是否都为空或者equals
     * @param str1
     * @param str2
     */
	public static boolean bothEmptyOrEquals(String str1, String str2) {
		if(isEmpty(str1) && isEmpty(str2)){
			return true;
		}
		return (isEmpty(str1)?"":str1).equals(isEmpty(str2)?"":str2);
	} 
	
}
