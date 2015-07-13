var TariffGrid = Ext.extend( Ext.grid.EditorGridPanel,{
	store:null,
	expDate:null,
	params:null,
	constructor:function(ids,parent){
		this.parent = parent;
		batchTariff = this;
		var fields = ["prod_sn","cust_id","user_id","prod_id","tariff_id","order_date","prod_name","next_bill_date",
						"tariff_name","tariff_rent","next_tariff_id","next_tariff_name",
						'eff_date','card_id','cust_name','isEdit','exp_date','terminal_type','cust_class','cust_colony',
						'cust_class_text','cust_colony_text','terminal_type_text'];
						
		if(App.getCust().cust_type == "UNIT"){
			this.params = {custId : ids.join(","),type:"CUST"};
		}else{
			this.params = {userId : ids.join(","),type:"USER"};
		}				
		this.store = new Ext.data.JsonStore({
			baseParams : this.params,
			url : Constant.ROOT_PATH+"/commons/x/QueryUser!queryProdByIds.action",
			fields: fields
		});
	
		this.store.on('load',this.doLoadResult,this);
		this.expDate = new Ext.form.DateField({
			format:'Y-m-d',
			allowBlank:false,
			editable: false,
			plugins:'monthPickerPlugin'
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm =  new Ext.ux.grid.LockingColumnModel({ 
			columns : [
				sm,
				{header:'客户名',dataIndex:'cust_name',width:105,renderer:App.qtipValue},
				{header:'智能卡号',dataIndex:'card_id',width:130,renderer:App.qtipValue},
				{header:'产品名称',dataIndex:'prod_name',width:85,renderer:App.qtipValue},
				{header:'当前资费',dataIndex:'tariff_name',width:85,renderer:App.qtipValue},
				{header:'未生效资费',dataIndex:'next_tariff_name',width:85,renderer:App.qtipValue},
				{header:'新生效日期(可改)',dataIndex:'eff_date',id:'effDateId',width:100,renderer:Ext.util.Format.dateFormat,editor: this.expDate},
				{header:'截止日期(可改)',dataIndex:'exp_date',id:'expDateId',width:100,renderer:Ext.util.Format.dateFormat,editor: new Ext.form.DateField({format:'Y-m-d',minValue : nowDate().format('Y-m-d')})}
			]
        });
        cm.isCellEditable = this.isCellEditable;
		TariffGrid.superclass.constructor.call(this,{
			id:'specGridId',
			title:'产品信息',
			store:this.store,
			cm:cm,
			region : 'center',
			border:false,
			view: new Ext.ux.grid.ColumnLockBufferView({}),
			clicksToEdit:1,
			sm:sm
		});
	},
	//是否可编辑
	isCellEditable:function(colIndex,rowIndex){
		var effIndex = this.getIndexById('effDateId');
		var expIndex = this.getIndexById('expDateId');
		var record = batchTariff.getStore().getAt(rowIndex);//当前编辑行对应record
		if(effIndex == colIndex){
			if(record.get('isEdit')==false){
				return false;
			}
		}
		if(expIndex == colIndex){
			if(Ext.isEmpty(record.get('exp_date'))){
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	initEvents : function(){
		TariffGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();			
		},this,{delay:10})
		
		this.on("afteredit",function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			var value = obj.value;
			if(fieldName == 'eff_date'){
				if(value<nowDate().clearTime()){
					record.set('eff_date',null);
					this.startEditing(obj.row,obj.column);
				}									
			}
		},this,{delay:100});
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
          
	},
	doLoadResult : function(store){
		Ext.getCmp('BusiPanel').hideTip();
		Ext.getCmp('specGridId').setTitle('产品信息,产品总数:'+store.getCount());
		//先清空数据
		this.parent.tarForm.resetTariff(Ext.getCmp('oldTariffNameId'));
		this.parent.tarForm.resetTariff(Ext.getCmp('newTariffNameId'));	
		this.parent.tarForm.resetTariff(Ext.getCmp('custClassId'));
		this.parent.tarForm.resetTariff(Ext.getCmp('custColonyId'));
		this.parent.tarForm.resetTariff(Ext.getCmp('terminalTypeTd'));
		//产生原资费数据
		this.parent.tarForm.doCombo(Ext.getCmp('oldTariffNameId'),'tariff_id','tariff_name',false);

	},
	getValues:function(){
		var arr = [],data;
		var records = this.getSelectValues();
		for(var i=0;i < records.length;i++){
			data = records[i].data;
			var obj = {};
				obj["user_id"] = data['user_id'];
				obj['card_id'] = data['card_id'];
				obj["prod_sn"] = data['prod_sn'];
				obj["prod_name"] = data['prod_name'];
				obj["tariff_id"] = data['tariff_id'];
				obj["cust_id"] = data['cust_id'];
				obj["eff_date"] = (Ext.isDate(data['eff_date']))?data['eff_date'].format('Y-m-d H:i:s'):data['eff_date'];
				obj["exp_date"] = (Ext.isEmpty(data['exp_date']))?data['exp_date']:data['exp_date'].format('Y-m-d H:i:s');
				arr.push(obj);
		}
		return arr;
	},
	getSelectValues:function(){
		this.stopEditing();
	 	return	this.getSelectionModel().getSelections();
	}
});

TariffForm = Ext.extend(Ext.Panel,{
	validDate:null,
	prodStore:null,
	prodTariffStore:null,
	params:null,
	oldTariffId:null,
	constructor : function(ids){
		var date = nowDate();
		date.setDate(1);
		date.setMonth(date.getMonth()+1);
		this.validDate =date;
		if(App.getCust().cust_type == "UNIT"){
			this.params = {custId : ids.join(","),type:"CUST"};
		}else{
			this.params = {userId : ids.join(","),type:"USER"};
		}
		this.prodStore = new Ext.data.JsonStore({
			autoLoad : true,
			baseParams : this.params,
			url : Constant.ROOT_PATH+"/commons/x/QueryUser!queryBaseProdByIds.action",
			fields:[{name:'prod_id'},{name:'prod_name'}]
		});
		
		//产品资费
		this.prodTariffStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/core/x/Prod!queryAllProdTariff.action",
			fields : ['tariff_id','tariff_name',{name : 'rent',type : 'float'}]
		});
		
		TariffForm.superclass.constructor.call(this,{
			region : 'south',
			height : 130,
			bodyStyle: Constant.TAB_STYLE,
			layout : 'form',
			defaults : {
				border :false,
				bodyStyle: "background:#F9F9F9"
			},
			items : [{
				layout : 'column',
				anchor : '100%',
				defaults : {
					layout : 'form',
					border :false,
					bodyStyle: "background:#F9F9F9",
					columnWidth : .5
				},
				items : [{
					items:[{
						id:'tbarCobId',
						xtype:'combo',
						fieldLabel : '产品名称',
		    			store:this.prodStore,
		    			triggerAction:'all',
		    			emptyText:'请选择产品',
		    			allowBlank : false,
						displayField:'prod_name',
						valueField:'prod_id',
						hiddenName : 'prod_name',
						hiddenValue : 'prod_id',
						editable:false,
						forceSelection:true,
						eableKeyEvents:true,
						selectOnFocus:true
						,listeners:{
							scope:this,
							select:this.doSelectProdItem
						}
					},{
						xtype:'combo',
						id:'oldTariffNameId',
						fieldLabel : '原资费',
						allowBlank : false,
						hiddenName : 'tariff_id',
						hiddenValue : 'tariff_id',
	    				store:new Ext.data.ArrayStore({
	    					fields:['tariff_id','tariff_name']
	    				}),displayField:'tariff_name',valueField:'tariff_id',emptyText:'请选择资费',
	    				editable:false,forceSelection:true,eableKeyEvents:true
	    				,listeners:{
	    					scope:this,
	    					select:this.doSelectTariffName
	    				}
					},{
						xtype:'combo',
						id:'newTariffNameId',
						fieldLabel : '新资费',
						allowBlank : false,
	    				store : this.prodTariffStore,
	    				hiddenName : 'tariff_id',
						hiddenValue : 'tariff_id',
						valueField : 'tariff_id',
						displayField : "tariff_name",
	    				editable:false,forceSelection:true,eableKeyEvents:true
	    				,listeners:{
	    					scope:this,
	    					'select' : this.doSelectProdTariff
	    				}
					}]
				},{
					id:'terminalTypeItem',
					items:[{
						xtype:'combo',
						id:'terminalTypeTd',
						fieldLabel : '终端类型',
	    				store:new Ext.data.ArrayStore({
	    					fields:['terminal_type','terminal_type_text']
	    				}),
	    				hiddenName : 'terminal_type',
						hiddenValue : 'terminal_type',
						valueField : 'terminal_type',
						displayField : "terminal_type_text",
	    				editable:false,forceSelection:true,eableKeyEvents:true
						,listeners:{
	    					scope:this,
	    					'select' : function(combo) {
	    						this.selectDate(this.oldTariffId,
	    						Ext.getCmp('custClassId').getValue(),Ext.getCmp('custColonyId').getValue(),combo.getValue())
	    				}}
					}]
				},{
					id:'custClassItem',
					items:[{
						xtype:'combo',
						id:'custClassId',
						fieldLabel : '客户优惠类型',
	    				store:new Ext.data.ArrayStore({
	    					fields:['cust_class','cust_class_text']
	    				}),
	    				hiddenName : 'cust_class',
						hiddenValue : 'cust_class',
						valueField : 'cust_class',
						displayField : "cust_class_text",
	    				editable:false,forceSelection:true,eableKeyEvents:true
	    				,listeners:{
	    					scope:this,
	    					'select' : function(combo) {
	    						this.selectDate(this.oldTariffId,combo.getValue(),
	    						Ext.getCmp('custColonyId').getValue(),Ext.getCmp('terminalTypeTd').getValue())
	    					}}
					}]
				},{
					id:'custColonyItem',
					items:[{
						xtype:'combo',
						id:'custColonyId',
						fieldLabel : '客户群体',
	    				store:new Ext.data.ArrayStore({
	    					fields:['cust_colony','cust_colony_text']
	    				}),
	    				hiddenName : 'cust_colony',
						hiddenValue : 'cust_colony',
						valueField : 'cust_colony',
						displayField : "cust_colony_text",
	    				editable:false,forceSelection:true,eableKeyEvents:true
	    				,listeners:{
	    					scope:this,
	    					'select' : function(combo) {
	    						this.selectDate(this.oldTariffId,
	    						Ext.getCmp('custClassId').getValue(),combo.getValue(),Ext.getCmp('terminalTypeTd').getValue())
	    				}}
					}]
				},{
					id:'prodEffDateItem',
					items:[{
						xtype : 'datefield',
						width : 100,
						fieldLabel : '统一生效日期',
		   				plugins: 'monthPickerPlugin',   
		                format: 'Y-m-d', 	
		                editable: false,
						value : this.validDate,
						id : 'prodeffdate',
						listeners:{
							scope:this,
							'select':this.doSelectDate
						}
					}]
				},{
					id:'prodExpDateItem',
					items:[{
						xtype : 'datefield',
						width : 100,
						fieldLabel : '统一截止日期',
						minValue : nowDate().format('Y-m-d'),
						format : 'Y-m-d',
						allowBlank : false,
						id : 'prodexpdate',
						listeners:{
							scope:this,
							'select':this.doExpDate
						}
					}]
				}]
			}]
		})
		Ext.getCmp('prodEffDateItem').hide();
		Ext.getCmp('prodExpDateItem').hide();
		Ext.getCmp('prodexpdate').allowBlank = true;
		if(App.getCust().cust_type == "NONRES"){
			Ext.getCmp('custClassItem').hide();
			Ext.getCmp('custColonyItem').hide();
		}
	},	
	//产品下拉框select事件
	doSelectProdItem:function(combo,record){
		var value = combo.getValue();
		var store = Ext.getCmp('specGridId').getStore();
		store.load({
			params : {
				prod_id : value
			}
		});
		Ext.getCmp('prodExpDateItem').hide();
		Ext.getCmp('prodexpdate').allowBlank = true;
		Ext.getCmp('prodexpdate').setValue("");
		Ext.getCmp('BusiPanel').showTip();
	},
	//资费下拉框select事件
	doSelectTariffName:function(combo,record){
		var value = combo.getValue();
			var store = Ext.getCmp('specGridId').getStore();
			this.oldTariffId = value;
//			store.rejectChanges();
			
			this.selectDate(value);
			this.doCombo(Ext.getCmp('custClassId'),'cust_class','cust_class_text',true);
			this.doCombo(Ext.getCmp('custColonyId'),'cust_colony','cust_colony_text',true);
			this.doCombo(Ext.getCmp('terminalTypeTd'),'terminal_type','terminal_type_text',true);
			
	},
	//产生新资费事件
	doSelectNewTariff:function(s,value){
		var key = false,arr=[];
		s.each(function(record){
			arr.push(record.get('user_id'));
			if(record.get('isEdit')){
				key = true;
			}
		},this);
		if(key){
			Ext.getCmp('prodEffDateItem').show();
			Ext.getCmp('prodeffdate').setValue(this.validDate);
		}else{
			Ext.getCmp('prodEffDateItem').hide();
			key = false;
		}
		
		this.resetTariff(Ext.getCmp('newTariffNameId'));	
		
		this.prodTariffStore.load({
			params : {
				prodId : Ext.getCmp('tbarCobId').getValue(),
				userId : arr.join(","),
				tariffId : value
			}
		});
	},
	//统一生效日期事件
	doSelectDate:function(combo){
		var value = combo.getValue();
		if(value<nowDate().clearTime()){
			Ext.getCmp('prodeffdate').setValue('');
		}else{
			var store = Ext.getCmp('specGridId').getStore();
			store.each(function(record){
				if(record.get('isEdit')){
					record.set('eff_date',value);
				}
			})
		}
		
	},
	//新资费下拉事件
	doSelectProdTariff : function(combo,rec){
		var store = Ext.getCmp('specGridId').getStore();
		if(rec.get('rent') <= 0){		
			Ext.getCmp('prodExpDateItem').show();
			Ext.getCmp('prodexpdate').allowBlank = false;
			Ext.getCmp('prodeffdate').setValue(nowDate());
			var store = Ext.getCmp('specGridId').getStore();
			var exp = Ext.getCmp('prodexpdate').getValue();
			store.each(function(record){
				if(record.get('isEdit')){
					record.set('eff_date',nowDate());
					record.set('exp_date',exp);
				}
			})
		}else{
			Ext.getCmp('prodExpDateItem').hide();
			Ext.getCmp('prodexpdate').allowBlank = true;
			Ext.getCmp('prodeffdate').setValue(this.validDate);
		}
		this.doLayout();
	},
	//重置下拉框
	resetTariff:function (comp){
			comp.reset();
		var d = comp.getStore();
			d.removeAll();
	},
	//统一失效日期事件
	doExpDate:function(combo){
		var value = combo.getValue();
		var store = Ext.getCmp('specGridId').getStore();
		store.each(function(record){
			record.set('exp_date',value);
		})
	},
	//下拉框增加数据
	doCombo:function(comp,valueCombo,nameCombo,key){
		comp.reset();
		var s = comp.getStore();
		s.removeAll();
		var store = Ext.getCmp('specGridId').getStore();
		var arr = [],idArr=[];
		if(key){
			arr.push(['','全部']);
		}
		store.each(function(record){
			var data = record.data;
			if(idArr.indexOf(data[valueCombo]) == -1){
				idArr.push(data[valueCombo]);
				arr.push([data[valueCombo],data[nameCombo]]);
			}
		},this);
		s.loadData(arr);
		if(s.getCount()==2 && key){
			comp.setValue(s.getAt(1).get(valueCombo));
			if(valueCombo=='tariff_id'){
				comp.fireEvent('select',comp,s.getAt(1));
			}
		}else if(!key && s.getCount()==1){
			comp.setValue(s.getAt(0).get(valueCombo));
			if(valueCombo=='tariff_id'){
				comp.fireEvent('select',comp,s.getAt(0));
			}
		}else{
		
		}
	},
	//下拉框选择后过滤产品信息
	selectDate:function(oldTar,custClass,custColony,terminalType){
		var store = Ext.getCmp('specGridId').getStore();
			store.filterBy(function(record){
				var str=[] ;
				if(!Ext.isEmpty(oldTar)){
					str.push(record.get('tariff_id')==oldTar);
				}
				if(!Ext.isEmpty(custClass)){
					str.push(record.get('cust_class')==custClass);
				}
				if(!Ext.isEmpty(custColony)){
					str.push(record.get('cust_colony')==custColony);
				}
				if(!Ext.isEmpty(terminalType)){
					str.push(record.get('terminal_type')==terminalType);
				}
	           switch(str.length){
	           		case 1 : return str[0];
	           				break;
	           		case 2 : return str[0]&&str[1];
	           				break;
	           		case 3 : return str[0]&&str[1]&&str[2];
	           				break;
	           		case 4 : return str[0]&&str[1]&&str[2]&&str[3];
	           				break;
	           		default : return true;
	           }
	       	});
	    if(store.getCount()>0){
			this.doSelectNewTariff(store,this.oldTariffId);
			Ext.getCmp('specGridId').getSelectionModel().selectAll();
			Ext.getCmp('specGridId').setTitle('产品信息,产品总数:'+store.getCount());
	    }
	}
})

BatchTariffPanel = Ext.extend(Ext.Panel,{
	tarGrid : null,
	tarForm : null,
	constructor : function(){
		var records ,ids = [];
		if(App.getCust().cust_type == "UNIT"){
			records = App.getApp().main.infoPanel.unitPanel.unitMemberGrid.getSelectionModel().getSelections();
			for(var i=0;i<records.length;i++){
				ids.push(records[i].get('cust_id'))
			}
		}else{
			records = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelections();
			for(var i=0;i<records.length;i++){
				ids.push(records[i].get('user_id'))
			}
		}
		
		this.tarGrid = new TariffGrid(ids,this);
		this.tarForm = new TariffForm(ids);
		BatchTariffPanel.superclass.constructor.call(this,{
			layout : 'border',
			border : false,
			items : [this.tarGrid,this.tarForm]
		})
	},
	getValues : function(){
		return this.tarGrid.getValues();
	},
	getSelectValues:function(){
		return this.tarGrid.getSelectValues();
	}
});