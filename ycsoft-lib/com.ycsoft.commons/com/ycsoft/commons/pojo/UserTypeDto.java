package com.ycsoft.commons.pojo;



/**
 * 记录用户类型信息。本身是一个抽象类，
 * 是给所有含有用户类型信息的JavaBean提供的基类
 *
 * @author hh
 * @data Apr 8, 2010 10:54:21 AM
 */
public interface UserTypeDto{

	public void setUser_type(String user_type);


	public void setServ_type(String serv_type);


	public void setTerminal_type(String terminal_type);


	public void setNet_type(String net_type);

}
