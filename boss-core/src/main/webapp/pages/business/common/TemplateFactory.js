
/**
  * 模板工厂类 gTemplate 用于产生系统对应的模板容器，
  * @class TemplateFactory
  */
 TemplateFactory = {};
 
 Ext.apply( TemplateFactory, {
 	/**
 	 * 生成一个Viewport
 	 */
 	gViewport: function( p ){
 		p.region = "center";
 		new Ext.Viewport({
			layout: 'border',
			items: p
		});
 	},
 	/**
 	 * 对外提供生成模板的静态函数，
 	 * 根据当前的业务编号，获取当前业务配置的杂费、施工单、业务单据、业务信息数据，
 	 * 根据这部分数据，如果判断没有存在配置的数据，则动态调整模板的布局方式。
 	 */
 	gTemplate: function( ownForm ){
 		var data = App.getApp().findBusiCfgData(App.getData().currentResource.busicode);
 		this.instanceForm( ownForm , data || {} );
 	/*	//获取数据包括 
		Ext.Ajax.request({
			scope: this, 
			params: { busiCode: },
			url: Constant.ROOT_PATH + "/commons/x/BusiCommon!initServices.action",
			success: function(res , ops){
				var o = Ext.decode(res.responseText);
				this.instanceForm( ownForm , o );
			}
		});*/
 	},
 	//private 实例化表单
 	instanceForm: function( ownForm , o){
 		var feeForm,extForm ,docForm,forms = {};
 		if(ownForm) forms[CoreConstant.BOX_FORMS_OWN] = ownForm;
 		if(o["busifee"] && o["busifee"].length > 0){
			feeForm = new ServicePanel(o["busifee"]);
			forms[CoreConstant.BOX_FORMS_FEE]= feeForm;
		}
		if((o["tasktype"] && o["tasktype"].length > 0) || (o["busidoc"] && o["busidoc"].length > 0 )){
			docForm = new WorkForm(o["tasktype"],o["busidoc"]);
			forms[CoreConstant.BOX_FORMS_DOC] = docForm;
		} 
		//如果存在扩展信息表单
		if(o["busiextform"] && o["busiextform"].length > 0){
			extForm = new BusiExtForm(o["busiextform"]);
			forms[CoreConstant.BOX_FORMS_BUSIEXT] = extForm;
		}
		var panels= this.dynamicLayout(ownForm,feeForm, extForm, docForm);
		App.box = new BusiPanel( panels ,forms );
		this.gViewport(App.box);
 	},
 	//private 根据表单的取值，动态布局
 	dynamicLayout: function( ownForm ,feeForm , extForm,docForm){
 		var lItems = [] ,rItems = [], bigItems = [],winW = 0,winH = 0;
	 	//左边的items
	 	if(ownForm){
	 		ownForm["region"] = "center";
	 		lItems.push( ownForm );
	 	}
	 	
	 	if(docForm){
	 		docForm["region"] = 'center';
 			rItems.push(docForm);
	 		if(feeForm){
	 			Ext.apply( feeForm, { region: 'north', height: 230, split: true});
	 			rItems.push(feeForm);
	 		}
	 		if(extForm){
		 		winH = 60;
		 		Ext.apply( extForm, {region: 'south', height: 120, split: true});
		 		lItems.push(extForm );
		 	}
	 		
	 	}else{
	 		if(extForm){
	 			extForm["region"] = 'center';
 				rItems.push(extForm);
	 		}
	 		if(feeForm){
	 			Ext.apply( feeForm, { region: 'north', height: 150, split: true});
	 			rItems.push(feeForm);
	 		}
	 		if(!extForm && feeForm){
	 			feeForm["region"] = 'center';
	 		}
	 	}
 		//bigItems
 		if(lItems.length > 0){
 			bigItems.push({region: 'center',layout: 'border',border: false,items: lItems});
 		}
 		if(rItems.length > 0){
 			winW = 250;
 			bigItems.push({region: 'east',width: winW,split: true,layout: 'border',border: false,items: rItems});
 		}
 		if(lItems.length == 0 && rItems.length > 0){
 			bigItems[0].region = "center";
 		}
 		//重新设置窗口的大小
 		if(winH > 0 || winW > 0){
 			var size = App.getData().ownFormSize;
 			var w = App.getApp().menu.bigWindow;
 			//var w = App.getApp().BusiWindow.win;
 			var _h = size.height;
 			var _w = size.width;
 			if(winH > 0){
 				_h = _h + winH;
 			}
 			if(winW > 0){
 				_w = _w + winW;
 			}
 			w.setSize(_w , _h);
 			w.center();
 		}
	 	return bigItems;
 	}
 });