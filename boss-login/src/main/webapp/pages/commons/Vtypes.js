/**
 * 新增vtype
 */
Ext.apply(Ext.form.VTypes, {
	
	//重写alphanum
	loginName : function(v){
		return /^[0-9a-zA-z_@]+$/.test(v);
	},
	
	loginNameText : "只能输入数字，字母下划线或@",
	
	timeHIS : function(v){
        return /^(([01]\d)|(2[0-3]))((0\d)|([1-5]\d)){2}$/.test(v);
    },
    timeHISText:'请输入时分秒,6位数字,例：19点45分-194500',
	
	invoiceBook : function(v){
        return /^[0-9]*$/.test(v);
    },
    invoiceBookText:'请输入数字',
    
    invoiceId : function(v){
        return /^[0-9]+$/.test(v);
    },
    invoiceIdText:'请输入数字',
 
    invoiceCode : function(v){
        return /^[0-9]{12}$/.test(v);
    },
    invoiceCodeText:'请输入12位数字',
    
    numZero : function(v){
        return /^[0-9]+$/.test(v);
    },
    numZeroText:'请输入数字',
    
	//整数或小数
	num : function(v){
		return /^([1-9]\d*|[1-9]\d*\.\d+|0\.\d*[1-9]\d*|0?\.0+|0)$/.test(v);
	},
	numText : '请输入正整数或小数',
	
	specChar : function(v){
		if(/.*\u0027.*/gi.test(v)){
			return false;
		}
		if(Ext.isString(v) && v.trim().length == 0){
			return false;
		}
		return true;
	},
	specCharText : '请不要输入单引号或者只输入空格',
	
	singleChar : function(v){
		if(/[\u4E00-\u9FA5].*/.test(v)){
			return false;
		}
		return true;
	},
	singleCharText : '不能输入中文',
	
	//ip地址
    IPAddress:  function(v) {
  	//  return /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(v);
     var reg = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
     if(reg.exec(v) != null)
     {
       if(RegExp.$1<0 || RegExp.$1>255) return false;
       if(RegExp.$2<0 || RegExp.$2>255) return false;
       if(RegExp.$3<0 || RegExp.$3>255) return false;
       if(RegExp.$4<0 || RegExp.$4>255) return false;
     }
     else
     {
       return false
     }
     return true;
    },
    IPAddressText: '必须是一个合法的ip',
    IPAddressMask: /[\d\.]/i,
    
    //身份证
    IDNum : function(_v) {
		// return /(^[0-9]{17}([0-9]|[Xx])$)|(^[0-9]{17}$)/.test(_v);
		var area = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		}
		var Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = _v.split("");
		// 地区检验
		if (area[parseInt(_v.substr(0, 2))] == null) {
			this.IDNumText = "身份证号码地区非法!!";
			return false;
		}
		// 身份号码位数及格式检验
		switch (_v.length) {
			case 15 :
				if ((parseInt(_v.substr(6, 2)) + 1900) % 4 == 0
						|| ((parseInt(_v.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(_v
								.substr(6, 2)) + 1900)
								% 4 == 0)) {
					ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
				} else {
					ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
				}
				if (ereg.test(_v))
					return true;
				else {
					this.IDNumText = "身份证号码出生日期超出范围,格式例如:19860817";
					return false;
				}
				break;
			case 18 :
				// 18位身份号码检测
				// 出生日期的合法性检查
				// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
				// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
				if (parseInt(_v.substr(6, 4)) % 4 == 0
						|| (parseInt(_v.substr(6, 4)) % 100 == 0 && parseInt(_v
								.substr(6, 4))
								% 4 == 0)) {
					ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
				} else {
					ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
				}
				if (ereg.test(_v)) {// 测试出生日期的合法性
					// 计算校验位
					S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
							* 7
							+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
							* 9
							+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
							* 10
							+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
							* 5
							+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
							* 8
							+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
							* 4
							+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
							* 2
							+ parseInt(idcard_array[7])
							* 1
							+ parseInt(idcard_array[8])
							* 6
							+ parseInt(idcard_array[9]) * 3;
					Y = S % 11;
					M = "F";
					JYM = "10X98765432";
					M = JYM.substr(Y, 1);// 判断校验位
					if (M == idcard_array[17]) {
						return true; // 检测ID的校验位
					} else {
						this.IDNumText = "身份证号码末位校验位校验出错,请注意X需要大写";
						return false;
					}
				} else {
					this.IDNumText = "身份证号码出生日期超出范围,格式例如:19860817";
					return false;
				}
				break;
			default :
				this.IDNumText = "身份证号码位数不对,应该为15位或是18位";
				return false;
				break;
		}
    },
    IDNumText: '必须是身份证号码格式，例如：32082919860817201x',
    IDNumMask: /[0-9xX]/i,
    //限制开始时间小于到期时间
    daterange : function(val, field) {
		var date = field.parseDate(val);
		var dispUpd = function(picker) {
			var ad = picker.activeDate;
			picker.activeDate = null;
			picker.update(ad);
		};
		if (field.startDateField) {
			var sd = Ext.getCmp(field.startDateField);
			sd.maxValue = field.customDay ? date.add(Date.DAY, field.customDay) : date;
			if (sd.menu && sd.menu.picker) {
				sd.menu.picker.maxDate = date;
				dispUpd(sd.menu.picker);
			}
		} else if (field.endDateField) {
			var ed = Ext.getCmp(field.endDateField);
			ed.minValue = field.customDay ? date.add(Date.DAY, field.customDay) : date;
			if (ed.menu && ed.menu.picker) {
				ed.menu.picker.minDate = date;
				dispUpd(ed.menu.picker);
			}
		}
		return true;
	},
	//判断密码是否一致
    password : function(val, field) {
        if (field.initialPassField) {
            var pwd = Ext.getCmp(field.initialPassField);
            return (val == pwd.getValue());
        }
        return true;
    },
    passwordText : '密码不一致'
});

