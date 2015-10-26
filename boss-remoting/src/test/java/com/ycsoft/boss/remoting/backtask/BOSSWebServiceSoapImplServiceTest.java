/**
 * BOSSWebServiceSoapImplServiceTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.ycsoft.boss.remoting.backtask;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;


/*
 *  BOSSWebServiceSoapImplServiceTest Junit test case
 */
public class BOSSWebServiceSoapImplServiceTest extends junit.framework.TestCase {
    /**
     * Auto generated test method
     */
    public void testreplyManuallyInfluencedWorkOrder()
        throws java.lang.Exception {
        com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub stub =
            new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub(); //the default implementation should point to the right endpoint

        com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReplyManuallyInfluencedWorkOrderE replyManuallyInfluencedWorkOrder6 =
            (com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReplyManuallyInfluencedWorkOrderE) getTestObject(com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReplyManuallyInfluencedWorkOrderE.class);
        // TODO : Fill in the replyManuallyInfluencedWorkOrder6 here
        assertNotNull(stub.replyManuallyInfluencedWorkOrder(
                replyManuallyInfluencedWorkOrder6));
    }

    /**
     * Auto generated test method
     */
    public void testreturnWorkOrder() throws java.lang.Exception {
        com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub stub =
            new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub(); //the default implementation should point to the right endpoint

        com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE returnWorkOrder8 =
            (com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE) getTestObject(com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.ReturnWorkOrderE.class);
        // TODO : Fill in the returnWorkOrder8 here
        assertNotNull(stub.returnWorkOrder(returnWorkOrder8));
    }

    /**
     * Auto generated test method
     */
    public void testdeviceFeedBack() throws java.lang.Exception {
        com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub stub =
            new com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub(); //the default implementation should point to the right endpoint

        com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceFeedBackE deviceFeedBack10 =
            (com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceFeedBackE) getTestObject(com.ycsoft.boss.remoting.backtask.BOSSWebServiceSoapImplServiceStub.DeviceFeedBackE.class);
        // TODO : Fill in the deviceFeedBack10 here
        assertNotNull(stub.deviceFeedBack(deviceFeedBack10));
    }

    //Create an ADBBean and provide it as the test object
    public org.apache.axis2.databinding.ADBBean getTestObject(
        java.lang.Class type) throws java.lang.Exception {
        return (org.apache.axis2.databinding.ADBBean) type.newInstance();
    }
}
