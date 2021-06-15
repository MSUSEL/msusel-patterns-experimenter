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
 *    (C) 2005-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;


import junit.framework.TestCase;

/**
 * Tests whether SLD Parser encodes Namespace in PropertyNames
 * 
 * 
 * @author Niels Charlier
 */
public class SLDParserNamespaceTest extends TestCase {

    public static String SLD = 
        "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" >"+
        " <NamedLayer>"+
        "  <Name>layer</Name>"+
        "  <UserStyle>"+
        "   <Name>style</Name>"+
        "   <FeatureTypeStyle>"+
        "    <Rule xmlns:gsml=\"urn:cgi:xmlns:CGI:GeoSciML:2.0\">"+
        "      <ogc:Filter xmlns:xlink=\"http://www.w3.org/1999/xlink\">"+
        "      <ogc:PropertyIsEqualTo>"+
        "         <ogc:PropertyName>gsml:specification/gml:GeologicFeature/@xlink:href</ogc:PropertyName>"+
        "         <ogc:Literal>foo</ogc:Literal>"+
        "      </ogc:PropertyIsEqualTo>"+
        "      </ogc:Filter>"+
        "      <PolygonSymbolizer>"+
        "           <Geometry> " +
        "              <ogc:PropertyName>gsml:shape</ogc:PropertyName>" +
        "           </Geometry> " +
        "        <Fill>"+
        "        <CssParameter name=\"fill\">#FF0000</CssParameter>"+
        "       </Fill>"+
        "      </PolygonSymbolizer>"+
        "    </Rule>"+
        "   </FeatureTypeStyle>"+
        "  </UserStyle>"+
        " </NamedLayer>"+
        "</StyledLayerDescriptor>";
    
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    
    public void testNamespace() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input());
        Style[] styles = parser.readXML();
        assertEquals(styles.length, 1);
        Style style = styles[0];
        List<FeatureTypeStyle> ftstyles = style.featureTypeStyles();
        assertEquals(ftstyles.size(), 1);
        FeatureTypeStyle ftstyle = ftstyles.get(0);
        List<Rule> rules = ftstyle.rules();
        assertEquals(rules.size(), 1);
        Rule rule = rules.get(0);
        Filter filter = rule.getFilter();
        assert(filter instanceof PropertyIsEqualTo);
        Expression expr = ((PropertyIsEqualTo) filter).getExpression1();
        assert(expr instanceof PropertyName);
        NamespaceSupport ns = ((PropertyName) expr).getNamespaceContext();
        assertEquals(ns.getURI("xlink"), "http://www.w3.org/1999/xlink");
        assertEquals(ns.getURI("gml"), "http://www.opengis.net/gml");
        assertEquals(ns.getURI("gsml"), "urn:cgi:xmlns:CGI:GeoSciML:2.0");
        
        Symbolizer s = rule.getSymbolizers()[0];
        expr = s.getGeometry();
        assert(expr instanceof PropertyName);
        ns = ((PropertyName) expr).getNamespaceContext();
        assertEquals(ns.getURI("xlink"), null);
        assertEquals(ns.getURI("gml"), "http://www.opengis.net/gml");
        assertEquals(ns.getURI("gsml"), "urn:cgi:xmlns:CGI:GeoSciML:2.0");
        
    }   
    
    
    InputStream input() {
        return new ByteArrayInputStream(SLD.getBytes());
    }
}
