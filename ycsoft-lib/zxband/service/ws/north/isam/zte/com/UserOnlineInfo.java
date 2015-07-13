/**
 * UserOnlineInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class UserOnlineInfo  implements java.io.Serializable {
    private java.util.Calendar accessTime;

    private java.lang.String account;

    private java.lang.String clientIp;

    private java.lang.String clientMac;

    private java.lang.String nasIp;

    private java.lang.String nasPort;

    private int nasPortType;

    private java.lang.String userGroupName;

    private java.lang.String userServiceName;

    private int vlan;

    public UserOnlineInfo() {
    }

    public UserOnlineInfo(
           java.util.Calendar accessTime,
           java.lang.String account,
           java.lang.String clientIp,
           java.lang.String clientMac,
           java.lang.String nasIp,
           java.lang.String nasPort,
           int nasPortType,
           java.lang.String userGroupName,
           java.lang.String userServiceName,
           int vlan) {
           this.accessTime = accessTime;
           this.account = account;
           this.clientIp = clientIp;
           this.clientMac = clientMac;
           this.nasIp = nasIp;
           this.nasPort = nasPort;
           this.nasPortType = nasPortType;
           this.userGroupName = userGroupName;
           this.userServiceName = userServiceName;
           this.vlan = vlan;
    }


    /**
     * Gets the accessTime value for this UserOnlineInfo.
     * 
     * @return accessTime
     */
    public java.util.Calendar getAccessTime() {
        return accessTime;
    }


    /**
     * Sets the accessTime value for this UserOnlineInfo.
     * 
     * @param accessTime
     */
    public void setAccessTime(java.util.Calendar accessTime) {
        this.accessTime = accessTime;
    }


    /**
     * Gets the account value for this UserOnlineInfo.
     * 
     * @return account
     */
    public java.lang.String getAccount() {
        return account;
    }


    /**
     * Sets the account value for this UserOnlineInfo.
     * 
     * @param account
     */
    public void setAccount(java.lang.String account) {
        this.account = account;
    }


    /**
     * Gets the clientIp value for this UserOnlineInfo.
     * 
     * @return clientIp
     */
    public java.lang.String getClientIp() {
        return clientIp;
    }


    /**
     * Sets the clientIp value for this UserOnlineInfo.
     * 
     * @param clientIp
     */
    public void setClientIp(java.lang.String clientIp) {
        this.clientIp = clientIp;
    }


    /**
     * Gets the clientMac value for this UserOnlineInfo.
     * 
     * @return clientMac
     */
    public java.lang.String getClientMac() {
        return clientMac;
    }


    /**
     * Sets the clientMac value for this UserOnlineInfo.
     * 
     * @param clientMac
     */
    public void setClientMac(java.lang.String clientMac) {
        this.clientMac = clientMac;
    }


    /**
     * Gets the nasIp value for this UserOnlineInfo.
     * 
     * @return nasIp
     */
    public java.lang.String getNasIp() {
        return nasIp;
    }


    /**
     * Sets the nasIp value for this UserOnlineInfo.
     * 
     * @param nasIp
     */
    public void setNasIp(java.lang.String nasIp) {
        this.nasIp = nasIp;
    }


    /**
     * Gets the nasPort value for this UserOnlineInfo.
     * 
     * @return nasPort
     */
    public java.lang.String getNasPort() {
        return nasPort;
    }


    /**
     * Sets the nasPort value for this UserOnlineInfo.
     * 
     * @param nasPort
     */
    public void setNasPort(java.lang.String nasPort) {
        this.nasPort = nasPort;
    }


    /**
     * Gets the nasPortType value for this UserOnlineInfo.
     * 
     * @return nasPortType
     */
    public int getNasPortType() {
        return nasPortType;
    }


    /**
     * Sets the nasPortType value for this UserOnlineInfo.
     * 
     * @param nasPortType
     */
    public void setNasPortType(int nasPortType) {
        this.nasPortType = nasPortType;
    }


    /**
     * Gets the userGroupName value for this UserOnlineInfo.
     * 
     * @return userGroupName
     */
    public java.lang.String getUserGroupName() {
        return userGroupName;
    }


    /**
     * Sets the userGroupName value for this UserOnlineInfo.
     * 
     * @param userGroupName
     */
    public void setUserGroupName(java.lang.String userGroupName) {
        this.userGroupName = userGroupName;
    }


    /**
     * Gets the userServiceName value for this UserOnlineInfo.
     * 
     * @return userServiceName
     */
    public java.lang.String getUserServiceName() {
        return userServiceName;
    }


    /**
     * Sets the userServiceName value for this UserOnlineInfo.
     * 
     * @param userServiceName
     */
    public void setUserServiceName(java.lang.String userServiceName) {
        this.userServiceName = userServiceName;
    }


    /**
     * Gets the vlan value for this UserOnlineInfo.
     * 
     * @return vlan
     */
    public int getVlan() {
        return vlan;
    }


    /**
     * Sets the vlan value for this UserOnlineInfo.
     * 
     * @param vlan
     */
    public void setVlan(int vlan) {
        this.vlan = vlan;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserOnlineInfo)) return false;
        UserOnlineInfo other = (UserOnlineInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accessTime==null && other.getAccessTime()==null) || 
             (this.accessTime!=null &&
              this.accessTime.equals(other.getAccessTime()))) &&
            ((this.account==null && other.getAccount()==null) || 
             (this.account!=null &&
              this.account.equals(other.getAccount()))) &&
            ((this.clientIp==null && other.getClientIp()==null) || 
             (this.clientIp!=null &&
              this.clientIp.equals(other.getClientIp()))) &&
            ((this.clientMac==null && other.getClientMac()==null) || 
             (this.clientMac!=null &&
              this.clientMac.equals(other.getClientMac()))) &&
            ((this.nasIp==null && other.getNasIp()==null) || 
             (this.nasIp!=null &&
              this.nasIp.equals(other.getNasIp()))) &&
            ((this.nasPort==null && other.getNasPort()==null) || 
             (this.nasPort!=null &&
              this.nasPort.equals(other.getNasPort()))) &&
            this.nasPortType == other.getNasPortType() &&
            ((this.userGroupName==null && other.getUserGroupName()==null) || 
             (this.userGroupName!=null &&
              this.userGroupName.equals(other.getUserGroupName()))) &&
            ((this.userServiceName==null && other.getUserServiceName()==null) || 
             (this.userServiceName!=null &&
              this.userServiceName.equals(other.getUserServiceName()))) &&
            this.vlan == other.getVlan();
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
        if (getAccessTime() != null) {
            _hashCode += getAccessTime().hashCode();
        }
        if (getAccount() != null) {
            _hashCode += getAccount().hashCode();
        }
        if (getClientIp() != null) {
            _hashCode += getClientIp().hashCode();
        }
        if (getClientMac() != null) {
            _hashCode += getClientMac().hashCode();
        }
        if (getNasIp() != null) {
            _hashCode += getNasIp().hashCode();
        }
        if (getNasPort() != null) {
            _hashCode += getNasPort().hashCode();
        }
        _hashCode += getNasPortType();
        if (getUserGroupName() != null) {
            _hashCode += getUserGroupName().hashCode();
        }
        if (getUserServiceName() != null) {
            _hashCode += getUserServiceName().hashCode();
        }
        _hashCode += getVlan();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserOnlineInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userOnlineInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accessTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account");
        elemField.setXmlName(new javax.xml.namespace.QName("", "account"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientIp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clientIp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientMac");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clientMac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("nasPortType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nasPortType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("userServiceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userServiceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vlan");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vlan"));
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
