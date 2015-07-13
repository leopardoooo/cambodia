ExtAttrFactory = {};

/**
 * params： 提交的参数包含tabName、group、pkValue三个参数。
 * 具体需要根据说明传入使用。
 * cfg: 列布局的配置参数
 * examples: 
   cfg = [{
   	  columnWidth: .5,
   	  layout: 'form',
   	  baseCls: 'x-plain'
   },{
	  columnWidth: .3,
	  layout: 'form',
	  baseCls: 'x-plain'
   }]
 */
ExtAttrFactory = function(){
   var defaultCfg = [{
   	  columnWidth: .5,
   	  layout: 'form',
   	  baseCls: 'x-plain'
   },{
	  columnWidth: .5,
	  layout: 'form',
	  baseCls: 'x-plain'
   }];
   
   return {
		/**
		 * 生成扩展信息表单面板，Insert面板
		 * @param params 参数.
		 * @param cfg 配置信息.
		 * @param funcScope 回调函数的作用域名.
		 * @param func 自定义回调函数.
		 */
		gExtForm: function(params, cfg, funcScope, func){
			cfg = cfg || defaultCfg;
			if(!params || !params.tabName){
				alert("请配置params参数，包括扩展属性的表名!");
				return ;
			}
			return new ExtAttrForm(params, cfg, func,funcScope);
		},
		/**
		 * 生成扩展信息修改面板，Update面板，
		 */
		gExtEditForm: function(params, cfg, func, funcScope){
			cfg = cfg || defaultCfg;
			if(!params || !params.tabName || !params.pkValue){
				alert("请配置params参数，包括扩展属性的表名、主键值!");
				return ;
			}
			return new ExtAttrUpdate(params, cfg, func,funcScope);
		},
		gExtView: function(params, cfg){
			cfg = cfg || defaultCfg;
			if(!params || !params.pkValue || !params.tabName){
				alert("请配置params参数，包括扩展属性的表名、主键值!");
				return ;
			}
			return new ExtAttrView(params, cfg);
		}
	}
 }();

/**
 * 扩展属性面板，根据cfg所传入的tabName属性，
 * 查询对应配置表中的扩展记录，并根据服务器响应的数据动态添加输入组件。
 */
ExtAttrForm = Ext.extend( Ext.Panel ,{
	
	//
	params: {},
	cfg: null,
	func: null,
	funcScope:null,
	
	url: Constant.ROOT_PATH + "/commons/x/BusiCommon!extAttrForm.action",
	
	constructor: function(params, cfg, func, funcScope){
		this.params = params;
		this.cfg = cfg;
		this.funcScope = funcScope;
		this.func = func;
		if(funcScope){//funcScope不为空才给再加上个回调.
			if(!this.func){
				this.func = function(){
					var comboes = this.findByType("paramcombo");
					for(var index =0;index<comboes.length;index++){
						var combo = comboes[index];
						var val = combo.getValue();
						combo.setValue('');
						combo.setValue(val);
					}
				}
			}else{
				this.func = this.func.createSequence(function(){
					var comboes = this.findByType("paramcombo");
					for(var index =0;index<comboes.length;index++){
						var combo = comboes[index];
						var val = combo.getValue();
						combo.setValue('');
						combo.setValue(val);
					}
				},this.funcScope);
			}
		}
		this.initData();
		ExtAttrForm.superclass.constructor.call(this,{
			border: false,
			layout: 'column',
			baseCls: 'x-plain',
			anchor: '100%',
			defaults: {
				bodyStyle: "background:#F9F9F9",
				anchor: '100%'
			}
		});
	},
	// 初始化数据
	initData: function(){
		Ext.Ajax.request({
			url: this.url,
			scope: this,
			params: this.params,
			success: function(res , ops){
				var data = Ext.decode(res.responseText);
				this.callback(data);
			}
		});
	},
	callback: function(data){
		this.addItems(data); 
		this.doLayout();
		App.form.initComboData( this.findByType("paramcombo"),this.func,this.funcScope);
	},
	//根据data为TabPanel添加item
	addItems: function( attrs ){
		if(!attrs || attrs.length === 0) return ;
		var fields = [];
	    for(var j= 0 ;j< attrs.length ; j++){
	    	fields[j] = this.createField(attrs[j]);
	    }
	    var items = fields.split(this.cfg.length);
		//根据配置
		for(var i = 0; i< this.cfg.length ; i++){
			this.cfg[i]["items"] = items[i];
		    this.add(this.cfg[i]);
		}
	},
	//动态创建field
	createField: function( attr ){
		var o = {
			xtype: attr["input_type"],
			anchor: '89%',
			value:attr['default_value'],
			fieldLabel: attr["attribute_name"],
            allowBlank: attr["is_null"] == 'F'? false: true
		};
		if(attr["input_type"] == 'paramcombo'){
			Ext.apply( o , {
				paramName: attr["param_name"],
				defaultValue: attr["edit_value"],
				hiddenName: this.params.prefixName + attr["col_name"]
			});
		}else if(attr["input_type"] == 'textarea'){
			o["name"] = this.params.prefixName + attr["col_name"];
			if(attr["edit_value"]){
				o["value"] = attr["edit_value"];
			}
			o["height"] = 40;
		}else{
			o["name"] = this.params.prefixName + attr["col_name"];
			if(attr["edit_value"]){
				o["value"] = attr["edit_value"];
			}
		}
		return o;
	}
});

/**
 * 根据表明及主键值，获取扩展属性值
 * 
 */
ExtAttrView = Ext.extend( ExtAttrForm , {
	
	url: Constant.ROOT_PATH + "/commons/x/BusiCommon!extAttrView.action",
	createField: function( attr ){
		var o = {
			xtype: "label",
			anchor: '90%',
			fieldLabel: attr["attribute_name"],
			text: attr["display_text"],
			style: 'margin-top: 3px;'
		};
		return o;
	},
	callback: function(groups){
		this.addItems(groups); 
		this.doLayout();
	}
});

/**
 * 根据表名及主键值，获取扩展属性信息。
 */
ExtAttrUpdate = Ext.extend( ExtAttrForm , {
	url: Constant.ROOT_PATH + "/commons/x/BusiCommon!extAttrView.action"
})
