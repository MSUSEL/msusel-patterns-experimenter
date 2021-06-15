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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.feature;

import java.util.ArrayList;
import java.util.List;

import org.opengis.feature.Attribute;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
/**
 * A build that can be used as a replacement for SimpleFeatureBuilder in order
 * to avoid validation.
 * <p>
 * The normal SimpleFeatureBuilder performs validation (rather than leaving that up
 * to the factory implementation). 
 * <p>
 * @author Jody Garnett
 *
 *
 *
 *
 * @source $URL$
 */
public class LenientBuilder {
    private SimpleFeatureType schema;
    private FeatureFactory factory;
    private Object[] properties;
    
    public LenientBuilder(SimpleFeatureType schmea ){
        this.schema = schmea;
        this.factory = new LenientFeatureFactory();
        reset();
    }
    
    public static SimpleFeature build( SimpleFeatureType ft, Object atts[], String fid ){
        LenientFeatureFactory featureFactory = new LenientFeatureFactory();
        List<Attribute> properties = new ArrayList<Attribute>();
        for( int i=0; i<atts.length;i++){
            Object value = atts[i];
            Attribute property = featureFactory.createAttribute(value, ft.getDescriptor(i), null);
            properties.add(property);            
        }
        return featureFactory.createSimpleFeature(properties, ft, fid );
    }

    /** You can inject another Factory; this builder will still not do validation */
    public void setFeatureFactory(FeatureFactory featureFactory) {
        factory = featureFactory;
    }

    public void addAll(Object[] values) {
        System.arraycopy(values, 0, properties, 0, properties.length);
    }

    public SimpleFeature buildFeature(String fid) {
        return factory.createSimpleFeature( properties, schema, fid );
    }

    public void reset(){
        properties = new Object[schema.getAttributeCount()];
    }

    public static SimpleFeature copy(SimpleFeature f) {
        if( f == null ) return null;
        
        LenientBuilder builder = new LenientBuilder(f.getFeatureType());        
        builder.addAll( f.getAttributes().toArray() );
        return builder.buildFeature(f.getID());
    }
}
