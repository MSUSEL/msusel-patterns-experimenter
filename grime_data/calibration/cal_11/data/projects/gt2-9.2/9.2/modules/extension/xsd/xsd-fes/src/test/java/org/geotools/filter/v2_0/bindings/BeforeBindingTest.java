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
package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.temporal.Before;
import org.opengis.temporal.Instant;

public class BeforeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = 
        "<fes:Filter " + 
        "   xmlns:fes='http://www.opengis.net/fes/2.0' " + 
        "   xmlns:gml='http://www.opengis.net/gml/3.2' " + 
        "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
        "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd" + 
        " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'>" + 
        "   <fes:Before> " + 
        "      <fes:ValueReference>timeInstanceAttribute</fes:ValueReference> " + 
        "      <gml:TimeInstant gml:id='TI1'> " + 
        "         <gml:timePosition>2005-05-19T09:28:40Z</gml:timePosition> " + 
        "      </gml:TimeInstant> " + 
        "   </fes:Before> " + 
        "</fes:Filter>";
        buildDocument(xml);
        
        Before before = (Before) parse();
        assertNotNull(before);
        
        assertTrue(before.getExpression1() instanceof PropertyName);
        assertEquals("timeInstanceAttribute", ((PropertyName)before.getExpression1()).getPropertyName());

        assertTrue(before.getExpression2() instanceof Literal);
        assertTrue(before.getExpression2().evaluate(null) instanceof Instant);
    }
}
