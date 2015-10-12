/*
 * @(#) BOSSWebServiceTestMain.java 1.0.0 2015年10月12日 上午10:10:25
 */
package com.ycsoft.boss.remoting.backtask;

/**
 * 
 * @author Killer
 */
public class BOSSWebServiceTestMain {

	public static void main(String[] args) {
		
		System.setProperty("javax.net.debug", "ssl,handshake");
        System.setProperty("javax.net.ssl.keyStore", "/Users/killer/Documents/MyWorks/GitHub/cambodia/boss-core/src/main/resources/cert/client/client.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
		System.setProperty("javax.net.ssl.trustStore", "/Users/killer/Documents/MyWorks/GitHub/cambodia/boss-core/src/main/resources/cert/server/ca.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		
        
        // TODO 接口方法调用： 
        // 参考 BOSSWebServiceSoapImplServiceTest的test方法
        
        

	}

}
