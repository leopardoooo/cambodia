Ext.ns('Ext.ux');
Ext.ux.ColorField = Ext.extend(Ext.form.TriggerField,{
      editable:false,
      triggerClass : 'x-form-arrow-trigger',
      initComponent : function(){
            Ext.ux.ColorField.superclass.initComponent.call(this);
            this.addEvents(
                'select'
            );
      },
      initEvents: function() {
            Ext.ux.ColorField.superclass.initEvents.call(this);
            this.keyNav = new Ext.KeyNav(this.el, {
                "down": function(e) {
                    this.onTriggerClick();
                },
                scope: this,
                forceKeyDown: true
            });
      },
        onDestroy : function(){
             Ext.destroy(this.menu, this.keyNav);
             Ext.ux.ColorField.superclass.onDestroy.call(this);
        },
        validateBlur : function(){
            return !this.menu || !this.menu.isVisible();
        },
      onTriggerClick : function(){
        if(this.disabled){
            return;
        }
        if(this.menu == null){
            this.menu = new Ext.menu.ColorMenu();
            if(this.getValue()){
                this.menu.palette.value=this.getValue();
            }
        }else{
            if(this.getValue()){
                this.menu.palette.select(this.getValue());
            }else if(this.menu.palette.value){
                 this.menu.palette.el.child('a.color-'+this.menu.palette.value).removeClass('x-color-palette-sel');
            }
        }
        this.onFocus();
        this.menu.show(this.el, "tl-bl?");//tl-bl?
        this.menuEvents('on');
    },
    //private
    menuEvents: function(method){
        this.menu[method]('select', this.onSelect, this);
        this.menu[method]('hide', this.onMenuHide, this);
        this.menu[method]('show', this.onFocus, this);
    },
    onSelect: function(m, color){
        this.setValue(color);
        this.fireEvent('select', this, color);
        this.menu.hide();
    },
    onMenuHide: function(){
        this.focus(false, 60);
        this.menuEvents('un');
    }
});
Ext.reg('colorfield', Ext.ux.ColorField);