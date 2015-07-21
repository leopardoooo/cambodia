/*
 * @(#) AAAUtils.java 1.0.0 2015年7月20日 下午3:02:26
 */
package com.ycsoft.boss.remoting.aaa;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.AAASubscriberInfo;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.AAASubscriberServiceInfo;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ActivateSubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ActivateSubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeactivateSubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeactivateSubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeleteAAASubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeleteAAASubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.NewAAASubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.NewAAASubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.RequestHeader;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.RequestType_type1;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResetAAASubscriberPswdRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResetAAASubscriberPswdRequestMsg;

/**
 * 华为接口工具类，解析请求与响应的内容
 * 
 * @author Killer
 */
public final class AAARequestUtils {

	// 默认的业务流水号
	private static final long DEFAULT_DONE_CODE = 0L;
	
	static enum CommandId{
		// 开户
		NewSubscriber,
		// 销户
		DeleteSubscriber,
		// 暂停用户
		DeactivateSubscriber,
		// 恢复用户
		ActivateSubscriber,
		// 重置密码
		ResetAAASubscriberPswd
	}
	
	/**
	 * 构建重置密码的请求数据结构
	 * @param doneCode 业务流水号
	 * @param bandId 宽带账号
	 * @param pswd 新的密码
	 * @return
	 */
	public static ResetAAASubscriberPswdRequestMsg buildResetAAASubscriberPswdRequestMsg(long doneCode, String bandId, String pswd){
		ResetAAASubscriberPswdRequestMsg request = new ResetAAASubscriberPswdRequestMsg();
		
		ResetAAASubscriberPswdRequest body = new ResetAAASubscriberPswdRequest();
		body.setSubscriberID(bandId);
		body.setPassword(pswd);
		
		request.setResetAAASubscriberPswdRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.ResetAAASubscriberPswd, doneCode));
		
		return request;
	}
	
	public static ResetAAASubscriberPswdRequestMsg buildResetAAASubscriberPswdRequestMsg(String bandId, String pswd){
		return buildResetAAASubscriberPswdRequestMsg(DEFAULT_DONE_CODE, bandId, pswd);
	}
	
	/**
	 * 构建恢复用户的请求数据结构
	 * 
	 * @param doneCode 业务流水号
	 * @param bandId 宽带账号
	 * @return
	 */
	public static ActivateSubscriberRequestMsg buildActivateSubscriberRequestMsg(long doneCode, String bandId){
		ActivateSubscriberRequestMsg request = new ActivateSubscriberRequestMsg();
		
		ActivateSubscriberRequest body = new ActivateSubscriberRequest();
		body.setSubscriberID(bandId);
		// 申请恢复
		body.setOprType(1);
		
		request.setActivateSubscriberRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.ActivateSubscriber, doneCode));
		return request;
	}
	
	public static ActivateSubscriberRequestMsg buildActivateSubscriberRequestMsg(String bandId){
		return buildActivateSubscriberRequestMsg(DEFAULT_DONE_CODE, bandId);
	}

	/**
	 * 构建暂停用户的请求数据结构
	 * @param doneCode 业务流水号
	 * @param bandId 宽带账号
	 * @return
	 */
	public static DeactivateSubscriberRequestMsg buildDeactivateSubscriberRequestMsg(long doneCode, String bandId){
		DeactivateSubscriberRequestMsg request = new DeactivateSubscriberRequestMsg();
		
		DeactivateSubscriberRequest body = new DeactivateSubscriberRequest();
		body.setSubscriberID(bandId);
		// 主动暂停
		body.setOprType(1);
		
		request.setDeactivateSubscriberRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.DeactivateSubscriber, doneCode));
		
		return request;
	}
	
	public static DeactivateSubscriberRequestMsg buildDeactivateSubscriberRequestMsg(String bandId){
		return buildDeactivateSubscriberRequestMsg(DEFAULT_DONE_CODE, bandId);
	}
	
	
	/**
	 * 构建销户的请求数据结构
	 * @param doneCode 业务流水号
	 * @param bandId 宽带账号
	 * @return
	 */
	public static DeleteAAASubscriberRequestMsg buildDeleteAAASubscriberRequestMsg(long doneCode, String bandId){
		DeleteAAASubscriberRequestMsg request = new DeleteAAASubscriberRequestMsg();
		
		DeleteAAASubscriberRequest body = new DeleteAAASubscriberRequest();
		body.setSubscriberID(bandId);
		
		request.setDeleteAAASubscriberRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.DeleteSubscriber, doneCode));
		
		return request;
	}
	
	public static DeleteAAASubscriberRequestMsg buildDeleteAAASubscriberRequestMsg(String bandId){
		return buildDeleteAAASubscriberRequestMsg(DEFAULT_DONE_CODE, bandId);
	}
	
	/**
	 * 构建一个创建宽带用户的请求数据结构
	 * 
	 * @param doneCode 业务流水号，可以为null
	 * @param bandId 宽带账号
	 * @param pswd 密码，最大16个字符
	 * @param policyId 接入策略 目前仅仅支持FBB&WiFi
	 * @return
	 */
	public static NewAAASubscriberRequestMsg buildNewAAASubscriberRequestMsg(long doneCode, String bandId, String pswd, Integer policyId){
		NewAAASubscriberRequestMsg request = new NewAAASubscriberRequestMsg();
		// 业务数据
		NewAAASubscriberRequest body = new NewAAASubscriberRequest();
		// 宽带账号
		body.setSubscriberID(bandId);
		AAASubscriberInfo basic = new AAASubscriberInfo();
		// 密码
		basic.setPassword(pswd);
		AAASubscriberServiceInfo serviceInfo = new AAASubscriberServiceInfo();
		// 接入方式 1090204:FBB&WiFi
		serviceInfo.setAccessType(1090204);
		// 1:固网 2:WLAN 3:固网+WLAN
		serviceInfo.setPermittedANTYpe(3);
		// 接入策略ID
		serviceInfo.setAccessPolicyID(policyId);
		serviceInfo.setChargingType("1");
		serviceInfo.setMaxSessNumber(0);
		serviceInfo.setCancelBinding(0);
		serviceInfo.setPortBindingType(0);
		
		body.setAAASubscriberInfo(basic);
		body.setAAASubscriberServiceInfo(new AAASubscriberServiceInfo[]{serviceInfo});
		
		request.setRequestHeader(buildReqeustHeader(CommandId.NewSubscriber, doneCode));
		request.setNewAAASubscriberRequest(body);
		
		return request;
	}
	
	public static NewAAASubscriberRequestMsg buildNewAAASubscriberRequestMsg(String bandId, String pswd, Integer policyId){
		return buildNewAAASubscriberRequestMsg(DEFAULT_DONE_CODE, bandId, pswd, policyId);
	}
	
	/**
	 * 构造一个默认的RequestHeader,这里会设置一些默认的参数值，
	 * 获得返回的请求头对象，可以覆盖默认值，也可以设置额外的参数值
	 * 
	 * @return
	 */
	public static RequestHeader buildReqeustHeader(CommandId cmd, long doneCode){
		RequestHeader header = new RequestHeader();
		// 命令代码
		header.setCommandId(cmd.name());
		// 接口版本号
		header.setVersion("1");
		// 事务ID,用来关联一个会话
		header.setTransactionId("1");
		// 序列ID
		header.setSequenceId("1");
		// 每个操作的序列号,需要保持唯一
		header.setSerialNo(String.valueOf(doneCode));
		// 操作类型, 默认为Event
		header.setRequestType(RequestType_type1.Event);
		// 语言包ZH,EN
		header.setLocale("ZH");
		
		return header;
	}
	
}
