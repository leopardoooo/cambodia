Ext.onReady(function(){
	
	var north=new Ext.Panel({
		title:'北方',
		collapsible: true,
		region:'north',
		collapseMode:'mini',
		height:280
	});
	
	var center=new Ext.Panel({
		title:'下',
		region:'center'
	})
	
	new Ext.Viewport({
		layout:'border',
		
		items:[north,center]
	})
});