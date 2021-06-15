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
package org.archive.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.archive.util.TmpDirTestCase;

public class RepositionableInputStreamTest extends TmpDirTestCase {
    private File testFile;
    private static final String LINE = "0123456789abcdefghijklmnopqrstuv";
    protected void setUp() throws Exception {
        super.setUp();
        this.testFile = new File(getTmpDir(), this.getClass().getName());
        PrintWriter pw = new PrintWriter(new FileOutputStream(testFile));
        for (int i = 0; i < 100; i++) {
            pw.print(LINE);
        }
        pw.close();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testname() throws Exception {
        // Make buffer awkward size so we run into buffers spanning issues.
        RepositionableInputStream ris =
            new RepositionableInputStream(new FileInputStream(this.testFile),
                57);
        int c = ris.read();
        assertEquals(1, ris.position());
        ris.read();
        ris.position(0);
        assertEquals(0, ris.position());
        int c1 = ris.read();
        assertEquals(c, c1);
        ris.position(0);
        byte [] bytes = new byte[LINE.length()];
        long offset = 0;
        for (int i = 0; i < 10; i++) {
            ris.read(bytes, 0, LINE.length());
            assertEquals(LINE, new String(bytes));
            offset += LINE.length();
            assertEquals(offset, ris.position());
        }
        long p = ris.position();
        ris.position(p - LINE.length());
        assertEquals(p - LINE.length(), ris.position());
        c = ris.read();
        assertEquals(c, c1);
    }
}
