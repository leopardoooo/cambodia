/**
 * 增加报表
 * 
 * @class DeployReportForm
 * @extends Ext.form.FormPanel
 */

DeployReportForm = Ext.extend(Ext.form.FormPanel, {
    loadData: null,
    isReloaded : null,//3个combo是否已经加载完的标识
    rep_remark:null,
    constructor: function (data) {
    	this.isReloaded = 0;
        this.loadData = data;
        DeployReportForm.superclass.constructor.call(this, {
            id: 'deployReportFormId',
            border: false,
            labelWidth: 70,
            autoScroll: true,
	            defaults: {
	                baseCls: 'x-plain'
            },
            bodyStyle: 'padding-top:10px',
            tbar:[
		    		'-',{text:'重载资源',scope:this,handler:function(){
                        	Ext.Ajax.request({
					    		url:root+'/system/Index!updateMemory.action',
					    		scope:this,
					    		success:function(res,opt){
					    			var data = res.responseText;
					    			if(Ext.isEmpty(data) || data == '""'){
					    				Alert("重载成功");
					    			}else{
					    				Alert("重载问题："+data);
					    			}
		    					}
	    					});
                        }},'-',{text:'查询条件清单',scope:this,handler:this.queryCond},'-',
                        {text:'列转换清单',scope:this,handler:this.queryMemorynd},'-'
		    	],
            items: [
            // form条件
            {
                layout: 'column',
                defaults: {
                    baseCls: 'x-plain',
                    defaultType: 'textfield'
                },
                border: false,
                items: [{
                    layout: 'form',
                    columnWidth: .3,
                    items: [{
                        fieldLabel: '报表ID',
                        id: 'rep_id_id',
                        name: 'repDefineDto.rep_id',
                        width: 122,
                        readOnly: true
                    },
                    {
                        fieldLabel: '报表类型',
                        id:'rep_type_id_id',
                        hiddenName: 'repDefineDto.rep_type',
                        xtype: 'combo',
                        allowBlank: false,
                        store: new Ext.data.JsonStore({
                            url: root + '/query/RepDesign!queryRepType.action',
                            fields: ['item_name', 'item_value']
                        }),
                        displayField: 'item_name',
                        valueField: 'item_value',
                        triggerAction: 'all',
                        mode: 'local'
                    },
                    {
                        fieldLabel: '数据源',
                        hiddenName: 'repDefineDto.database',
                        xtype: 'combo',
                        allowBlank: false,
                        store: new Ext.data.JsonStore({
                            url: root + '/query/RepDesign!queryDatabase.action',
                            fields: ['database', 'name']
                        }),
                        displayField: 'name',
                        valueField: 'database',
                        triggerAction: 'all',
                        mode: 'local'
                    }]
                },
                {
                    layout: 'form',
                    columnWidth: .3,
                    items: [{
                        xtype: 'treecombo',
                        fieldLabel: '所属菜单',
                        id: 'res_pid_id',
                        hiddenName: 'repDefineDto.res_pid',
                        minChars: 0,
                        height: 40,
                        treeWidth: 300,
                        allowBlank: false,
                        readOnly: true,
                        treeUrl: root + '/system/Index!queryResourcesByResType.action',
                        listeners: {
                            focus: function () {
                                if (this.list) {
                                   // this.doQuery("");
                                	this.expand();
                                }
                            }
                        }
                    },
                    {
                        fieldLabel: '分类',
                        hiddenName: 'repDefineDto.rep_info',
                        xtype: 'combo',
                        allowBlank: false,
                        store: new Ext.data.JsonStore({
                            url: root + '/query/RepDesign!queryRepInfo.action',
                            fields: ['item_name', 'item_value']
                        }),
                        displayField: 'item_name',
                        valueField: 'item_value',
                        triggerAction: 'all',
                        mode: 'local'
                    },
                    {
                        fieldLabel: '排序',
                        width: 122,
                        name: 'repDefineDto.sort_num'
                    }]
                },
                {
                    layout: 'form',
                    columnWidth: .4,
                    items: [{
                    	id:'rep_name_name',
                        fieldLabel: '报表名',
                        name: 'repDefineDto.rep_name',
                        width: 150,
                        allowBlank: false
                    },
                    {
                        xtype: 'panel',
                        layout: 'column',
                        border: false,
                        defaults: {
                            border:false
                        },
                        items: [{
                            layout: 'form',
                            columnWidth: .25,
                            items: [{
                                fieldLabel: '快捷模板',
                                xtype: 'label'
                            }]
                        },
                        {
                            layout: 'column',
                            columnWidth: .6,
                            border:false,
                            defaults:{border:false},
                            items: [
                            {
                            	xtype: 'button',
                            	text: '上传',
                            	scope: this,
                            	handler: this.doUpload
                            },{
			        			width: 20,
			        			html: "&nbsp;"
			        		},{
                            	xtype: 'button',
                            	text: '下载',
                            	scope: this,
                            	handler: this.doDownload
                            },{
			        			width: 10,
			        			html: "&nbsp;"
			        		},{
				                xtype: 'label',
                                id: 'quiee_raq_id',
                                name: 'repDefineDto.quiee_raq',
                                fieldLabel: '',
                                labelSeparator: ''
				            }]
                        }]
                    }]
                }]
            },
            // sql editor
            {
                id: 'sqlId',
                xtype: 'textarea',
                fieldLabel: '配置SQL',
                name: 'repDefineDto.sql',
                width: 690,
                height: 350,
                allowBlank: false
            },
            {height:5,html:'&nbsp;'},
            {
                layout: 'column',
                border: false,
                defaults: {
                	border: false
                },
                items: [{
                    width:75,
                    html:'&nbsp;'
                },
                {
                    layout: 'form',
                    width:215,
                    items: [{
                        xtype: 'button',
                        text: '测试SQL',
                        scope: this,
                        handler: this.testSQL
                    }]
                },
                {
                    layout: 'form',
                    width:278,
                    items: [{
                        xtype: 'lovcombo',
                        fieldLabel: '合计项选择',
                        id: 'hjx_id',
                        name: 'repDefineDto.rep_total_list',
                        store: new Ext.data.ArrayStore({
                            fields: ['hjx']
                        }),
                        displayField: 'hjx',
                        valueField: 'hjx',
                        triggerAction: 'all',
                        listWidth:250,
                        mode: 'local'
                    }

                    ]
                },{layout: 'form',
                	width:200,
                    items: [{
                        xtype: 'combo',
                        fieldLabel: '分组选择',
                        id: 'repgroup_idid',
                        name: 'repDefineDto.rep_group',
                        store: new Ext.data.ArrayStore({
                            fields: ['hjx','hjx_text']     
                        }),
                        displayField: 'hjx_text',
                        valueField: 'hjx',
                        triggerAction: 'all',
                        listWidth:250,
                        mode: 'local'
                    }]
               }]
            }],
            buttonAlign: 'left',
            buttons: [
            {xtype:'panel',border:false,width:210,html:"&nbsp;"},
            {
                text: '提交',
                scope: this,
                handler: this.doSubmit
            },
        	{xtype:'panel',border:false,width:5,html:"&nbsp;"},
            {
                text: '分配角色',
                scope: this,
                handler: function () {
                    var rep_id = Ext.getCmp('rep_id_id').getValue();
                    if (Ext.isEmpty(rep_id)) {
                        Alert("报表ID为空，请先保存报表");
                    } else {
                        this.addRole(rep_id);
                    }

                }
            },
        	{xtype:'panel',border:false,width:5,html:"&nbsp;"},
            {
                text: '配置明细查询',
                scope: this,
                handler: function () {
                	 var rep_id = Ext.getCmp('rep_id_id').getValue();
                    if (Ext.isEmpty(rep_id)) {
                        Alert("报表ID为空，请先保存报表");
                        return;
                    }else if (Ext.getCmp('rep_type_id_id').getValue()=='olap'){
                    	Alert("OLAP报表，该功能禁用");
                    	return;
                    }  else{
	                    var win = Ext.getCmp('deployDetailWinId');
	                    if (!win) {
	                        win = new DeployDetailWin();
	                    }
	                    win.show(Ext.getCmp('sqlId').getValue(),Ext.getCmp('rep_id_id').getValue());
                    }
                }
            },
        	{xtype:'panel',border:false,width:5,html:"&nbsp;"},
            {
            	text: '配置CUBE',
            	scope: this,
            	handler: function(){
            		var form = this.getForm();
			        if (!form.isValid()) return;
			        if(Ext.getCmp('rep_type_id_id').getValue()!='olap'){
			        	Alert("非Olap报表");
			        	return;
			        }
			        var values = this.getValues();
            		doReportSave(values, Ext.getCmp('rep_id_id'), function(repId){
            			new CubeWindow(repId).show();
            		});
            	}
            }]
        });
    },
    initComponent: function () {
        DeployReportForm.superclass.initComponent.call(this);
        // 初始化组件时，一起加载下拉框
        var comboes = this.findByType('combo');
        for (var i = 0; i < comboes.length; i++) {
            // 加载时忽略 TreeCombo 和 "合计项"下拉框
        	//
            if (!(comboes[i] instanceof Ext.ux.TreeCombo)&& comboes[i].id != 'hjx_id'&& comboes[i].id!='repgroup_idid' ){
            	comboes[i].getStore().load();
            	comboes[i].getStore().on('load',function(){
            		this.isReloaded = this.isReloaded + 1;
            		this.doLoadRecord();
            	},this);
            } 
        }
    },
    doLoadRecord : function(){
    	if(this.isReloaded == 3){
    		if (!Ext.isEmpty(this.loadData)) {
	            var obj = {};
	            for (var d in this.loadData) {
	                obj['repDefineDto.'.concat(d)] = this.loadData[d];
	            }
	            var ReportRecord = Ext.data.Record.create([{
	                name: 'repDefineDto.rep_name'
	            },
	            {
	                name: 'repDefineDto.sql'
	            },
	            {
	                name: 'repDefineDto.sort_num'
	            },
	            {
	                name: 'repDefineDto.res_pid'
	            },
	            {
	                name: 'repDefineDto.res_pid_text'
	            },
	            {
	                name: 'repDefineDto.rep_total_list'
	            },
	            {
	                name: 'repDefineDto.rep_id'
	            },
	            {
	                name: 'repDefineDto.rep_type'
	            },
	            {
	                name: 'repDefineDto.database'
	            },
	            {
	                name: 'repDefineDto.rep_info'
	            },
	            {
	                name: 'repDefineDto.quiee_raq'
	            },
	            {
	                name: 'repDefineDto.detail_id'
	            },
	            {
	                name: 'repDefineDto.remark'
	            }]);
	            this.getForm().loadRecord(new ReportRecord(obj));
	            //alert(this.loadData['res_pid_text']);
	            //Ext.getCmp('res_pid_id').setText(this.loadData['res_pid_text']);
	            //合计项值设置
	            Ext.getCmp('hjx_id').setRawValue(this.loadData['rep_total_list']);
	            
	            if(Ext.isEmpty(this.loadData['quiee_raq_text'])){
	            	Ext.getCmp('quiee_raq_id').setText('');
	            }else{
	            	Ext.getCmp('quiee_raq_id').setText(this.loadData['quiee_raq_text']);
	            }
	            Ext.getCmp('repgroup_idid').setValue(this.loadData['rep_group']);
	        }
    		this.isReloaded = 0;
    		
    		this.rep_remark = this.loadData ? this.loadData.remark : '';
    	}
    },
    // 获取form值，相当于this.getForm.getValues(),给组件加上前缀,后台action中组合成对象
    getValues: function () {
        var values = this.getForm().getValues();
        var obj = {};
        for (var v in values) {
            if (v.indexOf('repDefineDto.') != -1) {
                obj[v] = values[v];
            }
        }
        return obj;
    },
    testSQL: function () {
        var form = this.getForm();
        if (!form.isValid()) return;
        var values = this.getValues();
        Ext.Ajax.request({
            url: root + '/query/RepDesign!testSql.action',
            params: values,
            scope: this,
            success: function (res, opt) {
                var data = Ext.decode(res.responseText);
                if (Ext.isEmpty(data['exception'])) {
                	Alert('测试成功');
                	
                	var rep_groups=data["group"];//分组项
                	if (!Ext.isEmpty(rep_groups)) { 
                		var group=Ext.getCmp('repgroup_idid');
                	 	if(Ext.isEmpty(group.getValue())&&!rep_groups.indexOf(group.getValue)>-1)
                	 		group.setValue('');
                	 	var arr = rep_groups.split(',');
                        // set 合计项 数据
                        var loadData = [];
                        loadData.push(['','取消分组'])
                        for (var i = 0; i < arr.length; i++) {
                            loadData.push([arr[i],arr[i]]); // ArrayStore 数据格式
                        }
                	 	group.getStore().loadData(loadData);
                	}
                	
                    var rep_total_list = data["total"]; // "合计项"下拉框
                    // 数据
                    if (!Ext.isEmpty(rep_total_list)) {          
                        var arr = rep_total_list.split(',');
                        // set 合计项 数据
                        var loadData = [];
                        for (var i = 0; i < arr.length; i++) {
                            loadData.push([arr[i]]); // ArrayStore 数据格式
                        }
                        var hjx = Ext.getCmp('hjx_id');
                        hjx.getStore().loadData(loadData);
                        //判断合计项的值，是否需要重新刷新
                    	var hjx_value=hjx.getRawValue();
                        if(!Ext.isEmpty(hjx_value)){
                        	var hjxvalue_list=hjx_value.split(',');
                        	for(var i=0;i<hjxvalue_list.length;i++){
                        		if(rep_total_list.indexOf(hjxvalue_list[i].trim())<0)
                        			hjx.setRawValue('');
                        	}
                        }
                    }
                } else {
                    Alert(data['exception']);
                }
            }
        });
    },
    // 提交 按钮方法
    doSubmit: function () {
        var form = this.getForm();
        if (!form.isValid()) return;
        var values = this.getValues();
        
        
        if(this.rep_remark){
        		values['repDefineDto.remark']=this.rep_remark;
        }
        var win = Ext.getCmp('remarkWinId');
        if (!win) win = new RemarkWin();
        win.show(values);
    },
    // 查询条件代码 按钮方法
    queryCond: function () {
        var win = Ext.getCmp('condCodeWinId');
        if (!win) win = new CondCodeWin();
        win.show();
    },
    //MemoryKeyWin
    queryMemorynd: function () {
        var win = Ext.getCmp('MemoryKeyWinId');
        if (!win) win = new MemoryKeyWin();
        win.show();
    },
    //分配角色
    addRole: function (v) {
        var win = Ext.getCmp(v + 'rolewin');
        if (!win) {
            win = new RoleWindow(v);
        }
        win.show();
        win = null;
    },
    doUpload: function(){
    	var win = Ext.getCmp('uploadWinId');
        if (!win) win = new UploadWin();
        win.show();
    },
    doDownload: function(){
        var quiee_raq = Ext.getCmp('quiee_raq_id').text;
        alert(quiee_raq);
        if (Ext.isEmpty(quiee_raq)) {
            Alert('请先上传！');
            return;
        }
        window.location.href = root + "/query/RepDesign!downloadQuieeRaq.action?quiee_raq=" + quiee_raq;
    }
});

