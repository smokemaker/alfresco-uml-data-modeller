//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.28 at 06:00:48 PM MSK 
//


package ru.neodoc.modeller.extensions;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for model complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="model"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://neodoc.ru/modeller/extensions}modelObjectsContainer"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://neodoc.ru/modeller/extensions}extensionsData"/&gt;
 *         &lt;element ref="{http://neodoc.ru/modeller/extensions}customData"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://neodoc.ru/modeller/extensions}named"/&gt;
 *       &lt;attGroup ref="{http://neodoc.ru/modeller/extensions}umlRef"/&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "model", propOrder = {
    "extensionsData",
    "customData"
})
public class Model
    extends ModelObjectsContainer
    implements Named, UmlRef
{

    @XmlElement(required = true)
    protected ExtensionsData extensionsData;
    @XmlElement(required = true)
    protected CustomData customData;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "umlId")
    protected String umlId;
    @XmlAttribute(name = "umlName")
    protected String umlName;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the extensionsData property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsData }
     *     
     */
    public ExtensionsData getExtensionsData() {
        return extensionsData;
    }

    /**
     * Sets the value of the extensionsData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsData }
     *     
     */
    public void setExtensionsData(ExtensionsData value) {
        this.extensionsData = value;
    }

    /**
     * Gets the value of the customData property.
     * 
     * @return
     *     possible object is
     *     {@link CustomData }
     *     
     */
    public CustomData getCustomData() {
        return customData;
    }

    /**
     * Sets the value of the customData property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomData }
     *     
     */
    public void setCustomData(CustomData value) {
        this.customData = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the umlId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUmlId() {
        return umlId;
    }

    /**
     * Sets the value of the umlId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUmlId(String value) {
        this.umlId = value;
    }

    /**
     * Gets the value of the umlName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUmlName() {
        return umlName;
    }

    /**
     * Sets the value of the umlName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUmlName(String value) {
        this.umlName = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
