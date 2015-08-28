var MaterialDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	store:null,
	tariffData:null,//所有产品对应的资费信息
	constructor:function(custIds){
		materalThat = this;
		this.store = new Ext.data.JsonStore({
			url :root + '/commons/x/QueryDevice!queryDeviceCanBuy.action',
			fields:['device_model','device_model_text','total_num','buy_num','fee_value','fee_id',
			'fee_std_id','fee_back','device_id']
		});
		
		this.store.load();
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
			columns : [
				{header:'器材编号',dataIndex:'device_id',width:250,hidden:true},
				{header:'器材型号',dataIndex:'device_model_text',width:250},
				{header:'单价',dataIndex:'fee_value',width:100,renderer:Ext.util.Format.convertToYuan},
				{header:'库存数量',dataIndex:'total_num',width:100,renderer:App.qtipValue},
				{id:'buy_num_id',header:'购买数量',dataIndex:'buy_num',width:100,
					scope:this
					,editor: new Ext.form.NumberField({
						allowDecimals:false,//不允许输入小数 
		    			allowNegative:false,
		    			minValue:1//enableKeyEvents: true,
					})
				}
			]
        });
		cm.isCellEditable = this.cellEditable;
		MaterialDeviceGrid.superclass.constructor.call(this,{
			title : '器材信息',
			id:'buyMaterialId',
			store:this.store,
			cm:cm,
			clicksToEdit:1,
			region : 'center',
			border:false,
			view: new Ext.ux.grid.ColumnLockBufferView({}),
			sm:new Ext.grid.RowSelectionModel({})
		});	
	},
	cellEditable:function(colIndex,rowIndex){
		var record = materalThat.getStore().getAt(rowIndex);//当前编辑行对应record
		if(colIndex == this.getIndexById('buy_num_id')){
			if(record.get('total_num') == 0){
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	initEvents : function(){
		MaterialDeviceGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();
		},this,{delay:10});
		
		this.on("afteredit",this.afterEdit,this);
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
	afterEdit : function(obj){
		var record = obj.record;
		var fieldName = obj.field;//编辑的column对应的dataIndex
		var value = obj.value;
		if(fieldName=='buy_num'){
			if(!this.doChickValue(record,value)){
				record.set('buy_num', '');
				record.set('fee', '');
				record.set('fee_back', '');
				this.startEditing(obj.row,obj.column);
			}
		}
	},doChickValue:function(record,value){
		if(value != null){
			if(value >0 ){
				//输入正整数
				var re = /^[1-9]+[0-9]*]*$/;
			    if (!re.test(value)){  
			        Alert("请输入正整数");  
			        return false;  
			    }
			    
				if(record.get('total_num')<value){
					Alert("输入的数量已经超出库存");
					return false;
				}
				
				//计算金额
				var fee = value*record.get('fee_value');
				record.set('fee', fee);
				record.set('fee_back', Ext.util.Format.convertToYuan(fee));
			}else if(value == 0){
				return false;
			}
			return true;
		}
	},	
	getValues:function(){
		var arr = [],data;
		this.store.each(function(record){
			data = record.data;
			var obj = {};
			obj["device_model"] = data['device_model'];
			obj["fee_id"] = data['fee_id'];
			obj["fee_std_id"] = data['fee_std_id'];
			obj['buy_num'] = data['buy_num'];
			obj['fee'] = data['fee_value']*data['buy_num'];
			arr.push(obj);
		},this);
		return arr;
	}
});

BacthBuyMaterialForm = Ext.extend( BaseForm , {
	url : Constant.ROOT_PATH + "/core/x/Cust!buyMaterial.action",
	success : function(){
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	materialDeviceGrid : null,
	data : [],//提交数据
	constructor: function(){
		this.materialDeviceGrid = new MaterialDeviceGrid();
		BacthBuyMaterialForm.superclass.constructor.call(this,{
			autoScroll:true,
			layout:'border',
            border:false,
            bodyStyle: Constant.TAB_STYLE,
            items:[{
				region:'center',
				layout : 'fit',
				items:[this.materialDeviceGrid]
			}]
		});
	},
	getValues: function(){
		var all = {'payFeesData':Ext.encode(this.materialDeviceGrid.getValues())};
		return all;
	},
	doValid: function(){
		var data = this.panel.getValues();
		
		if(data.length==0){
			var obj = {};
			obj['msg'] = '器材不能为空';
			obj['isValid'] = false;
			return obj;
		}
		return BacthBuyMaterialForm.superclass.doValid.call(this);
	}
});

Ext.onReady(function(){
	var buy = new BacthBuyMaterialForm();
	var box = TemplateFactory.gTemplate(buy);
});