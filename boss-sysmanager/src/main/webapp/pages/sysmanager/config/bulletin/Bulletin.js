/**
 * 动态资源grid
 */
var BulletinGrid = Ext.extend(Ext.grid.GridPanel, {
	bulletinStore : null,
	constructor : function() {
		bulletinThiz = this;
		this.bulletinStore = new Ext.data.JsonStore({
					url : root + '/config/Bulletin!queryBulletin.action',
					fields : Ext.data.Record.create([{name : 'bulletin_id'}, {name : 'bulletin_title'}, {name : 'bulletin_content'}, {name : 'bulletin_publisher'}, {name : 'status'}, {name : 'eff_date'},{name : 'exp_date'}, {name : 'status_text'}, {name : 'optr_id'}]),
					root : 'records',
					totalProperty : 'totalProperty',
					autoDestroy : true
				});
			this.bulletinStore.load({
					params : {
						start : 0,
						limit : Constant.DEFAULT_PAGE_SIZE
					}
				});				
		var sm = new Ext.grid.CheckboxSelectionModel({});
		var columns = [{
					header : '公告主题',
					dataIndex : 'bulletin_title',
					renderer : App.qtipValue
				}, {
					header : '发布人',
					dataIndex : 'bulletin_publisher'
				}, {
					header : '状态',
					dataIndex : 'status_text'
				}, {
					header : '生效时间',
					width:120,
					dataIndex : 'eff_date'
				}, {
					header : '失效时间',
					width:120,
					dataIndex : 'exp_date'
				}, {
					header : '操作',
					dataIndex : 'status',
					renderer : function(value, cellmeta, record, i) {
						var txt = null;
						if(value =='INVALID'){
							txt = "启用";
						}else{
							txt = "禁用";
						}
						return "<a href='#' style='color:blue' onclick='bulletinThiz.doModify();'>修改</a>&nbsp;&nbsp;<a href='#' style='color:blue' onclick='bulletinThiz.changeBulletin();'>"+txt+"</a>";
					}
				}];

		BulletinGrid.superclass.constructor.call(this, {
					id : 'bulletinId',
					region : 'center',
					store : this.bulletinStore,
					autoExpandColumn : 'bulletin_content_id',
					sm : sm,
					columns : columns,
					viewConfig: {
			            forceFit:true,
			            enableRowBody:true,
			            showPreview:true,
			            getRowClass : function(record, rowIndex, p, store){
			                if(this.showPreview){
			                    p.body = '<p style="font-size:14px;font-family:NSimSun;color:#337FE5;">'+record.data.bulletin_content+'</p>';
			                    return 'x-grid3-row-expanded';
			                }
			                return 'x-grid3-row-collapsed';
			            }
			        },
					bbar : new Ext.PagingToolbar({
						store : this.bulletinStore,
			            displayInfo: true,
			            displayMsg: '展示公告 {0} - {1} of {2}',
			            emptyMsg: "没有公告",
			            items:[
			                '-', {
			                pressed: true,
			                enableToggle:true,
			                text: '隐藏内容',
			                showContent:true,
			                cls: 'x-btn-text-icon details',
			                toggleHandler: function(btn, pressed){
			                	this.showContent = pressed;
			                	if(this.showContent){
			                		this.setText('隐藏内容');
			                	}else{
			                		this.setText('查看内容');
			                	}
								var view = bulletinThiz.getView();
			                    view.showPreview = pressed;
			                    view.refresh();
							}
			            }]
					}),
					tbar : [' ', ' ', '输入关键字', ' ',
							new Ext.ux.form.SearchField({
										store : this.bulletinStore,
										width : 200,
										hasSearch : true,
										emptyText : '支持主题/发布人/内容模糊查询'
									}), '->', {
								text : '添加',
								iconCls : 'icon-add',
								scope : this,
								handler : this.addRecord
							}]
				});
	},
	addRecord : function(s) { 
		var win = Ext.getCmp('bulletinWinId');
	 	if (!win) {
			win = new BulletinDetailWin(s);
		}
		win.show();
 	},
	doModify : function() {
		var grid = Ext.getCmp('bulletinId');
		var record = grid.getSelectionModel().getSelected();
		this.addRecord(record);
	},changeBulletin:function(){
		var grid = Ext.getCmp('bulletinId');
		var record = grid.getSelectionModel().getSelected();
		var bulletinId = record.get('bulletin_id');
		var status = record.get('status');
		var t ="";
		if(status == 'INVALID'){
			t = "确定要启用该公告吗?";
		}else{
			t = "确定要禁用该公告吗?";
		}
				Confirm(t, null, function() {
			Ext.Ajax.request({
				url : root + '/config/Bulletin!changeBulletin.action',
				params : {
					query : bulletinId,doneId:status
				},
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert('操作成功!', function() {
									Ext.getCmp('bulletinId').getStore().load({params : {start : 0,limit : Constant.DEFAULT_PAGE_SIZE}});
								});
					} else {
						Alert('操作失败');
					}
				}
			});
		})
	}
});

