/**
 * UserAcctBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class UserAcctBindingStub extends org.apache.axis.client.Stub implements service.ws.north.isam.zte.com.UserAcct {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("changePassword");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "newPassword"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "oldPassword"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("changeService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://jaxb.dev.java.net/array", "stringArray"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("changeStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("close");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("offline");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userMac"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("open");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAcctInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userAcctInfo"), service.ws.north.isam.zte.com.UserAcctInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://jaxb.dev.java.net/array", "stringArray"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryOnlineUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "param"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryOnlineParam"), service.ws.north.isam.zte.com.QueryOnlineParam.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserOnlineInfo"));
        oper.setReturnClass(service.ws.north.isam.zte.com.RetUserOnlineInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryUserFailedLog");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "param"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryFailedParam"), service.ws.north.isam.zte.com.QueryFailedParam.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserFailedInfo"));
        oper.setReturnClass(service.ws.north.isam.zte.com.RetUserFailedInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryUserInfo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserAcctInfo"));
        oper.setReturnClass(service.ws.north.isam.zte.com.RetUserAcctInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("removeBind");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "userAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://jaxb.dev.java.net/array", "stringArray"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("", "item"));
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operatorInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo"), service.ws.north.isam.zte.com.OperatorInfo.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resInfoHolder"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"), service.ws.north.isam.zte.com.ResultInfo.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    public UserAcctBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public UserAcctBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public UserAcctBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "operatorInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.OperatorInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryFailedParam");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.QueryFailedParam.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryOnlineParam");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.QueryOnlineParam.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.ResultInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserAcctInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.RetUserAcctInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserFailedInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.RetUserFailedInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserOnlineInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.RetUserOnlineInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userAcctInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.UserAcctInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userFailedInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.UserFailedInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userOnlineInfo");
            cachedSerQNames.add(qName);
            cls = service.ws.north.isam.zte.com.UserOnlineInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://jaxb.dev.java.net/array", "stringArray");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public boolean changePassword(java.lang.String userAccount, java.lang.String newPassword, java.lang.String oldPassword, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "changePassword"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, newPassword, oldPassword, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean changeService(java.lang.String userAccount, java.lang.String[] serviceName, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "changeService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, serviceName, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean changeStatus(java.lang.String userAccount, int status, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "changeStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, new java.lang.Integer(status), operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean close(java.lang.String userAccount, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "close"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean offline(java.lang.String userAccount, int offType, java.lang.String userMac, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "offline"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, new java.lang.Integer(offType), userMac, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean open(service.ws.north.isam.zte.com.UserAcctInfo userAcctInfo, java.lang.String[] serviceName, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "open"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAcctInfo, serviceName, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public service.ws.north.isam.zte.com.RetUserOnlineInfo queryOnlineUser(service.ws.north.isam.zte.com.QueryOnlineParam param, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryOnlineUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {param, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return (service.ws.north.isam.zte.com.RetUserOnlineInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (service.ws.north.isam.zte.com.RetUserOnlineInfo) org.apache.axis.utils.JavaUtils.convert(_resp, service.ws.north.isam.zte.com.RetUserOnlineInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public service.ws.north.isam.zte.com.RetUserFailedInfo queryUserFailedLog(service.ws.north.isam.zte.com.QueryFailedParam param, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryUserFailedLog"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {param, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return (service.ws.north.isam.zte.com.RetUserFailedInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (service.ws.north.isam.zte.com.RetUserFailedInfo) org.apache.axis.utils.JavaUtils.convert(_resp, service.ws.north.isam.zte.com.RetUserFailedInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public service.ws.north.isam.zte.com.RetUserAcctInfo queryUserInfo(java.lang.String userAccount, java.lang.String password, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryUserInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, password, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return (service.ws.north.isam.zte.com.RetUserAcctInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (service.ws.north.isam.zte.com.RetUserAcctInfo) org.apache.axis.utils.JavaUtils.convert(_resp, service.ws.north.isam.zte.com.RetUserAcctInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean removeBind(java.lang.String userAccount, java.lang.String[] serviceName, service.ws.north.isam.zte.com.OperatorInfo operatorInfo, service.ws.north.isam.zte.com.holders.ResultInfoHolder resInfoHolder) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "removeBind"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userAccount, serviceName, operatorInfo, resInfoHolder.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) _output.get(new javax.xml.namespace.QName("", "resInfoHolder"));
            } catch (java.lang.Exception _exception) {
                resInfoHolder.value = (service.ws.north.isam.zte.com.ResultInfo) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "resInfoHolder")), service.ws.north.isam.zte.com.ResultInfo.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