var DeployDetailWin = Ext.extend(Ext.Window, {
	repDetailDto:null,
    constructor: function () {
        DeployDetailWin.superclass.constructor.call(this, {
            id: 'deployDetailWinId',
            title: '明细配置',
            width: 550,
            height: 525,
            closeAction: 'close',
            layout:'fit',
            items: [{
                layout: 'form',
                bodyStyle: 'padding-top:10px',
                border: false,
                labelAlign:'right',
                labelWidth:85,
                defaults:{border:false},
                items: [{
                	fieldLabel:'主表配置SQL',
                	id:'detail_sqlId',
                    xtype: 'textarea',
                    readOnly:true,
                    width: 400,
                    height: 150
                },
               // {height:5,html:'&nbsp;'},
                {
                	fieldLabel:'明细配置SQL',
                    xtype: 'textarea',
                    name: 'repDefineDto.sql',
                    width: 400,
                    height: 250
                },
                	//{height:5,html:'&nbsp;'},
                {
                    layout: 'column',
                    border: false,
                    defaults:{border:false},
                    items: [{
                        columnWidth:.3,html:'&nbsp'
                    },
                    {
                        columnWidth: .2,
                        layout: 'form',
                        items: [{
                            xtype: 'button',
                            text: '测试SQL',
                            scope: this,
                            handler: this.testSQL
                        }]
                    },
                    {
                        columnWidth: .45,
                        layout: 'form',
                        labelWidth: 50,
                        items: [{
                            xtype: 'lovcombo',
                            fieldLabel: '合计项',
                            name: 'repDefineDto.rep_total_list',
	                        store: new Ext.data.ArrayStore({
	                            fields: ['hjx']
	                        }),
	                        displayField: 'hjx',
	                        valueField: 'hjx',
                            triggerAction: 'all',
                            listWidth:250,
                            mode: 'local'
                        }]
                    }]
                }]
            }

            ],
            buttonAlign: 'center',
            buttons: [{
                text: '保存',
                scope: this,
                handler: this.doSave
            },
            {
                text: '关闭',
                scope: this,
                handler: this.close
            }]
        });
    },
    initComponent:function(){
    	DeployDetailWin.superclass.initComponent.call(this);
    },
    show:function(value,repId){
    	DeployDetailWin.superclass.show.call(this);
    	Ext.getCmp('detail_sqlId').setValue(value);
    	Ext.Ajax.request({
    		url:root+'/query/RepDesign!queryRepDetail.action',
    		params:{rep_id:repId},
    		scope:this,
    		success:function(res,opt){
    			var data = Ext.decode(res.responseText);
    			if(data){
    				var obj={};
    				for(var key in data){
    					obj["repDefineDto."+key]=data[key];
    				}
    				
    				this.repDetailDto = obj;
    				this.find('name','repDefineDto.sql')[0].setValue(data['sql']);
    				this.find('name','repDefineDto.rep_total_list')[0].setRawValue(data['rep_total_list']);
    			}
    		}
    	});
    },
    doSave:function(){
    	if(this.repDetailDto){
    		this.repDetailDto['repDefineDto.sql'] = this.find('name','repDefineDto.sql')[0].getValue();
    		this.repDetailDto['repDefineDto.rep_total_list'] = this.find('name','repDefineDto.rep_total_list')[0].getValue();
    		
    		this.repDetailDto['repDefineDto.main_sql']=Ext.getCmp('detail_sqlId').getValue();
    		
	    	Ext.Ajax.request({
	    		url:root+'/query/RepDesign!saveRepDetail.action',
	    		params:this.repDetailDto,
	    		scope:this,
	    		success:function(res,opt){
	    			var data = Ext.decode(res.responseText);
	    			this.close();
	    			if(Ext.isEmpty(data)){
	    				Ext.getCmp('detail_id_id').setValue('');
	    			}else{
	    				Ext.getCmp('detail_id_id').setValue(data);
	    			}
	    			
	    			
	    		}
	    	});
    	}
    },
    testSQL: function () {
    	
        this.repDetailDto['repDefineDto.sql'] = this.find('name','repDefineDto.sql')[0].getValue();
        
        if(Ext.isEmpty(this.repDetailDto['repDefineDto.sql']))
        	return;
        Ext.Ajax.request({
            url: root + '/query/RepDesign!testSql.action',
            params: this.repDetailDto,
            scope: this,
            success: function (res, opt) {
                var data = Ext.decode(res.responseText);
                if (Ext.isEmpty(data['exception'])) {
                	Alert('测试成功');
                    var rep_total_list = data['total']; // "合计项"下拉框
                    // 数据
                    if (!Ext.isEmpty(rep_total_list)) {
                        var arr = rep_total_list.split(',');
                        // set 合计项 数据
                        var loadData = [];
                        for (var i = 0; i < arr.length; i++) {
                            loadData.push([arr[i]]); // ArrayStore 数据格式
                        }
                        var hjx = this.find('name','repDefineDto.rep_total_list')[0];
                        hjx.getStore().loadData(loadData);
                        
                        //判断合计项的值，是否需要重新刷新
                    	var hjx_value=hjx.getValue();
                        if(!Ext.isEmpty(hjx_value)){
                        	var hjxvalue_list=hjx_value.split(',');
                        	for(var i=0;i<hjxvalue_list.length;i++){
                        		if(rep_total_list.indexOf(hjxvalue_list[i])<0)
                        			hjx.setRawValue('');
                        	}
                        }
                        
                    }
                } else {
                    Alert(data['exception']);
                }
            }
        });
    }
});


