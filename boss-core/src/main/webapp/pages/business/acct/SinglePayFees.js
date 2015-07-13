/**
 * 缴费
 */

//用户选择一次缴几个月的费用,分 月，季，半年 三中情况
//月 store data
var feeMonth = [[1],[2],[3],[4],[5],[6],[7],[8],[9],[10],[11],[12]];
//季 store data
var feeSeason = [[1],[2],[3],[4]];
//半年 store data
var feeHalfYear = [[1],[2]];

SinglePayFeesGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	payFeesStore:null,
	dataArr:null,//专用账目数据
	disctData:[],//所有资费对应折扣信息
	disctDataMap:{},//所有资费对应折扣信息,以tariff_id为key
	disctCombo:null,
	constructor:function(data){
		this.dataArr = data;
		this.payFeesStore = new Ext.data.GroupingStore({
			reader: new Ext.data.JsonReader({},[
				{name:'acct_id'},{name:'acctitem_id'},{name:'user_id'},{name:'prod_id'},
				{name:'tariff_id'},{name:'tariff_name'},{name:'disct_id'},{name:'disct_name'},{name:'disct_name_all'},
				{name:'user_name'},{name:'user_type_text'},{name:'stb_id'},{name:'card_id'},{name:'modem_mac'},
				{name:'prod_sn'},{name:'acctitem_name'},{name:'real_bill'},{name:'status'},
				{name:'fee'},{name:'fee_date',dataFormat:'Y-m-d'},{name:'invalid_date',dataFormat:'Y-m-d'},
				{name:'real_balance'},{name:'acct_type_text'},{name:'next_tariff_id'},{name:'next_tariff_name'},
				{name:'prod_status'},{name:'prod_status_text'},{name:'min_pay'},
				{name:'rent'},{name:'billing_type'},{name:'feeOther'},//缴费金额备份字段
				{name:'billing_cycle'},{name:'fee_month'}//自定义字段
				//折扣备份字段
				,{name:'disct_id_other'},{name:'disct_name_other'}
			]),
			groupField:'acct_id'//分组字段(根据账目分组)
		});
		this.payFeesStore.loadData(this.dataArr);
		
		this.disctCombo = new Ext.form.ComboBox({
		store:new Ext.data.JsonStore({
			fields : ['disct_id','disct_name_all','tariff_id','min_pay']
		}),
		hiddenName:'disct_id',//editable:true,forceSelection:true,
		displayField:'disct_name_all',valueField:'disct_name_all',emptyText:'请选择。。',
		minListWidth : 250,
		listeners:{
			scope:this,
			select:function(combo,record){
				var r = this.getSelectionModel().getSelected();
				r.set('disct_id',record.get('disct_id'));
				r.set('disct_name_all',record.get('disct_name_all'));
				r.set('min_pay',Ext.util.Format.formatFee( record.get('min_pay') ));
				//备份折扣信息
				r.set('disct_id_other',record.get('disct_id'));
				r.set('disct_name_other',record.get('disct_name_all'));
				this.stopEditing();
			}
		}
	});
		
		var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : false
				},
				columns:[new Ext.grid.RowNumberer(),
					{header:'账户ID',dataIndex:'acct_id',hidden:true},
					{header:'账目名称',dataIndex:'acctitem_name',width:160,renderer:App.qtipValue},
					{header:'当前资费',dataIndex:'tariff_name',width:100,renderer:App.qtipValue},
					{header:'未生效资费',dataIndex:'next_tariff_name',width:120,renderer:App.qtipValue},
					{header:'状态',dataIndex:'prod_status_text',width:85,renderer:Ext.util.Format.statusShow},
					{header:'预计到期日',dataIndex:'invalid_date',width:110,renderer:Ext.util.Format.dateFormat},
					{header:'实时余额',dataIndex:'real_balance',width:100,renderer:Ext.util.Format.formatFee},
					{id:'disct_name_id',header:'折扣',dataIndex:'disct_name_all',width:180,editor: this.disctCombo},
					{id:'fee_month_id',header:'到期日',dataIndex:'fee_month',width:120, renderer:Ext.util.Format.dateFormat,
						editor:new Ext.form.DateField({
							format: 'Y-m-d'
						})
					},
					{id:'fee_id',header:'缴费金额',dataIndex:'fee',width:100,
						scope:this,editor: new Ext.form.NumberField({allowNegative:false,minValue:0,//enableKeyEvents: true,
								scope:this,
								listeners:{
									specialKey : function(field, e) {
						                if (e.getKey() == Ext.EventObject.ENTER) {//响应回车
						                	var thiz = this.scope;
						                	if(thiz.getStore().getCount()>currentRow+1)
						                		setTimeout(function(){thiz.startEditing(currentRow,thiz.getColumnModel().getIndexById('fee_id'))},200);
						                }  
						            }
								}
							})
					}
				],
				scope:this,
				isCellEditable:this.isCellEditable
		});
		
		SinglePayFeesGrid.superclass.constructor.call(this,{
			title:'专用账目缴费',
			store:this.payFeesStore,
			stripeRows:true,
			columnLines:true,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({}),
			height:450,
			autoWidth:true,
			clicksToEdit:1,
			view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl:'{[values.rs[0].data["user_name"] ? "用户名:"+[values.rs[0].data["user_name"]]+"&nbsp;&nbsp;用户类型:"+[values.rs[0].data["user_type_text"]] : "客户账户" ]}' +
	            		'{[values.rs[0].data["stb_id"]?"&nbsp;&nbsp;机顶盒:"+[values.rs[0].data["stb_id"]]+" ":""]}' +
        				'{[values.rs[0].data["card_id"]? "&nbsp;&nbsp;智能卡:"+[values.rs[0].data["card_id"]]+" ": ""]}' +
        				'{[values.rs[0].data["modem_id"]? "&nbsp;&nbsp;modem号:"+[values.rs[0].data["modem_id"]]: ""]}'
        	})
		});
	},
	//是否可编辑
	isCellEditable:function(colIndex,rowIndex){
		var disctIndex = this.getIndexById('disct_name_id');//折扣index
		var grid = this.scope;//当前grid引用
		var record = grid.getStore().getAt(rowIndex);//当前编辑行对应record
		var tariffID =record.get('tariff_id');//资费编号
		if(disctIndex == colIndex){//折扣列
			var store = this.getCellEditor(colIndex,rowIndex).field.getStore();
			store.removeAll();//清空上一次选中行中 该列的数据
			if(Ext.isEmpty(tariffID)) return false;
			//资费对应的折扣信息,无折扣信息则不可编辑
			//加载当前资费对应的折扣信息
			if(grid.disctData.length>0){
				for(var i=0;i<grid.disctData.length;i++){
					var disct = grid.disctData[i];
					if(tariffID == disct['tariff_id']){
						if(disct['disctList'] && disct['disctList'].length>0){
							store.loadData(disct['disctList']);
						}else
							return false;
						break;
					}
				}
			}
		}else if(colIndex == this.getIndexById('fee_month_id')){
			var acctId = record.get("acct_id");
			var acctitemId = record.get("acctitem_id");
			var acctitem = App.getApp().main.infoPanel.getAcctPanel().acctItemGrid.getAcctItemByAcctId(acctId, acctitemId);
			var prod = App.getApp().main.infoPanel.getUserPanel().prodGrid.getProdByUserId(acctitem["user_id"], acctitem["prod_sn"]);
			if(prod["billing_cycle"] > 1){
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	listeners:{
		delay:100,
		afterrender:function(grid){
		
			var store = grid.getStore();
			
			//资费，用户id
			var tariffIds = [],userIds = [];
			store.each(function(record){
				if(record.get('tariff_id')){
					tariffIds.push(record.get('tariff_id'));
					userIds.push(record.get('user_id'));
				}
			});
			
			//将所有产品资费对应的折扣信息一次性取回
			if (tariffIds.length>0){
				Ext.Ajax.request({
					url:Constant.ROOT_PATH+'/core/x/Prod!queryTariffByTariffIds.action',
					params:{
						custId:App.getApp().getCustId(),
						tariffIds:Ext.encode(tariffIds),
						userIds:Ext.encode(userIds)
					},
					scope:this,
					success:function(res,options){
						this.disctData = Ext.decode(res.responseText);
						for(var index =0;index<this.disctData.length;index++){
							var data = this.disctData[index];
							this.disctDataMap[data.tariff_id] = data;
						}
						if(this.disctData && this.disctData.length > 0){
							//对应的资费金额匹配
							store.each(function(record){
								var tId = record.get('tariff_id');
								if(tId){
									for(var i=0;i<this.disctData.length;i++){
										var disct = this.disctData[i];
										if(tId == disct['tariff_id']){
											//当前资费对应的 月租和计费方式 赋值
											record.set('rent',disct['rent']);
											record.set('billing_type',disct['billing_type']);
											record.set('billing_cycle',disct['billing_cycle']);
											
											//初始化折扣字段
											if(disct['disctList'] && disct['disctList'].length>0){
												var disctArr = disct['disctList'];
												disctArr.push({
															'disct_id' : null,
															'disct_name_all' : '清空折扣',
															'tariff_id' : null,
															'min_pay' : null
														});
												//默认给当前产品 资费选择第一个折扣
												var disctListData = disctArr[0];
												record.set('disct_id',disctListData['disct_id']);
												record.set('disct_name_all',disctListData['disct_name_all']);
												record.set('min_pay',Ext.util.Format.formatFee( disctListData['min_pay'] ));
												
												//备份折扣信息
												record.set('disct_id_other',disctListData['disct_id']);
												record.set('disct_name_other',disctListData['disct_name_all']);
												
												record.commit();
											}
											break;
										}
									}
								}
							},this);
						}
						this.startEditing.defer(100,this,[0,this.getColumnModel().getIndexById('fee_id')]);
					}
				});
			}
			},
			beforeedit:function(obj){
				if(obj.field == 'fee'){
					//编辑行全局变量
					currentRow = obj.row;
				}else if(obj.field == 'fee_month'){
					if(!Ext.isEmpty(obj.record.data.disct_id)){//有折扣,不能编辑到期日
						this.stopEditing();
						return false;
					}
				}
			},
			afteredit:function(obj){
				var record = obj.record;
				var fieldName = obj.field;//编辑的column对应的dataIndex
				var value = obj.value;
				if(fieldName == 'disct_name_all'){//折扣
					var feeValue = record.get('feeOther');
					
					//缴费金额小于折扣对应的最小金额，清空缴费，重新编辑
					if(feeValue && record.get('min_pay') > feeValue){
						record.set('fee',null);
					}
					if(value != '清空折扣'){
						record.set('fee_month', "");
						record.commit();
					}else{
//						debugger;
						this.doAfterEditFee(record,record.get('fee'));
					}
					this.startEditing(obj.row,obj.column+2);
					
				}else if(fieldName == 'fee'){
					var disctId = record.get('disct_id');
					if(!Ext.isEmpty(disctId) && !Ext.isEmpty(value)){
						disctId = this.chooseDisct(record,null);
						if(!Ext.isEmpty(disctId)){
							return;
						}
					}
					this.doAfterEditFee(record,value);
				}else if(fieldName == 'fee_month'){
					if(this.disctCombo.getValue() != '清空折扣' && !Ext.isEmpty(this.disctCombo.getValue())){
						record.set('fee_month','');
						record.commit();
						return false;
					}
					this.getFeeByInvalidDate(record, value, function(fee){
						record.set('fee',fee);
						record.set('feeOther',fee);
						//总金额大于最小金额时，享受折扣
						if(fee>=record.get('min_pay')){
							record.set('disct_id',record.get('disct_id_other'));
							record.set('disct_name_all',record.get('disct_name_other'));
						}else{
							record.set('disct_id',null);
							record.set('disct_name_all',null);	
						}
					});
				}
			}
	},
	chooseDisct:function(record,realMinPay){
		var selectedDisctData = null;
		var tariffData = this.disctDataMap[record.get('tariff_id')];
		var disctRecs = tariffData.disctList;
		
		if(disctRecs && disctRecs.length >0){
			this.disctCombo.store.removeAll();
			for(var index =0;index<disctRecs.length;index++){
				var disct = disctRecs[index];
				if(Ext.isEmpty(realMinPay) && disct.min_pay/100 <=record.get('fee') ){
					selectedDisctData = disct;
					realMinPay = disct.min_pay/100;
					minPay = realMinPay;
					break;
				}
			}
			if(!selectedDisctData || Ext.isEmpty(selectedDisctData['disct_id'])){
				selectedDisctData = {};
			}else{
				record.set('fee_month', "");
			}
			record.set('disct_id',selectedDisctData['disct_id']);
			record.set('disct_name_all',selectedDisctData['disct_name_all']);
			record.set('min_pay',Ext.util.Format.formatFee( selectedDisctData['min_pay'] ));
			
			//备份折扣信息
			record.set('disct_id_other',selectedDisctData['disct_id']);
			record.set('disct_name_other',selectedDisctData['disct_name_all']);
			record.commit();
			return selectedDisctData['disct_id'];
		}
		return null;
	},
	doAfterEditFee:function(record, value){
		this.getInvalidDateByFee(record, value, function(date){
									record.set('fee_month', date);
									var minPay = record.get('min_pay');//最低缴费
									var realMinPay = null;//根据目前缴费判断可享受折扣的最低支付
									this.chooseDisct(record,realMinPay);
									//缴费金额小于折扣对应的最小金额,则无折扣
									if(minPay){
										if(minPay && value >= minPay){
											//大于最小金额时，享受折扣
											record.set('fee_month','');
											record.set('disct_id',record.get('disct_id_other'));
											record.set('disct_name_all',record.get('disct_name_other'));
										}else{
											record.set('disct_id',null);
											record.set('disct_name_all',null);
										}
									}
									record.set('feeOther',value);//用户单月缴费的数据备份
								});
	},
	getFeeByInvalidDate: function(record, value, fn){
		if("" == value){
			record.set('fee', "");
			record.commit();
			return;
		}
		var prodSn = record.get("prod_sn");
		var acctId = record.get("acct_id");
		var acctitemId = record.get("acctitem_id");
		var acctitem = App.getApp().main.infoPanel.getAcctPanel().acctItemGrid.getAcctItemByAcctId(acctId, acctitemId);
		var prod = App.getApp().main.infoPanel.getUserPanel().prodGrid.getProdByUserId(acctitem["user_id"], acctitem["prod_sn"]);
		
		// valid date
		var invalidDate = Date.parseDate(prod["invalid_date"], "Y-m-d H:i:s");
		var billinfoEffDate = Date.parseDate(prod["billinfo_eff_date"], "Y-m-d H:i:s");
		var now = nowDate();
		
		// TODO valid date
		var maxDate = null;
		if(now >= invalidDate && now >= billinfoEffDate){
			maxDate = now;
		}else if(invalidDate >= now && invalidDate >= billinfoEffDate){
			maxDate = invalidDate;
		}else if(billinfoEffDate >= now && billinfoEffDate >= invalidDate){
			maxDate = billinfoEffDate;
		}
		if(value <= maxDate){
			Alert("到期日至少要大于[" + maxDate.format("Y-m-d") + "]", function(){
				record.set("fee_month", "");
				record.set('fee', "");
				record.commit();
			});
			return;
		}
		
		
		// ajax get fee
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/core/x/Prod!getFeeByInvalidDate.action',
			params:{
				prodSn:prodSn,
				invaidDate: value.format("Y-m-d")
			},
			scope:this,
			success:function(res,options){
				var fee = Ext.util.Format.formatFee( res.responseText);
				fn.call(this, fee);
			}
		});
	},
	getInvalidDateByFee: function(record, value, fn){
		if("" == value){
			record.set('fee_month', "");
			record.commit();
			return;
		}
		var prodSn = record.get("prod_sn");
		var acctId = record.get("acct_id");
		var acctitemId = record.get("acctitem_id");
		var acctitem = App.getApp().main.infoPanel.getAcctPanel().acctItemGrid.getAcctItemByAcctId(acctId, acctitemId);
		var prod = App.getApp().main.infoPanel.getUserPanel().prodGrid.getProdByUserId(acctitem["user_id"], acctitem["prod_sn"]);
		
		
		// 如果计费周期为包年或者大于1的
		if(prod["billing_cycle"] > 1){
			var valueFee = parseInt(Ext.util.Format.convertToCent(value));
			var real_balance = acctitem.inactive_balance + acctitem.active_balance - acctitem.owe_fee + valueFee;
			if(real_balance < record.data.rent){
				record.set('fee_month', prod["invalid_date"]);
				return;
			}
			var month = parseInt( real_balance / record.data.rent) * prod.billing_cycle;
			var invalidDate = Date.parseDate(prod["invalid_date"], "Y-m-d H:i:s");
			if(prod.status != 'ACTIVE'){
				invalidDate = new Date();
			}
			invalidDate.setMonth(invalidDate.getMonth() + month);
			record.set('fee_month', invalidDate);
			return;
		}
		
		
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+'/core/x/Prod!getInvalidDateByFee.action',
			params:{
				
				fee: Ext.util.Format.convertToCent(value),
				prodSn:prodSn
			},
			scope:this,
			success:function(res,options){
				var o = Ext.decode(res.responseText);
				var date = Date.parseDate(o["invalidDate"], "Y-m-d");
				fn.call(this, date);
			}
		});
	},
	getValues:function(){
		this.stopEditing();
		var arr=[],data;
		var store = this.getStore();
		store.each(function(record){
			data = record.data;
			//当前行输入了 "缴费金额"，则提交当前行对应数据
			if (data['fee'] && data['fee']>0 ){
				var obj={};
				obj["acct_id"] = data['acct_id'];
				obj["acctitem_id"] = data['acctitem_id'];
				obj["user_id"] = data['user_id'];
				obj["prod_sn"] = data['prod_sn'];
				obj["tariff_id"] = data['tariff_id'];
				if(data['fee']>=data['min_pay']){
					obj["disct_id"] = data['disct_id'];
				}
				obj["invalid_date"] = data['invalid_date'];
				obj["fee"] = Ext.util.Format.formatToFen(data['fee']);
				arr.push(obj);
			}
		},this);
		return arr;
	}
});
