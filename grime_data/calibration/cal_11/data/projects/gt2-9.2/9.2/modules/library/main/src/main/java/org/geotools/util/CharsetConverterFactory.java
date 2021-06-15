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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.geotools.factory.Hints;

/**
 * Converter for going from a String to a {@link Charset} and vice versa.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 *
 *
 *
 * @source $URL$
 */
public class CharsetConverterFactory implements ConverterFactory {

    public Converter createConverter(Class<?> source, Class<?> target,
            Hints hints) {
        
        if ( CharSequence.class.isAssignableFrom( source ) && 
                Charset.class.isAssignableFrom( target ) ) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    try {
                        return(T) Charset.forName( (String) source );
                    }
                    catch( UnsupportedCharsetException e ) {
                        //TODO: log this
                        return null;
                    }
                }
            };
        }
        if ( Charset.class.isAssignableFrom( source ) && 
                CharSequence.class.isAssignableFrom( target ) ) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    return (T) ((Charset)source).toString();
                }
                
            };
        }
                
        return null;
    }

}
