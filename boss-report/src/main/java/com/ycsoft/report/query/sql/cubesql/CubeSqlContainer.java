package com.ycsoft.report.query.sql.cubesql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionLevel;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.MeasureGather;
import com.ycsoft.report.query.cube.impl.CubeHeadCellImpl;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

public class CubeSqlContainer implements AnalyseCubeSql {
	public static final String table_as = "cube";// 数据体别名
	public static final String comma = ",";
	public static final String and = " and ";
	
	private StringBuilder select = new StringBuilder();
	private StringBuilder from = new StringBuilder();
	private StringBuilder where = new StringBuilder();
	private StringBuilder group = new StringBuilder();
	private StringBuilder orderby = new StringBuilder();
	
	private List<CubeHeadCell> headcells=new ArrayList<CubeHeadCell>();//表头

	public CubeHeadCell[] getBaseHeadCells(){
		return headcells.toArray(new CubeHeadCell[headcells.size()]);
	}
	/**
	 * 获得组装分析的sql
	 */
	public String getAnalyseSql(String sql) {
		StringBuilder sqlbuffer = new StringBuilder();
		if (select.length() > comma.length()) {
			sqlbuffer.append("select ").append(
					select.substring(0, 
							select.length()- comma.length()));
		} else {
			return sql;
		}
		sqlbuffer.append("\n from ").append("(").append(sql).append(") ")
				.append(table_as);
		if (from.length() > comma.length())
			sqlbuffer.append(comma).append(
					from.substring(0, from.length() - comma.length()));
		if (where.length() > and.length())
			sqlbuffer.append("\n where ").append(
					where.substring(0, where.length() - and.length()));
		if (group.length() > comma.length())
			sqlbuffer.append("\n group by ").append(
					group.substring(0, group.length() - comma.length()));
		if (orderby.length() > comma.length())
			sqlbuffer.append("\n order by ")
					.append(orderby.substring(0, 
							orderby.length() - comma.length()));
		return sqlbuffer.toString();

	}

	/**
	 * 装载横向维
	 * 参数isgroup表示是否分组合计
	 * 参数warn 表示指标警戒条件(指标数值变色)
	 * @param crossDim
	 * @param dimlevel
	 */
	protected void appendSelect(Dimension crossDim, DimensionLevel dimlevel,Boolean isgroup) {
		
		if(StringHelper.isEmpty(crossDim.getTabel())){
			//rep_dimension未定义from_table
			select.append(crossDim.getId()).append("_").append(dimlevel.getLevel()).append(".").append(
					dimlevel.getColumn_code()).append(comma);
			//select.append(crossDim.getId()).append("_").append(dimlevel.getLevel()).append(".").append(
			//		dimlevel.getColumn_text()).append(comma);
		}else{
			//ID NAME 明细关系需要
			select.append(crossDim.getId()).append(".").append(
					dimlevel.getColumn_code()).append(comma);
			//select.append(crossDim.getId()).append(".").append(
					//dimlevel.getColumn_text()).append(comma);
		}
		
		CubeHeadCellImpl cell=new CubeHeadCellImpl();
		cell.setName(dimlevel.getName());
		cell.setDim(crossDim.getId());
		cell.setColspan(1);
		cell.setRowspan(1);
		cell.setDim_type(DimensionType.crosswise);
		cell.setLevel(dimlevel.getLevel());
		cell.setCell_type(CellType.dimension);
		this.headcells.add(cell);
	}

	public void appendSelect(DimensionRolap dimcon) {
		for (int i =1;i<=dimcon.getLevel() ; i++) {
			appendSelect(dimcon.getDim(), dimcon.getDim().getLevel(i),dimcon.isLevelTotal(i));
		}
	}

	/**
	 * 无纵向维装载度量
	 * 
	 * @param measures
	 */
	public void appendSelect(List<Measure> measures) {
		for (Measure mea : measures){
			// sum(cube.字段) 说明,
			select.append(mea.getCalculation()).append("(")
					.append(table_as).append(".").append(
							mea.getColumnCode()).append(")").append(" ")
					.append(mea.getColumnText()).append(comma);
			
			CubeHeadCellImpl cell=new CubeHeadCellImpl();
			cell.setId(mea.getColumnCode());
			cell.setName(mea.getColumnText());
			//cell.setDim(crossDim.getId());
			cell.setColspan(1);
			cell.setRowspan(1);
			cell.setDim_type(DimensionType.measure);
			//cell.setLevel(dimlevel.getLevel());
			cell.setCell_type(CellType.dimension);
			cell.setMea_detail_id(mea.getMeaRepId());
			cell.setMea_datatype(mea.getDateType());
			cell.setMea_custom(mea.getMeaCustom());
			cell.setMea_code(mea.getColumnCode());
			this.headcells.add(cell);
		}
	}

