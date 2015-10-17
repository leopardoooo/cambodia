/**
*修改业务员
*/
ModifyBusiOptrForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Pay!editBusiOptr.action",
	oldBusiOptrId:null,
	currentRecord:null,
	constructor:function(){
		var payfeePanel = App.getApp().main.infoPanel.getPayfeePanel();
		this.currentRecord = payfeePanel.acctFeeGrid.getSelectionModel().getSelected();
		if(Ext.isEmpty(this.currentRecord))
			this.currentRecord = payfeePanel.busiFeeGrid.getSelectionModel().getSelected();
		var feeSn = this.currentRecord.get('fee_sn');
		var busiOptrName = this.currentRecord.get('busi_optr_name');
		this.oldBusiOptrId = this.currentRecord.get('busi_optr_id');
		this.busiOptrStore = new Ext.data.JsonStore({
			url:root+'/system/x/Index!queryOptrByCountyId.action',
			fields:['optr_id','optr_name','attr']
		});
		this.busiOptrStore.load();
		ModifyBusiOptrForm.superclass.constructor.call(this,{
			border:false,
			labelWidth:120,
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items:[
				{xtype:'hidden',name:'fee_sn',value:feeSn},
				{xtype:'displayfield',fieldLabel: lmain("pay._form.oldOptrName"),value:busiOptrName},
				{
					hiddenName: 'busi_optr_id',
					fieldLabel: lmain("pay._form.newOptrName"),
					xtype:'combo',width:200,
					store:this.busiOptrStore,
					valueField:'optr_id',displayField:'optr_name',
					editable:true,forceSelection:true,allowBlank:false,
					listeners: {
						scope: this,
						'focus':{
							fn:function(combo){
								combo.expand();
								combo.doQuery(combo.allQuery, true);
							},
							buffer:200
						},
						beforequery: function(e){
							var combo = e.combo;
					        if(!e.forceAll){
					            var value = Ext.isEmpty(e.query) ? '' : e.query.toUpperCase();  
					            combo.store.filterBy(function(record,id){  
					                return record.get(combo.displayField).toUpperCase().indexOf(value) != -1;  
					            });
					            if(!combo.isExpanded()){
					            	combo.expand();
					            }
					            return false;
					        }
						}
					}
				}
			]
		});
	},
	doLoad:function(store){
		store.each(function(record){
			if(record.get('optr_id') == this.oldBusiOptrId){
				store.remove(record);
				return false;
			}
		},this);
	},
	getValues:function(){
		var values = this.getForm().getValues();
		values['old_busi_optr_id'] = this.oldBusiOptrId;
		return values;
	},
	success:function(form, resultData){
		form.currentRecord.set('busi_optr_id',resultData['busi_optr_id']);
		form.currentRecord.set('busi_optr_name',resultData['busi_optr_name']);
		form.currentRecord.commit();
		var panel = App.getApp().main.infoPanel.getDoneCodePanel();
		panel.refresh();
		panel.setReload(true);
		App.getData().busiOptrId = resultData['busi_optr_id'];
	}
});

Ext.onReady(function(){
	var form = new ModifyBusiOptrForm();
	var box = TemplateFactory.gTemplate(form);
});