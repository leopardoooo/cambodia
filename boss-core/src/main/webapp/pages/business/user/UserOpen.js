/**
 * 报开（如有需要分配设备，显示对应输入框）
 */
UserOpenForm = Ext.extend(Ext.Panel, {	
	oweFee:null,
	parent : null,
	cfg:["0","0","0"],
	constructor : function(p) {
		this.parent = p;
		var userRecords = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
		
		var items = [];
		if(userRecords[0].get('is_rstop_fee') == 'T'){
			items.push({
				xtype : 'textfield',
				fieldLabel : '往月欠费(元)',
				width : 100,
				id:'oweFee',
				name : 'oweFee',
				style : Constant.TEXTFIELD_STYLE,
				readOnly : true
			},{
				xtype : 'textfield',
				fieldLabel : '缴费金额(元)',
				width : 100,
				id : 'fee',
				name : 'fee',
				style : Constant.TEXTFIELD_STYLE,
				readOnly : true
			});
		}
		
		UserOpenForm.superclass.constructor.call(this, {
			title : '业务受理',
			region : 'center',
			layout : 'form',
			bodyStyle : Constant.TAB_STYLE,
			border : false,
			items : items
		});
	},
	initComponent : function() {
		UserOpenForm.superclass.initComponent.call(this);
		this.doInit();
		
		var custId = App.getApp().data.custFullInfo.cust.cust_id;
		Ext.Ajax.request({
			scope : this,
			url: root + '/commons/x/QueryCust!getOweFee.action?custId='+custId,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				this.oweFee=rs.simpleObj.owe_fee;
				if(Ext.getCmp('oweFee')){
					Ext.getCmp('oweFee').setValue(Ext.util.Format.formatFee(this.oweFee));
					Ext.getCmp('fee').setValue(Ext.util.Format.formatFee(this.oweFee));
				}
			}
		});
	},
	doInit : function(){
		//查找能用的设备
		var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();//客户设备面板store
		var device = cdStore.query('status','IDLE');
		var stbArr=[],cardArr=[],modemArr=[];
		device.each(function(item,index,length){
			var data = item.data;
			if(data['status'] == 'IDLE' && data['loss_reg'] == 'F'){
				if(data['device_type'] == 'STB'){
					stbArr.push([data['device_code']]);
				}else if(data['device_type'] == 'CARD'){
					cardArr.push([data['device_code']]);
				}else if(data['device_type'] == 'MODEM'){
					modemArr.push([data['device_code']]);
				}
			}
		});
		
		var stbStore = new Ext.data.ArrayStore({
			fields:['device_code'],
			data : stbArr
		});
		var cardStore = new Ext.data.ArrayStore({
			fields:['device_code'],
			data : cardArr
		});
		var modemStore = new Ext.data.ArrayStore({
			fields:['device_code'],
			data : modemArr
		});
		
		var stbField = new Ext.form.ComboBox({// 显示items后的stb
			fieldLabel : '机顶盒号',
			id:'stbid_id',
			name : 'stb_id',
			width:170,
			editable:true,
			store : stbStore,
			displayField:'device_code',valueField:'device_code',
			listeners : {
				'change' : function(field){
					field.fireEvent('select',field);
				},
				select:function(field){App.getApp().checkDevice(field,'STB',Ext.getCmp('cardid_id'));}
			}
		});
		var cardField = new Ext.form.ComboBox({// 显示items后的card
			fieldLabel : '智能卡号',	
			id:'cardid_id',
			name : 'card_id',
			width:170,
			allowBlank:true,editable:true,
			store:cardStore,
			displayField:'device_code',valueField:'device_code',
			listeners : {
				'change' : function(field){
					field.fireEvent('select',field);
				},
				select:function(field){App.getApp().checkDevice(field,'CARD')}
			}				
		});
		var modemField = new Ext.form.ComboBox({// 显示items后的modem_mac
			fieldLabel : '猫号',	
			id:'modemmac_id',
			name : 'modem_mac',
			width:150,
			allowBlank : false,editable:true,
			store : modemStore,
			displayField:'device_code',valueField:'device_code',
			listeners : {
				'change' : function(field){
					field.fireEvent('select',field);
				},
				select:function(field){App.getApp().checkDevice(field,'MODEM')}
			}
		}); 
		
		//用户选中记录
		var recs = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
		for (var i = 0; i < recs.length; i++) {										
			
			
			userType = recs[i].get("user_type"); //数字DTV，宽带BAND ，模拟ATV
			servType = recs[i].get("serv_type"); //单向SINGLE，双向用户DOUBLE								
			stbId  = recs[i].get("stb_id");
			cardId = recs[i].get('card_id');
			modemMac = recs[i].get("modem_mac"); 
			var netData = App.getApp().findAllBandNetType();
			
			if(userType=="BAND"){
				if(Ext.isEmpty(modemMac)){
					for(var k=0;k<netData.length;k++){
						if(recs[i].get('net_type') == netData[k].net_type){
							if(netData[k].need_device == 'T'){
								this.add(modemField);
								this.cfg[2]="1";
							}
							break;
						}
					}
					this.doLayout();
					break;
				}
			}else if(userType=="DTV"){
				if(Ext.isEmpty(stbId)){
					this.add(stbField);
					this.cfg[0]="1";
				}
				
				if(Ext.isEmpty(cardId)){
					this.add(cardField);
					this.cfg[1]="1";
				}
				
				if(servType=="DOUBLE" && Ext.isEmpty(modemMac)){
					for(var k=0;k<netData.length;k++){
						if(recs[i].get('net_type') == netData[k].net_type){
							if(netData[k].need_device == 'T'){
								this.add(modemField);
								this.cfg[2]="1";
							}
							break;
						}
					}
					
				}
				this.doLayout();
			}
		}
	}
});

		
UserOpen = Ext.extend(BaseForm, {
	userInfoPanel : null,
	userOpenForm : null,
	url : Constant.ROOT_PATH + "/core/x/User!openUser.action",
	listeners:{
		scope:this,
		render:function(){
			Ext.getCmp('ywSaveId').hide();//隐藏业务保存按钮
		}
	},
	constructor : function() {
		this.userInfoPanel = new UserInfoPanel();
		this.userOpenForm = new UserOpenForm(this);
		UserOpen.superclass.constructor.call(this, {
					border : false,							
					layout : 'border',
					items : [this.userInfoPanel, this.userOpenForm]
				}
		);
	},
	success : function(form, res) {
		var payFees = App.getData().busiTask['PayFees'];
		if(App.getApp().getCust().cust_type != 'RESIDENT'){
			payFees['callback'] = {
				fn : App.getApp().selectRelativeAcctUser,
				params : App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUserIds()
			};
		}else{
			payFees['callback'] = {
				fn : App.getApp().selectRelativeAcct
			};
		}
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);	
	},
	getValues:function(){
		var values = this.getForm().getValues();
		if(Ext.getCmp('fee')){
			values['specFee']=Ext.util.Format.formatToFen(Ext.getCmp('fee').getValue());
		}
		return values;
	},
	getFee : function(){
		if(Ext.getCmp('fee')){
			var fee = Ext.getCmp('fee').getValue();
			return fee;
		}else{
			return 0 ;
		}
	},
	doValid:function(){

		var stbCombo = Ext.getCmp('stbid_id');
		var cardCombo = Ext.getCmp('cardid_id');
		var modemCombo = Ext.getCmp('modemmac_id');
		if(stbCombo && stbCombo.hasFocus){
			App.getApp().checkDevice(stbCombo,'STB',cardCombo);
			if(stbCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "机顶盒有问题，请在验证后保存";
				return obj;
			}
		}
		if(cardCombo && cardCombo.hasFocus){
			App.getApp().checkCardDevice(cardCombo);
			if(cardCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "智能卡有问题，请在验证后保存";
				return obj;
			}
		}
		if(modemCombo && modemCombo.hasFocus){
			App.getApp().checkDevice(modemCombo,'MODEM');
			if(modemCombo.checked != true){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] = "MODEM有问题，请在验证后保存";
				return obj;
			}
		}
		
		if(Ext.getCmp('fee')){
			var fee = Ext.getCmp('fee').getValue();
			var owefee = Ext.getCmp('oweFee').getValue();
			if(fee<owefee){
				var obj = {};
				obj["isValid"] = false;
				obj["msg"] ="需要补足所欠报停维护费";
				return obj;
			}
		}
		
		if((this.userOpenForm.cfg[0]=="1"&&Ext.isEmpty(stbCombo.getValue()))
			||(this.userOpenForm.cfg[1]=="1"&&Ext.isEmpty(cardCombo.getValue()))
			||(this.userOpenForm.cfg[2]=="1"&&Ext.isEmpty(modemCombo.getValue()))){
			var obj = {};
				obj["isValid"] = true;
				this.confirmMsg = "报开时没有设备归于用户名下，是否保存？";
				this.yesBtn = false;
				this.isCloseBigWin = false;
		   return obj;
		}else{
			this.confirmMsg = null;
		}
		
		return UserOpen.superclass.doValid.call(this);
	}
});		
		
Ext.onReady(function() {
	var tf = new UserOpen();
	var box = TemplateFactory.gTemplate(tf);
});
