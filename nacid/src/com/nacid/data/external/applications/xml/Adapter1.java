//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-34 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.20 at 02:39:17 PM EEST 
//


package com.nacid.data.external.applications.xml;

import java.sql.Timestamp;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Timestamp>
{


    public Timestamp unmarshal(String value) {
        return (com.nacid.data.DataConverter.parseTimestamp(value));
    }

    public String marshal(Timestamp value) {
        return (com.nacid.data.DataConverter.formatTimestamp(value));
    }

}
