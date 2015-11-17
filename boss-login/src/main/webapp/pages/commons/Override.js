
/*****************************************************************
 * 覆盖Ext相关组件的默认值
 * */
 
//指定Ext空图片的路径
Ext.BLANK_IMAGE_URL = "/" + Constant.ROOT_PATH_LOGIN + '/components/ext3/resources/images/default/s.gif';
//初始化消息提示
Ext.QuickTips.init();
Ext.QuickTips.showDelay =50;
Ext.Msg.maxWidth = 400;
Ext.Msg.minWidth = 230;
FormFieldWidth = 130;

new Ext.KeyMap(document,
				[{key : Ext.EventObject.BACKSPACE,
					fn : function(key, e) {
						var t = e.target.tagName;
						if (t !== "INPUT" && t !== "TEXTAREA") {
							e.stopEvent();
						}
					}
				}]
		);  

// store 字段中文排序 补丁
 /*Ext.data.Store.prototype.applySort = function() {
	if (this.sortInfo && !this.remoteSort) {
		var s = this.sortInfo, f = s.field;
		var tmp = this.fields.get(f);
		if(!tmp || tmp.sortType){
			return;
		}
		var st = tmp.sortType;
		var fn = function(r1, r2) {
			var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
			// 添加:修复汉字排序异常的Bug
			if (typeof(v1) == "string") { // 若为字符串，
				return v1.localeCompare(v2);// 则用 localeCompare 比较汉字字符串, Firefox
											// 与 IE 均支持
			}
			// 添加结束
			return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
		};
		this.data.sort(s.direction, fn);
		if (this.snapshot && this.snapshot != this.data) {
			this.snapshot.sort(s.direction, fn);
		}
	}
};*/  

// ajax 请求超时时间
Ext.apply( Ext.data.Connection.prototype, {
	timeout:300000
});

////当绑定store的组件销毁时,也同时销毁store
//Ext.apply(Ext.data.Store.prototype, {
//	autoDestroy:true
//});

/*******************************************************************************
 * 为Ext组件添加系统默认属性值
 */
// TextField
Ext.apply( Ext.form.TextField.prototype , {
	selectOnFocus : true ,
	height:20,
	vtype : 'specChar'
});

// Field
Ext.apply(Ext.form.Field.prototype , {
	msgTarget: "qtip",
	getValue : function(){
		if(!this.rendered) {
			if(this.value && this.value.trim){
				return this.value.trim();
			}else{
				return this.value;
			}
        }
        var v = this.el.getValue();
        if(v === this.emptyText || v === undefined){
            v = '';
        }
        return v.trim();
	}
});

Ext.apply(Ext.form.ComboBox.prototype,{
	 getValue : function(){
        if(this.valueField){
        	if(Ext.isDefined(this.value)){
        		if(Ext.isString(this.value)){
        			return this.value.trim();
        		}else{
        			return this.value;
        		}
        	}else{
        		return '';
        	}
        }else{
            return Ext.form.ComboBox.superclass.getValue.call(this);
        }
    }
})


//Ext.apply(Ext.data.Record.prototype,{
//	get : function(name){
//		return this.data[name].trim();
//	}
//});

//combo
Ext.apply(Ext.form.ComboBox.prototype , {
	mode: 'local',
	listEmptyText: 'empty data',
	minChars: 0,
	editable: false,
	typeAhead: true,
    triggerAction: 'all',
	selectOnFocus : true,
	listWidth: 200,
	width:FormFieldWidth-8
})
// ColumnModel 
Ext.grid.ColumnModel.prototype.defaults = {
	width: 100 ,
    sortable: true ,
	menuDisabled: false,
	align:'center'
};
Ext.apply(Ext.grid.ColumnModel.prototype,{
	isMenuDisabled:function(col){
		if(this.config[col])
        	return !!this.config[col].menuDisabled;
        else
        	return false;
    }
});

//CheckboxSelectionModel
Ext.apply(Ext.grid.CheckboxSelectionModel.prototype,{
	onHdMouseDown: function(e , t){
		if(t.className == 'x-grid3-hd-checker'){
            e.stopEvent();
            var hd = Ext.fly(t.parentNode);
            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
            if(isChecked){
                hd.removeClass('x-grid3-hd-checker-on');
                this.clearSelections();
            }else{
                hd.addClass('x-grid3-hd-checker-on');
                this.onBeforeSelAllHanlder();
                this.selectAll();
            }
        }
	},
	onBeforeSelAllHanlder: function(){}
});

