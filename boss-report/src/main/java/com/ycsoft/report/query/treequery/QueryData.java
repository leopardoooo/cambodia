package com.ycsoft.report.query.treequery;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.report.query.tree.QData;

/**
 * 报表多叉树的基础数据
 */
public class QueryData implements QData<List<Double>> {
	
	/**
	 * rep_column.olap_type=EXTEND的列对应的ID
	 * 例子：select county_id,.....from table 其中county_id就是key 
	 */
	private String key;
	//rep_column.olap_type=TOTAL的列
	private List<Double> data;
	/**
	 * rep_column.olap_type=EXTEND的列对应的ID对应的描述
	 */
	private String name;

	public String getName() {
		return name;
	}
	public QueryData(String key,String name,List<Double> data){
		this.key=key;
		this.data=data;
		this.name=name;
	}
	public List<Double> getData() {
		return data;
	}

	public String getKey() {
		return key;
	}

	/**
	 * 基础数据运算:
	 * rep_column.olap_type=TOTAL的列sum计算
	 */
	public void operationData(List<Double> data) {
		if(data!=null||this.data!=null){
			List<Double> list=new ArrayList<Double>(data.size());
			for(int i=0;i<this.data.size();i++){
				list.add(this.data.get(i)+data.get(i));
			}
			this.data=list;
		}
	}
	
}
