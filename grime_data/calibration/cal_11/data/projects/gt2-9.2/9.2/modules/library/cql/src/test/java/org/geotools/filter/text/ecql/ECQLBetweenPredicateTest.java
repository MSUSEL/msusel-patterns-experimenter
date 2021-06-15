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

package org.geotools.filter.text.ecql;

import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLBetweenPredicateTest;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;
import org.opengis.filter.Filter;


/**
 * Test case for between predicate with expressions
 * <p>
 * <pre>
 * &lt; between predicate &gt; ::= &lt; expression &gt; [ "NOT" ] "BETWEEN" &lt; expression &gt; "AND" &lt; expression &gt;
 * </pre>
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class ECQLBetweenPredicateTest extends CQLBetweenPredicateTest{

    public ECQLBetweenPredicateTest() {
        // sets the language used to execute this test case
        super(Language.ECQL);
    }
    
    /**
     * sample: 2 BETWEEN 1 AND 3 
     * @throws CQLException 
     */
    @Test
    public void literalBetweenLiterals() throws Exception{

        String txtPredicate = FilterECQLSample.LITERAL_BETWEEN_TWO_LITERALS;
        Filter expected = FilterECQLSample.getSample(txtPredicate);
        
        testBetweenPredicate(txtPredicate, expected);
    }
    
    /**
     * sample: 2 BETWEEN (2-1) AND (2+1)
     * 
     * @throws CQLException 
     */
    @Test
    public void literalBetweenExpressions() throws Exception{
        

        String txtPredicate = FilterECQLSample.LITERAL_BETWEEN_TWO_EXPRESSIONS;
        Filter expected = FilterECQLSample.getSample(txtPredicate);
        
        testBetweenPredicate(txtPredicate, expected);
    }

    /**
     * sample: area( the_geom ) BETWEEN 10000 AND 30000
     * @throws Exception 
     */
    @Test
    public void functionBetweenLiterals() throws Exception{

        String txtPredicate = FilterECQLSample.FUNCTION_BETWEEN_LITERALS;
        Filter expected = FilterECQLSample.getSample(txtPredicate);
        
        testBetweenPredicate(txtPredicate, expected);
    }
    
    /**
     * sample: area( the_geom ) BETWEEN abs(10000) AND abs(30000)
     * @throws Exception 
     */
    @Test
    public void functionBetweenFunctions()throws Exception{
        
        final String txtPredicate= FilterECQLSample.FUNCTION_BETWEEN_FUNCTIONS;
        Filter expected = FilterECQLSample.getSample(txtPredicate);
        
        testBetweenPredicate(txtPredicate, expected);
    }
    
}
