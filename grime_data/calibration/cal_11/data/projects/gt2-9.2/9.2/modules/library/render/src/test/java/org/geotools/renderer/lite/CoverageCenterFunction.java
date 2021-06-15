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
package org.geotools.renderer.lite;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.capability.FunctionName;
import org.opengis.geometry.Envelope;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * A test rendering transformation that returns the center of the provided coverage
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class CoverageCenterFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("CoverageCenter", parameter("coverage",
            GridCoverage2D.class));

    public CoverageCenterFunction() {
        super(NAME);
    }

    public Object evaluate(Object gc) {
        GridCoverage2D coverage = (GridCoverage2D) gc;

        Envelope env = coverage.getEnvelope();
        GeometryFactory gf = new GeometryFactory();
        Point center = gf.createPoint(new Coordinate(env.getMedian(0), env.getMedian(1)));

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("center");
        tb.add("geom", Point.class, coverage.getCoordinateReferenceSystem2D());
        SimpleFeatureType ft = tb.buildFeatureType();

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        fb.add(center);
        SimpleFeature f = fb.buildFeature(null);
        
        return DataUtilities.collection(f);
    }
}
