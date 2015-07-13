/**
 * RetUserOnlineInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class RetUserOnlineInfo  implements java.io.Serializable {
    private int count;

    private service.ws.north.isam.zte.com.UserOnlineInfo[] userOnlineList;

    public RetUserOnlineInfo() {
    }

    public RetUserOnlineInfo(
           int count,
           service.ws.north.isam.zte.com.UserOnlineInfo[] userOnlineList) {
           this.count = count;
           this.userOnlineList = userOnlineList;
    }


    /**
     * Gets the count value for this RetUserOnlineInfo.
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }


    /**
     * Sets the count value for this RetUserOnlineInfo.
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    /**
     * Gets the userOnlineList value for this RetUserOnlineInfo.
     * 
     * @return userOnlineList
     */
    public service.ws.north.isam.zte.com.UserOnlineInfo[] getUserOnlineList() {
        return userOnlineList;
    }


    /**
     * Sets the userOnlineList value for this RetUserOnlineInfo.
     * 
     * @param userOnlineList
     */
    public void setUserOnlineList(service.ws.north.isam.zte.com.UserOnlineInfo[] userOnlineList) {
        this.userOnlineList = userOnlineList;
    }

    public service.ws.north.isam.zte.com.UserOnlineInfo getUserOnlineList(int i) {
        return this.userOnlineList[i];
    }

    public void setUserOnlineList(int i, service.ws.north.isam.zte.com.UserOnlineInfo _value) {
        this.userOnlineList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetUserOnlineInfo)) return false;
        RetUserOnlineInfo other = (RetUserOnlineInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.count == other.getCount() &&
            ((this.userOnlineList==null && other.getUserOnlineList()==null) || 
             (this.userOnlineList!=null &&
              java.util.Arrays.equals(this.userOnlineList, other.getUserOnlineList())));
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
        _hashCode += getCount();
        if (getUserOnlineList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUserOnlineList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUserOnlineList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetUserOnlineInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserOnlineInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userOnlineList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userOnlineList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userOnlineInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
