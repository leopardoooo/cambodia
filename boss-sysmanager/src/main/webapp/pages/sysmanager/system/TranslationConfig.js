/**
 * 翻译配置
 */
 
 TranslationGrid = Ext.extend(Ext.grid.EditorGridPanel, {
 	pageSize: 20,
 	constructor: function(){
 		this.transStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryDataTranslation.action',
			fields: ['id', 'data_cn', 'data_en'],
			totalProperty: 'totalProperty',
			root: 'records'
		});
		this.transStore.load({params:{start:0,limit:this.pageSize}});
		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
			sm,
			{header: '中文', dataIndex: 'data_cn', width: 400, editor: new Ext.form.TextField({}), renderer: App.qtipValue},
			{header: '英文', dataIndex: 'data_en', width: 400, editor: new Ext.form.TextField({}), renderer: App.qtipValue}
		];
 		TranslationGrid.superclass.constructor.call(this,{
 			columns: columns,
 			sm: sm,
 			store: this.transStore,
 			border: false,
 			clicksToEdit:1,
 			tbar: [
 				' ',' ','输入关键字' , ' ',
				new Ext.ux.form.SearchField({
					id: 'translationSearchFieldId',
	                store: this.transStore,
	                width: 200,
	                hasSearch : true,
	                pageSize: this.pageSize,
	                emptyText: '支持中文、英文名称模糊查询'
	            }),'-',
	            {text: '添加', iconCls: 'icon-add', scope: this, handler: this.doAdd}, '-',
	            {text: '删除', iconCls: 'icon-del', scope: this, handler: this.doDel}, '-',
 				'->', '-',
 				{text: '刷新', iconCls: 'icon-refresh', scope: this, handler: this.doRefresh}, '-',
 				{text: '保存', iconCls: 'icon-add', scope: this, handler: this.doSave}, '-'
 			],
 			bbar: new Ext.PagingToolbar({store: this.transStore, pageSize: this.pageSize})
 		});
 	},
 	doAdd: function(){
 		var recordType = this.getStore().recordType;
 		var record = new recordType({'data_cn':'', 'data_en':''});
 		this.transStore.insert(0, record);
 		this.startEditing(0,0);
 	},
 	doRefresh: function(){
 		this.transStore.removeAll();
 		this.transStore.baseParams['query'] = Ext.getCmp('translationSearchFieldId').getValue();
 		this.transStore.load();
 	},
 	doDel: function(){
 		var records = this.getSelectionModel().getSelections();
 		if(records.length > 0){
 			Confirm('确认删除吗？', this, function(){
 				
 				var ids = [];
	 			Ext.each(records, function(record){
	 				ids.push(record.get('id'));
	 			});
				Ext.Ajax.request({
					url:root+'/config/Config!deleteDataTranslation.action',
					params:{
						dataIds: ids
					},
					scope:this,
					success:function(req){
						Ext.getCmp('translationSearchFieldId').setValue('');
						this.doRefresh();
						Alert('删除成功!');
					}
				});
				
 			});
 		}
 	},
 	doSave: function(){
 		this.stopEditing();
 		var modifiedRecords = this.transStore.getModifiedRecords();
 		if(modifiedRecords.length > 0){
 			this.transStore.commitChanges();
	 		var values = [];
	 		Ext.each(modifiedRecords, function(record){
	 			var obj = record.data;
	 			values.push(obj);
	 		}, this);
	 		
	 		Ext.getBody().mask();
			Ext.Ajax.request({
				url:root+'/config/Config!saveDataTranslation.action',
				params:{
					dataTranslations: Ext.encode(values)
				},
				scope:this,
				success:function(req){
					this.doRefresh();
					Ext.getBody().unmask();
					Alert('修改成功!');
				}
			});
	 		
 		}
 	}
 });
 
 TranslationConfig = Ext.extend(Ext.Panel, {
    constructor: function () {
        this.grid = new TranslationGrid(this);
        TranslationConfig.superclass.constructor.call(this, {
            id: 'TranslationConfig',
            title: '翻译配置',
            closable: true,
            border: false,
            layout : 'fit',
            items: [this.grid]
        });
    }
});