// 配置CUBE window
CubeWindow = Ext.extend(Ext.Window, {
	rep_id: null,
	loadedCounter: 0,
	grid:null,
    constructor: function (rep_id) {
    	
    	this.rep_id = rep_id;
    	
    	this.columnTypeStore = new Ext.data.JsonStore({
			autoLoad: true,
            url: root + '/query/RepDesign!queryCubeDimensionTypes.action',
            fields: ['id', 'name']
        });
        
        this.measuresTypeStore = new Ext.data.JsonStore({
			autoLoad: true,
            url: root + '/query/RepDesign!queryMeasuresDataType.action',
            fields: ['id', 'name']
        });
        this.columnDefineStore = new Ext.data.JsonStore({
    		autoLoad: false,
            fields: ['id', 'name']
        });
        this.columnDefineStore1 = new Ext.data.JsonStore({
    		autoLoad: true,
            url: root + '/query/RepDesign!queryDimensions.action',
            fields: ['id', 'name','mappingKey'],
            baseParams: {rep_id:this.rep_id},
            listeners: {
            	scope: this,
            	load: this.lazyLoadGrid
            }
        });
        this.columnDefineStore2 = new Ext.data.JsonStore({
    		autoLoad: true,
            url: root + '/query/RepDesign!queryMeasures.action',
            fields: ['id', 'name'],
            listeners: {
            	scope: this,
            	load: this.lazyLoadGrid
            }
        });
        
        
        var that = this;
        var sm = new Ext.grid.RowSelectionModel();
    	this.grid = new Ext.grid.EditorGridPanel({
    		clicksToEdit: 1,
    		store: new Ext.data.JsonStore({
    			url: root + '/query/RepDesign!queryCuleDefine.action?rep_id=' + this.rep_id ,
    			fields: [
    				{name: 'column_code'},
    				{name: 'attribute_type'},
    				{name: 'column_type'},
    				{name: 'column_define'},
    				{name: 'column_as'},
    				{name: 'mea_detail_id'},
    				{name:'column_type_check'},
    				{name: 'rep_id'},
    				{name: 'show_control'}
    			]
    		}),
    		sm:sm,
    		columns: [
    			{id: 'field',header: '字段',width:125, sortable: true,dataIndex: 'column_code',renderer:App.qtipValue},
    			{header: '属性类型',width: 85, sortable: true,dataIndex: 'attribute_type'},
    			{header: '列类型',width: 100, sortable: true,dataIndex: 'column_type',
    			 editor: new Ext.form.ComboBox({
				    store: this.columnTypeStore,
                    displayField: 'name',
                    valueField: 'id',
			        typeAhead: true,
			        forceSelection: true,
			        triggerAction: 'all',
			        listWidth:200,
			        selectOnFocus:true
           		 }),
			     renderer: this.editColumnReaderer(that.columnTypeStore)},
			     {header:'类型属性',dataIndex:'column_type_check',width: 80,
			     	renderer:function(v, cellmeta, record, rowIndex, columnIndex, store){
			     		var data = record.data;
			     		var result = '';
			     		if(data.column_type == 'measure'){
			     			if(data.column_type_check == 'T'){
			     				result = '<input type="checkbox" checked="checked" onchange=Ext.getCmp("cubeWindowId").cubeRowConfig(this)> &nbsp;手工</input>';
			     			}else{
			     				result = '<input type="checkbox" onchange=Ext.getCmp("cubeWindowId").cubeRowConfig(this)> &nbsp;手工</input>';
			     			}
			     		}if(data.column_type == 'vertical'){
			     			if(data.column_type_check == 'T'){
				     			result = '<input type="checkbox" checked="checked" onchange=Ext.getCmp("cubeWindowId").verticalFixed(this)>&nbsp;&nbsp;固定</input>';
			     			}else{
			     				result = '<input type="checkbox" onchange=Ext.getCmp("cubeWindowId").verticalFixed(this)>&nbsp;&nbsp;固定</input>';
			     			}
			     		}
			     		return result;
			     	}
			     },
    			{header: '列定义',width: 105, sortable: true,dataIndex: 'column_define',
    			 editor: new Ext.form.ComboBox({
                	store: this.columnDefineStore,
                    displayField: 'name',
                    valueField: 'id',
			        typeAhead: true,
			        forceSelection: true,
			        triggerAction: 'all',
			        selectOnFocus:true,
			        listWidth:200,
			        listeners:{
			        	scope:this,
			        	select:function(combo,cdRecord,index){
			        		var rowRecord = this.grid.selModel.getSelected();
			        		if(rowRecord.get('column_type') == 'crosswise' || rowRecord.get('column_type') == 'vertical' ){
			        			this.columnDefineStore1.each(function(rec){//列定义下拉单选框选择时,对应的列别名设置=Dimension.mappingKey
			        				if(rec.get('id') == cdRecord.get('id')){
			        					rowRecord.set('column_as',rec.get('mappingKey'));
			        				}
			        			},this);
			        		}
			        		
			        	}
			        }
           		 }),
           		 renderer: this.editColumnDefineReaderer(that)},
    			{header: '列别名',width: 75, sortable: true,dataIndex: 'column_as',editor: new Ext.form.TextField({})},
           		 {header: '显示控制',dataIndex:'show_control',width:100,
           		 	editor: new Ext.form.ComboBox({
					    store: this.measuresTypeStore,
	                    displayField: 'name',
	                    valueField: 'id',
				        typeAhead: true,
				        forceSelection: true,
				        triggerAction: 'all',
				        listWidth:200,
				        selectOnFocus:true
	           		 }),
           		 	renderer: this.editShowControlReaderer(that)
           		 },
           		 {header: '明细报表',dataIndex:'mea_detail_id',width:110,renderer:function(v,meta,record){
           		 		var res = ( Ext.isEmpty(v)?'':v )+"&nbsp;";
           		 		if(record.get('column_type') == 'measure'){
           		 			res += "<a href=# onclick=Ext.getCmp('cubeWindowId').cubeConfiguration("+v+")>配置</a>&nbsp;";
           		 		}
           		 		if(!Ext.isEmpty(record.get('mea_detail_id'))){
           		 			res+="<a href=# onclick=Ext.getCmp('cubeWindowId').cubeDel()>删除</a>";
           		 		}
           		 		return res;
           		 	}
           		 }
    		],
    		stripeRows: true,
//       		autoExpandColumn: 'field',
       		listeners: {
       			scope: this,
       			beforeedit: function(e){
       				var t = e.record.get("column_type");
       				if(!t && e.field != "column_type"){
       					e.cancel = true;
       					return;
       				}
       				if(t == 'measure'){ // 度量
       					if(e.field === 'column_define'){
       						that.columnDefineStore.removeAll();
       						that.columnDefineStore.add(that.columnDefineStore2.getRange());
       						e.record.set("column_define", "");
	       				}else if(e.field == 'show_control'){
	       					e.record.set("show_control", "");
	       				}
       				
       				}else{
       					if(e.field === 'column_define'){
       						that.columnDefineStore.removeAll();
       						that.columnDefineStore.add(that.columnDefineStore1.getRange());
       						e.record.set("column_define", "");
       					}
       					if(e.field === 'column_as'){
	       					e.cancel = true;
	       				}else if(e.field == 'show_control'){
	       					e.cancel = true;
	       				}
       				}
       			}	
       		}
    	});
        CubeWindow.superclass.constructor.call(this, {
        	id:'cubeWindowId',
            width: 780,
            height: 400,
            border: false,
            closeAction: 'close',
            title: '配置CUBE',
            layout: 'fit',
            items: [this.grid],
            buttonAlign: 'right',
            buttons: [{
                text: '测试CUBE配置',
                scope: this,
                handler: this.testCube
            },{
            	text: '保存CUBE配置',
            	scope: this,
            	handler: this.saveCube
            }]
        });
    },
    cubeConfiguration: function(mea_detail_id){
    	new CubeDetailWindow( this.rep_id, mea_detail_id ).show();
    },
    qyhtChecked:function(checkBox){
    	var rowRecord = this.grid.selModel.getSelected();
    	var val = checkBox.checked ? 'T' : null;
    	rowRecord.set('show_control',val);
    	if(val !='T'){
    		rowRecord.set('mea_detail_id',null);
    		return;
    	}
    	
    	var options =[];
    	this.grid.store.each(function(rec){
    		if(rec.get('column_type')=='measure'){
	    		options.push({boxLabel: rec.get('column_code'), name: 'dim',inputValue:rec.get('column_code')});
    		}
    	});
    	var fp =new Ext.FormPanel({
    			layout:'form',
    			items:{
		            xtype: 'radiogroup',
		            fieldLabel: '选择维度',
		            itemCls: 'x-check-group-alt',
		            columns: 1,
		            items: options
		        }
    		});
    	var win = new Ext.Window({
    		parent:this,maximizable:false,resizable:false,
    		width:260,height:160,layout:'fit',bodyStyle:'padding-top:10px;',
    		items:fp,
    		buttonAlign:'center',
    		buttons:[
    			{text:'确定',scope:this,handler:function(){
    				var values = fp.getForm().getValues();
    				if(Ext.isEmpty(values.dim)){
    					Alert('请选择一个维度');
    					return false;
    				}
    				rowRecord.set('mea_detail_id',values.dim);
    				win.close();
    			}}
    		]
    	});
    	win.show();
    },
    verticalFixed:function(checkBox){
    	var rowRecord = this.grid.selModel.getSelected();
    	var val = checkBox.checked ? 'T' : null;
    	rowRecord.set('column_type_check',val);
    },
    cubeRowConfig:function(checkBox){
    	
    	var rowRecord = this.grid.selModel.getSelected();
    	var val = checkBox.checked ? 'T' : null;
    	
		this.grid.store.each(function(rec){
    		if(rec.get('column_type')=='measure'  /* && rowRecord.id != rec.id */  ){
    			rec.set('column_type_check',null);
    		}
    	});
    		
    	if(val !='T'){
    		return;
    	}
    	rowRecord.set('column_type_check','T');
    	
    	Ext.Ajax.request({
			url : root + '/query/RepDesign!queryCustomRowTypes.action',
			timeout : 9999999999,scope:this,
			params : {rep_id : this.rep_id},
			success : function(res, opt) {
				var dimsData = Ext.decode(res.responseText);
				if(!this.cubeRowCfgWin){
			    	this.cubeRowCfgWin =new com.yc.CubeRowCfgWin(this.rep_id,dimsData);
		    	}
				this.cubeRowCfgWin.show();
				this.cubeRowCfgWin.resetData(this.rep_id,dimsData,this);
			},
			failure : function() {
				Alert('加载数据类型出错');
			}
		});
    },
    cubeDel: function(){
    	Confirm("确认删除吗?",this,function(){
	    	var record = this.grid.getSelectionModel().getSelected();
	    	record.set('mea_detail_id',null);
	    	this.saveCube();
    	});
    },
    lazyLoadGrid: function(){
    	this.loadedCounter ++ ;
    	if(this.loadedCounter == 2){
    		this.grid.getStore().load();
    	}
    },
    testCube: function(){
    	if(true === this.valid()){
    		var vs = this.getValues();
    		var cubeString = Ext.encode({
    			"repcube": vs
    		});
    		Ext.Ajax.request({
	    		url: root + "/query/RepDesign!validateCube.action",
	    		params: {
	    			"rep_id": this.rep_id,
	    			"jsonParams": cubeString
	    		},
	    		success: function(res, ops){
	    			var rv = res.responseText;
	    			if(rv === 'null' || rv === ""){
	    				Alert("验证成功!");
	    			}else{
	    				Alert(rv);
	    			}
	    		}
	    	});
    	}
    },
    saveCube: function(){
    	this.focus();
    	if(true === this.valid()){
    		var vs = this.getValues();
    		var cubeString = Ext.encode({
    			"repcube": vs
    		});
    		Ext.Ajax.request({
	    		url: root + "/query/RepDesign!saveCuleDefine.action",
	    		params: {
	    			"rep_id": this.rep_id,
	    			"jsonParams": cubeString
	    		},
	    		success: function(res, ops){
	    			Alert("保存成功!");
	    		}
	    	});
    	}
    },
    getValues: function(){
    	var store = this.grid.getStore();
    	var data = [];
    	store.each(function(r){
    		data.push({
    			rep_id: this.rep_id,
    			column_code: r.get("column_code"),
    			column_type: r.get("column_type"),
    			column_define: r.get("column_define"),
    			column_as: r.get("column_as"),
    			show_control: r.get("show_control"),
    			mea_detail_id: r.get("mea_detail_id")
    		});
    	}, this);
    	return data;
    },
    valid: function(){
    	var store = this.grid.getStore();
    	var verticalCount = 0;
    	for(var i = 0; i < store.getCount(); i++){
    		var r = store.getAt(i);
    		var t = r.get("column_type");
    		if(!r.get("column_define")){
    			Alert(String.format("请选择列定义，行{0}", i));
    			return false;
    		}
    		if(t){
    			if(t === "measure"){
    				if(!r.get("column_as")){
	    				Alert(String.format("列类型为度量时，请选择列别名! 行{0}", i));
	    				return false;
    				}
    				if(!r.get("show_control")){
	    				Alert(String.format("列类型为度量时，请选择显示控制! 行{0}", i));
	    				return false;
    				}
    			}
    			if(t === 'vertical'){
	    			verticalCount ++;
	    		}
    		}else{
    			Alert(String.format("请选择列类型! 行{0}", i));
    			return false;
    		}
    	}
    	if(verticalCount > 1){
    		Alert("当前版本只支持最多一个纵向维! 已选择["+ verticalCount +"]个纵向维!");
    		return false;
    	}
    	return true;
    },
    editColumnReaderer: function(store){
    	return function(v){
    		var rowIndex = store.find("id", v);
	     	if(rowIndex >= 0){
	     		return store.getAt(rowIndex).get("name");
	     	}
	     	return "";
    	} 
    },
    editColumnDefineReaderer: function(that){
    	return function(v, m, r, ri, ci, store){
    		if(!v){return ""; }
	    	var _store = ( r.get("column_type") === 'measure' ? that.columnDefineStore2 : that.columnDefineStore1);
	    	var rowIndex = _store.find("id", v);
	    	if(r.get("column_type") == 'fdsfds'){
	    		
	    	}
	   		if(rowIndex >= 0){
	     		return _store.getAt(rowIndex).get("name");
	     	}
	     	return "";
    	}
    },
    editShowControlReaderer: function(that){
    	return function(value, cellmeta, record, rowIndex, columnIndex, store){
    		var data = record.data;
 			var checked = data.column_type_check =='T' ? 'checked' :'';//是否选中
 			if(data.column_type == 'vertical'){
 				if(data.show_control == 'T'){
	     			result = '<input type="checkbox" checked="checked" onchange=Ext.getCmp("cubeWindowId").qyhtChecked(this)> &nbsp;启用环同</input>';
     			}else{
     				result = '<input type="checkbox" onchange=Ext.getCmp("cubeWindowId").qyhtChecked(this)> &nbsp;启用环同</input>';
     			}
 				return result;
 			}
    		if(!value){return ""; };
	    	var rowIndex = that.measuresTypeStore.find("id", value);
	   		if(rowIndex >= 0){
	     		return that.measuresTypeStore.getAt(rowIndex).get("name");
	     	}
	     	return "";
    	}
    }
});

