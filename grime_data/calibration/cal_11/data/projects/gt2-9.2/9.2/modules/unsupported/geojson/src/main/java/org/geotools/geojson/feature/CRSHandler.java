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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.feature;

import java.io.IOException;

import org.geotools.geojson.HandlerBase;
import org.geotools.geojson.IContentHandler;
import org.geotools.referencing.CRS;
import org.json.simple.parser.ParseException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 *
 * @source $URL$
 */
public class CRSHandler extends HandlerBase implements IContentHandler<CoordinateReferenceSystem> {

    CoordinateReferenceSystem crs;
    int state = 0;
    
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("properties".equals(key)) {
            state = 1;
        }
        else if (("name".equals(key) || "code".equals(key)) && state == 1) {
            state = 2;
        }
        return true;
    }
    
    public boolean primitive(Object value) throws ParseException, IOException {
        if (state == 2) {
            try {
                try {
                    crs = CRS.decode(value.toString());
                }
                catch(NoSuchAuthorityCodeException e) {
                    //try pending on EPSG
                    try {
                        crs = CRS.decode("EPSG:" + value.toString());
                    }
                    catch(Exception e1) {
                        //throw the original
                        throw e;
                    }
                }
            }
            catch(Exception e) {
                throw (IOException) new IOException("Error parsing " + value + " as crs id").initCause(e);
            }
            state = -1;
        }
        
        return true;
    }

    public CoordinateReferenceSystem getValue() {
        return crs;
    }

}
