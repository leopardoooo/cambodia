FileForm = Ext.extend(Ext.FormPanel,{
	parent : null,
	constructor : function(p){
		this.parent = p;
		FileForm.superclass.constructor.call(this,{
			layout : 'column',
			region : 'north',
			height : 50,
			fileUpload: true,
			bodyStyle : "background:#F9F9F9;",
			border : false,
			defaults : {
				layout : 'form',
				border : false,
				bodyStyle : "background:#F9F9F9; padding-top: 10px"
			},
			items : [{
				columnWidth : .7,
				items : [{
					id:'checkInFielId',
					fieldLabel:'费用文件',
					name:'files',
					xtype:'textfield',
					inputType:'file',
					allowBlank:false,
					anchor:'95%',
					emptyText:''
				}]
			},{
				columnWidth : .15,
				items : [{
					xtype : 'button',
					text : '导入文件',
					scope : this,
					handler : this.doLoad
				}]
			},{
				columnWidth : .15,
				items:[{
					xtype : 'button',
					text : '模板下载',
					tooltip:'请勿在业务繁忙时操作;<br/>请勿删除模板中第一行;<br/>收费和赠送金额单位为分',
					scope : this,
					handler : function(){
						window.open(Constant.ROOT_PATH+'/template/batch_fee_template.xls');
					}
				}]
			}]
		})
	},
	doLoad : function(){
		if(this.getForm().isValid()){
			var file = Ext.getCmp('checkInFielId').getValue();
			var flag = this.checkFileType(file);
			if(!flag)return;
			
			this.getForm().submit({
				url:"core/x/Pay!loadFeeExcel.action",
				scope:this,
				waitTitle:'请稍后',
				waitMsg: '正在提交数据...',
				success:function(form,action){
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
							Ext.getCmp('batchPayFeeGrid').getStore().removeAll();
						}else{
							Ext.getCmp('batchPayFeeGrid').getStore().loadData(data.records);
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

BatchPayFeeGrid = Ext.extend(Ext.grid.GridPanel,{
	feeStore : null,
	constructor : function(){
		this.feeStore = new Ext.data.JsonStore({
			fields : ['invoice_code','invoice_book_id', 'invoice_id','invalid_date',
					'user_id','user_type',"cust_no","cust_name","cust_addr","prod_name",
					"prod_sn","fee","present_fee","user_type_text"]
		});
		
		this.feeStore.on("load",function(store){
			store.each(function(record){
				if(record.get('fee') == 0){
					record.set('invoice_id','');
					record.set('invoice_book_id','');
					record.set('invoice_code','');
					record.commit();
				}
			});
		},this);
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
			{header : '客户名称',dataIndex : 'cust_name',width:80},
			{header : '客户地址',dataIndex : 'cust_addr',width:120,renderer:App.qtipValue},
			{header : '用户类型',dataIndex : 'user_type_text',width:70},
			{header : '产品名称',dataIndex : 'prod_name',width:80},
			{header : '预计到期日',dataIndex : 'invalid_date',width:80},
			{header : '缴费金额',dataIndex : 'fee',width:80,renderer:Ext.util.Format.formatFee},
			{header : '赠送金额',dataIndex : 'present_fee',width:80,renderer:Ext.util.Format.formatFee},
			{header : '发票号',dataIndex : 'invoice_id',width:100},
			{header : '发票代码',dataIndex : 'invoice_code',width:100}
	        ]
	      });
		
		BatchPayFeeGrid.superclass.constructor.call(this,{
			id : 'batchPayFeeGrid',
			title : '收费列表',
			region : 'center',
			viewConfig : {
				forceFit : true
			},
			view: new Ext.ux.grid.ColumnLockBufferView({
				rowHeight: 19
			}),
			store : this.feeStore,
			cm : cm
		})
	},
	initEvents :function(){
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		BatchPayFeeGrid.superclass.initEvents.call(this);
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
BatchPayFeeWin = Ext.extend(Ext.Window,{
	fileForm : null,
	batchPayFeeGrid : null,
	data : null,
	constructor : function(){
		this.fileForm = new FileForm(this);
		this.batchPayFeeGrid = new BatchPayFeeGrid();
		
		BatchPayFeeWin.superclass.constructor.call(this,{
			id : 'batchPayFeeId',
			title : '批量收费',
			closeAction : 'close',
			height : 600,
			width : 700,
			layout : 'border',
			items : [this.fileForm,this.batchPayFeeGrid],
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
			var all = {'payFeesData':Ext.encode(this.data)}, busiParams = {};
			var msg = Show();
			busiParams['busiCode'] = "1241";
			all['jsonParams'] = Ext.encode(busiParams);
			
			
			Ext.Ajax.request({
				url : Constant.ROOT_PATH+"/core/x/Acct!payFees.action",
				params:all,
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:function(res,opt){
					msg.hide();
					msg = null;
					Alert('业务保存成功',function(){
						this.close();
					},this);
				}
			})
		
		})
	},
	doValid : function(){
		this.data = [];
		var msg = true;
		
		this.batchPayFeeGrid.getStore().each(function(record){
			if(Ext.isEmpty(record.get('prod_sn'))){
				msg = "表格中有一条数据prodSn为空，请核对";
				return false;
			}else if(Ext.isEmpty(record.get('fee'))){
				msg = "表格中有一条数据缴费金额为空，请核对";
				return false;
			}else if(record.get('fee') > 0 && Ext.isEmpty(record.get('invoice_id'))){
				msg = "表格中有一条数据发票号码为空，请核对";
				return false;
			}else{
				var obj = {};
				obj['cust_name'] = record.get('cust_name');
				obj['prod_sn'] = record.get('prod_sn');
				obj['fee'] = record.get('fee');
				obj['present_fee'] = record.get('present_fee');
				obj['invoice_id'] = record.get('invoice_id');
				obj['invoice_code'] = record.get('invoice_code');
				obj['invoice_book_id'] = record.get('invoice_book_id');
				this.data.push(obj);
			}
		},this);
		
		if(this.data.length == 0){
			msg = "请导入数据";
		}
		
		return msg;
	}
})