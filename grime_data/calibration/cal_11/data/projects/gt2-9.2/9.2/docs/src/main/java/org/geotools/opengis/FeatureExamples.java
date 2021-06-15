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
package org.geotools.opengis;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class FeatureExamples {

public void javaFlag() {
// javaFlag start
class Flag {
    public Point location;
    public String name;
    public int classification;
    public double height;
 };

 GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory();

 Flag here = new Flag();
 here.location = geomFactory.createPoint( new Coordinate(23.3,-37.2) );
 here.name = "Here";
 here.classification = 3;
 here.height = 2.0;
// javaFlag end
}

public void featureFlag() {
// featureFlag start
SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
b.setName( "Flag" );
b.setCRS( DefaultGeographicCRS.WGS84 );
b.add( "location", Point.class );
b.add( "name", String.class );
b.add( "classification", Integer.class );
b.add( "height", Double.class );
SimpleFeatureType type = b.buildFeatureType();

GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory();

SimpleFeatureBuilder f = new SimpleFeatureBuilder( type );
f.add( geomFactory.createPoint( new Coordinate(23.3,-37.2) ) );
f.add("here");
f.add(3);
f.add(2.0);
SimpleFeature feature = f.buildFeature("fid.1");
// featureFlag end
}

}
