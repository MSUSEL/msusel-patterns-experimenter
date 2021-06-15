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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;


/**
 * Binding object for the type http://www.opengis.net/gml:BoxType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="BoxType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         The Box structure defines an extent
 *              using a pair of coordinate tuples.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;element ref="gml:coord" minOccurs="2" maxOccurs="2"/&gt;
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
public class GMLBoxTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.BoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Envelope.class;
    }

    /**
     * <!-- begin-user-doc -->
     * This method returns an object of type
     * @link com.vividsolutions.jts.geom.Envelope.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List coordinates = node.getChildren("coord");

        if (!coordinates.isEmpty() && (coordinates.size() == 2)) {
            Node n1 = (Node) coordinates.get(0);
            Node n2 = (Node) coordinates.get(1);
            Coordinate c1 = (Coordinate) n1.getValue();
            Coordinate c2 = (Coordinate) n2.getValue();

            return new Envelope(c1.x, c2.x, c1.y, c2.y);
        }

        if (!coordinates.isEmpty()) {
            throw new RuntimeException("Envelope can have only two coordinates");
        }

        if (node.getChild("coordinates") != null) {
            CoordinateSequence cs = (CoordinateSequence) node.getChild("coordinates").getValue();

            if (cs.size() != 2) {
                throw new RuntimeException("Envelope can have only two coordinates");
            }

            return new Envelope(cs.getX(0), cs.getX(1), cs.getY(0), cs.getY(1));
        }

        throw new RuntimeException("Could not find coordinates for envelope");
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Envelope e = (Envelope) object;

        if (GML.coord.equals(name)) {
            return new Coordinate[] {
                new Coordinate(e.getMinX(), e.getMinY()), new Coordinate(e.getMaxX(), e.getMaxY())
            };
        } else if("srsName".equals(name.getLocalPart()) && e instanceof ReferencedEnvelope) {
            return GML2EncodingUtils.toURI(((ReferencedEnvelope) e).getCoordinateReferenceSystem());
        }

        return null;
    }
}
