/**
 * SwitchTemplate.js
 */
 
SwitchTemplateWindow = Ext.extend(Ext.Window,{
	layout:'fit',title:'切换模板',id:'SwitchTemplateID',
	form:null,parent:null,dataPassed:[],templateStore:null,currentRecord:null,
	reloadDatas:function(parent,dataPassed){
		this.form.getForm().reset();
		this.parent = parent;
		this.templateStore.loadData(dataPassed);
		this.currentRecord = null;
	},
	constructor:function(parent,dataPassed){
		this.parent = parent;
		this.dataPassed = dataPassed;
		
		this.templateStore = new Ext.data.JsonStore({
			fields:['cube_json','cube_config_text','default_show','name','optr_id','remark','rep_id','status','template_id']
		});
		
		this.form = new Ext.FormPanel({
			layout:'column',defaults:{xtype:'fieldset',border:false,layout:'form',defaults:{border:false,layout:'form'}},labelWidth:55,
			items:[
				{columnWidth:1,items:[{}]},
				{columnWidth:.5,items:{fieldLabel:'模板',xtype:'combo',hiddenName:'name',store:this.templateStore,valueField:'template_id',displayField:'name',
					listeners:{
						scope:this,select:this.changeTemplate
					}
				}},
				{columnWidth:.2,items:{xtype:'button',text:'删除摸板',height:20,handler:this.deleteTemplate,scope:this}},
				{columnWidth:.2,items:{fieldLabel:'设为首选',xtype:'checkbox',name:'default_show',
					listeners:{
						scope:this,check:this.saveAsDefault
					}
				}},
				{columnWidth:1,items:{fieldLabel:'配置',name:'cube_config_text',width:340,height:80,disabled:true,xtype:'textarea'}},
				{columnWidth:1,items:{fieldLabel:'备注:</br><a href="#" id="remarkModifyHrefId">修改:</a></br><a href="#" id="remarkSaveHrefId">保存</a>',
									name:'remark',id:'temp_remark',width:340,height:80,xtype:'textarea'}}
			]
		});
		this.templateStore.loadData(dataPassed);
		SwitchTemplateWindow.superclass.constructor.call(this,{
			width:450,height:340,
			items:this.form,
			buttonAlign:'center',
			buttons:[
				{text:'展现模板',handler:this.showTemplate,scope:this},
				{text:'警戒配置',handler:this.warningConfig,scope:this},
				{text:'取消',handler:this.cancel,scope:this}
			],
			listeners:{
				scope:this,afterrender:this.doAfterRender
			}
		});
	},
	cancel:function(){
		this.hide();
	},
	warningConfig:function(){//警戒配置
		/*
		　　RepDesignAction.queryCubeWarn参数template_id
		　　返回 List<MeaWarn>
		　　其中 MeaWarn 对应 一个指标tabpanel
		　　     MeaWarn.mea 对应 tabpanel名称
		　　     MeaWarn.rowlist 对应 条件界面的grid
		*/
		if(!this.currentRecord){
			Alert('请先选择一个模板!');
			return false;
		}
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/RepDesign!queryCubeWarn.action',
			scope:this,timeout:9999999999,
			params : {template_id:this.currentRecord.get('template_id')},
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				wait.hide();
				wait = null;
				if(!this.warnConfigWin ){
					this.warnConfigWin = new WarnConfigWindow(this,list);
				}else{
					this.warnConfigWin.rebuildCmps(this,list);
				}
				this.warnConfigWin.show();
				
//				this.hide();
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('警戒配置数据初始化出错');
			}
		})
	},
	showTemplate:function(){
		//showAction.cubeChangeMyCube()
		var param = {query_id:this.parent.query_id, template_id:this.currentRecord.get('template_id')};
		this.parent.showTemplate(param,this);
	},
	deleteTemplate:function(btn,eventObject){//删除模板
		//RepDesignAction.saveDeleteMyCube() 	参数 rep_id,mycube_name 
		//返回正确，则删除store对应数据
		var param = {rep_id:this.parent.rep_id,mycube_name:this.currentRecord.get('name')};
		this.templateStore.remove(this.currentRecord);
		this.changeTemplate(this.form.getForm().findField('name'),null);
		
		Ext.Ajax.request({
			url : root + '/query/RepDesign!saveDeleteMyCube.action',
			scope:this,
			params : param,
			success : function(res, opt) {
				Alert('操作成功',function(){
					this.templateStore.remove(this.currentRecord);
					this.parent.initQuickChangeTmpCombo();
					this.changeTemplate(this.form.getForm().findField('name'),null);
				},this);
			},
			failure:function(){
				Alert('设为首选操作出错');
			}
		});
	},
	saveAsDefault:function(checkBox,checkd){
		if(Ext.isEmpty(this.currentRecord) || !checkd ){//conbo没有选中任何记录,或者值为false
			return false;
		}
		if( this.currentRecord.get('couldNotSetDefault') == true ){//本来已经是首选的,不做操作
			return;
		}
		var comboRecIndex = this.templateStore.find('name',this.currentRecord.get('name'));
		var comboRec = this.templateStore.getAt(comboRecIndex);
		var param = {rep_id:this.parent.rep_id,mycube_name:this.currentRecord.get('name')};
		Ext.Ajax.request({
			url : root + '/query/RepDesign!saveMyCubeDefault.action',
			scope:this,
			params : param,
			success : function(res, opt) {
				Alert('模板 "' + param.mycube_name + '" 将会在下次查询的时候自动加载');
				comboRec.set('couldNotSetDefault',true);
				comboRec.set('default_show',true);
				this.currentRecord.set('couldNotSetDefault',true);//设置为表单已经加载过了,规避每次加载的时候调用该方法
				var length = this.templateStore.getCount();
				
				for(var index = 0;index<length;index++){
					if(index == comboRecIndex){
						continue;
					}
					var recNotDefault = this.templateStore.getAt(index);
					recNotDefault.set('couldNotSetDefault',false);
					recNotDefault.set('default_show',false);
				}
				
			},
			failure:function(){
				Alert('设为首选操作出错');
			}
		});
	},
	doAfterRender:function(){
		var save = Ext.get('remarkSaveHrefId');
		var modify = Ext.get('remarkModifyHrefId');
		modify.on('click',function(){
			var remark = this.form.getForm().findField('remark');
			remark.focus(true);
			remark.setDisabled(false);
		},this);
		save.on('click',this.saveRemark,this);
	},
	saveRemark:function(){
		var param = {
			rep_id : this.parent.rep_id,
			mycube_name : this.currentRecord.get('name'),
			mycube_remark : this.form.getForm().findField('remark').getValue()
		};
		
		var comboRecIndex = this.templateStore.find('name',this.currentRecord.get('name'));
		var comboRec = this.templateStore.getAt(comboRecIndex);
		Ext.Ajax.request({
			url : root + '/query/RepDesign!saveUpdateMyCubeRemark.action',
			scope:this,
			params : param,
			success : function(res, opt) {
				Alert('保存说明操作成功');
				comboRec.set('remark',param.mycube_remark);
				this.currentRecord.set('remark',param.mycube_remark);
			},
			failure:function(){
				Alert('保存说明操作出错');
			}
		});
	},
	changeTemplate:function(combo,record,index){
//		var record = combo.findRecord(combo.valueField, newValue);
		if(!record){
			this.currentRecord = null;
			this.form.getForm().reset();
			return;
		}
		if(record.get('default_show') == 'true' ){
			record.set('couldNotSetDefault',true);
		}
		//切换combo值的时候更新说明属性,增加这个属性避免每次切换触发其change事件
		this.currentRecord = record;
		
		var remark = this.form.getForm().findField('remark');
		var default_show = this.form.getForm().findField('default_show');
		
		this.form.getForm().loadRecord(record);
		
		if(record.get('status') == 'F'){//无效
			remark.setDisabled(true);
			default_show.setDisabled(true);
			this.buttons[0].setDisabled(true);
		}else{
			remark.setDisabled(false);
			default_show.setDisabled(false);
			this.buttons[0].setDisabled(false);
		}
		
	}
});
