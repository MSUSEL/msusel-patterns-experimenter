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
package org.geotools.filter.v1_0.capabilities;

import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialOperators;
import org.geotools.xml.Binding;


/**
 * 
 *
 * @source $URL$
 */
public class Spatial_OperatorsTypeBindingTest extends FilterCapabilitiesTestSupport {
    public void testType() {
        assertEquals(SpatialOperators.class, binding(OGC.Spatial_OperatorsType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Spatial_OperatorsType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.spatial(document, document);

        SpatialOperators spatial = (SpatialOperators) parse(OGC.Spatial_OperatorsType);

        assertNotNull(spatial.getOperator("BBOX"));
        assertNotNull(spatial.getOperator("Equals"));
        assertNotNull(spatial.getOperator("Disjoint"));
        assertNotNull(spatial.getOperator("Intersect"));
        assertNotNull(spatial.getOperator("Touches"));
        assertNotNull(spatial.getOperator("Crosses"));
        assertNotNull(spatial.getOperator("Within"));
        assertNotNull(spatial.getOperator("Contains"));
        assertNotNull(spatial.getOperator("Overlaps"));
        assertNotNull(spatial.getOperator("Beyond"));
        assertNotNull(spatial.getOperator("DWithin"));
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.spatial(),
                new QName(OGC.NAMESPACE, "Spatial_Operators"), OGC.Spatial_OperatorsType);

        assertNotNull(getElementByQName(dom, OGC.BBOX));
        assertNotNull(getElementByQName(dom, OGC.Equals));
        assertNotNull(getElementByQName(dom, OGC.Disjoint));
        assertNotNull(getElementByQName(dom, OGC.Intersect));
        assertNotNull(getElementByQName(dom, OGC.Touches));
        assertNotNull(getElementByQName(dom, OGC.Crosses));
        assertNotNull(getElementByQName(dom, OGC.Within));
        assertNotNull(getElementByQName(dom, OGC.Contains));
        assertNotNull(getElementByQName(dom, OGC.Overlaps));
        assertNotNull(getElementByQName(dom, OGC.Beyond));
        assertNotNull(getElementByQName(dom, OGC.DWithin));
    }
}
