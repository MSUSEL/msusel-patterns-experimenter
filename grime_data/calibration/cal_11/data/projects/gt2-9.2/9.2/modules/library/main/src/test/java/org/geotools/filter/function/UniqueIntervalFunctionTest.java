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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;


/**
 * Tests UniqueIntervalFunction
 *
 * @author Cory Horner
 *
 *
 * @source $URL$
 */
public class UniqueIntervalFunctionTest extends FunctionTestSupport {
    public UniqueIntervalFunctionTest(String testName) {
        super(testName);
    }

    protected void tearDown() throws java.lang.Exception {
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(UniqueIntervalFunctionTest.class);

        return suite;
    }

    /**
     * Test of getName method, of class
     * org.geotools.filter.functions.UniqueIntervalFunction.
     */
    public void testInstance() {
        Function equInt = ff.function("UniqueInterval", ff.literal(featureCollection));
        assertNotNull(equInt);
    }

    /**
     * Test of getName method, of class
     * org.geotools.filter.functions.UniqueIntervalFunction.
     */
    public void testGetName() {
        Function equInt = ff.function("UniqueInterval", ff.literal(featureCollection));
        assertEquals("UniqueInterval", equInt.getName());
    }

    /**
     * Test of setNumberOfClasses method, of class
     * org.geotools.filter.function.UniqueIntervalFunction.
     */
    public void testSetClasses() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName exp = ff.property("foo");
        UniqueIntervalFunction func = (UniqueIntervalFunction) ff.function("UniqueInterval", exp, classes);
        assertEquals(3, func.getClasses());
        func.setClasses(12);
        assertEquals(12, func.getClasses());
    }

    /**
     * Test of getValue method, of class
     * org.geotools.filter.function.UniqueIntervalFunction.
     */
    public void testEvaluate() throws Exception {
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("foo");
        UniqueIntervalFunction func = (UniqueIntervalFunction) ff.function("UniqueInterval", exp, classes);

        Object result = func.evaluate(featureCollection);
        assertTrue(result instanceof ExplicitClassifier);
        ExplicitClassifier classifier = (ExplicitClassifier) result;
        assertEquals(2, classifier.getSize());
        assertEquals(classifier.values[0].size(), classifier.values[1].size());
        assertFalse(classifier.values[0].removeAll(classifier.values[1]));
        
    }
}
