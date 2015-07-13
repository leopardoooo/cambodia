RuleProdTree = Ext.extend(Ext.tree.TreePanel, {
    prodId: null,
    constructor: function () {
    	resourceToOptrTreeThis = this;
		var root = new Ext.tree.AsyncTreeNode({
		  text:'gen',
		  id:'root',
			  draggable:false,
			children:[{id:'1',text:'无套餐内容',disabled:true,leaf:true}]
		 });
    	
        RuleProdTree.superclass.constructor.call(this, {
            split: true,
            id: 'RuleProdTreeId',
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            rootVisible:false,//是否显示根节点
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader:new Ext.tree.TreeLoader(),
            ddGroup:'mygroup',
		 	animate:false, //是否动画
		  	enableDD:true,
		  	border : false,
		  	dropConfig:{appendOnly:true}
        });
        this.setRootNode(root);
    },
    listeners: {
        'checkchange': function (node, checked) {
            if(node.parentNode.attributes.others.type == 'RULE'){//选产品
            	var num = 0;
            	node.parentNode.eachChild(function (child) {
	                if(child.attributes.checked && child.attributes.others.force_select == 'F'){
	                	num++;
	                }
	            });
	            if(!Ext.isEmpty(node.parentNode.attributes.others.prod_count) && num>node.parentNode.attributes.others.prod_count){
	            	Alert("可选产品只能选择"+node.parentNode.attributes.others.prod_count+"个!");
	            	node.attributes.checked = false;
	            	node.ui.toggleCheck(false);
	            }
            }else if(node.parentNode.attributes.others.type == 'GROUP'){//选资源
             	var num = 0;
             	if(!Ext.isEmpty(node.parentNode.parentNode.attributes.checked)){
             		node.parentNode.parentNode.attributes.checked = true;
             		node.parentNode.parentNode.ui.toggleCheck(true);
             	}
            	node.parentNode.eachChild(function (child) {
	                if(child.attributes.checked){
	                	num++;
	                }
	            });
	            if(num>node.parentNode.attributes.others.res_number){
	            	Alert("可选资源只能选择"+node.parentNode.attributes.others.res_number+"个!");
	            	node.attributes.checked = false;
	            	node.ui.toggleCheck(false);
	            }
            }
        }
    },initComponent : function() {
		RuleProdTree.superclass.initComponent.call(this);

	},loadData :function(data){
		var root = new Ext.tree.AsyncTreeNode({
				  text:'gen',
				  id:'root',
				  draggable:false,
				  children:data
				 });
		 this.setRootNode(root);
		 this.root.expand(true);
		
//	var root = new Ext.tree.AsyncTreeNode({
//	  text:'gen',
//	  id:'root',
//	  draggable:false,
//	  children:[{id:'1',text:'规则主终端',leaf:false,children:[
//		  {id:'2',text:'基本包产品',leaf:true,disabled:true,checked:true},
//		  {id:'3',text:'第一剧场[选]',leaf:false,children:[{id:'4',text:'精彩视野',leaf:true},{id:'5',text:'动漫快递',leaf:true},{id:'6',text:'影视剧场',leaf:true}]}]
//	  		},{id:'7',text:'规则副终端',leaf:false,children:[
//		  {id:'8',text:'基本包产品',leaf:true},{id:'9',text:'星空卫视',leaf:true}]}]
//	 });
//	 this.setRootNode(root);

	}
});

PromPayFeeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Acct!promPayFee.action",
	promFee : 0,
	ruleProdTree : null,
	promFeeId : null,	
	userAll:[],			//用户panel
	promTreeData:null,  //套餐树数据
	promUserData:null, //终端数据
	selectData:[], //最终选定的数据
	useData:[], //自动选的数据
	constructor : function(){
		this.promotionStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH + "/commons/x/QueryUser!querySelectablePromPay.action",
			fields : ['prom_fee_id','prom_fee_name','prom_fee','join_cnt','use_cnt','attr','remark'],
			sortInfo : {
				field : 'prom_fee',
				direction:'DESC'
			}
		})
		
		this.promotionStore.load({params : {custId : App.getApp().getCustId()}});
		
		this.promotionStore.on('load',function(){
			if(this.promotionStore.getCount() == 0){
				Alert('该客户下没有满足套餐规则的用户，请重新确认!');
			}
		},this);
		
		this.ruleProdTree = new RuleProdTree(this);
		
		PromPayFeeForm.superclass.constructor.call(this,{
			baseCls:'x-plain',
            layout:'border',
            id:'promPayFeeFormId',
            items : [{
            	xtype : 'panel',
            	region : 'north',
            	height:120,
            	layout : 'column',
            	bodyStyle : "background:#F9F9F9;",
            	border : false,
            	autoScroll:false,
            	defaults : {
					baseCls: 'x-plain',
					bodyStyle: "padding: 5px;background:#F9F9F9;",
					columnWidth:0.3,
					labelWidth : 80,
					layout: 'form'
				},
				items : [{columnWidth:0.4,
					items : [{
						xtype : 'combo',
						store : this.promotionStore,
						fieldLabel : '套餐名称',
						emptyText: '模糊套餐名称或金额',
						width:120,
						allowBlank : false,
						hiddenName : 'prom_fee_id',
						valueField : 'prom_fee_id',
						displayField : "prom_fee_name",
						forceSelection : true,
						minListWidth  : 250,
						editable:true,
						beforeBlur:function(){},
						listeners:{
							scope : this,
							'select' : this.doSelectProm,
							'beforequery':this.beforeQuery
						}
					} ]
				},{columnWidth:0.3,
					items : [{
		            	xtype : 'datefield',width:100,
						fieldLabel : '预开通日期',
						id:'preOpenTimeId',
						name:'preOpenTime',
						format:'Y-m-d',
						editable:true,
						minValue:nowDate().add(Date.DAY,1).format('Y-m-d')
						}
					]
				},{columnWidth:0.2,
					items : [{
		            	xtype : 'displayfield',
		            	readOnly:true,
						fieldLabel : '套餐价格',
						style : Constant.TEXTFIELD_STYLE,
						readOnly : true,
						id : 'promFee'
            		}
					]
				},{
					columnWidth:1,
        			bodyStyle:'padding-right:80px',
					items : [{
            			xtype:'panel',border:false,fieldLabel : '说明',
            			items:[{
							xtype:'textarea',border:false,
							id : 'promfee_remark',readOnly:true,
							width:'100%',
							height:70,
							bodyStyle:'padding-left:30px'
						}]
            		}
					]
				}]
            },{region : 'center',border : false,xtype : 'panel',title: '套餐内容',layout: 'fit',items:[this.ruleProdTree]
            	},{region:'east',width:'50%',border : false,layout: 'fit', items:[{
            	xtype : 'panel',
            	title: '终端信息',
            	autoScroll:true,
            	id:'userAllId',
            	border : false,
            	bodyStyle: 'background-color: #dfe8f6;',
//            	tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
//							listeners:{
//								scope:this,
//								keyup:function(txt,e){
//									if(e.getKey() == Ext.EventObject.ENTER){
//									Ext.getCmp('userAllId').removeAll();
//									this.doLayout();
//									}
//								}
//							}
//						}],
			    items: []
            	}]
			}]
		})
	},initComponent : function() {
		PromPayFeeForm.superclass.initComponent.call(this);
		var acctStore = App.getApp().main.infoPanel.getAcctPanel().acctGrid.getStore();
		var jKey = false; fKey = false;
		for (var k = 0; k < acctStore.getCount(); k++) {
			var rec = acctStore.getAt(k);
			var acctItems = rec.get('acctitems');
			if(acctItems!=null && rec.get('acct_type') == "SPEC"){
				for (var j = 0; j < acctItems.length; j++) {
					if (acctItems[j]['active_balance'] + acctItems[j]['order_balance'] - acctItems[j]['owe_fee'] -acctItems[j]['real_bill'] <0) {
						if (acctItems[j]['is_base'] == "T") {//溧阳的基本包是后付费的，基本都会欠费的，所以没必要提示
//							jKey = true;
//							break;
						}else{
							fKey = true;							
							break;
						}
					}
				}
			}
		}
		if(fKey){
			Alert("套餐不适用：付费节目有欠费!" ,function(){
				App.getApp().menu.hideBusiWin();
				return false;
			});
		}	
		if(jKey){
			Confirm("基本产品欠费，如直接缴费，优先扣除欠费，用户到期日会提前，可能无法冲正!", this, function() {
				},function(){
					App.getApp().menu.hideBusiWin();
					return false;
			});
		}
	},
	beforeQuery:function(e){
		var combo = e.combo;
		var store = combo.getStore();
		var value = e.query;
		if(Ext.isEmpty(value)){ 
			store.clearFilter();
        }else{
			combo.collapse();
			var re = new RegExp('^.*' + value + '.*$','i');
	        store.filterBy(function(record,id){
	            var text = record.get('attr');
	            return re.test(text);
	        });
	        combo.expand();
			return false;
		}
	}
	,
	 renderHander : function(cmp) {
		new Ext.dd.DropTarget(cmp.body.dom, {
			ddGroup: 'mygroup',
			notifyDrop: function(ddSource, e, data) {
					var key = true;
					var basenode = data.node;
					if(data.node.attributes.others.type == 'RULE' ){
						basenode = data.node;
					}else if(data.node.attributes.others.type == 'PROD'){
						basenode = data.node.parentNode;
					}else if(data.node.attributes.others.type == 'GROUP'){
						basenode = data.node.parentNode.parentNode;
					}else if(data.node.attributes.others.type == 'RES'){
						basenode = data.node.parentNode.parentNode.parentNode;
					}
					if(cmp.userType == 'BAND'){
						var prodMap = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap;
						var prods = prodMap[cmp.userValue];
						if(prods == null){
							Alert("宽带用户请先订购宽带产品!");
							key = false;
						}
					}
					
					//判断选择的规则是否可用于选择的终端
					var users = basenode.attributes.others.allow_user;
					var userKey = true;
	        		users = users == ""?"":users.split(",");
	        		if(users != ""){
	        			for(var i=0;i<users.length;i++){
	        				if(users[i]==cmp.userValue){
	        					userKey = false;
	        				}
	        			}
	        		}
					if(userKey){
						Alert("该规则不适合所选的终端!");
						key = false;
					}
		
					//判断所选的规则是否已经选择好产品或资源
		        	var num = 0;pFee = 0;canNum =0;
		        	basenode.eachChild(function (childp) {
		                if( childp.attributes.others.force_select == 'F'){
		                	canNum++;
		                	if(childp.attributes.checked){
		                		num++;
		                	}
		                }
		                childp.eachChild(function (childg) {
		                	var resnum = 0;
		                	childg.eachChild(function (childr) {
		                		if(childr.attributes.checked){
				                	resnum++;
				                }
		                	});
		                	if( ((childp.attributes.others.force_select == 'F' && childp.attributes.checked)||childp.attributes.others.force_select == 'T')  && resnum != childg.attributes.others.res_number){
				            	Alert("未选择"+childg.attributes.others.res_number+"个资源!");
				            	key = false;
				            }
		                });
		                if(childp.attributes.others.force_select == 'T' || childp.attributes.checked){ //必选和选中的可选产品
		               	 	pFee = pFee + parseInt(childp.attributes.others.real_pay);
		                }
		            });
		            if(key && canNum < basenode.attributes.others.prod_count){
		            	Alert("可选数【"+basenode.attributes.others.prod_count+"】大于实际存在的可选产品【"+canNum+"】!操作可以继续,请及时联系管理员查看套餐配置是否正确!");
		            }else{
			            if(!Ext.isEmpty(basenode.attributes.others.prod_count) && num != basenode.attributes.others.prod_count){
			            	Alert("未选择"+basenode.attributes.others.prod_count+"个产品!");
			            	key = false;
			            }
		            }
		            if(pFee != basenode.attributes.others.user_fee){
		            	Alert("产品实缴总金额"+Ext.util.Format.formatFee(pFee)+"与规则金额"+Ext.util.Format.formatFee(basenode.attributes.others.user_fee)+"不一致");
		            	key = false;
		            }
					//满足条件后禁用规则树
					if(key){
						var src ="";
						src = basenode.text;
						basenode.disable();
//						basenode.attributes.others.user_id = cmp.userValue;//赋值user_id
						basenode.eachChild(function (childp) {
							childp.disable();
							childp.eachChild(function (childg) {
								childg.disable();
								childg.eachChild(function (childr) {
									childr.disable();
								});
							});
						});
						cmp.body.dom.innerHTML += src+";";
						basenode.eachChild(function (childp) {
							if(childp.attributes.others.force_select == 'T' || childp.attributes.checked){
								var obj = {};rList = [];
								obj['rule_id'] = basenode.attributes.others.rule_id;
								obj['user_no'] = basenode.attributes.others.user_no;
								obj['user_id'] = cmp.userValue;
								obj['user_name'] = cmp.userName;
								obj['user_fee'] = basenode.attributes.others.user_fee;
								obj['prod_id'] = childp.attributes.others.prod_id;
								obj['should_pay'] = childp.attributes.others.should_pay;
								obj['real_pay'] = childp.attributes.others.real_pay;
								obj['tariff_id'] = childp.attributes.others.tariff_id;
								obj['months'] = childp.attributes.others.months;
								obj['bind_prod_id'] = childp.attributes.others.bind_prod_id;
								
								childp.eachChild(function (childg) {
									childg.eachChild(function (childr) {
										var objr = {};
										if(childr.attributes.checked){
					                		objr['res_id'] = childr.attributes.others.res_id;
					                		rList.push(objr);
					                	}
									});
								});
								obj['resList'] = rList;
								Ext.getCmp('promPayFeeFormId').selectData.push(obj);
								
							}
						});
						
					}
				return true;
			}
		});
	},
	doSelectProm : function(comb,rec){
		if(!Ext.isEmpty(rec.get('join_cnt')) && !Ext.isEmpty(rec.get('use_cnt')) && rec.get('join_cnt')<=rec.get('use_cnt')){
			Alert("该客户已经达到本套餐的限额:"+rec.get('join_cnt')+"次!");
			this.promTreeData = [{id:'1',text:'无内容',disabled:true,leaf:true}];
			this.promUserData = null;
			this.promFeeId = null;
			this.promFee = null;
			Ext.getCmp('promFee').setValue("");
			this.doReturnData();
			return;
		}
		
		var prodMap = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap;
		
		Ext.getCmp('promfee_remark').setValue(rec.get('remark'));
		if(!this.promFeeId || this.promFeeId != rec.get('prom_fee_id')){
			this.promFeeId = rec.get('prom_fee_id');
		}else{
			return;
		}
		
		this.promFee = Ext.util.Format.formatFee(rec.get('prom_fee'));
		Ext.getCmp('promFee').setValue(this.promFee);
		
		Ext.Ajax.request({
			url : Constant.ROOT_PATH + "/commons/x/QueryUser!querySelectUserProm.action",
			scope : this,
			params : {
				promFeeId : this.promFeeId,
				custId : App.getApp().getCustId()
			},
			success : function(res,opt){
				var rs = Ext.decode(res.responseText);
				//加载套餐内容树
				this.promTreeData = rs.simpleObj.promTree;
				for(var index =0;index<this.promTreeData.length;index++){//判断每个套餐里是否有基本产品,如果有,则可以直接订购
					var promTreeNodeData = this.promTreeData[index];
					for(var idx =0;idx<promTreeNodeData.children.length;idx++){
						var childAttr = promTreeNodeData.children[idx].others;
						if(childAttr){
							if(childAttr.is_base && childAttr.is_base =='T'){
								promTreeNodeData.isOderdable = true;
								break;
							}
						}
					}
				}
				var data = rs.simpleObj.userList;
				//无可用用户
				if(data.length == 1 && Ext.isEmpty(data[0].user_id) && !Ext.isEmpty(data[0].user_name_text)){
					Alert(data[0].user_name_text);
					return false;
				}
				
				this.selectData = [];
				this.useData = [];
				for(var i=0;i<this.promTreeData.length;i++){//规则
					var promTreeNodeData = this.promTreeData[i];
					var users = promTreeNodeData.others.allow_user;
					users = users == ""?"":users.split(",");
					var key = false;
					var userNames = [];
					if(users.length >0){
						for(var index =0;index<users.length;index++){
							var userProd = prodMap[users[index]];
							if(!userProd || userProd.length <1){//没有产品
								//在查看套餐里是否包含基本产品,如果包含,则可以缴费,后台直接订购
								if(!promTreeNodeData.isOderdable){
									var userid = users[index];
									users.remove(userid);
									for(var idx1 = 0;idx1 <data.length;idx1++){
										var d1 = data[idx1];
										if(d1.user_id == userid){
											data.remove(d1);
										}
									}
									userNames.push(d1.user_name);
								}
							}
						}
					}
					if(users.length==0){
						Alert('用户 ' + userNames.join(',') + ' 未订购基本产品');
						this.promTreeData.remove(promTreeNodeData);
//						promTreeNodeData = {id:'1',text:'无套餐内容',disabled:true,leaf:true};
					}
					if(users!=""){
						
						if( users.length == 1){//规则对应的适用终端只有1个,自动为终端选中该规则
							key = true;
							if(promTreeNodeData.children){
								var totalFee = 0;
								for(var k=0;k<promTreeNodeData.children.length ;k++ ){//产品
									if(promTreeNodeData.children[k].others.force_select == 'T'){
										totalFee = totalFee + parseInt(promTreeNodeData.children[k].others.real_pay);;
									}else{
										key = false;
										break;
									}
									if(promTreeNodeData.children[k].children){
										for(var z=0;z<promTreeNodeData.children[k].children.length ;z++ ){//资源组
											if(!Ext.isEmpty(promTreeNodeData.children[k].children[z].others.res_number)){
												key = false;
												break;
											}
										}
									}
									if(!key){
										break;
									}
								}
								//配置出错，规则金额与实际缴费金额不一致
								if(key && totalFee != promTreeNodeData.others.user_fee){
									Alert("产品实缴总金额【"+Ext.util.Format.formatFee(totalFee)+"】与规则金额【"+Ext.util.Format.formatFee(promTreeNodeData.others.user_fee)+"】不一致!配置出错,请联系管理员!");
									key = false;
									
								}
								var userName;
								if(key){
									for(var j=0;j<data.length ;j++ ){
										if(data[j].user_id == users[0]){
											if(data[j].user_type =='BAND'){
												var prods = prodMap[users[0]];
												if(prods == null){
													Alert("宽带用户请先订购宽带产品!");
													key = false;
												}
											}
											if(key){
												data[j].user_src == null ? data[j].user_src = promTreeNodeData.text : data[j].user_src += ","+promTreeNodeData.text;
												data[j].user_name_text +=  promTreeNodeData.text+";";
												userName = data[j].user_name;
											}
										}
									}								
									
								}
								
								if(key){
									
									for(var k=0;k<promTreeNodeData.children.length ;k++ ){//产品
										promTreeNodeData.disabled = true;
										var child = promTreeNodeData.children[k];
										child.disabled = true;
										if(child.children){
											for(var z=0;z<child.children.length ;z++ ){//资源组
													child.children[z].disabled = true;
													for(var y=0;y<child.children[z].children.length ;y++ ){//资源
														child.children[z].children[y].disabled = true;
													}
											}
										}
										
										var obj = {};
										obj['rule_id'] = promTreeNodeData.others.rule_id;
										obj['user_id'] = users[0];
										obj['user_no'] = promTreeNodeData.others.user_no;
										obj['user_fee'] = promTreeNodeData.others.user_fee;
										obj['user_name'] = userName;
										//
										obj['prod_id'] = child.others.prod_id;
										obj['should_pay'] = child.others.should_pay;
										obj['real_pay'] = child.others.real_pay;
										obj['tariff_id'] = child.others.tariff_id;
										obj['months'] = child.others.months;
										obj['bind_prod_id'] = child.others.bind_prod_id;
										this.selectData.push(obj);
										this.useData.push(obj);
									}
								}
							}
						}

					}
				}
				
				this.ruleProdTree.loadData(this.promTreeData);
				//生成终端内容
				
				for(var i=0;i<data.length ;i++ ){
					data[i].user_src = data[i].user_src == null?"":data[i].user_src.split(",");
					if(data[i].user_src!="" && data[i].user_src.length >1){
						Alert(data[i].user_src+"都用于同一终端:"+data[i].user_name);
					}
					this.userAll.push({
					        bodyStyle: 'background-color: #dfe8f6;',
							ddGroup: 'mygroup',
							height:42,
							listeners: {
								scope : this,
								render:this.renderHander
							},	
							frame:true,
							userValue:data[i].user_id,
							userCard:data[i].card_id,
							userName:data[i].user_name,
							userType:data[i].user_type,
					        html: "<span style='font-weight:bold'>"+data[i].user_name_text+"</span>"
					    });
				}
				Ext.getCmp('userAllId').removeAll();
				Ext.getCmp('userAllId').add(this.userAll);
				this.promUserData = this.userAll;
				this.userAll = [];
				this.doLayout();
			}
		})
	},
	doReturnData : function(){
		//重置
		this.ruleProdTree.loadData(this.promTreeData);
		Ext.getCmp('userAllId').removeAll();
		if(this.promUserData != null){
			Ext.getCmp('userAllId').add(this.promUserData);
		}
		this.selectData = [];
		this.selectData=this.useData.concat();
		
		this.doLayout();
	},
	getFee : function(){
		return this.promFee;
	},
	doValid : function(){
		var node = Ext.getCmp('RuleProdTreeId').getRootNode();
		var result = {};
		if(!this.promFeeId){
			result["isValid"] = false;
			result["msg"] = "未选择有效套餐!";
			return result;
		}
		var arr = Ext.getCmp('promPayFeeFormId').selectData;
		if(!arr || arr.length <1){
			result["isValid"] = false;
			result["msg"] = "未选择要缴费的套餐!";
			return result;
		}
		var totalFee = 0; tNum = [];gNum = [];
		var d = Ext.getCmp('promPayFeeFormId').selectData;
		for(var i=0;i<this.promTreeData.length;i++){
			var key=true;
			for(var j =0 ;j <tNum.length;j++){
				if(tNum[j] == this.promTreeData[i].others.rule_id+"_"+this.promTreeData[i].others.user_no){
					key = false;
					break;
				}
			}
			if(key){
				tNum.push(this.promTreeData[i].others.rule_id+"_"+this.promTreeData[i].others.user_no);
			}
		}
		
		for(var i=0;i<d.length;i++){
			totalFee = totalFee + parseInt(d[i].real_pay);
			var key=true;
			for(var j =0 ;j <gNum.length;j++){
				if(gNum[j] == d[i].rule_id+"_"+d[i].user_no){
					key = false;
					break;
				}
			}
			if(key){
//				totalFee = totalFee + parseInt(d[i].real_pay);
				gNum.push(d[i].rule_id+"_"+d[i].user_no);
			}
		}
		
		if(gNum.length != tNum.length){
			result["isValid"] = false; 
			result["msg"] = "还有未选择的规则!";
			return result;
		}
		
		if(totalFee != Ext.util.Format.formatToFen(this.promFee)){
			result["isValid"] = false;
			result["msg"] = "产品实缴总金额【"+Ext.util.Format.formatFee(totalFee)+"】必须和套餐金额【"+this.promFee+"】一致";
			return result;
		}
		
		return PromPayFeeForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var all  = {};
		all['promFeeId'] = this.promFeeId;
		all['promFee'] = Ext.util.Format.formatToFen(this.promFee);
		all["prodArrStr"] = Ext.encode(Ext.getCmp('promPayFeeFormId').selectData);
		
		var preOpenTime = Ext.getCmp('preOpenTimeId').getValue();
		if(preOpenTime)
			all['preOpenTime'] = preOpenTime.format('Y-m-d');
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
})



Ext.onReady(function(){
	var cpf = new PromPayFeeForm();
	var box = TemplateFactory.gTemplate(cpf);
});