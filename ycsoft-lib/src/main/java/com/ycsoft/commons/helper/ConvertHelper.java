package com.ycsoft.commons.helper;


/**
 * 提供一系列的转换机制功能。
 *
 * @author hh
 * @date Dec 3, 2009 1:10:48 PM
 */
public class ConvertHelper {


	private ConvertHelper(){
	}

	/**
	 * 将数字型人民币转换成大写的中文字符描述
	 * @param input 输入字符串
	 * @return
	 */
	public static String numToChinese(String input) {
		String s1 = "零壹贰叁肆伍陆柒捌玖";
		String s4 = "分角整元拾佰仟万拾佰仟亿拾佰仟";
		String temp = "";
		String result = "";
		try {
			temp = input.trim();
			Float.parseFloat(temp);
		} catch (Exception e) {
			return "输入字串不是数字串只能包括以下字符（´0´～´9´，´.´)，输入字符串最大只能精确到仟亿，小数点只能两位！";
		}
		int len = 0;
		if (temp.indexOf(".") == -1)
			len = temp.length();
		else
			len = temp.indexOf(".");
		if (len > s4.length() - 3)
			return "输入字串最大只能精确到仟亿，小数点只能两位！";
		int n1 = 0;
		String num = "";
		String unit = "";

		for (int i = 0; i < input.length(); i++) {
			if (i > len + 2) {
				break;
			}
			if (i == len) {
				continue;
			}
			n1 = Integer.parseInt(String.valueOf(temp.charAt(i)));
			num = s1.substring(n1, n1 + 1);
			n1 = len - i + 2;
			unit = s4.substring(n1, n1 + 1);
			result = result.concat(num).concat(unit);
		}
		if (len == temp.length() || len == temp.length() - 1)
			result = result.concat("整");
		if (len == temp.length() - 2)
			result = result.concat("零分");
		return result;
	}
}
