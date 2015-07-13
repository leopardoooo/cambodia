/**
 *修改用户资料 
 */

EditUserForm = Ext.extend( UserBaseForm , {
	record : null,
	url : null,
	doFilterTerminal : function(){},//重写过滤终端的方法，
	doInitAttrForm: function(group){
		var cfg = [{
	   	   columnWidth: .5,
	   	   layout: 'form',
	   	   baseCls: 'x-plain',
	   	   labelWidth: 90
	    },{
		   columnWidth: .5,
		   layout: 'form',
		   baseCls: 'x-plain',
		   labelWidth: 90
	    }];
	    
	    var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
	    
	    //扩展信息面板
		this.extAttrForm = ExtAttrFactory.gExtEditForm({
			tabName: Constant.C_USER,
			pkValue: record.get('user_id'),
			group: group,
			prefixName : ''
		}, cfg,function(){
			this.doExtAttrFunc.defer(100,this);
		},this); 
	},
	doInit:function(){
		EditUserForm.superclass.doInit.call(this);
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		this.changeSubFrom(record.get('user_type'));
		this.record = record;
		
		this.getForm().loadRecord(this.record);
		var expDate = this.record.get('user_class_exp_date');
		if(expDate){
			Ext.getCmp('userClassExpDate').originalValue = Date.parseDate(expDate,'Y-m-d h:i:s');
			Ext.getCmp('userClassExpDate').setValue(Date.parseDate(expDate,'Y-m-d h:i:s'));
		}
		
		
		if(record.get('user_type') == 'BAND'){
			Ext.getCmp('check_type_id').fireEvent('select',Ext.getCmp('check_type_id'));
			Ext.getCmp('net_type_id').fireEvent('select',Ext.getCmp('net_type_id'));
		}
		
		Ext.getCmp('comboForUserBaseForm').setDisabled(true);
		this.setCanUpdateField();
	},
	setCanUpdateField:function(){
		//查找可以修改的字段，将不可修改的字段设置为disabled
		Ext.Ajax.request({
			scope:this,
			url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryCanUpdateCustField.action",
			params:{busiCode:App.getData().currentResource.busicode},
			success:function(response,opts){
				this.getForm().loadRecord(this.record);
				
				this.getForm().items.each(function(f){
					Ext.fly(f.label.id).setStyle('color','gray');
					f.disable();
				});
				
				var fields = Ext.decode(response.responseText);
				if(fields){
					for (var i=0;i<fields.length;i++){
						var field = this.find("name",fields[i].field_name)[0]||this.find("hiddenName",fields[i].field_name)[0];
						if (field){
							field.enable() ;
							Ext.fly(field.label.id).setStyle({
								'font-weight':'bold',
								'color' : 'black'
								});
						}
					}
				}
				
				//如果允许修改密码，激活密码确认输入框
				var password = Ext.getCmp('password');
				if(password && !password.disabled){
					//重置原密码
					password.reset();
					password.allowBlank = false;
					var repassword = Ext.getCmp('repassword');
					repassword.enable();
					repassword.allowBlank = false;
					Ext.fly(repassword.label.id).setStyle({
						'font-weight':'bold',
						'color' : 'black'
					});
				}
				
				//如果是第二终端转副机业务
				if(App.getApp().getData().currentResource.busicode == '1038'){
					this.addEzdToFzdPanel();
				}
			}
		});
	},
	//添加第二终端转副机面板
	addEzdToFzdPanel : function(){
		//终端类型下拉框
		var terminal = Ext.getCmp('terminal_type_id');
		var terStore = terminal.getStore();
		terStore.removeAt(terStore.find('item_value','ZZD'));
		terStore.removeAt(terStore.find('item_value','EZD'));
		terminal.setValue('FZD');
		
		var userPanel = App.getApp().main.infoPanel.getUserPanel();
		var userId = userPanel.userGrid.getSelectedUserIds()[0];
		var records = userPanel.prodGrid.prodMap[userId];
		if(records){//存在产品
			//产品资费
			var baseParams ={};
			baseParams['custId'] = App.getApp().getCustId();
			baseParams['userId'] = userId;
			
			var prodName = '';
			var prodSn = '';
			for(var i=0;i<records.length;i++){
				if(records[i]['is_base'] == 'T'){
					prodName = records[i].prod_name;
					prodSn = records[i]['prod_sn'];
					baseParams['prodId'] = records[i]['prod_id'];
					baseParams['tariffId'] = records[i]['tariff_id'];
					break;
				}
			}
			
			
			
			
			
			var prodTariffStore = new Ext.data.JsonStore({
				autoLoad : true,
				baseParams:baseParams,
				url : Constant.ROOT_PATH+"/core/x/Prod!queryTariffForEzdToFzd.action",
				fields : ['tariff_id','tariff_name','tariff_desc',
				{name : 'rent',type : 'float'}]
			});
			
			var panel = new Ext.Panel({
				id : 'EzdToFzdPanel',
				title : prodName + '的新资费',
				border : false,
				layout : 'form',
				bodyStyle : Constant.TAB_STYLE,
				labelWidth : 75,
				items : [{
					xtype : 'hidden',
					id : 'prodSn',
					value : prodSn
				},{
					xtype : 'combo',
					width : 150,
					store : prodTariffStore,
					fieldLabel : '新资费',
					emptyText: '请选择',
					allowBlank : false,
					mode: 'local',
					hiddenName : 'tariff_id',
					hiddenValue : 'tariff_id',
					valueField : 'tariff_id',
					displayField : "tariff_name",
					forceSelection : true,
					triggerAction : "all",
					editable : false,
					listeners:{
						scope : this,
						'select' : this.doSelectProdTariff
					}
				}]
			});
			
			var item = this.items.itemAt(1).items;
			item.insert(2,panel);
			this.doLayout();
		}
	},
	doSelectProdTariff : function(combo,rec){
		var panel = Ext.getCmp('EzdToFzdPanel');
		
		if(panel.items.itemAt(2)){
			panel.remove(panel.items.itemAt(2),true);
		}
		
		if(rec.get('rent') <= 0){
			panel.add({
				xtype : 'datefield',
				width : 150,
				fieldLabel : '截止日期',
				minValue : nowDate().format('Y-m-d'),
				format : 'Y-m-d',
				allowBlank : false,
				id : 'prodexpdate'
			});
		}
		
		panel.doLayout();
	},
	doValid : function(){
		//第二终端转副机
		if(Ext.getCmp('prodSn')){
			this.url = Constant.ROOT_PATH+"/core/x/User!ezdTofzd.action";
		}else{
			this.url = Constant.ROOT_PATH+"/core/x/User!editUser.action";
		}
		
		if(!Ext.isEmpty(Ext.getCmp('serv_type_id'))){
			this.interactiveType = Ext.getCmp('serv_type_id').getValue();
		}
		return EditUserForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var all = {};
		var vs = this.getForm().getChangeValues();
		var arr = [];
		for(var i=0;i<vs.length;i++){
			if(vs[i].columnName.indexOf(CoreConstant.Ext_FIELDNAME_PREFIX) === 0){
				all[vs[i].columnName] = vs[i].newValue;
			}else{
				arr.push(vs[i]);
			}
		}
		
		
		if(Ext.getCmp('prodSn')){
			arr.push({columnName: 'terminal_type',newValue : 'FZD',oldValue : 'EZD'});
			
			var tariffId = this.find('hiddenName','tariff_id')[0];
			all['prodSn'] = Ext.getCmp('prodSn').getValue();
			all['newTariffId'] = tariffId.getValue();
			
			if(Ext.getCmp('prodexpdate')){
				all['expDate'] = Ext.getCmp('prodexpdate').getValue().format("Y-m-d");
			}
		}
		
		Ext.apply( all , {
			'userChangeInfo':Ext.encode(arr)
		})
		
		return all;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

//Ext.onReady(function(){
//	var nuf = new EditUserForm();
//	var box = TemplateFactory.gTemplate(nuf);
//});

