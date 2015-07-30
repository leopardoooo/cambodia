/**
 * 产品订购之选择用户
 */
SelectUserWindow = Ext.extend(Ext.Window, {
	userGrid: null,
	store: null,
	constructor: function(){
	    this.store = new Ext.data.GroupingStore({
            reader: new Ext.data.ArrayReader({}, [
       	       {name: 'user_id'},
    	       {name: 'user_type'},
    	       {name: 'device_code'}
    	    ]),
            data: [
                ['000001',"OTT","222222222222"],
    		    ['000001',"OTT","111111111111"],
    		    ['000001',"OTT","111111111111"],
       		    ['000001',"BAND","tommy123"]
       	    ],
            groupField:'user_type'
        });
    
	    var sm = new Ext.grid.CheckboxSelectionModel();
		this.userGrid = new Ext.grid.GridPanel({
			store: this.store,
	        columns: [sm,
	            {id: 'autoExpandColumn',menuDisabled: true, align: 'left', header: "终端信息", sortable: false, dataIndex: 'device_code'},
	            {menuDisabled: true, hidden: true,width: 20, align: 'left', header: "类型", sortable: false, dataIndex: 'user_type'}
	        ],
	        view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl: 'A产品，共{[values.rs.length]} 个终端，已选 0个'
	        }),
	        sm: sm,
	        width: 700,
	        border: false,
	        autoExpandColumn: 'autoExpandColumn',
	        height: 450,
	        animCollapse: false,
	        tbar: [{
	        	xtype: 'textfield',
	        	emptyText: 'filter..',
	        	enableKeyEvents: true,
	        	id: 'txtFilterInput',
	        	width: 180,
	        	listeners: { 
	        		scope: this,
	        		specialKey: function(field, e){
	        			if (e.getKey() == e.ENTER) {
	        				this.doFilterStoreData(field);
	        	        }
	        		}
	        	}
	        },' ',{
	        	iconCls: 'icon-del',
	        	hidden: true,
	        	id: 'btnClearFilter',
	        	text: '清除过滤',
	        	scope: this,
	        	handler: function(){
	        		var field = Ext.getCmp("txtFilterInput");
	        		field.setValue("");
	        		this.doFilterStoreData(field);
	        	}
	        },' ',{
	        	iconCls: 'icon-collapse-all',
	        	scope: this,
	        	handler: function(){
	        		this.userGrid.getView().toggleAllGroups();
	        	}
	        }]
		});
		
		// Window 构造
		return SelectUserWindow.superclass.constructor.call(this, {
			layout: "fit",
			title: "选择订购的用户",
			width: 300,
			height: 400,
			resizable: false,
			iconCls: 'icon-edit-user',
			maximizable: false,
			closeAction: 'hide',
			minimizable: false,
			items: [this.userGrid]
		});
	},
	doFilterStoreData: function(field){
		var v = field.getValue().trim();
		if(v){
			this.store.filterBy(function(r){
				return new RegExp("^.*" + field.getValue()+".*$").test(r.get("device_code"));
			}, this);
			Ext.getCmp("btnClearFilter").setVisible(true);
		}else{
			this.store.clearFilter();
			Ext.getCmp("btnClearFilter").setVisible(false);
		}
	}
});

/**
 * 转移支付
 */
TransferPayWindow = Ext.extend(Ext.Window, {
	store: new Ext.data.JsonStore({
		fields: ["prod_name","traff_name", "user_ids", "month_count", "fee", "last_order_end_date", "start_date", "start_date"]
	}),
	constructor: function(){
		var columns = [
       	    {header: "产品名称", width: 100,sortable:true, dataIndex: 'prod_name'},
       	    {header: "资费", width: 70, sortable:true, dataIndex: 'traff_name'},
       	    {header: "用户", width: 60, sortable:true, dataIndex: 'user_ids'},
       	    {header: "转移计费日", width: 80, sortable:true, dataIndex: 'month_count'},
       	    {header: "结束计费日", width: 80, sortable:true, dataIndex: 'fee'},
       	    {header: "转移金额", width: 60, sortable:true, dataIndex: 'fee'}
       	];
		
		// Window Init 
		return TransferPayWindow.superclass.constructor.call(this, {
			layout:"fit",
			title: "转移支付明细",
			width: 450,
			height: 200,
			resizable: false,
			maximizable: false,
			closeAction: 'hide',
			minimizable: false,
			items: [{
				xtype: 'grid',
				stripeRows: true,
				border: false,
				store: this.store,
				columns: columns,
		        stateful: true
			}]
		});
	}
});