var BulletinForm = Ext.extend(Ext.form.FormPanel, {
	gridData : null,editorRendered:false,
	publisher:null,editor:false,
	constructor : function(s) {
		this.gridData = s;
		if(this.gridData.data==null){
			this.publisher=App.data.optr.optr_name;
		}
		this.htmlEditor = new Ext.form.TextArea({
			xtype : 'textarea',id:'bulletin_content_div_id',
			fieldLabel : '公告内容',
			name : 'bulletin_content',
			allowBlank : true,//留待提交时候校验
			width : 510,
			height : 300
		});
		BulletinForm.superclass.constructor.call(this, {
			id : 'bulletinFormId',
			height : 460,//layout : 'column',
			border : false,
			defaults : {
				labelAlign : "right",
				layout : 'form',
				border : false,
				columnWidth : 0.5,
				baseCls : "x-plain",
				labelWidth : 75
			},
			items : [{name:'bulletin_id',xtype:'hidden'},{
						bodyStyle : 'padding-top: 10px',
						items : [{
									fieldLabel : '主题名称',
									name : 'bulletin_title',
									xtype : 'textfield',
									allowBlank : false
								}]
					}, {
						items : [{
									fieldLabel : '发布人',
									id : 'bulletinPublisherId',
									name : 'bulletin_publisher',
									xtype : 'hidden',
									allowBlank : false
								}]
					}, {
						items : [{
							xtype : 'xdatetime',
							fieldLabel : '生效日期',
							width : 180,
							id : 'bulletinEffId',
							name:'eff_date',
							value:new Date(),
							vtype : 'daterange',
							endDateField : 'bulletinEndId',
							minText : '不能选择当日之前',
							timeWidth : 80,
							allowBlank : false,
							timeFormat : 'H:i:s',
							timeConfig : {
								increment : 5,
								editable:true,
								altFormats : 'H:i:s',
								allowBlank : false
							},
							dateFormat : 'Y-m-d',
							dateConfig : {
								altFormats : 'Y-m-d|Y-n-d',
								allowBlank : false
							}
						}]
					}, {
						items : [{
							xtype : 'xdatetime',
							fieldLabel : '失效日期',
							width : 180,
							id : 'bulletinEndId',
							name : 'exp_date',
							vtype : 'daterange',
							startDateField : 'bulletinEndId',
							timeWidth : 80,
							allowBlank : false,
							timeFormat : 'H:i:s',
							timeConfig : {
								increment : 5,
								editable:true,
								altFormats : 'H:i:s',
								allowBlank : false
							},
							dateFormat : 'Y-m-d',
							dateConfig : {
								altFormats : 'Y-m-d|Y-n-d',
								allowBlank : false
							}
						}]
					}, {
						columnWidth : 1,
						items : [this.htmlEditor]
					}]
		});
	},
	initComponent : function() {
		BulletinForm.superclass.initComponent.call(this);
		var form = Ext.getCmp('bulletinFormId').getForm();
		if(this.gridData.data!=null){
			var data = this.gridData.data;
			var prodInfo = {};
			for (var prop in data) {
				prodInfo[prop] = data[prop];
			}
			prodInfo['eff_date'] = Date.parseDate(prodInfo["eff_date"],'Y-m-d h:i:s');
			prodInfo['exp_date'] = Date.parseDate(prodInfo["exp_date"],'Y-m-d h:i:s');
			form.loadRecord(new Ext.data.Record(prodInfo));
		}
		if(!Ext.isEmpty(this.publisher)){
			Ext.getCmp('bulletinPublisherId').setValue(this.publisher);
		}
	}
});

