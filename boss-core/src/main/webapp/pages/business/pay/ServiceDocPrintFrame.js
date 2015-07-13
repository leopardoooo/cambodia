//打印常量定义
DOC = function(){
	var BR= "<div class='split_1'>&nbsp;</div>",
		PAGE = "<div style='page-break-before: always; height: 1px;'>&nbsp;</div>";
		
	return {
		br: function(){
			return BR;
		},
		page: function(){
			return PAGE;
		},
		/**
		 * <p>根据所传入的模板字符串及数据,使用Ext.XTemplate替换变量数据，
		 *  将替换后的字符串返回</p>
		 * @param {String} templateTab 符合Ext.XTemplate规范的模板字符串
		 * @param {Object} data 替换模板参数的可用数据源
		 */ 
		transTpl: function( templateTab , data){
			var tpl = new Ext.XTemplate( templateTab);
			return tpl.applyTemplate(data);
		}
	};
}();


/*
 * <p> 将给定的分值，转化为按元做单位,保留2位小数点
 * example introduce 10 , return 0.1, and 1000 return  10.00</p>
 * @param {String/Number} v 金额的字符或数值
 */
Ext.util.Format.convertToYuan = function( v ){
	if(isNaN(v)) return "" ;
	v = parseInt(v*100)/10000;
	return v.toFixed(2);

}

Ext.util.Format.formatIdOrPhone= function (info) {   
    if(Ext.isEmpty(info) || info.length <5){
		return info;
	}
	return info.substr(0,info.length-6) + '**' + info.substr(info.length-4,info.length-1); 
}
//格式化银行账号,只显示前后各四位
Ext.util.Format.formatBankAccount= function (info) {   
    if(Ext.isEmpty(info) || info.length <5){
		return info;
	}
	return info.substr(0,4) + '******' + info.substr(info.length-4,info.length-1); 
}

/**
 * 将金额转换为大写字符 
 */
Ext.util.Format.convertToChinese = function( num ){
  num = Ext.util.Format.convertToYuan(num);
  var strOutput = "";   
  var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';   
  num += "00";   
  var intPos = num.indexOf('.');   
  if (intPos >= 0)   {
    num = num.substring(0, intPos) + num.substr(intPos + 1, 2);   
  }
  strUnit = strUnit.substr(strUnit.length - num.length);   
  for (var i=0; i < num.length; i++){
    strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i,1),1) + strUnit.substr(i,1);   
  }
  return strOutput.replace(/零角零分$/, '整')
   				  .replace(/零[仟佰拾]/g, '零')
   				  .replace(/零{2,}/g, '零')
   				  .replace(/零([亿|万])/g, '$1')
   				  .replace(/零+元/, '元')
   				  .replace(/亿零{0,3}万/, '亿')
   				  .replace(/^元/, "零元"); 
}