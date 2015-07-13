Ext.namespace("Ext.ux");
Ext.ux.RemoteCheckboxGroup = Ext.extend(Ext.form.CheckboxGroup, {
    baseParams: null,
    url: '',
    defaultItems: [
    new Ext.form.Checkbox(
    {
        xtype: 'checkbox',
        boxLabel: 'No Items',
        disabled: true
    })],
    fieldId: 'id',
    fieldName: 'name',
    fieldLabel: 'boxLabel',
    fieldValue: 'inputValue',
    fieldChecked: 'checked',
    reader: null,

    //private
    initComponent: function ()
    {

        this.addEvents(
        /**
         * @event add
         * Fires when a checkbox is added to the group
         * @param {Ext.form.CheckboxGroup} this
         * @param {object} chk The checkbox that was added.
         */
        'add',
        /**
         * @event beforeadd
         * Fires before a checkbox is added to the group
         * @param {Ext.form.CheckboxGroup} this
         * @param {object} chk The checkbox to be added.
         */
        'beforeadd',
        /**
         * @event load
         * Fires when a the group has finished loading (adding) new records
         * @param {Ext.form.CheckboxGroup} this
         */
        'load',
        /**
         * @event beforeremove
         * Fires before a checkbox is removed from the group
         * @param {Ext.form.CheckboxGroup} this
         * @param {object} chk The checkbox to be removed.
         */
        'beforeremove');

        Ext.ux.RemoteCheckboxGroup.superclass.initComponent.apply(this, arguments);
    },

    onRender: function ()
    {
        Ext.ux.RemoteCheckboxGroup.superclass.onRender.apply(this, arguments);
        if (this.showMask)
        {
            this.loadmask = new Ext.LoadMask(this.ownerCt.getEl(), {
                msg: "Loading..."
            });
        }
        this.reload();
    },

    reload: function ()
    {
        this.items = this.defaultItems;

        if ((this.url != '') && (this.reader != null))
        {


            this.removeAll(); //ensure we clear existing checkboxes
            if (this.showMask)
            {
                this.loadmask.show();
            }
            cbObj = this; //save a reference to the checkboxgroup
            handleCB = function (responseObj)
            {

                var response = Ext.decode(responseObj.responseText);

                if (response.success)
                {
                    var data = cbObj.reader.readRecords(Ext.decode(responseObj.responseText));

                    var item;
                    var record;
                    var id, name, checked;

                    for (var i = 0; i < data.records.length; i++)
                    {
                        record = data.records[i];
                        item = new Ext.form.Checkbox(
                        {
                            xtype: 'checkbox',
                            listeners: {
                                'render': cbObj.cbRenderer
                            },
                            handler: cbObj.cbHandler,
                            boxLabel: record.get(cbObj.fieldLabel),
                            inputValue: record.get(cbObj.fieldValue)
                        });

                        if (cbObj.fieldId != '')
                        {
                            item.id = record.get(cbObj.fieldId);
                        }

                        if (cbObj.fieldName != '')
                        {
                            item.name = record.get(cbObj.fieldName);
                        }

                        if (cbObj.fieldChecked != '')
                        {
                            item.checked = record.get(cbObj.fieldChecked);
                        }

                        if (record.get('disabled'))
                        {
                            item.disabled = true;
                        }

                        if (cbObj.fireEvent('beforeadd', cbObj, item) !== false)
                        {
                            var chk = cbObj.panel.getComponent(0).add(item);
                            cbObj.items[i] = chk;
                            cbObj.panel.getComponent(0).doLayout();
                            cbObj.fireEvent('add', cbObj, item);
                        }
                    }

                    cbObj.fireEvent('load', cbObj);
                }
                if (cbObj.showMask)
                {
                    cbObj.loadmask.hide();
                }
            }

        }
        var fail = function ()
        {
            console.log("fail")
        };

        Ext.Ajax.request(
        {
            headers: ['Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8'],
            method: 'POST',
            url: this.url,
            params: this.baseParams,
            success: handleCB,
            failure: fail
        });
    },
    removeAll: function ()
    {
        cbObj = this;
        for (var j = 0; j < this.columns.length; j++)
        {
            if (cbObj.panel.getComponent(j).items.length > 0)
            {
                cbObj.panel.getComponent(j).items.each(

                function (i)
                {
                    if (cbObj.fireEvent('beforeremove', cbObj, i) !== false)
                    {
                        i.destroy();
                    }
                });
            }
        }
    },
    getGroupValue: function ()
    {
        var valuesArray = [];
        for (var j = 0; j < this.columns; j++)
        {
            if (this.panel.getComponent(j).items.length > 0)
            {
                this.panel.getComponent(j).items.each(

                function (i)
                {
                    if (i.checked)
                    {
                        valuesArray.push(i.inputValue);
                    }
                });
            }
        }
        return valuesArray;
    }

});
Ext.reg("remotecheckboxgroup", Ext.ux.RemoteCheckboxGroup);