BillFileForm = Ext.extend(Ext.FormPanel,{
	parent : null,
	constructor : function(p){
		this.parent = p;
		BillFileForm.superclass.constructor.call(this,{
			layout : 'column',
			region : 'north',
			height : 50,
			fileUpload: true,
			bodyStyle : "background:#F9F9F9;",
			border : false,
			defaults : {
				layout : 'form',
				border : false,
				bodyStyle : "background:#F9F9F9; padding-top: 10px",
				columnWidth : .5
			},
			items : [{
				items : [{
					id:'billFile',
					fieldLabel:'费用文件',
					name:'files',
					xtype:'textfield',
					inputType:'file',
					allowBlank:false,
					anchor:'95%',
					emptyText:''
				}]
			},{
				items : [{
					xtype : 'button',
					text : '导入文件',
					scope : this,
					handler : this.doLoad
				}]
			}]
		})
	},
	doLoad : function(){
		if(this.getForm().isValid()){
			var file = Ext.getCmp('billFile').getValue();
			var flag = this.checkFileType(file);
			if(!flag)return;
			
			var msg = Show();
			this.getForm().submit({
				url:"core/x/Pay!loadMobileBillExcel.action",
				scope:this,
				success:function(form,action){
					msg.hide();
					msg = null;
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Ext.getCmp('mobileBillGrid').getStore().loadData(data.records);
						}
					}
				},  
				failure : function(form, action) {  
					alert("文件上传失败!");  
				}
			});
		}
	},
	checkFileType : function(fileText){
		if(fileText.lastIndexOf('xlsx')>0 || fileText.lastIndexOf('.xlsx')==fileText.length-5){
			Alert('请选择excel2003文件进行上传,文件后缀名为.xls!');
			return false;
		}else if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
			Alert('请选择excel文件进行上传！');
			return false;
		}
		return true;
	}
});

MobileBillGrid = Ext.extend(Ext.grid.GridPanel,{
	billStore : null,
	constructor : function(){
		this.billStore = new Ext.data.JsonStore({
			fields : ['invoice_book_id','invoice_code', 'invoice_id','prod_name',"acctitem_id",
					'user_id',"cust_no","cust_name","fee",'done_code','create_time']
		});
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
			columns : [
			{header : '客户编号',dataIndex : 'cust_no'},
			{header : '客户名称',dataIndex : 'cust_name'},
			{header : '产品名称',dataIndex : 'prod_name'},
			{header : '缴费流水',dataIndex : 'done_code'},
			{header : '缴费金额',dataIndex : 'fee',renderer:Ext.util.Format.formatFee},
			{header : '缴费时间',dataIndex : 'create_time'},
			{header : '发票号',dataIndex : 'invoice_id'},
			{header : '发票代码',dataIndex : 'invoice_code'}
	        ]
	      });
		
		MobileBillGrid.superclass.constructor.call(this,{
			id : 'mobileBillGrid',
			title : '缴费信息',
			region : 'center',
			viewConfig : {
				forceFit : true
			},
			view: new Ext.ux.grid.ColumnLockBufferView({
				rowHeight: 19
			}),
			store : this.billStore,
			cm : cm
		})
	},
	initEvents :function(){
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		MobileBillGrid.superclass.initEvents.call(this);
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
	}
})
MobileBillWin = Ext.extend(Ext.Window,{
	fileForm : null,
	mobileBillGrid : null,
	data : null,
	constructor : function(){
		this.fileForm = new BillFileForm(this);
		this.mobileBillGrid = new MobileBillGrid();
		
		BatchPayFeeWin.superclass.constructor.call(this,{
			id : 'mobileBillWinId',
			title : '未支付结账',
			closeAction : 'close',
			height : 600,
			width : 700,
			layout : 'border',
			items : [this.fileForm,this.mobileBillGrid],
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
		})
	},
	doSave : function(){
		var msg = this.doValid();
		if(msg != true){
			Alert(msg);
			return;
		}
		
		Confirm("确定保存吗?", this ,function(){
			var all = {'payFeesData':Ext.encode(this.data)};
			var msg = Show();
			
			Ext.Ajax.request({
				url : Constant.ROOT_PATH+"/core/x/Pay!checkMobileBill.action",
				params:all,
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:function(res,opt){
					msg.hide();
					msg = null;
					var rs = Ext.decode(res.responseText);
					Alert('业务保存成功,共结账'+rs.simpleObj+'条数据');
					this.close();
				}
			})
		
		})
	},
	doValid : function(){
		this.data = [];
		var msg = true;
		
		this.mobileBillGrid.getStore().each(function(record){
			if(Ext.isEmpty(record.get('done_code'))){
				msg = "表格中有一条数据缴费流水为空，请核对";
				return false;
			}else if(Ext.isEmpty(record.get('fee'))){
				msg = "表格中有一条数据缴费金额为空，请核对";
				return false;
			}else if(Ext.isEmpty(record.get('user_id'))){
				msg = "表格中有一条数据用户编号为空，请核对";
				return false;
			}else if(Ext.isEmpty(record.get('acctitem_id'))){
				msg = "表格中有一条数据产品编号为空，请核对";
				return false;
			}else if(Ext.isEmpty(record.get('invoice_id'))){
				msg = "表格中有一条数据发票号码为空，请核对";
				return false;
			}else{
//				var obj = {};
//				obj['user_id'] = record.get('user_id');
//				obj['acctitem_id'] = record.get('acctitem_id');
//				obj['fee'] = record.get('fee');
//				obj['done_code'] = record.get('done_code');
//				obj['invoice_id'] = record.get('invoice_id');
//				obj['invoice_code'] = record.get('invoice_code');
//				obj['invoice_book_id'] = record.get('invoice_book_id');
				this.data.push(record.data);
			}
		},this);
		
		return msg;
	}
})