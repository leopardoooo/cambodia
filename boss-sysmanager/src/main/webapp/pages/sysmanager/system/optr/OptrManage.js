/**
 * Ext 构建操作员管理页面
 */
var OptrManageForm = Ext.extend(Ext.form.FormPanel, {
	storeData:null,
	passname:false,
	passtext:null,
	deptId:null,
	deptName:null,
	countyId:null,
	loginSysId:null,
	type: null,
	constructor : function(v,type) {
		if(!Ext.isEmpty(v)){
			this.storeData = v.data;
		}
		if(!Ext.isEmpty(type)){
			this.type = type;
		}
		if(v){
			this.passtext = '        ';
			this.passname = true;
			this.deptId = v.get('dept_id');
			this.deptName = v.get('dept_name');
			this.countyId = v.get('county_id');
		}else{		
			if (Ext.getCmp('navigationDept')) {
				this.deptId = Ext.getCmp('navigationDept').deptId;
				this.deptName = Ext.getCmp('navigationDept').deptName;
				this.countyId = Ext.getCmp('navigationDept').countyId;
			}
		}
		OptrManageForm.superclass.constructor.call(this, {
			id : 'optrManageFormId',
			region : 'south',
			split : true,
			height : 220,
			layout : 'column',
			border:false,
			bodyStyle : 'padding: 5px',
			defaults : {
				columnWidth : .5,
				layout : 'form',
				baseCls : 'x-plain',
				defaultType : 'textfield'
			},
			items : [{xtype : 'hidden',name : 'optr_id',id : 'optr_id'
					},{xtype:'hidden',name:'dept_id',value:this.deptId},
						{
						labelWidth : 90,
						items : [{
									fieldLabel : '操作员姓名',
									name : 'optr_name',
									allowBlank : false,
									maxLength : 24
								}, {
									fieldLabel : '密码',
									inputType : 'password',
									name : 'password',
									emptyText: this.passtext,
									id : 'password',
									allowBlank:this.passname,
									listeners :{
										scope : this,
										change:function(txt){
											if(!Ext.isEmpty(txt.getValue())){
												Ext.getCmp('confirmPwd').allowBlank = false;											 
											}else{
												Ext.getCmp('confirmPwd').allowBlank = true;	
											}
										}
									
									}
								}, {
									fieldLabel : '确认密码',
									inputType : 'password',
									name : 'confirmPwd',
									emptyText: this.passtext,
									allowBlank:this.passname,
									id : 'confirmPwd',
									vtype : 'password',
									initialPassField : 'password'
								},{
									fieldLabel : '职位',
									name : 'position'
								},{
									fieldLabel : '电话',
									name : 'tel',
									xtype: 'textfield',
									vtype: 'numZero' 
								}/*,
								{
						            fieldLabel: '业务员',
						            hiddenName: 'is_busi_optr',
						            allowBlank: false,
						            xtype: 'paramcombo',
						            paramName: 'BOOLEAN',
						            defaultValue: 'F',
						            disabled : false,
						            width: 120
						        }*/]
					}, {
						labelWidth : 90,
						items : [{
									fieldLabel : '操作员工号',
									name : 'login_name',
									allowBlank : false,
									maxLength : 32,
									id:'loginName',
									xtype : 'textfield',
									listeners:{
										scope:this,
										'change':this.chickLoginName
									}
								}, {
									id:'subSystemSelectId',fieldLabel:'默认登录系统',hiddenName:'login_sys_id',
									xtype:'combo',displayField:'sub_system_text',valueField:'login_sys_id',
									store:new Ext.data.JsonStore({fields:['login_sys_id','sub_system_text']}),
									listeners:{
											scope:this,
											focus:function(combo,record){
												Ext.getCmp('subSystemSelectId').getStore().removeAll();
												var comp = Ext.getCmp('cfgRoleGridId');
												if(comp){
													comp.stopEditing();
													var rolestore = comp.getStore();											
													var recordType = rolestore.recordType;
													var key = false;
													rolestore.each(function(record){	
														if(!Ext.isEmpty(record.get('sub_system_id'))){
															key = true;
															var record = new recordType({
																login_sys_id:record.get('sub_system_id'),sub_system_text:record.get('sub_system_text')
															});
															Ext.getCmp('subSystemSelectId').getStore().add(record);
														}
													});
												}
//												if(!key){
//													Alert("请在下方先配置菜单角色,才可以配置本项!");
//												}
											},
											select:function(combo){
												this.loginSysId = combo.getValue();
											}
									}
								},
								{
									fieldLabel : '部门',
									id : 'deptId',
									name : 'dept_name',
									disabled:true,
									value:this.deptName
								},
								{
									xtype:'combo',
									fieldLabel : '复制操作员',
									hiddenName: 'copy_optr_id',
									store:new Ext.data.JsonStore({
										url:root+'/system/Optr!queryOptrByCountyId.action',
										fields:['optr_id','optr_name'],
										listeners:{
											scope:this,
											load:function(){
												if(this.storeData){
													var comp = this.getForm().findField('copy_optr_id');
													comp.setValue(this.storeData['copy_optr_id']);
												}
											}
										}
									}),
									displayField:'optr_name',valueField:'optr_id',value:'',
									forceSelection:true,editable:true,allowBlank:false,
									disabled:true
								},{
									fieldLabel : '电话2',
									name : 'mobile',
									xtype: 'textfield',
									vtype: 'numZero' 									
								}
								]
					},{
						labelWidth : 90,
						columnWidth : 1,
						items:[{
								fieldLabel : '备注',
								name : 'remark',
								height : 40,
								width : 350,
								id:'remark',
								xtype : 'textarea'
							}]
					}]
		})
	},
	initComponent : function(){
		OptrManageForm.superclass.initComponent.call(this);
		// 初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes,this.doInit, this);
		this.doInit();
	},
	doInit : function(){
		if(this.storeData != null){
			var record = new Ext.data.Record(this.storeData);
			this.getForm().loadRecord(record);
			this.loginSysId = record.get('login_sys_id');
			Ext.getCmp('subSystemSelectId').setValue(record.get('sub_system_text'));
		}
		if(this.type == 'update'){
			this.getForm().findField('copy_optr_id').setValue('');
			Ext.getCmp('loginName').setDisabled(true);
		}
	},
	chickLoginName:function(field){
		var value = field.getValue();
		Ext.Ajax.request({
				params : {query:value},
				url: root + '/system/Optr!chickLoginName.action',
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					if (false === rs.success) {
						Alert('工号已经存在!', function() {
							Ext.getCmp('loginName').setValue('');
							Ext.getCmp('loginName').focus();
										}, this);
					} 
				}
			});
	},chickTel:function(field){
		var value = field.getValue();
		var reg_CN = /(^(\d{3,4}-)?\d{7,8})$|(13[0-9]\d{8}|15[0-9]\d{8})|(((\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
		if(!reg_CN.test(value)){
			Alert("电话号码错误,格式为:11位手机号码(13++,15++)；(3-4位区号可省)-(7-8位直播号码)-(1-4位分机号可省)。");
		}
	}
});

var CopyOptrWind = Ext.extend(Ext.Window, {
	optrManageForm: null,
	constructor: function(v,type){
		this.optrManageForm = new OptrManageForm(v,type);
		CopyOptrWind.superclass.constructor.call(this,{
			id:'copyOptrWindId',
			width:600,
			height:250,
			layout:'fit',
			closeAction:'close',
			border:false,
			items:[this.optrManageForm],
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
		});
		var comp = this.optrManageForm.getForm().findField('copy_optr_id');
		comp.setDisabled(false);
		comp.getStore().load({
			params:{
				countyId : this.optrManageForm.countyId
			}
		});
	},
	doSave: function(){
		if(this.optrManageForm.getForm().isValid()){
			var values = this.optrManageForm.getForm().getValues();
			var newValues = {};
			for (var key in values) {
				newValues["newoptr." + key] = values[key];
			}
//			var msg = Show();
			Ext.Ajax.request({
				url: root + '/system/Optr!copyOptr.action',
				params:newValues,
				scope:this,
				success:function(res){
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert("操作成功!", function() {
							var deptId = Ext.getCmp('optrManageFormId').deptId;
									Ext.getCmp('optrManagePanelId').getStore().load({
												params : {pid: deptId,start : 0,limit : Constant.DEFAULT_PAGE_SIZE}
											});
									this.close();
								},this);
					} else {
						Alert("操作失败!");
					}
				}
			});
		}
	}
});

cfgRoleType = {
	"DATA" : "DATA",
	"MENU" : "MENU"
};
CfgRoleGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	optrRoleStore:null,
	optrId:null,
	constructor:function(v){
		thatTothis = this;
		if(!Ext.isEmpty(v)){
			this.optrId = v.get('optr_id');
		}
		this.optrRoleStore = new Ext.data.JsonStore({
			url: root + '/system/Optr!queryOptrRole.action',
			fields:['role_type','sub_system_id','data_right_type','role_id','data_right_type_text','sub_system_text','role_name','role_type_text','key']
		});
		
		this.roleTypeCombo = new Ext.ux.ParamCombo({
			typeAhead:false,paramName:'ROLE_TYPE',valueField:'item_name',
			forceSelection:true,selectOnFocus:true,editable:true,
			listeners:{
				scope:this,
				select:function(combo,r){				
					var record = this.getSelectionModel().getSelected();
					record.set('sub_system_text','');
					record.set('sub_system_id','');
					record.set('data_right_type_text','');
					record.set('data_right_type','');
					record.set('role_name','');
					record.set('role_id','');
					record.set('role_type',r.get('item_value'));
				}
			}
		});
		this.subSystemCombo = new Ext.ux.ParamCombo({
			typeAhead:false,paramName:'SUB_SYSTEM',valueField:'item_name',id:'subSystemCombo',
			forceSelection:true,selectOnFocus:true,editable:true,
			listeners:{
				scope:this,
				select:function(combo,r){
					if(combo.startValue == Ext.getCmp('subSystemSelectId').lastSelectionText){
						Ext.getCmp('subSystemSelectId').setValue('');
					}
					//当设备类型改变时，清空"型号"列
					var record = this.getSelectionModel().getSelected();
					record.set('role_name','');
					record.set('role_id','');
					record.set('sub_system_id',r.get('item_value'));
				}
			}
		});
		
		this.dataTypeCombo = new Ext.ux.ParamCombo({
			typeAhead:false,paramName:'DATA_TYPE',valueField:'item_name',
			forceSelection:true,selectOnFocus:true,editable:true,
			listeners:{
				scope:this,
				select:function(combo,r){
					var record = this.getSelectionModel().getSelected();
					record.set('data_right_type',r.get('item_value'));
					record.set('role_name','');
					record.set('role_id','');
				}
			}});
			
			this.roleCombo = new Ext.form.ComboBox({
			store:new Ext.data.JsonStore({
				url:root+ '/system/Role!queryRoleByObj.action',
				fields:['role_id','role_name']
			}),displayField:'role_name',valueField:'role_name',
			forceSelection:true,selectOnFocus:true,editable:true
			});
			
		var cm = new Ext.grid.ColumnModel([
			{header:'角色类型',dataIndex:'role_type_text',width:100,editor:this.roleTypeCombo},
			{id:'subSystemId',header:'子系统',dataIndex:'sub_system_text',width:120,editor:this.subSystemCombo},
			{id:'dataRightType',  header:'数据类型',dataIndex:'data_right_type_text',width:120,editor:this.dataTypeCombo},
			{id:'roleId',header:'角色',dataIndex:'role_name',width:120,editor:this.roleCombo},
			{header : '操作',width : 100,
					renderer : function(v, md, record, i) {
						var rs = Ext.encode(record.data);
						return String.format("&nbsp;&nbsp;<a href='#' onclick='thatTothis.deleteRecord({0},{1});' style='color:blue'> 删除 </a>",rs, i);
					}
			}
		]);
		
		cm.isCellEditable = this.cellEditable;
		
		CfgRoleGrid.superclass.constructor.call(this,{
			id:'cfgRoleGridId',
			region:'center',
			title:'角色配置',
			border:false,
			ds:this.optrRoleStore,
			enableColumnMove :false,
			clicksToEdit:1,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:[
				 '-',{text:'添加',handler:this.doAdd,scope:this}, '-',
				{text:'刷新',handler:this.comomRefresh,scope:this} ,'-'
			]
		});
	},
	cellEditable:function(colIndex, rowIndex){
		var subSystemIdIndex = this.getIndexById('subSystemId');
		var dataRightTypeIndex = this.getIndexById('dataRightType');
		var roleIdIndex = this.getIndexById('roleId');
		var count = thatTothis.getStore().getCount();
		if(count!=rowIndex){
			var roleType = thatTothis.getStore().getAt(rowIndex).get('role_type');
			var subSystemId = thatTothis.getStore().getAt(rowIndex).get('sub_system_id');
			var dataRightType = thatTothis.getStore().getAt(rowIndex).get('data_right_type');
			if(colIndex === subSystemIdIndex){
				//"设备类型"列为空时，不能选择"型号"列
				if(Ext.isEmpty(roleType)){
					return false;
				}else{
					if(roleType == cfgRoleType["DATA"]){
						return false;
					}
				}
			}
			if(colIndex === dataRightTypeIndex){
				//"设备类型"列为空时，不能选择"型号"列
				if(Ext.isEmpty(roleType)){
					return false;
				}else{
					if(roleType == cfgRoleType["MENU"]){
						return false;
					}
				}
			}
			if(colIndex === roleIdIndex){
				//"设备类型"列为空时，不能选择"型号"列
				if(Ext.isEmpty(roleType)||(Ext.isEmpty(subSystemId)&&Ext.isEmpty(dataRightType))){
					return false;
				}
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	}
	,
	initComponent:function(){
		CfgRoleGrid.superclass.initComponent.call(this);
		App.form.initComboData([this.roleTypeCombo,this.subSystemCombo,this.dataTypeCombo],this.loadData,this);
	},
	initEvents:function(){
		CfgRoleGrid.superclass.initEvents.call(this);
		this.on('beforeedit',this.beforeEdit,this);
		this.on('afteredit',this.afterEdit,this);
	},beforeEdit:function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			if(fieldName == 'role_name'){
				var all  = {};
				if(!Ext.isEmpty(record.get('data_right_type'))){
					all["role.data_right_type"]=record.get('data_right_type');
				}
				if(!Ext.isEmpty(record.get('role_type'))){
					all["role.role_type"]=record.get('role_type');
				}
				if(!Ext.isEmpty(record.get('sub_system_id'))){
					all["role.sub_system_id"]=record.get('sub_system_id');
				}
				this.roleCombo.getStore().load({params:all});
			}
	},afterEdit:function(obj){
		if(obj.field == 'role_name'){
			var index = this.roleCombo.getStore().findExact('role_name',obj.value);
			obj.record.data.role_id = this.roleCombo.getStore().getAt(index).get('role_id');
		}
	
	},
	
	doAdd:function(){
		this.commonDoAdd(this,{
			role_name:'',role_id:'',data_right_type:'',data_right_type_text:'',
			role_type:'MENU',role_type_text:'菜单',sub_system_id:'',sub_system_text:'',ck:'ok'
		});
	},comomRefresh:function(){
		this.getStore().removeAll();
		if(!Ext.isEmpty(this.optrId)){
			this.optrRoleStore.baseParams.query = this.optrId ;
			this.optrRoleStore.load();
		}
	}, commonDoAdd : function(scopeThis,fieldsObj,editColumn){
		editColumn = editColumn || 0;
		var store = scopeThis.getStore();
		var count = store.getCount();
		var recordType = store.recordType;
		var recordData = new recordType(fieldsObj);
		scopeThis.stopEditing();
		store.insert(0,recordData);
		scopeThis.startEditing(0,editColumn);
		scopeThis.getSelectionModel().selectRow(0);
	},
	CheckOptrRole : function() {
		thatTothis.stopEditing();// 停止编辑
		var store = thatTothis.getStore();
		var count = store.getCount();// 总个数

		var config = thatTothis.getColumnModel().config;

		var dataIndexes = [];
		for (var i = 0; i < config.length; i++) {
			dataIndexes.push(config[i].dataIndex);
		}
		var flag = true;
		for (var i = 0; i < count; i++) {
			var data = store.getAt(i).data;
			for (var k = 0; k < dataIndexes.length; k++) {
				var a = dataIndexes[k];
				if (Ext.isEmpty(data[a])) {
					if(a == 'role_type_text'||a == 'role_name'){
						flag = false;
					}
					if(data["role_type"] == cfgRoleType["MENU"]&& a == 'sub_system_text'){
						flag = false;
					}
					if(data["role_type"] == cfgRoleType["DATA"]&& a == 'data_right_type_text'){
						flag = false;
					}
					if (!flag){
						Alert('请编辑完整!', function() {
								thatTothis.getSelectionModel().selectRow(i);
								thatTothis.startEditing(i, k);
							});
						break;	
					}
				}
			}
			if (!flag)break;
			if(count>1){	
				for(var j=i+1;j<count;j++){
					var d = store.getAt(j).data;
					if(data["role_id"] == d["role_id"]){
						flag = false;
						Alert("第"+(i+1)+"行和第"+(j+1)+"行相同,请重新编辑！",function(){
							thatTothis.getSelectionModel().selectRow(i);
							thatTothis.startEditing(i,3);
						});
						break;
					}
					
					if(!Ext.isEmpty(data["sub_system_id"])&&data["sub_system_id"] == d["sub_system_id"]){
						flag = false;
						Alert("第"+(i+1)+"行和第"+(j+1)+"行,菜单角色对应的子系统不能重复!",function(){
							thatTothis.getSelectionModel().selectRow(i);
							thatTothis.startEditing(i,1);
						});
						break;
					}
					if(!Ext.isEmpty(data["data_right_type"])&&data["data_right_type"] == d["data_right_type"]){
						flag = false;
						Alert("第"+(i+1)+"行和第"+(j+1)+"行,数据角色对应的数据类型不能重复!",function(){
							thatTothis.getSelectionModel().selectRow(i);
							thatTothis.startEditing(i,2);
						});
						break;
					}
				}	
			}
			if(!flag)break;	
		}
		return flag;
	},loadData:function(){
		if(!Ext.isEmpty(this.optrId)){
			this.optrRoleStore.baseParams.query = this.optrId ;
			this.optrRoleStore.load();
		}
	},deleteRecord : function(v, index) {
		var rec = this.getSelectionModel().getSelected();

		var optrId=this.optrId;
		if (!Ext.isEmpty(v.ck)){
			this.optrRoleStore.remove(rec);
			if(Ext.getCmp('subSystemSelectId').getValue()==rec.get('sub_system_id')||Ext.getCmp('subSystemSelectId').getValue() == rec.get('sub_system_text')){
				Ext.getCmp('subSystemSelectId').setValue('');
			}			
		} else {
			Confirm("确定要删除该数据吗?", null, function() {
				Ext.Ajax.request({
					url : root + '/system/Optr!deteleOptrRole.action',
					params : {doneId : v.role_id,optr_id :optrId},
					success : function(res, ops) {
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							Alert('删除成功!', function() {
									thatTothis.optrRoleStore.remove(rec);
									if(Ext.getCmp('subSystemSelectId').getValue()==rec.get('sub_system_id')||Ext.getCmp('subSystemSelectId').getValue() == rec.get('sub_system_text')){
										Ext.getCmp('subSystemSelectId').setValue('');
									}
							}, thatTothis);
						} else {
							Alert('删除失败!');
						}
					}
				});
			})
	}

}
});



