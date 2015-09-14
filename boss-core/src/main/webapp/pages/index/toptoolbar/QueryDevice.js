//设备查询框
var QueryDeviceWin = Ext.extend(Ext.Window,{
	deviceTpl:null,
	constructor:function(){
		this.LU_DEV = langUtils.bc('home.tools.queryDevice');
		
		this.deviceTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>' + this.LU_DEV['labelDevType'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_type_text}</td>',
					'<td class="label" width=20%>' + this.LU_DEV['labelDevModel'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_model_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>' + this.LU_DEV['labelModelName'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{model_name}</td>',
				'</tr>',				
				'<tr height=24>',
					'<td class="label" width=20%>' + this.LU_DEV['labelDevCode'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_code}</td>',
					'<td class="label" width=20%>' + this.LU_DEV['labelDeptName'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{depot_id_text}</td>',
			      	
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>{pair_card_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_code}</td>',
			      	'<td class="label" width=20%>{pair_card_type_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_model_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>{pair_modem_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_modem_code}</td>',
			      	'<td class="label" width=20%>{pair_modem_type_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_modem_model_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>' + this.LU_DEV['labelCustNo'] + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{cust_no}</td>',
			      	'<td class="label" width=20%>' + this.LU_DEV['labelCustName'] + '：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{cust_name}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		      		'<td class="label" width=20%>' + this.LU_DEV['labelDevStatus'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{device_status_text}</td>',
		      		'<td class="label" width=20%>' + this.LU_DEV['labelDepStatus'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{depot_status_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		     		'<td class="label" width=20%>' + this.LU_DEV['labelTranStatus'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{tran_status_text}</td>',
		      		'<td class="label" width=20%>' + this.LU_DEV['labelOwnership'] + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{ownership_text}</td>',
		    	'</tr>',
			'</table>'
		);
		QueryDeviceWin.superclass.constructor.call(this,{
			id:'queryDeviceWinId',
			title:this.LU_DEV['_title'],
			closeAction:'close',
			width:550,   
			height:340,
			bodyStyle: "background:#F9F9F9",
			items:[
				{layout:'column',bodyStyle:'padding:15px',
					border:false,items:[
					{columnWidth:.8,layout:'form',defaults:{labelWidth:100},border:false,items:[
						{xtype:'textfield',fieldLabel:this.LU_DEV['labelDevNo'],allowBlank:false,
							name:'device_code',vtype:'alphanum',width:200,
							listeners:{
								scope:this,
								specialkey:function(field,e){
									if(e.getKey() === Ext.EventObject.ENTER){
										this.doQuery();
									}
								}
							}
						}
					]},
					{columnWidth:.2,border:false,items:[
						{xtype:'button',text:langUtils.bc('common.queryBtnWithBackSpace'),iconCls:'icon-query',
							listeners:{
								scope:this,
								click:this.doQuery
							}
						}
					]}
				]},
				{xtype : "panel",title:this.LU_DEV['titleDevInfo'],
					border : false,
					bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
					html : this.deviceTpl.applyTemplate({})
				}
			]
		});
	},
	show:function(){
		QueryDeviceWin.superclass.show.call(this);
		this.find('name','device_code')[0].focus(100,true);
	},
	doQuery:function(){
		var deviceCodeComp = this.find('name','device_code')[0];
		var value = deviceCodeComp.getValue();
		if(Ext.isEmpty(value))return;
		Ext.Ajax.request({
			url:root+'/commons/x/QueryDevice!queryDeviceInfoByCode.action',
			params:{deviceCode:value},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data){
					if(this.items.itemAt(1).getEl()){
						if(data.device_type == 'STB'){
							data['pair_card_label'] = this.LU_DEV['labelCardNo'];//'配对卡编号';
							data['pair_card_type_label'] = this.LU_DEV['labelCardModel'];//'配对卡型号';
							data['pair_modem_label'] = this.LU_DEV['labelModemNo'];//'配对MODEM编号';
							data['pair_modem_type_label'] = this.LU_DEV['labelModemModel'];//'配对MODEM型号';
						}else if(data.device_type == 'CARD'){
							data['pair_card_label'] = this.LU_DEV['labelCardNo'];//'配对机顶盒编号';
							data['pair_card_type_label'] = this.LU_DEV['labelCardNo'];//'配对机顶盒型号';
							data['pair_modem_label'] = this.LU_DEV['labelModemNo'];//'配对MODEM编号';
							data['pair_modem_type_label'] = this.LU_DEV['labelModemModel'];//'配对MODEM型号';
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}else {
							data['pair_card_label'] = this.LU_DEV['labelCardNo'];//'配对卡编号';
							data['pair_card_type_label'] = this.LU_DEV['labelCardModel'];//'配对卡型号';
							data['pair_modem_label'] = this.LU_DEV['labelStbNo'];//'配对机顶盒编号';
							data['pair_modem_type_label'] = this.LU_DEV['labelStbModel'];//'配对机顶盒型号';
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}
						this.deviceTpl.overwrite( this.items.itemAt(1).body, data);
					}
				}else{
					Alert(this.LU_DEV['tipDevNotExists'],function(){
						if(this.items.itemAt(1).getEl()){
							this.deviceTpl.overwrite( this.items.itemAt(1).body, {});
						}
						deviceCodeComp.reset();
						deviceCodeComp.focus(true,100);
					},this);
				}
			}
		});
	}
});