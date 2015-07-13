/**
 * 促销配置
 */
Ext.ns('Promotion');

/**
 * 促销主题配置
 * @class PromotionThemeGrid
 * @extends Ext.grid.GridPanel
 */
PromotionThemeGrid = Ext.extend(Ext.grid.GridPanel,{
	themeStore : null,
	constructor : function(){
		promThemethis = this;
		this.themeStore = new Ext.data.JsonStore({
			url : root + '/system/Promotion!queryPromThemes.action',
			fields :['theme_id','theme_name','promotion_desc','promotion_type','orgarize_type','county_id','area_id','county_name','area_name']
		});
		
		var cm = [{
			header : '主题名称',
			width : 200,
			id : 'theme',
			dataIndex : 'theme_name'
		},{
			header : '地区',
			width : 80,
			id : 'area_id',
			dataIndex : 'area_name'
		},{
			header : '县市',
			width : 80,
			id : 'county_id',
			dataIndex : 'county_name'
		},{
			header : '促销描述',
			width : 60,
			hidden : true,
			dataIndex : 'promotion_desc'
		},{
			header : '促销类型',
			width : 60,
			hidden : true,
			dataIndex : 'promotion_type'
		},{
			header : '组织形式',
			width : 60,
			hidden : true,
			dataIndex : 'orgarize_type'
		},{ header: '操作',
	        width:80,
	        scope:this,
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}
		}];
		var sm = new Ext.grid.CheckboxSelectionModel({});
		PromotionThemeGrid.superclass.constructor.call(this,{
			id : 'PromotionThemeGrid',
			title : '促销主题配置',
			region : 'west',
			width : '35%',
			split : true,
			collapseMode:'mini',
			store : this.themeStore,
			sm:sm,
			columns : cm,
	        tbar : [' ',' ','输入关键字' , ' ',       //搜索功能
				new Ext.ux.form.SearchField({  
	                store: this.themeStore,
	                width: 140,
	                hasSearch : true,
	                emptyText: '输入主题名称查询'
	            }),'-',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        }]
		});
	},
	initEvents : function(){
		this.on('rowclick',this.doClick,this);
		PromotionThemeGrid.superclass.initEvents.call(this);
	},doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501'
			|| App.data.optr['county_id'] == record.get('county_id')
			|| App.data.optr['county_id'] == record.get('area_id')){
			btns = btns + "<a href='#' onclick='promThemethis.modifyRec();' style='color:blue'>修改</a>";
		}
		return btns;
	},
	doClick : function(grid,rowIndex,e){
		var themeId = grid.getStore().getAt(rowIndex).get('theme_id');
		Ext.getCmp('PromotionGrid').remoteRefresh(themeId);
	},
	addRecord : function(){
		var win = new PromotionThemeWin();
		win.setTitle('增加促销主题');
		win.show();
	},
	modifyRec : function(){
		var record = this.getSelectionModel().getSelected();
		var win = new PromotionThemeWin(record);
		win.setTitle('修改促销主题');
		win.show();
	}
});

PromotionThemeTree = Ext.extend(Ext.tree.TreePanel, {
    prodId: null,
    constructor: function (v) {
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Promotion!getThemeCountyTree.action?query=" + v        
        });
        PromotionThemeTree.superclass.constructor.call(this, {
            region: 'east',
            width: '40%',
            border : false,
            split: true,
            title: '应用地市',
            id: 'PromotionThemeTreeId',
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
    }
});

/**
 * 促销主题增加修改窗口
 * @class PromotionThemeWin
 * @extends Ext.Window
 */
