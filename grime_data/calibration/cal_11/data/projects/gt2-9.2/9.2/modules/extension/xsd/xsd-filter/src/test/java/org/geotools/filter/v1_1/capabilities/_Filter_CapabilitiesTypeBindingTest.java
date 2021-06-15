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
import org.opengis.filter.capability.FilterCapabilities;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:_Filter_Capabilities.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="_Filter_Capabilities"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="Spatial_Capabilities" type="ogc:Spatial_CapabilitiesType"/&gt;
 *          &lt;xsd:element name="Scalar_Capabilities" type="ogc:Scalar_CapabilitiesType"/&gt;
 *          &lt;xsd:element name="Id_Capabilities" type="ogc:Id_CapabilitiesType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
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
public class _Filter_CapabilitiesTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(FilterCapabilities.class, binding(OGC._Filter_Capabilities).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC._Filter_Capabilities).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.capabilities(document, document);

        FilterCapabilities caps = (FilterCapabilities) parse();

        assertEquals(FilterCapabilities.VERSION_110, caps.getVersion());
        assertNotNull(caps.getScalarCapabilities());
        assertNotNull(caps.getSpatialCapabilities());
        assertNotNull(caps.getIdCapabilities());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.capabilities(), OGC.Filter_Capabilities);

        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Spatial_Capabilities"));
        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Scalar_Capabilities"));
        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Id_Capabilities"));
    }
}
