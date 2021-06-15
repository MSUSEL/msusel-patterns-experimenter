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
package org.geotools.data.jdbc;

import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;



/**
 * Unit test for sql encoding of filters into where statements.
 *
 * @author Chris Holmes, TOPP
 * @author Saul Farber, MassGIS
 *
 *
 *
 * @source $URL$
 */
public class FilterToSQLTest extends TestCase {
    private FilterFactory filterFac = CommonFactoryFinder.getFilterFactory(null);
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc");
    
    private SimpleFeatureType integerFType;
    private SimpleFeatureType stringFType;
    private FilterToSQL encoder;
    private StringWriter output;

    public void setUp() throws Exception {
        Level debugLevel = Level.FINE;
        Logger log = LOGGER;
        while (log != null) {
            log.setLevel(debugLevel);
            for (int i = 0; i < log.getHandlers().length; i++) {
                Handler h = log.getHandlers()[i];
                h.setLevel(debugLevel);
            }
            log = log.getParent();
        }        
        
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
		ftb.setName("testFeatureType");
		ftb.add("testAttr", Integer.class);
        integerFType = ftb.buildFeatureType();
        
        ftb.setName("testFeatureType");
        ftb.add("testAttr", String.class);
        stringFType = ftb.buildFeatureType();
        
        output = new StringWriter();
        encoder = new FilterToSQL(output);
    }
    
    public void testIntegerContext() throws Exception {
        
        Expression literal = filterFac.literal(5);
        Expression prop = filterFac.property(integerFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);
        
        encoder.setFeatureType(integerFType);
        encoder.encode(filter);
        
        LOGGER.fine("testAttr is an Integer " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = 5");
    }
    
    public void testStringContext() throws Exception {
        Expression literal = filterFac.literal(5);
        Expression prop = filterFac.property(stringFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);
        

        encoder.setFeatureType(stringFType);
        encoder.encode(filter);
        
        LOGGER.fine("testAttr is a String " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = '5'");
    }
    
    public void testInclude() throws Exception {
        encoder.encode(Filter.INCLUDE);
        assertEquals(output.getBuffer().toString(), "WHERE 1 = 1");
    }
    
    public void testExclude() throws Exception {
        encoder.encode(Filter.EXCLUDE);
        assertEquals(output.getBuffer().toString(), "WHERE 0 = 1");
    }
    
    public void testIdFilter() throws Exception {
        
        // SOMEONE NEEDS TO WRITE AN ID FILTER!
        // SDE doesn't use the FIDMapper classes, so 
        // I'm not sure how a real-world encoder would want them encoded
    }
    
    public void testEscapeQuote() throws FilterToSQLException {
        PropertyIsEqualTo equals = filterFac.equals(filterFac.property("attribute"), filterFac.literal("A'A"));
        encoder.encode(equals);
        assertEquals("WHERE attribute = 'A''A'", output.toString());
    }
    
    public void testExpression() throws Exception {
        Add a = filterFac.add(filterFac.property("testAttr"), filterFac.literal(5));
        encoder.encode(a);
        assertEquals("testAttr + 5", output.toString());
    }
    
    public void testEscapeQuoteFancy() throws FilterToSQLException  {
        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Object fancyLiteral = new Object() {
        
            public String toString() {
                return "A'A";
            }
        
        };
        PropertyIsEqualTo equals = ff.equals(ff.property("attribute"), ff.literal(fancyLiteral));
        StringWriter output = new StringWriter();
        FilterToSQL encoder = new FilterToSQL(output);
        encoder.encode(equals);
        assertEquals("WHERE attribute = 'A''A'", output.toString());
    }
    
    public void testNumberEscapes() throws Exception {
        Add a = filterFac.add(filterFac.property("testAttr"), filterFac.literal(5));
        PropertyIsEqualTo equal = filterFac.equal(filterFac.property("testAttr"), a, false);
        StringWriter output = new StringWriter();
        FilterToSQL encoder = new FilterToSQL(output);
        // this test must pass even when the target feature type is not known
        // encoder.setFeatureType(integerFType);
        encoder.encode(equal);
        assertEquals("WHERE testAttr = testAttr + 5", output.toString());
    }

    public void testInline() throws Exception {
        PropertyIsEqualTo equal = filterFac.equal(filterFac.property("testAttr"), filterFac.literal(5), false);
        StringWriter output = new StringWriter();
        FilterToSQL encoder = new FilterToSQL(output);
        encoder.setInline(true);
        
        encoder.encode(equal);
        assertEquals("testAttr = 5", output.toString());
    }
}
