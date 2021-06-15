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
package org.geotools.ows.v1_1;

import java.util.Map;

import net.opengis.ows11.DomainType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.ValueType;

import org.geotools.xml.Parser;

public class CapabilitiesParseTest extends OWSTestSupport {

    public void testParseCapabilities() throws Exception {
        Parser p = new Parser(createConfiguration());
        Object o = p.parse(getClass().getResourceAsStream("exampleCapabilities1.xml"));

        //the core ows schema is abstract, and meant to be extended, so the root caps document
        // is usually declared by the specific schema, since hte ows schema doens't know about it
        // we get back a map
        assertTrue(o instanceof Map);

        Map caps = (Map) o;
        assertTrue(caps.containsKey("OperationsMetadata"));

        OperationsMetadataType om = (OperationsMetadataType) caps.get("OperationsMetadata");
        assertEquals(3, om.getOperation().size());

        OperationType op = (OperationType) om.getOperation().get(0);
        assertEquals("GetCapabilities", op.getName());
        
        assertEquals(1, op.getParameter().size());
        DomainType d = (DomainType) op.getParameter().get(0);
        assertEquals("Format", d.getName());

        assertEquals(1, d.getAllowedValues().getValue().size());

        ValueType v =  (ValueType) d.getAllowedValues().getValue().get(0);
        assertEquals("text/xml", v.getValue());
    }
}
