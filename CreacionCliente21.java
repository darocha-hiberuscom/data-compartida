
package com.pichincha.sp.generated;

import java.io.Serializable;
import jakarta.annotation.Generated;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for anonymous complex type&lt;/p&gt;.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="headerIn" type="{http://bpichincha.com/servicios}GenericHeaderIn"/&gt;
 *         &lt;element name="bodyIn" type="{http://bpichincha.com/servicios}BodyInCreacionCliente21"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "headerIn",
    "bodyIn"
})
@XmlRootElement(name = "CreacionCliente21")
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
public class CreacionCliente21
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    private static final long serialVersionUID = 1L;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    protected GenericHeaderIn headerIn;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    protected BodyInCreacionCliente21 bodyIn;

    /**
     * Gets the value of the headerIn property.
     * 
     * @return
     *     possible object is
     *     {@link GenericHeaderIn }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    public GenericHeaderIn getHeaderIn() {
        return headerIn;
    }

    /**
     * Sets the value of the headerIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenericHeaderIn }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    public void setHeaderIn(GenericHeaderIn value) {
        this.headerIn = value;
    }

    /**
     * Gets the value of the bodyIn property.
     * 
     * @return
     *     possible object is
     *     {@link BodyInCreacionCliente21 }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    public BodyInCreacionCliente21 getBodyIn() {
        return bodyIn;
    }

    /**
     * Sets the value of the bodyIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link BodyInCreacionCliente21 }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v4.0.6", date = "2026-06-01T01:02:32-05:00")
    public void setBodyIn(BodyInCreacionCliente21 value) {
        this.bodyIn = value;
    }

}
