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

import org.geotools.gml2.GML;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;


/**
 * 
 *
 * @source $URL$
 */
public class GMLMultiPointTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance mp;
    ElementInstance point1;
    ElementInstance point2;
    GeometryFactory gf;

    protected void setUp() throws Exception {
        super.setUp();

        point1 = createElement(GML.NAMESPACE, "myPoint", GML.POINTMEMBERTYPE, null);
        point2 = createElement(GML.NAMESPACE, "myPoint", GML.POINTMEMBERTYPE, null);
        mp = createElement(GML.NAMESPACE, "myMultiPoint", GML.MULTIPOINTTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryCollectionTypeBinding.class);
        container.registerComponentImplementation(GMLMultiPointTypeBinding.class);
    }

    public void test() throws Exception {
        Node node = createNode(mp, new ElementInstance[] { point1, point2 },
                new Object[] {
                    new GeometryFactory().createPoint(new Coordinate(0, 0)),
                    new GeometryFactory().createPoint(new Coordinate(1, 1))
                }, null, null);

        GMLGeometryCollectionTypeBinding s1 = (GMLGeometryCollectionTypeBinding) container
            .getComponentInstanceOfType(GMLGeometryCollectionTypeBinding.class);
        GMLMultiPointTypeBinding s2 = (GMLMultiPointTypeBinding) container
            .getComponentInstanceOfType(GMLMultiPointTypeBinding.class);

        MultiPoint mpoint = (MultiPoint) s2.parse(mp, node, s1.parse(mp, node, null));

        assertNotNull(mpoint);
        assertEquals(mpoint.getNumGeometries(), 2);

        assertEquals(((Point) mpoint.getGeometryN(0)).getX(), 0d, 0d);
        assertEquals(((Point) mpoint.getGeometryN(0)).getY(), 0d, 0d);
        assertEquals(((Point) mpoint.getGeometryN(1)).getX(), 1d, 0d);
        assertEquals(((Point) mpoint.getGeometryN(1)).getY(), 1d, 0d);
    }
}
