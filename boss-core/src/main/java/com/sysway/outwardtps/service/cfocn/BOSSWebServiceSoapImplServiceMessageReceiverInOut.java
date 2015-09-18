/**
 * BOSSWebServiceSoapImplServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.sysway.outwardtps.service.cfocn;


/**
 *  BOSSWebServiceSoapImplServiceMessageReceiverInOut message receiver
 */
public class BOSSWebServiceSoapImplServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {
    public void invokeBusinessLogic(
        org.apache.axis2.context.MessageContext msgContext,
        org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault {
        try {
            // get the implementation class for the Web Service
            Object obj = getTheImplementationObject(msgContext);

            BOSSWebServiceSoapImplServiceSkeletonInterface skel = (BOSSWebServiceSoapImplServiceSkeletonInterface) obj;

            //Out Envelop
            org.apache.axiom.soap.SOAPEnvelope envelope = null;

            //Find the axisOperation that has been set by the Dispatch phase.
            org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext()
                                                                      .getAxisOperation();

            if (op == null) {
                throw new org.apache.axis2.AxisFault(
                    "Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
            }

            java.lang.String methodName;

            if ((op.getName() != null) &&
                    ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(
                            op.getName().getLocalPart())) != null)) {
                if ("replyManuallyInfluencedWorkOrder".equals(methodName)) {
                    com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE replyManuallyInfluencedWorkOrderResponse7 =
                        null;
                    com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE wrappedParam =
                        (com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE) fromOM(msgContext.getEnvelope()
                                                                                                                 .getBody()
                                                                                                                 .getFirstElement(),
                            com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE.class,
                            getEnvelopeNamespaces(msgContext.getEnvelope()));

                    replyManuallyInfluencedWorkOrderResponse7 = skel.replyManuallyInfluencedWorkOrder(wrappedParam);

                    envelope = toEnvelope(getSOAPFactory(msgContext),
                            replyManuallyInfluencedWorkOrderResponse7, false,
                            new javax.xml.namespace.QName(
                                "http://cfocn.service.outwardtps.sysway.com/",
                                "replyManuallyInfluencedWorkOrder"));
                } else
                 if ("returnWorkOrder".equals(methodName)) {
                    com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE returnWorkOrderResponse9 =
                        null;
                    com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE wrappedParam =
                        (com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE) fromOM(msgContext.getEnvelope()
                                                                                                .getBody()
                                                                                                .getFirstElement(),
                            com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE.class,
                            getEnvelopeNamespaces(msgContext.getEnvelope()));

                    returnWorkOrderResponse9 = skel.returnWorkOrder(wrappedParam);

                    envelope = toEnvelope(getSOAPFactory(msgContext),
                            returnWorkOrderResponse9, false,
                            new javax.xml.namespace.QName(
                                "http://cfocn.service.outwardtps.sysway.com/",
                                "returnWorkOrder"));
                } else
                 if ("deviceFeedBack".equals(methodName)) {
                    com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE deviceFeedBackResponse11 =
                        null;
                    com.sysway.outwardtps.service.cfocn.DeviceFeedBackE wrappedParam =
                        (com.sysway.outwardtps.service.cfocn.DeviceFeedBackE) fromOM(msgContext.getEnvelope()
                                                                                               .getBody()
                                                                                               .getFirstElement(),
                            com.sysway.outwardtps.service.cfocn.DeviceFeedBackE.class,
                            getEnvelopeNamespaces(msgContext.getEnvelope()));

                    deviceFeedBackResponse11 = skel.deviceFeedBack(wrappedParam);

                    envelope = toEnvelope(getSOAPFactory(msgContext),
                            deviceFeedBackResponse11, false,
                            new javax.xml.namespace.QName(
                                "http://cfocn.service.outwardtps.sysway.com/",
                                "deviceFeedBack"));
                } else {
                    throw new java.lang.RuntimeException("method not found");
                }

                newMsgContext.setEnvelope(envelope);
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    //
    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.cfocn.DeviceFeedBackE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.cfocn.DeviceFeedBackE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
        org.apache.axiom.soap.SOAPFactory factory,
        com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE param,
        boolean optimizeContent, javax.xml.namespace.QName methodQName)
        throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope.getBody()
                         .addChild(param.getOMElement(
                    com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE.MY_QNAME,
                    factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE wrapreplyManuallyInfluencedWorkOrder() {
        com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE wrappedElement =
            new com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE();

        return wrappedElement;
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
        org.apache.axiom.soap.SOAPFactory factory,
        com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE param,
        boolean optimizeContent, javax.xml.namespace.QName methodQName)
        throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope.getBody()
                         .addChild(param.getOMElement(
                    com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE.MY_QNAME,
                    factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE wrapreturnWorkOrder() {
        com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE wrappedElement =
            new com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE();

        return wrappedElement;
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
        org.apache.axiom.soap.SOAPFactory factory,
        com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE param,
        boolean optimizeContent, javax.xml.namespace.QName methodQName)
        throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope.getBody()
                         .addChild(param.getOMElement(
                    com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE.MY_QNAME,
                    factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE wrapdeviceFeedBack() {
        com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE wrappedElement =
            new com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE();

        return wrappedElement;
    }

    /**
     *  get the default envelope
     */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
        org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private java.lang.Object fromOM(org.apache.axiom.om.OMElement param,
        java.lang.Class type, java.util.Map extraNamespaces)
        throws org.apache.axis2.AxisFault {
        try {
            if (com.sysway.outwardtps.service.cfocn.DeviceFeedBackE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.cfocn.DeviceFeedBackE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.cfocn.DeviceFeedBackResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.cfocn.ReplyManuallyInfluencedWorkOrderResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.cfocn.ReturnWorkOrderE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.cfocn.ReturnWorkOrderResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }

        return null;
    }

    /**
     *  A utility method that copies the namepaces from the SOAPEnvelope
     */
    private java.util.Map getEnvelopeNamespaces(
        org.apache.axiom.soap.SOAPEnvelope env) {
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();

        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }

        return returnMap;
    }

    private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();

        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }
} //end of class
