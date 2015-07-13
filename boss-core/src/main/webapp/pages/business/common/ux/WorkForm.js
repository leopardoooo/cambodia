

/**
 * 工单明细类型Record定义
 */
WorkTaskType = Ext.data.Record.create([
	{name: 'task_type_name' },	
	{name: 'task_type' }
]);

/************************************
 * 施工单表单
 */
WorkForm = Ext.extend( BaseForm, {

	workData: null,
	bgColor: '#F5F5F5',
	
	ColumnCount: 1,
	ColumnWidth: 1,
	constructor: function(workData,docForm, allowTitle){
		
		this.workData = workData || [] ;
		
		var items = [];
		for(var i = 0; i< this.ColumnCount; i++){
			items.push({columnWidth: this.ColumnWidth});
		}
		
		//setter default linkman and mobile
		var linkman="", mobile = "";
		var data = App.getApp().data;
		var info = data.custFullInfo;
		if(info && data.currentResource.busicode != '1001'){
			linkman = info.cust.cust_name;
			mobile = info.linkman.mobile;
			if(!mobile || mobile == ""){
				mobile = info.linkman.tel;
			}
		}
		
		this.linkmanField = new Ext.form.TextField({
			name: 'task_cust_name',
			value: linkman,
			fieldLabel: '联系人'
		});
		
		this.mobileField = new Ext.form.TextField({
			name: 'mobile',
			value: mobile,
			fieldLabel: '联系电话'
		});
		
		//call superclass constructor
		WorkForm.superclass.constructor.call(this, {
			title: (allowTitle === false) ? null : '工单' ,
			layout: 'border',
			items: [{
				region: 'north',
				height: 130,
				border: false,
				layout: 'form',
				labelWidth: 70,
				bodyStyle: "padding-top: 5px;",
				items: [{
						xtype : 'xdatetime',
						fieldLabel : '预约时间',
						width : 160,
						name:'books_time',
						minText : '不能选择当日之前',
						timeWidth : 60,
						timeFormat : 'H:i',
						timeConfig : {
							increment : 60,
							editable:true,
							altFormats : 'H:i|H:i:s',
							minValue:'04:00',  
    						maxValue:'21:00' 
						},
						dateFormat : 'Y-m-d'
						,dateConfig : {
							altFormats : 'Y-m-d|Y-n-d',
							minValue : new Date().format('Y-m-d')
						}
				},this.linkmanField,this.mobileField,{
					layout:'form',labelWidth:70,border:false,items:[{
						fieldLabel:'备注',xtype:'textarea',name: 'remark',height:40,anchor:'96%'
					}]				
				}]
			},{
				bodyStyle: "padding-left: 5px;",
				layout: 'column',
				border: false,
				defaults:{
					border: false,
					layout: 'form',
					style: 'margin-left: 5px; margin-right: 5px;',
					defaults:{
						style: 'margin-right: 5px;',
						bodyStyle: "border:none; margin-right: 5px;"
					}
				},
				region: 'center',
				items: items
			}]
		});  
	},
	setLinkmanValue: function(v){
		this.linkmanField.setValue(v);
	},
	setMobileValue: function(v){
		this.mobileField.setValue(v);
	},
	getValues:function(){
		var obj = {}, works = [];
		for(var i=0; i< this.workData.length ;i++){
			var tmp = Ext.getCmp('task_' + i).inputValue;
			var docType = Ext.getCmp('checkboxgroup_' + i).getValue();
			//igore
			if(Ext.getCmp('task_' + i).collapsed == true || docType.length == 0){
				continue;
			}
			for(var j=0; j < docType.length ; j++){
				tmp += "#" + docType[j].inputValue;
			}
			works.push(tmp);
		}
		obj['taskIds'] = works;
		var fvs = this.getForm().getValues();
		obj["task_books_time"] = Ext.isEmpty(fvs["books_time"])? null : fvs["books_time"];
		obj["task_cust_name"] = fvs["task_cust_name"];
		obj["task_mobile"] = fvs["mobile"];
		obj["task_remark"] = fvs["remark"];
		return obj;
	},
	doValid: function(){
		var fvs = this.getForm().getValues();
		if(Ext.isEmpty(fvs["task_cust_name"])){
			return{isValid: false,
			msg: "联系人不能为空!"};
		}
		for(var i=0; i< this.workData.length ;i++){
			if(Ext.getCmp('task_' + i).collapsed == false 
				&& Ext.getCmp('checkboxgroup_' + i).getValue().length == 0){
				return {
					isValid: false,
					msg: "工单选中时，必须选择至少一个服务类型!"
				};
			}
		}
		
		return true;
	},
	doInit: function(){
		Ext.Ajax.request({
			params: {comboParamNames: ['USER_TYPE']},
			url: root + "/ps.action",
			scope: this,
			success: function( res, ops){
				var data = Ext.decode(res.responseText );
				for(var i=0;i<data[0].length;i++){
					if(data[0][i].item_value != 'ATV'){
						this.serviceTypes.push(data[0][i]);
					}
				}
				var taskItems = this.items.itemAt(1).items;
				//施工单生成
				for(var i=0; i< this.workData.length; i++){
					var taskPanel = this.gTaskItems( new WorkTaskType( this.workData[i] ), i, this.serviceTypes);
					this.items.itemAt(1).items.get( i % this.ColumnCount ).add(taskPanel);
				}
				this.doLayout();
				this.loadSuccess = true;
				//开户时就不默认
				var busiCode = App.getApp().data.currentResource.busicode;
				if('1020' != busiCode && '1001' != busiCode){
					this.setDefaultChecked();
				}
			}
		});	
	},
	serviceTypes: [],
	loadSuccess: false,
	//设置默认选中的工单类型，根据首页查询的用户信息，然后选中对应的类型
	setDefaultChecked: function(){
		var users = App.getApp().data.users;
		var checked = {};
		for(var i = 0; i< users.length; i++){
			checked[users[i]["user_type"]] = true;
		}
		this.setChecked(checked);
	},
	setChecked: function(userType){
		//这里判断是否加载成功，工单和其它业务表单都有个远程初始化数据的过程
		//而且都是异步调用的，所以这里有个先后顺序的过程，因此下面的代码是为了标记
		//工单是否加载完毕，see #doInit()
		if(this.loadSuccess === false){
			var that = this;
			setTimeout( function(){
				that.setChecked(userType);
			} , 100);
			return;
		}
		for(var i=0; i< this.workData.length ;i++){
			var checkedO = {};
			for(var j = 0 ;j< this.serviceTypes.length; j++){
				var type = this.serviceTypes[j].item_value;
				if(Ext.getCmp('checkboxgroup_item_' + i + "_" + type )){
					Ext.getCmp('checkboxgroup_item_' + i + "_" + type ).setValue(userType[type] ? true : false);
				}
			}
		}
	},
	gTaskItems: function( r, i , serviceTypes){
		var typeItems = [];
		for(var k = 0; k <serviceTypes.length ;k++ ){
			typeItems.push({
				id: 'checkboxgroup_item_' + i + "_" + serviceTypes[k].item_value,
				boxLabel: serviceTypes[k].item_name, 
				inputValue: serviceTypes[k].item_value,
				checked: false
			})
		}
		return {
			xtype: 'fieldset',
			title: r.get('task_type_name'), 
			id: 'task_' + i,
//			collapsible: true,
			checkboxToggle: true,
      	  	collapsed: true, // fieldset initially collapsed
			inputValue: r.get('task_type'),
			layout: 'fit',
			items: [{
				xtype: 'checkboxgroup',
				height:20,
            	columns: 3,
            	id: 'checkboxgroup_' + i,
            	items: typeItems
			}]
		};
	}
});