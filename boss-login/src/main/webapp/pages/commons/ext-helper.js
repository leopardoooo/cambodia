/**
 * Author: hh
 * Date:2009.12.25  
 * Action:
 *  提供对Ext library 的帮助类。
 */

Ext.Ajax.handleResponse = Ext.Ajax.handleResponse.createInterceptor(function(response, options){//处理函数。 
	var data = Ext.decode(response.responseText);
	if(data && data["success"]===false && data["exception"]){
		var obj = data["exception"];
		if(obj && obj.type){
			if(obj.type==1){//业务异常
				//显示后台抛出的异常
				Ext.MessageBox.show({
					title:obj.title,
					msg:obj.content,
					icon:Ext.MessageBox.ERROR,
					buttons:Ext.MessageBox.OK
				});
			}else if(obj.type==2){//系统异常
				new DebugWindow(obj).show();
			}else if(obj.type==7){//报表异常
				if(!Ext.isEmpty(obj.detail)){
					
					
					//处理出错后打开DebugWindow窗口，进度条无法关闭的问题
					var _al=Ext.MessageBox.show(" "," ");
					_al.hide();
					_al=null;
					new DebugWindow(obj).show();
					
				}else 
					Ext.MessageBox.show({
						title:obj.title,
						msg:obj.content,
						icon:Ext.MessageBox.ERROR,
					    buttons:Ext.MessageBox.OK
					});
			}else if(obj.type==3){//登录超时
				App.clearLoadImage();
				Ext.MessageBox.show({
					title:obj.title,
					msg:obj.content,
					icon:Ext.MessageBox.ERROR,
					buttons:Ext.MessageBox.OK,
					fn:function(){
						if (window.parent)
							window.parent.location=Constant.ROOT_PATH + "/goLogin.jsp";//强制重新登录
						else
							window.location=Constant.ROOT_PATH + "/goLogin.jsp";//强制重新登录
					}
				}); 
			}
		}
		options = options || response.argument.options;
		//自定义处理错误数据函数(函数名统一为clearData)
		if(options && Ext.isFunction(options["clearData"])){
			options.clearData.call();
		}
		return false;
	}
	return true;
});

Ext.Ajax.handleFailure= Ext.Ajax.handleFailure.createInterceptor(function(response){//处理函数。
	//readyState=0 未初始化状态，刚创建XMLHttpRequest对象，但还没有初始化
	//readyState=1 发送状态，已准备好把一个请求发送到服务器
	//readState=2  发送状态，已通过send()方法把请求发送到服务器，但还没收到响应
	//当XMLHttpRequest对象正在接受HTTP响应头信息，但还没有完成接受完(readyState=3 正在接收状态)
	//或 响应已完整接收(readyState=4 已加载)时，XMLHttpRequest的属性status才可用
	var url = response.options?response.options['url']:'';
//	Alert('系统异常  HTTP状态代码 : '+response['status']+' HTTP状态代码文本: '+response['statusText']+
//		' 是否超时: '+response['isTimeout']+' 是否中断 ：'+response['isAbort']
//		+' 请求URL：'+url);
	Alert('Error [' + response.statusText + ",("+ url +")]",function(){
//		if (window.parent){
//			window.parent.location.reload();
//		}else{
//			window.location.reload();
//		}
	});
	
	//Alert('系统异常');
}); 

/*********************************************************
 * other 
 */
