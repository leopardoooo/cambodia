package com.ycsoft.report.query.cube.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
/**
 * 维使用容器
 * 记录维的选择，交换，赚取，切片等信息
 */
public class DimensionContainer  implements DimensionRolap {

	/**
	 * 维和数据体的映射
	 */
	private String cubemapping=null;
	/**
	 * 维度容器ID
	 */
    private String id;
    /** 
     * 维度容器名称
    **/
    private String name;
    /** 
    * 是否使用标记
    **/
    private boolean usesign;
    /** 
    * 纵向维使用标记
    **/
    private boolean verticalsign;
    /** 
    * 当前维度容器的层级设置(当前维度容器处在哪个层)
    * */
    private int level;
    /**
     * 被切片的维度层=层级索引
     */
	private Integer slices_level;
	/**
	 * 被切片的维度层取值
	 */
	private String[] slices_value;
	/**
	 * 维度合计配置，维度的哪些层要合计
	 */
	private Integer[] level_totals;	
	/**
	 * 启用环比标志
	 */
	private boolean date_compare=false;
	
	/**
	 * 自定义层级排序
	 */
	private Map<Integer,String[]> level_sort_map;
	
	/**
	 * 初始化为最底层级维度配置
	 * @param repcube
	 * @throws ReportException
	 */
	public DimensionContainer(RepCube repcube) throws ReportException{
		if(repcube==null)
			throw new ReportException("repcube is null");
		if(repcube.getColumn_type().equals(DimensionType.measure.name()))
			throw new ReportException("measure cannot init DimensionContainer");
		this.setId(repcube.getColumn_define());
		Dimension dim=DimensionManage.getDimension(repcube.getColumn_define());
		this.setName(dim.getName());
		if(dim==null)
			throw new ReportException("Dimension:"+repcube.getColumn_define()+" undefine.");
		this.setUsesign(true);
		if(repcube.getColumn_type().equals(DimensionType.vertical.name()))
			this.setVerticalsign(true);
		this.setLevel(dim.getLevelNum());
		this.cubemapping=repcube.getColumn_code();
		
		if(repcube.getShow_control()!=null&&!repcube.getShow_control().trim().equals("")){
			//this.warnMap=gson.fromJson(repcube.getShow_control(), map_type);
		}
	}
	
	/**
	 * 维和cube数据体映射关系的维映射键值
	 */
	public String getColumnMappingKey() {
		Dimension dim=DimensionManage.getDimension(this.getId());
		return dim.getLevel(dim.getLevelNum()).getColumn_code();
	}

	/**
	 * 维和cube数据体映射关系的cube映射键值
	 */
	public String getCubeMappingKey() {
		return this.cubemapping;
	}
	
	/**
	 * 获得维对象
	 */
	public Dimension getDim() {
		return DimensionManage.getDimension(this.getId());
	}
	
	/**
	 * 创建切片过滤map
	 * @return
	 */
	private Map<String,Integer> createSlicesValueMap(){
		Map<String,Integer> valuemap=new HashMap<String,Integer>();
		if(this.slices_level!=null){
			for(String o: this.slices_value)
				valuemap.put(o, this.slices_level);
		}
		return valuemap;
	}
	/**
	 * 自定义层级排序
	 * @param level
	 * @param list
	 * @return
	 */
	private List<QueryKeyValue> sortLevelValue(int level,List<QueryKeyValue> list){
		if(this.level_sort_map!=null&&list.size()>0){
			String[] ids=this.level_sort_map.get(level);
			if(ids!=null||ids.length>0){
				Map<String,QueryKeyValue> map=new HashMap<String,QueryKeyValue>(list.size());
				for(QueryKeyValue vo: list){
					map.put(vo.getId(), vo);
				}
				List<QueryKeyValue> sortlist=new ArrayList<QueryKeyValue>(list.size());
				//先装入自定义排序内容
				for(String value:ids){
					if(map.containsKey(value)){
						sortlist.add(map.get(value));
						map.remove(value);
					}
				}
				//然后装入非自定义排序范围的值
				for(QueryKeyValue vo: list){
					if(map.containsKey(vo.getId())){
						sortlist.add(map.get(vo.getId()));
					}
				}
				return sortlist;
			}
		}
		return list;
	}
	
