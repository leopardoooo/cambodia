package com.ycsoft.http;

import java.util.Collection;

/**
 *
 * 模拟HTTP请求接口定义
 *
 * @author Killer
 */
public interface HttpClient {

	
	/**
	 * 该方法定义了可传入一组请求任务，
	 * 具体是按顺序执行还是并发执行，则由实现类完成。
	 * 
	 * @param tasks
	 * @throws Throwable
	 */
	public Collection<ResponseBody> doPost(Collection<HttpTask> tasks)throws Throwable;
	
}
