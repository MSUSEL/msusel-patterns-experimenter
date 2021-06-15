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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.wms;

// OpenGIS dependencies
import org.opengis.referencing.NoSuchAuthorityCodeException;

// Geotools dependencies
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A code parsed by the {@link AutoCRSFactory} methods.
 * The expected format is {@code AUTO:code,unit,lon0,lat0} where {@code AUTO} is optional.
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
final class Code {
    /**
     * The authority name. Should usually be {@code AUTO}.
     */
    public final String authority;

    /**
     * The code number.
     */
    public final int code;

    /**
     * The central longitude.
     */
    public final double longitude;

    /**
     * The central latitude.
     */
    public final double latitude;

    /**
     * The type of the CRS to be constructed (e.g. {@code GeographicCRS.class}).
     * Used only in case of failure for constructing an error message.
     */
    final Class type;

    /**
     * Parse the code string to retrive the code number and central longitude / latitude.
     * Assumed format is {@code AUTO:code,lon0,lat0} where {@code AUTO} is optional.
     *
     * @param  text The code in the {@code AUTO:code,lon0,lat0} format.
     * @param  The type of the CRS to be constructed (e.g. {@code GeographicCRS.class}).
     *         Used only in case of failure for constructing an error message.
     * @throws NoSuchAuthorityCodeException if the specified code can't be parsed.
     */
    public Code(final String text, final Class type) throws NoSuchAuthorityCodeException {
        String authority = "AUTO";
        int    code      = 0;
        int    unit      = 9001;
        double longitude = Double.NaN;
        double latitude  = Double.NaN;
        
        // there are two syntaxes for the AUTO factory:
        // AUTO:code,unit,longitude,latitude (from WMS 1.1 spec)
        // AUTO:code,longitude,latitude (from early WMS specs)
        // here we try to support both of them
        
        // the AUTO prefix is optional, remove it if necessary (and support also AUTO2)
        String[] parts;
        if(text.startsWith("AUTO"))
            parts = text.replaceAll("AUTO(2)?\\s*:", "").split("\\s*,\\s*");
        else
            parts = text.split("\\s*,\\s*");
        
        // do we have enough components?
        if(parts.length < 3) {
            throw noSuchAuthorityCode(type, text);
        } 
        
        try {
            if(parts.length < 4) {
                // code,lon,lat
                code = Integer.parseInt  (parts[0]);
                longitude = Double.parseDouble(parts[1]); 
                latitude  = Double.parseDouble(parts[2]);  
            } else {
                // code,unit,lon,lat
                code = Integer.parseInt  (parts[0]);
                unit = Integer.parseInt  (parts[1]);
                longitude = Double.parseDouble(parts[2]); 
                latitude  = Double.parseDouble(parts[3]);
            }
        } catch(NumberFormatException exception) {
            // If a number can't be parsed, then this is an invalid authority code.
            NoSuchAuthorityCodeException e = noSuchAuthorityCode(type, text);
            e.initCause(exception);
            throw e;
        }
        
        if (!(longitude>=Longitude.MIN_VALUE && longitude<=Longitude.MAX_VALUE &&
              latitude >= Latitude.MIN_VALUE && latitude <= Latitude.MAX_VALUE))
        {
            // A longitude or latitude is out of range, or was not present
            // (i.e. the field still has a NaN value).
            throw noSuchAuthorityCode(type, text);
        }
        this.authority = authority;
        this.code      = code;
        this.longitude = longitude;
        this.latitude  = latitude;
        this.type      = type;
    }

    /**
     * Creates an exception for an unknow authority code.
     *
     * @param  type  The GeoAPI interface that was to be created.
     * @param  code  The unknow authority code.
     * @return An exception initialized with an error message built from the specified informations.
     */
    private static NoSuchAuthorityCodeException noSuchAuthorityCode(final Class  type,
                                                                    final String code)
    {
        final String authority = "AUTO";
        return new NoSuchAuthorityCodeException(Errors.format(ErrorKeys.NO_SUCH_AUTHORITY_CODE_$3,
                   code, authority, type), authority, code);
    }

    /**
     * Returns a string representation of this code.
     */
    public String toString(){
        return authority + ':' + code + ',' + longitude + ',' + latitude;
    }
}
