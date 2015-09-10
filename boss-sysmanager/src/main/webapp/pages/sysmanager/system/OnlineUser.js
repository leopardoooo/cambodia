/**
 * 在线用户
 */
 
DoneResultGrid =Ext.extend(Ext.grid.GridPanel,{ 
	queryStore : null,
	constructor:function(optrId){
		var	columns = [
			{header: '业务名称',dataIndex: 'busi_name',width:100},
         	{header:'操作总数',dataIndex:'done_num',width:70},
         	{header:'正常数量',dataIndex:'active_num',width:70},
         	{header:'回退数量',dataIndex:'invalid_num',width:70},
         	{header:'最新操作业务时间',dataIndex:'last_done_date',width:140}
        ];
        var fields = [
        	'optr_name','busi_name','done_num','active_num','invalid_num','last_done_date'
		];
		this.queryStore = new Ext.data.JsonStore({ 
			autoLoad:true,
			baseParams : {query:optrId},
			url: root + '/system/Index!queryOnelineUserBusi.action',
			fields: fields       
		}); 
		DoneResultGrid.superclass.constructor.call(this,{
			store:this.queryStore,
			columns:columns,
			tbar:[{text:'刷新',iconCls:'icon-refresh',scope:this,handler:this.refresh},'-',{xtype: 'displayfield',id:'onlineInfoId'}]
		});
	},
	initComponent: function () {
        DoneResultGrid.superclass.initComponent.call(this);
        this.refresh();
    },
    refresh:function(){
    	this.queryStore.load();
		this.queryStore.on('load',this.doint,this);
    },
	 doint: function(s,recs){
	 	var info ="",num = 0;
	 	if(s.getCount()>0){
    		info = s.getAt(0).get('optr_name');
	    	s.each(function(record){
	    		num = num + parseInt(record.get('done_num'));
	    	})
	 	}
    	Ext.getCmp('onlineInfoId').setValue("<font style='color:red'><b> "+info+ " </b><b>"+s.getCount()+"种业务</b><b>"+num+"笔操作</b></font>");
    	this.doLayout();
    }
});


DoneCodeInfoWindow = Ext.extend( Ext.Window , {
	doneGrid:null,
	constructor: function (optrId){
		this.doneGrid = new DoneResultGrid(optrId);
		DoneCodeInfoWindow.superclass.constructor.call(this,{
			width: 500,
			height: 400,
			title: '当天实际操作业务明细',
			layout: 'fit',
			border: false,
			items: this.doneGrid
		});
	}
});
UserGrid = Ext.extend(Ext.grid.GridPanel,{
	userStore:null,
	constructor : function(){
		this.userStore = new Ext.data.GroupingStore({
						url:'/boss-login/online',
			baseParams:{countyId:App.data.optr['county_id']},
			reader: new Ext.data.JsonReader(
				{
				fields:[{name:'optr_name',mapping:'optr.optr_name'},
					{name:'optr_id',mapping:'optr.optr_id'},
					{name:'login_name',mapping:'optr.login_name'},
					{name:'userIp',mapping:'userIp'},
					{name:'bwver',mapping:'bwver'},
					{name:'loginTime',mapping:'loginTime'},
					{name:'dept_name',mapping:'optr.dept_name'},
					{name:'resourceList',mapping:'resourceList'},
					{name:'county_id',mapping:'optr.county_id'},
					{name:'county_name',mapping:'optr.county_name'},
					{name:'lastResTime',mapping:'lastResTime'}
				]
			}),
			sortInfo:{field: 'county_name', direction: "ASC"},
			groupField:'county_name'
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		UserGrid.superclass.constructor.call(this,{
			store:this.userStore,
			id:'onlineUserGridId',
			columns : [
					{header:'',dataIndex:'county_name',hidden:true},
					{header:'登录名',dataIndex:'login_name',sortable: true,width:65},
					{header:'操作员名称',dataIndex:'optr_name',sortable: true,width:90,renderer:App.qtipValue},
					{header:'部门',dataIndex:'dept_name',sortable: true,width:90},
					{header:'登陆时间',dataIndex:'loginTime',sortable: true,width:130},
					{header:'最后操作时间',dataIndex:'lastResTime',sortable: true,width:130},
					{header:'浏览器',dataIndex:'bwver',sortable: true,width:90,renderer:App.qtipValue},
					{header:'登陆IP',dataIndex:'userIp',sortable: true,width:90},
					{header:'点击内容',dataIndex:'resourceList',sortable: true,width:200,renderer:App.qtipValue}
				],
			autoScroll: true,
			sm: sm
			,view : new Ext.grid.GroupingView({
				forceFit:true,
				groupTextTpl : '{[values.rs[0].data["county_name"]]} 在线用户数:{[values.rs.length]}个'
			}),
			tbar: ['-',{xtype: 'displayfield',id:'onlineUserNumId'},'-','过滤:',
			{xtype:'textfield',emptyText: '登录名,操作员,部门,时间',enableKeyEvents:true,width:200,
				listeners:{
					scope:this,
						keyup:function(txt,e){
								if(e.getKey() == Ext.EventObject.ENTER){
									var value = txt.getValue();
										this.userStore.filterBy(function(record){
											if(Ext.isEmpty(value))
												return true;
											else
												return record.get('login_name').indexOf(value)>=0 || record.get('optr_name').indexOf(value)>=0 
											||record.get('dept_name').indexOf(value)>=0||this.isChick(record.get('loginTime'),value);
									},this);
								}
							}
						}
			},'-',{text:'刷新',iconCls:'icon-refresh',scope:this,handler:this.refresh},'-',{xtype:'displayfield',width:40},{xtype:'displayfield',value:'双击行,查看操作员当天业务明细'}]
			,			
			listeners : {
				"rowdblclick" : function(g, i, e) {
					var record = g.getStore().getAt(i);
					var optrId = record.get('optr_id');
					new DoneCodeInfoWindow( optrId ).show();
				}
			}
		})
	},
	initComponent: function () {
        UserGrid.superclass.initComponent.call(this);
        this.refresh();
    },
    refresh:function(){
    	this.userStore.load();
		this.userStore.on('load',this.doint,this);
    },
    doint: function(s,recs){
    	var txt = "";
    	if(App.data.optr['county_id']=='4501'){
    		txt = "在线用户总数:";
    	}else{
    		txt = App.data.optr['county_name']+"在线用户数:";
    	}
    		Ext.getCmp('onlineUserNumId').setValue(txt+"<font style='color:red'><b> "+s.getCount()+ " </b></font>个");
    	this.doLayout();
    },   
    renderName:function(value, metadata, record){
	    metadata.attr = 'style="white-space:normal;"'; 
		return value; 
    },
    dateFormat : function(v){
		if(!v) return "" ;
		if(Ext.isDate(v)){
			return v.format('Y-m-d h:i:s');
		}
		var date = Date.parseDate(v,'Y-m-d h:i:s')
		return date;
	},
	isChick:function(v1,v2){
		if(isNaN(v2)){
			return false;
		}else{
			var date = this.dateFormat(v1);
			if(Ext.isDate(date) && Date.parseDate(Ext.util.Format.lpadRight( v2 , 14,'0'),'YmdHis')<=date){
				return true;
			}else{
				return false;
			}
		}
	}
})

OnlineUser = Ext.extend(Ext.Panel, {
	grid:null,
    constructor: function () {
        this.grid = new UserGrid(this);
        OnlineUser.superclass.constructor.call(this, {
            id: 'OnlineUser',
            title: '在线用户',
            closable: true,
            border: false,
            layout : 'fit',
            items: [this.grid]
        });
    }
});