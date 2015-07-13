/**
 * Debug window ,显示错误信息
 */
DebugWindow  = Ext.extend( Ext.Window, {

	error:{},
	
	constructor: function( error , anmi ){
		this.error = error;
		
		DebugWindow.superclass.constructor.call(this, {
			title:this.error["title"],
			width:600,
			height:400,
			layout:'fit',
//			modal:true,
			bodyStyle: 'background-color: white',
			maximizable:true,
	        closeAction:'close',
			html: (!Ext.isEmpty(this.error)&&!Ext.isEmpty(this.error["type"])&&this.error["type"]==7)
			       ?("<div class='x-div-screen-center'>"
					+"<div class='left-div-img-icon x-img-failure'></div>"
					+"<div class='msg-div'><br />"
					+"	<label>友情提示：</label><br />"
					+"	<div>"
					+"		<ul>"
					+"			<li>错误信息：<span>"+ this.error["content"] +"</span>"
					+"			<li>详细内容：<span>"+ this.error["detail"] +"</span>"
					+"			</li>"
					+"		</ul>"
					+"	</div>"
					+"</div>"
					+"</div>"):
					("<div class='x-div-screen-center'>"
					+"<div class='left-div-img-icon x-img-failure'></div>"
					+"<div class='msg-div'><br />"
					+"	<label>提示：操作异常</label><br />"
					+"</div>"
					+"</div>")
		});
	}
});