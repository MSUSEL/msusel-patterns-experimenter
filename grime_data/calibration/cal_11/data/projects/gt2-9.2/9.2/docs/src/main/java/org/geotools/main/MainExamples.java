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
package org.geotools.main;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Main examples used for sphinx documentation.-+*
 * 
 * @author Jody Garnett
 */
public class MainExamples {

void exampleDataUtilities() throws Exception {
    FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;
    
    // exampleDataUtilities start
    SimpleFeatureCollection features = DataUtilities.simple(collection);
    // exampleDataUtilities end
}

// exampleRetype start
void exampleRetype() throws Exception {
    SimpleFeatureType origional = DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");
    SimpleFeatureType modified = DataUtilities.createSubType(origional, new String[]{"centerline"});
    
    SimpleFeature feature = DataUtilities.template( origional );
    
    SimpleFeature changed = DataUtilities.reType( modified, feature);
}
// exampleRetype end

void exampleIterator() throws Exception {
    SimpleFeatureCollection featureCollection = null;
    // exampleIterator start
    SimpleFeatureIterator iterator = featureCollection.features();
    try {
        while( iterator.hasNext() ){
            SimpleFeature feature = iterator.next();
            // process feature
        }
    }
    finally {
        iterator.close();
    }
    // exampleIterator end
}
}
