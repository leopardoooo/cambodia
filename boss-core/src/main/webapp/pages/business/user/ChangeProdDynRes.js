
ChangeProdDynResForm = Ext.extend(BaseForm,{
	url: Constant.ROOT_PATH + '/core/x/User!changeProdDynRes.action',
	prodSn: null,
	constructor: function(){
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
		this.prodSn = record.get('prod_sn');
		var sm = new Ext.grid.CheckboxSelectionModel();
		ChangeProdDynResForm.superclass.constructor.call(this,{
			layout:'border',
			border:false,
			items:[
				{region:'center',layout:'fit',border:false,items:[{
						xtype : 'grid',
						title : '原动态资源',
						id : 'oldDynamicGrid',
						store : new Ext.data.JsonStore({fields:['res_id','res_name','group_name']}),
						columns : [
							{header:'资源ID',dataIndex:'res_id',width:50},
							{header:'资源名称',dataIndex:'res_name',width:110},
							{header:'组名',dataIndex:'group_name',width:140}
						]
				}]},
				{region:'east',layout:'fit',border:false,width:'50%',items:[{
						xtype : 'grid',
						title : '新动态资源选择',
						id : 'dynamicGrid',
						store : new Ext.data.GroupingStore({
							reader: new Ext.data.JsonReader({},[
								'res_id','res_name','group_name','res_desc','prod_id',
								{name : 'res_number',type : 'float'}]),
							groupField : 'group_name'
						}),
						sm : sm,
						columns : [sm,
							{header:'资源ID',dataIndex:'res_id',width:60},
							{header:'资源名称',dataIndex:'res_name',width:120},
							{header:'组名',dataIndex:'group_name',width:150}
						],
						view: new Ext.grid.GroupingView({
				            forceFit:true,
				            groupTextTpl: '{text} (共{[values.rs.length]}项，请选择{[values.rs[0].data["res_number"]]}项)'
			        	})
				}]}
			]
		});
	},
	doInit: function(){
		Ext.Ajax.request({
			url:Constant.ROOT_PATH + '/commons/x/QueryUser!queryDynResByProdSn.action',
			params:{
				prodSn: this.prodSn
			},
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data){
					var oldDynResList = data['oldDynamicResList']
					if(oldDynResList.length > 0){
						var records = [];
						for(var i=0;i<oldDynResList.length;i++){
							var groupName = oldDynResList[i].group_name;
							var resList = oldDynResList[i].resList;
							for(var j=0;j<resList.length;j++){
								var record =resList[j];
								record['group_name'] = groupName;
								record['res_number'] = oldDynResList[i].res_number;
								record['prod_id'] = res['prod_id'];
								records.push(record);
							}
						}
						Ext.getCmp('oldDynamicGrid').getStore().loadData(records);
					}
					
					var dynResList = data['dynamicResList'];
					if(dynResList.length > 0){
						var records = [];
						for(var i=0;i<dynResList.length;i++){
							var groupName = dynResList[i].group_name;
							var resList = dynResList[i].resList;
							for(var j=0;j<resList.length;j++){
								var record =resList[j];
								record['group_name'] = groupName;
								record['res_number'] = dynResList[i].res_number;
								record['prod_id'] = res['prod_id'];
								records.push(record);
							}
						}
						Ext.getCmp('dynamicGrid').getStore().loadData(records);
					}
				}
			}
		});
	},
	doValid: function(){
		var records = Ext.getCmp('dynamicGrid').getSelectionModel().getSelections();
		var store = Ext.getCmp('dynamicGrid').getStore();
		var result = {};
		if(store.getCount() > 0){
			
			var firstRec = store.getAt(0);
			var firstObj = {};
			firstObj['group_name'] = firstRec.get('group_name');
			firstObj['res_number'] = firstRec.get('res_number');
			var array = [];
			array.push(firstObj);
			var groupName = firstRec.get('group_name');
			store.each(function(rec){
				if(rec.get('group_name') != groupName){
					groupName = rec.get('group_name');
					var obj = {};
					obj['group_name'] = rec.get('group_name');
					obj['res_number'] =  rec.get('res_number');
					array.push(obj);
				}
			});
			
			if(records.length == 0){
				for(var i=0;i<array.length;i++){
					if(array[i].res_number > 0){
						result["isValid"] = false
						result["msg"] = array[i].group_name+"请按要求选择项目";
						return result;
					}
				}
			}else{
				for(var i=0;i<array.length;i++){
					var total = 0;
					for(var j=0;j<records.length;j++){
						if(array[i].group_name == records[j].get('group_name')){
							total = total + 1;
						}
					}
					if(array[i].res_number != total){
						result["isValid"] = false
						result["msg"] = array[i].group_name+"请按要求选择项目";
						return result;
					}
				}
				var oldStore = Ext.getCmp('oldDynamicGrid').getStore(),sFlag = true;
				oldStore.each(function(oldRecord){
					var flag = true;
					for(var i=0,len=records.length;i<len;i++){
						if(oldRecord.get('res_id') == records[i].get('res_id')){
							flag = false;
							break;
						}
					}
					if(flag){
						sFlag = true;
						return false;
					}else{
						sFlag = false;
						flag = true;
					}
				});
				if(sFlag === false){
					result["isValid"] = false
					result["msg"] = "请选择与原订购产品不同的资源";
					return result;
				}
			}
			
		}
		if(result.isValid == false){
			return result;
		}else{
			result["isValid"] = this.getForm().isValid();
			return result;
		}
	},
	getValues: function(){
		var all = {};
		all['prodSn'] = this.prodSn;
		
		var dyresList = [];
		var records = Ext.getCmp('dynamicGrid').getSelectionModel().getSelections();
		for(var i=0;i<records.length;i++){
			var dyres = {};
			dyres['prodId'] = records[i].get('prod_id');
			var obj = {};
			obj["res_id"] = records[i].get('res_id');
			dyres["rscList"] = [obj];
			dyresList.push(dyres);
		}
		all["dynamicRscList"] = Ext.encode(dyresList);
		
		return all;
	}
});

Ext.onReady(function(){
	TemplateFactory.gTemplate(new ChangeProdDynResForm());
});