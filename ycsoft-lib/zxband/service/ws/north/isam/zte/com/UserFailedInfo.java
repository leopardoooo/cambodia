/**
 * UserFailedInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class UserFailedInfo  implements java.io.Serializable {
    private java.lang.String account;

    private java.lang.String authFailedCause;

    private java.util.Calendar authFailedTime;

    private int nasGutter;

    private java.lang.String nasIp;

    private java.lang.String nasPort;

    private java.lang.String userGroupName;

    private java.lang.String userIp;

    private java.lang.String userMac;

    private java.lang.String userServiceName;

    private int vlanId;

    public UserFailedInfo() {
    }

    public UserFailedInfo(
           java.lang.String account,
           java.lang.String authFailedCause,
           java.util.Calendar authFailedTime,
           int nasGutter,
           java.lang.String nasIp,
           java.lang.String nasPort,
           java.lang.String userGroupName,
           java.lang.String userIp,
           java.lang.String userMac,
           java.lang.String userServiceName,
           int vlanId) {
           this.account = account;
           this.authFailedCause = authFailedCause;
           this.authFailedTime = authFailedTime;
           this.nasGutter = nasGutter;
           this.nasIp = nasIp;
           this.nasPort = nasPort;
           this.userGroupName = userGroupName;
           this.userIp = userIp;
           this.userMac = userMac;
           this.userServiceName = userServiceName;
           this.vlanId = vlanId;
    }


    /**
     * Gets the account value for this UserFailedInfo.
     * 
     * @return account
     */
    public java.lang.String getAccount() {
        return account;
    }


    /**
     * Sets the account value for this UserFailedInfo.
     * 
     * @param account
     */
    public void setAccount(java.lang.String account) {
        this.account = account;
    }


    /**
     * Gets the authFailedCause value for this UserFailedInfo.
     * 
     * @return authFailedCause
     */
    public java.lang.String getAuthFailedCause() {
        return authFailedCause;
    }


    /**
     * Sets the authFailedCause value for this UserFailedInfo.
     * 
     * @param authFailedCause
     */
    public void setAuthFailedCause(java.lang.String authFailedCause) {
        this.authFailedCause = authFailedCause;
    }


    /**
     * Gets the authFailedTime value for this UserFailedInfo.
     * 
     * @return authFailedTime
     */
    public java.util.Calendar getAuthFailedTime() {
        return authFailedTime;
    }


    /**
     * Sets the authFailedTime value for this UserFailedInfo.
     * 
     * @param authFailedTime
     */
    public void setAuthFailedTime(java.util.Calendar authFailedTime) {
        this.authFailedTime = authFailedTime;
    }


    /**
     * Gets the nasGutter value for this UserFailedInfo.
     * 
     * @return nasGutter
     */
    public int getNasGutter() {
        return nasGutter;
    }


    /**
     * Sets the nasGutter value for this UserFailedInfo.
     * 
     * @param nasGutter
     */
    public void setNasGutter(int nasGutter) {
        this.nasGutter = nasGutter;
    }


    /**
     * Gets the nasIp value for this UserFailedInfo.
     * 
     * @return nasIp
     */
    public java.lang.String getNasIp() {
        return nasIp;
    }


    /**
     * Sets the nasIp value for this UserFailedInfo.
     * 
     * @param nasIp
     */
    public void setNasIp(java.lang.String nasIp) {
        this.nasIp = nasIp;
    }


    /**
     * Gets the nasPort value for this UserFailedInfo.
     * 
     * @return nasPort
     */
    public java.lang.String getNasPort() {
        return nasPort;
    }


    /**
     * Sets the nasPort value for this UserFailedInfo.
     * 
     * @param nasPort
     */
    public void setNasPort(java.lang.String nasPort) {
        this.nasPort = nasPort;
    }


    /**
     * Gets the userGroupName value for this UserFailedInfo.
     * 
     * @return userGroupName
     */
    public java.lang.String getUserGroupName() {
        return userGroupName;
    }


    /**
     * Sets the userGroupName value for this UserFailedInfo.
     * 
     * @param userGroupName
     */
    public void setUserGroupName(java.lang.String userGroupName) {
        this.userGroupName = userGroupName;
    }


    /**
     * Gets the userIp value for this UserFailedInfo.
     * 
     * @return userIp
     */
    public java.lang.String getUserIp() {
        return userIp;
    }


    /**
     * Sets the userIp value for this UserFailedInfo.
     * 
     * @param userIp
     */
    public void setUserIp(java.lang.String userIp) {
        this.userIp = userIp;
    }


    /**
     * Gets the userMac value for this UserFailedInfo.
     * 
     * @return userMac
     */
    public java.lang.String getUserMac() {
        return userMac;
    }


    /**
     * Sets the userMac value for this UserFailedInfo.
     * 
     * @param userMac
     */
    public void setUserMac(java.lang.String userMac) {
        this.userMac = userMac;
    }


    /**
     * Gets the userServiceName value for this UserFailedInfo.
     * 
     * @return userServiceName
     */
    public java.lang.String getUserServiceName() {
        return userServiceName;
    }


    /**
     * Sets the userServiceName value for this UserFailedInfo.
     * 
     * @param userServiceName
     */
    public void setUserServiceName(java.lang.String userServiceName) {
        this.userServiceName = userServiceName;
    }


    /**
     * Gets the vlanId value for this UserFailedInfo.
     * 
     * @return vlanId
     */
    public int getVlanId() {
        return vlanId;
    }


    /**
     * Sets the vlanId value for this UserFailedInfo.
     * 
     * @param vlanId
     */
    public void setVlanId(int vlanId) {
        this.vlanId = vlanId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserFailedInfo)) return false;
        UserFailedInfo other = (UserFailedInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.account==null && other.getAccount()==null) || 
             (this.account!=null &&
              this.account.equals(other.getAccount()))) &&
            ((this.authFailedCause==null && other.getAuthFailedCause()==null) || 
             (this.authFailedCause!=null &&
              this.authFailedCause.equals(other.getAuthFailedCause()))) &&
            ((this.authFailedTime==null && other.getAuthFailedTime()==null) || 
             (this.authFailedTime!=null &&
              this.authFailedTime.equals(other.getAuthFailedTime()))) &&
            this.nasGutter == other.getNasGutter() &&
            ((this.nasIp==null && other.getNasIp()==null) || 
             (this.nasIp!=null &&
              this.nasIp.equals(other.getNasIp()))) &&
            ((this.nasPort==null && other.getNasPort()==null) || 
             (this.nasPort!=null &&
              this.nasPort.equals(other.getNasPort()))) &&
            ((this.userGroupName==null && other.getUserGroupName()==null) || 
             (this.userGroupName!=null &&
              this.userGroupName.equals(other.getUserGroupName()))) &&
            ((this.userIp==null && other.getUserIp()==null) || 
             (this.userIp!=null &&
              this.userIp.equals(other.getUserIp()))) &&
            ((this.userMac==null && other.getUserMac()==null) || 
             (this.userMac!=null &&
              this.userMac.equals(other.getUserMac()))) &&
            ((this.userServiceName==null && other.getUserServiceName()==null) || 
             (this.userServiceName!=null &&
              this.userServiceName.equals(other.getUserServiceName()))) &&
            this.vlanId == other.getVlanId();
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
        if (getAccount() != null) {
            _hashCode += getAccount().hashCode();
        }
        if (getAuthFailedCause() != null) {
            _hashCode += getAuthFailedCause().hashCode();
        }
        if (getAuthFailedTime() != null) {
            _hashCode += getAuthFailedTime().hashCode();
        }
        _hashCode += getNasGutter();
        if (getNasIp() != null) {
            _hashCode += getNasIp().hashCode();
        }
        if (getNasPort() != null) {
            _hashCode += getNasPort().hashCode();
        }
        if (getUserGroupName() != null) {
            _hashCode += getUserGroupName().hashCode();
        }
        if (getUserIp() != null) {
            _hashCode += getUserIp().hashCode();
        }
        if (getUserMac() != null) {
            _hashCode += getUserMac().hashCode();
        }
        if (getUserServiceName() != null) {
            _hashCode += getUserServiceName().hashCode();
        }
        _hashCode += getVlanId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserFailedInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userFailedInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account");
        elemField.setXmlName(new javax.xml.namespace.QName("", "account"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authFailedCause");
        elemField.setXmlName(new javax.xml.namespace.QName("", "authFailedCause"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authFailedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "authFailedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nasGutter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nasGutter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nasIp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nasIp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nasPort");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nasPort"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userGroupName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userGroupName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userIp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userIp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userMac");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userMac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userServiceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userServiceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vlanId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vlanId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
