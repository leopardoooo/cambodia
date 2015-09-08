/*
 * @(#) BOSSBandServiceAdapter.java 1.0.0 2015年7月20日 下午1:33:33
 */
package com.ycsoft.boss.remoting.aaa;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.AAASubscriberServiceInfo;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ActivateSubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.CancelSubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeactivateSubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeleteAAASubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ModifySubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.NewAAASubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.OrderSubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.QuerySubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.QuerySubscriberServiceResult;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.QuerySubscriberServiceResultMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResetAAASubscriberPswdRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResultHeader;


/**
 * 该接口与华为的WS进行交互，这里并不包含所有的接口，仅包含BOSS系统需要的业务接口。
 * 
 * @see AAARequestUtils
 * 
 * @author Killer
 */
public class BOSSBandServiceAdapter {

	private AAAInterfaceBusinessMgrServiceStub aaaStub;
	
	private Logger LOG = LoggerFactory.getLogger(getClass());
	
	public BOSSBandServiceAdapter(){
		try {
			aaaStub = new AAAInterfaceBusinessMgrServiceStub();
		} catch (AxisFault e) {
			LOG.error("创建AAA Stub时错误，请检查网络是否通畅!", e);
		}
	}
	
	/** 开户 */
	public boolean create(final NewAAASubscriberRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.newAAASubscriber(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 开户
	 * @see AAARequestUtils#buildNewAAASubscriberRequestMsg(long, String, String, Integer)
	 */
	public boolean create(long doneCode, String bandId, String pswd, Integer policyId)throws AAAException{
		NewAAASubscriberRequestMsg request = AAARequestUtils.buildNewAAASubscriberRequestMsg(doneCode, bandId, pswd, policyId);
		return create(request);
	}
	
	/** 销户 */
	public boolean delete(final DeleteAAASubscriberRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.deleteAAASubscriber(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 销户
	 * @see AAARequestUtils#buildDeleteAAASubscriberRequestMsg(long, String)
	 */
	public boolean delete(long doneCode, String bandId)throws AAAException{
		DeleteAAASubscriberRequestMsg request = AAARequestUtils.buildDeleteAAASubscriberRequestMsg(doneCode, bandId);
		return delete(request);
	}
	
	/** 暂停用户 */
	public boolean pause(final DeactivateSubscriberRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.deactivateSubscriber(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 暂停用户
	 * @see AAARequestUtils#buildDeactivateSubscriberRequestMsg(long, String)
	 */
	public boolean pause(long doneCode, String bandId)throws AAAException{
		DeactivateSubscriberRequestMsg request = AAARequestUtils.buildDeactivateSubscriberRequestMsg(doneCode, bandId);
		return pause(request);
	}
	
	/** 恢复用户 */
	public boolean resume(final ActivateSubscriberRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.activateSubscriber(request).getResultHeader();
			}
		});
	}
	
	/** 
	 * 恢复用户
	 * @see AAARequestUtils#buildActivateSubscriberRequestMsg(long, String)
	 */
	public boolean resume(long doneCode, String bandId)throws AAAException{
		ActivateSubscriberRequestMsg request = AAARequestUtils.buildActivateSubscriberRequestMsg(doneCode, bandId);
		return resume(request);
	}
	
	/** 重置密码 */
	public boolean resetPswd(final ResetAAASubscriberPswdRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.resetAAASubscriberPswd(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 重置密码
	 * @see AAARequestUtils#buildResetAAASubscriberPswdRequestMsg(long, String, String)
	 */
	public boolean resetPswd(long doneCode, String bandId, String newPswd)throws AAAException{
		ResetAAASubscriberPswdRequestMsg request = AAARequestUtils.buildResetAAASubscriberPswdRequestMsg(doneCode, bandId, newPswd);
		return resetPswd(request);
	}
	
	/** 订购业务 */
	public boolean orderService(final OrderSubscriberServiceRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.orderSubscriberService(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 订购业务
	 * @see AAARequestUtils#buildOrderSubscriberServiceRequestMsg(long, String, Integer, String, String)
	 */
	public boolean orderService(long doneCode, String userId, Integer policyId, String effectTime, String expireTime)throws AAAException{
		OrderSubscriberServiceRequestMsg request = AAARequestUtils.buildOrderSubscriberServiceRequestMsg(doneCode, userId, policyId, effectTime, expireTime);
		return orderService(request);
	}
	
	/** 取消用户下所有订购业务 */
	public boolean cancelOrder(final CancelSubscriberServiceRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.cancelSubscriberService(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 取消用户下所有订购业务
	 * @see AAARequestUtils#buildCancelSubscriberServiceRequestMsg(long, String)
	 */
	public boolean cancelOrder(long doneCode, String userId)throws AAAException{
		CancelSubscriberServiceRequestMsg request = AAARequestUtils.buildCancelSubscriberServiceRequestMsg(doneCode, userId);
		return cancelOrder(request);
	}
	
	/**
	 * 查询用户订购记录
	 * TODO 查询订购有问题 没有实现
	 * @see AAARequestUtils#buildQuerySubscriberServiceMsg(long, String)
	 */
	public List<AAASubscriberServiceInfoWrapper> querySubscriberService(long doneCode, String userId)throws AAAException{
		QuerySubscriberServiceRequestMsg request = AAARequestUtils.buildQuerySubscriberServiceMsg(doneCode, userId);
		return querySubscriberService(request);
	}
	
	public List<AAASubscriberServiceInfoWrapper> querySubscriberService(QuerySubscriberServiceRequestMsg request)throws AAAException{
		ResultHeader result = null;
		QuerySubscriberServiceResult obj = null;
		try {
			QuerySubscriberServiceResultMsg resultMsg = aaaStub.querySubscriberService(request);
			result = resultMsg.getResultHeader();
			obj = resultMsg.getQuerySubscriberServiceResult();
		} catch (RemoteException e) {
			throw new AAAException(e);
		}
		if(AAAResultUtils.success(result)){	
			List<AAASubscriberServiceInfoWrapper> aaas = new ArrayList<>(
					obj.getAAASubscriberServiceInfo().length);
			for (AAASubscriberServiceInfo a: obj.getAAASubscriberServiceInfo()) {
				aaas.add(new AAASubscriberServiceInfoWrapper(a));
			}
			return aaas;
		}else{
			throw new AAAException(result);
		}
	}
	
	/** 修改订购业务 */
	public boolean modifyOrderService(final ModifySubscriberServiceRequestMsg request)throws AAAException{
		return applyTemplate(new Callback(){
			@Override
			public ResultHeader doCallback() throws RemoteException {
				return aaaStub.modifySubscriberService(request).getResultHeader();
			}
		});
	}
	
	/**
	 * 修改订购业务
	 * @see AAARequestUtils#buildModifySubscriberServiceRequestMsg(long, String, Integer, String, String)
	 */
	public boolean modifyOrderService(long doneCode, String userId, Integer policyId, String effectTime, String expireTime)throws AAAException{
		ModifySubscriberServiceRequestMsg request = AAARequestUtils.buildModifySubscriberServiceRequestMsg(doneCode, userId, policyId, effectTime, expireTime);
		return modifyOrderService(request);
	}
	
	public boolean applyTemplate(Callback callback)throws AAAException{
		ResultHeader result = null;
		try {
			result = callback.doCallback();
		} catch (RemoteException e) {
			throw new AAAException(e);
		}
		if(AAAResultUtils.success(result)){	
			return true;
		}else{
			throw new AAAException(result);
		}
	}
	
	private interface Callback{
		ResultHeader doCallback()throws RemoteException;
	}
}
