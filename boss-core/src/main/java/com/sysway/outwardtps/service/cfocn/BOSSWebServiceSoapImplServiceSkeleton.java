/**
 * BOSSWebServiceSoapImplServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.sysway.outwardtps.service.cfocn;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.service.impl.SnTaskService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;

/**
 *  BOSSWebServiceSoapImplServiceSkeleton java skeleton for the axisService
 */
public class BOSSWebServiceSoapImplServiceSkeleton
    implements BOSSWebServiceSoapImplServiceSkeletonInterface {
	private static Logger LOG = LoggerFactory.getLogger(BOSSWebServiceSoapImplServiceSkeleton.class);
	private SnTaskService snTaskService;
	
	/**
     * @param replyManuallyInfluencedWorkOrder0
     * @return replyManuallyInfluencedWorkOrderResponse1
     */
    public com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE replyManuallyInfluencedWorkOrder(
        com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE replyManuallyInfluencedWorkOrder0) {
        throw new java.lang.UnsupportedOperationException("Please implement " +
            this.getClass().getName() + "#replyManuallyInfluencedWorkOrder");
    }

    /**
     * 工单完成后，会调用该接口通知boss
     * 
     * RespType	回执类号	String	QC工单完成 QD工单未完成 
     * WorkerOrderNo	工单号	String	
     * ISPCode	运营商编号	String	
     * UserNo	用户编号	String	
     * RespMsg	回执描述	String	
     * AttachData	附加数据	String	便于以后扩展，数据格式未定
     * 
     * HeadCode	返回代码	String	SUCCESS接收通知成功。FAIL接收通知失败。原因在HeadMsg说明
     * HeadMsg	代码描述	String	
     * 
     * @param returnWorkOrder2
     * @return returnWorkOrderResponse3
     */
    public com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE returnWorkOrder(
        com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE returnWorkOrder2) {
    	WorkOrderResp resp = returnWorkOrder2.getReturnWorkOrder().getArg0();
    	// 工单编号
    	String taskId = resp.getOrderNo();
    	// 完工类型
    	String resultType = resp.getRespType();
    	// 回执消息, 如果失败的情况
    	String msg = resp.getRespMsg();
    	if(StringHelper.isNotEmpty(resp.getAttachData())){
    		msg=msg+";"+resp.getAttachData();
    	}
    	
    	
     	try {
     		if(LOG.isDebugEnabled()){
     			LOG.debug(JsonHelper.fromObject(resp));
     		}
		} catch (Exception e1) {e1.printStackTrace();}
    	
    	try{
    		// 调用boss接口完成工单
    		snTaskService.setParam(getServiceParam());
    		snTaskService.finishTask(taskId, resultType, "", "",msg,false);
    		// 返回成功的结果
    		return createReturnWorkOrderResponse(createResultHeadForSuccess());
    	}catch(Exception e){
    		try {
    			LOG.info(JsonHelper.fromObject(resp));
    		} catch (Exception e1) {e1.printStackTrace();}
    		LOG.error("cfocn完工失败，工单编号:"+taskId,e);
    		try {
    			snTaskService.setParam(getServiceParam());
				snTaskService.saveErrorLog(taskId,BusiCodeConstants.TASK_FINISH, e.getMessage());
			} catch (Exception e1) {
				LOG.error("记录cfocn调用BOSS接口错误日志异常",e);
			}
    		// 返回失败的结果
    		return createReturnWorkOrderResponse(createResultHeadForFail(e));
    	}
    }
    
    private BusiParameter getServiceParam(){
    	SOptr optr=new SOptr();
    	optr.setOptr_id("249");
    	optr.setLogin_name("jgsg001");
    	optr.setDept_id("3");
    	optr.setCounty_id("4501");
    	optr.setArea_id("4500");
    	BusiParameter busiParameter=new BusiParameter();
    	busiParameter.setOptr(optr);
    	return busiParameter;
    }
    private ReturnWorkOrderResponseE createReturnWorkOrderResponse(ResultHead head){
    	ReturnWorkOrderResponseE response = new ReturnWorkOrderResponseE();
    	
    	ReturnWorkOrderResponse body = new ReturnWorkOrderResponse();
    	body.set_return(head);
    	
    	response.setReturnWorkOrderResponse(body);
    	
    	return response;
    }
    

    /**
     * 
     * 设备回填
     * 
     * workOrderNo： 单号
     * type = Normal 正常设备回填  （需要回填全部设备）
     * type = Replace 正常回填后，需要再次更换设备（比如调试过程发现设备坏了，只需要回填更换的设备） 
     * 
     * productInfos ProductInfo[] : 见收单的参数说明
     * ProductName	产品名称	String	产品名称	不为空	
     * ProductCode	产品编码	String		不为空	
     * 
     * DeviceInfos		DeviceInfo[]		可为空	
     * DeviceName	备名称	String	设备名称	不为空
     * DeviceSN	设备标识号	String	设备标识号	不为空
     * OriginalDeviceType	原设备名称	String	原设备名称	可空（更换设备时不为空）	
     * OriginalDeviceSN	原设备标识号	String	原设备标识号	可空（更换设备时不为空）	
     * 
     * @param deviceFeedBack4
     * @return deviceFeedBackResponse5
     */
    public com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE deviceFeedBack(
        com.sysway.outwardtps.service.cfocn.DeviceFeedBackE deviceFeedBack4) {
    	DeviceFeedBack dfb = deviceFeedBack4.getDeviceFeedBack();
    	
    	// 工单编号
    	String taskId = dfb.getArg0();
    	String type = dfb.getArg1();//无效参数；原意是标明本次是新装还是变更设备，实际上并没有这么使用
    	
    	
		try {
     		if(LOG.isDebugEnabled()){
     			LOG.debug(JsonHelper.fromObject(dfb));
     		}
		} catch (Exception e1) {e1.printStackTrace();}
		

    	// 设备信息
    	ProductInfo[] prodArray = dfb.getArg2();
    	List<TaskFillDevice> devices = new ArrayList<>();
    	for(ProductInfo prodInfo:prodArray){
	    	for (DeviceInfo deviceInfo : prodInfo.getDeviceInfos()) {
	    		TaskFillDevice device = new TaskFillDevice();
	    		device.setDeviceCode(deviceInfo.getDeviceSN());
	    		device.setFcPort(deviceInfo.getIsFCPort());
	    		device.setOldDeviceCode(deviceInfo.getOriginalDeviceSN());
	    		if (device.isFcPort()){
	    			device.setOccNo(deviceInfo.getOCCSerialCode());//交接箱编号
	    			device.setPosNo(deviceInfo.getPOSSerialCode());//分光器编号
	    		}
	    		
	    		devices.add(device);
	    	}
    	}
    	
    	try {
    		snTaskService.setParam(getServiceParam());
			snTaskService.fillTask(taskId, devices);
			
			return createDeviceFeedBackResponse(createResultHeadForSuccess());
		} catch (Exception e) {
			try {	     		
	     		LOG.info(JsonHelper.fromObject(dfb));	 
			} catch (Exception e1) {e1.printStackTrace();}
			LOG.error("cfocn回填失败，工单编号："+taskId,e);
			try {
				snTaskService.setParam(getServiceParam());
				snTaskService.saveErrorLog(taskId, BusiCodeConstants.TASK_FILL,e.getMessage());
			} catch (Exception e1) {
				LOG.error("记录cfocn调用BOSS接口错误日志异常",e);
			}
			return createDeviceFeedBackResponse(createResultHeadForFail(e));
		}
    	
    }
    
    private DeviceFeedBackResponseE createDeviceFeedBackResponse(ResultHead head){
    	DeviceFeedBackResponseE dfbre = new DeviceFeedBackResponseE();
    	DeviceFeedBackResponse dfbr = new DeviceFeedBackResponse();
    	dfbr.set_return(head);
    	dfbre.setDeviceFeedBackResponse(dfbr);
    	
    	return dfbre;
    }
    
    /**
     * 创建一个返回头信息
     */
    private ResultHead createResultHead(boolean result, String msg){
    	ResultHead rh = new ResultHead();
    	rh.setHeadCode(result ? "SUCCESS" : "FAIL");
    	rh.setHeadMsg(msg);
    	
    	return rh;
    }
    
    private ResultHead createResultHeadForSuccess(){
    	return createResultHead(true, null);
    }
    
    private ResultHead createResultHeadForFail(Exception e){
    	return createResultHead(false, e.getMessage());
    }

	public void setSnTaskService(SnTaskService snTaskService) {
		this.snTaskService = snTaskService;
	}
}
