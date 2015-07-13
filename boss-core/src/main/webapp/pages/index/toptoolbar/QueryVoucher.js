//设备查询框
var QueryVoucherWin = Ext.extend(Ext.Window,{
	voucherTpl:null,
	constructor:function(){
		this.voucherTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>面额：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.voucher_value)]}</td>',
					'<td class="label" width=20%>状态：</td>',
					'<td class="input_bold" width=30%>&nbsp;{status_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>使用日期：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[fm.dateFormat(values.used_time)]}</td>',
			      	'<td class="label" width=20%>使用金额：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.used_money)]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>未使用金额：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[fm.formatFee(values.unused_money)]}</td>',
			      	'<td class="label" width=20%>生成日期：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{[fm.dateFormat(values.create_time)]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>失效日期：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[fm.dateFormat(values.invalid_time)]}</td>',
			      	'<td class="label" width=20%>是否领用：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{is_procured_text}</td>',
		    	'</tr>',
			'</table>'
		);
		QueryVoucherWin.superclass.constructor.call(this,{
			id:'queryVoucherWinId',
			border:false,
			title:'代金券查询',
			closeAction:'close',
			width:500,
			height:250,
			bodyStyle: "background:#F9F9F9",
			items:[
				{layout:'column',bodyStyle:'padding:10px',
					border:false,items:[
					{columnWidth:.8,layout:'form',defaults:{labelWidth:100},border:false,items:[
						{xtype:'numberfield',fieldLabel:'代金券号',allowBlank:false,
							name:'voucher_id',width:200,
							maxLength:11,minLength:11,
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
				{xtype : "panel",title:'代金券信息',
					border : false,
					bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
					html : this.voucherTpl.applyTemplate({})
				}
			]
		});
	},
	show:function(){
		QueryVoucherWin.superclass.show.call(this);
		this.find('name','voucher_id')[0].focus(100,true);
	},
	doQuery:function(){
		var voucherIdComp = this.find('name','voucher_id')[0];
		var value = voucherIdComp.getValue();
		if(Ext.isEmpty(value) || value.toString().length != 11)return;
		Ext.Ajax.request({
			url:root+'/system/x/Index!queryVoucherById.action',
			params:{voucherId:value},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data){
					if(this.items.itemAt(1).getEl()){
						this.voucherTpl.overwrite( this.items.itemAt(1).body, data);
					}
				}else{
					Alert('查询代金券不存在！',function(){
						if(this.items.itemAt(1).getEl()){
							this.voucherTpl.overwrite( this.items.itemAt(1).body, {});
						}
						voucherIdComp.reset();
						voucherIdComp.focus(true,100);
					},this);
				}
			}
		});
	}
});