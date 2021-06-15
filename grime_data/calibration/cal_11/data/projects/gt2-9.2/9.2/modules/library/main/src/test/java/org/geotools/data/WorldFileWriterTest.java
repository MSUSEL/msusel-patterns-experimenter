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
package org.geotools.data;

import static org.junit.Assert.*;

import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;

public class WorldFileWriterTest {

    @Test
    public void testWrite() throws Exception {
        AffineTransform at = new AffineTransform(42.34, 0, 0, -42.34,347671.10, 5196940.18);

        File tmp = File.createTempFile("write", "wld", new File("target"));
        new WorldFileWriter(tmp, at);

        BufferedReader r = new BufferedReader(new FileReader(tmp));
        assertEquals(42.34, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(0, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(0, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(-42.34, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(347671.10, Double.parseDouble(r.readLine()), 0.1);
        assertEquals(5196940.18, Double.parseDouble(r.readLine()), 0.1);

        assertNull(r.readLine());
    }
}
