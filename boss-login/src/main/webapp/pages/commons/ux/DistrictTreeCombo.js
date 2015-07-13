Ext.namespace("Ext.ux");

DISTRICT_MODEL_AREA_COUNTY =  "area/county";
DISTRICT_MODEL_AREA_COUNTY_DEPT = "area/county/dept";
DISTRICT_MODEL_AREA_COUNTY_DEPOT = "area/county/depot";
/***
 * 区域、县市、部门的关系树控件,
 * 扩展树控件
 */
Ext.ux.DistrictTreeCombo = Ext.extend( Ext.ux.TreeCombo , {
	// model 分为两种模式 
	// 1：area/county  只包含地区/县市 默认
	// 2：area/county/dept 包含地区/县市/部门
	model: DISTRICT_MODEL_AREA_COUNTY,
	treeUrl: root + "/common/District!queryDistrict.action",
	editable: false,
	hideTrigger: false,
	treeWidth: 240,
	// 初始化List
	initList: function() {
		this.treeParams = { model: this.model };
		Ext.ux.DistrictTreeCombo.superclass.initList.call(this);
	},
	//覆盖原有的查询函数
	doQuery: function(q, forceAll) {
		this.expand();
		this.list.expand( false,false);
	},
	onNodeClick: function(node, e) {
		if(!this.isCanClick(node))
			return ;
		var idArr = [] , textArr = [];
		do{
			if(node == this.list.getRootNode()) 
				break;
			idArr[idArr.length] = node.id;
			textArr[textArr.length] = node.text;
			
			node = node.parentNode;
		}while(node);
		var o = {};
		o[this.displayField] = textArr.reverse() ;
		o[this.valueField] = idArr.reverse() ;
		this.setValue( o );
		this.collapse();
		
		this.fireEvent('select',this,node,node.attributes);
	},
	/*
	 * o格式为如: {
	 *	  id: [0100,0101,1],
	 *	  text: ['南宁',"南宁市","江南营业厅"]
	 *  }
	 * @param ids Object 
	 */
	setValue: function( o ){
		if(!Ext.isObject(o)){
			return ;
		}
		var texts = o[ this.displayField ], 
			ids = o[ this.valueField];
		var _t = texts.join(' / ');
		var _id = ids.join(',');
		//显示隐藏值
		this.addOption( _id , _t);
		//设置显示值
		Ext.ux.DistrictTreeCombo.superclass.setValue.call(this, _id );
	}
});

Ext.reg("districtcombo" , Ext.ux.DistrictTreeCombo);