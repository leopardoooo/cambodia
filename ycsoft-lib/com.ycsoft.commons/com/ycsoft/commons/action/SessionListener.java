package com.ycsoft.commons.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.SocketHelp;

public class SessionListener implements HttpSessionListener {
	public void sessionCreated(HttpSessionEvent se) { 
	}

	public void sessionDestroyed(HttpSessionEvent se) { 
		HttpSession session = se.getSession();
		String ssoSid = (String) session
				.getAttribute(Environment.SSO_TOKEN_PARAM_KEY);
		String contextPath = (String) session
				.getAttribute(Environment.CONTEXT_PATH);
		if(ssoSid!=null)
			try {
				SsoUnit.destrorySso(ssoSid,contextPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static String getSsoLoginUrl(HttpServletRequest req) {
		String ssoLoginUrl = SsoUnit.getSsoLoginUrl();
		if ("".equals(ssoLoginUrl)) {
			String domain = req.getHeader("referer");
			try{
				Matcher m = Pattern.compile("^(http|https)://((((\\w+\\.)+\\w+)+)|(\\w+))(:\\w+)*").matcher(domain);
				if (m.find()) 
					domain = m.group();
			}catch (Exception e) {
				domain = "";
			}
			ssoLoginUrl = domain + Environment.ROOT_PATH_BOSS_LOGIN+"/";
		}
		return ssoLoginUrl;
	}
}