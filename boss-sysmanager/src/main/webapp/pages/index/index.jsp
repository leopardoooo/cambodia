<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.ycsoft.commons.constants.Environment"/>
<%@page import="com.ycsoft.commons.action.SessionListener"%>
<jsp:directive.page import="com.ycsoft.commons.helper.DateHelper"/>
<% 
	String root = request.getContextPath(),
		   boss_res = Environment.ROOT_PATH_BOSS_LOGIN,
		   sub_system_id=request.getParameter("sub_system_id");
	String optr = session.getAttribute(
		Environment.USER_IN_SESSION_NAME).toString();//当前登录用户信息
	String nowDate = DateHelper.formatNowTime();	
	String basePath = request.getLocalAddr()+":"+request.getServerPort();
	String lang = session.getAttribute(Environment.USER_IN_SESSION_LANG).toString();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>首页-BOSS系统</title>
    <meta http-equiv="cache-control" content="no-cache">
    <script type="text/javascript">
		function nowDate(){
			return Date.parseDate('<%=nowDate%>','Y-m-d h:i:s');
		}
		var regourl = '<%=SessionListener.getSsoLoginUrl(request) %>';
		var bossBasePath = '<%=basePath %>';
	</script>
    <link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/ext-all.css" type="text/css" />
<!--     <link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/xtheme-gray.css" type="text/css" /> -->
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/index-other.css" type="text/css" />
    <link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" />
    <link rel="stylesheet" href="<%=boss_res %>/resources/css/animated-dataview.css" type="text/css" />  
    
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/boss-all.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/msg-tip.css" type="text/css" />
	
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/dynamic.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/treegrid.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/treegrideditor.css" type="text/css" />
	
     <script type="text/javascript"> var root = '<%=root %>',token_id ='${param.tokenId }';</script>
     <script type="text/javascript"> var sub_system_id ='${param.sub_system_id }';</script>
	 <script type="text/javascript"> var optr = '<%=optr%>';</script>
	 
  </head>
  <body style="overflow: hide;">
  	
	<div id="header"">
		<div class="logo">
			<img src="<%=boss_res %>/resources/images/logo.jpg"/>
		</div><div id="titleId" class="title">系统管理</div>
	</div>
	<div id="loading"><div>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-lang-zh_CN.js" ></script>
	
    <script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/ext-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/resouces-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/boss-core-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/boss-sysmanager-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/langUtils.js"></script>
     
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/CheckColumn.js"></script>
  	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js"></script>	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/jquery/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/jquery/ext-async-adapter.js"></script>

	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Vtypes.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/busi-helper.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Grid.js" charset="UTF-8"></script>
	
 	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterTreePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DistrictTreeCombo.js" charset="UTF-8"></script>
  	<script type="text/javascript" src="<%=boss_res %>/pages/commons/DebugWindow.js"></script>
  	
  	<script type="text/javascript" src="<%=boss_res %>/pages/commons/App.data.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/TopToolbar.js" ></script>
	<script type="text/javascript" src="<%=root %>/pages/index/MainArea.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/NavigationMenu.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/index.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/PrintTools.js"></script>
	
	<!-- TreeGrid -->
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridSorter.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridColumnResizer.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridNodeUI.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridLoader.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridColumns.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGrid.js"></script>
  	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeNodeChecked.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditor.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorEventModel.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNode.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNodeUI.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNodeReader.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNodeWriter.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorLoader.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorDragZone.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorSelectionModel.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DataView-more.js"></script>
    
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DateTimeField.js" charset="UTF-8" ></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/lovcombo.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/MultiSelect.js" charset="UTF-8" ></script>    
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ItemSelector.js" charset="UTF-8" ></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/PinyinFilter.js" charset="UTF-8" ></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeFilter.js" charset="UTF-8" ></script>

    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ParamComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/MonthPickerPlugin.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/LockingGridView.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/Spinner.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SpinnerField.js"></script>

	<!-- 系统管理系统 -->
	<%
	if("2".equals(sub_system_id)){ %>
		<jsp:include page = "/pages/index/systemLibrary.jsp" />
	<!-- 仓库管理系统 -->
	<%}else if("5".equals(sub_system_id)){ %>
		<script type="text/javascript">document.getElementById('titleId').innerHTML = '仓库管理';</script>
		<jsp:include page = "/pages/index/depotLibrary.jsp" />
	<%}else if("6".equals(sub_system_id)){ %>
		<script type="text/javascript">document.getElementById('titleId').innerHTML = '收据管理';</script>
		<jsp:include page = "/pages/index/invoiceLibrary.jsp" />
	<%}else if("8".equals(sub_system_id)){ %>
		<script type="text/javascript">document.getElementById('titleId').innerHTML = '项目管理';</script>
		<jsp:include page = "/pages/index/projectLibrary.jsp" />
	<%}else if("9".equals(sub_system_id)){ %>
		<script type="text/javascript">document.getElementById('titleId').innerHTML = '工单管理';</script>
		<jsp:include page = "/pages/index/taskLibrary.jsp" />
	<%} %>
  </body>
</html>
