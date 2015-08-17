/**
 * AgentPaymentServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.sysway.outwardtps.service.pay;

import com.ycsoft.business.service.impl.UserServiceSN;

/**
 *  AgentPaymentServiceSkeleton java skeleton for the axisService
 */
public class AgentPaymentServiceSkeleton
    implements AgentPaymentServiceSkeletonInterface {
	
	private UserServiceSN userServiceSN;
	
    /**
     * Auto generated method signature
     *
     * @param recharge0
     * @return rechargeResponse1
     */
    public com.sysway.outwardtps.service.pay.RechargeResponse1 recharge(
        com.sysway.outwardtps.service.pay.RechargeE recharge0) {
    	
    	System.out.println("注入了spring service: " + userServiceSN);
    	
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " +
            this.getClass().getName() + "#recharge");
    }

    /**
     * Auto generated method signature
     *
     * @param customerValidate2
     * @return customerValidateResponse3
     */
    public com.sysway.outwardtps.service.pay.CustomerValidateResponse0 customerValidate(
        com.sysway.outwardtps.service.pay.CustomerValidateE customerValidate2) {
    	
    	System.out.println("注入了spring service: " + userServiceSN);
    	
    	//TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " +
            this.getClass().getName() + "#customerValidate");
    }

	public void setUserServiceSN(UserServiceSN userServiceSN) {
		this.userServiceSN = userServiceSN;
	}
}
