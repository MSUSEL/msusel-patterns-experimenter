//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.18 at 07:08:06 PM MST 
//


package edu.montana.gsoc.msusel.patterns.collector.impl.pmd;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.montana.gsoc.msusel.patterns.collector.impl.pmd package. 
 * <p>An ObjectFactory allows you to programatically 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.montana.gsoc.msusel.patterns.collector.impl.pmd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Pmd }
     * 
     */
    public Pmd createPmd() {
        return new Pmd();
    }

    /**
     * Create an instance of {@link File }
     * 
     */
    public File createFile() {
        return new File();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link Suppressedviolation }
     * 
     */
    public Suppressedviolation createSuppressedviolation() {
        return new Suppressedviolation();
    }

    /**
     * Create an instance of {@link Configerror }
     * 
     */
    public Configerror createConfigerror() {
        return new Configerror();
    }

    /**
     * Create an instance of {@link Violation }
     * 
     */
    public Violation createViolation() {
        return new Violation();
    }

}
