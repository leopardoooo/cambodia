/** boss-core简体中文语言包 */
BCLang = {}
//主页模块
BCLang.home = {
	topWelcome: "当前操作员",
	searchTabs:["名称编号","设备编号","安装地址","电话号码","多条件搜索"],
	searchTip:"客户编号|受理编号|银行账号|宽带账号",
	searchBtns:["搜索","缴费"],
	main: {
		tabs: ["客户信息","单位信息", "用户信息", "账户信息", "缴费记录", "单据信息", "业务流水","指令信息", "账单信息"],
		cust: {
			base: {_title: "基本信息",name: "客户名称",busiId: "受理编号",openDate: "开户日期",addr: "地址",status: "客户状态",
				type: "客户类型",certType: "证件类型",certNum: "证件号码",linkMan: "联系人",tel: "固定电话",barthday: "出生日期",
				mobile: "手机号码",areaCateory: "区域小区",houseNetType: "小区网络类型",houseManager: "小区客户经理",
				houseOptr: "小区运维人员",postalAddr: "邮寄地址", remark: "备注"
			},
			device: {
				_title: "设备信息"
			}
		}
	}
}