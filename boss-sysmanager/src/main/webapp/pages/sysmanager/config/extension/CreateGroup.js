/**
 * 暂时不用
 */

//Ext.ns('Extension');
///*
// * 扩展分组类型定义
// */
//GroupWindow = Ext.extend( Ext.Window ,{
//	isModefied : false,
//	groupStore : null,
//	extendType : null,
//	extendValue : null,
//	node : null,
//	constructor:function(extendType,extendValue,groupStore,node){
//		this.node = node;//点击节点
//		this.groupStore = groupStore;
//		this.extendType = extendType;
//		this.extendValue = extendValue;
//		var sm = new Ext.grid.CheckboxSelectionModel({
//			singleSelect : true
//		});
//		var cm = [sm,{
//			header : '分组ID',
//			dataIndex : 'group_id',
//            editor: new Ext.form.TextField({
//                allowBlank: false
//            })
//		},{
//			header : '分组显示名',
//			dataIndex : 'group_name',
//            editor: new Ext.form.TextField({
//                allowBlank: false
//            })
//		},{
//            header: '操作', 
//            width : 60,
//            dataIndex: 'attribute_id',
//            renderer:function(){
//				return "<a href='#' onclick=Ext.getCmp(\'"+"groupwindow"+"\').deleteGroupRow(); style='color:blue'> 删除 </a>";
//			}
//		}];
//		
//		
//		GroupWindow.superclass.constructor.call(this,{
//			id : 'groupwindow',
//			title : '新建分组',
//			height : 400,
//			width : 300,
//			closeAction: 'close',
//			modal : true,
//			layout : 'fit',
//			items : [{
//				xtype : 'editorgrid',
//				id : 'groupeditorgrid',
//				sm : sm,
//				store : this.groupStore,
//				clicksToEdit:1,
//				columns:cm,
//				viewConfig:{
//		        	forceFit : true
//		        },
//				tbar : [{
//	            	text:'增加分组',
//	            	iconCls : 'icon-add',
//	            	scope : this,
//	            	handler : this.addGroupRec
//	            }]
//			}],
//			buttons : [{
//				text : '保存',
//				scope : this,
//				iconCls : 'icon-save',
//				handler : this.doSave
//			}, {
//				text : '关闭',
//				scope : this,
//				handler : function() {
//					this.close();
//				}
//			}]
//		})
//	},
//	addGroupRec : function(){
//		var Plant = Ext.getCmp('groupeditorgrid').getStore().recordType;
//		var p = new Plant({
//            group_id: '',
//            group_name: ''
//        });
//        Ext.getCmp('groupeditorgrid').stopEditing();
//        this.groupStore.insert(this.groupStore.getCount(), p);
//        Ext.getCmp('groupeditorgrid').startEditing(this.groupStore.getCount()-1, 0);
//	},
//	deleteGroupRow : function(){
//		Confirm("确定要删除该数据吗?", this ,function(){
//			var record = Ext.getCmp('groupeditorgrid').getSelectionModel().getSelected();
//			Ext.getCmp('groupeditorgrid').getStore().remove(record);
//			this.isModefied = true;
//		});
//	},
//	doSave : function(){
//		var records = [];
//       	for(var i=0;i<this.groupStore.getCount();i++){
//       		var data = this.groupStore.getAt(i).data;
//       		if(data.group_id==null||data.group_id.trim().length==0
//       		|| data.group_name==null || data.group_name.trim().length==0){
//       			Alert("不能有数据为空");
//				return;
//       		}
//       		records.push(data);
//       	}
//       	if(records.length == 0 && this.isModefied == false){
//       		Alert("没有数据，不能保存");
//       		return;
//       	}
//       	Confirm("确定保存吗?", this ,function(){
//       		mb = Show();//显示正在提交
//	       	//保存数据
//			Ext.Ajax.request({
//				scope : this,
//				url  : root + '/config/ExtendTable!saveExtendGroups.action' ,
//				params:{
//					extendValue : this.extendValue,
//					extendType : this.extendType,
//					groups : Ext.encode(records)
//					},
//				success:function( res,ops ){
//					mb.hide();//隐藏提示框
//					mb = null;
//					var rs = Ext.decode(res.responseText);
//					if(true === rs.success){
//						Alert('操作成功!');
//						this.node.removeAll();
//						
//						if(rs.records.length != 0){
//							this.node.leaf = false;
//							this.node.attributes.leaf = false;
//							for(var i=0;i<rs.records.length;i++){
//								this.node.appendChild(new Ext.tree.TreeNode({
//									id : rs.records[i].extend_id+"_"+rs.records[i].group_id,
//									text : rs.records[i].group_name,
//									others : {
//										attr : 'T'
//									}
//								}));
//							}
//						}else{
//							this.node.leaf = true;
//							this.node.attributes.leaf = true;
//						}
//						this.groupStore.loadData(rs.records);
//						this.close();
//					}else{
//						Alert('操作失败');
//			 		}
//				},
//				failure:function(){
//					Alert('错误','请与后台服务人员联系');
//				}
//			});
//       	});
//	}
//});