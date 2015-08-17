/**
 * AgentPaymentServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.sysway.outwardtps.service.pay;


/**
 *  AgentPaymentServiceMessageReceiverInOut message receiver
 */
public class AgentPaymentServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {
    public void invokeBusinessLogic(
        org.apache.axis2.context.MessageContext msgContext,
        org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault {
        try {
            // get the implementation class for the Web Service
            Object obj = getTheImplementationObject(msgContext);

            AgentPaymentServiceSkeletonInterface skel = (AgentPaymentServiceSkeletonInterface) obj;

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
                if ("recharge".equals(methodName)) {
                    com.sysway.outwardtps.service.pay.RechargeResponse1 rechargeResponse5 =
                        null;
                    com.sysway.outwardtps.service.pay.RechargeE wrappedParam = (com.sysway.outwardtps.service.pay.RechargeE) fromOM(msgContext.getEnvelope()
                                                                                                                                              .getBody()
                                                                                                                                              .getFirstElement(),
                            com.sysway.outwardtps.service.pay.RechargeE.class,
                            getEnvelopeNamespaces(msgContext.getEnvelope()));

                    rechargeResponse5 = skel.recharge(wrappedParam);

                    envelope = toEnvelope(getSOAPFactory(msgContext),
                            rechargeResponse5, false,
                            new javax.xml.namespace.QName(
                                "http://pay.service.outwardtps.sysway.com/",
                                "recharge"));
                } else
                 if ("customerValidate".equals(methodName)) {
                    com.sysway.outwardtps.service.pay.CustomerValidateResponse0 customerValidateResponse7 =
                        null;
                    com.sysway.outwardtps.service.pay.CustomerValidateE wrappedParam =
                        (com.sysway.outwardtps.service.pay.CustomerValidateE) fromOM(msgContext.getEnvelope()
                                                                                               .getBody()
                                                                                               .getFirstElement(),
                            com.sysway.outwardtps.service.pay.CustomerValidateE.class,
                            getEnvelopeNamespaces(msgContext.getEnvelope()));

                    customerValidateResponse7 = skel.customerValidate(wrappedParam);

                    envelope = toEnvelope(getSOAPFactory(msgContext),
                            customerValidateResponse7, false,
                            new javax.xml.namespace.QName(
                                "http://pay.service.outwardtps.sysway.com/",
                                "customerValidate"));
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
        com.sysway.outwardtps.service.pay.RechargeE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.pay.RechargeE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.pay.RechargeResponse1 param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.pay.RechargeResponse1.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.pay.CustomerValidateE param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.pay.CustomerValidateE.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(
        com.sysway.outwardtps.service.pay.CustomerValidateResponse0 param,
        boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(com.sysway.outwardtps.service.pay.CustomerValidateResponse0.MY_QNAME,
                org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
        org.apache.axiom.soap.SOAPFactory factory,
        com.sysway.outwardtps.service.pay.CustomerValidateResponse0 param,
        boolean optimizeContent, javax.xml.namespace.QName methodQName)
        throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope.getBody()
                         .addChild(param.getOMElement(
                    com.sysway.outwardtps.service.pay.CustomerValidateResponse0.MY_QNAME,
                    factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private com.sysway.outwardtps.service.pay.CustomerValidateResponse0 wrapcustomerValidate() {
        com.sysway.outwardtps.service.pay.CustomerValidateResponse0 wrappedElement =
            new com.sysway.outwardtps.service.pay.CustomerValidateResponse0();

        return wrappedElement;
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
        org.apache.axiom.soap.SOAPFactory factory,
        com.sysway.outwardtps.service.pay.RechargeResponse1 param,
        boolean optimizeContent, javax.xml.namespace.QName methodQName)
        throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope.getBody()
                         .addChild(param.getOMElement(
                    com.sysway.outwardtps.service.pay.RechargeResponse1.MY_QNAME,
                    factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private com.sysway.outwardtps.service.pay.RechargeResponse1 wraprecharge() {
        com.sysway.outwardtps.service.pay.RechargeResponse1 wrappedElement = new com.sysway.outwardtps.service.pay.RechargeResponse1();

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
            if (com.sysway.outwardtps.service.pay.CustomerValidateE.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.pay.CustomerValidateE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.pay.CustomerValidateResponse0.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.pay.CustomerValidateResponse0.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.pay.RechargeE.class.equals(type)) {
                return com.sysway.outwardtps.service.pay.RechargeE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (com.sysway.outwardtps.service.pay.RechargeResponse1.class.equals(
                        type)) {
                return com.sysway.outwardtps.service.pay.RechargeResponse1.Factory.parse(param.getXMLStreamReaderWithoutCaching());
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
