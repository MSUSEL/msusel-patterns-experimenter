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
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xs.bindings;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.geotools.data.DataUtilities;
import org.geotools.xml.Parser;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XSConfiguration;

import junit.framework.TestCase;

public class XSUnsignedBindingsTest extends TestCase {

    Parser p;

    @Override
    protected void setUp() throws Exception {
        XSConfiguration xs = new XSConfiguration();
        p = new Parser(xs);
    }

    public void testParseUnsignedInt() throws Exception {
        Long l = (Long) p.parse(new ByteArrayInputStream(doc("unsignedInt", "12").getBytes()));
        assertEquals(12l, l.longValue());
    }

    public void testParseUnsignedByte() throws Exception {
        Short s = (Short) p.parse(new ByteArrayInputStream(doc("unsignedByte", "12").getBytes()));
        assertEquals(12, s.shortValue());
    }

    public void testParseUnsignedShort() throws Exception {
        Integer i = (Integer) p.parse(new ByteArrayInputStream(doc("unsignedShort", "12").getBytes()));
        assertEquals(12, i.longValue());
    }
    
    public void testParseUnsignedLong() throws Exception {
        BigDecimal l = (BigDecimal) p.parse(new ByteArrayInputStream(doc("unsignedLong", "12").getBytes()));
        assertEquals(12l, l.longValue());
    }

    String doc(String elName, String elValue) throws IOException {
        File sampleXsd = File.createTempFile("sample", "xsd", new File("target"));
        copy(TestSchema.class.getResourceAsStream("sample.xsd"), sampleXsd);

        return String.format("<my:%s xmlns:my='http://localhost/xob/test' " +
                            "schemaLocation='http://localhost/xob/test %s'>%s</my:%s>",
            elName, DataUtilities.fileToURL(sampleXsd), elValue, elName);
    }

    void copy(InputStream in, File to) throws IOException {
        FileOutputStream fout = new FileOutputStream(to);
        byte[] buffer = new byte[1024];
        int n = 0;

        while (( n = in.read(buffer)) > 0) {
            fout.write(buffer, 0, n);
        }
        fout.flush();
        fout.close();
    }
}
