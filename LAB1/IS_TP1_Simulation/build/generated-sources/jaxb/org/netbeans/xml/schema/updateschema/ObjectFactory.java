//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.17 at 11:20:45 PM WEST 
//


package org.netbeans.xml.schema.updateschema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.netbeans.xml.schema.updateschema package. 
 * &lt;p&gt;An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MyPlace_QNAME = new QName("http://xml.netbeans.org/schema/updateSchema", "MyPlace");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.netbeans.xml.schema.updateschema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TMyPlace }
     * 
     */
    public TMyPlace createTMyPlace() {
        return new TMyPlace();
    }

    /**
     * Create an instance of {@link TPlace }
     * 
     */
    public TPlace createTPlace() {
        return new TPlace();
    }

    /**
     * Create an instance of {@link TPosition }
     * 
     */
    public TPosition createTPosition() {
        return new TPosition();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TMyPlace }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TMyPlace }{@code >}
     */
    @XmlElementDecl(namespace = "http://xml.netbeans.org/schema/updateSchema", name = "MyPlace")
    public JAXBElement<TMyPlace> createMyPlace(TMyPlace value) {
        return new JAXBElement<TMyPlace>(_MyPlace_QNAME, TMyPlace.class, null, value);
    }

}
