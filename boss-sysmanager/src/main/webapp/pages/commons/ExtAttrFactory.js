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
		 */
		gExtForm: function(params, cfg){
			cfg = cfg || defaultCfg;
			if(!params || !params.tabName){
				alert("请配置params参数，包括扩展属性的表名!");
				return ;
			}
			return new ExtAttrForm(params, cfg);
		},
		/**
		 * 生成扩展信息修改面板，Update面板，
		 */
		gExtEditForm: function(params, cfg){
			cfg = cfg || defaultCfg;
			if(!params || !params.tabName || !params.pkValue){
				alert("请配置params参数，包括扩展属性的表名、主键值!");
				return ;
			}
			return new ExtAttrUpdate(params, cfg);
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
	
	url :root + '/config/ExtendTable!findTabExtAttr.action' ,
	
	constructor: function(params, cfg){
		this.params = params;
		this.cfg = cfg;
		this.initData();
		ExtAttrForm.superclass.constructor.call(this,{
			border: false,
			layout: 'column',
			baseCls: 'x-plain',
			anchor: '100%',
			defaults: {
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
		App.form.initComboData( this.findByType("paramcombo"));
	},
	//根据data为TabPanel添加item
	addItems: function( attrs ){
		if(!attrs || attrs.length === 0) return ;
		var fields = [];
	    for(var j= 0 ;j< attrs.length ; j++){
	    	fields[j] = this.createField(attrs[j]);
	    }
	    items = fields.split(this.cfg.length);
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
			fieldLabel: attr["attribute_name"],
            allowBlank: attr["is_null"] == 'F'? false: true
		};
		if(!this.params.prefixName){
			this.params.prefixName = '';
		}
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


