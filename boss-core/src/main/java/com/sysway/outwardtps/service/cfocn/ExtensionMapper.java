/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package com.sysway.outwardtps.service.cfocn;


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
        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "deviceFeedBack".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.DeviceFeedBack.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "deviceFeedBackResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponse.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "workOrderResp".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.WorkOrderResp.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "returnWorkOrderResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponse.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "returnWorkOrder".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.ReturnWorkOrder.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "replyManuallyInfluencedWorkOrderResponse".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponse.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "resultHead".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.ResultHead.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "productInfo".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.ProductInfo.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "replyManuallyInfluencedWorkOrder".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrder.Factory.parse(reader);
        }

        if ("http://cfocn.service.outwardtps.sysway.com/".equals(namespaceURI) &&
                "deviceInfo".equals(typeName)) {
            return com.sysway.outwardtps.service.cfocn.DeviceInfo.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
