package com.yaochen.boss.commons;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;


/**
 * FtpUtil 构造器
 * 
 * @author Tom
 */
public class FtpUtilBuilder implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	// ------------------------------------------------------------------------
	//  需要注入的属性
	// 
	private String username;
	private String password;
	private String hostname;
	private Integer port;
	private String remoteBankPath;
	private String localDownloadPath;
	private String localTempPath; 
	private String remoteUploadPath;
	private String localHistoryPath;
	
	
	public FtpUtil buildFtpUtil(){
		return new FtpUtil(hostname, port, username, password);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if(StringUtils.isEmpty(hostname)){
			throw newException("hostname");
		}
		
		if(null == port || 0 == port){
			throw newException("port");
		}
		
		if(StringUtils.isEmpty(username)){
			throw newException("username");
		}
		
		if(StringUtils.isEmpty(password)){
			throw newException("password");
		}
		
		if(StringUtils.isEmpty(remoteBankPath)){
			throw newException("remoteBankPath");
		}
		
		if(StringUtils.isEmpty(localDownloadPath)){
			throw newException("localDownloadPath");
		}
		
		if(StringUtils.isEmpty(localTempPath)){
			throw newException("localTempPath");
		}
		
		if(StringUtils.isEmpty(remoteUploadPath)){
			throw newException("remoteUploadPath");
		}
	}
	
	private Exception newException(String property){
		Exception e = new IllegalArgumentException( property + " is require.");
		logger.error("ftp configuration error", e);
		return e;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getRemoteBankPath() {
		return remoteBankPath;
	}

	public void setRemoteBankPath(String remoteBankPath) {
		this.remoteBankPath = remoteBankPath;
	}

	public String getLocalDownloadPath() {
		return localDownloadPath;
	}

	public void setLocalDownloadPath(String localDownloadPath) {
		this.localDownloadPath = localDownloadPath;
	}

	public String getLocalTempPath() {
		return localTempPath;
	}

	public void setLocalTempPath(String localTempPath) {
		this.localTempPath = localTempPath;
	}

	public String getRemoteUploadPath() {
		return remoteUploadPath;
	}

	public void setRemoteUploadPath(String remoteUploadPath) {
		this.remoteUploadPath = remoteUploadPath;
	}

	public String getLocalHistoryPath() {
		return localHistoryPath;
	}

	public void setLocalHistoryPath(String localHistoryPath) {
		this.localHistoryPath = localHistoryPath;
	}
	
}
