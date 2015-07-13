<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ycsoft.commons.constants.Environment"%>
<%@page import="com.ycsoft.beans.system.SOptr"%>
<%@page import="com.ycsoft.commons.helper.JsonHelper" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	ServletContext pokerWeb = application.getContext("/poker");
	if (pokerWeb != null ) {
		HttpSession pokerSession = (HttpSession) pokerWeb.getAttribute(Environment.CURRENT_POKER_SESSION);
		if(pokerSession!=null&&pokerSession.getAttribute(Environment.CURRENT_BUSI_OPTR_ID)!=null){
			
			//out.println(pokerSession.getAttribute("user").getClass());
			String username=(String)pokerSession.getAttribute(Environment.CURRENT_BUSI_OPTR_ID);
		   
			SOptr optr=new SOptr();
			optr.setOptr_id(username);
			optr.setOptr_name(username);
			optr.setLogin_name(username);
			optr.setDept_id("1");
			optr.setDept_name("管理中心");
			optr.setCounty_id("4501");
			optr.setArea_id("4500");
			
			System.err.println(session.getId());
			
			session.setAttribute(Environment.USER_IN_SESSION_NAME,JsonHelper.fromObject(optr));
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("Location","/boss_report/pages/poker/index.jsp");
			return;
		}else{
			out.println("请重新登录");
		}
	}else{
		out.println("请重新登录");
	}
%>
