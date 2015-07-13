package com.ycsoft.report.query.cube.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeDataSet;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.graph.CubeGraph;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * cube实现类
 * 
 */
public class Cube implements CubeExec {
	
	/**
	 * 横向维清单
	 */
	private List<Dimension> crosswiseDimensions = new ArrayList<Dimension>();;
	/**
	 * 纵向维清单
	 */
	private List<Dimension> verticalDimensions = new ArrayList<Dimension>();

	/**
	 * 维容器
	 */
	Map<Dimension, DimensionRolap> dimconMap = new HashMap<Dimension, DimensionRolap>();
	/**
	 * 有效度量清单
	 */
	private List<Measure> measures = new ArrayList<Measure>();
	
	private Map<String,Measure> meaMap=new HashMap<String,Measure>();
	
	/**
	 * 表头属性
	 */
	private CubeHeadCell[] headcells;

	public Cube(List<RepCube> repcubelist) throws ReportException {
		for (RepCube repcube : repcubelist) {
			if (repcube.getColumn_type().equals(DimensionType.crosswise.name())) {
				DimensionContainer dimcon = new DimensionContainer(repcube);
				dimconMap.put(dimcon.getDim(), dimcon);
				crosswiseDimensions.add(dimcon.getDim());
			} else if (repcube.getColumn_type().equals(DimensionType.vertical.name())) {
				DimensionContainer dimcon = new DimensionContainer(repcube);
				dimconMap.put(dimcon.getDim(), dimcon);
				verticalDimensions.add(dimcon.getDim());
			} else if(repcube.getColumn_type().equals(DimensionType.measure.name())){
				Measure mea = new MeasureImpl(repcube);
				measures.add(mea);
				this.meaMap.put(mea.getColumnCode(), mea);
			} else{
				throw new ReportException(repcube.getColumn_type()+": rep_cube.column_type is undefined.");
			}
		}
		
	}

	/**
	 * cube执行计算得到Olap数据体
	 */
	public CubeDataSet execute(CubeDataSet t) throws ReportException {
		t.setCube(this);
		t.assembleCubeDataSet();
		this.headcells=t.getBaseHeadCells();
		t.execute();
		return t;
	}

	/**
	 * 图形计算
	 */
	public CubeGraph executeGraph(CubeDataSet t, CubeGraph graph) throws ReportException {
		//标题等设置
		Measure mea= this.meaMap.get(graph.getMeasure());
		if(mea==null)
			throw new ReportException(graph.getGraphType().getDesc()+":mea is undefind");
		graph.getGraphData().setYtitle(mea.getColumnText());
		
		StringBuilder subtitle=new StringBuilder();
		subtitle.append("维:");
		if(graph.getCrossDimension()!=null){
			DimensionRolap rolap= this.dimconMap.get(graph.getCrossDimension());
			if(rolap==null)
				throw new ReportException(graph.getCrossDimension().getId()+" is undefind in cube.");
			
			String level_name=rolap.getDim().getLevel(rolap.getLevel()).getName();
			subtitle.append(rolap.getName()).append("(").append(level_name).append("),");
			graph.getGraphData().setXtitle(level_name);			
		}
		if(graph.getVertDimension()!=null){
			DimensionRolap rolap= this.dimconMap.get(graph.getVertDimension());
			if(rolap==null)
				throw new ReportException(graph.getVertDimension().getId()+" is undefind in cube.");
			String level_name=rolap.getDim().getLevel(rolap.getLevel()).getName();
			subtitle.append(rolap.getName()).append("(")
				.append(level_name).append("),");
			graph.getGraphData().setXtitle(level_name);
		}
		subtitle.append("指标:").append(mea.getColumnText()).append("_").append(mea.getCalculation());
		graph.getGraphData().setSubtitle(subtitle.toString());
		
		//组装图形数据源
		t.assembleGraphDataSet(graph);
		return t.graph(graph);
	}
	
	
	/**
	 * cube预览，主要是提供Olap表头效果查看
	 */
	public List<CubeHeadCell[]> preview(List<DimensionRolap> dimrolaps,List<Measure> mealist) throws ReportException {
		//CubeSqlContainer sqlcon=assembleSql(dimrolaps,mealist);
		//return this.createOlapHead(sqlcon.getBaseHeadCells(), dimrolaps,mealist);
		return null;
	}
	
