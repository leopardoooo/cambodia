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
		//Keep only run single application 
//	    FileLock lck = new FileOutputStream("flagFile").getChannel().tryLock();         
//	    if(lck == null) {
//	      System.out.println("另外一个进程已经运行中....");
//	      System.exit(1);
//	    }
		
		//System.setProperty("javax.net.debug", "ssl,handshake");
		//TODO 改成你本地的地址
        System.setProperty("javax.net.ssl.keyStore", "D:/boss/cambodia/boss-core/src/main/resources/cert/cfocn/admin.pfx");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
        //TODO 改成你本地的地址
		System.setProperty("javax.net.ssl.trustStore", "D:/boss/cambodia/boss-core/src/main/resources/cert/cfocn/cfocn.ca.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
				
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