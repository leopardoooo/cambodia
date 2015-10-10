/**
 * 修改费用
 */
/**
 * 费用项表格构建
 * @class ServiceGrid
 * @extends Ext.grid.EditorGridPanel
 */
BusiFeeGrid = Ext.extend( Ext.grid.EditorGridPanel, {
	busiFeeStore: null,
	totalFee : null,
	doneCode : null,
	busiCode:null,
	constructor: function(){
		var record = App.getApp().main.infoPanel.getDoneCodePanel().doneCodeGrid.getSelectionModel().getSelected();
		this.busiFeeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+"/core/x/DoneCode!queryEditFee.action",
			fields: [
				{name: 'fee_id'},
				{name: 'fee_name'},
				{name: 'fee_type'},
				{name: 'default_value', type: 'float'},
				{name: 'sum_fee', type: 'float'},
				{name: 'real_pay', type: 'float'},
				{name: 'feeValue', type: 'float'},
				{name: 'buy_num', type: 'int'},
				{name: 'addr_id'},{name:'device_model'},{name:'device_model_text'},
				'fee_std_id'
			]
		});
		this.doneCode = record.get('done_code');
		this.busiCode = record.get('busi_code');
		this.busiFeeStore.load({
			params : {
				doneCode : record.get('done_code'),
				busiCode : record.get('busi_code'),
				custId : App.getApp().getCustId()
			}
		});
		this.busiFeeStore.on('load',this.doLoadData,this);
		
		var baseColumns = [
					{ header: '单价', dataIndex: 'default_value',width:55,renderer : Ext.util.Format.formatFee},
					{ id:'buy_num_id',header: '个数', dataIndex: 'buy_num',width:45,editor: new Ext.form.NumberField({
						allowBlank : false,
						allowNegative : false
					})},
					{ header: '累计收费', dataIndex: 'sum_fee',width:65,renderer : this.formatFenToYuan},
					{ id : 'real_pay',header: '实际应收', dataIndex: 'real_pay',width:65,renderer : this.formatFee,
						editor : new Ext.form.NumberField({
							allowBlank : false,
							allowNegative : false
						})},
					{ header : '本次收费', dataIndex: 'feeValue',width:65,renderer : this.formatFee}]
		
		var topColumns = [
					{ id : 'fee_name',header: '费用项', dataIndex: 'fee_name'}
				];
		var columns = [];				
		if(this.busiCode =='1109' || this.busiCode =='1108'){
			var	deviceColumns = [
				{ header: '型号', dataIndex: 'device_model_text',width:220,renderer:App.qtipValue}					
			];
			columns = topColumns.concat(deviceColumns).concat(baseColumns);					
		}else{
			columns = topColumns.concat(baseColumns);	
		}
		BusiFeeGrid.superclass.constructor.call(this, {
			region : 'center',
			store : this.busiFeeStore,
			forceValidation: true,
			autoExpandColumn : 'fee_name',
	        clicksToEdit: 1,
	        loadMask : true,
			cm: new Ext.grid.ColumnModel({
				columns: columns
			}),
			tbar : ['->','费用合计:',{
								id: 'txtSumFee',
								xtype:'tbtext',
								readOnly: true,
								width: 50,
								value: 0
							}],
			listeners:{
				scope:this,
				afterrender: function(){
					Ext.getCmp('BusiPanel').showTip();
				},
				beforeedit: this.beforeEdit,
				afteredit: this.afterEdit
			}
		});
	},
	beforeEdit: function(obj){
		if(obj.field == 'buy_num'){
			var value = obj.record.get('buy_num');
			if(this.busiCode =='1109' || this.busiCode =='1108' ){
				if(Ext.isEmpty(obj.record.get('real_pay'))){
					return false;
				}
				return true;
			}
			if(Ext.isEmpty(value) || value == 0 || value == 1){
				return false;
			}
		}
		
		if(obj.field == 'real_pay'){
			if(this.busiCode =='1109' || this.busiCode =='1108'){
				return false;
			}
		}
		
	},
	afterEdit : function(obj){
		var record = obj.record,field = obj.field, fee = 0;
		var buyNum = record.get('buy_num');
		if(field == 'buy_num'){
			record.set('real_pay',Ext.util.Format.formatFee( obj.value * record.get('default_value') ));
			record.set('feeValue',Ext.util.Format.formatFee(Ext.util.Format.formatToFen(record.get('real_pay'))-record.get('sum_fee')));
			this.setTotalFee();
		}
		if(field == 'real_pay'){
			fee = Ext.util.Format.formatFee(Ext.util.Format.formatToFen(record.get('real_pay')) - record.get('sum_fee'));
			if( buyNum > 1 ){
				var addBuyNum = fee % Ext.util.Format.formatFee( record.get('sum_fee')/record.get('buy_num') );
				
				//购买多个配件时，购买个数=费用除以单价，所以必须是整数(例：购买配件)
				//若购买设备，购买个数为1个，此时不必是整数(例：购买设备、销售设备、更换设备)
				if(record.get('buy_num') > 1 && addBuyNum != 0  ){
					Alert('本次收费不能整除单价!<br/> <font color=red>购买个数应为整数!</font>');
					record.set('real_pay',Ext.util.Format.formatFee(record.get('sum_fee')));
					record.set('feeValue',0);
				}else{
					record.set('feeValue',fee);
					this.setTotalFee();
				}
			}else{
				record.set('feeValue',fee);
				this.setTotalFee();
			}
		}
	},
	//将0的显示为空，或分转成元
	formatFenToYuan : function(value){
		if(Ext.isEmpty(value) || value == 0){
			return '';
		}else{
			return parseFloat(Ext.util.Format.convertToYuan(value));
		}
	},
	//将0的显示为空
	formatFee : function(value){
		if(Ext.isEmpty(value) || value == 0){
			return '';
		}else{
			return parseFloat(value);
		}
	},
	doLoadData : function(){
		Ext.getCmp('BusiPanel').hideTip();
		
		if(this.busiFeeStore.getCount() == 0){
			Alert("该业务没有收费项，不能修改",function(){
				App.getApp().menu.hideBusiWin();
			},this)
		}else{
			this.busiFeeStore.each(function(record){
				record.set('real_pay',this.formatFenToYuan(record.get('sum_fee')));
			},this);
		}
		if(this.busiCode =='1109' || this.busiCode =='1108'){
			this.startEditing.defer(100,this,[0,this.getColumnModel().getIndexById('buy_num_id')]);
		}else{
			this.startEditing.defer(100,this,[0,this.getColumnModel().getIndexById('real_pay')]);
		}
	},
	setTotalFee : function(){
		this.totalFee = 0;
		this.busiFeeStore.each(function(rec){
			this.totalFee = this.totalFee + rec.get('feeValue');
		},this);
		Ext.getCmp('txtSumFee').setText(this.totalFee);
	},
	doValid : function(){
		this.stopEditing();
		var flag = false;//是否有需要保存的费用
		this.getStore().each(function(rec){
			if(rec.get('feeValue') != 0){
				flag = true;
				return false;//跳出each
			}
		})
		return flag;
	},
	getValues: function(){
		var result = {};
		var store = this.getStore();
		var data = [];
		var that = this;
		store.each(function(rec){
			if(rec.get('feeValue') != 0){
				var obj = {};
				obj['fee_id'] = rec.get('fee_id');
				obj['fee_type'] = rec.get('fee_type');
				obj['real_pay'] = Ext.util.Format.formatToFen(rec.get('feeValue'));
				obj['fee_std_id'] = rec.get('fee_std_id');
				
				obj['addr_id']=rec.get('addr_id');
				//器材购买，数量是 本次收费/单价=变化的数量
				if(that.busiCode =='1109' || that.busiCode =='1108'){
					obj['buy_num'] = rec.get('feeValue')/Ext.util.Format.formatFee(rec.get('default_value'));
				}else{
					var price = rec.get('sum_fee')/rec.get('buy_num');	//单价
					
					if(rec.get('buy_num') == 1 && obj['real_pay'] >= 0 ){
						obj['buy_num'] = 0;
					}else{
						obj['buy_num'] = obj['real_pay'] / price;
					}
				}
				data.push(obj);
			}
		});
		result['feeBusiListStr'] = Ext.encode(data);
		return result;
	}
});

EditPayForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Pay!editFee.action",
	busiFeeGrid : null,
	constructor:function(){
		this.busiFeeGrid = new BusiFeeGrid();
		
		EditPayForm.superclass.constructor.call(this,{
			layout : 'fit',
			border : false,
			items:[{
				xtype : 'panel',
				layout : 'fit',
				items : [this.busiFeeGrid]
			}]
		});
	},
	doValid : function(){
		if(this.busiFeeGrid.doValid()){
			return EditPayForm.superclass.doValid.call(this);
		}else{
			var result = {};
			result["isValid"] = false;
			result["msg"] = "没有新的费用，不需要保存";
			return result
		}
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getValues:function(){
		var result = this.busiFeeGrid.getValues();
		result['donecode'] = this.busiFeeGrid.doneCode;
		return result;
	},
	getFee : function(){
		return this.busiFeeGrid.totalFee;
	}
});

Ext.onReady(function(){
	var panel = new EditPayForm();
	TemplateFactory.gTemplate(panel);
});