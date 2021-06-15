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

import it.unimi.dsi.fastutil.io.RepositionableStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.archive.io.GzipHeader;
import org.archive.io.NoGzipMagicException;
import org.archive.net.UURI;

public class ARCUtils implements ARCConstants {
    /**
     * @param pathOrUri Path or URI to extract arc filename from.
     * @return Extracted arc file name.
     * @throws URISyntaxException 
     */
    public static String parseArcFilename(final String pathOrUri)
    throws URISyntaxException {
        String path = pathOrUri;
        if (UURI.hasScheme(pathOrUri)) {
            URI url = new URI(pathOrUri);
            path = url.getPath();
        }
        return (new File(path)).getName();
    }
    
    /**
     * @param arcFile File to test.
     * @return True if <code>arcFile</code> is compressed ARC.
     * @throws IOException
     */
    public static boolean isCompressed(File arcFile) throws IOException {
        return testCompressedARCFile(arcFile);
    }
    
    /**
     * Check file is compressed and in ARC GZIP format.
     *
     * @param arcFile File to test if its Internet Archive ARC file
     * GZIP compressed.
     *
     * @return True if this is an Internet Archive GZIP'd ARC file (It begins
     * w/ the Internet Archive GZIP header and has the
     * COMPRESSED_ARC_FILE_EXTENSION suffix).
     *
     * @exception IOException If file does not exist or is not unreadable.
     */
    public static boolean testCompressedARCFile(File arcFile)
    throws IOException {
        return testCompressedARCFile(arcFile, false);
    }

    /**
     * Check file is compressed and in ARC GZIP format.
     *
     * @param arcFile File to test if its Internet Archive ARC file
     * GZIP compressed.
     * @param skipSuffixCheck Set to true if we're not to test on the
     * '.arc.gz' suffix.
     *
     * @return True if this is an Internet Archive GZIP'd ARC file (It begins
     * w/ the Internet Archive GZIP header).
     *
     * @exception IOException If file does not exist or is not unreadable.
     */
    public static boolean testCompressedARCFile(File arcFile,
            boolean skipSuffixCheck)
    throws IOException {
        boolean compressedARCFile = false;
        isReadable(arcFile);
        if(!skipSuffixCheck && !arcFile.getName().toLowerCase()
                .endsWith(COMPRESSED_ARC_FILE_EXTENSION)) {
            return compressedARCFile;
        }
        
        final InputStream is = new FileInputStream(arcFile);
        try {
            compressedARCFile = testCompressedARCStream(is);
        } finally {
            is.close();
        }
        return compressedARCFile;
    }
    
    /**
     * Tests passed stream is gzip stream by reading in the HEAD.
     * Does not reposition the stream.  That is left up to the caller.
     * @param is An InputStream.
     * @return True if compressed stream.
     * @throws IOException
     */
    public static boolean testCompressedARCStream(final InputStream is)
            throws IOException {
        boolean compressedARCFile = false;
        GzipHeader gh = null;
        try {
            gh = new GzipHeader(is);
        } catch (NoGzipMagicException e ) {
            return compressedARCFile;
        }
        
        byte[] fextra = gh.getFextra();
        // Now make sure following bytes are IA GZIP comment.
        // First check length. ARC_GZIP_EXTRA_FIELD includes length
        // so subtract two and start compare to ARC_GZIP_EXTRA_FIELD
        // at +2.
        if (fextra != null &&
        		ARC_GZIP_EXTRA_FIELD.length - 2 == fextra.length) {
            compressedARCFile = true;
            for (int i = 0; i < fextra.length; i++) {
                if (fextra[i] != ARC_GZIP_EXTRA_FIELD[i + 2]) {
                    compressedARCFile = false;
                    break;
                }
            }
        }
        return compressedARCFile;
    }
    
    /**
     * Tests passed stream is gzip stream by reading in the HEAD.
     * Does reposition of stream when done.
     * @param rs An InputStream that is Repositionable.
     * @return True if compressed stream.
     * @throws IOException
     */
    public static boolean testCompressedRepositionalStream(
            final RepositionableStream rs)
    throws IOException {
        boolean compressedARCFile = false;
        long p = rs.position();
        try {
            compressedARCFile = testCompressedStream((InputStream)rs);
        } finally {
            rs.position(p);
        }
        return compressedARCFile; 
    }
    
    /**
     * Tests passed stream is gzip stream by reading in the HEAD.
     * Does reposition of stream when done.
     * @param is An InputStream.
     * @return True if compressed stream.
     * @throws IOException
     */
    public static boolean testCompressedStream(final InputStream is)
    throws IOException {
        boolean compressedARCFile = false;
        try {
            new GzipHeader(is);
            compressedARCFile = true;
        } catch (NoGzipMagicException e) {
            return compressedARCFile;
        }
        return compressedARCFile;
    }
    
    /**
     * Check file is uncompressed ARC file.
     * 
     * @param arcFile
     *            File to test if its Internet Archive ARC file uncompressed.
     * 
     * @return True if this is an Internet Archive ARC file.
     * 
     * @exception IOException
     *                If file does not exist or is not unreadable.
     */
    public static boolean testUncompressedARCFile(File arcFile)
    throws IOException {
        boolean uncompressedARCFile = false;
        isReadable(arcFile);
        if(arcFile.getName().toLowerCase().endsWith(ARC_FILE_EXTENSION)) {
            FileInputStream fis = new FileInputStream(arcFile);
            try {
                byte [] b = new byte[ARC_MAGIC_NUMBER.length()];
                int read = fis.read(b, 0, ARC_MAGIC_NUMBER.length());
                fis.close();
                if (read == ARC_MAGIC_NUMBER.length()) {
                    StringBuffer beginStr
                        = new StringBuffer(ARC_MAGIC_NUMBER.length());
                    for (int i = 0; i < ARC_MAGIC_NUMBER.length(); i++) {
                        beginStr.append((char)b[i]);
                    }
                    
                    if (beginStr.toString().
                            equalsIgnoreCase(ARC_MAGIC_NUMBER)) {
                        uncompressedARCFile = true;
                    }
                }
            } finally {
                fis.close();
            }
        }

        return uncompressedARCFile;
    }
    

    /**
     * @param arcFile File to test.
     * @exception IOException If file does not exist or is not unreadable.
     */
    private static void isReadable(File arcFile) throws IOException {
        if (!arcFile.exists()) {
            throw new FileNotFoundException(arcFile.getAbsolutePath() +
                " does not exist.");
        }

        if (!arcFile.canRead()) {
            throw new FileNotFoundException(arcFile.getAbsolutePath() +
                " is not readable.");
        }
    }
}
