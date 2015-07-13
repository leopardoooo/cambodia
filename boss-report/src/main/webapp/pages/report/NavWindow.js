
DimensionSelectPanel = Ext.extend(Ext.Panel,{
	xtype: 'panel',
	width: "50%",height: "50%",
	region: 'center',border: false,title: '维选择',
	selector:null,parent:null,
	constructor:function(selector,parent){
		this.selector = selector;
    	this.parent = parent;
		
		DimensionSelectPanel.superclass.constructor.call(this,{
			border:false,items:this.selector
		})
	}
});
ExchangePanel = Ext.extend(Ext.Panel,{
	region: 'center',border: false,title: '维交换',
    parent:null,leftStore:null,rightStore:null,
    selector:null,
    constructor:function(parent,selector,ls,rs){
    	this.parent = parent;
    	this.selector = selector;
    	this.leftStore = ls;
    	this.rightStore = rs;
    	
    	this.rightStore.on('add',this.exchange,this);
    	
		ExchangePanel.superclass.constructor.call(this,{
			border:false,layout:'fit',autoScroll:true,
			items:[this.selector]
		})
	},
	roolBack:function(record){
		var rec = this.rightStore.find('id',record.get('id'));
		this.rightStore.removeAt(rec);
		this.leftStore.add(record);
	},
	exchange:function(store,record,index){
		var counts = this.rightStore.getCount();
		if(counts >1){
			Alert('纵向维只能有一个');
			var task = new Ext.util.DelayedTask(this.roolBack.createDelegate(this,record,true));
			task.delay(500);
			return;
		}
	}
})

/**
 * 各种条件.
 * @class NavPanel
 * @extends Ext.Panel
 */
