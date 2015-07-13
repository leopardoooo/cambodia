/**
 *
 *
 * <p>打印组件,根据Boss的系统打印业务，扩展的一个组件
 * 根据所传入的数据，及打印模板，根据模板将数据转化为模板的变量，进行打印</p>
 *
 *  @param {Array} data ,数据的格式定义如： [{
 * 	template: "<?xml version=\"1.0\" encoding=\"GBK\"?> "
 *			 +"<print-info> "
 *			 +"	<default-setting>"
 *			 +"		<width>1000</width><height>800</height><font>12</font>"
 *			 +"	</default-setting>"
 *			 +"	<items>"
 *			 +"     <printitem><x>400</x><y>30</y><data>{title}</data></printitem>"
 *			 +"		<printitem><x>200</x><y>50</y><data>客户名: {cust_name}</data></printitem>"
 *			 +"	</items>"
 *			 +"</print-info>", 
 *	data: { title: '确认单', cust_name: 'zhangsan' }
 * }]
 */

PrintTools = {};
Ext.apply( PrintTools , {
	
	/**
	 * <p>根据所传入的模板字符串及数据,使用Ext.XTemplate替换变量数据，
	 *  将替换后的字符串返回</p>
	 * @param {String} templateTab 符合Ext.XTemplate规范的模板字符串
	 * @param {Object} data 替换模板参数的可用数据源
	 */ 
	getContent: function( templateTab , data){
		var tpl = new Ext.XTemplate( templateTab);
		return tpl.applyTemplate(data);
	}
});

/** 
 * 为Ext XTemplate的添加几项额外的辅助函数
 */ 
Ext.apply(Ext.XTemplate.prototype , {
	chinese: function( v ){
		v = Ext.util.Format.convertToYuan(v);
		return Ext.util.Format.convertToChinese(v);
	}
});
