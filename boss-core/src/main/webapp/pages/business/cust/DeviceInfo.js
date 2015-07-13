var deviceInfoHTML=
'<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
	'<tr height=24>'+
		'<td class="label"  width="20%" >设备号：</td>' +
		'<td class="input">&nbsp;{[values.device_code || ""]}</td>'+
		'<tpl if="this.isNull(values.pairCard)">' +
			'<td class="label"  width="20%" >配对设备号：</td>' +
		'<td class="input">&nbsp;{[values.pairCard.card_id || ""]}</td>'+
		'</tpl>' +
	'</tr>' + 
	'<tr height=24>'+
		'<td class="label" >设备类型：</td>' +
		'<td class="input">&nbsp;{[values.device_type_text || ""]}</td>'+
		'<tpl if="this.isNull(values.pairCard)">' +
			'<td class="label"  width="20%" >设备类型：</td>' +
			'<td class="input">&nbsp;{["智能卡"]}</td>'+
		'</tpl>' +
	'</tr>' +
	'<tr height=24>'+
		'<td class="label" >设备型号：</td>' +
		'<td class="input">&nbsp;{[values.deviceModel.device_model_text || ""]}</td>'+
		'<tpl if="this.isNull(values.pairCard)">' +
			'<td class="label" >设备型号：</td>' +
			'<td class="input">&nbsp;{[values.pairCard.device_model_text || ""]}</td>'+
		'</tpl>' +
	'</tr>' +
	'<tr height=24>'+
		'<td class="label" >设备状态：</td>' +
		'<td class="input">&nbsp;{[values.device_status_text || ""]}</td>'+
	'</tr>' +
'</table>';

/**
 * 设备信息面板构造
 */
DeviceInfo = Ext.extend( Ext.Panel , {
	border : false,
	constructor: function(title,html){
		DeviceInfo.superclass.constructor.call(this, {
			title : title,
			border: false,
			bodyStyle: Constant.TAB_STYLE,
			html: html
		});
	}
});

DeviceSelectForm = Ext.extend(Ext.Panel , {
	store:null,
	constructor: function(){	
		this.store = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryDevice!queryReclaimDevice.action",
			fields:[
				"device_id",
				"device_code",
				"device_type_text",
				"device_type",
				"ownership_text",
				"ownership",
				"buy_mode_text"
			]
		});
		var a = App.getApp().main.infoPanel.getCustPanel().custDeviceGrid.getSelectionModel().getSelections();
		this.store.load({params : {deviceCode :  a[0].get("device_code") }});	
		this.store.on("load", processResult ,this.store);
		function processResult(_store, _rs, ops){
			var tpl = new Ext.XTemplate(deviceInfoHTML,{
			     isNull: function(str){
			     	 if (str != null && str !="") {
								return str;
							} else {
								return false;
							}
			     }}
			);
			tpl.compile();
			var ppp = Ext.getCmp('deviceForm').items.itemAt(0);
				var data = _store.reader.jsonData.simpleObj;
				var panel = new DeviceInfo('设备信息',tpl.applyTemplate(data));			
				ppp.removeAll();
				ppp.add(panel);
			    ppp.doLayout();
		};
		DeviceSelectForm.superclass.constructor.call(this, {
			border: false,
			id:'deviceForm',
			layout: 'anchor',
			anchor: '100%',
			defaults: {
					bodyStyle: "background:#F9F9F9"
				},
			items : [{
				xtype : "panel",
				region: 'center',
				border : false				
			}]
		});	
	}
	
});
		

		