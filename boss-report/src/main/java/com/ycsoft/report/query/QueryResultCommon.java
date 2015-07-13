package com.ycsoft.report.query;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.helper.ZipHelper;
import com.ycsoft.report.bean.RepHead;
import com.ycsoft.report.commons.CacheInput;
import com.ycsoft.report.commons.CacheOutput;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.FileObjectOutputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.ResultExcel;
import com.ycsoft.report.commons.StringUtil;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.sql.AnalyseMemoryKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;

/**
 * 通用的查询结果集
 * 
 * @author new
 * 
 */
public class QueryResultCommon implements QueryResult {

	
	private static final long serialVersionUID = -3458355170580312345L;
	// 总行数
	protected int rows = 0;
	
	protected String rep_id=null;
	
	protected String query_id = null;

	protected String database = null;

	private Date visit_date=new Date();
	//列导出配置
	private Integer[] export_cols=null;

	protected String rep_type=null;
	
	private boolean sum_sign=false;
	
	//结果集 列属性
	class QueryRsmd {
		
		protected String ColumnName = null;
		// sql结果集中列的类型
		protected String TypeName = null;
		//列类型是否数值型
		protected boolean isnumber=false;
		// 某行某列的值
		protected Object ColumnValue = null;

		protected String OlapType=null;
		// 分组统计使用的参数
		protected boolean isgroupkey=false;//是否分组定义列
		protected boolean isgroupbycolumn=false;//是否分组列
		protected boolean isgroup = false;//分组统计数据列
		protected double group = 0;

		// 末尾合计使用的参数
		protected boolean istotal = false;
		protected double total = 0;
		
		//列id内存转换
		protected boolean ismemory=false;
		protected Map<String,String> memorymap=null;
		public Object getMemoryValue(Object value){
			if(ismemory){
				String temp=null;
				if(isnumber)
					value=value.toString();			
				temp=memorymap.get(value);
				if(temp==null)
					return value;
				else
					return temp;
			}
			return value;
		}
	}

	private String group;
	private String[] total;
	
	
	
	protected QueryResultCommon(InitQueryDto qdto,boolean do_exec) throws ReportException {
		instance(qdto,do_exec);
	}
	/**
	 * 通用查询
	 * 
	 * @param qdto
	 * @throws ReportException
	 */
	public QueryResultCommon(InitQueryDto qdto) throws ReportException {
		
		instance(qdto,true);
	}

