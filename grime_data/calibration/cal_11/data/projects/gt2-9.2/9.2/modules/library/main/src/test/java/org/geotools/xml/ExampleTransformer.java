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

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;

public class ExampleTransformer extends TransformerBase {
    private final int bufferEveryNth;
    private final int exceptionEveryNth;
    private final boolean ignoreErrors;

    public ExampleTransformer(int bufferEveryNth, int exceptionEveryNth, boolean ignoreErrors) {
        this.bufferEveryNth = bufferEveryNth;
        this.exceptionEveryNth = exceptionEveryNth;
        this.ignoreErrors = ignoreErrors;
    }

    public Translator createTranslator(ContentHandler handler) {
        return new ExampleTranslator(handler);
    }

    private class ExampleTranslator extends TranslatorSupport {
        public ExampleTranslator(ContentHandler handler) {
            super(handler, "test", "http://geotools.org/test");
        }

        public void encode(Object o) {
            Integer i = (Integer)o;
            start("integers");
            for (int j = 1; j <= i; j++) {
                boolean buffer =
                    ((bufferEveryNth != 0) && (j % bufferEveryNth == 0));
                boolean exception = 
                    ((exceptionEveryNth != 0) && (j % exceptionEveryNth == 0));

                try {
                    if (buffer) mark();
                    element("integer", String.valueOf(j));
                    if (exception) throw new RuntimeException();
                    if (buffer) commit();
                } catch (RuntimeException e) {
                    if (!ignoreErrors) throw e;
                } finally {
                    reset();
                }
            }

            end("integers");
        }
    }
}
