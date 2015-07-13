<!-- 
	Author：hh
	Date  : 2009/12/25
	Action: 使用Ext库所需要导入的文件。使用时只需要包含该文件即可。
 -->
 
<%@ page contentType="text/html; charset=UTF-8" %>
<% 
	String r = request.getContextPath(),
		   boss_res = com.ycsoft.commons.constants.Environment.ROOT_PATH_BOSS_LOGIN; 
%>
<html>
	<head> 
		<link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/ext-all.css" type="text/css" />
		<link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/xtheme-blue.css" type="text/css" />
		<link rel="stylesheet" href="<%=boss_res %>/resources/css/boss-all.css" type="text/css" />
		<link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" />
		<link rel="stylesheet" href="<%=boss_res %>/resources/css/msg-tip.css" type="text/css" />
		<link rel="stylesheet" href="<%=boss_res %>/resources/css/index-other.css" type="text/css" />
		
		<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
		<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
		<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-lang-zh_CN.js" charset="UTF-8"></script>
		<script type="text/javascript">
			var root = '<%=r %>';
		</script>
		<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js"></script>
		<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
		
		
		<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
		<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ParamComboBox.js" charset="UTF-8"></script>
	</head>
	<body>
		 <div id='loading' >
			<img src="<%=boss_res %>/resources/images/loading.gif" width="32" height="32" 
				style="margin-right:4px;" align='absmiddle' />
			<span style='font: normal 12px arial,tahoma,sans-serif;font-weight:bold'>正在加载...</span>
		</div>
	</body>
</html>

