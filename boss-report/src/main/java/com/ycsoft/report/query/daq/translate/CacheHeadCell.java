package com.ycsoft.report.query.daq.translate;

import java.util.HashMap;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.MeasureGather;
import com.ycsoft.report.query.cube.impl.CubeHeadCellImpl;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * cube内存计算用单元格
 * @author new
 *
 */
public class CacheHeadCell extends CubeHeadCellImpl {
	
	protected Dimension dimension;
	/**
	 * repcube.column_code
	 * 维度和基础数据表的对应关系
	 * dimrolap.getCubeMappingKey
	 */
	protected String cubemappingkey;
	/**
	 * 度量聚集方法
	 */
	protected MeasureGather meagather;
	/**
	 * 权限和切片影响下的层级取值
	 */
	protected Map<Integer,Map<String,QueryKeyValue>> levelMap=new HashMap<Integer,Map<String,QueryKeyValue>>();
	
	/**
	 * 当前维度的各个层级的取值内容的顺序Map<level,Map<维度当前层的取值ID,排序大小>>
	 */
	protected Map<Integer,Map<String,Integer>> sortMap=new HashMap<Integer,Map<String,Integer>>();
	
	/**
	 * 表头使用标志
	 * 未使用的维度(有权限影响的)，使用最低层判断，该值=false;
	 */
	protected boolean usesign=true;
	


	/**
	 * 横向单元格=根据横向维度计算得到
	 * @param cell
	 * @param rolap
	 * @return
	 * @throws ReportException
	 */
	public static CacheHeadCell createCrossHead(CubeHeadCell cell,DimensionRolap rolap,CacheMap cacheMap) throws ReportException{
		if(!cell.getDim_type().equals(DimensionType.crosswise))
			throw new ReportException("head is not crosswise.");
		CacheHeadCell cross=new CacheHeadCell(cell);
		cross.dimension=rolap.getDim();
		cross.cubemappingkey=rolap.getCubeMappingKey();
		if(cross.getLevel()>cross.getDimension().getLevelNum()){
			cross.setLevel(cross.getDimension().getLevelNum());
		}
			
		if(cacheMap.dimLevelMap.containsKey(cross.dimension)){
			cross.levelMap=cacheMap.dimLevelMap.get(cross.dimension);
			cross.sortMap =cacheMap.dimSrotMap.get(cross.dimension);
		}else{
			for(int i=1;i<=rolap.getDim().getLevelNum();i++){
				Map<String,QueryKeyValue> lmap=new HashMap<String,QueryKeyValue>();//id-value的map层取值
				Map<String,Integer> smap=new HashMap<String,Integer>();//层排序
				int idx=0;
				for( QueryKeyValue vo: rolap.queryLevelValue(i)){
					lmap.put(vo.getId(), vo);
					smap.put(vo.getId(), idx++);
				}
				cross.levelMap.put(i, lmap);
				cross.sortMap.put(i, smap);
			}
			cacheMap.dimLevelMap.put(cross.dimension, cross.levelMap);
			cacheMap.dimSrotMap.put(cross.dimension, cross.sortMap);
		}
		return cross;
	}
	/**
	 * 纵向单元格=根据纵向维度计算得到
	 * @param cell
	 * @param rolap
	 * @param mea
	 * @return
	 * @throws ReportException
	 */
	public static CacheHeadCell createVerticalHead(CubeHeadCell cell,DimensionRolap rolap,Measure mea,CacheMap cacheMap) throws ReportException{
		if(!cell.getDim_type().equals(DimensionType.vertical))
			throw new ReportException("head is not vertical.");
		CacheHeadCell vertical=new CacheHeadCell(cell);
		vertical.dimension=rolap.getDim();
		vertical.meagather=mea.getCalculation();
		vertical.cubemappingkey=rolap.getCubeMappingKey();
		if(vertical.getLevel()>vertical.getDimension().getLevelNum()){
				vertical.setLevel(vertical.getDimension().getLevelNum());
		}
		if(cacheMap.dimLevelMap.containsKey(vertical.dimension)){
			vertical.levelMap=cacheMap.dimLevelMap.get(vertical.dimension);
			vertical.sortMap =cacheMap.dimSrotMap .get(vertical.dimension);
		}else{
			for(int i=1;i<=rolap.getDim().getLevelNum();i++){
				Map<String,QueryKeyValue> lmap=new HashMap<String,QueryKeyValue>();
				Map<String,Integer> smap=new HashMap<String,Integer>();//层排序
				int idx=0;
				for( QueryKeyValue vo: rolap.queryLevelValue(i)){
					lmap.put(vo.getId(), vo);
					smap.put(vo.getId(), idx++);
				}
				vertical.levelMap.put(i, lmap);
				vertical.sortMap.put(i, smap);
			}
			cacheMap.dimLevelMap.put(vertical.dimension, vertical.levelMap);
			cacheMap.dimSrotMap.put(vertical.dimension, vertical.sortMap);
		}
		return vertical;
	}
	/**
	 * 生成度量的单元格
	 * @param cell
	 * @param mea
	 * @return
	 * @throws ReportException
	 */
	public static CacheHeadCell createMeasureHead(CubeHeadCell cell,Measure mea) throws ReportException{
		if(!cell.getDim_type().equals(DimensionType.measure))
			throw new ReportException("head is not vertical.");
		CacheHeadCell measure=new CacheHeadCell(cell);
		
		measure.meagather=mea.getCalculation();
		measure.cubemappingkey=measure.getMea_code();
		return measure;
	}
	
