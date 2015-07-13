
StbFilledGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor: function(){
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Job!queryCurrentDateLog.action',
			fields:['func_code','func_code_text','rec_id','rec_name','optr_id','optr_name','done_date']
		});
		var columns = [
			{header:'指令类型',dataIndex:'func_code_text'},
			{header:'操作员',dataIndex:'optr_name',width:100},
			{header:'灌装时间',dataIndex:'done_date',width:120}
		];
		StbFilledGrid.superclass.constructor.call(this,{
			border:false,
			title:'灌装记录(双击行显示CA指令)',
			store:this.store,
			sm:new Ext.grid.RowSelectionModel(),
			columns:columns,
			listeners:{
				scope:this,
				rowdblclick: this.doClick
			}
		});
	},
	doClick: function(){
		var record = this.getSelectionModel().getSelected();
		var win = Ext.getCmp('stbFillCaWindowId');
		if(!win){
			win = new StbFillCaWindow();
		}
		win.show(record.get('rec_id'),record.get('rec_name'));
	}
});

//灌装CA指令
var StbFillCaWindow = Ext.extend(Ext.Window,{
	constructor: function(){
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Job!queryCurrDateCommand.action',
			fields:['transnum','job_id','cmd_type','prg_name','card_id','stb_id','control_id',
				'auth_begin_date','auth_end_date','is_sent','record_date','send_date','error_info',
				'is_sent','result_flag',
				'cmd_type_text']
		});
		var authDateFormat = function(value){
			if(Ext.isEmpty(value))return '';
			var date = Date.parseDate(value,'YmdHis',true);
			return date.format('Y-m-d H:i:s');
		}
		var columns = [
			{header:'控制字',dataIndex:'control_id',width:50},
			{header:'节目名称',dataIndex:'prg_name',width:75,renderer:App.qtipValue},
			{header:'授权类型',dataIndex:'cmd_type_text',width:60},
			{header:'授权开始日期',dataIndex:'auth_begin_date',width:115,renderer:authDateFormat},
			{header:'授权截止日期',dataIndex:'auth_end_date',width:115,renderer:authDateFormat},
			{header:'生成时间',dataIndex:'record_date',width:115},
			{header:'发送时间',dataIndex:'send_date',width:115},
			{header:'是否发送',dataIndex:'is_sent',width:60,renderer:function(value){
					if(value == 'N'){
						value = "未处理";
					}else if(value == 'Y'){
						value = "已处理";
					}else if(value == '1'){
						value = "待处理";
					}else if(value == '2'){
						value = "重复指令";
					}
					return value;
				}
			},
			{header:'结果标记',dataIndex:'result_flag',width:60},
			{id:'error_info_id',header:'错误信息',dataIndex:'error_info',renderer:App.qtipValue}
		];
		StbFillCaWindow.superclass.constructor.call(this,{
			id:'stbFillCaWindowId',
			title:'灌装CA指令',
			border:false,
			width:800,
			height:500,
			layout:'fit',
			items:[{
				xtype:'grid',
				store:this.store,
				columns:columns,
				autoExpandColumn:'error_info_id'
			}]
		});
	},
	show:function(jobId, cardId){
		StbFillCaWindow.superclass.show.call(this);
		this.store.load({
			params:{
				jobId: jobId,
				cardId: cardId
			}
		});
	}
});

StbFilled = Ext.extend(Ext.FormPanel,{
	constructor:function(){
		this.stbFilledGrid = new StbFilledGrid();
		StbFilled.superclass.constructor.call(this,{
			id:'StbFilled',
			title:'灌装',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[{
				region:'north',layout:'column',height:80,bodyStyle:'padding-top:15px',border:false,autoScroll:true,
				defaults: {
                    labelWidth: 100,
                    layout: 'form',
                    border: false,
                    baseCls: "x-plain"
                },items:[{columnWidth:.3,border:false,items:[{
	                        xtype: 'textfield',
	                        fieldLabel: '机顶盒号',
	                        name: 'stb_id',
	                        vtype: 'alphanum',
	                        width: 160,
	                        id: 'stbFilledId',
	                        listeners: {
	                            scope: this,
	                            specialkey: function (field, e) {
	                                if (e.getKey() === Ext.EventObject.ENTER) {
	                                    this.doCheck('stbFilledId');
	                                }
	                            },
	                            change: function () {
	                                this.doCheck('stbFilledId');
	                            }
	                        }
	                    }]},
						{
						columnWidth:.7,border:false,items:[
								{xtype:'textfield',fieldLabel:'智能卡号',name:'card_id', width: 150,id:'pairCardFilledId',vtype:'alphanum',allowBlank:false
									,listeners:{
										scope:this,
										specialkey: function (field, e) {
			                                if (e.getKey() === Ext.EventObject.ENTER) {
			                                    this.doCheck('pairCardFilledId');
			                                }
			                            },
			                            change: function () {
			                                this.doCheck('pairCardFilledId');
			                            }
									}
								}
						]},{columnWidth:.4,border:false,items:[
							{xtype:'displayfield'}
						]},
						{columnWidth:.1,border:false,items:[
							{xtype:'button',text:'灌装',iconCls:'icon-save',scope:this,handler:this.doSave}
						]},
						{columnWidth:.1,border:false,items:[
							{xtype:'button',text:'查询',iconCls:'icon-query',scope:this,handler:function(){this.doQuery(this.find('name','card_id')[0]);}}
						]}
				]
			},{
				region:'center',border:false,layout:'fit',items:[this.stbFilledGrid]
			}
			]
		});
	},
    doCheck: function (key) {
        var cmp = Ext.getCmp(key);
        Ext.Ajax.request({
            url: root + '/resource/Device!queryDeviceByStbId.action',
            params: {
                deviceCode: cmp.getValue()
            },
            scope:this,
            success: function (res, ops) {
                var rs = Ext.decode(res.responseText);
                if (rs != null) {                    
 					if(key == 'stbFilledId' && rs.device_type == 'CARD'){ 				
 						Ext.getCmp('pairCardFilledId').setValue(rs.device_code);
 						Ext.getCmp('stbFilledId').setValue('');
 					}
 					if(rs.pair_device_code){
 						Ext.getCmp('pairCardFilledId').setValue(rs.pair_device_code);
 					}
                } else {
                    Alert('无设备信息!');
                }
            },
            clearData: function () {
                //清空组件
            	cmp.reset();
				cmp.focus();
            }
        });
    },
	doQuery:function(card){
		var value = card.getValue();
		if(Ext.isEmpty(value))return;
		this.stbFilledGrid.getStore().load({
			params:{cardId: value}
		});
	},
	doSave:function(){
		var card = Ext.getCmp('pairCardFilledId');
		var stb = Ext.getCmp('stbFilledId');
		if(Ext.isEmpty(card.getValue()))return;
		
		Ext.Ajax.request({
			url:root+'/resource/Job!createCmdStbFilled.action',
			params:{cardId:card.getValue(),stbId:stb.getValue()},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					Alert('保存成功!',function(){
						card.reset();
						stb.reset();
						stb.focus();
					},this);
				}
			}
		});
	}
});