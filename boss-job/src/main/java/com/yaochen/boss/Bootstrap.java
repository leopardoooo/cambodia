/*
 * @(#)Bootstrap.java 1.0.0 Jun 16, 2011 3:02:04 PM 
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.yaochen.boss;

import java.net.ServerSocket;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 声明式启动任务
 * 
 * @author hh
 * @version 1.0, Jun 16, 2011
 */
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		//证书相关设置
		//System.setProperty("javax.net.debug", "ssl,handshake");
		/*改成你本地的地址
        System.setProperty("javax.net.ssl.keyStore", "D:/ycsoft/ssl/server.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
        // 改成你本地的地址
		System.setProperty("javax.net.ssl.trustStore", "D:/ycsoft/ssl/cfocn.ca.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        **/
		//Keep only run single application 
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					ServerSocket aa =new ServerSocket(15678);
					aa.accept();
				}catch(Exception e){
					e.printStackTrace();
					System.exit(0);
				}
			}	
		}).start();
		
		final String[] xmlFiles = new String[] { "spring-beans.xml",
				"spring-jobs.xml", "spring-client.xml" };
		
		new ClassPathXmlApplicationContext(xmlFiles);
	}

	
}