Ext.namespace("Ext.ux");
Ext.ux.RemoteCheckboxGroup = Ext.extend(Ext.form.CheckboxGroup, {
    items: [{xtype: 'checkbox',boxLabel: ' ',disabled: true }],
    reload: function (records){
            this.removeAll(); 
            var item;
            var record;
            for (var i = 0; i < records.length; i++){
                record = records[i];
                item = new Ext.form.Checkbox({ xtype: 'checkbox',boxLabel: record.name,inputValue: record.id});
                var chk = this.panel.getComponent(i%this.columns.length).add(item);
                this.items[i] = chk;    
            }
            this.doLayout();
    },
    removeAll: function (){
        for (var j = 0; j < this.columns.length; j++)
        	this.panel.getComponent(j).removeAll();
    },
    getValue: function (){
        var boxvalues='';
        for (var j = 0; j < this.columns.length; j++){
            if (this.panel.getComponent(j).items.length > 0) {
                this.panel.getComponent(j).items.each( function (i){
                	if (i.checked) 
                		if(boxvalues==='') boxvalues=i.inputValue; 
                		else boxvalues=boxvalues+"','"+i.inputValue;
                		}
                );
            }
        }
        return boxvalues;
    }
});
Ext.reg("remotecheckboxgroup", Ext.ux.RemoteCheckboxGroup);