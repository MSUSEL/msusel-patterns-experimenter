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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml3.v3_2.gts;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.gco.GCO;
import org.opengis.feature.type.Schema;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.isotc211.org/2005/gts schema.
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public final class GTS extends GML.DelegatingXSD {

    /** singleton instance */
    private static final GTS instance = new GTS();
    
    /**
     * Returns the singleton instance.
     */
    public static final GTS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GTS() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add( GCO.getInstance() );
    }

    @Override
    protected Schema buildTypeSchema() {
        return new GTSSchema();
    }
    
    /**
     * Returns 'http://www.isotc211.org/2005/gts'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gts.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gts.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.isotc211.org/2005/gts";
    
    /* Type Definitions */
    /** @generated */
    public static final QName TM_PeriodDuration_PropertyType = 
        new QName("http://www.isotc211.org/2005/gts","TM_PeriodDuration_PropertyType");
    /** @generated */
    public static final QName TM_Primitive_PropertyType = 
        new QName("http://www.isotc211.org/2005/gts","TM_Primitive_PropertyType");

    /* Elements */
    /** @generated */
    public static final QName TM_PeriodDuration = 
        new QName("http://www.isotc211.org/2005/gts","TM_PeriodDuration");

    /* Attributes */

}
    
