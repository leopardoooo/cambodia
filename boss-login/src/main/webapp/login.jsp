<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String root = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>尧辰网络BOSS系统v4.0-登录</title>
	<link rel="stylesheet" type="text/css" href="<%=root %>/resources/css/login.css">
  	<script type="text/javascript" src="<%=root %>/components/ext3/ext-core.js"></script>
  	<script type="text/javascript">
  		var root = '<%=root %>';
  	</script>
  	<script type="text/javascript" src="<%=root %>/pages/commons/Constant.js"></script>
  	<script type="text/javascript" src="<%=root %>/pages/login.js"></script>
  	<script type="text/javascript">
  	function SetHome(obj,vrl){
        try{
                obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);
        }
        catch(e){
                if(window.netscape) {
                        try {
                                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
                        }
                        catch (e) {
                                alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");
                        }
                        var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
                        prefs.setCharPref('browser.startup.homepage',vrl);
                 }
        }
	}
  	</script>
  </head>
  <body onload="javascript:document.getElementById('loginName').focus();">
  	<table cellpadding="0" cellspacing="0" border=0 width=100% height=100% >
  		<tr height="70px">
  			<td>
  				<div class="top">
  					<a href="#" onclick="SetHome(this,window.location)">设为首页</a> 
  					<a href="/help/">使用帮助</a>
  					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  				</div>
  			</td>
  		</tr>
  		<tr>
  			<td align="center">
  				<div id="login-info">
  					<div class="ml"></div>
  					<div class="input">
  						<div class ="logfrom">
  							<div class="logo"></div>
							<table class="logTab" cellpadding="0" cellspacing="0" border=0 width="100%">
								<tr>
									<th>用户名</th>
									<td class="inner-input-cmp">
										<input class="input-blur"  maxlength="20" id="loginName" value=''
											   onfocus="this.select();this.className='input-focus';" 
											   onblur="this.className='input-blur';"/>
									</td>
									<td></td>
								</tr>
								<tr>
									<th>密&nbsp;&nbsp;&nbsp;&nbsp;码</th>
									<td width="90" class="inner-input-cmp">
										<input class="input-blur" maxlength="20" id="pwd" type="password"  value=''
											   onfocus="this.select();this.className='input-focus';" 
											   onblur="this.className='input-blur';"/>
									</td>
									<td>
									</td>
								</tr>
								<tr>
									<th>&nbsp;</th>
									<td colspan=2>
										<div class="Sel_Ver">
											<input onmouseout="this.className='login-b'" 
												   onmousedown="this.className='login-b3'" 
												   onmouseover="this.className='login-b2'" 
												   type='button' id='btnSubmit'
												   class="login-b" onclick='Login.goLogin();'>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan=3 class="message"><ul id="msg"></ul></td>
								</tr>
							</table>
							</div>
  						</div>
  					<div class="mr"></div>
  				</div>
  			</td>
  		</tr>
  		<tr height="75px">
  			<td>
  				<div class="foot">
  					<ul>
  					</ul>
  					<div>	
  						尧辰科技版权所有　&copy; 2009-2014&nbsp; &nbsp; 
  					</div>
  				</div>
  			</td>
  		</tr>
  	</table>
  	 <script type="text/javascript">
	  	Ext.onReady(function(){
			Login.enterToNext('loginName','pwd')('pwd','btnSubmit');
	  	})
	  </script>
  </body>
</html>
