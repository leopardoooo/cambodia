<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.ycsoft.commons.constants.Environment"/>
<% 
	String root = request.getContextPath(),
		   boss_res = Environment.ROOT_PATH_BOSS_LOGIN;
	String optr = session.getAttribute(
		Environment.USER_IN_SESSION_NAME).toString();//当前登录用户信息
	String single_rep_id=request.getParameter("rep_id");
	String single_rep_name=request.getParameter("rep_name");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>首页-BOSS系统</title>
    <link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/ext-all.css" type="text/css" />
    <link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/xtheme-gray.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/index-other.css" type="text/css" />
    <link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" />
    <script type="text/javascript"> var root = '<%=root %>',token_id ='${param.tokenId }';</script>
    <script type="text/javascript"> var sub_system_id ='${param.sub_system_id }';</script>
    <script type="text/javascript"> 
    var optr = '<%=optr%>';
    var single_rep_id='<%=single_rep_id%>';
	var single_rep_name='<%=single_rep_name%>';
	alert(single_rep_id);
	alert(single_rep_name);
    
    </script>
  </head>
  <body style="overflow: hide;">
  	<div id="header">
  		<div class="logo">
			<img src="<%=boss_res %>/resources/images/index/logo4.png"/>
		</div><div id="titleId" class="title">报表系统</div>
  	</div>
  	<div id="loading">
  	 <div>
  	  <img src="<%=boss_res %>/resources/images/loading.gif" width="31" height="31" 
		   style="margin-right:8px;float:left;vertical-align:top;" />
			 BOSS系统 V3.1<br />
	  <span class="loading-msg">正在初始化模块...</span>
	 </div>
	</div>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-lang-zh_CN.js" ></script>
	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Vtypes.js" ></script>	
	<!-- 全局错误处理，存在问题 -->
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/DebugWindow.js" ></script>
	
	<script type="text/javascript" src="<%=boss_res  %>/pages/commons/ux/ext-basex.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/EditLovcombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/remotecheckboxgroup.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js"></script>
		

	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterTreePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DistrictTreeCombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ColumnHeaderGroup.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/App.data.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/TopToolbar.js" ></script>

	<script type="text/javascript" src="<%=root %>/pages/report/RepQuery.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/singleindex.js"></script>
  </body>
</html>
