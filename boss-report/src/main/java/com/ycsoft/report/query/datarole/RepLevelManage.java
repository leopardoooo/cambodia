package com.ycsoft.report.query.datarole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepKeyLevel;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.dao.keycon.RepKeyLevelDao;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.menory.MemoryCacheInit;

/**
 * 报表数据权限定义管理器
 */
public class RepLevelManage implements MemoryCacheInit {
	
	private static Map<String,RepKeyLevel> repkeylevelMap=new HashMap<String,RepKeyLevel>();
	
	/**
	 * 报表字段权限取值
	 * Map(rep_key_level.key,Map<rep_key_level.s_data_right_type,Map<rule_id,rule取值>>>
	 */
	private static Map<String,Map<String,List<QueryKeyValue>>> sdatarightValueMap=new HashMap<String,Map<String,List<QueryKeyValue>>>();
	
	public RepKeyLevel getKeyLevel(String key){
		return repkeylevelMap.get(key);
	}
	
	public List<QueryKeyValue> getDatarightValues(String datatype,String rule_id){
		return sdatarightValueMap.containsKey(datatype)?sdatarightValueMap.get(datatype).get(rule_id):null;
	}
	
	public String initAll() throws ReportException {
		try {
			List<RepKeyLevel> list=repKeyLevelDao.findAll();
			Map<String,RepKeyLevel> temp_repkeylevelMap=new HashMap<String,RepKeyLevel>();
			Map<String,Map<String,List<QueryKeyValue>>> temp_sdatarightValueMap=new HashMap<String,Map<String,List<QueryKeyValue>>>();
			
			for(RepKeyLevel o:list){
				temp_repkeylevelMap.put(o.getKey(), o);
				if(StringHelper.isNotEmpty(o.getS_data_right_type())){
					List<QueryKeyValue> rulesqllist=repKeyLevelDao.queryRuleSql(o.getS_data_right_type());
					if(rulesqllist==null||rulesqllist.size()==0)
						throw new ReportException(o.getS_data_right_type()+":s_data_right_type is undefined.");
					Map<String,List<QueryKeyValue>> ruleValueMap=new HashMap<String,List<QueryKeyValue>>();
					for(QueryKeyValue vo:rulesqllist){
						ruleValueMap.put(vo.getId(), queryKeyValueDao.findList(ReportConstants.DATABASE_SYSTEM, vo.getName()));
					
					}
					temp_sdatarightValueMap.put(o.getS_data_right_type(), ruleValueMap);
				}
			}
			repkeylevelMap=temp_repkeylevelMap;
			sdatarightValueMap=temp_sdatarightValueMap;
			return "";
		} catch (ReportException e) {
			throw e;
		} catch (JDBCException e) {
			throw new ReportException("初始化rep_key_level错误",e);
		}
		
	}
	private RepKeyLevelDao repKeyLevelDao;
	private QueryKeyValueDao queryKeyValueDao;

	public QueryKeyValueDao getQueryKeyValueDao() {
		return queryKeyValueDao;
	}
	public void setQueryKeyValueDao(QueryKeyValueDao queryKeyValueDao) {
		this.queryKeyValueDao = queryKeyValueDao;
	}
	public RepKeyLevelDao getRepKeyLevelDao() {
		return repKeyLevelDao;
	}
	public void setRepKeyLevelDao(RepKeyLevelDao repKeyLevelDao) {
		this.repKeyLevelDao = repKeyLevelDao;
	}
}
