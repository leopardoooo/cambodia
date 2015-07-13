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
//		this.busiOptrStore.on('load',this.doLoad,this);		//多选修改可能选择其中几个相同的业务员
		ModifyBusiOptrForm.superclass.constructor.call(this,{
			border:false,
			labelWidth:75,
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items:[
				{xtype:'hidden',name:'fee_sn',value:feeSn},
				{xtype:'displayfield',fieldLabel:'旧业务员',value:busiOptrName},
				{
					hiddenName: 'busi_optr_id',
					fieldLabel: '新业务员',
					xtype:'lovcombo',width:200,
					store:this.busiOptrStore,
					valueField:'optr_id',displayField:'optr_name',boxMaxHeight:500,
					editable:true,forceSelection:true,allowBlank:true,
					beforeBlur:function(){},
					listeners:{
						beforequery:function(e){
							var combo = e.combo;
							var store = combo.getStore();
				            var value = e.query;
					        if(Ext.isEmpty(value)){ 
								store.clearFilter();
					        }else{
					            combo.collapse();
					        	var re = new RegExp('^.*' + value + '.*$','i');
					            store.filterBy(function(record,id){
					                var text = record.get('attr');
					                return re.test(text);
					            });
					            combo.expand();
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
	doValid: function(){
		var value = this.getForm().findField('busi_optr_id').getValue();
		var arr = value.split(',');
		if(arr.length > 3){
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = '业务员最多选择3个!';
			return obj;
		}
		return ModifyBusiOptrForm.superclass.doValid.call(this);
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