// GridPanel
Ext.apply( Ext.grid.GridPanel.prototype , {
//	iconCls: 'icon-grid',
	stripeRows: true,
	autoScroll: true,
	//loadMask: {msg: '数据加载中...'},
	sm: new Ext.grid.RowSelectionModel()
});

/*gridpanel单元格复制*/
//if  (!Ext.grid.GridView.prototype.templates) {    
//    Ext.grid.GridView.prototype.templates = {};
//}
//Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(    
//     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,    
//     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,    
//     '</td>'
//);

//默认显示15条
Ext.apply( Ext.PagingToolbar.prototype ,{
	pageSize: Constant.DEFAULT_PAGE_SIZE ,
	displayInfo: true,
	displayMsg: lbc("common.pageDisplayMsg"),
	emptyMsg: lbc("common.emptyMsg")
});


// FormPanel 
Ext.apply( Ext.form.FormPanel.prototype , {
	autoScroll: true,
	labelAlign: 'right',
	defaults: {
		width: FormFieldWidth
	},
	//获取不为空的字段值
	getNotNullValues: function(bool){
		var all = this.getForm().getValues();
		var ps = null;
		for(var key in all){
			if(!Ext.isEmpty(all[key])){
				ps = ps==null?{}:ps ;
				ps[key] = all[key];
			}
		}
		if( bool && ps ){
			return Ext.util.JSON.encode( ps );
		}
		return ps ;
	}
});

//BasicForm
Ext.apply( Ext.form.BasicForm.prototype , {
	getChangeValues:function(){
		var v = [];
		var i=0;
		this.items.each(function(f){
			if(f.isDirty()){
				t = {};
				t.columnName = f.getName();
				if(Ext.isDate(f.getValue())){
					t.newValue = f.getValue().format('Y-m-d');
				}else{
					t.newValue = f.getValue();
				}
				if(Ext.isDate(f.originalValue)){
					t.oldValue = f.originalValue.format('Y-m-d');
				}else{
					t.oldValue = f.originalValue;
				}
				
				v[i] = t;
				i++;
			}
		});
		return v;
	},
	getValues : function(asString){
        var fs = Ext.lib.Ajax.serializeForm(this.el.dom);
        if(asString === true){
            return fs;
        }
        var all = Ext.urlDecode(fs);
        for(var key in all){
        	if(all[key]){
        		all[key] = all[key].trim();
        	}
        }
        return all;
    }
});

//Window
Ext.apply( Ext.Window.prototype , {
	closeAction: 'hide',
	modal: true, 
	maximizable: true
});

// MessageBox
Ext.Msg.title = lbc("common.alertTitle");
Alert= function( msg , fn , scope ){
	var m = Ext.Msg ;
	return m.show({
		title: m.title,
		msg: msg ,
		icon: m.INFO ,
		buttons: m.OK,
		closable : false,
		fn : fn ,
		scope: scope 
	});
}; 
Confirm= function( msg , scope , yesHanlder , noHanlder ){
	return Ext.Msg.confirm( Ext.Msg.title, msg , function(text){
		if(text == "yes" && yesHanlder){
			yesHanlder.call( scope );
		}else if( text=="no" && noHanlder){
			noHanlder.call( scope );
		}
	}, scope );
}
Show = function( anim ){
	return Ext.MessageBox.show({
       msg: lbc("common.submitingText"),
       wait: true,
       waitConfig: { interval: 150 },
       icon:'icon-download',
       animEl: anim
   });
}

Ext.applyIf( Array.prototype, {
	
	/**
	 * 给定指定的数组，拆分指定数量的子数组,
	 */
	split: function(count){
		if(this.length == 0 || !count || count == 0) return ;
		var target = [count],arr = this;
		var i = 0, 
			len = parseInt((arr.length/count).toFixed()),
			index = 0;
		if(len === 0) len = 1;
		while(true){
			target[index] = arr.slice(i , i + len);
			index++;
			i = i + len;
			if(i >= arr.length){
				break;	
			}
		}
		return target;
	},
	contain: function(value){
		if(this.length == 0 || !value || value == '') return false;
		
		var arr = this;
		for( var i=0;i<arr.length;i++){
			if(value == arr[i]){
				return true;
			}
		}
		
		return false;
	}
});

