
package com.ycsoft.ly.intface.taskclient;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class Service1Client {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;
    private String wsUrl;

    public Service1Client(String wsUrl) {
    	this.wsUrl = wsUrl;
        create0();
        Endpoint Service1SoapEP = service0 .addEndpoint(new QName("http://tempuri.org/", "Service1Soap"), new QName("http://tempuri.org/", "Service1Soap"), wsUrl);
        endpoints.put(new QName("http://tempuri.org/", "Service1Soap"), Service1SoapEP);
        Endpoint Service1SoapLocalEndpointEP = service0 .addEndpoint(new QName("http://tempuri.org/", "Service1SoapLocalEndpoint"), new QName("http://tempuri.org/", "Service1SoapLocalBinding"), "xfire.local://Service1");
        endpoints.put(new QName("http://tempuri.org/", "Service1SoapLocalEndpoint"), Service1SoapLocalEndpointEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((com.ycsoft.ly.intface.taskclient.Service1Soap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://tempuri.org/", "Service1SoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://tempuri.org/", "Service1Soap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public Service1Soap getService1Soap() {
        return ((Service1Soap)(this).getEndpoint(new QName("http://tempuri.org/", "Service1Soap")));
    }

    public Service1Soap getService1Soap(String url) {
        Service1Soap var = getService1Soap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public Service1Soap getService1SoapLocalEndpoint() {
        return ((Service1Soap)(this).getEndpoint(new QName("http://tempuri.org/", "Service1SoapLocalEndpoint")));
    }

    public Service1Soap getService1SoapLocalEndpoint(String url) {
        Service1Soap var = getService1SoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public static void main(String[] args) {
        

//        Service1Client client = new Service1Client("http://192.168.16.188:280/webservice1/service1.asmx?WSDL");
        Service1Client client = new Service1Client("http://localhost:808/cc/Service1Soap?wsdl");
        
		//create a default service endpoint
        Service1Soap service = client.getService1Soap();
        service.createTask("1", "2", "3", "", "", "", "", "", "", "", "", "", "");
		//TODO: Add custom client code here
        		//
        		//service.yourServiceOperationHere();
        
		System.out.println("test client completed");
        		System.exit(0);
    }

}