OptrWindow = Ext.extend(Ext.Window, {
	optrManageForm : null,
	cfgRoleGrid : null,
	constructor : function(v,type) {
		this.type = type;
		this.optrManageForm = new OptrManageForm(v,type);
		this.cfgRoleGrid = new CfgRoleGrid(v);
		OptrWindow.superclass.constructor.call(this, {
					width : 600,
					height : 500,
					layout : 'border',
					id:'optrwindow',
					closeAction : 'close',
					defaults : {
						layout : 'fit',
						bodyStyle : "background:#F9F9F9"
					},
					items : [{
								region : 'north',
								title : '基本信息【默认登录系统与下面角色配置的子系统关联】',
								collapsible : true,
								height : 210,
								split : true,
								border:false,
								items : [this.optrManageForm]
							}, {
								region : 'center',
								spilt : true,
								border:false,
								items : [this.cfgRoleGrid]
							}],
					buttons : [{
								text : '保存',
								scope : this,
								iconCls : 'icon-save',
								handler : this.doSave
							}, {
								text : '关闭',
								scope : this,
								iconCls : 'icon-close',
								handler : function() {
									this.close();
								}
							}]
				});
	},
	doSave : function() {
		if(!this.cfgRoleGrid.CheckOptrRole()){
			return;
		}
		if (!this.optrManageForm.getForm().isValid()) {
			return;
		}
		var old = this.optrManageForm.getForm().getValues(), newValues = {};
		var store = this.cfgRoleGrid.getStore();
		var data = [];
		var isHaveSys = false;
		store.each(function(record){
			var obj = {};
			obj["role_id"] = record.get('role_id');
			obj["role_name"] =record.get('role_name');
			data.push(obj);
		})
		
		
		newValues['optrRoleList'] = Ext.encode(data);
		for (var key in old) {
			newValues["newoptr." + key] = old[key];
		}
		
		if(Ext.isEmpty(newValues["newoptr.login_sys_id"])){
			newValues["newoptr.login_sys_id"] = '1';
		}else{
			newValues["newoptr.login_sys_id"] = this.optrManageForm.loginSysId;
		}
		
		newValues["newoptr.dept_id"] = Ext.getCmp('optrManageFormId').deptId;
		if(this.type =='update'){
			newValues["newoptr.login_name"] = Ext.getCmp('loginName').getValue();
		}
		Confirm("确定要保存吗?", this, function() {
			Ext.Ajax.request({
				params : newValues,
				url: root + '/system/Optr!save.action',
				scope:this,
				success : function(res, ops) {
					var rs = Ext.decode(res.responseText);
					if (true === rs.success) {
						Alert("操作成功!", function() {
							var deptId = Ext.getCmp('optrManageFormId').deptId;
									Ext.getCmp('optrManagePanelId').getStore().load({
												params : {pid: deptId,start : 0,limit : Constant.DEFAULT_PAGE_SIZE}
											});
									this.close();
								},this);
					} else {
						Alert("操作失败!");
					}
				}
			});
		});
	}
});

