
DimGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	parent:null,datas:[],query_id:null,
	loadData:function(dimList,query_id){
		this.datas = dimList;
		this.store.loadData(dimList);
		this.query_id = query_id;
		this.doAfterRender();
	},
	constructor:function(parent,datas){
		this.parent=parent;
		this.query_id = this.parent.query_id;
		this.datas = datas;
		this.store = new Ext.data.JsonStore({
			fields : ['dim', 'dim_name','total_name','totallist'],
			data:this.datas
		});
		
		DimGrid.superclass.constructor.call(this,{
			sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
			buttonAlign:'center',
			buttons : [
			{id : 'dcw_confirmed_btn',text:'确定',handler : this.submitBtnHandler,scope:this},
			{id : 'dcw__cancel_btn',text:'取消',handler : this.cancelBtnHandler,scope:this}
			],
    		columns:[
				  {header:'维度',hidden:true,dataIndex:'dim'},
				  {header:'维度',width:180,dataIndex:'dim_name'},
				  {header:'设置',width:180,dataIndex:'totallist',renderer:this.renderTotalList,scope:this}
			],
			listeners:{
				scope:this,
				afterRender:this.doAfterRender
			},
    		store:this.store
		});
	},
	checkDataChanged:function(formerData,newData){//数据是否改变,没有改变就不再提交.
		var flag = false;
		for(var dim in formerData){
			var oldArray = formerData[dim];
			var oldValArray = [];
			Ext.each(oldArray,function(od){
				if(od.check){
					oldValArray.push(od.id);
				}
			});
			var newArray = newData[dim];
			if(newArray.join() != oldValArray.join()){
				flag = true;
			}
			if(flag){
				break;
			}
		}
		return flag;
	},
	submitBtnHandler:function(){
		var map = {};
		function parseComboValue(value){
			value = value.replace(new RegExp('\'','gi'), '');
			var array = value.split(',');
			var result = [];
			Ext.each(array,function(str){
				var num = parseInt(str);
				if(Ext.isNumber(num)){
					result.push(num);
				}
			});
			return result;
		}
		var formerData = {};
		Ext.each(this.datas,function(data){
			var dim = data.dim;
			var id = dim + '_combo_id';
			var cmp = Ext.getCmp(dim);
			var vals = cmp.getValue();
			formerData[dim] = data.totallist;
			map[dim] = parseComboValue(vals);
		},this);
		//String com.ycsoft.report.web.action.query.ShowAction.cubeDimTotal() throws Exception
		var cube = {dimtotalmap:map};
		if(!this.checkDataChanged(formerData,map)){//如果数据没有改变,不用提交.
			this.parent.hide();
			return false;
		}
		var param = {query_id:this.query_id,jsonParams:Ext.encode({mycube:cube})};
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Show!cubeDimTotal.action',
			timeout:9999999999,
			scope:this,
			params : param,
			success : function(res, opt) {
				this.parent.refreshParent();
				wait.hide();
				wait = null;
				this.parent.hide();
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('加载数据出错');
			}
		});
	},
	cancelBtnHandler:function(){
		this.parent.hide();
	},
	renderTotalList:function(value, cellmeta, record, rowIndex, columnIndex, store,appendParams){
		var id = record.data.dim + '_combo_id';
		return String.format('<div id="{0}"><div id="{1}"></div></div>',Ext.id(),id);
	},
	doAfterRender:function(cmp){
		new Ext.util.DelayedTask(function(){
			for(var index =0;index<this.datas.length;index++){
				var id = this.datas[index].dim + '_combo_id';
				var array =[];
				var values = [];
				Ext.each(this.datas[index].totallist,function(data){
					//测试
					//data.check =true;
					if(data.check){
						values.push(data.id);
					}
					array.push(data);
				});
				
				var combo = new Ext.ux.form.LovCombo({
					id:this.datas[index].dim,
                	store : new Ext.data.JsonStore({
						fields : ['id', 'name','pid','check'],data:array
					}),
                	displayField : 'name',valueField : 'id',checkField:'check',
                	width:160,mode: 'local',triggerAction : "all"
                });
                combo.render(id);
                combo.setValue(values.join());
                
                if(values.length >1){
	                Ext.each(values,function(val){
	                	var index = combo.store.find('id',val);
	                	var record = combo.store.getAt(index);
	                	combo.onSelect(record, index);
	                });
                }
			}
		},this).delay(300);
	}
	
});

DimCalcWin = Ext.extend(Ext.Window,{
	id:'DimCalcWinId',width:380,height:260,layout:'fit',
	grid:null,query_id:null,
	dimList:[],parent:null,
	loadData:function(parent,datas,query_id){
		this.parent = parent;
		this.dimList = datas;
		this.query_id = query_id;
		this.grid.loadData(this.dimList,query_id);
	},
	constructor:function(parent,dimList,query_id){
		this.parent = parent;
		this.query_id = query_id;
		this.dimList = dimList;
		this.grid = new DimGrid(this,this.dimList);
		DimCalcWin.superclass.constructor.call(this,{
			items:this.grid
		});
	},
	refreshParent:function(){
		this.parent.xtablePanel.loadAndView(0);
	}
});