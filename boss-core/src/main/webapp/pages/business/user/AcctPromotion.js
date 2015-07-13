/*
 * 账目促销
 */

AcctPromotionGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	acctListStore: null,
	totalAcctFee: null,
	totalAcctCount: null,//赠送产品总数
	selRows: [],
	constructor: function(){
		this.acctListStore = new Ext.data.JsonStore({
			fields : [
				{name : 'acctitem_id'},
				{name : 'acctitem_name'},
				{name : 'active_amount' ,type : 'float'},
				{name : 'cycle'},
				{name : 'fee', type : 'float'},
				{name : 'repetition_times', type : 'float'},
				{name : 'tariff_id'},
				{name : 'necessary'},
				{name : 'promotion_id'},
				{name : 'present_type'},
				{name : 'present_month'},
				{name : 'originalFee'}//控制修改后的金额不能大于配置金额
			],
			sortInfo : {
				field:'necessary',
				direction:'DESC'
			}
		});
		var acctSm = new Ext.grid.CheckboxSelectionModel({
			singleSelect:false
		});
		var acctColumns = [acctSm,
			{header : '账目名称',dataIndex : 'acctitem_name',width : 150,tooltip : ''},
			{header : '赠送月份',dataIndex : 'present_month',width : 60},
			{header : '每期激活金额',dataIndex : 'active_amount',width : 100,renderer : Ext.util.Format.formatFee},
			{header : '返还周期',dataIndex : 'cycle',width : 60},
			{header : '金额',dataIndex : 'fee',width : 60,
				editor : new Ext.form.NumberField({
					allowBlank : false,
					allowNegative : false,
					listeners:{
						scope:this,
						blur:function(txt){
							if(txt.getValue() > 0){
								this.getSelectionModel().selectRows(this.selRows,true);
							}
						}
					}
				})}
		];
		AcctPromotionGrid.superclass.constructor.call(this,{
			title : '账目优惠',
			id : 'acctPromotionGrid',
			store : this.acctListStore,
			sm : acctSm,
			columns : acctColumns,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
	        listeners : {
	        	scope : this,
	        	beforeedit : this.beforeEdit,
	        	afteredit : this.afterEdit,
	        	rowclick: function(grid,rowIndex){
	        		if(this.selRows.indexOf(rowIndex) == -1){
	        			this.selRows.push(rowIndex);
	        		}else{
	        			this.selRows.remove(rowIndex);
	        		}
	        		this.getSelectionModel().selectRows(this.selRows,true);
	        	}
	        }
		});
	},
	beforeEdit : function(obj){
		if(obj.record.data.necessary == 'T'){
			return false;
		}
	},
	afterEdit : function(obj){
		if(obj.originalValue != 0 && obj.value > obj.record.get('originalFee')){
			Alert('不能大于配置金额.');
			obj.record.set('fee',obj.originalValue);
		}
	},
	doLoadData : function(acctList,totalAcctFee,totalAcctCount){
		this.totalAcctFee = totalAcctFee;
		this.totalAcctCount = totalAcctCount;
		
		var title = '账目优惠';
		if(this.totalAcctFee > 0){
			title = title + '，赠送总金额：'+ this.totalAcctFee;
		}
		if(this.totalAcctCount > 0){
			title = title + '，请选择'+ this.totalAcctCount+'个产品';
		}
		
		this.setTitle(title);
		
		if(acctList){
			for(var i=0,len=acctList.length;i<len;i++){
				acctList[i].fee = Ext.util.Format.formatFee(acctList[i].fee);
				//控制修改后的金额不能大于配置金额
				acctList[i].originalFee = acctList[i].fee;
			}
			this.acctListStore.loadData(acctList);
			
			for(var i=0;i<this.acctListStore.getCount();i++){
				if(this.acctListStore.getAt(i).get('necessary') == 'T'){
					this.getSelectionModel().selectRow(i,true);
				}
			}
		}
	},
	getSelectData:function(){
		var acctList = [];
		var acctRecs = this.getSelectionModel().getSelections();
		for(var i=0;i<acctRecs.length;i++){
			acctRecs[i].data.fee = Ext.util.Format.formatToFen(acctRecs[i].data.fee);
			acctList.push(acctRecs[i].data);
		}
		return acctList;
	},
	doValid : function(){
		var store = this.getStore();
		var sm = this.getSelectionModel();
		
		for(var i=0;i<store.getCount();i++){
			if(store.getAt(i).get('necessary')=='T'){
				if(!sm.isSelected(i)){
					return '账目促销: 必须选择'+store.getAt(i).get('acctitem_name');
				}
			}
		}
		
		var total = 0;
		var records = sm.getSelections();
		for(var i=0;i<records.length;i++){
			total = total + records[i].get('fee');
		}
			
		if(total > this.totalAcctFee){
			return '账目促销: 赠送总额不能大于'+this.totalAcctFee;
		}
		
		if(this.totalAcctCount && records.length != this.totalAcctCount){
			return '账目促销: 您选择了'+records.length+'个产品，请选择'+this.totalAcctCount + '个产品';
		}
		
		return true;
	}
});