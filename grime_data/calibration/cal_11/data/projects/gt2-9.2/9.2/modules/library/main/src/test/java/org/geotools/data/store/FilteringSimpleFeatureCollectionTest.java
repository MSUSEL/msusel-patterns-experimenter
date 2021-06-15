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
package org.geotools.data.store;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class FilteringSimpleFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testCount() {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        assertEquals(1, collection.size());
    }
    
    public void testVisitor() throws IOException {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        collection.accepts(new FeatureVisitor() {
            
            public void visit(Feature feature) {
                assertEquals(1, feature.getProperty("someAtt").getValue());
                
            }
        }, null);
    }
}
