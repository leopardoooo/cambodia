package com.ycsoft.report.query.cube.impl;

/***********************************************************************
 * Module:  DimensionImpl.java
 * Author:  new
 * Purpose: Defines the Class DimensionImpl
 ***********************************************************************/



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.report.bean.RepDimension;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionLevel;
import com.ycsoft.report.query.datarole.DataControl;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/** 维定义实现
 * 
 * @pdOid 20c3163c-64c2-4ff6-8951-a5c1a23edfa8 */
public class DimensionImpl extends RepDimension implements Dimension {
	
	private static final String detailprefix="%";
	
	private Map<Integer,DimensionLevel> levels;
	private QueryKeyValueDao queryKeyValueDao;
	private DataControl dataControl;
	
	public DimensionImpl(QueryKeyValueDao queryKeyValueDao,DataControl dataControl,Map<Integer,DimensionLevel> levels,RepDimension repDimension) throws ReportException{
		this.queryKeyValueDao=queryKeyValueDao;
		this.dataControl=dataControl;
		
		this.levels=levels;
		
		try {
			BeanHelper.copyProperties(this, repDimension);
			
			if(this.getFrom_table()!=null&&this.getFrom_table().toUpperCase().contains("FROM")
					&&this.getFrom_table().toUpperCase().contains("SELECT")){
				//当from_table字段不为空时,且含有from、select关键字时，给from_table字段加上()
				this.setFrom_table("("+this.getFrom_table()+")");
			}
		} catch (Exception e) {
			throw new ReportException(e);
		} 
		if(levels==null||levels.size()==0)
			throw new ReportException("维定义"+this.getId()+"的rep_dimension_level 配置为空");
	}
	
	/**
	 * 层是从1开始编码的
	 */
	public DimensionLevel getLevel(int level) {
	    if(level<=0||level>levels.size()) return null;
		return levels.get(level);
	}
	/**
	 * 维的层级数
	 */
	public int getLevelNum() {
		return levels.size();
	}

