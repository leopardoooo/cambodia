/**
 * QueryOnlineParam.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class QueryOnlineParam  implements java.io.Serializable {
    private java.lang.String groupName;

    private java.lang.String[] queryTimeRange;

    private int queryType;

    private java.lang.String serviceName;

    private java.lang.String userAccount;

    public QueryOnlineParam() {
    }

    public QueryOnlineParam(
           java.lang.String groupName,
           java.lang.String[] queryTimeRange,
           int queryType,
           java.lang.String serviceName,
           java.lang.String userAccount) {
           this.groupName = groupName;
           this.queryTimeRange = queryTimeRange;
           this.queryType = queryType;
           this.serviceName = serviceName;
           this.userAccount = userAccount;
    }


    /**
     * Gets the groupName value for this QueryOnlineParam.
     * 
     * @return groupName
     */
    public java.lang.String getGroupName() {
        return groupName;
    }


    /**
     * Sets the groupName value for this QueryOnlineParam.
     * 
     * @param groupName
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }


    /**
     * Gets the queryTimeRange value for this QueryOnlineParam.
     * 
     * @return queryTimeRange
     */
    public java.lang.String[] getQueryTimeRange() {
        return queryTimeRange;
    }


    /**
     * Sets the queryTimeRange value for this QueryOnlineParam.
     * 
     * @param queryTimeRange
     */
    public void setQueryTimeRange(java.lang.String[] queryTimeRange) {
        this.queryTimeRange = queryTimeRange;
    }

    public java.lang.String getQueryTimeRange(int i) {
        return this.queryTimeRange[i];
    }

    public void setQueryTimeRange(int i, java.lang.String _value) {
        this.queryTimeRange[i] = _value;
    }


    /**
     * Gets the queryType value for this QueryOnlineParam.
     * 
     * @return queryType
     */
    public int getQueryType() {
        return queryType;
    }


    /**
     * Sets the queryType value for this QueryOnlineParam.
     * 
     * @param queryType
     */
    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }


    /**
     * Gets the serviceName value for this QueryOnlineParam.
     * 
     * @return serviceName
     */
    public java.lang.String getServiceName() {
        return serviceName;
    }


    /**
     * Sets the serviceName value for this QueryOnlineParam.
     * 
     * @param serviceName
     */
    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }


    /**
     * Gets the userAccount value for this QueryOnlineParam.
     * 
     * @return userAccount
     */
    public java.lang.String getUserAccount() {
        return userAccount;
    }


    /**
     * Sets the userAccount value for this QueryOnlineParam.
     * 
     * @param userAccount
     */
    public void setUserAccount(java.lang.String userAccount) {
        this.userAccount = userAccount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryOnlineParam)) return false;
        QueryOnlineParam other = (QueryOnlineParam) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.groupName==null && other.getGroupName()==null) || 
             (this.groupName!=null &&
              this.groupName.equals(other.getGroupName()))) &&
            ((this.queryTimeRange==null && other.getQueryTimeRange()==null) || 
             (this.queryTimeRange!=null &&
              java.util.Arrays.equals(this.queryTimeRange, other.getQueryTimeRange()))) &&
            this.queryType == other.getQueryType() &&
            ((this.serviceName==null && other.getServiceName()==null) || 
             (this.serviceName!=null &&
              this.serviceName.equals(other.getServiceName()))) &&
            ((this.userAccount==null && other.getUserAccount()==null) || 
             (this.userAccount!=null &&
              this.userAccount.equals(other.getUserAccount())));
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
        if (getGroupName() != null) {
            _hashCode += getGroupName().hashCode();
        }
        if (getQueryTimeRange() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getQueryTimeRange());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQueryTimeRange(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getQueryType();
        if (getServiceName() != null) {
            _hashCode += getServiceName().hashCode();
        }
        if (getUserAccount() != null) {
            _hashCode += getUserAccount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryOnlineParam.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "queryOnlineParam"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "groupName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryTimeRange");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryTimeRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
