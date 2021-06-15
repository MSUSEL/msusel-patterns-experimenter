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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.spatial;

import java.util.Map;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * PointCoveredByPolygonBoundaryValidation purpose.
 * 
 * <p>
 * Checks to ensure the Point is covered by the Polygon Boundary.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class PointCoveredByPolygonBoundaryValidation
    extends PointPolygonAbstractValidation {
    /**
     * PointCoveredByPolygonBoundaryValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public PointCoveredByPolygonBoundaryValidation() {
        super();
    }

    /**
     * Ensure Point is covered by the Polygon Boundary.
     * 
     * <p></p>
     *
     * @param layers a HashMap of key="TypeName" value="FeatureSource"
     * @param envelope The bounding box of modified features
     * @param results Storage for the error and warning messages
     *
     * @return True if no features intersect. If they do then the validation
     *         failed.
     *
     * @throws Exception DOCUMENT ME!
     *
     * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map,
     *      com.vividsolutions.jts.geom.Envelope,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(Map layers, Envelope envelope,
        ValidationResults results) throws Exception {
        SimpleFeatureSource pointSource = (SimpleFeatureSource) layers.get(getPointTypeRef());
        SimpleFeatureSource polySource = (SimpleFeatureSource) layers.get(getRestrictedPolygonTypeRef());

        Object[] polys = polySource.getFeatures().toArray();
        Object[] points = pointSource.getFeatures().toArray();

        if (!envelope.contains(polySource.getBounds())) {
            results.error((SimpleFeature) polys[0],
                "Point Feature Source is not contained within the Envelope provided.");

            return false;
        }

        if (!envelope.contains(pointSource.getBounds())) {
            results.error((SimpleFeature) points[0],
                "Line Feature Source is not contained within the Envelope provided.");

            return false;
        }

        for (int i = 0; i < points.length; i++) {
            SimpleFeature tmp = (SimpleFeature) points[i];
            Geometry gt = (Geometry) tmp.getDefaultGeometry();

            if (gt instanceof Polygon) {
            	Polygon ls = (Polygon) gt;

                boolean r = false;
                for (int j = 0; j < polys.length && !r; j++) {
                    SimpleFeature tmp2 = (SimpleFeature) polys[j];
                    Geometry gt2 = (Geometry) tmp2.getDefaultGeometry();

                    if (gt2 instanceof Point) {
                    	Point pt = (Point) gt2;
                        if(!ls.getBoundary().contains(pt)){
                        	r = true;
                        }
                    }
                }
                if(!r){
                    results.error(tmp, "Polygon does not contained one of the specified points.");
                	return false;
                }
            }
        }

        return true;
    }
}
