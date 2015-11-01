/**
 * 处理登录页面的事件函数。
 */
Ext.ns("Login");

Ext.apply( Login , {
	isLogin : false,
	DEFAULT_LOGIN_SUBSYS_URL: "/{0}/pages/index/index.jsp",
	
	//设为首页
	setHome: function(obj){
		var vrl = window.location.href;
	    try{
	    	obj.style.behavior='url(#default#homepage)';
	    	obj.setHomePage(vrl);
	    }catch(e){
	        if(window.netscape) {
	            try {
	            	netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
	            }catch (e) {
	                alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");
	            }
	            var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
	            prefs.setCharPref('browser.startup.homepage',vrl);
	    	}
	    }
	},
	//登陆
	goLogin: function(){
		if (this.isLogin){
			alert("请不要重复登陆，或重新打开后登陆");
			return;
		}
		var loginName= Ext.get( "loginName" ), 
			pwd= Ext.get( "pwd" ),
			rmb= Ext.get( "rmbUser"),
			msg= Ext.get( "msg" ),
			lang = Ext.get("lang");
		
		if( Ext.isEmpty(loginName.getValue())){
			//uid.radioClass(errorCls);
			msg.update("<li>请输入您的用户名!</li>");
			loginName.focus();
			return;
		}else if(Ext.isEmpty(pwd.getValue())){
			//pwd.radioClass(errorCls);
			msg.update("<li>请输入您的密码!</li>");
			pwd.focus();
			return ;
		}
		msg.update("<li>正在登录...</li>");
		Ext.Ajax.request({
			params: {
				loginName: loginName.getValue(),
				pwd: pwd.getValue(),
				lang: lang.getValue()
			},
			url: "login",
			scope: this,
			success: function( res, ops){
				var o = Ext.decode(res.responseText);
				var errs = o["records"];
				if(errs && errs.length > 0){
					msg.update("<li>"+ errs[0] +"</li>");
				}else{
					
					var Days = 3000; 
				    var exp = new Date(); 
				    exp.setTime(new Date().getTime() + Days*24*60*60*1000); 
				    document.cookie = "boss_login_language="+ escape(lang.getValue()) + ";expires=" + exp.toGMTString();
					
					msg.update("<li>正在跳转...</li>");
					var urls = o['simpleObj'];
					this.openUrl( urls['sub_system_url']+"/rego?sub_system_id="+urls['sub_system_id']);
					msg.update("<li>登录成功</li>");
					this.isLogin=true;
					//window.close();
				}
			}
		});
	},
	openUrl: function( url ){
		//登陆后不弹出一个独立页面
		window.location.href = url;
		return;
		//登陆后弹出一个独立页面，需要浏览器设置当前站点安全
		var width = screen.width - 10;
		var height = screen.height - 60;
		var property = 'left=1,top=1,resizable=no,status=no,menubar=no,scrollbars=no,location=no,toolbar=no,' +
				'width=' + width + ',height=' + height;
		//给登录窗口命名，以防从首页"安全退出"后重新登录时，关闭IE
		window.name='ycsoft_boss_login';
		var sysWin = window.open(url, "win_boss_ycsoft", property);
		if(null!=sysWin){
			if(sysWin.closed){
				sysWin  =  window.open(url, "win_boss_ycsoft", property);
			}else{
				sysWin.focus();
			}
		}else{
			//可以提示允许弹出窗口
		}
	},
	//回车将光标移动至下一个元素
	enterToNext: function( own , nextE ){
		Ext.EventManager.on( own , 'keydown', function(e){
			if(e.getCharCode() == 13){
				var ne = Ext.get(nextE).dom;
				if(ne.tagName == "button" || 
					(ne.tagName == "input" && ne.type == "button")){
					ne.click();
				}else{
					ne.focus();
				}
			}
		});
		return Login.enterToNext;
	}
});
