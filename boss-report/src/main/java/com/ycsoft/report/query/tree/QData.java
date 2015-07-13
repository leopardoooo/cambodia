package com.ycsoft.report.query.tree;

import java.io.Serializable;
/**
 * 节点内容
 * @author new
 *
 * @param <T>
 */
public interface QData<T> extends Serializable {

	public String getKey();
	
	public String getName();

	public T getData();
	
	/**
	 * data 数据运算
	 * @param data
	 */
	public void operationData(T data);
}
