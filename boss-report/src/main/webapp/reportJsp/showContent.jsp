<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="java.net.URLDecoder" %>
<%
	request.setCharacterEncoding("GBK");

	String regex = "http[s]?://.*";

	String reportUrl = request.getParameter("report_url");
	reportUrl = URLDecoder.decode(reportUrl, "GBK");
	if(!reportUrl.matches(regex)) reportUrl = request.getContextPath() + reportUrl;

	String commentUrl = request.getParameter("comment_url");
	commentUrl = URLDecoder.decode(commentUrl, "GBK");
	if(!commentUrl.matches(regex)) commentUrl = request.getContextPath() + commentUrl;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<style type="text/css">
	body{
		margin: 0px;
		background-color: #fff;
		font-size: 14px;
	}
	table{
		float: left;
		width: 100%;
		height: 100%;
		background-color: #fff;
		border-collapse: collapse;
		border-left:1px solid #dbdbdb ;
		
	}
	.toolbar{
		width: 100%;
		padding: 0px 0 0 10px;
		border: 0px solid #ccc;
		background: url(../images/tab-bg.gif) repeat-x left top;
	}
	.toolbar span{
		float: left;
		font-weight: bold;
		width: 94px;
		height: 32px;
		line-height:28px;
		cursor: pointer;
		font-size: 12px;
		text-align: center;
		color:#8a8a8b;
	    background: url(../images/tab-line.gif) no-repeat right top;
	}
	.toolbar span.selected{
		font-weight: bold;
		color:#ffffff;
		width:94px;
		height:32px;
		background: url(../images/tab-on.gif) no-repeat left top;
	}
	.content{
		width: 100%;
		height: 100%;
		border: 0px solid #ccc;
		padding: 0px;
		background:#ffffff;
	}
	.content div iframe{
		width: 100%;
		height: 100%;
		overflow: auto;
		background:#ffffff;
	}
	</style>
	<script language="javascript">
		// 设置当前内容
		var currContent = "report";
		function changeView(button){
			// 获取当前按钮和视图
			var currButton = document.getElementById("tb_"+currContent);
			var currView = document.getElementById(currContent+"_view");
			// 重置样式
			currButton.className = "";
			currView.style.display = "none";

			// 更新按钮和视图
			currContent = button.id.substring(3,button.id.length);
			button.className = "selected";
			var view = document.getElementById(currContent+"_view");
			view.style.display = "";
		}
	</script>
</head><body>
	<table>
		<tr><td class="toolbar">
			<span id="tb_report" class="selected" onClick="changeView(this);">报表</span>
			<span id="tb_comment" onClick="changeView(this);">介绍</span>
		</td></tr>
		<tr><td class="content">
			<div id="report_view" style="height: 100%;">
				<iframe frameborder="0"  border="0" src="<%=reportUrl%>"></iframe>
			</div>
			<div id="comment_view" style="height: 100%; display: none; background:#ffffff;">
				<iframe frameborder="0"  border="0" src="<%=commentUrl%>"></iframe>
			</div>
		</td></tr>
	</table>
</body></html>