/*
 * @(#) BOSSWebServiceTestMain.java 1.0.0 2015年10月12日 上午10:10:25
 */
package com.ycsoft.boss.remoting.backtask;

import org.apache.axis2.AxisFault;

import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrder;
import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.WorkOrderResp;

/**
 * 
 * @author Killer
 */
public class BOSSWebServiceTestMain {

	public static void main(String[] args) {
		
		System.setProperty("javax.net.debug", "ssl,handshake");
       // System.setProperty("javax.net.ssl.keyStore", "/Users/killer/Documents/MyWorks/GitHub/cambodia/boss-core/src/main/resources/cert/client/client.p12");
        System.setProperty("javax.net.ssl.keyStore", "E:/MyWork/workspace_st/cambodia/boss-core/src/main/resources/cert/client/client.p12");
        
        System.setProperty("javax.net.ssl.keyStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
		//System.setProperty("javax.net.ssl.trustStore", "/Users/killer/Documents/MyWorks/GitHub/cambodia/boss-core/src/main/resources/cert/client/ca.p12");
		System.setProperty("javax.net.ssl.trustStore", "E:/MyWork/workspace_st/cambodia/boss-core/src/main/resources/cert/client/ca.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
		
        
        // TODO 接口方法调用： 
        // 参考 BOSSWebServiceSoapImplServiceTest的test方法
        try {
			com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub stub =
			        new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub();
			com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE returnWorkOrder8 =
		            new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE();
			
			WorkOrderResp arg=new WorkOrderResp();
			// 工单编号
			arg.setOrderNo("123");
	    	// 完工类型
	    	arg.setRespType("QC");
	    	// 回执消息, 如果失败的情况
	    	arg.setRespMsg("你好啊OK");
	    	
			ReturnWorkOrder param=new ReturnWorkOrder();
			param.setArg0(arg);
			returnWorkOrder8.setReturnWorkOrder(param);
			stub.returnWorkOrder(returnWorkOrder8);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
        

	}

}
