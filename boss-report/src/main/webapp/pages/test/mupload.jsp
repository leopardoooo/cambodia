<%@ page language="java"  pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  <body>
  	<%@ include file="/pages/commons/library-ext.jsp" %>
   </body>
  		<script type="text/javascript">
	var rep_id=<%=request.getParameter("rep_id")%>
	</script>
  <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DistrictTreeCombo.js" charset="UTF-8"></script>
  <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/lovcombo.js" charset="UTF-8"></script>
  <script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js"></script>
  <script type="text/javascript" src="<%=r %>/pages/test/swfupload.js"  ></script>
  <script type="text/javascript" src="<%=r %>/pages/test/mupload.js" charset="UTF-8" ></script>

  <script type="text/javascript">
 		App.clearLoadImage();
  </script>
  <body>
         <h1 style="margin:20px 0px 0px 20px;">上传文件</h1>
         <br />
         <div style="padding-left:20px;">
<p>
    <div id="form1"></div><br>
     <div id="form2"></div><br>
    <div >执行操作：</div>
    <textarea id='op' rows="10" style="width:800px;"></textarea>

<br />
</div>
</html> 