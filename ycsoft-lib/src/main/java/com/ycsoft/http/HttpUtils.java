package com.ycsoft.http;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.alibaba.fastjson.JSON;

/**
 * Http 协议的远程调用工具类
 * 
 * @author Killer
 */
public class HttpUtils {

	/** 串行化的实现类 */
	private static HttpClient client = new HttpClientOrderly();

	/**
	 * 发送一组http请求，采用有序的串行化请求
	 * 
	 * @param tasks 封装好的task
	 * @return
	 */
	public static Collection<ResponseBody> doPost(Collection<HttpTask> tasks)
			throws Throwable {
		return client.doPost(tasks);
	}
	
	/**
	 * 发送单个Http请求任务
	 * @param task
	 * @return
	 * @throws Throwable
	 */
	public static ResponseBody doPost(HttpTask task)
			throws Throwable {
		Collection<HttpTask> tasks = Arrays.asList(task);
		Collection<ResponseBody> responseList = client.doPost(tasks);
		Iterator<ResponseBody> ite = responseList.iterator();
		if(ite.hasNext()){
			return ite.next();
		}
		return null;
	}
	
	public static ResponseBody doPost(String requestUrl, String jsonDataString)throws Throwable{
		return doPost(new HttpTask(requestUrl, jsonDataString));
	}
	
	public static ResponseBody doPostObject(String requestUrl, Object data)
			throws Throwable {
		String jsonDataString = "";
		if(data != null){
			jsonDataString = JSON.toJSONString(data);
		}
		return doPost(requestUrl, jsonDataString);
	}
}
