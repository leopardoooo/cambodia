/**
 * 批量订购产品
 */
 
/**
 * 产品资费选择面板，用于用户订购产品和客户订购套餐
 * @class BusiOrderProd
 * @extends Ext.Panel
 */
BusiOrderProd = Ext.extend(Ext.Panel,{
	oldProdID : null,
	oldProdName:null,
	tariffId : null,
	node : null,//记录产品节点信息
	resNum : null,//动态资源中需多选的各个值，用于提交验证
	prodTariffStore : null,
	parent : null,
	userIds : null,
	isBase:null,//是否是基本包
	userType: 'DTV,ITV',
	constructor: function(p){
		this.parent = p;
		//产品资费
		this.prodTariffStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/core/x/Prod!queryBatchProdTariff.action",
			fields : ['tariff_id','tariff_name','tariff_desc','billing_cycle',
			{name : 'rent',type : 'float'}]
		});
		
		this.prodTariffStore.on('load',this.doLoadData,this);
		
		var prodCombo = new PinyinTreeCombo({
			id : 'batchOrderProdTreeId',
	    	fieldLabel: '产品名称',
	    	readOnly : true,
			treeWidth:300,
			minChars:0,
			allowBlank: false,
			treeUrl: Constant.ROOT_PATH+"/core/x/Prod!queryBatchProdTree.action",
			listeners : {
				scope:this,
				'focus' : function(comp){
					comp.treeParams['userType'] = this.userType;
					if(comp.list){
						comp.expand();
						comp.doQuery('');
					}
//					if(comp.list){
//						comp.doQuery('');
//					}
					var expDateComp = Ext.getCmp('exp_date');
					if(expDateComp && !expDateComp.disabled){
						expDateComp.setValue(null);
						expDateComp.disable();
					}
				},
				'select' : function(node,obj){
					Ext.getCmp('batchOrderProdTreeId').hasFocus = false;
					var pordId = obj.attributes.id.split(',');
					if(!Ext.getCmp('busiOrderProd').oldProdID){
						Ext.getCmp('busiOrderProd').oldProdID = pordId[1];
						Ext.getCmp('busiOrderProd').oldProdName = this.lastSelectionText;
						Ext.getCmp('busiOrderProd').node =  obj.attributes;
					}else{
						if(Ext.getCmp('busiOrderProd').oldProdID == pordId[1]){
							return;
						}
						Ext.getCmp('busiOrderProd').oldProdName = this.lastSelectionText;
						Ext.getCmp('busiOrderProd').oldProdID = pordId[1];
						Ext.getCmp('busiOrderProd').node =  obj.attributes;
					}
					this.isBase = obj.attributes.others['is_base'];
					Ext.getCmp('busiOrderProd').doSelectProd();
					
				}
			}
		});
		
		prodCombo.treeParams['userType'] = this.userType;
		
		//获取选择的用户ID
		BusiOrderProd.superclass.constructor.call(this,{
			border: false,
			id : 'busiOrderProd',
			bodyStyle: "background:#F9F9F9",
			layout : 'border',
			items:[{
				region:'north',
				bodyStyle:'padding-top:4px',
				border:false,
				layout:'form',
				labelWidth: 105,
				height:35,
				items:[
					{fieldLabel:'用户类型',xtype:'paramcombo',paramName:'USER_TYPE',id:'user_type_id',						
						allowBlank:false,
						listeners:{
							scope:this,
							select:function(combo){
								this.userType = combo.getValue();
								prodCombo.setValue(null);
								this.prodTariffStore.removeAll();
								Ext.getCmp('prodTariffId').setValue("");
								this.parent.resPanel.stcResStore.removeAll();
								this.parent.resPanel.dycResStore.removeAll();
							}
						}
					}
				]
			},{
				region : 'center',
				border : false,
				layout : 'column',
				defaults : {
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form',
					labelWidth: 105
				},
				items : [{
					items : [prodCombo]
				},{
					items : [{
						xtype : 'combo',
						id : 'prodTariffId',
						store : this.prodTariffStore,
						fieldLabel : '资费',
						emptyText: '请选择',
						disabled : true,
						allowBlank : false,
						minListWidth : 200,
						mode: 'local',
						hiddenName : 'tariff_id',
						hiddenValue : 'tariff_id',
						valueField : 'tariff_id',
						displayField : "tariff_name",
						forceSelection : true,
						triggerAction : "all",
						listeners:{
							'select' : function(combo,rec){
								Ext.getCmp('busiOrderProd').doSelectProdTariff(rec);
							}
						}
					}]
				}]
			},{
				region : 'south',
				height : 35,
				border : false,
				layout : 'column',
				defaults : {
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form'
				},
				items : [{
					labelWidth: 105,
					items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '开始计费日期',
						minValue : nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						value : nowDate(),
						allowBlank : false,
						id : 'prodstartdate'
					}]
				},{
					labelWidth: 105,
					items : [{
						xtype : 'textarea',
						fieldLabel : '资费描述',
						style : Constant.TEXTFIELD_STYLE,
						disabled : true,
						grow : true,
						preventScrollbars : true,
						height : 40,
						width : 125,
						readOnly : true,
						id : 'prodTariffDesc'
					}]
				}]
			}]
		})
	},
	doLoadData : function(store,recs){
		if(recs.length == 1){
			Ext.getCmp('prodTariffId').setValue(recs[0].get('tariff_id'));
			this.doSelectProdTariff(recs[0]);
		}
	},
	doSelectProd : function(){//选中产品后的触发事件
		this.tariffId = null;
		//加载资费信息
		this.loadTariffRec(this.oldProdID);
		
		this.parent.resPanel.reset();
		
		//刷新资源面板
		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH+"/core/x/Prod!queryProdRes.action",
			params : {
				prodId : this.oldProdID
			},
			success : function(response,opt){
				var res = Ext.decode(response.responseText);
				this.parent.refreshResPanel(res);
			}
		});
	},
	doSelectProdTariff : function(rec){//选中产品资费后的触发事件
		this.tariffId = rec.get('tariff_id');
		
		var expDateComp = Ext.getCmp('exp_date');
		var billingCycle = rec.get('billing_cycle');
		//基本包，非包月的去失效日期
		if(this.isBase == 'T' || billingCycle > 1){
			if(expDateComp && !expDateComp.disabled){
				expDateComp.disable();
			}
		}else{
			if(expDateComp && expDateComp.disabled){
				expDateComp.enable();
			}
		}
		
		this.items.itemAt(2).removeAll();
		if(this.node.others.just_for_once=='T'){
			this.items.itemAt(2).add({
				items : [{
					xtype : 'textfield',
					width : 120,
					fieldLabel : '预计到期日',
					value : this.node.others.invalid_date,
					readOnly :true,
					id : 'prodstartdate'
				}]
			});
		}else{
			if(rec.get('rent') > 0){
				this.items.itemAt(2).add({
					items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '开始计费日期',
						minValue :nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						value : nowDate(),
						allowBlank : false,
						id : 'prodstartdate'
					}]
				});
			}else{
				this.items.itemAt(2).add({
					items : [{
						xtype : 'datefield',
						width : 120,
						fieldLabel : '预计到期日',
						minValue : nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						allowBlank : false,
						id : 'prodstartdate'
					}]
				})
			}
		}
		this.items.itemAt(2).add({
			items : [{
				xtype : 'textarea',
				fieldLabel : '资费描述',
				style : Constant.TEXTFIELD_STYLE,
				disabled : true,
				grow : true,
				preventScrollbars : true,
				height : 40,
				width : 125,
				readOnly : true,
				id : 'prodTariffDesc'
			}]
		})
		Ext.getCmp('prodTariffDesc').setValue(rec.get('tariff_desc'));
		this.doLayout();
	},
	loadTariffRec : function(prodId){//加载资费数据
		Ext.getCmp('prodTariffId').setDisabled(false);
		Ext.getCmp('prodTariffId').clearValue();
		Ext.getCmp('batchOrderProdTreeId').setDisabled(true);
		Ext.getCmp('batchOrderProdTreeId').setDisabled(false);
		this.prodTariffStore.load({
			params : {
				prodId : prodId
			}
		});
	}/*,
	doShowDisct : function(tariffId){
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/core/x/Prod!queryBatchTariffByTariffId.action',
			params:{
				tariffIds:Ext.encode([tariffId])
			},
			scope:this,
			success:function(res,options){
				var disctData = Ext.decode(res.responseText);
				if(disctData.length > 0){
					if(disctData[0]['disctList']){
						Ext.getCmp('disct_price').setValue(Ext.util.Format.formatFee(disctData[0]['disctList'][0]['final_rent']));
					}else{
						Ext.getCmp('disct_price').setValue('');
					}
					
				}
			}
		})
	}*/
});