	/**
	 * 验证cube的配置是否正确
	 * @throws ReportException 
	 */
	public String validate(CubeDataSet t) throws ReportException {
		if(this.measures.size()<=0) return "error:no measure.";
		if(this.verticalDimensions.size()>1) return "error:verticalDimension >1 ";
		t.setCube(this);
		t.assembleCubeDataSet();
		return t.validate();
	}
	

	public List<DimensionRolap> getDimensionRolaps() {
		List<DimensionRolap> list=new ArrayList<DimensionRolap>();
		for(Dimension dim:this.crosswiseDimensions){
			list.add(this.dimconMap.get(dim));
		}
		for(Dimension dim:this.verticalDimensions){
			list.add(this.dimconMap.get(dim));
		}
		for(DimensionRolap dimcon:  this.dimconMap.values()){
			if(!dimcon.isUsesign())
				list.add(dimcon);
		}
		return list;
	}

	public List<Measure> getMeasures() {
		return this.measures;
	}


	
	/**
	 * 展开一个维度
	 * level+1
	 */
	public void expandDimension(Dimension dim) {
		DimensionContainer dimrolap= (DimensionContainer)this.dimconMap.get(dim);
		if(dimrolap!=null&&dimrolap.getLevel()<dimrolap.getDim().getLevelNum()){
			dimrolap.setLevel(dimrolap.getLevel()+1);
		}
	}
	/**
	 * 收缩一个维度
	 * level-1
	 */
	public void shrinkDimension(Dimension dim) {
		DimensionContainer dimrolap= (DimensionContainer)this.dimconMap.get(dim);
		if(dimrolap!=null&&dimrolap.getLevel()>1){
			dimrolap.setLevel(dimrolap.getLevel()-1);
		}
	}
	
	
	/**
	 * 维度切片
	 * 指定层切片
	 * @throws ReportException 
	 */
	public void slicesDimension(Dimension dim,Integer level,String...values) throws ReportException {
		DimensionContainer dimcon=(DimensionContainer)this.dimconMap.get(dim);
		if(dimcon==null)
			throw new ReportException("dim is null or undefined in cube.");
		if(values==null||values.length==0){
			dimcon.setSlices_level(null);
			dimcon.setSlices_value(null);
		}else{
			for(String o:values){
				if(o==null||o.equals(""))
					throw new ReportException(o+":values[i] is null");
				if(!DimensionLevelValueManage.getDimLevelValueMap(dimcon.getDim().getLevel(level)).containsKey(o)){
					throw new ReportException(o+":values[i] is not in dimlevel_valuelist.");
				}
			}
			dimcon.setSlices_level(level);
			dimcon.setSlices_value(values);
		}
	}
	/**
	 * 维度层级自定义排序
	 */
	public void sortDimension(Dimension dim, Map<Integer,String[]> sortmap)throws ReportException {
		DimensionContainer dimcon=(DimensionContainer)this.dimconMap.get(dim);
		if(dimcon==null)
			throw new ReportException("dim is null or undefined in cube.");
		dimcon.setLevel_sort_map(sortmap);
	}
	
	public DimensionRolap getDimensionRolap(Dimension dim) {
		return this.dimconMap.get(dim);
	}

	public void selectDimension(Dimension vertdim,Dimension... dims) throws ReportException {
		Map<Dimension,Boolean> map=new HashMap<Dimension,Boolean>();
		if(dims!=null&&dims.length>0){
			for(Dimension dim:dims){
				map.put(dim, false);
				if(!this.dimconMap.containsKey(dim))
					throw new ReportException(dim.getId()+": dim is null or undefiend in cube");
				
			}
			this.crosswiseDimensions.clear();
			for(Dimension dim:dims){
				if(!dim.equals(vertdim))
					this.crosswiseDimensions.add(dim);
			}
		}else{
			this.crosswiseDimensions.clear();
		}
		
		if(vertdim!=null){
			if(!this.dimconMap.containsKey(vertdim))
				throw new ReportException(vertdim.getId()+": dim is undefiend in cube");
			map.put(vertdim, true);
			this.verticalDimensions.clear();
			this.verticalDimensions.add(vertdim);
		}else{
			this.verticalDimensions.clear();
		}
		
		for(DimensionRolap o: this.dimconMap.values()){
			DimensionContainer con=(DimensionContainer)o;
			if(map.containsKey(con.getDim())){
				con.setUsesign(true);
				con.setVerticalsign(map.get(o.getDim()));
			}else{
				con.setUsesign(false);
				con.setVerticalsign(false);
				//不使用的维度，清空切片内容
				this.slicesDimension(con.getDim(),null);
			}
		}
	}