PromotionThemeWin = Ext.extend(Ext.Window,{
	form : null,
	record : null,
	tree : null,
	themeId : null,
	constructor : function(record){
		if(record){
			this.themeId = record.get('theme_id');
		}
		this.tree = new PromotionThemeTree(this.themeId);
		this.record = record;
		
		var formItems = [{
			xtype : 'hidden',
			id : 'themeId',
			name : 'theme_id'
		},{
			fieldLabel : '促销主题名',
			name : 'theme_name',
			allowBlank : false
		},{
			fieldLabel : '促销类型',
			maxLength : 8,
			name : 'promotion_type'
		},{
			fieldLabel : '组织形式',
			maxLength : 8,
			name : 'orgarize_type'
		},{
			fieldLabel : '促销描述',
			xtype:'textarea',width:180,
			height:80,
			name : 'promotion_desc'
		}];
		
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 85,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			defaults : {
				xtype : 'textfield'
			},
			items : formItems
		});
		
		PromotionThemeWin.superclass.constructor.call(this,{
			layout : 'border',
			height : 450,
			width : 600,
			closeAction : 'close',
			items : [{
					region : 'west',
					layout : 'fit',
					split : true,
					width : '50%',
					items : [this.form]
				},{
					region : 'center',
					layout : 'fit',
					items : [this.tree]
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
		
		if(this.record){
			this.form.getForm().loadRecord(this.record);
		}
	},
	doSave : function(){
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
//				mb = Show();//显示正在提交
				var url = '';
				if(Ext.getCmp('themeId').getValue()){
					url = root + '/system/Promotion!editPromTheme.action';
				}else{
					url = root + '/system/Promotion!savePromTheme.action';
				}
				
				var old =  this.form.getForm().getValues(), newValues = {};
				for(var key in old){
					newValues["promotionTheme." + key] = old[key];
				}
				
				var promCountyId = [];
			    if (Ext.getCmp('PromotionThemeTreeId')) {
		            var nodes = Ext.getCmp('PromotionThemeTreeId').getChecked();
		            for (var i in nodes) {
		                if (nodes[i].leaf) {
		                    promCountyId.push(nodes[i].id);
		                }
		            }
		        }
		        newValues["promCountyIds"] = promCountyId.join(",");
				Ext.Ajax.request({
					url :url,
					params : newValues,
					scope : this,
					success : function(res,opt){
//						mb.hide();//隐藏提示框
//						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('PromotionThemeGrid').getStore().reload();
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			})
		}
	}
});

/**
 * 促销信息表格
 * @class PromotionGrid
 * @extends Ext.grid.GridPanel
 */
PromotionGrid = Ext.extend(Ext.grid.GridPanel,{
	promotionStore : null,
	optr : null,
	themeId : null,
	ruleStore : null,
	county:null,
	constructor : function(){
		this.promotionStore = new Ext.data.JsonStore({
			url : root + '/system/Promotion!queryProms.action',
			fields :['promotion_id','promotion_name','rule_name','status_text','theme_id','theme_name','priority',
			'protocol_id','total_acct_fee' ,'total_acct_count' ,'rule_id','auto_exec','auto_exec_text','eff_date','repetition_times',
			'exp_date','feeList','acctList','giftList','cardList','county_id','days','times']
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		})
		var cm = [sm,{
			header : '促销名称',
			width : 80,
			dataIndex : 'promotion_name'
		},{
			header : '主题名称',
			width : 80,
			dataIndex : 'theme_name'
		},{
			header : '规则名',
			width : 80,
			dataIndex : 'rule_name'
		},{
			header : '自动执行',
			width : 80,
			dataIndex : 'auto_exec_text'
		},{
			header : '生效日期',
			width : 80,
			dataIndex : 'eff_date',
			renderer : Ext.util.Format.dateFormat
		},{
			header : '失效日期',
			dataIndex : 'exp_date',
			width : 80,
			renderer : Ext.util.Format.dateFormat
		},{
			header : '账目赠送总额',
			width : 80,
			dataIndex : 'total_acct_fee',
			renderer : Ext.util.Format.formatFee
		},{
			header : '产品赠送总数',
			width : 80,
			dataIndex : 'total_acct_count'
		},{
			header : '协议编号',
			width : 60,
			dataIndex : 'protocol_id'
		},{
			header : '优先级',
			width : 60,
			dataIndex : 'priority'
		},{
			header : '重复次数',
			width : 60,
			dataIndex : 'repetition_times'
		},{
			header : '天数',
			width : 60,
			dataIndex : 'days'
		},{
			header : '促销次数',
			width : 60,
			dataIndex : 'times'
		},{ header: '操作',
	        width:170,
	        scope:this,
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}
		}];
		PromotionGrid.superclass.constructor.call(this,{
			id : 'PromotionGrid',
			title : '促销配置',
			region : 'center',
			store : this.promotionStore,
			columns : cm,
			sm : sm,
	        tbar : [' ',' ',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	id : 'AddPromConfig',
	        	scope : this,
	        	disabled : true,
	        	handler : this.addRecord
	        }]
		});
		
		Ext.Ajax.request({
			scope : this,
			url : root + '/system/Promotion!queryOptr.action',
			async : false,
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				this.optr = data;
			}
		});
		
		//规则编号数据
		this.ruleStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/system/Promotion!queryRules.action',
			fields : ['rule_id','rule_name']
		});
		
	},
	initEvents : function(){
		this.on('rowclick',this.doClick,this);
		this.promotionStore.on('load',this.doLoad,this);
		PromotionGrid.superclass.initEvents.call(this);
	},
	doLoad : function(store,records){//金额相关数值转化为元
		for(var k=0;k<records.length;k++){
			var feeList = records[k].get('feeList');
			for(var i=0;i<feeList.length;i++){
				feeList[i].disct_value = parseFloat(Ext.util.Format.convertToYuan(feeList[i].disct_value));
			}
			
			var acctList = records[k].get('acctList');
			for(var i=0;i<acctList.length;i++){
				acctList[i].active_amount = parseFloat(Ext.util.Format.convertToYuan(acctList[i].active_amount));
				acctList[i].fee = parseFloat(Ext.util.Format.convertToYuan(acctList[i].fee));
			}
			
			var giftList = records[k].get('giftList');
			for(var i=0;i<giftList.length;i++){
				giftList[i].money = parseFloat(Ext.util.Format.convertToYuan(giftList[i].money));
			}
			
			var cardList = records[k].get('cardList');
			for(var i=0;i<cardList.length;i++){
				cardList[i].card_value = parseFloat(Ext.util.Format.convertToYuan(cardList[i].card_value));
			}
		}
		Ext.getCmp('PromotionDetailPanel').doReset();
		var promotionId = Ext.getCmp('PromotionDetailPanel').promotionId;
		if(promotionId){
			for(var i=0;i<records.length;i++){
				if(promotionId == records[i].get('promotion_id')){
					this.getSelectionModel().selectRow(i);
					Ext.getCmp('FeePromotionGrid').loadData(records[i],records[i].get('feeList'));
					Ext.getCmp('AcctPromotionGrid').loadData(records[i],records[i].get('acctList'));
					Ext.getCmp('GiftPromotionGrid').loadData(records[i],records[i].get('giftList'));
					Ext.getCmp('CardPromotionGrid').loadData(records[i],records[i].get('cardList'));
				}
			}
		}
	},doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == record.get('county_id')){
			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"PromotionGrid"+"\').modifyRec(); style='color:blue'>修改</a>";
		}
		return btns;
	},
	doClick : function(grid,rowIndex){
		var record = grid.getStore().getAt(rowIndex);

//		if(record.get('auto_exec') == 'T'){
//			Ext.getCmp('addAcctProm').setDisabled(false);
//			Ext.getCmp('addFeeProm').setDisabled(true);
//			Ext.getCmp('addCardProm').setDisabled(true);
//			Ext.getCmp('addGiftProm').setDisabled(true);
//		}else{
//			Ext.getCmp('addAcctProm').setDisabled(false);
//			Ext.getCmp('addFeeProm').setDisabled(false);
//			Ext.getCmp('addCardProm').setDisabled(false);
//			Ext.getCmp('addGiftProm').setDisabled(false);
//		}
		
		this.county = record.get('county_id');
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == record.get('county_id')){
			Ext.getCmp('savePromotion').setDisabled(false);
			Ext.getCmp('addAcctProm').setDisabled(false);
			Ext.getCmp('addFeeProm').setDisabled(false);
			Ext.getCmp('addCardProm').setDisabled(false);
			Ext.getCmp('addGiftProm').setDisabled(false);
			if(record.get('auto_exec') == 'T'){
				Ext.getCmp('addAcctProm').setDisabled(false);
				Ext.getCmp('addFeeProm').setDisabled(true);
				Ext.getCmp('addCardProm').setDisabled(true);
				Ext.getCmp('addGiftProm').setDisabled(true);
			}
		}else{
			Ext.getCmp('savePromotion').setDisabled(true);
			Ext.getCmp('addAcctProm').setDisabled(true);
			Ext.getCmp('addFeeProm').setDisabled(true);
			Ext.getCmp('addCardProm').setDisabled(true);
			Ext.getCmp('addGiftProm').setDisabled(true);
		}
		
		Ext.getCmp('FeePromotionGrid').loadData(record,record.get('feeList'));
		
		Ext.getCmp('AcctPromotionGrid').loadData(record,record.get('acctList'));
		
		Ext.getCmp('GiftPromotionGrid').loadData(record,record.get('giftList'));
		
		Ext.getCmp('CardPromotionGrid').loadData(record,record.get('cardList'));
	},
	remoteRefresh : function(themeId){
		Ext.getCmp('AddPromConfig').setDisabled(false);
		if(this.themeId == null || this.themeId != themeId){
			this.getStore().load({
				params : {
					themeId : themeId
				}
			});
			this.themeId = themeId;
		}
	},
	addRecord : function(){
		var win = new PromotionWin(this.optr,null,this.ruleStore,this.themeId);
		win.setTitle('增加促销记录');
		win.setIconClass('icon-add-user');
		win.show();
	},
	modifyRec : function(){
		var record = this.getSelectionModel().getSelected();
		var win = new PromotionWin(this.optr,record,this.ruleStore,this.themeId);
		win.setTitle('修改促销记录');
		win.setIconClass('icon-edit-user');
		win.show();
	},
	deleteRec : function(){
		Confirm("确定删除吗?", this ,function(){
//			mb = Show();//显示正在提交
			
			var promotionId = this.getSelectionModel().getSelected().get('promotion_id');
			
			Ext.Ajax.request({
				url :root + '/system/Promotion!removePromotion.action',
				params : {
					promotionId : promotionId	
				},
				scope : this,
				success : function(res,opt){
//					mb.hide();//隐藏提示框
//					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						Ext.getCmp('PromotionGrid').getStore().reload();
						Ext.getCmp('PromotionDetailPanel').doReset();
						this.close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		})
	}
});

PromotionCountyTree = Ext.extend(Ext.tree.TreePanel, {
    prodId: null,
    constructor: function (v,themeId) {
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Promotion!getCountyTree.action?query=" + v+"&themeId="+themeId      
        });
        PromotionCountyTree.superclass.constructor.call(this, {
            region: 'east',
            width: '40%',
            border : false,
            split: true,
            title: '应用地市',
            id: 'PromotionCountyTreeId',
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
    }
});

/**
 * 添加或修改促销
 * @class PromotionWin
 * @extends Ext.Window
 */
PromotionWin = Ext.extend(Ext.Window,{
	form : null,
	record : null,
	optr : null,//操作员
	tree : null,
	constructor : function(optr,record,ruleStore,themeId){
		this.record = record;
		this.optr = optr;
		if(this.record){
			this.tree = new PromotionCountyTree(record.get('promotion_id'),themeId);
		}else{
			this.tree = new PromotionCountyTree(null,themeId);
		}
		
		
		
		var formItems = [{
			xtype : 'hidden',
			id : 'promotionId',
			name : 'promotion_id'
		},{
			fieldLabel : '促销名称',
			name : 'promotion_name',
			width : 150,
			allowBlank : false
		},{
			fieldLabel : '主题名称',
			xtype : 'combo',
			width : 150,
			store : Ext.getCmp('PromotionThemeGrid').getStore(),
			displayField : 'theme_name',
			valueField : 'theme_id',
			hiddenName : 'theme_id',
			editable : false,
			readOnly : true,
			value : Ext.getCmp('PromotionGrid').themeId,
			allowBlank : false
		},{
			fieldLabel : '协议编号',
			name : 'protocol_id',
			width : 150
		},{
			fieldLabel : '规则名称',
			allowBlank : false,
			width : 150,
			xtype :'combo',
			store : ruleStore,
			displayField : 'rule_name',
			valueField : 'rule_id',
			forceSelection : true,
			editable : true,
			boxMinWidth : 250,
			minListWidth : 250,
			hiddenName : 'rule_id'
		},{
			xtype : 'paramcombo',
			fieldLabel : '自动执行',
			paramName : 'BOOLEAN',
			id : 'autoExecForProm',
			width : 150,
			defaultValue : 'F',
			allowBlank : false,
			hiddenName : 'auto_exec'
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
		},{
			xtype : 'numberfield',
			fieldLabel : '账目赠送总额',
			id : 'total_acct_fee',
			name : 'total_acct_fee',
			width : 150,
			allowNegative : false
		},{
			xtype : 'numberfield',
			fieldLabel : '产品赠送总数',
			id : 'total_acct_count',
			minValue : 1,
			name : 'total_acct_count',
			width : 150,
			allowNegative : false
		},{
			xtype : 'numberfield',
			fieldLabel : '优先级',
			id : 'priorityForProm',
			name : 'priority',
			width : 150,
			allowNegative : false
		},{
			xtype : 'numberfield',
			fieldLabel : '重复次数',
			name : 'repetition_times',
			allowBlank : false,
			maxValue : 99,
			width : 150,
			allowNegative : false
		},{
			xtype : 'numberfield',
			fieldLabel : '天数',
			name : 'days',
			allowBlank : false,
			emptyText:'多少天内促销',
			blankText:'多少天内促销',
			value:30,
			width : 150,
			allowNegative : false,
			allowDecimals: false
		},{
			xtype : 'numberfield',
			fieldLabel : '促销次数',
			name : 'times',
			emptyText:'多少天内促销次数',
			blankText:'多少天内促销次数',
			allowBlank : false,
			value:1,
			width : 150,
			allowNegative : false,
			allowDecimals: false
		}];
		
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
		App.form.initComboData( this.form.findByType("paramcombo"),this.doInit,this);
		
		PromotionWin.superclass.constructor.call(this,{
			layout : 'border',
			height : 450,
			width : 600,
			closeAction : 'close',
			items : [{
					region : 'west',
					layout : 'fit',
					split : true,
					width : '50%',
					items : [this.form]
				},{
					region : 'center',
					layout : 'fit',
					items : [this.tree]
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
	doInit : function(){
		if(this.record){
			if(this.record.get('eff_date') && typeof(this.record.get('eff_date')) == 'string'){
				this.record.data.eff_date = Date.parseDate(this.record.get('eff_date'),'Y-m-d h:i:s')
			}
			if(this.record.get('exp_date') && typeof(this.record.get('exp_date')) == 'string'){
				this.record.data.exp_date = Date.parseDate(this.record.get('exp_date'),'Y-m-d h:i:s')
			}
			this.form.getForm().loadRecord(this.record);
			var cmp = Ext.getCmp('total_acct_fee');
			cmp.setValue(Ext.util.Format.formatFee(cmp.getValue()));
		}
	},
	doSave : function(){
		if(this.form.getForm().isValid()){
			if(Ext.getCmp('autoExecForProm').getValue() == 'T' && !Ext.getCmp('priorityForProm').getValue()){
				Alert('自动执行的促销必选输入优先级');
				return;
			}
			Confirm("确定保存吗?", this ,function(){
//				mb = Show();//显示正在提交
				var url = '';
				if(Ext.getCmp('promotionId').getValue()){
					url = root + '/system/Promotion!editPromotion.action';
				}else{
					url = root + '/system/Promotion!savePromotion.action';
				}
				
				var old =  this.form.getForm().getValues(), newValues = {};
				old['total_acct_fee'] = old['total_acct_fee'] * 100;
				for(var key in old){
					newValues["promotion." + key] = old[key];
				}
				
				var promCountyId = [];
			    if (Ext.getCmp('PromotionCountyTreeId')) {
		            var nodes = Ext.getCmp('PromotionCountyTreeId').getChecked();
		            for (var i in nodes) {
		                if (nodes[i].leaf) {
		                    promCountyId.push(nodes[i].id);
		                }
		            }
		        }
		        
		        newValues["promCountyIds"] = promCountyId.join(",");
				
				Ext.Ajax.request({
					url :url,
					params : newValues,
					scope : this,
					success : function(res,opt){
//						mb.hide();//隐藏提示框
//						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('PromotionGrid').getStore().reload();
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
 * 促销种类公用父类
 * @class ComPromGrid
 * @extends Ext.grid.EditorGridPanel
 */
ComPromGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	promotionId : null,
	totalAcctFee :null,
	totalAcctCount : null,
	repetitionTimes : null,
	autoExec : null,
	constructor : function(cfg){
		ComPromGrid.superclass.constructor.call(this,cfg);
	},
	initEvents : function(){
		this.on("afteredit", this.afterEdit, this);
		ComPromGrid.superclass.initEvents.call(this);
	},
	afterEdit : function(obj){
		
	},
	checkValid : function(){//验证配置不能重复,且不允许出现单元为空
		if(this.getStore().getCount() > 0){//检验第一条数据是否输入完整
			var rec = this.getStore().getAt(0).data;
			for(var key in rec){
				if(Ext.isEmpty(rec[key])){
					return "数据没有编辑完整";
				}
			}
		}
		//检验第一条以后的数据是否输入完整以及是否存在两个配置相同
		for(var i=1;i<this.getStore().getCount();i++){//第二个开始依次向前进行比较
			for(var k=i;k>0;k--){
				var flag = true;//默认两个对象的属性值相同
				for(var key in this.getStore().getAt(i).data){
					if(Ext.isEmpty(this.getStore().getAt(i).data[key])){//检验第一条以后的数据是否输入完整
						return "数据没有编辑完整";
					}
					if(this.getStore().getAt(i).data[key] != this.getStore().getAt(k-1).data[key]){
						flag = false;//属性值不同
						break;
					} 
				}
				if(flag){
					return "第"+(i+1)+"个配置和第"+(k)+"个配置相同，请重新编辑";
				}
			}
		}
    	return true;
	},
	addRecord : function(rec){
		if(Ext.getCmp('PromotionGrid').getSelectionModel().getSelections().length == 0){
			Alert('请先选择一条促销记录。');
			return;
		}
		var Plant= this.getStore().recordType;
		var p = new Plant(rec);
		this.stopEditing();
		this.getStore().insert(0,p);
		this.startEditing(0,0);
	},
	loadData : function(record,data){
		this.repetitionTimes = record.get('repetition_times');
		this.autoExec = record.get('auto_exec');
		this.promotionId = record.get('promotion_id');
		this.totalAcctCount = record.get('total_acct_count');
		this.totalAcctFee = Ext.util.Format.formatFee(record.get('total_acct_fee'));
		this.getStore().loadData(data);
		
		if(this.autoExec == 'T'){
			Ext.getCmp('AcctPromotionGrid').getColumnModel().setEditable(8,false);
		}else{
			Ext.getCmp('AcctPromotionGrid').getColumnModel().setEditable(8,true);
		}
	},
	deleteRow : function(){//删除行
		Confirm("确定要删除该数据吗?", this ,function(){
			Ext.getCmp('savePromotion').setDisabled(false);//激活保存按钮
			var record= this.getSelectionModel().getSelected();
			this.getStore().remove(record);
		});
	}
})

/**
 * 费用促销
 * @class FeePromotionGrid
 * @extends Ext.grid.EditorGridPanel
 */
FeePromotionGrid = Ext.extend(ComPromGrid,{
	feeListStore : null,
	deviceModelStore : null,//设备型号
	busiFeeStore : null,//业务费用
	constructor : function(){
		
		this.feeListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'device_model'},
				{name : 'device_model_name'},
				{name : 'disct_value', type : 'float'},
				{name : 'fee_id'},
				{name : 'fee_name'},
				{name : 'fee_type'},
				{name : 'promotion_id'}
			]
		})
		
		this.deviceModelStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/system/Promotion!queryAllModel.action',
			fields : ['device_model','model_name']
		});
		
		this.busiFeeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/system/Promotion!queryAllFee.action',
			fields : ['fee_id','fee_name','fee_type']
		});
		
		var feeColumns = [{
			header :'费用名称',
			dataIndex : 'fee_name',
			editor : new Ext.form.ComboBox({
				store :this.busiFeeStore,
				forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				displayField : 'fee_name',
				valueField : 'fee_name'
			})
		},{
			header : '设备型号',
			dataIndex : 'device_model_name',
			editor : new Ext.form.ComboBox({
				store : this.deviceModelStore,
				forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				displayField : 'model_name',
				valueField : 'model_name'
			})
		},{
			header : '优惠后金额',
			dataIndex : 'disct_value',
			editor : new Ext.form.NumberField({
				allowBlank : false
			})
		},{ header: '操作',
	        width:170,
	        scope:this,
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}
		}];
		
		FeePromotionGrid.superclass.constructor.call(this,{
			id : 'FeePromotionGrid',
			sm : new Ext.grid.CheckboxSelectionModel(),
			store : this.feeListStore,
			columns : feeColumns,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [{
	        	text : '添加',
	        	scope : this,
	        	id :'addFeeProm',
	        	disabled : true,
	        	iconCls : 'icon-add',
	        	handler : this.addRecord
	        }]
		});
	},
	initEvents : function(){
		this.on("afteredit", this.afterEdit, this);
		this.on('beforeedit',this.beforeEdit,this);
		ComPromGrid.superclass.initEvents.call(this);
	},doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == Ext.getCmp('PromotionGrid').county){
			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"FeePromotionGrid"+"\').deleteRow(); style='color:blue'>删除</a>";
		}
		return btns;
	},
	beforeEdit : function(obj){
		if(obj.field == 'device_model_name'){
			if(Ext.isEmpty(obj.record.data.fee_name)){
				Alert('请先选择费用名称。')
				return false;
			}else{
				if(obj.record.data.fee_type != 'DEVICE'){
					Alert(obj.record.data.fee_name+'不是设备类型费用，无需选择设备型号');
					return false;
				}
			}
		}
	},
	afterEdit : function(obj){
		if(obj.field == 'device_model_name'){
			var index = this.deviceModelStore.findExact('model_name',obj.value);
			obj.record.data.device_model = this.deviceModelStore.getAt(index).get('device_model');
		}else if(obj.field == 'fee_name'){
			var index = this.busiFeeStore.findExact('fee_name',obj.value);
			obj.record.data.fee_id = this.busiFeeStore.getAt(index).get('fee_id');
			obj.record.data.fee_type = this.busiFeeStore.getAt(index).get('fee_type');
		}
	},
	deleteRow : function(){
		FeePromotionGrid.superclass.deleteRow.call(this);
	},
	addRecord : function(){
		var obj = {
			'promotion_id' : this.promotionId,
			'device_type' : '',
			'disct_value' : 0,
			'fee_id' : ''
		}
		//必须调用父类方法
		FeePromotionGrid.superclass.addRecord.call(this,obj);
	},
	checkValid : function(){//验证配置不能重复,且不允许出现单元为空
		var count = this.getStore().getCount();
		if( count > 0){//检验第一条数据是否输入完整
			for(var i=0;i<count;i++){
				var rec = this.getStore().getAt(i);
				if(rec.get('fee_type') == 'DEVICE'){
					if(Ext.isEmpty(rec.get('fee_name')) || Ext.isEmpty(rec.get('device_model_name'))
							|| Ext.isEmpty(rec.get('disct_value')) ){
						return "数据没有编辑完整";
					}
				}else{
					if(Ext.isEmpty(rec.get('fee_name')) || Ext.isEmpty(rec.get('disct_value')) ){
						return "数据没有编辑完整";
					}
				}
			}
		}
		//检验第一条以后的数据是否输入完整以及是否存在两个配置相同
		for(var i=1;i<count;i++){//第二个开始依次向前进行比较
			for(var k=i;k>0;k--){
				var flag = true;//默认两个对象的属性值相同
				for(var key in this.getStore().getAt(i).data){
					if(Ext.isEmpty(this.getStore().getAt(i).data[key])){//检验第一条以后的数据是否输入完整
						return "数据没有编辑完整";
					}
					if(this.getStore().getAt(i).data[key] != this.getStore().getAt(k-1).data[key]){
						flag = false;//属性值不同
						break;
					} 
				}
				if(flag){
					return "第"+(i+1)+"个配置和第"+(k)+"个配置相同，请重新编辑";
				}
			}
		}
    	return true;
	}
})

/**
 * 礼品促销
 * @class GiftPromotionGrid
 * @extends Ext.grid.EditorGridPanel
 */
GiftPromotionGrid = Ext.extend(ComPromGrid,{
	giftListStore : null,
	constructor : function(){
		this.giftListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'gift_type'},
				{name : 'money', type : 'float'},
				{name : 'amount'},
				{name : 'promotion_id'}
			]
		});
		var giftColumns = [{
			header : '促销ID',
			dataIndex : 'promotion_id',
			hidden : true
		},{
			header : '礼品类型',
			dataIndex : 'gift_type',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '价值',
			dataIndex : 'money',
			editor : new Ext.form.NumberField({
				allowBlank : false
			})
		},{
			header : '数量',
			dataIndex : 'amount',
			editor : new Ext.form.NumberField({
				allowBlank : false
			})
		},{ header: '操作',
	        width:170,
	        scope:this,
	        dataIndex : 'promotion_id',
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}		
		}];
		
		//删除行
	    deleteRow = function(){
			Confirm("确定要删除该数据吗?", this ,function(){
				Ext.getCmp('savePromotion').setDisabled(false);//激活保存按钮
				var record= Ext.getCmp('GiftPromotionGrid').getSelectionModel().getSelected();
				Ext.getCmp('GiftPromotionGrid').getStore().remove(record);
			});
		};
		
		GiftPromotionGrid.superclass.constructor.call(this,{
			id : 'GiftPromotionGrid',
			sm : new Ext.grid.CheckboxSelectionModel(),
			store : this.giftListStore,
			columns : giftColumns,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	id : 'addGiftProm',
	        	disabled : true,
	        	handler : this.addRecord
	        }]
		});
	},
	afterEdit : function(obj){
		if(obj.field == 'money'){
			obj.value = obj.value * 100;
		}
	},doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == Ext.getCmp('PromotionGrid').county){
			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"GiftPromotionGrid"+"\').deleteRow(); style='color:blue'>删除</a>";
		}
		return btns;
	},
	deleteRow : function(){
		GiftPromotionGrid.superclass.deleteRow.call(this);
	},
	addRecord : function(){
		var obj = {
			'promotion_id' : this.promotionId,
			'gift_type' : '',
			'money' : 0,
			'amount' : 0
		}
		//必须调用父类方法
		GiftPromotionGrid.superclass.addRecord.call(this,obj);
	}
})

