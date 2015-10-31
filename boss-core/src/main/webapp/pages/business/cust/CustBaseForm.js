


/**
 * 联系人面板
 * @class LinkPanel
 * @extends Ext.Panel
 */
LinkPanel = Ext.extend(Ext.Panel,{
	parent : null,
	constructor : function(p){
		this.parent = p;
		LinkPanel.superclass.constructor.call(this,{
			layout : 'form',
			anchor: '100%',
			border : false,
			baseCls: 'x-plain',
			items : [{
				layout:'column',
				baseCls: 'x-plain',
				anchor: '100%',
				defaults:{
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form',
					labelWidth: 120
				},
				items: [{
					items:[{
						fieldLabel: langUtils.main("cust.base.linkMan"),
						name:'linkman.linkman_name',
						xtype:'textfield'
					},{
						fieldLabel: langUtils.main("cust.base.certType"),
						xtype:'paramcombo',
						allowBlank:false,
						hiddenName:'linkman.cert_type',
						paramName:'CERT_TYPE',
						defaultValue:'SFZ'
					},{
						fieldLabel: langUtils.main("cust.base.tel"),
						name:'linkman.tel',
						xtype:'textfield',
						allowBlank: false,
						id: 'linkmanTel'
					},{
						fieldLabel: langUtils.main("cust.base.email"),
						name:'linkman.email',
						xtype:'textfield',
						vtype:'email'
					},{
						fieldLabel:langUtils.main("cust.base.postcode"),
						name:'linkman.postcode',
						xtype:'textfield'
					}]
				},{
					items:[{
						fieldLabel:langUtils.main("cust.base.sex"),
						id : 'linkmanSex',
						hiddenName:'linkman.sex',
						xtype:'paramcombo',
						paramName:'SEX'
					},{
						fieldLabel: langUtils.main("cust.base.certNum"),
						xtype:'textfield',
						vtype: 'alphanum',
						width : 130,
						allowBlank:false,
						name:'linkman.cert_num',
						id: 'linkman_cert_num_el',
						listeners: {
							scope: this,
							change: this.parent.generatePwd
						}
					},{
						fieldLabel: langUtils.main("cust.base.mobile"),
						name:'linkman.mobile',
						xtype:'numberfield',						
						id: 'linkmanMobile'
					},{
						id: 'cust_pwd_id',
						fieldLabel: langUtils.bc("common.pswd"),
						vtype : 'loginName',
						xtype:'textfield',
						value: '000000',
						inputType: 'password',
						name:'cust.password'
					},{
						fieldLabel: langUtils.main("cust.base.barthday"),
						width : 125,
						id : 'linkmanBirthday',
						name:'linkman.birthday',
						xtype:'datefield',
						format:'Y-m-d'
					}]
				},{
					columnWidth:1,
					items:[{
						fieldLabel:langUtils.main("cust.base.postalAddr"),
						width : 400,
						id : 'linkman.mail_address',
						name:'linkman.mail_address',
						xtype:'textfield'
					}]
					
				}]
			},{
				fieldLabel: langUtils.bc("common.remark"),
				name:'cust.remark',
				preventScrollbars : true,
				height : 30,
				width : 350,
				xtype:'textarea'
			}]
		})
	}
});
/**
 * 客户的Formg构造,这是一个基类，
 * 为客户开户、修改资料等功能提供的基类
 */
