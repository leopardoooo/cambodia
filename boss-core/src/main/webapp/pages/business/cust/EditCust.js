/**
 * 修改客户信息表单
 * @class TransferForm
 * @extends Ext.FormPanel
 */
EditCustForm = Ext.extend( CustBaseForm , {
	url:  Constant.ROOT_PATH + "/core/x/Cust!editCust.action",
	custFullInfo : {},
	oldCustName:null,
	oldAddrName:null,
	constructor: function(){
		EditCustForm.superclass.constructor.call(this, {
		});
	},
	doInitAttrForm: function(){
		var groupId = 1;
		if(App.getData().custFullInfo.cust.cust_type =="RESIDENT"){
			groupId = 2;
		}
		this.extAttrForm = ExtAttrFactory.gExtEditForm({
			tabName: Constant.C_CUST,
			pkValue: App.getCustId(),
			prefixName : 'cust.',
			group: groupId
		}); 
	},
	doInit:function(){
		EditCustForm.superclass.doInit.call(this);
		this.initField();
		this.setCanUpdateField();
		
		//模拟大客户不能修改为普通客户，普通客户也不能修改为模拟大客户
		var value = App.getData().custFullInfo.cust.cust_colony;
		if(Ext.isEmpty(value)){
			this.remove(this.unitForm);
			this.doLayout();
		}else{
			var comp = Ext.getCmp('custcount_itemId');
			comp.show();comp.enable();
			Ext.getCmp('cust_count_id').allowBlank = false;
		}
	},
	initField:function(){
		//初始化地址下拉框信息
		var addrTreeCombo = Ext.getCmp('addrTreeCombo');
		
		var cust = App.getApp().getCust();
		/**
		 * 现在地址cust.address：区域+小区；
		 * cust.addr_id_text :　小区
		 */
		var custAddress = cust.address;
		if(cust.address){//割接 无地址
			if(cust.t1 || cust.t2 || cust.t3 || cust.t4 || cust.t5 || cust.note){
				index = cust.address.indexOf(cust.addr_id_text);
				//index + index+cust.addr_id_text.length 客户地址前面部分
				custAddress = cust.address.substring(0,index+cust.addr_id_text.length);
			}
		}
		addrTreeCombo.addOption(cust.addr_id,custAddress);
		Ext.getCmp('tempCustAddress').setValue(custAddress);
		
		//从app.data.cust,linkman,resident中初始化客户信息
		var custFullInfo = {};
		if (App.getData().custFullInfo.cust){
			for (var prop in App.getData().custFullInfo.cust){
				custFullInfo['cust.'+prop] =App.getData().custFullInfo.cust[prop];
			}
		};
		
		if (App.getData().custFullInfo.linkman){
			for (var prop in App.getData().custFullInfo.linkman){
				custFullInfo['linkman.'+prop] = App.getData().custFullInfo.linkman[prop];
			}
		};
		
		custFullInfo['linkman.birthday']=Date.parseDate(custFullInfo["linkman.birthday"],'Y-m-d h:i:s');
		this.doCustTypeChange(custFullInfo['cust.cust_type']);
		this.custFullInfo = custFullInfo;
	},
	setCanUpdateField:function(){
		that = this;
		//查找可以修改的字段，将不可修改的字段设置为disabled
		Ext.Ajax.request({
			scope:this,
			url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryCanUpdateCustField.action",
			params:{busiCode:App.getData().currentResource.busicode},
			success:function(response,opts){
				this.getForm().loadRecord(new Ext.data.Record(this.custFullInfo));
				//客户地址
				this.custAddress = App.getData().custFullInfo.cust.address;
				
				this.getForm().items.each(function(f){
					if(f.label){
						Ext.fly(f.label.id).setStyle('color','gray');
					}
					f.disable();
				});

				var fields = Ext.decode(response.responseText);
				
				var compare = function(f,name){
					if(fields){
						for (var i=0;i<fields.length;i++){
							if(name == fields[i].field_name){
								f.enable();
								if(f.label){
									Ext.fly(f.label.id).setStyle({
										'font-weight':'bold',
										'color' : 'black'
										});	
								}
							}
							if(fields[i].field_name == 'cust.addr_id'){//如果是地址，把剩余的放开允许修改
								Ext.getCmp('cust.t1').enable();
								Ext.getCmp('cust.t2').enable();
								Ext.getCmp('cust.t3').enable();
								Ext.getCmp('cust.t4').enable();
								Ext.getCmp('cust.t5').enable();
								Ext.getCmp('cust.note').enable();
							}
						}
					}
				
				}
				
				var eachItems = function(items){
					if(items && items.length>0){
						items.each(function(f){
							var name = f.name || f.hiddenName;
							if(Ext.isEmpty(name) || (f.items && f.items.length>0)){
								compare(f,name);
									eachItems(f.items);
							}else{
									compare(f,name);
							}
						});
					}
				};
				eachItems(this.items);
			}
		});
	},
	doValid:function(){
		var oldCustName = App.getData().custFullInfo.cust.cust_name;
		var oldAddress = App.getData().custFullInfo.cust.address;
		
		var custName = Ext.getCmp('cust.cust_name').getValue();
		
		var busiCode = App.getData().currentResource.busicode;
		
		var obj = {};
		if(busiCode == '1003'){//过户
			if(oldCustName == custName){
				obj['isValid'] = false;
				obj['msg'] = '过户请修改客户名称！';
				return obj;
			}
		}else if(busiCode == '1010'){//移机
			if(oldAddress == this.custAddress){
				obj['isValid'] = false;
				obj['msg'] = '移机请修改客户地址！';
				return obj;
			}
		}else{//修改客户
			var values = this.getForm().getChangeValues();
			if(Ext.isEmpty(values) || values.length == 0){
				obj['isValid'] = false;
				obj['msg'] = '您没有修改任何资料！';
				return obj;
			}
		}
		
		return EditCustForm.superclass.doValid.call(this);
	},
	getValues:function(){
		var all = {},key;
		var vs = this.getForm().getChangeValues();
		var arr = [];
		for(var i=0;i<vs.length;i++){
			if(vs[i].columnName.indexOf(CoreConstant.Ext_FIELDNAME_PREFIX) === 0){
				all[vs[i].columnName] = vs[i].newValue;
			}else{
				arr.push(vs[i]);
			}
		}
		
		if(this.custAddress){
			var oldAddress = App.getApp().getData().custFullInfo.cust.address;
			arr.push({
				columnName : 'cust.address',
				newValue : this.custAddress,
				oldValue : oldAddress
			});
		}
		
		Ext.apply( all , {
			'custChangeInfo':Ext.encode(arr)
		})
		return all;
	},
	success:function(){
		if(App.getData().currentResource.busicode == '1003'){
			var acctBank=App.getApp().data.custFullInfo.acctBank;
			if(acctBank && acctBank.cust_id){
				App.getApp().alertMsg("该客户有银行签约扣款，请询问客户是否需要继续银行扣款，如不需要务必操作银行解约！");
			}
		}
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});


/**
 * 入口
 */
Ext.onReady(function(){
	var ecf = new EditCustForm();
	var box = TemplateFactory.gTemplate(ecf);
});
