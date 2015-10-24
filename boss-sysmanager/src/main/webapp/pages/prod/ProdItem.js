// 动态资源
DynResGrid = Ext.extend(Ext.grid.EditorGridPanel, {
    dynResStore: null,
    dynsm: null,
    prodId:null,
    constructor: function (v) {
    	this.prodId = v;
    	dynThis = this;
        this.dynResStore = new Ext.data.JsonStore({
            fields: ['group_id','group_name','serv_id','group_desc','res_number',{name : 'max_count',type : 'float'}]
        });
        this.dynsm = new Ext.grid.CheckboxSelectionModel();
        var cm = new Ext.grid.ColumnModel([{
            header: '组名称',
            dataIndex: 'group_name',
            width: 100,
            renderer: App.qtipValue
        }, {
            header: '数量',
            dataIndex: 'res_number',
            id: 'resnumber',
            width: 50,
            editor: new Ext.form.NumberField({
                minValue: 0,
                allowBlank: false,
                allowNegative: false,
                allowDecimals: false
            }),
            renderer: App.qtipValue
        }]);
        cm.isCellEditable = this.cellEditable;
        DynResGrid.superclass.constructor.call(this, {
            id: 'dynResGrid',
            ds: this.dynResStore,
            clicksToEdit: 1,
            sm: this.dynsm,
            cm: cm
        })
    },
    showData: function (res) {
        this.dynResStore.loadData(res);
    },
    initEvents: function () {
        DynResGrid.superclass.initEvents.call(this);
        this.on("afteredit", function (obj) {
            // grid编辑之后触发事件
            if (obj.field == "res_number") {
                if (obj.record.get('max_count') <= obj.value) {
                    Alert('数值不能大于或等于该资源组的总资源数!');
                    this.dynResStore.rejectChanges();
                }
            }
        })
    },
	cellEditable:function(colIndex,rowIndex){
		var resNumberIndex = this.getIndexById("resnumber");
		var record = dynThis.getStore().getAt(rowIndex);
		if(resNumberIndex === colIndex){
			if(!Ext.isEmpty(dynThis.prodId)){
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	}
});
CountyResTree = Ext.extend(Ext.tree.TreePanel, {
    searchFieldWidth: 100,
    chick: false,
    constructor: function (v, areaId) {
        countyResTreeThis = this;
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Prod!getDistrictResTree.action?ServId=" + v + "&areaId=" + areaId
        });

        CountyResTree.superclass.constructor.call(this, {
            region: 'west',
            width: 220,
            split: true,
            id: 'countyResId',
            margins: '0 0 3 2',
            autoScroll: true,
            rootVisible: false,
            enableDD: true,
            containerScroll: true,
            ddGroup: 'organizerDD',
            animate: true,
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '0',
                iconCls: 'x-tree-root-icon',
                nodeType: 'async',
                draggable: false,
                text: '地市结构'
            },
            listeners: {
                nodedragover: function (dropEvent) {
                    dropEvent.target.expand();
                },
                beforeload:function(node){
					if(node.attributes.others && node.attributes.others.att == "res" && Ext.getCmp('startdt').getValue().format('Y-m-d') <= nowDate().format('Y-m-d')){
						node.disable();
					}
                },
                beforenodedrop: function (dropEvent) {
                    var node = dropEvent.target; // 目标结点
                    var data = dropEvent.data; // 拖拽的数据
                    var point = dropEvent.point; // 拖拽到目标结点的位置
                    // 如果data.node为空，则不是tree本身的拖拽，而是从grid到tree的拖拽，需要处理
                    if (!data.node) {
                        var nodeData = Ext.getCmp('resdatas').getRecords(data.nodes);
                        if (!this.isCanClick(node, point, data.nodes)) {
                            return;
                        }

                        this.validCountyRes(node, point, data.nodes, nodeData);
                    }
                },
                startdrag: function (tree, node) {
                    if (node.id == '0' || node.attributes.others.att != "res") {
                        Alert("只能删除资源");
                        return false;
                    }
                    Confirm("确定删除吗?", this, function () {
                        node.remove();
                    })
                }
            }
        });

        if (App.data.optr['count_id'] != '4501') {
            this.expandAll();
        } else {
            this.getRootNode().expand();
        }

    },
    addRes: function (node, point, nodeData) {
        switch (point) {
        case 'append':
            // 添加时，目标结点为node，子结点设为空
            this.ddFunction(node, null, nodeData);
            break;
        case 'above':
            // 插入到node之上，目标结点为node的parentNode，子结点为node
            this.ddFunction(node.parentNode, node, nodeData);
            break;
        case 'below':
            // 插入到node之下，目标结点为node的parentNode，子结点为node的nextSibling
            this.ddFunction(node.parentNode, node.nextSibling, nodeData);
            break;
        }
    },
    getCountyRes: function () {
        return this.getSelectedItems(this.root);
    },
    getSelectedItems: function (_root) {
        if (_root) {
            var areanodes = _root.childNodes;
            if (areanodes.length == 0) {
                return;
            }
            var data = [];
            for (var i = 0; i < areanodes.length; i++) {
                if (areanodes[i].loaded == false) {
                    areanodes[i].expand();
                    areanodes[i].collapse();
                }
                var countynodes = areanodes[i].childNodes;
                if (countynodes.length > 0) {
                    for (var j = 0; j < countynodes.length; j++) { // wuhanshi
                        if (countynodes[j].loaded == false) {
                            countynodes[j].expand();
                            countynodes[j].collapse();
                        }
                        var resnodes = countynodes[j].childNodes;
                        if (resnodes.length > 0) { // 3
                            for (var k = 0; k < resnodes.length; k++) {
                                data.push({
                                    county_id: countynodes[j].id,
                                    res_id: resnodes[k].id
                                });
                            }
                        }
                    }
                }
            }
            return data;
        }
    },
    ddFunction: function (node, refNode, selections) {
        if (node.id == '4500' || node.id == '4501') {
            var areas = node.ownerTree.root.childNodes;
            for (var j = 0; j < areas.length; j++) {
                if (areas[j].loaded == false) {
                    areas[j].expand();
                    areas[j].collapse();
                }
                var countys = areas[j].childNodes;
                for (var k = 0; k < countys.length; k++) {
                    if (countys[k].loaded == false) {
                        countys[k].expand();
                        countys[k].collapse();
                    }
                    for (var i = 0; i < selections.length; i++) {
                        var ress = countys[k].childNodes;
                        var record = selections[i].data;
                        if (ress.length > 0) {
                            for (var y = 0; y < ress.length; y++) {
                                if (ress[y].id == record.res_id) {
                                    countys[k].expand();
                                    ress[y].remove();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            var areas = node.ownerTree.root.childNodes;
            for (var j = 0; j < areas.length; j++) {
                if (areas[j].loaded == false) {
                    areas[j].expand();
                    areas[j].collapse();
                }
                var countys = areas[j].childNodes;
                for (var k = 0; k < countys.length; k++) {
                    if (countys[k].loaded == false) {
                        countys[k].expand();
                        countys[k].collapse();
                    }
                    if (countys[k].id == '4501') {
                        for (var i = 0; i < selections.length; i++) {
                            var record = selections[i].data;
                            var ress = countys[k].childNodes;
                            if (ress.length > 0) {
                                for (var y = 0; y < ress.length; y++) {
                                    if (ress[y].id == record.res_id) {
                                        countys[k].expand();
                                        ress[y].remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (var i = 0; i < selections.length; i++) {
            var record = selections[i].data;
            node.expand();
            node.appendChild(new Ext.tree.TreeNode({
                text: record.res_name,
                id: record.res_id,
                leaf: true,
                others: {
                    att: 'res'
                },
                cls: 'image-node'
            }));
        }
    },
    isCanClick: function (node, point, data) {
        if (point == "append") {
            if (node.childNodes.length > 0) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    for (var j = 0; j < data.length; j++) {
                        if (node.childNodes[i].attributes.id == data[j].id) {
                            Alert("资源已经存在");
                            return false;
                        }
                    }
                }
            }
            if (node.attributes.others && node.attributes.others.att == 'county') {
                return true;
            } else {
                Alert('请移到县市区的地方');
                return false;
            }

        } else if ((point == "above" || point == "below")) {

            if (node.parentNode.childNodes.length > 0) {
                for (var i = 0; i < node.parentNode.childNodes.length; i++) {
                    for (var j = 0; j < data.length; j++) {
                        if (node.parentNode.childNodes[i].attributes.id == data[j].id) {
                            Alert("资源已经存在");
                            return false;
                        }
                    }
                }
            }

            if (node.attributes.others && node.attributes.others.att == 'res') {
                return true;
            } else {
                Alert('请移到跟资源平级的地方');
                return false;
            }
        }
    },
    validRes: function (node, data) {
        if (node.childNodes.length > 0) {
            for (var i = 0; i < node.childNodes.length; i++) {
                for (var j = 0; j < data.length; j++) {
                    if (node.childNodes[i].attributes.id == data[j].id) {
                        Alert("资源已经存在");
                        return false;
                    }
                }
            }
        }
    },
    validCountyRes: function (node, point, data, nodeData) {
        var resArray = [];
        for (var i in data) {
            if (!Ext.isEmpty(data[i].id)) {
                resArray.push(data[i].id);
            }
        }
        var resRecords = resArray.join(",");
        Ext.Ajax.request({
            url: root + '/system/Prod!validRes.action',
            params: {
                records: resRecords,
                doneId: node.attributes.id
            },
            scope: this,
            success: function (res, ops) {
                var rs = Ext.decode(res.responseText);
                if (rs.simpleObj != null) {
                    Alert('【' + rs.simpleObj.resNames + '】不能配置到该县市!');
                    return false;
                } else {
                    this.addRes(node, point, nodeData);
                }
            }
        });
    }
});
// 资源组件
ResProdForm = Ext.extend(Ext.Panel, {
    dynResGrid: null,
    prodId: null,
    selectType: null,
    dyndata: null,
    staticdata: null,
    areaId: null,
    constructor: function (prodId) {
//        this.dynResGrid = new DynResGrid(prodId);
        this.prodId = prodId;
        ResThis = this;
        ResProdForm.superclass.constructor.call(this, {
            region: 'center',
            split: true,
            layout: 'border',
            id: 'resForm',
            title:'配置资源',
            defaults: {
                layout: 'fit',
                border: false
            },
//            tbar: [{
//                text: '选择资源',
//                iconCls: 'icon-add-user'
//            }, '-',
//            {
//                fieldLabel: '费用类型',
//                xtype: 'combo',
//                hiddenName: 'feeType',
//                store: new Ext.data.ArrayStore({
//                    fields: ['text', 'value'],
//                    data: [
//                        ['配置静态资源', 'STATIC'],
//                        ['配置动态资源', 'DYN']
//                    ]
//                }),
//                displayField: 'text',
//                valueField: 'value',
//                allowBlank: false,
//                value: 'STATIC',
//                listeners: {
//                    scope: this,
//                    select: this.doSelectRes
//                }
//            }],
            items: [{
                region: 'center',
                spilt: true,
                layout: 'fit',
                items: []
//            },{
//                region: 'east',
//                width:'20%',
//                spilt: true,
//                layout: 'fit',
//                items: [this.dynResGrid]
            }]
        })
    },
//    doSelectRes: function (combo) {
//        if (combo.getValue() != this.selectType) {
//            this.selectType = combo.getValue();
//            if (combo.getValue() == 'STATIC') {
//                this.createRes();
//            } else {
//                this.items.itemAt(0).removeAll();
//                this.items.itemAt(0).add(new DynResGrid());
//                this.items.itemAt(0).doLayout();
//                Ext.getCmp('dynResGrid').showData(this.dyndata);
//            }
//        }
//    },
    showData: function (dyndata, staticdata, areaId) {
//        this.dynResGrid.showData(dyndata);
//        this.selectType = 'STATIC';
//        this.dyndata = dyndata;
        this.staticdata = staticdata;
        this.areaId = areaId;

        this.createRes();
    },
    createRes: function () {
        var resPanel = this.createResPanel(this.staticdata, this.areaId);
        this.items.itemAt(0).removeAll();
        this.items.itemAt(0).add(resPanel);
        this.items.itemAt(0).doLayout();
        var dragZone = new ImageDragZone(Ext.getCmp('resdatas'), {
            containerScroll: true,
            ddGroup: 'organizerDD'
        });
    },
    createResPanel: function (staticdata, areaId) {
        if (0 < staticdata.length) {
            var store = new Ext.data.JsonStore({
                fields: ['res_id', 'res_name', 'serverIds', 'currency',
                {
                    name: 'serverName',
                    mapping: 'serverIds',
                    convert: this.shortName
                }]
            });
            store.loadData(staticdata);
            
            var dataview = new Ext.DataView({
                store: store,
                tpl: new Ext.XTemplate('<tpl for=".">', '<tpl if="values.currency == \'T\'">', 
                '<div class="viewres" id="{res_id}" title="{res_name}{serverIds}">', '<span>{res_name}</br>{serverName}</span>', '</div>', '</tpl>', 
                '<tpl if="values.currency == \'F\'">', 
                	'<div class="viewres" id="{res_id}" title="{res_name}{serverIds}">', 
                		'<span style="color: 000099;">{res_name}</br>{serverName}</span>', '</div>', '</tpl>', '</tpl>'),
                selectedClass: 'x-view-selected',
                id: 'resdatas',
                itemSelector: 'div.viewres',
                overClass: 'x-view-over',
                plugins: new Ext.DataView.DragSelector({
                    dragSafe: true
                }),
                multiSelect: true
                ,style: 'overflow:auto'
	            ,simpleSelect: true
            });
            var images = new Ext.Panel({
                id: 'images',
                region: 'center',
                margins: '5 5 5 0',
                layout: 'fit',
                items: dataview
            });
            var panel = new Ext.Panel({
                layout: 'border',
                border: false,
                items: [new CountyResTree(this.prodId, areaId), images]
            });
        } else {
            var panel = new Ext.Panel({
                bodyStyle: Constant.SET_STYLE,
                layout: 'column',
                defaults: {
                    baseCls: 'x-plain',
                    layout: 'form'
                },
                items: [{
                    bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
                    html: "<font style='font-family:微软雅黑;font-size:20'>该服务类型还未配置静态资源</font>"
                }]
            });
        }
        return panel;
    },
    shortName: function (filename) {
        var short_filename;
        var num = ResThis.getChars(filename);
        var count = 30;
        if(num > count){
        	for(var i=0 ;i<num/count;i++){
        		if(i==0){
        			short_filename = ResThis.sb_substr(filename, i*count, count*(i+1))+"</br>";
        		}else{
        			short_filename = short_filename+ ResThis.sb_substr(filename, i*count, count*(i+1))+"</br>";
        		}
        	}
        }else{
        	short_filename = filename;
        }
        return short_filename;
    },
    sb_substr: function (str, startp, endp) {
        var i = 0,c = 0,unicode = 0,rstr = '';
        var len = str.length;
        var sblen = ResThis.getChars(str);
        if (startp < 0) {
            startp = sblen + startp;
        }
        if (endp < 1) {
            endp = sblen + endp; // - ((str.charCodeAt(len-1) < 127) ? 1 : 2);   
        }
        // 寻找起点   
        for (i = 0; i < len; i++) {
            if (c >= startp) {
                break;
            }
            var unicode = str.charCodeAt(i);
            if (unicode < 127) {
                c += 1;
            } else {
                c += 2;
            }
        }
        // 开始取   
        for (i = i; i < len; i++) {
            var unicode = str.charCodeAt(i);
            if (unicode < 127) {
                c += 1;
            } else {
                c += 2;
            }
            rstr += str.charAt(i);
            if (c >= endp) {
                break;
            }
        }
        return rstr;
    },
    getChars: function (str) {
        var i = 0,c = 0.0,unicode = 0,len = 0;
        if (str == null || str == "") {
            return 0;
        }
        len = str.length;
        for (i = 0; i < len; i++) {
            unicode = str.charCodeAt(i);
            if (unicode < 127) { //判断是单字符还是双字符   
                c += 1;
            } else { //chinese   
                c += 2;
            }
        }
        return c;
    }
});
ProdList = Ext.extend(Ext.grid.EditorGridPanel,{
	store:null,
	prodId:null,
	prodComb:null,
	userTypeComb:null,
	terminalComb:null,
	constructor:function(v){
		this.prodId = v;
		this.store = new Ext.data.JsonStore({
			url : root + "/system/Prod!querypackageByProdId.action",
			fields:['package_id','package_group_id','package_group_name','user_type','max_user_cnt','prod_list',
			'precent','user_type_text','terminal_type','terminal_type_text','prod_list_text']
		});
		
//		this.store.load();
		
		this.userTypeComb = new Ext.ux.ParamCombo({
			paramName:'USER_TYPE',
			isFilter:false,
			listWidth:200
		});
		this.terminalComb = new Ext.ux.ParamCombo({
			paramName:'TERMINAL_TYPE',
			isFilter:false,
			allowBlankItem:true,
			listWidth:200
		});
		this.prodComb = new Ext.ux.ParamLovCombo({
			paramName:'PROD_BASE',
			isFilter:false,
			listWidth:350
		});
		App.form.initComboData([this.userTypeComb,this.terminalComb,this.prodComb]);
		var paramComboRender = function(value){
			if(!Ext.isEmpty(value)){
				if(value.indexOf(',') == -1){
					var index = this.find('item_value',value);
					var record = this.getAt(index);
					if(!Ext.isEmpty(record)){
						return record.get('item_name');
					}
				}else{
					var countyIds = value.split(','), county_text = '';
					Ext.each(countyIds,function(c){
						var index = this.find('item_value',c);
						var record = this.getAt(index);
						county_text += record.get('item_name')+',';
					},this);
					
					county_text = county_text.substring(0,county_text.lastIndexOf(','));
					return '<div ext:qtitle="" ext:qtip="' + county_text + '">' + county_text +'</div>';
				}
			}
			return '';
		}
		var cm = new Ext.ux.grid.LockingColumnModel({ 
			columns : [
				{header:'套餐内容组ID',dataIndex:'package_group_id',hidden:true},
				{header:'套餐内容组名称',dataIndex:'package_group_name',width:200,editor:new Ext.form.TextField({})},
				{header:'产品内容组',dataIndex:'prod_list',width:250,editor :this.prodComb,
					renderer:paramComboRender.createDelegate(this.prodComb.getStore())
				},		
				{header:'用户类型',dataIndex:'user_type',width:100,
					editor :this.userTypeComb,
					renderer:paramComboRender.createDelegate(this.userTypeComb.getStore())
				},
				{header:'终端类型',dataIndex:'terminal_type',width:100,editor :this.terminalComb,
					renderer:paramComboRender.createDelegate(this.terminalComb.getStore())
				},
				{header:'用户数',dataIndex:'max_user_cnt',width:60,renderer:App.qtipValue,editor: new Ext.form.NumberField({
					allowDecimals:false,//不允许输入小数 
	    			allowNegative:false,
	    			minValue:0//enableKeyEvents: true,
				})},
				{header:'权重',dataIndex:'precent',width:80,renderer:App.qtipValue,editor: new Ext.form.NumberField({
					allowDecimals:false,//不允许输入小数 
	    			allowNegative:false,
	    			minValue:0//enableKeyEvents: true,
				})}
			]
        });
		cm.isCellEditable = this.cellEditable;
		ProdList.superclass.constructor.call(this,{
			title:'套餐内容',
			id: 'prodPkgForm',
		    region: 'center',
		    border: false,
		    store:this.store,
			cm:cm,
			clicksToEdit:1,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar : [{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        }]
		});	
		},
		cellEditable:function(colIndex,rowIndex){
			return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
		},
		initEvents : function(){
			ProdList.superclass.initEvents.call(this);
			this.on('afterrender',function(){
				this.swapViews();
			},this,{delay:10});
			
			this.on("afteredit",this.afterEdit,this);
			this.on("beforedit",this.beforeEdit,this);
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
		beforeEdit : function(obj){
			
		},
		afterEdit : function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			var value = obj.value;
//			if(fieldName == 'user_type_text'){
//				var cmp = this.userTypeComb;
//				var index = cmp.store.find('item_name',value);
//				var data = cmp.store.getAt(index);
//				record.set('user_type',data.get('item_value'));
//			}else if(fieldName == 'terminal_type_text'){
//				var cmp = this.terminalComb;
//				var index = cmp.store.find('item_name',value);
//				var data = cmp.store.getAt(index);
//				record.set('terminal_type',data.get('item_value'));
//			}
		},
		addRecord : function(){
			var count = this.getStore().getCount();
			var recordType = this.getStore().recordType;
			var record = new recordType({
				package_group_id:'',
				package_group_name : '',
				user_type : '',
				terminal_type : '',
				max_user_cnt : 1,
				prod_list: '',
				precent:0
			});

			this.stopEditing();
			this.getStore().add(record);
			this.startEditing(count,0);
			this.getSelectionModel().selectRow(count);
		},
		getValues:function(){
			this.stopEditing();
			var records = this.getStore().getModifiedRecords();
			if(records.length == 0){Alert('数据没有修改');flag = false;}
			var arr = []
			Ext.each(records,function(record){
				var values = {};
				values["package_group_name"] = record.get('package_group_name');
				values["user_type"] =record.get('user_type');
				values["prod_list"] = record.get('prod_list');
				values["max_user_cnt"] = record.get('max_user_cnt');
				values["precent"] = record.get('precent');
				values["package_group_id"] = record.get('package_group_id');
				values["terminal_type"] = record.get('terminal_type');
				arr.push(values);
			},this);
			return arr;
		}


})


/*// 套餐-子产品组件
ProdList = Ext.extend(Ext.Panel, {
    uprodGrid: null,
    upackGrid: null,
    uprodStroe: null,
    upackStore: null,
    setProd: null,
    setUprod: null,
    store: null,
    packTariffStore: null,
    oldData : null,//旧数据
    constructor: function (s) {
        this.store = s;
        this.uprodStroe = new Ext.data.JsonStore({
            fields: ['prod_id', 'prod_name', 'prod_desc','countyList','countyNameList']
        });
        this.uprodStroe.setDefaultSort("prod_name", "ASC");
        this.setProd = new Ext.grid.CheckboxSelectionModel();
        this.setUprod = new Ext.grid.CheckboxSelectionModel();
        this.upackStore = new Ext.data.JsonStore({
        	url: root + '/system/Prod!queryPackById.action',
            fields: ['prod_id', 'prod.prod_name', 'package_id', 'tariff_id', 'max_prod_count', 'tariff_name', 'prod.prod_desc', 'percent_value','countyList','countyNameList']
        });
        if(!Ext.isEmpty(Ext.getCmp('resForm').prodId)){
        	this.upackStore.baseParams.doneId = Ext.getCmp('resForm').prodId;
        }
        this.upackStore.load();
        this.uprodGrid = new Ext.grid.EditorGridPanel({
            title: '未分配产品',
            border: true,
            autoScroll: true,
            ds: this.uprodStroe,
            sm: this.setProd,
            region: 'center',
            columns: [this.setProd,
            {
                header: '产品名称',
                dataIndex: 'prod_name'
            }, {
                header: '产品描述',
                hidden : true,
                dataIndex: 'prod_desc',
                renderer: App.qtipValue
            }, {
                header: '适用地区',
                hidden : false,
                width : 200,
                dataIndex: 'countyNameList',
                renderer: App.qtipValue
            }],
            tbar: ['过滤:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            Ext.getCmp('prodPkgForm').doFilter(Ext.getCmp('ProdCountyTree').getCheckedIds(),value,'T');
                        }
                    }
                }
            },{xtype:'displayfield',value:"<font style='font-family:微软雅黑;font-size:14px;color:red'><b>" +
						"先选择适用地区!</b></font>"}]
        });

        this.upackGrid = new Ext.grid.EditorGridPanel({
            title: '已分配产品',
            region: 'center',
            ds: this.upackStore,
            sm: this.setUprod,
            height: 280,
            autoScroll: true,
            border: true,
            clicksToEdit: 1,
            columns: [this.setUprod,
            {
                header: '产品名称',
                dataIndex: 'prod.prod_name',
                sortable: true,
                width: 100,
                renderer: App.qtipValue
            }],
            tbar: ['过滤:',
            {
                xtype: 'textfield',
                enableKeyEvents: true,
                id: 'selectProdName',
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            this.upackStore.filterBy(function (record) {
                                if (Ext.isEmpty(value)) return true;
                                else return record.get('prod.prod_name').indexOf(value) >= 0;
                            }, this);
                        }
                    }
                }
            }]
        });
        ProdList.superclass.constructor.call(this, {
            id: 'prodPkgForm',
            region: 'center',
            layout: 'border',
            border: false,
            items: [this.uprodGrid,
            {
                region: 'east',
                border: false,
                layout: 'border',
                width: 450,
                items: [this.upackGrid,
                {
                    border: false,
                    bodyStyle: 'background-color: #DFE8F6;',
                    region: 'west',
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
                        tooltip: '将左边已勾选的产品分配给该套餐!',
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
                        tooltip: '将右边勾选中的套餐子产品取消!',
                        width: 40,
                        scale: 'large',
                        iconAlign: 'center',
                        scope: this,
                        handler: function () {
                            this.doProdOut(this.setUprod.getSelections());
                        }
                    }]
                }]
            }]
        });
    },
    doProdIn: function (arr) {
        if (arr.length == 0) {
            Alert("请在左边的列表中选择产品!");
            return;
        }
        var records = [];
        for (var i = 0; i < arr.length; i++) {
            var panl = this.upackStore.recordType;
            if(this.upackStore.query('prod_id',arr[i].data.prod_id).getCount() == 0){
            	var u = new panl({
	                prod_id: arr[i].data.prod_id,
	                "prod.prod_name": arr[i].data.prod_name,
	                tariff_id: '',
	                'tariff_name': '',
	                'prod.prod_desc': arr[i].data.prod_desc,
	                'countyList' : arr[i].data.countyList,
	                'countyNameList' : arr[i].data.countyNameList
	            });
	            this.upackGrid.stopEditing();
	            this.upackStore.insert(this.upackStore.getCount(), u);
	            this.upackGrid.startEditing(this.upackStore.getCount(), 0);
            }
            this.uprodStroe.remove(arr[i]);
        }
    },
    doProdOut: function (arr) {
        if (arr.length == 0) {
            Alert("请在右边的列表中选择产品!");
            return;
        }
        for (var i = 0; i < arr.length; i++) {
            var panl = this.uprodStroe.recordType;
            var u = new panl({
                prod_id: arr[i].data.prod_id,
                'prod_name': arr[i].data["prod.prod_name"],
                'prod_desc': arr[i].data["prod.prod_desc"],
                'countyList' : arr[i].data.countyList,
                'countyNameList' : arr[i].data.countyNameList
            });
            this.uprodGrid.stopEditing();
            this.uprodStroe.insert(this.uprodStroe.getCount(), u);
            this.uprodGrid.startEditing(this.uprodStroe.getCount(), 0);
            this.upackStore.remove(arr[i]);
        }
        Ext.getCmp('selectProdName').setValue("");
        this.upackStore.filterBy(function (record) {
            return true;
        });
    },
    showData: function (res) {
    	this.oldData = res;
        this.uprodStroe.loadData(res);
        if (this.store) {
            for (var i = this.uprodStroe.getCount() - 1; i >= 0; i--) {
                var ck = false;
                for (var j = 0; j < this.upackStore.getCount(); j++) {
                    if (this.store.prod_id == this.uprodStroe.getAt(i).get("prod_id") || 
                    this.uprodStroe.getAt(i).get("prod_id") == this.upackStore.getAt(j).get("prod_id")) {
                    	this.upackStore.getAt(j).set('countyList',this.uprodStroe.getAt(i).get('countyList'));
                    	this.upackStore.getAt(j).set('countyNameList',this.uprodStroe.getAt(i).get('countyNameList'));
                        ck = true;
                    }
                }
                if (ck) {
                    this.uprodStroe.removeAt(i);
                }
            }
        }
    },
    doFilter : function(prodCountyIds,value,pk){
		if(Ext.isEmpty(pk)){
			this.uprodStroe.removeAll();
	    	this.upackStore.removeAll();
			if(this.oldData){
				this.uprodStroe.loadData(this.oldData);
			}
		}
    	if(prodCountyIds.length == 0){
    		return;
    	}
        this.uprodStroe.filterBy(function(record){
    		var include = true;
    		for(var i=0;i<prodCountyIds.length;i++){
    			if(!record.get('countyList').contain(prodCountyIds[i])){
    				include = false;
    				break;    				
    			}
    		}
    		if (Ext.isEmpty(value)){ return include;}
            else{ return include==true&&record.get('prod_name').indexOf(value) >= 0;}
    	})
    }
});*/