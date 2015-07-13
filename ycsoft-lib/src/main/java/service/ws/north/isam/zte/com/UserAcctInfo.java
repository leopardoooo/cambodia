/**
 * UserAcctInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class UserAcctInfo  implements java.io.Serializable {
    private java.lang.String bankCard;

    private java.lang.String groupName;

    private java.lang.String idNumber;

    private java.lang.String notes;

    private java.lang.String password;

    private int payType;

    private int portLimit;

    private java.lang.String userAccount;

    private java.lang.String userAddress;

    private java.lang.String userEmail;

    private java.lang.String userTel;

    public UserAcctInfo() {
    }

    public UserAcctInfo(
           java.lang.String bankCard,
           java.lang.String groupName,
           java.lang.String idNumber,
           java.lang.String notes,
           java.lang.String password,
           int payType,
           int portLimit,
           java.lang.String userAccount,
           java.lang.String userAddress,
           java.lang.String userEmail,
           java.lang.String userTel) {
           this.bankCard = bankCard;
           this.groupName = groupName;
           this.idNumber = idNumber;
           this.notes = notes;
           this.password = password;
           this.payType = payType;
           this.portLimit = portLimit;
           this.userAccount = userAccount;
           this.userAddress = userAddress;
           this.userEmail = userEmail;
           this.userTel = userTel;
    }


    /**
     * Gets the bankCard value for this UserAcctInfo.
     * 
     * @return bankCard
     */
    public java.lang.String getBankCard() {
        return bankCard;
    }


    /**
     * Sets the bankCard value for this UserAcctInfo.
     * 
     * @param bankCard
     */
    public void setBankCard(java.lang.String bankCard) {
        this.bankCard = bankCard;
    }


    /**
     * Gets the groupName value for this UserAcctInfo.
     * 
     * @return groupName
     */
    public java.lang.String getGroupName() {
        return groupName;
    }


    /**
     * Sets the groupName value for this UserAcctInfo.
     * 
     * @param groupName
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }


    /**
     * Gets the idNumber value for this UserAcctInfo.
     * 
     * @return idNumber
     */
    public java.lang.String getIdNumber() {
        return idNumber;
    }


    /**
     * Sets the idNumber value for this UserAcctInfo.
     * 
     * @param idNumber
     */
    public void setIdNumber(java.lang.String idNumber) {
        this.idNumber = idNumber;
    }


    /**
     * Gets the notes value for this UserAcctInfo.
     * 
     * @return notes
     */
    public java.lang.String getNotes() {
        return notes;
    }


    /**
     * Sets the notes value for this UserAcctInfo.
     * 
     * @param notes
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }


    /**
     * Gets the password value for this UserAcctInfo.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this UserAcctInfo.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the payType value for this UserAcctInfo.
     * 
     * @return payType
     */
    public int getPayType() {
        return payType;
    }


    /**
     * Sets the payType value for this UserAcctInfo.
     * 
     * @param payType
     */
    public void setPayType(int payType) {
        this.payType = payType;
    }


    /**
     * Gets the portLimit value for this UserAcctInfo.
     * 
     * @return portLimit
     */
    public int getPortLimit() {
        return portLimit;
    }


    /**
     * Sets the portLimit value for this UserAcctInfo.
     * 
     * @param portLimit
     */
    public void setPortLimit(int portLimit) {
        this.portLimit = portLimit;
    }


    /**
     * Gets the userAccount value for this UserAcctInfo.
     * 
     * @return userAccount
     */
    public java.lang.String getUserAccount() {
        return userAccount;
    }


    /**
     * Sets the userAccount value for this UserAcctInfo.
     * 
     * @param userAccount
     */
    public void setUserAccount(java.lang.String userAccount) {
        this.userAccount = userAccount;
    }


    /**
     * Gets the userAddress value for this UserAcctInfo.
     * 
     * @return userAddress
     */
    public java.lang.String getUserAddress() {
        return userAddress;
    }


    /**
     * Sets the userAddress value for this UserAcctInfo.
     * 
     * @param userAddress
     */
    public void setUserAddress(java.lang.String userAddress) {
        this.userAddress = userAddress;
    }


    /**
     * Gets the userEmail value for this UserAcctInfo.
     * 
     * @return userEmail
     */
    public java.lang.String getUserEmail() {
        return userEmail;
    }


    /**
     * Sets the userEmail value for this UserAcctInfo.
     * 
     * @param userEmail
     */
    public void setUserEmail(java.lang.String userEmail) {
        this.userEmail = userEmail;
    }


    /**
     * Gets the userTel value for this UserAcctInfo.
     * 
     * @return userTel
     */
    public java.lang.String getUserTel() {
        return userTel;
    }


    /**
     * Sets the userTel value for this UserAcctInfo.
     * 
     * @param userTel
     */
    public void setUserTel(java.lang.String userTel) {
        this.userTel = userTel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserAcctInfo)) return false;
        UserAcctInfo other = (UserAcctInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bankCard==null && other.getBankCard()==null) || 
             (this.bankCard!=null &&
              this.bankCard.equals(other.getBankCard()))) &&
            ((this.groupName==null && other.getGroupName()==null) || 
             (this.groupName!=null &&
              this.groupName.equals(other.getGroupName()))) &&
            ((this.idNumber==null && other.getIdNumber()==null) || 
             (this.idNumber!=null &&
              this.idNumber.equals(other.getIdNumber()))) &&
            ((this.notes==null && other.getNotes()==null) || 
             (this.notes!=null &&
              this.notes.equals(other.getNotes()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            this.payType == other.getPayType() &&
            this.portLimit == other.getPortLimit() &&
            ((this.userAccount==null && other.getUserAccount()==null) || 
             (this.userAccount!=null &&
              this.userAccount.equals(other.getUserAccount()))) &&
            ((this.userAddress==null && other.getUserAddress()==null) || 
             (this.userAddress!=null &&
              this.userAddress.equals(other.getUserAddress()))) &&
            ((this.userEmail==null && other.getUserEmail()==null) || 
             (this.userEmail!=null &&
              this.userEmail.equals(other.getUserEmail()))) &&
            ((this.userTel==null && other.getUserTel()==null) || 
             (this.userTel!=null &&
              this.userTel.equals(other.getUserTel())));
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
        if (getBankCard() != null) {
            _hashCode += getBankCard().hashCode();
        }
        if (getGroupName() != null) {
            _hashCode += getGroupName().hashCode();
        }
        if (getIdNumber() != null) {
            _hashCode += getIdNumber().hashCode();
        }
        if (getNotes() != null) {
            _hashCode += getNotes().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        _hashCode += getPayType();
        _hashCode += getPortLimit();
        if (getUserAccount() != null) {
            _hashCode += getUserAccount().hashCode();
        }
        if (getUserAddress() != null) {
            _hashCode += getUserAddress().hashCode();
        }
        if (getUserEmail() != null) {
            _hashCode += getUserEmail().hashCode();
        }
        if (getUserTel() != null) {
            _hashCode += getUserTel().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserAcctInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "userAcctInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankCard");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bankCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "groupName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "notes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("portLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "portLimit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userTel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userTel"));
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
