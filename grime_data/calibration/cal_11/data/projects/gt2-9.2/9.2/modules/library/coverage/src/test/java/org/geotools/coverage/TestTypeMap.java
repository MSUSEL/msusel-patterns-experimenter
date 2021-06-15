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
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;

import org.junit.Test;
import org.opengis.coverage.SampleDimensionType;

/**
 * Testing {@link TypeMap} class.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 * @source $URL$
 */
public class TestTypeMap {

    @Test
    public void testGetSampleDimensionTypeSampleModel() {
        
        final SampleModel sm=new BufferedImage(512, 512, BufferedImage.TYPE_USHORT_555_RGB).getSampleModel();
        assertSame(SampleDimensionType.UNSIGNED_8BITS, TypeMap.getSampleDimensionType(
                sm, 0));
        assertSame(SampleDimensionType.UNSIGNED_8BITS, TypeMap.getSampleDimensionType(
                sm, 1));
        assertSame(SampleDimensionType.UNSIGNED_8BITS, TypeMap.getSampleDimensionType(
                sm, 2));
    }
   

    @Test
    public void testIsSigned() {
        assertTrue(TypeMap.isSigned(SampleDimensionType.REAL_32BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.REAL_64BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.SIGNED_16BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.SIGNED_32BITS));
        assertTrue(TypeMap.isSigned(SampleDimensionType.SIGNED_8BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_16BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_1BIT));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_2BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_32BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_4BITS));
        assertFalse(TypeMap.isSigned(SampleDimensionType.UNSIGNED_8BITS));
        
    }
   
}