	/**
	 * 顺序 使用+未使用
	 */
	public List<Measure> getDefaultMeasures() {
		List<Measure> list=new ArrayList<Measure>();
		Map<String,String> map=new HashMap<String,String>();
		list.addAll(this.measures);
		for(Measure mea:this.measures){
			map.put(mea.getColumnCode(), null);
		}
		for(String key:this.meaMap.keySet()){
			if(!map.containsKey(key)){
				list.add(this.meaMap.get(key));
			}
		}
		return list;
	}

	public void selectMeasure(String... meas) throws ReportException {
		if(meas==null||meas.length==0)
			throw new ReportException("error:meas.size<1");
		List<Measure> list=new ArrayList<Measure>();
		for(String mea:meas){
			if(this.meaMap.containsKey(mea)){
				list.add(this.meaMap.get(mea));
			}else{
				throw new ReportException(mea+":mea is undefined in cube.");
			}
		}
		this.measures=list;
	}

	public void configDimensionTotal(Dimension dim, Integer... levels) throws ReportException {
		DimensionContainer rolap=(DimensionContainer) this.dimconMap.get(dim);
		if(rolap==null)
			throw new ReportException("dim is null or undefined in cube");
		if(levels!=null&&levels.length>0){
			for(Integer o:levels){
				if(o==null)
					throw new ReportException("level is null");
				if(o>rolap.getDim().getLevelNum()||o<1)
					throw new ReportException("level>dim.levelnum or level<1.");
			}
		}
		
		rolap.setLevelTotal(levels);
	}
	
	public List<CubeHeadCell[]> getCubeHead() throws ReportException {
		return createOlapHead(this.headcells,this.getDimensionRolaps(),this.measures);
	}
	
