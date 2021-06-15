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
package org.geotools.geojson;

import static org.geotools.geojson.GeoJSONUtil.DATE_FORMAT;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureJSONExtendedTest extends GeoJSONTestSupport {

    FeatureJSON fjson = new FeatureJSON();
    SimpleFeatureType featureType;
    SimpleFeatureBuilder fb;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setSRS("EPSG:4326");
        tb.add("int", Integer.class);
        tb.add("double", Double.class);
        tb.add("string", String.class);
        tb.add("date", Date.class);
        tb.add("geometry", Geometry.class);
        
        featureType = tb.buildFeatureType();
        fjson.setFeatureType(featureType);
        
        fb = new SimpleFeatureBuilder(featureType);
    }
    
    public void testFeatureWrite() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.writeFeature(feature(1), writer);
        
        assertEquals(strip(featureText(1)), writer.toString());
    }
    
    public void testFeatureRead() throws Exception {
        SimpleFeature f1 = feature(1);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureText(1)))); 
        assertEqualsLax(f1, f2);
    }
    
    public void testFeatureReadMismatched() throws Exception {
        SimpleFeature f1 = feature(1, true);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureText(1, true)))); 
        assertEqualsLax(f1, f2);
    }
    
    public void testFeatureWriteMismatched() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.writeFeature(feature(1, true), writer);
        
        assertEquals(strip(featureText(1, true, false)), writer.toString());
    }
    
    public void testFeatureWriteMismatchedWithNullProperties() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.setEncodeNullValues(true);
        fjson.writeFeature(feature(1, true), writer);

        assertEquals(strip(featureText(1, true, true)), writer.toString());
    }

    SimpleFeature feature(int val) {
        return feature(val, false);
    }
    
    SimpleFeature feature(int val, boolean excludeString) {
        fb.add(val);
        fb.add(val + 0.1);
        
        if (!excludeString) {
            fb.add(toString(val));
        }
        else {
            fb.add(null);
        }
        
        fb.add(toDate(val));
        fb.add(new GeometryFactory().createPoint(new Coordinate(val+0.1,val+0.1)));
        
        return fb.buildFeature("feature." + val);
    }
    
    String featureText(int val) {
        return featureText(val, false, false);
    }

    String featureText(int val, boolean excludeString) {
        return featureText(val, excludeString, false);
    }
    
    String featureText(int val, boolean excludeString, boolean valIsNull) {
        String text = 
            "{" +
            "  'type': 'Feature'," +
            "  'geometry': {" +
            "     'type': 'Point'," +
            "     'coordinates': [" + (val+0.1) + "," + (val+0.1) + "]" +
            "   }, " +
            "'  properties': {" +
            "     'int': " + val + "," +
            "     'double': " + (val + 0.1) + ",";
        
        if (valIsNull) {
            text += "     'string': null,";
        } else if (!excludeString) {
            text += "     'string': '" + toString(val) + "',";
            
        }
        text += 
            "     'date': '" +  toDateString(val) + "'" + 
            "   }," +
            "   'id':'feature." + val + "'" +
            "}";
        
        return text;
    }
    
    String toDateString(int val) {
        return String.format("%s-%s-%sT%s:%s:%s.000+0000", pad(2000+val), pad(val), pad(val), pad(val), 
                pad(val), pad(val));
    }
    
    Date toDate(int val) {
        try {
            return DATE_FORMAT.parse(toDateString(val));
        } 
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    String pad(int val) {
        if (String.valueOf(val).length() < 2) {
            return "0" + val;
        }
        return "" + val;
    }
}
