/**
 * 变更产品的 预开通日期.
 * @class PreOpenTimeChangeForm
 * @extends BaseForm
 */
PreOpenTimeChangeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Prod!updatePreOpenDate.action",
	record:null,
	constructor: function(){
		this.record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//		if (this.record == null){
//			this.record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
//		}
		
		var minDate = nowDate().format('Y-m-d');
		
		PreOpenTimeChangeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			buttonAlign:'center',
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			buttons:[{text:'保存',handler:this.doSave,scope:this}],
			items : [{
				xtype : 'hidden',
				value : this.record.get('prod_sn'),
				id : 'prodSn'
			},{
				xtype : 'textfield',
				fieldLabel : '产品名称',
				style : Constant.TEXTFIELD_STYLE,
				value : this.record.get('prod_name'),
				id : 'prodName'
			},{
				width : 150,
				fieldLabel : '原预开通日期',
				xtype : 'textfield',
				style : Constant.TEXTFIELD_STYLE,
                value:this.record.get('pre_open_time').substr(0,10)
			},{
				xtype : 'datefield',
				width : 150,
				fieldLabel : '新预开通日期',
                format: 'Y-m-d',
                minValue:minDate,
                allowBlank:false,
                editable: false,
                listeners:{
					scope:this,
					select:function(field,value){
						var oldPreOpenDate = Date.parseDate(this.record.data.pre_open_time.substr(0,10),'Y-m-d');
						if(value.getTime() == oldPreOpenDate.getTime()){
							Alert('您选择的预开通日期与现在的一样,请重新选择!');
							field.setValue(null);
							return;
						}
						var prodstartdate = this.getForm().findField('feeDate');
						prodstartdate.setMinValue(value.format('Y-m-d'));
						var fee_date = prodstartdate.getValue();
						
						if(!Ext.isDate(fee_date) || (fee_date.getTime() - value.getTime()<0) ){
							prodstartdate.setValue(value);
						}
					}
				},
				id : 'preOpenDate'
			},{
				xtype : 'datefield',
				width : 150,
				fieldLabel : '开始计费日期',
                format: 'Y-m-d',
                minValue:minDate,
                allowBlank:false,
                editable: false,
				id : 'feeDate'
			}]
		})
		
	},
	validate:function(){
		return true;
	},
	getValues : function(){
		var params = this.getForm().getValues();
		var ps = App.getApp().getValues();
		ps.busi_code = App.getApp().getData().currentResource.busicode;
		params['countyId'] = this.record.data.county_id;
		params.jsonParams = Ext.encode(ps);
		return params;
	},
	doSave:function(){
		if(!this.validate()){
			return false;
		}
		var param = this.getValues();
		if(Ext.isEmpty(param.feeDate) || Ext.isEmpty(param.feeDate) ){
			Alert('新的预开通日期、开始计费日不能为空!');
			return false;
		}
		var msg = '修改后的预开通时间是： ' + param.preOpenDate + '</br> 开始计费日期是：' + param.feeDate + '</br>需要重新修改请点击“否”';
		tip = Show();
		Confirm(msg,this,function(){
			Ext.Ajax.request({
				url:this.url,
				params:param,
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:this.success
			});
		});
	},
	success : function(){
		Alert('操作成功',function(){
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
			tip.hide();
			tip = null;
			App.getApp().menu.bigWindow.hide();
		},this);
	}
});
Ext.onReady(function(){
	var form = new PreOpenTimeChangeForm();
//	var box = TemplateFactory.gTemplate(form);
	var box = TemplateFactory.gViewport(form);
});