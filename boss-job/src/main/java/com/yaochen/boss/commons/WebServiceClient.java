/*
 * @(#) WebServiceClient.java 1.0.0 Aug 2, 2011 12:55:49 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yaochen.boss.commons;

import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.xfire.client.Client;

/**
 * Xfire dynamic client invoke way
 *
 * @author allex
 * @since 1.0
 */
public class WebServiceClient {
	
	private Client client;
	
	private String wsdlUrl;
	
	/**
	 * 调用Web Services 接口函数，根据函数名称和方法
	 * @param methodName 方法名称
	 * @param params 方法参数值，顺序依据远程接口方法的参数顺序
	 */
	public int remoteInvoke(String methodName, Object ... params) 
													throws MalformedURLException, Exception{
		if(this.client == null){
			client = new Client(new URL(this.wsdlUrl));
		}
		Object[] results = client.invoke(methodName, params); 
		return Integer.parseInt(results[0].toString()) ;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	
	public static void main(String[] args) throws Exception{
		WebServiceClient client = new WebServiceClient();
		client.setWsdlUrl("http://203.93.210.27/services/CardCharge?wsdl");
		int rs;
		//开户
//		  rs = client.remoteInvoke("CardNewUser2", "tr123", 67, 4, "123456");
		  rs = client.remoteInvoke("CardNewUser3", "tr123", 67, 4, "123456","测试客户","1301001212","台城镇台山一中","2030-01-01","1","2011-09-20","测试开户");
		//销户
//		  rs = client.remoteInvoke("CardDelUser", "tr123");
		System.out.println(rs);
	}
}
