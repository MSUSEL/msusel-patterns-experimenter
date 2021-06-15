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
package org.geotools.kml.v22;

import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.simple.SimpleFeatureType;

public class SchemaRegistry {

    private Map<String, SimpleFeatureType> featureTypes;

    public SchemaRegistry() {
        this.featureTypes = new HashMap<String, SimpleFeatureType>();
    }

    public void add(String featureTypeName, SimpleFeatureType featureType) {
        featureTypes.put(featureTypeName, featureType);
    }

    public SimpleFeatureType get(String featureTypeName) {
        return featureTypes.get(featureTypeName);
    }

    public SimpleFeatureType get(SimpleFeatureType featureType) {
        return get(featureType.getName().getLocalPart());
    }

}
