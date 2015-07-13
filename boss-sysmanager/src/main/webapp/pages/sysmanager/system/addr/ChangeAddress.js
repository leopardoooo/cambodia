/**
 * 小区挂载
 */
 AddressTreeCombo = Ext.extend(Ext.ux.TreeCombo,{
	onNodeClick : function(node, e) {
		if (!this.isCanClick(node)) {
			return;
		}
		var attrs = this.dispAttr.split(".");
		
		 var level = node.attributes.others.tree_level;
		if(!Ext.isEmpty(level)){
			this.level =level;
		}
		
		var temp = null;
		
		//区域对应的文本
		var temp = node.parentNode.attributes;
		var addrText = temp[this.dispAttr];
		
		temp = node.attributes;
		addrText = addrText + temp[this.dispAttr];
		
		
		// 显示隐藏值
		this.addOption(node.id, addrText);
		this.setValue(node.id);
		// 设置显示值
		this.setRawValue(addrText);
		this.collapse();
		this.fireEvent('select', this, node, node.attributes);
	}
})
QueryCustGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	store:null,	
	Columns:null,
	county:{},
	constructor:function(p,c){
		That = this ;
		this.county = {countyId:c};
		this.store = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+ "/system/Address!queryCust.action",
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields:['cust_id','cust_name','addr_id','address','addr_id_text','t1','t2','t3','t4','t5','note','old_address','county_id','area_id']
		});
		if(p!=null){
			this.store.baseParams = {custId:p,county_id:c};
			this.store.load({params: { start: 0, limit: 50 }});
		}
		this.Columns = [
			{header:'客户姓名',dataIndex:'cust_name',width:100,renderer:App.qtipValue},
			{id:'addrTextId',header:'小区(可改)',dataIndex:'addr_id_text',width:130,renderer:App.qtipValue,editor:new Ext.form.TextField({})},
			{header:'号(可改)',dataIndex:'t1',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'栋(可改)',dataIndex:'t2',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'单元(可改)',dataIndex:'t3',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'楼(可改)',dataIndex:'t4',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'室(可改)',dataIndex:'t5',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'备注(可改)',dataIndex:'note',width:80,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'新地址',dataIndex:'address',width:250,renderer:App.qtipValue},
			{header:'旧地址',dataIndex:'old_address',width:250,renderer:App.qtipValue}
		];
		QueryCustGrid.superclass.constructor.call(this,{
			title:'客户信息',
			border:false,
			ds:this.store,
			id:'queryCustAddressId',
			columns:this.Columns,
			sm : new Ext.grid.CheckboxSelectionModel(),
			clicksToEdit : 1,				
			bbar: new Ext.PagingToolbar({store: this.store,pageSize: 50})
		});
	},initEvents:function(){
		QueryCustGrid.superclass.initEvents.call(this);
		this.on('beforeedit',this.beforeEdit,this);
		this.on('afteredit',this.afterEdit,this);
	},beforeEdit:function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			if(fieldName === 'addr_id_text'){//属性列
				var propIdCm = this.getColumnModel().getColumnById('addrTextId');
					propIdCm.editor = new AddressTreeCombo({
						store:new Ext.data.JsonStore({
							fields:['id','text','others']
						}),valueField:'text',
				    	width:140,
						treeWidth:400,
						minChars:2,
						height: 22,
						allowBlank: false,
						treeParams : this.county,
						treeUrl : root + "/config/SendMsg!getAddrByName.action",
						hiddenName:'addr_id'
						,listeners:{
							delay:100,
							scope:this,
							select:function(combo,record,index){
								var condRecord = Ext.getCmp('queryCustAddressId').getSelectionModel().getSelected();
								condRecord.set('addr_id',record.id);
							},
							change:function(){That.changeAddr(obj)}
						}
					})
			}
	},afterEdit:function(obj){
		That.changeAddr(obj);
	},changeAddr:function(obj){
		var condRecord = obj.record;
		var addr = "";
		condRecord.set('address','');
		if(!Ext.isEmpty(condRecord.get('addr_id_text')))
		addr +=condRecord.get('addr_id_text');
		if(!Ext.isEmpty(condRecord.get('t1')))
		addr +=condRecord.get('t1')+'号';
		if(!Ext.isEmpty(condRecord.get('t2')))
		addr +=condRecord.get('t2')+'栋';
		if(!Ext.isEmpty(condRecord.get('t3')))
		addr +=condRecord.get('t3')+'单元';
		if(!Ext.isEmpty(condRecord.get('t4')))
		addr +=condRecord.get('t4')+'楼';
		if(!Ext.isEmpty(condRecord.get('t5')))
		addr +=condRecord.get('t5')+"室";
		if(!Ext.isEmpty(condRecord.get('note')))
		addr +=condRecord.get('note');
		condRecord.set('address',addr);
	}
});

