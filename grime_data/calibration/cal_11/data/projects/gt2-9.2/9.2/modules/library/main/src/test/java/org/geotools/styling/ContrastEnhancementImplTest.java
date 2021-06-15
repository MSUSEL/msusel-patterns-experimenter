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
package org.geotools.styling;

import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.FilterFactory;
import org.opengis.style.ContrastMethod;
import static org.junit.Assert.*;

/**
 * The ContrastEnhancementImpl UnitTest
 * @author Jared Erickson
 *
 *
 * @source $URL$
 */
public class ContrastEnhancementImplTest extends TestCase {

    private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /**
     * Test of getGammaValue method, of class ContrastEnhancementImpl.
     */
    public void testGetSetGammaValue() {
        System.out.println("getGammaValue");
        ContrastEnhancementImpl contrastEnhancementImpl = new ContrastEnhancementImpl();
        double expected = 1.5;
        contrastEnhancementImpl.setGammaValue(filterFactory.literal(expected));
        double actual = ((Double)((Literal)contrastEnhancementImpl.getGammaValue()).getValue()).doubleValue();
        assertEquals(expected, actual, 0.1);
    }

    /**
     * Test of setMethod method, of class ContrastEnhancementImpl.
     */
    public void testGetSetMethod() {
        System.out.println("setMethod");
        ContrastMethod expected = ContrastMethod.HISTOGRAM;
        ContrastEnhancementImpl contrastEnhancementImpl = new ContrastEnhancementImpl();
        contrastEnhancementImpl.setMethod(expected);
        ContrastMethod actual = contrastEnhancementImpl.getMethod();
        assertEquals(expected, actual);
    }
}
