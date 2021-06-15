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
package org.geotools.data.wfs.internal;

import java.io.IOException;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

public class GetFeatureResponse extends WFSResponse {

    private final GetFeatureParser features;

    private boolean featuresReturned;

    public GetFeatureResponse(WFSRequest originatingRequest, HTTPResponse httpResponse,
            GetFeatureParser features) throws ServiceException, IOException {

        super(originatingRequest, httpResponse);
        this.features = features;

    }

    public GetFeatureParser getFeatures() {
        return features;
    }

    public GetFeatureParser getFeatures(GeometryFactory geometryFactory) {
        if (featuresReturned) {
            throw new IllegalStateException("getFeatures can be called only once");
        }
        GetFeatureParser features = getFeatures();
        if (geometryFactory != null) {
            features.setGeometryFactory(geometryFactory);
        }
        featuresReturned = true;
        return features;
    }

    public GetFeatureParser getSimpleFeatures(GeometryFactory geometryFactory) {
        GetFeatureParser rawFeatures = getFeatures(geometryFactory);
        FeatureType featureType = rawFeatures.getFeatureType();
        if (featureType instanceof SimpleFeatureType) {
            return rawFeatures;
        }

        throw new UnsupportedOperationException("implementa adapting to SimpleFeature");
    }

}
