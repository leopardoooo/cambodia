package com.ycsoft.report.query.daq.translate;

import static com.ycsoft.report.commons.ReportConstants.memory_row_max;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.commons.CacheInput;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.daq.DataReader;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
/**
 * cube从缓存中提取数据体
 */
public class CacheAcquisition implements CacheTranslateCube {

	private boolean translate_sucess=false;
	
	private Map<CacheKey,List<Double>> menoryMap=new HashMap<CacheKey,List<Double>>();
	
	//private List<CacheKey> menoryList=new ArrayList<CacheKey>();
	
	/**
	 * 计算转换用的权限判断的虚拟表头
	 * @param dataset
	 * @return
	 * @throws ReportException
	 */
	protected List<CacheHeadCell> createDataCotrolHead(AbstractDataSet dataset) throws ReportException {
		//未使用的但有权限控制影响的维度清单
		List<DimensionRolap> datacotrolRolaps=new ArrayList<DimensionRolap>();
		for(DimensionRolap rolap:dataset.getCube().getDimensionRolaps()){
			if(!rolap.isUsesign()&&rolap.getDim().getDimLevelControlMap().size()>0)
					datacotrolRolaps.add(rolap);
		}
		//创建计算用cube表头
		List<CacheHeadCell> headlist=new ArrayList<CacheHeadCell>();
		//未使用的维度(有权限的)的虚拟表头计算
		for(DimensionRolap rolap: datacotrolRolaps){
			CacheHeadCell o=CacheHeadCell.createDummyHead(rolap);
			headlist.add(o);
		}
		return headlist;
	}
	/**
	 * 计算转换用的cube表头
	 * @param cube
	 * @return
	 * @throws ReportException
	 */
	protected List<CacheHeadCell> createCacheHeadCell(AbstractDataSet dataset) throws ReportException{
		
		List<DimensionRolap> dimrolaps= dataset.getCube().getDimensionRolaps();
		//有效维清单维度列表
		Map<String,DimensionRolap> rolapMap=new HashMap<String,DimensionRolap>();
		for(DimensionRolap rolap:dimrolaps){
			if(rolap.isUsesign()) rolapMap.put(rolap.getId(), rolap);
		}
		Map<String,Measure> meaMap=new HashMap<String,Measure>();
        for(Measure o:dataset.getCube().getMeasures()){
        	meaMap.put(o.getColumnCode(), o);
        }
		//创建计算用cube表头
		List<CacheHeadCell> headlist=new ArrayList<CacheHeadCell>();
		//cacheMap用于缓存表头在权限和切片影响下维度各个层级取值和排序取值(目的是为了减少计算量)
		CacheMap cacheMap=new CacheMap();
		//有效维度表头计算
		for(CubeHeadCell cell: dataset.getBaseHeadCells()){
			if(cell.getCell_type().equals(CellType.dimension)){
				CacheHeadCell o=null;
				if(cell.getDim_type().equals(DimensionType.crosswise))
					o=CacheHeadCell.createCrossHead(cell, rolapMap.get(cell.getDim()),cacheMap);
				else if(cell.getDim_type().equals(DimensionType.vertical))
					o=CacheHeadCell.createVerticalHead(cell, rolapMap.get(cell.getDim()), meaMap.get(cell.getMea_code()),cacheMap);
				else if(cell.getDim_type().equals(DimensionType.measure))
					o=CacheHeadCell.createMeasureHead(cell, meaMap.get(cell.getMea_code()));
				else throw new ReportException("cell type error.");
				headlist.add(o);
			}
		}
		return headlist;
	}
	/**
	 * 创建CacheKey初始化用值
	 * @param headlist
	 * @param no_crosshead_start
	 * @param rowmap
	 * @return
	 * @throws ReportException 
	 */
	protected CacheKey getCacheKey(List<CacheHeadCell> headlist,int no_crosshead_start,Map<String,Object> rowmap,List<CacheHeadCell> datacotrolheads) throws ReportException{
		
		//未使用的维度的虚拟表头权限判断
		for(CacheHeadCell cell:  datacotrolheads){
			QueryKeyValue keyvalue= cell.translateKeyValue(rowmap.get(cell.getCubemappingkey()).toString()) ;
			if(keyvalue==null) return null;
		}
		
		List<Object> keylist=new ArrayList<Object>();
		List<Integer> sortlist=new ArrayList<Integer>();
		//判断keyvalue为空的情况
		for(int i=0;i<no_crosshead_start;i++ ){
			CacheHeadCell cell=headlist.get(i);
			QueryKeyValue keyvalue= cell.translateKeyValue(rowmap.get(cell.getCubemappingkey()).toString()) ;
			if(keyvalue==null) return null;
			sortlist.add(cell.translateSortValue(keyvalue));
			keylist.add(keyvalue.getId());
		}
		return createCacheKey(keylist,sortlist);
	}
	
