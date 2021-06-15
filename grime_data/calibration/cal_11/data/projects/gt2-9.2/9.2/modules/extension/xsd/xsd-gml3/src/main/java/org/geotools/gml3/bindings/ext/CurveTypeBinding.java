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
package org.geotools.gml3.bindings.ext;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.LineStringTypeBinding;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;


/**
 * Binding object for the type http://www.opengis.net/gml:CurveType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="CurveType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Curve is a 1-dimensional primitive. Curves are continuous, connected, and have a measurable length in terms of the coordinate system.
 *                                  A curve is composed of one or more curve segments. Each curve segment within a curve may be defined using a different interpolation method. The curve segments are connected to one another, with the end point of each segment except the last being the start point of the next segment in the segment list.
 *                                  The orientation of the curve is positive.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractCurveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:segments"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;This element encapsulates the segments of the curve.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
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
public class CurveTypeBinding extends org.geotools.gml3.bindings.CurveTypeBinding
    implements Comparable {
    
    public CurveTypeBinding(GeometryFactory gf) {
        super(gf);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML.segments.equals(name)) {
            //Curve curve = (Curve) object;
            MultiLineString curve = (MultiLineString) object;
            LineString[] segments = new LineString[curve.getNumGeometries()];

            for (int i = 0; i < segments.length; i++) {
                segments[i] = (LineString) curve.getGeometryN(i);
            }

            return segments;
        }

        return null;
    }

    public int compareTo(Object o) {
        if (o instanceof LineStringTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
