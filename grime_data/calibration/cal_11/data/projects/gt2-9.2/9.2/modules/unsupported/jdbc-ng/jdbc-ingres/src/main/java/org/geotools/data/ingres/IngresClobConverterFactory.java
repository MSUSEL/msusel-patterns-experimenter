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

package org.geotools.data.ingres;

import java.lang.reflect.Method;
import java.io.InputStream;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class IngresClobConverterFactory implements ConverterFactory {
	
	IngresClobConverter converter = new IngresClobConverter();
	static final Class<?> INGRES_CLOB;
	static final Method INGRES_GET_STREAM;
	static final Method INGRES_LENGTH;
	
	static {
	    Class<?> jdbcClobClass = null;
	    try {
	        jdbcClobClass = Class.forName("java.sql.Clob");
	    } catch (ClassNotFoundException e) {
	        // java.sql.Clob not found
	    }
	    if (jdbcClobClass == null) {
	        INGRES_CLOB = null;
	        INGRES_GET_STREAM = null;
	        INGRES_LENGTH = null;
	    } else {
	        try {
	            INGRES_CLOB = jdbcClobClass;
	            INGRES_LENGTH = INGRES_CLOB.getMethod("length");
	            INGRES_GET_STREAM = INGRES_CLOB.getMethod("getAsciiStream");
	        } catch(Exception e) {
	            throw new RuntimeException("Could not initialize the ingres blob converter", e);
	        }
	    }
	}

	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
	    // if the jdbc driver is not in the classpath don't bother trying to convert
	    if(INGRES_CLOB == null)
	        return null;
	    
		// can only convert towards String
		if (!(String.class.equals(target)))
			return null;

		// can only deal with ingres specific blob classes
		if (!INGRES_CLOB.isAssignableFrom(source))
			return null;
		
		// converter is thread safe, so cache and return just one
		return converter;
	}
	
	class IngresClobConverter implements Converter {

		public <T> T convert(Object source, Class<T> target) throws Exception {
		    int length = ((Long) INGRES_LENGTH.invoke(source)).intValue();
		    byte[] buffer = new byte[length]; 
		    InputStream dataStream = (InputStream) INGRES_GET_STREAM.invoke(source);
		    dataStream.read(buffer, 0, length);
		    return (T) new String(buffer);
		}

	}

	
}
