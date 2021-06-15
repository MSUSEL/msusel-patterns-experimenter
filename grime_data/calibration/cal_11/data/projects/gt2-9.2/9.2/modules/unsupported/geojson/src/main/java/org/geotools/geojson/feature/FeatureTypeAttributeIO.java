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
package org.geotools.geojson.feature;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.geotools.geojson.GeoJSONUtil;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * 
 *
 * @source $URL$
 */
public class FeatureTypeAttributeIO implements AttributeIO {

    HashMap<String, AttributeIO> ios = new HashMap();
    
    public FeatureTypeAttributeIO(SimpleFeatureType featureType) {
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            AttributeIO io = null;
            if (Date.class.isAssignableFrom(ad.getType().getBinding())) {
                io = new DateAttributeIO();
            }
            else {
                io = new DefaultAttributeIO();
            }
            ios.put(ad.getLocalName(), io);
        }
    }
    
    public String encode(String att, Object value) {
        return ios.get(att).encode(att, value);
        
    }

    public Object parse(String att, String value) {
        return ios.get(att).parse(att, value);
    }
    
    static class DateAttributeIO implements AttributeIO {

        public String encode(String att, Object value) {
            return GeoJSONUtil.DATE_FORMAT.format((Date)value);
        }

        public Object parse(String att, String value) {
            try {
                return GeoJSONUtil.DATE_FORMAT.parse(value);
            } 
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        
    }

}
