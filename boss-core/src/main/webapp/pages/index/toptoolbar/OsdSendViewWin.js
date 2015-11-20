/**
 * osd催缴
 */
OsdSendPanel = Ext.extend(Ext.FormPanel, {
    constructor: function () {
        OsdSendPanel.superclass.constructor.call(this, {
            id: 'OsdSendPanelId',
            region: 'center',
            layout: 'column',
            border: false,
            fileUpload: true,
            bodyStyle:'padding-top:10px',
            defaults: {
				layout: 'form',
				border: false,
				columnWidth : 0.5,
				baseCls: 'x-plain'				
			},
			items:[{
					columnWidth : 0.3,
					items:[{
	                        xtype: 'datefield',
	                        fieldLabel: lbc('home.tools.osdSend.forms.begin_date'),
	                        format: 'Y-m-d',
	                        allowBlank: false,
	                        editable: false,
	                        name: 'begin_date'
	                    }]
					
				},{
					columnWidth : 0.7,
					items:[{
	                        xtype: 'datefield',
	                        fieldLabel:  lbc('home.tools.osdSend.forms.end_date'),
	                        format: 'Y-m-d',
	                        allowBlank: false,
	                        editable: false,
	                        name: 'end_date'
	                    }]
					
				},{
					columnWidth:0.6,
					items:[{
	                        xtype: 'textfield',
	                        anchor:'96%',
	                        fieldLabel: lbc('home.tools.osdSend.forms.detail_time'),
	                        allowBlank: false,
	                        name: 'detail_time'
	                    }]
					
				},{
					labelWidth :1,
					columnWidth:0.4,
					items:[{
			                xtype: 'displayfield',
			               	anchor:'96%',
			                value:"<font style='font-size:10px;color:red'>"+lbc('home.tools.osdSend.msg.detail_time_info')+"</font>"
						}]
					
				},{
					columnWidth : 1,
					items:[{
						fieldLabel: lbc('home.tools.osdSend.forms.osd_call_type'),
						xtype:'paramcombo',
						allowBlank:false,
						anchor:'50%',
						hiddenName:'osd_call_type',
						paramName:'OSD_CALL_TYPE',
						defaultValue:'ROLL'
					}]
				},{
					columnWidth:0.5,
                    items: [{
                        fieldLabel: lbc('home.tools.osdSend.forms.send_title'),
                        name: 'send_title',
                        xtype: 'textfield',
                        anchor:'98%'
                    },{
						fieldLabel: lbc('home.tools.osdSend.forms.template'),
						xtype:'paramcombo',
						allowBlank:false,
						anchor:'98%',
						isContrary:true,
						hiddenName:'template',
						paramName:'OSD_CALL_CONTENT',
//						defaultValue:'RESIDENT',
						listeners:{
							scope: this,
							'select': function(combo,rec){
								Ext.getCmp('messageOsd').setValue(combo.getValue());
							}
						}
					}]
                },{
                	columnWidth:0.5,
                    items: [{
                        fieldLabel: lbc('home.tools.osdSend.forms.send_optr'), 
                        name: 'send_optr',
                        xtype: 'textfield',
                        anchor:'90%'
                    },{
                	id:'dataOsdFileId',xtype:'textfield',fieldLabel: lbc('home.tools.osdSend.forms.file') ,inputType:'file',
                	allowBlank:false,anchor:'95%',name:'files'}]
                },{
	                columnWidth: 1,
                    items: [{
                        fieldLabel: lbc('home.tools.osdSend.forms.message'), 
                        xtype: 'textarea',
                        id: 'messageOsd',
                        anchor:'96%',
                        allowBlank: false,
                        height: 150,
                        readOnly:true,
                        name: 'message'
               		 }]
				}]
        });

    },
    initComponent : function() {
		OsdSendPanel.superclass.initComponent.call(this);
		// 初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes, this, this);
	},
    removeRes: function () {
        Ext.getCmp('cmdTypeId').setValue('');
    },
    getSelections:function(){
    	
    
    },
    checkTxtFileType : function(){
    	var file = Ext.getCmp('dataOsdFileId').getValue();
		if(file.lastIndexOf('txt') >0 || file.lastIndexOf('.txt')==file.length-4){
			return 'TXT';
		}		
		Alert(lbc('home.tools.osdSend.msg.dataMustTxt'));
		return false;
	}
});

