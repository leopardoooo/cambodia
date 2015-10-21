/**
 * 省定义配置
 */
 
 ProvinceGrid = Ext.extend(Ext.grid.EditorGridPanel, {
 	constructor: function(){
 		this.store = new Ext.data.JsonStore({
			url: root+'/config/Config!queryProvince.action',
			autoLoad: true,
			fields: ['id', 'name', 'description', 'task_code', 'cust_code', 'remark', 'domain_name']
		});
		var columns = [
			{header: '序号', dataIndex: 'id', width: 70, hidden: true},
			{header: '名称', dataIndex: 'name', width: 300, editor: new Ext.form.TextField({})},
			{header: '描述', dataIndex: 'description', width: 300, editor: new Ext.form.TextField({})},
			{header: '地址码', dataIndex: 'task_code', width: 80},
			{header: '客户编号前缀', dataIndex: 'cust_code', width: 200, editor: new Ext.form.TextField({
					vtype: 'numZero'
				})
			},
			{header: '宽带账号域名', dataIndex: 'domain_name', width: 200, editor: new Ext.form.TextField({})}
		];
 		ProvinceGrid.superclass.constructor.call(this,{
 			columns: columns,
 			store: this.store,
 			border: false,
 			clicksToEdit:1,
 			buttonAlign: 'center',
 			buttons: [
 				{text: '刷新', iconCls: 'icon-refresh', scope: this, handler: this.doRefresh},
 				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave}
 			]
 		});
 	},
 	doRefresh: function(){
 		this.store.removeAll();
 		this.store.reload();
 	},
 	doSave: function(){
 		this.stopEditing();
 		var modifiedRecords = this.store.getModifiedRecords();
 		if(modifiedRecords.length > 0){
 			this.store.commitChanges();
	 		var values = [];
	 		Ext.each(modifiedRecords, function(record){
	 			var obj = record.data;
	 			values.push(obj);
	 		}, this);
	 		
			Ext.Ajax.request({
				url:root+'/config/Config!saveProvince.action',
				params:{
					provinces: Ext.encode(values)
				},
				scope:this,
				success:function(req){
					this.doRefresh();
					Alert('修改成功!');
				}
			});
	 		
 		}
 	}
 });
 
 ProvinceConfig = Ext.extend(Ext.Panel, {
    constructor: function () {
        this.grid = new ProvinceGrid(this);
        ProvinceConfig.superclass.constructor.call(this, {
            id: 'ProvinceConfig',
            title: '省定义配置',
            closable: true,
            border: false,
            layout : 'fit',
            items: [this.grid]
        });
    }
});