	private void instance(InitQueryDto qdto, boolean do_exec) throws ReportException {
		this.query_id=qdto.getQuery_id();
		this.database=qdto.getDatabase();
		this.rep_type=qdto.getRep_type();
		this.rep_id=qdto.getRep_id();
		
		group=qdto.getGroup();
		total=qdto.getTotals();

		if (rep_type.endsWith(ReportConstants.REP_TYPE_QUIEE))
			total = null;
		if(do_exec)
			execute(qdto.getSql());
		
	}
	/**
	 * 创建缓存输出流
	 * @return
	 * @throws IOException
	 * @throws ReportException 
	 */
	protected CacheOutput createCacheOutput() throws IOException, ReportException{
		return new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+ query_id);
	}
	protected void execute(String sql) throws ReportException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		CacheOutput foo = null;
		try {

			foo =this.createCacheOutput();
			conn = ConnContainer.getConn(database);

			stmt = conn.createStatement();
			stmt.setFetchSize(1000);
			
			LoggerHelper.debug(this.getClass(),sql);
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			List<QueryRsmd> qrlist=this.createQRList(rsmd);
			// 结果集字段属性
			
			//获得分组标志,分组保存标志
			boolean group_sign=false,groupsave_sign=true;
			for(QueryRsmd qr:qrlist)
				group_sign=qr.isgroupbycolumn||group_sign;
			//快逸报表不能有分组统计
			if(rep_type.equals(ReportConstants.REP_TYPE_QUIEE))
				group_sign=false;
			
			// 设置文件头
			List<List<RepHead>> headlist=this.createHead(qrlist);
			foo.writeHead(headlist);//保存到文件
			List<Object> queryresult=null;//一条查询结果
			while (rs.next()) {

				// 一行查询结果
				queryresult = new ArrayList<Object>(rsmd
						.getColumnCount());
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					QueryRsmd qr = qrlist.get(i - 1);
					Object value = rs.getObject(i);
					
					if (value == null)
						value = "";
					
					//列转换处理
					value=qr.getMemoryValue(value);
					
					queryresult.add( value);
					// 处理合计项
					if (qr.istotal)
						qr.total = qr.total + rs.getDouble(i);
					//分组sum
					if(qr.isgroup)
						qr.group=qr.group+rs.getDouble(i);
					//分组列
					if(qr.isgroupbycolumn){
						if(!groupsave_sign&&!value.equals(qr.ColumnValue)){
							foo.writeObject(this.getGroup(qrlist));
							rows++;
							groupsave_sign=true;
						}
						qr.ColumnValue=value;
					}
				}
				// 把一行查询结果写入文件
				foo.writeObject(queryresult);
				rows++;
				//分组合计保存标志
				groupsave_sign=false;
			}
			//最后一行的分组统计
			if(group_sign&&queryresult!=null){
				foo.writeObject(this.getGroup(qrlist));
				rows++;
			}
			//把最后的合计行写入
			//明细报表并存在统计列
			if(isExecTotal()){
					List<Object> totallist= getTail(qrlist);
					foo.writeObject(totallist);
					rows++;
					this.sum_sign=true;
			}
		} catch (SQLException e) {
			throw new ReportException(e,sql);
		} catch (IOException e) {
			throw new ReportException(e);
		} catch (Exception e) {
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
				if (rs != null){
					rs.close();
					rs=null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null){
					stmt.close();
					stmt=null;
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null){
					conn.close();
					conn=null;
				}
			} catch (Exception e) {
			}
		}
	}
	
	

	@SuppressWarnings("unchecked")
	public List<List<String>> getPage(Integer start, Integer limit)
			throws ReportException {
		CacheInput foi = null;
		if (start > this.rows || start < 0 || limit <= 0)
			throw new ReportException("page_index start or limit error.");
		try {
			foi = this.createCacheInput();// new FileObjectInputStream(ReportConstants.REP_TEMP_TXT + query_id);
			List<List<String>> pagelist = new ArrayList<List<String>>(limit);
			List<String> list = null;
			int i = 0;
			int min_i = start;
			int max_i = start + limit;
			//跳过查询头
			foi.readHead();
			
			while ((list = (List<String>) foi.readObject()) != null) {
				if (i >= min_i){
					//合计行判断
					if(this.sum_sign&&i==this.rows-1)
						list.add("T");
					else
						list.add("F");
					pagelist.add(list);
				}
				i++;
				if (i >= max_i)
					break;
			}
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

	/**
	 * 最后一行是合计行标记
	 */
	public boolean getTailSumRowSign(){
		return this.sum_sign;
	}
	/**
	 * 获取合计行
	 * @param qrlist
	 * @return
	 */
	private List<Object>  getTail(List<QueryRsmd> qrlist)  {
		List<Object> taillist = new ArrayList<Object> (qrlist.size());
		for(int i=0;i<qrlist.size();i++){
			QueryRsmd qr =qrlist.get(i);
			if (qr.istotal){
				BigDecimal BDtotal=new BigDecimal(qr.total).setScale(2, BigDecimal.ROUND_HALF_UP);
				taillist.add(i==0?"合计"+BDtotal.toString():BDtotal);
			}else
				taillist.add(i==0?"合计":"");
		}
		return taillist;
	}
	/**
	 * 获取分组行
	 * @param qrlist
	 * @return
	 */
	private List<Object>  getGroup(List<QueryRsmd> qrlist)  {
		List<Object> taillist = new ArrayList<Object> (qrlist.size());
		for(int i=0;i<qrlist.size();i++){
			QueryRsmd qr =qrlist.get(i);
			if (qr.isgroup){
				BigDecimal BDgroup=new BigDecimal(qr.group).setScale(2, BigDecimal.ROUND_HALF_UP);
				taillist.add(BDgroup);
			    qr.group=0;
			}else if(qr.isgroupkey) 
				taillist.add("小计");
			else taillist.add("");
		}
		return taillist;
	}
	public String getDatabase() {
		return this.database;
	}

	public String getQueryId() {
		return this.query_id;
	}

	protected CacheInput createCacheInput() throws IOException, ReportException{
		return new FileObjectInputStream(ReportConstants.REP_TEMP_TXT+ query_id);
	}
	/**
	 * 获取查询头
	 */
	public List<List<RepHead>> getHead() throws ReportException {
		CacheInput foi = null;
		try {
			foi = createCacheInput();
		
			List<List<RepHead>> list=(List<List<RepHead>>) foi.readHead();
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
	}

	public String getRepId() {
		return rep_id;
	}
	
	/**
	 * 导出快逸报表初始化数据源zip
	 * @return
	 * @throws ReportException
	 */
	private String exportRaq() throws ReportException{
		FileObjectOutputStream foo=null;
		try {
			foo=new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+this.query_id+ReportConstants.INDEX);
			//写入查询结果集对象
			foo.writeObject(this);
		    //写入查询维度容器
		    foo.writeObject(DimKeyContainer.getDimkeymap());
		    foo.close();
		    foo=null;
		    String[] files={(ReportConstants.REP_TEMP_TXT+this.query_id),(ReportConstants.REP_TEMP_TXT+this.query_id+ReportConstants.INDEX)};  
		    ZipHelper.zip(files, ReportConstants.REP_TEMP_TXT, this.query_id);
		    return ReportConstants.REP_TEMP_TXT+this.query_id+ReportConstants.ZIPPOSTFIX;
		} catch (IOException e) {
			throw new ReportException(e);
		}finally{
			try {
				if (foo != null){
					foo.close();
					foo=null;
				}
			} catch (Exception e1) {
			}
		}
	    
	}
	
	/**
	 * 导出单个excel文档
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	private static String exportXls(String query_id,Integer... export_cols) throws ReportException{
		ResultExcel re=new ResultExcel(query_id,export_cols);
		return re.getExportFile();
	}
	
	/**
	 * 导出Zip,加同步标志(保证整个系统只能执行一个Zip包压缩)
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	private synchronized static String exportZip(String query_id,Integer... export_cols) throws ReportException{
		ResultExcel re=new ResultExcel(query_id,export_cols);
		return re.getExportFile();
	}
	
	/**
	 * 判断导出配置是否一致
	 * @param col_indexs
	 * @return
	 */
	private boolean isexportcolssame(Integer... col_indexs){
		if(this.export_cols==null&&col_indexs==null)
			return true;
		if(this.export_cols!=null&&col_indexs!=null&&this.export_cols.length==0&&col_indexs.length==0)
			return true;
		if(this.export_cols!=null&&col_indexs!=null&&this.export_cols.length==col_indexs.length){
			Map<String,Integer> a=new HashMap<String,Integer>();
			Map<String,Integer> b=new HashMap<String,Integer>();
			for(Integer o:this.export_cols)
				a.put(o.toString(), o);
			for(Integer o:col_indexs)
				b.put(o.toString(), o);
			for(Integer o:this.export_cols){
				if(!b.containsKey(o.toString()))
					return false;
			}
			for(Integer o:col_indexs){
				if(!a.containsKey(o.toString()))
					return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * 返回导出文档的全文件名
	 * col_indexs为列索引数组,值为空表示导出快逸初始化数据源
	 * @throws ReportException 
	 */
	public String export(Integer... col_indexs) throws ReportException {
		this.updateVisitDate();
		
		//判断导出文件是否已经存在
		String exportfile=ReportConstants.REP_TEMP_TXT+this.query_id+ReportConstants.ZIPPOSTFIX;
		if(new File(exportfile).exists()&&this.isexportcolssame(col_indexs))
			return exportfile;
		exportfile=ReportConstants.REP_TEMP_TXT+this.query_id+ReportConstants.EXCELPOSTFIX;
		if(new File(exportfile).exists()&&this.isexportcolssame(col_indexs))
			return exportfile;
		this.export_cols=col_indexs;
		//执行导出
		if(ReportConstants.REP_TYPE_QUIEE.equals(this.rep_type)){
			//导出快逸初始化用数据源
			return this.exportRaq();
		}else{
			//导出明细报表
			//当生成excel文档需要zip压缩时，现在只能同时执行一个压缩（使用同步实现或其他省资源的方式
			if(this.rows>ReportConstants.WORKBOOKMAXROWS)
				return exportZip(this.query_id,this.export_cols);
			else 
				return exportXls(this.query_id,this.export_cols);
		}
	}
	/**
	 * 创建表头
	 * @param qrlist
	 * @return
	 */
	protected List<List<RepHead>> createHead(List<QueryRsmd> qrlist) {
		List<List<RepHead>> headlist = new ArrayList<List<RepHead>>();
		List<RepHead> list = new ArrayList<RepHead>();
		for (int i = 0; i < qrlist.size(); i++) {
			QueryRsmd qr = qrlist.get(i);
			RepHead rh = new RepHead();
			rh.setCol_desc(qr.ColumnName);
			rh.setCol_length(1);
			rh.setCol_seq(i);
			rh.setRow_seq(1);
			rh.setOlap_type(qr.OlapType);
			list.add(rh);
		}
		headlist.add(list);
		return headlist;
	}


	/**
	 * 创建结果集的列属性
	 * 分组统计逻辑还存在问题
	 * @param rsmd
	 */
	protected List<QueryRsmd> createQRList(ResultSetMetaData rsmd)
			throws SQLException {
		List<QueryRsmd> qrlist = new ArrayList<QueryRsmd>(rsmd.getColumnCount());
		
		//分组合计标志
		boolean groupcolumn_sign=false,groupsum_sign=false;
		if(StringHelper.isNotEmpty(group)){
			groupcolumn_sign=true;
			groupsum_sign=true;
		}
		for (int i = 0; i <rsmd.getColumnCount(); i++) {
			QueryRsmd qr=new QueryRsmd();
			
			//列数值属性
			if("NUMBER".equals(rsmd.getColumnTypeName(i + 1))
					|| "INTEGER".equals(rsmd.getColumnTypeName(i + 1)))
				qr.isnumber=true;
			
			qr.ColumnName = rsmd.getColumnName(i+1);
			//内存ID转换
			Map<String,String> mkmap=AnalyseMemoryKey.getMemoryMap(qr.ColumnName);
			if(mkmap!=null&&mkmap.size()>0){
				qr.ColumnName=AnalyseMemoryKey.getMemoryKeyDesc(qr.ColumnName);
				qr.memorymap=mkmap;
				qr.ismemory=true;
			}//合计项设置
			for(int j=0;total!=null&& j<total.length;j++){
	            if(qr.ColumnName.equals(total[j]))
	            	qr.istotal=true;	            
			}
			//分组统计项目设置
			if(groupsum_sign){
				if(!groupcolumn_sign&&(rsmd.getColumnTypeName(i+1).equals("NUMBER")
						||rsmd.getColumnTypeName(i+1).equals("INTEGER"))){
					qr.isgroup=true;
				}else if(groupcolumn_sign){
					 qr.isgroupbycolumn=true;
					if(qr.ColumnName.equals(group)){
						groupcolumn_sign=false;
						qr.isgroupkey=true;
					}
				}
			}
			qrlist.add(qr);
		}
		//groupcolumn_sign=true 说明未找到分组列
		if(groupcolumn_sign){
			for(QueryRsmd qr:qrlist){
				qr.isgroup=false;
				qr.isgroupbycolumn=false;
			}
		}
		return qrlist;
	}



	protected boolean isExecTotal() {
		return total!=null;
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