/*
 * <p> 将给定的值按照一定规则转换为分，给定的值单位为：元 </p>
 * 参数最多保留两位小数。将忽略其它小数
 * @param {String/Number} v 金额的字符或数值
 */
Ext.util.Format.convertToCent = function( v ){ 
	if(isNaN(v)) return "";
	var s = String(parseFloat(v).toFixed(2));
   	var slen = s.indexOf('.');
   	if( slen == -1){
   		return s + "00";
   	} 
   	if(s.substr( slen ).length == 1)
   		return s + "0";
   	return s.replace(/\./i, '');
}

/*
 * <p> 左补足字符串 </p>
 * 
 * @param {String} v 字符串
 * @param {Number} l 补足长度
 * @param {Char} c 补足字符
 * 			
 */
Ext.util.Format.lpad = function( v,l,c ){ 
	 var len = v.toString().length;
	    while(len < l) {
	        v = c + v;
	        len++;
	    }
	 return v;
}

Ext.util.Format.lpadRight = function( v,l,c ){ 
	 var len = v.toString().length;
	    while(len < l) {
	        v = v + c;
	        len++;
	    }
	 return v;
}

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
Ext.util.Format.toDecimal= function (x) {   
    var f = parseFloat(x);   
    if (isNaN(f)) {   
        return;   
    }   
    f = Math.round(x*100)/100;   
    return f;   
}   
/**
 * 返回“是”，“否”文本
 */
Ext.util.Format.booleanRenderer = function( v ){
	if(v == "T" || v == "true" || v == "1"){
		return "<span style='color: green'>"+ lbc("common.yes") +"</span>";
	}else{
		return "<span style='color: red'>"+lbc("common.no")+"</span>";
	}
}
/**
 * 封装一个金额的渲染函数，包含￥字符，{@link Ext.ux.MoneyColumn}
 */
Ext.util.Format.moneyRenderer = function( v ){
	if(isNaN(v)) return "" ;
	return "￥" + Ext.util.Format.convertToYuan(v) ;
}
/**
 * 将金额转换为大写字符 
 */
Ext.util.Format.convertToChinese = function( num){
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

/*
 * 日期显示为年月日
 */
Ext.util.Format.dateFormat = function(v){
	if(!v) return "" ;
	if(Ext.isDate(v)){
		return v.format('Y-m-d');
	}
	var date = Date.parseDate(v,'Y-m-d H:i:s');
	return date.format('Y-m-d');
}
Ext.util.Format.dateFormatHIS = function(v){
	if(!v) return "" ;
	if(Ext.isDate(v)){
		return v.format('Y-m-d H:i:s');
	}
	var date = Date.parseDate(v,'Y-m-d H:i:s');
	return date.format('Y-m-d h:i:s');
}
/**
 * 计算当前日期与传入日期的天数
 */
Ext.util.Format.DateDiffToday = function(v){
	if(Ext.isEmpty(v))return '';
	var sDate1,sDate2,iDay;
	  sDate1 =new Date(Date.parse(v.replace(/-/g,"/")));
	  sDate2 = nowDate();
	  iDay = parseInt((sDate2 - sDate1) / 86400000);
	  if(iDay==0){
	  	iDay="当天";
	  }
	return iDay;
}
/**
 * 用户根据状态显示时间
 * @param {} t 状态
 * @param {} b 变更状态的时间
 * @param {} e 预报停时间
 * @return {String}
 */
Ext.util.Format.DateWithToday = function(t,b,e){
	if(t=='REQSTOP'){
		if(!e){
			if(Ext.util.Format.dateFormat(b) == Ext.util.Format.dateFormat(new Date()))
			return "当天";
			return "";
		}else{
		 	return Ext.util.Format.dateFormat(e);
		}
	}else{
		if(!e) return "" ;
		return Ext.util.Format.dateFormat(e);
	}
}

/**
 * 判断该字符串有几个字节
 */
Ext.util.Format.getByteLen = function(v){
	var n = v.length;
	var len = v.length;
	for ( var i=0; i<len; i++ ){
		if ( v.charCodeAt(i) <0 || v.charCodeAt(i) >255 ){
			   ++n;
		}
	}
	return n;
}
/**
 * 截取指定长度字节，中文按两个字符计算
 */
Ext.util.Format.subStringLength = function(str,len){	
	var result="";
	for (var i=0 ;i<len;i++){
		result = result+str.charAt(i);
		if (str.charCodeAt(i)<0||str.charCodeAt(i)>255) 
			len--
	}
	return result;
}


/**
 * 格式化费用，显示元
 * @param {} value
 * @return {Number}
 */
Ext.util.Format.formatFee = function(value){
	if(Ext.isEmpty(value)){
		return 0;
	}else{
		return parseFloat(Ext.util.Format.convertToYuan(value));
	}
}
Ext.util.Format.getMonths = function(date1 , date2){
    //用-分成数组
    date1 = date1.split("-");
    date2 = date2.split("-");
    //获取年,月数
    var year1 = parseInt(date1[0]) , 
        month1 = parseInt(date1[1]) , 
        year2 = parseInt(date2[0]) , 
        month2 = parseInt(date2[1]) , 
        //通过年,月差计算月份差
        months = (year2 - year1) * 12 + (month2-month1);
    return months;    
}
Ext.util.Format.getDays = function(strDateStart, strDateEnd) {// sDate1和sDate2是2004-10-18格式
	var oDate1, oDate2, iDays;
	oDate1 = strDateStart.split("-");
	oDate2 = strDateEnd.split("-");
	var strDateS = new Date(oDate1[0], oDate1[1] - 1, oDate1[2]);
	var strDateE = new Date(oDate2[0], oDate2[1] - 1, oDate2[2]);
	var i = 1;
	if (strDateS.getTime() > strDateE.getTime()) {
		i = -1;
	}
	iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60 / 24)// 把相差的毫秒数转换为天数
	return iDays * i;
}

