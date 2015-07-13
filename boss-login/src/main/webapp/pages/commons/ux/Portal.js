/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ns("Ext.ux");
Ext.ux.Portal = Ext.extend(Ext.Panel, {
    layout : 'column',
    autoScroll : true,
    cls : 'x-portal',
    defaultType : 'portalcolumn'
});

Ext.reg('portal', Ext.ux.Portal);

Ext.ux.PortalColumn = Ext.extend(Ext.Container, {
    layout : 'anchor',
    defaultType : 'portlet',
    cls : 'x-portal-column'
});

Ext.reg('portalcolumn', Ext.ux.PortalColumn);

Ext.ux.Portlet = Ext.extend(Ext.Panel, {
    anchor : '100%',
    frame : true,
    collapsible : true,
    cls : 'x-portlet'
});

Ext.reg('portlet', Ext.ux.Portlet);

