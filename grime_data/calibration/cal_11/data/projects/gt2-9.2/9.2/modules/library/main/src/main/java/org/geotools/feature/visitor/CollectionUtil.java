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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;


/**
 * DOCUMENT ME!
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 *
 *
 * @source $URL$
 */
public class CollectionUtil {
    /**
     * Navigate the collection and call vistor.visit( Feature ) for each
     * element in the collection.
     *
     * @param collection the SimpleFeatureCollection containing the features we want to visit
     * @param visitor the visitor which already knows which attributes it wants to meet
     */
    static void accept(SimpleFeatureCollection collection, FeatureVisitor visitor) {
        SimpleFeatureIterator iterator = collection.features();
        try {
            while( iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                visitor.visit(feature);
            }
        }
        finally {
            iterator.close();
        }
    }

    static void accept(SimpleFeatureCollection collection, FeatureVisitor[] visitors) {
        SimpleFeatureIterator iterator = collection.features();
        try {
            while( iterator.hasNext()) {
            	SimpleFeature feature = (SimpleFeature) iterator.next();
    
                for (int i = 0; i < visitors.length; i++) {
                    FeatureVisitor visitor = visitors[i];
                    visitor.visit(feature);
                }
            }
        } finally {
            iterator.close();
        }
    }

    public static Object calc(SimpleFeatureCollection collection,
        FeatureCalc calculator) {
        accept(collection, calculator);

        return calculator.getResult();
    }
}
