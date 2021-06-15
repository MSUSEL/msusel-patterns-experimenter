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
package org.geotools.data.efeature.util;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;
import org.geotools.util.logging.Logging;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

/**
 * @author kengu
 *
 *
 * @source $URL$
 */
public class EFeatureXMLHelper extends XMLHelperImpl {
    
    /**
     * Cached {@link Logger} for this class
     */
    protected static final Logger LOGGER = Logging.getLogger(EFeatureXMLHelper.class);

    @Override
    public String convertToString(EFactory factory, EDataType dataType, Object value) {
        //
        // Only serialize Geometry instances
        //
        if(value instanceof Geometry) {
            //
            // Convert geometry to byte array
            //
            byte[] b = DataBuilder.toWKB((Geometry)value);
            //
            // Convert to compressed string
            //
            return toString(b);
            //return DataBuilder.toWKT((Geometry)value);
        }
        //
        // Forward to default implementation
        //
        return super.convertToString(factory, dataType, value);
    }

    @Override
    protected Object createFromString(EFactory eFactory, EDataType eDataType, String value) {
        //
        // Get data type
        //
        Class<?> type = eDataType.getInstanceClass();
        //
        // Only serialize Geometry instances
        //
        if(DataTypes.isGeometry(type)) {
            //
            // Convert hex string to WKB formated byte array
            //
            byte[] b = toByte(value);
            try {
                
                //
                // Convert to Geometry
                //
                return DataBuilder.toGeometry(b);
                
            } catch (ParseException e) {
                //
                // Notify
                //
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                //
                // Try to create an empty geometry of given type
                //
                try {
                    return DataBuilder.toEmptyGeometry(type);
                } catch (ParseException e1) {
                    //
                    // Notify again
                    //
                    LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
                    //
                    // This time, nothing can be done, just return null.
                    //
                    return null;
                }
            }
        }
        //
        // Forward to default implementation
        //
        return super.createFromString(eFactory, eDataType, value);
    }
    
    private static final String toString(byte[] b) {
        //
        // Create a BigInteger using the byte array
        //
        BigInteger bi = new BigInteger(b);
        //
        // Return as hex string
        //
        return bi.toString(32);
    }
    
    private static final byte[] toByte(String s) {
        //
        // Create a BigInteger using the byte array
        //
        BigInteger bi = new BigInteger(s,32);
        //
        // Return as byte array
        //
        return bi.toByteArray();
    }
    

}
