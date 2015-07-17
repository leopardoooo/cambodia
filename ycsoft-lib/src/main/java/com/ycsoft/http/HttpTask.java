package com.ycsoft.http;


/**
 *
 * Http请求任务
 *
 * @author Killer
 */
public class HttpTask {

	/** 完整的URL*/
	private String requestUrl;
	
	/** 请求json数据 */
	private String jsonData;

	public HttpTask(String requestUrl, String jsonData){
		this.requestUrl = requestUrl;
		this.jsonData = jsonData;
	}
	
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
}
