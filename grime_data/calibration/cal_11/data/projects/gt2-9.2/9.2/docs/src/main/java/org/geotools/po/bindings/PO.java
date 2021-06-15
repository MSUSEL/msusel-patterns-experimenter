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
package org.geotools.po.bindings;


import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.geotools.org/po schema.
 *
 * @generated
 */
public final class PO extends XSD {

    /** singleton instance */
    private static final PO instance = new PO();
    
    /**
     * Returns the singleton instance.
     */
    public static final PO getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private PO() {
    }
    
    protected void addDependencies(Set dependencies) {
       //TODO: add dependencies here
    }
    
    /**
     * Returns 'http://www.geotools.org/po'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'po.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("po.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.geotools.org/po";
    
    /* Type Definitions */
    /** @generated */
    public static final QName Items = 
        new QName("http://www.geotools.org/po","Items");
    /** @generated */
    public static final QName PurchaseOrderType = 
        new QName("http://www.geotools.org/po","PurchaseOrderType");
    /** @generated */
    public static final QName SKU = 
        new QName("http://www.geotools.org/po","SKU");
    /** @generated */
    public static final QName USAddress = 
        new QName("http://www.geotools.org/po","USAddress");
    /** @generated */
    public static final QName Items_item = 
        new QName("http://www.geotools.org/po","Items_item");

    /* Elements */
    /** @generated */
    public static final QName comment = 
        new QName("http://www.geotools.org/po","comment");
    /** @generated */
    public static final QName purchaseOrder = 
        new QName("http://www.geotools.org/po","purchaseOrder");

    /* Attributes */

}
    