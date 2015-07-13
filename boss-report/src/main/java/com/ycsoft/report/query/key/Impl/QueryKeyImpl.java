package com.ycsoft.report.query.key.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.datarole.DataControl;
import com.ycsoft.report.query.key.QueryKey;

public class QueryKeyImpl implements QueryKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3971240963271361561L;
	/**
	 * 获得一张报表(sql中含有的及其父key)需要使用的查询条件
	 */
	public List<RepKeyDto> getQueryKeyList(String sql) throws ReportException {
		Map<String, RepKeyDto>  querykeymap  =new HashMap<String,RepKeyDto>();
		//是否存在子key的判断map
		Map<String,String> hassonmap=new HashMap<String,String>();
		
		List<RepKeyDto>         querykeylist=new ArrayList<RepKeyDto>();
		try {
			//获取sql中定义的keymap,keylist
			List<RepKeyCon> conlist = SystemConfig.getConList();
			for (RepKeyCon key : conlist) {
				if (sql.indexOf(key.getKey()) >= 0) {
					RepKeyDto keydto = new RepKeyDto();
					keydto.setKey(key.getKey());
					keydto.setName(key.getName());
					keydto.setFkey(key.getFkey());
					keydto.setHtmlcode(key.getHtmlcode());
					keydto.setType(key.getType());
					keydto.setDatabase(key.getDatabase());
					keydto.setIsnull(key.getIsnull());
					querykeymap.put(keydto.getKey(), keydto);
					if(StringHelper.isNotEmpty(keydto.getFkey())
							&&keydto.getType().equals(ReportConstants.keytype_cascade))
						hassonmap.put(keydto.getFkey(), keydto.getFkey());
					querykeylist.add(keydto);
				}
			}

			
			//系统定义的全部key_con_map 
			Map<String, RepKeyCon> conmap = SystemConfig.getConMap();
			
			for (RepKeyDto key : querykeylist) {
				//父级不为空，且父级不存在sql定义中
				//反向取父级查询条件
				RepKeyDto sonkey=key;
				RepKeyDto fkeydto=null;
				//反向取级联父级条件条件定义，一直往上级推导查询条件
				while ((fkeydto=this.findFkey(sonkey, querykeymap))!=null){
					querykeymap.put(fkeydto.getKey(), fkeydto);
					if(StringHelper.isNotEmpty(fkeydto.getFkey())
							&&fkeydto.getType().equals(ReportConstants.keytype_cascade)){
						hassonmap.put(fkeydto.getFkey(), fkeydto.getFkey());
					}
					sonkey=fkeydto;
				}
			}
			
			querykeylist.clear();
			//排序
			for (RepKeyCon key : conlist) {
				if (querykeymap.containsKey(key.getKey()))
					querykeylist.add(querykeymap.get(key.getKey()));
			}
			//更新son标志
			for(RepKeyDto k:querykeylist)
				if(hassonmap.containsKey(k.getKey())) k.setSonkey(true);
			return querykeylist;
		} catch (ReportException e) {
			throw e;
		}catch (Exception e) {
			throw new ReportException("系统错误",e);
		}
	}
	/**
	 * 并对的父子关系的datefield,textfiedl组件类型的条件组装到同一行
	 */
	public List<RepKeyDto> getQueryKeySameLineList(String sql) throws ReportException {
		Map<String, RepKeyDto>  querykeymap  =new HashMap<String,RepKeyDto>();
		//是否存在子key的判断map
		Map<String,String> hassonmap=new HashMap<String,String>();
		
		List<RepKeyDto>         querykeylist=new ArrayList<RepKeyDto>();
		try {
			//获取sql中定义的keymap,keylist
			List<RepKeyCon> conlist = SystemConfig.getConList();
			for (RepKeyCon key : conlist) {
				if (sql.indexOf(key.getKey()) >= 0) {
					RepKeyDto keydto = new RepKeyDto();
					keydto.setKey(key.getKey());
					keydto.setName(key.getName());
					keydto.setFkey(key.getFkey());
					keydto.setHtmlcode(key.getHtmlcode());
					keydto.setType(key.getType());
					keydto.setDatabase(key.getDatabase());
					keydto.setIsnull(key.getIsnull());
					querykeymap.put(keydto.getKey(), keydto);
					if(StringHelper.isNotEmpty(keydto.getFkey())
							&&keydto.getType().equals(ReportConstants.keytype_cascade))
						hassonmap.put(keydto.getFkey(), keydto.getFkey());
					querykeylist.add(keydto);
				}
			}

			for (RepKeyDto key : querykeylist) {
				
				RepKeyDto sonkey=key;
				RepKeyDto fkeydto=null;
				//反向取级联父级条件条件定义，一直往上级推导查询条件
				while ((fkeydto=this.findFkey(sonkey, querykeymap))!=null){
					querykeymap.put(fkeydto.getKey(), fkeydto);
					if(StringHelper.isNotEmpty(fkeydto.getFkey())
							&&fkeydto.getType().equals(ReportConstants.keytype_cascade)){
						hassonmap.put(fkeydto.getFkey(), fkeydto.getFkey());
					}
					sonkey=fkeydto;
				}
				
				//非级联查询条件，把父子查询条件装载到同一行
				if(!key.getType().equals(ReportConstants.keytype_cascade)
					 &&querykeymap.containsKey(key.getFkey())){
					querykeymap.get(key.getFkey()).setSamelinekey(key);
					querykeymap.remove(key.getKey());
				}
			}
			
			querykeylist.clear();
			//排序
			for (RepKeyCon key : conlist) {
				if (querykeymap.containsKey(key.getKey()))
					querykeylist.add(querykeymap.get(key.getKey()));
			}
			//更新son标志
			for(RepKeyDto k:querykeylist)
				if(hassonmap.containsKey(k.getKey())) k.setSonkey(true);
			return querykeylist;
		} catch (ReportException e) {
			throw e;
		}catch (Exception e) {
			throw new ReportException("系统错误",e);
		}
	}
	/**
	 * 反向取级联父级条件条件定义
	 * @param key
	 * @param querykeymap
	 * @return
	 * @throws ReportException
	 */
	private RepKeyDto findFkey(RepKeyDto key,Map<String, RepKeyDto>  querykeymap) throws ReportException{
		//反向取级联父级条件条件定义
		//父级不为空，且父级不存在sql定义中
		if (StringHelper.isNotEmpty(key.getFkey())
				&&key.getType().equals(ReportConstants.keytype_cascade)
				&& !querykeymap.containsKey(key.getFkey())) {
			RepKeyDto keydto = new RepKeyDto();
			RepKeyCon fkeydto=SystemConfig.getConMap().get(key.getFkey());
			if(fkeydto==null) throw new ReportException("rep_key_con.key="+key.getKey()+" 记录的 fkey 未定义");
			keydto.setKey(fkeydto.getKey());
			keydto.setName(fkeydto.getName());
			keydto.setFkey(fkeydto.getFkey());
			keydto.setHtmlcode(fkeydto.getHtmlcode());
			keydto.setType(fkeydto.getType());
			keydto.setDatabase(fkeydto.getDatabase());
			keydto.setIsnull(fkeydto.getIsnull());
			return keydto;
		}
		return null;
	}
	/**
	 * 对象转换成map，然后转换成json,然后ZipUtil压缩字符串
	 * 把\u0027替换成'
	 * 如果长度超过3500个则压缩处理
	 */
	public String toString(List<? extends ConKeyValue> keydtolist) throws ReportException {
		try {
			if(keydtolist==null||keydtolist.size()==0)
				return "";
			Map<String,String> keyvaluemap=new HashMap<String,String>();
			for(ConKeyValue dto:keydtolist)
				keyvaluemap.put(dto.getKey(), dto.getValue()==null?"":dto.getValue());
			String json= JsonHelper.fromObject(keyvaluemap).replaceAll("\\\\u0027", "'");
			//如果json长度大于2000，则启用过滤null
			if(json.length()>2000){
				Map<String,String> nonullmap=new HashMap<String,String>();
				for(String key: keyvaluemap.keySet()){
					String value=keyvaluemap.get(key);
					if(value!=null&&!"".equals(value))
						nonullmap.put(key, value);
				}
				if(nonullmap.size()==0) return "";
				json=JsonHelper.fromObject(nonullmap).replaceAll("\\\\u0027", "'");
			}
			/**
			 * 当json长度还是超过某个值，执行压缩
			 * rep_query_log增加标记，表示被压缩。
			 *if(json.length()>3000)
			 *	json=ZipUtil.compress(json);
			 **/
			return json;
		} catch (Exception e) {
			throw new ReportException("系统错误",e);
		}
	}
	/**
	 * ZipUtil解压字符串，然后转换成map
	 * @param json
	 * @return
	 * @throws ReportException
	 */
	public Map<String,String> toValueMap(String json) throws ReportException{
		try {
			Map<String,String> keyvaluemap=new HashMap<String,String>();
			if(StringHelper.isEmpty(json))
				return keyvaluemap;
			keyvaluemap=JsonHelper.toMap(json);
			return keyvaluemap;
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (Exception e) {
			throw new ReportException(e);
		}
		
	}

	/**
	 * 把indexComponent转化成tree顺序
	 * 参数key为树根
	 */
	public List<RepKeyDto> translateToTree(List<RepKeyDto> keydtolist,String key)
			throws ReportException {
		
		try {
			List<RepKeyDto> tree=new ArrayList<RepKeyDto>();
			if(keydtolist==null) return tree;
			
			//sql定义中存在的条件map
			Map<String,RepKeyDto> keydtomap=CollectionHelper.converToMapSingle(keydtolist, "key");
			
			if(StringHelper.isEmpty(key)){
				for(RepKeyCon con:SystemConfig.getConTree()){
					if(keydtomap.containsKey(con.getKey())&&StringHelper.isNotEmpty(con.getDatabase()))
						tree.add(keydtomap.get(con.getKey()));
				}
			}
			else{
				//定义 以传入key为除根的keytreemap
				Map<String,RepKeyDto> keytreemap=new HashMap<String,RepKeyDto>();
				for(RepKeyCon con:SystemConfig.getConTree()){
					if(con.getKey().equals(key)){
						//设置树根key
						keytreemap.put(key, keydtomap.get(key));
					}else{
						// 当 条件 以传入key为除根的keytreemap中存在，且存在sql定义的
						if(keytreemap.containsKey(con.getFkey())&&keydtomap.containsKey(con.getKey())){
							keytreemap.put(con.getKey(), keydtomap.get(con.getKey()));
							tree.add(keydtomap.get(con.getKey()));
						}
					}
				}
			}
			return tree;
		} catch (Exception e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 获得一个查询条件的取值
	 * @throws ReportException 
	 */
	public List<QueryKeyValue> getKeyValue(String key, String fkey_value,
			String countyid) throws ReportException {		
		if(dataControl.isControl(key)){
			List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
			if(StringHelper.isNotEmpty(fkey_value)&&dataControl.isDataRightTypeControl(key)){
				List<QueryKeyValue> temp_list=queryKeyValueDao.fineList(countyid, key, fkey_value);
				Map<String,String> checkMap=new HashMap<String,String>();
				for(QueryKeyValue vo: dataControl.getControlValues(key))
					checkMap.put(vo.getId(), vo.getId());
				for(QueryKeyValue vo:temp_list)
					if(checkMap.containsKey(vo.getId())) list.add(vo);	
			}else{
				list=dataControl.getControlValues(key);
			}
			return list;
		}else{
			return queryKeyValueDao.fineList(countyid, key, fkey_value);
		}
	}
	/**
	 * 检查页面回传的查询条件取值，判断键值有无权限如此取值
	 * @param keyvalues
	 * @throws ReportException 
	 */
	public void checkKeyValueList(List<ConKeyValue> keyvalues) throws ReportException {
		if(keyvalues==null||keyvalues.size()==0) return;
		for(ConKeyValue vo:keyvalues){
			if((vo.getValue()==null||vo.getValue().equals(""))
					&&dataControl.isControl(vo.getKey())){
				StringBuilder value=new StringBuilder();
				List<QueryKeyValue> list=dataControl.getControlValues(vo.getKey());
				for(int i=0;i<list.size();i++){
					if(i>0)
						value.append("','").append(list.get(i).getId());
					else 
						value.append(list.get(i).getId());
				}
				vo.setValue(value.toString());
			}
		}
	}
	
	private DataControl dataControl;
	private QueryKeyValueDao queryKeyValueDao;
	public DataControl getDataControl() {
		return dataControl;
	}
	public void setDataControl(DataControl dataControl) {
		this.dataControl = dataControl;
	}
	public QueryKeyValueDao getQueryKeyValueDao() {
		return queryKeyValueDao;
	}
	public void setQueryKeyValueDao(QueryKeyValueDao queryKeyValueDao) {
		this.queryKeyValueDao = queryKeyValueDao;
	}
	

}
