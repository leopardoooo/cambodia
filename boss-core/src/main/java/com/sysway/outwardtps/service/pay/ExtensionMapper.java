/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package com.sysway.outwardtps.service.pay;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static java.lang.Object getTypeObject(
        java.lang.String namespaceURI, java.lang.String typeName,
        javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "CustomerValidateResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.CustomerValidateResponseE.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "RechargeRequest".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.RechargeRequest.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "CustomerValidateRequest".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.CustomerValidateRequest.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "recharge".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.Recharge.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "customerValidateResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.CustomerValidateResponse.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "CustomerValidateResponseMsg".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.CustomerValidateResponseMsg.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "customerValidate".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.CustomerValidate.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "CustomerValidateRequestMsg".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.CustomerValidateRequestMsg.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "RequestHeader".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.RequestHeader.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "ResponseHeader".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.ResponseHeader.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "RechargeResponseMsg".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.RechargeResponseMsg.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "RechargeResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.RechargeResponseE.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "rechargeResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.RechargeResponse.Factory.parse(reader);
        }

        if ("http://pay.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "RechargeRequestMsg".equals(typeName)) {
            return com.sysway.outwardtps.service.pay.RechargeRequestMsg.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
