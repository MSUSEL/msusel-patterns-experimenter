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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.Date;

import junit.framework.TestCase;

import org.opengis.filter.capability.FunctionName;
import org.opengis.parameter.Parameter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * 
 *
 * @source $URL$
 */
public class FunctionImplTest extends TestCase {

    public void testFunctionName() throws Exception {
        FunctionName fn = FunctionImpl.functionName("foo", "bar:Integer", "a", "x:String:1,1", 
            "y:MultiPolygon", "z:java.util.Date:1,");
        
        assertEquals("foo", fn.getName());
        check(fn.getReturn(), "bar", Integer.class, 1, 1);
        check(fn.getArguments().get(0), "a", Object.class, 1, 1);
        check(fn.getArguments().get(1), "x", String.class, 1, 1);
        check(fn.getArguments().get(2), "y", MultiPolygon.class, 1, 1);
        check(fn.getArguments().get(3), "z", Date.class, 1, -1);
        
        fn = FunctionImpl.functionName("foo", "a", "geom::1,1", "b:Object:,");
        check(fn.getArguments().get(0), "geom", Geometry.class, 1, 1);
        check(fn.getArguments().get(1), "b", Object.class, -1, -1);
        
        fn = FunctionImpl.functionName("foo", "value", "geom::,");
        check(fn.getArguments().get(0), "geom", Geometry.class, -1, -1);
    }
    
    void check(Parameter p, String name, Class type, int min, int max) {
        assertEquals(name, p.getName());
        assertEquals(type, p.getType());
        assertEquals(min, p.getMinOccurs());
        assertEquals(max, p.getMaxOccurs());
    }
}
