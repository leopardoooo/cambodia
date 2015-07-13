/**
 * 套餐表格
 * @class PromFeeGrid
 * @extends Ext.grid.GridPanel
 */
PromFeeGrid = Ext.extend(Ext.grid.GridPanel,{
	promFeeStore : null,
	promFeeId : null,

	constructor : function(){
		this.promFeeStore = new Ext.data.JsonStore({
//			autoLoad : true,
			totalProperty:'totalProperty',
			root:'records',
			url : root + '/system/PromFee!queryPromFee.action',
			fields : ['prom_fee_id','prom_fee_name','prom_fee','printitem_id','printitem_name','optr_id','optr_name','area_id','county_id',
			'eff_date','exp_date','create_time','join_cnt','prom_type_name','prom_type','status','status_text','remark']
		})
		this.doInit();		
		var sm = new Ext.grid.RowSelectionModel();
		
		var cm = [{
			header : '套餐名称',
			dataIndex  : 'prom_fee_name'
		},{
			header : '套餐分类',
			dataIndex : 'prom_type_name'
		},{
			header : '打印名称',
			dataIndex : 'printitem_name'
		},{
			header : '金额',
			dataIndex : 'prom_fee',
			width : 70,
			renderer : Ext.util.Format.formatFee
		},{
			header : '参加次数',
			width : 80,
			dataIndex : 'join_cnt'
		},{
			header : '状态',
			dataIndex : 'status_text',
			width : 50,
			renderer: Ext.util.Format.statusShow
		},{
			header : '生效日期',
			dataIndex : 'eff_date'
		},{
			header : '失效日期',
			dataIndex : 'exp_date'
		},{
			header : '操作',
			scope:this,
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}
		},{
			header : '操作员',
			dataIndex : 'optr_name'
		},{
			header : '创建时间',
			dataIndex : 'create_time'
		},{
			header : '备注',hidden:true,
			dataIndex : 'remark'
		}];
		
		
		PromFeeGrid.superclass.constructor.call(this,{
			id : 'PromFeeGrid',
			height : 300,
			region : 'north',
			columns : cm,
			sm : sm,
			store : this.promFeeStore,
			bbar:new Ext.PagingToolbar({store:this.promFeeStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar : [' ','输入关键字', ' ',
							new Ext.ux.form.SearchField({
										store : this.promFeeStore,
										width : 200,
										hasSearch : true,
										emptyText : '支持套餐名称模糊查询'
									}), '->',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        }]
		})
	},
	
	initEvents : function(){
		this.on('rowclick',this.doClick,this);
		PromFeeGrid.superclass.initEvents.call(this);
	},
	doClick : function(){
		var record = this.getSelectionModel().getSelected();
		if(record){
			var promFeeId = record.get('prom_fee_id');
			
			if(this.promFeeId && this.promFeeId == promFeeId){
				return ;
			}
			this.promFeeId = promFeeId;
			Ext.getCmp('PromFeeUserGrid').loadData(record);
			Ext.getCmp('PromFeeProdGrid').loadData(record);
			Ext.getCmp('PromFeeDivisionGrid').loadData(record);
		}else{
			Ext.getCmp('PromFeeUserGrid').getStore().removeAll();
			Ext.getCmp('PromFeeProdGrid').getStore().removeAll();
			Ext.getCmp('PromFeeDivisionGrid').getStore().removeAll();
		}
		
	},
	doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == record.get('county_id')){
			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"PromFeeGrid"+"\').modifyRec(); style='color:blue'>修改</a>";
			if(record.get('status') == 'ACTIVE'){
				btns += "&nbsp;<a href='#' onclick=Ext.getCmp(\'"+"PromFeeGrid"+"\').stopProm(); style='color:blue'>禁用</a>" 
			}else{
				btns += "&nbsp;<a href='#' onclick=Ext.getCmp(\'"+"PromFeeGrid"+"\').userProm(); style='color:blue'>启用</a>" 
			}
		}
		return btns;
	},
	doInit:function(){
		this.promFeeStore.load({
					params : {
						start : 0,
						limit : Constant.DEFAULT_PAGE_SIZE
					}
				});	
	},
	modifyRec : function(){
		var record = this.getSelectionModel().getSelected();
		new PromFeeConfigWin(record).show();
	},
	addRecord : function(){
		new PromFeeConfigWin().show();
	},
	stopProm : function(){
		var rec = this.getSelectionModel().getSelected();
		Confirm("确定要禁用该套餐吗?", null, function() {
			Ext.Ajax.request({
				url : root + '/system/PromFee!stopProm.action',
				params : {promFeeId : rec.get('prom_fee_id')},
				scope:this,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert('操作成功!');
						Ext.getCmp('PromFeeGrid').doInit();
					} else {
						Alert('操作失败!');
					}
				}
			});
		})
	},
	userProm : function(){
		var rec = this.getSelectionModel().getSelected();
		Confirm("确定要启用该套餐吗?", null, function() {
			Ext.Ajax.request({
				url : root + '/system/PromFee!userProm.action',
				params : {promFeeId : rec.get('prom_fee_id')},
				scope:this,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert('操作成功!');
						Ext.getCmp('PromFeeGrid').doInit();
					} else {
						Alert('操作失败!');
					}
				}
			});
		})
	}
});

