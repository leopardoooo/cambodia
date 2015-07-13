/**
 * 停机数配置
 */
StopCount = Ext.extend(Ext.grid.EditorGridPanel,{
	constructor: function(){
		StopCount.superclass.constructor.call(this,{
			id:'StopCount',
			title:'停机数配置',
			closable: true,
			border : false ,
			style: 'background: #dfe8f6',
			store: new Ext.data.JsonStore({
				url : root + '/config/Server!queryStopCount.action',
				autoLoad: true,
				fields: [
					{name: 'addr_id', type: 'string'},
					{name: 'addr_name', type: 'string'},
					{name: 'county_id', type: 'string'},
					{name: 'area_id', type: 'string'},
					{name: 'base_prod_count', type: 'int'}
				]
			}),
	        cm: new Ext.grid.ColumnModel({
	            columns: [
	                {header: '地区名称',dataIndex: 'addr_name', width: 320},
	                {header: '停机数量',dataIndex: 'base_prod_count',width: 120, editor: new Ext.form.NumberField({minValue: 0}) }
	            ]
	        }),
	        clicksToEdit: 1,
	        buttonAlign: 'center',
	        buttons: [{
	            text: '保存',
	            iconCls: 'icon-save',
	            scope: this,
	            handler : this.doSave
	        }]
		});
	},
	doSave: function(){
		var rs = this.getStore().getModifiedRecords();
		if(rs.length == 0){
			Alert("数据未变更，无需保存!");
		}else{
			Confirm("确定要提交吗?", this, function(){
				var params = this.getValues(rs);
				var msg = Show();
				Ext.Ajax.request({
					url : root + '/config/Server!saveStopCount.action',
					params : params,
					scope : this,
					success : function(res, option) {
						msg.hide();
						msg = null;
						if (res.responseText == "true") {
							Alert('保存成功');
						}
					}
				});
				
			});
		}
	},
	getValues: function(records){
		var data = [];
		for(var i= 0;i < records.length; i++){
			var rs = records[i];
			data.push(rs.data);
		}
		
		return {
			"changeList": Ext.encode(data)
		};
	}
});

