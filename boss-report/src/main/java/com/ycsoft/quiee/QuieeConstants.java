package com.ycsoft.quiee;

import java.io.File;
import java.util.Map;

import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.treequery.DimKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;

public class QuieeConstants {

	
	/**
	 * 初始化维度key容器
	 * @param test_query_id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void initTestDimKey(String test_query_id) throws Exception{
		FileObjectInputStream foi = null;
		try {
			if(DimKeyContainer.getDimkeymap().size()>0) return;
			if(test_query_id==null||test_query_id.equals(""))
				throw new Exception("test_query_id is null");
			String indexfilepath=ReportConstants.REP_TEMP_TXT
			+ test_query_id+ReportConstants.INDEX;
			File testindexfile=new File(indexfilepath);
			if(!testindexfile.exists())
				throw new Exception("File:"+indexfilepath+"is not exist.");
			foi = new FileObjectInputStream(indexfilepath);
			//跳过查询结果集
			 foi.readObject();
			 //取维度
			 Map<String, DimKey> dimkeymap=(Map<String, DimKey>) foi.readObject();
			if(dimkeymap==null) 
				throw new Exception("dimkeymap:"+indexfilepath+" is null");
		    DimKeyContainer.setDimkeymap(dimkeymap);
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (foi != null) {
					foi.close();
					foi = null;
				}
			} catch (Exception e) {
			}
		}
		
	}
}
