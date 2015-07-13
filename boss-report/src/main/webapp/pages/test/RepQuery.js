/*
 * 查询界面
 */
 
App.pageSize = 100 ;

//明细报表回传结果集
RepResultGrid =Ext.extend(Ext.grid.GridPanel,{ 
	constructor:function(headers,query_id,rep_name){
		var fields = [], columns=[], rows = [];
     	for(i=0;i<headers.length;i++){
     		if(i==headers.length - 1){
     			var j=0;
     			Ext.each(headers[i],function(h){       				 	
					columns.push({header:h.col_desc, dataIndex:h.col_desc});
					fields.push({name : h.col_desc,mapping : j});
					j++;
				});
     		}else{
     			var row = [];
     			Ext.each(headers[i],function(h){  
	     				 		row.push({header: h.col_desc,align: 'center',colspan: h.col_length});
				});
				rows.push(row);
     		}     					
		}

		var group = new Ext.ux.grid.ColumnHeaderGroup({
			rows: rows
		});
		// 报表信息查询
		var QueryStore = new Ext.data.JsonStore({ 
			url  : root + '/query/Show!queryReport.action' ,
			root : 'records' ,
			totalProperty: 'totalProperty',
			autoDestroy: true,
			baseParams: {limit: App.pageSize,start: 0 ,query_id : query_id },
			fields: fields       
		}); 
		QueryStore.load({
			params : {query_id : query_id}
		});
		
		RepResultGrid.superclass.constructor.call(this,{
			store:QueryStore,
			columns:columns,
			plugins: group,

			stripeRows: true,             
			tbar:[{text:'返回查询界面',scope:this,handler:this.returnQueryPanel},
				{text:'下载',scope:this,handler:function(){
				window.location.href=root+"/query/Show!downloadExp.action?query_id="
					+query_id+"&rep_name="+rep_name;
				}
			}],
			bbar: new Ext.PagingToolbar({store: QueryStore ,pageSize:App.pageSize})// ,//分页信息
		});
	},
	returnQueryPanel:function(){
		this.ownerCt.remove(this);
	}
});
//报表查询条件面板
QueryPanel = Ext.extend( Ext.form.FormPanel ,{
	rep_id: null,
	rep_name:null,
	rep_key_date:null,
	constructor: function(rep_id,rep_name){
		this.rep_id = rep_id;
		this.rep_name=rep_name;
		QueryPanel.superclass.constructor.call(this,{
			collapseMode:'mini',
			autoScroll:true,
			split: true,
			border: false,
			layout: 'absolute'
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
				this.callback(data);
			}
		});
	},
	callback: function(data){
		this.addItems(data); 
	},
	addItems: function( attrs ){
		var items = [];
		items.push({
			xtype:'textfield',
			width:200,
			fieldLabel:'报表名称',
			style : 'padding:3px 2px;vertical-align:middle;background:url() repeat-x scroll 0 0 ;border:0;',
			value:this.rep_name+'('+this.rep_id+')',
			readOnly:true
		},{
			xtype:'textfield',
			width:200,
			fieldLabel:'数据源',
			style : 'padding:3px 2px;vertical-align:middle;background:url() repeat-x scroll 0 0 ;border:0;',
			value:attrs.simpleObj["database_text"],
			readOnly:true
		});
		if(!Ext.isEmpty(attrs)&& attrs.records.length > 0){
		    for(var j= 0 ;j< attrs.records.length ; j++){
		    	var field = this.createField(attrs.records[j]);
		    	items.push(field);
		    }
		}
        if(Ext.isEmpty(this.rep_key_date.simpleObj.detail_id)){
		    this.add({
		    	frame : false,
		    	//bodyStyle:'padding-left:150px;padding-top:10px',
		    	bodyStyle:'padding-top:10px',
		    	layout : 'form',
		    	anchor : '95%',
		    	border : false,
		    	items : items,
		    	buttonAlign : 'left',
		    	tbar:[
		    		{text:'配置报表',scope:this,handler:this.configRep},'-',
		    		{text:'查看报表说明',scope:this,handler:this.openRemark},'-',
		    		{text:'设为我的收藏',scope:this,handler:this.saveMyReport}
		    	],
		    	buttons: ['&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp',
		    			  {text: ' 查询 ',scope:this, handler: function(){this.doQuery()}}
		    	]
		    });
        }else{
        	this.add({
        		frame :false, 
		    	//bodyStyle:'padding-left:150px;padding-top:10px',
		    	bodyStyle:'padding-top:10px',
		    	layout : 'form',
		    	anchor : '95%',
		    	border : false,
		    	items : items,
		    	buttonAlign : 'left',
		    	tbar:[
		    		{text:'配置报表',scope:this,handler:this.configRep},'-',
		    		{text:'查看报表说明',scope:this,handler:this.openRemark},'-',
		    		{text:'设为我的收藏',scope:this,handler:this.saveMyReport}
		    	],
		    	buttons: ['&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp',
		    			  {text: ' 统计 ',scope:this, handler: function(){this.doQuery()}},'&nbsp&nbsp&nbsp&nbsp',
		    			  {text: ' 明细 ',scope:this, handler: function(){this.doQuery(this.rep_key_date.simpleObj.detail_id)}}
		    	]
		    });
        }
	    this.doLayout();
	    this.doInitKeys();
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
			repKeyList.push(newValue);
			if(!Ext.isEmpty(this.rep_key_date.records[j].samelinekey)){
					var newValue2={};
					newValue2["key"]=this.rep_key_date.records[j].samelinekey.key;
					newValue2["value"]=Ext.getCmp(this.rep_id+this.rep_key_date.records[j].samelinekey.key+"_id").getValue();
					if(this.rep_key_date.records[j].samelinekey.htmlcode==='datefield'&&!Ext.isEmpty(newValue2["value"]))
						newValue2["value"]=newValue2["value"].format('Ymd');
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
	//执行结果集展现
	openShow:function(query_id,quiee_raq){
		
		if(Ext.isEmpty(quiee_raq)){
			//加载普通报表 表头加载
			Ext.Ajax.request({
     			scope : this,
     			url : root +'/query/Show!queryHeader.action',
     			params: {query_id : query_id},
     			success : function(res,opt){
     				var headers = Ext.decode(res.responseText).records;   
     				var repgrid=new RepResultGrid(headers,query_id,this.rep_name);
					this.ownerCt.insert(0,repgrid);
					this.ownerCt.doLayout();
     			}
			});
		}else{
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
		}
	},
	//动态创建field查询组件
	createField: function( attr ){
		if(attr["htmlcode"] == 'combo'){
			return this.createCombo(attr);
		}else if(attr["htmlcode"] == 'lovcombo'){
			return this.createLovCombo(attr);
		}else if(attr["htmlcode"] == 'textarea'){   
			return this.createTextArea(attr);
		}else if(attr["htmlcode"] == 'textfield'){
			return this.createTextField(attr);
		}else if(attr["htmlcode"] == 'datefield'){
		    return this.createDateField(attr);
		}else if(attr["htmlcode"] == 'remotecheckboxgroup'){
			 return this.createRemoteCheckBoxGroup(attr);
		}else{ return {xtype:'textfield',width:200,fieldLabel:attr.name,value:attr.htmlcode+' 组件未定义',readOnly:true};	}
	},
	createDateField : function(attr){
		
		if(Ext.isEmpty(attr["samelinekey"])){
			var o={ fieldLabel: attr["name"],
					id : this.rep_id+attr['key']+"_id",
					name : attr["key"],
					xtype : attr["htmlcode"],
					width : 200,
					allowBlank:(attr["isnull"]=='T'?false:true),
					format:"Ymd",
					htmlcode:attr["htmlcode"]
				};
			return o;				
		}else{	
		 var o={layout:'form',
			    xtype:'panel',
			    border:false,
			    width:400,
				items:[{ layout: 'column',
						anchor : '100%',
						border : false,
						defaults : {
							border : false,
							layout : 'form'
						},items:[
						   {columnWidth: .5,
						    items:[{
							    fieldLabel: attr["name"],
								displayField : 'name',
								valueField : 'id',
								id : this.rep_id+attr['key']+"_id",
								name:attr["key"],
								xtype:attr["htmlcode"],
								format:"Ymd",
								allowBlank:(attr["isnull"]=='T'?false:true),
								htmlcode:attr["htmlcode"],
								width:85
							}]}	,
							{columnWidth: .5,
							labelWidth : 15,
							items:[{
								    fieldLabel: "至",
									displayField : 'name',
									valueField : 'id',
									id : this.rep_id+attr["samelinekey"]['key']+"_id",
									name:attr["samelinekey"]["key"],
									xtype:attr["samelinekey"]["htmlcode"],
									format:"Ymd",
									htmlcode:attr["samelinekey"]["htmlcode"],
									width:85
							}]}]
					 }]}
					 ;
			 return o;
		}
	},
	createTextArea  : function(attr){
		var o = {
			fieldLabel: attr["name"],
			id : this.rep_id+attr['key']+"_id",
			name : attr["key"],
			xtype : attr["htmlcode"],
			width : 200,
			height:60,
			allowBlank:(attr["isnull"]=='T'?false:true),
			//emptyText:'添加多个请用逗号分隔!',
			htmlcode:attr["htmlcode"]
		};
		return o;			
	},
	createTextField : function(attr){
		if(Ext.isEmpty(attr["samelinekey"])){
			var o = {
				fieldLabel: attr["name"],
				id : this.rep_id+attr['key']+"_id",
				name : attr["key"],
				xtype : attr["htmlcode"],
				width : 200,
				allowBlank:(attr["isnull"]=='T'?false:true),
				htmlcode:attr["htmlcode"]
			};
			return o;
		}else{
			var o={layout:'form',
			    xtype:'panel',
			    border:false,
			    width:450,
				items:[{ layout: 'column',
						anchor : '100%',
						border : false,
						defaults : {
							border : false,
							layout : 'form'
						},items:[
						   {columnWidth: .5,
						    items:[{
							    fieldLabel: attr["name"],
								id : this.rep_id+attr['key']+"_id",
								name : attr["key"],
								xtype : attr["htmlcode"],
								allowBlank:(attr["isnull"]=='T'?false:true),
								htmlcode:attr["htmlcode"],
								width:100
							}]}	,{columnWidth: .5,
							labelWidth : 15,
							items:[{
								fieldLabel: "至",
								id : this.rep_id+attr["samelinekey"]['key']+"_id",
								name:attr["samelinekey"]["key"],
								xtype:attr["samelinekey"]["htmlcode"],
								htmlcode:attr["samelinekey"]["htmlcode"],
								width:100
							}]}]}]
							};
			 return o;
		}
	},
	createCombo : function(attr){
		var o = {
			fieldLabel: attr["name"],
			displayField : 'name',
			valueField : 'id',
			xtype: attr["htmlcode"],
			store : new Ext.data.JsonStore({
				fields : ['id','name']
			}),
			width : 200,
			hiddenName :attr['key'],
			forceSelection : true,
			triggerAction:'all',
			mode:'local',
			id : this.rep_id+attr['key']+"_id",
			htmlcode:attr["htmlcode"],
			editable : (attr["isnull"]=='T'&&!attr['sonkey']?false:true)
		};
		
		//存在子级的添加监听，加载子级的数据
	    if(attr['sonkey'] == true){
	    	Ext.apply(o,{
	    		 listeners:{  
					scope: this,
					'select': this.doInitKeys
	    		 }
	    	})
	    }
		
		return o;
	},
	createLovCombo : function(attr){
		var o = {
			fieldLabel: attr["name"],
			displayField : 'name',
			valueField : 'id',
			xtype: attr["htmlcode"],
			store : new Ext.data.JsonStore({
				fields : ['id','name']
			}),
			width : 200,
			hiddenName :attr['key'],
			forceSelection : true,
			triggerAction:'all',
			mode:'local',
			id : this.rep_id+attr['key']+"_id",
            //emptyText : "输入关键字过滤",
            editable : true,
            htmlcode:attr["htmlcode"]
            ,listeners:{
            	beforequery:function(e){
            		var combo = e.combo;    
				    try{       
				        var value = e.query;   
				           
				        combo.lastQuery = value;    
				        combo.store.filterBy(function(record,id){      
				            var text = record.get(combo.displayField);    
				            return (text.indexOf(value)!=-1);      
				        });   
				        combo.onLoad();   
				        combo.expand();   
				        return false;   
				    }catch(e){   
				        combo.selectedIndex = -1;   
				        combo.store.clearFilter();   
				        combo.onLoad();    
				        combo.expand();   
				        return false;     
				    }   

            	}
            }
		};
		//存在子级的添加监听，加载子级的数据
	    if(attr['sonkey'] == true){
	    	Ext.apply(o,{
	    		 listeners:{  
					scope: this,
					'select': this.doInitKeys
	    		 }
	    	})
	    }
		return o;
	},
	createRemoteCheckBoxGroup:function(attr){
		var o={
			fieldLabel: attr["name"],
			id : this.rep_id+attr['key']+"_id",
			xtype:attr["htmlcode"],
			name : attr["key"],
			width : 460,
			columns:3,
			htmlcode:attr["htmlcode"]
		};
		return o;
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
							if(Ext.getCmp(this.rep_id+key+"_id").getStore){
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
								if(htmlcode=='textarea'||htmlcode=='textfield'||htmlcode=='datefield'){
									var v_queryvaluelist=rs.others[key];
									if(!Ext.isEmpty(v_queryvaluelist)&&v_queryvaluelist.length>0)
									    if(htmlcode=='datefield'){
									    	var v_date=Date.parseDate(v_queryvaluelist[0].id,"Ymd");
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
					title : '配置报表',
					id : 'deployReportId',
					closable: true,
					items : [new DeployReportForm(repdto)]
				});
				App.page.activate('deployReportId');
			}
		});
	},//报表说明
	openRemark:function(){
		var win = Ext.getCmp(this.rep_id+("oneRemarkWinId"));
		if( !win)
			win=new OneRemarkWin(this.rep_id);
		win.show();
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
	}
});

var OneRemarkWin=Ext.extend(Ext.Window, {
	constructor : function(rep_id){
		this.logGrid=new LogGrid(rep_id);
		this.logGrid.getStore().load();
		OneRemarkWin.superclass.constructor.call(this, {
			id:rep_id+"oneRemarkWinId",
			border : false,
			width : 550,
			height : 350,
			items:[this.logGrid],
			layout:'border',
			buttonAlign : 'center',
			buttons : [ {text : '关闭',scope : this,handler : function() {this.hide();}}]
		});
}});	

MainPanel = Ext.extend(Ext.Panel,{
	queryPanel:null,
	constructor : function(rep_id,rep_name){
		this.queryPanel=new QueryPanel(rep_id,rep_name);
		this.queryPanel.expand();
		MainPanel.superclass.constructor.call(this,{
			id :  rep_id+'panelId',
	   		baseCls: "x-plain",
	   		layout : 'fit',
	   		items : [this.queryPanel]
		})}
});