OsdEndPanel = Ext.extend(Ext.FormPanel, {
    constructor: function () {
        OsdEndPanel.superclass.constructor.call(this, {
        	id: 'OsdEndPanelId',
            region: 'center',
            border: false,
            bodyStyle:'padding-top:10px',
            baseCls: 'x-plain',
            items:[{
                fieldLabel: 'cs',
                name: 'cs',
                allowBlank: false,
                xtype: 'textfield',
                anchor:'90%'
            }]
           
        })
    },
    getSelections:function(){
    
    
    }
})
        

OsdTabInfo = Ext.extend(Ext.TabPanel,{
	osdSendPanel : null,
	osdEndPanel:null,
	constructor : function() {
		this.osdSendPanel = new OsdSendPanel();
		this.osdEndPanel = new OsdEndPanel();
		OsdTabInfo.superclass.constructor.call(this, {
				activeTab: 0,
				closable : true,
				defaults : {border: false,layout : 'fit'},
				items:[
					{title : lbc('home.tools.osdSend.forms.sendOSD'),items:[this.osdSendPanel]},
					{title : lbc('home.tools.osdSend.forms.cancleOSD'),items:[this.osdEndPanel]}
				]
		})
	},
	getSelections:function(){
		var panelId = this.getPanelId();
		if(panelId === "OsdSendPanelId"){
			return this.osdSendPanel.getSelections();
		}else{
			return this.osdEndPanel.getSelections();
		}
	},
	getPanelId:function(){
		return this.getActiveTab().items.itemAt(0).getId();
	}
})
OsdSendViewWin = Ext.extend(Ext.Window,{
//	osdTabInfo:null,
	osdSendPanel : null,
	constructor:function(){
		this.osdSendPanel = new OsdSendPanel();
//		this.osdTabInfo = new OsdTabInfo();
		OsdSendViewWin.superclass.constructor.call(this,{
			id:'OsdSendViewWinId',
			layout : 'fit',
			title : lbc('home.tools.osdSend._title'),
			border : false ,
			closeAction : 'hide',
			width:800,
			height : 450,
			items : [this.osdSendPanel],
			buttons : [{text : lbc('common.save'),scope : this,handler : function() {this.doSave();}},
				{text : lbc('common.close'),scope : this,handler : function() {this.close();}}]
		});
	},
    doSave: function () {
//    	var panelId = this.osdTabInfo.getPanelId();
//    	if(panelId == 'OsdSendPanelId'){
//    		var sendForm = this.osdTabInfo.osdSendPanel.getForm();
    	var sendForm = this.osdSendPanel;
    		if(sendForm.getForm().isValid()){
				var flag = sendForm.checkTxtFileType();
				if(flag === false)return;
				
				function callbackSave(rec) {
					var msg = Show();
					var txt =lbc('home.tools.osdSend.msg.confirmSendOsd',null,[rec[0],rec[1]]);
					Confirm(txt, this, function() {
						sendForm.getForm().submit({
							url:Constant.ROOT_PATH + '/commons/x/QueryParam!saveOsd.action',
							scope:this,
							success:function(form,action){
								msg.hide();
								var data = action.result;
								if(data.success == true){
									if(data.msg){//错误信息
										Alert(data.msg);
									}else{
										Alert(lbc('home.tools.osdSend.msg.actionSuccess'),function(){
											Ext.getCmp('OsdSendViewWinId').close();
										},this);
									}
								}
							},  
							failure : function(form, action) {  
								Alert(lbc('home.tools.osdSend.msg.actionFailed'));  
							}
						});
					});
				}
				
				var msg = Show();
				sendForm.getForm().submit({
					url:Constant.ROOT_PATH + '/commons/x/QueryParam!queryCanToSendOsd.action',
					scope:this,
					success:function(form,action){
						msg.hide();
						var data = action.result;
						if(data.success == true){
							if(data.msg){//错误信息
								Alert(data.msg);
							}else{
								callbackSave(data.records);
							}
						}
					}
				});			
			}
    		
//    	}else if(panelId == 'OsdEndPanelId'){
//    	
//    	
//    		
//    	}else{
//    		return;
//    	}
       
    }
});