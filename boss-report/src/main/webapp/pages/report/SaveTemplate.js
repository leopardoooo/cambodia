/**
 * SaveTemplate.js
 */
SaveTemplateWindow = Ext.extend(Ext.Window,{
	layout:'fit',title:'存为模板',id:'SaveTemplateID',plain:false,
	form:null,parent:null,
	reloadDatas:function(parent,dataPassed){
		this.form.getForm().reset();
		this.parent = parent;
		Ext.getCmp('mycube_cfg_list').setValue(this.reTrialPassedData(dataPassed) );
	},
	reTrialPassedData:function(dataPassed){
		//dataPassed = dataPassed.replace('"','').replace('"','');//去掉首尾的引号
		dataPassed = dataPassed.split('"').join('');//去掉所有引号
		if(Ext.isWindows){
			var array = dataPassed.split('\\n');
			dataPassed = array.join('\r\n');
		}
		return dataPassed;
	},
	constructor:function(parent,dataPassed){
		this.parent = parent;
		
		this.form = new Ext.FormPanel({
			layout:'column',defaults:{xtype:'fieldset',border:false,defaults:{border:false,layout:'form'}},
			items:[
				{columnWidth:1,items:[{}]},
				{columnWidth:.6,items:[
					{fieldLabel:'模板名称',name:'mycube_name',editable:true,xtype:'textfield',allowBlank:false}
				]},
				{columnWidth:1,height:100,
					items:{fieldLabel:'cube设置清单',id:'mycube_cfg_list',disabled:true,
							xtype:'textarea',width:300,height:80}},
				{columnWidth:1,
					items:{fieldLabel:'模板说明',name:'mycube_remark',xtype:'textarea',width:300,height:80}}
			]
		});
		
		Ext.getCmp('mycube_cfg_list').setValue(this.reTrialPassedData(dataPassed) );
		
		SaveTemplateWindow.superclass.constructor.call(this,{
			width:450,height:330,
			items:this.form,
			buttonAlign:'center',
			buttons:[
				{text:'确定',handler:this.submit,scope:this},
				{text:'取消',handler:this.cancel,scope:this}
			]
		});
	},
	submit:function(){
		if(!this.form.getForm().isValid()){
			Alert('请完整填写表单');
			return ;
		}
		var values = this.form.getForm().getValues();
		var param = {query_id:this.parent.query_id,rep_id:this.parent.rep_id};
		Ext.apply(param,values);
		param.mycube_show = false;
		
		Ext.Ajax.request({
			url : root + '/query/RepDesign!saveMyCube.action',
			scope:this,
			params : param,
			success : function(res, opt) {
				Alert('操作成功',function(){
					this.parent.initQuickChangeTmpCombo();
					this.hide();
				},this);
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
	},
	cancel:function(){
		this.hide();
	}

});
