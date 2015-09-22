/**
 * 单位成员信息表格
 */ 
UnitMemberGrid = Ext.extend(Ext.ux.Grid,{
	region:'center',
	border:false,
	memberStore:null,
	tbar:[],
	isReload : true,
	isReQuery : true,
	constructor:function(){
		this.memberStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryUnitMember.action",
			fields: ["cust_id","cust_no","cust_name","address","create_time"]
		});
		
		this.memberStore.on('load',this.doLoadResult,this);
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
            sm,
			{header:'客户名编号',dataIndex:'cust_no',width:160,renderer:App.qtipValue	},
			{header:'客户名称',dataIndex:'cust_name',width:160,renderer:App.qtipValue},
			{header:'地址',dataIndex:'address',	width:180, renderer:App.qtipValue},
			{header:'加入时间',dataIndex:'create_time',	width:100}
        	]
     	});
     	
		UnitMemberGrid.superclass.constructor.call(this,{
			id : 'UnitMember',
			region: 'center',
			title: '客户信息',
			loadMask: null,
			store:this.memberStore,
			sm:sm,
			cm:cm,
			view: new Ext.ux.grid.ColumnLockBufferView({})
			,tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
				
				var comp = this.tools.search;
				if(this.memberStore.getCount()>0){
					if(win)win.close();
						win = FilterWindow.addComp(this,[
								{text:'单位名称',field:'cust_name',showField:'cust_name'},
								{text:'地址',field:'address',type:'textfield'}
							],200,"1",false);
						
					win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
					win.show();
				}else{
					Alert('请先查询数据！');
				}
		    }}]
			
		})
	},
	initEvents: function(){
		UnitMemberGrid.superclass.initEvents.call(this);
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
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
	remoteRefresh:function(){
		this.memberStore.baseParams.custId=App.data.custFullInfo.cust.cust_id;
		this.memberStore.load();
	},
	localRefresh:function(){
		unitRecords = this.getSelectionModel().getSelections();
		for (var i in unitRecords){
			this.memberStore.remove(unitRecords[i]);
		}
	},
	doLoadResult : function(store){
		this.setTitle('客户信息,总人数:'+store.getCount())
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});


/**
 * 客户查询的结果显示面板构造，
 * 包括客户基本信息、客户套餐、过户信息、移机信息、设备信息等面板
 */
UnitPanel = Ext.extend( BaseInfoPanel , {
	// 面板属性定义
	custInfoPanel: null,
//	packageGrid: null,
	propChangeGrid : null,
	unitMemberGrid:null,
	constructor: function(){
		//子面板实例化
		this.custInfoPanel =new CustInfoPanel('C_CUST_UNIT');
		this.propChangeGrid = new PropChangeGrid();
//		this.packageGrid = new PackageGrid('C_PACKAGE_UNIT');
		this.unitMemberGrid = new UnitMemberGrid();
		UnitPanel.superclass.constructor.call(this, {
			layout:"border",
			border:false,
			items:[{
				region:"center",
				layout:"anchor",
				border: false,
				items:[{
					anchor:"100% 62%",
					layout:'fit',
					items:[this.custInfoPanel]
				},{
					anchor:"100% 38%",
					layout:'fit',
					title : '异动信息',
					items:[this.propChangeGrid]
				}]
			},{
				region:"east",
				split:true,
				width:"50%",
				layout:"anchor",
				border: false,
				items:[{
					anchor:"100% 100%",
					layout:'fit',
					items:[this.unitMemberGrid]
//				},{
//					anchor:"100% 38%",
//					layout:'fit',
//					items:[this.packageGrid]
				}]
			}]
		});
	},
	refresh:function(){
		if (App.data.custFullInfo.cust.cust_type=='UNIT'){
			this.custInfoPanel.remoteRefresh();
			this.unitMemberGrid.remoteRefresh();
//			this.packageGrid.remoteRefresh();
			this.propChangeGrid.remoteRefresh();
		}
	}
});
Ext.reg( "untpanel" , UnitPanel );