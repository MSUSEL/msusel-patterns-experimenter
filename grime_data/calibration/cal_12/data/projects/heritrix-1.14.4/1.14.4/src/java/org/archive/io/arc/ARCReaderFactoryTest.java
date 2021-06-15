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
package org.archive.io.arc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.archive.util.TmpDirTestCase;

public class ARCReaderFactoryTest extends TmpDirTestCase {
//    public void testGetHttpURL() throws MalformedURLException, IOException {
//        ARCReader reader = null;
//        try {
//            // TODO: I can get a single ARCRecord but trying to iterate from
//            // a certain point is getting an EOR when I go to read GZIP header.
//            reader = ARCReaderFactory.
//                get(new URL("http://localhost/test.arc.gz"), 0);
//            for (final Iterator i = reader.iterator(); i.hasNext();) {
//                ARCRecord ar = (ARCRecord)i.next();
//                System.out.println(ar.getMetaData().getUrl());
//            }
//        } finally {
//            if (reader != null) {
//                reader.close();
//            }
//        }
//    }
    
    /**
     * Test File URL.
     * If a file url, we just use the pointed to file.  There is no
     * copying down to a file in tmp that gets cleaned up after close.
     * @throws MalformedURLException
     * @throws IOException
     */
    public void testGetFileURL() throws MalformedURLException, IOException {
        File arc = ARCWriterTest.createARCFile(getTmpDir(), true);
        doGetFileUrl(arc);
    }
    
    protected void doGetFileUrl(File arc)
    throws MalformedURLException, IOException {
        ARCReader reader = null;
        File tmpFile = null;
        try {
            reader = ARCReaderFactory.
                get(new URL("file:////" + arc.getAbsolutePath()));
            tmpFile = null;
            for (Iterator i = reader.iterator(); i.hasNext();) {
                ARCRecord r = (ARCRecord)i.next();
                if (tmpFile == null) {
                    tmpFile = new File(r.getMetaData().getArc());
                }
            }
            assertTrue(tmpFile.exists());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        assertTrue(tmpFile.exists());
    }
    
    /**
     * Test path or url.
     * @throws MalformedURLException 
     * @throws IOException 
     */
    public void testGetPathOrURL() throws MalformedURLException, IOException {
        File arc = ARCWriterTest.createARCFile(getTmpDir(), true);
        ARCReader reader = ARCReaderFactory.get(arc.getAbsoluteFile());
        assertNotNull(reader);
        reader.close();
        doGetFileUrl(arc);
    }   
}