var CubeDetailWindow = Ext.extend(Ext.Window,{
	rep_id: null,
	mea_detail_id: null,
	constructor: function( rep_id,mea_detail_id ){
		this.rep_id = rep_id;
		this.mea_detail_id = mea_detail_id;
		this.grid = new Ext.grid.GridPanel({
			region:'north',
			height:105,
			clicksToEdit: 1,
    		store: new Ext.data.JsonStore({
    			url: root + '/query/RepDesign!queryCubeDimensions.action',
    			fields: ['prefixid','name','mappingKey']
    		}),
    		columns: [
    			{header: '维度ID',width:150,dataIndex: 'prefixid'},
    			{header: '维度名称',width: 150, sortable: true,dataIndex: 'name'},
    			{header: '维度键值',width: 150, sortable: true,dataIndex: 'mappingKey'}
    		]
		});
		this.grid.getStore().load({params:{rep_id: this.rep_id}});
		
		this.form = new Ext.form.FormPanel({
			region:'center',
			labelWidth:65,
			labelAlign:'right',
			bodyStyle: 'padding-top:10px',
			layout:'column',
			defaults : {
				layout:'form',
				border:false
			},
			items:[
				{xtype:'hidden',name:'rep_id'},
				{xtype:'hidden',name:'rep_name'},
				{xtype:'hidden',name:'rep_type'},
				{xtype:'hidden',name:'database'},
				{xtype:'hidden',name:'rep_info'},
				{xtype:'hidden',name:'quiee_raq'},
				{xtype:'hidden',name:'detail_id'},
				{xtype:'hidden',name:'remark'},
				{xtype:'hidden',name:'res_pid'},
				{xtype:'hidden',name:'rep_group'},
				{xtype:'hidden',name:'mea_detail_id'},
				
				{columnWidth:1,items:[
					{xtype:'textarea',readOnly:true,fieldLabel:'cube',name:'main_sql',anchor:'95%',height:80,allowBlank:false}
				]},
				{columnWidth:1,items:[
					{xtype:'textarea',fieldLabel:'明细sql',name:'sql',anchor:'95%',height:200,allowBlank:false}
				]},
				{columnWidth:.2,border:false,bodyStyle: 'padding:5px 0 0 30px',items:[
					{xtype:'button',text:'测试SQL',scope:this,handler:this.testSql}
				]},
				{columnWidth:.8,border:false,labelWidth : 10,
					defaults:{bodyStyle: 'padding-top:5px'},items:[
					{xtype: 'itemselector',
                    name: 'rep_total_list',
                    border:false,
                    drawUpIcon:false,
				    drawDownIcon:false,
				    drawLeftIcon:true,
				    drawRightIcon:true,
				    drawTopIcon:false,
				    drawBotIcon:false,
                    imagePath: '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage',
                    multiselects: [{
                        legend: '可选',
                        width: 140,
                		height: 100,
                        store: new Ext.data.JsonStore({
                            fields: ['rep_column']
                        }),
                        displayField: 'rep_column',
                        valueField: 'rep_column',
                        tbar: [{
                            xtype: 'textfield',
                            emptyText: '过滤..',
                            enableKeyEvents: true,
                            listeners: {
                                scope: this,
                                keyup: function (txt, e) {
                                    if (e.getKey() == Ext.EventObject.ENTER) {
                                        var value = txt.getValue();
                                        Ext.getCmp('selectResId').multiselects[0].store.filterBy(function (record) {
                                            if (Ext.isEmpty(value)) return true;
                                            else return record.get('rep_column').indexOf(value) >= 0;
                                        }, this);
                                    }
                                }
                            }
                        }]
                    }, {
                        legend: '已选',
                        width: 140,
                		height: 100,
                        store: new Ext.data.JsonStore({
                            fields: ['rep_column']
                        }),
                        displayField: 'rep_column',
                        valueField: 'rep_column'
                    }]
                }
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:'确定',scope:this,handler:this.doSave},
				{text:'取消',scope:this,handler:this.doCancel}
			]
		});
		
		CubeDetailWindow.superclass.constructor.call(this,{
			id: 'cubeDetailWindowId',
			border:false,
			width:500,
			height:600,
			closeAction:'close',
			title: 'CUBE明细报表配置',
			layout:'border',
			items: [this.grid,this.form]
		});
		
		this.doInit();
	},
	doInit: function(){
		Ext.Ajax.request({
			url: root + '/query/RepDesign!queryCubeDetail.action',
			params:{
				rep_id: this.rep_id,
				mea_detail_id: this.mea_detail_id
			},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				
				if(data){
					var form = this.form.getForm();
					form.setValues(data);
					
					var rep_total_list = data.rep_total_list;
					if(rep_total_list){
						var arr = data.rep_total_list.split(',');
						for(var i=0,len=arr.length;i<len;i++){
							arr.push({'rep_column':arr[i]});
						}
						form.findField('rep_total_list').multiselects[1].store.loadData(arr);
					}
				}
			}
		});
	},
	getFormValues:function(){
		var form = this.form.getForm();
		if(form.isValid()){
			var values = form.getValues();
			var store = form.findField('rep_total_list').multiselects[1].store;
			var str = '';
			store.each(function(record){
				var v = record.get('rep_column');
				if(!Ext.isEmpty(v)){
					str += v+",";
				}
			});
			values['rep_total_list'] = str.substring(0,str.length-1);
			return values;
		}else{
			return null;
		}
		
	},
	testSql: function(){
		var values = this.getFormValues();
		if(!values)return null;
		var obj={};
		obj['repdefinedto'] = values;
		Ext.Ajax.request({
			url: root + '/query/RepDesign!testCubeDetail.action',
			params:{
				'rep_id': this.rep_id,
				'jsonParams': Ext.encode(obj)
			},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.success === true && !Ext.isEmpty(data.simpleObj) ){
					
					var returnArr = data.simpleObj.split(',');	//查询返回数据
					
					var itemSel = this.form.getForm().findField('rep_total_list');
					var optionStore = itemSel.multiselects[0].store,optionArr=[];	//可选框store
					var hasStore = itemSel.multiselects[1].store,hasArr = [];		//已选框store
					var flag = false;
					
					//若已选数据有数据不包含在返回数据中，则清空已选数据，将数据加载到可选数据框中
					//若返回数据包含已选数据，不包含的加载到可选数据框中
					hasStore.each(function(record){
						var v = record.get('rep_column');
						if(!Ext.isEmpty(v)){
							hasArr.push(v);
							if( returnArr.indexOf( record.get('rep_column') ) == -1 ){
								flag = true;
								return false;
							}
						}
					});
					
					var buildData = function(arr){
						var dataArr = [];
						for(var i=0,len=arr.length;i<len;i++){
							dataArr.push({'rep_column':returnArr[i]});
						}
						return dataArr;
					}
					
					//有不相同的数据，清空已选数据框，加载可选数据框
					if(flag){
						hasStore.removeAll();
						optionStore.loadData( buildData(returnArr) );
					}else{
						//将返回数据中不在已选数据框中的数据，加载到可选数据框
						for(var i=0,len=hasArr.length;i<len;i++){
							returnArr.remove(hasArr[i]);
						}
						if(returnArr.length > 0){
							optionStore.loadData( buildData(returnArr) );
						}
					}
				}
			}
		});
	},
	doSave: function(){
		var values = this.getFormValues();
		if(!values)return null;
		var obj={};
		obj['repdefinedto'] = values;
		Ext.Ajax.request({
			url: root + '/query/RepDesign!saveCubeDetail.action',
			params:{
				'rep_id': this.rep_id,
				'jsonParams': Ext.encode(obj)
			},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data){
					var win = Ext.getCmp('cubeWindowId');
					var record = win.grid.getSelectionModel().getSelected();
					if(Ext.isEmpty(record.get('mea_detail_id'))){
						record.set('mea_detail_id',data);
						record.commit();
						win.saveCube();
					}
					Alert('保存成功!',function(){
						this.doCancel();
					},this);
				}
			}
		});
	},
	doCancel: function(){
		this.close();
	}
});


