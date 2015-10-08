BulletinGrid = Ext.extend(Ext.grid.GridPanel,{
	width:710,height:500, trackMouseOver:false,/*disableSelection:false,*/
	border:false,
	store:null,orginalData:[],loadMask: true,orginalDataMap:{},checkedCount:0,
	listeners:{
		scope:this,
		afterrender:function(grid){
			var dom = grid.el.dom;
			var head = Ext.DomQuery.select('.x-grid3-header',dom);
			if(head.length ==1){
				head[0].style.display='none';
			}
		},
		rowclick:function(grid,rowNum){
			var view = grid.getView();
			view.ignoreToolBarStatus = false;
			var rec = grid.store.getAt(rowNum);
			if(rec.get('isChecked') == true ){//mantis 0001394: 登陆弹出的公告修正  如果没有点击 确认 按钮，不允许收缩成标题
				rec.set('shouldExpand',!rec.get('shouldExpand'));
	            view.refresh();
			}
		}
	},
	loadData:function(datas){
		this.orginalData.length =0;
		this.orginalDataMap ={};
		for(var index = 0;index<datas.length;index++){
			var data = datas[index];
			var item = this.orginalDataMap[parseInt(data.bulletin_id)];
			if(item){
				continue;
			}
			data.shouldExpand = true;
			this.orginalDataMap[parseInt(data.bulletin_id)] = data;
			this.orginalData.push(data);
		}
		this.checkedCount = 0;
		this.store.loadData(this.orginalData);
	},
	constructor:function(cfg){
		Ext.apply(this,cfg||{});
		this.store = new Ext.data.JsonStore({
	        idProperty: 'bulletin_id',
	        fields: [
		        'bulletin_id', 'bulletin_title', 'bulletin_content','create_date','eff_date','exp_date',
				'bulletin_publisher', 'status', 'optr_id','county_id','isChecked','shouldExpand'
	        ]
	    });
	    this.loadData(this.orginalData||[]);
	    this.store.setDefaultSort('bulletin_id', 'desc');
		BulletinGrid.superclass.constructor.call(this,{
			store: this.store,
			columns:[{
	            id: 'bulletin_id',header: "标题",
	            dataIndex: 'bulletin_title',width: 690,
	            renderer: this.renderTopic,sortable: true
	        }],
	        viewConfig: {
	            forceFit:true,
	            enableRowBody:true,
	            showPreview:true,
	            getRowClass : function(record, rowIndex, p, store){
	                if(!this.showPreview && this.ignoreToolBarStatus){//全部隐藏的时候点击单行
		                return 'x-grid3-row-collapsed';
	                }else{//显示
	                	if(record.get('shouldExpand') != true){
	                		return 'x-grid3-row-collapsed';
	                	}
	                	p.body = '<div><div style="font-size:14px;font-family:KaiTi;color:#337FE5;">'
	                    + '&nbsp&nbsp' +record.data.bulletin_content + '</br></div>'
	                    + '<div style="width:' + (BulletinGrid.prototype.width -20) + ';height:1;"/>'
	                    + '<div style="text-align:right;float:right;"> ' + 
	                    String.format('{0}发布于 {1}', record.get('bulletin_publisher'),record.get('create_date')) + '&nbsp&nbsp';
	                    if(record.get('isChecked') != true ){
	                    	p.body += '&nbsp&nbsp&nbsp&nbsp <button onclick="App.getApp().bulletinToViewWin.grid.checkBulletin(' + record.data.bulletin_id + ',this)" class="x-btn-text" style="width:55;">确认</button>&nbsp&nbsp ';
	                    }
	                    p.body += '</div></div>';
	                    return 'x-grid3-row-expanded';
	                }
	            }
	        },
	        checkBulletin:function(bid,btn){//bid,flag,rowIndex
                	btn.style.display='none';
		        	Ext.Ajax.request({
						url : root+ '/system/x/Index!checkBulletin.action',
						params : {bulletin_id : bid},
						scope:this,
						success : function() {
								var record = App.bulletinToViewWin.grid.selModel.getSelected();
								record.set('isChecked',true);
								this.checkedCount+=1;
								if(this.checkedCount == this.orginalData.length){
						        	var win = App.bulletinToViewWin;
						        	App.bulletinToViewWin.showStatus = -1;
						        	win.hide();
						        	Alert('<div style="color:red">您已查看所有未读的公告,如需重新查看,请点击顶部公告菜单！</div>');
					        	}
							}
					});
		        }
		});
	},
	renderTopic :function(value, p, record){
        return String.format( '<strong><font size="5">{0}</font></strong>', record.data.bulletin_title);//<font size="4px">
//        return String.format( '<div><a href="#" >{0}</a>', record.data.bulletin_title);
    },
    renderLast:function(value, p, r){
        return String.format('{0}<br/>发布于 {1}', r.get('bulletin_publisher'),r.get('create_date'));
    }
});

