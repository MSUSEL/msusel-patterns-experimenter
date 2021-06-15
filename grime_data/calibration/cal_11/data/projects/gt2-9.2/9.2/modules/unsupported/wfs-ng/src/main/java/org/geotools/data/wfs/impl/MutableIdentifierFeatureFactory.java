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
package org.geotools.data.wfs.impl;

import java.util.Collection;

import org.geotools.feature.AbstractFeatureFactoryImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * If only {@link AbstractFeatureFactoryImpl}'s filter factory were setteable this class wouldn't be
 * needed....
 */
class MutableIdentifierFeatureFactory extends AbstractFeatureFactoryImpl {

    private static FilterFactory2 MUTABLE_FIDS_FILTER_FACTORY = new FilterFactoryImpl() {
        @Override
        public MutableFeatureId featureId(String id) {
            return new MutableFeatureId(id);
        }

        @Override
        public MutableFeatureId featureId(String fid, String featureVersion) {
            return new MutableFeatureId(fid, featureVersion);
        }

    };

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Feature createFeature(Collection value, AttributeDescriptor descriptor, String id) {
        return new FeatureImpl(value, descriptor, MUTABLE_FIDS_FILTER_FACTORY.featureId(id));
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Feature createFeature(Collection value, FeatureType type, String id) {
        return new FeatureImpl(value, type, MUTABLE_FIDS_FILTER_FACTORY.featureId(id));
    }

    @Override
    public SimpleFeature createSimpleFeature(Object[] array, SimpleFeatureType type, String id) {
        if (type.isAbstract()) {
            throw new IllegalArgumentException(
                    "Cannot create an feature of an abstract FeatureType " + type.getTypeName());
        }
        return new SimpleFeatureImpl(array, type, MUTABLE_FIDS_FILTER_FACTORY.featureId(id), false);
    }
}
