/**
 * 单卡指令
 */
var CaGrid = Ext.extend(Ext.grid.GridPanel, {
    caStore: null,
    cardIds: null,
    constructor: function () {
        this.caStore = new Ext.data.JsonStore({
            url: root + '/resource/Job!queryCaCommand.action',
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
        CaGrid.superclass.constructor.call(this, {
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
                                else return record.get('control_id')==null?false:record.get('control_id').indexOf(value) >= 0;
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
                pageSize: Constant.DEFAULT_PAGE_SIZE
            })
        });
    },
    refresh: function () {
        this.caStore.load({
            params: {
                start: 0,
                limit: Constant.DEFAULT_PAGE_SIZE
            }
        });
    },
    doLoad: function () {
        this.caStore.baseParams.cardIds = this.cardIds;
        this.refresh();
    }
});

CardSendForm = Ext.extend(Ext.FormPanel, {
    caGrid: null,
    lenHis: 0,
    county_id: null,
    area_id: null,
    resStore: null,
    supplier_id: null,
    constructor: function () {
        this.caGrid = new CaGrid(this);
        this.resStore = new Ext.data.JsonStore({
            url: root + "/system/Prod!queryResByCountyId.action",
            fields: ['res_id', 'res_name', 'serverIds', 'currency']
        });
        CardSendForm.superclass.constructor.call(this, {
            id: 'CardSendFormId',
            region: 'center',
            layout: 'border',
            border: false,
            fileUpload: true,
            items: [{
                region: 'north',
                border: false,
                height: 300,
                bodyStyle: 'padding-top:10px',
                layout:'hbox',
			defaults:{layout:'form',border:false,flex:1},
			items : [{
					xtype:'panel',flex:1,id:'stbCardFormId',layout : 'column',defaults : {
							width:'200',
							baseCls : 'x-plain',
							columnWidth : 1,
							layout : 'form',
							defaultType : 'textfield',
							labelWidth : 90
					},
					baseCls : 'x-plain',bodyStyle : 'padding-top: 5px',
					items:[{
		                    columnWidth: 1,
		                    items: [{
	                    	xtype:'combo',fieldLabel:'发送类型',hiddenName:'sendType',
	                    	store:new Ext.data.JsonStore({
	                    		fields:['text','value'],
	                    		data:[{'text':'单卡','value':'CARD'},{'text':'文件','value':'FILE'}]
	                    	}),width: 100,value:'CARD',
	                    	displayField:'text',valueField:'value',triggerAction:'all',
	                    	listeners:{
	                    		scope:this,
	                    		select:function(combo){
	                    			Ext.getCmp('cmdTypeId').getStore().removeAll();
	                    			var value = combo.getValue();
	                    			if(value == 'CARD'){
	                    				Ext.getCmp('cardFormCfgId').show();
	                    				Ext.getCmp('pairStbId').show();
	                    				Ext.getCmp('countyFormCfgId').hide();
	                    	        	Ext.getCmp('fileFormId').hide();
	                    				this.clearComData();
	                    			}else if(value == 'FILE'){
	                    				Ext.getCmp('cardFormCfgId').hide();
	                    				Ext.getCmp('pairStbId').hide();
	                    				Ext.getCmp('countyFormCfgId').show();
	                    				Ext.getCmp('fileFormId').show();
	                    				this.clearComData();
	                    				this.getForm().findField('countyId').getStore().load();
	                    			}
	                    			Ext.getCmp('queryCaBtnId').setDisabled(true);
	                    		}
	                    	}
	                    }]},{
		                    columnWidth: 1,
		                    id:'cardFormCfgId',
		                    items: [{
	                        xtype: 'textfield',
	                        fieldLabel: '智能卡号',
	                        allowBlank: false,
	                        name: 'device_code',
	                        vtype: 'alphanum',
	                        width: 160,
	                        id: 'deviceCodeId',
	                        listeners: {
	                            scope: this,
	                            specialkey: function (field, e) {
	                                if (e.getKey() === Ext.EventObject.ENTER) {
	                                    this.doCheck();
	                                }
	                            },
	                            change: function () {
	                                this.doCheck();
	                            }
	                        }
	                    }]},{
		                    columnWidth: 1,
		                    id:'pairStbId',
		                    items: [{
	                        xtype: 'textfield',
	                        fieldLabel: '机顶盒号',
	                        name: 'pair_device_code',
	                        vtype: 'alphanum',
	                        width: 160,
	                        id: 'pairDeviceCodeId'
	                    }]},{
		                    columnWidth: 1,
		                    id:'fileFormId',
		                    items: [{
		                    	id:'caFileId',xtype:'textfield',fieldLabel:'文件',inputType:'file',
		                    	allowBlank:false,anchor:'95%',name:'files'}]
	                    },{
		                    columnWidth: 1,
		                    id:'countyFormCfgId',
		                    items: [{
	                    	xtype:'combo',fieldLabel:'县市',allowBlank:false,
	                    	store:new Ext.data.JsonStore({
	                    		url:root+'/config/Rule!queryRuleCounty.action',
	                    		fields:['county_id','county_name','area_id']
	                    	}),displayField:'county_name',valueField:'county_id',
	                    	hiddenName:'countyId',
	                    	listeners:{
	                    		scope:this,
	                    		select:function(combo,record){
	                    			this.county_id = combo.getValue();
	                    			this.area_id = record.get('area_id');
	                    			Ext.getCmp('cmdTypeId').getStore().load({
	                    				params:{
	                    					type:'',
	                    					county_id: this.county_id
	                    				}
	                    			});
	                    		}
	                    	}
	                    }]},{
		                    columnWidth: 1,
		                    items: [{
		                    fieldLabel: '指令类型',
		                    xtype: 'combo',
		                    hiddenName: 'cmd_id',
		                    width: 100,
		                    id: 'cmdTypeId',
		                    allowBlank: false,
		                    store: new Ext.data.JsonStore({
		                        url: root + '/resource/Device!querySendTypeByType.action',
		                        fields: ['supplier_cmd_name', 'cmd_id','supplier_id'],
		                        listeners:{
		                        	scope:this,
		                        	load:function(store){
		                        		store.each(function(record){
		                        			record.set('supplier_cmd_name',record.get('supplier_cmd_name')+'('+record.get('supplier_id')+')');
		                        			record.set('cmd_id',record.get('cmd_id')+','+record.get('supplier_id'));
		                        		});
		                        		store.sort('supplier_cmd_name','ASC');
		                        	}
		                        }
		                    }),
		                    displayField: 'supplier_cmd_name',
		                    valueField: 'cmd_id',
		                    listeners: {
		                        scope: this,
		                        'select': function (combo, record, index) {
		                            Ext.getCmp('sendCaId').setDisabled(false);
		                            this.doQueryType(combo.getValue());
		                            this.supplier_id = record.get('supplier_id');
		                        }
		                    }
		                }]}	
		                ,{
		                    id: 'CardEndDateId',
		                    items: [{
		                        xtype: 'datefield',
		                        width: 100,
		                        fieldLabel: '预计到期日期',
		                        format: 'Y-m-d',
		                        allowBlank: false,
		                        editable: false,
		                        name: 'end_date'
		                    }]
		                }					
					]
				},{
					xtype:'panel',flex:1,layout : 'column',defaults : {
							width:'200',
							baseCls : 'x-plain',
							columnWidth : 1,
							layout : 'form',
							defaultType : 'textfield',
							labelWidth : 90
					},
					baseCls : 'x-plain',bodyStyle : 'padding-top: 5px',
					items:[
						{
	                    id: 'ResAllId',
	                    items:[{
	                        xtype: 'itemselector',
	                        name: 'resSelect',
	                        id: 'selectResId',
	                        imagePath: '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage',
	                        multiselects: [{
	                            legend: '待选资源',
	                            width: 160,
	                            height: 220,
	                            store: new Ext.data.ArrayStore({
	                                fields: ['res_id', 'res_name']
	                            }),
	                            displayField: 'res_name',
	                            valueField: 'res_id'
	                            ,tbar: ['过滤:',
	                            {
	                                xtype: 'textfield',
	                                enableKeyEvents: true,
	                                listeners: {
	                                    scope: this,
	                                    keyup: function (txt, e) {
	                                        if (e.getKey() == Ext.EventObject.ENTER) {
	                                            var value = txt.getValue();
	                                            Ext.getCmp('selectResId').multiselects[0].store.filterBy(function (record) {
	                                                if (Ext.isEmpty(value)) return true;
	                                                else return record.get('res_name').indexOf(value) >= 0;
	                                            }, this);
	                                        }
	                                    }
	                                }
	                            }]
	                        	}, {
		                            legend: '已选资源',
		                            width: 100,
		                            height: 220,
		                            store: new Ext.data.ArrayStore({
		                                fields: ['res_id', 'res_name']
		                            }),
		                            displayField: 'res_name',
		                            valueField: 'res_id'
		                        }]
	                    	}]
						},{
		                    id: 'mailTitleOsdId',
		                    items: [{
		                        fieldLabel: '邮件标题',
		                        name: 'mail_title',
		                        allowBlank: false,
		                        xtype: 'textfield',
		                        width: 160
		                    }]
		                }, {
		                    id: 'messageOsdId',
		                    items: [{
		                        fieldLabel: '信息',
		                        xtype: 'textarea',
		                        id: 'messageOsd',
		                        width: 180,
		                        allowBlank: false,
		                        height: 100,
		                        name: 'message',
		                        listeners: {
		                            scope: this,
		                            change: function (txt) {
		                                this.changeMessage(txt.getValue());
		                            }
		                        }
		                    }, {
		                    	xtype: 'displayfield',
		                        id: 'messageNumberOsdId',
		                        layout: 'form',
		                        id: 'invoiceBookStatisOsdId',
		                        value: '还剩96个字符(48个中文)',
		                        labelWidth: 75,
		                        border: false
		                    }]
		                }]
           	  	}],
           	  	buttonAlign:'center',
           	  	buttons:[
           	  		{text:'发 送 指 令', id: 'sendCaId',scope:this,disabled: true,handler:this.save},
					{text:'查 询 指 令',id:'queryCaBtnId',scope:this,disabled: true,handler:this.doQuery}
				]
            }, {
                region: 'center',
                layout: 'fit',
                border: false,
                items: [this.caGrid]
            }]
        });

    },
    initEvents: function () {
        this.on('afterrender', function () {
            Ext.getCmp('deviceCodeId').focus(true, 500);
        	Ext.getCmp('CardEndDateId').hide();
	        Ext.getCmp('ResAllId').hide();
	        Ext.getCmp('messageOsdId').hide();
	        Ext.getCmp('mailTitleOsdId').hide();
        	Ext.getCmp('fileFormId').hide();
	       	Ext.getCmp('countyFormCfgId').hide();
	        
        });
	    this.doLayout();
        CardSendForm.superclass.initEvents.call(this);
    },
    clearComData:function(){
    	this.getForm().findField('device_code').setValue('');
	    this.getForm().findField('pair_device_code').setValue('');
    	this.getForm().findField('countyId').setValue('');
    	Ext.getCmp('cmdTypeId').setValue('');
    },
    doCheck: function () {
        if (Ext.isEmpty(Ext.getCmp('deviceCodeId').getValue())) {
            Alert("请先输入智能卡号!");
            return;
        }
        Ext.getCmp('queryCaBtnId').enable();
        var store = Ext.getCmp('cmdTypeId').getStore();
        this.removeRes();
        Ext.Ajax.request({
            url: root + '/resource/Device!queryDeviceById.action',
            params: {
                deviceCode: Ext.getCmp('deviceCodeId').getValue()
            },
            scope:this,
            success: function (res, ops) {
                var rs = Ext.decode(res.responseText);
                if (rs != null) {
                    this.county_id = rs.county_id;
                    this.area_id = rs.area_id;
                    store.load({
                        params: {
                            deviceType: rs.ca_type
                        }
                    });
                    if (rs.ca_type != 'TF') {
                        if (!Ext.isEmpty(rs.pair_device_code)) {
                            Ext.getCmp('pairDeviceCodeId').setValue(rs.pair_device_code);
	                        Ext.getCmp('pairDeviceCodeId').setDisabled(true);
                        }else{
                        	Ext.getCmp('pairDeviceCodeId').setDisabled(false);
                        	Ext.getCmp('pairDeviceCodeId').setValue('11111111111111111');
                        }
                        
                    } else {
                        if (!Ext.isEmpty(rs.pair_device_code)) {
                            Ext.getCmp('pairDeviceCodeId').setValue(rs.pair_device_code);
                            Ext.getCmp('pairDeviceCodeId').setDisabled(true);
                        } else {
                            Ext.getCmp('pairDeviceCodeId').setValue('11111111111111111');
                        	Ext.getCmp('pairDeviceCodeId').setDisabled(false);
                        }
                    }
                } else {
                    Alert('无该卡信息!');
                }
            },
            clearData: function () {
                //清空组件
                store.removeAll();
                Ext.getCmp('deviceCodeId').setValue('');
                Ext.getCmp('deviceCodeId').setDisabled(false);
                Ext.getCmp('pairDeviceCodeId').setValue('11111111111111111');
                Ext.getCmp('pairDeviceCodeId').setDisabled(false);
                Ext.getCmp('sendCaId').setDisabled(true);
            }
        });
    },
    removeRes: function () {
        Ext.getCmp('cmdTypeId').setValue('');
        if (Ext.getCmp('selectResId')) {
            Ext.getCmp('selectResId').multiselects[0].store.removeAll();
            Ext.getCmp('selectResId').multiselects[1].store.removeAll();
        }
    },
    doLoadResData: function () {
        var arr = [];
        this.resStore.each(function (record) {
            arr.push([record.data.res_id, record.data.serverIds + record.data.res_name]);
        });
        Ext.getCmp('selectResId').multiselects[0].store.loadData(arr);
        Ext.getCmp('selectResId').multiselects[1].store.removeAll();
    },
    doQuery: function () {
    	var sendType = this.getForm().findField('sendType').getValue();
        if (sendType == 'CARD' && Ext.isEmpty(Ext.getCmp('deviceCodeId').getValue())) {
            Alert("请先输入智能卡号!");
            return;
        }
        if(sendType == 'CARD') this.caGrid.cardIds = [Ext.getCmp('deviceCodeId').getValue()];
        this.caGrid.doLoad(this.cardIds);
    },
    doQueryType: function (v) {
    	var catypeArr = v.split(',');
		var catype = catypeArr[0];
        if (catype == 'OSD') {
            Ext.getCmp('CardEndDateId').hide();
            Ext.getCmp('ResAllId').hide();
            Ext.getCmp('messageOsdId').show();
            Ext.getCmp('mailTitleOsdId').hide();
        } else if (catype == 'MAIL') {
            Ext.getCmp('CardEndDateId').hide();
            Ext.getCmp('ResAllId').hide();
            Ext.getCmp('mailTitleOsdId').show();
            Ext.getCmp('messageOsdId').show();
        } else if (catype == 'AVT_PROD'||catype == 'PST_PROD') {
        	if(catype == 'AVT_PROD'){
	            Ext.getCmp('CardEndDateId').show();
        	}else{
        		Ext.getCmp('CardEndDateId').hide();
        	}
            Ext.getCmp('ResAllId').show();
            Ext.getCmp('selectResId').show();
            Ext.getCmp('messageOsdId').hide();
            Ext.getCmp('mailTitleOsdId').hide();
            if (Ext.getCmp('selectResId')) {
                this.resStore.load({
                    params: {
                        countyId: this.county_id
                    }
                });
                this.resStore.on("load", this.doLoadResData, this);
            }
        } else {
            Ext.getCmp('CardEndDateId').hide();
            Ext.getCmp('ResAllId').hide();
            Ext.getCmp('messageOsdId').hide();
            Ext.getCmp('mailTitleOsdId').hide();
        }
//		this.doLayout();
    },
    changeMessage: function (txt) {
        var temp = 96 - Ext.util.Format.getByteLen(txt);
        var temp1 = temp / 2;
        if (temp % 2 == 1) temp1 = temp1 - 0.5;
        if (temp < 0) {
            Ext.getCmp('messageOsd').setValue(Ext.util.Format.subStringLength(txt, this.lenHis));
            Alert("输入已满!");
            return;
        } else {
            Ext.getCmp('invoiceBookStatisOsdId').setValue("还剩" + temp + "个字符(" + temp1 + "个中文)");
            this.lenHis = Ext.util.Format.getByteLen(txt);
        }
    },
    save: function () {
        var valid = true;
        //由于有组件会隐藏，修改验证方法
        this.getForm().items.each(function (f) {
            if (!f.validate() && f.ownerCt.hidden === false) {
                valid = false;
            }
        });
        if (!valid) return;
        var caForm = this.getForm().getValues();
        newValues = {};
        newValues["busiCmd.card_id"] = caForm["device_code"];
        if (!Ext.isEmpty(Ext.getCmp('pairDeviceCodeId').getValue())) {
            newValues["busiCmd.stb_id"] = Ext.getCmp('pairDeviceCodeId').getValue();
        }

        var catypeArr = caForm["cmd_id"].split(',');
		var catype = catypeArr[0];
        newValues["busiCmd.busi_cmd_type"] = catype;
        newValues["busiCmd.county_id"] = this.county_id;
        newValues["busiCmd.area_id"] = this.area_id;

        if (catype == 'OSD') {
            newValues["busiCmd.detail_params"] = "TITLE:''-1'';MSG:''" + caForm["message"] + "'';style:''1'';duration:''60''";
        } else if (catype == 'MAIL') {
            newValues["busiCmd.detail_params"] = "TITLE:''" + caForm["mail_title"] + "'';MSG:''" + caForm["message"] + "''";
        } else if (catype == 'AVT_PROD'||catype == 'PST_PROD') {
            if (Ext.isEmpty(caForm["resSelect"])) {
                Alert('请选择资源!');
                return false;
            }
            if(catype == 'PST_PROD'){//减授权
            	newValues["busiCmd.busi_cmd_type"] = 'PST_RES';
            	caForm["end_date"] = '2015-12-31';	
            }else{//加授权
            	newValues["busiCmd.busi_cmd_type"] = 'AVT_RES';
            }
            newValues["busiCmd.detail_params"] = "RES_ID:''" + caForm["resSelect"] + "'';ENDDATE:''" + caForm["end_date"] + "''";
        }

        Confirm("确定发送指令吗?", this, function () {
        	var sendType = caForm['sendType'];
        	if(sendType == 'CARD'){
	            mb = Show(); // 显示正在提交
	            Ext.Ajax.request({
	                url: root + '/resource/Job!createBusiCmd.action',
	                params: newValues,
	                scope:this,
	                success: function (res, ops) {
	                    mb.hide(); // 隐藏提示框
	                    mb = null;
	                    var rs = Ext.decode(res.responseText);
	                    if (true === rs.success) {
	                        Alert('发送成功!');
	                        this.removeRes();
//	                        setTimeout(function () {
	                        	this.caGrid.cardIds = [Ext.getCmp('deviceCodeId').getValue()];
	                            this.caGrid.doLoad();
//	                        }, 5000);
	
	                    } else {
	                        Alert('发送失败!');
	                    }
	                }
	            });
        	}else if(sendType == 'FILE'){
        		var flag = checkFileType(Ext.getCmp('caFileId').getValue());
				if(!flag)return;
        		newValues['busiCmd.supplier_id'] = this.supplier_id;
        		mb = Show(); // 显示正在提交
        		Ext.Ajax.request({
        			form:this.form.el.dom,
        			url:root + '/resource/Job!createBusiCmdFile.action',
        			params:newValues,
        			scope:this,
        			isUpload:true,
        			success:function(res, ops){
        				mb.hide(); // 隐藏提示框
	                    mb = null;
        				var rs = Ext.decode(res.responseText);
        				if(rs.success === true){
        					if(rs.msg) Alert(rs.msg);
        					else Alert('发送成功!');
        					
        					Ext.getCmp('queryCaBtnId').enable();
	                        this.removeRes();
        				} else {
	                        Alert('发送失败!');
	                    }
        			}
        		});
        	}
        });
    }
});

CardSend = Ext.extend(Ext.Panel, {
    constructor: function () {
        var cardSendForm = new CardSendForm();
        CardSend.superclass.constructor.call(this, {
            id: 'CardSend',
            title: '单卡指令',
            closable: true,
            border: false,
            layout: 'border',
            baseCls: "x-plain",
            items: [cardSendForm]
        });
    }
});