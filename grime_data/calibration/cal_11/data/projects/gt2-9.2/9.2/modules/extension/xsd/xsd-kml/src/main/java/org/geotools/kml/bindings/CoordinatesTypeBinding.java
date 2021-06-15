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
package org.geotools.kml.bindings;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;

import org.geotools.kml.KML;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:CoordinatesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;simpleType name="CoordinatesType"&gt;
 *      &lt;list itemType="string"/&gt;
 *  &lt;/simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class CoordinatesTypeBinding extends AbstractSimpleBinding {
    CoordinateSequenceFactory csFactory;
    
    public CoordinatesTypeBinding( CoordinateSequenceFactory csFactory ) {
        this.csFactory = csFactory;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return KML.CoordinatesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        //return Coordinate[].class;
        return CoordinateSequence.class;
    }
    
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        List list = (List) value;
        Coordinate[] coordinates = new Coordinate[list.size()];
        int i = 0;

        for (Iterator l = list.iterator(); l.hasNext(); i++) {
            StringTokenizer st = new StringTokenizer((String) l.next(), ",");
            Coordinate c = new Coordinate();

            c.x = Double.parseDouble(st.nextToken());
            c.y = Double.parseDouble(st.nextToken());

            if (st.hasMoreTokens()) {
                c.z = Double.parseDouble(st.nextToken());
            }

            coordinates[i] = c;
        }

        return csFactory.create( coordinates );
    }
    
    public String encode(Object object, String value) throws Exception {
        StringBuffer sb = new StringBuffer();
        CoordinateSequence cs = (CoordinateSequence) object;
        for ( int i = 0; i < cs.size(); i++ ) {
            Coordinate c = cs.getCoordinate(i);
            sb.append( c.x ).append(",").append( c.y );
            if ( cs.getDimension() == 3 && !Double.isNaN( c.z ) ) {
                sb.append(",").append( c.z );
            }
            sb.append( " " );
        }
        sb.setLength(sb.length()-1);
    
        return sb.toString();
    }
}
