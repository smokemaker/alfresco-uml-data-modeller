//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.09 at 02:53:15 AM MSK 
//


package org.alfresco.model.dictionary._1;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, String>
{


    public String unmarshal(String value) {
        return (ru.neodoc.jaxb.utils.CDataAdapter.parse(value));
    }

    public String marshal(String value) {
        return (ru.neodoc.jaxb.utils.CDataAdapter.print(value));
    }

}
