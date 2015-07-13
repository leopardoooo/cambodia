ProdDictTree = Ext.extend(Ext.ux.tree.TreeGridEditor,{
	constructor : function(){
		ProdDictTree.superclass.constructor.call(this,{
			id : 'ProdDictTree',
			loader: new Ext.tree.TreeLoader({
	            dataUrl: root+"/system/Prod!queryProdDictByCountyId.action",
	            baseParams: {
	                method: 'load'
	            }
	        }),
	        mouseoverShowObar: true,// mouseover事件触发显示Obar
	        singleEdit: true,// 只允许同时编辑一条记录
	        // 显示列
	        columns: [{
	            header: '目录名称',
	            dataIndex: 'text',
	            autoWidth: true,
	            displayTip: true
	        }],
	        // 设置Obar
	        obarCfg: {
	            column: {
	                header: '操作',
	                dataIndex: 'id',
	                width: 350
	            },
	            btns: [{
	                id: 'add',
	                handler : function(n){
	                	new ProdDictWin('add',n).show();
	                }
	            },{
	                id: 'edit',
	                handler : function(n){
	                	if(n.attributes.others.countyId != App.data.optr['county_id'] && App.data.optr['county_id'] != '4501'){
	                		Alert('非本地区所建,无法修改!')
	                		return;
	                	}
	                	new ProdDictWin('edit',n).show();
	                }
	            }, {
	                id: 'remove',
	                handler : this.doDelete,
	                // 删除节点校验函数
	                validator: this.checkRemove
	            }]
	        }
		})
		
		this.expandAll();
	},
	checkRemove : function(n){
		if(n.others.countyId != App.data.optr['county_id'] && App.data.optr['county_id'] != '4501'){
    		Alert('非本地区所建产品目录,无法删除!')
    		return false;
    	}
        return true;
	},
	doDelete : function(n){
		Confirm("确定要删除该数据吗?", this ,function(){
			var msg = Show();
			Ext.Ajax.request({
				scope : this,
				url : root+"/system/Prod!removeDict.action",
				params : {
					doneId : n.id
				},
				success : function(res,opt){
					msg.hide();msg=null;
					var rs = Ext.decode(res.responseText);
					var data = Ext.decode(rs.simpleObj);
					if(data['success'] == false){
						Alert(data['msg']);
					}else{
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('ProdDictTree').getRootNode().reload();
							Ext.getCmp('ProdDictTree').expandAll();
						}else{
							Alert('操作失败');
				 		}
					}
				}
			})
		});
	}
});


DictCountyTree = Ext.extend(Ext.tree.TreePanel, {
    nodeId: null,
    nodePid:null,
    type:null,
    checkchange : false,
    constructor: function (v,p,t) {
    	if (!Ext.isEmpty(v)) {
            this.nodeId = v;
            this.nodePid = p;
            this.type = t;
    	}
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Prod!getDictCountyTree.action?query=" + this.nodeId +"&doneId="+this.nodePid +"&ServId="+ this.type   
        });
        DictCountyTree.superclass.constructor.call(this, {
        	id : 'DictCountyTree',
        	border : false,
            region : 'east',
            width: '50%',
            margins: '0 0 3 2',
            layout: 'fit',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '-1',
                iconCls: 'x-tree-root-icon',
                checked : false,
                text: '应用地市'
            }
        });
        this.expandAll();
    },
       listeners: {
        'checkchange': function (node, checked) {
            node.expand();
            node.attributes.checked = checked;
            node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
        }
    }
});


ProdDictWin = Ext.extend(Ext.Window,{
	node : null,
	title : null,
	record : null,
	form : null,
	dictCountyTree:null,
	constructor : function(type,node){
		this.record = {};
		//操作节点
		this.node = node;
		if(type == 'add'){
			this.title = '增加目录';
			this.record.node_pid = node.attributes.id;
		}else if(type == 'edit'){
			this.title = '修改目录';
			this.record.node_id = node.attributes.id;
			this.record.node_pid = node.attributes.pid;
			this.record.node_name = node.attributes.text;			
		}
		
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 85,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			items : [{
				xtype : 'hidden',
				name : 'node_id'
			},{
				xtype : 'hidden',
				name : 'node_pid'
			},{
				xtype : 'textfield',
				name : 'node_name',
				allowBlank : false,
				fieldLabel : '目录名称'
			}]
		});
		this.form.getForm().loadRecord(new Ext.data.Record(this.record));

		this.dictCountyTree = new DictCountyTree(node.attributes.id,node.attributes.pid,type);
		
		
		ProdDictWin.superclass.constructor.call(this,{
			title : this.title,
			height : 450,
			width : 500,
			layout: 'border',
			closeAction : 'close',
			items : [{
					region : 'west',
					layout : 'fit',
					split : true,
					width : '250',
					items : [this.form]
				},{
					region : 'center',
					layout : 'fit',
					items : [this.dictCountyTree]
				}],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	doSave : function(){
		//添加和修改
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
				mb = Show();//显示正在提交
				var oldValues = this.form.getForm().getValues(),newValues = {};
				for(var key in oldValues){
					newValues['prodDict.'+key] = oldValues[key];
				}
				var prodCountyIds = [];
			    if (Ext.getCmp('DictCountyTree')) {
		            var nodes = Ext.getCmp('DictCountyTree').getChecked();
		            for (var i in nodes) {
		                if (nodes[i].leaf) {
		                    prodCountyIds.push(nodes[i].id);
		                }
		            }
		        }
		        newValues["countyId"] = prodCountyIds.join(",");
				Ext.Ajax.request({
					url : root+"/system/Prod!saveProdDict.action",
					params : newValues,
					scope : this,
					success : function(res,opt){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('ProdDictTree').getRootNode().reload();
							Ext.getCmp('ProdDictTree').expandAll();
							
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			})
		}
	}
})


ProdDictView = Ext.extend(Ext.Panel,{
	constructor : function(){
		AddressView.superclass.constructor.call(this,{
			id : 'ProdDictView',
			layout : 'fit',
			title : '目录管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new ProdDictTree()]
		})
	}
})
