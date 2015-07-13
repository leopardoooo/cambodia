package com.ycsoft.quiee;


import com.runqian.base4.util.ReportError;
import com.runqian.report4.dataset.DataSet;
import com.runqian.report4.dataset.IDataSetFactory;
import com.runqian.report4.dataset.Row;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.CustomDataSetConfig;
import com.runqian.report4.usermodel.DataSetConfig;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.treequery.DimKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;

/**
 * 维度自定义数据集工厂
 */
public class DimKeyDataSet implements IDataSetFactory {
	
	/**
	 * 自定义数据集中的参数
	 */

	public DataSet createDataSet(Context ctx, DataSetConfig dsc, boolean retrieve) {
		String dim_key=null;
		String query_id=null;
		String rep_id=null;
        String test_query_id=null; 
        try {
        	//提取自定义数据集中rep_id,test_query_id
	        CustomDataSetConfig cdsc = (CustomDataSetConfig) dsc;
			String[] argnames=cdsc.getArgNames();
			String[] argvalues=cdsc.getArgValue();
			if(argnames!=null){			 
				for(int i=0;i<argnames.length;i++){
					if(argnames[i].equals(ReportConstants.DIM_KEY))
						dim_key=argvalues[i];
				}
			}
			rep_id=(String)ctx.getParamValue(ReportConstants.REP_ID);
			test_query_id=(String)ctx.getParamValue(ReportConstants.TEST_QUERY_ID);
			//验证rep_id
			if(rep_id==null||rep_id.equals(""))
				throw new Exception("rep_id is null:自定义数据集的参数rep_id未定义");
			if(dim_key==null||dim_key.equals(""))
				throw new Exception("dim_key is null:自定义数据集的参数dim_key未定义");
			
			//模板定义的参数query_id
			query_id=(String) ctx.getParamValue(ReportConstants.QUERY_ID);
			
			//query_id为空说明是在测试，使用测试数据
			if(query_id==null||query_id.equals("")){
				QuieeConstants.initTestDimKey(test_query_id);
			}
			
			DimKey dim=DimKeyContainer.getDimKey(dim_key);
			if(dim==null)
				throw new Exception("dim_key:"+dim_key+" is not config.");
			
			DataSet ds=new DataSet(dim.getDesc());
			
			ds.addCol("key");
			ds.addCol("desc");
			if(retrieve){
				//取数据集内容
			    while(dim!=null){
			    	Row row=ds.addRow();
			    	row.setData(1, dim.getKey());
			    	row.setData(2, dim.getDesc());
			    	dim=DimKeyContainer.getDimKey(dim.getPkey());
			    }
			}
			return ds;
		} catch (Exception e) {			
			throw new ReportError("DimKeyDataSet_Error "+e.getMessage(),e);
		}
	}

}
