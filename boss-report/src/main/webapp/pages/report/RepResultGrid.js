App.pageSize = 100 ;

/**
 * 明细查询后台回传结果集
 * @class RepResultGrid
 * @extends Ext.grid.GridPanel
 */
RepResultGrid =Ext.extend(Ext.grid.GridPanel,{ 
	constructor:function(headers,query_id,rep_id,rep_info){
		var fields = [], columns=[], rows = [];
			
     	for(i=0;i<headers.length;i++){
     		if(i==headers.length - 1){
     			var j=0;
     			Ext.each(headers[i],function(h){       				 	
					columns.push({header:h.col_desc, dataIndex:j});
					fields.push({name : j,mapping : j});
					j++;
				});
				//合计列标记列
				fields.push({name:'issumrow_report',mapping:j});
     		}else{
     			var row = [];
     			Ext.each(headers[i],function(h){  
	     				 		row.push({header: h.col_desc,align: 'center',colspan: h.col_length});
				});
				rows.push(row);
     		}     					
		}
		
		var group = new Ext.ux.grid.ColumnHeaderGroup({
			rows: rows
		});
		// 报表信息查询
		var QueryStore = new Ext.data.JsonStore({ 
			url  : root + '/query/Show!queryReport.action' ,
			root : 'records' ,
			totalProperty: 'totalProperty',
			autoDestroy: true,
			baseParams: {limit: App.pageSize,start: 0 ,query_id : query_id },
			fields: fields
		}); 
		QueryStore.load({
			params : {query_id : query_id}
		});
		var back_sn={text:'返回查询界面',scope:this,handler:this.returnQueryPanel};
		var design_download_sn={text:'定制下载',scope:this,handler:function(){
						//打开一个对话框配置 参数  rep_id
						  	var win = Ext.getCmp('colExportWinId');
		            		if (!win) win = new ColExportWin();
		           			win.show(query_id);
				  	}};
		var download_sn={text:'下载',scope:this,handler:function(){
					  	    var mask = Show();//进度条
					  		Ext.Ajax.request({
					  			//scope : this,
								timeout:9999999999,
						    	url:root+"/query/Show!createExp.action",
						    	params:{query_id:query_id},
						    	success:function(res){
						    		mask.hide();
									mask=null;
									AlertReport("&nbsp&nbsp&nbsp&nbsp&nbsp<a href="+root+"/query/Show!downloadExp.action?query_id="
													+query_id+"&rep_id="+rep_id+" >点击下载</a>");
						    	}
						    });
					    }};
		var items = [];
		items.push(back_sn);
		items.push('-');
		items.push(design_download_sn);
		items.push('-');
		items.push(download_sn);
		
		if(rep_info==='osdsql'){
			//创建osdsql模板
			var createOsdsql_sn={text:'OSD模板',scope:this,handler:function(btn){			                    
									var win=Ext.getCmp('odssqltitlewin');
									if(!win) win=new OsdSqlTitleWin();
									win.show(query_id);
								}};
			items.push('-');
			items.push(createOsdsql_sn);
		}
		
		var showsql_sn={text:'查看SQL',scope:this,handler:function(){
				Ext.Ajax.request({
					url : root + '/query/RepDesign!showSql.action',
					params : {query_id:query_id},
					scope : this,
					success : function(res, opt) {
						var repdto=Ext.decode(res.responseText);
						var win = Ext.getCmp('oneRemarkWinId');
						if( !win)
							win=new OneRemarkWin();
						win.show(repdto);
					}
				});
			}};
		items.push('->');
		items.push(showsql_sn);
		items.push(' ');
		items.push(' ');
		
		RepResultGrid.superclass.constructor.call(this,{
			store:QueryStore,
			columns:columns,
			//plugins: group,

			stripeRows: true,             
			tbar:items,
			border: false,
			bbar: new Ext.PagingToolbar({store: QueryStore ,pageSize:App.pageSize})// ,//分页信息
		});
	},
	returnQueryPanel:function(){
		this.ownerCt.remove(this);
	}
});

