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
 * Test case for Like predicate
 * 
 * <p>
 * <pre>
 *  &lt;text predicate &gt; ::=
 *      &lt;attribute name &gt; [ NOT ] LIKE  &lt;character pattern &gt;
 *      For example:
 *      attribute like '%contains_this%'
 *      attribute like 'begins_with_this%'
 *      attribute like '%ends_with_this'
 *      attribute like 'd_ve' will match 'dave' or 'dove'
 *      attribute not like '%will_not_contain_this%'
 *      attribute not like 'will_not_begin_with_this%'
 *      attribute not like '%will_not_end_with_this'
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
public class CQLLikePredicateTest {

    protected final Language language;
    
    public CQLLikePredicateTest(){
        this(Language.CQL);
    }

    public CQLLikePredicateTest(final Language language) {
        
        assert language != null: "language cannot be null";
        
        this.language = language;
    }

    /**
     * Test Text Predicate
     * <p>
     *
     * <pre>
     *  &lt;text predicate &gt; ::=
     *      &lt;attribute name &gt; [ NOT ] <b>LIKE</b>  &lt;character pattern &gt;
     *      For example:
     *      attribute like '%contains_this%'
     *      attribute like 'begins_with_this%'
     *      attribute like '%ends_with_this'
     *      attribute like 'd_ve' will match 'dave' or 'dove'
     * </pre>
     *
     * </p>
     */
    @Test
    public void likePredicate() throws Exception {

        // Like
        Filter resultFilter = CompilerUtil.parseFilter(this.language, FilterCQLSample.LIKE_FILTER);

        Assert.assertNotNull("Filter expected", resultFilter);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.LIKE_FILTER);

        Assert.assertEquals("like filter was expected", expected, resultFilter);

    }
    
    /**
     * Test Text Predicate
     * <p>
     *
     * <pre>
     *  &lt;text predicate &gt; ::=
     *      &lt;attribute name &gt; <b>[ NOT ] LIKE</b>  &lt;character pattern &gt;
     *      For example:
     *      attribute not like '%will_not_contain_this%'
     *      attribute not like 'will_not_begin_with_this%'
     *      attribute not like '%will_not_end_with_this'
     * </pre>
     *
     * </p>
     */
    @Test
    public void notLikePredicate() throws Exception{
        // not Like
        Filter resultFilter = CompilerUtil.parseFilter(this.language,FilterCQLSample.NOT_LIKE_FILTER);

        Assert.assertNotNull("Filter expected", resultFilter);

        Filter expected = FilterCQLSample.getSample(FilterCQLSample.NOT_LIKE_FILTER);

        Assert.assertEquals("like filter was expected", expected, resultFilter);
        
    }
    
}
