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

package org.geotools.data.oracle;

import java.lang.reflect.Method;
import java.util.Date;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class OracleDateConverterFactory implements ConverterFactory {
	
	OracleDateConverter converter = new OracleDateConverter();
	static final Class<?> ORA_DATE;
	static final Class<?> ORA_TIMESTAMP;
	static final Method ORA_DATE_DVALUE;
	static final Method ORA_DATE_TSVALUE;
	static final Method ORA_TS_DVALUE;
	static final Method ORA_TS_TSVALUE;
	
	static {
	    Class<?> oracleDateClass = null;
	    try {
	        oracleDateClass = Class.forName("oracle.sql.DATE");
	    } catch (ClassNotFoundException e) {
	        // ojdbc*.jar not on the path
	    }
	    if (oracleDateClass == null) {
	        ORA_DATE = null;
	        ORA_DATE_DVALUE = null;
	        ORA_DATE_TSVALUE = null;
	        ORA_TIMESTAMP = null;
	        ORA_TS_DVALUE = null;
	        ORA_TS_TSVALUE = null;
	    } else {
	        try {
	            ORA_DATE = oracleDateClass;
	            ORA_DATE_DVALUE = ORA_DATE.getMethod("dateValue");
	            ORA_DATE_TSVALUE = ORA_DATE.getMethod("timestampValue");
	            ORA_TIMESTAMP = Class.forName("oracle.sql.TIMESTAMP");
	            ORA_TS_DVALUE = ORA_TIMESTAMP.getMethod("dateValue");
	            ORA_TS_TSVALUE = ORA_TIMESTAMP.getMethod("timestampValue");
	        } catch(Exception e) {
	            throw new RuntimeException("Could not initialize the oracle date converter", e);
	        }
	    }
	}

	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
	    // if the jdbc driver is not in the classpath don't bother trying to convert
	    if(ORA_DATE == null)
	        return null;
	    
		// can only convert towards java.util.Date && subclasses
		if (!(Date.class.isAssignableFrom(target)))
			return null;

		// can only deal with oracle specific date classes
		if (!(ORA_TIMESTAMP.isAssignableFrom(source)) && !(ORA_DATE.isAssignableFrom(source)))
			return null;
		
		// converter is thread safe, so cache and return just one
		return converter;
	}
	
	class OracleDateConverter implements Converter {

		public <T> T convert(Object source, Class<T> target) throws Exception {
			if (ORA_TIMESTAMP.isInstance(source)) {
				if (java.sql.Date.class.isAssignableFrom(target))
					return (T) ORA_TS_DVALUE.invoke(source);
				else
					return (T) ORA_TS_TSVALUE.invoke(source);
			} else {
				if (java.sql.Date.class.isAssignableFrom(target))
					return (T) ORA_DATE_DVALUE.invoke(source);
				else
					return (T) ORA_DATE_TSVALUE.invoke(source);
			}
		}

	}

	
}
