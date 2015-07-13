/**
 * RetUserFailedInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class RetUserFailedInfo  implements java.io.Serializable {
    private int count;

    private service.ws.north.isam.zte.com.UserFailedInfo[] userFailedList;

    public RetUserFailedInfo() {
    }

    public RetUserFailedInfo(
           int count,
           service.ws.north.isam.zte.com.UserFailedInfo[] userFailedList) {
           this.count = count;
           this.userFailedList = userFailedList;
    }


    /**
     * Gets the count value for this RetUserFailedInfo.
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }


    /**
     * Sets the count value for this RetUserFailedInfo.
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    /**
     * Gets the userFailedList value for this RetUserFailedInfo.
     * 
     * @return userFailedList
     */
    public service.ws.north.isam.zte.com.UserFailedInfo[] getUserFailedList() {
        return userFailedList;
    }


    /**
     * Sets the userFailedList value for this RetUserFailedInfo.
     * 
     * @param userFailedList
     */
    public void setUserFailedList(service.ws.north.isam.zte.com.UserFailedInfo[] userFailedList) {
        this.userFailedList = userFailedList;
    }

    public service.ws.north.isam.zte.com.UserFailedInfo getUserFailedList(int i) {
        return this.userFailedList[i];
    }

    public void setUserFailedList(int i, service.ws.north.isam.zte.com.UserFailedInfo _value) {
        this.userFailedList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetUserFailedInfo)) return false;
        RetUserFailedInfo other = (RetUserFailedInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.count == other.getCount() &&
            ((this.userFailedList==null && other.getUserFailedList()==null) || 
             (this.userFailedList!=null &&
              java.util.Arrays.equals(this.userFailedList, other.getUserFailedList())));
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
        if (getUserFailedList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUserFailedList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUserFailedList(), i);
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
        new org.apache.axis.description.TypeDesc(RetUserFailedInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserFailedInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userFailedList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userFailedList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userFailedInfo"));
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
