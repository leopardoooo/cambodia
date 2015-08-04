var _ajax_request_fn = Ext.lib.Ajax.request;
Ext.apply(Ext.lib.Ajax, {
	// 重写Ext Ajax方法
	request : function(method, uri, cb, data, options) {
		// 如果是同步请求，采用Jquery异步加载
		if(options.async === false){
			console.warn("检测到一个同步Ajax["+ uri +"], \n这是不建" +
					"议的行为，未来有可能浏览器会被取消这种做法，请尽早修改成异步回调模式! " +
					"http://xhr.spec.whatwg.org/");
			$.ajax({
        		type: method, 
        		url: uri, 
        		async: options.async, 
        		data: data,
                dataType: "text", 
                timeout: options.timeout,
                success: function(dataText, textStatus){
                	if(options.success){
                		if(!options["scope"]){
                			options["scope"] = this;
                		}
                		// 伪造Ext Ajax Success回调参数
                		options.success.call(options["scope"], {
                			responseText: dataText,
                			statusText: textStatus
                		},options);
                	}
                },
                error: function(){
                	console.log("请求出错了，错误参数：");
                	console.log(arguments);
                }
        	});
			
		// 使用EXT AJAX进行加载
		}else{
			_ajax_request_fn(method, uri, cb, data, options);
		}
	}
});