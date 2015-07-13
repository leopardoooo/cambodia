OsdPhraseCfgFormWin = Ext.extend(Ext.Window,{
	form:null,width:450,height:250,parent:null,
	constructor:function(p){
		this.parent = p;
		this.form = new Ext.FormPanel({
			layout:'form',bodyStyle:'padding-top:10px',
			items:[
				{xtype:'hidden',name:'pid'},
				{xtype : 'textarea',fieldLabel:'词组内容',maxLength:400,
					name : 'phrase',height:150,width:250
				}
			]
		});
		
		OsdPhraseCfgFormWin.superclass.constructor.call(this,{
			layout:'fit',items:this.form,buttonAlign:'center',
			buttons:[
			{text:'确定 ',scope:this,handler:this.doSave},
			{text:'重置',scope:this,handler:this.doReset},
			{text:'取消 ',scope:this,handler:this.doCancel}
			]
		});
	},
	doSave:function(){
		var values = this.form.getForm().getValues();
		var param = {};
		for(var key in values){
			param ['phrase.'+key] = values[key];
		}
		
		Ext.Ajax.request({
			url: root + '/resource/Osd!souPhrase.action',
			params:param,scope:this,
			success:function(req){
				this.parent.phraseStore.reload();
				this.doCancel();
			}
		});
		
	},
	doReset:function(){
		var Type = this.parent.phraseStore.recordType;
		var newRec = new Type({pid:null,phrase:null});
		this.form.getForm().loadRecord(newRec);
	},
	doCancel:function(){
		this.doReset();
		this.hide();
	}
});

/**
 * OSD合法词组配置.
 * @class OsdPhraseCfg
 * @extends Ext.grid.GridPanel
 */
OsdPhraseCfg = Ext.extend(Ext.Panel,{
	phraseStore:null,closable:true,id:'OsdPhraseCfg',
	constructor:function(){
		
		this.phraseStore = new Ext.data.JsonStore({
			fields:['pid','phrase'],autoLoad:true,
			url: root + '/resource/Osd!queryOsdPhrase.action',root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20}
		});
		this.grid = new Ext.grid.GridPanel({
			columns:[
				{dataIndex:'pid',hidden:true},
				{header:'合法词组',dataIndex:'phrase',width:900,renderer:App.qtipValue}
			],
			store:this.phraseStore
		});
		OsdPhraseCfg.superclass.constructor.call(this,{
			layout:'fit',border:false,title:'OSD合法词组配置',
			tbar:[
				{text:'新增',scope:this,handler:this.addRecord},'-',
				{text:'修改',scope:this,handler:this.updateRecord},'-',
				{text:'删除',scope:this,handler:this.removeRecord},'-'
			],
			items:this.grid,
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.phraseStore
			})
		});
	},
	addRecord:function(){
		if(!this.formWin){
			this.formWin =new OsdPhraseCfgFormWin(this);
		}
		this.formWin.setTitle('新增');
		this.formWin.doReset();
		this.formWin.show();
	},
	updateRecord:function(){
		if(!this.formWin){
			this.formWin =new OsdPhraseCfgFormWin(this);
		}
		
		var records = this.grid.selModel.getSelections();
		if(!records || records.length !=1 ){
			Alert('请选择且仅选择一条记录.');
			return ;
		}
		var record = records[0];
		
		
		var form = this.formWin.form;
		form.getForm().loadRecord(record);
		this.formWin.setTitle('更新');
		this.formWin.show();
	},
	removeRecord:function(){
		var records = this.grid.selModel.getSelections();
		if(!records || records.length !=1 ){
			Alert('请选择且仅选择一条记录.');
			return ;
		}
		var record = records[0];
		var store = this.phraseStore;
		var params = {};
		for(var key in record.data){
			params['phrase.'+key] = record.get(key);
		}
		Ext.Ajax.request({
			url: root + '/resource/Osd!removePhrase.action',
			scope : this,
			params : params,
			success : function(req) {
				var data = Ext.decode(req.responseText);
				store.reload();
			}

		});
	}
});