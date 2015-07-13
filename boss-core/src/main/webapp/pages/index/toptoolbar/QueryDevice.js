//设备查询框
var QueryDeviceWin = Ext.extend(Ext.Window,{
	deviceTpl:null,
	constructor:function(){
		this.deviceTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>设备类型：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_type_text}</td>',
					'<td class="label" width=20%>设备型号：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_model_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>型号名称：</td>',
					'<td class="input_bold" width=30%>&nbsp;{model_name}</td>',
				'</tr>',				
				'<tr height=24>',
					'<td class="label" width=20%>设备编号：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_code}</td>',
					'<td class="label" width=20%>所在仓库：</td>',
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
					'<td class="label" width=20%>客户编号：</td>',
					'<td class="input_bold" width=30%>&nbsp;{cust_no}</td>',
			      	'<td class="label" width=20%>客户姓名：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{cust_name}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		      		'<td class="label" width=20%>设备状态：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{device_status_text}</td>',
		      		'<td class="label" width=20%>库存状态：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{depot_status_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		     		'<td class="label" width=20%>流转状态：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{tran_status_text}</td>',
		      		'<td class="label" width=20%>产    权：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{ownership_text}</td>',
		    	'</tr>',
			'</table>'
		);
		QueryDeviceWin.superclass.constructor.call(this,{
			id:'queryDeviceWinId',
			title:'设备查询',
			closeAction:'close',
			width:550,
			height:340,
			bodyStyle: "background:#F9F9F9",
			items:[
				{layout:'column',bodyStyle:'padding:15px',
					border:false,items:[
					{columnWidth:.8,layout:'form',defaults:{labelWidth:100},border:false,items:[
						{xtype:'textfield',fieldLabel:'设备序列号',allowBlank:false,
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
						{xtype:'button',text:'查  询',iconCls:'icon-query',
							listeners:{
								scope:this,
								click:this.doQuery
							}
						}
					]}
				]},
				{xtype : "panel",title:'设备信息',
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
							data['pair_card_label'] = '配对卡编号';
							data['pair_card_type_label'] = '配对卡型号';
							data['pair_modem_label'] = '配对MODEM编号';
							data['pair_modem_type_label'] = '配对MODEM型号';
						}else if(data.device_type == 'CARD'){
							data['pair_card_label'] = '配对机顶盒编号';
							data['pair_card_type_label'] = '配对机顶盒型号';
							data['pair_modem_label'] = '配对MODEM编号';
							data['pair_modem_type_label'] = '配对MODEM型号';
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}else {
							data['pair_card_label'] = '配对卡编号';
							data['pair_card_type_label'] = '配对卡型号';
							data['pair_modem_label'] = '配对机顶盒编号';
							data['pair_modem_type_label'] = '配对机顶盒型号';
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}
						this.deviceTpl.overwrite( this.items.itemAt(1).body, data);
					}
				}else{
					Alert('查询设备不存在！',function(){
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