CustAddrWin = Ext.extend(Ext.Window,{
	addrgrid:null,
	countyId:null,
	constructor:function(data,countyId){
		this.addrgrid = new QueryCustGrid(data,countyId);
		this.countyId = countyId;
		CUSTTHAT = this;
		CustAddrWin.superclass.constructor.call(this,{
			layout:'border',
			border:false,
			width:750,
			height:500,
			items:[{region : 'center',layout:'fit',items:[this.addrgrid]}],
			buttons : [{text : '保存',scope : this,iconCls : 'icon-save',handler : this.doSave
					}, {text : '关闭',scope : this,handler : function() {this.close();}}]
		})
	},doSave:function(){
		var grid = Ext.getCmp('queryCustAddressId');
			grid.stopEditing();// 停止编辑
			var records = [];
			var modifiedRec = grid.getStore().getModifiedRecords();
			if(modifiedRec.length == 0){Alert("没有修改任何数据!"); return false};
			for (var i = 0; i < modifiedRec.length; i++) {
				records.push(modifiedRec[i].data);
			}
			grid.getStore().commitChanges();//将Store中所有的Record的原始版本修改为当前版本
			Confirm("确定保存吗?", this, function () {
				mb = Show();
				Ext.Ajax.request({
							scope : this,
							url : Constant.ROOT_PATH+ "/system/Address!updateAddressList.action",
							params : {
								addrListStr : Ext.encode(records),countyId:CUSTTHAT.countyId
							},
							success : function(res, opt) {
								mb.hide();// 隐藏提示框
								mb = null;
								var rs = Ext.decode(res.responseText);
								if (true === rs.success) {
									grid.getStore().reload();
									Confirm("操作成功!【是】：继续操作，【否】：关闭所有窗口",this, this,function () {
										CUSTTHAT.close();
									});
								} else {
									Alert('操作失败');
								}
							}
						})
			});	
	}

})



