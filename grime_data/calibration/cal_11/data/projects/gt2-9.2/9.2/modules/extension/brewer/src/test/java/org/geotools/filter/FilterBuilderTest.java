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
package org.geotools.filter;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.spatial.Equals;


/**
 * FilterBuilder is a main entry from a fluent programming point of view. We will mostly test using
 * this as a starting point; and break out other test cases on an as needed basis.
 *
 *
 *
 * @source $URL$
 */
public class FilterBuilderTest {
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @Test
    public void add() {
        Add expected = ff.add(ff.literal(1), ff.literal(1));
    }

    @Test
    public void and() {
        PropertyIsNull nn = ff.isNull(ff.property("bar"));
        Equals eq = ff.equal(ff.property("foo"), ff.literal("john"));
        And and = ff.and(nn, eq);
    }

//    @Test
//    public void bbox() {
//        ff.bbox(geometry, bounds);
//    }
//
//    public void between() {
//        ff.between(ff.property("value"), ff.literal(1), ff.literal(9));
//    }
//    public void beyond() {        
//        Geometry poly = ff.beyond(ff.literal(poly), ff.literal(point), 3.0, "km");
//
//    }
}
