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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;
import org.geotools.xs.bindings.XSDateBinding;
import org.geotools.xs.bindings.XSDateTimeBinding;
import org.opengis.temporal.Position;

/**
 * Binding object for the type http://www.opengis.net/gml:TimePositionUnion.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;simpleType name="TimePositionUnion"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation xml:lang="en"&gt;The ISO 19108:2002 hierarchy of subtypes for temporal position are collapsed 
 *        by defining a union of XML Schema simple types for indicating temporal position relative 
 *        to a specific reference system. 
 *        
 *        Dates and dateTime may be indicated with varying degrees of precision.  
 *        dateTime by itself does not allow right-truncation, except for fractions of seconds. 
 *        When used with non-Gregorian calendars based on years, months, days, 
 *        the same lexical representation should still be used, with leading zeros added if the 
 *        year value would otherwise have fewer than four digits.  
 *        
 *        An ordinal position may be referenced via URI identifying the definition of an ordinal era.  
 *        
 *        A time coordinate value is indicated as a decimal (e.g. UNIX time, GPS calendar).&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;union memberTypes="gml:CalDate time dateTime anyURI decimal"/&gt;
 *  &lt;/simpleType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class TimePositionUnionBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.TimePositionUnion;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Position.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        //JD: for the moment we will just handle the easy ones of date and datetime
        Date date = null;
        try {
            Timestamp c = new XSDateTimeBinding().parse(instance, value);
            if (c != null) {
                date = new Date(c.getTime());
            }
        }
        catch(Exception e) {}

        if (date == null) {
            try {
                date = (Date) new XSDateBinding().parse(instance, value); 
            }
            catch(Exception e) {
                e.printStackTrace();
            };
        }
        
        if (date == null) {
            throw new IllegalArgumentException("Unable to parse " + value);
        }

        return new DefaultPosition(date);
    }

}