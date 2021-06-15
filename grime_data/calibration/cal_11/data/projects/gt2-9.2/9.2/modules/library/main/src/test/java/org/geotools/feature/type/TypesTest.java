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
package org.geotools.feature.type;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

/**
 * 
 *
 * @source $URL$
 */
public class TypesTest extends TestCase {
    public void testWithoutRestriction(){
        // used to prevent warning
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

        String attributeName = "string";
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); //$NON-NLS-1$
        builder.setName("test");
        builder.add(attributeName, String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();
        
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[]{"Value"},
                null);
        
        assertNotNull( feature );        
    }
    /**
     * This utility class is used by Types to prevent attribute modification.
     */
    public void testRestrictionCheck() {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

        String attributeName = "string";
        PropertyIsEqualTo filter = fac.equals(fac.property("."), fac
                .literal("Value"));

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(); //$NON-NLS-1$
        builder.setName("test");
        builder.restriction(filter).add(attributeName, String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();
        
        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, new Object[]{"Value"},
                null);
        
        assertNotNull( feature );

    }
    
    public void testAssertNamedAssignable(){
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Test");
        builder.add("name", String.class );
        builder.add("age", Double.class );
        SimpleFeatureType test = builder.buildFeatureType();
        
        builder.setName("Test");
        builder.add("age", Double.class );
        builder.add("name",String.class);
        SimpleFeatureType test2 = builder.buildFeatureType();
        
        builder.setName("Test");
        builder.add("name",String.class);
        SimpleFeatureType test3 = builder.buildFeatureType();
     
        builder.setName("Test");
        builder.add("name",String.class);
        builder.add("distance", Double.class );
        SimpleFeatureType test4 = builder.buildFeatureType();
     
        Types.assertNameAssignable( test, test );        
        Types.assertNameAssignable( test, test2 );
        Types.assertNameAssignable( test2, test );        
        try {
            Types.assertNameAssignable( test, test3 );    
            fail("Expected assertNameAssignable to fail as age is not covered");
        }
        catch ( IllegalArgumentException expected ){            
        }
        
        Types.assertOrderAssignable( test, test4 );
    }
    
}
