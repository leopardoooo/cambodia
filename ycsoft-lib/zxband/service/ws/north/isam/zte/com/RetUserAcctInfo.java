/**
 * RetUserAcctInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package service.ws.north.isam.zte.com;

public class RetUserAcctInfo  implements java.io.Serializable {
    private java.lang.Double balance;

    private java.lang.String bankCard;

    private java.util.Calendar endDate;

    private java.lang.String groupName;

    private java.lang.String idNumber;

    private java.lang.String notes;

    private java.util.Calendar startDate;

    private java.lang.String status;

    private java.lang.String userAccount;

    private java.lang.String userAddress;

    private java.lang.String userEmail;

    private java.lang.String userTel;

    public RetUserAcctInfo() {
    }

    public RetUserAcctInfo(
           java.lang.Double balance,
           java.lang.String bankCard,
           java.util.Calendar endDate,
           java.lang.String groupName,
           java.lang.String idNumber,
           java.lang.String notes,
           java.util.Calendar startDate,
           java.lang.String status,
           java.lang.String userAccount,
           java.lang.String userAddress,
           java.lang.String userEmail,
           java.lang.String userTel) {
           this.balance = balance;
           this.bankCard = bankCard;
           this.endDate = endDate;
           this.groupName = groupName;
           this.idNumber = idNumber;
           this.notes = notes;
           this.startDate = startDate;
           this.status = status;
           this.userAccount = userAccount;
           this.userAddress = userAddress;
           this.userEmail = userEmail;
           this.userTel = userTel;
    }


    /**
     * Gets the balance value for this RetUserAcctInfo.
     * 
     * @return balance
     */
    public java.lang.Double getBalance() {
        return balance;
    }


    /**
     * Sets the balance value for this RetUserAcctInfo.
     * 
     * @param balance
     */
    public void setBalance(java.lang.Double balance) {
        this.balance = balance;
    }


    /**
     * Gets the bankCard value for this RetUserAcctInfo.
     * 
     * @return bankCard
     */
    public java.lang.String getBankCard() {
        return bankCard;
    }


    /**
     * Sets the bankCard value for this RetUserAcctInfo.
     * 
     * @param bankCard
     */
    public void setBankCard(java.lang.String bankCard) {
        this.bankCard = bankCard;
    }


    /**
     * Gets the endDate value for this RetUserAcctInfo.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this RetUserAcctInfo.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the groupName value for this RetUserAcctInfo.
     * 
     * @return groupName
     */
    public java.lang.String getGroupName() {
        return groupName;
    }


    /**
     * Sets the groupName value for this RetUserAcctInfo.
     * 
     * @param groupName
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }


    /**
     * Gets the idNumber value for this RetUserAcctInfo.
     * 
     * @return idNumber
     */
    public java.lang.String getIdNumber() {
        return idNumber;
    }


    /**
     * Sets the idNumber value for this RetUserAcctInfo.
     * 
     * @param idNumber
     */
    public void setIdNumber(java.lang.String idNumber) {
        this.idNumber = idNumber;
    }


    /**
     * Gets the notes value for this RetUserAcctInfo.
     * 
     * @return notes
     */
    public java.lang.String getNotes() {
        return notes;
    }


    /**
     * Sets the notes value for this RetUserAcctInfo.
     * 
     * @param notes
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }


    /**
     * Gets the startDate value for this RetUserAcctInfo.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this RetUserAcctInfo.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the status value for this RetUserAcctInfo.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this RetUserAcctInfo.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the userAccount value for this RetUserAcctInfo.
     * 
     * @return userAccount
     */
    public java.lang.String getUserAccount() {
        return userAccount;
    }


    /**
     * Sets the userAccount value for this RetUserAcctInfo.
     * 
     * @param userAccount
     */
    public void setUserAccount(java.lang.String userAccount) {
        this.userAccount = userAccount;
    }


    /**
     * Gets the userAddress value for this RetUserAcctInfo.
     * 
     * @return userAddress
     */
    public java.lang.String getUserAddress() {
        return userAddress;
    }


    /**
     * Sets the userAddress value for this RetUserAcctInfo.
     * 
     * @param userAddress
     */
    public void setUserAddress(java.lang.String userAddress) {
        this.userAddress = userAddress;
    }


    /**
     * Gets the userEmail value for this RetUserAcctInfo.
     * 
     * @return userEmail
     */
    public java.lang.String getUserEmail() {
        return userEmail;
    }


    /**
     * Sets the userEmail value for this RetUserAcctInfo.
     * 
     * @param userEmail
     */
    public void setUserEmail(java.lang.String userEmail) {
        this.userEmail = userEmail;
    }


    /**
     * Gets the userTel value for this RetUserAcctInfo.
     * 
     * @return userTel
     */
    public java.lang.String getUserTel() {
        return userTel;
    }


    /**
     * Sets the userTel value for this RetUserAcctInfo.
     * 
     * @param userTel
     */
    public void setUserTel(java.lang.String userTel) {
        this.userTel = userTel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetUserAcctInfo)) return false;
        RetUserAcctInfo other = (RetUserAcctInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.balance==null && other.getBalance()==null) || 
             (this.balance!=null &&
              this.balance.equals(other.getBalance()))) &&
            ((this.bankCard==null && other.getBankCard()==null) || 
             (this.bankCard!=null &&
              this.bankCard.equals(other.getBankCard()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.groupName==null && other.getGroupName()==null) || 
             (this.groupName!=null &&
              this.groupName.equals(other.getGroupName()))) &&
            ((this.idNumber==null && other.getIdNumber()==null) || 
             (this.idNumber!=null &&
              this.idNumber.equals(other.getIdNumber()))) &&
            ((this.notes==null && other.getNotes()==null) || 
             (this.notes!=null &&
              this.notes.equals(other.getNotes()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
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
        if (getBalance() != null) {
            _hashCode += getBalance().hashCode();
        }
        if (getBankCard() != null) {
            _hashCode += getBankCard().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
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
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(RetUserAcctInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://com.zte.isam.north.ws.service", "retUserAcctInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "balance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankCard");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bankCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
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
