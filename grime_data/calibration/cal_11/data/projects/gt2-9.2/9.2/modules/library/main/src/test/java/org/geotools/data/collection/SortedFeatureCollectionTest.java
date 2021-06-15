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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.util.Comparator;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.FeatureCollectionWrapperTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.SortedSimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class SortedFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    public void testNaturalSort() throws Exception {
        SortedSimpleFeatureCollection sorted = new SortedSimpleFeatureCollection(delegate,
                new SortBy[] { SortBy.NATURAL_ORDER });
        checkSorted(sorted, DataUtilities.sortComparator(SortBy.NATURAL_ORDER));
    }

    public void testReverseSort() throws Exception {
        SortedSimpleFeatureCollection sorted = new SortedSimpleFeatureCollection(delegate,
                new SortBy[] { SortBy.REVERSE_ORDER });
        checkSorted(sorted, DataUtilities.sortComparator(SortBy.REVERSE_ORDER));
    }

    public void testSortAttribute() throws Exception {
        SortBy sort = ff.sort("someAtt", SortOrder.ASCENDING);
        SortedSimpleFeatureCollection sorted = new SortedSimpleFeatureCollection(delegate,
                new SortBy[] { sort });
        checkSorted(sorted, DataUtilities.sortComparator(sort));
    }

    private void checkSorted(SortedSimpleFeatureCollection sorted,
            Comparator<SimpleFeature> comparator) {
        SimpleFeatureIterator fi = sorted.features();
        SimpleFeature prev = null;
        while (fi.hasNext()) {
            SimpleFeature curr = fi.next();
            if (prev != null) {
                assertTrue("Failed on " + prev + " / " + curr, comparator.compare(prev, curr) <= 0);
            }
            prev = curr;
        }
        fi.close();
    }

}
