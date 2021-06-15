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

package org.hsqldb.lib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.hsqldb.lib.java.JavaSystem;

/**
 * Creates a direct, compressed or decompressed copy of a file.
 *
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @version 1.9.0
 * @since 1.9.0
 */
public class FileArchiver {

    public static final int  COMPRESSION_NONE = 0;
    public static final int  COMPRESSION_ZIP  = 1;
    public static final int  COMPRESSION_GZIP = 2;
    private static final int COPY_BLOCK_SIZE  = 1 << 16;

    /*
    public static void compressFile(String infilename, String outfilename,
                                    FileAccess storage) throws IOException {
        FileArchiver.archive(infilename, outfilename, storage, COMPRESSION_ZIP);
    }

    public static void decompressFile(String infilename, String outfilename,
                                      FileAccess storage) throws IOException {
        FileArchiver.unarchive(
                infilename, outfilename, storage, COMPRESSION_ZIP);
    }

    public static void copyFile(String infilename, String outfilename,
                                    FileAccess storage) throws IOException {
        FileArchiver.archive(
                infilename, outfilename, storage, COMPRESSION_NONE);
    }

    public static void restoreFile(String infilename, String outfilename,
                                      FileAccess storage) throws IOException {
        FileArchiver.unarchive(
                infilename, outfilename, storage, COMPRESSION_NONE);
    }
    */
    public static void archive(String infilename, String outfilename,
                               FileAccess storage,
                               int compressionType) throws IOException {

        InputStream          in        = null;
        OutputStream         f         = null;
        OutputStream         fOut      = null;
        DeflaterOutputStream deflater  = null;
        boolean              completed = false;

        // if there is no file
        if (!storage.isStreamElement(infilename)) {
            return;
        }

        try {
            byte[] b = new byte[COPY_BLOCK_SIZE];

            in   = storage.openInputStreamElement(infilename);
            f    = storage.openOutputStreamElement(outfilename);
            fOut = f;

            switch (compressionType) {

                case COMPRESSION_ZIP :
                    f = deflater = new DeflaterOutputStream(f,
                            new Deflater(Deflater.BEST_SPEED), b.length);
                    break;

                case COMPRESSION_GZIP :
                    f = deflater = new GZIPOutputStream(f, b.length);
                    break;

                case COMPRESSION_NONE :
                    break;

                default :
                    throw new RuntimeException("FileArchiver"
                                               + compressionType);
            }

            while (true) {
                int l = in.read(b, 0, b.length);

                if (l == -1) {
                    break;
                }

                f.write(b, 0, l);
            }

            completed = true;
        } catch (Throwable e) {
            throw JavaSystem.toIOException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (f != null) {
                    if (deflater != null) {
                        deflater.finish();
                    }

                    if (fOut instanceof FileOutputStream) {
                        storage.getFileSync(fOut).sync();
                    }

                    f.close();
                }

                if (!completed && storage.isStreamElement(outfilename)) {
                    storage.removeElement(outfilename);
                }
            } catch (Throwable e) {
                throw JavaSystem.toIOException(e);
            }
        }
    }

    public static void unarchive(String infilename, String outfilename,
                                 FileAccess storage,
                                 int compressionType) throws IOException {

        InputStream  f         = null;
        OutputStream outstream = null;
        boolean      completed = false;

        try {
            if (!storage.isStreamElement(infilename)) {
                return;
            }

            storage.removeElement(outfilename);

            byte[] b = new byte[COPY_BLOCK_SIZE];

            f = storage.openInputStreamElement(infilename);

            switch (compressionType) {

                case COMPRESSION_ZIP :
                    f = new InflaterInputStream(f, new Inflater());
                    break;

                case COMPRESSION_GZIP :
                    f = new GZIPInputStream(f, b.length);
                    break;

                case COMPRESSION_NONE :
                    break;

                default :
                    throw new RuntimeException("FileArchiver: "
                                               + compressionType);
            }

            outstream = storage.openOutputStreamElement(outfilename);

            while (true) {
                int l = f.read(b, 0, b.length);

                if (l == -1) {
                    break;
                }

                outstream.write(b, 0, l);
            }

            completed = true;
        } catch (Throwable e) {
            throw JavaSystem.toIOException(e);
        } finally {
            try {
                if (f != null) {
                    f.close();
                }

                if (outstream != null) {
                    outstream.flush();

                    if (outstream instanceof FileOutputStream) {
                        storage.getFileSync(outstream).sync();
                    }

                    outstream.close();
                }

                if (!completed && storage.isStreamElement(outfilename)) {
                    storage.removeElement(outfilename);
                }
            } catch (Throwable e) {
                throw JavaSystem.toIOException(e);
            }
        }
    }
}
