/*
 * @(#) SSLTest.java 1.0.0 2015年9月17日 下午8:39:26
 */
package com.ycsoft.boss.remoting.cfocn;

import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.TestSSL;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.TestSSLResponse;

/**
 * 
 * @author Killer
 */
public class SSLTest {
	
	public static void main(String[] args) throws Exception{
		
		System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStore", "/Users/killer/Downloads/工单/admin.pfx");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
		System.setProperty("javax.net.ssl.trustStore", "/Users/killer/Downloads/工单/cfocn.ca.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		
		
		CFOCN_WebSvc_WorkOrderStub stub = new CFOCN_WebSvc_WorkOrderStub();
		TestSSL testSsl = new TestSSL();
		testSsl.setParam1("Hello");
		
		TestSSLResponse response = stub.testSSL(testSsl);
		System.out.println("服务器返回的结果：" + response.getTestSSLResult());
	}

}