/**
 * 资源面板
 * @class ResPanel
 * @extends Ext.Panel
 */
ResPanel = Ext.extend(Ext.Panel,{
	dycResStore : null,
	stcResStore : null,
	parent:null,
	constructor : function(p){
		this.parent = p;
		this.stcResStore = new Ext.data.JsonStore({
			fields : ["res_desc","res_id","res_name"]
		});
		this.dycResStore = new Ext.data.GroupingStore({
			reader: new Ext.data.JsonReader({},[
				{name:'prod_id'},{name:'res_desc'},{name:'res_id'},
					{name:'res_name'},{name:'group_name'},{name : 'res_number',type : 'float'}
			]),
			groupField : 'group_name'
		});
		var sm = new Ext.grid.CheckboxSelectionModel()
		ResPanel.superclass.constructor.call(this,{
			border: false,
			baseCls: 'x-plain',
			layout:'border',
			bodyStyle: "background:#F9F9F9",
			items : [{
				region : 'west',
				border : false,
				width:"30%",
				layout : 'fit',
				items : [{
					xtype : 'grid',
					title : '静态资源',
					height : 240,
					store : this.stcResStore,
					columns : [
						{header:'资源ID',dataIndex:'res_id',hidden : true},
						{header:'资源名字',dataIndex:'res_name'}
					],
					viewConfig : {
						forceFit : true
					}
				}]
			},{
				split:true,
				region : 'center',
				layout: 'fit',
				border: false,
				items : [{
					xtype : 'grid',
					title : '动态资源选择',
					id : 'dynamicGrid',
					height : 250,
					store : this.dycResStore,
					sm : sm,
					columns : [sm,
						{header:'资源ID',dataIndex:'res_id',hidden : true},
						{header:'资源名称',dataIndex:'res_name'},
						{header:'组名',dataIndex:'group_name'}
					],
					view: new Ext.grid.GroupingView({
			            forceFit:true
			            ,groupTextTpl: '{text} (共{[values.rs.length]}项，请选择{[values.rs[0].data["res_number"]]}项)'
		        	})
				}]
			}]
		})
	},
	doLoadData : function(recs){
		for(var k=0;k<recs.length;k++){
			//加载静态资源
			var res = recs[k];
			this.stcResStore.loadData(res.staticResList,true);
			
			var records = [];
			for(var i=0;i<res.dynamicResList.length;i++){
				var groupName = res.dynamicResList[i].group_name;
				var resList = res.dynamicResList[i].resList;
				for(var j=0;j<resList.length;j++){
					var record =resList[j];
					record['group_name'] = groupName;
					record['res_number'] = res.dynamicResList[i].res_number;
					record['prod_id'] = res['prod_id'];
					records.push(record);
				}
			}
			this.dycResStore.loadData(records,true);
		}
	},
	reset : function(){
		this.stcResStore.removeAll();
		this.dycResStore.removeAll();
	}
});


