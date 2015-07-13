/**
 * 通授权
 */
var CmdFormPanel = Ext.extend(Ext.form.FormPanel, {
    supplier_id: null,
    dateNum : 31,
    constructor: function () {
        CmdFormPanel.superclass.constructor.call(this, {
            border: false,
            bodyStyle: 'padding-top:10px',
            region:'north',
                layout: 'column',
                defaults: {
                    baseCls: 'x-plain',
                    layout: 'form',
                    labelWidth: 65
                },
                items: [{
                    columnWidth: 1,
                    items: [
		                    {
		                xtype: 'displayfield',
		                value: "<font style='font-family:微软雅黑;font-size:16px;color:red'><b>" + "本授权针对所有的机顶盒，请谨慎操作，如有疑问请联系管理员，切不可随意操作</b></font>"
		            }
                    ]},{
                    labelWidth: 100,
                    columnWidth: 0.3,
                    items: [{
                        xtype: 'combo',
                        fieldLabel: '服务器',
                        hiddenName: 'server_id',
                        store: new Ext.data.JsonStore({
                            url: root + '/resource/Job!queryServerByCountyId.action',
                            fields: ['server_id', 'server_name', 'supplier_id']
                        }),
                        displayField: 'server_name',
                        valueField: 'server_id',
                        forceSelection: true,
                        editable: true,
                        allowBlank: false,
                        listeners: {
                            scope: this,
                            select: function (combo, record) {
                                this.supplier_id = record.get('supplier_id');
                            }
                        }
                    }]
                },{
                    labelWidth: 75,
                    columnWidth: 0.7,
                    items: [{
            			fieldLabel:'发送类型',
            			hiddenName:'ca_type',
						xtype:'lovcombo',
						allowBlank: false,
						id:'caTypeId',
						store:new Ext.data.ArrayStore({
							fields:['item_name','item_value'],
							data:[['OSD催缴','OSD'],['邮件','MAIL']]
						}),displayField:'item_name',valueField:'item_value',
						triggerAction:'all',mode:'local',editable:false,
						beforeBlur:function(){}
						}]
				}, {
                    labelWidth: 100,
                    columnWidth: 0.3,
                    items: [{
                        fieldLabel: '发送开始时间',
                        xtype: 'datefield',
                        id: 'startDateId',
                        editable: false,
                        vtype: 'daterange',
                        endDateField: 'endDateId',
                        name: 'start_date',
                        format: 'Y-m-d',
                        allowBlank: false,
                        value: new Date().format('Y-m-d'),
                        minValue: new Date().format('Y-m-d'),
                        listeners:{
							scope:this,
		            		blur: function(comp){
		            			var v = comp.getValue();
		            			if(v){
		            				var time = v.add(Date.DAY,this.dateNum)
		            				if(time<Ext.getCmp('endDateId').getValue()){
		            					Ext.getCmp('endDateId').setValue(null);
		            				}
		            				Ext.getCmp('endDateId').setMaxValue(time);
		            			}else{
		            				Ext.getCmp('endDateId').setMaxValue(null);
		            			}
		            		}
						}
                    }]
                }, {
                    labelWidth: 75,
                    columnWidth: 0.25,
                    items: [{
                        fieldLabel: '截止时间',
                        xtype: 'datefield',
                        editable: false,
                        vtype: 'daterange',
                        id: 'endDateId',
                        allowBlank: false,
                        startDateField: 'startDateId',
                        name: 'end_date',
                        format: 'Y-m-d',
                        maxValue:nowDate().add(Date.DAY,this.dateNum)
                    }]
                },{
                    labelWidth: 5,
                    columnWidth: 0.45,
                    items:[
	                    {
				    	xtype : 'displayfield',
				    	html :"<font style='font-family:微软雅黑;font-size:16px;color:red'><b>" + "最大值是开始时间后"+this.dateNum+"天</b></font>" 
                    }]
                }, 
                {
                	columnWidth:1,border:false,
                	layout:'column',
                	defaults:{layout:'form',border:false},
                	items:[
                		{
	                    labelWidth: 100,
	                    columnWidth: 0.3,
	                    items: [{
	                        xtype: 'xdatetime',
	                        fieldLabel: '发送开始时段',
	                        allowBlank: false,
	                        id: 'sendTimeId',
	                        timeWidth: 80,
	                        name: 'send_time',
	                        timeFormat: 'H:i',
	                        timeConfig: {
	                            increment: 60,
	                            altFormats: 'H:i',
	                            editable: true,
	                            allowBlank: false
	                        },
	                        dateConfig: {
	                            hidden: true
	                        }
	                    }]
	                }, {
	                    labelWidth: 75,
	                    columnWidth: 0.7,
	                    items: [{
	                        xtype: 'xdatetime',
	                        fieldLabel: '截止时段',
	                        allowBlank: false,
	                        id: 'endTimeId',
	                        timeWidth: 80,
	                        name: 'end_time',
	                        timeFormat: 'H:i',
	                        timeConfig: {
	                            increment: 60,
	                            altFormats: 'H:i',
	                            editable: true,
	                            allowBlank: false
	                        },
	                        dateConfig: {
	                            hidden: true
	                        }
	                    }]
	                }
                	]
                }
                ,{labelWidth: 100,
                    columnWidth: 0.3,
                    items: [
                    	{
				layout:'table',
				layoutConfig: { columns:12 },
				baseCls: 'x-plain',
				fieldLabel : '发送周期',
				anchor: '100%',
				labelWidth: 75,				
			    items: [
                    {
			    	width : 40,
			    	xtype:'numberfield',
			    	allowDecimals:false, //不允许输入小数 
			    	allowNegative:false, //不允许输入负数
			    	maxValue:10,  
			    	value:'1',
			    	id:'sendNumId',
			    	allowBlank: false,
			    	name : 'send_num'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'次/'
			    },{
			    	width : 40,
			    	xtype:'numberfield',
			    	allowDecimals:false, 
			    	allowNegative:false,
			    	maxValue:24,  
			    	value:'1',
			    	id:'timeNumId',
			    	allowBlank: false,
			    	name : 'time_num'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'小时'
			    }]
                }
                ]},{labelWidth: 75,
                    columnWidth: 0.7,
                    items: [{
				layout:'table',
				layoutConfig: { columns:12 },
				baseCls: 'x-plain',
				fieldLabel : '发送遍数',
				anchor: '100%',
				labelWidth: 75,				
			    items: [
                    {
			    	width : 50,
			    	xtype:'numberfield',
			    	allowDecimals:false, 
			    	allowNegative:false,
			    	maxValue:5,        
			    	value:'1',
			    	id:'sendForId',
			    	allowBlank: false,
			    	name : 'send_for'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'遍/次'+"<font style='font-family:微软雅黑;font-size:14px;color:red'><b>" + "&nbsp;&nbsp;&nbsp;&nbsp;范围是1-5</b></font>"
			    }]}
                    ]
                    }, {
                    labelWidth: 100,
                    columnWidth: 1,
                    items: [{
                        xtype: 'textarea',
                        fieldLabel: '授权内容',
                        name: 'detail_params',
                        width: 600,
                        height: 50,
                        allowBlank: false
                    }]
                }],

            buttonAlign: 'center',
            buttons: [{
                text: '授权',
                scope: this,
                handler: this.doSave
            }, {
                text: '取消',
                scope: this,
                handler: function () {
                    this.getForm().reset();
                }
            }]
        });
    },
    initComponent: function () {
        CmdFormPanel.superclass.initComponent.call(this);
        this.getForm().findField('server_id').getStore().load();
    },
    doSave: function () {
        if (!this.getForm().isValid()) return;
        var sendTime = Ext.getCmp('sendTimeId').getValue();
        var endTime = Ext.getCmp('endTimeId').getValue();
        var startDate = Ext.getCmp('startDateId').getValue();
        if(sendTime>endTime){
        	Alert("开始时间点不能小于截止时间点!");
        	Ext.getCmp('endTimeId').setValue(null);
        	return false;
        }
        if(startDate.format("Y-m-d") == nowDate().format("Y-m-d")){
        	if(sendTime.format("Y-m-d H:i:s") < nowDate().format("Y-m-d H:i:s")){
        		Alert("发送开始时间不能小于当前时间!");
        		Ext.getCmp('sendTimeId').setValue(null);
        		return false;
        	}
        }
        
        var values = {};
//      var values = this.getForm().getValues();
        values['server_id'] = this.getForm().findField('server_id').getValue();
        values['supplier_id'] = this.supplier_id;
        values['start_date'] = startDate;
        values['end_date'] = Ext.getCmp('endDateId').getValue();
        values['send_time']= sendTime;
        values['end_time'] =endTime; 
        values['ca_type'] = Ext.getCmp('caTypeId').getValue();
        values['send_num'] = Ext.getCmp('sendNumId').getValue();
        values['time_num'] = Ext.getCmp('timeNumId').getValue();
        values['send_for'] = Ext.getCmp('sendForId').getValue();
        values['detail_params'] = this.getForm().findField('detail_params').getValue();
        
        var sendTimeDate = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate(),
        	sendTime.getHours(), sendTime.getMinutes());		//开始发送时间
        var endTimeDate = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate(),
        	endTime.getHours(), endTime.getMinutes());		//发送结束时间
        	
        //执行多少天
        var minDay = (values['end_date'].getTime() - values['start_date'].getTime()) / 86400000 + 1;
        var seconds = (values['time_num']/values['send_num'])*3600;  			//每次需要多少秒
        var onceTime = ( endTimeDate.getTime() - sendTimeDate.getTime() )/1000;	//时间段一共多少秒;
		var numTime = parseInt(onceTime/seconds); 		//在时间段里，执行多少次		
		
		var count = minDay * values['send_for'];
		if(numTime > 0){
			count *= numTime;
		}
		
		Confirm("总共发送<b> "+count+" </b>次授权", this, function(){
			var mb = Show();// 显示正在提交
	        Ext.Ajax.request({
	            url: root + '/resource/Job!saveSendAllCmd.action',
	            params: values,
	            scope: this,
	            success: function (res, opt) {
	            	mb.hide();// 隐藏提示框
					mb = null;
	                var data = Ext.decode(res.responseText);
	                if (data.success === true) {
	                     Alert('通授权成功!', function () {
	                        Ext.getCmp('SendAllGridId').getStore().load({
	                            params: {
	                                start: 0,
	                                limit: Constant.DEFAULT_PAGE_SIZE
	                            }
	                        });
	                    });
	                    this.getForm().reset();
	                }
	            }
	        });
		});
    }
});
SendAllGrid = Ext.extend(Ext.grid.GridPanel, {
	sendstore : null,
	constructor : function(cfg) {
		Ext.apply(this, cfg || {});
		SendAllGridThiz = this;
		this.sendstore = new Ext.data.JsonStore({
					url : root + '/resource/Job!querySendAllCmd.action',
					fields : ['done_code', 'server_name', 'done_date', 'message','optr_id',
							'optr_name', 'county_id','num','next_time','ca_type'],
					root : 'records',
					totalProperty : 'totalProperty',
					autoDestroy : true
				});

		this.sendstore.load({
					params : {
						start : 0,
						limit : Constant.DEFAULT_PAGE_SIZE
					}
				});
		SendAllGrid.superclass.constructor.call(this, {
			region : 'center',
			id : 'SendAllGridId',
			ds : this.sendstore,
			sm : new Ext.grid.CheckboxSelectionModel(),
			autoExpandColumn : 'message',
			cm : new Ext.grid.ColumnModel([{
						header : '操作流水',
						width : 60,
						dataIndex : 'done_code'						
					},{
						header : '发送类型',
						width: 60,
						dataIndex :'ca_type'
						
					},{
						header : '未发送数',
						width : 60,
						dataIndex : 'num'
					}, {
						header : 'CA服务器',
						width : 75,
						dataIndex : 'server_name'						
					}, {
						header : '创建时间',
						width : 120,
						dataIndex : 'done_date'
					}, {
						header : '下次发送时间',
						width : 120,
						dataIndex : 'next_time'											
					}, {
						id : 'message',
						header : '内容',
						width : 160,
						dataIndex : 'message',
						renderer : App.qtipValue
					},{
						header : '操作人',
						width : 75,
						dataIndex : 'optr_name'
					}, {
				       header: '操作',
				       width:60,
				       scope:this,
			           renderer:function(value,meta,record,rowIndex,columnIndex,store){
			           		var btns = this.doFilterBtns(record);
			            	return btns;
					   }
			
					}]),
			bbar : new Ext.PagingToolbar({
						store : this.sendstore
					}),
			tbar : [' ', '查询条件:', new Ext.ux.form.SearchField({
								store : this.sendstore,
								width : 220,
								hasSearch : true,
								emptyText : '支持授权内容'
							})]

		})
	},
	doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || record.get('county_id') == App.data.optr['county_id']){
			btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick='SendAllGridThiz.deleteRecord();'> 删除 </a>";
		}
		return btns;
	},
    deleteRecord: function () {
    	var grid  = Ext.getCmp('SendAllGridId');
    	var done_code = grid.getSelectionModel().getSelected().get('done_code');
        Confirm("确定要删除该通授权吗?", null, function () {
            Ext.Ajax.request({
                url: root + '/resource/Job!deleteSendAllCmd.action',
                params: {
                    done_code: done_code
                },
                success: function (res, ops) {
                    var rs = Ext.decode(res.responseText);
                    if (true === rs.success) {
                        Alert('操作成功!', function () {
                            Ext.getCmp('SendAllGridId').getStore().load({
                                params: {
                                    start: 0,
                                    limit: Constant.DEFAULT_PAGE_SIZE
                                }
                            });
                        });
                    } else {
                        Alert('操作失败');
                    }
                }
            });
        }) 
	}
});

CmdPropGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor: function(){
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Job!querySendAllCmdProp.action',
			root:'records',
			totalProperty:'totalProperty',
			autoDestroy: true,
			fields:['done_code','cas_id','server_name','supplier_id','supplier_name','cmd_type',
				'send_start_date','send_end_date','send_start_time','send_end_time','send_cycle',
				'send_times','send_repeat_times','optr_id','optr_name','message','done_date','cmd_type']
		});
		var columns = [
			{header:'流水号',dataIndex:'done_code',width:70},
			{header:'CA类型',dataIndex:'server_name',width:80},
			{header:'发送类型',dataIndex:'cmd_type',width:65},
			{header:'CA供应商',dataIndex:'supplier_name',width:70},
			{header:'开始发送时间',dataIndex:'send_start_date',width:80,renderer:Ext.util.Format.dateFormat},
			{header:'发送截止日期',dataIndex:'send_end_date',width:80,renderer:Ext.util.Format.dateFormat},
			{header:'发送开始时段',dataIndex:'send_start_time',width:80},
			{header:'发送截止时段',dataIndex:'send_end_time',width:80},
			{header:'发送次数',dataIndex:'send_times',width:60},
			{header:'发送周期(小时)',dataIndex:'send_cycle',width:90},
			{header:'发送遍数',dataIndex:'send_repeat_times',width:65},
			{header:'内容',dataIndex:'message',width:85,renderer:App.qtipValue},
			{header:'配置时间',dataIndex:'done_date',width:120},
			{header:'操作员',dataIndex:'optr_name',width:70}
		];
		CmdPropGrid.superclass.constructor.call(this,{
			region:'center',
			border:false,
			store:this.store,
			columns:columns,
			bbar : new Ext.PagingToolbar({
				store : this.store
			}),
			tbar : [' ', '查询条件:', 
				new Ext.ux.form.SearchField({
						store : this.store,
						width : 220,
						hasSearch : true,
						emptyText : '支持授权内容'
			})]
		});
	}
});

