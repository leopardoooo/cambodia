<%@ page contentType="text/html;charset=GBK" %>
<html>
<title>报表异常信息</title>
<body>

<%
	Exception e = ( Exception ) request.getAttribute( "exception" );
	out.println( "<div style='color:red'>" + e.getMessage() + "</div>" );
%>

</body>
</html>