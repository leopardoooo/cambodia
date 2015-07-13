package com.ycsoft.report.query.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.bean.RepTemplateKey;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 列模板转换
 */
public class AnalyseTemplateKey {

	private static final String key_replace="@";
	
	private static final String key_id="#id#";
	private static final String key_name="#name#";
	private static final String key_pid="#pid#";
	
	/**
	 * translate_sql类型的转换键
	 * contect 例子：      sum(case when prod_id=#id# then 1 else 0 end) '#name#'
	 * contect_cycle 例子：select prod_id,prod_name from busi.p_prod
	 * @param list
	 * @param qdao
	 * @return
	 */
	public static Map<String,String> initKeyMap(List<RepTemplateKey> list,QueryKeyValueDao qdao,StringBuilder error_info){
		
		Map<String,String> tmap=new HashMap<String,String>();
		if(list==null||list.size()==0)
			return tmap;
		
		for(RepTemplateKey k:list){
			if(ReportConstants.template_type_fixed_sql.equals(k.getTemplate_type())){
				tmap.put(k.getTemplate_key(), k.getContect());
			}else if (ReportConstants.template_type_translate_sql.equals(k.getTemplate_type())){
				try{
					List<QueryKeyValue> contectlist=qdao.findList(k.getDatabase(), k.getContect_cycle());
					if(contectlist!=null&&contectlist.size()>0){
						StringBuilder sb=new StringBuilder();
						String contect=k.getContect();
						//给id加',用于数据转换
						contect=contect.replaceAll(key_id, "'"+key_id+"'");
						for(QueryKeyValue vo: contectlist){
							String temp=contect;
							temp=temp.replaceAll(key_id, vo.getId());
							temp=temp.replaceAll(key_name, vo.getName());
							sb.append(" ").append(temp).append(",");
						}
						contect=sb.toString();
						tmap.put(k.getTemplate_key(), contect.substring(0, contect.length()-1));
					}
				}catch(Exception e){
					LoggerHelper.info(AnalyseTemplateKey.class, k.getTemplate_key()+" init error:"+e.getMessage());
					error_info.append("rep_template_key_init_error:").append(k.getTemplate_key()).append(e.getMessage()).append(";");
				}
			}else {
				LoggerHelper.info(AnalyseTemplateKey.class, k.getTemplate_key()+" type is undefine.");
				error_info.append("rep_template_key_init_error:").append(k.getTemplate_key()).append(" type is undefine;");
			}
		}
		return tmap;
	}
	
	public static void main(String args[]){
		System.out.println("@aa@".replaceAll("@", ""));
		System.out.println("@aa@,".substring(0,4));
		
	}
}
