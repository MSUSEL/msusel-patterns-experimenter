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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.transform.TransformerException;

import junit.framework.TestCase;

public class TransformerBaseTest extends TestCase {

    public void testUnbufferedUsageNoErrors() throws FileNotFoundException, TransformerException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer><test:integer>10</test:integer></test:integers>";
        ExampleTransformer tx = new ExampleTransformer(0, 0, false);
        String actual = tx.transform(10);
        assertEquals(expected, actual);
    }

    public void testUnbufferedUsageOneError() throws FileNotFoundException, TransformerException {
        StringWriter w = new StringWriter();
        try {
            ExampleTransformer tx = new ExampleTransformer(0, 10, false);
            tx.transform(10, w);
            fail("Should have thrown an exception before reaching this point");
        } catch (TransformerException e) {
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer><test:integer>10</test:integer>";
            String actual = w.toString();
            assertEquals(expected, actual);
        }
    }

    public void testBufferedUsageNoErrors() throws FileNotFoundException, TransformerException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer><test:integer>10</test:integer></test:integers>";
        ExampleTransformer tx = new ExampleTransformer(1, 0, false);
        String actual = tx.transform(10);
        assertEquals(expected, actual);
    }

    public void testBufferedUsageOneError() throws FileNotFoundException, TransformerException {
        StringWriter w = new StringWriter();
        try {
            ExampleTransformer tx = new ExampleTransformer(1, 10, false);
            tx.transform(10, w);
            fail("Should have thrown an exception before reaching this point!");
        } catch (TransformerException e) {
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer>";
            String actual = w.toString();
            assertEquals(expected, actual);
        }
    }

    public void testBufferedUsageIgnoringOneError() throws FileNotFoundException, TransformerException {
        ExampleTransformer tx = new ExampleTransformer(1, 10, true);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>2</test:integer><test:integer>3</test:integer><test:integer>4</test:integer><test:integer>5</test:integer><test:integer>6</test:integer><test:integer>7</test:integer><test:integer>8</test:integer><test:integer>9</test:integer></test:integers>";
        String actual = tx.transform(10);
        assertEquals(expected, actual);
    }

    public void testBufferedUsageIgnoringMultipleErrors() throws FileNotFoundException, TransformerException {
        ExampleTransformer tx = new ExampleTransformer(1, 2, true);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test:integers xmlns=\"http://geotools.org/test\" xmlns:test=\"http://geotools.org/test\"><test:integer>1</test:integer><test:integer>3</test:integer><test:integer>5</test:integer><test:integer>7</test:integer><test:integer>9</test:integer></test:integers>";
        String actual = tx.transform(10);
        assertEquals(expected, actual);
    }

    public void testPassingInNull() throws FileNotFoundException, TransformerException {
        ExampleTransformer tx = new ExampleTransformer(0, 0, true);
        try {
            tx.transform(null);
            fail("Expected NullPointerException but none was thrown.");
        } catch (TransformerException e) {
            // Swallow exception IFF it was due to a NullPointerException; otherwise rethrow
            if (!(e.getCause() instanceof NullPointerException)) throw e;
        }
    }
}
