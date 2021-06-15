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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.util.Collections;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;
import org.opengis.metadata.citation.Citation;


/**
 *
 * @since 2.4
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: PropertyExistsFunctionTest.java 24966 2007-03-30 11:33:47Z
 *          vmpazos $
 *
 * @source $URL$
 */
public class PropertyExistsFunctionTest {
    private static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    PropertyExistsFunction f;

    @Before
    public void setUp() {
        f = new PropertyExistsFunction();
    }

    @After
    public void tearDown() {
        f = null;
    }

    @Test
    public void testName() {
        Assert.assertEquals("propertyexists", f.getName().toLowerCase());
    }

    @Ignore
    public void testFind() {
        Function function = ff.function("propertyexists", ff.property("testPropName"));
        Assert.assertNotNull(function);
    }

    @Test
    public void testEvaluateFeature() throws Exception{
        SimpleFeatureType type = DataUtilities.createType("ns", "name:string,geom:Geometry");
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add("testName");
        build.add(null);
        SimpleFeature feature = build.buildFeature(null);

        f.setParameters(Collections.singletonList(ff.property("nonExistant")));
        Assert.assertEquals(Boolean.FALSE, f.evaluate(feature));

        f.setParameters(Collections.singletonList(ff.property("name")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(feature));

        f.setParameters(Collections.singletonList(ff.property("geom")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(feature));
    }

    @Test
    public void testEvaluatePojo() {
        Citation pojo = new CitationImpl();

        f.setParameters(Collections.singletonList(ff.property("edition")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(pojo));

        f.setParameters(Collections.singletonList(ff.property("alternateTitles")));
        Assert.assertEquals(Boolean.TRUE, f.evaluate(pojo));

        // wrong case (note the first letter)
        f.setParameters(Collections.singletonList(ff.property("AlternateTitles")));
        Assert.assertEquals(Boolean.FALSE, f.evaluate(pojo));

        f.setParameters(Collections.singletonList(ff.property("nonExistentProperty")));
        Assert.assertEquals(Boolean.FALSE, f.evaluate(pojo));
    }

    @Test
    public void testEquals() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyExistsFunction actual = new PropertyExistsFunction();
        f.setParameters(Collections.singletonList(ff.property("testPropName")));
        actual.setParameters(Collections.singletonList(ff
                .property("testPropName")));
        Assert.assertEquals(f, actual);
    }
}
