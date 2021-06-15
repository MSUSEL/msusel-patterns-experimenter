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
package org.geotools.gml3.smil;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xml.SchemaLocator;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.w3.org/2001/SMIL20/ schema.
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public final class SMIL20 extends XSD {
    /**
     * singleton instance
     */
    private static SMIL20 instance = new SMIL20();

    /** @generated */
    public static final String NAMESPACE = "http://www.w3.org/2001/SMIL20/";

    /* Type Definitions */
    /** @generated */
    public static final QName ANIMATECOLORPROTOTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "animateColorPrototype");

    /** @generated */
    public static final QName ANIMATEMOTIONPROTOTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "animateMotionPrototype");

    /** @generated */
    public static final QName ANIMATEPROTOTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "animatePrototype");

    /** @generated */
    public static final QName FILLDEFAULTTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "fillDefaultType");

    /** @generated */
    public static final QName FILLTIMINGATTRSTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "fillTimingAttrsType");

    /** @generated */
    public static final QName NONNEGATIVEDECIMALTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "nonNegativeDecimalType");

    /** @generated */
    public static final QName RESTARTDEFAULTTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "restartDefaultType");

    /** @generated */
    public static final QName RESTARTTIMINGTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "restartTimingType");

    /** @generated */
    public static final QName SETPROTOTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "setPrototype");

    /** @generated */
    public static final QName SYNCBEHAVIORDEFAULTTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "syncBehaviorDefaultType");

    /** @generated */
    public static final QName SYNCBEHAVIORTYPE = new QName("http://www.w3.org/2001/SMIL20/",
            "syncBehaviorType");

    /* Elements */
    /** @generated */
    public static final QName ANIMATE = new QName("http://www.w3.org/2001/SMIL20/", "animate");

    /** @generated */
    public static final QName ANIMATECOLOR = new QName("http://www.w3.org/2001/SMIL20/",
            "animateColor");

    /** @generated */
    public static final QName ANIMATEMOTION = new QName("http://www.w3.org/2001/SMIL20/",
            "animateMotion");

    /** @generated */
    public static final QName SET = new QName("http://www.w3.org/2001/SMIL20/", "set");

    /**
     * private constructor.
     */
    private SMIL20() {
    }

    public static SMIL20 getInstance() {
        return instance;
    }

    protected void addDependencies(Set dependencies) {
        dependencies.add(XMLMOD.getInstance());
        dependencies.add(SMIL20LANG.getInstance());
    }

    /**
     * Returns 'http://www.w3.org/2001/SMIL20/'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'smil20.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("smil20.xsd").toString();
    }

    public SchemaLocator createSchemaLocator() {
        //we explicity return null here because of a circular dependnecy with 
        //gml3 schema... returning null breaks the circle when the schemas are 
        //being built
        return null;
    }
    
    /* Attributes */
}
