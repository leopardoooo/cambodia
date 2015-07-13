package com.ycsoft.quiee;

import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.model.expression.Variant2;
import com.runqian.report4.usermodel.Context;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.treequery.DimKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;

/**
 * 提取key对应idname数据中id对应的name信息
 */
public class TransCellName  extends Function{

	@Override
	public Object calculate(Context ctx, boolean inputValue) {
		// 判断参数个数
		if (this.paramList.size() != 2) 
			throw new ReportError("TransCellName: function.missingParam:TransCellName(value(),key)");
		
		String test_query_id=(String) ctx.getParamValue(ReportConstants.TEST_QUERY_ID);
		String query_id=(String)ctx.getParamValue(ReportConstants.QUERY_ID);
		if(query_id==null||query_id.equals("")){
			try {
				QuieeConstants.initTestDimKey(test_query_id);
			} catch (Exception e1) {
				throw new ReportError("TransCellName:init_test_query_error"+e1.getMessage());
			}
		}
		//取第一个参数的值
		Expression param1=(Expression)this.paramList.get(0);
		Object result1 = Variant2.getValue(param1.calculate(ctx, inputValue), false, inputValue);
        String id=result1.toString();
		
		//函数的第二个参数的值
		String key=this.paramList.get(1).toString();
		
		//查询头选择的维度值
		String key_ctx=(String) ctx.getParamValue(key);
		DimKey dim_ctx=DimKeyContainer.getDimKey(key_ctx);
		
		//该模板配置的维度值
		DimKey dim=    DimKeyContainer.getDimKey("#"+key+"#");
		
		if(dim==null)
			throw new ReportError("TransCellName(value(),key) key is null or "+key+" is undefined");
		try {
			if(dim_ctx!=null&&!dim_ctx.equals(dim)){
				DimKey pdim=DimKeyContainer.getDimKey(dim.getPkey());								
				while(!dim_ctx.equals(pdim)){
					if(pdim==null)
						throw new ReportError("TransCellName: function.ParamError:第二个参数"+key+"和参数模板中配置的变量不一致");					
					pdim=DimKeyContainer.getDimKey(pdim.getPkey());
				}
				return pdim.getName(id);
			}else{
				return dim.getName(id);
			}
		} catch (ReportError e) {
			throw e;
		}catch (Exception e) {
			throw new ReportError("DimKeyName:report_error_"+e.getMessage());
		}
	   
	}

}
