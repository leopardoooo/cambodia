package com.ycsoft.report.query;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.commons.CacheInput;
import com.ycsoft.report.commons.CacheOutput;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.FileObjectOutputStream;
import com.ycsoft.report.commons.OlapPageExcel;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.StringUtil;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.compute.CellCompute;
import com.ycsoft.report.query.cube.compute.DataCompute;
import com.ycsoft.report.query.cube.compute.GroupCompute;
import com.ycsoft.report.query.cube.compute.TotalCompute;
import com.ycsoft.report.query.cube.graph.CubeGraph;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.cube.showclass.cellwarn.CellColour;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarnApplyTo;
import com.ycsoft.report.query.daq.CubeCache;
import com.ycsoft.report.query.daq.DataReader;
import com.ycsoft.report.query.daq.QueryExtract;

/**
 * cubeOLAP展现
 */
public class QueryResultOlap extends AbstractDataSet implements QueryResult{
	
	protected int rows = 0;
	
	protected String rep_id=null;
	
	protected String query_id = null;

	protected String database = null;

	private Date visit_date=new Date();

	protected String rep_type=null;
	
	private static final int exprot_max=1000;
	
	protected String cache_query_id=null;
	/**
	 * 数据体sql
	 */
	private String datasetsql;
	
	public String getCacheQueryID(){
		return this.cache_query_id;
	}
	
	public QueryResultOlap(String query_id, RepDefine rep, String sql,String cache_query_id) {
		this.query_id=query_id;
		this.database=rep.getDatabase();
		this.rep_type=rep.getRep_type();
		this.rep_id=rep.getRep_id();
		this.datasetsql = sql;
		this.cache_query_id=cache_query_id;
	}
		
	/**
	 * 获得原始配置的sql
	 */
	public String getDataSet() {
		return this.datasetsql;
	}

