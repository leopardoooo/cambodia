Ext.ns("Ext.ux");

FilterWindow = function(){
	var cmbName='combobox_';//新建组件id的头部
	var fields=null;//保存传递的column属性数组
	var compArr=null;
	var store = null;
	var title = null;
	var allData = null;
	var beginDate=" 00:00:00";
	var endDate=" 23:59:59"
	var grid=null;
	var keyTypeId = null;//判断是否过滤已经选中的项，如果不是null将界面显示保存已经选中的项，如果是null将不考虑已经选中项
	var isRemote = false;//是否跨页远程搜索
	var objectKey = null;//查询条件对象
	var remoteFields = [];
	
	/**
	 * private
	 * 重新加载所有数据
	 */
	function reloadData(){
		if(!Ext.isEmpty(store)){
			if(isRemote === false){
				store.clearFilter();
			}else{
				objectKey = Ext.isEmpty(objectKey) ? "" : objectKey+".";
				Ext.each(remoteFields, function(fields){
					store.baseParams[objectKey+''+fields] = '';
				});
				store.load({
					params:{
						start:0,
						limit:grid.pageSize
					}
				});
			}
		}
	}
	
	function destoryData(){
		grid = null;
		store = null;
		fields = null;
		keyTypeId = null;
		isRemote = false;
		remoteFields = [];
		objectKey = null;
	}
	
	function closeWin(){
		try{
			reloadData();
		}catch(e){
		}finally{
			destoryData();
		}
	}
	
	/**
	 * private
	 * 下拉框 下拉 时触发
	 */
	function doLoadRecord(cmb){
		if(isRemote === false){
			//数据未加载时，禁止expand
			if(store.data.length==0){
				cmb.getStore().removeAll();//清空列表数据
				return;
			}
			var id = cmb.id;
			var field = id;
			var arrField = [];
			store.each(function(record){
				var value = record.get(field);
				if(!Ext.isEmpty(value) && arrField.indexOf(value) == -1)//过滤为空的值
					arrField.push(value);
			});
	//		arrField = getNewArray(arrField);//过滤重复的值
			
			var text;
			for(var i=0;i<fields.length;i++){
				if(id==fields[i].field){//选出和当前combobox ID对应的text
					text=fields[i].text;break;
				}
			}
			
			var data=[{'text':text,'value':''}];//设置默认值(store.data数据格式)
			for(var i=0;i<arrField.length;i++){
				var text = store.getAt(store.find(id, arrField[i])).get(cmb.showField);
				data.push({'text':text,'value':arrField[i]});
			}
			
			cmb.getStore().loadData(data);
		}
	}
	/**
	 * 过滤已经选中的项
	 * field 过滤的字段
	 * value 过滤值
	 */
	function doChangeRecord(field,value){
		var ck = false;
		var getData = grid.getSelectionModel().getSelections();
		store.filterBy(function(record){
			if(keyTypeId != null){
				Ext.each(getData,function(records){
					if(record.get(field) == records.get(field)){
						ck = true;
					}
				});
				if(ck){
					ck = false
					return true;
				}
			}
			
			if(record.get(field)){
				return record.get(field).indexOf(value)>=0;
			}else{
				return false;
			}
		});
	}
	/**
	 * private
	 * 下拉框 select时 触发
	 */
	function doSelectRecord(cmb){
		//数据未加载时，禁止select值
//		if(store.data.length==0 && isRemote === false)
//			return;

		var fieldName = cmb.id;
		var value = cmb.getValue();
		if(isRemote === false){
			if(value==''){//选择默认值时，加载所有
				reloadData();
			}else{
				doChangeRecord(fieldName,value);		
				
	//			store.filter(field,value);//过滤与当前值不同的数据
				/** 
				 * snapshot data 缓存数据
				 * 过滤时，store会备份所有数据到snapshot
				 * 而data中存放的是过滤后的数据
				 * 
				 * 将store中的备份数据重置为过滤后的数据,
				 * 这样当文本框删选数据时，直接从combobox筛选出来的数据中筛选
				 * 否则会从整个store中筛选数据
				 */
	//			store.snapshot=store.data;
			}
			resetComp(fieldName);
		}else{
			remoteLoadData(fieldName, cmb.getValue());
		}
	}
	function resetComp(fieldName){
		for(var i=0;i<compArr.length;i++){
			if(compArr[i].id != fieldName)//重置除当前组件外的其他组件
				compArr[i].reset();
		}
	}
	/**
	 * private
	 * textfield 回车 筛选
	 */
	function doFilterRecord(txt,e){
		if(e.getKey() != e.ENTER) return ;
		var value = txt.getValue();
		var fieldName = txt.id;
		if(isRemote === false){
			doChangeRecord(fieldName, value);
			resetComp(fieldName);
		}else{
			remoteLoadData(fieldName, value);
		}
	}
	//远程查询数据
	function remoteLoadData(fieldName, value){
		var objectKeyOther = objectKey;	//备份
		objectKey = Ext.isEmpty(objectKey) ? "" : objectKey+".";
		store.baseParams[objectKey+''+fieldName] = value;
		store.load({
			params:{
				start:0,
				limit:grid.pageSize
			}
		});
		objectKey = objectKeyOther;
		if(remoteFields.indexOf(fieldName) == -1 && !Ext.isEmpty(value)){
			remoteFields.push(fieldName);
		}
	}
	
	/**
	 * private
	 * datefield 回车 筛选
	 */
	function doDateEnter(df,e){
		if(e.getKey() != e.ENTER) return ;
		
		var fieldName = df.id;
		if(isRemote === false){
			var preDf = compArr[compArr.indexOf(df)-2];//前一个控件
			var lastDf = compArr[compArr.indexOf(df)+2];//后一个控件
			
			
			var preDfValue,lastDfValue;
			if(preDf) preDfValue = preDf.getRawValue();
			if(lastDf) lastDfValue = lastDf.getRawValue();
			var dfId = fieldName.substr(0,fieldName.length-1);
			
			if(preDf && preDf instanceof Ext.form.DateField && !Ext.isEmpty(preDfValue) ){
				store.filterBy(function(record){
					return record.get(dfId) >= preDfValue.concat(beginDate);
				},df);
			}else if(lastDf && lastDf instanceof Ext.form.DateField && !Ext.isEmpty(lastDfValue)){
				store.filterBy(function(record){
					return record.get(dfId) <= lastDfValue.concat(endDate);
				},df);
			}else{
				reloadData();
			}
		}else{
			remoteForDate(fieldName, df.getRawValue());
		}
	}
	
	function remoteForDate(fieldName, value){
		if(!Ext.isEmpty(value)){
			if(fieldName.lastIndexOf('1') == fieldName.length - 1) value += beginDate;
			else if(fieldName.lastIndexOf('2') == fieldName.length - 1) value += endDate;
		}
		remoteLoadData(fieldName, value);
	}
	/**
	 * private
	 * datefield select事件 筛选
	 */
	function doDateFilter(df,date){
		var fieldName = df.id;
		var dfValue = df.getRawValue();
		if(isRemote === false){
			var preDf = compArr[compArr.indexOf(df)-2];//前一个控件
			var lastDf = compArr[compArr.indexOf(df)+2];//后一个控件
			
			
			
			var preDfValue,lastDfValue;
			
			if(preDf) preDfValue = preDf.getRawValue();
			if(lastDf) lastDfValue = lastDf.getRawValue();
			
			var dfId = fieldName.substr(0,fieldName.length-1);
			
			//前一个控件存在，且是datefield，显示值也不为空 
			if(preDf && preDf instanceof Ext.form.DateField && !Ext.isEmpty(preDfValue) ){
				store.filterBy(function(record){
					return record.get(dfId) >= preDfValue.concat(beginDate)
						&& record.get(dfId) <= dfValue.concat(endDate);
				},df);
			}else if(lastDf && lastDf instanceof Ext.form.DateField && !Ext.isEmpty(lastDfValue)){
				store.filterBy(function(record){
					return record.get(dfId) >= dfValue.concat(beginDate)
						&& record.get(dfId) <= lastDfValue.concat(endDate);
				},df);
			}else if(preDf && preDf instanceof Ext.form.DateField && Ext.isEmpty(preDfValue)){
				store.filterBy(function(record){
					return record.get(dfId) <= dfValue.concat(endDate);
				},df);
			}else if(lastDf && lastDf instanceof Ext.form.DateField && Ext.isEmpty(lastDfValue)){
				store.filterBy(function(record){
					return record.get(dfId) >= dfValue.concat(beginDate)
				},df);
			}
		}else{
			remoteForDate(fieldName, df.getRawValue());
		}
	}
	
	/*function getNewArray(arr){
	    var arrResult = [];
	    for (var i=0; i<arr.length; i++){
	        if (isExist(arrResult, arr[i])) //判断数组中该元素是否存在
	            arrResult.push(arr[i]); // 添加该元素到新数组。
	    }
	    return arrResult;
	}*/
	function isExist(arr, checkItem){
	    var flag = true;
	    for (var i=0; i<arr.length; i++){//循环遍历，以判断该元素是否存在
	        if (arr[i]==checkItem){
	            flag = false;//当前元素存在，不添加
	            break;
	        }
	    }
	    return flag;
	}
	
	/**
	 * public
	 */
	var pub = {
		relatedCmps:{},
		/**
		 * 处理重新查询客户之后,title仍然为上次查询的结果的情况.
		 */
		clearRelatedCmpsTitle:function(){
			for(var key in this.relatedCmps){
				var ss = this.relatedCmps[key];
				if(ss){
					var title = ss._grid_Original_Title_;
					if(title){
						ss.setTitle(title);
					}
				}
			}
		},
		addComp :function(ss,showFields,width,keyId,remote,obj){
			store = ss.getStore();
			this.relatedCmps[ss.id] = ss;
			/* 修改所有过滤之后 标题显示对应的记录条数 */
			var originalFilterByFn = store.filterBy,
				originalClearFilterFn = store.clearFilter,
				setGridTitleFn = function(){
					var titleModified = ss._title_Modified_By_Filter_Window_;
					var title = ss._grid_Original_Title_;
					if(!titleModified){
						title = ss.title;
						ss._grid_Original_Title_ = title;
						ss._title_Modified_By_Filter_Window_ = true;
					}else{
						title = ss._grid_Original_Title_;
					}
					if(store){
						ss.setTitle(title + lbc("common.totalRecord", null, store.getCount()));
					}
				};
			store.filterBy = function(fn, scope){
				originalFilterByFn.call(store, fn, scope);
				setGridTitleFn();
			};
			store.clearFilter = function(b){
				originalClearFilterFn.call(store, b);
				setGridTitleFn();
			}
			store.on("load", setGridTitleFn);
			
			allData = ss.getStore().data;
			grid = ss;
			title = ss.title;
			fields = showFields;
			keyTypeId = keyId;
			isRemote = remote;
			objectKey = obj;
			
			compArr=[];
			var comp=null;
			for(var i=0,len=showFields.length;i<len;i++){
				var showField = showFields[i];
				//根据type区分创建哪种组件,默认为combobox
				if(showField.type == 'textfield'){
					comp = new Ext.form.TextField({
						id:showField.field,
						value: showField.text,
						emptyText: showField.text,
						width:120,
						enableKeyEvents: true, 
						cls:"ux-readOnly",
						listeners:{
							scope: this,
							'specialkey':doFilterRecord
						}
					});
					compArr.push(comp);
					
				}else if(showField.type == 'datefield'){
					comp = new Ext.form.DateField({
						id:showField.field.concat('1'),
						format:'Y-m-d',
						emptyText:showField.text,
						plugins:showField.plugins,
						cancelText:true,
						width:90,
						listeners:{
							scope: this,
							'select': doDateFilter,
							'specialkey': doDateEnter//键盘敲击事件
						}
					});
					compArr.push(comp);
					
					comp = new Ext.form.DisplayField({
						value:'-'
					});
					compArr.push(comp);
					
					comp = new Ext.form.DateField({
						id:showField.field.concat('2'),
						format:'Y-m-d',plugins:showField.plugins,
						emptyText:showField.text,
						width:90,
						listeners:{
							scope: this,
							'select': doDateFilter,
							'specialkey': doDateEnter//键盘敲击事件
						}
					});
					compArr.push(comp);
					
				}else{
					comp = new Ext.form.ComboBox({
						id:showField.field,
						value: showField.text,
						height:showField.height || 30,
						width: showField.width || 90,triggerAction: 'all',
						store: new Ext.data.JsonStore({
							fields:['text','value'],
							data:showField.data ? showField.data : []
						}),
						triggerAction: 'all',
						mode:'local',displayField:'text',valueField:'value',
						showField:showField.showField,
			            listeners:{
			            	scope:this,
			            	'select':doSelectRecord,
			            	'expand':doLoadRecord
			            }
					});
					compArr.push(comp);
				}
			}
			
			var columnCount = compArr.length;
			
			if(columnCount > 0){
				
				var panel = new Ext.Panel({
						mode:false,
						height:35,
						layout:'fit',
						frame:true,
						plain:true,
						border:false,
						items:[{
							xtype: 'compositefield',
							layout:'fit',
	    					items:compArr
						}]
				});
				return new Ext.Window({
					id:'filterWinID',
					title:lbc("common.filterTitle"),
					plain:true,
					closeAction:'close',
					maximizable:false,
					animate:true,
					border:false,
					modal: false,
					width:width,
					items:[panel],
					listeners : {
						scope : this,
						close : closeWin,
						hide : closeWin
					}
				});
			}else{
				return null;	
			}
		}
	};
	
	return pub;
}();