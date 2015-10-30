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
		
		if(!this.oldCustType){
			this.oldCustType = 'RESIDENT';
		}
		this.provinceStore.load();
		this.removeCustType();
		
	},
	initField:function(){
		//初始化地址下拉框信息
//		var addrTreeCombo = Ext.getCmp('addrTreeCombo');
		
		var cust = App.getApp().getCust();
		/**
		 * 现在地址cust.address：区域+小区；
		 * cust.addr_id_text :　小区
		 */
		var custAddress = cust.address;
		
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
				var cust = App.getData().custFullInfo.cust;
				//客户地址
				this.custAddress = cust.address;
				
				
				this.getForm().items.each(function(f){
					if(f.label){
						Ext.fly(f.label.id).setStyle('color','gray');
					}
					f.disable();
				});
				Ext.getCmp('clickAddrId').setDisabled(true);
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
							if(fields[i].field_name == 'cust.addr_id'){//如果可以修改地址，把 选择地址按钮放开，隐藏的地址ID放开
								Ext.getCmp('clickAddrId').enable();
								Ext.getCmp('custAddrId').enable();
							}
							
						}
					}
				}
				if(cust.status == 'PREOPEN'){//如果是意向客户，把 选择地址按钮放开，隐藏的地址ID放开
					Ext.getCmp('clickAddrId').enable();
					Ext.getCmp('custAddrId').enable();
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
		var custAddress = Ext.getCmp('tempCustAddress').getValue();
		
		var busiCode = App.getData().currentResource.busicode;
		
		var obj = {};
		if(busiCode == '1003'){//过户
			if(oldCustName == custName){
				obj['isValid'] = false;
				obj['msg'] = lmsg('transferModifyCustName');
				return obj;
			}
		}else if(busiCode == '1010'){//移机
			if(oldAddress == custAddress){
				obj['isValid'] = false;
				obj['msg'] = lmsg('changeModifyCustAddr');
				return obj;
			}
		}else{//修改客户
			var values = this.getForm().getChangeValues();
			if(Ext.isEmpty(values) || values.length == 0){
				obj['isValid'] = false;
				obj['msg'] = lmsg('notModifyAnyInfo');
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
		var address = Ext.getCmp('tempCustAddress').getValue();
		//地址变更
		if(address){
			var oldAddress = App.getApp().getData().custFullInfo.cust.address;
			//地址变更后，如果原先是预开户PREOPEN的，改为正常ACTIVE
			if(oldAddress != address){
				if(Ext.getCmp('custStatusId').getValue() =='PREOPEN'){
					Ext.getCmp('custStatusId').setValue('ACTIVE');
					arr.push({
						columnName : 'cust.status',
						newValue : 'ACTIVE',
						oldValue : 'PREOPEN'
					});
				}			
				arr.push({
					columnName : 'cust.address',
					newValue : address,
					oldValue : oldAddress
				});

			}
		}
		//ROOM变更
		var oldNote = App.getApp().getData().custFullInfo.cust.note
		if(Ext.getCmp('custNoteId').getValue() != oldNote){
			arr.push({
				columnName : 'cust.note',
				newValue : Ext.getCmp('custNoteId').getValue(),
				oldValue : oldNote
			});
		}
		
		Ext.apply( all , {
			'custChangeInfo':Ext.encode(arr)
		})
		return all;
	},
	success:function(){
		if(App.getData().currentResource.busicode == '1003' || App.getData().currentResource.busicode == '1010'){
			App.getApp().refreshPayInfo(parent);
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
