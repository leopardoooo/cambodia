package com.ycsoft.report.query.cube.impl;
/***********************************************************************
 * Module:  DimensionManage.java
 * Author:  new
 * Purpose: Defines the Class DimensionManage
 ***********************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDimension;
import com.ycsoft.report.bean.RepDimensionLevel;
import com.ycsoft.report.dao.config.RepDimensionDao;
import com.ycsoft.report.dao.config.RepDimensionLevelDao;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionLevel;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.datarole.DataControl;
import com.ycsoft.report.query.key.Impl.ConKeyCheck;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.menory.MemoryCacheInit;

/** 维管理
 * 维初始化，维获取
 * 
 * @pdOid 0d4a36bd-bdf4-4ef1-ac9f-e16df2b0e0a6 */
public class DimensionManage implements MemoryCacheInit {
	
   /** @pdOid 25eff1be-1250-4eda-b8c0-937a2437def3 */
   private static Map<String,Dimension> dimensionMap=new HashMap<String,Dimension>();
   
   private RepDimensionDao repDimensionDao;
   private RepDimensionLevelDao repDimensionLevelDao;
   private QueryKeyValueDao queryKeyValueDao;
   private DataControl dataControl;
    
   public void setDataControl(DataControl dataControl) {
	this.dataControl = dataControl;
}

/** 根据ID获得一个维
    * 
    * @param id
    * @pdOid 13c6f940-514a-40b2-99bd-7de3db0d6f9e */
   public static Dimension getDimension(String id) {
      return dimensionMap.get(id);
   }

   public static List<Dimension> getDimList(){
	   List<Dimension> list=new ArrayList<Dimension>();
	   list.addAll(dimensionMap.values());
	   return list;
   }
   
   /**
    * 获得一个维顺序排序的层清单
    * @param dim
    * @return
    */
   public List<DimensionLevel> getDimLevelsOrder(String dim){
	   List<DimensionLevel> dimlevels=new ArrayList<DimensionLevel>();
	   Dimension dimension=dimensionMap.get(dim);
	   if(dim!=null){
		   for(int i=1;i<=dimension.getLevelNum();i++)
			   dimlevels.add(dimension.getLevel(i));
	   }
	   return dimlevels;
   }
    
   public String initAll() throws  ReportException{
		try {
			List<RepDimension> dimlist=repDimensionDao.findAll();
			List<RepDimensionLevel> levellist=repDimensionLevelDao.findAll();
			
			Map<String,List<RepDimensionLevel>> levelMap=CollectionHelper.converToMap(levellist, "id");
			
			Map<String,Dimension> dimMap=new HashMap<String,Dimension>();
			StringBuilder bf=new StringBuilder();
			for(RepDimension repdim:dimlist){
				if(levelMap.containsKey(repdim.getId())){
					Map<Integer,DimensionLevel> dimlevelMap=new HashMap<Integer,DimensionLevel>();
					for(RepDimensionLevel o:levelMap.get(repdim.getId()))
						dimlevelMap.put(o.getDim_level(), new DimensionLevelImpl(o));
					dimMap.put(repdim.getId(), new DimensionImpl(queryKeyValueDao,dataControl,dimlevelMap,repdim));
				}else{
					bf.append(repdim.getId()).append("_dimension_init_error:level is not exits.;");
					LoggerHelper.error(this.getClass(), repdim.getId()+"_dimension_init_error:level is not exits.;");
				}
			}
			
			dimensionMap=dimMap;
			return bf.toString();
		} catch (ReportException e) {
			throw e;
		} catch (JDBCException e) {
			throw new ReportException("Dimension内存初始化错误",e);
		} catch (Exception e) {
			throw new ReportException("Dimension内存初始化错误",e);
		}
		
	}


	public void setRepDimensionDao(RepDimensionDao repDimensionDao) {
		this.repDimensionDao = repDimensionDao;
	}


	public void setRepDimensionLevelDao(RepDimensionLevelDao repDimensionLevelDao) {
		this.repDimensionLevelDao = repDimensionLevelDao;
	}


	public void setQueryKeyValueDao(QueryKeyValueDao queryKeyValueDao) {
		this.queryKeyValueDao = queryKeyValueDao;
	}
   
}