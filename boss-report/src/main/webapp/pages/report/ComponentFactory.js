/**
 * 组件生产工厂
 * @type 
 */
 
ComponentFactory = {
	//报表名称
	createRepName:function(rep_name,rep_id){
		var o={
			xtype:'textfield',
			width:200,
			fieldLabel:'报表名称',
			style : 'padding:3px 2px;vertical-align:middle;background:url() repeat-x scroll 0 0 ;border:0;',
			value:rep_name+'('+rep_id+')',
			readOnly:true
		};
		return o;
	},
	//数据源
	createDatabase:function(database_text){
		var o={
			xtype:'textfield',
			width:200,
			fieldLabel:'数据源',
			style : 'padding:3px 2px;vertical-align:middle;background:url() repeat-x scroll 0 0 ;border:0;',
			value:database_text,
			readOnly:true
		};
		return o;
	},
	//日期类型组件
	createDateField : function(attr,rep_id){
		//单日期组件
		if(Ext.isEmpty(attr["samelinekey"])){
			var o={ fieldLabel: attr["name"],
					id : rep_id+attr['key']+"_id",
					name : attr["key"],
					xtype : 'datefield',
					width : 200,
					allowBlank:(attr["isnull"]=='T'?false:true),
					format:"Ymd",
					htmlcode:attr["htmlcode"]
					};
			return o;				
		}else{	
		 //双日期组件
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
								id : rep_id+attr['key']+"_id",
								name:attr["key"],
								xtype:'datefield',
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
									id : rep_id+attr["samelinekey"]['key']+"_id",
									name:attr["samelinekey"]["key"],
									xtype:'datefield',
									format:"Ymd",
									htmlcode:attr["samelinekey"]["htmlcode"],
									width:85
							}]}]
					 }]}
					 ;
			 return o;
		}
	},
	
	//日期带时分秒类型组件
	createDateTimeField : function(attr,rep_id){
		//单日期组件
		if(Ext.isEmpty(attr["samelinekey"])){
			var o={ fieldLabel: attr["name"],
					id : rep_id+attr['key']+"_id",
					name : attr["key"],
					xtype : 'datetimefield',
					width : 200,
					allowBlank:(attr["isnull"]=='T'?false:true),
					format:"Ymd H:i:s",
					timeinit:'23:59:59',
					htmlcode:attr["htmlcode"] 
				};
			return o;				
		}else{	
		 //双日期组件
		 var o={layout:'form',
			    xtype:'panel',
			    border:false,
			    width:480,
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
								id : rep_id+attr['key']+"_id",
								name:attr["key"],
								xtype:'datetimefield',
								format:"Ymd H:i:s",
								timeinit:'00:00:00',
								allowBlank:(attr["isnull"]=='T'?false:true),
								htmlcode:attr["htmlcode"],
								width:132
							}]}	,
							{columnWidth: .5,
							labelWidth : 15,
							items:[{
								    fieldLabel: "至",
									displayField : 'name',
									valueField : 'id',
									id : rep_id+attr["samelinekey"]['key']+"_id",
									name:attr["samelinekey"]["key"],
									xtype:'datetimefield',
									format:"Ymd H:i:s",
									timeinit:'23:59:59',
									htmlcode:attr["samelinekey"]["htmlcode"],
									width:132
							}]}]
					 }]}
					 ;
			 return o;
		}
	},
	//文本域
	createTextArea  : function(attr,rep_id){
		var o = {
			fieldLabel: attr["name"],
			id : rep_id+attr['key']+"_id",
			name : attr["key"],
			xtype : 'textarea',
			width : 200,
			height:60,
			allowBlank:(attr["isnull"]=='T'?false:true),
			//emptyText:'添加多个请用逗号分隔!',
			htmlcode:attr["htmlcode"]
		};
		return o;			
	},
	//创建文本框
	createTextField : function(attr,rep_id){
		if(Ext.isEmpty(attr["samelinekey"])){
			var o = {
				fieldLabel: attr["name"],
				id : rep_id+attr['key']+"_id",
				name : attr["key"],
				xtype : 'textfield',
				width : 200,
				allowBlank:(attr["isnull"]=='T'?false:true),
				htmlcode:attr["htmlcode"]
			};
			return o;
		}else{
			//双文本框
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
								id : rep_id+attr['key']+"_id",
								name : attr["key"],
								xtype : 'textfield',
								allowBlank:(attr["isnull"]=='T'?false:true),
								htmlcode:attr["htmlcode"],
								width:100
							}]}	,{columnWidth: .5,
							labelWidth : 15,
							items:[{
								fieldLabel: "至",
								id : rep_id+attr["samelinekey"]['key']+"_id",
								name:attr["samelinekey"]["key"],
								xtype:'textfield',
								htmlcode:attr["samelinekey"]["htmlcode"],
								width:100
							}]}]}]
							};
			 return o;
		}
	},
	//下拉单选框
	createCombo : function(attr,rep_id){
		var o = {
			fieldLabel: attr["name"],
			displayField : 'name',
			valueField : 'id',
			xtype: 'combo',
			store : new Ext.data.JsonStore({
				fields : ['id','name'],
				data: []
			}),
			width : 200,
			hiddenName :attr['key'],
			forceSelection : true,
			triggerAction:'all',
			mode:'local',
			id : rep_id+attr['key']+"_id",
			htmlcode:attr["htmlcode"],
			editable : (attr["isnull"]=='T'&&!attr['sonkey']?false:true)
		};
		return o;
	},
	//下拉多选框
	createLovCombo : function(attr,rep_id){
		var o = {
			fieldLabel: attr["name"],
			displayField : 'name',
			valueField : 'id',
			xtype: 'lovcombo',
			store : new Ext.data.JsonStore({
				fields : ['id','name']
			}),
			width : 200,
			hiddenName :attr['key'],
			forceSelection : true,
			triggerAction:'all',
			mode:'local',
			id : rep_id+attr['key']+"_id",
            //emptyText : "输入关键字过滤",
            editable : true,
            htmlcode:attr["htmlcode"],
            listeners:{
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
		return o;
	},
	createItemSelector:function(attr,rep_id){
		var o=	{		        xtype : 'itemselector',
								name : attr["key"],
								id : rep_id+attr['key']+"_id",
								htmlcode:'itemselector',
								fieldLabel : attr["name"],
								allowBlank : false,
								delimiter:"','",
								imagePath : '/' + Constant.ROOT_PATH_LOGIN
										+ '/resources/images/itemselectorImage',
								multiselects : [{
											legend : '待选',
											width : 150,
											height : 270,
											store :  new Ext.data.JsonStore({
														fields : ['id',
																'name']
													}),
											displayField : 'name',
											valueField : 'id'
											,tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
												listeners:{
													scope:this,
													keyup:function(txt,e){
														if(e.getKey() == Ext.EventObject.ENTER){
															var value = txt.getValue();
																Ext.getCmp(rep_id+attr['key']+"_id").multiselects[0].store.filterBy(function(record){
																	if(Ext.isEmpty(value))
																		return true;
																	else
																		return record.get('name').indexOf(value)>=0;
																},this);
														}
													}
												}
											}]
										}, {
											legend : '已选',
											width : 150,
											height : 270,
											store :  new Ext.data.JsonStore({
														fields : ['id',
																'name']
													}),
											displayField : 'name',
											valueField : 'id'
										}]
								   		
							};
							return o;
	},
	//复选框
	createRemoteCheckBoxGroup:function(attr,rep_id){
		var o={
			fieldLabel: attr["name"],
			id : rep_id+attr['key']+"_id",
			xtype:'remotecheckboxgroup',
			name : attr["key"],
			width : 460,
			columns:3,
			htmlcode:attr["htmlcode"]
		};
		return o;
	},
	//文件上传查询组件
	createFileUpload:function(attr,rep_id,obj){
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
								id : rep_id+attr['key']+"_id_show",
								xtype : 'textfield',
								allowBlank:(attr["isnull"]=='T'?false:true),
								htmlcode:attr["htmlcode"],
								readOnly:true,
								width:100
							}]}	,{columnWidth: .4,
							labelWidth : 5,
							items:[{
								xtype: 'button',
                        		text: '上传文件',
                        		scope: obj,
                        		handler: function(){this.uploadfile(rep_id+attr['key']+"_id_show",rep_id+attr['key']+"_id",attr["key"]);}
							}]},{
							columnWidth: .1,
							items:[{
								id : rep_id+attr['key']+"_id",
								name : attr["key"],
								xtype : 'hidden',
								htmlcode:attr["htmlcode"]
							}]
							}
							]}]
							};
		return o;
	}
}