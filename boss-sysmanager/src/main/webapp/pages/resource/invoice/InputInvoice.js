
CheckInInvoiceForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		CheckInInvoiceForm.superclass.constructor.call(this,{
			id:'checkInInvoiceFormId',
			layout:'column',
			border:false,
			labelWidth: 120,
			labelAlign:'left',
			trackResetOnLoad:true,
			bodyStyle:'padding-top:10px;padding-left:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[
				{columnWidth:.4,layout:'form',defaultType:'textfield',items:[
					{id: 'invoidType_checkIn',fieldLabel:'&nbsp;&nbsp;&nbsp;发票类型',hiddenName:'invoiceDto.invoice_type',xtype:'paramcombo',
						paramName:'INVOICE_TYPE',defaultValue:'2',listeners:{
							scope:this,
							select:this.setAmountDisable
						}},
					{fieldLabel:'发票代码',xtype:'textfield',name:'invoiceDto.invoice_code',allowBlank:false},
					{fieldLabel:'开始发票号',name:'invoiceDto.start_invoice_id',allowBlank:false,
						maxLength:8,enableKeyEvents:true,vtype:'invoiceId',
						listeners:{
							scope:this,
							keyup:this.showInvoiceDetail
						}},
					{fieldLabel:'结束发票号',name:'invoiceDto.end_invoice_id',allowBlank:false,
						maxLength:8,enableKeyEvents:true,vtype:'invoiceId',
						listeners:{
							scope:this,
							keyup:this.showInvoiceDetail
						}}
					
				]}
				,{id:'invoiceLabelStatisId',columnWidth:.6,layout:'form',
					defaultType:'displayfield',labelWidth:65,
//					defaults:{
//						labelStyle : "text-align:right;",style:Constant.TEXTFIELD_STYLE},
					items:[
						{height:20},
						{/*fieldLabel:'发票共',*/id:'invoiceStatisId',labelSeparator:''}
					]
				}
			]
		});
	},
	initComponent:function(){
		CheckInInvoiceForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'),this.doInit);
	},
	doInit : function(){
		Ext.getCmp('invoiceLabelStatisId').hide();
	},
	buling:function(value){
		while(value.length<8){
			value = "0".concat(value);	
		}
		return value;
	},setAmountDisable:function(v){
		var form = this.getForm();
		var comp = form.findField('invoiceDto.invoice_amount');
		if(v.value=='100'){
			comp.allowBlank=false;
			comp.minValue=1;
			comp.enable();
		}else{
			comp.allowBlank=true;
			comp.minValue=0;
			comp.disable();
		}
 	},
	showInvoiceDetail:function(){
		var form = this.getForm();
		if(form.isValid()){
			
			var startInvoiceId = form.findField('invoiceDto.start_invoice_id').getValue();//开始发票号
			var endInvoiceId = form.findField('invoiceDto.end_invoice_id').getValue();//结束发票号
			if (startInvoiceId.length!=endInvoiceId.length){
				Ext.getCmp('invoiceStatisId').setValue("发票开始号和发票结束号长度不一致");
				return;
			}
			//不足8位，前面补零
//			if(startInvoiceId.length <8)startInvoiceId = this.buling(startInvoiceId);
//			if(endInvoiceId.length <8)endInvoiceId = this.buling(endInvoiceId);
			
			var comp = Ext.getCmp('invoiceLabelStatisId');
			if(!Ext.isEmpty(startInvoiceId) && !Ext.isEmpty(endInvoiceId) 
				&& parseInt(startInvoiceId,10) <= parseInt(endInvoiceId,10)){
					var intStart = parseInt(startInvoiceId,10);
					var intEnd = parseInt(endInvoiceId,10);
					
					if(comp.hidden){
						comp.show();	
					}
					Ext.getCmp('invoiceStatisId').setValue('发票共 '+(intEnd - intStart + 1)+" 张");
				}else{
					if(!comp.hidden)
						comp.hide();
				}
		}
	},
	transferZero:function(count){
		var o="";
		for(var i=0;i<count;i++){
			o = o.concat("0");
		}
		return o;
	},
	getValues:function(){
		var values = this.getForm().getValues();
		var obj = {};
		for(var i in values){
			if(i.indexOf('invoiceDto') == 0){
				obj[i]=values[i];
			}
		}
		return obj;
	}
});

var CheckInPanel = Ext.extend(Ext.Panel,{
	checkInInvoiceForm:null,
	constructor:function(){
		this.checkInInvoiceForm = new CheckInInvoiceForm();
		CheckInPanel.superclass.constructor.call(this,{
			id:'checkInWinId',
			width:560,
			height:210,
			title:'发票入库',
			region:'center',
			items:[this.checkInInvoiceForm],
			buttonAlign:'center',
			buttons:[
				{text:'录入',scope:this,handler:this.doSave}
			]
		});
	},
	doSave:function(){
	  if(!this.checkInInvoiceForm.getForm().isValid())return;
		var form = this.checkInInvoiceForm.getForm();
		var startInvoiceId = form.findField('invoiceDto.start_invoice_id').getValue();//开始发票号
		var endInvoiceId = form.findField('invoiceDto.end_invoice_id').getValue();//结束发票号
		if (startInvoiceId.length!=endInvoiceId.length){
			Ext.getCmp('invoiceStatisId').setValue("发票开始号和发票结束号长度不一致");
			return;
		}
		var values = this.checkInInvoiceForm.getValues();
		var startInvoiceId = values['invoiceDto.start_invoice_id'];
		var endInvoiceId = values['invoiceDto.end_invoice_id'];
		values['invoiceDto.invoice_amount'] = Ext.util.Format.formatToFen(values['invoiceDto.invoice_amount']);
		var comp = Ext.getCmp('invoiceLabelStatisId');
		if(comp.hidden && parseInt(startInvoiceId,10) > parseInt(endInvoiceId,10)){
			Alert('请正确输入发票号!');
			return;
		}
		var mb = Show();
		Ext.Ajax.request({
			url : 'resource/Invoice!saveInvoiceInput.action',
			params : values,
			scope:this,
			timeout:9999999999999,
			success : function(res, opt) {
				mb.hide();
				if (true === Ext.decode(res.responseText).success){
					Alert('入库成功!',function(){
						this.checkInInvoiceForm.getForm().reset();
					},this);
				}else
					Alert('入库失败!');
			}
		});
	}
});

InputInvoice = Ext.extend(Ext.Panel,{
	checkInPanel:null,
	constructor:function(){
		this.checkInPanel = new CheckInPanel();
		InputInvoice.superclass.constructor.call(this,{
			id:'InputInvoice',
			title:'发票入库',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[this.checkInPanel]
		});
	}
});