	protected CacheKey createCacheKey(List<Object> keylist,List<Integer> sortlist) throws ReportException{
		return new CacheKey(keylist,sortlist);
	}
	/**
	 * CacheKey 值运算
	 * @param valuelist
	 * @param headlist
	 * @param no_crosshead_start
	 * @param rowmap
	 * @return
	 * @throws ReportException 
	 */
	protected List<Double> getCacheValue(Boolean key_sign,List<Double> valuelist,List<CacheHeadCell> headlist,int no_crosshead_start,Map<String,Object> rowmap) throws ReportException{

		for(int i=no_crosshead_start;i<headlist.size();i++){
			CacheHeadCell cell=headlist.get(i);
			double value=0;
			//数据行表头是纵向维度
			if(cell.getDim_type().equals(DimensionType.vertical)){
				//纵向表头存储的值与维度最低层id值逆推的当前层的值一致
				QueryKeyValue keyvalue= cell.translateKeyValue(rowmap.get(cell.getCubemappingkey()).toString());
				if(keyvalue!=null&&cell.getId().toString().equals(keyvalue.getId())){
					value=((Number)rowmap.get(cell.getMea_code())).doubleValue();
				}
			}else {
				//数据行表头是度量
				value=((Number)rowmap.get(cell.getCubemappingkey())).doubleValue();
			}
			if(key_sign){
				valuelist.set(i-no_crosshead_start, cell.getMeagather().gather(valuelist.get(i-no_crosshead_start),value));
			}else
				valuelist.add(value);
		}
		return valuelist;
	}
	/**
	 * CacheKey 值运算
	 * @param valuelist
	 * @param headlist
	 * @param no_crosshead_start
	 * @param rowmap
	 * @return
	 * @throws ReportException 
	 */
	protected List<Double> getCacheValue(CacheKey cachekey,List<CacheHeadCell> headlist,int no_crosshead_start,Map<String,Object> rowmap) throws ReportException{

		List<Double> valuelist=menoryMap.get(cachekey);
		boolean value_exist=true;
		if(valuelist==null) {
			valuelist=new ArrayList<Double>();
			menoryMap.put(cachekey, valuelist);
			value_exist=false;
		}
		for(int i=no_crosshead_start;i<headlist.size();i++){
			CacheHeadCell cell=headlist.get(i);
			double value=0;
			//数据行表头是纵向维度
			if(cell.getDim_type().equals(DimensionType.vertical)){
				//纵向表头存储的值与维度最低层id值逆推的当前层的值一致
				QueryKeyValue keyvalue= cell.translateKeyValue(rowmap.get(cell.getCubemappingkey()).toString());
				if(keyvalue!=null&&cell.getId().toString().equals(keyvalue.getId())){
					value=((Number)rowmap.get(cell.getMea_code())).doubleValue();
				}
			}else {
				//数据行表头是度量
				value=((Number)rowmap.get(cell.getCubemappingkey())).doubleValue();
			}
			if(value_exist){
				valuelist.set(i-no_crosshead_start, cell.getMeagather().gather(valuelist.get(i-no_crosshead_start),value));
			}else
				valuelist.add(value);
		}
		return valuelist;
	}
	/**
	 * 基础文件的读取queryid_base
	 * @param full_cache_id
	 * @return
	 * @throws IOException
	 * @throws ReportException 
	 */
	protected CacheInput createBaseCacheInput(String query_cache_id) throws IOException, ReportException{
		return new FileObjectInputStream(ReportConstants.REP_TEMP_TXT+ query_cache_id+ReportConstants.CACHE_BASE);
	}
	
