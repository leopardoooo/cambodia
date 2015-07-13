/**
 * 封装过滤的Grid组件，
 * 在grid的Top bar追加多个列名下拉框和输入框。
 */
Ext.ns("Ext.ux");

Ext.ux.FilterGridTbar = Ext.extend( Ext.grid.EditorGridPanel , {
	cmbName: 'combobox_',//新建组件id的头部
	fields: null,//保存传递的column属性数组
	
	constructor: function(cfg){
		Ext.ux.FilterGridTbar.superclass.constructor.call(this,cfg);
	},
	/**
	 * 该方法必须在构造函数之前调用
	 * 
	 * 根据传递的column属性，在toolbar上创建各个组件
	 * 
	 * @param {} thiz toolbar的容器(this)
	 * @param {} showFields 要创建的column属性数组
	 * 		(格式：[{field:'name',text:'jack',type:'textfield'},{field:'address',text:'hz'}])
	 * 
	 * 在扩展本组件的remoteRefresh方法中添加以下代码：
	 * 
	 * 重置新增到toolbar上的组件
	 * var items = this.getTopToolbar().items.items;
	 * if(items && items.length>0)
	 * 	for(var i=0;i<items.length;i++){
	 * 		if(items[i] instanceof Ext.form.TextField || items[i] instanceof Ext.form.ComboBox){
	 * 			items[i].reset();
	 * 		}
	 * 	}
	 * 具体请参照CustPanel.js中的CustDeviceGrid组件
	 */
	addCompToToolbar: function(thiz,showFields){
		fields = showFields;
		var tbar = thiz.tbar || [];
		var comp=null;
		var filterCmb = [];
		for(var i=0;i<showFields.length;i++){
			//根据type区分创建哪种组件,默认为combobox
			if(showFields[i].type=='textfield'){
				comp = new Ext.form.TextField({
					id:this.cmbName.concat(showFields[i].field),
					value: showFields[i].text,
					width:90,
					listeners:{
						scope: this,
						'specialkey': this.doFilterRecord//键盘敲击事件
					}
				});
			}else{
				comp = new Ext.form.ComboBox({
					id:this.cmbName.concat(showFields[i].field),
					value: showFields[i].text,
					width: 90,triggerAction: 'all',
					store: new Ext.data.SimpleStore({data:[[showFields[i].text,'']],fields:['field','value']}),
					mode:'local',displayField:'field',valueField:'value'
		            ,listeners:{
		            	scope:this,
		            	'select':this.doSelectRecord
		            	,'expand':this.doLoadRecord
		            }//,delay:200 //延迟
				});
			}
				filterCmb.push(comp,'-');
		}
		//复制到tbar属性中
//		Ext.apply(thiz , {
//			tbar: tbar.concat(filterCmb)
//		});
		var toolbar = new Ext.Toolbar(tbar);
		toolbar.render('filterID');
	}
	,doLoadRecord: function(cmb){
		var store = this.getStore();
		//数据未加载时，禁止expand
		if(store.data.length==0){
			cmb.getStore().removeAll();//清空列表数据
			return;
		}
		var id = cmb.id;
		var field = id.substr(this.cmbName.length);
		var arrField = [];
		store.each(function(record){
			if(record.get(field) && record.get(field)!='')//过滤为空的值
				arrField.push(record.get(field));
		});
		arrField = this.getNewArray(arrField);//过滤重复的值
		
		var text;
		for(var i=0;i<fields.length;i++){
			if(id.substr(this.cmbName.length)==fields[i].field){//选出和当前combobox ID对应的text
				text=fields[i].text;break;
			}
		}
		
		var data=[[text,'']];//设置默认值(store.data数据格式)
		for(var i=0;i<arrField.length;i++){
			data.push([arrField[i],arrField[i]]);
		}
		
		cmb.getStore().loadData(data);
	}
	//combobox筛选数据
	,doSelectRecord: function(cmb){
		var store = this.getStore();
		//数据未加载时，禁止select值
		if(store.data.length==0)
			return;

		var field = cmb.id.substr(this.cmbName.length);
		var value = cmb.getValue();
		if(value==''){//选择默认值时，加载所有
			//this.storeData 为当前panel中store无url，本地加载数据
			if(Ext.isEmpty(this.storeData))
				store.reload();
			else{
				store.removeAll();
				for(var i=0;i<this.storeData.length;i++)
					store.loadData(this.storeData[i],true);
			}
		}else{
			store.filter(field,value);//过滤与当前值不同的数据
			/** 
			 * snapshot data 缓存数据
			 * 过滤时，store会备份所有数据到snapshot
			 * 而data中存放的是过滤后的数据
			 * 
			 * 将store中的备份数据重置为过滤后的数据,
			 * 这样当文本框删选数据时，直接从combobox筛选出来的数据中筛选
			 * 否则会从整个store中筛选数据
			 */
			store.snapshot=store.data;
		}
		var type;
		for(var i=0;i<fields.length;i++){
			if(fields[i].field!=field)//重置除当前组件外的其他组件
				Ext.getCmp(this.cmbName.concat(fields[i].field)).reset();
		}
	}
	//textfield筛选
	,doFilterRecord: function(txt,e){
		if(e.getKey() != e.ENTER) return ;
		this.getStore().filter(txt.id.substr(this.cmbName.length),txt.getValue().trim(),true,false);
	}
	,getNewArray : function (arr){
	    var arrResult = [];
	    for (var i=0; i<arr.length; i++){
	        if (this.isExist(arrResult, arr[i])) //判断数组中该元素是否存在
	            arrResult.push(arr[i]); // 添加该元素到新数组。
	    }
	    return arrResult;
	}
	,isExist : function(arr, checkItem){
	    var flag = true;
	    for (var i=0; i<arr.length; i++){//循环遍历，以判断该元素是否存在
	        if (arr[i]==checkItem){
	            flag = false;//当前元素存在，不添加
	            break;
	        }
	    }
	    return flag;
	}
//	,initEvents:function(){
//		Ext.ux.FilterGridTbar.superclass.initEvents.call(this);
//	}
});
