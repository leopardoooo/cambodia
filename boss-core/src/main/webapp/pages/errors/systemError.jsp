<!-- 
	Author：黄辉
	Date:2009.11.28
	Action:当后台产生异常并且向上抛出
	由Struts负责声明式异常拦截器转向到此页面。
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ycsoft.daos.core.JDBCException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.ActionException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.ServicesException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.ComponentException"/>
<jsp:directive.page import="com.ycsoft.commons.exception.DaoException"/>
<jsp:directive.page import="com.opensymphony.xwork2.config.ConfigurationException"/>
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
%>
<html>
  
  <head>
    <title>系统内部错误</title>
    <link rel="stylesheet" type="text/css" href="<%=path %>/components/ext3.0/resources/css/ext-all.css"/>
  	<link rel="stylesheet" type="text/css" href="<%=path %>/resources/css/msg-tip.css" />
  	<style type="text/css">
  		h1,h2,h3,h4,h5,h6,th{
  			margin:0;
  			padding:0;
  			font-style:inherit;
  			font-weight:bolder;
  			font-size:medium;
  		}
  		th{text-align:center;}
  		td{font-family: serif;
  		   padding:5px;
  		}
  	</style>
  </head>
  
  <body style="">
  
   	<div class="x-div-screen-center">
   		<div class="left-div-img-icon x-img-failure" ></div>
   		<div class="msg-div"><br />
   			<label>友情提示：</label><br />
   			<div>
   				<%= msg %> 
   				<ul>
   					<li>请求URI：<span><s:property value="com.opensymphony.xwork2.ActionContext.name" /></span></li>
   					<li>错误信息：<span><s:property value="exception.message"/></span>
   					</li>
   				</ul>
   				<a href="#" onclick="showDebugMessage(this);return false;" >&gt;&gt;&gt;More Debug Message</a>
   			</div>
   		</div>
   	</div>
   	<div style="display:none"><s:debug/></div>
  </body>
  <script type="text/javascript" src="<%=path %>/components/ext3/ext-base.js" ></script>
  <script type="text/javascript" src="<%=path %>/components/ext3/ext-all.js" ></script>
  <script type="text/javascript">
  		var debugWindow = null ;
		function showDebugMessage(obj){
			if(null == debugWindow){
			 	debugWindow = new Ext.Window({
					title:'Debug Message Window',
					width:700,
					height:550,
					layout:'fit',
					modal:true,
					maximizable:true,
	                closeAction:'hide',
	                autoScroll:true,
	                //maximized:true,
					html:document.getElementById('debug').innerHTML
				});
			}
			debugWindow.show(obj);
		}
  </script>
</html>

