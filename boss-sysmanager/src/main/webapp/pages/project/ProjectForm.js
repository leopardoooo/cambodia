
ProjectForm = Ext.extend(Ext.form.FormPanel,{
	projectInfo: null,
	countyStore: null,
	constructor:function(projectInfo){
		this.projectInfo = projectInfo;
		
		this.countyStore = new Ext.data.JsonStore({
			url:root+'/project/Project!queryByCountyId.action',
			fields:['county_id','county_name','project_county_id']
		});
		this.countyStore.load();
		
		ProjectForm.superclass.constructor.call(this,{
			hideBorders:true,
			bodyStyle:'padding-top:10px',
			labelWidth:100,
			labelAlign:'right',
			layout:'table',
			layoutConfig:{
				columns:6,
				tableAttrs:{
		            style:{width: '100%'}
		        }
			},
			defaults:{
				layout:'form',
				defaults:{xtype:'textfield'}
			},
			items:[
				{colspan:2,items:[
					{id:'project_number_id',fieldLabel:'项目编号',name:'project_number',minLength:13,maxLength:13,allowBlank:false,
						regex:/^0[0-6][0-9]{4}[XGS][0-9]{6}$/,regexText:'项目编号除第7位为字母XGS外，其他位均为数字',
						listeners:{
							scope:this,
							blur:function(txt){
								if(txt.isValid()){
									var value = txt.getValue();
									Ext.Ajax.request({
										url:root+'/project/Project!checkProjectNumber.action',
										params:{projectNumber:value},
										scope:this,
										success:function(res){
											var data = Ext.decode(res.responseText);
											if(data === true){
												alert("该项目编号已存在");
												txt.setValue('');
											}
										}
									});
								}
							}
						}
					}
				]},
				{colspan:2,items:[ 
					{fieldLabel:'状态',hiddenName:'status',xtype:'paramcombo',paramName:'PROJECT_STATUS',defaultValue:'0',allowBlank:false}
				]},
				{colspan:2,items:[
					{xtype:'hidden',name:'project_id'}
				]},
				{colspan:2,items:[
					{fieldLabel:'项目类型',hiddenName:'project_type',xtype:'paramcombo',paramName:'PROJECT_TYPE',defaultValue:'X',
						listeners:{
							scope:this,
							select:function(combo){
								var projectIdComp = this.getForm().findField('project_number');
								var oldValue = projectIdComp.getValue();
								projectIdComp.setValue(oldValue.substring(0,6)+""+combo.getValue()+""+oldValue.substring(7,13));
							}
						}
					},
					{fieldLabel:'县市名称',xtype:'combo',hiddenName:'county_id',store:this.countyStore,
						displayField:'county_name',valueField:'county_id',triggerAction:'all',
						listeners:{
							scope:this,
							select:function(combo,record){
								var projectCountyId = record.get('project_county_id');
								var projectIdComp = this.getForm().findField('project_number');
								var oldValue = projectIdComp.getValue();
								projectIdComp.setValue(projectCountyId+""+oldValue.substring(2,oldValue.length));
							}
						}
					},
					/*{fieldLabel:'项目顺序码',name:'project_order_code',xtype:'textfield',
						vtype:'numZero',minLength:4,maxLength:4,listeners:{
							scope:this,
							blur:function(txt){
								var projectIdComp = this.getForm().findField('project_number');
								var oldValue = projectIdComp.getValue();
								projectIdComp.setValue(oldValue.substring(0,2)+""+txt.getValue()+""+oldValue.substring(6,oldValue.length));
							}
						}
					},*/
					{fieldLabel:'预计开工日期',name:'pre_start_date',xtype:'datefield',format:'Y-m-d',width:122},
					{fieldLabel:'实际开工日期',name:'start_date',xtype:'datefield',format:'Y-m-d',width:122},
					{fieldLabel:'工程预算金额',name:'project_plan_money'}
					
				]},
				{colspan:2,items:[
					{fieldLabel:'项目名称',name:'project_name'},
					/*{fieldLabel:'年份顺序码',name:'year_order_code',xtype:'textfield',
						vtype:'numZero',minLength:2,maxLength:2,listeners:{
							scope:this,
							blur:function(txt){
								var projectIdComp = this.getForm().findField('project_number');
								var oldValue = projectIdComp.getValue();
								projectIdComp.setValue(oldValue.substring(0,11)+""+txt.getValue());
							}
						}
					},*/
					{fieldLabel:'计划覆盖数',name:'plan_num',xtype:'numberfield',allowDecimals:false,allowNegative:false},
					{fieldLabel:'预计完工日期',name:'pre_end_date',xtype:'datefield',format:'Y-m-d',width:122},
					{fieldLabel:'实际完工日期',name:'end_date',xtype:'datefield',format:'Y-m-d',width:122},
					{fieldLabel:'工程决算金额',name:'project_final_money',allowBlank:false}
				]},
				{colspan:2,items:[
					{fieldLabel:'申报日期',name:'create_date',xtype:'datefield',format:'Y-m-d',altFormats:'Y-m-d',width:122,value:nowDate().format('Y-m-d')},
					{fieldLabel:'实际覆盖数',name:'real_num',xtype:'numberfield',allowDecimals:false,allowNegative:false},
					{fieldLabel:'光缆长度',name:'optic_cable_length',xtype:'numberfield',allowDecimals:false,allowNegative:false},
					{fieldLabel:'电缆长度',name:'electric_cable_length',xtype:'numberfield',allowDecimals:false,allowNegative:false},
					{fieldLabel:'光节点个数',name:'optical_node_number',xtype:'numberfield',allowDecimals:false,allowNegative:false}
				]},
				{colspan:6,border:true,bodyBorder:false,height:1},
				{items:[
					{xtype:'displayfield',fieldLabel:''},
					{xtype:'displayfield',fieldLabel:'计划收入/元&nbsp;&nbsp;',labelSeparator:''},
					{xtype:'displayfield',fieldLabel:'发展用户数&nbsp;&nbsp;',labelSeparator:''}
					
				]},
				{items:[
					{xtype:'displayfield',fieldLabel:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第一年',labelSeparator:'',labelStyle:'text-align:left'},
					{name:'plan_income_1',xtype:'numberfield',hideLabel:true,allowNegative:false,width:80},
					{name:'plan_users_1',xtype:'numberfield',hideLabel:true,allowNegative:false,allowDecimals:false,width:80}			
				]},
				{items:[
					{xtype:'displayfield',fieldLabel:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第二年',labelSeparator:'',labelStyle:'text-align:left'},
					{name:'plan_income_2',xtype:'numberfield',hideLabel:true,allowNegative:false,width:80},
					{name:'plan_users_2',xtype:'numberfield',hideLabel:true,allowNegative:false,allowDecimals:false,width:80}				
				]},
				{items:[
					{xtype:'displayfield',fieldLabel:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第三年',labelSeparator:'',labelStyle:'text-align:left'},
					{name:'plan_income_3',xtype:'numberfield',hideLabel:true,allowNegative:false,width:80},
					{name:'plan_users_3',xtype:'numberfield',hideLabel:true,allowNegative:false,allowDecimals:false,width:80}
				]},
				{items:[
					{xtype:'displayfield',fieldLabel:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第四年',labelSeparator:'',labelStyle:'text-align:left'},
					{name:'plan_income_4',xtype:'numberfield',hideLabel:true,allowNegative:false,width:80},
					{name:'plan_users_4',xtype:'numberfield',hideLabel:true,allowNegative:false,allowDecimals:false,width:80}
				]},
				{items:[
					{xtype:'displayfield',fieldLabel:'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第五年',labelSeparator:'',labelStyle:'text-align:left'},
					{name:'plan_income_5',xtype:'numberfield',hideLabel:true,allowNegative:false,width:80},
					{name:'plan_users_6',xtype:'numberfield',hideLabel:true,allowNegative:false,allowDecimals:false,width:80}
				]},
				{colspan:6,border:true,bodyBorder:false,height:1},
				{colspan:6,bodyStyle:'padding-top:10px',items:[
					{name:'remark',fieldLabel:'备注',anchor:'98%'}					
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'取消',scope:this,handler:this.doCancel}
			]
		});
		
		this.countyStore.on('load',function(store){
			if(store.getCount() == 1){
				var record = store.getAt(0);
				var countyIdComp = this.getForm().findField('county_id');
				countyIdComp.setValue(record.get('county_id'));
				
				countyIdComp.fireEvent('select',countyIdComp,record);
			}
		},this);
		
		App.form.initComboData( this.findByType("paramcombo"),function(){
			if( !Ext.isEmpty(this.projectInfo) ){
				this.getForm().setValues(this.projectInfo);
			}
		},this);
//		this.getForm().findField('project_number').allowBlank = true;
//		Ext.getCmp('project_number_id').allowBlank = true;
	},
	initComponent: function(){
		ProjectForm.superclass.initComponent.call(this);
		
		var form = this.getForm();
		
		//激活组件
		var disableFormField = function(fieldsArray){
			if(Ext.isArray(fieldsArray)){
				Ext.each(fieldsArray,function(fieldName){
					var field = form.findField(fieldName);
					if(field){
						field.disable();
						field.labelStyle = 'color:gray;';
					}
				});
			}
		}
		
		/*form.items.each(function(field){
			if(field.isFormField && !(field instanceof Ext.form.DisplayField)){
				field.allowBlank = true;
			}
		},this);*/
		
		var optr = App.data.optr;
		//省公司操作员可以修改任何属性
		if(optr['county_id'] != '4501'){
			//分公司不能修改 项目编号 状态 工程决算金额
			disableFormField(['project_number','status','project_final_money']);
			/*'project_name','project_type','declare_date',
					'pre_start_date','pre_end_date','plan_num',
					'plan_users_1','plan_income_1',
					'plan_users_2','plan_income_2',
					'plan_users_3','plan_income_3',
					'plan_users_4','plan_income_4',
					'plan_users_5','plan_income_5',
					'project_plan_money'*/
			
		}
		/*Ext.each(['project_number','status','project_final_money'],function(fieldName){
				var field = form.findField(fieldName);
				field.allowBlank = false;
			});*/
	},
	doSave: function(){
		//让项目编号失去焦点，检测是否已存在该项目编号
		this.getForm().findField('project_name').focus();
		var isValid = this.doValid();
		if(isValid['isValid'] === true){
			var values = this.doValue(), obj={};
			for(var i in values){
				obj['project.'+i] = values[i];
			}
			
			Ext.Ajax.request({
				url:root+'/project/Project!saveProject.action',
				params:obj,
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data['success'] === true){
						if(Ext.isEmpty(data['simpleObj']) ){
							Alert('保存成功');
							this.doCancel();
							Ext.getCmp('projectQueryGridId').getStore().load({params:{start:0,limit:projectLimit}});
						}else{
							Alert('项目编号顺序码:'+ values['project_number'].substring(2, 6)
								+ '<br/>在小区 <font color=red>' + data['simpleObj']+ '</font> 已存在,' 
								+' <br/>请修改项目编号顺序码');
						}
					}
				}
			});
			
		}else{
			if(isValid['msg']){
				Alert(isValid['msg']);
			}
		}
	},
	doCancel: function(){
		this.ownerCt.close();
//		Ext.getCmp('projectWindowId').close();
//		this.ownerCt.fireEvent('close',this.ownerCt);
//		var win = Ext.getCmp('projectWindowId');
//		win.fireEvent('close',win);
//		win.isDestroyed = true;
//		win.close();
	},
	doValid: function(){
		var obj= {};
		obj['isValid'] = false;
		if(this.getForm().isValid()){
			obj['isValid'] = true;
			var values = this.getForm().getValues();
			
			//可以将 "新建" 按钮设置数据权限，只有省公司操作员才显示，其他隐藏
			if (Ext.isEmpty( values['project_number'] )
					&& Ext.isEmpty( values['status'] )
					&& Ext.isEmpty( values['project_final_money'] ) ) {
				obj['isValid'] = false;
				obj['msg'] = "只有省公司操作员才能新建项目";
				return obj;
			}
			
			var projectNumber = values['project_number'];
			var unitCode = projectNumber.substring(0,2);			//单位代码
			var projectOrderCode = projectNumber.substring(2,6);	//项目顺序码
			var projectType = projectNumber.charAt(6);				//项目类别
			
			var project_county_id = '',county_id = values['county_id'];
			this.countyStore.each(function(r){
				if(r.get('county_id') == county_id){
					project_county_id = r.get('project_county_id');
					return false;
				}
			});
			if(unitCode != project_county_id){
				obj['isValid'] = false;
				obj['msg'] = "项目编号前2位和选择的县市不符";
				return obj;
			}
			
			if(projectType != values['project_type']){
				obj['isValid'] = false;
				obj['msg'] = "项目编号第七位和选择的项目类型不符";
				return obj;
			}
			
		}
		return obj;
	},
	doValue: function(){
		if(this.doValid()['isValid'] === true){
			var values = this.getForm().getValues();
			
			//分公司人员修改，项目编号等被禁用，getValue为空
			if(Ext.isEmpty(values['project_number'])){
				values['project_number'] = this.projectInfo['project_number'];
			}
			if(Ext.isEmpty(values['status'])){
				values['status'] = this.projectInfo['status'];
			}
			if(Ext.isEmpty(values['project_final_money'])){
				values['project_final_money'] = this.projectInfo['project_final_money'];
			}
			return values;
		}
	},
	doReset: function(){
		this.getForm().reset();
	}
	
});