// 列定制window
var ColExportWin = Ext.extend(Ext.Window, {
    query_id: null,
    constructor: function () {
        ColExportWin.superclass.constructor.call(this, {
            id: 'colExportWinId',
            border: false,
            title: '定制下载',
            width: 380,
            heigth: 550,
            items: [{layout: 'form',
                bodyStyle: 'padding-top:5px',
                border: false,
                labelWidth : 10,
                 items: [{		xtype : 'itemselector',
								name : 'itemselector',
								id : 'colExportitem_id',
								fieldLabel : '',
								allowBlank : false,
								imagePath : '/' + Constant.ROOT_PATH_LOGIN
										+ '/resources/images/itemselectorImage',
								multiselects : [{
											legend : '待选',
											width : 150,
											height : 270,
											store : new Ext.data.ArrayStore({
														fields : ['col_idx',
																'col_name']
													}),
											displayField : 'col_name',
											valueField : 'col_idx'
											,tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
												listeners:{
													scope:this,
													keyup:function(txt,e){
														if(e.getKey() == Ext.EventObject.ENTER){
															var value = txt.getValue();
																Ext.getCmp('colExportitem_id').multiselects[0].store.filterBy(function(record){
																	if(Ext.isEmpty(value))
																		return true;
																	else
																		return record.get('col_name').indexOf(value)>=0;
																},this);
														}
													}
												}
											}]
										}, {
											legend : '已选',
											width : 150,
											height : 270,
											store : new Ext.data.ArrayStore({
														fields : ['col_idx',
																'col_name']
													}),
											displayField : 'col_name',
											valueField : 'col_idx'
										}]
								   		
							}]}],
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
            }]
        });
    },
    show: function (query_id) {
        RemarkWin.superclass.show.call(this);
        this.query_id = query_id;
        this.loadData();
    },
    doSave: function () {
    	var store=Ext.getCmp('colExportitem_id').multiselects[1].store;
        var arr = [];
		store.each(function(record) {
					arr.push(record.get('col_idx'));
				});

	    Ext.Ajax.request({
	    	url:root+"/query/RepDesign!saveColExeport.action",
	    	params:{cols:arr,query_id:this.query_id},
	    	success:function(res){
	    	}
	    });
        this.close();
    },
    loadData:function(){
    			Ext.Ajax.request({
			url : root + '/query/RepDesign!getColExport.action',
			params : {
				query_id : this.query_id
			},
			scope : this,
			success : function(res, opt) {
				var data = Ext.decode(res.responseText);
				if(data){
					var arrs1=[];
					var arrs2=[]
					for (var k = 0; k < data.length; k++) {
						
						if(data[k].ischeck)
							arrs2.push([data[k].col_idx, data[k].col_name]);
						else
							arrs1.push([data[k].col_idx, data[k].col_name]);
					}
					if(arrs1){
					
						Ext.getCmp('colExportitem_id').multiselects[0].store.loadData(arrs1);
						//alert(Ext.decode(Ext.getCmp('colExportitem_id').multiselects[0].store));
					}
					if(arrs2){
						Ext.getCmp('colExportitem_id').multiselects[1].store.loadData(arrs2);
					}
				}
			}
		});
    }
});

var OsdSqlTitleWin=Ext.extend(Ext.Window, {
	query_id:null,
	constructor : function(){
		OsdSqlTitleWin.superclass.constructor.call(this, {
			id:'odssqltitlewin',
			border : false,
			title:'OSD催缴模板标题定制',
			width : 410,
			height : 310,
			items:[{
                layout: 'form',
                defaults: {
                    baseCls: 'x-plain'
                },
                labelWidth: 1,
                items: [{
                    name: 'title',
                    fieldLabel: '',
                    xtype: 'textarea',
                    allowBlank: false,
                    readOnly:false,
                    width: 380,
                    height: 230
                }]
            }],
			buttonAlign : 'center',
			buttons : [ {
                text: '保存',
                scope: this,
                handler: this.doSave
            },{text : '关闭',scope : this,handler : function() {this.hide();}}
			]
		});
	},
	show:function(query_id){
		OsdSqlTitleWin.superclass.show.call(this);
		this.query_id=query_id;
		Ext.Ajax.request({
	    	url:root+"/query/RepDesign!queryOsdsqltitle.action",
	    	params:{query_id:this.query_id},
	    	scope:this,
	    	success:function(res){
	    		var data = Ext.decode(res.responseText);
	    		if(!Ext.isEmpty(data)){
	    			this.find('name', 'title')[0].setValue(data);
	    		}
	    	}
	    });
	},
	doSave:function(){
		var remark = this.find('name', 'title')[0].getValue();
		if(Ext.isEmpty(remark)) return;
		
		Ext.Ajax.request({
	    	url:root+"/query/RepDesign!createOsdsql.action",
	    	params:{query_id:this.query_id,title:remark},
	    	success:function(res){
	    		Ext.MessageBox.alert("提示框","创建OSD模板成功!");
	    	}
	    });
        this.close();
	}
	});