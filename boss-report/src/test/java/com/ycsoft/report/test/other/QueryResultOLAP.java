package com.ycsoft.report.test.other;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepColumn;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.FileObjectOutputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.QueryManage;
import com.ycsoft.report.query.QueryManageImpl;
import com.ycsoft.report.query.QueryResult;
import com.ycsoft.report.query.QueryResultCommon;
import com.ycsoft.report.query.treequery.DimKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;

public class QueryResultOLAP extends QueryResultCommon {


	public QueryResultOLAP(InitQueryDto qdto) throws ReportException {
		super(qdto);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5705346361807550427L;
	
	//查询列属性
	class QueryRsmd extends RepColumn{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7310071478774524974L;
		
		//private String 
		
		public String OlapType=null;
		
		
		
		public String super_id=null;//当前设置的最高级的id
		public DimKey super_dimkey;//当前设置的最高级dim_key
		
		public String ID=null;
		public String name=null;
		
		public DimKey dimkey;//原始的报表配置的dim_key
		
		public String ID_before;//上一个的ID
		public String name_before;//上一个name
		//统计行
		public double sum=0;
		
		//末尾合计使用的参数
		public boolean istotal = false;
		public double total = 0;
	}
	
	private List<QueryRsmd> qrlist=null;
	



}