ResourceToOptrTree = Ext.extend(Ext.ux.FilterTreePanel, {
    prodId: null,
    constructor: function (v) {
        var loader = new Ext.tree.TreeLoader({
        	url: root + "/system/Optr!ResourceToOptrTree.action",
        	baseParams: {
       			optr_id : v.get('optr_id')
        	}
        });
        ResourceToOptrTree.superclass.constructor.call(this, {
            split: true,
            id: 'ResourceToOptrTreeId',
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            rootVisible : false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: new Ext.tree.AsyncTreeNode()
        });
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
    },openNext:function(){
		var childarr = this.getRootNode().childNodes;
		 if (childarr.length > 0) {
			for (var i = 0; i < childarr.length; i++) {
				if (childarr[i].loaded == false) {
                    childarr[i].expand();
                    childarr[i].collapse();
                }
			}
		}
	},initComponent : function() {
		ResourceToOptrTree.superclass.initComponent.call(this);
		this.getRootNode().expand();
		this.getRootNode().on('expand', function() {
			this.openNext();
		},this);
	}
});

ResourceToOptrWindow = Ext.extend(Ext.Window, {
	resourceToOptrTree : null,
	optrId:null,
	constructor : function(v) {
		this.optrId = v.get('optr_id');
		this.resourceToOptrTree = new ResourceToOptrTree(v);
		ResourceToOptrWindow.superclass.constructor.call(this, {
			layout : 'fit',
			width : 400,
			height : 500,
			closeAction : 'hide',
			items : this.resourceToOptrTree,
			buttons : [{
				text : '保存',
				scope : this,
				handler :this.saveOptr
			},{
				text : '关闭',
				scope:this,
				handler:function(){
					this.hide();
				}
			}]
		});
	},
	saveOptr:function(){
		var resourceIds = [],all={};
        if (Ext.getCmp('ResourceToOptrTreeId')) {
            var nodes = Ext.getCmp('ResourceToOptrTreeId').getChecked();
            for (var i in nodes) {
                if (nodes[i].leaf) {
                    resourceIds.push(nodes[i].id);
                }
            }
        }
        if (resourceIds.length > 0) {
            all["resourceIds"] = resourceIds;
        }
        all["optr_id"]=this.optrId;
		Ext.Ajax.request({
			url : root + '/system/Optr!saveResourceToOptrs.action',
			params : all,
			scope:this,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				if (true === rs.success) {
					Alert('操作成功!', function() {this.close();}, this);
				} else {Alert('操作失败!');}
			}
		});
	}
});
		