/**
 * 历史记录
 * @class CmdHisGrid
 * @extends Ext.grid.GridPanel
 */
CmdHisGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor: function(){
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Job!querySendAllCmdHis.action',
			root:'records',
			totalProperty:'totalProperty',
			autoDestroy: true,
			fields:['done_code', 'server_name', 'done_date', 'message','optr_id',
							'optr_name', 'county_id','num','next_time','ca_type']
		});
		var columns = [
			{header:'流水号',dataIndex:'done_code',width:70},
			{header:'发送类型',dataIndex:'ca_type',width:70},
			{header:'未发送数',dataIndex:'num',width:60},
			{header:'CA服务器',dataIndex:'server_name',width:80},
			{header:'创建时间',dataIndex:'done_date',width:110},
			{header:'下次发送时间',dataIndex:'next_time',width:110},
			{header:'内容',dataIndex:'message',width:200,renderer:App.qtipValue},
			{header:'操作人',dataIndex:'optr_name',width:75}
		];
		CmdHisGrid.superclass.constructor.call(this,{
			region:'south',
			height:300,
			border:false,
			store:this.store,
			columns:columns,
			bbar : new Ext.PagingToolbar({
				store : this.store
			}),
			tbar : [' ', '查询条件:', 
				new Ext.ux.form.SearchField({
						store : this.store,
						width : 220,
						hasSearch : true,
						emptyText : '支持授权内容'
			})]
		});
	}
});

