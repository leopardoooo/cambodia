///*
// * 查询结果显示界面
// */
//
//
////var QueryStore;
////var columns=[];
////var ReportGrid;
////var query_id;
//
//ReportForm = Ext.extend(Ext.Panel,{
//	rep_id : null,
//	rep_name:null,
//	constructor : function(rep_id,rep_name){
//		this.rep_id = rep_id;
//		this.rep_name=rep_name;
//		ReportForm.superclass.constructor.call(this,{
//// title: '报表查询信息',
//			region: "north" ,
//			collapsible: true,
//			collapseMode:'mini',
//			height:800,
//			split: true,
//			layout: 'fit',
//			id:this.rep_id+('reportForm'),
//			defaults: { 
//				labelWidth: 100
//			}, 
//			items : [
//				new ReportShow(this.rep_id,this.rep_name)
//	       	]
//		})
//	}
//})
//
//
//function getHeaders(repId,query_id){
//     	Ext.Ajax.request({
//     			scope : this,
//     			// 表头加载
//     			url : root +'/query/Show!queryHeader.action',
//     			params: {
//     				query_id : query_id
//     			},
//     			success : function(res,opt){
//     				var headers = Ext.decode(res.responseText).records;     				
//     				var fields = [],
//     				columns=[],
//     				rows = [];
//     				for(i=0;i<headers.length;i++){
//     					if(i==headers.length - 1){
//     						var j=0;
//     						Ext.each(headers[i],function(h){  
//	     				 	
//					         columns.push({
//					            header:h.col_desc,       
//					            dataIndex:h.col_desc       
//					            });
//					         fields.push({
//					         	name : h.col_desc,
//					         	mapping : j
//					         });
//					         j++;
//					         
//					     	});
//     					}else{
//     						var row = [];
//     						Ext.each(headers[i],function(h){  
//	     				 		row.push({
//					                header: h.col_desc,
//					                align: 'center',
//					                colspan: h.col_length
//					            });
//					     	});
//					     	rows.push(row);
//     					}
//     					
//     				}
//
//     				var group = new Ext.ux.grid.ColumnHeaderGroup({
//				        rows: rows
//				    });
//     				// 报表信息查询
//     				QueryStore = new Ext.data.JsonStore({ 
//					   url  : root + '/query/Show!queryReport.action' ,
//					   root : 'records' ,
//					   totalProperty: 'totalProperty',
//					   autoDestroy: true,
//					   baseParams: {limit: App.pageSize,start: 0 ,query_id : query_id },
//		               fields: fields       
//				     }); 
//     				QueryStore.load({
//     					params : {
//     						query_id : query_id
//     					}
//     				})
//				     ReportGrid =new Ext.grid.GridPanel({ 
//				     	store:QueryStore,
//						columns:columns,
//						plugins: group,
//						viewConfig: {
//				            forceFit: true
//				        },
//						stripeRows: true,             
//						tbar:[{text:'下载',scope:this,handler:function(){
//								var title = Ext.getCmp(repId+"MainPanel").title;
//								window.location.href=root+"/query/Show!downloadExp.action?query_id="+query_id+"&rep_name="+title;
//							}
//						}],
//				        bbar: new Ext.PagingToolbar({store: QueryStore ,pageSize:App.pageSize})// ,//分页信息
//				     });
//				    
//			
//				     Ext.getCmp(repId+('panelId')).items.itemAt(1).items.add(ReportGrid);
//				     Ext.getCmp(repId+('panelId')).items.itemAt(0).collapse(true);
//				     //Ext.getCmp(repId+('panelId')).doLayout();
//				    // Ext.getCmp(repId+('reportForm')).hide();
//					 //Ext.getCmp(repId+('panelId')).remove(Ext.getCmp(repId+('panelId')).items.itemAt(0),true);
//				    // var com =  Ext.getCmp(repId+('panelId')).items.itemAt(1);
//				     //com['anchor']='100% 100%';
//				     //Ext.getCmp(repId+('panelId')).doLayout();
//     			}
//     		});
//     }
//     
//
//     
//
