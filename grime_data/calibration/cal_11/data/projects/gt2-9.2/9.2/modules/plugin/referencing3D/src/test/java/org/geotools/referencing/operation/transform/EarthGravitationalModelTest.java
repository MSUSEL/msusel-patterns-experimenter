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
package org.geotools.referencing.operation.transform;

// JUnit dependencies
import junit.framework.TestCase;

// OpenGIS dependencies
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;

// Geotools dependencies
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.geometry.GeneralDirectPosition;


/**
 * Tests the {@link EarthGravitationalModel} class.
 *
 * @since 2.3
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class EarthGravitationalModelTest extends TestCase {
    /**
     * Tests the {@link EarthGravitationalModel#heightOffset} method for WGS 84.
     */
    public void testHeightOffsetWGS84() throws Exception {
        final EarthGravitationalModel gh = new EarthGravitationalModel();
        gh.load("EGM180.nor");
        assertEquals( 1.505, gh.heightOffset(45, 45,    0), 0.001);
        assertEquals( 1.515, gh.heightOffset(45, 45, 1000), 0.001);
        assertEquals(46.908, gh.heightOffset( 0, 45,    0), 0.001);
    }

    /**
     * Tests the {@link EarthGravitationalModel#heightOffset} method for WGS 72.
     */
    public void testHeightOffsetWGS72() throws Exception {
        final EarthGravitationalModel gh = new EarthGravitationalModel(180, false);
        gh.load("EGM180.nor");
        assertEquals( 1.475, gh.heightOffset(45, 45,    0), 0.001);
        assertEquals(46.879, gh.heightOffset( 0, 45,    0), 0.001);
        assertEquals(23.324, gh.heightOffset( 3, 10,   10), 0.001);
        assertEquals( 0.380, gh.heightOffset(75,-30,    0), 0.001);
    }

    /**
     * Tests the creation of the math transform from the factory.
     */
    public void testMathTransform() throws FactoryException, TransformException {
        final MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        final ParameterValueGroup p = mtFactory.getDefaultParameters("Earth gravitational model");
        final MathTransform mt = mtFactory.createParameterizedTransform(p);
        DirectPosition pos = new GeneralDirectPosition(new double[] {45, 45, 1000});
        pos = mt.transform(pos, pos);
        assertEquals(  45.000, pos.getOrdinate(0), 0.001);
        assertEquals(  45.000, pos.getOrdinate(1), 0.001);
        assertEquals(1001.515, pos.getOrdinate(2), 0.001);
    }
}
