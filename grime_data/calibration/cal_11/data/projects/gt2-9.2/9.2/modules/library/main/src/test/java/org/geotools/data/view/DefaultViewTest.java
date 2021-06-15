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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.view;

import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.IllegalFilterException;

import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import junit.framework.TestCase;
import org.geotools.data.Query;

/**
 * 
 *
 * @source $URL$
 */
public class DefaultViewTest extends TestCase {

    String typeName = "type1";
    private MemoryDataStore ds;

    protected void setUp() throws Exception {
        super.setUp();
        SimpleFeatureType ft=DataUtilities.createType(typeName, "geom:Point,name:String,id:int");
        ds=new MemoryDataStore();
        ds.addFeature(createFeatures(ft,1));
        ds.addFeature(createFeatures(ft,2));
        ds.addFeature(createFeatures(ft,3));
        ds.addFeature(createFeatures(ft,4));
    }

    private SimpleFeature createFeatures(SimpleFeatureType ft, int i) throws IllegalAttributeException {
        GeometryFactory fac=new GeometryFactory();
        return SimpleFeatureBuilder.build(ft,new Object[]{
            fac.createPoint(new Coordinate(i,i)),
            "name"+i, 
            new Integer(i)
        }, null);
    }

    public void testGetFeatures() throws Exception {

        SimpleFeatureSource view = getView();
        
        SimpleFeatureIterator features = view.getFeatures().features();
        int count=0;
        while( features.hasNext() ){
            count++;
            features.next();
        }
        
        assertEquals(2, count);
    }

    public void testGetFeaturesQuery() throws Exception {

        SimpleFeatureSource view = getView();
        
        SimpleFeatureIterator features = view.getFeatures(getQuery()).features();
        int count=0;
        while( features.hasNext() ){
            count++;
            features.next();
        }
        
        assertEquals(1, count);
    }
    public void testGetFeaturesFilter() throws Exception {

        SimpleFeatureSource view = getView();
        Filter f = getFilter();
        SimpleFeatureIterator features = view.getFeatures(f).features();
        int count=0;
        while( features.hasNext() ){
            count++;
            features.next();
        }
        
        assertEquals(1, count);
    }
  
    public void testGetCount() throws Exception {
        SimpleFeatureSource view = getView();
        
        Query query = getQuery();
        int count = view.getCount(query);
        assertEquals(1, count);
    }

    private Query getQuery() throws IllegalFilterException {
        Filter f = getFilter();
        Query query = new Query(typeName, f, new String[0]);
        return query;
    }

    private Filter getFilter() throws IllegalFilterException {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Filter f = fac.equals(fac.property("name"), fac.literal("name2"));
        return f;
    }

    private SimpleFeatureSource getView() throws IllegalFilterException, IOException, SchemaException {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Filter f = fac.less(fac.property("id"), fac.literal(3));

        SimpleFeatureSource view = DataUtilities.createView(ds, new Query(typeName, f));
        return view;
    }

}