var OptrDeptWind = Ext.extend(Ext.Window, {
	deptId:null,
	constructor: function(record){
		this.deptId = record.get('dept_id');
		OptrDeptWind.superclass.constructor.call(this,{
			title:'更换部门',
			layout : 'form',
			width : 300,
			height : 200,
			bodyStyle:'padding-top:15px;background:#F9F9F9',
			labelAlign:'right',
			items : [
				{xtype:'displayfield',fieldLabel:'姓名',value:record.get('optr_name')},
				{xtype:'displayfield',fieldLabel:'原部门',value:record.get('dept_name')},
				{xtype:'combo',fieldLabel:'新部门',hiddenName:'dept_id',
					store: new Ext.data.JsonStore({
						url:root+'/system/Optr!queryDeptByCountyId.action',
						fields:['dept_id','dept_name']
					}),
					displayField:'dept_name',valueField:'dept_id',
					forceSelection:true,editable:true,allowBlank:false
				}
			],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls:'icon-save',
				handler :this.doSave
			},{
				text : '关闭',
				scope:this,
				iconCls:'icon-close',
				handler:function(){
					this.hide();
				}
			}]
		});
		this.find('hiddenName','dept_id')[0].getStore().load({
			params:{
				countyId: record.get('county_id')
			}
		});
	},
	doSave: function(){
		var newDeptId = this.find('hiddenName','dept_id')[0].getValue();
		if(!Ext.isEmpty(newDeptId)){
			var optrId = Ext.getCmp('optrManagePanelId').getSelectionModel().getSelected().get('optr_id');
			Ext.Ajax.request({
					url: root + '/system/Optr!changeDept.action',
					params : {
						optrId: optrId,
						deptId: newDeptId
					},
					scope:this,
					success : function(res, ops) {
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							Alert("操作成功!", function() {
								var deptId = this.deptId;
										Ext.getCmp('optrManagePanelId').getStore().load({
													params : {pid: deptId,start : 0,limit : Constant.DEFAULT_PAGE_SIZE}
												});
										this.close();
									},this);
						} else {
							Alert("操作失败!");
						}
					}
				});
		}
	}
});		