ChangeAddressForm = Ext.extend(Ext.form.FormPanel, {
    IdleAddrGrid: null,
    useAddrGrid: null,
    idleAddrStroe: null,
    useAddrStroe: null,
    setProd: null,
    setUprod: null,
    forCountyId: null,
    key:'DETAIL',
    constructor: function () {
        CHANGEThat = this;
        this.idleAddrStroe = new Ext.data.JsonStore({
            url: root + '/system/Address!queryAddrByaddrId.action',
            fields: ['addr_id', 'addr_name', 'num']
        });
        this.idleAddrStroe.setDefaultSort("addr_name", "ASC");
        this.setProd = new Ext.grid.CheckboxSelectionModel();
        this.setUprod = new Ext.grid.CheckboxSelectionModel();
        this.useAddrStroe = new Ext.data.JsonStore({
            fields: ['addr_id', 'addr_name']
        });
        this.useAddrStroe.setDefaultSort("addr_name", "ASC");
        var oneTbar = new Ext.Toolbar({
            items: ['过滤:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                id: 'selectAddrGo',
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            this.useAddrStroe.filterBy(function (record) {
                                if (Ext.isEmpty(value)) return true;
                                else return record.get('addr_name').indexOf(value) >= 0;
                            }, this);
                        }
                    }
                }
            }]
        });

        var twoTbar = new Ext.Toolbar({
            items: ['过滤:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                id: 'selectAddrTo',
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            this.idleAddrStroe.filterBy(function (record) {
                                if (Ext.isEmpty(value)) return true;
                                else return record.get('addr_name').indexOf(value) >= 0;
                            }, this);
                        }
                    }
                }
            }]
        });
        this.IdleAddrGrid = new Ext.grid.EditorGridPanel({
            title: '转出',
            border: true,
            autoScroll: true,
            ds: this.idleAddrStroe,
            sm: this.setProd,
            columns: [this.setProd,
            {
                header: '小区名称',
                dataIndex: 'addr_name',
                width: 200
            }, {
                header: '客户数',
                dataIndex: 'num',
                width: 80
            }],
            tbar: ['区域:', new Ext.form.ComboBox({
                width: 140,
                allowBlank: false,
                hiddenName: 'goDept',
                id: 'goDeptId',
                store: new Ext.data.JsonStore({
                    url: root + '/system/Address!queryAddrByCountyId.action',
                    fields: ['addr_id', 'addr_name']
                }),
                displayField: 'addr_name',
                valueField: 'addr_id',
                editable : true,
				triggerAction : 'all',
				forceSelection : true,
                listeners: {
                    scope: this,
                    'select': function (combo, record, index) {
                        this.idleAddrStroe.load({
                            params: {
                                addrId: combo.getValue()
                            }
                        });
                        this.useAddrStroe.removeAll();
                    }
                }
            })],
            listeners: {
                'render': function () {
                    twoTbar.render(this.tbar);
                }
            }
        });

        this.useAddrGrid = new Ext.grid.EditorGridPanel({
            title: '转入',
            ds: this.useAddrStroe,
            sm: this.setUprod,
            height: 180,
            autoScroll: true,
            border: true,
            clicksToEdit: 1,
            columns: [this.setUprod,
            {
                header: '小区名称',
                dataIndex: 'addr_name',
                width: 200,
                renderer: App.qtipValue
            }, {
                header: '客户数',
                dataIndex: 'num',
                width: 80
            }],
            tbar: ['区域:', new Ext.form.ComboBox({
                width: 140,
                allowBlank: false,
                hiddenName: 'toDept',
                id: 'toDeptId',
                store: new Ext.data.JsonStore({
                    url: root + '/system/Address!queryAddrByCountyId.action',
                    fields: ['addr_id', 'addr_name']
                }),
                editable : true,
				triggerAction : 'all',
				forceSelection : true,
                displayField: 'addr_name',
                valueField: 'addr_id'
            })],
            listeners: {
                'render': function () {
                    oneTbar.render(this.tbar);
                }
            }
        });

        ChangeAddressForm.superclass.constructor.call(this, {
            id: 'ChangeAddressRorm',
            region: 'center',
            layout: 'border',
            border: false,
            fileUpload:true,
            items: [{
                region: 'center',
                border: false,
                layout : 'column',
                labelWidth: 75,
                bodyStyle: 'padding-top:10px',
                defaults:{
					baseCls:'x-plain',
					columnWidth:.5,
					layout:'form'
				},
                items: [{
                	
                	items:[{
	                    fieldLabel: '地区',
	                    xtype: 'combo',
	                    allowBlank: false,
	                    hiddenName: 'county_name',
	                    id: 'AddressCountyId',
	                    store: new Ext.data.JsonStore({
	                        url: root + '/system/Address!queryFgsByCountyId.action',
	                        fields: ['dept_id', 'dept_name']
	                    }),
	                    displayField: 'dept_name',
	                    valueField: 'dept_id',
	                    listeners: {
	                        scope: this,
	                        'select': function (combo, record, index) {
	                            CHANGEThat.forCountyId = combo.getValue();
	                            this.doReset();
	                            this.loadGo(combo.getValue());
	                        }
	                    }
                    }]
                },{
                	id:'bacthDeptItemId',
                	items:[{
                		fieldLabel: '转入区域',
                    	xtype: 'combo',
		                width: 140,
		                allowBlank: false,
		                hiddenName: 'bacthDept',
		                id: 'bacthDeptId',
		                store: new Ext.data.JsonStore({
		                    url: root + '/system/Address!queryAddrByCountyId.action',
		                    fields: ['addr_id', 'addr_name']
		                }),
		                displayField: 'addr_name',
		                valueField: 'addr_id',
		                editable : true,
						triggerAction : 'all',
						forceSelection : true
				}]
                },{
                	id:'BacthFielId',
                	items:[{id:'checkInFielId',fieldLabel:'小区文件', tooltip:'小区文件：第一列：小区编号号',width : 200,name:'file',xtype:'textfield',inputType:'file',allowBlank:false,emptyText:''}]
                }
                ]
            }, {
                region: 'south',
                height:400,
                border: false,
                id:'addrGridId',
                layout: 'border',
                items: [{
                    region: 'west',
                    width: '45%',
                    border: false,
                    layout: 'fit',
                    items: [this.IdleAddrGrid]
                }, {
                    border: false,
                    bodyStyle: 'background-color: #DFE8F6;',
                    region: 'center',
                    layout: 'fit',
                    width: 60,
                    layout: {
                        type: 'vbox',
                        pack: 'center',
                        align: 'center'
                    },
                    items: [{
                        xtype: 'button',
                        iconCls: 'move-to-right',
                        width: 40,
                        scale: 'large',
                        tooltip: '将左边已勾选的小区分配给该区域!',
                        iconAlign: 'center',
                        scope: this,
                        handler: function () {
                            this.doProdIn(this.setProd.getSelections());
                        }
                    }, {
                        height: 30,
                        baseCls: 'x-plain'
                    }, {
                        xtype: 'button',
                        iconCls: 'move-to-left',
                        tooltip: '将右边勾选中的小区取消!',
                        width: 40,
                        scale: 'large',
                        iconAlign: 'center',
                        scope: this,
                        handler: function () {
                            this.doProdOut(this.setUprod.getSelections());
                        }
                    }]
                },{
                width: '45%',
                region: 'east',
                layout: 'fit',
                border: false,
                items: [this.useAddrGrid]
            }]
            }
            ],
            tbar:['-',{
	                    xtype: 'button',
	                    text: '批量转入',
	                    width: 100,
	                    id: 'batchGoToId',
	                    iconCls:'icon-excel',
	                    listeners: {
	                        scope: this,
	                        click: this.fileCheckIn
	                    }
	                },'-',{
	                    xtype: 'button',
	                    text: '明细转入',
	                    width: 100,
	                    disabled:true,
	                    id: 'detailGoToId',
	                    iconCls:'icon-hand',
	                    listeners: {
	                        scope: this,
	                        click: this.detailCheckIn
	                    }
	                },'-'],
            buttons: [{
                text: '保存',
                scope: this,
                handler: this.save
            }, {
                text: '重置',
                scope: this,
                handler: function () {
                    this.doAllReset();
                }
            }]
        });
        this.getForm().findField('county_name').getStore().load();
        this.getForm().findField('county_name').getStore().on("load", this.doLoadData, this);
    },
	fileCheckIn:function(){
		Ext.getCmp('addrGridId').hide();
		Ext.getCmp('BacthFielId').show();
		Ext.getCmp('detailGoToId').setDisabled(false);
		Ext.getCmp('bacthDeptItemId').show();
		this.key = 'BACTH';
		this.doLayout();
	},
	detailCheckIn:function(){
		Ext.getCmp('addrGridId').show();
		Ext.getCmp('BacthFielId').hide();
		Ext.getCmp('bacthDeptItemId').hide();
		this.key = 'DETAIL';
		this.doLayout();
	},
	doAllReset : function(){
		this.getForm().reset();
		this.doReset();
	},
    doReset: function () {
        if(this.key == 'DETAIL'){
	        Ext.getCmp('toDeptId').reset();
	        Ext.getCmp('goDeptId').reset();
	        this.idleAddrStroe.removeAll();
	        this.useAddrStroe.removeAll();
        }else{
            Ext.getCmp('bacthDeptId').reset();
	        var fileComp =this.getForm().findField('file');
	        fileComp.getEl().dom.select();
			document.execCommand("delete");
        }
		this.doLayout();
    },
    doLoadData: function () {
        if (this.getForm().findField('county_name').getStore().getCount() > 0) {
            this.getForm().findField('county_name').setValue(this.getForm().findField('county_name').getStore().getAt(0).data.dept_name);
            this.forCountyId = this.getForm().findField('county_name').getStore().getAt(0).data.dept_id;
        }
		Ext.getCmp('BacthFielId').hide();
		Ext.getCmp('bacthDeptItemId').hide();
        this.loadGo(this.forCountyId);
    },
    loadGo: function (v) {
        Ext.getCmp('goDeptId').getStore().load({
            params: {
                countyId: v
            }
        });
        Ext.getCmp('toDeptId').getStore().load({
            params: {
                countyId: v
            }
        });
        Ext.getCmp('bacthDeptId').getStore().load({
            params: {
                countyId: v
            }
        });
        
    },
    save: function () {
        var all = {};
        all['countyId'] = this.forCountyId;
        //明细导入
        if(this.key == 'DETAIL'){
			if(Ext.isEmpty(Ext.getCmp('toDeptId').getValue())){
				Alert("请选择转入的区域!");
	            return;
			}
	        if (Ext.getCmp('toDeptId').getValue() == Ext.getCmp('goDeptId').getValue()) {
	            Alert("转入区域和转出区域相同不能操作!");
	            return;
	        }
	        var store = this.useAddrGrid.getStore();
	        if (store.getCount() == 0) {
	            Alert("请选择小区!");
	            return;
	        }
	        var addrIds = [];
	        store.each(function (record) {
	            addrIds.push(record.get('addr_id'))
	        });
	        all['newAddrId'] = Ext.getCmp('toDeptId').getValue();
	        all['addrId'] = addrIds.join(",");
	        Confirm("确定保存吗?", this, function () {
	            mb = Show();
		        Ext.Ajax.request({
		                url: root + '/system/Address!changeAddr.action',
		                params: all,
		                scope:this,
		                success: function (res, ops) {
		                    mb.hide(); // 隐藏提示框
		                    mb = null;
		                    var rs = Ext.decode(res.responseText);
		                    if (true === rs.success) {
								if(rs.simpleObj!=null){
									this.doAllReset();
									Confirm("操作成功!有部分客户地址需要手动修改，【是】将手动修改，【否】关闭窗口", this, function () {
										var win = new CustAddrWin(rs.simpleObj,this.forCountyId);
										win.show();
									});
								}else{
									Alert('操作成功!', function () {
			                            this.doAllReset();
			                        }, this);
								}
							}else{
								Alert('操作失败');
							}
		                }
		            });
	        });
        }
         //批量导入
        if(this.key == 'BACTH'){
        	if(Ext.isEmpty(Ext.getCmp('bacthDeptId').getValue())){
				Alert("请选择转入的区域!");
	            return;
			}
        	var file = Ext.getCmp('checkInFielId').getValue();
			var flag = this.checkFileType(file);
			if(!flag)return;
	        all['newAddrId'] = Ext.getCmp('bacthDeptId').getValue();
	        Confirm("确定保存吗?", this, function () {
	            mb = Show();
	            this.getForm().submit({
	            	url: root + '/system/Address!changeAddr.action',
					scope:this,
					params:all,
					success:function(form,action){
						mb.hide();mb = null;
						var data = action.result;
						if(data.success == true){
							if(data.msg){//错误信息
								Alert(data.msg);
							}else if(data.records != null && data.records.length>0){
								this.doAllReset();
								Confirm("操作成功!有部分客户地址需要手动修改，【是】将手动修改，【否】关闭窗口", this, function () {
									var win = new CustAddrWin(data.records,this.forCountyId);
									win.show();
								});
							}else{
								Alert('操作成功!',function(){
									this.doAllReset();
								},this);
							}
						}
					}
				});
			})
        }
        
    },
    checkFileType :function(fileText){
		if(fileText.lastIndexOf('xlsx')>0 || fileText.lastIndexOf('.xlsx')==fileText.length-5){
			Alert('请选择excel2003文件进行上传,文件后缀名为.xls!');
			return false;
		}else if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
			Alert('请选择excel文件进行上传！');
			return false;
		}
		return true;
	},
    doProdIn: function (arr) {
        if (arr.length == 0) {
            Alert("请在左边的列表中选择系统功能!");
            return;
        }
        for (var i = 0; i < arr.length; i++) {
            var panl = this.useAddrStroe.recordType;
            var u = new panl({
                addr_id: arr[i].data.addr_id,
                addr_name: arr[i].data.addr_name,
                num: arr[i].data.num
            });
            this.useAddrGrid.stopEditing();
            this.useAddrStroe.insert(this.useAddrStroe.getCount(), u);
            this.useAddrGrid.startEditing(this.useAddrStroe.getCount(), 0);
            this.idleAddrStroe.remove(arr[i]);
        }
        Ext.getCmp('selectAddrTo').setValue("");
        this.idleAddrStroe.filterBy(function (record) {
            return true;
        });
    },
    doProdOut: function (arr) {
        if (arr.length == 0) {
            Alert("请在右边的列表中选择系统功能!");
            return;
        }
        for (var i = 0; i < arr.length; i++) {
            var panl = this.idleAddrStroe.recordType;
            var u = new panl({
                addr_id: arr[i].data.addr_id,
                addr_name: arr[i].data.addr_name,
                num: arr[i].data.num
            });
            this.IdleAddrGrid.stopEditing();
            this.idleAddrStroe.insert(this.idleAddrStroe.getCount(), u);
            this.IdleAddrGrid.startEditing(this.idleAddrStroe.getCount(), 0);
            this.useAddrStroe.remove(arr[i]);
        }
        Ext.getCmp('selectAddrGo').setValue("");
        this.useAddrStroe.filterBy(function (record) {
            return true;
        });
    }
});


ChangeAddress = Ext.extend(Ext.Panel, {
    constructor: function () {
        var changeAddressForm = new ChangeAddressForm();
        ChangeAddress.superclass.constructor.call(this, {
            id: 'ChangeAddress',
            title: '小区管理',
            closable: true,
            border: false,
            layout: 'border',
            baseCls: "x-plain",
            items: [changeAddressForm]
        });
    }
});