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
package org.geotools.filter.v1_1.capabilities;

import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.GeometryOperand;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:GeometryOperandType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:simpleType name="GeometryOperandType"&gt;
 *      &lt;xsd:restriction base="xsd:QName"&gt;
 *          &lt;xsd:enumeration value="gml:Envelope"/&gt;
 *          &lt;xsd:enumeration value="gml:Point"/&gt;
 *          &lt;xsd:enumeration value="gml:LineString"/&gt;
 *          &lt;xsd:enumeration value="gml:Polygon"/&gt;
 *          &lt;xsd:enumeration value="gml:ArcByCenterPoint"/&gt;
 *          &lt;xsd:enumeration value="gml:CircleByCenterPoint"/&gt;
 *          &lt;xsd:enumeration value="gml:Arc"/&gt;
 *          &lt;xsd:enumeration value="gml:Circle"/&gt;
 *          &lt;xsd:enumeration value="gml:ArcByBulge"/&gt;
 *          &lt;xsd:enumeration value="gml:Bezier"/&gt;
 *          &lt;xsd:enumeration value="gml:Clothoid"/&gt;
 *          &lt;xsd:enumeration value="gml:CubicSpline"/&gt;
 *          &lt;xsd:enumeration value="gml:Geodesic"/&gt;
 *          &lt;xsd:enumeration value="gml:OffsetCurve"/&gt;
 *          &lt;xsd:enumeration value="gml:Triangle"/&gt;
 *          &lt;xsd:enumeration value="gml:PolyhedralSurface"/&gt;
 *          &lt;xsd:enumeration value="gml:TriangulatedSurface"/&gt;
 *          &lt;xsd:enumeration value="gml:Tin"/&gt;
 *          &lt;xsd:enumeration value="gml:Solid"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class GeometryOperandTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(GeometryOperand.class, binding(OGC.GeometryOperandType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.GeometryOperandType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.geometryOperand(document, document, "Envelope");

        GeometryOperand operand = (GeometryOperand) parse(OGC.GeometryOperandType);

        assertEquals(GeometryOperand.Envelope, operand);
    }

    public void testEncode() throws Exception {
        Document dom = encode(GeometryOperand.Envelope,
                new QName(OGC.NAMESPACE, "GeometryOperand"), OGC.GeometryOperandType);
        assertEquals("gml:Envelope", dom.getDocumentElement().getFirstChild().getNodeValue());
    }
}
