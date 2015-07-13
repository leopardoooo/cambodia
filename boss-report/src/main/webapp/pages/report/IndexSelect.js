IndexSelectWin=Ext.extend(Ext.Window,{
	width:305,height:225,title:'指标选择',
	parent:null,selector:null,id:'IndexSelectID',
	storeToBeSelect:null,
	storeSelected:null,
	dataPassed:[],
	loadStoreData:function(){
		Ext.each(this.dataPassed,function(data){
			if(data.check == true){
				this.storeSelected.add(new Ext.data.Record(data));
			}else{
				this.storeToBeSelect.add(new Ext.data.Record(data));
			}
		},this);
	},
	reloadDatas:function(parent,list){
		this.parent = parent;
		this.dataPassed = list;
		this.storeSelected.removeAll();
		this.storeToBeSelect.removeAll();
		this.loadStoreData();
	},
	constructor:function(parent,list){
		this.parent = parent;
		this.dataPassed = list;
		this.storeToBeSelect = new Ext.data.JsonStore({fields : ['id', 'name','check','pid']});
		this.storeSelected = new Ext.data.JsonStore({fields : ['id', 'name','check','pid']});
		
		this.selector = this.createItemSelector();
		this.storeToBeSelect.on('add',this.exchange,this);
		
		this.loadStoreData();
		IndexSelectWin.superclass.constructor.call(this,{
			buttonAlign:'center',
			items:[this.selector],
			buttons : [
				{text:'确定',handler : this.submitBtnHandler,scope:this},
				{text:'取消',handler : this.cancelBtnHandler,scope:this}
				]
		});
	},
	//functions
	submitBtnHandler:function(){
		var meas = [];
		this.storeSelected.each(function(record){
			meas.push(record.get('id'));
		},this);
		
		var formerData = [];
		Ext.each(this.dataPassed,function(data){
			if(data.check == true){
				formerData.push(data.id);
			}
		},this);
		
		if(formerData.join() == meas.join()){
			this.hide();
			return;
		}
		
		var param  = {query_id:this.parent.query_id,meas:meas};
		
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Show!cubeMeaSelect.action',
			scope:this,timeout:9999999999,
			params : param,
			success : function(res, opt) {
				this.parent.xtablePanel.loadAndView(0);
				wait.hide();
				wait = null;
				this.hide();
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('加载数据出错');
			}
		});
	},
	cancelBtnHandler:function(){
		this.hide();
	},
	createItemSelector:function(){
		var selector = {
			htmlcode : 'itemselector',
			allowBlank : false,delimiter : "','",
			imagePath : '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage',
			multiselects : [{
						legend : '待选',width:135,height:160,
						store : this.storeToBeSelect,displayField : 'name',valueField : 'id'
					}, {
						legend : '已选',width:135,height:160,
						store : this.storeSelected,displayField : 'name',valueField : 'id'
					}]
	
		};
		return new Ext.ux.ItemSelector(selector);
	},
	roolBack:function(index){
		var record =this.storeToBeSelect.getAt(index);  
		this.storeToBeSelect.removeAt(index);
		this.storeSelected.add(record);
	},
	exchange:function(store,record,index){
		var counts = this.storeSelected.getCount();
		if(counts <1){
			Alert('至少要选择一个指标');
			var task = new Ext.util.DelayedTask(this.roolBack.createDelegate(this,index,true));
			task.delay(500);
			return;
		}
	}
});

