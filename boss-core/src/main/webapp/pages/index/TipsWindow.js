TipsWindow = Ext.extend(Ext.Window,{

	width:200,
	height:150,
	layout:'fit',
	modal:false,
	maximizable:false,
	plain:true,
	shadow: false,//去除阴影
    draggable: false,//默认不可拖拽
    resizable: false,
    closable: true,
    closeAction: 'close',//默认关闭为隐藏
    autoHide: 3000,
    
    constructor:function(config){
    	Ext.applyIf(this,config);
    	TipsWindow.superclass.constructor.call(this,config);
    	this.initPosition(true);
    },
    initEvents:function(){
    	TipsWindow.superclass.initEvents.call(this);
    	var task ;
    	//自动隐藏
        if (false !== this.autoHide)
        {
            task = new Ext.util.DelayedTask(this.hide, this),
            second = parseInt(this.autoHide) || 3000;
            this.on('beforeshow',function(self){task.delay(second);});
        }
        this.on('beforeshow', this.showTips);
        this.on('beforehide', this.hideTips);

        Ext.EventManager.onWindowResize(this.initPosition, this); //window大小改变时，重新设置坐标
        Ext.EventManager.on(window, 'scroll', this.initPosition, this); //window移动滚动条时，重新设置坐标
    },
    //参数: flag - true时强制更新位置
    initPosition: function(flag)
    {
        if (true !== flag && this.hidden)
        { //不可见时，不调整坐标
            return false;
        }
        var doc = document,
        bd = (doc.body || doc.documentElement);
        //ext取可视范围宽高(与上面方法取的值相同), 加上滚动坐标
        var left = bd.scrollLeft + Ext.lib.Dom.getViewWidth() - 4 - this.width;
        var top = bd.scrollTop + Ext.lib.Dom.getViewHeight() - 4 - this.height;
        this.setPosition(left, top);
    },
    showTips: function()
    {
        var self = this;
        if (!self.hidden)
        {
            return false;
        }

        self.initPosition(true); //初始化坐标
        self.el.slideIn('b',
        {
            callback: function()
            {
                //显示完成后,手动触发show事件,并将hidden属性设置false,否则将不能触发hide事件
                self.fireEvent('show', self);
                self.hidden = false;
            }
        });
        return false; //不执行默认的show
    },
    hideTips: function()
    {
        var self = this;
        if (self.hidden)
        {
            return false;
        }

        self.el.slideOut('b',
        {
            callback: function()
            {
                //渐隐动作执行完成时,手动触发hide事件,并将hidden属性设置true
                self.fireEvent('hide', self);
                self.hidden = true;
            }
        });
        return false; //不执行默认的hide
    }
});