/**
 * 账户优惠
 * @class AcctPromotionGrid
 * @extends Ext.grid.EditorGridPanel
 */
AcctPromotionGrid = Ext.extend(ComPromGrid,{
	acctListStore : null,
	acctItemStore : null,//账目数据
	tariffStore : null,//资费数据
	booleanStore : null,//是否必选
	constructor : function(){
		this.acctListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'acctitem_id'},
				{name : 'acctitem_name'},
				{name : 'present_type'},
				{name : 'present_type_text'},
				{name : 'present_month'},
				{name : 'active_amount' ,type : 'float'},
				{name : 'cycle'},
				{name : 'fee', type : 'float'},
				{name : 'repetition_times', type : 'float'},
				{name : 'tariff_id'},
				{name : 'tariff_name'},
				{name : 'necessary'},
				{name : 'necessary_text'},
				{name : 'promotion_id'}
			]
		});
		
		this.acctItemStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/system/Promotion!queryAllAcct.action',
			fields : ['acctitem_id','acctitem_name']
		});
		this.tariffStore = new Ext.data.JsonStore({
			url : root + '/system/Promotion!queryAllTariff.action',
			fields : ['tariff_id','tariff_name',{name : 'rent' ,type : 'float'}]
		});
		
		//是否必选
		this.booleanStore = new Ext.data.SimpleStore({
			fields:['value', 'text'],
			data: [
				['F','否'],
				['T','是']
			]
		});
		
		var acctColumns = [{
			header : '账目名称',
			dataIndex : 'acctitem_name',
			editor : new Ext.form.ComboBox({
				store :this.acctItemStore,
				forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
				mode:'local',
				boxMinWidth : 150,
				editable : true,
				allowBlank : false,
				displayField : 'acctitem_name',
				valueField : 'acctitem_name'
			})
		},{
			header : '资费名称',
			id :'tariff_name',
			dataIndex : 'tariff_name',
			editor: new Ext.form.TextField()
		},{
			header : '赠送类型',
			dataIndex : 'present_type_text',
			editor: new Ext.ux.ParamCombo({
				allowBlank:false,
				id : 'presentType',
				forceSelection:true,
				selectOnFocus:true,
				editable : true,
				valueField : 'item_name',
				typeAhead:false,
				paramName:'ACCT_PRESENT_TYPE'
			})
		},{
			header : '赠送月数',
			dataIndex : 'present_month',
			editor : new Ext.form.NumberField({
				allowBlank : false,
				allowNegative : false
			})
		},{
			header : '返还周期',
			dataIndex : 'cycle',
			editor : new Ext.form.NumberField({
				allowBlank : false,
				allowNegative : false
			})
		},{
			header : '每期激活金额',
			dataIndex : 'active_amount',
			editor : new Ext.form.NumberField({
				allowBlank : false,
				allowNegative : false
			})
		},{
			header : '金额',
			dataIndex : 'fee',
			editor : new Ext.form.NumberField({
				allowBlank : false,
				allowNegative : false
			})
		},{
			header : '重复次数',
			dataIndex : 'repetition_times',
			editor : new Ext.form.NumberField({
				minValue : 1,
				maxValue : 99,
				allowBlank : false,
				allowNegative : false
			})
		},{
        	header : '必选',
        	dataIndex : 'necessary_text',
        	editor : new Ext.form.ComboBox({
					store: this.booleanStore,
					allowBlank : false,
					mode: 'local',
					selectOnFocus:true,
					editable : true,
					forceSelection : true,
					triggerAction : 'all',
					valueField: 'text',
					displayField: 'text'
			})
        },{ header: '操作',
	        width:170,
	        scope:this,
	        dataIndex : 'promotion_id',
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}	      
		}];
		
		App.form.initComboData([Ext.getCmp('presentType')]);
		
		AcctPromotionGrid.superclass.constructor.call(this,{
			id : 'AcctPromotionGrid',
			sm : new Ext.grid.CheckboxSelectionModel(),
			store : this.acctListStore,
			columns : acctColumns,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
	        viewConfig:{
	        	forceFit : true
	        },
	        tbar : [{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	id : 'addAcctProm',
	        	disabled : true,
	        	handler : this.addRecord
	        }]
		});
	},
	initEvents : function(){
		this.on('beforeedit',this.beforeEdit,this);
		this.on('afteredit',this.afterEdit,this);
		AcctPromotionGrid.superclass.initEvents.call(this);
	},doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == Ext.getCmp('PromotionGrid').county){
			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"AcctPromotionGrid"+"\').deleteRow(); style='color:blue'>删除</a>";
		}
		return btns;
	},
	checkValid : function(){//验证是否输入完整
		var store = this.getStore();
    	for(var i=0;i<store.getCount();i++){
			if(Ext.isEmpty(store.getAt(i).get('acctitem_name')) || Ext.isEmpty(store.getAt(i).get('necessary_text')) ){
    			return '数据没有编辑完整';
			}
    		
    	}
    	return true;
	},
	beforeEdit : function(obj){
		if(obj.field == 'tariff_name'){
			if(obj.record.data.acctitem_id){
				this.tariffStore.load({
					params : {
						acctitemId : obj.record.data.acctitem_id,
						promotionId : this.promotionId
					}
				});
				var combo = new Ext.form.ComboBox({
					store :this.tariffStore,
					forceSelection:true,
					selectOnFocus:true,
					triggerAction:'all',
					mode:'local',
					boxMinWidth : 150,
					editable : true,
					displayField : 'tariff_name',
					valueField : 'tariff_name'
				});
				this.getColumnModel().getColumnById('tariff_name').editor = combo;
			}else{
				Alert("请先选择账目名称");
				this.stopEditing();
				return false;
			}
		}else if(obj.field == 'present_month' ||obj.field == 'cycle' 
			|| obj.field == 'active_amount' || obj.field == 'fee'){
			if(!obj.record.get('present_type')){
				Alert("请先选择赠送类型");
				this.stopEditing();
				return false;	
			}else{
				if(obj.record.get('present_type') == 'TIME'){
					if(obj.field != 'present_month'){
						return false;
					}
				}else{
					if(obj.field == 'present_month'){
						return false;
					}
				}
			}
		}else if(obj.field == 'present_type_text'){
			if(obj.record.get('tariff_id')){
				return false;
			}
		}
	},
	afterEdit : function(obj){
		if(obj.field == 'acctitem_name'){
			var index = this.acctItemStore.findExact('acctitem_name',obj.value);
			obj.record.data.acctitem_id = this.acctItemStore.getAt(index).get('acctitem_id');
			obj.record.set('tariff_name','');
			obj.record.set('tariff_id' ,'');
			obj.record.set('present_type_text','');
			obj.record.set('present_type','');
		}else if(obj.field == 'tariff_name'){
			var index = this.tariffStore.findExact('tariff_name',obj.value);
			if(index > -1){
				var record = this.tariffStore.getAt(index);
				obj.record.data.tariff_id = record.get('tariff_id');
				
				//宽带自动匹配 送时长 
				if(record.get('rent') == 0 || record.get('acctitem_id') == 'BAND'){
					obj.record.set('present_type_text','时长');
					obj.record.set('present_type','TIME');
					
					obj.record.set('cycle',0);
					obj.record.set('active_amount',0);
					obj.record.set('fee',0);
				}else{
					obj.record.set('present_type_text','金额');
					obj.record.set('present_type','FEE');
					
					obj.record.set('present_month',0);
				}
			}else{
				obj.record.set('tariff_id' ,'');
			}
		}else if(obj.field == 'necessary_text'){
			var index = this.booleanStore.find('text',obj.value);
			obj.record.data.necessary = this.booleanStore.getAt(index).get('value');
		}else if(obj.field == 'present_type_text'){
			var store = Ext.getCmp('presentType').getStore();
			var index = store.find('item_name',obj.value);
			obj.record.data.present_type = store.getAt(index).get('item_value');
			if(obj.record.get('present_type') == 'TIME'){
				obj.record.set('cycle',0);
				obj.record.set('active_amount',0);
				obj.record.set('fee',0);
			}else{
				obj.record.set('present_month',0);
			}
		}
	},
	deleteRow : function(){
		AcctPromotionGrid.superclass.deleteRow.call(this);
	},
	addRecord : function(){
		var obj = {
			'promotion_id' : this.promotionId,
			'acctitem_id' : '',
			'tariff_id' : '',
			'present_month' : 0,
			'fee' : 0,
			'cycle' : 0,
			'active_amount' : 0,
			'repetition_times' : this.repetitionTimes,
			'necessary' : '',
			'necessary_text' : ''
		}
		if(this.autoExec == 'T'){
			obj['necessary'] = 'T';
			obj['necessary_text'] = '是';
//			this.getColumnModel().setEditable(8,false);
		}
//		else{
//			this.getColumnModel().setEditable(8,true);
//		}
		//必须调用父类方法
		AcctPromotionGrid.superclass.addRecord.call(this,obj);
	},
	getTotalFee :function(){
		var total = 0;
		this.getStore().each(function(rec){
			if(rec.get('necessary') == 'T'){
				total = total + rec.get('fee')
			}
		});
		return total;
	},
	getTotalAcctCount : function(){
		var total = 0;
		this.getStore().each(function(rec){
			if(rec.get('necessary') == 'T'){
				total = total + 1;
			}
		});
		return total;
	}
})

