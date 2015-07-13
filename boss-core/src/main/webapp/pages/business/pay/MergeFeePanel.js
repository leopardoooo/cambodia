/**
 * 合并打印项
 */ 

MergeFeePanel = Ext.extend( Ext.Panel , {
	
	leftGrid: null ,
	leftStore: null,
	selModel: null,
	
	right: null,
	
 	constructor: function(){
	 	// 选择的模型
	 	this.selModel = new Ext.grid.CheckboxSelectionModel({
			singleSelect: false
		});
	 	this.leftStore = new Ext.data.JsonStore({
	 		url: Constant.ROOT_PATH+"/core/x/Pay!queryUnMergeFees.action",
			fields: [
				'fee_sn','fee_text',
				{name: 'real_pay', type: 'int'},
				'printitem_id','printitem_name',
				'create_time'
			]
	 	});
	 	//左边Grid实例化
	 	this.leftGrid = new Ext.grid.GridPanel({
	 		title: '未合并的项',
	 		region: 'center',
	 		border: true,
	 		autoScroll: true,
			autoExpandColumn: 'e',
			columns: [
				this.selModel,
				{ id: 'e', header: '费用名称', dataIndex: 'fee_text'},
				{ header: '打印名称', dataIndex: 'printitem_name', width: 110},
				{ header: '实付金额', dataIndex: 'real_pay', width: 100, xtype: 'moneycolumn'},
				{ header: '费用生成时间', dataIndex: 'create_time', width: 90}
			],
			sm: this.selModel,
			ds: this.leftStore
	 	});
	 	this.right = new Ext.tree.TreePanel({
	 		region: 'center',
	 		title: '已合并的项',
	 		iconCls: 'icon-busi',
	 		// useArrows: true,
		    animate: false,
		    autoScroll: true,
		    bodyStyle: 'padding: 5px;padding-top: 2px;',
		    root: new Ext.tree.AsyncTreeNode({
		    	text: '所有已合并的项',
		    	expanded: true,
		    	children: []
		    }),
	 		tbar: [{
	 			width: 160,
	 			id: 'cmbPrintItem',
	 			emptyText: '请选择新打印项名称',
	 			allowBlank: false,
				hiddenName: 'printitem',
				xtype: 'paramcombo',
				paramName: 'PRINTITEM_NAME'
	 		}]
	 	});
	 	// 组装
 		MergeFeePanel.superclass.constructor.call(this,{
 			layout: 'border',
 			border: false,
 			items: [this.leftGrid,{
 				region: 'east',
 				border: false,
 				layout: 'border',
 				width: 340,
 				items:[this.right , {
	 				border: false,
	 				bodyStyle: 'background-color: #DFE8F6;',
	 				region: 'west',
	 				width: 60,
	 				layout: {
					    type: 'vbox',
					    pack: 'center',
					    align: 'center'
					},
					items:[{
					    xtype: 'button',
					    iconCls: 'move-to-right',
					    width: 40,
			            scale: 'large',
			            tooltip: '将左边已勾选的费用项合并!',
			            iconAlign: 'center',
			            scope: this,
			            handler: function(){
			            	this.doMergeFees(this.selModel.getSelections());
			            }
					},{
						height: 30,
						baseCls: 'x-plain'
					},{
						xtype: 'button',
					    iconCls: 'move-to-left',
					    tooltip: '将右勾选中的费用项重新合并!',
					    width: 40,
			            scale: 'large',
			            iconAlign: 'center',
			            scope: this,
			            handler: function(){
			            	this.doReMergeFees(this.right.getChecked());
			            }
					}]
	 			}]
 			}],
 			buttons: [{
 				text: '保存并打印',
 				width: 120,
 				scope: this,
 				handler: this.doSave
 			}]
 		});
 		this.doInit();
 	},
 	doInit: function(){
 		var cmb = Ext.getCmp('cmbPrintItem');
 		App.form.initComboData( [cmb]);
 		//加载数据
		this.leftStore.load({
			params: {custId: App.getCustId()}
		});
		this.leftGrid.on("rowdblclick" , function( grid , rowIndex){
			this.doMergeFees([this.leftStore.getAt(rowIndex)]);
		},this);
		cmb.on("change", this.doChangePrintItemName, this);
 	},
 	/**
 	 * 合并费用项
 	 */
 	doMergeFees: function( arr ){
 		var feeNodes= [],checkNode,
 			root= this.right.getRootNode(),cns= root.childNodes;
 		if(arr.length == 0 ){
 			Alert("请在左边的费用列表中选择你需要合并的项!");
 			return ;
 		}
 		// get checked node 
 		for(var i = 0; i< cns.length ;i++){
			if(cns[i].getUI().isChecked()){
				checkNode = cns[i];
				break ;
			}
		}
 		if(!checkNode){
 			var text= '<font color="red">打印项' + (cns.length + 1)+ "</font>";
 			checkNode = this.createPrintItemNode( text );
 		}
 		//在新生成的打印项节点，添加费用项子节点
 		for(var i = 0; i< arr.length; i++){
 			feeNodes[i] = this.createFeeNode(arr[i]);
 			this.leftStore.remove(arr[i]);
 		}
 		root.appendChild( checkNode );
 		checkNode.appendChild(feeNodes);
 		this.right.expandAll();
 	},
 	/**
 	 * 删除右边已经选择的费用项，重新合并
 	 */
 	doReMergeFees: function(selNodes){
 		var pNode = {}, p; //使用pNode = {} 是为了去重复
		Ext.each(selNodes, function(node){
		 	if(node.leaf){
		 		this.leftStore.add( node.attributes.data );
		 		p = node.parentNode;
		 		p.removeChild(node);
		 		pNode[p.id] = p ;
		 	}
        } , this);
       	//删除 打印项节点
       	var n;
       	for( var id in pNode){
       		n = pNode[id];
       		if( n.childNodes.length == 0 && n.parentNode){
		 		n.parentNode.removeChild(n);
		 	}
       	}
 	},
 	// private 创建打印项节点
	createPrintItemNode: function( text ){
		var node =  new Ext.tree.TreeNode({
			text: text,
	   		checked: false,
	   		leaf: false,
	   		qtip: '选中可以更改打印名称!',
	   		listeners: {
	   			scope: this,
	   			checkchange: this.doCheckChildNodes
	   		}
		});
		return node;
	},
	// private 创建费用项节点
	createFeeNode: function( r ){
		return new Ext.tree.TreeNode({
			id: r.get("fee_sn"),
			text: r.get('fee_text'),
	   		checked: false,
	   		leaf: true,
	   		data: r,
	   		listeners: {
	   			scope: this,
	   			dblclick: function( node ){
	   				this.doReMergeFees([node]);
	   			}
	   		}
		});
	},
	// 选中/不选中当前节点下的所有子节点
 	doCheckChildNodes: function(node, checked){
 		node.eachChild(function(n) {
	        n.getUI().toggleCheck(checked);
	    });
 	},
 	doChangePrintItemName: function(){
 		var cmbPrintItem = Ext.getCmp('cmbPrintItem');
 		var v = cmbPrintItem.getValue();
 		if( v ){
 			var text = App.form.getCmbDisplayValue(cmbPrintItem);
 			var nodes = this.right.getChecked();
 			for(var i = 0;i < nodes.length ;i++){
 				if(nodes[i].leaf) continue;
 				nodes[i].setText( text);
 				nodes[i].attributes.isValid = true;
 				nodes[i].attributes.printitem_id = v ;
 			}
 		}else{
 			cmbPrintItem.isValid();
 		}
 		return false;
 	},
 	doReset: function(){},
 	//提交的验证函数
 	doValid: function(){ 
 		var nodes = this.right.getRootNode().childNodes;
 		if(nodes.length == 0){
 			Alert("没有需要保存的合并项!");
 			return false;
 		}
 		for(var i =0;i<nodes.length;i++){
 			if(!nodes[i].attributes.isValid){
 				Alert("请设置打印项的名称，选中打印项名称后，点击编辑即可!");
 				return false;
 			}
 		}
 		return true;
 	},
 	doSave: function(){
 		if(!this.doValid()) return false;
 		var mb = Show();
		//提交
		Ext.Ajax.request({
			scope: this,
			params: this.getValues(),
			url: Constant.ROOT_PATH+"/core/x/Pay!saveMergeFee.action",
			success: function( res, ops){
				var o = Ext.decode( res.responseText );
				mb.hide();
				if(o["exception"]){
					new DebugWindow( o , "btnBusiSave").show();
				} else {
					App.getApp().menu.bigWindow.toggleUrl( "打印" , {width: 700, height: 450},"/pages/business/pay/Print.jsp" );
				}
			}
		});
 	},
 	getValues: function(){
 		var nodes = this.right.getRootNode().childNodes;
 		var ps=[],item,feeSn,childs,data,amount=0;
 		for(var i=0;i<nodes.length; i++){
 			feeSn=[];
 			childs = nodes[i].childNodes;
 			amount = 0;
 			for(var j=0;j<childs.length;j++){
 				data = childs[j].attributes.data;
 				feeSn[j] = data.get('fee_sn');
 				amount += data.get("real_pay")
 			}
 			item={
 				printitem_id: nodes[i].attributes.printitem_id,
 				amount: amount,
 				fee_sns: feeSn
 			};
 			ps[i] = item;
 		}
 		var all = {
 			merges: Ext.encode(ps)
 		}
		all["parameter.custFullInfo.cust.cust_id"] = App.getCustId();
		all["parameter.busiCode"] = '1027';
		return all;
 	}
});

Ext.onReady(function(){
	var merge = new MergeFeePanel();
	TemplateFactory.gViewport(merge);
});
