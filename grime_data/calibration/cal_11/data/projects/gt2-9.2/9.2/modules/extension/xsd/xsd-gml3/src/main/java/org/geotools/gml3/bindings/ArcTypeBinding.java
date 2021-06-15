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

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.Circle;


/**
 *  &lt;complexType name="ArcType">
 *        &lt;annotation>
 *            &lt;documentation>An Arc is an arc string with only one arc unit, i.e. three control points.&lt;/documentation>
 *        &lt;/annotation>
 *        &lt;complexContent>
 *            &lt;restriction base="gml:ArcStringType">
 *                &lt;sequence>
 *                    &lt;choice>
 *                        &lt;annotation>
 *                            &lt;documentation>GML supports two different ways to specify the control points of a curve segment.
 *1. A sequence of "pos" (DirectPositionType) or "pointProperty" (PointPropertyType) elements. "pos" elements are control points that are only part of this curve segment, "pointProperty" elements contain a point that may be referenced from other geometry elements or reference another point defined outside of this curve segment (reuse of existing points).
 *2. The "posList" element allows for a compact way to specifiy the coordinates of the control points, if all control points are in the same coordinate reference systems and belong to this curve segment only. The number of direct positions in the list must be three.&lt;/documentation>
 *                        &lt;/annotation>
 *                        &lt;choice minOccurs="3" maxOccurs="3">
 *                            &lt;element ref="gml:pos"/>
 *                            &lt;element ref="gml:pointProperty"/>
 *                            &lt;element ref="gml:pointRep">
 *                                &lt;annotation>
 *                                    &lt;documentation>Deprecated with GML version 3.1.0. Use "pointProperty" instead. Included for backwards compatibility with GML 3.0.0.&lt;/documentation>
 *                                &lt;/annotation>
 *                            &lt;/element>
 *                        &lt;/choice>
 *                        &lt;element ref="gml:posList"/>
 *                        &lt;element ref="gml:coordinates">
 *                            &lt;annotation>
 *                                &lt;documentation>Deprecated with GML version 3.1.0. Use "posList" instead.&lt;/documentation>
 *                            &lt;/annotation>
 *                        &lt;/element>
 *                    &lt;/choice>
 *                &lt;/sequence>
 *                &lt;attribute name="numArc" type="integer" use="optional" fixed="1">
 *                    &lt;annotation>
 *                        &lt;documentation>An arc is an arc string consiting of a single arc, the attribute is fixed to "1".&lt;/documentation>
 *                    &lt;/annotation>
 *                &lt;/attribute>
 *            &lt;/restriction>
 *        &lt;/complexContent>
 *    &lt;/complexType>
 *
 * @author Erik van de Pol. B3Partners BV.
 *
 *
 * @source $URL$
 */
public class ArcTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;
    CoordinateSequenceFactory csFactory;
    ArcParameters arcParameters;

    public ArcTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory, ArcParameters arcParameters) {
        this.gFactory = gFactory;
        this.csFactory = csFactory;
        this.arcParameters = arcParameters;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.ArcType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LineString.class;
    }

    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {

        //check that three points were specified
        LineString arcLineString = GML3ParsingUtils.lineString(node, gFactory, csFactory);
        if (arcLineString.getCoordinates().length != 3) {
            throw new RuntimeException("Number of coordinates in an arc must be exactly 3, " 
                    + arcLineString.getCoordinates().length + " were specified: " + arcLineString);
        }

        return value;
    }
}
