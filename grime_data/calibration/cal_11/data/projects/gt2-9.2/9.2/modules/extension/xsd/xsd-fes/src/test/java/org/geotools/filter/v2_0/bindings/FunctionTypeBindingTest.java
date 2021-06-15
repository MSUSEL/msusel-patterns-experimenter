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

import org.geotools.filter.v1_1.FilterMockData;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.w3c.dom.Document;

public class FunctionTypeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<fes:Function xmlns:fes='" + FES.NAMESPACE + "' name='abs'>" + 
            "   <fes:Literal>12</fes:Literal> " + 
            "</fes:Function>";
        buildDocument(xml);

        Function f = (Function) parse();
        assertNotNull(f);
        assertEquals("abs", f.getName());
        assertEquals(1, f.getParameters().size());
        assertTrue(f.getParameters().get(0) instanceof Literal);
    }
    
    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.function(), FES.Function);
        assertEquals("fes:Function", dom.getDocumentElement().getNodeName());
        assertEquals("abs", dom.getDocumentElement().getAttribute("name"));
        
        assertNotNull(getElementByQName(dom, FES.ValueReference));
    }
}
