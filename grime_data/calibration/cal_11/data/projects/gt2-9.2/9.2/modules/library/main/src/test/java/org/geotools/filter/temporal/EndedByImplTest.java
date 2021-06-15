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
public class EndedByImplTest extends TemporalFilterTestSupport {

    public void test() throws Exception {
        Instant i1 = instant("2001-07-04T12:08:00.000-0700");
        Instant i2 = instant("2001-07-05T12:08:00.000-0700");
        doAssert(i1, i2, false);
        doAssert(i2, i1, false);
        doAssert(i1, i1, false);
        
        Period p1 = period("2001-07-04T12:07:00.000-0700", "2001-07-05T12:08:00.000-0700");
        doAssert(p1, i2, true);
        doAssert(i2, p1, false);

        doAssert(p1, p1, false);

        Period p2 = period("2001-07-04T12:07:01.000-0700", "2001-07-05T12:08:00.000-0700");
        Period p3 = period("2001-07-04T12:07:00.000-0700", "2001-07-05T12:07:59.000-0700");
        Period p4 = period("2001-07-04T12:06:59.000-0700", "2001-07-05T12:08:00.000-0700");
        doAssert(p2, p1, false);
        doAssert(p3, p1, false);
        doAssert(p4, p1, true);
    }

    void doAssert(TemporalPrimitive tp1, TemporalPrimitive tp2, boolean b) {
        EndedByImpl a = new EndedByImpl(ff.literal(tp1), ff.literal(tp2));
        assertEquals(b, a.evaluate(null));
    }
}
