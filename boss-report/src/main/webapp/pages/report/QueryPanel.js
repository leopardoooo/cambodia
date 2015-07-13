/**
 * 报表查询面板
 * @class QueryPanel
 * @extends Ext.form.FormPanel
 */
QueryPanel = Ext.extend( Ext.form.FormPanel ,{
	rep_id: null,
	rep_name:null,
	rep_key_date:null,
	repDefine: null,
	roleMap:null,
	constructor: function(rep_id,rep_name){
		this.rep_id = rep_id;
		this.rep_name=rep_name;
		QueryPanel.superclass.constructor.call(this,{
			collapseMode:'mini',
			autoScroll:true,
			split: true,
			border: false,
			layout: 'fit'
			//,listeners:{ scope: this,'afterrender': this.doInitKeys}
		});
	},
	initComponent : function(){
		QueryPanel.superclass.initComponent.call(this);
		this.initData();
	}, // 初始化数据
	initData: function(){
		Ext.Ajax.request({
			scope: this,
			url:root +'/query/Key!initKeys.action',
			params: {rep_id:this.rep_id},//async:false,
			success: function(res , ops){
				var data = Ext.decode(res.responseText);
				this.rep_key_date=data;
				this.repDefine = data.simpleObj;
				this.roleMap=data.others;
				
				this.callback(data);
			}
		});
	},
	callback: function(data){
		this.addItems(data); 
		this.doLayout();
	    this.doInitKeys();
	},
	addItems: function( attrs ){
		var items = this.createField(attrs);		
		var queryframe={
		    	frame:false,
		    	//bodyStyle:'padding-left:150px;padding-top:10px',
		    	bodyStyle:'padding-top:10px',
		    	layout : 'form',
		    	border : false,
		    	items : items,
		    	autoScroll: true,
		    	buttonAlign : 'left',
		    	tbar:['-',
		    		{text:'配置报表',scope:this,handler:this.configRep},'-',
		    		{text:'查看报表说明',scope:this,handler:this.openRemark},'-',
		    		{text:'设为我的收藏',scope:this,handler:this.saveMyReport},'-','->','-',
		    		{text:'清除缓存',scope:this,handler:this.saveClearCache},'-'
		    	]}; 
        if(Ext.isEmpty(this.rep_key_date.simpleObj.detail_id)){
        	
            if(this.roleMap && this.roleMap['EDITREP'] === true){
	        	items.push({
	        		xtype: 'panel',
	        		height: 40,
	        		border: false,
	        		bodyStyle: 'padding: 10px 0px 0px 170px;',
	        		layout:'column',
	        		defaults: {border: false},
	        		items: [{
	        			width: 70,
	        			xtype: 'button',
	        			text: '查询',
	        			scope:this, 
	        			handler: function(){ this.doQuery(); }
	        		},{
	        			width: 10,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '查询任务',
	        			scope:this, 
	        			handler: function(){this.doTaskQuery();}
	        		}]
	        	});
        	}else{
	        	items.push({
	        		xtype: 'panel',
	        		height: 40,
	        		border: false,
	        		bodyStyle: 'padding: 10px 0px 0px 170px;',
	        		layout:'column',
	        		defaults: {border: false},
	        		items: [{
	        			width: 70,
	        			xtype: 'button',
	        			text: '查询',
	        			scope:this, 
	        			handler: function(){ this.doQuery(); }
	        		}]
	        	});
        	}
        	
        }else{
        	if(this.roleMap && this.roleMap['EDITREP'] === true){
        		items.push({
	        		xtype: 'panel',
	        		height: 40,
	        		border: false,
	        		bodyStyle: 'padding: 10px 0px 0px 0px;',
	        		layout: 'column',
	        		defaults: {border: false},
	        		items: [{
	        			width: 140,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '统计',
	        			scope:this, 
	        			handler: function(){ this.doQuery(); }
	        		},{
	        			width: 10,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '明细',
	        			scope:this, 
	        			handler: function(){
	        				this.doQuery(this.rep_key_date.simpleObj.detail_id); 
	        			}
	        		},{
	        			width: 10,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '统计任务',
	        			scope:this, 
	        			handler: function(){this.doTaskQuery();}
	        		},{
	        			width: 10,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '明细任务',
	        			scope:this, 
	        			handler: function(){ this.doTaskQuery(this.rep_key_date.simpleObj.detail_id); }
	        		}]
        		});	
        	}else{
        		items.push({
	        		xtype: 'panel',
	        		height: 40,
	        		border: false,
	        		bodyStyle: 'padding: 10px 0px 0px 0px;',
	        		layout: 'column',
	        		defaults: {border: false},
	        		items: [{
	        			width: 140,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '统计',
	        			scope:this, 
	        			handler: function(){ this.doQuery(); }
	        		},{
	        			width: 10,
	        			html: "&nbsp;"
	        		},{
	        			width: 70,
	        			xtype: 'button',
	        			text: '明细',
	        			scope:this, 
	        			handler: function(){
	        				this.doQuery(this.rep_key_date.simpleObj.detail_id); 
	        			}
	        		}]
        		});
        	}
        	
        }
        this.add(queryframe);
	},//生成查询界面选择的数据
	createKeyValueMap:function(){
		var parjson={};
		var repKeyList=[];
		for(var j= 0 ;j< this.rep_key_date.records.length ; j++){
			var newValue={};
			newValue["key"]=this.rep_key_date.records[j].key;
			newValue["value"]=Ext.getCmp(this.rep_id+this.rep_key_date.records[j].key+"_id").getValue();
			if(this.rep_key_date.records[j].htmlcode==='datefield'&&!Ext.isEmpty(newValue["value"]))
				newValue["value"]=newValue["value"].format('Ymd');
			if(this.rep_key_date.records[j].htmlcode==='datetimefield'&&!Ext.isEmpty(newValue["value"]))
				newValue["value"]=newValue["value"].format('Ymd H:i:s');
			repKeyList.push(newValue);
			if(!Ext.isEmpty(this.rep_key_date.records[j].samelinekey)){
					var newValue2={};
					newValue2["key"]=this.rep_key_date.records[j].samelinekey.key;
					newValue2["value"]=Ext.getCmp(this.rep_id+this.rep_key_date.records[j].samelinekey.key+"_id").getValue();
					if(this.rep_key_date.records[j].samelinekey.htmlcode==='datefield'&&!Ext.isEmpty(newValue2["value"]))
						newValue2["value"]=newValue2["value"].format('Ymd');
					if(this.rep_key_date.records[j].samelinekey.htmlcode==='datetimefield'&&!Ext.isEmpty(newValue2["value"]))
						newValue2["value"]=newValue2["value"].format('Ymd H:i:s');
					repKeyList.push(newValue2);
			}
		}
		parjson["repkeys"]=repKeyList;  
		return parjson;
	},//验证条件是否可为空
	doKeyValueVisible:function(){
		var datefieldnamelist='',datefieldsign=false;
		for(var j= 0 ;j< this.rep_key_date.records.length ; j++){
			if(this.rep_key_date.records[j].isnull==='T'){
				if(this.rep_key_date.records[j].htmlcode==='datefield'){//日期组件特殊处理，所有不可为空的日期组件只一个有值，就可通过验证
					datefieldnamelist=datefieldnamelist+' '+this.rep_key_date.records[j].name;
					if (!Ext.isEmpty((Ext.getCmp(this.rep_id+this.rep_key_date.records[j].key+"_id").getValue())))
						datefieldsign=datefieldsign||false;
					else
						datefieldsign=datefieldsign||true;
				}else if(Ext.isEmpty((Ext.getCmp(this.rep_id+this.rep_key_date.records[j].key+"_id").getValue()))){
					Alert(this.rep_key_date.records[j].name+' 不能为空');
					return false;
				}
			}
		}
		if(datefieldsign) {
			Alert(datefieldnamelist+' 请至少选择一个设置日期');
			return false;
		}
		return true;
	},//执行查询,detail_id为不空表示执行明细查询
	doQuery:function(detail_id){
		 if(!this.doKeyValueVisible()) return;
		 var mask = Show();//进度条
		 Ext.Ajax.request({
			scope : this,
			timeout:9999999999,
			params: {
				jsonParams:Ext.encode(this.createKeyValueMap()),
				rep_id:(Ext.isEmpty(detail_id)?this.rep_id:detail_id)
			},
			url: root + '/query/Report!initQuery.action',
			success: function(res,ops){
				mask.hide();
				mask=null;
				var rs = Ext.decode(res.responseText);
				var query_id=rs.query_id;
				if(query_id==null){
					alert("你没查询出数据！");
					return;
				}
				var quiee_raq=rs.quiee_raq;
				this.openShow(query_id,Ext.isEmpty(detail_id)?quiee_raq:null);
			}
		 });
	},
	doTaskQuery: function(detail_id){
		if(!this.doKeyValueVisible()) return;
		var repId = (Ext.isEmpty(detail_id)?this.rep_id:detail_id);
		var win = new TaskWin();
		var form = win.formPanel.getForm();
		win.show();
		form.findField('rep_id').setValue(repId);
		form.findField('rep_name').setValue(this.rep_name);
		win.setJsonParams(Ext.encode(this.createKeyValueMap()));
	},
	//创建普通报表查询显示界面
	createCommonShow:function(query_id){
		Ext.Ajax.request({
     			scope : this,
     			url : root +'/query/Show!queryHeader.action',
     			params: {query_id : query_id},
     			success : function(res,opt){
     				var headers = Ext.decode(res.responseText).records;
     				var repgrid=new RepResultGrid(headers,query_id,this.rep_id,this.rep_key_date.simpleObj.rep_info);
					this.ownerCt.insert(0,repgrid);
					this.ownerCt.doLayout();
     			}
		});
	},
	createOlapShow: function(query_id){
		var xtable = new ResultXTable(query_id, this.rep_id, this.repDefine.rep_info);
 		this.ownerCt.insert(0, xtable);
		this.ownerCt.doLayout();
		xtable.doViewHtml();
	},
	//创建快逸报表查询显示界面
	createQuieeShow:function(query_id,quiee_raq){
		/**
			//加载快逸报表模块
			this.collapse(true);
			this.ownerCt.queryShow.getTopToolbar().add({
				text:"返回查询界面",
				//scope:this,
				handler:function(){
					//this=button;this.ownerCt=tbar;this.ownerCt.ownerCt=queryShow;
					this.ownerCt.ownerCt.ownerCt.queryPanel.expand();
				}
			})
			var url=root+"/reportJsp/showReport.jsp?raq=/"+quiee_raq+".raq&query_id="+query_id;
			//更改iframe指向的资源
			document.getElementById(this.rep_id+'quieeiframe').src = url;
			document.getElementById(this.rep_id+'quieeiframe').location.href=url;
		**/
		if(Ext.getCmp(this.rep_id+'quieetab')){
			App.getApp().page.remove(Ext.getCmp(this.rep_id+'quieetab'));
		}
		App.page.add({
					title : this.rep_name,
					id : this.rep_id+'quieetab',
					closable: true,
					tbar:[{text:"返回查询界面",scope:this,handler:function(){
							if(!Ext.getCmp(this.rep_id+'MainPanel')){
								App.getApp().page.add({
									title : this.rep_name,
									id : this.rep_id+'MainPanel',
									closable: true,
									items : [new MainPanel(this.rep_id,this.rep_name)]
								});
							}
							App.page.activate(this.rep_id+'MainPanel');
							App.getApp().page.remove(this.rep_id+'quieetab');
					}}],
					html :"<iframe id='"+this.rep_id+"quieeiframe' width=100% height=100%  frameborder=no src='"
							+root+"/reportJsp/showReport.jsp?raq=/"+quiee_raq+".raq&query_id="+query_id+"'></iframe>",
					listeners : { 	
						scope:this,
						beforedestroy : function(){//销毁iframe
							document.getElementById(this.rep_id+'quieeiframe').src='javascript:false';
						}
					}
		});
		App.page.activate(this.rep_id+ 'quieetab');
		App.getApp().page.remove(this.rep_id+'MainPanel');
	},
	//执行结果集展现
	openShow:function(query_id,quiee_raq){
		if( this.repDefine.rep_type === "olap"){
			this.createOlapShow(query_id);
		}else{
			if(Ext.isEmpty(quiee_raq)){
				this.createCommonShow(query_id);
			}else{
				this.createQuieeShow(query_id,quiee_raq);
			}
		}
	},
	//动态创建field查询组件
	createField: function( attrs ){
		
		var items = [];
		items.push(ComponentFactory.createRepName(this.rep_name, this.rep_id));
		items.push(ComponentFactory.createDatabase(attrs.simpleObj["database_text"]));
		
		if(!Ext.isEmpty(attrs)&& attrs.records.length > 0){
		    for(var j= 0 ;j< attrs.records.length ; j++){
		    	var attr=attrs.records[j];
		    	var field = {xtype:'textfield',width:200,fieldLabel:attr.name,value:attr.htmlcode+' 组件未定义',readOnly:true};
				if(attr["htmlcode"] == 'combo'){
					field= ComponentFactory.createCombo(attr,this.rep_id);
					if(attr['sonkey'] == true)
				    	Ext.apply(field,{listeners:{scope:this,'select': this.doInitKeys }});
				}else if(attr["htmlcode"] == 'lovcombo'){
					field= ComponentFactory.createLovCombo(attr,this.rep_id);
					if(attr['sonkey'] == true)
				    	Ext.apply(field,{listeners:{scope:this,'select': this.doInitKeys }});				    
				}else if(attr["htmlcode"] == 'textarea'){   
					field= ComponentFactory.createTextArea(attr,this.rep_id);
				}else if(attr["htmlcode"] == 'textfield'){
					field= ComponentFactory.createTextField(attr,this.rep_id);
				}else if(attr["htmlcode"] == 'datefield'){
		    		field= ComponentFactory.createDateField(attr,this.rep_id);
				}else if(attr["htmlcode"] == 'remotecheckboxgroup'){
			 		field=ComponentFactory.createRemoteCheckBoxGroup(attr,this.rep_id);
				}else if(attr["htmlcode"]=='itemselector'){
					field=ComponentFactory.createItemSelector(attr,this.rep_id);
				}else if(attr["htmlcode"]=='datetimefield'){
					field=ComponentFactory.createDateTimeField(attr,this.rep_id);
				}else if(attr["htmlcode"]=='fileupload'){
					field=ComponentFactory.createFileUpload(attr,this.rep_id,this);
				}
		    	items.push(field);
		    }
		}
		return items;
	},
	doInitKeys : function(c){
		var field_key="";
		var field_countyid="";
		var field_value="";
		if(c){
			if(c.hiddenName){
				field_key = c.hiddenName;
			}
			if(c.getValue().split("','").length===1){
				field_value= c.getValue();
				if(Ext.getCmp(this.rep_id+"#countyid#"+"_id")!=null){
					field_countyid=Ext.getCmp(this.rep_id+"#countyid#"+"_id").getValue();
				}
			}
		}
		Ext.Ajax.request({
				scope:this,
                params : {
                	rep_id:this.rep_id,key:field_key,key_value:field_value,countyid:field_countyid
                },
				url: root + '/query/Key!loadAllKeyValue.action',
				//async: false,   //ASYNC 是否异步( TRUE 异步 , FALSE 同步)
				success: function(res,ops){
					var rs = Ext.decode(res.responseText);
					for(var key in rs.others){
						if(Ext.getCmp(this.rep_id+key+"_id")){
							if(Ext.getCmp(this.rep_id+key+"_id").htmlcode=='itemselector'){
								Ext.getCmp(this.rep_id+key+"_id").multiselects[0].store.removeAll();
								Ext.getCmp(this.rep_id+key+"_id").multiselects[0].store.loadData(rs.others[key]);
								Ext.getCmp(this.rep_id+key+"_id").multiselects[1].store.removeAll();
								
							}else if(Ext.getCmp(this.rep_id+key+"_id").getStore){
								Ext.getCmp(this.rep_id+key+"_id").getStore().removeAll();
								Ext.getCmp(this.rep_id+key+"_id").clearValue();
								Ext.getCmp(this.rep_id+key+"_id").getStore().loadData(rs.others[key]);
							    //加载默认显示第一个
								if(rs.others[key].length==1){
									 Ext.getCmp(this.rep_id+key+"_id").setValue(rs.others[key][0].id);
									 Ext.getCmp(this.rep_id+key+"_id").setReadOnly(true);
								}else{
									Ext.getCmp(this.rep_id+key+"_id").setReadOnly(false);
								}
								//对下拉单选框 增加一个空行选择
								if(Ext.getCmp(this.rep_id+key+"_id").htmlcode=='combo'&&rs.others[key].length>1){
									if(Ext.getCmp(this.rep_id+key+"_id").editable){
										//如果条件允许为空 则插入一条空数据
										var Plant= Ext.getCmp(this.rep_id+key+"_id").getStore().recordType;
										var p = new Plant({
											'id': '','name': '选择所有'
										});
										Ext.getCmp(this.rep_id+key+"_id").getStore().insert(0,p);
									}else{
										Ext.getCmp(this.rep_id+key+"_id").setValue(rs.others[key][0].id);
									}
								}
							}else{
								var htmlcode=Ext.getCmp(this.rep_id+key+"_id").htmlcode;
								if(htmlcode=='textarea'||htmlcode=='textfield'||htmlcode=='datefield'||htmlcode=='datetimefield'){
									var v_queryvaluelist=rs.others[key];
									if(!Ext.isEmpty(v_queryvaluelist)&&v_queryvaluelist.length>0)
									    if(htmlcode=='datefield'){
									    	var v_date=Date.parseDate(v_queryvaluelist[0].id,"Ymd");
									    	Ext.getCmp(this.rep_id+key+"_id").setValue(v_date);
									    }else if(htmlcode=='datetimefield'){
									    	var v_date=Date.parseDate(v_queryvaluelist[0].id,"Ymd H:i:s");
									    	Ext.getCmp(this.rep_id+key+"_id").setValue(v_date);
									    } else{
											Ext.getCmp(this.rep_id+key+"_id").setValue(v_queryvaluelist[0].id);
									    }
								}else if(htmlcode==='remotecheckboxgroup'){
									Ext.getCmp(this.rep_id+key+"_id").reload(rs.others[key]);
								}
							}
						}
					}
			    }
		});
    },//配置报表
	configRep:function(){
		//判断该操作员是否有权限配置报表
		Ext.Ajax.request({
			url : root + '/query/RepDesign!queryRepDefine.action',
			params : {rep_id:this.rep_id},
			scope : this,
			success : function(res, opt) {
				var repdto=Ext.decode(res.responseText);
				if(Ext.getCmp('deployReportId')){
					App.getApp().page.remove(Ext.getCmp('deployReportId'));
				}
				App.page.add({
					title : '配置 '+this.rep_name,
					id : 'deployReportId',
					closable: true,
					items : [new DeployReportForm(repdto)]
				});
				App.page.activate('deployReportId');
			}
		});
	},//报表说明
	openRemark:function(){
		Ext.Ajax.request({
			url : root + '/query/RepDesign!queryRepRemark.action',
			params : {rep_id:this.rep_id},
			scope : this,
			success : function(res, opt) {
				var win = Ext.getCmp('oneRemarkWinId');
				if( !win)
					win=new OneRemarkWin();
				win.show(Ext.decode(res.responseText),this.rep_id);
			}
		});
	},//设为我的收藏
	saveMyReport:function(){
		Ext.Ajax.request({
			url:root+"/query/RepDesign!saveMyRep.action",
			params:{rep_id:this.rep_id},
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data) Ext.MessageBox.alert("提示框","收藏成功");
			}
		});
	},//组件上传文件
	uploadfile:function(file_show_id,file_value_id,key_name){
		var win=Ext.getCmp('uploadqueryfilewinId');
		if(!win)
			win=new UploadQueryFileWin();
		win.show(this.rep_id,file_show_id,file_value_id,key_name);
		//Alert("只支持excel文档,最多支持1-6列数据，第一列第一行数据不能为空.");
	},//清空报表缓存
	saveClearCache:function(){
		Ext.Ajax.request({
			url:root+"/query/RepDesign!saveRepClearCache.action",
			params:{rep_id:this.rep_id},
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data) Ext.MessageBox.alert("提示框","<center>缓存清理成功</center>");
			}
		});
	}
});