NavPanel = Ext.extend(Ext.Panel,{
	parent:null,myCube:null,
	dimensionSelectPanel: null,exchangePanel: null,
	selectStoreLeft:null,selectStoreRight:null,changeStoreLeft:null,changeStoreRight:null,
	
	getDataStore:function(){
		return new Ext.data.JsonStore({fields : ['id', 'name','level','slices','usesign','verticalsign']});
	},
	getStoreData:function(storeName){
		var datas = [];
		var dimensions = this.myCube.dimensions;
		for(var index =0;index<dimensions.length;index++){
			var data = dimensions[index];
			switch(storeName){
				case 'selectRight'://维选择右边
					if(data.usesign==true){
						datas.push(data);
					}
					break;
				case 'selectLeft'://维选择 左边
					if(data.usesign==false){
						datas.push(data);
					}
					break;
				case 'changeRight': //纵向维
					if(data.usesign==true && data.verticalsign==true ){
						datas.push(data);
					}
					break;
				case 'changeLeft': //横向维
					if(data.usesign==true && data.verticalsign==false ){
						datas.push(data);
					}
					break;
			}
		}
		return datas;
	},
	getDatas:function(){
		var dimlist = [];
		var vertdim = [];
		this.selectStoreRight.each(function(record){
			dimlist.push(record.get('id'));
		},this);
		this.changeStoreRight.each(function(record){
			vertdim.push(record.get('id'));
		},this);
		return {dimlist:dimlist,vertdim:vertdim};
	},
	initData:function(myCube){
		if(myCube){
			this.myCube = myCube;
		}
		
		this.selectStoreLeft.removeAll();
		this.selectStoreRight.removeAll();
		this.changeStoreLeft.removeAll();
		this.changeStoreRight.removeAll();
		
		this.selectStoreLeft.loadData(this.getStoreData('selectLeft'));
		this.selectStoreRight.loadData(this.getStoreData('selectRight'));
		this.changeStoreLeft.loadData(this.getStoreData('changeLeft'));
		this.changeStoreRight.loadData(this.getStoreData('changeRight'));
		
	},
	constructor: function(myCube,parent){
		this.myCube = myCube;
		this.parent = parent;
		var wnh = this.parent.getCmpWidthAndHeight();
		//为选择选择器
		var selectorWidth = wnh.width * 0.21;
		var selectorHeight = wnh.height * 0.65;
		
		this.selectStoreLeft = this.getDataStore('selectLeft');
		this.selectStoreRight = this.getDataStore('selectRight');
		this.changeStoreLeft = this.getDataStore('changeLeft');
		this.changeStoreRight = this.getDataStore('changeRight');
		
		//维选择
		this.dss = this.createItemSelector(
			selectorWidth,selectorHeight, this.selectStoreLeft,this.selectStoreRight);
			
		this.dss.toFrom = function(){
			var selectionsArray = this.dss.toMultiselect.view.getSelectedIndexes();
	        var records = [];
	        if (selectionsArray.length > 0) {
	            for (var i=0; i<selectionsArray.length; i++) {
	                record = this.dss.toMultiselect.view.store.getAt(selectionsArray[i]);
	                records.push(record);
	            }
	            selectionsArray = [];
	            for (var i=0; i<records.length; i++) {
	                record = records[i];
	                this.dss.toMultiselect.view.store.remove(record);
	                if(!this.dss.allowDup){
	                    this.dss.fromMultiselect.view.store.add(record);
	                    selectionsArray.push((this.dss.fromMultiselect.view.store.getCount() - 1));
	                }
	            }
	        }
	        this.dss.fromMultiselect.view.refresh();
	        this.dss.toMultiselect.view.refresh();
	        var si = this.dss.fromMultiselect.store.sortInfo;
	        if (si){
	            this.dss.fromMultiselect.store.sort(si.field, si.direction);
	        }
	        this.dss.fromMultiselect.view.select(selectionsArray);
	        
	        Ext.each(records,function(record){
	        	var rec = this.changeStoreLeft.find('id',record.get('id'));
				this.changeStoreLeft.removeAt(rec);
				rec = this.changeStoreRight.find('id',record.get('id'));
				this.changeStoreRight.removeAt(rec);
	        },this);
	        
		}.createDelegate(this);
		
		
		//this.dss.toFrom = Ext.ux.ItemSelector.toFrom.createDele
		
		this.dss.fromTo = function() {
	        var selectionsArray = this.dss.fromMultiselect.view.getSelectedIndexes();
	        var records = [];
	        if (selectionsArray.length > 0) {
	            for (var i=0; i<selectionsArray.length; i++) {
	                record = this.dss.fromMultiselect.view.store.getAt(selectionsArray[i]);
	                records.push(record);
	            }
	            if(!this.dss.allowDup)selectionsArray = [];
	            for (var i=0; i<records.length; i++) {
	                record = records[i];
	                if(this.dss.allowDup){
	                    var x=new Ext.data.Record();
	                    record.id=x.id;
	                    delete x;
	                    this.dss.toMultiselect.view.store.add(record);
	                }else{
	                    this.dss.fromMultiselect.view.store.remove(record);
	                    this.dss.toMultiselect.view.store.add(record);
	                    selectionsArray.push((this.dss.toMultiselect.view.store.getCount() - 1));
	                }
	            }
	        }
	        this.dss.toMultiselect.view.refresh();
	        this.dss.fromMultiselect.view.refresh();
	        var si = this.dss.toMultiselect.store.sortInfo;
	        if(si){
	            this.dss.toMultiselect.store.sort(si.field, si.direction);
	        }
	        this.dss.toMultiselect.view.select(selectionsArray);
	        
	        Ext.each(records,function(record){
	        	this.changeStoreLeft.add(record);
	        },this);
	        
	    }.createDelegate(this);
		
		this.changeSelector = this.createItemSelector(
			selectorWidth,selectorHeight, this.changeStoreLeft,this.changeStoreRight,'横向轴','纵向轴');
			
		this.dimensionSelectPanel = new DimensionSelectPanel(this.dss, this);
		this.exchangePanel = new ExchangePanel(this,this.changeSelector,this.changeStoreLeft,this.changeStoreRight);
		
		this.initData();
		
		NavPanel.superclass.constructor.call(this,{
			layout: "anchor",
			border: false,
			region: "center",
			items:[{
				anchor: "100% 100%",
				layout:'border',
				border: false,
				items:[{
					layout:'border',
					border: false,
					region:'center',
					margins:'0 5 0 5',
					items:[this.dimensionSelectPanel]
				},{
					region:'east',
					width:"50%",
					layout:'border',
					border: false,
					margins:'0 5 0 5',
					items:[this.exchangePanel]
				}]
			}]
		})
		
	},
	createItemSelector:function(width,height,lstore,rstore,legendLeft,legendRight){
		var selector = {
			htmlcode : 'itemselector',
			allowBlank : false,delimiter : "','",
			imagePath : '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage',
			multiselects : [{
						legend : legendLeft||'待选',
						width:width,
						height:height,
						store : lstore,
						displayField : 'name',
						valueField : 'id'
					}, {
						legend : legendRight||'已选',
						width:width,
						height:height,
						store : rstore,
						displayField : 'name',
						valueField : 'id'
					}]
	
		};
		return new Ext.ux.ItemSelector(selector);
	}
	
})