	/**
	 * 取维容器被切片影响的层取值(list元素顺序被排序影响)
	 * 
	 * @throws ReportException 
	 */
	public List<QueryKeyValue> queryLevelValue(int level) throws ReportException {
		//超范围
		if(level<1||level>this.getDim().getLevelNum()) 
			return new ArrayList<QueryKeyValue>();
		//无切片影响
		if(this.getSlices_level()==null)
			return sortLevelValue(level,this.getDim().getLevelValues(level));
		//切片影响
		//切片层 
		Map<String,Integer> map=this.createSlicesValueMap();
		
		if(level>this.slices_level){
			//向下展开
			map=this.getLevelValueMapByDown(this.slices_level, level, map);
		}else if(level<this.slices_level){
			//向上收缩取值
			map=this.getLevelValueMapByUp(this.slices_level, level, map);
		}
		List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
		for(QueryKeyValue vo:this.getDim().getLevelValues(level)){
			if(map.containsKey(vo.getId()))
				list.add(vo);
		}
		return sortLevelValue(level,list);
	}
	
	/**
	 * 向上展开获得取值level_tar层的取值
	 * @param level_start
	 * @param level_tar
	 * @param startMap
	 * @return
	 * @throws ReportException 
	 */
	private Map<String,Integer> getLevelValueMapByUp(int level_start,int level_tar,Map<String,Integer> startMap) throws ReportException{
		if(level_start<=level_tar) return startMap;
		
		Map<String,Integer> nextMap=new HashMap<String,Integer>();
		for(QueryKeyValue vo:this.getDim().getLevelValues(level_start)){
			if(startMap.containsKey(vo.getId()))
				nextMap.put(vo.getPid(), level_start-1);
		}
		
		return getLevelValueMapByUp(level_start-1,level_tar,nextMap);
	}
	/**
	 * 向下展开获得level_tar层的取值
	 * @param level_start
	 * @param level_tar
	 * @param startMap
	 * @return
	 * @throws ReportException 
	 */
	private Map<String,Integer> getLevelValueMapByDown(int level_start,int level_tar,Map<String,Integer> startMap) throws ReportException{
		if(level_start>=level_tar) return startMap;
		
		Map<String,Integer> nextMap=new HashMap<String,Integer>();
		for(QueryKeyValue vo:this.getDim().getLevelValues(level_start+1)){
			if(startMap.containsKey(vo.getPid()))
				nextMap.put(vo.getId(), level_start+1);
		}
		return getLevelValueMapByDown(level_start+1,level_tar,nextMap);
	}
	
	public Integer getSlices_level() {
		return slices_level;
	}

	public void setSlices_level(Integer slices_level) {
		this.slices_level=null;
		if(slices_level==null) return;
		for(int i=1;i<=this.getDim().getLevelNum();i++){
			if(i==slices_level){
				this.slices_level = slices_level;
				return;
			}
		}
	}

	public String[] getSlices_value() {
		return slices_value;
	}

	public void setSlices_value(String[] slices_value) {
		this.slices_value = slices_value;
	}

	public Boolean isLevelTotal(int level) {
		if(this.level_totals==null||this.level_totals.length==0) return false;
		for(int o:this.level_totals)
			if(o==level) return true;
		return false;
	}

	public void setLevelTotal(Integer... levels) {
		this.level_totals=null;
		if(levels==null||levels.length==0) return;
		
		List<Integer> list=new ArrayList<Integer>();
		
		for(int i=1;i<=this.getDim().getLevelNum();i++){
			for(int o:levels)
				if(o==i) list.add(o);
		}
		this.level_totals=list.toArray(new Integer[list.size()]);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUsesign() {
		return usesign;
	}

	public void setUsesign(boolean usesign) {
		this.usesign = usesign;
	}

	public boolean isVerticalsign() {
		return verticalsign;
	}

	public void setVerticalsign(boolean verticalsign) {
		this.verticalsign = verticalsign;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Integer[] getLevel_totals() {
		return level_totals;
	}

	public void setLevel_totals(Integer[] level_totals) {
		this.level_totals = level_totals;
	}

	public boolean isDate_compare() {
		return date_compare&&this.getDim().isDateDim();
	}

	public void setDate_compare(boolean date_compare) {
		this.date_compare = date_compare;
	}

	public Map<Integer, String[]> getLevel_sort_map() {
		return level_sort_map;
	}

	public void setLevel_sort_map(Map<Integer, String[]> level_sort_map) {
		this.level_sort_map = level_sort_map;
	}

}
