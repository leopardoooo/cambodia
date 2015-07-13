/**
*修改机顶盒号
*/

EditStbForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!editStb.action",
	constructor:function(){
		var itemArr = null;
		var userRecord = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		//如果机卡都为空，卡必输，机非必须
		if(!userRecord.get('card_id') && !userRecord.get('stb_id')){
			itemArr=[{
				xtype:'combo',
				hiddenName:'stb_id',
				fieldLabel:'机顶盒号',width:150,selectOnFocus:true,
				store:new Ext.data.ArrayStore({fields:['device_code']}),
				displayField:'device_code',valueField:'device_code',
				editable:true,emptyText:'请选择或输入...',
				listeners:{
					scope:this,
					change:function(comp){
						App.getApp().checkDevice(comp,'STB',null);
					}
				}
	       	},{
				xtype:'combo',
				hiddenName:'card_id',
				allowBlank:false,
				fieldLabel:'智能卡号',width:150,selectOnFocus:true,
				store:new Ext.data.ArrayStore({fields:['device_code']}),
				displayField:'device_code',valueField:'device_code',
				editable:true,emptyText:'请选择或输入...',
				listeners:{
					scope:this,
					change:function(comp){
						App.getApp().checkDevice(comp,'CARD')
					}
				}
           	}]
		}else if(!userRecord.get('card_id')){
			itemArr=[{
				xtype:'combo',
				hiddenName:'card_id',
				allowBlank:false,
				fieldLabel:'智能卡号',width:150,selectOnFocus:true,
				store:new Ext.data.ArrayStore({fields:['device_code']}),
				displayField:'device_code',valueField:'device_code',
				editable:true,emptyText:'请选择或输入...',
				listeners:{
					scope:this,
					change:function(comp){
						App.getApp().checkDevice(comp,'CARD')
					}
				}
           	}]
		}else{
			itemArr=[{
				xtype:'combo',
				hiddenName:'stb_id',
				allowBlank:false,
				fieldLabel:'机顶盒号',width:150,selectOnFocus:true,
				store:new Ext.data.ArrayStore({fields:['device_code']}),
				displayField:'device_code',valueField:'device_code',
				editable:true,emptyText:'请选择或输入...',
				listeners:{
					scope:this,
					change:function(comp){
						App.getApp().checkDevice(comp,'STB',null);
					}
				}
	       	}]
		}
		
		EditStbForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
            border:false,
            bodyStyle: Constant.TAB_STYLE,
           	items:itemArr
		});
	},
	initComponent:function(){
		EditStbForm.superclass.initComponent.call(this);
		var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();
		var device = cdStore.query('status','IDLE');
		var stbArr = [],cardArr=[];
		device.each(function(item,index,length){
			var data = item.data;
			if(data.loss_reg == 'F'){//空闲且没挂失
				if(data['device_type'] == 'STB'){
					stbArr.push([data['device_code']]);
				}else if(data['device_type'] == 'CARD'){
					cardArr.push([data['device_code']]);
				}
			}
		},this);
		if(stbArr.length>0 && this.getForm().findField('stb_id'))
			this.getForm().findField('stb_id').getStore().loadData(stbArr);
			
		if(cardArr.length>0 && this.getForm().findField('card_id'))
			this.getForm().findField('card_id').getStore().loadData(cardArr);
			
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var form = new EditStbForm();
	var box = TemplateFactory.gTemplate(form);
});