/**
 * 封装结果集面板
 */
var OptrManagePanel = Ext.extend( Ext.grid.GridPanel , {
	store: null ,
	constructor: function( cfg ){
		Ext.apply( this , cfg || {});
		optrManagePanel =this;
		// create the data store
		this.store = new Ext.data.JsonStore({
			url  : root + '/system/Optr!queryOptrs.action' ,
			root : 'records' ,
			totalProperty: 'totalProperty',
			autoDestroy: true,
			listeners :{
				beforeload :{
					fn : function(thiz,options){
						thiz.baseParams["pid"]= Ext.getCmp('navigationDept').deptId; 
					}
				}
			},
			fields : Ext.data.Record.create([
				{name: 'optr_id'},
				{name: 'optr_name'},
				{name: 'login_name'},
				{name: 'dept_id'},
				{name: 'status'},
				{name: 'area_id'},
				{name: 'county_id'},
				{name: 'create_time'},
				{name: 'creator_id'},
				{name: 'login_sys_id'},
				{name: 'dept_name'},
				{name: 'county_name'},
				{name: 'area_name'},
				{name: 'status_text'},
				{name: 'creator_name'},
				{name: 'report_data_level'},
				{name: 'tel'},{name:'mobile'},
				{name: 'sub_system_text'},
				{name: 'position'},
				{name: 'is_busi_optr'},
				{name: 'copy_optr_id'},
				{name: 'copy_optr_name'},
				{name: 'remark'}
			])
		}) ;
		var sm = new Ext.grid.CheckboxSelectionModel({});
		OptrManagePanel.superclass.constructor.call( this , {
			id:'optrManagePanelId',
			region : "center" ,
			ds: this.store ,
			sm:sm,
			cm: new Ext.grid.ColumnModel([
				{header: '操作员姓名', dataIndex: 'optr_name',width: 70,renderer:App.qtipValue},
				{id:'login_name', header: '工号', dataIndex: 'login_name',width: 80,renderer:App.qtipValue},
				{header: '状态', dataIndex: 'status_text',width: 40,renderer:Ext.util.Format.statusShow},
				{header: '电话1', dataIndex: 'tel',width: 80,renderer:App.qtipValue},
				{header: '电话2', dataIndex: 'mobile',width: 80,renderer:App.qtipValue},
				{header: '职位', dataIndex: 'position',width: 60,renderer:App.qtipValue},
				{header: '所在部门', dataIndex: 'dept_name',width: 80,renderer:App.qtipValue},
				{header: '所属县市', dataIndex: 'county_name',width: 80,renderer:App.qtipValue},
				{header: '复制操作员', dataIndex:'copy_optr_name',width:80,renderer:App.qtipValue},
				{header: '备注', dataIndex:'remark',width:100,renderer:App.qtipValue},
				{header: '操作', dataIndex: 'status',width: 250, renderer: function(v, md, record , i ){
					var txt = null;
					if(v =='INVALID'){
						txt = "启用";
						return "<a href='#' onclick='optrManagePanel.deleteInfo();'style='color:blue'>"+txt+" </a>";
					}
					if(v =='ACTIVE'){
						txt = "禁用";
						return "<a href='#' onclick='optrManagePanel.goToOptr();' style='color:blue'>分配权限</a>"
							+"&nbsp;&nbsp;<a href='#' onclick='optrManagePanel.updateRecord();'style='color:blue'> 修改 </a>"
						//	+"&nbsp;&nbsp;<a href='#' onclick='optrManagePanel.updateCopyRecord();'style='color:blue'> 修改复制 </a>"
							+"&nbsp;&nbsp;<a href='#' onclick='optrManagePanel.changeDept();'style='color:blue'> 更换部门 </a>"
							+"&nbsp;&nbsp;<a href='#' onclick='optrManagePanel.deleteInfo();'style='color:blue'>"+txt+" </a>";
					}
				}}
			]),
			bbar: new Ext.PagingToolbar({store: this.store}),
			tbar: [' ',' ','输入关键字' , ' ',
				new Ext.ux.form.SearchField({
	                store: this.store,
	                width: 200,
	                id:'searchValueId',
	                hasSearch : true,
	                emptyText: '支持工号,姓名模糊查询'
	            }),'->','-',{
	            		id:'copyOptrRecord',
	            		text:'复制操作员',
	            		iconCls:'icon-add',
	            		scope:this,
	            		handler: this.copyOptr
	            	
	            },'-',{
						text : '添加',
						iconCls : 'icon-add',
						scope : this,
						id:'addOptrRecord',
						handler : this.addRecord
	            }
			]
		});
	},
	deleteInfo :function(){
		var record = this.getSelectionModel().getSelected();
		var optrId = record.get('optr_id');
		var status = record.get('status');
		var t ="";
		if(status == 'INVALID'){
			t = "确认是否启用该操作员!";
		}
		if(status =='ACTIVE'){
			t = "确认是否禁用该操作员!";
		}
  		Confirm(t, null ,function(){
	  		Ext.Ajax.request({
	  			url: root + '/system/Optr!updateOptrStatus.action',
	  			params: {optr_id: optrId ,doneId:status},
	  			success: function( res,ops ){
	  				var rs = Ext.decode(res.responseText);
	  				if(true === rs.success){
		  				Alert('操作成功!',function(){
		  					optrManagePanel.loadData();
		  				});
	  				}else{
	  					Alert('操作失败');
	  				}
		  		}
	  		});
  		})
  	},
  	copyOptr: function(){
  		var win = new CopyOptrWind();
  		win.setTitle('添加复制操作员');
  		win.setIconClass('icon-add-user');
  		win.show();
  	},
  	updateCopyRecord: function(){
  		var record = this.getSelectionModel().getSelected();
  		var win = new CopyOptrWind(record,'updateCopy');
  		win.setTitle('修改复制操作员');
  		win.setIconClass('icon-edit-user');
  		win.show();
  	},
  	changeDept: function(){
  		var record = this.getSelectionModel().getSelected();
  		new OptrDeptWind(record).show();
  	},
  	addRecord : function() {
		var win = new OptrWindow();
		win.setTitle('增加操操作员');
		win.setIconClass('icon-add-user');
		win.show();
	},
	updateRecord : function() {
		var record = this.getSelectionModel().getSelected();
		var win = new OptrWindow(record,'update');
		win.setTitle('修改操作员');
		win.setIconClass('icon-edit-user');
		win.show();
	},
	loadData: function( ps ){
		var deptId = null ;
		if(Ext.getCmp('navigationDept')){
			deptId = Ext.getCmp('navigationDept').deptId;
		}
		Ext.getCmp('optrManagePanelId').getStore().load({
					params : {pid : deptId,start : 0,limit : Constant.DEFAULT_PAGE_SIZE}
				});
	},goToOptr:function(v){
    	var grid  = Ext.getCmp('optrManagePanelId');
        var win = new ResourceToOptrWindow(grid.getSelectionModel().getSelected());
    	win.setTitle('分配权限->操作员');
 		win.setIconClass('icon-add-user');   	
 		win.show();
    }
});

