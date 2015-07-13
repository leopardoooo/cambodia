<%-- 
	Author：黄辉
	Date:2010-3-18
	Action:当后台产生异常并且向上抛出
	由Struts负责声明式异常拦截器转向到此页面。
 --%>
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ycsoft.daos.core.JDBCException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.ActionException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.ServicesException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.ComponentException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.DaoException"/>
<jsp:directive.page import="com.opensymphony.xwork2.config.ConfigurationException"/>
<jsp:directive.page import="java.util.Map"/>
<jsp:directive.page import="java.util.HashMap"/>
<jsp:directive.page import="com.google.gson.Gson"/>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	Exception ex = (Exception)request.getAttribute("exception");
	String _exName = ex.getClass().getName();
	ex.printStackTrace();
	String msg = "" ;
	if (NoSuchMethodException.class.getName().equals( _exName )){
		msg = "Struts异常：在对应的Action中没有发现与之对应的执行函数!" ;
	} else if (ConfigurationException.class.getName().equals(_exName)){
		msg = "struts异常：系统返回跳转路径名时在struts配置文件没有找到与之匹配的名称!" ; 
	} else if (DaoException.class.getName().equals(_exName)){
		msg = "系统异常：DAO层发生异常!" ;
	} else if (ComponentException.class.getName().equals(_exName)){
		msg = "系统异常：Component层发生异常!" ;
	} else if (ServicesException.class.getName().equals(_exName)){
		msg = "系统异常：Services层发生异常!" ;
	} else if (ActionException.class.getName().equals(_exName)){
		msg = "系统异常：Action层发生异常!" ; 
	} else if (JDBCException.class.getName().equals(_exName)){
		msg = "Daos异常：Daos库发生异常!" ;
	}else {
		msg = "未捕获异常：系统执行过程中发生未知的异常!";
	}
	
	Map<String ,Object> map = new HashMap<String ,Object>();
	map.put("errorMsg",  ex.getMessage());
	map.put("action" , request.getAttribute("com.opensymphony.xwork2.ActionContext.name"));
	map.put("title" , msg);
	map.put("exception", true);
%>
<%= new Gson().toJson(map) %>

