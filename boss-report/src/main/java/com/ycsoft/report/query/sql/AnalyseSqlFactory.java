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
import com.ycsoft.report.query.key.Impl.ConKeyValue;

/**
 * sql拆分组装工厂
 */
public class AnalyseSqlFactory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6513684046350324826L;
	private String sql_splits[] = null;
	private String sql_groups[] = null;
	private final static String sql_regex = "[\\s\\)]+([oO][rR]|[aA][nN][dD]|[wW][hH][eE][rR][eE])\\s+([nN][oO][tT]\\s)?\\s*[eE][xX][iI][sS][tT][sS][\\s\\(]+|[\\s\\)]+([oO][rR]|[aA][nN][dD]|[wW][hH][eE][rR][eE]|[gG][rR][oO][uU][pP]|[oO][rR][dD][eE][rR]|[uU][nN][iI][oO][nN]|[bB][eE][tT][wW][eE][eE][nN])[\\s\\(]+";
	private final static String sql_regex_where = "\\)[^,]*,[\\s\\(]*";

	public AnalyseSqlFactory(String sql) throws ReportException{
		init(sql);
	}
	
	public void init2(String sql) throws ReportException{
		Pattern p = Pattern.compile(sql_regex);
		// 正则表达式拆分sql
		sql_splits = p.split(sql);
		// 定义拆分关键字的数组
		sql_groups = new String[sql_splits.length - 1];
		Matcher m = p.matcher(sql);
		int between_index = 0;
		// 初始化拆分关键字
		for (; m.find(); between_index++) {
			if (between_index >= sql_splits.length - 1)
				throw new ReportException(
						"sql_regex_pattern_matcher_error_more.", sql);
			sql_groups[between_index] = m.group();
		}
		if (between_index != sql_splits.length - 1)
			throw new ReportException("sql_regex_pattern_matcher_error_less.",
					sql);
		//分析'('和')'的数量
		// 第一个和最后一个分组不需要分析
		for (int i = 1; i < sql_splits.length - 1; i++) {
			char[] group_char = sql_splits[i].toCharArray();
			int right = 0;// (的数量
			int left = 0; // )的数量
			for (char c : group_char) {
				if (c == '(')
					right++;
				if (c == ')')
					left++;
			}
			// 检测')'数量
			for (int a = 0; a < right - left; a++) {
				int kuohao = sql_groups[i].indexOf(')');
				if (kuohao > -1) {
					sql_splits[i] = sql_splits[i] + sql_groups[i].substring(0,kuohao+1);
					sql_groups[i] = sql_groups[i].substring(kuohao + 1);
					//sql_splits[i] = sql_splits[i] + ")";
				}
			}
			// 检测'('数量
			for (int a = 0; a < left - right; a++) {
				int kuohao = sql_groups[i - 1].lastIndexOf('(');
				if (kuohao > -1) {
					sql_splits[i] = sql_groups[i - 1].substring(kuohao) + sql_splits[i];
					sql_groups[i - 1] = sql_groups[i - 1].substring(0, kuohao);
					
				}
			}
		}
		// 分析拆分关键字，如果是between则按对应的and重组sql_group和sql_between
		List<String> sql_group_list = new ArrayList<String>(
				sql_splits.length + 1);
		List<String> sql_split_list = new ArrayList<String>(
				sql_splits.length + 1);

		for (int i = 0; i < sql_groups.length; i++) {
			String sql_group = sql_groups[i].toUpperCase();
			if (sql_group.indexOf("BETWEEN") > -1) {
				sql_group = sql_groups[i + 1].toUpperCase();
				if (sql_group.indexOf("AND") < 0)
					throw new ReportException("between_and error.", sql);
				sql_split_list.add(sql_splits[i] + sql_groups[i]
						+ sql_splits[i + 1] + sql_groups[i + 1]
						+ sql_splits[i + 2]);
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
	
	public void init(String sql) throws ReportException {
		Pattern p = Pattern.compile(sql_regex);
		// 正则表达式拆分sql
		sql_splits = p.split(sql);
		// 定义拆分关键字的数组
		sql_groups = new String[sql_splits.length - 1];
		Matcher m = p.matcher(sql);
		int between_index = 0;
		// 初始化拆分关键字
		for (; m.find(); between_index++) {
			if (between_index >= sql_splits.length - 1)
				throw new ReportException(
						"sql_regex_pattern_matcher_error_more.", sql);
			sql_groups[between_index] = m.group();
		}
		if (between_index != sql_splits.length - 1)
			throw new ReportException("sql_regex_pattern_matcher_error_less.",
					sql);
		// 分析where 前对应语句 用 ,号拆分
		// 处理子查询可能会出现的问题
		List<String> group_list = new ArrayList<String>(sql_splits.length + 1);
		List<String> split_list = new ArrayList<String>(sql_splits.length + 1);
		p = Pattern.compile(sql_regex_where);
		for (int i = 0; i < sql_groups.length; i++) {
			if (sql_groups[i].toUpperCase().indexOf("WHERE") > -1) {
				String sql_where_splits[] = p.split(sql_splits[i]);
				for (String sql_where_split : sql_where_splits)
					split_list.add(sql_where_split);
				m = p.matcher(sql_splits[i]);
				while (m.find())
					group_list.add(m.group());
				group_list.add(sql_groups[i]);
			} else {
				split_list.add(sql_splits[i]);
				group_list.add(sql_groups[i]);
			}
			if (i == sql_groups.length - 1)
				split_list.add(sql_splits[i + 1]);

		}
		//无查询条件不进行特殊化处理
		if(split_list.size()==0&&group_list.size()==0){
			return;
		}

		if (split_list.size() != group_list.size() + 1)
			throw new ReportException("sql_regex_pattern_matcher_error_less.",
					sql);
		sql_splits = new String[split_list.size()];
		sql_groups = new String[group_list.size()];
		split_list.toArray(sql_splits);
		group_list.toArray(sql_groups);

		// 分析拆分的字段租中的(和)的数量，确定拆分关键字和字段组是否需要重组
		// 第一个和最后一个分组不需要分析
		for (int i = 1; i < sql_splits.length - 1; i++) {
			char[] group_char = sql_splits[i].toCharArray();
			int right = 0;// (的数量
			int left = 0; // )的数量
			for (char c : group_char) {
				if (c == '(')
					right++;
				if (c == ')')
					left++;
			}
			// 检测')'数量
			for (int a = 0; a < right - left; a++) {
				int kuohao = sql_groups[i].indexOf(')');
				if (kuohao > -1) {
					sql_splits[i] = sql_splits[i] + sql_groups[i].substring(0,kuohao+1);
					sql_groups[i] = sql_groups[i].substring(kuohao + 1);
					//sql_splits[i] = sql_splits[i] + ")";
				}
			}
			// 检测'('数量
			for (int a = 0; a < left - right; a++) {
				int kuohao = sql_groups[i - 1].lastIndexOf('(');
				if (kuohao > -1) {
					sql_splits[i] = sql_groups[i - 1].substring(kuohao) + sql_splits[i];
					sql_groups[i - 1] = sql_groups[i - 1].substring(0, kuohao);
					
				}
			}
		}
		// 分析拆分关键字，如果是between则按对应的and重组sql_group和sql_between
		List<String> sql_group_list = new ArrayList<String>(
				sql_splits.length + 1);
		List<String> sql_split_list = new ArrayList<String>(
				sql_splits.length + 1);

		for (int i = 0; i < sql_groups.length; i++) {
			String sql_group = sql_groups[i].toUpperCase();
			if (sql_group.indexOf("BETWEEN") > -1) {
				sql_group = sql_groups[i + 1].toUpperCase();
				if (sql_group.indexOf("AND") < 0)
					throw new ReportException("between_and error.", sql);
				sql_split_list.add(sql_splits[i] + sql_groups[i]
						+ sql_splits[i + 1] + sql_groups[i + 1]
						+ sql_splits[i + 2]);
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
	public String getAnaSql(List<ConKeyValue> keylist) throws ReportException {
		if(keylist==null)
			return getFormatSql();
		String sql_splits_temp[] = new String[this.sql_splits.length];
		System.arraycopy(this.sql_splits, 0, sql_splits_temp, 0,
				this.sql_splits.length);
		for (ConKeyValue fitcon : keylist) {
			for (int i = 0; i < sql_splits_temp.length; i++) {
				if (sql_splits_temp[i] != null
						&& sql_splits_temp[i].indexOf(fitcon.getKey()) > -1) {
					if (fitcon.getValue() == null
							|| "".equals(fitcon.getValue()))
						sql_splits_temp[i] = " 1=1 ";
					else if("fileupload".equals(SystemConfig.getConMap().get(fitcon.getKey()).getHtmlcode())){
						//文件组件上传数据格式
						RepKeyCon keycon=SystemConfig.getConMap().get(fitcon.getKey());
						sql_splits_temp[i] = sql_splits_temp[i].replaceAll(
								fitcon.getKey(), keycon.getValuesql().replaceAll(keycon.getKey(), fitcon.getValue()));
					}else if ("like".equals(SystemConfig.getConMap().get(fitcon.getKey()).getType())) {
						String[] like_values = fitcon.getValue().split(",");
						String sql_like = "";
						for (int a = 0; a < like_values.length; a++) {
							sql_like = sql_like
									+ (a == 0 ? " ( " : " or ")
									+ sql_splits_temp[i].replaceAll(fitcon
											.getKey(), "%"
											+ like_values[a].trim() + "%");
							if (a == like_values.length - 1)
								sql_like = sql_like + " ) ";
						}
						sql_splits_temp[i] = sql_like;
					} else {
						sql_splits_temp[i] = sql_splits_temp[i].replaceAll(
								fitcon.getKey(), fitcon.getValue());
					}
				}
			}
		}
		return this.getSql(sql_splits_temp, sql_groups);
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
		
		String sql_splits_temp[] = new String[this.sql_splits.length];
		System.arraycopy(this.sql_splits, 0, sql_splits_temp, 0,
				this.sql_splits.length);
		for (RepKeyCon fitcon : SystemConfig.getConList()) {
			for (int i = 0; i < sql_splits_temp.length; i++) {
				if (sql_splits_temp[i] != null
						&& sql_splits_temp[i].indexOf(fitcon.getKey()) > -1) {
					if (fitcon.getTestvalue() == null
							|| "".equals(fitcon.getTestvalue()))
						sql_splits_temp[i] = " 1=1 ";
					else if("fileupload".equals(fitcon.getHtmlcode())){
						//文件组件上传数据格式
						sql_splits_temp[i] = sql_splits_temp[i].replaceAll(
								fitcon.getKey(), fitcon.getValuesql().replaceAll(fitcon.getKey(), fitcon.getTestvalue()));
					}else if ("like".equals(fitcon.getType())) {
						String[] like_values = fitcon.getTestvalue().split(",");
						String sql_like = "";
						for (int a = 0; a < like_values.length; a++) {
							sql_like = sql_like
									+ (a == 0 ? " ( " : " or ")
									+ sql_splits_temp[i].replaceAll(fitcon
											.getKey(), "%"
											+ like_values[a].trim() + "%");
							if (a == like_values.length - 1)
								sql_like = sql_like + " ) ";
						}
						sql_splits_temp[i] = sql_like;
					} else {
						sql_splits_temp[i] = sql_splits_temp[i].replaceAll(
								fitcon.getKey(), fitcon.getTestvalue());
					}
				}
			}
		}
		return this.getSql(sql_splits_temp, sql_groups);
	}
	
	/**
	 * cube明细查询组装sql
	 * @param keylist
	 * @return
	 * @throws ReportException
	 */
	public String getCubeDetail(List<ConKeyValue> keylist) throws ReportException{
		if(keylist==null)
			return getFormatSql();
		String sql_splits_temp[] = new String[this.sql_splits.length];
		System.arraycopy(this.sql_splits, 0, sql_splits_temp, 0,
				this.sql_splits.length);
		for (ConKeyValue fitcon : keylist) {
			for (int i = 0; i < sql_splits_temp.length; i++) {
				if (sql_splits_temp[i] != null
						&& sql_splits_temp[i].indexOf(fitcon.getKey()) > -1) {
					if (fitcon.getValue() == null
							|| "".equals(fitcon.getValue()))
						sql_splits_temp[i] = " 1=1 ";
					 else {
						sql_splits_temp[i] = sql_splits_temp[i].replaceAll(
								fitcon.getKey(), fitcon.getValue());
					}
				}
			
			}
		}
		return this.getSql(sql_splits_temp, sql_groups);
	}
}
