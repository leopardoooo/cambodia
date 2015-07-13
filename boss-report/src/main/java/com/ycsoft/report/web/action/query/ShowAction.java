package com.ycsoft.report.web.action.query;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.component.query.FileComponent;
import com.ycsoft.report.component.query.QueryComponent;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.impl.CubeHeadCellImpl;

/**
 * 结果展示界面控制
 * @author new
 *
 */
public class ShowAction extends ReportAction {
	
	private QueryComponent queryComponent;

	private FileComponent fileComponent;
	
	private Integer[] column_index_list;
	
	private String rep_name;
	
	/**
	 * 维名称
	 */
	private String dim;
	
	private Integer level;
	
	private String[] level_values;
	
	private String[] meas;
	
//	private String mycube_json;
	
	private String graphtype;
	
	private String[] dimensions;
	
	private String context_id;
	private String context_json;
	
	private String headdatacells;
	
	private String template_id;
	
	private String cube_sort_map;
	
	/**
	 * cube编辑模式
	 * @return
	 */
	public String cubeEdit() throws Exception{
		queryComponent.cubeEdit(query_id);
		return JSON_SUCCESS;
	}
	/**
	 * 显示可编辑表格的编辑模式
	 * 参数 维度坐标串=headdatacells ，rep_id,
	 * 返回
	 * simpleobj=List<GridRow> 表头配置信息
	 * records=List<RowData> 内容列表
	 * @return
	 * @throws Exception 
	 */
	public String cubeShowCustomDetail() throws Exception{
		getRoot().setSimpleObj(queryComponent.queryCustomRows(rep_id));
		Type type=new TypeToken<List<CubeHeadCellImpl>>(){}.getType();
		List<CubeHeadCell> list=JsonHelper.toList(headdatacells, type);
		getRoot().setRecords(queryComponent.cubeShowCustomDetail(rep_id,list));
		return JSON;
	}
	/**
	 * 更新或保存一条可编辑表格内容数据
	 * 参数rep_id,headdatacells,context_id,context_json
	 * 返回context_id
	 * @return
	 */
	public String cubeSaveCustomRow() throws Exception{
		Type type=new TypeToken<List<CubeHeadCellImpl>>(){}.getType();
		List<CubeHeadCell> list=JsonHelper.toList(headdatacells, type);
		getRoot().setSimpleObj(queryComponent.updateOneCustom(rep_id, getOptr().getOptr_id(), context_id, context_json, list));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 刷新报表按钮
	 * 参数Paramter.MyCube
	 * @return
	 * @throws Exception
	 */
	public String cubeRefresh()throws Exception{
		queryComponent.cubeRefush(query_id,getParameter().getMycube().getDimlist()
										  ,getParameter().getMycube().getVertdim()
										  ,getParameter().getMycube().getMealist());
		return JSON_SUCCESS; 
	}
	
	//public String queryM
	//public String cube
	/**
	 * 执行图形计算，返回计算数据
	 * 参数query_id,图形类型,维度
	 * @return
	 * @throws Exception
	 */
	public String cubeGraph()throws Exception{
		getRoot().setSimpleObj(queryComponent.cubeGraph(query_id, graphtype, dimensions, meas));
		return JSON_SIMPLEOBJ;
	}
	/**
	 * 执行模板切换展现
	 * @return
	 * @throws Exception
	 */
	public String cubeChangeMyCube()throws Exception{
		queryComponent.cubeChangeMyCube(query_id, template_id);
		return JSON_SUCCESS;
	}
	/**
	 * 指标选择
	 * @return
	 * @throws Exception
	 */
	public String cubeMeaSelect()throws Exception{
		queryComponent.cubeMeaSelect(query_id, meas);
		return JSON_SUCCESS;
	}
	/**
	 * 维选择
	 * @return
	 * @throws Exception
	 */
	public String cubeDimSelect()throws Exception{
		queryComponent.cubeDimSelect(query_id
				, getParameter().getMycube().getDimlist()
				, getParameter().getMycube().getVertdim());
		return JSON_SUCCESS;
	}
	/**
	 * 维合计配置
	 */
	public String cubeDimTotal()throws Exception{
		queryComponent.cubeDimTotal(query_id
				, getParameter().getMycube().getDimtotalmap());
		return JSON_SUCCESS;
	}
	/**
	 * 维过滤切片
	 * @return
	 * @throws Exception
	 */
	public String cubeSlices()throws Exception{
		queryComponent.cubeSlices(query_id, dim, level, level_values);
		return JSON_SUCCESS;
	}
	/**
	 * 维自定义排序
	 * @return
	 * @throws Exception
	 */
	public String cubeSort()throws Exception{
		Type type=new TypeToken<Map<Integer,String[]>>(){}.getType();
		System.out.println(cube_sort_map);
		Map<Integer,String[]> map=JsonHelper.toMap(cube_sort_map, type);
		
		queryComponent.cubeSort(query_id, dim, map);
		return JSON_SUCCESS;
	}
	/**
	 * 维展开
	 * @return
	 * @throws Exception
	 */
	public String cubeExpand()throws Exception{
		queryComponent.cubeExpend(query_id, dim);
		return JSON_SUCCESS;
	}
	/**
	 * 维收缩
	 * @return
	 * @throws Exception
	 */
	public String cubeShrink()throws Exception{
		queryComponent.cubeShrink(query_id, dim);
		return JSON_SUCCESS;
	}
	/**
	 * 查询OLAP表头
	 * @return
	 * @throws Exception
	 */
	public String queryOlapHeader()throws Exception{
		getRoot().setRecords(queryComponent.queryOlapHeader(query_id));
		return JSON_RECORDS;
	}
	
	/**
	 * 报表信息查询
	 */
	public String queryOlapReport() throws Exception {
		getRoot().setPage(queryComponent.queryOlapReport(query_id, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 组合单元格合并表格
	 */
	public String queryTable() throws Exception {
		getRoot().setRecords(queryComponent.queryOlapHeader(query_id));
		getRoot().setPage(queryComponent.queryOlapReport(query_id, start, limit));
		return JSON;
	}
	
	/**
	 * 创建导出文件
	 * @return
	 */
	public String createExp()throws Exception{
		column_index_list=fileComponent.queryExportConfig(this.getOptr().getOptr_id(), query_id);
		fileComponent.downloadExp(query_id, column_index_list);
		return JSON_SUCCESS;
	}
	/**
	 * 下载导出数据
	 * @throws Exception 
	 */
	public String downloadExp() throws Exception{
		
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		try{
		column_index_list=fileComponent.queryExportConfig(this.getOptr().getOptr_id(), query_id);
		String filefullname=fileComponent.downloadExp(query_id, column_index_list);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//前台回传的rep_name转化成正确的编码
		//rep_name=new String(rep_name.getBytes("ISO-8859-1"),"UTF-8");
		//把rep_name转化成文件下载流能识别的中文文件名编码
		//rep_name=new String(rep_name.getBytes(),"ISO-8859-1");
		//从数据库获得导出文件名,并转化成下载流能识别的中文文件名编码
		 String rep_name=fileComponent.queryHtmlExpFileName(rep_id);
		 rep_name=new String(rep_name.getBytes(),"ISO-8859-1");
		if(filefullname.endsWith(".zip")){
			response.setContentType( "application/zip");
			response.setHeader("Content-disposition", "attachment; filename="+rep_name+".zip");
		}else{
			response.setContentType( "application/x-msdownload");
			response.setHeader("Content-disposition", "attachment; filename="+rep_name+".xls");
		}
		bis = new java.io.BufferedInputStream(new FileInputStream(
				filefullname));
		bos = new java.io.BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		return NONE;
		}catch(Exception e){
			throw e;
		}finally{
			try{
				bis.close();
			}catch(Exception e){}
			try{
				bos.close();
			}catch(Exception e){}
		}
	}

	/**
	 * 表头名称查询
	 */
	public String queryHeader() throws Exception {
		getRoot().setRecords(queryComponent.queryHeader(query_id));
		//getRoot().setSimpleObj(queryComponent.queryTailRow(query_id));
		return JSON;
	}

	/**
	 * 报表信息查询
	 */
	public String queryReport() throws Exception {
		getRoot().setPage(queryComponent.queryReport(query_id, start, limit));
		return JSON_PAGE;
	}
	
	public QueryComponent getQueryComponent() {
		return queryComponent;
	}

	public void setQueryComponent(QueryComponent queryComponent) {
		this.queryComponent = queryComponent;
	}

	public FileComponent getFileComponent() {
		return fileComponent;
	}

	public void setFileComponent(FileComponent fileComponent) {
		this.fileComponent = fileComponent;
	}

	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}
	public void setColumn_index_list(Integer[] column_index_list) {
		this.column_index_list = column_index_list;
	}
	public void setDim(String dim) {
		this.dim = dim;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public void setLevel_values(String[] level_values) {
		this.level_values = level_values;
	}
	public void setMeas(String[] meas) {
		this.meas = meas;
	}
	public Integer[] getColumn_index_list() {
		return column_index_list;
	}
	public String getRep_name() {
		return rep_name;
	}
	public String getDim() {
		return dim;
	}
	public Integer getLevel() {
		return level;
	}
	public String[] getLevel_values() {
		return level_values;
	}
	public String[] getMeas() {
		return meas;
	}
	public String getGraphtype() {
		return graphtype;
	}
	public void setGraphtype(String graphtype) {
		this.graphtype = graphtype;
	}
	public String[] getDimensions() {
		return dimensions;
	}
	public void setDimensions(String[] dimensions) {
		this.dimensions = dimensions;
	}
	public void setContext_id(String context_id) {
		this.context_id = context_id;
	}
	public void setContext_json(String context_json) {
		this.context_json = context_json;
	}
	public void setHeaddatacells(String headdatacells) {
		this.headdatacells = headdatacells;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getContext_id() {
		return context_id;
	}
	public String getContext_json() {
		return context_json;
	}
	public String getHeaddatacells() {
		return headdatacells;
	}
	public String getCube_sort_map() {
		return cube_sort_map;
	}
	public void setCube_sort_map(String cube_sort_map) {
		this.cube_sort_map = cube_sort_map;
	}
}
