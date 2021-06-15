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

import net.opengis.wfs20.DescribeFeatureTypeType;

import org.geotools.wfs.v2_0.WFSTestSupport;

public class DescribeFeatureTypeTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<DescribeFeatureType service='WFS' version='2.0.0' xmlns='http://www.opengis.net/wfs/2.0' " + 
            "xmlns:myns='http://www.myserver.com/myns' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
            "xsi:schemaLocation='http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd'>" + 
            "<TypeName>myns:TreesA_1M</TypeName>" + 
            "<TypeName>myns:RoadL_1M</TypeName>" + 
         "</DescribeFeatureType>";
        buildDocument(xml);
        
        DescribeFeatureTypeType dft = (DescribeFeatureTypeType) parse();
        assertNotNull(dft);
        
        assertEquals(2, dft.getTypeName().size());
        assertEquals(new QName("http://www.myserver.com/myns", "TreesA_1M"), dft.getTypeName().get(0));
        assertEquals(new QName("http://www.myserver.com/myns", "RoadL_1M"), dft.getTypeName().get(1));
    }
}