Ext.util.Format.addMoth = function(d, m) {
	var date = Date.parseDate(d,"Y-m-d");
	var ds = d.split('-'), _d = ds[2] - 0;
	var nextM = new Date(date.getFullYear(), date.getMonth()+1+m, 0);
	var max = nextM.getDate();
	date = new Date(nextM.getFullYear(), nextM.getMonth(), Ext.util.Format.toChangep(_d > max? max: _d));
	return date.format("Y-m-d");
}

Ext.util.Format.addTime = function(time, m,d) {
	var date = Date.parseDate(time,"Y-m-d");
	var nextM = new Date(date.getFullYear(), date.getMonth()+m, date.getDate()+d);
	return nextM.format("Y-m-d");
}

Ext.util.Format.toChangep = function(s) {
    return s < 10 ? '0' + s: s;
}

Ext.util.Format.formatToFen = function(value){
	var finalFee = 0;
	if(Ext.isEmpty(value)){
		return finalFee;
	}else{
		value = value + "";//转成string
		var fee = value.split(".");
		if(fee.length > 1){
			finalFee = parseFloat(fee[0]) * 100;
			//如果小于0
			if(value < 0){
				//小数点后面只有1位
				if(fee[1].length == 1){
					finalFee = finalFee - parseFloat(fee[1]) * 10;
				}else{
					finalFee = finalFee - parseFloat(fee[1].substring(0,2));
				}
			}else{
				if(fee[1].length == 1){
					finalFee = finalFee + parseFloat(fee[1]) * 10;
				}else{
					finalFee = finalFee + parseFloat(fee[1].substring(0,2));
				}
				
			}
			
		}else{
			finalFee = parseFloat(fee[0]) * 100;
		}
		return finalFee;
	}
}
/**四舍五入*/
Ext.util.Format.round = function(num,n){
	return Math.round(num*Math.pow(10,n))/Math.pow(10,n);
}
/**不小余它的最小整数*/
Ext.util.Format.ceil = function(num,n){
	return Math.ceil(num*Math.pow(10,n))/Math.pow(10,n);
}
/**
 * 状态字体显示
 * @param {} v
 * @return {}
 */
