package com.ycsoft.boss.remoting.cfocn;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TaskCustExtInfo;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ArrayOfDeviceInfo;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ArrayOfProductInfo;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.DeviceInfo;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ManuallyInfluencedWorkOrder;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ProductInfo;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ReceiveWorkOrder;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ResultHead;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.WorkOrder;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;



public class WorkOrderClient {
	private CFOCN_WebSvc_WorkOrderStub workOrderStub;
	private static Logger LOG = LoggerFactory.getLogger(WorkOrderClient.class);
	
	private final String INFL_TYPE_CANCEL="Cancle";//撤销工单
	private final String INFL_TYPE_REMIND="Remind";//催单
	private final String INFL_TYPE_CONTACT="Contact";//联系单
	
	private final String ISP_CODE="1";
	
	
	public WorkOrderClient (){
		try {
			workOrderStub = new CFOCN_WebSvc_WorkOrderStub();
		} catch (AxisFault e) {
			LOG.error("创建CNFON工单 Stub时错误，请检查网络是否通畅!", e);
		}
	}
	
	public boolean createTaskService(WTaskBaseInfo task,List<WTaskUser> userList,TaskCustExtInfo extInfo) throws WordOrderException{
		ReceiveWorkOrder receiveWorkOrder = new ReceiveWorkOrder();
		//设置工单基本信息
		WorkOrder workOrder = getWorkOrderBaseInfo(task, extInfo.getCust_no(),extInfo.getArea_code(),extInfo.getCustManager(),userList);
		//设置设备信息
		ArrayOfProductInfo productArray = getDeviceInfo(userList);
		workOrder.setProductInfos(productArray);
		receiveWorkOrder.setMWorkOrder(workOrder);
		try {
			if(LOG.isDebugEnabled()){
				LOG.debug(JsonHelper.fromObject(receiveWorkOrder));
			}
		} catch (Exception e) {e.printStackTrace();}
		
		return createTaskService(receiveWorkOrder);
	}

	private ArrayOfProductInfo getDeviceInfo(List<WTaskUser> userList) {
		ArrayOfProductInfo productArray = new ArrayOfProductInfo();
		ProductInfo product = new ProductInfo();
		product.setProductName("supernet");
		//product.setProductName("supernet");
		
		ArrayOfDeviceInfo deviceArray = new ArrayOfDeviceInfo();
		for (WTaskUser user:userList){
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceName(user.getDevice_model());
			if(StringHelper.isNotEmpty(user.getDevice_id())){
				deviceInfo.setDeviceSN(user.getDevice_id());//设备编号
				
			}
			if(StringHelper.isNotEmpty(user.getOcc_no())){
				deviceInfo.setOCCSerialCode(user.getOcc_no());//交接箱编号
			}
			if(StringHelper.isNotEmpty(user.getPos_no())){
				deviceInfo.setPOSSerialCode(user.getPos_no());//分光器编号
			}
			
			deviceInfo.setIsFCPort(SystemConstants.USER_TYPE_BAND.equals(user.getUser_type())?true:false);
			deviceArray.addDeviceInfo(deviceInfo);
		}
		product.setDeviceInfos(deviceArray);
		productArray.addProductInfo(product);
		return productArray;
	}

