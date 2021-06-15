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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import java.util.Arrays;

import javax.xml.namespace.QName;

import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.w3c.dom.Document;

public class StoredQueryDescriptionTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        String xml = 
        "   <wfs:StoredQueryDefinition id='myStoredQuery' xmlns:wfs='http://www.opengis.net/wfs/2.0' " +
        "       xmlns:fes='http://www.opengis.net/fes/2.0' xmlns:gml='http://www.opengis.net/gml/3.2'>" + 
        "      <wfs:Parameter name='AreaOfInterest' type='gml:Polygon'/> " + 
        "      <wfs:QueryExpressionText " + 
        "           returnFeatureTypes='sf:PrimitiveGeoFeature' " + 
        "           language='urn:ogc:def:queryLanguage:OGC-WFS::WFS_QueryExpression' " + 
        "           isPrivate='false'> " + 
        "         <wfs:Query typeNames='myns:Parks'> " + 
        "            <fes:Filter> " + 
        "               <fes:Within> " + 
        "                  <fes:ValueReference>pointProperty</fes:ValueReference> " + 
        "                  ${AreaOfInterest} " + 
        "               </fes:Within> " + 
        "            </fes:Filter> " + 
        "         </wfs:Query> " + 
        "      </wfs:QueryExpressionText> " + 
        "   </wfs:StoredQueryDefinition> ";
        buildDocument(xml);
        
        StoredQueryDescriptionType sqd = (StoredQueryDescriptionType) parse(WFS.StoredQueryDescriptionType);
        assertNotNull(sqd);
        
        assertEquals(1, sqd.getParameter().size());
        
        ParameterExpressionType p = sqd.getParameter().get(0);
        assertEquals("AreaOfInterest", p.getName());
        assertEquals(GML.Polygon, p.getType());
        
        assertEquals(1, sqd.getQueryExpressionText().size());
    }

    public void testEncode() throws Exception {
        Wfs20Factory f = Wfs20Factory.eINSTANCE;
        StoredQueryDescriptionType sqd = f.createStoredQueryDescriptionType();
        sqd.setId("foo");

        ParameterExpressionType p = f.createParameterExpressionType();
        p.setName("AreaOfInterest");
        p.setType(GML.Polygon);
        sqd.getParameter().add(p);

        String xml = 
        "<wfs:Query typeNames='myns:Parks'>" + 
        "  <fes:Filter>" + 
        "   <fes:Within>\n" + 
        "     <fes:ValueReference>geometry</fes:ValueReference>" + 
        "     ${AreaOfInterest}" + 
        "   </fes:Within>" + 
        "  </fes:Filter>" + 
        "</wfs:Query> "; 
        
        QueryExpressionTextType qet = f.createQueryExpressionTextType();
        sqd.getQueryExpressionText().add(qet);
        
        qet.setLanguage("urn:ogc:def:queryLanguage:OGC-WFS::WFS_QueryExpression");
        qet.setReturnFeatureTypes(Arrays.asList(new QName("http://www.someserver.com/myns", "Parks")));
        qet.setValue(xml);

        Document dom = encode(sqd, WFS.StoredQueryDescription, WFS.StoredQueryDescriptionType);
        print(dom);
        assertEquals("wfs:StoredQueryDescription", dom.getDocumentElement().getNodeName());
        assertEquals(1, dom.getElementsByTagName("wfs:QueryExpressionText").getLength());
        
        assertTrue(dom.getElementsByTagName("wfs:QueryExpressionText").item(0).getFirstChild()
            .getNodeName().equals("wfs:Query"));
    }
}