	/**
	 * 创建维度表头层
	 * 最外层，最高层表头
	 * @param headcells
	 * @return
	 */
	private CubeHeadCell[] createDimHead(CubeHeadCell[] headcells){
		List<CubeHeadCell> dimcelllist=new ArrayList<CubeHeadCell>();
		CubeHeadCellImpl dimcell=null;
		for(CubeHeadCell cell:headcells){
			if(dimcell==null||StringHelper.isEmpty(cell.getDim())||!dimcell.getDim().equals(cell.getDim())){
				//维不同
				dimcell=new CubeHeadCellImpl(cell);
				dimcell.setId(null);
				dimcell.setName(dimcell.getDim()==null?"":DimensionManage.getDimension(dimcell.getDim()).getName());
				dimcell.setRowspan(1);
				/**
				 * 判断哪些按钮要显示
				 */
				if(StringHelper.isEmpty(dimcell.getDim())){
					dimcell.setShow_expand(false);
					dimcell.setShow_shrink(false);
					dimcell.setShow_slices(false);
				}else{
					dimcell.setShow_expand(true);
					dimcell.setShow_shrink(true);
					dimcell.setShow_slices(true);
					dimcell.setShow_sort(true);
					DimensionRolap rolap=this.dimconMap.get(DimensionManage.getDimension(dimcell.getDim()));
					if(rolap.getLevel()==1)
						dimcell.setShow_shrink(false);
					if(rolap.getLevel()==rolap.getDim().getLevelNum())
						dimcell.setShow_expand(false);
				}				
				dimcelllist.add(dimcell);
			}else {
				//维相同
				dimcell.setColspan(dimcell.getColspan()+cell.getColspan());
			}
		}
		return dimcelllist.toArray(new CubeHeadCell[dimcelllist.size()]);
	}
	/**
	 * 递归生成完整表头
	 * 表头类型：横向维 层头(dim_type=crosswise);
	 *         纵向维 数据头(dim_type=vertical);
	 *         纵向维 合计头(dim_type=vertical,cell_type=group,level=哪层分组合计);
	 *         指标维 头(dim_type=measures）不会处理到
	 * 1.存在度量维头 返回
	 * 2.存在纵向维头，如果pid=null 返回
	 * 3.多个指标判断
	 * 
	 * 参数 level_index 第几层表头(不含维度层)  level_index<=1退出
	 * 参数 headcells 当前表头行
	 * 返回 CubeHeadCell[] 上一级表头行
	 * @param headcells
	 * @return
	 */
	private void recursionCreateHead(List<CubeHeadCell[]> headlist,CubeHeadCell[] headcells,int level_index,Dimension verticalDim){
		if(level_index<=1){
			//表头层计算结束，套上维度头层
			headlist.add(this.createDimHead(headcells));
			return;
		}
		//计算目标层 表头
		List<CubeHeadCell> nextlist=new ArrayList<CubeHeadCell>();
		//计算数据头表头取值map
		Map<String,QueryKeyValue> verticalnextmap=DimensionLevelValueManage.getDimLevelValueMap(verticalDim.getLevel(level_index-1));
		CubeHeadCellImpl nextcell=null;
		Map<Object,String> vertmap=new HashMap<Object,String>();
		Map<Integer,Map<Object,String>> vertgroupmap=new HashMap<Integer,Map<Object,String>>();
		for(CubeHeadCell cell:headcells){
			if(DimensionType.crosswise.equals(cell.getDim_type())){
				//横向维 层头
				nextcell=new CubeHeadCellImpl(cell);
				nextcell.setRowspan(nextcell.getRowspan()+1);
				cell.setRowspan(0);
				cell.setColspan(0);
				nextlist.add(nextcell);
			}else if(CellType.group.equals(cell.getCell_type())){
				//纵向维 合计头
				if(level_index==cell.getLevel()){
					//当前分组合计
					nextcell.setColspan(nextcell.getColspan()+cell.getColspan());
				}else{
					//非当前 行和列合并
					if(!vertgroupmap.containsKey(cell.getLevel())){
						vertgroupmap.put(cell.getLevel(), new HashMap<Object,String>());
					}
					if(vertgroupmap.get(cell.getLevel()).containsKey(cell.getPid())){
						//列合并
						nextcell.setColspan(nextcell.getColspan()+cell.getColspan());
					}else{
						vertgroupmap.get(cell.getLevel()).put(cell.getPid(), null);
						nextcell=new CubeHeadCellImpl(cell);
						nextcell.setName("合计");
						//行合并
						nextcell.setRowspan(nextcell.getRowspan()+1);
						nextlist.add(nextcell);
					}
					cell.setRowspan(0);
					cell.setColspan(0);
				}
			}else{
				//纵向维 数据头
				if(vertmap.containsKey(cell.getPid())){
					nextcell.setColspan(nextcell.getColspan()+cell.getColspan());
				}else{
					vertmap.put(cell.getPid(), null);
					nextcell=new CubeHeadCellImpl(cell);
					QueryKeyValue vo=verticalnextmap.get(cell.getPid().toString());
					nextcell.setId(vo.getId());
					nextcell.setName(vo.getName());
					nextcell.setPid(vo.getPid());
					nextlist.add(nextcell);
				}
			}
		}
		
		CubeHeadCell[] nextcells=nextlist.toArray(new CubeHeadCell[nextlist.size()]);
		recursionCreateHead(headlist,nextcells,level_index-1,verticalDim);
		headlist.add(nextcells);
	}
	
	/**
	 * 组装多级报表头
	 * 列隐藏未处理和能否隐藏未处理
	 * @throws ReportException 
	 */
	private List<CubeHeadCell[]> createOlapHead(CubeHeadCell[] headcells,List<DimensionRolap> dimrolaps,List<Measure> measures) throws ReportException {
		
		CubeHeadCell[] copyheadcells=new CubeHeadCell[headcells.length];
		for(int i=0;i<headcells.length;i++){
			copyheadcells[i]=new CubeHeadCellImpl(headcells[i]);
		}
		//判断层数
		int index_max=0;
		List<CubeHeadCell[]> celllist=new ArrayList<CubeHeadCell[]>();
		DimensionRolap vertrolap=null;
		for(DimensionRolap dimrolap : dimrolaps){
			if(dimrolap.isUsesign()&&dimrolap.isVerticalsign())
				vertrolap=dimrolap;
		}
		if(vertrolap!=null){
			index_max=vertrolap.getLevel();
			if(measures.size()>1)
				index_max++;
			this.recursionCreateHead(celllist, copyheadcells, index_max, vertrolap.getDim());
			
		}else{
			index_max=1;
			this.recursionCreateHead(celllist, copyheadcells, index_max, null);
			//return celllist;
		}
		celllist.add(copyheadcells);
		return celllist;
	}


}