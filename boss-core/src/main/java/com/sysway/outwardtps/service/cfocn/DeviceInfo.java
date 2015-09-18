/**
 * DeviceInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package com.sysway.outwardtps.service.cfocn;


/**
 *  DeviceInfo bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class DeviceInfo implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = deviceInfo
       Namespace URI = http://cfocn.service.outwardtps.sysway.com/
       Namespace Prefix = ns1
     */

    /**
     * field for DeviceName
     */
    protected java.lang.String localDeviceName;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDeviceNameTracker = false;

    /**
     * field for DeviceSN
     */
    protected java.lang.String localDeviceSN;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDeviceSNTracker = false;

    /**
     * field for DeviceSpecCode
     */
    protected java.lang.String localDeviceSpecCode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localDeviceSpecCodeTracker = false;

    /**
     * field for IsFCPort
     */
    protected boolean localIsFCPort;

    /**
     * field for OCCSerialCode
     */
    protected java.lang.String localOCCSerialCode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOCCSerialCodeTracker = false;

    /**
     * field for OriginalDeviceName
     */
    protected java.lang.String localOriginalDeviceName;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOriginalDeviceNameTracker = false;

    /**
     * field for OriginalDeviceSN
     */
    protected java.lang.String localOriginalDeviceSN;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOriginalDeviceSNTracker = false;

    /**
     * field for OriginalDeviceSpecCode
     */
    protected java.lang.String localOriginalDeviceSpecCode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localOriginalDeviceSpecCodeTracker = false;

    /**
     * field for POSSerialCode
     */
    protected java.lang.String localPOSSerialCode;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPOSSerialCodeTracker = false;

    public boolean isDeviceNameSpecified() {
        return localDeviceNameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDeviceName() {
        return localDeviceName;
    }

    /**
     * Auto generated setter method
     * @param param DeviceName
     */
    public void setDeviceName(java.lang.String param) {
        localDeviceNameTracker = param != null;

        this.localDeviceName = param;
    }

    public boolean isDeviceSNSpecified() {
        return localDeviceSNTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDeviceSN() {
        return localDeviceSN;
    }

    /**
     * Auto generated setter method
     * @param param DeviceSN
     */
    public void setDeviceSN(java.lang.String param) {
        localDeviceSNTracker = param != null;

        this.localDeviceSN = param;
    }

    public boolean isDeviceSpecCodeSpecified() {
        return localDeviceSpecCodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDeviceSpecCode() {
        return localDeviceSpecCode;
    }

    /**
     * Auto generated setter method
     * @param param DeviceSpecCode
     */
    public void setDeviceSpecCode(java.lang.String param) {
        localDeviceSpecCodeTracker = param != null;

        this.localDeviceSpecCode = param;
    }

    /**
     * Auto generated getter method
     * @return boolean
     */
    public boolean getIsFCPort() {
        return localIsFCPort;
    }

    /**
     * Auto generated setter method
     * @param param IsFCPort
     */
    public void setIsFCPort(boolean param) {
        this.localIsFCPort = param;
    }

    public boolean isOCCSerialCodeSpecified() {
        return localOCCSerialCodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOCCSerialCode() {
        return localOCCSerialCode;
    }

    /**
     * Auto generated setter method
     * @param param OCCSerialCode
     */
    public void setOCCSerialCode(java.lang.String param) {
        localOCCSerialCodeTracker = param != null;

        this.localOCCSerialCode = param;
    }

    public boolean isOriginalDeviceNameSpecified() {
        return localOriginalDeviceNameTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOriginalDeviceName() {
        return localOriginalDeviceName;
    }

    /**
     * Auto generated setter method
     * @param param OriginalDeviceName
     */
    public void setOriginalDeviceName(java.lang.String param) {
        localOriginalDeviceNameTracker = param != null;

        this.localOriginalDeviceName = param;
    }

    public boolean isOriginalDeviceSNSpecified() {
        return localOriginalDeviceSNTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOriginalDeviceSN() {
        return localOriginalDeviceSN;
    }

    /**
     * Auto generated setter method
     * @param param OriginalDeviceSN
     */
    public void setOriginalDeviceSN(java.lang.String param) {
        localOriginalDeviceSNTracker = param != null;

        this.localOriginalDeviceSN = param;
    }

    public boolean isOriginalDeviceSpecCodeSpecified() {
        return localOriginalDeviceSpecCodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getOriginalDeviceSpecCode() {
        return localOriginalDeviceSpecCode;
    }

    /**
     * Auto generated setter method
     * @param param OriginalDeviceSpecCode
     */
    public void setOriginalDeviceSpecCode(java.lang.String param) {
        localOriginalDeviceSpecCodeTracker = param != null;

        this.localOriginalDeviceSpecCode = param;
    }

    public boolean isPOSSerialCodeSpecified() {
        return localPOSSerialCodeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getPOSSerialCode() {
        return localPOSSerialCode;
    }

    /**
     * Auto generated setter method
     * @param param POSSerialCode
     */
    public void setPOSSerialCode(java.lang.String param) {
        localPOSSerialCodeTracker = param != null;

        this.localPOSSerialCode = param;
    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
        final javax.xml.namespace.QName parentQName,
        final org.apache.axiom.om.OMFactory factory)
        throws org.apache.axis2.databinding.ADBException {
        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,
                parentQName);

        return factory.createOMElement(dataSource, parentQName);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter, boolean serializeType)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "http://cfocn.service.outwardtps.sysway.com/");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":deviceInfo", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "deviceInfo", xmlWriter);
            }
        }

        if (localDeviceNameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "deviceName", xmlWriter);

            if (localDeviceName == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "deviceName cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDeviceName);
            }

            xmlWriter.writeEndElement();
        }

        if (localDeviceSNTracker) {
            namespace = "";
            writeStartElement(null, namespace, "deviceSN", xmlWriter);

            if (localDeviceSN == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "deviceSN cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDeviceSN);
            }

            xmlWriter.writeEndElement();
        }

        if (localDeviceSpecCodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "deviceSpecCode", xmlWriter);

            if (localDeviceSpecCode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "deviceSpecCode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localDeviceSpecCode);
            }

            xmlWriter.writeEndElement();
        }

        namespace = "";
        writeStartElement(null, namespace, "isFCPort", xmlWriter);

        if (false) {
            throw new org.apache.axis2.databinding.ADBException(
                "isFCPort cannot be null!!");
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localIsFCPort));
        }

        xmlWriter.writeEndElement();

        if (localOCCSerialCodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "OCCSerialCode", xmlWriter);

            if (localOCCSerialCode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "OCCSerialCode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOCCSerialCode);
            }

            xmlWriter.writeEndElement();
        }

        if (localOriginalDeviceNameTracker) {
            namespace = "";
            writeStartElement(null, namespace, "originalDeviceName", xmlWriter);

            if (localOriginalDeviceName == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "originalDeviceName cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOriginalDeviceName);
            }

            xmlWriter.writeEndElement();
        }

        if (localOriginalDeviceSNTracker) {
            namespace = "";
            writeStartElement(null, namespace, "originalDeviceSN", xmlWriter);

            if (localOriginalDeviceSN == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "originalDeviceSN cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOriginalDeviceSN);
            }

            xmlWriter.writeEndElement();
        }

        if (localOriginalDeviceSpecCodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "originalDeviceSpecCode",
                xmlWriter);

            if (localOriginalDeviceSpecCode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "originalDeviceSpecCode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localOriginalDeviceSpecCode);
            }

            xmlWriter.writeEndElement();
        }

        if (localPOSSerialCodeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "POSSerialCode", xmlWriter);

            if (localPOSSerialCode == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "POSSerialCode cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPOSSerialCode);
            }

            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://cfocn.service.outwardtps.sysway.com/")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix,
        java.lang.String namespace, java.lang.String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix,
        java.lang.String namespace, java.lang.String attName,
        java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace,
        java.lang.String attName, java.lang.String attValue,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace,
        java.lang.String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        java.lang.String attributeValue;

        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     *  method to handle Qnames
     */
    private void writeQName(javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);

            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" +
                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            }
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }

                namespaceURI = qnames[i].getNamespaceURI();

                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);

                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                     .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            qnames[i]));
                }
            }

            xmlWriter.writeCharacters(stringToWrite.toString());
        }
    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);

                if ((uri == null) || (uri.length() == 0)) {
                    break;
                }

                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     *
     */
    public javax.xml.stream.XMLStreamReader getPullParser(
        javax.xml.namespace.QName qName)
        throws org.apache.axis2.databinding.ADBException {
        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        if (localDeviceNameTracker) {
            elementList.add(new javax.xml.namespace.QName("", "deviceName"));

            if (localDeviceName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDeviceName));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "deviceName cannot be null!!");
            }
        }

        if (localDeviceSNTracker) {
            elementList.add(new javax.xml.namespace.QName("", "deviceSN"));

            if (localDeviceSN != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDeviceSN));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "deviceSN cannot be null!!");
            }
        }

        if (localDeviceSpecCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "deviceSpecCode"));

            if (localDeviceSpecCode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localDeviceSpecCode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "deviceSpecCode cannot be null!!");
            }
        }

        elementList.add(new javax.xml.namespace.QName("", "isFCPort"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                localIsFCPort));

        if (localOCCSerialCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "OCCSerialCode"));

            if (localOCCSerialCode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOCCSerialCode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "OCCSerialCode cannot be null!!");
            }
        }

        if (localOriginalDeviceNameTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "originalDeviceName"));

            if (localOriginalDeviceName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOriginalDeviceName));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "originalDeviceName cannot be null!!");
            }
        }

        if (localOriginalDeviceSNTracker) {
            elementList.add(new javax.xml.namespace.QName("", "originalDeviceSN"));

            if (localOriginalDeviceSN != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOriginalDeviceSN));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "originalDeviceSN cannot be null!!");
            }
        }

        if (localOriginalDeviceSpecCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "originalDeviceSpecCode"));

            if (localOriginalDeviceSpecCode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localOriginalDeviceSpecCode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "originalDeviceSpecCode cannot be null!!");
            }
        }

        if (localPOSSerialCodeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "POSSerialCode"));

            if (localPOSSerialCode != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPOSSerialCode));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "POSSerialCode cannot be null!!");
            }
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName,
            elementList.toArray(), attribList.toArray());
    }

    /**
     *  Factory class that keeps the parse method
     */
    public static class Factory {
        /**
         * static method to create the object
         * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
         *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         *                If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static DeviceInfo parse(javax.xml.stream.XMLStreamReader reader)
            throws java.lang.Exception {
            DeviceInfo object = new DeviceInfo();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"deviceInfo".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (DeviceInfo) com.sysway.outwardtps.service.cfocn.ExtensionMapper.getTypeObject(nsUri,
                                type, reader);
                        }
                    }
                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "deviceName").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "deviceName" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDeviceName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "deviceSN").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "deviceSN" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDeviceSN(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "deviceSpecCode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "deviceSpecCode" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDeviceSpecCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "isFCPort").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "isFCPort" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIsFCPort(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "OCCSerialCode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "OCCSerialCode" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOCCSerialCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "originalDeviceName").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "originalDeviceName" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOriginalDeviceName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "originalDeviceSN").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "originalDeviceSN" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOriginalDeviceSN(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "originalDeviceSpecCode").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "originalDeviceSpecCode" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOriginalDeviceSpecCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "POSSerialCode").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "POSSerialCode" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPOSSerialCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }
    } //end of factory class
}
