<!-- 
	Author：黄辉
	Date:2009.11.28
	Action:用于显示控制器输出错误或属性值错误的提示页面
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
%>
<html>
  
  <head>
    <title>系统内部错误</title>
  	<link rel="stylesheet" type="text/css" href="<%=path %>/resources/css/msg-tip.css" />
  </head>
  
  <body style="">
  
   	<div class="x-div-screen-center">
   		<div class="left-div-img-icon x-img-failure" ></div>
   		<div class="msg-div"><br />
   			<label>友情提示：</label><br />
   			<div>
				本次提交检测到错误信息如下：
				<s:fielderror />
				<s:actionerror/>
   			</div>
   		</div>
   	</div>
  </body>
</html>

