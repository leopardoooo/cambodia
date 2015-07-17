package com.ycsoft.http;

/**
 *
 * HTTP响应内容封装
 *
 * @author Killer
 */
public class ResponseBody {

	private String body;
	
	public ResponseBody(String body){
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	@Override
	public String toString() {
		return body;
	}
}
