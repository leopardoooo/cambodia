/*
 * @(#) AAAUtils.java 1.0.0 2015年7月20日 下午3:02:26
 */
package com.ycsoft.boss.remoting.aaa;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.AAASubscriberInfo;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.AAASubscriberServiceInfo;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ActivateSubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ActivateSubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.CancelSubscriberServiceRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.CancelSubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeactivateSubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeactivateSubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeleteAAASubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.DeleteAAASubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ModifySubscriberServiceRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ModifySubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.NewAAASubscriberRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.NewAAASubscriberRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.OperatorInfo;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.OrderSubscriberServiceRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.OrderSubscriberServiceRequestMsg;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.QuerySubscriberServiceRequest;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.QuerySubscriberServiceRequestMsg;
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
	
	private static final String  DEFAULT_LIMIT_PORT_GROUP_ID="1";
	
	// 华为AAA接入方式支持很多种，但目前boss系统仅支持1090204:FBB&WiFi
	private static final Integer DEFAULT_ACCESS_TYPE = 1090204;
	
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
		ResetAAASubscriberPswd,
		// 订购业务
		OrderSubscriberService,
		// 修改订购业务
		ModifySubscriberService,
		// 删除订购业务
		CancelSubscriberService,
		// 查询用户订购业务
		QuerySubscriberService
	}
	
	/**
	 * 构建修改订购业务的请求数据结构
	 * @param doneCode 流水号用于生成请求头信息
	 * @param userId boss系统的userId
	 * @param policyId 对应AAA的policyId
	 * @param effectTime YYMMDDhhmmss 如：20370101000000
	 * @param expireTime YYMMDDhhmmss 如：20370101000000
	 * @return
	 */
	public static ModifySubscriberServiceRequestMsg buildModifySubscriberServiceRequestMsg(long doneCode, String userId, Integer policyId, String effectTime, String expireTime){
		ModifySubscriberServiceRequestMsg request = new ModifySubscriberServiceRequestMsg();
		
		ModifySubscriberServiceRequest body = new ModifySubscriberServiceRequest();
		body.setSubscriberID(userId);
		
		AAASubscriberServiceInfo serviceInfo = createServiceInfo(policyId, effectTime, expireTime);
		body.setAAASubscriberServiceInfo(serviceInfo);
		
		request.setModifySubscriberServiceRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.ModifySubscriberService, doneCode));
		
		return request;
	}
	
	public static ModifySubscriberServiceRequestMsg buildModifySubscriberServiceRequestMsg(String userId, Integer policyId, String effectTime, String expireTime){
		return buildModifySubscriberServiceRequestMsg(DEFAULT_DONE_CODE, userId, policyId, effectTime, expireTime);
	}
	
	/**
	 * TODO 查询订购有问题没有实现
	 * 构建查询订购业务的请求数据结构
	 * @param doneCode 流水号用于生成请求头信息
	 * @param userId boss系统的userId
	 * @return
	 */
	public static QuerySubscriberServiceRequestMsg buildQuerySubscriberServiceMsg(long doneCode, String userId){
		QuerySubscriberServiceRequestMsg request = new QuerySubscriberServiceRequestMsg();
		
		QuerySubscriberServiceRequest body = new QuerySubscriberServiceRequest();
		body.setSubscriberID(userId);
		// 接入方式 1090204:FBB&WiFi
		body.setAccessType(DEFAULT_ACCESS_TYPE);
		System.out.println(body.getSubscriberIDType());
		body.setSubscriberIDType(3);

	
		System.out.println(body.getSubscriberIDType());
		
		request.setQuerySubscriberServiceRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.QuerySubscriberService, doneCode));
		
		return request;
	}
	
	public static QuerySubscriberServiceRequestMsg buildQuerySubscriberServiceMsg(String userId){
		return buildQuerySubscriberServiceMsg(DEFAULT_DONE_CODE, userId);
	}
	
	/**
	 * 构建订购业务的请求数据结构
	 * @param doneCode 流水号用于生成请求头信息
	 * @param userId boss系统的userId
	 * @param policyId 对应AAA的policyId
	 * @param effectTime YYMMDDhhmmss 如：20370101000000
	 * @param expireTime YYMMDDhhmmss 如：20370101000000
	 * @return
	 */
	public static OrderSubscriberServiceRequestMsg buildOrderSubscriberServiceRequestMsg(long doneCode, String userId, Integer policyId, String effectTime, String expireTime){
		OrderSubscriberServiceRequestMsg request = new OrderSubscriberServiceRequestMsg();
		
		OrderSubscriberServiceRequest body = new OrderSubscriberServiceRequest();
		body.setSubscriberID(userId);
		
		AAASubscriberServiceInfo serviceInfo = createServiceInfo(policyId, effectTime, expireTime);
		body.setAAASubscriberServiceInfo(serviceInfo);
		
		request.setOrderSubscriberServiceRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.OrderSubscriberService, doneCode));
		
		return request;
	}
	
	public static OrderSubscriberServiceRequestMsg buildOrderSubscriberServiceRequestMsg(String userId, Integer policyId, String effectTime, String expireTime){
		return buildOrderSubscriberServiceRequestMsg(DEFAULT_DONE_CODE, userId, policyId, effectTime, expireTime);
	}
	
	/**
	 * 取消业务订购
	 * @param doneCode 业务流水号
	 * @param userId BOSS系统的用户编号
	 * @return
	 */
	public static CancelSubscriberServiceRequestMsg buildCancelSubscriberServiceRequestMsg(long doneCode, String userId){
		CancelSubscriberServiceRequestMsg request = new CancelSubscriberServiceRequestMsg();
		
		CancelSubscriberServiceRequest body = new CancelSubscriberServiceRequest();
		body.setSubscriberID(userId);
		// 接入方式 1090204:FBB&WiFi
		body.setAccessType(DEFAULT_ACCESS_TYPE);
		body.setOperatorInfo(createDefaultOperatorInfo());
		
		request.setCancelSubscriberServiceRequest(body);
		request.setRequestHeader(buildReqeustHeader(CommandId.CancelSubscriberService, doneCode));
		
		return request;
	}
	
	public static CancelSubscriberServiceRequestMsg buildCancelSubscriberServiceRequestMsg(String userId){
		return buildCancelSubscriberServiceRequestMsg(DEFAULT_DONE_CODE, userId);
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
		
		body.setAAASubscriberInfo(basic);
		//开户时订购策略
		if(policyId!=null){
			AAASubscriberServiceInfo serviceInfo = createServiceInfo(policyId, null, null);
			body.setAAASubscriberServiceInfo(new AAASubscriberServiceInfo[]{serviceInfo});
		}
		
		request.setRequestHeader(buildReqeustHeader(CommandId.NewSubscriber, doneCode));
		request.setNewAAASubscriberRequest(body);
		
		return request;
	}
	
	/**
	 * 创建一个订购内容
	 * @param policyId 策略编号， 可以为空
	 * @param effectTime 生效时间， 可以为空
	 * @param expireTime 失效时间，可以为空
	 * @return
	 */
	private static AAASubscriberServiceInfo createServiceInfo(Integer policyId, String effectTime, String expireTime){
		AAASubscriberServiceInfo serviceInfo = new AAASubscriberServiceInfo();
		// 接入方式 1090204:FBB&WiFi
		serviceInfo.setAccessType(DEFAULT_ACCESS_TYPE);
		// 1:固网 2:WLAN 3:固网+WLAN
		serviceInfo.setPermittedANTYpe(3);
		// 接入策略ID
		if (policyId != null)
			serviceInfo.setAccessPolicyID(policyId);
		serviceInfo.setChargingType("1");
		serviceInfo.setMaxSessNumber(0);
		serviceInfo.setCancelBinding(0);
		serviceInfo.setPortBindingType(0);
		serviceInfo.setLimitPortGroupID(DEFAULT_LIMIT_PORT_GROUP_ID);
		// 开始日期
		if(null != effectTime){
			serviceInfo.setEffectTime(effectTime);
		}
		// 结束日期
		if(null != expireTime){
			serviceInfo.setExpireTime(expireTime);
		}
		return serviceInfo;
	}
	
	/**
	 * 创建一个默认的操作员
	 * @return
	 */
	private static OperatorInfo createDefaultOperatorInfo(){
		OperatorInfo optr = new OperatorInfo();
		optr.setDeptID("boss");
		optr.setOperID("0");
		
		return optr;
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