BulletinCountyTree = Ext.extend(Ext.tree.TreePanel, {
     bulletinId:null,
    constructor: function (v) {
    	 this.bulletinId=v;
        var loader = new Ext.tree.TreeLoader({
            url: root + "/config/Bulletin!getDeptTree.action?query=" + this.bulletinId
//          url: root +"/system/Dept!queryDeptByCountyId.action"
        });
        BulletinCountyTree.superclass.constructor.call(this, {
            region: 'east',
            width: '40%',
            border : false,
            split: true,
            title: '应用地市',
            id: 'BulletinCountyTreeId',
            margins: '0 0 3 2',
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
                nodeType: 'async',
                text: '地市结构'
            }
        });
       this.expandAll();
    },
 	initEvents: function(){
		this.on("afterrender",function(){
			//如果不是修改
	        if(!this.bulletinId){
 	        		var node = this.getRootNode();
	        		node.attributes.checked = true;
	        		node.ui.toggleCheck(true);
	        		node.fireEvent('checkchange', node, true);
 	        }else{
 	        	
 	        }
		},this,{delay:1000});
		
		BulletinCountyTree.superclass.initEvents.call(this);
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
        },
        'load': function (node, checked) {
//            node.expand();
        }
     }
});

BulletinDetailWin = Ext.extend(Ext.Window, {
	form : null,
	countyTree: null,
	constructor : function(s) {
		this.form = new BulletinForm(s);
		var bulletinId='';
     		if(s){
				if(undefined!=s.data){
					var data = s.data;
					bulletinId = data['bulletin_id'];
				}
    		}
		this.countyTree = new BulletinCountyTree(bulletinId);
		BulletinDetailWin.superclass.constructor.call(this, {
			id : 'bulletinWinId',
			title : '公告配置',
			border : false,
			width : 900,
			height : 500,
			 layout : 'border',
			items : [{
					region : 'west',
					layout : 'fit',
					split : true,
					width : '70%',
					items : [this.form]
			},{
					region : 'center',
					layout : 'fit',
					items : [this.countyTree]
			}],
				
			buttonAlign : 'center',
			closeAction : 'close',
			buttons : [{
						id:'bulletin_save_btn',
						text : '保存',
						scope : this,
						handler : this.doSave
					}, {
						text : '关闭',
						scope : this,
						handler : function() {
							this.close();
						}
					}]
		});
	},
	doSave : function() {
		var form = this.form.getForm();
		if (!form.isValid())
			return;
		var values = form.getValues(),obj={};
		var html = this.form.editor.html();
		if(Ext.isEmpty(html.trim())){
			Alert('内容不能为空!');
			return ;
		}
		html = html.replaceAll('\'','\\\'' );
		values.bulletin_content = html;
		if(values['eff_date'] >= values['exp_date']){
			Alert('生效日期应小于失效日期!');
			return;
		}
		for (var v in values) {
			obj['bulletin.' + v] = values[v];
		}
		var bullCountyId = [];
		if (Ext.getCmp('BulletinCountyTreeId')) {
		     var nodes = Ext.getCmp('BulletinCountyTreeId').getChecked();
		     for (var i = 0;i<nodes.length;i++) {
		     	var id = nodes[i].id;
		     	if( id.indexOf('-1') <0 ){
			     	bullCountyId.push(id);
		     	}
		     }
		 }
		        
		obj["bullCountyIds"] = bullCountyId.join(",");
		var msg = Show();
		Ext.Ajax.request({
					url : root + '/config/Bulletin!saveBulletin.action',
					params : obj,
					scope : this,
					success : function(res, option) {
						msg.hide();
						msg = null;
						var data = Ext.decode(res.responseText);
						if (data.success === true) {
							Alert('保存成功', function() {
										Ext.getCmp('bulletinId').getStore().load({params : {start : 0,limit : Constant.DEFAULT_PAGE_SIZE}});
										this.close();
									}, this)
						}
					}
				});
	},
	show : function() {
		BulletinDetailWin.superclass.show.call(this);
		this.form.getForm().findField('bulletin_title').focus(true, 100);
		var dom = Ext.get('bulletin_content_div_id');
		if(this.form.editor){
			this.form.editor = null;
		}
		var plugins =[
		        'source', '|', 'undo', 'redo', '|', 'preview', 'template', 'code', 'cut', 'copy', 'paste',
		        '|', 'justifyleft', 'justifycenter', 'justifyright',
		        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
		        'superscript', 'clearhtml', 'quickformat', 'selectall', '|',
		        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',
		        'table', 'hr', 'pagebreak',
		        'anchor', 'link', 'unlink' 
		];
		this.form.editor = KindEditor.create('#bulletin_content_div_id', {
					autoHeightMode : true,
					height:320,width:520,
					items : plugins,
					afterCreate : function() {
						//TODO nothing
					}
				});
		//
	}
});

BulletinView = Ext.extend(Ext.Panel, {
			constructor : function() {
				bulletinGrid = new BulletinGrid();
				BulletinView.superclass.constructor.call(this, {
							id : 'BulletinView',
							title : '公告配置',
							closable : true,
							border : false,
							layout : 'border',
							baseCls : "x-plain",
							items : [bulletinGrid]
						});
			}
		});