/**
 * 充值卡优惠
 * @class CardPromotionGrid
 * @extends Ext.grid.EditorGridPanel
 */
CardPromotionGrid = Ext.extend(ComPromGrid,{
	constructor : function(){
		this.cardListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'card_type'},
				{name : 'card_value', type : 'float'},
				{name : 'promotion_id'}
			]
		});
		var cardColumns = [{
			header : '促销ID',
			dataIndex : 'promotion_id',
			hidden : true
		},{
			header : '有价卡类型',
			dataIndex : 'card_type',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '金额',
			dataIndex : 'card_value',
			editor : new Ext.form.NumberField({
				allowBlank : false
			})
		},{ header: '操作',
	        width:170,
	        scope:this,
	        dataIndex : 'promotion_id',
            renderer:function(value,meta,record,rowIndex,columnIndex,store){
            	var btns = this.doFilterBtns(record);
            	return btns;
			}	      
		}];
		
		CardPromotionGrid.superclass.constructor.call(this,{
			id : 'CardPromotionGrid',
			sm : new Ext.grid.CheckboxSelectionModel(),
			store : this.cardListStore,
			columns : cardColumns,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	id : 'addCardProm',
	        	disabled : true,
	        	handler : this.addRecord
	        }]
		});
	},doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || App.data.optr['county_id'] == Ext.getCmp('PromotionGrid').county){
			btns = btns + "<a href='#' onclick=Ext.getCmp(\'"+"CardPromotionGrid"+"\').deleteRow(); style='color:blue'>删除</a>";
		}
		return btns;
	},
	afterEdit : function(obj){
		if(obj.field == 'card_value'){
			obj.value = obj.value * 100;
		}
	},
	deleteRow : function(){
		CardPromotionGrid.superclass.deleteRow.call(this);
	},
	addRecord : function(){
		var obj = {
			'promotion_id' : this.promotionId,
			'card_type' : '',
			'card_value' : 0
		}
		//必须调用父类方法
		CardPromotionGrid.superclass.addRecord.call(this,obj);
	}
})

