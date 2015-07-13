/**
 * 报修单
 */
NewRepairTaskForm = Ext.extend(Ext.Panel , {
	taskForm : null,
	constructor: function(){
		var data = App.getApp().findBusiCfgData(App.getData().currentResource.busicode);
		this.taskForm = new WorkForm(data["tasktype"], null, false);
		this.taskForm.border = false ;
		this.bugCauseCombo = new Ext.ux.ParamCombo({
			fieldLabel:'故障类型',
			xtype:'paramcombo',
			allowBlank:false,
			anchor: '95%',
			paramName:'TASK_BUG_CAUSE'
		});
		
		//初始化下拉框的数据 
		App.form.initComboData([this.bugCauseCombo]);

//		this.remarkField = new Ext.form.TextArea({
//			anchor: '95%',
//			height: 42,
//			fieldLabel: '备注信息'
//		});
				
		NewRepairTaskForm.superclass.constructor.call(this, {
			layout: 'border',
			//border: false,
			bodyStyle: 'padding-top: 10px;',
			items: [{
				region: 'center',
				border: false,
				layout: 'fit',
				items:[this.taskForm]
			},{
				region: 'south',
				height: 90,
				//border: false,
				bodyStyle: 'padding-top: 5px;border-left: none;border-right: none;border-bottom: none;',
				layout: 'form',
				labelWidth: 70,
				labelAlign: 'right',
				items: [this.bugCauseCombo]
			}],
			buttons: [{
				text: '业务保存',
				width: 100,
				height: 25,
				iconCls: 'icon-save',
				scope: this,
				handler: this.doSave
			}]
		});
		
	},
	url: Constant.ROOT_PATH + "/core/x/Task!saveBugTask.action",
	doSave: function(){
		var result = this.taskForm.doValid();
		//不是业务的表单（如杂费）返回的是true/false
		if(result && result != true){
			if(result.isValid == false){
				if(result.msg){
					Alert(result.msg);
				}else{
					Alert("含有验证不通过的输入项!");
				}
				return false;
			}
		}else if(result == false){
			Alert("含有验证不通过的输入项!");
			return false;
		}
		
		var task = this.taskForm.getValues();
		var bugCauuse = this.bugCauseCombo.getValue();
		if(task["taskIds"]. length == 0){
			Alert("请选择报修单");
			return ;
		}
		if(Ext.isEmpty(bugCauuse)){
			Alert("请选择故障类型");
			return ;
		}

		mb = Show();
		var all = {};
		//获取通用的参数
		var commons = App.getValues();
		Ext.apply(commons, task);
//		commons["remark"] = this.remarkField.getValue();
		//设置提交参数
		all[CoreConstant.JSON_PARAMS] = Ext.encode(commons);
		all["bugCause"] = bugCauuse;
		//提交
		Ext.Ajax.request({
			scope: this,
			params: all,
			url: this.url,
			success: function( res, ops){
				mb.hide();
				Alert("操作成功", function(){
					App.getApp().menu.hideBusiWin();
					App.getApp().main.infoPanel.getDocPanel().taskGrid.remoteRefresh();
				});
			}
		});
		
	}
});

/**
 * 入口
 */
Ext.onReady(function(){
	var form  = new NewRepairTaskForm();
	//render
	TemplateFactory.gViewport(form);
});