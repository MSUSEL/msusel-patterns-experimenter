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

package org.geotools.data.db2;

import java.lang.reflect.Method;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class DB2ClobConverterFactory implements ConverterFactory {
	
	DB2ClobConverter converter = new DB2ClobConverter();
	static final Class<?> DB2_CLOB;
	static final Method DB2_GET_SUBSTRING;
	static final Method DB2_LENGTH;
	
	static {
	    Class<?> db2ClobClass = null;
	    try {
	        db2ClobClass = Class.forName("com.ibm.db2.jcc.DB2Clob");
	    } catch (ClassNotFoundException e) {
	        // db2jcc.jar not on the path
	    }
	    if (db2ClobClass == null) {
	        DB2_CLOB = null;
	        DB2_GET_SUBSTRING = null;
	        DB2_LENGTH = null;
	    } else {
	        try {
	            DB2_CLOB = db2ClobClass;
	            DB2_LENGTH = DB2_CLOB.getMethod("length");
	            DB2_GET_SUBSTRING = DB2_CLOB.getMethod("getSubString", long.class, int.class );
	        } catch(Exception e) {
	            throw new RuntimeException("Could not initialize the db2 clob converter", e);
	        }
	    }
	}

	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
	    // if the jdbc driver is not in the classpath don't bother trying to convert
	    if(DB2_CLOB == null)
	        return null;
	    
		// can only convert towards String
		if (!(String.class.equals(target)))
			return null;

		// can only deal with db2 specific blob classes
		if (!DB2_CLOB.isAssignableFrom(source))
			return null;
		
		// converter is thread safe, so cache and return just one
		return converter;
	}
	
	class DB2ClobConverter implements Converter {

		public <T> T convert(Object source, Class<T> target) throws Exception {
		    int length = ((Long) DB2_LENGTH.invoke(source)).intValue();
		    return (T) DB2_GET_SUBSTRING.invoke(source, 1l, length);		    
		    
		}

	}

	
}
