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
package org.geotools.gml2.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;


/**
 * Binding object for the type http://www.opengis.net/gml:LinearRingType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="LinearRingType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         A LinearRing is defined by four or more
 *              coordinate tuples, with          linear interpolation
 *              between them; the first and last coordinates          must
 *              be coincident.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;element ref="gml:coord" minOccurs="4" maxOccurs="unbounded"/&gt;
 *                      &lt;element ref="gml:coordinates"/&gt;
 *                  &lt;/choice&gt;
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
public class GMLLinearRingTypeBinding extends AbstractComplexBinding {
    CoordinateSequenceFactory csFactory;
    GeometryFactory gFactory;

    public GMLLinearRingTypeBinding(CoordinateSequenceFactory csFactory, GeometryFactory gFactory) {
        this.csFactory = csFactory;
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.LinearRingType;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LinearRing.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List coordinates = node.getChildren("coord");

        if (!coordinates.isEmpty() && (coordinates.size() < 4)) {
            throw new RuntimeException("LinearRing must have at least 4 coordinates");
        }

        if (!coordinates.isEmpty()) {
            Node cnode = (Node) coordinates.get(0);
            CoordinateSequence seq = (CoordinateSequence) cnode.getValue();
            int dimension = GMLUtil.getDimension(seq);

            CoordinateSequence lineSeq = csFactory.create(coordinates.size(), dimension);

            for (int i = 0; i < coordinates.size(); i++) {
                cnode = (Node) coordinates.get(i);
                seq = (CoordinateSequence) cnode.getValue();

                for (int j = 0; j < dimension; j++) {
                    lineSeq.setOrdinate(i, j, seq.getOrdinate(0, j));
                }
            }

            return gFactory.createLinearRing(lineSeq);
        }

        if (node.getChild("coordinates") != null) {
            Node cnode = (Node) node.getChild("coordinates");
            CoordinateSequence lineSeq = (CoordinateSequence) cnode.getValue();

            return gFactory.createLinearRing(lineSeq);
        }

        throw new RuntimeException("Could not find coordinates to build linestring");
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        LinearRing linearRing = (LinearRing) object;

        if (GML.coordinates.equals(name)) {
            return linearRing.getCoordinateSequence();
        }

        return null;
    }
}
