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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.expression.Expression;


/**
 * Unit test for between predicate
 * <p>
 * <pre>
 *  This cql clause is an extension for convenience.
 *  &lt;between predicate &gt; ::=
 *  &lt;attribute name &gt; [ NOT ] BETWEEN  &lt;literal&amp; #62; AND  &lt; literal  &gt;
 * </pre>
 * </p>
 * 
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.5
 *
 *
 *
 * @source $URL$
 */
public class CQLBetweenPredicateTest {

    
    protected final Language language;

    public CQLBetweenPredicateTest(){
        
        this(Language.CQL);
    }

    public CQLBetweenPredicateTest(final Language language){
        
        assert language != null: "language cannot be null value";
        
        this.language = language;
    }

    /**
     * BETWEEN predicate test
     */
    @Test
    public void propertyBetweenLiterals() throws Exception {
        // between
        Filter expected = FilterCQLSample.getSample( FilterCQLSample.BETWEEN_FILTER );
        
        testBetweenPredicate( FilterCQLSample.BETWEEN_FILTER, expected );
    }
    
    /**
     * NOT BETWEEN predicate test
     */
    @Test
    public void notBetweenPredicate() throws Exception{
        
        // not between
        Filter expected = FilterCQLSample.getSample(FilterCQLSample.NOT_BETWEEN_FILTER);

        testBetweenPredicate(FilterCQLSample.NOT_BETWEEN_FILTER, expected);
    }

    /**
     * test for between predicate with compound attribute
     * 
     * @throws Exception
     */
    @Test
    public void compoundAttributeInBetweenPredicate() throws Exception{
        
        // test compound attribute gmd:aa:bb.gmd:cc.gmd:dd
        final String prop = "gmd:aa:bb.gmd:cc.gmd:dd";
        final String propExpected = "gmd:aa:bb/gmd:cc/gmd:dd";
        Filter resultFilter = CQL.toFilter(prop + " BETWEEN 100 AND 200 ");

        Assert.assertTrue("PropertyIsBetween filter was expected",
            resultFilter instanceof PropertyIsBetween);

        PropertyIsBetween filter = (PropertyIsBetween) resultFilter;
        Expression property = filter.getExpression();

        Assert.assertEquals(propExpected, property.toString());
    }
    
    /**
     * Execute the test with the provided sample
     * 
     * @param samplePredicate
     * @throws Exception
     */
    protected void testBetweenPredicate(final String samplePredicate, Filter expected) throws Exception{
        
        Filter actual = CompilerUtil.parseFilter(this.language, samplePredicate);

        Assert.assertNotNull("expects a not null filter", actual);

        Assert.assertEquals("between filter error", expected, actual);
    }
    
    
}
