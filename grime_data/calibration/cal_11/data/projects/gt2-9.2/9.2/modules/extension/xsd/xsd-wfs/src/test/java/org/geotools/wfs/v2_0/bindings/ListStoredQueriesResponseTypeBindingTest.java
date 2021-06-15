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
package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs20.ListStoredQueriesResponseType;
import net.opengis.wfs20.StoredQueryListItemType;
import net.opengis.wfs20.TitleType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ListStoredQueriesResponseTypeBindingTest extends WFSTestSupport {

    public void testEncode() throws Exception {
        Wfs20Factory factory = Wfs20Factory.eINSTANCE;
        
        StoredQueryListItemType sqli = factory.createStoredQueryListItemType();
        sqli.setId("fooId");
        
        TitleType title = factory.createTitleType();
        title.setValue("fooTitle");
        sqli.getTitle().add(title);
        
        sqli.getReturnFeatureType().add(new QName("http://foo.org", "fooName", "foo"));
        
        ListStoredQueriesResponseType lsqr = factory.createListStoredQueriesResponseType();
        lsqr.getStoredQuery().add(sqli);

        Document dom = encode(lsqr, WFS.ListStoredQueriesResponse);
        
        Element e = getElementByQName(dom, WFS.StoredQuery);
        assertEquals("fooId", e.getAttribute("id"));
        
        assertNotNull(getElementByQName(dom, WFS.Title));
        assertNotNull(getElementByQName(e, new QName(WFS.NAMESPACE, "ReturnFeatureType")));
    }
}
