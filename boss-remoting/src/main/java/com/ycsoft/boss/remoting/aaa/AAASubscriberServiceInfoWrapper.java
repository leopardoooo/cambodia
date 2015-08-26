/*
 * @(#) AAASubscriberServiceInfoWrapper.java 1.0.0 Aug 22, 2015 4:53:22 PM
 */
package com.ycsoft.boss.remoting.aaa;

import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.AAASubscriberServiceInfo;

/**
 * 华为AAA查询订购业务的返回值重新包装
 * @author Killer
 */
public class AAASubscriberServiceInfoWrapper {
	private int accessType;
	private int permittedANTYpe;
	private int accessPolicyID;
	private java.lang.String chargingType;
	private int maxSessNumber;
	private java.lang.String ipAddress;
	private java.lang.String ipMask;
	private java.lang.String effectTime;
	private java.lang.String expireTime;
	private int cancelBinding;
	private java.lang.String[] ueid;
	private java.lang.String framedInterfaceId;
	private java.lang.String framedIPv6Prefix;
	private java.lang.String delegatedIPv6Pre;
	private java.lang.String framedIPv6Address;
	private int roamingLevel;
	private int hwidType;
	private int msType;
	private int wiMAXUserMobilityCapabilities;
	private int limitedAccessLocation;
	private java.lang.String bindLocation;
	private java.lang.String imsi;
	private java.lang.String limitPortGroupID;
	private int portBindingType;
	
	public AAASubscriberServiceInfoWrapper(AAASubscriberServiceInfo a){
		this.accessType = a.getAccessType();
		this.permittedANTYpe = a.getPermittedANTYpe();
		this.accessPolicyID = a.getAccessPolicyID();
		this.chargingType = a.getChargingType();
		this.maxSessNumber = a.getMaxSessNumber();
		this.ipAddress = a.getIPAddress();
		this.ipMask = a.getIPMask();
		this.effectTime = a.getEffectTime();
		this.expireTime = a.getExpireTime();
		this.cancelBinding = a.getCancelBinding();
		this.ueid = a.getUEID();
		this.framedInterfaceId = a.getFramedInterfaceId();
		this.framedIPv6Prefix = a.getFramedIPv6Prefix();
		this.delegatedIPv6Pre = a.getDelegatedIPv6Pre();
		this.framedIPv6Address = a.getFramedIPv6Address();
		this.roamingLevel = a.getRoamingLevel();
		this.hwidType = a.getHWIDType();
		this.msType = a.getMSType();
		this.wiMAXUserMobilityCapabilities = a.getWiMAXUserMobilityCapabilities();
		this.limitedAccessLocation = a.getLimitedAccessLocation();
		this.bindLocation = a.getBindLocation();
		this.imsi = a.getIMSI();
		this.limitPortGroupID = a.getLimitPortGroupID();
		this.portBindingType = a.getPortBindingType();
	}

	public int getAccessType() {
		return accessType;
	}

	public int getPermittedANTYpe() {
		return permittedANTYpe;
	}

	public int getAccessPolicyID() {
		return accessPolicyID;
	}

	public java.lang.String getChargingType() {
		return chargingType;
	}

	public int getMaxSessNumber() {
		return maxSessNumber;
	}

	public java.lang.String getIpAddress() {
		return ipAddress;
	}

	public java.lang.String getIpMask() {
		return ipMask;
	}

	public java.lang.String getEffectTime() {
		return effectTime;
	}

	public java.lang.String getExpireTime() {
		return expireTime;
	}

	public int getCancelBinding() {
		return cancelBinding;
	}

	public java.lang.String[] getUeid() {
		return ueid;
	}

	public java.lang.String getFramedInterfaceId() {
		return framedInterfaceId;
	}

	public java.lang.String getFramedIPv6Prefix() {
		return framedIPv6Prefix;
	}

	public java.lang.String getDelegatedIPv6Pre() {
		return delegatedIPv6Pre;
	}

	public java.lang.String getFramedIPv6Address() {
		return framedIPv6Address;
	}

	public int getRoamingLevel() {
		return roamingLevel;
	}

	public int getHwidType() {
		return hwidType;
	}

	public int getMsType() {
		return msType;
	}

	public int getWiMAXUserMobilityCapabilities() {
		return wiMAXUserMobilityCapabilities;
	}

	public int getLimitedAccessLocation() {
		return limitedAccessLocation;
	}

	public java.lang.String getBindLocation() {
		return bindLocation;
	}

	public java.lang.String getImsi() {
		return imsi;
	}

	public java.lang.String getLimitPortGroupID() {
		return limitPortGroupID;
	}

	public int getPortBindingType() {
		return portBindingType;
	}
}
