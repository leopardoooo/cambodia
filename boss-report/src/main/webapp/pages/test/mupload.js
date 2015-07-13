var app={};
var swfu;
Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'under';
	swfu=new SWFUpload({
		button_placeholder_id:"form1",
		upload_url : "upload.ashx", 
		flash_url : root+"/pages/test/swfupload_f9.swf" ,
		file_size_limit : "10240",
		file_types : "*.jpg;*.gif",
		file_post_name : "Filedata",
		requeue_on_error : false,
		post_params : {},
		file_types_description:'图片',
		flash_color : "#FFFFFF", 
		file_queued_handler : function(file){
			var filetype=(file.type.substr(1)).toUpperCase();
			if(filetype=='JPG' | filetype=='GIF'){swfu.startUpload();
			}else{Ext.Msg.alert('错误','只能上传JPG或GIF格式文件');}
		}, 
		upload_start_handler:function(file){Ext.Msg.progress('上传文件','正在上传文件：'+file.name,'0%');return true;}, 
		upload_progress_handler:function(file,bytesloaded){
			var percent = Math.ceil((bytesloaded / file.size) * 100);
				Ext.Msg.updateProgress(percent/100,percent+'%');
			}, 
			upload_success_handler:function(file, server_data){
			var msg=Ext.decode(server_data);
			if(msg){
				if(msg.success){
					Ext.getCmp('imagePane').body.dom.innerHTML="<img width='100' src='upload/"+msg.file+"'>"
					Ext.get('op').dom.value+="----------------------------\n"
							+"执行回调函数：success\n" +"返回值："+server_data+'\n';
					var stats=swfu.getStats();
					if(stats.files_queued>0)
						swfu.startUpload();
					Ext.Msg.hide();
				}else{Ext.Msg.alert('错误',msg.msg);	}
			}else{Ext.Msg.alert('错误','上传错误！');}
		},
		upload_error_handler:function(file,error_code,message){
			Ext.Msg.alert('错误','上传文件“'+file.name+'”发生错误。<br>错误代码：'+error_code+'<br>'+'错误信息：'+message);
		},
		file_queue_error_handler:function(file,error_code,message){
			switch (error_code) {
				case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
					Ext.Msg.alert('错误',"文件不允许超过300k！<br> 文件名: " + file.name + "<br> 大小: " + file.size );
					break;
				case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
					Ext.Msg.alert('错误',"不允许上传0字节文件！<br> 文件名: " + file.name + "<br> 大小: " + file.size );
					break;
				case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
					Ext.Msg.alert('错误',"已超出上传文件数量！<br> 文件名: " + file.name + "<br> 大小: " + file.size );
					break;
				case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
					Ext.Msg.alert('错误',"不允许上传该类文件！<br> 文件名: " + file.name + "<br> 大小: " + file.size );
					break;
				default:
					Ext.Msg.alert('错误',"发生未知错误，错误代码:"+error_code+"！<br> 文件名: " + file.name + "<br> 大小: " + file.size );
					break;
			}
		}});
		var frm = new Ext.form.FormPanel({   
			renderTo: "form2",
			width: 400,
			height:300,
			frame: true,
			labelWidth:80,
			labelSeparator:"：",
			title:'上传文件',
			fileUpload:true,
			items:[ 
				{xtype:'textfield',name:'title',fieldLabel:'标题',anchor:'-30',allowBlank:false},
					{xtype:'textfield',name:'Filedata',fieldLabel:'文件',anchor:'-30',allowBlank:false,inputType:'file'},
					{layout:'column',border:false,items:[
						{columnWidth:.4,border:false,
							items: [{xtype:'button',text:'上传文件',handler:function(){
								swfu.setPostParams({title:frm.form.findField("title").getValue()});
								swfu.selectFile();
							}}]},
						{columnWidth:.1,border:false,
							items: [{xtype:'panel',html:'&nbsp;',height:26,border:false}]},
						{columnWidth:.4,layout: 'form',border:false,
							items: [{xtype:'button',text:'上传多个文件',handler:function(){
								swfu.setPostParams({title:frm.form.findField("title").getValue()});
								swfu.selectFiles();
							}}]}
					]},
					{xtype:'panel',id:'imagePane',bodyStyle:'padding:5px',html:'&nbsp;',height:120,width:120}
			]
			,
			buttons: [
				{text: '保存',scope:this,handler:function(){
					 if(frm.form.isValid()){
						frm.form.doAction('submit',
							{waitTitle:'上传文件',waitMsg:'正在上传文件……',url:'upload.ashx',method:'post',params:'',
								success:function(form,action){
									Ext.getCmp('imagePane').body.dom.innerHTML="<img width='100' src='upload/"+action.result.file+"'>"
									Ext.get('op').dom.value+="----------------------------\n"+"执行回调函数：success\n"
										+"返回值："+Ext.encode(action.result)+'\n';
								},
								failure:function(form,action){
									Ext.get('op').dom.value+="----------------------------\n"
										 +"执行回调函数：failure\n" +"返回值："+Ext.encode(action.result)+'\n';
								}                                                                         
							});
                    }}},
					{text: '取消',scope:this,handler:function(){frm.form.reset();}
			}
			]/*,
			listeners:{
				render:function(fp){
					
					fp.form.waitMsgTarget = fp.getEl();
			}}*/
		});
});