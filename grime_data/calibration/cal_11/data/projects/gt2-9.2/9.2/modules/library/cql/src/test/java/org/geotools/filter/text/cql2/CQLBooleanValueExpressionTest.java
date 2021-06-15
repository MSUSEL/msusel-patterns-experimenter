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
import org.opengis.filter.Not;

/**
 * Test boolean value expressions.
 * <p>
 * 
 * <pre>
 *  &lt;boolean value expression &gt; ::=
 *          &lt;boolean term &gt;
 *      |   &lt;boolean value expression &gt; OR  &lt;boolean term &gt;
 *  &lt;boolean term &gt; ::=
 *          &lt;boolean factor &gt;
 *      |   &lt;boolean term &gt; AND  &lt;boolean factor&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class CQLBooleanValueExpressionTest {
    protected final org.geotools.filter.text.commons.Language language;

    public CQLBooleanValueExpressionTest(){
        
        this(Language.CQL);
    }

    public CQLBooleanValueExpressionTest(final Language language){
        
        assert language != null: "language cannot be null value";
        
        this.language = language;
    }

    /**
     * Sample: ATTR1 < 10 AND ATTR2 < 2
     * @throws CQLException 
     */
    @Test 
    public void and() throws CQLException{
        Filter result = CompilerUtil.parseFilter(this.language,FilterCQLSample.FILTER_AND);

        Assert.assertNotNull("filter expected", result);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_AND);

        Assert.assertEquals("ATTR1 < 10 AND ATTR2 < 2 was expected", expected, result);
        
    }

    /**
     * Sample: "ATTR1 > 10 OR ATTR2 < 2"
     * @throws CQLException 
     */
    @Test
    public void or() throws CQLException{
        // "ATTR1 > 10 OR ATTR2 < 2"
        Filter result = CompilerUtil.parseFilter(this.language,FilterCQLSample.FILTER_OR);

        Assert.assertNotNull("filter expected", result);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_OR);

        Assert.assertEquals("ATTR1 > 10 OR ATTR2 < 2 was expected", expected, result);
        
    }

    /**
     * Sample 1: ATTR1 < 10 AND ATTR2 < 2 OR ATTR3 > 10
     * Sample 2: ATTR3 < 4 AND (ATT1 > 10 OR ATT2 < 2)
     * @throws CQLException 
     */
    @Test
    public void andOr() throws CQLException{
        Filter result;
        Filter expected;
        // ATTR1 < 10 AND ATTR2 < 2 OR ATTR3 > 10
        result = CompilerUtil.parseFilter(language, FilterCQLSample.FILTER_OR_AND);

        Assert.assertNotNull("filter expected", result);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_OR_AND);

        Assert.assertEquals("a bad filter was expected", expected, result);

        // ATTR3 < 4 AND (ATT1 > 10 OR ATT2 < 2)
        result = CompilerUtil.parseFilter(language, FilterCQLSample.FILTER_OR_AND_PARENTHESIS);

        Assert.assertNotNull("filter expected", result);

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_OR_AND_PARENTHESIS);

        Assert.assertEquals("a bad filter was expected", expected, result);
        
    }

    /**
     * Sample: NOT  ATTR1 < 10
     * @throws Exception
     */
    @Test 
    public void not() throws Exception {

        final String stmt = "NOT " + FilterCQLSample.LESS_FILTER_SAMPLE;
        Filter result = CompilerUtil.parseFilter(language, stmt);

        Assert.assertNotNull("filter expected", result);

        Assert.assertTrue( result instanceof Not);

        Not notFilter = (Not)result;
        
        Filter actual = notFilter.getFilter();
        
        Filter expected = FilterCQLSample.getSample(FilterCQLSample.LESS_FILTER_SAMPLE);

        Assert.assertEquals(FilterCQLSample.LESS_FILTER_SAMPLE + "was expected", expected, actual);
    }
    
    /**
     * <pre>
     * Sample: 
     * ATTR3 < 4 AND (NOT( ATTR1 < 10 AND ATTR2 < 2))
     * ATTR1 < 1 AND (NOT (ATTR2 < 2)) AND ATTR3 < 3
     * </pre>
     * @throws Exception
     */
    @Test
    public void andNot() throws Exception {
        Filter result;
        Filter expected;

        // ATTR3 < 4 AND (NOT( ATTR1 < 10 AND ATTR2 < 2))
        result = CompilerUtil.parseFilter(language,
                FilterCQLSample.FILTER_AND_NOT_AND);

        Assert.assertNotNull("filter expected", result);

        expected = FilterCQLSample
                .getSample(FilterCQLSample.FILTER_AND_NOT_AND);

        Assert.assertEquals("a bad filter was produced", expected, result);

        // "ATTR1 < 1 AND (NOT (ATTR2 < 2)) AND ATTR3 < 3"
        result = CompilerUtil.parseFilter(language,
                FilterCQLSample.FILTER_AND_NOT_COMPARASION);

        Assert.assertNotNull("filter expected", result);

        expected = FilterCQLSample
                .getSample(FilterCQLSample.FILTER_AND_NOT_COMPARASION);

        Assert.assertEquals("a bad filter was produced", expected, result);
    }
     


}
