package com.ycsoft.http;

import java.util.Collection;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 抽象的HTTP请求实现
 *
 * @author Killer
 */
public abstract class AbstractHttpClient implements HttpClient{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 默认的请求类型 */
	private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON;
	
	/**
	 * a template implementation
	 * 
	 * @see #doSendRequest(CloseableHttpClient, Collection)
	 */
	public Collection<ResponseBody> doPost(Collection<HttpTask> tasks)
			throws Throwable {
		if(tasks == null || tasks.size() == 0 ){
			logger.warn("The request task is empty.");
			return null;
		}
		logger.info("A total of "+ tasks.size() +" HTTP requests.");
		CloseableHttpClient httpClient = createCloseableHttpClient();
		try{
			return this.doSendRequest(httpClient, tasks);
		}finally{
			if(httpClient != null){
				httpClient.close();
			}
		}
		
	}
	
	/**
	 * 实现类负责实现具体的发送请求
	 * 
	 * @param httpClient
	 * @param tasks
	 * @return
	 * @throws Throwable
	 */
	public abstract Collection<ResponseBody> doSendRequest(
			CloseableHttpClient httpClient, Collection<HttpTask> tasks)
			throws Throwable;

	protected ContentType getContentType(){
		return DEFAULT_CONTENT_TYPE;
	}

	protected CloseableHttpClient createCloseableHttpClient(){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		return httpClient;
	}
}
