langUtils = function(DOC){
	function isTargetType(o, type){
		return typeof(o) === type; 
	}
	function isObject(o){
		return isTargetType(o, "object");
	}
	function isString(o){
		return isTargetType(o, "string");
	}
	
	// find a lang string from current scope start.
	function findLangString(langKeys){
		var current = this;
		if(!isObject(current)){
			return "";
		}
		
		// split langKeys
		var levels = langKey.split(".");
		var lastKeyIndex = levels.length - 1;
		for(var i = 0; i < levels.length; i++){
			var nextObj = current[levels[i]];
			if (!nextObj || (i !== lastKeyIndex && !isObject(nextObj))) {
				return nextObj;
			}
			current = nextObj;
		}
		return current;
	}
	var FORM = "INPUT,TEXTAREA,SELECT";
	function set(text, dom){
		if(!dom || !text){
			return text;
		}
		if(isString(dom)){
			dom = DOC.getElementById(domId);
		}
		if(FORM.indexOf(dom.tagName.toUpperCase())){
			dom.value = text;
		}else{
			dom.innerText = text;
		}
		return text;
	}
	
	return {
		/**
		 * 获取boss-core-lang.js的国际化属性值，如果传入的dom元素是一个表单元素，
		 * 则设置value属性值，如果是其它元素则设置innerText属性值
		 * 
		 * @param langKeys 如传入:"home.topWelcome"
		 * @param dom 一个可选参数，可以是ID或Dom对象
		 */
		bc: function(langKeys, dom){
			return set(findLangString.apply(BCLang, [langKeys]), dom);
		},
		/**
		 * 获取resource-lang.js资源菜单的属性值
		 * 方法实现同fn: bc是一致的
		 */
		res: function(langKeys, dom){
			return set(findLangString.apply(ResLang, [langKeys]), dom);
		}
	}
}(document);
