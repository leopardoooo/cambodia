/*******************************************************************************
* KindEditor - WYSIWYG HTML Editor for Internet
* Copyright (C) 2006-2011 kindsoft.net
*
* @author Roddy <luolonghao@gmail.com>
* @site http://www.kindsoft.net/
* @licence http://www.kindsoft.net/license.php
*******************************************************************************/
function  changeFile(idx){
  		var files = document.getElementsByName('imgFile');
  		var file = files[idx];
  		file.click();
  	}
  	function fileChanged(obj,idx){
  		var texts = document.getElementsByName('sudoFileId');
  		var text = texts[idx];
  		text.value = obj.value;
  	}
KindEditor.plugin('docpastecheck', function(K) {
	var self = this, name = 'docpastecheck',
		allowImageUpload = K.undef(self.allowImageUpload, true),
		imageTabIndex = K.undef(self.imageTabIndex, 0),
		imgPath = self.pluginsPath + 'image/images/',
		extraParams = K.undef(self.extraFileUploadParams, {}),
		filePostName = K.undef(self.filePostName, 'imgFile'),
		fillDescAfterUploadImage = K.undef(self.fillDescAfterUploadImage, false),
		lang = self.lang(name + '.');
	
	self.plugin.docpastecheck = {
		check:function(){
			var html = self.html();
                //取出所有的 IMG 标签,并按照 src为key,相同src的img元素数组为value存入map.
                var imgs = KindEditor.queryAll('img',self.edit.doc.body);
                if(imgs.length < 0){
                	return false;
                }
                var map = {},counts = 0;
                for(var index = 0;index<imgs.length;index++){
                	var img = imgs[index];
                	var src = img.src;
                	if(src.indexOf('file:') !=0 ){//必须是以  "file://" 开头的才是本地文件
                		continue;
                	}
                	var arr = map[src] ||[];
                	arr.push(img);
                	map[img.src] = arr;
                	counts++;
                }
                //以此处理所有的img元素的src.是本地的就上传,然后获取本地地址替换本地URL.
                return counts>0?map:false;
		},
		edit : function() {
			//前期工作,如果没有图片或者图片都不是本地文件,则不继续工作.
			var map = self.plugin.docpastecheck.check();
			if(!map){alert('没有需要处理的本地未上传图片!');return;}
			
			var params = [];
			for (var key in map) {
				params.push(key.substr(8));
			}
			// String host,String context,String [] imgs
			var app;
			if (document.applets.length > 0) {
				app = document.applets[0];
			} else if (document.embeds.length > 0) {
				app = document.embeds[0];
			} else {
				alert("没有找到applet应用!");
				return "";
			}
			var location = window.location;
			var hostName = location.hostname;
			var context = window.location.pathname.split('/')[1];
			var msg = app.processFiles(hostName,context,params);
			var obj = Ext.decode(msg);
			if(obj.eror){//有错误报错
				alert(obj.eror);
				return;
			}
			var html = self.html();
			var newHtml = html;
			
			for(var key in obj){
				var remote = obj[key]; 
				var local = 'file://' + key; // var imgArray = imgMap[local];
				while(newHtml.indexOf(local) >=0){
					newHtml = newHtml.replace(local,remote); 
				}
				
			}
			
			self.html(newHtml);
			
		},
		'delete' : function() {
			var target = self.plugin.getSelectedImage();
			if (target.parent().name == 'a') {
				target = target.parent();
			}
			target.remove();
		}
	};
	self.clickToolbar(name, self.plugin.docpastecheck.edit);
});
