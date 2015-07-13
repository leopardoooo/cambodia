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
  <script type="text/javascript" src="<%=r %>/pages/test/test.js" charset="UTF-8" ></script>
   
  <script type="text/javascript">
 		App.clearLoadImage();
  </script>
</html> 