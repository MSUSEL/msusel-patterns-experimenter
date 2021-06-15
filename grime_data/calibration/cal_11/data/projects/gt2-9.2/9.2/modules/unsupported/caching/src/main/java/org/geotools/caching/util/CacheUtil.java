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
package org.geotools.caching.util;

import org.geotools.caching.spatialindex.Region;
import org.geotools.filter.spatial.BBOXImpl;

import com.vividsolutions.jts.geom.Envelope;

/**
 * 
 *
 * @source $URL$
 */
public class CacheUtil {
	/**
	 * Extracts an envelope from a bbox filter.
	 * 
	 * @param filter
	 * @return
	 */
	public static Envelope extractEnvelope(BBOXImpl filter) {
        return new Envelope(filter.getMinX(), filter.getMaxX(), filter.getMinY(), filter.getMaxY());
    }
	
    /**
     * Converts and envelope to a region.
     *
     * @param e
     * @return null if e is null; otherwise a region
     */
    public static Region convert(Envelope e) {
        if (e == null) return null;
        return new Region(new double[] { e.getMinX(), e.getMinY() },
            new double[] { e.getMaxX(), e.getMaxY() });
    }

    /**
     * Converts a region to an envelope.
     *
     * @param r
     * @return
     */
    public static Envelope convert(Region r) {
        return new Envelope(r.getLow(0), r.getHigh(0), r.getLow(1), r.getHigh(1));
    }
}
