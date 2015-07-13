/**
 * 居民转集团
 * @class
 * @extends BaseForm
 */
var custInfoHTML = '<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
	'<tr height=24>'+
		'<td class="label" width=20%>客户名称:</td>' +
		'<td class="input_bold" colspan=3>&nbsp;{[values.cust.cust_name || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label" width=20%>受理编号:</td>' +
		'<td class="input_bold" colspan=3>&nbsp;{[values.cust.cust_no || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">模拟编号:</td>' +
		'<td class="input">&nbsp;{[values.cust.old_cust_no|| ""]}</td>'+
		'<td class="label">黑名单:</td>' +
		'<td class="input">&nbsp;{[values.cust.is_black_text || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">客户地址:</td>' +
		'<td class="input" colspan=3>&nbsp;{[values.cust.address || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">客户状态:</td>' +
		'<td class="input">&nbsp;{[Ext.util.Format.statusShow(values.cust.status_text) || ""]}</td>'+
		'<td class="label">客户类型:</td>' +
		'<td class="input">&nbsp;{[values.cust.cust_type_text || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">证件类型:</td>' +
		'<td class="input">&nbsp;{[values.linkman.cert_type_text || ""]}</td>'+
		'<td class="label">证件号码:</td>' +
		'<td class="input">&nbsp;{[values.linkman.cert_num || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">联系人:</td>' +
		'<td class="input">&nbsp;{[values.linkman.linkman_name || ""]}</td>'+
		'<td class="label">固定电话:</td>' +
		'<td class="input">&nbsp;{[values.linkman.tel || ""]}</td>'+	
	'</tr>' +'<tr height=24>'+
		'<td class="label">出生日期:</td>' +
		'<td class="input">&nbsp;{[fm.dateFormat(values.linkman.birthday) || ""]}</td>'+
		'<td class="label">手机:</td>' +
		'<td class="input">&nbsp;{[values.linkman.mobile || ""]}</td>'+
	'</tr>' +
		'<tr height=24>'+
		'<td class="label">邮寄地址:</td>' +
		'<td class="input" colspan="3">&nbsp;{[values.linkman.mail_address ? values.linkman.mail_address + "&nbsp;&nbsp;&nbsp;&nbsp;邮编:&nbsp;" + [values.linkman.postcode || ""] : "" ]}</td>'+
	'</tr>' +	
	'<tpl if="values.cust.cust_colony == \'MNDKH\' || values.cust.cust_colony == \'XYKH\' ">' +
	'<tr height=24>'+
		'<td class="label">容量:</td>' +
		'<td class="input">&nbsp;{[values.cust.cust_count || 0]}</td>'+
		'<td class="label">实际数量:</td>' +
		'<td class="input">&nbsp;{[values.cust.real_cust_count || 0]}</td>'+
	'</tr>' +
	'</tpl>' +
	'<tr height=24>'+
		'<td class="label">备注:</td>' +
		'<td class="input" colspan="3">&nbsp;{[values.cust.remark || ""]}</td>'+
	'</tr>' +	
	'<tpl if="values.acctBank && values.acctBank.bank_account && values.acctBank.status==\'ACTIVE\'">' +
	'<tr height=24>'+
		'<td class="label">是否签约:</td>' +
		'<td class="input">&nbsp;是</td>'+
		'<td class="label">银行账号:</td>' +
		'<td class="input">&nbsp;{[values.acctBank.bank_account || ""]}</td>'+
	'</tr>' + 
	'</tpl>' +
'</table>';

