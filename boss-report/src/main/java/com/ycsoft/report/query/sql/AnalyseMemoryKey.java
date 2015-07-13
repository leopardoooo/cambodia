package com.ycsoft.report.query.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.report.bean.RepMemoryKey;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
/**
 * 处理内存中sql别值转换
 */
public class AnalyseMemoryKey {
	
	private static final String left_key="\"#M_";
	private static final String right_key="_M#\"";
		
	/**
	 * 转换内存关键字为更复杂的关键字
	 * @param sql
	 * @return
	 */
	public static String translateColumnToKey(String sql){
		for(RepMemoryKey  mk:SystemConfig.getMemoryKeyListt()){
			sql=sql.replaceAll(mk.getMemory_key(), left_key+mk.getMemory_key().substring(1, mk.getMemory_key().length()-1)+right_key);
		}
		return sql;
	}
	
	/**
	 * 转换内存关键字为描述
	 * @param sql
	 * @return
	 */
	public static String translateColumnToDesc(String sql){
		for(RepMemoryKey  mk:SystemConfig.getMemoryKeyListt()){
			sql=sql.replaceAll(mk.getMemory_key(), mk.getMemory_desc());
		}
		return sql;
	}
	/**
	 * 通过转换后的复杂关键字获获取列描述
	 * @param columntokey
	 * @return
	 */
	public static String getMemoryKeyDesc(String columntokey){
		String key=columntokey.replaceAll(left_key.substring(1), "\"").replaceAll(right_key.substring(0, right_key.length()-1), "\"");
		RepMemoryKey  mk=SystemConfig.getMemoryKeyMap().get(key);
		if(mk!=null)
			return mk.getMemory_desc().trim();
		else 
			return columntokey;
	}
	/**
	 * 通过转换后的复杂关键字获取值MAP
	 * @param columntokey
	 * @return
	 */
	public static Map<String,String> getMemoryMap(String columntokey){
		String key=columntokey.replaceAll(left_key.substring(1), "\"").replaceAll(right_key.substring(0, right_key.length()-1), "\"");
		
		RepMemoryKey  mk=SystemConfig.getMemoryKeyMap().get(key);
		if(mk!=null){
			if(mk.getMemory_type().equals(ReportConstants.memory_my_define))
				return SystemConfig.getMy_memory().get(key);
			else{
				 if(mk.getValue_key()!=null){
					 Map<String, SItemvalue> simap=MemoryDict.getDictMap().get(mk.getValue_key());
				 	 if(simap!=null&&simap.size()>0){
				 		 Map<String,String> valuemap=new HashMap<String,String>();
				 		 for(SItemvalue si: simap.values())
				 			valuemap.put(si.getItem_value(), si.getItem_name());
				 		 return valuemap;
				 	 }
				 }
			}
		}
		return null;
	}
	/**
	 * 根据内存键值和ID取对应的名称
	 * @param memorykey
	 * @param id
	 * @return
	 */
	public static String getMemoryIDtoName(String memorykey,String id){
		
		RepMemoryKey  mk=SystemConfig.getMemoryKeyMap().get(memorykey);
		if(mk==null) return null;
		if(mk.getMemory_type().equals(ReportConstants.memory_my_define))
			return SystemConfig.getMy_memory().get(memorykey).get(id);
		else if(mk.getValue_key()!=null)
			return MemoryDict.getDictMap().get(mk.getValue_key()).get(id).getItem_name();
		return null;
	}
	/**
	 * 根据内存键取List
	 * @param memorykey
	 * @return
	 */
	public static List<QueryKeyValue> getMemoryList(String memorykey){
		RepMemoryKey  mk=SystemConfig.getMemoryKeyMap().get(memorykey);
		if(mk==null) return null;
		
		if(mk.getMemory_type().equals(ReportConstants.memory_my_define)){
			Map<String,String> mymemorymap= SystemConfig.getMy_memory().get(memorykey);
			if(mymemorymap!=null&&mymemorymap.size()>0){
				List<QueryKeyValue> list=new ArrayList<QueryKeyValue>() ;
				Iterator<String> iterator=mymemorymap.keySet().iterator();
				while(iterator.hasNext()){
					QueryKeyValue vo=new QueryKeyValue();
					String id=iterator.next();
					vo.setId(id);
					vo.setName(mymemorymap.get(id));
					list.add(vo);
				}
			    return list;
			}
		}else if(mk.getValue_key()!=null){
				 Map<String, SItemvalue> simap=MemoryDict.getDictMap().get(mk.getValue_key());
			 	 if(simap!=null&&simap.size()>0){
			 		List<QueryKeyValue> list=new ArrayList<QueryKeyValue>() ;
			 		 for(SItemvalue si: simap.values()){
			 			QueryKeyValue vo=new QueryKeyValue();
			 			vo.setId(si.getItem_value());
			 			vo.setName(si.getItem_name());
			 			list.add(vo);
			 		 }
			 		 return list;
			 	 }
			 }
		return null;
	}
}
