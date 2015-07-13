Ext.ns('com.yc');

com.yc.CubeRowCfgWin = Ext.extend(Ext.Window,{
	title:'配置信息',width:530,height:300,buttonAlign:'center',parent:null,closeAction:'hide',
	grid:null,rowTypeEditor:null,gridStore:null,
	rep_id:null,rowTypeEditorData:[],rowTypeEditorStore:null,
	resetData:function(rep_id,rowTypeEditorData,parent){
		this.rep_id = rep_id;
		this.rowTypeEditorData =  rowTypeEditorData ;
		this.parent = parent;
		
		this.rowTypeEditorStore.loadData(this.rowTypeEditorData);
		this.rep_id = rep_id||this.rep_id;
		this.gridStore.load({params : {rep_id : this.rep_id}});
	},
	constructor:function(rep_id,rowTypeEditorData,parent){
		this.gridStore = new Ext.data.JsonStore({
			autoLoad:false,
			url:root + '/query/RepDesign!queryCustomRows.action',
			fields:['row_idx','row_name','row_type','dataformat']
		});
		this.rowTypeEditorStore = new Ext.data.JsonStore({fields : ['id','key','name','value'],data:this.rowTypeEditorData});
		this.rowTypeEditor = new Ext.form.ComboBox({
			hiddenName : 'row_type',displayField : 'name',valueField : 'id',triggerAction : 'all',forceSelection : true,
			store:this.rowTypeEditorStore,listWidth:200,
			listeners:{
				scope:this,
				select:function(combo,record,index){
					if(record.get('id') == 'textfield' || record.get('id') == 'combo' ){
						return true;
					}
					var rec = this.grid.selModel.getSelected();
					rec.set('dataformat','');
				}
			}
		});
		
		this.resetData(rep_id,rowTypeEditorData,parent);
		
		this.grid = new Ext.grid.EditorGridPanel({
			store:this.gridStore,clicksToEdit:1,border:false,
			columns : [
					new Ext.grid.RowNumberer(),
					{header:'row_idx',dataIndex:'row_idx',hidden:true},
					{header:'列名',dataIndex:'row_name',width:110,editor:new Ext.form.TextField()},
					{header:'数据类型',dataIndex:'row_type',width:110,editor:this.rowTypeEditor,
						renderer:function(v){
							var result = '';
							Ext.each(this.rowTypeEditorData,function(data){
								if(data.id == v){
									result = data.name;
								}
							});
							return result;
						},scope:this
					},
					{header:'数据/数据限制',dataIndex:'dataformat',width:140,editor:new Ext.form.TextField()}
		        ],
		     listeners:{
		     	scope:this,
		     	beforeedit:function(param){
		     		// grid,record,field,value,rowIndex,columnIndex
		     		var record = param.record;
		     		if(param.field != 'dataformat'){
		     			return true;
		     		}
		     		if(record.get('row_type') == 'textfield' || record.get('row_type') == 'combo' ){
		     			return true;
		     		}
		     		return false;
		     	}
		     }
		});
		
		com.yc.CubeRowCfgWin.superclass.constructor.call(this,{
			items:this.grid,layout:'fit',
			tbar:['-',
				{text:'增加',handler:this.addNew,scope:this},'-',
				{text:'删除',handler:this.deleteRow,scope:this},'-'
			],
			buttonAlign:'right',
			buttons:[
			{text:'保存',handler:this.save,scope:this},
			{text:'取消',handler:this.cancel,scope:this}
			]
		})
	},
	loadData:function(rep_id){
		this.rep_id = rep_id||this.rep_id;
		this.gridStore.load({params : {rep_id : this.rep_id}});
	},
	save:function(){
		var array = [];
		this.gridStore.each(function(rec){
			var data = rec.data;
			data.rep_id = this.rep_id;
		    array.push(data);
		},this);
		
		Ext.Ajax.request({
			url : root + '/query/RepDesign!saveCustomRows.action',
			scope : this,
			timeout : 9999999999,
			params : {rep_id : this.rep_id,gridrows:Ext.encode(array)},
			success : function(res, opt) {
				var dimsData = Ext.decode(res.responseText);
				if(dimsData){
					Alert('操作成功!');
					this.hide();
				}else{
					Alert('操作失败!');
				}
			},
			failure : function() {
				Alert('加载数据出错');
			}
		});
		
	},
	cancel:function(){
		this.hide();
	},
	addNew:function(){
		var lastRecord = this.gridStore.getAt(this.gridStore.getCount()-1);
		var row_idx = 0;
		if(lastRecord){
			row_idx = lastRecord.get('row_idx') +1;
		}
		var rec = new Ext.data.Record({row_idx:row_idx});
		this.gridStore.add(rec);
	},
	deleteRow:function(){
		this.gridStore.remove(this.grid.selModel.getSelections());
	}
});
