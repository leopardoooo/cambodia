package com.ycsoft.bulletin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.login.DBConnection;

public class BulletinServlet extends HttpServlet {
	private DateFormat df = null;
	/**
	 * Constructor of the object.
	 */
	public BulletinServlet() {
		super();
		String FORMAT_TIME="yyyy-MM-dd HH:mm:ss";
		df = new SimpleDateFormat(FORMAT_TIME);
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=gb2312");
		PrintWriter out = response.getWriter();
		//参数
		String optr = request.getParameter("optr");
		String dept_id = request.getParameter("dept_id");
		String chechFlag = request.getParameter("chechFlag");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		List<Map<String, Object>> bulletins = new ArrayList<Map<String,Object>>();
		
		chechFlag = chechFlag ==null || chechFlag.trim().length() ==0 ? "F":chechFlag;//默认是未查看的F,否则是T
		try{
			if(chechFlag.equals("F")){
				bulletins = queryUnchecked(optr,dept_id);
				out.println(JsonHelper.fromObject(bulletins));
			}else{
				
				Pager<Map<String, Object>> page = queryAll(optr,dept_id,start,limit);
				Map<String, Object> pageInfo = new HashMap<String, Object>();
				pageInfo.put("records", page.getRecords());
				pageInfo.put("totalProperty", page.getTotalProperty());
				pageInfo.put("start", start);
				pageInfo.put("limit", limit);
				
				out.println(JsonHelper.fromObject(pageInfo));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
//		out.print(JsonHelper.fromObject(root));
		out.flush();
		out.close();
	}

	/**
	 * @deprecated 不再使用.
	 * @param optrId
	 * @param dept_id
	 * @param startStr
	 * @param limitStr
	 * @return
	 * @throws Exception
	 */
	private Pager<Map<String, Object>> queryAll(String optrId, String dept_id, String startStr, String limitStr) throws Exception{
		List<Map<String, Object>> record = new ArrayList<Map<String,Object>>();
		Integer start = Integer.parseInt(startStr);
		Integer limit = Integer.parseInt(limitStr);
		Integer totalProperty = 0;
		////TODO 时间倒叙
		Pager<Map<String, Object>> page = new Pager<Map<String,Object>>();
		
		String sql = " SELECT a.*, c.check_date FROM s_bulletin a, s_bulletin_check c , s_bulletin_county sbc  "+
				" WHERE a.bulletin_id = c.bulletin_id(+) and a.bulletin_id = sbc.bulletin_id "+
				" and c.optr_id(+) =?  AND a.status ='ACTIVE' "+
				" AND to_date(to_char(sysdate, 'yyyymmdd hh24:mi:ss'), 'yyyymmdd hh24:mi:ss') BETWEEN a.eff_date AND a.exp_date "+
				" AND sbc.dept_id = ?   order by a.eff_date desc  ";
		String countSql = "select count(1) from ( "+ sql + ")";
		Connection conn = null;
		PreparedStatement pst =null;
		ResultSet rst = null;
		try{
			conn = DBConnection.getConnection();
			pst = conn.prepareStatement(countSql);
			pst.setString(1, optrId);
			pst.setString(2, dept_id);
			rst = pst.executeQuery();
			while(rst.next()){
				totalProperty = rst.getInt(1);
			}
			page.setTotalProperty(totalProperty);
			
			String querySql = "SELECT * FROM (SELECT row_.*, ROWNUM rownum_ FROM ( "
					+ sql + ")  row_ WHERE rownum <= ? ) WHERE rownum_ > ?";
			pst = conn.prepareStatement(querySql);
			pst.setString(1, optrId);
			pst.setString(2, dept_id);
			pst.setInt(3, (start + limit));
			pst.setInt(4, start);
			ResultSet rst1 = pst.executeQuery();
			
			this.extractData(record, rst1);
			page.setRecords(record);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				DBConnection.closeRsStConn(rst, pst, conn);
			}catch (Exception e) {
				throw e;
			}
		}
		
		return page;
	}
	private List<Map<String, Object>> queryUnchecked(String optrId, String dept_id) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//TODO 时间顺序
		String sql = "SELECT * FROM s_bulletin a , s_bulletin_county sbc WHERE a.bulletin_id = sbc.bulletin_id and "
				+ " a.status='ACTIVE' AND to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') BETWEEN  a.eff_date AND a.exp_date "
				+ " and sbc.dept_id = ? "
				+ " AND NOT EXISTS (SELECT 1 FROM s_bulletin_check c WHERE c.bulletin_id=a.bulletin_id and ? = c.optr_id ) order by  eff_date ";
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rst = null;
		try{
			conn = DBConnection.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, dept_id);
			pst.setString(2, optrId);
			rst = pst.executeQuery();
			extractData(list, rst);
			List<String> ids = new ArrayList<String>();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for(Map<String, Object> bul :list){
				String id = bul.get("bulletin_id")!= null ? bul.get("bulletin_id").toString():"";
				if(!ids.contains(id) ){
					ids.add(id.toString());
					result.add(bul);
				}
			}
			list = result;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				DBConnection.closeRsStConn(rst, pst, conn);
			}catch (Exception e) {
				throw e;
			}
		}
		return list;
	}

	private void extractData(List<Map<String, Object>> list, ResultSet rst)
			throws SQLException {
		ResultSetMetaData meta = rst.getMetaData();
		int columnCount = meta.getColumnCount();
		String [] columns = new String[columnCount];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = meta.getColumnName(i + 1);
		}
		while(rst.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(int index =1;index<=columnCount;index++){
				String key = meta.getColumnName(index);

                switch (meta.getColumnType(index)) {
                case Types.TIMESTAMP: {
                	Timestamp ts = rst.getTimestamp(key);
                    map.put(key.toLowerCase(), this.df.format(new java.util.Date(ts.getTime())));
                    break;
                }
                case Types.DATE: {
                	Timestamp ts = rst.getTimestamp(key);
                    map.put(key.toLowerCase(), this.df.format(new java.util.Date(ts.getTime())));
                    break;
                }
                case Types.CLOB: {
                    map.put(key.toLowerCase(), rst.getString(key));
                    break;
                }
                default:
                    map.put(key.toLowerCase(), rst.getObject(key));
                    break;
                }
			}
			list.add(map);
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
