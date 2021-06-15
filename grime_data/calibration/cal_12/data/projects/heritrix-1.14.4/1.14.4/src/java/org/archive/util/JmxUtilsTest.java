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
package org.archive.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

import junit.framework.TestCase;

public class JmxUtilsTest extends TestCase {
    public void testCreateCompositeType() throws OpenDataException {
        Map<String,Object> m =  new HashMap<String,Object>();
        m.put("0", new Long(0));
        m.put("1", new Double(1));
        m.put("2", "2");
        CompositeType ct = JmxUtils.createCompositeType(m, "ct", "description");
        testCompositeDataHasMapContent(ct, m);
        // Now mess with the order.
        Map<String,Object> n = new HashMap<String,Object>();
        n.put("0", new Long(17));
        n.put("2", "Some old string");
        n.put("1", new Double(17.45));
        testCompositeDataHasMapContent(ct, n);
    }

    private void testCompositeDataHasMapContent(final CompositeType ct,
            final Map m)
    throws OpenDataException {
        CompositeData cd = new CompositeDataSupport(ct, m);
        for (final Iterator i = m.keySet().iterator(); i.hasNext();) {
            String key = (String)i.next();
            assertTrue(cd.containsKey(key));
            assertEquals(m.get(key), cd.get(key));
        }
    }

}