NavWindow = Ext.extend(Ext.Window,{
	title:'OLAP导航界面',plain:true,id:'olapNavWinId',
	width:600,height:320,
	resizable:false,maximizable:false,
	layout:'anchor',
	dementionsPanel:null,//
	headersPanel:null,
	navPanel:null,
	myCube:null,query_id:null,parent:null,
	constructor:function(parent,myCube,query_id,rep_id,rep_info){
		this.parent = parent;
		this.rep_id=rep_id;
		this.rep_info = rep_info;
		this.myCube = myCube;
		this.query_id = query_id;
		//维度面板
		this.navPanel =new NavPanel(myCube,this);
		
		this.dementionsPanel = new Ext.Panel({
			border:false,anchor:'100% 100%',
			buttons : [
				{id : 'confirmed_btn',text:'确定',handler : this.submitBtnHandler,scope:this},
				{id : 'cancel_btn',text:'取消',handler : this.cancelBtnHandler,scope:this}
				],
			items:this.navPanel,
			buttonAlign:'center'
		});
		
		
		NavWindow.superclass.constructor.call(this,{
			defaults : {layout : 'fit'},
			items:[this.dementionsPanel]
		});
	},
	//functions
	getCmpWidthAndHeight:function(){//获取顶级容器大小用于确定组件的大小.
		return {width:this.width,height:this.height};
	},
	//重新加载MyCube数据
	reloadDatas:function(parent,myCube,query_id,rep_id,rep_info){
		this.parent = parent;
		this.myCube = myCube;
		this.query_id = query_id;
		this.rep_id=rep_id;
		this.rep_info = rep_info;
		this.navPanel.initData(this.myCube);
	},
	checkDataChanged:function(){//数据是否改变,没有改变就不再提交.
		//{dimlist:dimlist,vertdim:vertdim};
		var dimList = this.myCube.dimlist;
		var vertdim = this.myCube.vertdim;
		var oldDimList = [];
		var oldVertdim = [];
		Ext.each(this.myCube.dimensions,function(data){
			//if(data.usesign ==true)//这个是肯定保证的了
			oldDimList.push(data.id);
			if(data.verticalsign == true){
				oldVertdim.push(data.id);
			}
		},this);
		
		return ( dimList.join() != oldDimList.join() ) || (vertdim.join() != oldVertdim.join());
		
	},
	submitBtnHandler:function(){
		//提交URL:   query/Report!navigateCube.action 传递query_id,MyCube(jsonParams)
		Ext.apply(this.myCube,this.navPanel.getDatas());
		//检查参数是否有变化
		if(!this.checkDataChanged()){//如果数据没有改变,不用提交.
			this.hide();
			return false;
		}
		if(this.myCube.vertdim.length ==0){
			delete this.myCube.vertdim;
		}
		
		this.myCube.dimensions= [];//减少传送的数据量
		var param = {query_id:this.query_id,jsonParams:Ext.encode({mycube:this.myCube})};
		
		var wait = Show();//进度条
		
		Ext.Ajax.request({
			url : root + '/query/Show!cubeDimSelect.action',
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
	}
	
});

