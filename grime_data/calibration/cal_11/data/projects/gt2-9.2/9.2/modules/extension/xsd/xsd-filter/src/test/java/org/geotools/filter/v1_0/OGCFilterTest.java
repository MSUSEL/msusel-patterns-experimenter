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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v1_0;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DWithin;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xml.Parser.Properties;


/**
 * 
 *
 * @source $URL$
 */
public class OGCFilterTest extends TestCase {
    Parser parser;

    protected void setUp() throws Exception {
        super.setUp();

        Configuration configuration = new OGCConfiguration();
        parser = new Parser(configuration);
    }

    public void testRun() throws Exception {
        Object thing = parser.parse(getClass().getResourceAsStream("test1.xml"));
        assertNotNull(thing);
        assertTrue(thing instanceof PropertyIsEqualTo);

        PropertyIsEqualTo equal = (PropertyIsEqualTo) thing;
        assertTrue(equal.getExpression1() instanceof PropertyName);
        assertTrue(equal.getExpression2() instanceof Literal);

        PropertyName name = (PropertyName) equal.getExpression1();
        assertEquals("testString", name.getPropertyName());

        Literal literal = (Literal) equal.getExpression2();
        assertEquals("2", literal.toString());
    }

    public void testLax() throws Exception {
        String xml = "<Filter>" + "  <PropertyIsEqualTo>" + "    <PropertyName>foo</PropertyName>"
            + "    <Literal>bar</Literal>" + "  </PropertyIsEqualTo>" + "</Filter>";

        OGCConfiguration configuration = new OGCConfiguration();
        configuration.getProperties().add(Properties.IGNORE_SCHEMA_LOCATION);

        Parser parser = new Parser(configuration);
        Filter filter = (Filter) parser.parse(new ByteArrayInputStream(xml.getBytes()));
        assertNotNull(filter);
    }
    

    public void testDWithinParse() throws Exception {

       String xml = "<Filter>" +
           "<DWithin>" +
             "<PropertyName>the_geom</PropertyName>" + 
             "<Point>" +  
                 "<coordinates>-74.817265,40.5296504</coordinates>" + 
              "</Point>" +
              "<Distance units=\"km\">200</Distance>" +
            "</DWithin>" +
          "</Filter>";
       
       OGCConfiguration configuration = new OGCConfiguration();
       configuration.getProperties().add(Properties.IGNORE_SCHEMA_LOCATION);

       Parser parser = new Parser(configuration);
       DWithin filter = (DWithin) parser.parse(new ByteArrayInputStream(xml.getBytes()));
       assertNotNull(filter);
       
       //Asserting the Property Name
       assertNotNull(filter.getExpression1());
       PropertyName propName = (PropertyName) filter.getExpression1();
       String name = propName.getPropertyName();
       assertEquals("the_geom", name);
       
       //Asserting the Geometry
       assertNotNull(filter.getExpression2());
       Literal geom = (Literal) filter.getExpression2();
       assertEquals("POINT (-74.817265 40.5296504)", geom.toString());
       
       //Asserting the Distance
       assertTrue(filter.getDistance() > 0 );
       Double dist = filter.getDistance();
       assertEquals(200.0, dist);
       
       //Asserting the Distance Units
       assertNotNull(filter.getDistanceUnits());
       String unit = filter.getDistanceUnits();
       assertEquals("km", unit);
    }
}