SendCmdTab = Ext.extend(Ext.TabPanel,{
	constructor:function(){
		SendCmdTab.superclass.constructor.call(this,{
			region:'center',
			activeTab: 0,
			border: false,
			defaults : {
				layout : 'fit'
			},
			items:[{
					title:'未发送授权',
					items:[new SendAllGrid()]
				},{
					title:'已发送授权',
					items:[new CmdPropGrid()]
				},{
					title:'配置记录',
					items:[new CmdHisGrid()]
				}
			],
			listeners:{
				activate:function(p){
					p.items.itemAt(0).getStore().load({
                            params: {
                                start: 0,
                                limit: Constant.DEFAULT_PAGE_SIZE
                            }
                        });
				}
			}
		});
	},
	initEvents: function(){
	  	for (var i = 0; i < this.items.length; i++) {
			var p = this.items.itemAt(i);
			p.on("activate", this.refreshPanel, this );
		}
	    SendCmdTab.superclass.initEvents.call(this);
	},
	refreshPanel : function(p){
		p.items.itemAt(0).getStore().load({
            params: {
                start: 0,
                limit: Constant.DEFAULT_PAGE_SIZE
            }
        });
	}
});

SendAllCmd = Ext.extend(Ext.Panel, {
    constructor: function () {
        var fp = new CmdFormPanel();
        var tab = new SendCmdTab();
        SendAllCmd.superclass.constructor.call(this, {
            id: 'SendAllCmd',
            title: '通授权',
            closable: true,
            border: false,
            layout : 'border',
            baseCls: "x-plain",
            items: [
            	{region:'north',height:240,layout:'fit',split: true,items:[fp]}, 
        		{region: 'center',split: true,layout: 'fit',items: [tab]}
        	]
        });
    }
});