	/**
	 * 设置纵向维和度量
	 * 
	 * @throws ReportException
	 */
	public void appendSelect(DimensionRolap dimcon,
			List<Measure> defalutMeasures, String pid, int level,boolean isgraph)
			throws ReportException {
		//System.out.println(level+":层"+pid+":开始计算");
		for (QueryKeyValue vo : dimcon.queryLevelValue(level)) {
			if(StringHelper.isEmpty(pid) || pid.equals(vo.getPid())){
				if (dimcon.getLevel() > level) {
					appendSelect(dimcon,defalutMeasures,vo.getId(),level+1,isgraph);
				} else {
					//System.out.println(level+":层"+pid+":记录"+vo.getId());
					// sum(case when cube.字段=id then 度量 else 0/null end)
					for(Measure mea:defalutMeasures){
						select.append(mea.getCalculation()).append("(").append(
						" case when ").append(dimcon.getDim().getId()).append(".").append(
						dimcon.getDim().getLevel(dimcon.getLevel())
								.getColumn_code()).append("='").append(
						vo.getId()).append("' then ").append(table_as)
						.append(".").append(mea.getColumnCode())
						.append(" else ").append(
								mea.getCalculation().equals(
										MeasureGather.SUM) ? "0" : "null").append(
								" end) \"").append(vo.getName().substring(0, vo.getName().length()>6?6:vo.getName().length())).append(mea.getColumnText()).append("\"").append(
								comma);
			
						CubeHeadCellImpl cell=new CubeHeadCellImpl();
						cell.setId(vo.getId());
						cell.setPid(defalutMeasures.size()==1?vo.getPid():vo.getId());
						//多指标时，使用指标名称
						cell.setName(defalutMeasures.size()==1? vo.getName():mea.getColumnText());
						cell.setColspan(1);
						cell.setRowspan(1);
						cell.setCell_type(CellType.dimension);
						cell.setDim(dimcon.getDim().getId());
						cell.setDim_type(DimensionType.vertical);
						//多个指标时，level+1,目的是为cube计算表头时提供表头层数依据
						cell.setLevel(defalutMeasures.size()==1?level:level+1);
						cell.setMea_detail_id(mea.getMeaRepId());
						cell.setMea_code(mea.getColumnCode());
						cell.setMea_datatype(mea.getDateType());
						cell.setMea_custom(mea.getMeaCustom());
						this.headcells.add(cell);
					}
				}
			}
		}
		//循环结束插入判断有无合计头，有则插入合计头
		if(dimcon.isLevelTotal(level)&&!isgraph){
			for(Measure mea:defalutMeasures){
				CubeHeadCellImpl cell=new CubeHeadCellImpl();
				cell.setPid(pid);
				cell.setName(defalutMeasures.size()==1?"合计": mea.getColumnText());//多个指标时，使用指标名称
				cell.setColspan(1);
				cell.setRowspan(1);
				cell.setCell_type(CellType.group);
				cell.setDim(dimcon.getDim().getId());
				cell.setDim_type(DimensionType.vertical);
				//多个指标时，level+1,目的是为cube计算表头时提供表头层数依据
				cell.setLevel(defalutMeasures.size()==1?level:level+1);
				cell.setMea_detail_id(mea.getMeaRepId());
				cell.setMea_code(mea.getColumnCode());
				cell.setMea_datatype(mea.getDateType());
				cell.setMea_custom(mea.getMeaCustom());
				this.headcells.add(cell);
			}
		}
	}

	/**
	 * 维度表
	 * 
	 * @param crossDim
	 */
	public void appendFrom(Dimension dim) {
		if(StringHelper.isEmpty(dim.getTabel())){
			for(int i=1;i<=dim.getLevelNum();i++){
				DimensionLevel level=dim.getLevel(i);
				from.append(level.getColumn_table()).append(" ").append(dim.getId()).append("_").append(i).append(
						comma);
			}
		}else{
		from.append(dim.getTabel()).append(" ").append(dim.getId()).append(
				comma);
		}
	}

