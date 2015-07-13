/*
 * @(#) BandCmd.java 1.0.0 Aug 1, 2011 4:31:34 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yaochen.boss.commons;

import com.ycsoft.beans.core.user.BandSimpleInfo;

/**
 * 
 * 宽带指令接口定义
 * 
 * @author allex
 * @since 1.0
 */
public interface BandCmdService {

	public int createUser(String loginName, String serviceName, String pwd)
			throws Exception;
	
	public int removeUser(String loginName) throws Exception;

	public int modifyPwd(String loginName, String oldPwd, String newPwd)
			throws Exception;
	public int modifyPwd2(String loginName,  String newPwd)
	throws Exception;

	public int stopUser(String loginName,String status) throws Exception;

	public int openUser(String loginName) throws Exception;
	
	public int createUser5(BandSimpleInfo bandSimpleInfo)throws Exception;
	//修改宽带用户资料
	public int ModifyUserInfo4(BandSimpleInfo bandSimpleInfo)throws Exception;

	
}
