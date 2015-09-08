ExchangeFormWin = Ext.extend(Ext.Window,{
	width:300,height:140,closeAction:'hide',
	title:langUtils.sys('ExchangeRateManage.commons.addNewOne'),
	parent:null,form:null,
	loadRecord:function(rec){
		this.recordModify = rec.data;
		var data = {eff_date:Date.parseDate(rec.data.eff_date,'Y-m-d H:i:s'),exchange:rec.data.exchange}
		this.form.getForm().setValues(rec.data);
	},
	resetForm:function(){
		this.recordModify = null;
		this.form.getForm().reset();
	},
	constructor:function(p){
		this.parent = p;
		this.form = new Ext.FormPanel({
			items:[
			{xtype:'datefield',name:'eff_date',minValue:Date.parseDate(new Date().format('Y-m-d'),'Y-m-d'),fieldLabel:langUtils.sys('ExchangeRateManage.commons.effective_time'),allowBlank:false,editable:false},
			{xtype:'numberfield',allowDecimals:false,allowNegative: false,name:'exchange',minValue:1,fieldLabel:langUtils.sys('ExchangeRateManage.commons.exchange_rate'),allowBlank:false}
			]
		});
		ExchangeFormWin.superclass.constructor.call(this,{
			items:this.form,
			buttonAlign:'center',
			buttons:[
			{text:langUtils.sys('ExchangeRateManage.commons.saveBtn'),scope:this,handler:this.doSave},
			{text:langUtils.sys('ExchangeRateManage.commons.cancelBtn'),scope:this,handler:this.doCancel}
			]
		});
	},
	doSave:function(){
		var values = this.form.getForm().getValues();
		this.recordModify = this.recordModify || {};
		var params = {
			oldExchange:this.recordModify.exchange, effDate:this.recordModify.eff_date,
			newExchange:values.exchange,newEffDate:Date.parseDate(values.eff_date,this.form.items.itemAt(0).format).format('Y-m-d')
		};
		Ext.Ajax.request({
				params: params,scope:this,
				url:root+'/config/Exchange!saveOrUpdate.action',
				success: function( res, ops){
//					var data = Ext.decode(res.responseText );
					Alert(langUtils.sys('ExchangeRateManage.msg.successMsg'),function(){
						this.parent.reloadGrid();
						this.doCancel();
					},this);
				}
			});
	},
	doCancel:function(){
		this.form.getForm().reset();
		this.hide();
	}
})


