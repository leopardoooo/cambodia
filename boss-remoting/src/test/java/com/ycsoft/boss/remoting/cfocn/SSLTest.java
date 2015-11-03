/*
 * @(#) SSLTest.java 1.0.0 2015年9月17日 下午8:39:26
 */
package com.ycsoft.boss.remoting.cfocn;

import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.TestSSL;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.TestSSLResponse;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.WorkOrder;
import com.ycsoft.commons.helper.JsonHelper;

/**
 * 
 * @author Killer
 */
public class SSLTest {
	
	public static void main(String[] args) throws Exception{
		// DEBUG模式
        System.setProperty("javax.net.ssl.keyStore", "D:/ycsoft/ssl/server.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
        //TODO 改成你本地的地址
		System.setProperty("javax.net.ssl.trustStore", "D:/ycsoft/ssl/cfocn.ca.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		
		
		CFOCN_WebSvc_WorkOrderStub stub = new CFOCN_WebSvc_WorkOrderStub();
		//TestSSL testSsl = new TestSSL();
		//testSsl.setParam1("Hello");
		
		//TestSSLResponse response = stub.testSSL(testSsl);
		com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.GetWorkOrder getWorkOrder=new 
				com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.GetWorkOrder();
		getWorkOrder.setOrderNo("10104110");//10104110
		
		com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.GetWorkOrderResponse res=
				stub.getWorkOrder(getWorkOrder);
		//System.out.println(JsonHelper.fromObject(res));
		WorkOrder or=res.getGetWorkOrderResult();
		//or.get
		System.out.println(JsonHelper.fromObject(or));
		
		//System.out.println("服务器返回的结果：" + response.getTestSSLResult());
	}

}
