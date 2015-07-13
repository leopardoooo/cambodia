package com.ycsoft.report.query.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dto.RepKeyDto;

/**
 * sql拆分组装工厂
 */
public class AnalyseSqlFactory_new implements Serializable{

	/**
	 * 
	 */
	private String sql_splits[] = null;
	private String sql_groups[] = null;
	//sql关键字拆分正则表达式
	private final static String sql_regex="[\\s\\)]+(or|and|where|group|order|union|between|when|then)[\\s\\(]*((not\\s)?\\s*exists)?[\\s\\(]+";
	//between and 语句组合匹配正则表达式
	private final static String sql_regex_between=".*[bB][eE][tT][wW][eE][eE][nN][\\s\\(]+.*";
	private final static String sql_regex_and=".*[aA][nN][dD][\\s\\(]+.*";
	
	public AnalyseSqlFactory_new(String sql) throws ReportException{
		init(sql);
	}
	
	public void init(String sql) throws ReportException{
		Pattern p = Pattern.compile(sql_regex,Pattern.CASE_INSENSITIVE);
		// 正则表达式拆分sql
		sql_splits = p.split(sql);
		// 定义拆分关键字的数组
		sql_groups = new String[sql_splits.length - 1];
		Matcher m = p.matcher(sql);
		int group_index = 0;
		StringBuilder buff=new StringBuilder();
		
		// 初始化拆分关键字
		for (; m.find(); group_index++) {
			if (group_index >= sql_splits.length - 1)
				throw new ReportException(
						"sql_regex_pattern_matcher_error_more.", sql);
			sql_groups[group_index] = m.group();
		}
		if (group_index != sql_splits.length - 1)
			throw new ReportException("sql_regex_pattern_matcher_error_less.",
					sql);
		if(group_index==0)
			return;
		//分析'('和')'的数量
		// 第一个和最后一个分组不需要分析
		for (int i = 1; i < sql_splits.length - 1; i++) {
			char[] split_chars = sql_splits[i].toCharArray();
			int right = 0;// (和)的匹配数量
			//int left = -1; // )的非匹配位置
			for (int j=0;j<split_chars.length;j++) {
				if (split_chars[j] == '(')
					right++;
				if (split_chars[j] == ')')
					right--;
				if(right<0)
					break;
			}
			if(right<0){
				int group_right=sql_groups[i-1].lastIndexOf('(');
				sql_splits[i]=buff.delete(0,buff.length()).append(sql_groups[i-1].substring(group_right))
				.append(sql_splits[i])
				.toString();
				sql_groups[i-1]=sql_groups[i-1].substring(0, group_right);
				i--;
			}else if(right>0){
				int group_left=sql_groups[i].indexOf(')')+1;
				sql_splits[i]=buff.delete(0,buff.length()).append(sql_splits[i]).append(sql_groups[i].substring(0,group_left)).toString();
				sql_groups[i]=sql_groups[i].substring(group_left);
				i--;
			}
		}
		// 分析拆分关键字，如果是between则按对应的and重组sql_group和sql_between
		List<String> sql_group_list = new ArrayList<String>(
				sql_splits.length + 1);
		List<String> sql_split_list = new ArrayList<String>(
				sql_splits.length + 1);

		for (int i = 0; i < sql_groups.length; i++) {
			if (sql_groups[i].length()>=8&&sql_groups[i].matches(sql_regex_between)) {
				if (i+2>=sql_splits.length||!sql_groups[i + 1].matches(sql_regex_and))
					throw new ReportException("between_and error.", sql);
				sql_split_list.add(buff.delete(0, buff.length()).append(sql_splits[i]).append(sql_groups[i])
						.append(sql_splits[i + 1]).append(sql_groups[i + 1])
						.append( sql_splits[i + 2]).toString());
				i = i + 2;
				if (i < sql_groups.length) {
					sql_group_list.add(sql_groups[i]);
				}
				if (i == sql_groups.length - 1) {
					sql_split_list.add(sql_splits[i + 1]);
				}

			} else {
				sql_group_list.add(sql_groups[i]);
				sql_split_list.add(sql_splits[i]);
				if (i == sql_groups.length - 1)
					sql_split_list.add(sql_splits[i + 1]);
			}
		}

		if (sql_split_list.size() != sql_group_list.size() + 1)
			throw new ReportException("sql_split_and_group_error.", sql);

		sql_groups = new String[sql_group_list.size()];
		sql_group_list.toArray(sql_groups);
		sql_splits = new String[sql_split_list.size()];
		sql_split_list.toArray(sql_splits);
		
	}
	/**
	 * 获得用页面传入值填充的sql
	 * @param keylist
	 * @return
	 * @throws ReportException 
	 */
	public String getAnaSql(List<RepKeyDto> keylist) throws ReportException {
		if(keylist==null)
			return getFormatSql();
		for (RepKeyDto fitcon : keylist) {
			for (int i = 0; i < sql_splits.length; i++) {
				if (sql_splits[i] != null
						&& sql_splits[i].indexOf(fitcon.getKey()) > -1) {
					if (fitcon.getValue() == null
							|| "".equals(fitcon.getValue()))
						sql_splits[i] = " 1=1 ";
					else if ("like".equals(fitcon.getType())) {
						String[] like_values = fitcon.getValue().split(",");
						String sql_like = "";
						for (int a = 0; a < like_values.length; a++) {
							sql_like = sql_like
									+ (a == 0 ? " ( " : " or ")
									+ sql_splits[i].replaceAll(fitcon
											.getKey(), "%"
											+ like_values[a].trim() + "%");
							if (a == like_values.length - 1)
								sql_like = sql_like + " ) ";
						}
						sql_splits[i] = sql_like;
					} else {
						sql_splits[i] = sql_splits[i].replaceAll(
								fitcon.getKey(), fitcon.getValue());
					}
				}
			}
		}
		return this.getSql(sql_splits, sql_groups);
	}

	private String getSql(String sql_splits[], String sql_groups[]) throws ReportException {
		if(sql_splits==null||sql_splits.length==0)
			throw new ReportException("sql is null");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sql_splits.length; i++) {
			sb.append(sql_splits[i]);
			if (i < sql_groups.length)
				sb.append(sql_groups[i]);
		}
		return sb.toString();
	}

	/**
	 * 获得原始sql格式后的sql
	 * @return
	 * @throws ReportException 
	 */
	public String getFormatSql() throws ReportException {
		return getSql(this.sql_splits, this.sql_groups);
	}

	/**
	 * 获得用测试值填充的sql
	 * @param keylist
	 * @return
	 * @throws ReportException 
	 */
	public String getTestSql() throws ReportException {
		List<RepKeyDto> list=new ArrayList<RepKeyDto>(SystemConfig.getConList().size());
		for(RepKeyCon fitcon : SystemConfig.getConList()){
			RepKeyDto vo=new RepKeyDto();
			vo.setType(fitcon.getType());
			vo.setValue(fitcon.getTestvalue());
			vo.setKey(fitcon.getKey());
			list.add(vo);
		}
		return this.getAnaSql(list);
	}
}
