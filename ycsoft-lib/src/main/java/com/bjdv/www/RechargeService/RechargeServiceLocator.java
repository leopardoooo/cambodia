/**
 * RechargeServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjdv.www.RechargeService;

public class RechargeServiceLocator extends org.apache.axis.client.Service implements com.bjdv.www.RechargeService.RechargeService {

    public RechargeServiceLocator() {
    }


    public RechargeServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RechargeServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RechargeServiceHttpPort
    private java.lang.String RechargeServiceHttpPort_address = "http://127.0.0.1:8088/recharge_interface/services/RechargeService";

    public java.lang.String getRechargeServiceHttpPortAddress() {
        return RechargeServiceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RechargeServiceHttpPortWSDDServiceName = "RechargeServiceHttpPort";

    public java.lang.String getRechargeServiceHttpPortWSDDServiceName() {
        return RechargeServiceHttpPortWSDDServiceName;
    }

    public void setRechargeServiceHttpPortWSDDServiceName(java.lang.String name) {
        RechargeServiceHttpPortWSDDServiceName = name;
    }

    public com.bjdv.www.RechargeService.RechargeServicePortType getRechargeServiceHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RechargeServiceHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRechargeServiceHttpPort(endpoint);
    }

    public com.bjdv.www.RechargeService.RechargeServicePortType getRechargeServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.bjdv.www.RechargeService.RechargeServiceHttpBindingStub _stub = new com.bjdv.www.RechargeService.RechargeServiceHttpBindingStub(portAddress, this);
            _stub.setPortName(getRechargeServiceHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRechargeServiceHttpPortEndpointAddress(java.lang.String address) {
        RechargeServiceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.bjdv.www.RechargeService.RechargeServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.bjdv.www.RechargeService.RechargeServiceHttpBindingStub _stub = new com.bjdv.www.RechargeService.RechargeServiceHttpBindingStub(new java.net.URL(RechargeServiceHttpPort_address), this);
                _stub.setPortName(getRechargeServiceHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RechargeServiceHttpPort".equals(inputPortName)) {
            return getRechargeServiceHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.bjdv.com/RechargeService", "RechargeService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.bjdv.com/RechargeService", "RechargeServiceHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RechargeServiceHttpPort".equals(portName)) {
            setRechargeServiceHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