	private WorkOrder getWorkOrderBaseInfo(WTaskBaseInfo task, String custNo,String areaCode,SOptr custManager,List<WTaskUser> userList) {
		WorkOrder order = new WorkOrder();
		order.setOrderNo(task.getTask_id());
		order.setOrderType(Integer.parseInt(task.getTask_type_id()));
		order.setAreaCode(areaCode);
		order.setISPCode(ISP_CODE);
		
		order.setUserAddress(task.getNew_addr());
		order.setUserNo(custNo);
		order.setUserName(task.getCust_name());
		if(StringHelper.isNotEmpty(task.getMobile())){
			order.setUserTel(task.getTel()+"*"+task.getMobile());
		}else{
			order.setUserTel(task.getTel());
		}
		
		StringBuilder OrderContent=new StringBuilder();
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL)){
			 //客户编号
			OrderContent.append("客户编号CustNo:").append(custNo);
		    //安装的设备类型数量 :   安装设备 ONT:1 OTT:2
			this.fillTaskDeviceInfo(OrderContent, userList, "安装设备Install:");
		    //带宽    账号  密码
			this.fillTaskBroadbandInfo(OrderContent, userList, true);
		    //客户经理 
		}else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_MOVE)){
			OrderContent.append("客户编号CustNo:").append(custNo);
			this.fillTaskBroadbandInfo(OrderContent, userList, false);
		}
		else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_LINE)){
			OrderContent.append("客户编号CustNo:").append(custNo);
			this.fillTaskDeviceInfo(OrderContent, userList, "拆除线路WriteoffLine:");
			this.fillTaskBroadbandInfo(OrderContent, userList, false);
		}else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT)){
			OrderContent.append("客户编号CustNo:").append(custNo);
			this.fillTaskBroadbandInfo(OrderContent, userList, false);
			OrderContent.append("\n").append("故障现象BugDetail:").append(task.getBug_detail());
		}else {
			OrderContent.append("other");
		}
		
		order.setOrderContent(OrderContent.toString());
		
		//TODO 预约上门时间
		//order.setArriveTime(DateHelper.formatNowTime());
		//创建时间
		order.setCreateTime(DateHelper.formatNowTime());
		//上门施工截止日期
		order.setDealLine(DateHelper.format(DateHelper.addDate(new Date(), 7)));
		order.setOrderStatus("0");
		order.setPublisherNo("admin");
		//设置客户经理信息
		if (custManager != null){
			JsonObject managerInfo = new JsonObject();
			managerInfo.addProperty("custManagerName", custManager.getOptr_name());
			if(StringHelper.isNotEmpty(custManager.getMobile())){
				managerInfo.addProperty("mobile", custManager.getTel()+"*"+custManager.getMobile());
			}else{
				managerInfo.addProperty("mobile", custManager.getTel());
			}
			order.setAttachData(managerInfo.toString());
		}
		return order;
	}
	private void fillTaskBroadbandInfo(StringBuilder OrderContent,List<WTaskUser> userList,boolean needPD){
		for(WTaskUser taskUser: userList){
	    	if(SystemConstants.USER_TYPE_BAND.equals(taskUser.getUser_type())){
	    		OrderContent.append("\n").append("宽带Broadband:").append(taskUser.getBandwidth())
	    		.append(" 账号Loginname:").append(taskUser.getLogin_name());
	    		if(needPD){
	    			OrderContent.append(" 密码Password").append(taskUser.getPassword());
	    		}
	    	}
	    }
	}
	private void  fillTaskDeviceInfo(StringBuilder OrderContent,List<WTaskUser> userList,String taskTypeInfo){
		int ont_cnt=0;
		int ott_cnt=0;
	    for(WTaskUser taskUser: userList){
	    	if(SystemConstants.USER_TYPE_BAND.equals(taskUser.getUser_type())){
	    		ont_cnt++;
	    	}
	    	if(SystemConstants.USER_TYPE_OTT.equals(taskUser.getUser_type())){
	    		ott_cnt++;
	    	}
	    }
	    if(ont_cnt+ott_cnt>0){
	    	OrderContent.append("\n").append(taskTypeInfo);
	    	if(ont_cnt>0){
	    		OrderContent.append(" ONT:").append(ont_cnt);
	    	}
	    	if(ott_cnt>0){
	    		OrderContent.append(" OTT:").append(ott_cnt);
	    	}
	    }
	}
	
	private boolean createTaskService(final ReceiveWorkOrder workOrder) throws WordOrderException{
		return applyTemplate(new Callback(){

			@Override
			public ResultHead doCallback() throws RemoteException {
				return workOrderStub.receiveWorkOrder(workOrder).getReceiveWorkOrderResult();
			}
		});
	}
	
	/**
	 * 取消工单
	 * @param doneCode
	 * @param taskId
	 * @return
	 * @throws WordOrderException
	 */
	public boolean cancelTaskService(int doneCode,String taskId) throws WordOrderException{
		ManuallyInfluencedWorkOrder influence = new ManuallyInfluencedWorkOrder();
		influence.setWorkOrderNo(taskId);
		influence.setNo(""+doneCode);
		influence.setType(INFL_TYPE_CANCEL);
		influence.setContent("cancel work order "+taskId);
		try {
			if(LOG.isDebugEnabled()){
				LOG.debug(JsonHelper.fromObject(influence));
			}
		} catch (Exception e) {e.printStackTrace();}
		return cancelTaskService(influence);
	}
	
	private boolean cancelTaskService(final ManuallyInfluencedWorkOrder influence) throws WordOrderException{
		return applyTemplate(new Callback(){

			@Override
			public ResultHead doCallback() throws RemoteException {
				return workOrderStub.manuallyInfluencedWorkOrder(influence).getManuallyInfluencedWorkOrderResult();
			}
		});
	}
	
	private boolean applyTemplate(Callback callback)throws WordOrderException{
		ResultHead result = null;
		try {
			result = callback.doCallback();
			//System.out.println("============================="+result.getHeadCode()+"      "+result.getHeadMsg());
		} catch (RemoteException e) {
			throw new WordOrderException(e);
		}
		if(WordOrderResultUtils.success(result)){	
			return true;
		}else{
			throw new WordOrderException(result);
		}
	}
	
	private interface Callback{
		ResultHead doCallback()throws RemoteException;
	} 

}