var ChangeNonresCustForm = Ext.extend(Ext.Panel, {
	url: Constant.ROOT_PATH + "/core/x/Cust!changeNonresCust.action",
	tpl: null,
	constructor:function(){
		ChangeNonresCustForm.superclass.constructor.call(this,{
			labelAlign:'right',
			bodyStyle:'padding-top:10px',
			layout:'form',
			defaults:{
				border:false
			},
			items:[{
				xtype:'combo',id:'nonres_cust_id',fieldLabel:'集团名称',
				store: new Ext.data.JsonStore({
					url: root + '/commons/x/QueryCust!searchNonresCust.action',
					root: 'records',
					totalProperty: 'totalProperty',
					params:{start:0,limit:15},
					fields:['cust_id','cust_no','cust_name'],
					listeners:{
						scope:this,
						load: this.doNonresLoad
					}
				}),displayField:'cust_name',valueField:'cust_id',triggerAction:'all',width:350,
				typeAhead: true,mode:'remote',minChars:2,pageSize:15,editable:true,
				listeners:{
					scope:this,
					select: this.doNonresCustSelect
				}
			}]
		});
	},
	addCustBaseForm: function(){
		var custBaseForm = new CustBaseForm({
			id:'custBaseFormId',
			doInit: function(){
				CustBaseForm.superclass.doInit.call(this);
				var comp = this.getForm().findField('cust.cust_type');
				comp.setValue('NONRES');
				this.doCustTypeChange('NONRES');
				comp.setReadOnly(true);
			}
		});
		this.add({
			id:'newCustId',title:'新建客户',
			border:false,defaults:{border:false},
			items:[custBaseForm]
		});
		this.doLayout();
	},
	addCustInfo: function(data){
		if(this.items.length > 1 && this.items.itemAt(1).id == 'custInfoId'){
			this.remove(this.items.itemAt(1));
			this.doLayout();
		}
		this.tpl = new Ext.XTemplate(custInfoHTML);
		this.add({
			id:'custInfoId',
			xtype : "panel",
			border : false,
			bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
			html : this.tpl.applyTemplate( data )
		});
		this.doLayout();
	},
	doNonresLoad: function(store){
		var len = this.items.length;
		
		if(store.getCount() == 0 ){
			if(len > 1){
				if(this.items.itemAt(1).id == 'custInfoId'){
					this.remove(this.items.itemAt(1));
					this.doLayout();
					this.addCustBaseForm();
				}
			}else{
				this.addCustBaseForm();
			}
			Ext.getCmp('cust.cust_name').setValue(Ext.getCmp('nonres_cust_id').getRawValue());
		}else{
			this.remove(this.items.itemAt(1));
			this.doLayout();
		}
	},
	doNonresCustSelect: function(combo){
		var custId = combo.getValue();
			Ext.Ajax.request({
				scope : this,
				url:  root + '/commons/x/QueryCust!searchCustById.action',
				params: { 
					custId: custId
				},
				success: function(res,ops){
					var data = Ext.decode(res.responseText);
					if(data){
						this.addCustInfo(data);
					}
				 }
			});
	},
	doValid:function(){
		
		if(Ext.getCmp('custBaseFormId') && !Ext.getCmp('custBaseFormId').getForm().isValid()){
			var obj = {};
			obj['isValid'] = false;
			return obj;
		}
		
		if( Ext.getCmp('addrTreeCombo') && !Ext.getCmp('addrTreeCombo').getValue()){
			Ext.getCmp('addrTreeCombo').setValue("");
			
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = '无效的客户地址，请重新输入！';
			return obj;
		}

		if(Ext.getCmp('nonres_cust_id')){
			var custId = Ext.getCmp('nonres_cust_id').getValue();
			if(Ext.isEmpty(custId)){
				var obj = {};
				obj['isValid'] = false;
				obj['msg'] = '请选择一个集团!';
				return obj;
			}
		}
	},
	getValues: function(){
		var all = {};
		if(Ext.getCmp('custInfoId')){
			all['cust.cust_id'] = Ext.getCmp('nonres_cust_id').getValue();
		}else{
			var all = Ext.getCmp('custBaseFormId').getForm().getValues();
			if(this.custColony){
				all['cust.cust_colony'] = this.custColony;
			}
			all['cust.address'] = this.custAddress;
		}
		return all;
	},
	getFee: function (){ return 0 ; },
	success:function(form,res){
		App.getApp().data.custFullInfo.cust.status = 'INVALID';
		App.getApp().main.infoPanel.changeDisplay();
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var tf = new ChangeNonresCustForm();
	var box = TemplateFactory.gTemplate(tf);
});