Remark_SRC = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
			'<tr height=100>',
				'<td class="input_bold" width=30%>&nbsp;{[values.columns_name_src ||""]}</td>',
			'</tr>',
	'</table>'
);

var RemarkSrcPanel = Ext.extend(Ext.Panel,{
	tplCN :null,
	constructor:function(){
		this.tplCN = Remark_SRC;
		this.tplCN.compile();
		RemarkSrcPanel.superclass.constructor.call(this,{
			id:'remarkSrcPanelId',
			width:'75%',
			region:'east',
			layout:'anchor',
			autoScroll:true,
			defaults: {
				anchor:'95%',
				style:'margin-left:15px;margin-top:15px;margin-right:15px;'
			},
			items:[{
				xtype:'fieldset',
				title:'描述',
				html : this.tplCN.applyTemplate({})
			}]
		});
	}
});

var RemarkGrid = Ext.extend(Ext.grid.GridPanel,{
	refObjStore :null,
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.refObjStore = new Ext.data.JsonStore({
			fields:['columns_name','columns_name_src']
		});
		var columns = [
			{header:'字段名称',fields:'columns_name',width:250}
		];
		this.refObjStore.loadData([
		{'columns_name':'主题-促销类型,组织形式','columns_name_src':'用于报表统计分类，没有报表可以不填'},
		{'columns_name':'配置-规则名称,自动执行,协议编号','columns_name_src':'规则名称:促销需要满足的规则条件;<br> 自动执行:判断是自动促销还是手动促销;' +
				'<br> 协议编号:只是记录属于某一协议，没有就不用填，一般不需要'},
		{'columns_name':'配置-账目赠送总额,产品赠送总数','columns_name_src':'账目赠送总额:促销一共赠送的金额数；<br>产品赠送总数:账目优惠中必选产品的数量不能超过该值'},
		{'columns_name':'配置-优先级','columns_name_src':'处理促销的时候优先处理的顺序，如3条促销该值为1，2，3处理顺序为1->2->3'},
		{'columns_name':'配置-重复次数,账目优惠-重复次数','columns_name_src':'配置-重复次数:允许重复的次数，一般为1；账目优惠-重复次数：账目赠送的重复次数，一般为1;2者一般是一致的' +
				'<br>--------手动促销：账目优惠-重复次数小于配置-重复次数，取账目优惠-重复次数，否则取配置-重复次数;如下：' +
				'<br>某账目优惠-金额为10元，配置-重复次数为3，账目优惠-重复次数为2，那么重复次数取2，该账目赠送总额10*2=20元' +
				'<br>某账目优惠-金额为10元，配置-重复次数为1，账目优惠-重复次数为99，那么重复次数取1，该账目赠送总额10*1=10元' +
				'<br>--------自动促销：促销规则【已订购NVOD的用户，20元/月美剧剧场缴费满80元，赠送160元】，规则代码：【laterOrderByType("NVOD")!=null and  ACCTITEM_2750>=8000*times】' +
				'<br>配置-重复次数为99,账目优惠-重复次数为99,那么客户可以对美剧剧场缴费80*X，X<99，可以赠送160*X元'},
		{'columns_name':'配置-天数,促销次数','columns_name_src':'该促销这次参加允许下次参加之间的天数和次数，<br>如客户参加了促销A，该促销A配置天数为30天，促销次数为1，那么该客户参加了这次后，只能在30天后才能再次参加；' +
				'<br>如果促销A配置天数为30天，促销次数为2，那么30天内可以办理2次促销；'},
		{'columns_name':'账目优惠-赠送类型,赠送月份,返还周期,每期激活金额,金额','columns_name_src':'赠送类型：分为金额和时长;' +
				'<br>时长:只能是0资费和账目名称为宽带自动匹配的才能使用，其他都是金额；<br>赠送月份:直接送X个月到账目；' +
				'<br>--------------------' +
				'<br>金额:赠送金额到账目；' +
				'<br>返还周期:多长时间返还一次，一般是1即每月返还；如果是2即每2个月返还金额；' +
				'<br>每期激活金额:每个返还周期赠送到指定账目的金额' +
				'<br>金额:赠送到指定账目的总金额，列表中所有必选账目总额不能大于促销配置中的账目赠送总额；'},
		{'columns_name':'账目优惠-必选','columns_name_src':'必选:如果某个促销配置了4个账目优惠，2个必选，2个可选，配置-产品赠送总数为3，' +
				'<br>那么还可以配置1个必选账目，总共不能超过3个，必选账目的总金额不能超过配置-账目赠送总额，' +
				'<br>可选账目可以在前台营业系统，手动促销界面进行选择需要赠送的账目'}		
		]);
		RemarkGrid.superclass.constructor.call(this,{
			id:'refObjGridId',
			region:'center',
			store:this.refObjStore,
			columns:columns,
			listeners:{
				rowclick:this.doRowDblClick
			}
		});
	},
	doRowDblClick:function(grid,rowIndex,e){
		var record = grid.getSelectionModel().getSelected();
		this.parent.remarkForm.tplCN.overwrite(this.parent.remarkForm.items.itemAt(0).body,record.data);		
	}
	
	
});
RemarkPanel = Ext.extend(Ext.Panel,{
	remarkGrid :null,
	remarkForm:null,
	constructor : function(){
		this.remarkForm = new RemarkSrcPanel();
		this.remarkGrid = new RemarkGrid(this);
		RemarkPanel.superclass.constructor.call(this,{
			id:'RemarkId',
			closable: true,
			border : false ,
			layout : 'border',
			baseCls: "x-plain",
			items:[this.remarkGrid,this.remarkForm]
		})
	}
})