CustBaseForm = Ext.extend( BaseForm , {
	//扩展属性面板
	extAttrForm: null,
	linkPanel : null,
	oldCustType:'RESIDENT',
	custLevel : null,
	navMenu:null,
	provinceStore:null,
	custCode:null,
	constructor: function(config){
		Ext.apply(this, config);
		this.provinceStore = new Ext.data.JsonStore({
 				url: root + '/commons/x/QueryCust!queryProvince.action',
 				fields : ['name','cust_code']
 		});
 		this.busiOptrStore = new Ext.data.JsonStore({
			url:root+'/system/x/Index!queryOptrByCountyId.action',
			fields:['optr_id','optr_name','attr'],
			autoLoad: true
		});
		//居民信息扩展
		this.doInitAttrForm(2);
		this.linkPanel = new LinkPanel(this);
		CustBaseForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			labelWidth: 120,
			bodyStyle: Constant.TAB_STYLE,
			items:[{
				layout:'column',
				baseCls: 'x-plain',
				anchor: '100%',
				defaults:{
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form',
					labelWidth: 120
				},
				items: [{
					items:[{
						fieldLabel: langUtils.main("cust.base.type"),
						xtype:'paramcombo',
						allowBlank:false,
						width : 125,
						hiddenName:'cust.cust_type',
						paramName:'CUST_TYPE',
						defaultValue:'RESIDENT',
						listeners:{
							scope: this,
							'select': function(combo,rec){
								this.doCustTypeChange(combo.getValue());
							}
						}
					},{
						fieldLabel: langUtils.main("cust.base.name"),
						xtype:'textfield',
						id : 'cust.cust_name',
						name:'cust.cust_name',
						allowBlank:false,
						listeners:{
							scope: this,
							'change': this.doCustNameChange
						}
					}]	
				},{
					items:[{
							xtype: 'checkbox',
						    fieldLabel: langUtils.main("cust._form.thinkCust"),
						    id: "isCanToCustId",
						    listeners:{
				            	scope: this,
				            	check: this.doCheckedChange
				            }
						},{
							xtype:'combo',
							id : 'provinceId',
							fieldLabel: langUtils.main("cust._form.province"),
							forceSelection : true,
							store : this.provinceStore,
							triggerAction : 'all',
							mode: 'local',
							displayField : 'name',
							valueField : 'cust_code',
							disabled:true,
							editable : false,
							listeners:{
								scope:this,
								select:function(combo){
									this.custCode = combo.getValue();
								}
							}
						}
					]
				},{
					columnWidth:0.80,
					items:[{
						width:380,
						fieldLabel: langUtils.main("cust.base.addr"),						
						xtype:'textfield',
						id : 'tempCustAddress',
						name:'cust.address',
						allowBlank:false,
						disabled:true,
						listeners:{
							scope:this,
							change:this.doAddressChange
						}
					}]
				},{
					columnWidth:0.15,
					items:[{
						id:'clickAddrId',
						xtype : 'button',
						text: langUtils.bc("common.switchor"),
						scope: this,
						width: 60,
						height : 18,
						iconCls:'icon-tree',
						handler: this.doClickAddr
					}]
				},{
					id:'addCustItemsOne',
					items:[{
						id:'cust_level_id',
						fieldLabel: langUtils.main("cust.base.cust_level"),
						xtype:'paramcombo',
						allowBlank:false,
						hiddenName:'cust.cust_level',
						paramName:'CUST_LEVEL',
						defaultValue:'YBKH',
						listeners : {
							scope : this,
							select : function(combo){
								this.custLevel = combo.getValue();
							}
						}
					},{
						id: 'comboDvpId',
						fieldLabel: langUtils.main("cust.base.developName"),
						xtype:'combo',
						hiddenName: 'cust.str9',
						store: this.busiOptrStore,
						valueField:'optr_id',displayField:'optr_name',
						editable:true,
						allowBlank:false,
				        forceSelection: true,
				        selectOnFocus:true,
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
					}]
				},{
					id:'addCustItemsTwo',
					items:[{
							fieldLabel: langUtils.main("cust.base.languageType"),
							xtype: 'paramcombo',
							paramName: 'LANGUAGE_TYPE',
							allowBlank: false,
							hiddenName:'cust.str6'
						},{
							id:'cust_spkg_sn_id',
							fieldLabel:langUtils.main("cust.base.spkgSn"),
							xtype:'textfield',
							name:'cust.spkg_sn'
						}]
				}]
			},{
				xtype : 'hidden',
				name:'cust.note',
				id : 'custNoteId'
			}
			,{
				xtype : 'hidden',
				id : 'custAddrId',
				name:'cust.addr_id'
			},{
				xtype : 'hidden',
				id : 'custStatusId',
				name:'cust.status'
			},this.linkPanel,this.extAttrForm]
		});
	},
	doClickAddr:function(btn){
		var win = Ext.getCmp('addrCustSelectWinId');
		if(!win){
			win = new AddrCustSelectWin(Ext.getCmp('custAddrId').getValue());
		}
		win.show();
		win.maximize();
	},
	doCheckedChange:function(box, checked){
		Ext.getCmp('tempCustAddress').setValue('');
		Ext.getCmp('linkman.mail_address').setValue('');
		Ext.getCmp('provinceId').setValue('');
		this.custCode = null;
		if(checked){
			//意向客户，addrId 指定为9
			Ext.getCmp('custAddrId').setValue(9);
			Ext.getCmp('provinceId').setDisabled(false);
			Ext.getCmp('tempCustAddress').setDisabled(false);
			Ext.getCmp('clickAddrId').setDisabled(true);
			Ext.getCmp('custStatusId').setValue('PREOPEN');
		}else{
			Ext.getCmp('tempCustAddress').setDisabled(true);
			Ext.getCmp('provinceId').setDisabled(true);
			Ext.getCmp('clickAddrId').setDisabled(false);
		}
	},
	removeCustLevel:function(){
		var store = this.findById('cust_level_id').getStore();
		store.each(function(record){
			if(record.get('item_value') != 'YBKH'){
				store.remove(record);
			}
		});		
	},
	removeCustType:function(){
		var comp = this.find("hiddenName" ,'cust.cust_type')[0];
		var typeStore = comp.getStore();
		typeStore.each(function(record){
			if(record.get('item_value') == 'UNIT'){
				typeStore.remove(record);
			}
		});
	},
	doCustTypeChange:function(custType){//客户类型修改
		if (this.oldCustType == custType)
			return ;
		this.remove(this.linkPanel,true);
		this.remove(this.extAttrForm,true);
		
		this.linkPanel = new LinkPanel(this);
		if (custType == 'RESIDENT'){
			this.doInitAttrForm(2);
			this.add(this.linkPanel);
			this.add(this.extAttrForm);
		}else{
			this.doInitAttrForm(1);
			var countyId = App.getData().optr.county_id;
			
			this.add(this.linkPanel);
			this.add(this.extAttrForm);
		}
		
		//非居民客户，需要营业执照,税号,协议编号
		if(custType == 'NONRES'){
			Ext.getCmp('addCustItemsOne').add({
					id:'cust_str7_id',
					fieldLabel: langUtils.main("cust.base.businessLicence"),
					xtype:'textfield',
					name:'cust.str7'
				});
			Ext.getCmp('addCustItemsTwo').add({
					id:'cust_str8_id',
					fieldLabel: langUtils.main("cust.base.unitNumber"),
					xtype:'textfield',
					allowBlank: false,
					name:'cust.str8'
				});
			/**
			Ext.getCmp('addCustItemsTwo').add({
					id:'cust_spkg_sn_id',
					fieldLabel:langUtils.main("cust.base.spkgSn"),
					xtype:'textfield',
					name:'cust.spkg_sn'
				})
			**/
		}else{
			var comp = Ext.getCmp('cust_str7_id');
			if(comp){
				Ext.getCmp('addCustItemsOne').remove(comp,true);
				Ext.getCmp('addCustItemsTwo').remove(Ext.getCmp('cust_str8_id'),true);
				//Ext.getCmp('addCustItemsTwo').remove(Ext.getCmp('cust_spkg_sn_id'),true);
			}
		}
		App.form.initComboData( this.linkPanel.findByType("paramcombo"));
		this.doLayout();
		this.oldCustType = custType;
	},
	//默认为新增的扩展信息
	doInitAttrForm: function(group){
		var cfg = [{
	   	   columnWidth: .5,
	   	   layout: 'form',
	   	   baseCls: 'x-plain',
	   	   labelWidth: 75
	    },{
		   columnWidth: .5,
		   layout: 'form',
		   baseCls: 'x-plain',
		   labelWidth: 75
	    }];
		this.extAttrForm = ExtAttrFactory.gExtForm({
			tabName: Constant.C_CUST,
			group: group,
			prefixName : 'cust.'
		}, cfg,this);
	},
	/**
	 * 客户名称修改后，如果名称小于5位，设置联系人名称同客户名相同
	 */
	doCustNameChange:function(c,r,i){
		var name = c.getValue();
		var linkManName = this.find('name','linkman.linkman_name')[0];
		if(this.oldCustType = 'RESIDENT'){
			linkManName.setValue(name);
		}
	},
	doAddressChange : function(obj){
		Ext.getCmp('linkman.mail_address').setValue(obj.getValue());
	},
	doValid:function(){
		if(!Ext.getCmp('linkmanTel').getValue() && !Ext.getCmp('linkmanMobile').getValue()){
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = lmsg("phoneOrFixedPhoneMustBeEnterOne");
			return obj;
		}
		return CustBaseForm.superclass.doValid.call(this);
	},
	generatePwd: function(field, custCertNum){
		//如果没有输入密码，则根据证件号生成
		//证件号后六位数字，如果数字不足6位就前补零补满六位，无证件号默认为'000000'
		var pwdComp = Ext.getCmp('cust_pwd_id');
		//新建开户时生成，修改客户不改变
		if(pwdComp && App.getData().currentResource.busicode == '1001'){
			var pwd = '000000';
			if(custCertNum){
					custCertNum = custCertNum.substring(custCertNum.length-6, custCertNum.length);
					var ss = '';
					for(var i=0,len=custCertNum.length;i<len;i++){
						var s = custCertNum[i];
						if(/[0-9]/.test(s)){
							ss+=s;
						}
					}
					pwd = String.leftPad(ss, 6, '0');
			}
			pwdComp.setValue(pwd);
		}
		
	}
});