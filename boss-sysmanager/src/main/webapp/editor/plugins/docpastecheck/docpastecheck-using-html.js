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
		formatUploadUrl = K.undef(self.formatUploadUrl, true),
		uploadJson = K.undef('upload/normal', self.basePath + 'php/upload_json.php'),
		imageTabIndex = K.undef(self.imageTabIndex, 0),
		imgPath = self.pluginsPath + 'image/images/',
		extraParams = K.undef(self.extraFileUploadParams, {}),
		filePostName = K.undef(self.filePostName, 'imgFile'),
		fillDescAfterUploadImage = K.undef(self.fillDescAfterUploadImage, false),
		lang = self.lang(name + '.');

	self.plugin.docpastecheckDialog = function(options) {
		var imageUrl = options.imageUrl,
			imageTitle = K.undef(options.imageTitle, ''),
			imgDomInfo = options.imgDomInfo,
			clickFn = options.clickFn;
			this.inputs = options.inputs;
			
		var target = 'kindeditor_upload_iframe_' + new Date().getTime();
		var hiddenElements = [];
		for(var k in extraParams){
			hiddenElements.push('<input type="hidden" name="' + k + '" value="' + extraParams[k] + '" />');
		}
		var html = [
			'<div style="padding:20px;">',
			'<iframe name="' + target + '" style="display:none;"></iframe>',
			'<form id="uploadFileFormId" class="ke-upload-area ke-form" method="post" enctype="multipart/form-data" target="' + target + '" action="' + K.addParam(uploadJson, 'dir=image') + '">',
			'<div class="ke-dialog-row">',//file
			hiddenElements.join(''),
			this.inputs,
			'</div>',
			'</form>',
			'</div>'
		].join('');
		
		var dialog = self.createDialog({
			name : name,
			width : 600,
			height : 400,
			title : self.lang(name),
			body : html,
			yesBtn : {
				name : self.lang('yes'),
				click : function(e) {
					// Bugfix: http://code.google.com/p/kindeditor/issues/detail?id=319
					if (dialog.isLoading) {
						return;
					}
					// insert local image
					var files = document.getElementsByName('imgFile');
					var flag = false;
					for(var index =0;index<files.length;index++){
						var file = files[index];
						if(file.value !=''){
							flag = true;break;
						}
					}
					if (!flag) {
						alert(self.lang('pleaseSelectFile'));
						return;
					}
					dialog.showLoading(self.lang('uploadLoading'));
//					uploadbutton.submit();
					Ext.Ajax.request({
						url:uploadJson,
						method:'post',
						params:{muti:'y'},
						isUpload : true,
						fileUpload:true,
						form:document.getElementById('uploadFileFormId'),
						success:function(req){
							dialog.hideLoading();//关掉loading图片.
							var text = req.responseText;
							var text2 = text.split('[')[1].split(']')[0];//返回的responseText 有 <pre>标签需要去掉
							text2 = '['+text2+']';
							var info = Ext.decode(text2);
							if(info.detailMessage && info.stackTrace){//有错误信息
								Alert(info.detailMessage);
								return ;
							}
							var newMap = {};
							var imgDomInfo = options.imgDomInfo;
							for(var key in imgDomInfo){
								var arr = key.split('/');
								var src = arr[arr.length -1];
								newMap[src] = imgDomInfo[key];
							}
							//置空form
							var html = self.html();
							var newHtml = html;
							for(var index =0;index<info.length;index++){
								var item = info[index];
								var localShort = item.local;
								var remote = item.remote;
								var arr = newMap[localShort];
								for(var idx=0;idx<arr.length;idx++){
									var dom = arr[idx];
									var oldSrcFolders = dom.src.substr(8).split('/');
									var replace = 'file://' + oldSrcFolders[0] + '\\';
									for(var i=1;i<oldSrcFolders.length-1;i++){
										replace += oldSrcFolders[i] + '\\';
									}
									replace += oldSrcFolders[oldSrcFolders.length -1];
									while(newHtml.indexOf(replace) >=0){
										newHtml = newHtml.replace(replace,remote); 
									}
								}
							}
							self.html(newHtml);
							self.hideDialog();
							document.getElementById('tipImgInfoTextArea').value ='';
							//隐藏Ext自动生成的iframe
							var iframes = document.getElementsByTagName('iframe');
							for(var index =0;index<iframes.length;index++){
								var iframe = iframes[index];
								if(iframe.name.indexOf('ext-gen') >=0){
									iframe.style.display = 'none';
								}
							}
						}
					});
					return;
					// insert remote image
					var title = titleBox.val();
					
					if (!/^\d*$/.test(width)) {
						alert(self.lang('invalidWidth'));
						return;
					}
					if (!/^\d*$/.test(height)) {
						alert(self.lang('invalidHeight'));
						return;
					}
					clickFn.call(self, url, title, width, height, 0, null);
				}
			},
			beforeRemove : function() {
			}
		}) ;
		return dialog;
	};
	
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
			var flag = true;
			
			var htmlInputs = '';
			var title = '<div style="color:blue;">下面给出需要上传的本地图片路径,请点击浏览后将每个文件路径复制到弹出的选择框里,不区分顺序</div>';
			var index = 1;
			var msg = '' ;
			for(var key in map){
            	if(key.indexOf('file:') !=0 ){//必须是以  "file://" 开头的才是本地文件
            		continue;
            	}
            	var idx = index -1;
            	htmlInputs += '<label style="width:60px;">文件' + index + ' :</label>';
            	htmlInputs += '<input type="text" style="width:400px;height:20px;" name="sudoFileId" readonly="readonly" ondblclick="changeFile(' + idx+ ')">';
            	htmlInputs += '<button onclick="changeFile(' + idx + ')" style="width: 80px;height:26px;">浏览</button><br>';
		  		htmlInputs += '<div style="display: none;">';
		  		htmlInputs += '<input type="file" name="imgFile" onchange="fileChanged(this,' + idx + ')" style="width: 120">';
		  		htmlInputs += '</div>';
            	msg += '文件' + index +'：'+key +'\n';
            	index ++;
			}
			htmlInputs = title +'<textarea id="tipImgInfoTextArea" style="color:red;width:550px;" rows="4" value="' + msg + 
					'" readonly="readonly">' + msg + '</textarea>' + htmlInputs; 
			/*
            if(!window.confirm('是否要处理html里的本地文件？')){
            	return ;
            }
            */
			var img = self.plugin.getSelectedImage();
			self.plugin.docpastecheckDialog({
				inputs:htmlInputs,
				imgDomInfo:map,
				clickFn : function(url, title, width, height, border, align) {
					self.exec('insertimage', url, title, width, height, border, align);
					// Bugfix: [Firefox] 上传图片后，总是出现正在加载的样式，需要延迟执行hideDialog
					setTimeout(function() {
						self.hideDialog().focus();
					}, 0);
				}
			});
			
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
