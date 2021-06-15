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
package org.geotools.temporal;

import java.util.Date;

import org.geotools.factory.Hints;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.opengis.temporal.Instant;
import org.opengis.temporal.TemporalObject;

/**
 * Factory that converts String and {@link java.util.Date} objects to instances of 
 * {@link TemporalObject}.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL$
 */
public class TemporalConverterFactory implements ConverterFactory {

    static Converter dateToInstant = new Converter() {
        public <T> T convert(Object source, Class<T> target) throws Exception {
            return (T) new DefaultInstant(new DefaultPosition((Date)source));
        }
    };
    
    static Converter stringToInstant = new Converter() {

        public <T> T convert(Object source, Class<T> target) throws Exception {
            //first go to java.util.Date
            Date d = Converters.convert(source, Date.class);
            
            //then go from date to instant
            return d != null ? dateToInstant.convert(d, target) : null;
        }
        
    };
    
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if (Instant.class.isAssignableFrom(target)) {
            if (Date.class.isAssignableFrom(source)) {
                return dateToInstant;
            }
            
            if (String.class.equals(source)) {
                return stringToInstant;
            }
        }
        
        return null;
    }

    
    
}
