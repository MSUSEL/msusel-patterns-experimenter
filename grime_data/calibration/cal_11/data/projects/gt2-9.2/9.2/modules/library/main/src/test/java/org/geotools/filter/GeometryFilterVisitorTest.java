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

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.GeometryTransformationVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

/**
 * 
 *
 * @source $URL$
 */
public class GeometryFilterVisitorTest extends TestCase {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testSimpleBuffer() {
        org.opengis.filter.expression.Expression geomTx = ff.function("buffer", ff.property("the_geom"), ff.literal(2));
        
        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);
        
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);
        
        ReferencedEnvelope expected = new ReferencedEnvelope(-2, 4, -2, 4, null);
        assertEquals(expected, result);
    }
    
    public void testChainBuffer() {
        // check buffer chaining
        Function innerBuffer = ff.function("buffer", ff.property("the_geom"), ff.literal(3));
        Function geomTx = ff.function("buffer", innerBuffer, ff.literal(2));
        
        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);
        
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);
        
        ReferencedEnvelope expected = new ReferencedEnvelope(-5, 7, -5, 7, null);
        assertEquals(expected, result);
    }
    
    public void testChainIntersection() {
        Function innerBuffer1 = ff.function("buffer", ff.property("the_geom"), ff.literal(3));
        Function innerBuffer2 = ff.function("buffer", ff.property("other_geom"), ff.literal(2));
        Function geomTx = ff.function("intersection", innerBuffer1, innerBuffer2);
        
        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);
        
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);
        
        ReferencedEnvelope expected = new ReferencedEnvelope(-3, 5, -3, 5, null);
        assertEquals(expected, result);
        
    }
}
