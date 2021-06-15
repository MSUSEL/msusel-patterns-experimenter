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
package org.geotools.xml;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.geotools.factory.Hints;
import org.geotools.util.CommonsConverterFactory;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.xml.impl.DatatypeConverterImpl;


/**
 * A ConverterFactory which can convert strings using {@link javax.xml.datatype.DatatypeFactory}.
 * <p>
 * Supported converstions:
 * <ul>
 *         <li>String to {@link java.util.Date}
 *         <li>String to {@link java.util.Calendar}
 *         <li>{@link java.util.Date} to String
 *         <li>{@link java.util.Calendar} to String
 * </ul>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class XmlConverterFactory implements ConverterFactory {
    
    public Converter createConverter(Class source, Class target, Hints hints) {
        // make sure either source or target is String in order not to step over
        // TemporalConverterFactory
        if (String.class.equals(target)) {
            if (java.util.Date.class.isAssignableFrom(source)
                    || Calendar.class.isAssignableFrom(source)) {
                return new XmlConverter();
            }
        } else if (String.class.equals(source)) {
            if (java.util.Date.class.isAssignableFrom(target)
                    || Calendar.class.isAssignableFrom(target)) {
                return new XmlConverter();
            }
        }
        return null;
    }

    static class XmlConverter implements Converter {
        public Object convert(Object source, Class target)
            throws Exception {
            if (String.class.equals(target)) {
                return convertToString(source);
            }
            return convertFromString((String) source, target);
        }
        
        private Object convertFromString(final String source, final Class<?> target) {

            // don't bother performing conversions if the target types are not dates/times
            if(!Calendar.class.equals(target) && !Date.class.isAssignableFrom(target))
                return null;

            //JD: this is a bit of a hack but delegate to the 
            // commons converter in case we are executing first.
            try {
                Converter converter = new CommonsConverterFactory().createConverter(String.class,
                        target, null);
    
                if (converter != null) {
                    Object converted = null;
    
                    try {
                        converted = converter.convert(source, target);
                    } catch (Exception e) {
                        //ignore
                    }
    
                    if (converted != null) {
                        return converted;
                    }
                }
            }
            catch(Exception e) {
                //fall through to jaxb parsing
            }
            
            Calendar date;

            //try parsing as dateTime
            try {
                try {
                    date = DatatypeConverterImpl.getInstance().parseDateTime(source);
                }
                catch(Exception e) {
                    //try as just date
                    date = DatatypeConverterImpl.getInstance().parseDate(source);    
                }
                
            } catch (Exception e) {
                //try as just time
                date = DatatypeConverterImpl.getInstance().parseTime(source);
            }

            if (Calendar.class.equals(target)) {
                return date;
            }

            if (Date.class.isAssignableFrom(target)) {
                Date time = date.getTime();

                //check for subclasses
                if (java.sql.Date.class.equals(target)) {
                    return new java.sql.Date(time.getTime());
                }

                if (Time.class.equals(target)) {
                    return new Time(time.getTime());
                }

                if (Timestamp.class.equals(target)) {
                    return new Timestamp(time.getTime());
                }

                return time;
            }

            return null;
        }

        private String convertToString(Object unconvertedValue) {
            String textValue = null;

            if (unconvertedValue instanceof Calendar) {

                Calendar cal = (Calendar) unconvertedValue;
                textValue = DatatypeConverterImpl.getInstance().printDateTime(cal);

            } else if (unconvertedValue instanceof java.util.Date) {

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                cal.setTimeInMillis(((java.util.Date) unconvertedValue).getTime());
                DatatypeConverterImpl converter = DatatypeConverterImpl.getInstance();

                if (unconvertedValue instanceof java.sql.Date) {
                    textValue = converter.printDate(cal);
                } else if (unconvertedValue instanceof java.sql.Time) {
                    textValue = converter.printTime(cal);
                } else {
                    // java.util.Date and java.sql.TimeStamp
                    textValue = converter.printDateTime(cal);
                }
            }

            return textValue;
        }
    }

}