/**
 * 适用地区
 * @class PromFeeCountyTree
 * @extends Ext.tree.TreePanel
 */
PromFeeCountyTree = Ext.extend(Ext.tree.TreePanel, {
	promFeeId:null,
    constructor: function (promFeeId) {
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/PromFee!getCountyTree.action?promFeeId=" + promFeeId      
        });
        this.promFeeId = promFeeId;
        PromFeeCountyTree.superclass.constructor.call(this, {
            region: 'east',
            width: '40%',
            border : false,
            split: true,
            title: '应用地市',
            id: 'PromFeeCountyTree',
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '-1',
                iconCls: 'x-tree-root-icon',
                nodeType: 'async',
                text: '地市结构'
            }
        });
        this.getRootNode().expand(true);
    },
    listeners: {
        'checkchange': function (node, checked) {
            node.expand();
            node.attributes.checked = checked;
            node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
        }
    },
    initEvents: function(){
    	this.on("afterrender",function(){
			//如果不是修改,自动全选
    		if(!this.promFeeId){
    			//分公司操作员，
    			if(App.data.optr['county_id'] != '4501'){
    				var node = this.getRootNode();
	        		node.attributes.checked = true;
	        		node.ui.toggleCheck(true);
	        		node.fireEvent('checkchange', node, true);
    			}
    		}
		},this,{delay:3000});
    }
});

/**
 * 套餐配置窗口
 * @class PromFeeConfigWin
 * @extends Ext.Window
 */
