package com.ycsoft.commons.action;

import org.springframework.beans.factory.InitializingBean;

public class InitializingAction implements  InitializingBean{

	public void afterPropertiesSet() throws Exception {
		//启动时清除当前服务器的登陆信息
		SsoUnit.clearAll();
	}

}
