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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.filter.temporal;

import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.TemporalPrimitive;

/**
 * 
 *
 * @source $URL$
 */
public class BeforeImplTest extends TemporalFilterTestSupport {

    public void test() throws Exception {
        Instant i1 = instant("2001-07-05T12:08:56.235-0700");
        Instant i2 = instant("2001-07-04T12:08:56.235-0700");
        Instant i3 = instant("2001-07-05T12:08:57.235-0700");
        doAssert(i1, i2, false);
        doAssert(i1, i3, true);
        doAssert(i1, i1, false);
        
        Period p1 = period("2001-07-05T12:08:56.235-0700", "2001-07-05T12:09:56.235-0700");
        Period p2 = period("2001-07-05T12:09:56.235-0700", "2001-07-05T12:10:56.235-0700");
        Period p3 = period("2001-07-05T12:010:56.235-0700", "2001-07-05T12:11:56.235-0700");
        doAssert(p1, p2, false);
        doAssert(p1, p1, false);
        doAssert(p2, p3, false);
        

        Instant i4 = instant("2001-07-04T12:08:56.233-0700");
        Instant i5 = instant("2001-07-04T12:08:56.234-0700");
        Period p4 = period("2001-07-04T12:08:56.234-0700", "2001-07-04T12:08:56.235-0700");
        doAssert(i4, p4, true);
        doAssert(i5, p4, false);
        
        Instant i6 = instant("2001-07-04T12:08:56.234-0700");
        Instant i7 = instant("2001-07-04T12:08:56.236-0700");
        doAssert(p4, i6, false);
        doAssert(p4, i7, true);
    }

    void doAssert(TemporalPrimitive tp1, TemporalPrimitive tp2, boolean b) {
        BeforeImpl a = new BeforeImpl(ff.literal(tp1), ff.literal(tp2));
        assertEquals(b, a.evaluate(null));
    }
}
