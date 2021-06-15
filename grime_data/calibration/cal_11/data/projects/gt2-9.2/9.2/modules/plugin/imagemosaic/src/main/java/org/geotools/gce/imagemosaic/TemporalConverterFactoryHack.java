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
package org.geotools.gce.imagemosaic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

/**
 * Converter factory which created converting between temporal types and {@link String}
 * <p>
 * Supported save conversions:
 * <ul>
 * <li>{@link java.util.Date} to {@link String}
 * <li>{@link java.sql.Time} to {@link to {@link String}}
 * <li>{@link java.util.Date} to {@link to {@link String}}
 * <li>{@link java.util.Calendar} to {@link to {@link String}}
 * <li>{@link XMLGregorianCalendar} to {@link to {@link String}}
 * </ul>
 * </p>
 * <p>
 * The hint {@link ConverterFactory#SAFE_CONVERSION} is used to control which conversions will be 
 * applied.
 * </p>
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @since 9.0
 *
 *
 *
 * @source $URL$
 */
class TemporalConverterFactoryHack implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        boolean isSafeOnly = false;
        
        if (hints != null){
            Object safe = hints.get(ConverterFactory.SAFE_CONVERSION);
            if (safe instanceof Boolean && ((Boolean)safe).booleanValue()){
                isSafeOnly = true;
            }
        }
        
        if (Date.class.isAssignableFrom(source)) {
            
            // target is string
            if (String.class.equals(target)) {
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("UTC")); // we DO work only with UTC times  

                return new Converter() {
                    public Object convert(Object source, Class target) throws Exception {
                        if(source instanceof Date){
                            return df.format((Date) source);
                        }
                        return null;
                    }
                };
            }
        }

        // this should handle java.util.Calendar to
        // String
        if (Calendar.class.isAssignableFrom(source)) {

            // target is string
            if (String.class.equals(target)) {
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("UTC")); // we DO work only with UTC times  

                return new Converter() {
                    public Object convert(Object source, Class target) throws Exception {
                        if(source instanceof Calendar){
                            return df.format(((Calendar) source).getTime());
                        }  
                        return null;
                    }
                };
            }
        }

        if (XMLGregorianCalendar.class.isAssignableFrom(source)) {
            // target is string
            if (String.class.equals(target)) {
                final SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                df.setTimeZone(TimeZone.getTimeZone("UTC")); // we DO work only with UTC times  

                return new Converter() {
                    public Object convert(Object source, Class target) throws Exception {
                        if(source instanceof XMLGregorianCalendar){
                            return df.format(((XMLGregorianCalendar) source).toGregorianCalendar(TimeZone.getTimeZone("GMT"),Locale.getDefault(),null).getTime());
                        }
                        return null;
                    }
                };
            }            
        }
        return null;
    }
}
