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

import java.util.Date;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLTemporalPredicateTest;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.During;
import org.opengis.temporal.Period;


/**
 * ECQL Temporal predicate
 * 
 * <p>
 * Executes all cql temporal predicate test using the ECQL parser
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class ECQLTemporalPredicateTest extends CQLTemporalPredicateTest {

    public ECQLTemporalPredicateTest() {
        // sets the TXT language used to execute this test case
        super(Language.ECQL);
    }
    
    /**
     * Before predicate with dateTime in the leftHand
     * 
     * @throws CQLException
     */
    @Test
    public void dateTimeExpresionBeforeDateTimeExpresion() throws CQLException{
        
        final String predicate = "2006-11-30T01:00:00Z BEFORE 2006-11-30T01:30:00Z ";
        Filter resultFilter = CompilerUtil.parseFilter(this.language, predicate);

        Assert.assertTrue( resultFilter instanceof Before);

        Before lessFilter = (Before) resultFilter;
        
        Literal expr1 = (Literal)lessFilter.getExpression1();
        Date leftHandDate = (Date) expr1.getValue();
        Assert.assertTrue(leftHandDate instanceof Date);

        Literal expr2 = (Literal) lessFilter.getExpression2();
        Date rightHandDate = (Date) expr2.getValue();
        Assert.assertTrue(rightHandDate instanceof Date);
        
        Assert.assertTrue(resultFilter.evaluate(null));
    }
    /**
     * After predicate with dateTime in the leftHand
     * 
     * @throws CQLException
     */
    @Test
    public void dateTimeExpresionAfterDateTimeExpresion() throws CQLException{
        
        final String predicate = "2006-11-30T01:00:00Z AFTER 2006-11-30T01:30:00Z";
        Filter resultFilter = CompilerUtil.parseFilter(this.language, predicate);

        Assert.assertTrue( resultFilter instanceof After);

        After lessFilter = (After) resultFilter;
        
        Literal expr1 = (Literal)lessFilter.getExpression1();
        Date leftHandDate = (Date) expr1.getValue();
        Assert.assertTrue(leftHandDate instanceof Date);

        Literal expr2 = (Literal) lessFilter.getExpression2();
        Date rightHandDate = (Date) expr2.getValue();
        Assert.assertTrue(rightHandDate instanceof Date);
        
        Assert.assertFalse(resultFilter.evaluate(null));
    }

    /**
     * During predicate with dateTime in the leftHand
     * 
     * @throws CQLException
     */
    @Test
    public void dateTimeDuringPeriod() throws CQLException{
        
        final String predicate = "2006-11-30T01:00:00Z DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z ";
        Filter resultFilter = CompilerUtil.parseFilter(this.language, predicate);

        Assert.assertTrue( resultFilter instanceof During);

        During lessFilter = (During) resultFilter;
        
        Literal expr1 = (Literal)lessFilter.getExpression1();
        Assert.assertTrue(expr1.getValue() instanceof Date);

        Literal expr2 = (Literal) lessFilter.getExpression2();
        Assert.assertTrue(expr2.getValue() instanceof Period);
        
        Assert.assertTrue(resultFilter.evaluate(null));
    }
    
    /**
     * The left hand should be a property or temporal expression. 
     * An math expression in the left hand of the temporal predicate is a syntax error
     * @throws CQLException
     */
    @Test(expected = CQLException.class)
    public void beforeInvalidLeftHandExpression() throws CQLException{
        
        final String predicate = "(1+2) BEFORE 2006-11-30T01:30:00Z ";
        CompilerUtil.parseFilter(this.language, predicate);
    }
    
}