//任务配置弹出框
var TaskWin = Ext.extend(Ext.Window, {
	formPanel: null,
	taskExecday: null,	//上一次选择的值
	jsonParams: null,
	weekArr:[
		{text:'星期一',value:'2'},
		{text:'星期二',value:'3'},
		{text:'星期三',value:'4'},
		{text:'星期四',value:'5'},
		{text:'星期五',value:'6'},
		{text:'星期六',value:'7'},
		{text:'星期日',value:'1'}
	],
	constructor: function(){
		this.formPanel = new Ext.form.FormPanel({
			border:false,
			bodyStyle: 'padding-top:10px',
			labelWidth:75,
			defaults:{xtype:'textfield'},
			items:[
				{xtype:'hidden',name:'rep_id'},
				{fieldLabel:'报表名称',name:'rep_name',readOnly:true},
				{fieldLabel:'任务名称',name:'task_name',allowBlank: false},
				{xtype:'paramcombo',fieldLabel:'任务类型',hiddenName:'task_type',paramName:'REPORT_TASK_TYPE',
					listeners:{
						scope: this,
						select: this.doTaskType
					}
				},
				{fieldLabel:'执行日',hiddenName:'task_execday',xtype:'combo',
					store:new Ext.data.JsonStore({
						fields:['text','value']
					}),displayField:'text',valueField:'value',triggerAction:'all'
				},
				{fieldLabel:'备注',xtype:'textarea',name:'remark',width:'90%',height:80}
			]
		});
		TaskWin.superclass.constructor.call(this,{
			title:'任务配置',
			closeAction:'close',
			width:400,
			height:280,
			layout:'fit',
			items:[this.formPanel],
			buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:function(){this.close();}}
			]
		});
		App.form.initComboData([this.formPanel.getForm().findField('task_type')]);
	},
	setJsonParams: function(jsonParams){
		this.jsonParams = jsonParams;
	},
	doTaskType: function(combo,record){
		var value = combo.getValue();
		if(this.taskExecday != value){	//上一次选择的值 和 当前值不同
			var ItemRecord = Ext.data.Record.create(['text','value']);
			
			this.formPanel.getForm().findField('task_execday').setDisabled(false);
			this.formPanel.getForm().findField('task_execday').setValue('');
			
			if(value == 'one'){				//一次性执行
				if(this.taskExecday != 'one'){	//上一次值不是one时，才删除重新创建日期选择框
					this.formPanel.remove(this.formPanel.getForm().findField('task_execday'));
					this.formPanel.insert(4, new Ext.form.DateField({
						fieldLabel:'执行时间',name:'task_execday',format:'Y-m-d',minValue:nowDate().add(Date.DAY,1)
					}));
					this.formPanel.doLayout();
				}
			}else{
				if(this.taskExecday == 'one' && value != 'day'){ //上一次值是one 且 当前值不是day时，才删除重新创建日期选择框
					this.formPanel.remove(this.formPanel.getForm().findField('task_execday'));
					this.formPanel.insert(4, new Ext.form.ComboBox({fieldLabel:'执行日',hiddenName:'task_execday',xtype:'combo',
						store:new Ext.data.JsonStore({
							fields:['text','value']
						}),displayField:'text',valueField:'value',triggerAction:'all'
					}));
					this.formPanel.doLayout();
				}
				
				if(value == 'day'){			//每日重复
					this.formPanel.getForm().findField('task_execday').setDisabled(true);
				}else if(value == 'month'){	//每月重复
					var arr = [];
					for(var i=1;i<31;i++){
						arr.push(new ItemRecord({'text': i+'号', 'value': i}));
					}
					var store = this.formPanel.getForm().findField('task_execday').getStore();
					store.removeAll();
					store.add(arr);
				}else if(value == 'week'){	//每周重复
					var arr = [];
					arr.push(new ItemRecord({'text':'星期一','value': '2'}));
					arr.push(new ItemRecord({'text':'星期二','value': '3'}));
					arr.push(new ItemRecord({'text':'星期三','value': '4'}));
					arr.push(new ItemRecord({'text':'星期四','value': '5'}));
					arr.push(new ItemRecord({'text':'星期五','value': '6'}));
					arr.push(new ItemRecord({'text':'星期六','value': '7'}));
					arr.push(new ItemRecord({'text':'星期日','value': '1'}));
					var store = this.formPanel.getForm().findField('task_execday').getStore();
					store.removeAll();
					store.add(arr);
				}
			}
			
			this.taskExecday = value;
		}
	},
	doSave: function(){
		var form = this.formPanel.getForm();
		if(form.isValid()){
			var values = form.getValues();
			var obj={};
			obj['reptask'] = Ext.encode(values);
			obj['jsonParams'] = this.jsonParams;
			
//			var mask = Show();
			Ext.Ajax.request({
				scope : this,
				params: obj,
				url: root + '/query/Report!saveRepTask.action',
				success: function(res,ops){
//					mask.hide();
//					mask=null;
					var rs = Ext.decode(res.responseText);
					if(rs === true){
						Alert('保存成功');
						this.close();
					}
				}
			 });
		}
	}
});

