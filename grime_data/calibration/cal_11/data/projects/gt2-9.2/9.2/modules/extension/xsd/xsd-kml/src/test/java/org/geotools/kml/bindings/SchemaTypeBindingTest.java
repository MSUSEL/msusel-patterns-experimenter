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
package org.geotools.kml.bindings;

import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xml.Binding;
import org.opengis.feature.simple.SimpleFeatureType;

public class SchemaTypeBindingTest extends KMLTestSupport {

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.SchemaType).getExecutionMode());
    }

    public void testGetType() {
        assertEquals(SimpleFeatureType.class, binding(KML.SchemaType).getType());
    }

    public void testParse() throws Exception {
        String xml = "<Schema name=\"foo\">"
                + "<SimpleField type=\"int\" name=\"quux\"></SimpleField>" + "</Schema>";
        buildDocument(xml);
        SimpleFeatureType ft = (SimpleFeatureType) parse();
        assertEquals("Unexpected number of attributes", 1, ft.getAttributeCount());
        assertEquals("Unexpected column type", Integer.class, ft.getDescriptor("quux").getType()
                .getBinding());
        assertEquals("foo", ft.getName().getLocalPart());
    }

}
