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
package org.geotools.process.vector;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

@DescribeProcess(title = "Feature from Geometry", description = "Converts a geometry into a feature collection.")
/**
 * 
 *
 * @source $URL$
 */
public class FeatureProcess implements VectorProcess {

    @DescribeResult(name = "result", description = "Output feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "geometry", description = "Input geometry", min = 1) Geometry geometry,
            @DescribeParameter(name = "crs", description = "Coordinate reference system of the input geometry (if not provided in the geometry)") CoordinateReferenceSystem crs,
            @DescribeParameter(name = "typeName", description = "Feauturetype name for the feature collection", min = 1) String name) {
        // get the crs
        if (crs == null) {
            try {
                crs = (CoordinateReferenceSystem) geometry.getUserData();
            } catch (Exception e) {
                // may not have a CRS attached
            }
        }
        if (crs == null && geometry.getSRID() > 0) {
            try {
                crs = CRS.decode("EPSG:" + geometry.getSRID());
            } catch (Exception e) {
                // may not have a CRS attached
            }
        }

        // build the feature type
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(name);
        tb.add("geom", geometry.getClass(), crs);
        SimpleFeatureType schema = tb.buildFeatureType();

        // build the feature
        SimpleFeature sf = SimpleFeatureBuilder.build(schema, new Object[] { geometry }, null);
        ListFeatureCollection result = new ListFeatureCollection(schema);
        result.add(sf);
        return result;
    }

}