PromFeeConfigWin = Ext.extend(Ext.Window,{
	promFeeId : null,
	promFeeCountyTree : null,
	constructor : function(record){
		this.record = record;
		if (!Ext.isEmpty(record)) {
            this.promFeeId = record.get('prom_fee_id');
        }
		this.promFeeCountyTree = new PromFeeCountyTree(this.promFeeId);
		var formItems = [{
			xtype : 'hidden',
			id : 'promFeeId',
			name : 'prom_fee_id'
		},{
			fieldLabel : '套餐名称',
			name : 'prom_fee_name',
			width : 150,
			allowBlank : false
		},{ 
            fieldLabel: '套餐分类',
            paramName: 'PROM_TYPE',
            xtype: 'paramcombo',
            allowBlank: false,
            hiddenName: 'prom_type',
            id:'prom_type_promfeeid'
        },{
			xtype : 'numberfield',
			fieldLabel : '套餐金额',
			name : 'prom_fee',
			id : 'prom_fee',
			width : 150,
			allowBlank : false,
			allowNegative : false
		},{
			xtype : 'numberfield',
			fieldLabel : '允许参加次数',
			name : 'join_cnt',
			width : 150,
			allowBlank : false,
			allowNegative : false
		},{
			xtype : 'datefield',
			width : 150,
			id : 'eff_dateForProm',
			name : 'eff_date',
			format : 'Y-m-d',
			editable : false,
			allowBlank : false,
			fieldLabel : '生效期限'
		},{
			xtype : 'datefield',
			width : 150,
			id : 'exp_dateForProm',
			name : 'exp_date',
			format : 'Y-m-d',
			minValue : nowDate().format('Y-m-d'),
			editable : false,
			fieldLabel : '失效期限'
		},new PrintItemPanel({allowBlank : false}),
		
		{
			fieldLabel : '描述',xtype:'displayfield'
		},
		{
			xtype:'panel',border:false,
			bodyStyle:'padding-left:30px',
			items:[
				{xtype : 'textarea',
				height : 110,
				width : 250,
				name : 'remark'
				}
			]
		}
		];
		
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 100,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			defaults : {
				xtype : 'textfield'
			},
			items : formItems
		});
		
		PromFeeConfigWin.superclass.constructor.call(this,{
			title : '套餐配置',
			layout : 'border',
			height : 450,
			width : 600,
			closeAction : 'close',
			items : [{
					region : 'center',
					layout : 'fit',
					split : true,
					items : [this.form]
				},{
					region : 'east',
					layout : 'fit',
					width : '50%',
					items : [this.promFeeCountyTree]
				}],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	initComponent: function () {
        PromFeeConfigWin.superclass.initComponent.call(this);
        var comboes = this.findByType("paramcombo");
        App.form.initComboData(comboes, this.doInit, this);
    },
	doInit : function(){
		if(!this.record)
			return;

		if(this.record.get('eff_date') && typeof(this.record.get('eff_date')) == 'string'){
			this.record.data.eff_date = Date.parseDate(this.record.get('eff_date'),'Y-m-d h:i:s')
		}
		if(this.record.get('exp_date') && typeof(this.record.get('exp_date')) == 'string'){
			this.record.data.exp_date = Date.parseDate(this.record.get('exp_date'),'Y-m-d h:i:s')
		}
		this.form.getForm().loadRecord(this.record);
		
		var cmp = Ext.getCmp('prom_fee');
		cmp.setValue(Ext.util.Format.formatFee(cmp.getValue()));
		Ext.getCmp('prom_type_promfeeid').setValue(this.record.get('prom_type'));
	},
	doSave : function(){
		if(this.form.getForm().isValid()){
			if(this.find("name","join_cnt")[0].getValue()<=0){
				Alert("参加次数必须大于0");
				return;
			}
			Confirm("确定保存吗?", this ,function(){
				var old =  this.form.getForm().getValues(), newValues = {};
				old['prom_fee'] = old['prom_fee'] * 100;
				for(var key in old){
					newValues["promFee." + key] = old[key];
				}
				
				var promCountyId = [];
			    if (Ext.getCmp('PromFeeCountyTree')) {
		            var nodes = Ext.getCmp('PromFeeCountyTree').getChecked();
		            for (var i in nodes) {
		                if (nodes[i].leaf) {
		                    promCountyId.push(nodes[i].id);
		                }
		            }
		        }
		        
		        newValues["countyIds"] = promCountyId.join(",");
				
				Ext.Ajax.request({
					url :root + '/system/PromFee!savePromFee.action',
					params : newValues,
					scope : this,
					success : function(res,opt){
//						mb.hide();//隐藏提示框
//						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							/**
							Ext.getCmp('PromFeeGrid').getStore().reload();
							Ext.getCmp('PromFeeUserGrid').store.removeAll();
							Ext.getCmp('PromFeeProdGrid').store.removeAll();
							**/
							var promFeeGrid = Ext.getCmp('PromFeeGrid');
							promFeeGrid.getStore().reload();
							promFeeGrid.promFeeId = '';
							
							var promFeeProdGrid = Ext.getCmp('PromFeeProdGrid');
							promFeeProdGrid.store.removeAll();
							promFeeProdGrid.promFeeId = '';
							
							var PromFeeUserGrid = Ext.getCmp('PromFeeUserGrid');
							PromFeeUserGrid.store.removeAll();
							PromFeeUserGrid.promFeeId = '';
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			})
		}
	}
})

/**
 * 套餐用户表格
 * @class PromFeeUserGrid
 * @extends Ext.grid.GridPanel
 */
PromFeeUserGrid= Ext.extend(Ext.grid.GridPanel,{
	promFeeUserStore : null,
	promFeeId : null,
	promfee:0,
	userNo : null,
	constructor : function(){
		this.promFeeUserStore = new Ext.data.JsonStore({
			url : root + '/system/PromFee!queryPromFeeUsers.action',
			fields : ['prom_fee_id','user_no','prod_count','rule_id','rule_name','user_fee']
		})
		
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : true});
		
		var cm = [sm,{
			header : '编号',
			dataIndex : 'user_no'
		},{
			header : '规则名称',
			dataIndex : 'rule_name'
		},{
			header : '规则金额',
			dataIndex : 'user_fee',
			renderer : Ext.util.Format.formatFee
		},{
			header : '可选产品数',
			dataIndex : 'prod_count'
		}];
		
		
		
		PromFeeUserGrid.superclass.constructor.call(this,{
			id : 'PromFeeUserGrid',
			title : '套餐用户',
			border : false,
			region : 'center',
			store : this.promFeeUserStore,
			columns : cm,
			sm : sm,
			tbar : [' ',' ',{
				id : 'PromFeeUserConfig',
	        	text : '配置',
	        	iconCls : 'icon-modify',
	        	scope : this,
	        	handler : this.doConfig
	        }]
		})
	},
	initEvents : function(){
		this.on('rowclick',this.doClick,this);
		PromFeeUserGrid.superclass.initEvents.call(this);
	},
	doClick : function(){
		var userNo = this.getSelectionModel().getSelected().get('user_no');
		
//		if(this.userNo && this.userNo == userNo){
//			return ;
//		}
		this.userNo = userNo;
		
		Ext.getCmp('PromFeeProdGrid').showData(this.userNo);
		
	},
	doConfig : function(){
		if(!this.promFeeId){
			Alert('请选择选择套餐记录');
			return;
		}
		
		new PromFeeUserConfigWin(this.getStore().getRange(),this.promFeeId,this.promfee).show();
	},
	loadData : function(record){
		//根据操作员地区，现在是否允许修改
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == record.get('county_id')){
			Ext.getCmp('PromFeeUserConfig').enable();
		}else{
			Ext.getCmp('PromFeeUserConfig').disable();
		}
		
		var promFeeId = record.get('prom_fee_id');
		this.promfee=record.get('prom_fee');
		if(this.promFeeId && this.promFeeId == promFeeId){
			return ;
		}
		this.promFeeId = promFeeId;
		
		this.promFeeUserStore.load({
			params : {
				promFeeId : promFeeId
			}
		})
	}
})

/**
 * 套餐用户配置
 * @class PromFeeUserConfigGrid
 * @extends Ext.grid.EditorGridPanel
 */
PromFeeUserConfigGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	promFeeId : null,
	ruleStore : null,
	idSequence:1,//初始化最大的id +1
	constructor : function(records,promFeeId){
		
		var array = [];
		Ext.each(records,function(rec){
			var data = rec.data;
			var dataCopy = {};
			var index = parseInt(data.user_no);
			if(index>=this.idSequence){
				this.idSequence = index +1;
			}
			for(var key in data){
				dataCopy[key]=data[key];
			}
			dataCopy.user_fee=data.user_fee/100;
			array.push(new Ext.data.Record(dataCopy));
		},this);
		
		this.store = new Ext.data.JsonStore({
			fields : ['prom_fee_id','user_no','prod_count','rule_id','rule_name','user_fee']
		})
		this.store.add(array);
		
		
		
		//this.store.each(function(record){
			//record.data.user_fee = record.data.user_fee / 100;
		//});
		
		this.ruleStore = new Ext.data.JsonStore({
			autoLoad : true,
            url: root + '/system/PromFee!queryPromFeeUserRule.action',
            fields: ['rule_id', 'rule_name']
        });
		
		this.promFeeId = promFeeId;
		
		var cm = [{
			header : '用户编号',
			dataIndex : 'user_no'
		},{
			header : '规则名称',
			dataIndex : 'rule_name',
			editor : new Ext.form.ComboBox({
				store : this.ruleStore,
				editable : true,
				boxMinWidth : 250,
				minListWidth : 250,
				forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				displayField : 'rule_name',
				valueField : 'rule_name'
			})
		},{
			header : '规则金额',
			dataIndex : 'user_fee',
			editor: new Ext.form.NumberField({
				allowNegative : false
			})
		},{
			header : '可选产品数量',
			dataIndex : 'prod_count',
			editor : new Ext.form.NumberField({
				allowNegative : false
			})
		},{ header: '操作',
	        width:170,
	        scope:this,
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	return "<a href='#' onclick=Ext.getCmp(\'"+"PromFeeUserConfigGrid"+"\').deleteRow(); style='color:blue'>删除</a>";
			}
		}];
		
		PromFeeUserConfigGrid.superclass.constructor.call(this,{
			id : 'PromFeeUserConfigGrid',
			border : false,
			store : this.store,
			sm : new Ext.grid.RowSelectionModel(),
			columns : cm,
			clicksToEdit: 1,
	        viewConfig : {
	        	forceFit : true
	        },
			tbar : [' ',' ',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        }]
		})
	},
	initEvents : function(){
		PromFeeUserConfigGrid.superclass.initEvents.call(this);
		
		this.on('afteredit',this.afterEdit,this);
	},
	afterEdit : function(obj){
		if(obj.field =='rule_name'){
			var index = this.ruleStore.findExact('rule_name',obj.value);
			obj.record.data.rule_id = this.ruleStore.getAt(index).get('rule_id');
		}
	},
	addRecord : function(){
		var count = this.getStore().getCount();
		var RecordType = this.getStore().recordType;
		var record = new RecordType({
            'prom_fee_id':this.promFeeId,
            'user_no':this.idSequence,
            'user_fee':0,
            'prod_count':null,
            'rule_id':'',
            'rule_name':''
        });
        this.idSequence = this.idSequence +1;
        this.stopEditing();
        this.getStore().insert(count, record);
        this.startEditing(count, 0);
	},
	deleteRow : function(){
		this.getStore().remove(this.getSelectionModel().getSelected());
	},
	doValid : function(){
		var valid = true;
		this.getStore().each(function(record){
			if(!record.get('rule_id')){
				Alert("用户编号"+record.get('user_no')+"对应规则不能为空");
				valid = false;
				return false;
			}
		})
		
		return valid;
	},
	getValues : function(){
		var data = [];
		this.getStore().each(function(record){
			if(!record.data.prod_count){
				record.data.prod_count = null;
			}
			data.push(record.data);
		})
		return data;
	}
})