var OneRemarkWin=Ext.extend(Ext.Window, {
    rep_id:null,
	constructor : function(){
		OneRemarkWin.superclass.constructor.call(this, {
			id:'oneRemarkWinId',
			title:'报表说明',
			width : 410,
			height : 320,
			layout:'fit',
			items:[{
                layout: 'form',
                border:false,
                bodyStyle:'padding-top:12px',
                labelWidth: 1,
                items: [{
                    name: 'remark',
                    fieldLabel: '',
                    xtype: 'textarea',
                    width: 380,
                    height: 230
                }]
            }],
			buttonAlign : 'center',
			buttons : [ {text:'修改',scope:this,handler:function(){this.saveRemark();}},{text : '关闭',scope : this,handler : function() {this.hide();}}]
		});
	},
	show:function(remark,rep_id){
		OneRemarkWin.superclass.show.call(this);
		this.find('name', 'remark')[0].setValue(remark);
		this.rep_id=rep_id;
	},
	saveRemark:function(){
		 if(!Ext.isEmpty(this.rep_id)){
		     Ext.Ajax.request({
				url:root+"/query/RepDesign!saveReportRemark.action",
				params:{rep_id:this.rep_id,mycube_remark:this.find('name', 'remark')[0].getValue()},
				scope:this,
				success:function(res){
					Alert("修改成功!");
					this.hide();
				}
			});
		}else{
		    Alert("只读!");
		}
	}
});
	
