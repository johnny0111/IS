//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.03.14 at 11:47:04 PM WET 
//


package org.netbeans.xml.schema.updateschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for tPosition complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="tPosition"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="xx" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="yy" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPosition", propOrder = {
    "xx",
    "yy"
})
public class TPosition {

    protected int xx;
    protected int yy;

    /**
     * Gets the value of the xx property.
     * 
     */
    public int getXx() {
        return xx;
    }

    /**
     * Sets the value of the xx property.
     * 
     */
    public void setXx(int value) {
        this.xx = value;
    }

    /**
     * Gets the value of the yy property.
     * 
     */
    public int getYy() {
        return yy;
    }

    /**
     * Sets the value of the yy property.
     * 
     */
    public void setYy(int value) {
        this.yy = value;
    }

}