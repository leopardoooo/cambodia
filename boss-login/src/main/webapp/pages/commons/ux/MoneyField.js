Ext.ns("Ext.ux");

/**
 * MoneyField 统一对Boss系统的金额封装的控件，
 */
Ext.ux.MoneyField = Ext.extend( Ext.form.NumberField , {
	
	vtype: 'money',
	style: Constant.MONEY_FIELD_STYLE,
	firstInit: true ,
 	/**
 	 * 所传入的金额值按分作单位，将分转换为元
 	 * @param type String/Number
 	 */ 
 	setValue : function(v){
 		if(this.firstInit){
 			v = Ext.util.Format.convertToYuan(v);
 			this.firstInit = false;
 		}
        return Ext.ux.MoneyField.superclass.setValue.call(this, v) ; 
    },
    /**
     * 将返回值处理为元，将控件值元，转换为分。
     */
    getMoneyValue: function(){
    	return Ext.util.Format.convertToCent(this.getValue());
    }
});

Ext.reg("moneyfield", Ext.ux.MoneyField );