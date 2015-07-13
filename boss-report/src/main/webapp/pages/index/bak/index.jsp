<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.ycsoft.commons.constants.Environment"/>
<%@page import="com.ycsoft.commons.action.SessionListener"%>
<% 
	String root = request.getContextPath(),
		   boss_res = Environment.ROOT_PATH_BOSS_LOGIN;
	String optr = session.getAttribute(
		Environment.USER_IN_SESSION_NAME).toString();//当前登录用户信息
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>首页-BOSS系统</title>
    <link rel="stylesheet" href="<%=root %>/components/ext3.4/resources/css/ext-all.css" type="text/css" />
    <link rel="stylesheet" href="<%=root %>/components/ext3.4/resources/css/xtheme-gray.css" type="text/css" />
    <link rel="stylesheet" href="<%=root %>/components/ext3.4/ux/css/MultiSelect.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/index-other.css" type="text/css" />
    <link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" />
    <link rel="stylesheet" href="<%=root %>/resources/icon.css" type="text/css" />
    <link rel="stylesheet" href="<%=root %>/components/xreport/resources/XTable.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=root %>/pages/commons/css/datetime.css" />
    <link rel="stylesheet" type="text/css" href="<%=root %>/pages/commons/css/Spinner.css" />
        <script type="text/javascript"> var root = '<%=root %>',token_id ='${param.tokenId }';</script>
    <script type="text/javascript"> var sub_system_id ='${param.sub_system_id }';</script>
    <script type="text/javascript"> var optr = '<%=optr%>';</script>
    <script type="text/javascript"> var regourl = '<%=SessionListener.getSsoLoginUrl(request)%>';</script>
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
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ext-base.js" ></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ext-all.js" ></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ext-lang-zh_CN.js" ></script>
	<script type="text/javascript" src="<%=root %>/components/highcharts/jquery-1.7.2.min.js" ></script>
	<script type="text/javascript" src="<%=root %>/components/highcharts/highcharts.js" ></script>
	<script type="text/javascript" src="<%=root %>/components/highcharts/exporting.js" ></script>
	
	
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Vtypes.js" ></script>	
	<!-- 全局错误处理，存在问题 -->
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/DebugWindow.js" ></script>
	
	<script type="text/javascript" src="<%=boss_res  %>/pages/commons/ux/ext-basex.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/EditLovcombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/remotecheckboxgroup.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/DateTimeMenu.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/DateTimePicker.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/DateTimeField.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/Spinner.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/ux/SpinnerField.js" charset="UTF-8"></script>

	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js"></script>
		

	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterTreePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DistrictTreeCombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ColumnHeaderGroup.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/MultiSelect.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/components/ext3.4/ux/ItemSelector.js" charset="UTF-8"></script>
	

	<script type="text/javascript" src="<%=boss_res %>/pages/commons/App.data.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/TopToolbar.js" ></script>

	<script type="text/javascript" src="<%=root %>/pages/report/DeployReport.js"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/report/RepResultGrid.js"></script>
	<script type="text/javascript" src="<%=root %>/components/xreport/XTable.js"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/report/DimCalcWin.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/NavWindow.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/SaveTemplate.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/SwitchTemplate.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/IndexSelect.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/GraphicSelect.js"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/report/ChartFactory.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/ResultXTable.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/ComponentFactory.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/QueryPanel.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/report/MainPanel.js"></script>
	
	<script type="text/javascript" src="<%=root %>/pages/index/NavigationMenu.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/ReportMenu.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/index.js"></script>
	<script type="text/javascript">
		Show = function( anim ){
			App.currentBar = Ext.MessageBox.show({
		       msg: '正在提交数据...&nbsp;&nbsp;<a href=#>[取消]</a>',
		       wait: true,
		       waitConfig: { interval: 150 },
		       icon:'icon-download',
		       animEl: anim
		   });
		   return App.currentBar;
		}	
	</script>
  </body>
</html>