	/**
	 * 验证变换后的sql是否正确
	 * @throws ReportException 
	 */
	public String validate() throws ReportException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql="";
		try {
			conn = ConnContainer.getConn(database);
			stmt = conn.createStatement();
			sql= this.getAssembleDataSet();
			LoggerHelper.debug(this.getClass(), sql);
			rs = stmt.executeQuery(sql);
			return null;
		} catch (SQLException e) {
			throw new ReportException("error:"+e.getMessage(),e,sql);
			//return "error:" + e.getMessage();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
			}
		}

	}
	/**
	 * 创建前台展现的内容写入文件缓存对象
	 * @return
	 * @throws IOException
	 * @throws ReportException 
	 */
	protected CacheOutput createCacheOutput() throws IOException, ReportException{
		return new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+ query_id);
	}
	
	/**
	 * 创建cube计算结果提取器对象
	 * @return
	 */
	protected QueryExtract getQueryExtract(){
		return new CubeCache(this.cache_query_id,this,this);
	}
	/**
	 * 执行查询，数据写入文件
	 * @param sql
	 * @throws ReportException
	 */
	public void execute() throws ReportException{

		CacheOutput foo = null;
		DataReader datareader=null;
		try {
			rows=0;
			foo =this.createCacheOutput();
			//数据提取
			QueryExtract cache=this.getQueryExtract();
			this.cache_query_id= cache.extractCache();
			datareader=cache.getData();
			//打开数据源，如果是查数据库则创建数据区查询并返回结果集，如果缓存则执行key排序
			datareader.open();
			// 结果集字段属性
			List<CubeHeadCell[]> olapheadlist=this.getCube().getCubeHead();
			CubeHeadCell[] headcells=olapheadlist.get(olapheadlist.size()-1);
			// 设置文件头
			foo.writeHead(olapheadlist);//保存到文件
			CubeCell[] datacells=null;
			//数据行计算器
			CellCompute<CubeCell[]> datacompute=new DataCompute(headcells,datareader);
			//分组小计行计算器
			CellCompute<CubeCell[]>[] groupcomputes=GroupCompute.createGroupCompute(headcells);
			//合计行计算器
			CellCompute<CubeCell[]> totalcompute=new TotalCompute(headcells);
			
			//警戒计算配置
			List<MeaWarnApplyTo> checks=CellColour.createCellColour(this);
			
			datacompute.setWarnCheck(CellColour.getMeaWarnCheck(checks,CellType.data));
			for( CellCompute<CubeCell[]> group: groupcomputes){
				group.setWarnCheck(CellColour.getMeaWarnCheck(checks, CellType.group));
			}
			totalcompute.setWarnCheck(CellColour.getMeaWarnCheck(checks, CellType.total));
			while (datareader.next()) {
				// 一行查询结果
				datacells = new CubeCell[headcells.length];
				//生成数据行
				datacompute.compute(datacells);
				datacells=datacompute.getResult();
					
				//分组小计
				for(CellCompute<CubeCell[]> group:groupcomputes){
					if(group.compute(datacells)){
						foo.writeObject(group.getResult());
						rows++;
					}
				}
				foo.writeObject(datacells);
				rows++;
				totalcompute.compute(datacells);
			}
			//末尾分组写入文件
			if(rows>0)
				for(CellCompute<CubeCell[]> group:groupcomputes){
					group.compute(null);//末尾分组计算
					foo.writeObject(group.getResult());
					rows++;
				}
			//合计行写入文件
			if(rows>0){
				foo.writeObject(totalcompute.getResult());
				rows++;
			}
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (ReportException e) {
			throw e;
		}catch (Exception e) {
			throw new ReportException(e);
		} finally {
			try {
				if (foo != null){
					foo.close();
					foo=null;
				}
			} catch (Exception e1) {
			}
			try {
				if (datareader != null){
					datareader.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 数据体合并单元格
	 * page 数据体
	 * row_start 开始的行索引
	 * row_max   结束的行索引
	 * col_index 计算的列索引
	 * @param page
	 * @param row
	 * @param col
	 */
	private void consolidateOlapPage(List<CubeCell[]> page,int row_start,int row_max,int col_index,int col_max){
		if(row_max==0) return;
		if(col_index>=col_max-1) return;
		
		CubeCell datecell=null;
		for(int i=row_start;i<row_max;i++){
			CubeCell[] cells=page.get(i);
		
			if(datecell==null){
				//空值
				datecell=cells[col_index];
			}else if(datecell.getId()==null||!datecell.getId().equals(cells[col_index].getId())){
				//id变化
				datecell=cells[col_index];
				consolidateOlapPage(page,row_start,i,col_index+1,col_max);
				row_start=i;
			}else{
				datecell.setRowspan(datecell.getRowspan()+cells[col_index].getRowspan());
				cells[col_index].setRowspan(0);
				cells[col_index].setColspan(0);
				//最后一行
				if(i==row_max-1){
					consolidateOlapPage(page,row_start,i,col_index+1,col_max);
				}
			}
			
			
		}
	}
	/**
	 * 创建 展现结果缓存读取对象
	 * @return
	 * @throws IOException 
	 * @throws ReportException 
	 */
	protected CacheInput createCacheInput() throws IOException, ReportException{
		return new FileObjectInputStream(ReportConstants.REP_TEMP_TXT + query_id);
	}
	
	public List<CubeCell[]> getPage(Integer start, Integer limit) throws ReportException{
		CacheInput foi = null;
		if (start > this.rows || start < 0 || limit <= 0)
			throw new ReportException("page_index start or limit error.");
		try {
			foi = this.createCacheInput();
			List<CubeCell[]> pagelist = new ArrayList<CubeCell[]>(limit);
			CubeCell[] list = null;
			int i = 0;
			int min_i = start;
			int max_i = start + limit;
			//查询头
			List<CubeHeadCell[]> headlist=(List<CubeHeadCell[]>)foi.readHead();
			int cross_col_max=0;
			for(CubeHeadCell cell:headlist.get(headlist.size()-1)){
				if(cell.getDim_type().equals(DimensionType.crosswise))
					cross_col_max++;
			}
				
			while ((list = (CubeCell[]) foi.readObject()) != null) {
				if (i >= min_i)
					pagelist.add(list);
				i++;
				if (i >= max_i)
					break;
			}
			//单元格合并
			consolidateOlapPage(pagelist,0,pagelist.size(),0,cross_col_max);
			return pagelist;
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		} finally {
			try {
				if (foi != null){
					foi.close();
					foi=null;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public String getDatabase() {
		return this.database;
	}

	public String getQueryId() {
		return this.query_id;
	}

	
	public List<CubeHeadCell[]> getHead() throws ReportException {
		CacheInput foi = null;
		try {
			foi = this.createCacheInput();
		
			List<CubeHeadCell[]> list=(List<CubeHeadCell[]>) foi.readHead();
			if(list==null)
				throw new ReportException("rephead is null");
			return list;
		}catch (IOException e) {
			throw new ReportException(e);
		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		} finally {
			try {
				if (foi != null){
					foi.close();
					foi=null;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void updateVisitDate() {
		this.visit_date = new Date();
	}

	public Date getVisitDate() {
		return visit_date;
	}

	public int getRowSize() {
		return rows;
	}

	public void clear() {
		File file = new File(ReportConstants.REP_TEMP_TXT+ query_id);
		if (file.exists())
			file.delete();
		file = new File(ReportConstants.REP_TEMP_TXT + query_id + ".xls");
		if (file.exists())
			file.delete();
		file = new File(ReportConstants.REP_TEMP_TXT +query_id + ".zip");
		if (file.exists())
			file.delete();
		file = new File(ReportConstants.REP_TEMP_TXT +query_id + "_index");
		if (file.exists())
			file.delete();
		//清理_base文件
		if(ReportConstants.DATABASETYPE_REALTIME.equals(SystemConfig.getDatabaseMap().get(database).getType())){
			file=new File(ReportConstants.REP_TEMP_TXT +query_id + ReportConstants.CACHE_BASE);
			if(file.exists())
				file.delete();
		}
	}

	public String getRepId() {
		return rep_id;
	}
		
	/**
	 * 导出单个excel文档
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	private static String exportXls(QueryResultOlap olap) throws ReportException{
		OlapPageExcel re=new OlapPageExcel(olap,exprot_max);
		return re.getExportFile();
	}
	
	/**
	 * 导出Zip,加同步标志(保证整个系统只能执行一个Zip包压缩)
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	private synchronized static String exportZip(QueryResultOlap olap) throws ReportException{
		OlapPageExcel re=new OlapPageExcel(olap,exprot_max);
		return re.getExportFile();
	}
	
	/**
	 * 返回导出文档的全文件名
	 * col_indexs为列索引数组,值为空表示导出快逸初始化数据源
	 * @throws ReportException 
	 */
	public String export(Integer... col_indexs) throws ReportException {
		this.updateVisitDate();		
		//导出明细报表
		//当生成excel文档需要zip压缩时，现在只能同时执行一个压缩（使用同步实现或其他省资源的方式
		if(this.rows>exprot_max)
			return exportZip(this);
		else 
			return exportXls(this);
	
	}

	/**
	 * 图形运算
	 */
	public CubeGraph graph(CubeGraph cg) throws ReportException {
		QueryGraph query=new QueryGraph(this,cg,this.cache_query_id);
		query.execute();
		return query.getGraph();
	}

	private String qrkeys_sha1=null;
	public String getQRSHA() {
		return qrkeys_sha1;
	}

	public void setQRSHA(String qrkeys) throws ReportException {
		try {
			this.qrkeys_sha1=StringUtil.SHA1(qrkeys);
		} catch (Exception e) {
			throw new ReportException("sha1计算失败",e);
		}
	}
}