/**
 * 促销配置面板
 */
PromotionDetailPanel = Ext.extend(Ext.TabPanel,{
	cardPromotionGrid : null,
	feePromotionGrid : null,
	giftPromotionGrid : null,
	acctPromotionGrid : null,
	promotionId : null,
	remarkPanel:null,
	constructor : function(){
		this.cardPromotionGrid = new CardPromotionGrid();
		this.feePromotionGrid = new FeePromotionGrid();
		this.giftPromotionGrid = new GiftPromotionGrid();
		this.acctPromotionGrid = new AcctPromotionGrid();
		this.remarkPanel = new RemarkPanel();
		PromotionDetailPanel.superclass.constructor.call(this,{
			title : '促销内容详细配置',
			id : 'PromotionDetailPanel',
			region : 'south',
			height : 250,
			border : false,
			activeTab: 0,
			defaults: {
				border: false,
				defaults: { border: false }
			},
			items : [{
				xtype : 'panel',
				title : '账目优惠',
				border : false,
				layout : 'fit',
				items : [this.acctPromotionGrid]
			},{
				xtype : 'panel',
				title : '充值卡优惠',
				border : false,
				layout : 'fit',
				items : [this.cardPromotionGrid]
			},{
				xtype : 'panel',
				title : '礼品优惠',
				border : false,
				layout : 'fit',
				items : [this.giftPromotionGrid]
			},{
				xtype : 'panel',
				title : '费用优惠',
				border : false,
				layout : 'fit',
				items : [this.feePromotionGrid]
			},{
				xtype : 'panel',
				title : '促销描述',
				border : false,
				layout : 'fit',
				items : [this.remarkPanel]
			}],
			buttons : [{
				text : '保存',
				iconCls : 'icon-save',
				scope : this,
				id : 'savePromotion',
				handler : this.doSave
			}]
		});
	},
	initEvents : function(){
		this.on('beforetabchange',this.beforeChange,this);
		PromotionDetailPanel.superclass.initEvents.call(this);
	},
	beforeChange : function(tabPanel,newPanel,curPanel){
		if(curPanel && curPanel.title != '促销描述'){
			var msg = curPanel.items.itemAt(0).checkValid();
			if(msg != true){
				Alert(msg);
				return false;
			}
		}
	},
	doReset : function(){
		this.acctPromotionGrid.getStore().removeAll();
		this.cardPromotionGrid.getStore().removeAll();
		this.giftPromotionGrid.getStore().removeAll();
		this.feePromotionGrid.getStore().removeAll();
	},
	doValid : function(){//分别验证各个种类促销的配置数据
		this.cardPromotionGrid.stopEditing();
		var msg = this.cardPromotionGrid.checkValid();
		if(msg != true){
			return "有价卡优惠 ："+msg;
		}
		
		this.feePromotionGrid.stopEditing();
		msg = this.feePromotionGrid.checkValid();
		if(msg != true){
			return "费用优惠 ："+msg;
		}
		
		this.giftPromotionGrid.stopEditing();
		msg = this.giftPromotionGrid.checkValid();
		if(msg != true){
			return "礼品优惠 ："+msg;
		}
		
		this.acctPromotionGrid.stopEditing();
		msg = this.acctPromotionGrid.checkValid();
		if(msg != true){
			return "账目优惠 ："+msg;
		}
		if(this.acctPromotionGrid.totalAcctFee && this.acctPromotionGrid.totalAcctFee < this.acctPromotionGrid.getTotalFee()){
			return "账目优惠 ：" + "必选的累积金额不能大于账目赠送总额"+this.acctPromotionGrid.totalAcctFee;
		}
		
		if(this.acctPromotionGrid.totalAcctCount && this.acctPromotionGrid.totalAcctCount < this.acctPromotionGrid.getTotalAcctCount()){
			return "账目优惠 ：" + "必选的产品数量不能大于产品赠送总数"+this.acctPromotionGrid.totalAcctCount;
		}
		return true;
	},
	doSave : function(){
		var msg = this.doValid();//验证
		if(msg != true){
			Alert(msg);
			return;
		}
		
		Confirm('确定保存吗',this,function(){
//			mb = Show();//显示正在提交
			var all = {};
			this.promotionId = this.cardPromotionGrid.promotionId;
			all['promotionId'] = this.promotionId;
			
			//账目优惠数据
			var acctList = [];
			Ext.getCmp('AcctPromotionGrid').getStore().each(function(item){
				item.data.active_amount = item.get('active_amount') * 100;
				item.data.fee = item.get('fee') * 100;
				acctList.push(item.data);
			});
			all['acctListStr'] = Ext.encode(acctList);
			
			var feeList = [];
			Ext.getCmp('FeePromotionGrid').getStore().each(function(item){
				item.data.disct_value = item.get('disct_value') * 100;
				feeList.push(item.data);
			});
			all['feeListStr'] = Ext.encode(feeList);
			
			var cardList = [];
			Ext.getCmp('CardPromotionGrid').getStore().each(function(item){
				item.data.card_value = item.get('card_value') * 100;
				cardList.push(item.data);
			});
			all['cardListStr'] = Ext.encode(cardList);
			
			var giftList = [];
			Ext.getCmp('GiftPromotionGrid').getStore().each(function(item){
				item.data.money = item.get('money') * 100;
				giftList.push(item.data);
			});
			all['giftListStr'] = Ext.encode(giftList);
			
			
			Ext.Ajax.request({
				scope : this,
				url :  root + '/system/Promotion!savePromDetail.action',
				params : all,
				success : function(res,opt){
//					mb.hide();//隐藏提示框
//					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						Ext.getCmp('PromotionGrid').getStore().reload();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		})
	}
})

/**
 * 促销配置展示面板
 * @class PromotionView
 * @extends Ext.Panel
 */
PromotionView = Ext.extend(Ext.Panel,{
	constructor : function(){
		PromotionView.superclass.constructor.call(this,{
			id : 'PromotionView',
			layout : 'border',
			title : '促销配置管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new PromotionThemeGrid(),new PromotionGrid(),new PromotionDetailPanel()]
		})
	}
})