<%@ page language="java"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.ycsoft.commons.constants.Environment"%>
<%@page import="com.ycsoft.commons.action.SessionListener"%>
<%  
	String root = request.getContextPath();
	String boss_res = Environment.ROOT_PATH_BOSS_LOGIN;
	String optr=null;
	if(session==null||session.getAttribute(Environment.USER_IN_SESSION_NAME)==null){
%>
	<script type="text/javascript">
		if(window.top != window){
			window.top.location.href = "<%=SessionListener.getSsoLoginUrl(request)%>";
		}else{
			window.location.href = "<%=SessionListener.getSsoLoginUrl(request)%>";
		}
	</script>
<%
	}else{
		 optr = session.getAttribute(Environment.USER_IN_SESSION_NAME).toString();//当前登录用户信息
	}
	String basePath = request.getLocalAddr()+":"+request.getServerPort();
%>
 <head>
	<title> 报表系统首页 </title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="<%=root %>/components/ext3.4/resources/css/ext-all.css" rel="stylesheet" type="text/css" />
	<link href="<%=root %>/components/ext3.4/resources/css/xtheme-gray.css" rel="stylesheet" type="text/css" />
	<link href="<%=root %>/components/ext3.4/ux/css/Portal.css" rel="stylesheet" type="text/css" />	
	<link rel="stylesheet" href="<%=root %>/components/ext3.4/ux/css/MultiSelect.css" type="text/css" />
	<link rel="stylesheet" href="<%=root %>/components/ext3.4/ux/css/lovcombo.css" type="text/css" />
	
    <link rel="stylesheet" type="text/css" href="<%=root %>/pages/commons/css/datetime.css" />
    <link rel="stylesheet" type="text/css" href="<%=root %>/pages/commons/css/Spinner.css" />
    <link rel="stylesheet" href="<%=root %>/components/xreport/resources/XTable.css" type="text/css"/>
	<link href="<%=root %>/resources/main.css" rel="stylesheet" type="text/css" />
	<link href="<%=root %>/resources/icon.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript"> 
		var root = '<%=root %>',token_id ='${param.tokenId }';
		var sub_system_id ='${param.sub_system_id }';
		var optr = <%=optr%>;
		var regourl = '<%=SessionListener.getSsoLoginUrl(request)%>';
		function nowDate(){
			return Date.parseDate('<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %>','Y-m-d h:i:s');
		}
		function $(id){
			return document.getElementById(id);
		}
		var bossBasePath = '<%=basePath %>';
	</script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ext-base.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ext-all.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/Portal.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/PortalColumn.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/Portlet.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ext-lang-zh_CN.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/highcharts/jquery-1.7.2.min.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/highcharts/highcharts.js"charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=root %>/components/highcharts/exporting.js" charset="UTF-8"></script>
	
	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Vtypes.js" charset="UTF-8"></script>	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ParamComboBox.js" charset="UTF-8"></script>
	<!-- 全局错误处理，存在问题 -->
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/DebugWindow.js"charset="UTF-8" ></script>
	
	<script type="text/javascript" src="<%=boss_res  %>/pages/commons/ux/ext-basex.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/EditLovcombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/remotecheckboxgroup.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/DateTimeMenu.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/DateTimePicker.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/DateTimeField.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/Spinner.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/SpinnerField.js" charset="UTF-8"></script>

	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js" charset="UTF-8"></script>
		

	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterTreePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DistrictTreeCombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ColumnHeaderGroup.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/MultiSelect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/ItemSelector.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/ColorField.js" charset="UTF-8"></script>

	<script type="text/javascript" src="<%=boss_res %>/pages/commons/App.data.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/TopToolbar.js" charset="UTF-8"></script>

	<script type="text/javascript" src="<%=root %>/pages/report/DeployReport.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/CubeRowConfig.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/report/RepResultGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/xreport/XTable.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/report/DimListView.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/DimCalcWin.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/NavWindow.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/SaveTemplate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/SwitchTemplate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/IndexSelect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/GraphicSelect.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=root %>/pages/report/WarnConfig.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/report/ChartFactory.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/ResultXTable.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/ComponentFactory.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/QueryPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/MainPanel.js" charset="UTF-8"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/index/welcome.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/index.js" charset="UTF-8"></script>
 </head>

 <body>
	<!-- loading tip -->
	<div id="loading">
    	<div class="loading-indicator">
    		<img src="<%=root %>/resources/images/extanim32.gif" width="32" 
    			height="32" style="margin-right:8px;" align="absmiddle"/>Loading...
    	</div>
  	</div>

	<!-- page -->
	<div id="top" style="display: none;">
		<div>
			<div class="logo fl">
				<a href="#"><img src="<%=boss_res %>/resources/images/replogo.png" /></a>
			</div>
			<div id="titleId" class="title">报表系统</div>
			<div id="top_rt_link" class="fr">
				<span class="fl"></span>
				<span class="fr"></span>
				<ul>
					<li><a href="#" id="MenuSystem"><div class="toggle-system">切换系统</div></a></li>
					<li><a href="#" id="MenuUpdate"><div class="user-update">个人修改</div></a></li>
					<li><a href="#" id="MenuHelp"><div class="user-help">帮助</div></a></li>
					<li><a href="#" id="MenuLogout"><div class="exit">注销</div></a></li>
				</ul>
			</div>
		</div>
	</div>
	<div id="main"></div>
	<div id="ss-shadow" class="hd"></div>
	<div id="ss-panel" class="hd">
		<div id="ss-bts"></div>
		<div id="ss-close">&nbsp;</div>
	</div>
 </body>
 <script type="text/javascript">
	Ext.QuickTips.init();
	Ext.onReady(function() {
		<% String[] ds = new SimpleDateFormat("yyyy-MM-dd-E").format(new Date()).split("-"); %>
		App.LoginInfo = "今天是<b><%=ds[0]%></b>年<b><%=ds[1]%></b>月<b><%=ds[2]%></b>日 &nbsp;<b><%=ds[3]%></b>&nbsp;&nbsp;欢迎您：&nbsp;<b>"+ optr["optr_name"] +"</b>";
		// create widgets for index
		App.left = new LeftPanel();
		App.page = new RightTabPanel();
		App.centerViewport = new ViewportCenterPanel();
		
		//remove loading image and show top logo image
		Ext.get('loading').remove();
		Ext.get('top').setVisible(true);
		
		//assembling index page
		App.viewport = new Ext.Viewport({
			layout : 'border',
			items : [{
				region : 'north',
				height : 70,
				xtype : 'box',
				el : 'top'
			}, App.centerViewport]
		});
		App.top = App.viewport.items.get(0);
		TopHelper.initEvents();
	});
 </script>
</html>