// 上传window
var UploadWin = Ext.extend(Ext.Window, {
    form: null,
    constructor: function () {
        this.form = new Ext.form.FormPanel({
            id: 'uploadFormId',
            border: false,
            labelWidth: 70,
            fileUpload: true,
            trackResetOnLoad: true,
            bodyStyle: 'padding-top:10px',
            defaults: {
                baseCls: 'x-plain'
            },
            items: [{
                id: 'uploadQuieeRaqsId',
                xtype: 'textfield',
                fieldLabel: '上传文件',
                name: 'uploadQuieeRaqs',
                inputType: 'file',
                anchor: '95%',
                emptyText: ''
            }]
        });
        UploadWin.superclass.constructor.call(this, {
            id: 'uploadWinId',
            width: 300,
            height: 100,
            border: false,
            closeAction: 'hide',
            title: '上传',
            items: [this.form],
            buttonAlign: 'right',
            buttons: [{
                text: '保存',
                scope: this,
                handler: this.doSave
            },
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.fireEvent('hide'); // 触发hide事件
                }
            }],
            listeners: {
                scope: this,
                hide: function () {
                    Ext.getCmp('uploadQuieeRaqsId').getEl().dom.select();
                    document.execCommand("delete"); // 删除选中区域
                    this.hide();
                }
            }
        });
    },
    doSave: function () {
        var name = Ext.getCmp('uploadQuieeRaqsId').getValue();
        name = name.substring(name.lastIndexOf('\\') + 1); // 获取上传文件名

        this.form.getForm().submit({
            url: root + '/query/RepDesign!uploadQuieeRaq.action',
            params: {
                uploadQuieeNames: name
            },
            waitTitle: '提示',
            waitMsg: '正在上传中,请稍后...',
            scope: this,
            success: function (form, action) {
                var data = action.result;
                if (data.success === true) Alert('上传成功', function () {
                    this.hide();
                    Ext.getCmp('quiee_raq_id').setText(data.msg);
                }, this);
            }
        });
    }
});

