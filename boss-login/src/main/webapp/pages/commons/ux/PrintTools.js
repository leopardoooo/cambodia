
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
	},
	//分页的DIV
	PRINT_PAGE: "<DIV style=\"page-break-after:always;position:relative;\">{0}</DIV>",
	//转换多行的数据，每行一页
	getPageHTML: function(tpl, dataArr){
		var moreHtml = "", pt = PrintTools;
		for(var i = 0; i< dataArr.length; i++){
			moreHtml += pt.getHTML(tpl, dataArr[i]);
		}
		return moreHtml;
	},
	//单行的数据，并加上打印换页
	getHTML: function(tpl, data){
		var row = PrintTools.getContent(tpl, data);
		return String.format(PrintTools.PRINT_PAGE, row);
	},
	showPrintWindow: function(xmlStr){
	 	App.Print = { HTML: xmlStr };
	 	var url = '/'+Constant.ROOT_PATH_LOGIN+ '/components/cab/PrintFrame.html';
	 	var feature = "left=1px,top=1px,height=10px,width=10px,status=no,toolbar=no,menubar=no,location=no,resizable:no,titlebar: 0,directories: 0";
	 	window.open(url, '', feature);
	 	//var feature = "dialogLeft: 3000px; dialogTop: 3000px;dialogWidth: 1px;dialogHeight:1px;help: no;resizable: no;scroll:no;status:no;unadorned: no;";
	 	//window.showModalDialog(url, xmlStr, feature);
	},
		/**
	 * 使用控件实现套打
	 * @param xmlStr 需要打印的xml文档字符串
	 */
	print: function(xmlStr){
		PrintCtrl.XMLDoc = xmlStr;
		PrintCtrl.Print();
	},
	webPrint: function(xmlStr){
		var url = root + "/pages/print/Print.html";
		window.returnValue = xmlStr;
		
		window.open(url, 'noiePrintWin', "left=1px,top=1px,height=10px,width=10px,status=no,toolbar=no,menubar=no,location=no,resizable:no,titlebar: 0,directories: 0");
	},
	appletPrint: function(papers){
		var paperData = [];
		for(var i = 0 ; i< papers.length; i++){
			paperData.push(papers[i].dataString);
		}
		var paperString = paperData.join("|");
		
		var DOC = parent.document;
		var applet = DOC.getElementById("jsteak");
		if(!applet){
			applet = document.createElement("APPLET");
			applet.setAttribute("id", "jsteak");
			applet.setAttribute("code", "com.jsteak.PrintApplet.class");
			applet.setAttribute("archive", root + "/applet/JSteak.jar");
			applet.setAttribute("style", "position: absolute; left: -100px; top: -100px;");
			applet.setAttribute("width", "1");
			applet.setAttribute("height", "1");
			DOC.body.appendChild(applet);
			//parent.window["jsteakReady"] = function(){
				applet.print(paperString, 900, 600);
			//}
		}else{
			applet.print(paperString, 900, 600);
		}
	},
	isIE: function(){
		return !!(window.attachEvent && navigator.userAgent.indexOf('Opera') === -1);
	}(),
	parsePrintXML: function(xmlStr){
		var root = (new DOMParser()).parseFromString(xmlStr, "text/xml");
		
		var settingNode = root.getElementsByTagName("page-setting")[0];
		var width = settingNode.getElementsByTagName("width")[0].childNodes[0].nodeValue.trim();
		var height = settingNode.getElementsByTagName("height")[0].childNodes[0].nodeValue.trim();
		
		var items = root.getElementsByTagName("item");
		var values = [];
		for(var i = 0; i< items.length; i++){
			var text = items[i].childNodes[0].nodeValue.trim();
			var left = items[i].getAttribute("left").trim();
			var top = items[i].getAttribute("top").trim();
			values.push("text=" + text + "&posX=" + left + "&posY=" + top);
		}
		values.join("^");
		
		return {
			width: width,
			height: height,
			dataString: values.join("^")
		}
	},
	parsePrintXML: function(xmlStr){
		var root = (new DOMParser()).parseFromString(xmlStr, "text/xml");
		
		var settingNode = root.getElementsByTagName("page-setting")[0];
		var width = settingNode.getElementsByTagName("width")[0].childNodes[0].nodeValue.trim();
		var height = settingNode.getElementsByTagName("height")[0].childNodes[0].nodeValue.trim();
		
		var items = root.getElementsByTagName("item");
		var values = [];
		for(var i = 0; i< items.length; i++){
			var text = items[i].childNodes[0].nodeValue.trim();
			var left = items[i].getAttribute("left").trim();
			var top = items[i].getAttribute("top").trim();
			values.push("text=" + text + "&posX=" + left + "&posY=" + top);
		}
		values.join("^");
		
		return {
			width: width,
			height: height,
			dataString: values.join("^")
		}
	}

	
});

/** 
 * 为Ext XTemplate的添加几项额外的辅助函数
 */ 
Ext.apply(Ext.XTemplate.prototype , {
	chinese: function( v ){
		v = Ext.util.Format.convertToYuan(v);
		return Ext.util.Format.convertToChinese(v);
	},
	toYuan: function(v){
		return Ext.util.Format.convertToYuan(v);
	}
});