var UploadQueryFileWin = Ext.extend(Ext.Window, {
    form: null,
    rep_id:null,
    file_show_id:null,
    file_value_id:null,
    key_name:null,
    constructor: function () {
        this.form = new Ext.form.FormPanel({
            border: false,
            labelWidth: 70,
            fileUpload: true,
            trackResetOnLoad: true,
            bodyStyle: 'padding-top:10px',
            defaults: {
                baseCls: 'x-plain'
            },
            items: [{
                id: 'uploadqueryfileId',
                xtype: 'textfield',
                fieldLabel: '上传文件',
                name: 'uploadqueryfile',
                inputType: 'file',
                anchor: '95%',
                emptyText: ''
            }]
        });
        UploadQueryFileWin.superclass.constructor.call(this, {
            id: 'uploadqueryfilewinId',
            width: 300,
            height: 100,
            border: false,
            closeAction: 'hide',
            title: '上传',
            items: [this.form],
            buttonAlign: 'right',
            buttons: [{
                text: '保存',
                scope: this,
                handler: this.doSave
            },
            {
                text: '关闭',
                scope: this,
                handler: function () {
                	this.close();
                }
            }],
            listeners: {
                scope: this,
                hide: function () {
                    Ext.getCmp('uploadqueryfileId').getEl().dom.select();
                    document.execCommand("delete"); // 删除选中区域
                    this.hide();
                }
            }
        });
    },show:function(rep_id,file_show_id,file_value_id,key_name){
		UploadQueryFileWin.superclass.show.call(this);
		this.file_show_id=file_show_id;
		this.file_value_id=file_value_id;
		this.key_name=key_name;
	},
    doSave: function () {
        var name = Ext.getCmp('uploadqueryfileId').getValue();
        name = name.substring(name.lastIndexOf('\\') + 1); // 获取上传文件名
		//文件名显示
        Ext.getCmp(this.file_show_id).setValue(name);
        this.form.getForm().submit({
            url: root + '/query/Key!uploadQueryFile.action',
            params: {
                rep_id:this.rep_id,key:this.key_name
            },
            waitTitle: '提示',
            waitMsg: '正在上传中,请稍后...',
            scope: this,
            success: function (form, action) {
                var data = action.result;
                Alert('上传成功');    
	            Ext.getCmp(this.file_value_id).setValue(data.msg);
	            this.close();
            },
           failure:function (from,action){
           		var data = action.result;
           		//Alert('上传失败');
           		Ext.MessageBox.show({
           			title:'提示',
           			msg:'上传失败',
           			icon:Ext.MessageBox.ERROR,
           			buttons:Ext.MessageBox.OK
           		})
           		
           }
        });
    }
});