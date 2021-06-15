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
package org.geotools.feature.collection;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.sort.SortedFeatureIterator;
import org.geotools.factory.Hints;
import org.opengis.filter.sort.SortBy;

/**
 * A wrapper that will sort a feature collection using a size sensitive algorithm, in main memory
 * for small collections, using secondary memory otherwise. The threshold is defined by the
 * {@link Hints#MAX_MEMORY_SORT} feature count
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class SortedSimpleFeatureCollection extends DecoratingSimpleFeatureCollection {

    private SortBy[] sort;

    public SortedSimpleFeatureCollection(SimpleFeatureCollection delegate, SortBy[] sort) {
        super(delegate);
        this.sort = sort;
    }

    @Override
    public SimpleFeatureIterator features() {
        try {
            SimpleFeatureIterator features = ((SimpleFeatureCollection) delegate).features();
            // sort if necessary
            if (sort != null) {
                features = new SortedFeatureIterator(features, getSchema(), sort, -1);
            }
            return features;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
