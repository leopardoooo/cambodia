/**
 * ResultInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class ResultInfo  implements java.io.Serializable {
    private int infoNo;

    private java.lang.String infoStr;

    public ResultInfo() {
    }

    public ResultInfo(
           int infoNo,
           java.lang.String infoStr) {
           this.infoNo = infoNo;
           this.infoStr = infoStr;
    }


    /**
     * Gets the infoNo value for this ResultInfo.
     * 
     * @return infoNo
     */
    public int getInfoNo() {
        return infoNo;
    }


    /**
     * Sets the infoNo value for this ResultInfo.
     * 
     * @param infoNo
     */
    public void setInfoNo(int infoNo) {
        this.infoNo = infoNo;
    }


    /**
     * Gets the infoStr value for this ResultInfo.
     * 
     * @return infoStr
     */
    public java.lang.String getInfoStr() {
        return infoStr;
    }


    /**
     * Sets the infoStr value for this ResultInfo.
     * 
     * @param infoStr
     */
    public void setInfoStr(java.lang.String infoStr) {
        this.infoStr = infoStr;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultInfo)) return false;
        ResultInfo other = (ResultInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.infoNo == other.getInfoNo() &&
            ((this.infoStr==null && other.getInfoStr()==null) || 
             (this.infoStr!=null &&
              this.infoStr.equals(other.getInfoStr())));
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
        _hashCode += getInfoNo();
        if (getInfoStr() != null) {
            _hashCode += getInfoStr().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "resultInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoNo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoStr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoStr"));
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
