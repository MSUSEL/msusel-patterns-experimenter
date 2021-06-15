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
package org.geotools.process.function;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

/**
 * 
 *
 * @source $URL$
 */
public class ParameterFunctionTest {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testEmtpy() {
        Function param = ff.function("parameter");
        try {
            param.evaluate(null);
            fail("This should have failed with an illegal argument exception");
        } catch(IllegalArgumentException e) {
            // fine
        }
    }
    
    @Test
    public void testContext() {
        Object context = new Object();
        Function param = ff.function("parameter", ff.literal("argument"));
        Map<String, Object> result = (Map<String, Object>) param.evaluate(context);
        assertEquals(1, result.size());
        assertEquals("argument", result.keySet().iterator().next());
        assertSame(context, result.values().iterator().next());
    }
    
    @Test
    public void testOne() {
        Object value = new Object();
        Function param = ff.function("parameter", ff.literal("argument"), ff.literal(value));
        Map<String, Object> result = (Map<String, Object>) param.evaluate(null);
        assertEquals(1, result.size());
        assertEquals("argument", result.keySet().iterator().next());
        assertSame(value, result.values().iterator().next());
    }
    
    @Test
    public void testMany() {
        Object value1 = new Object();
        Object value2 = new Object();
        Function param = ff.function("parameter", ff.literal("argument"), ff.literal(value1), ff.literal(value2));
        Map<String, Object> result = (Map<String, Object>) param.evaluate(null);
        assertEquals(1, result.size());
        assertEquals("argument", result.keySet().iterator().next());
        List<Object> value = (List<Object>) result.values().iterator().next();
        assertEquals(2, value.size());
        assertSame(value1, value.get(0));
        assertSame(value2, value.get(1));
    }
}
