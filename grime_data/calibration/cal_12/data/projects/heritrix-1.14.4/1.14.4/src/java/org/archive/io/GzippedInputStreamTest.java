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

import it.unimi.dsi.fastutil.io.RepositionableStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.archive.util.TmpDirTestCase;

/**
 * @author stack
 * @version $Date: 2006-08-04 00:13:51 +0000 (Fri, 04 Aug 2006) $, $Revision: 4431 $
 */
public class GzippedInputStreamTest extends TmpDirTestCase {
    /**
     * Number of records in gzip member file.
     */
    final static int GZIPMEMBER_COUNT = 4;
    final static String TEXT = "Some old text to compress.";
    // Create file to use in tests below.
    private File compressedFile = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.compressedFile = createMultiGzipMembers();
    }
    
    protected void tearDown() throws Exception {
        if (this.compressedFile != null) {
            this.compressedFile.delete();
        }
        super.tearDown();
    }

    public static void main(String [] args) {
        junit.textui.TestRunner.run(GzippedInputStreamTest.class);
    }
    
    protected class RepositionableRandomAccessInputStream
    extends RandomAccessInputStream
    implements RepositionableStream {
        public RepositionableRandomAccessInputStream(final File file)
        throws IOException {
            super(file);
        }
        
        public RepositionableRandomAccessInputStream(final File file,
            final long offset)
        throws IOException {
            super(file, offset);
        }
    }

    protected File createMultiGzipMembers() throws IOException {
        final File f =
            new File(getTmpDir(), this.getClass().getName() + ".gz");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
        for (int i = 0; i < GZIPMEMBER_COUNT; i++) {
            os.write(GzippedInputStream.gzip(TEXT.getBytes()));
        }
        os.close();
        return f;
    }
    
    public void testCountOfMembers()
    throws IOException {
        InputStream is =
            new RepositionableRandomAccessInputStream(this.compressedFile);
        GzippedInputStream gis = new GzippedInputStream(is);
        int records = 0;
        // Get offset of second record.  Will use it later in tests below.
        long offsetOfSecondRecord = -1;
        for (Iterator i = gis.iterator(); i.hasNext();) {
            long offset = gis.position();
            if (records == 1) {
                offsetOfSecondRecord = offset;
            }
            is = (InputStream)i.next();
            records++;
        }
        assertTrue("Record count is off " + records,
            records == GZIPMEMBER_COUNT);
        gis.close();
        
        // Test random record read.
        is = new RepositionableRandomAccessInputStream(this.compressedFile);
        gis = new GzippedInputStream(is);
        byte [] buffer = new byte[TEXT.length()];
        // Seek to second record, read in gzip header.
        gis.gzipMemberSeek(offsetOfSecondRecord);
        gis.read(buffer);
        String readString = new String(buffer);
        assertEquals("Failed read", TEXT, readString);
        gis.close();
        
        // Test the count we get makes sense after iterating through
        // starting at second record.
        is = new RepositionableRandomAccessInputStream(this.compressedFile,
            offsetOfSecondRecord);
        gis = new GzippedInputStream(is);
        records = 0;
        for (final Iterator i = gis.iterator(); i.hasNext(); i.next()) {
            records++;
        }
        assertEquals(records,
            GZIPMEMBER_COUNT - 1 /*We started at 2nd record*/);
        gis.close();
    }
    
    public void testCompressedStream() throws IOException {
        byte [] bytes = "test".getBytes();
        ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
        assertFalse(GzippedInputStream.isCompressedStream(baos));
        
        byte [] gzippedMetaData = GzippedInputStream.gzip(bytes);
        baos = new ByteArrayInputStream(gzippedMetaData);
        assertTrue(GzippedInputStream.isCompressedStream(baos));
        
        gzippedMetaData = GzippedInputStream.gzip(bytes);
        final RepositionableByteArrayInputStream rbaos =
            new RepositionableByteArrayInputStream(gzippedMetaData);
        final int totalBytes = gzippedMetaData.length;
        assertTrue(GzippedInputStream.isCompressedRepositionableStream(rbaos));
        long available = rbaos.available();
        assertEquals(available, totalBytes);
        assertEquals(rbaos.position(), 0);
    }
    
    private class RepositionableByteArrayInputStream
    extends ByteArrayInputStream implements RepositionableStream {
        public RepositionableByteArrayInputStream(final byte [] bytes) {
            super(bytes);
        }
        
        public void position(long p) {
            this.pos = (int)p;
        }
        public long position() {
            return this.pos;
        }
    }
}