Ext.util.Format.statusShow = function(v){
	if(v=="正常" || v == "使用" || v == 'Normal' || v == 'Use'){
		return "<span style='font-weight:bold'>"+v+"</span>";
	}else if(v == "失效" || v == "报停" || v == "欠费停" || v == "关联停" 
		|| v== "报废" || v == 'Invalid' || v == 'Termination Report' || v == 'Arrearage Stop' || v == 'Associated stop' || v == 'Scrapped'){
		return "<span style='color:red;font-weight:bold'>"+v+"</span>";
	}else if(v== "预开户" || v == "未支付" || v == "空闲" || v == "未打印"
		|| v == 'Pre-Opening' || v == 'UnPaid' ||  v == 'Idle' || v == 'NotPrint'){
		return "<span style='color:green;font-weight:bold'>"+v+"</span>";
	}else if(v == "欠费未停" ||  v == 'Arrears did not stop'){
		return "<span style='color:#BE8415;font-weight:bold'>"+v+"</span>";
	}else if(v == "已支付" || v == "已打印" || v == 'Paid' || v == 'Print'){
		return "<span style='color:blue;font-weight:bold'>"+v+"</span>";
	}else if (v){
		return "<span style='color:purple;font-weight:bold'>"+v+"</span>";
	}else{
		return "";
	}
}

Ext.util.Format.formatIdOrPhone= function (info) {   
    if(Ext.isEmpty(info) || info.length <5){
		return info;
	}
	return info.substr(0,info.length-6) + '**' + info.substr(info.length-4,info.length-1); 
}

/**
 * 表单非空字段前加*
 */
Ext.form.Field.prototype.initComponent = function(){        
        if(this.allowBlank==false){
            this.fieldLabel='<font color="red">*</font>'+' ' +this.fieldLabel;
        }
        Ext.form.Field.superclass.initComponent.call(this);
        this.addEvents(
            
            'focus',
            
            'blur',
            
            'specialkey',
            
            'change',
            
            'invalid',
            
            'valid'
        );
};


Ext.tree.TreeNodeUI.prototype.updateExpandIcon = function(){//当树的节点变叶子时加变叶子图标
        if(this.rendered){
            var n = this.node,
                c1,
                c2,
                cls = n.isLast() ? "x-tree-elbow-end" : "x-tree-elbow",
                hasChild = n.hasChildNodes();
            if(hasChild || n.attributes.expandable){
                if(n.expanded){
                    cls += "-minus";
                    c1 = "x-tree-node-collapsed";
                    c2 = "x-tree-node-expanded";
                }else{
                    cls += "-plus";
                    c1 = "x-tree-node-expanded";
                    c2 = "x-tree-node-collapsed";
                }
                if(this.wasLeaf){
                    this.removeClass("x-tree-node-leaf");
                    this.wasLeaf = false;
                }
                if(this.c1 != c1 || this.c2 != c2){
                    Ext.fly(this.elNode).replaceClass(c1, c2);
                    this.c1 = c1; this.c2 = c2;
                }
            }else{
                if(!this.wasLeaf){
                    Ext.fly(this.elNode).replaceClass("x-tree-node-expanded", "x-tree-node-collapsed");
                    Ext.fly(this.elNode).addClass('x-tree-node-leaf');
                    delete this.c1;
                    delete this.c2;
                    this.wasLeaf = true;
                }
            }
            var ecc = "x-tree-ec-icon "+cls;
            if(this.ecc != ecc){
                this.ecNode.className = ecc;
                this.ecc = ecc;
            }
        }
    };
    
Ext.apply(Ext.Button.prototype,{
	disableSelfCtrl:false,
	listeners:{
		scope:this,
		click:function(btn,e){
			/**
			 * 屏蔽分页按钮
			 */
			
			if(btn.iconCls && btn.iconCls.indexOf('x-tbar-page') > -1){
				return;
			}
			
			//屏蔽datefield中"今天"按钮
			if(!btn.scope || btn.scope.ctCls != 'x-menu-date-item'){
				if(!btn.disableSelfCtrl){
					btn.disable();
					btn.enable.defer(1000,btn);
				}
			}
		}
	}
});
/**
 * 给string添加replaceAll方法.
 */
Ext.apply(String.prototype,{
	replaceAll:function(former,newStr){
	   var reg = new RegExp(former,"g");
	   return this.replace(reg,newStr);
	}
});

/**
 * 给string添加replaceAll方法.
 */
Ext.apply(String.prototype,{
	filterSpecChar:function(){
		var map = {
			'$':'\$',
			'(':'\(',
			')':'\)',
			'*':'\*',
			'+':'\+',
			'.':'\.',
			'[':'\[',
			']':'\]',
			'?':'\?',
			'^':'\^',
			'{':'\{',
			'|':'\|'
		};
		var str = this;
		for (var key in map) {
			str = str.replace(key, '\\' + key);
		}
	   return str;
	}
});
