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
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class SortByTypeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = 
        "      <fes:SortBy xmlns:fes='http://www.opengis.net/fes/2.0'> " + 
        "         <fes:SortProperty> " + 
        "            <fes:ValueReference>myns:depth</fes:ValueReference> " + 
        "         </fes:SortProperty> " + 
        "         <fes:SortProperty> " + 
        "            <fes:ValueReference>myns:temperature</fes:ValueReference> " + 
        "            <fes:SortOrder>DESC</fes:SortOrder> " + 
        "         </fes:SortProperty> " + 
        "      </fes:SortBy>";
        buildDocument(xml);

        SortBy[] sortBy = (SortBy[]) parse();
        assertEquals(2, sortBy.length);

        assertEquals("myns:depth", sortBy[0].getPropertyName().getPropertyName());
        assertEquals(SortOrder.ASCENDING, sortBy[0].getSortOrder());
        
        assertEquals("myns:temperature", sortBy[1].getPropertyName().getPropertyName());
        assertEquals(SortOrder.DESCENDING, sortBy[1].getSortOrder());
    }
}