/**
  * 部门关系导航树
  */
var NavigationDept = Ext.extend( Ext.ux.FilterTreePanel , {
	searchFieldWidth: 140,
	countyId:null,
	deptId:null,
	deptName:null,
	constructor: function(){
		that= this;
		var loader = new Ext.tree.TreeLoader({
			dataUrl: root+"/system/Dept!queryDeptByCountyId.action"
		});
		NavigationDept.superclass.constructor.call(this, {
			region: 'west',
			width 	: 200,
			split	: true,
			id:'navigationDept',
			minSize	: 210,
	        maxSize	: 260,
	        margins		:'0 0 3 2',
	        lines		:false,
	        autoScroll	:true,
	        animCollapse:true,
	        animate		: false,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',
			loader 		: loader,
			rootVisible : false,
	        root: {
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				text: '系统地区结构',
				others : {
					att : 'async'
					}
			}
		});
//		this.getRootNode().expand();
		this.expandAll();
	},
	initEvents : function(){
		this.on("click" , function( node , e){
			var id = node.id ;
				this.deptId = id;
				this.deptName = node.text;
				this.countyId = node.attributes.others.countyId;
				if(node.attributes.others.dept_type != 'DQ'){
					Ext.getCmp('addOptrRecord').setDisabled(false);
					Ext.getCmp('addOptrRecord').setText('新增操作员');
					var url = root + '/system/Optr!queryOptrs.action'
					Ext.getCmp('optrManagePanelId').getStore().load({
						params: {pid: id,start : 0,limit : Constant.DEFAULT_PAGE_SIZE },
						url: url
					});
					Ext.getCmp('copyOptrRecord').setDisabled(false);
				}else{
					Ext.getCmp('addOptrRecord').setDisabled(true);
					Ext.getCmp('addOptrRecord').setText('先选择部门');
					Ext.getCmp('copyOptrRecord').setDisabled(true);
				}
		} , this);
		
		this.on("load" ,function(){
			Ext.getCmp('addOptrRecord').setDisabled(true);
			Ext.getCmp('addOptrRecord').setText('先选择部门');
			Ext.getCmp('copyOptrRecord').setDisabled(true);
		})
		NavigationDept.superclass.initEvents.call(this);
	}
});

/**
 * Main 入口函数
 */
optrManage = Ext.extend(Ext.Panel,{
	constructor:function(){
		var optrManagePanel = new OptrManagePanel();
		var navigationDept = new NavigationDept();
		optrManage.superclass.constructor.call(this,{
				id:'optrManage',
				title:'操作员管理',
				closable:true,
				border : false ,
				layout : 'border',
				baseCls:"x-plain",
				items : [ optrManagePanel, navigationDept ] 
		});
	}
});	
