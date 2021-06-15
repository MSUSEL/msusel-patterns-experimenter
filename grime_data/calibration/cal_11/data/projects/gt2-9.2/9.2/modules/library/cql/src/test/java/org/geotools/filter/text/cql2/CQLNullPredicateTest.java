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

/**
 * Test CQL Null Predicate:
 * <p>
 *
 * <pre>
 * &lt;null predicate &gt; ::=  &lt;attribute name &gt; IS [ NOT ] NULL
 * </pre>
 *
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.5 
 *
 *
 *
 * @source $URL$
 */
public class CQLNullPredicateTest  {
    protected final Language language;
    
    public CQLNullPredicateTest(){
        this(Language.CQL);
    }

    public CQLNullPredicateTest(final Language language) {
        
        assert language != null: "language cannot be null";
        
        this.language = language;
    }

    /**
     * test the 
     * @param samplePredicate
     * @param expected
     * @throws Exception
     */
    protected void testNullPredicate(final String samplePredicate, Filter expected) throws Exception{
        
        Filter actual = CompilerUtil.parseFilter(this.language, samplePredicate);

        Assert.assertNotNull("expects a not null filter", actual);

        Assert.assertEquals("Filter error", expected, actual);
    }
    
    /**
     * Sample: ATTR1 IS NULL
     */
    @Test
    public void propertyIsNull() throws Exception {

        final String samplePredicate = FilterCQLSample.PROPERTY_IS_NULL;
        Filter expected = FilterCQLSample.getSample(samplePredicate);
        testNullPredicate(samplePredicate, expected);
    }
    
    /**
     * Sample: ATTR1 IS NOT NULL
     * @throws Exception 
     */
    @Test
    public void propertyIsNotNull() throws Exception{
        
        final String samplePredicate = FilterCQLSample.PROPERTY_IS_NOT_NULL;
        Filter expected = FilterCQLSample.getSample(samplePredicate);
        testNullPredicate(samplePredicate, expected);
    }
    
}
