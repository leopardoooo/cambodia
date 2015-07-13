/**
 * UserAcctServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class UserAcctServiceLocator extends org.apache.axis.client.Service implements service.ws.north.isam.zte.com.UserAcctService {

    public UserAcctServiceLocator() {
    }


    public UserAcctServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UserAcctServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UserAcctPort
    private java.lang.String UserAcctPort_address = "http://127.0.0.1:80/ZXISAM3NorthSVR/UserAcctService";

    public java.lang.String getUserAcctPortAddress() {
        return UserAcctPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UserAcctPortWSDDServiceName = "UserAcctPort";

    public java.lang.String getUserAcctPortWSDDServiceName() {
        return UserAcctPortWSDDServiceName;
    }

    public void setUserAcctPortWSDDServiceName(java.lang.String name) {
        UserAcctPortWSDDServiceName = name;
    }

    public service.ws.north.isam.zte.com.UserAcct getUserAcctPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UserAcctPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUserAcctPort(endpoint);
    }

    public service.ws.north.isam.zte.com.UserAcct getUserAcctPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            service.ws.north.isam.zte.com.UserAcctBindingStub _stub = new service.ws.north.isam.zte.com.UserAcctBindingStub(portAddress, this);
            _stub.setPortName(getUserAcctPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUserAcctPortEndpointAddress(java.lang.String address) {
        UserAcctPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (service.ws.north.isam.zte.com.UserAcct.class.isAssignableFrom(serviceEndpointInterface)) {
                service.ws.north.isam.zte.com.UserAcctBindingStub _stub = new service.ws.north.isam.zte.com.UserAcctBindingStub(new java.net.URL(UserAcctPort_address), this);
                _stub.setPortName(getUserAcctPortWSDDServiceName());
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
        if ("UserAcctPort".equals(inputPortName)) {
            return getUserAcctPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "UserAcctService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "UserAcctPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UserAcctPort".equals(portName)) {
            setUserAcctPortEndpointAddress(address);
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