/**
 * 订购产品表单
 * @class BatchOrderProdForm
 * @extends BaseForm
 */
BatchOrderProdForm = Ext.extend(Ext.form.FormPanel,{
	busiOrderProd: null,
	resPanel : null,
	prodMessage:null,
	orderProdId: null,
	constructor : function() {
		this.busiOrderProd = new BusiOrderProd(this);
		this.resPanel = new ResPanel(this);
		BatchOrderProdForm.superclass.constructor.call(this, {
			border : false,
			layout : 'border',
			fileUpload:true,
			items : [
			{layout: 'border',
				region : 'north',
				height : 160,
				items:[{
					region:'center',
					layout:'fit',
					border : false,
					items:[this.busiOrderProd]
				},{
					region:'south',
					height:55,
					border : false,
					layout:'column',
					defaults : {
						columnWidth:0.5,
						layout: 'form',
						border : false,
						labelWidth: 105
					},
					items:[{
						items : [{
							xtype:'datefield',
							fieldLabel: '失效日期',
							disabled:true,
							width:120,
							id:'exp_date',
							format:'Y-m-d'
						}]
					}/*,{
						items : [{
							xtype:'displayfield',
							fieldLabel: '折扣价格',
							width:120,
							id:'disct_price'
						}]
					}*/,{
						columnWidth:1,
						items : [{
							xtype:'textfield',
							inputType:'file',
							fieldLabel:'用户ID文件',
							allowBlank : false,
							name:'file',
							width : 200,
							anchor:'90%',allowBlank:false,
							buttonText:'浏览...'
						}]
					}]
				}]
			},{
				region : 'center',
				layout : 'fit',
				items : [this.resPanel]
			}]
		});
		App.form.initComboData(this.findByType('paramcombo'),this.doUserType,this);
	},
	doUserType : function(){//设置用户类型
		var userType = Ext.getCmp('user_type_id');
		if(userType){
			var terStore = userType.getStore();
			terStore.each(function(record){
				if(record.get('item_value') == 'DTV'){
					record.set('item_value','DTV,ITV');
				}
			});
			userType.setValue('DTV,ITV');
		}
	},
	refreshResPanel : function(data){
		this.resPanel.doLoadData(data);
	},
	getValues : function(){
		var all = {};
		this.orderProdId = all["prodId"] = Ext.getCmp('busiOrderProd').oldProdID;
		all["tariffId"] = Ext.getCmp('prodTariffId').getValue();
		all["feeDate"] = Ext.getCmp('prodstartdate').getValue();
		all["expDate"] = Ext.getCmp('exp_date').getValue();
		
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
	},
	doValid: function(){		
		var result = {};
		var records = Ext.getCmp('dynamicGrid').getSelectionModel().getSelections();
		var store = Ext.getCmp('dynamicGrid').getStore();
		
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
			})
			if(records.length == 0){
				for(var i=0;i<array.length;i++){
					if(array[i].res_number > 0){
						result["isValid"] = false;
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
						result["isValid"] = false;
						result["msg"] = array[i].group_name+"请按要求选择项目";
						return result;
					}
				}
			}
		}
		
		if(result.isValid == false){
			return result;
		}else{
			result["isValid"] = this.getForm().isValid();
			return result;
		}
	}
});

var BatchOrderProdWin = Ext.extend(Ext.Window,{
	form : null,
	constructor:function(){
		this.form = new BatchOrderProdForm();
		BatchOrderProdWin.superclass.constructor.call(this,{
			title:'批量订购产品',
			border:false,
			width:700,
			height:550,
			layout:'fit',
			items:[this.form],
			closeAction:'close',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:this.closeWin}
			]
		});
	},
	doSave:function(){
		var valid = this.form.doValid();
		if(valid['isValid'] === true){
			var values = this.form.getValues();
			var ps={},all={};
			ps['busiCode'] = '1914';
			ps['optr'] = App.getData().optr;
			all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
			Ext.apply(all,values);
			
			var msg = Show();
			this.form.getForm().submit({
				url:root+'/core/x/User!batchOrderProd.action',
				scope:this,
				params:all,
				success:function(form,action){
					msg.hide();msg = null;
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('操作成功!',function(){
								this.closeWin();
							},this);
						}
					}
				}
			});
		}else{
			if(valid['msg'])
				Alert(valid['msg']);
		}
	},
	closeWin:function(){
		this.close();
	}
});
