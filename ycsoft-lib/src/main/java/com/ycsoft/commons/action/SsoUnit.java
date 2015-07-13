package com.ycsoft.commons.action;

import java.io.IOException;
import java.net.UnknownHostException;

import com.ycsoft.commons.helper.SocketHelp;

public class SsoUnit {
	private static final String DEFAULT_SSO_IP = "127.0.0.1";
	private static final String DEFAULT_SSO_PORT = "8000";
	private static String ssoIp = DEFAULT_SSO_IP;
	private static String ssoPort = DEFAULT_SSO_PORT;
	private static String ssoLoginUrl;

	/**
	 * 验证，并返回操作员信息
	 * 
	 * @param ssoSid
	 *            sso服务器id
	 * @param sessionId
	 *            本地id
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static String checkSso(String ssoSid, String sessionId,
			String contextPath) throws IOException {
		return sendDate("g" + ssoSid + "," + sessionId + "," + contextPath);
	}

	public static void loginOut(String ssoSid) throws IOException {
		if (ssoSid != null)
			sendDate("o" + ssoSid);
	}

	public static void clearAll() {
		try {
			sendDate("d");
		} catch (IOException e) {
			System.err.println("启动时清除当前服务器的登陆信息失败");
		}
	}

	public static void destrorySso(String ssoSid, String contextPath) throws IOException {
		if (ssoSid != null)
			sendDate("c" + ssoSid + "," + contextPath);
	}

	public static void addResoucreInfo(String ssoSid, String contextPath) throws IOException {
		if (ssoSid != null)
			sendDate("a"+ssoSid+","+contextPath);		
	}
	
	private static String sendDate(String data) throws IOException {
		return SocketHelp.sendData(getSsoIp(),
				getSsoPort(), data);
	}
	
	private static Integer getSsoPort() {
		return Integer.parseInt(SsoUnit.ssoPort);
	}

	private static String getSsoIp() {
		 return SsoUnit.ssoIp;
	}

	/**
	 * @return the ssoLoginUrl
	 */
	public static String getSsoLoginUrl() {
		return ssoLoginUrl;
	}

	/**
	 * @param ssoLoginUrl
	 *            the ssoLoginUrl to set
	 */
	public void setSsoLoginUrl(String ssoLoginUrl) {
		SsoUnit.ssoLoginUrl = ssoLoginUrl;
	}

	/**
	 * @param ssoIp
	 *            the ssoIp to set
	 */
	public void setSsoIp(String ssoIp) {
		if (ssoIp != null)
			SsoUnit.ssoIp = ssoIp;
	}

	/**
	 * @param ssoPort
	 *            the ssoPort to set
	 */
	public void setSsoPort(String ssoPort) {
		if (ssoPort != null)
			SsoUnit.ssoPort = ssoPort;
	}
}