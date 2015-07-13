package com.ycsoft.quiee;


import com.runqian.base4.resources.EngineMessage;
import com.runqian.base4.resources.MessageManager;
import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.model.expression.Variant2;
import com.runqian.report4.usermodel.Context;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.query.treequery.DimKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;

/**
 * dimkey粒度取值
 *
 */
public class DimKeySelect extends Function {

	/**
	 * 第一个参数 对应数据集的单元格数据
	 * 第二个参数 dim_key对应值 例如#countyid#
	 */
	@Override
	public Object calculate(Context ctx, boolean inputValue) {
		// 判断参数个数
		if (this.paramList.size() != 2) {
			MessageManager mm = EngineMessage.get();
			throw new ReportError("DimKeySelect:"
					+ mm.getMessage("function.missingParam:格式 dimkey(cellvalue,key)"));
		}
		//初始化环境
		String test_query_id=(String) ctx.getParamValue(ReportConstants.TEST_QUERY_ID);
		String query_id=(String)ctx.getParamValue(ReportConstants.QUERY_ID);
		if(query_id==null||query_id.equals("")){
			try {
				QuieeConstants.initTestDimKey(test_query_id);
			} catch (Exception e1) {
				throw new ReportError(e1.getMessage());
			}
		}
		//取第一个参数的值
		Expression param1=(Expression)this.paramList.get( 0 );
		Object result1 = Variant2.getValue(param1.calculate(ctx, inputValue), false, inputValue);
        String id=result1.toString();
		
		//第二个参数的值
		String key=this.paramList.get(1).toString();
		//对应的dim_key要和参数模板传递过来的值一致
		String key_ctx=(String) ctx.getParamValue(key);
		//System.out.println("key_ctx:"+key_ctx);
		DimKey dim_ctx=DimKeyContainer.getDimKey(key_ctx);
		DimKey dim=DimKeyContainer.getDimKey("#"+key+"#");
		
        
		if(dim==null){
			MessageManager mm = EngineMessage.get();
			throw new ReportError("DimKeySelect:"
					+ mm.getMessage("function.missingParam:格式 dimkey(cellvalue,key) key is null or "+key+" is undefined"));
		
		}
		try {
			if(dim_ctx!=null&&!dim_ctx.equals(dim)){
				DimKey pdim=DimKeyContainer.getDimKey(dim.getPkey());				
				id=dim.getPid(id);	
				while(!dim_ctx.equals(pdim)){
					if(pdim==null){
						MessageManager mm = EngineMessage.get();
						throw new ReportError("DimKeySelect:"
								+ mm.getMessage("function.ParamError:第二个参数"+key+"和参数模板中配置的变量不一致"));					
					}						 
					id=pdim.getPid(id);
					pdim=DimKeyContainer.getDimKey(pdim.getPkey());
				}
			}
		} catch (ReportError e) {
			throw e;
		}catch (Exception e) {
			throw new ReportError("DimKeySelect:"+e.getMessage());
		}
	    return id; 
	}
	
}
