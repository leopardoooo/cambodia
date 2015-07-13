package com.ycsoft.test;

import com.runqian.report4.dataset.DataSet;
import com.runqian.report4.dataset.IDataSetFactory;
import com.runqian.report4.dataset.Row;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.CustomDataSetConfig;
import com.runqian.report4.usermodel.DataSetConfig;

public class MyDataSet implements IDataSetFactory {

	public DataSet createDataSet(Context ctx, DataSetConfig dsc, boolean arg2) {
		DataSet ds = new DataSet("ds1");

		ds.addCol("姓名"); // 列名
		ds.addCol("subject"); // 列名
		ds.addCol("GGG"); // 列名
		for (int i = 0; i < 10; i++) {
			Row row = ds.addRow();

			row.setData(1, "name" + i);
			row.setData(2, "subject" + i);
			row.setData(3, "grade" + i);
		}

		for(Object o: ctx.getAllParamMap().keySet().toArray())
			System.out.println(o.toString()+":"+ctx.getParamValue(o.toString()));
		
		System.out.println("报表传过来的参数值1：" + ctx.getParamValue("arg1"));

		System.out.println("报表传过来的参数值2：" + ctx.getParamValue("query_id"));
		
		CustomDataSetConfig cdsc = (CustomDataSetConfig) dsc; // 把数据集定义类转成自定义数据集类

		String[] args = cdsc.getArgNames(); // 获取自定义数据集传入参数名的集合

		String[] vals = cdsc.getArgValue(); // 获取自定义数据集传入参数值的集合

		int intValue = 0;

		Object expValue = null;

		System.out.println("自定义参数:"+(args==null?"null": args.length));
		if (args != null) {

			for (int i = 0; i < args.length; i++) { // 依次获取传入参数值

				String key = args[i];

				if (key == null)
					continue;

				String value = vals[i];

				if (key.equalsIgnoreCase("intValue")) { // 把传入参数转成整型

					intValue = Integer.parseInt(value);

				}
				//
				// else if(key.equalsIgnoreCase("expValue")){
				// //把传入参数解析成表达式并计算出表达式值
				//
				// Expression exp=new Expression(ctx,value);
				//
				// expValue=exp.calculate(ctx, isinput);
				//
				// }

				System.out.println("定义数据集时传入参数" + key + "的值是:" + value); // 打出传入参数值

				// ……… //主体代码

			}

		}

		return ds;

	}

}
