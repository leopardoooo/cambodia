package com.ycsoft.business.service;

import java.util.List;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.dto.core.cust.CustOTTMobile;
import com.ycsoft.business.dto.core.user.UserLoginPwd;

public interface IAccountService extends IOrderService{
	
	public List<UserLoginPwd> createOttMobile(CustOTTMobile custOtt) throws Exception;
	public List<PProd> queryOTTMobileFreeProd() throws Exception;
}
