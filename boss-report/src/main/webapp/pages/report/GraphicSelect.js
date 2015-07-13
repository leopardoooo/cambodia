GraphicSelectWin=Ext.extend(Ext.Window,{
	width:250,height:200,title:'图形选择',
	bodyStyle:'padding-top:10px',
	parent:null,id:'GraphicSelectID',
	storeOfGraphic:null,storeOfDimensionOne:null,storeOfDimensionTwo:null,storeOfIndex:null,
	graphicCombo:null,dimensionOneCombo:null,dimensionTwoCombo:null,indexCombo:null,
	dimsData:[],graphicData:[],indexData:[],lastD1Record:null,
	lastChoices:{graphtype : '',dimensions : [],meas : ''},//上次选择的内容
	loadStoreData:function(){
		//KeyAction.queryDims  维度stroe 
		Ext.Ajax.request({
			url : root + '/query/Key!queryDims.action',
			scope:this,timeout:9999999999,
			params : {query_id:this.parent.query_id},
			success : function(res, opt) {
				this.dimsData = Ext.decode(res.responseText);
				Ext.each(this.dimsData,function(data){
					if(data.usesign == true){
						this.storeOfDimensionOne.add(new Ext.data.Record(data));
						this.storeOfDimensionTwo.add(new Ext.data.Record(data));
					}
				},this);
				
				var lastD1 = this.lastChoices.dimensions[0];
				if(!Ext.isEmpty(lastD1)){//先选择上次选择的
					this.dimensionOneCombo.setValue(lastD1);
				}
				
				var lastD2 = this.lastChoices.dimensions[1];
				if(!Ext.isEmpty(lastD2)){//先选择上次选择的
					this.dimensionTwoCombo.setValue(lastD2);
				}
				
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
		
		//KeyAction.queryMeas()  参数 query_id  返回 List<ConKeyCheck>   指标store
		Ext.Ajax.request({
			url : root + '/query/Key!queryMeas.action',
			scope:this,timeout:9999999999,
			params : {query_id:this.parent.query_id},
			success : function(res, opt) {
				this.indexData = Ext.decode(res.responseText);
				this.storeOfIndex.loadData(this.indexData);
				
				var lastIndex = this.lastChoices.meas;
				if(!Ext.isEmpty(lastIndex)){//先选择上次选择的
					this.indexCombo.setValue(lastIndex);
				}
				
				if(this.indexData.length==1){//如果只有一个指标,无论如何选这个.
					this.indexCombo.setValue(this.indexData[0].id);
				}
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
		
		/**
		 * 调用KeyAction.queryGraphTypes 返回List<QueryKeyValue> 选择图形store
		 * 其中QueryKeyValue.pid 表示动态生成几个维度选择下拉框
		 */
		Ext.Ajax.request({
			url : root + '/query/Key!queryGraphTypes.action',
			scope:this,timeout:9999999999,
			params : {query_id:this.parent.query_id},
			success : function(res, opt) {
				this.graphicData = Ext.decode(res.responseText);
				this.storeOfGraphic.loadData(this.graphicData);
				
				var lastGraphic = this.lastChoices.graphtype;
				if(!Ext.isEmpty(lastGraphic)){//先选择上次选择的
					this.graphicCombo.setValue(lastGraphic);
				}
				
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
		
	},
	reloadDatas:function(parent){
		this.parent = parent;
		this.storeOfDimensionOne.removeAll();
		this.storeOfDimensionTwo.removeAll();
		this.storeOfGraphic.removeAll();
		this.storeOfIndex.removeAll();
		this.dimensionOneCombo.setValue(null);
		this.dimensionTwoCombo.setValue(null);
		if(this.indexData.length==1){
			this.indexCombo.setValue(this.indexData[0].id);
		}else{
			this.indexCombo.setValue(null);
		}
		this.graphicCombo.setValue(null);
		this.lastD1Record = null;
		this.loadStoreData();
	},
	onGraphicComboSelect:function(combo,record,index){ //  combo,record,index
		this.storeOfDimensionOne.removeAll();
		this.storeOfDimensionTwo.removeAll();
		
		Ext.each(this.dimsData,function(data){
			if(data.usesign == true){
				this.storeOfDimensionOne.add(new Ext.data.Record(data));
				this.storeOfDimensionTwo.add(new Ext.data.Record(data));
			}
		},this);
		
		this.dimensionOneCombo.setValue(null);
		
		this.dimensionTwoCombo.setValue(null);
		this.dimensionTwoCombo.setDisabled(true);
		
		if(this.indexData.length==1){
			this.indexCombo.setValue(this.indexData[0].id);
		}else{
			this.indexCombo.setValue(null);
		}
		
		this.lastD1Record = null;
	},
	initEvents:function(){
		
		this.graphicCombo.on('select',this.onGraphicComboSelect,this);
		
		this.dimensionOneCombo.on('select',function(combo,record,index){
			
			var graRec = this.graphicCombo.findRecord('id',this.graphicCombo.getValue());
			var pid = graRec.get('pid');
			this.dimensionTwoCombo.setValue(null);
			if(pid == '2'){
				this.dimensionTwoCombo.clearInvalid();
				this.dimensionTwoCombo.setDisabled(true);
			}else if(pid == '3'){
				this.dimensionTwoCombo.setDisabled(false);
				this.dimensionTwoCombo.markInvalid();
			}
			
			if(this.lastD1Record){
				this.storeOfDimensionTwo.add(this.lastD1Record);
			}
			this.lastD1Record = record;
			if(record.get('id') == this.dimensionTwoCombo.getValue()){
				this.dimensionTwoCombo.setValue(null);
			}
			this.storeOfDimensionTwo.removeAt(index);
		},this);
		
		GraphicSelectWin.superclass.initEvents.call(this);
	},
	constructor:function(parent){
		this.parent = parent;
		this.storeOfGraphic = new Ext.data.JsonStore({fields : ['id', 'name','pid']});
		this.storeOfDimensionOne = new Ext.data.JsonStore({fields : ['columnMappingKey', 'cubeMappingKey','dim','id','level','level_totals','name','slices_level','slices_value','usesign','verticalsign']});
		this.storeOfDimensionTwo = new Ext.data.JsonStore({fields : ['columnMappingKey', 'cubeMappingKey','dim','id','level','level_totals','name','slices_level','slices_value','usesign','verticalsign']});
		this.storeOfIndex = new Ext.data.JsonStore({fields : ['id', 'name','check','pid']});
		

		this.graphicCombo = this.createCombo(this.storeOfGraphic,'图形');
		this.dimensionOneCombo = this.createCombo(this.storeOfDimensionOne,'维度1');
		this.dimensionTwoCombo = this.createCombo(this.storeOfDimensionTwo,'维度2');
		this.indexCombo = this.createCombo(this.storeOfIndex,'指标');
		
		this.reloadDatas(this.parent);
		
		GraphicSelectWin.superclass.constructor.call(this,{
			buttonAlign:'center',layout:'form',labelAlign:'right',labelWidth:55,
			items:[this.graphicCombo,this.dimensionOneCombo,this.dimensionTwoCombo,this.indexCombo],
			buttons : [
				{text:'确定',handler : this.submitBtnHandler,scope:this},
				{text:'取消',handler : this.cancelBtnHandler,scope:this}
				]
		});
	},
	//functions
	doCheckValues:function(){
		var graphtype = this.graphicCombo.getValue();
		var graphic = this.graphicCombo.findRecord('id',graphtype);
		var pid = graphic.get('pid');
		var index = this.indexCombo.getValue();
		var d1 = this.dimensionOneCombo.getValue();
		var d2 = this.dimensionTwoCombo.getValue();
		
		var flag = Ext.isEmpty(graphtype) || Ext.isEmpty(index)|| Ext.isEmpty(d1);
		if(pid == '3'){
			flag = flag || Ext.isEmpty(d2); 
		}
		
		var dimensions = [d1,d2];
		var param = {
			query_id : this.parent.query_id,
			graphtype : graphtype,
			dimensions : dimensions,
			meas : index
		};
		this.lastChoices = param;
		return !flag?param:!flag;
	},
	submitBtnHandler:function(){
		//ShowAction.cubeGraph()
		var values = this.doCheckValues();
		if(!values){
			Alert('请将表单填充完整');
			return false;
		}
//		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Show!cubeGraph.action',
			scope:this,timeout:9999999999,
			params : values,
			success : function(res, opt) {
				var chartData = Ext.decode(res.responseText);
				this.parent.doAddChartView(chartData);
				this.hide();
			},
			failure:function(){
				Alert('加载数据出错');
			}
		});
	},
	cancelBtnHandler:function(){
		this.hide();
	},
	createCombo:function(storePassed,label){
		var cfg = {
			allowBlank : false,store:storePassed,anchor:'95%',editable:false,
			valueField:'id',displayField:'name',fieldLabel:label
		};
		return new Ext.form.ComboBox(cfg);
	}
});