//参照 系统变更功能
ExchangeRateManageGrid = Ext.extend(Ext.grid.GridPanel,{
	reloadGrid:function(){
		this.exchangeStore.reload();
	},
	exchangeStore : null,
	constructor: function(){
		this.exchangeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url:root+'/config/Exchange!query.action',
			fields : ['eff_date', 'status','exchange','optr_id','optr_name', 'create_time'],
			totalProperty:'totalProperty',
			root:'records',
			listeners:{
				scope:this,
				beforeload:function(){
					var param = this.getQueryParam(true);
					Ext.apply(this.exchangeStore.baseParams,param);
					Ext.applyIf(this.exchangeStore.baseParams,{start: 0,limit: 20});
					return true;
				}
			}
		});
		var cm =[
			{header:langUtils.sys('ExchangeRateManage.commons.effective_time'),width:160,dataIndex:'eff_date',renderer:function(v){
				if(null == v ) return '';
				return Date.parseDate(v,'Y-m-d H:i:s').format('Y-m-d');
			}},
			{header:langUtils.sys('ExchangeRateManage.commons.status'),width:160,dataIndex:'status'},
			{header:langUtils.sys('ExchangeRateManage.commons.exchange_rate'),width:160,dataIndex:'exchange'},
			{header:langUtils.sys('ExchangeRateManage.commons.optr'),width:160,dataIndex:'optr_name'},
			{header:langUtils.sys('ExchangeRateManage.commons.create_time'),width:160,dataIndex:'create_time'}
		];
		
		this.effDateCmp = new Ext.form.DateField({});
		this.exchangeCmp = new Ext.form.NumberField({
			allowDecimals:false,allowNegative: false
		});
		
		this.statusCmp = new Ext.form.ComboBox({
				store:new Ext.data.JsonStore({
					fields:['key','value'],
					data:[
					{key:'',value:langUtils.sys('ExchangeRateManage.status.empty')},
						{key:'ACTIVE',value:langUtils.sys('ExchangeRateManage.status.ACTIVE')},
							{key:'INVALID',value:langUtils.sys('ExchangeRateManage.status.INVALID')}
					]
				}),displayField:'value',valueField:'key',triggerAction:'all',
				scope:this
		});
		
		ExchangeRateManageGrid.superclass.constructor.call(this, {
			border:false,
			region:'center',
			store:this.exchangeStore,
			columns:cm,
			sm : new Ext.grid.CheckboxSelectionModel({}),
			viewConfig : {
				forceFit : true
			},
			bbar:new Ext.PagingToolbar({store:this.exchangeStore,pageSize:20})
			,
			tbar : [' ', langUtils.sys('ExchangeRateManage.commons.effective_time')+' :',this.effDateCmp,
					'-',langUtils.sys('ExchangeRateManage.commons.exchange_rate')+' :',this.exchangeCmp,'-' ,
					langUtils.sys('ExchangeRateManage.commons.status')+' :',this.statusCmp,'-' ,
					{text:'&nbsp;&nbsp;' +
							langUtils.sys('ExchangeRateManage.commons.query') ,scope:this,handler:this.doQuery},'->',
					{text:langUtils.sys('ExchangeRateManage.commons.addNewOne') ,scope:this,handler:this.doAdd},'-',
					{text:langUtils.sys('ExchangeRateManage.commons.update') ,scope:this,handler:this.doUpdate},'-',
					{text:langUtils.sys('ExchangeRateManage.commons.invalid') ,scope:this,handler:this.doInvalid},'-'
					]
					
		});
	},
	getQueryParam:function(){
		var exchange = this.exchangeCmp.getValue();
		var oldEffDate = this.effDateCmp.getValue();
		var status = this.statusCmp.getValue();
		return  {oldExchange:exchange,effDate : oldEffDate ? oldEffDate.format('Y-m-d') :null,status:status};
	},
	doQuery:function(){
		this.getQueryParam(true);
		this.exchangeStore.load();
	},
	doAdd:function(){
		if(!this.win){
			this.win = new ExchangeFormWin(this);
		}
		this.win.resetForm();
		this.win.setTitle(langUtils.sys('ExchangeRateManage.commons.addNewOne'));
		this.win.show();
	},
	doUpdate:function(){
		if(!this.win){
			this.win = new ExchangeFormWin(this);
		}
		var recs = this.selModel.getSelections();
		if(recs.length != 1){
			Alert(langUtils.sys('ExchangeRateManage.msg.onlyOne'));
			return false;
		}
		this.win.loadRecord(recs[0]);
		this.win.setTitle(langUtils.sys('ExchangeRateManage.commons.update'));
		this.win.show();
	},
	doInvalid:function(){
		var recs = this.selModel.getSelections();
		if(recs.length != 1){
			Alert(langUtils.sys('ExchangeRateManage.msg.onlyOne'));
			return false;
		}
		var data = recs[0].data;
		var params = {effDate:Date.parseDate(data.eff_date,'Y-m-d H:i:s').format('Y-m-d'),oldExchange:data.exchange};
		Confirm(langUtils.sys('ExchangeRateManage.msg.areYouSure') , this , function(){
			Ext.Ajax.request({
				params: params,scope:this,
				url:root+'/config/Exchange!invalid.action',
				success: function( res, ops){
					Alert(langUtils.sys('ExchangeRateManage.msg.successMsg'),function(){
						this.reloadGrid();
					},this);
				}
			});
		} , function(){});
		
	}
})

ExchangeRateManage = Ext.extend(Ext.Panel,{
	grid : null,layout:'fit',
	constructor:function(){
		this.grid  = new ExchangeRateManageGrid();		
		ExchangeRateManage.superclass.constructor.call(this,{
			id:'ExchangeRateManage',
			title:langUtils.sys('ExchangeRateManage.title'),
			closable:true,
			border:false,
			layout:'border',
			items:[this.grid ]
		});
	},
	initEvents : function(){
		ExchangeRateManage.superclass.initEvents.call(this);
	}
});