function showBulletinWhenInit() {
//	if(!App.bulletinToViewWin){
		var grid = new BulletinGrid({orginalData:[]});
		App.bulletinToViewWin = new Ext.Window({
			showStatus:0,//是否已经被隐藏 0,初始化, 1,显示  ,-1 隐藏
			grid:grid,
			width:grid.width + 8,height:grid.height + 32,
			title:'&nbsp;&nbsp;公告 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;点击单条可以单独收起或者展开 ',closable:false,resizable:false,maximizable:false,
			items:[grid]
			/*
			,tbar: ['->',{
                text: '全部收起',
                cls: 'x-btn-text-icon details',
                handler: function(btn, pressed){
                    var view = App.bulletinToViewWin.grid.getView();
                    view.ignoreToolBarStatus = true;
                    view.showPreview = false;
                	 App.bulletinToViewWin.grid.store.each(function(rec){
                    	rec.set('shouldExpand',false);
                    });
                    view.refresh();
                }},{
                text: '全部展开',
                cls: 'x-btn-text-icon details',
                handler: function(btn, pressed){
                    var view = App.bulletinToViewWin.grid.getView();
                    view.ignoreToolBarStatus = true;
                    view.showPreview = true;
                	 App.bulletinToViewWin.grid.store.each(function(rec){
                    	rec.set('shouldExpand',true);
                    });
                    view.refresh();
                }}]
                */
		});
//	}
		
	Ext.Ajax.request({
		url : '/'+Constant.ROOT_PATH_LOGIN +'/bulletin?optr='+App.data.optr.optr_id+'&dept_id='+App.data.optr.dept_id,
		scope : this,
		success : function(res, opt) {
			var data = Ext.decode(res.responseText);
			if (!data || data.length ==0) {
				return ;
			}
			var grid = App.bulletinToViewWin.grid;
			grid.loadData(data);
			App.bulletinToViewWin.show();
			App.bulletinToViewWin.showStatus = 1;
			Ext.TaskMgr.stop(bulletinTask);
		},
		failure : function() {
			Alert('加载数据出错!');
		}
	});

}
//taskTime最好大于winHideTime,由于请求的原因，时间有差异，所以尽量设置明显的时间差
//以下时间为测试数据。正式 循环执行时间最好大于1分钟！
var taskTime =  300*1000;//公告任务循环执行时间(单位为毫秒)
//var winHideTime = 10000;//公告框显示多少时间后隐藏(单位为毫秒)

//公告任务，每10秒查询一次
var bulletinTask = {
	run:function(){showBulletin();},
	interval:taskTime
};

//公告
var showBulletin = function(){
	if(!App.bulletinToViewWin){
		showBulletinWhenInit();
		return;
	}
	Ext.Ajax.request({
		url : '/'+Constant.ROOT_PATH_LOGIN +'/bulletin?optr='+App.data.optr.optr_id+'&dept_id='+App.data.optr.dept_id,
		scope:this,
		success:function(res,opt){
			var data = Ext.decode(res.responseText);
			if(data && data.length >0){
				data = data[0];
				var bulletinContent = Ext.util.Format.ellipsis(data['bulletin_content'],120,true);
				var win = Ext.getCmp('bulletWinId');
				if(!win){
					win = new TipsWindow({
						id:'bulletWinId',title:'公告',shadow:true,
						autoHide:false,//不自动隐藏
						html:"<a href='#' onclick='javascript:doBulletinClick()'><div id='bulletinDivId'>"+bulletinContent+"</div></a>"
						,buttons : [{text : '查看',handler : function() {doBulletinClick();}}]
						,listeners:{
							hide:function(){
								//确认公告或者关闭window，重新开始公告任务，延迟10秒执行
								Ext.TaskMgr.start.defer(taskTime,null,[bulletinTask]);
							},
							show:function(){
								//停止任务
								Ext.TaskMgr.stop(bulletinTask);
							}
						}
					});
				}else{
					Ext.get('bulletinDivId').update(bulletinContent);
				}
				win.setTitle(data.bulletin_title);
				win.show();
				//不看明细直接保存公告
				saveBulletin = function(v) {
					Ext.Ajax.request({
								url : root+ '/system/x/Index!checkBulletin.action',
								params : {bulletin_id : data['bulletin_id']},
								success : function() {Ext.getCmp(v).close();}
							});
				}
				
				//打开公告详细window后停止循环公告任务
				doBulletinClick = function(){
					win.hide();
					//停止任务
					Ext.TaskMgr.stop(bulletinTask);
					var w = Ext.getCmp('bulletinWinId');
					if(!w)
						w = new Ext.Window({
							id:'bulletinWinId',maximizable:false,
							title:'公告信息',height:400,width:600,border:false,
							closeAction:'close',
							layout:'fit',
							items:[
								{layout:'form',labelAlign:'right',labelWidth:75,
									bodyStyle:'padding-top:10px',
									bodyStyle: "background:#F9F9F9",
									border:false,items:[
									{fieldLabel:'标题',xtype:'displayfield',value:data['bulletin_title']},
									{fieldLabel:'发布人',xtype:'displayfield',value:data['bulletin_publisher']},
									{fieldLabel:'内容',xtype:'displayfield',html:
										'<div style="overflow-y:auto;overflow-x:hidden;height:250px;">'+data['bulletin_content']+'</div>',
									width:500,height:265,readOnly :true}
								]}
							],
							listeners:{
								scope:this,
								close:function(){
									//确认公告或者关闭window，重新开始公告任务，延迟10秒执行
									Ext.TaskMgr.start.defer(taskTime,this,[bulletinTask]);
								}
							},
							buttonAlign:'right',
							buttons:[{text:'确认',handler:function(){saveBulletin('bulletinWinId');}}]
						});
					w.show();
			 	}
			}
		}
	});
}