/**
 * 套餐用户配置窗口
 * @class PromFeeUserConfigWin
 * @extends Ext.Window
 */
PromFeeUserConfigWin = Ext.extend(Ext.Window,{
	promFeeId : null,
	grid : null,
	prom_fee:0,
	constructor : function(records,promFeeId,promFee){
		this.promFeeId = promFeeId;
		this.grid = new PromFeeUserConfigGrid(records,promFeeId);
		this.prom_fee=promFee;
		
		PromFeeUserConfigWin.superclass.constructor.call(this,{
			title : '套餐用户配置',
			layout : 'fit',
			height : 350,
			width : 500,
			closeAction : 'close',
			items : [this.grid],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	doSave : function(){
		this.grid.stopEditing();
		if(this.grid.doValid()){
			
			var data1 = this.grid.getValues();
			
	
			if(!Ext.isEmpty(data1)){
				
				var fee=0;
				for(var i=0;i<data1.length;i++){
					data1[i]["user_fee"]=data1[i]["user_fee"]*100;
					fee=fee+data1[i]["user_fee"];
				}
				
				if(fee!=this.prom_fee){
					Alert("规则总金额"+fee/100+"必须等于套餐总金额"+this.prom_fee/100);
					for(var i=0;i<data1.length;i++){
						data1[i]["user_fee"]=data1[i]["user_fee"]/100;
					}
					return;
				}
			}
			
			Confirm("确定保存吗?", this ,function(){
				var data = this.grid.getValues();
				
				var values = {};
				values['promFeeUserListStr'] = Ext.encode(data);
				values['promFeeId'] = this.promFeeId;
				
				Ext.Ajax.request({
					url :root + '/system/PromFee!savePromFeeUsers.action',
					params : values,
					scope : this,
					success : function(res,opt){
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							
							var check='';
							
							Ext.each(data,function(recored){
								if(!Ext.isEmpty(recored.prod_count)&&recored.prod_count>0){
									check=check+'请检查编号'+recored.user_no+'对应的套餐产品中可选产品数量是否>='+recored.prod_count+';';
								}
							});
							
							Alert('操作成功!'+check);
							
							Ext.getCmp('PromFeeUserGrid').getStore().reload();
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
				
			})
		}
	}
})

/**
 * 套餐产品
 * @class PromFeeProdGrid
 * @extends Ext.grid.GridPanel
 */
PromFeeProdGrid = Ext.extend(Ext.grid.GridPanel,{
	promFeeProdStore : null,
	promFeeId : null,
	record : null,
	userNo : null,
	dataMap : null,
	constructor : function(){
		this.promFeeProdStore = new Ext.data.JsonStore({
			fields : ['prom_fee_id','user_no','prod_id','prod_name','tariff_id',
			'tariff_name','real_pay','months','force_select','force_select_text','bind_prod_id','bind_prod_name']
		})
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var cm = [sm,{
			header : '产品名称',
			dataIndex : 'prod_name'
		},{
			header : '资费名称',
			dataIndex : 'tariff_name'
		},{
			header : '缴费月份',
			dataIndex : 'months'
		},{
			header : '实缴金额',
			dataIndex : 'real_pay'
		},{
			header : '必选',
			dataIndex : 'force_select_text'
		},{
		    header:'到期日绑定',
		    dataIndex:'bind_prod_name'}];
		
		PromFeeProdGrid.superclass.constructor.call(this,{
			id : 'PromFeeProdGrid',
			title : '套餐产品',
			border : false,
			region:'east',
			split : true,
			width : '60%',
			columns : cm,
			sm : sm,
			store : this.promFeeProdStore,
			tbar : [' ',' ',{
				id : 'PromFeeProdConfig',
	        	text : '配置',
	        	iconCls : 'icon-modify',
	        	scope : this,
	        	handler : this.doConfig
	        }]
		})
	},
	doConfig : function(){
		if(!this.promFeeId || !this.userNo){
			Alert("请选择一个套餐用户");
			return;
		}
		
		new PromFeeProdConfigWin(this.promFeeId,this.userNo,this.getStore().getRange()).show();
	},
	loadData : function(record,userNo){//加载所有产品数据
		if(record){
			//根据操作员地区，现在是否允许修改
			if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == record.get('county_id')){
				Ext.getCmp('PromFeeProdConfig').enable();
			}else{
				Ext.getCmp('PromFeeProdConfig').disable();
			}
			
			this.record = record;
			var promFeeId = record.get('prom_fee_id');
			
			if(!userNo && this.promFeeId && this.promFeeId == promFeeId){
				return ;
			}
			
			this.promFeeId = promFeeId;
			this.userNo = null;
		}
		
		this.getStore().removeAll();
		
		Ext.Ajax.request({
			url : root + '/system/PromFee!queryPromFeeProds.action',
			scope : this,
			params : {promFeeId : this.promFeeId},
			success : function(res,opt){
				this.dataMap = Ext.decode(res.responseText);
				if(userNo){
					this.showData(userNo);
				}
			}
		})
	},
	showData : function(userNo){//显示对应产品数据
		this.userNo = userNo;
		this.promFeeProdStore.removeAll();
		this.promFeeProdStore.loadData(this.dataMap[userNo]);
	}
})

/**
 * 套餐产品配置
 * @class PromFeeProdConfigGrid
 * @extends Ext.grid.EditorGridPanel
 */
PromFeeProdConfigGrid =  Ext.extend(Ext.grid.EditorGridPanel,{
	promFeeId : null,
	userNo : null,
	constructor : function(records,promFeeId,userNo){
		
		this.store = new Ext.data.JsonStore({
			fields : ['prom_fee_id','user_no','prod_id','prod_name','tariff_id',
			'tariff_name','real_pay','months','force_select','force_select_text','bind_prod_id','bind_prod_name']
		})
		Ext.each(records,function(rec){
			var rawData = rec.data;
			var newData = {};
			for(var key in rawData){
				newData[key] = rawData[key];
			}
			this.store.add(new Ext.data.Record(newData));
		},this);
		
		
		this.promFeeId = promFeeId;
		this.userNo = userNo;
		
		this.prodStore = new Ext.data.JsonStore({
			autoLoad : true,
			baseParams : {promFeeId:promFeeId},
			url : root + '/system/PromFee!queryProdAll.action',
			fields : ['prod_id','prod_name']
		})
		
		this.tariffStore = new Ext.data.JsonStore({
			url : root + '/system/PromFee!queryAllTariff.action',
			fields : ['tariff_id','tariff_name',{name : 'rent' ,type : 'float'}]
		});
		
		this.selectStore = new Ext.data.SimpleStore({
			fields:['item_value', 'item_name'],
			data: [
				['F','否'],
				['T','是']
			]
		});
		
		this.bindStore=new Ext.data.SimpleStore({
			fields:['prod_id','prod_name']
		})
		Ext.each(records,function(rec){
			var newData = {prod_id:rec.get('prod_id'),prod_name:rec.get('prod_name')};
			
			this.bindStore.add(new Ext.data.Record(newData));
		},this);
		
		this.bindEditor = new Ext.form.ComboBox({
				store : this.bindStore,editable : true,
				boxMinWidth : 100,minListWidth : 100,
				forceSelection:true,selectOnFocus:true,
				triggerAction:'all',mode:'local',
				displayField : 'prod_name',valueField : 'prod_id'
			}); 
		
		
		var cm = [{
			header : '产品名称',
			dataIndex : 'prod_name',
			editor : new Ext.form.ComboBox({
				store : this.prodStore,
				editable : true,
				boxMinWidth : 150,
				minListWidth : 150,
				forceSelection:true,
				allowBlank : false,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				displayField : 'prod_name',
				valueField : 'prod_name',
				listeners:{
					scope:this,
					select:function(combo,record,index){
						var target = this.selModel.getSelected();
						target.set('tariff_id','');
						target.set('tariff_name','');
					}
				}
			})
		},{
			header : '资费名称',
			dataIndex : 'tariff_name',
			editor : new Ext.form.ComboBox({
				store : this.tariffStore,
				editable : true,
				boxMinWidth : 200,
				minListWidth : 200,
				forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				displayField : 'tariff_name',
				valueField : 'tariff_name'
			})
		},{
			header : '缴费月份',
			dataIndex : 'months',
			editor : new Ext.form.NumberField({
				allowBlank : false,
				minValue : 0,
				allowNegative : false
			})
		},{
			header : '实缴金额',
			dataIndex : 'real_pay',
			editor : new Ext.form.NumberField({
				allowBlank : false,
				minValue : 0,
				allowNegative : false
			})
		},{
			header : '必选',
			dataIndex : 'force_select_text',
			editor : new Ext.form.ComboBox({
				store : this.selectStore,
				editable : true,
				boxMinWidth : 50,
				minListWidth : 50,
				forceSelection:true,
				allowBlank : false,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				displayField : 'item_name',
				valueField : 'item_name'
			})
		},{
		    header:'到期日绑定',width:150,
		    dataIndex:'bind_prod_id',editor:this.bindEditor,renderer:this.renderBindField,scope:this},
		    { header: '操作',
	        width:100,
	        scope:this,
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	return "<a href='#' onclick=Ext.getCmp(\'"+"PromFeeProdConfigGrid"+"\').deleteRow(); style='color:blue'>删除</a>";
			}
		}];
		
		PromFeeProdConfigGrid.superclass.constructor.call(this,{
			id : 'PromFeeProdConfigGrid',
			border : false,
			store : this.store,
			sm : new Ext.grid.RowSelectionModel(),
			columns : cm,
			clicksToEdit: 1,
	        viewConfig : {
	        	forceFit : true
	        },
			tbar : [' ',' ',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        }]
		})
	},
	initEvents : function(){
		PromFeeProdConfigGrid.superclass.initEvents.call(this);
		
		this.on('afteredit',this.afterEdit,this);
		this.on('beforeedit',this.beforeEdit,this);
	},
	renderBindField:function(v){
		var result = '';
		Ext.each(this.bindStore.getRange(0),function(rec){
			if(v == rec.get('prod_id')){
				result = rec.get('prod_name');
			}
		});
		return result;
	},
	beforeEdit : function(param){
		var record = param.record;
		var field = param.field;
		var shouldNotEdit = !Ext.isEmpty(record.get('bind_prod_id'));
		if((field =='months' || field == 'real_pay') && shouldNotEdit){
			return false;
		}
		
		
		if(field == 'bind_prod_id'){
			this.bindStore.removeAll();
			var array = [];
			Ext.each(this.store.getRange(0),function(rec){
				if(rec.get('prod_id') != record.get('prod_id') && rec.get('force_select') =='T'){
					var newData = {prod_name:rec.get('prod_name'),prod_id:rec.get('prod_id')};
					array.push(new Ext.data.Record(newData));
				}
			});
			array.push(new Ext.data.Record({prod_name:'取消绑定',prod_id:''}));
			this.bindStore.add(array);
		}else if(field == 'tariff_name'){
			if(record.data.prod_id){
				this.tariffStore.load({
					params : {
						prodId : record.data.prod_id,
						promFeeId : this.promFeeId
					}
				});
				
			}else{
				Alert("请先选择产品名称");
				this.stopEditing();
				return false;
			}
		}
	},
	afterEdit : function(obj){
		var field = obj.field;
		var value = obj.value;
		var record = obj.record;
		if(field =='prod_name'){
			var index = this.prodStore.findExact('prod_name',value);
			record.data.prod_id = this.prodStore.getAt(index).get('prod_id');
		}else if(field =='tariff_name'){
			if(value){
				var index = this.tariffStore.findExact('tariff_name',value);
				record.data.tariff_id = this.tariffStore.getAt(index).get('tariff_id');
			}else{
				record.data.tariff_id = null;
			}
		}else if(field =="force_select_text"){
	    	var index = this.selectStore.find('item_name',value);
	    	record.data.force_select = this.selectStore.getAt(index).get('item_value');
	    }else if(field == 'bind_prod_id' && !Ext.isEmpty(value)){
			record.set('months','0');
			record.set('real_pay','0');
		}
	},
	addRecord : function(){
		var count = this.getStore().getCount();
		var RecordType = this.getStore().recordType;
		var record = new RecordType({
            'prom_fee_id':this.promFeeId,
            'user_no':this.userNo,
            'prod_id':'',
            'prod_name':'',
            'tariff_id':'',
			'tariff_name':'',
			'real_pay':0,
			'months':'',
			'force_select':'T',
			'force_select_text' : '是'
        });
        this.stopEditing();
        this.getStore().insert(count, record);
        this.selModel.selectRow(this.store.getCount()-1);
        this.startEditing(count, 0);
	},
	deleteRow : function(){
		this.getStore().remove(this.getSelectionModel().getSelected());
	},
	doValid : function(){
		var valid = true;
		this.getStore().each(function(record){
			if(Ext.isEmpty(record.get('prod_id'))){
				Alert("产品不能为空");
				valid = false;
				return false;
			}
			var bind_prod_id = record.get('bind_prod_id');
			
			if( Ext.isEmpty(record.get('months')) && Ext.isEmpty(bind_prod_id)){
				Alert("月份不能为空");
				valid = false;
				return false;
			}
			if(!Ext.isEmpty(bind_prod_id)){
				this.beforeEdit({record:record,field:'bind_prod_id'});//确认bindStore里是最新的数据
				if(Ext.isEmpty(this.renderBindField(bind_prod_id))){//如果bindStore里没有对应的数据,那么返回的为空,否则一定有返回内容,由此判断
					Alert('选择的到期日期绑定对应的条目不存在,或者不是必选=是!');
					record.set('bind_prod_id','');
					valid = false;
					return false;
				}
			}
			if(!record.get('tariff_id')){
				Alert("资费不能为空");
				valid = false;
				return false;
			}
		},this)
		
		return valid;
	},
	getValues : function(){
		var data = [];
		this.getStore().each(function(record){
			record.data.real_pay = record.data.real_pay * 100;
			data.push(record.data);
		})
		return data;
	}
})

/**
 * 套餐产品配置窗口
 * @class PromFeeProdConfigWin
 * @extends Ext.Window
 */
PromFeeProdConfigWin = Ext.extend(Ext.Window,{
	promFeeId : null,
	userNo : null,
	grid : null,
	constructor : function(promFeeId,userNo,records){
		this.userNo = userNo;
		this.promFeeId = promFeeId;
		this.grid = new PromFeeProdConfigGrid(records,promFeeId,userNo);
		
		PromFeeProdConfigWin.superclass.constructor.call(this,{
			title : '套餐产品配置',
			layout : 'fit',
			height : 350,
			width : 500,
			closeAction : 'close',
			items : [this.grid],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	doSave : function(){
		if(this.grid.doValid()){
			Confirm("确定保存吗?", this ,function(){
				var data = this.grid.getValues();
				
				var values = {};
				values['promFeeProdListStr'] = Ext.encode(data);
				values['promFeeId'] = this.promFeeId;
				values['userNo'] = this.userNo;
				
				Ext.Ajax.request({
					url :root + '/system/PromFee!savePromFeeProds.action',
					params : values,
					scope : this,
					success : function(res,opt){
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							//null,取原来的promFEE
							Ext.getCmp('PromFeeProdGrid').loadData(null,this.userNo);
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
				
			})
		}
	}
})

/**
 * 套餐分成
 * @class PromFeeDivisionGrid
 * @extends Ext.grid.EditorGridPanel
 */
PromFeeDivisionGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	divisionStore : null,
	typeStore : null,
	promFeeId : null,
	allowModify : false,
	constructor : function(){
		this.divisionStore = new Ext.data.GroupingStore({
			url : root + '/system/PromFee!queryPromFeeDivision.action',
			reader: new Ext.data.JsonReader({
				fields:[{name:'prod_id',mapping:'prod_id'},
					{name:'prod_name',mapping:'prod_name'},
					{name:'prom_fee_id',mapping:'prom_fee_id'},
					{name:'user_no',mapping:'user_no'},
					{name:'real_pay',mapping:'real_pay'},
					{name:'tariff_name',mapping:'tariff_name'},
					{name:'percent_value',mapping:'percent_value'},
					{name:'percent',mapping:'percent'},
					{name:'type',mapping:'type'},
					{name:'type_text',mapping:'type_text'}
				]
			}),
			groupField:'type',
			sortInfo:{
				field:'type',direction:'DESC'
			}
		})
		
		PromFeeDivisionGrid.superclass.constructor.call(this,{
			id : 'PromFeeDivisionGrid',
			store : this.divisionStore,
			sm: new Ext.grid.CheckboxSelectionModel(),
			clicksToEdit: 1,
            columns: [
           	 	{header:'',dataIndex:'type',hidden:true},//该行不能少，否则报错
            	{header : '用户编号',dataIndex:'user_no'},
	            {header: '产品名称',dataIndex: 'prod_name',sortable: true,renderer: App.qtipValue},
	            {header: '产品资费',dataIndex: 'tariff_name'},
	            {header: '实缴金额',dataIndex: 'real_pay',renderer : Ext.util.Format.formatFee},
	            {header: '分成值',sortable: true,width: 60,dataIndex: 'percent_value',
	                editor: new Ext.form.NumberField({
	                    allowNegative : false,allowDecimals : false,minValue : 0
	                })
	            }
            ]
            ,view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl:'分成类型：{[values.rs[0].data["type_text"]]}'
        	})
		})
	},
	initEvents : function(){
		this.on('beforeedit',this.beforeEdit,this);
		
		PromFeeDivisionGrid.superclass.initEvents.call(this);
	},
	beforeEdit : function(obj){
		if(!this.allowModify){
			return false;
		}
	},
	loadData : function(record){
		
		//根据操作员地区，现在是否允许修改
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == record.get('county_id')){
			Ext.getCmp('PromFeeDivisionSave').enable();
			this.allowModify = true;
		}else{
			this.allowModify = false;
			Ext.getCmp('PromFeeDivisionSave').disable();
		}
		
		var promFeeId = record.get('prom_fee_id');
		
		this.divisionStore.removeAll();
		
		if(promFeeId){
			this.divisionStore.load({
				params : {
					promFeeId : promFeeId
				}
			})
		}
	},
	getValues : function(){
		var data = [];
		this.divisionStore.each(function(rec){
			if(rec.get('percent_value')){
				data.push(rec.data);
			}
		})
		return data;
	}
})

PromFeeConfigPanel = Ext.extend(Ext.Panel,{
	promFeeUserGrid : null,
	promFeeProdGrid : null,
	constructor : function(){
		this.promFeeProdGrid = new PromFeeProdGrid();
		this.promFeeUserGrid = new PromFeeUserGrid();
		
		PromFeeConfigPanel.superclass.constructor.call(this,{
			border : false,
			layout : 'border',
			items : [this.promFeeUserGrid,this.promFeeProdGrid]
		})
	}
})

PromFeeTab = Ext.extend(Ext.TabPanel,{
	promFeeConfigPanel : null,
	promFeeDivisionGrid : null,
	constructor : function(){
		
		this.promFeeConfigPanel = new PromFeeConfigPanel();
		this.promFeeDivisionGrid = new PromFeeDivisionGrid();
		
		PromFeeTab.superclass.constructor.call(this,{
			region : 'center',
			activeTab:0,
			border:false,
			defaults: {
				layout : 'fit',
				defaults: { tabWidth:250 }
			},
			items:[{
				title : '套餐详细配置',
				items:[this.promFeeConfigPanel]
			},{
				title : '分成配置',
				items:[this.promFeeDivisionGrid],
				tbar : [{
					id : 'PromFeeDivisionSave',
					text : '保存',
					scope : this,
					iconCls : 'icon-save',
					handler : this.doSave
				}]
			}]
		})
	},
	initEvents : function(){
		for (var i = 0; i < this.items.length; i++) {
			var p = this.items.itemAt(i);
			p.on("activate", this.refreshPanel, this );
		}
		
		PromFeeTab.superclass.initEvents.call(this);
	},
	refreshPanel : function(p){
		var content = p.items.itemAt(0);
		if(content.id == 'PromFeeDivisionGrid'){
//			content.loadData(this.promFeeConfigPanel.promFeeUserGrid.promFeeId);
			var record = Ext.getCmp('PromFeeGrid').getSelectionModel().getSelected();
			if(record){
				content.loadData(record);
			}
		}
	},
	doSave : function(){
		var data = this.promFeeDivisionGrid.getValues();
		if(data.length == 0){
			Alert("没有分成值需要保存");
			return;
		}
		
		Confirm("确定保存吗?", this ,function(){
			var params = {};
			params['promFeeId'] = this.promFeeConfigPanel.promFeeUserGrid.promFeeId;
			params['promFeeDivisonListStr'] = Ext.encode(data);
			
			
			Ext.Ajax.request({
				url :root + '/system/PromFee!savePromFeeDivision.action',
				params : params,
				scope : this,
				success : function(res,opt){
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						this.promFeeDivisionGrid.getStore().reload();
						this.close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
			
		})
		
		
		
	}
})

PromFeeView = Ext.extend(Ext.Panel,{
	constructor : function(){
		PromFeeView.superclass.constructor.call(this,{
			id : 'PromFeeView',
			layout : 'border',
			title : '套餐缴费配置管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new PromFeeGrid(),new PromFeeTab()]
		})
	}
})