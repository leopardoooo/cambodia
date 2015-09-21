package com.ycsoft.boss.remoting.cfocn;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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



public class WorkOrderClient {
	private CFOCN_WebSvc_WorkOrderStub workOrderStub;
	private Logger LOG = LoggerFactory.getLogger(getClass());
	
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
	
	public boolean createTaskService(WTaskBaseInfo task,String custNo,String areaCode,List<WTaskUser> userList) throws WordOrderException{
		ReceiveWorkOrder receiveWorkOrder = new ReceiveWorkOrder();
		//设置工单基本信息
		WorkOrder workOrder = getWorkOrderBaseInfo(task, areaCode);
		//设置设备信息
		ArrayOfProductInfo productArray = getDeviceInfo(userList);
		workOrder.setProductInfos(productArray);
		receiveWorkOrder.setMWorkOrder(workOrder);
		return createTaskService(receiveWorkOrder);
	}

	private ArrayOfProductInfo getDeviceInfo(List<WTaskUser> userList) {
		ArrayOfProductInfo productArray = new ArrayOfProductInfo();
		ProductInfo product = new ProductInfo();
		product.setProductName("supernet");
		product.setProductName("supernet");
		
		ArrayOfDeviceInfo deviceArray = new ArrayOfDeviceInfo();
		for (WTaskUser user:userList){
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceName(user.getDevice_model()+"("+user.getDevice_model_text()+")");
			deviceArray.addDeviceInfo(deviceInfo);
		}
		product.setDeviceInfos(deviceArray);
		productArray.addProductInfo(product);
		return productArray;
	}

	private WorkOrder getWorkOrderBaseInfo(WTaskBaseInfo task, String areaCode) {
		WorkOrder order = new WorkOrder();
		order.setOrderType(Integer.parseInt(task.getTask_type_id()));
		order.setAreaCode(areaCode);
		order.setISPCode(ISP_CODE);
		order.setUserAddress(task.getOld_addr());
		order.setUserName(task.getCust_name());
		order.setUserTel(task.getMobile());
		if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_INSTALL))
			order.setOrderContent("install");
		else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_MOVE))
			order.setOrderContent("move");
		else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_WRITEOFF_LINE))
			order.setOrderContent("writeoff");
		else if (task.getTask_type_id().equals(SystemConstants.TASK_TYPE_FAULT))
			order.setOrderContent(task.getBug_detail());
		else 
			order.setOrderContent("other");
		
		order.setArriveTime(DateHelper.formatNowTime());
		order.setOrderStatus("0");
		order.setPublisherNo("admin");
		return order;
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