// 提交时显示"备注"window
var RemarkWin = Ext.extend(Ext.Window, {
    repDefineDtoValues: null,
    constructor: function () {
        RemarkWin.superclass.constructor.call(this, {
            id: 'remarkWinId',
            border: false,
            width: 400,
            heigth: 350,
            items: [{
                layout: 'form',
                defaults: {
                    baseCls: 'x-plain'
                },
                labelWidth: 60,
                items: [{
                	id:'remarkWinareaId',
                    name: 'remark',
                    fieldLabel: '备注',
                    xtype: 'textarea',
                    //allowBlank: false,
                    width: 280,
                    height: 200
                }]
            }],
            buttonAlign: 'center',
            buttons: [{
                text: '保存',
                scope: this,
                handler: this.doSave
            },
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.hide();
                }
            }],
            listeners: {
                scope: this,
                hide: function () {
                    this.find('name', 'remark')[0].reset();
                }
            }
        });
    },
    show: function (values) {
        RemarkWin.superclass.show.call(this);
        this.repDefineDtoValues = values;
        this.find('name', 'remark')[0].setValue(this.repDefineDtoValues['repDefineDto.remark']);
    },
    doSave: function () {
        var remark = this.find('name', 'remark')[0].getValue();
        //if (Ext.isEmpty(remark)) return;
        this.repDefineDtoValues['repDefineDto.remark'] = remark;
        Ext.getCmp('deployReportFormId').rep_remark=remark;
        doReportSave(this.repDefineDtoValues,Ext.getCmp('rep_id_id'));
        this.close();
    }
});