	/**
	 * 非横向维表头的开始位置索引
	 * @param headlist
	 * @return
	 */
	protected int getNoCrossheadStart(List<CacheHeadCell> headlist){
		int no_crosshead_start=0;
		for(CacheHeadCell cell:headlist){
			if(cell.getDim_type().equals(DimensionType.crosswise)) 	no_crosshead_start++ ;
			else break;
		}
		return no_crosshead_start;
	}
	/**
	 * 从缓存文件中提取转换出cube数据体
	 */
	public DataReader translate(String query_cache_id,AbstractDataSet dataset) throws ReportException {
		CacheInput foi=null;
		try {
			//维度计算用表头
			List<CacheHeadCell> headlist=createCacheHeadCell(dataset);
			//指标关联开始索引
			int no_crosshead_start=this.getNoCrossheadStart(headlist);
			//读取基础计算数据
			foi=this.createBaseCacheInput(query_cache_id);
			//未使用的维度权限控制的虚拟表头
			List<CacheHeadCell> datacotrolheadlist=this.createDataCotrolHead(dataset);
			//基础缓存文件行记录
			Map<String,Object> rowmap=null;
			boolean key_exist=true; 
			while((rowmap=(Map<String, Object>) foi.readObject())!=null){
				//计算key 
				CacheKey cachekey=this.getCacheKey(headlist, no_crosshead_start, rowmap,datacotrolheadlist);
				if(cachekey!=null){	
					//值运算
					this.getCacheValue(cachekey, headlist, no_crosshead_start, rowmap);
					//判断转换后的内存快是否超过1000行，超过则跳出
					if(isMenoryMax()){
						this.translate_sucess=false;
						this.menoryMap.clear();
						return null;
					}
				}
			}
			translate_sucess=true;

			LoggerHelper.debug(this.getClass(),dataset.getAssembleDataSet());
//			int i=1;
//			
//			for(CacheKey key: this.menoryList){
//				System.out.print("\t"+i);
//				for(Object o: key.getKeys())
//					System.out.print("\t"+o);
//				for(Double o: this.menoryMap.get(key))
//					System.out.print("\t"+o);
//				System.out.println();
//				i++;
//			}
//			System.out.println("debug end.");
			
			return  new DR();
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		}finally{
			if(foi!=null){
				try {
					foi.close();
				} catch (Exception e) {
				}
			}
				
		}
	}
	/**
	 * 是否达到内存限额
	 * @return
	 * @throws ReportException 
	 */
	protected boolean isMenoryMax() throws ReportException{
		return menoryMap.size()>memory_row_max?true:false;
	}
	
	public boolean isTranslateSucess() {
		return translate_sucess;
	}
	
	class DR implements DataReader{
		private int dr_idx=0;
		private int dr_max=-1;
		private List<CacheKey> menoryList=null;
		
		public DR(){
			menoryList=new ArrayList<CacheKey>(menoryMap.size());
		}
		
		public void close() throws ReportException {
			if(menoryList!=null) 
				menoryList.clear();
			menoryMap.clear();
		}

		public Object getObject(int i) throws ReportException {
			return menoryList.get(dr_idx).getKeys().get(i-1);
		}

		public String getString(int i) throws ReportException {
			return menoryList.get(dr_idx).getKeys().get(i-1).toString();
		}

		public boolean next() throws ReportException {
			if(dr_max<0)
				throw new ReportException("DataReader is hasnot open.");
			dr_idx++;
			if(dr_idx<dr_max){
				CacheKey cachekey=menoryList.get(dr_idx);
				cachekey.getKeys().addAll(menoryMap.get(cachekey));
				return true;
			}else 
				return false;
		}
		/**
		 * 转换成正确的格式
		 */
		public void open() throws ReportException {
			if(translate_sucess){
				dr_idx=-1;
				dr_max=menoryMap.size();
				Iterator<CacheKey> it=menoryMap.keySet().iterator();
				while(it.hasNext()){
					this.menoryList.add(it.next());
				}
				//执行排序
				Collections.sort(menoryList);
				//限制只能打开一遍
				translate_sucess=false;
			}else
				throw new ReportException("cannot open again.");
		}
	}
	
	protected Map<CacheKey, List<Double>> getMenoryMap() {
		return menoryMap;
	}

}
