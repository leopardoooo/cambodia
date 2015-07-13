/**
 * RechargeReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjdv.bean;

public class RechargeReq  implements java.io.Serializable {
    private java.lang.String icCard;

    private java.lang.String password;

    private java.lang.String rechargeCard;

    private java.lang.String userName;

    public RechargeReq() {
    }

    public RechargeReq(
           java.lang.String icCard,
           java.lang.String password,
           java.lang.String rechargeCard,
           java.lang.String userName) {
           this.icCard = icCard;
           this.password = password;
           this.rechargeCard = rechargeCard;
           this.userName = userName;
    }


    /**
     * Gets the icCard value for this RechargeReq.
     * 
     * @return icCard
     */
    public java.lang.String getIcCard() {
        return icCard;
    }


    /**
     * Sets the icCard value for this RechargeReq.
     * 
     * @param icCard
     */
    public void setIcCard(java.lang.String icCard) {
        this.icCard = icCard;
    }


    /**
     * Gets the password value for this RechargeReq.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this RechargeReq.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the rechargeCard value for this RechargeReq.
     * 
     * @return rechargeCard
     */
    public java.lang.String getRechargeCard() {
        return rechargeCard;
    }


    /**
     * Sets the rechargeCard value for this RechargeReq.
     * 
     * @param rechargeCard
     */
    public void setRechargeCard(java.lang.String rechargeCard) {
        this.rechargeCard = rechargeCard;
    }


    /**
     * Gets the userName value for this RechargeReq.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this RechargeReq.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RechargeReq)) return false;
        RechargeReq other = (RechargeReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.icCard==null && other.getIcCard()==null) || 
             (this.icCard!=null &&
              this.icCard.equals(other.getIcCard()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.rechargeCard==null && other.getRechargeCard()==null) || 
             (this.rechargeCard!=null &&
              this.rechargeCard.equals(other.getRechargeCard()))) &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getIcCard() != null) {
            _hashCode += getIcCard().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getRechargeCard() != null) {
            _hashCode += getRechargeCard().hashCode();
        }
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RechargeReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://bean.bjdv.com", "RechargeReq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icCard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.bjdv.com", "icCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.bjdv.com", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rechargeCard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.bjdv.com", "rechargeCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.bjdv.com", "userName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