// 内存值用户列转换window
var MemoryKeyWin = Ext.extend(Ext.Window, {
    store: null,
    grid: null,
    constructor: function () {
        this.store = new Ext.data.JsonStore({
            url: root + '/query/RepDesign!queryAllMemoryKeys.action',
            fields: ['memory_key', 'memory_desc', 'memory_type', 'value_key', 'database', 'remark']
        });
        this.store.load();
        var columns = [{
            header: '描述',
            dataIndex: 'memory_desc',
            width: 110,
            sortable: true
        },
        {
            header: '列转换key',
            dataIndex: 'memory_key',
            width: 120,
            sortable: true,
            renderer:App.qtipValue
        },
        {
            header: '取值类型',
            dataIndex: 'memory_type',
            width: 85,
            sortable: true
        },
        {
            header: '取值键值',
            dataIndex: 'value_key',
            width: 130,
            sortable: true,
            renderer:App.qtipValue
        },
        {
            header: '数据源',
            dataIndex: 'database',
            width: 55,
            sortable: true
        },
        {
            header: '备注',
            dataIndex: 'remark',
            width: 75,
            sortable: true,
            renderer:App.qtipValue
        }];
        this.grid = new Ext.grid.GridPanel({
            ds: this.store,
            columns: columns,
            border: false,
            baseCls: 'x-plain',
            autoScroll: true
        });
        CondCodeWin.superclass.constructor.call(this, {
            id: 'MemoryKeyWinId',
            title: '列转换关键字',
            layout: 'fit',
            modal: false,
            width: 620,
            height: 450,
            closeAction: 'hide',
            items: [this.grid]
        })
    }
});
// 条件代码window
var CondCodeWin = Ext.extend(Ext.Window, {
    store: null,
    grid: null,
    constructor: function () {
        this.store = new Ext.data.JsonStore({
            url: root + '/query/RepDesign!queryAllKeyCon.action',
            fields: ['key', 'name', 'type', 'htmlcode', 'htmlorder', 'database']
        });
        this.store.load();
        var columns = [{
            header: '描述',
            dataIndex: 'name',
            width: 120,
            sortable: true,
            renderer:App.qtipValue
        },
        {
            header: '组合条件',
            dataIndex: 'key',
            width: 120,
            sortable: true,
            renderer:App.qtipValue
        },
        {
            header: 'HTML组件',
            dataIndex: 'htmlcode',
            width: 130,
            sortable: true
        },
        {
            header: '类型',
            dataIndex: 'type',
            width: 65,
            sortable: true
        },
        {
            header: '加载顺序',
            dataIndex: 'htmlorder',
            width: 60,
            sortable: true
        },
        {
            header: '数据库',
            dataIndex: 'database',
            width: 60,
            sortable: true
        }];
        this.grid = new Ext.grid.GridPanel({
            ds: this.store,
            columns: columns,
            border: false,
            baseCls: 'x-plain',
            autoScroll: true
        });
        CondCodeWin.superclass.constructor.call(this, {
            id: 'condCodeWinId',
            title: '查询条件',
            layout: 'fit',
            modal: false,
            width: 600,
            height: 450,
            closeAction: 'hide',
            items: [this.grid]
        })
    }
});

