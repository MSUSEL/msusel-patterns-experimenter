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
package org.geotools.filter.function;

import java.text.DecimalFormatSymbols;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * 
 *
 * @source $URL$
 */
public class NumberFormatTest extends TestCase {

    public void testFormatDouble() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("#.##");
        Literal number = ff.literal("10.56789");
        
        Function f = ff.function("numberFormat", new Expression[]{pattern, number});
        char ds = new DecimalFormatSymbols().getDecimalSeparator();
        assertEquals("10" + ds + "57", f.evaluate(null , String.class));
    }
    
    public void testFormatInteger() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###");
        Literal number = ff.literal("123456");
        
        Function f = ff.function("numberFormat", new Expression[]{pattern, number});
        char gs = new DecimalFormatSymbols().getGroupingSeparator();
        assertEquals("123" + gs + "456", f.evaluate(null , String.class));
    }
    
    public void testNumberFormat2() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal pattern = ff.literal("###,###.##");
        Literal number = ff.literal("-123456.7891");
        Literal minus = ff.literal("x");
        Literal ds = ff.literal(":");
        Literal gs = ff.literal(";");
        
        Function f = ff.function("numberFormat2", new Expression[]{pattern, number, minus, ds, gs});
        assertEquals("x123;456:79", f.evaluate(null, String.class));
        
    }
}
