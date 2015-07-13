package com.ycsoft.quiee;

import java.io.File;
import java.util.List;

import com.runqian.base4.util.ReportError;
import com.runqian.report4.dataset.ColInfoBase;
import com.runqian.report4.dataset.DataSet;
import com.runqian.report4.dataset.IDataSetFactory;
import com.runqian.report4.dataset.Row;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.DataSetConfig;
import com.ycsoft.report.bean.RepHead;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.QueryContainer;
import com.ycsoft.report.query.QueryResult;
/**
 * 公共查询(查询结果集)的自定义数据集生产工厂
 */
public class QueryDataSet implements IDataSetFactory{

	/**
	 * ctx为报表引擎环境，
	 * dsc为数据集定义类，
	 * reteieve是指是否检查出数据，如果为false，只返回数据集的结构
	 */
	public DataSet createDataSet(Context ctx, DataSetConfig dsc, boolean retrieve) {
		
		String query_id=null;
        String rep_id=null;
        String test_query_id=null; 
        try {
        	//模板定义的参数query_id,rep_id,test_query_id
        	rep_id=(String) ctx.getParamValue(ReportConstants.REP_ID);
        	test_query_id=(String) ctx.getParamValue(ReportConstants.TEST_QUERY_ID);
			query_id=(String) ctx.getParamValue(ReportConstants.QUERY_ID);
        	//验证rep_id
			if(rep_id==null||rep_id.equals(""))
				throw new Exception("rep_id is null:自定义数据集的参数rep_id未定义");
			QueryResult qr=null;
			/**
			 * 当query_id为空时，使用数据集中的自定义的测试数据
			 */
			if(query_id==null||query_id.equals("")){
				if(test_query_id==null||test_query_id.equals(""))
					throw new Exception("test_query_id is null:自定义数据集的参数test_query_id未定义");			   
				qr=getQueryResult(test_query_id);
				if(qr==null)
					throw new Exception("QueryResult("+test_query_id+") is null");
				//rep_id 验证				
				if(!rep_id.equals(qr.getRepId()))
					throw new Exception("ERROR:reportfile.repid("+rep_id+") !=queryresult.repid("+qr.getRepId()+")");
			}else{
				qr=QueryContainer.getRepQuery(query_id);
				if(qr==null)
					throw new Exception("QueryResult("+query_id+") is null");
			}
					
			//数据集字段属性设置
			List<List<RepHead>> headlist=(List<List<RepHead>>) qr.getHead();
			List<RepHead> repheads=headlist.get(headlist.size()-1);
			 
			DataSet ds = new DataSet(qr.getRowSize()-1,repheads.size(),"pbds1");

			for(RepHead head:repheads){
				ColInfoBase ci = new ColInfoBase();   //逐列构造列信息对象
				ci.setColName(head.getCol_desc());    //设置列英文名
				ci.setColTitle(head.getCol_desc());  //设置列中文名，这里省略，直接用英文名代替
				if(ReportConstants.OLAP_TYPE_TOTAL.equals(head.getOlap_type()))
					ci.setDataType(com.runqian.report4.usermodel.Types.DT_DOUBLE);
				else
					ci.setDataType(com.runqian.report4.usermodel.Types.DT_STRING);  //设置列的数据类型
				ds.addColInfo(ci);
			}
			
			if(retrieve)//取数据集内容
				return getDataSetFromQueryResult(ds,qr);
			else//只返回结构
				return ds;
		} catch (Exception e) {			
			throw new ReportError("QueryDataSet_Error:"+e.getMessage(),e);
		}
	}
	/**
	 * 获取查询结果集对象
	 * @param test_query_id
	 * @return
	 * @throws Exception
	 */
	public QueryResult getQueryResult(String test_query_id)throws Exception{
		FileObjectInputStream foi = null;
		try {
			String indexfilepath=ReportConstants.REP_TEMP_TXT
			+ test_query_id+ReportConstants.INDEX;
			File testindexfile=new File(indexfilepath);
			if(!testindexfile.exists())
				throw new Exception("File:"+indexfilepath+"is not exist.");
			foi = new FileObjectInputStream(indexfilepath);
			QueryResult queryResult=(QueryResult) foi.readObject();
			if(queryResult==null) 
				throw new Exception("QueryResult:"+indexfilepath+" is null");
			
			return queryResult;
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
	/**
	 * 从queryresult中生成dataset
	 * @param ds
	 * @param qr
	 * @return
	 */
	public DataSet getDataSetFromQueryResult(DataSet ds,QueryResult qr)throws Exception{
		FileObjectInputStream foi = null;		
		try {
			foi = new FileObjectInputStream(ReportConstants.REP_TEMP_TXT + qr.getQueryId());			
			List<Object> list = null;
			int i = 0;
			int max_i = qr.getRowSize()-1;
			if(max_i==0) return ds;
			//跳过查询头
			foi.readObject();
			
			while ((list = (List<Object>) foi.readObject()) != null) {				
				Row row = ds.addRow();
				for(int j=0;j<list.size();j++)
					row.setData(j+1, list.get(j));
				i++;
				if (i >= max_i)
					break;
			}
			return ds;
		} catch (Exception e) {
			throw e;
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

}