	/**
	 * 装载横向维
	 * 
	 * @param crossDim
	 * @throws ReportException 
	 */
	public void appendWhere(DimensionRolap dimcon) throws ReportException {
		if(StringHelper.isEmpty(dimcon.getDim().getTabel())){
			where.append(dimcon.getDim().getId()).append("_").append(dimcon.getDim().getLevelNum()).append(".").append(
					dimcon.getColumnMappingKey()).append("=").append(table_as)
					.append(".").append(dimcon.getCubeMappingKey()).append(and);
			for(int i=1;i<dimcon.getDim().getLevelNum();i++){
				where.append(dimcon.getDim().getId()).append("_").append(i).append(".")
				.append(dimcon.getDim().getLevel(i).getColumn_code()).append("=")
				.append(dimcon.getDim().getId()).append("_").append(i+1).append(".")
				.append(dimcon.getDim().getLevel(i+1).getColumn_pid()).append(and);
			}
			
		}else{
			where.append(dimcon.getDim().getId()).append(".").append(
				dimcon.getColumnMappingKey()).append("=").append(table_as)
				.append(".").append(dimcon.getCubeMappingKey()).append(and);
		}
		//切片处理
		if (dimcon.getSlices_level()!=null
				&&dimcon.getSlices_value()!=null
				&&dimcon.getSlices_value().length>0){
			if(StringHelper.isEmpty(dimcon.getDim().getTabel())){
				where.append(dimcon.getDim().getId()).append("_").append(dimcon.getSlices_level()).append(".").append(
						dimcon.getDim().getLevel(dimcon.getSlices_level())
								.getColumn_code()).append(" in ('");
			}else{
			where.append(dimcon.getDim().getId()).append(".").append(
					dimcon.getDim().getLevel(dimcon.getSlices_level())
							.getColumn_code()).append(" in ('");
			}
			for(int i=0;i<dimcon.getSlices_value().length;i++){
				where.append(dimcon.getSlices_value()[i]);
				if(i<dimcon.getSlices_value().length-1)
					where.append("','");
			}
			
		    where.append("')").append(and);
		}
		//权限控制处理
		Map<DimensionLevel, List<QueryKeyValue>> levelcontrolMap= dimcon.getDim().getDimLevelControlMap();
		for(DimensionLevel level: levelcontrolMap.keySet()){
			if(StringHelper.isEmpty(dimcon.getDim().getTabel())){
				where.append(dimcon.getDim().getId()).append("_").append(level.getLevel()).append(".")
			     .append(level.getColumn_code()).append(" in ('");
			}else{
				where.append(dimcon.getDim().getId()).append(".")
				     .append(level.getColumn_code()).append(" in ('");
			}
			List<QueryKeyValue> valuelist=levelcontrolMap.get(level);
			if(valuelist==null||valuelist.size()==0)
				throw new ReportException("数据权限异常，请联系管理员!");
			for(int i=0;i<valuelist.size();i++){
				where.append(valuelist.get(i).getId());
				if(i<valuelist.size()-1)
					where.append("','");
			}
			where.append("')").append(and);
		}
	}

	/**
	 * 装载横向维的group
	 * 
	 * @param crossDim
	 * @param dimlevel
	 */
	private void appendGroup(Dimension crossDim, DimensionLevel dimlevel) {
		if(StringHelper.isEmpty(crossDim.getTabel())){
			group.append(crossDim.getId()).append("_").append(dimlevel.getLevel()).append(".").append(
					dimlevel.getColumn_code()).append(comma);
			//group.append(crossDim.getId()).append("_").append(dimlevel.getLevel()).append(".").append(
			//		dimlevel.getColumn_text()).append(comma);
		}else{
			group.append(crossDim.getId()).append(".").append(
					dimlevel.getColumn_code()).append(comma);
			//group.append(crossDim.getId()).append(".").append(
			//		dimlevel.getColumn_text()).append(comma);
		}
	}

	/**
	 * 多级装载横向维
	 * 
	 * @param dimcon
	 */
	public void appendGroup(DimensionRolap dimcon) {
		
		for (int i =1;i<=dimcon.getLevel() ; i++) {
			appendGroup(dimcon.getDim(), dimcon.getDim().getLevel(i));
		}
	}

	private void appendOrderBy(Dimension crossDim, DimensionLevel dimlevel) {
		if(StringHelper.isEmpty(crossDim.getTabel())){
			orderby.append(crossDim.getId()).append("_").append(dimlevel.getLevel()).append(".").append(
					dimlevel.getColumn_code()).append(comma);
		}else{
			orderby.append(crossDim.getId()).append(".").append(
				dimlevel.getColumn_code()).append(comma);
		}
	}

	public void appendOrderBy(DimensionRolap dimcon) {
		
		for (int i =1;i<=dimcon.getLevel() ; i++) {
			appendOrderBy(dimcon.getDim(), dimcon.getDim().getLevel(i));
		}
	}
}
