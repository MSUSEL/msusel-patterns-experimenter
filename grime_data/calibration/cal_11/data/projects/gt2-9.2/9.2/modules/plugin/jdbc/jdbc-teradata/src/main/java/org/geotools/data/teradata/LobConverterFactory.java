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
package org.geotools.data.teradata;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class LobConverterFactory implements ConverterFactory {

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {

        if (Blob.class.isAssignableFrom(source) && byte[].class.isAssignableFrom(target)) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof Blob && byte[].class.isAssignableFrom(target)) {
                        Blob blob = (Blob) source;
                        InputStream blobIS = blob.getBinaryStream();
                        byte[] blobBA = new byte[blobIS.available()];
                        blobIS.read(blobBA);
                        blobIS.close();
                        return (T) blobBA;
                    }
                    return null;
                }
            };
        }

        if (Clob.class.isAssignableFrom(source) && String.class.isAssignableFrom(target)) {
            return new Converter() {
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    if (source instanceof Clob && String.class.isAssignableFrom(target)) {
                        Clob clob = (Clob) source;
                        Reader clobReader = clob.getCharacterStream();
                        char[] clobChars = new char[(int) clob.length()];
                        clobReader.read(clobChars);
                        String clobString = new String(clobChars);
                        return (T) clobString;
                    }
                    return null;
                }
            };
        }

        return null;
    }
}