/**
 * 配置报表保存
 * 
 * @param {}
 *            values
 * @param {}
 *            repIdComp
 */
var doReportSave = function (values, repIdComp, callback) {
    var mask = Show();
    Ext.Ajax.request({
        url: root + '/query/RepDesign!saveRepDesign.action',
        waitTitle: '提示',
        waitMsg: '正在保存中,请稍后...',
        params: values,
        scope: this,
        success: function (res, opt) {
            mask.hide();
            mask = null;
            var data = Ext.decode(res.responseText);
            if (Ext.isEmpty(data['exception'])) {
                var repId = data['simpleObj'];
                if (repIdComp) repIdComp.setValue(repId);
               
               if(callback){
               		callback.call(null, repId);
               }else{
	               	Confirm("是否打开查询界面测试",this,function(){
               			if(Ext.getCmp(repId+'quieetab'))
							App.getApp().page.remove(repId+'quieetab');					
						if(Ext.getCmp(repId+'MainPanel'))
							App.getApp().page.remove(repId+'MainPanel');							
						var repName=Ext.getCmp('rep_name_name').getValue();
						App.page.add({
							id : repId + 'MainPanel',
							title : repName,
							layout: 'fit',
							closable: true,
							items: new MainPanel( repId , repName)
						});
						App.page.activate(repId+'MainPanel');				
	                });
               }
            } else {
                Alert(data['exception']);
            }
        }
    });
}
/**
 * 角色关系树
 */
var RoleTree = Ext.extend(Ext.ux.FilterTreePanel, {
    searchFieldWidth: 140,
    constructor: function (v) {
        var loader = new Ext.tree.TreeLoader({
            url: root + "/query/RepDesign!getResourceRepRole.action?rep_id=" + v
        });
        RoleTree.superclass.constructor.call(this, {
            region: 'west',
            width: 210,
            split: true,
            minSize: 210,
            maxSize: 260,
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '0',
                iconCls: 'x-tree-root-icon',
                nodeType: 'async',
                text: '系统角色'
            }
        });
        this.getRootNode().expand(true);
    },
    listeners: { //!!!
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
/**
 * 角色配置窗口
 */
var RoleWindow = Ext.extend(Ext.Window, {
    roleTree: null,
    constructor: function (v) {
        this.roleTree = new RoleTree(v), RoleWindow.superclass.constructor.call(this, {
            id: v + "rolewin",
            title: '菜单资源分配给系统角色',
            layout: 'fit',
            width: 400,
            height: 450,
            border: false,
            closeAction: 'hide',
            //close就是把此window destroy    ,hide是把此window隐藏
            items: this.roleTree,
            buttons: [{
                text: '保存',
                scope: this,
                handler: function () {
                    this.save(v);
                }
            },
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.close();
                }
            }]
        });
    },
    save: function (v) {
        var all = {
            rep_id: v
        };
        var roleId = [];
        var nodes = this.roleTree.getChecked();
        for (var i in nodes) {
            if (nodes[i].leaf) { //
                roleId.push(nodes[i].id);
            }
        }
        if (roleId.length == 0) {
            all["clear"] = true;
        } else {
            all["roleIds"] = roleId;
        }
        Ext.Ajax.request({
            url: root + '/query/RepDesign!saveResource2Role.action',
            params: all,
            success: function (res, ops) {
                var rs = Ext.decode(res.responseText);
                if (true === rs.success) {
                    Alert('操作成功!');
                } else {
                    Alert('操作失败!');
                }
            }
        });
    }
});