	/**
	 * 获得一个权限层级限定的维度各个层级取值
	 * @throws ReportException 
	 */
	private void getDimLevelControlMap(DimensionLevel dimlevel,Map<Integer,Map<String,String>> controlMap) throws ReportException{
		
		//存储每一个层级的权限关联取值
		Map<Integer,List<QueryKeyValue>> controllist=new HashMap<Integer,List<QueryKeyValue>>();
		List<QueryKeyValue> startList= dataControl.getControlValues(dimlevel.getDataRoleKey());
		controllist.put(dimlevel.getLevel(), startList);
		
		//反推上级
		Map<String,String> startMap=new HashMap<String,String>();
		for(QueryKeyValue vo:startList){
			startMap.put(vo.getPid(), null);
		}
		for(int i=dimlevel.getLevel()-1;i>=1;i--){
			Map<String,String> nextMap=new HashMap<String,String>();
			List<QueryKeyValue> nextList=new ArrayList<QueryKeyValue>();
			for(QueryKeyValue vo:DimensionLevelValueManage.getDimLevelValueList(this.getLevel(i))){
				if(startMap.containsKey(vo.getId())){
					nextMap.put(vo.getPid(), null);
					nextList.add(vo);
				}
			}
			startMap=nextMap;
			controllist.put(i, nextList);
		}
		//反推下级
		startMap.clear();
		for(QueryKeyValue vo:startList){
			startMap.put(vo.getId(), null);
		}
		for(int i=dimlevel.getLevel()+1;i<=this.getLevelNum();i++){
			Map<String,String> nextMap=new HashMap<String,String>();
			List<QueryKeyValue> nextList=new ArrayList<QueryKeyValue>();
			for(QueryKeyValue vo:DimensionLevelValueManage.getDimLevelValueList(this.getLevel(i))){
				if(startMap.containsKey(vo.getPid())){
					nextMap.put(vo.getId(), null);
					nextList.add(vo);
				}
			}
			startMap=nextMap;
			controllist.put(i, nextList);
		}
		//取交集
		for(int i=1;i<=this.getLevelNum();i++){
			Map<String,String> levelmap=controlMap.get(i);
			List<QueryKeyValue> levellist=controllist.get(i);
			if(levelmap==null||levelmap.size()==0){
				levelmap=new HashMap<String,String>();
				for(QueryKeyValue vo:levellist){
					levelmap.put(vo.getId(), null);
				}
				controlMap.put(i, levelmap);
			}else{
				Map<String,String> newlevelmap=new HashMap<String,String>();
				for(QueryKeyValue vo:levellist){
					if(levelmap.containsKey(vo.getId()))
						newlevelmap.put(vo.getId(), null);
				}
				controlMap.put(i, newlevelmap);
			}
		}
	}
	/**
	 * 获取一个维度的权限控制各个层级取值清单
	 * @return
	 * @throws ReportException
	 */
	private Map<Integer,Map<String,String>> getDimControlMap() throws ReportException{
		Map<Integer,Map<String,String>> controlMap=new HashMap<Integer,Map<String,String>>();
		for(int i=1;i<=this.getLevelNum();i++){
			DimensionLevel dlevel=this.getLevel(i);
			if(dataControl.isControl(dlevel.getDataRoleKey())){
				this.getDimLevelControlMap(dlevel, controlMap);
			}
		}
		return controlMap;
	}
	/**
	 * 获得一个维一个层的取值
	 * 从内存中获取
	 * 权限控制
	 * 指定父级ID
	 * @throws ReportException  
	 */
	public List<QueryKeyValue> getLevelValues(int level,String...pid) throws ReportException {
		DimensionLevel dlevel=this.getLevel(level);
		List<QueryKeyValue> level_list=DimensionLevelValueManage.getDimLevelValueList(dlevel);
		//权限控制计算
		Map<String,String> level_control=this.getDimControlMap().get(level);
		if(level_control!=null){
			List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
			for(QueryKeyValue vo: level_list){
				if(level_control.containsKey(vo.getId()))
					list.add(vo);
			}
			level_list=list;
		}
		//父id过滤计算
		if(pid!=null&&pid.length>0){
			List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
			Map<String,String> pmap=new HashMap<String,String>();
			for(String o:pid)
				pmap.put(o, null);
			
			for(QueryKeyValue vo: level_list){
				if(pmap.containsKey(vo.getPid()))
					list.add(vo);
			}
			level_list=list;
		}

		return level_list;
	}
	/**
	 * 获得一个维一个层的取值，
	 * 从数据库获取
	 */
	public List<QueryKeyValue> getLevelValuesByDatabase(int level)throws ReportException {
		DimensionLevel dlevel=this.getLevel(level);
		if(dlevel!=null){
			return this.queryKeyValueDao.findList(this.getDatabase(), this.assemblyLevelSql(dlevel));
		}else 
			return null;
	}
	/**
	 * 装配一个维层级的取值sql
	 * @param level
	 * @return
	 */
	private String assemblyLevelSql(DimensionLevel level){
		DimensionLevel p= this.getLevel(level.getLevel()-1);
		return StringHelper.append("select distinct ",getId(),".",level.getColumn_code()," id, ",getId(),".",level.getColumn_text()," name ",(p==null?"":(","+getId()+"."+p.getColumn_code()+" pid"))," from ",this.getTabel()," ",getId()," order by ",getId(),".",level.getColumn_order()).toString();
	}

	public String getTabel() {
		return getFrom_table();
	}

	public String getMappingKey() {
		return this.getLevel(this.getLevelNum()).getColumn_code();
	}

	public String getPrefixid() {
		return detailprefix+this.getId()+detailprefix;
	}

	public Map<DimensionLevel,List<QueryKeyValue>> getDimLevelControlMap() throws ReportException{
		Map<DimensionLevel,List<QueryKeyValue>> levelcontrolMap=new HashMap<DimensionLevel,List<QueryKeyValue>>();
		for(DimensionLevel level: this.levels.values()){
			if(this.dataControl.isControl(level.getDataRoleKey()))
				levelcontrolMap.put(level, this.dataControl.getControlValues(level.getDataRoleKey()));
		}
		return levelcontrolMap;
	}

	public boolean isDateDim() {
		return ReportConstants.VALID_T.equals(this.getDatesign());
	}
}