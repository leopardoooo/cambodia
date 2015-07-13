
package com.ycsoft.ly.intface.taskclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "Service1Soap", targetNamespace = "http://tempuri.org/")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface Service1Soap {

	@WebMethod(operationName = "createTask", action = "http://tempuri.org/createTask")
	@WebResult(name = "createTaskResult", targetNamespace = "http://tempuri.org/")
	public String createTask(
			@WebParam(name = "work_id", targetNamespace = "http://tempuri.org/")
			String work_id,
			@WebParam(name = "cust_id", targetNamespace = "http://tempuri.org/")
			String cust_id,
			@WebParam(name = "user_id", targetNamespace = "http://tempuri.org/")
			String user_id,
			@WebParam(name = "install_addr", targetNamespace = "http://tempuri.org/")
			String install_addr,
			@WebParam(name = "task_cust_name", targetNamespace = "http://tempuri.org/")
			String task_cust_name,
			@WebParam(name = "tel", targetNamespace = "http://tempuri.org/")
			String tel,
			@WebParam(name = "task_areaid", targetNamespace = "http://tempuri.org/")
			String task_areaid,
			@WebParam(name = "old_addr", targetNamespace = "http://tempuri.org/")
			String old_addr,
			@WebParam(name = "task_type", targetNamespace = "http://tempuri.org/")
			String task_type,
			@WebParam(name = "task_status", targetNamespace = "http://tempuri.org/")
			String task_status,
			@WebParam(name = "books_time", targetNamespace = "http://tempuri.org/")
			String books_time,
			@WebParam(name = "bug_cause", targetNamespace = "http://tempuri.org/")
			String bug_cause,
			@WebParam(name = "remark", targetNamespace = "http://tempuri.org/")
			String remark);

}