	/**
	 * 虚拟单元格=维度在cube未使用但是存在权限控制
	 * @param rolap
	 * @return
	 * @throws ReportException
	 */
	public static CacheHeadCell createDummyHead(DimensionRolap rolap) throws ReportException{
		CacheHeadCell dummy=new CacheHeadCell();
		dummy.dimension=rolap.getDim();
		dummy.cubemappingkey=rolap.getCubeMappingKey();
		dummy.setLevel(rolap.getDim().getLevelNum());
		Map<String,QueryKeyValue> lmap=new HashMap<String,QueryKeyValue>();
		for( QueryKeyValue vo: rolap.queryLevelValue(dummy.getLevel()))
			lmap.put(vo.getId(), vo);
		dummy.levelMap.put(dummy.getLevel(), lmap);
		return dummy;
	}
	
	private CacheHeadCell(CubeHeadCell cell){
		super(cell);
	}
	
	private CacheHeadCell(){};
		
	/**
	 * 根据最低层的id 提取当前层级键值
	 * @throws ReportException 
	 */
	public QueryKeyValue translateKeyValue(String id) throws ReportException{
		if(DimensionType.measure.equals(this.getDim_type()))
			throw new ReportException("measure cannot translateKeyValue.");
		for(int i=this.dimension.getLevelNum();i>this.getLevel();i--){
			QueryKeyValue vo=this.levelMap.get(i).get(id);
			if(vo==null) return null;
			id=vo.getPid();
		}
		return this.levelMap.get(this.getLevel()).get(id);
	}
	/**
	 * 提取当前层键值的排序值
	 * @param id
	 * @return
	 * @throws ReportException
	 */
	public Integer translateSortValue(QueryKeyValue id) throws ReportException{
		Map<String,Integer> map=this.sortMap.get(this.getLevel());
		if(map==null) throw new ReportException(this.getDim()+"维度_"+this.getLevel()+"层:"+id.getId()+"找不到排序值");
		Integer sort=map.get(id.getId());
		if(sort==null) throw new ReportException(this.getDim()+"维度_"+this.getLevel()+"层:"+id.getId()+"找不到排序值");
		return sort;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	public String getCubemappingkey() {
		return cubemappingkey;
	}
	public MeasureGather getMeagather() {
		return meagather;
	}

	public boolean isUsesign() {
		return usesign;
	}
	public Map<Integer,Map<String, Integer>> getSortMap() {
		return sortMap;
	}
}

/**
 * 计算维度取值和排序的缓存类
 */
class CacheMap {
	
	//权限和切片影响下的层级取值的计算缓存
	Map<Dimension,Map<Integer,Map<String,QueryKeyValue>>> dimLevelMap=new HashMap<Dimension,Map<Integer,Map<String,QueryKeyValue>>>();
	//维度的层取值排序计算缓存
	Map<Dimension,Map<Integer,Map<String,Integer>>> dimSrotMap=new HashMap<Dimension, Map<Integer,Map<String,Integer>>>();
}