package com.ycsoft.boss.remoting.cfocn;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ManuallyInfluencedWorkOrder;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ReceiveWorkOrder;
import com.ycsoft.boss.remoting.cfocn.CFOCN_WebSvc_WorkOrderStub.ResultHead;



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
	
	private boolean createTaskService(WTaskBaseInfo task,String custNo,List<WTaskUser> userList) throws WordOrderException{
		ReceiveWorkOrder workOrder = new ReceiveWorkOrder();
		return createTaskService(workOrder);
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
