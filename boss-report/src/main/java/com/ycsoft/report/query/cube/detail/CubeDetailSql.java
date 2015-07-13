package com.ycsoft.report.query.cube.detail;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.BeanHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepDetailData;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.cube.CubeDataSet;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.key.Impl.ConKeyValue;

/**
 * cube数据明细查询sql组装实现
 */
public class CubeDetailSql implements CubeDetail {
	/**
	 * 横向维表头转换
	 * 通过装载的data值ID的表头，生成报表条件key
	 * CubeCell 是对应表头属性，其中id被装入对应数据行值
	 * @throws ReportException 
	 */
	public ConKeyValue createRepKeyDto(CubeHeadCell headcell) throws ReportException{
		if(headcell==null)
			throw new ReportException("headcell is null");
		if(!DimensionType.crosswise.equals(headcell.getDim_type()) && !DimensionType.vertical.equals(headcell.getDim_type()))
			throw new ReportException("headcell is not dim");
		
		String sql= assembleDimSqlById(DimensionManage.getDimension(headcell.getDim()),headcell.getLevel(),headcell.getId());
		
		ConKeyValue rkd=new ConKeyValue();
		rkd.setKey(DimensionManage.getDimension(headcell.getDim()).getPrefixid());
		rkd.setValue(sql);
		return rkd;
	}
	
	/**
	 * 组装一个维的值表达式
	 */
	public String assembleDimSqlById(Dimension dim,int level,Object id){
		if(level>dim.getLevelNum()) level=dim.getLevelNum();
		StringBuilder sqlbuffer=new StringBuilder();
		sqlbuffer.append("select ")
		.append(dim.getLevel(dim.getLevelNum()).getColumn_code())
		.append(" from ").append(dim.getTabel()).
		append(" where ").append(dim.getLevel(level).getColumn_code())
		.append("='").append(id.toString()).append("'");
		return sqlbuffer.toString();
	}


	public List<ConKeyValue> createCubeDetailKeys(CubeExec cube,List<CubeHeadCell> headdatacells) throws ReportException {
		//Map<Dimension,>
		//判断headdatacells有效组装map
		Map<Dimension,CubeHeadCell> dimcellMap=new HashMap<Dimension,CubeHeadCell>();
		if(headdatacells!=null){
			for(CubeHeadCell cell:headdatacells){
				//cell的值非空,表示有效的计算单元格
				if(cell.getId() != null && StringHelper.isNotEmpty(cell.getId().toString())){
					if(cell.getLevel()==null)
						throw new ReportException("headdatacell.level is null");
					Dimension dim=DimensionManage.getDimension(cell.getDim());
					if(dim==null)
						throw new ReportException("headdatacell.dim is null or undefined.");
					if(dimcellMap.containsKey(dim)){
						//相同的维取最大的层
						if(cell.getLevel()>dimcellMap.get(dim).getLevel())
							dimcellMap.put(dim, cell);
					}else{
						dimcellMap.put(dim, cell);
					}
				}
			}
		}
		
		List<ConKeyValue> keylist=new ArrayList<ConKeyValue>();
		
		for(DimensionRolap dimrolap: cube.getDimensionRolaps()){
			if(dimcellMap.containsKey(dimrolap.getDim())){
				keylist.add(this.createRepKeyDto(dimcellMap.get(dimrolap.getDim())));
			}else{
				ConKeyValue rkd=new ConKeyValue();
				rkd.setKey(dimrolap.getDim().getPrefixid());
				rkd.setValue("");
				keylist.add(rkd);
			}
		}
		return keylist;
	}

	public List<ConKeyValue> createTestKeys(CubeExec cube) {
		List<ConKeyValue> list=new ArrayList<ConKeyValue>();
		for(DimensionRolap rolap: cube.getDimensionRolaps()){
			ConKeyValue dto=new ConKeyValue();
			dto.setKey(rolap.getDim().getPrefixid());
			dto.setValue(this.assembleDimSqlById(rolap.getDim(), rolap.getDim().getLevelNum(), "1"));
			list.add(dto);
		}
		return list;
	}
	
	
	public RepDetailData customDimData(List<CubeHeadCell> headdatacells,
			Map<Dimension, Integer> detaildimmap) throws ReportException {
		//判断headdatacells有效组装map
		Map<Dimension,CubeHeadCell> dimcellMap=new HashMap<Dimension,CubeHeadCell>();
		if(headdatacells!=null){
			for(CubeHeadCell cell:headdatacells){
				//cell的值非空,表示有效的计算单元格
				if(cell.getId() != null && StringHelper.isNotEmpty(cell.getId().toString())){
					if(cell.getLevel()==null)
						throw new ReportException("headdatacell.level is null");
					Dimension dim=DimensionManage.getDimension(cell.getDim());
					if(dim==null)
						throw new ReportException("headdatacell.dim is null or undefined.");
					if(dimcellMap.containsKey(dim)){
						//相同的维取最大的层
						if(cell.getLevel()>dimcellMap.get(dim).getLevel())
							dimcellMap.put(dim, cell);
					}else{
						dimcellMap.put(dim, cell);
					}
				}
			}
		}
		
		//dim位置赋值
		RepDetailData data=new RepDetailData();
		for(Dimension dim:detaildimmap.keySet()){
			//判断定义的维是否哎坐标表头中都存在
			if(!dimcellMap.containsKey(dim))
				throw new ReportException("detailcustom."+dim.getId()+" is undefined.");
			
			Integer idx=detaildimmap.get(dim);
			try {
				BeanHelper.setPropertyString(data,"dim"+idx,dimcellMap.get(dim).getId());
			} catch (Exception e) {
				throw new ReportException(e);
			}
		}
		return data;
	}

	public String customQuerySql(List<CubeHeadCell> headdatacells,
			Map<Dimension, Integer> detaildimmap) throws ReportException {
		//判断headdatacells有效组装map
		Map<Dimension,CubeHeadCell> dimcellMap=new HashMap<Dimension,CubeHeadCell>();
		if(headdatacells!=null){
			for(CubeHeadCell cell:headdatacells){
				//cell的值非空,表示有效的计算单元格
				if(cell.getId() != null && StringHelper.isNotEmpty(cell.getId().toString())){
					if(cell.getLevel()==null)
						throw new ReportException("headdatacell.level is null");
					Dimension dim=DimensionManage.getDimension(cell.getDim());
					if(dim==null)
						throw new ReportException("headdatacell.dim is null or undefined.");
					if(dimcellMap.containsKey(dim)){
						//相同的维取最大的层
						if(cell.getLevel()>dimcellMap.get(dim).getLevel())
							dimcellMap.put(dim, cell);
					}else{
						dimcellMap.put(dim, cell);
					}
				}
			}
		}
		
		StringBuilder customsql=new StringBuilder();
		for(Dimension dim: dimcellMap.keySet()){
			Integer idx= detaildimmap.get(dim);
			if(idx==null)
				throw new ReportException("detailcustom."+dim.getId()+" is undefined.");
			customsql.append(" and dim").append(idx).append(" in (")
			.append(this.createRepKeyDto(dimcellMap.get(dim)).getValue())
			.append(")");
		}
		return customsql.toString();
	}
}
