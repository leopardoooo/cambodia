/*
 * @(#) BOSSWebServiceTestMain.java 1.0.0 2015年10月12日 上午10:10:25
 */
package com.ycsoft.boss.remoting.backtask;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceFeedBack;
import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceInfo;
import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ProductInfo;
import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrder;
import com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.WorkOrderResp;

/**
 * 
 * @author Killer
 */
public class BOSSWebServiceTestMain {

	public static void main(String[] args) {
		//E:/MyWork/workspace_st/cambodia
		System.setProperty("javax.net.debug", "ssl,handshake");
       // System.setProperty("javax.net.ssl.keyStore", "E:/MyWork/workspace_st/cambodia/boss-remoting/src/main/resources/backtask/boss-test-client.p12");
        System.setProperty("javax.net.ssl.keyStore", "D:/ycsoft/ssl/boss-test-client.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
		
		//System.setProperty("javax.net.ssl.trustStore", "E:/MyWork/workspace_st/cambodia/boss-core/src/main/resources/cert/client/ca.p12");
		System.setProperty("javax.net.ssl.trustStore", "D:/ycsoft/ssl/ca.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "a1234567");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		
        BOSSWebServiceTestMain aa=new BOSSWebServiceTestMain();
        //完工测试
        aa.testEnd("a123");
			


	}
	
	/**
	 * TODO 使用一个指定的上下文构造相应的Stub，详情请参考：
	 * https://issues.apache.org/jira/browse/AXIS2-3919
	 * 所有的stub的都必须使用指定文件系统的context进行构造，否则必须定时清除文件
	 * @return
	 * @throws Exception
	 */
	public static BOSSWebServiceSoapImplServiceStub createContextStub()throws Exception{
        ConfigurationContext axisConfigContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        BOSSWebServiceSoapImplServiceStub bss = new BOSSWebServiceSoapImplServiceStub(axisConfigContext);
        return bss;
	}
	
	//回退测试
	public void testFill(){
		try{
			com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub stub =
			        new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub();
			//回填测试
			com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceFeedBackE deviceFeedBack10
			=new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceFeedBackE();
			
			DeviceFeedBack param1=new DeviceFeedBack();
			param1.setArg0("10003317");//工单ID
	    	
	    	// 设备信息
	    	ProductInfo prodArray[] = new ProductInfo[1];
	    	prodArray[0]=new ProductInfo();
	    	List<DeviceInfo> DeviceInfos=new ArrayList<>();
	    	
	    	DeviceInfo d1=new DeviceInfo();
	    	DeviceInfos.add(d1);
	    	
	    	d1.setDeviceSN("SKW031501000178");//设备编号
	    	d1.setIsFCPort(false);//OTT
	    	d1.setOriginalDeviceSN(null);//原设备编号
	    	d1.setOCCSerialCode(null);//光路信息1
	    	d1.setPOSSerialCode(null);//光路信息2
	    	
	    	DeviceInfo d2=new DeviceInfo();
	    	DeviceInfos.add(d2);
	    	
	    	d2.setDeviceSN("ZTEGC06D2420");//设备编号
	    	d2.setIsFCPort(true);//光猫
	    	d2.setOriginalDeviceSN(null);//原设备编号
	    	d2.setOCCSerialCode("5555");//光路信息1
	    	d2.setPOSSerialCode("4443");//光路信息2
	    	
	    	prodArray[0].setDeviceInfos(DeviceInfos.toArray(new DeviceInfo[DeviceInfos.size()]));
	    	param1.setArg2(prodArray);
			deviceFeedBack10.setDeviceFeedBack(param1);			
			stub.deviceFeedBack(deviceFeedBack10);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	/**
	 * 完工测试
	 * @param taskId
	 */
	public void testEnd(String taskId){
		try{
			com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub stub =
		        new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub();
		
			//完工测试
			com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE returnWorkOrder8 =
		            new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE();
			
			WorkOrderResp arg=new WorkOrderResp();
			// 工单编号
			arg.setOrderNo(taskId);
	    	// 完工类型
	    	arg.setRespType("QC");
	    	// 回执消息, 如果失败的情况
	    	arg.setRespMsg("你好啊OK");
	    	
			ReturnWorkOrder param=new ReturnWorkOrder();
			param.setArg0(arg);
			returnWorkOrder8.setReturnWorkOrder(param);
			stub.returnWorkOrder(returnWorkOrder8);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
