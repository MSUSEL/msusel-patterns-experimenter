/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.geotools.csw;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://purl.org/dc/elements/1.1/ schema.
 *
 * @generated
 */
public final class DC extends XSD {

    /** singleton instance */
    private static final DC instance = new DC();
    
    /**
     * Returns the singleton instance.
     */
    public static final DC getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private DC() {
    }
    
    protected void addDependencies(Set dependencies) {
       //TODO: add dependencies here
    }
    
    /**
     * Returns 'http://purl.org/dc/elements/1.1/'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'rec-dcmes.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("rec-dcmes.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://purl.org/dc/elements/1.1/";
    
    /* Type Definitions */
    /** @generated */
    public static final QName elementContainer = 
        new QName("http://purl.org/dc/elements/1.1/","elementContainer");
    /** @generated */
    public static final QName SimpleLiteral = 
        new QName("http://purl.org/dc/elements/1.1/","SimpleLiteral");

    /* Elements */
    /** @generated */
    public static final QName contributor = 
        new QName("http://purl.org/dc/elements/1.1/","contributor");
    /** @generated */
    public static final QName coverage = 
        new QName("http://purl.org/dc/elements/1.1/","coverage");
    /** @generated */
    public static final QName creator = 
        new QName("http://purl.org/dc/elements/1.1/","creator");
    /** @generated */
    public static final QName date = 
        new QName("http://purl.org/dc/elements/1.1/","date");
    /** @generated */
    public static final QName DCelement = 
        new QName("http://purl.org/dc/elements/1.1/","DC-element");
    /** @generated */
    public static final QName description = 
        new QName("http://purl.org/dc/elements/1.1/","description");
    /** @generated */
    public static final QName format = 
        new QName("http://purl.org/dc/elements/1.1/","format");
    /** @generated */
    public static final QName identifier = 
        new QName("http://purl.org/dc/elements/1.1/","identifier");
    /** @generated */
    public static final QName language = 
        new QName("http://purl.org/dc/elements/1.1/","language");
    /** @generated */
    public static final QName publisher = 
        new QName("http://purl.org/dc/elements/1.1/","publisher");
    /** @generated */
    public static final QName relation = 
        new QName("http://purl.org/dc/elements/1.1/","relation");
    /** @generated */
    public static final QName rights = 
        new QName("http://purl.org/dc/elements/1.1/","rights");
    /** @generated */
    public static final QName source = 
        new QName("http://purl.org/dc/elements/1.1/","source");
    /** @generated */
    public static final QName subject = 
        new QName("http://purl.org/dc/elements/1.1/","subject");
    /** @generated */
    public static final QName title = 
        new QName("http://purl.org/dc/elements/1.1/","title");
    /** @generated */
    public static final QName type = 
        new QName("http://purl.org/dc/elements/1.1/","type");

    /* Attributes */

}
    