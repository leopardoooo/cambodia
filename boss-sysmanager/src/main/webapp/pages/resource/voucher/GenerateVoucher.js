var VoucherForm = Ext.extend(Ext.form.FormPanel,{
	initComponent:function(){
		VoucherForm.superclass.initComponent.call(this);
		App.form.initComboData([this.getForm().findField('voucherDto.voucher_type')]);
	},
	constructor: function(){
		VoucherForm.superclass.constructor.call(this,{
			border:false,
			region:'north',
			height:80,
			layout:'column',
			labelWidth:95,
			bodyStyle:'padding-top:10px',
			defaults:{
				layout:'form',
				border:false
			},
			items:[
				{columnWidth:.25,defaults:{maxLength:11,minLength:11,enableKeyEvents:true,
							allowDecimals:false,allowNegative:false,allowBlank:false},
					items:[
			        	{fieldLabel:'代金券起始号',xtype:'numberfield',name:'voucherDto.start_voucher_id'}
					]
				},
				{columnWidth:.25,defaults:{maxLength:11,minLength:11,enableKeyEvents:true,
							allowDecimals:false,allowNegative:false,allowBlank:false},items:[
						{fieldLabel:'代金券结束号',xtype:'numberfield',name:'voucherDto.end_voucher_id'}
					]
				},
				{columnWidth:.25,items:[
					{fieldLabel:'面额',xtype:'numberfield',allowBlank:false,name:'voucherDto.voucher_value',
						allowNegative:false,allowBlank:false
					}
				]},
				{columnWidth:.25,items:[
					{fieldLabel:'地区',xtype:'combo',allowBlank:false,hiddenName:'voucherDto.for_county_id',
						store:new Ext.data.JsonStore({
							url:root+'/resource/Voucher!queryFgsByDeptId.action',
							fields:['dept_id','dept_name']
						}),displayField:'dept_name',valueField:'dept_id'
					}
				]},
				{columnWidth:.25,items:[
					{fieldLabel:'失效时期',xtype:'datefield',name:'voucherDto.invalid_time',
						allowBlank:false,format:'Y-m-d',editable:false,minValue:nowDate().format('Y-m-d')
					}
				]},
				{columnWidth:.25,items:[
					{fieldLabel:'类型',hiddenName:'voucherDto.voucher_type',allowBlank:false,
								xtype:'paramcombo',paramName:'VOUCHER_TYPE',forceSelection:true,editable:false
							}
				]},
				{columnWidth:.15,items:[
					{xtype:'button',text:'生  成',iconCls:'icon-add',
						listeners:{
							scope:this,
							click:this.doGenerateVoucher
						}
					}
				]},
				{columnWidth:.15,items:[
					{xtype:'button',text:'取  消',iconCls:'icon-reset',
						listeners:{
							scope:this,
							click:this.doCancel
						}
					}
				]}
			]
		});
		this.getForm().findField('voucherDto.for_county_id').getStore().load();
	},
	doGenerateVoucher: function(){
		var form = this.getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		var startVoucherId = values['voucherDto.start_voucher_id'],
			endVoucherId = values['voucherDto.end_voucher_id'];
		if(startVoucherId > endVoucherId){
			Alert('结束值应该大于起始值！');
			return;
		}
		values['voucherDto.voucher_value'] = values['voucherDto.voucher_value']*100;
		Ext.Ajax.request({
			url:root+'/resource/Voucher!saveVoucher.action',
			params:values,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.simpleObj === true){
					Alert('代金券生成成功,请到选择的地区去查询!');
					Ext.getCmp('voucherGridId').getStore().load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
					this.doCancel();
				}else{
					Alert('输入代金券段已存在,请确认!');
				}
			}
		});
	},
	doCancel: function(){
		this.getForm().reset();
	}
});

VoucherGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(){
		this.voucherStore = new Ext.data.JsonStore({
			url:root+'/resource/Voucher!queryVoucher.action',
			fields:['voucher_id','voucher_value','status','status_time','for_county_id','optr_id',
					'used_time','used_money','unused_money','exp_time','status_text','voucher_type','voucher_type_name',
					'optr_name','for_county_id_text','create_time','invalid_time','is_procured','is_procured_text'],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.voucherStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var cm = new Ext.grid.ColumnModel({
			columns: [
				{header:'代金券号',dataIndex:'voucher_id',width:100,renderer:App.qtipValue},
				{header:'类型',dataIndex:'voucher_type_name',width:50,renderer:App.qtipValue},
				{header:'面额',dataIndex:'voucher_value',width:50,renderer:Ext.util.Format.formatFee},
				{header:'状态',dataIndex:'status_text',width:55,renderer:Ext.util.Format.statusShow},
				{header:'状态改变时间',dataIndex:'status_time',width:115},
				{header:'是否领用',dataIndex:'is_procured_text',width:65},
				{header:'适用地区',dataIndex:'for_county_id_text',width:85,renderer:App.qtipValue},
				{header:'已使用金额',dataIndex:'used_money',width:75,renderer:Ext.util.Format.formatFee},
				{header:'操作员',dataIndex:'optr_name',width:75,render:App.qtipValue},
				{header:'未使用金额',dataIndex:'unused_money',width:75,renderer:Ext.util.Format.formatFee},
				{header:'使用时间',dataIndex:'used_time',width:115},
				{header:'创建时间',dataIndex:'create_time',width:115},
				{header:'失效时间',dataIndex:'invalid_time',width:75,renderer:Ext.util.Format.dateFormat},
				{header:'操作',dataIndex:'voucher_id',renderer:function(v,meta,r){
					var result = '';
						if(r.get('status') == 'IDLE'){
							result = '<a href="#" onclick=Ext.getCmp("voucherGridId").cancleVoucher('+v+')>' +
									'失效</a>&nbsp;&nbsp;';
							if(r.get('is_procured') == 'T')
							result += '<a href="#" onclick=Ext.getCmp("voucherGridId").cancleProcure('+v+')>' +
									'退库</a>';
						}
						return result;
					}
				}
			]
		});
		VoucherGrid.superclass.constructor.call(this,{
			id:'voucherGridId',
			title:'代金券信息',
			border:false,
			region:'center',
			store:this.voucherStore,
			cm:cm,
			sm: new Ext.grid.RowSelectionModel(),
			bbar:new Ext.PagingToolbar({pageSize:Constant.DEFAULT_PAGE_SIZE,store:this.voucherStore}),
            tbar: [' ', ' ', '输入关键字', ' ', new Ext.ux.form.SearchField({
                store: this.voucherStore,
                width: 200,
                hasSearch: true,
                emptyText: '支持代金券号模糊查询'
            })]
		});
	},
	cancleVoucher: function(voucherId){
		Confirm('确定失效吗?', this, function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/resource/Voucher!updateVoucherStatus.action',
				params:{voucherId: voucherId},
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data.success === true){
						record.set('status','INVALID');
						record.set('status_text','失效');
						record.commit();
					}
				}
			});
		});
	},
	cancleProcure: function(voucherId){
		Confirm('确定退库吗?', this, function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/resource/Voucher!updateVoucherProcureStatus.action',
				params:{voucherId: voucherId},
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data.success === true){
						record.set('is_procured','F');
						record.set('is_procured_text','否');
						record.commit();
					}
				}
			});
		});
	}
});

GenerateVoucher = Ext.extend(Ext.Panel,{
	constructor:function(){
		GenerateVoucher.superclass.constructor.call(this,{
			id:'GenerateVoucher',
			layout:'border',
			title:'生成代金券',
			closable: true,
			border : false ,
			items:[new VoucherForm(), new VoucherGrid()]
		});
	}
});