Ext.ns("App");
Ext.apply( App, {
	//清除当前页面加载提示的图片。
	clearLoadImage: function(){
		setTimeout(function(){
			Ext.get('loading').remove();
	        // Ext.get('loading').fadeOut({remove:true});
	    }, 250 );
	},
	//获取父窗口的App，当父窗口的App不存在时则返回当前页面的App.
	getApp: function(){
		return (parent.App || App);
	},
	//App作用域下的data对象
	getData: function(){
		return App.getApp().data;
	},
	getCust:function(){
		return App.getData().custFullInfo.cust;
	},
	//获取操作员编号
	getCustId: function(){
		return App.getData().custFullInfo.cust.cust_id;
	},
	getLinkman : function(){
		return App.getData().custFullInfo.linkman;
	},
	//获取父面板中的资料信息
	getValues: function(){
		var ps={}, 
			data=App.getData(); 
		
		
		//设置客户的信息
		ps["custFullInfo"] = data.custFullInfo;
		//设置选中的用户信息
		var selectedUsers = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUsers();
		ps["selectedUsers"] = selectedUsers;
//		ps["selectedAtvs"] = [];
//		ps["selectedDtvs"] = [];
//		ps["selectedBands"] = [];
//		for (i=0;i<selectedUsers.length;i++){
//			if (selectedUsers[i].user_type=="OTT"){
//				ps["selectedUsers"].push(selectedUsers[i]);
//			}
//			if (selectedUsers[i].user_type=="DTT"){
//				ps["selectedDtvs"].push(selectedUsers[i]);
//			}
//			if (selectedUsers[i].user_type=="BAND"){
//				ps["selectedBands"].push(selectedUsers[i]);
//			}
//		}
		//业务代码
		if (data.currentResource)
			ps["busiCode"] = data.currentResource.busicode;
		return ps ;
	},
	//根据指定的业务代码，获取配置数据
	//返回的格式如{busifee: {},busidoc: {}, tasktype:{}, busiextform: {}}
	findBusiCfgData: function( busiCode ){
		if(App.data.busiCfgData){
			return App.data.busiCfgData[busiCode];
		}
	},
	findCfgData: function(keyname){
		if(App.data.cfgData){
			return App.data.cfgData[keyname];
		}
	},
	//给定一个新的url去链接，加载显示
	href: function( url ){
		window.location.href = url ;
	},
	qtipValue: function (value) {
		if(Ext.isEmpty(value))return "";
		if(value == 'null') return "";
    	return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
	},
	hideTip : function(){
		//隐藏数据加载提示框
		Ext.getCmp('ViewPortPanel').hideTip();
	},
	showTip : function(){
		//显示数据加载提示框
		Ext.getCmp('ViewPortPanel').showTip();
	},
	removeTip : function(){
		//去除数据加载提示框
		Ext.getCmp('ViewPortPanel').removeTip();
	}
});


/***********************************************************
 * 以下是对组件提供的辅助函数，对应的组件辅助函数与ext的子包名一致
 * 如ComboBox 组件的辅助函数可以分为form
 */
Ext.ns("App.form");
Ext.apply( App.form ,{
	/**
	 * 获取cmb显示的字段值
	 * @param Combobox cmb
	 */
	getCmbDisplayValue: function( cmb ){
		var _vf = cmb.valueField;
		cmb.valueField = null ;
		var rs = cmb.getValue();
		cmb.valueField = _vf ; 
		return rs ; 
	},
	/**
	 * 初始化下拉框的数据,该下拉框必须设置paramName属性值,
	 * @param comboArrs 下拉框的数据
	 * @param callback 当下拉框初始完毕时被调用
	 * @param scope 回调函数的作用域
	 */
	initComboData: function( comboArrs , callback ,scope ){
		var params = [];
		for(var i=0;i < comboArrs.length;i++){
			params[i] = comboArrs[i].paramName;
		}
		if(params.length > 0 ){
			Ext.Ajax.request({
				params: {comboParamNames: params},
				url: root + "/ps.action",
				success: function( res, ops){
					var data = Ext.decode(res.responseText );
					for( var i=0;i<data.length ;i++ ){
						if(comboArrs[i].isContrary){
							comboArrs[i].displayField = 'item_value';
							comboArrs[i].valueField = 'item_name';
						}
						comboArrs[i].getStore().loadData(data[i]);
						// 设置默认值
						if(comboArrs[i].defaultValue){
							comboArrs[i].setValue(comboArrs[i].defaultValue);
							comboArrs[i].originalValue = comboArrs[i].defaultValue;
						}
						//判断是否需要添加一个空项
						if( comboArrs[i].allowBlankItem ){
							comboArrs[i].getStore().insert(0 ,new Ext.data.Record({
								item_name: lbc("common.plsSwitch"), item_value: ''
							}));
						}
						
					}
					//回调函数
					if(Ext.isFunction(callback)){
						callback.call(scope);
					}
				}
			});
		}
	}
});

