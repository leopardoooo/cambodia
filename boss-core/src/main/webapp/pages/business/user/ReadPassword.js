function GetPassword(iType) {
	
	KdbKbdControl1.iComPort = 1; //PC串口设置
	KdbKbdControl1.iBaudRate = 9600; //PC串口波特率
	KdbKbdControl1.iTimeOut = 6; //超时时间(秒)
	//KdbKbdControl1.iBpPort=1;      //串口扩展盒端口
	KdbKbdControl1.iBp_K_1200 = 0;//  iBp_K_1200=0;     //设置K口为1200波特率的BP盒标识,如果普通密码键盘连接到此口设置为1,否设置为0.

	//加密类型 设置为0是通用版本密码键盘(12SKB) ,设置为1表示DES密码键盘
	//加密类型，3表示为3DES密码键盘
	KdbKbdControl1.iEncryptType = 0; //加密类型 设置为0是通用版本密码键盘(12SKB) ,设置为1表示DES密码键盘
	KdbKbdControl1.iDesPinLength = 6; //DES密码键盘长度设置
	KdbKbdControl1.iPadTimes = iType; //读密码键盘次数

	KdbKbdControl1.ReadwebPin(); //读取密码键盘函数
	if (KdbKbdControl1.iStatus == 22) {
		alert("读密码错误或者超时");
		return;
	}

	switch (iType) {
	case 1:
		Ext.getCmp('password').setValue(KdbKbdControl1.cPwdData);
		break;
	case 2:
		Ext.getCmp('repassword').setValue(KdbKbdControl1.cPwdData);
		break;
	}
}

var loadActiveX = function(rootPath){
	if(App.getApp().data.optr.county_id == '0501'){
		Ext.get('activeId').dom.innerHTML='<OBJECT id=KdbKbdControl1 codeBase="'+rootPath+'/components/cab/stwebdll.dll#version=1,0,0,4" height="1" width="1" classid="clsid:00A5A260-956A-49E4-82FF-58CE009742C5"></OBJECT>';
	}
}