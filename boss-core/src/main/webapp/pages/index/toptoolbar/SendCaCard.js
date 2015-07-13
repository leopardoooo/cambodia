var QueryCaCardGrid = Ext.extend(Ext.grid.GridPanel, {
    caStore: null,
    cardId: null,
    constructor: function () {
        this.caStore = new Ext.data.JsonStore({
        	url:Constant.ROOT_PATH + "/core/x/Prod!queryCaCommand.action",
            root: 'records',
            totalProperty: 'totalProperty',
            fields: ['done_code', 'stb_id', 'card_id', 'auth_end_date', 'prg_name', 'result_flag', 'control_id',
            'error_info', 'record_date', 'send_date', 'cmd_type_text', 'optr_name']
        });
        var cm = [{
            header: '业务流水号',
            dataIndex: 'done_code',
            width: 80,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '智能卡号',
            dataIndex: 'card_id',
            width: 130,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '控制字',
            dataIndex: 'control_id',
            width: 60,
            sortable: true
        }, {
            header: '资源名称',
            dataIndex: 'prg_name',
            width: 100,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '预计到期日期',
            dataIndex: 'auth_end_date',
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '指令类型',
            dataIndex: 'cmd_type_text',
            width: 60,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '结果标记',
            dataIndex: 'result_flag',
            width: 60,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '生成时间',
            dataIndex: 'record_date',
            width: 110,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '发送时间',
            dataIndex: 'send_date',
            width: 110,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '错误信息',
            dataIndex: 'error_info',
            width: 100,
            sortable: true,
            renderer: App.qtipValue
        }, {
            header: '操作员',
            dataIndex: 'optr_name',
            width: 80,
            sortable: true,
            renderer: App.qtipValue
        }]
        QueryCaCardGrid.superclass.constructor.call(this, {
            id: 'caGridId',
            autoScroll: true,
            ds: this.caStore,
            columns: cm,
            tbar: ['【CA信息】', '-', '指令类型:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                width: 80,
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            this.caStore.filterBy(

                            function (record) {
                                if (Ext.isEmpty(value)) return true;
                                else return record.get('cmd_type_text').indexOf(value) >= 0;
                            }, this);
                        }
                    }
                }
            }, '-', '控制字:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                width: 60,
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            this.caStore.filterBy(

                            function (record) {
                                if (Ext.isEmpty(value)) return true;
                                else return  record.get('control_id')==null?false:record.get('control_id').indexOf(value) >= 0;
                            }, this);
                        }
                    }
                }
            }, '-', '生成时间:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                width: 100,
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            this.caStore.filterBy(

                            function (record) {
                                if (Ext.isEmpty(value)) return true;
                                else return record.get('record_date').indexOf(value) >= 0;
                            }, this);
                        }
                    }
                }
            }],
            bbar: new Ext.PagingToolbar({
                store: this.caStore,
                pageSize: 10
            })
        });
    },
    refresh: function () {
        this.caStore.load({
            params: {
                start: 0,
                limit: 10
            }
        });
    },
    doLoad: function () {
        this.caStore.baseParams.cardId = this.cardId;
        this.refresh();
    }
});



var SendCaCardWin = Ext.extend(Ext.Window,{
	caGrid: null,
	constructor:function(){
		this.caGrid = new QueryCaCardGrid(this);
		SendCaCardWin.superclass.constructor.call(this,{
			id:'sendCaCardWinId',
			border:false,
			title:'一体机授权',
			closeAction:'close',
			width:650,
			height:420,
			layout: 'border',
			bodyStyle: "background:#F9F9F9",
			items:[{
					region:'north',
					height:85,
					border : false,
					anchor:'100%',
					layout : 'column',
	            	bodyStyle : "background:#F9F9F9;",
	            	border : false,
	            	autoScroll:false,
	            	defaults : {
						baseCls: 'x-plain',
						bodyStyle: "padding: 5px;background:#F9F9F9;",
						columnWidth:0.3,
						labelWidth : 80,
						layout: 'form'
					},
					items : [{
							columnWidth:1,
							layout : {
								type:'hbox',
								padding : '5',
								pack:'start',
								align : 'top'
							},
							defaults : {
								margins : '0 5 0 0',
								height:20
							},items:[
							{
								xtype : 'spacer',
								width : 15
							},{xtype:'displayfield',value:'智能卡'},{xtype:'textfield',vtype:'alphanum',allowBlank:false,
								name:'card_id',width:180
							},{
								xtype:'button',
								text:'预授权',
								iconCls:'icon-busi',
								listeners:{
									scope:this,
									click:this.doSave
								}
							}, {
								xtype : 'button',
								iconCls:'icon-query',
								text : '查询指令',
								listeners : {
									scope : this,
									click:this.doQuery
								}
							}]
				},{
					columnWidth:1,
					xtype: 'displayfield',
					value: "<font style='font-family:微软雅黑;font-size:14px;color:red'><b>" + "&nbsp;&nbsp;&nbsp;&nbsp;设备必须是已领用的一体机购买的卡，授权期限为7天，在此期间可以重复授权，但授权期限为第一次授权期限</b></font>"
				}]
				},{
					region: 'center',
	                layout: 'fit',
	                border: false,
					items:[this.caGrid]	
				}
			]
			
		});
	},
	show:function(){
		SendCaCardWin.superclass.show.call(this);
		this.find('name','card_id')[0].focus(100,true);
	},
	doSave:function(){
		var voucherIdComp = this.find('name','card_id')[0];
		var value = voucherIdComp.getValue();
		if(Ext.isEmpty(value))return;
		var obj = {}, records = [];
		obj['cardId'] = value;
		var mb = Show();
		Ext.Ajax.request({
			scope : this,
			url:Constant.ROOT_PATH + "/core/x/Prod!saveBusiCmdCard.action",
			params : obj,
			timeout:36000000,
			success : function(res, opt) {
				mb.hide();// 隐藏提示框
				mb = null;
				var rs = Ext.decode(res.responseText);
				if (true === rs.success) {
					Alert('保存授权成功!',function(){
						this.caGrid.cardId = value;
                        this.caGrid.doLoad();
					},this);
				} else {
					Alert('操作失败');
				}
			}
		});
		
		
	},
	doQuery:function(){
		var voucherIdComp = this.find('name','card_id')[0];
		var value = voucherIdComp.getValue();
		if(Ext.isEmpty(value))return;
		this.caGrid.cardId = value;
	    this.caGrid.doLoad();
	}
});