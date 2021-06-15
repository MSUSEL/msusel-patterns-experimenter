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
package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class implements a {@link CoordinateReferenceSystem CRS} cache.
 * <p>
 * Each {@link CoordinateReferenceSystem CRS} is associated with a 
 * spatial reference ID. The reference to each 
 * cached {@link CoordinateReferenceSystem CRS} is {@link WeakReference weak}, 
 * ensuring that unused CRS can be garbage collected when more memory is needed.  
 * </p>
 * @author kengu
 *
 *
 * @source $URL$
 */
public class CRSCache 
{
    private static final WeakHashMap<String, CoordinateReferenceSystem> 
        cache = new WeakHashMap<String, CoordinateReferenceSystem>();

    public static CoordinateReferenceSystem decode(String srid)
            throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = cache.get(srid);
        if (crs == null) {
            //
            // Decode srid into a CRS instance
            //
            crs = CRS.decode(srid);
            //
            // Add to cache
            //
            cache.put(srid, crs);
        }
        return crs;
    }

    public static CoordinateReferenceSystem decode(EFeatureInfo eInfo, boolean tryDefault)
            throws Exception {
        try {
            // Decode SRID code into CRS instance.
            // Use cache to reduce memory footprint.
            //
            eInfo.crs = decode(eInfo.srid);
    
            // Finished
            //
            return eInfo.crs;
        } catch (Exception e) {
            if (!tryDefault) {
                throw e;
            }
        }
        // Try once more, this time using default SRID code
        //
        eInfo.srid = EFeatureConstants.DEFAULT_SRID;
    
        // Do not try to recover this time, throws a
        // CoreExeception it this also fails.
        //
        return decode(eInfo, false);
    }

}
