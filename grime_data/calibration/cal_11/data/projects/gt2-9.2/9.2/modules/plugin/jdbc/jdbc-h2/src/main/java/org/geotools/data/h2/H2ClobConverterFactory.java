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

package org.geotools.data.h2;

import java.io.Reader;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.h2.jdbc.JdbcClob;

/**
 * Converts a H2 JdbcClob to a String
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 *
 *
 * @source $URL$
 */
public class H2ClobConverterFactory implements ConverterFactory {

    H2ClobConverter converter = new H2ClobConverter();

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        // can only convert towards String
        if (!(String.class.equals(target)))
            return null;

        // can only deal with JdbcClob
        if (!JdbcClob.class.isAssignableFrom(source))
            return null;

        // converter is thread safe, so cache and return just one
        return converter;
    }

    class H2ClobConverter implements Converter {

        public <T> T convert(Object source, Class<T> target) throws Exception {
            JdbcClob clob = (JdbcClob) source;
            Reader r = null;
            try {
                StringBuilder sb = new StringBuilder();
                char[] cbuf = new char[4096];
                int read;

                r = clob.getCharacterStream();
                while ((read = r.read(cbuf)) > 0) {
                    sb.append(cbuf, 0, read);
                }

                return (T) sb.toString();
            } finally {
                r.close();
            }

        }
    }

}
