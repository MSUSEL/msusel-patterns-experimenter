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

package org.hsqldb.persist;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.hsqldb.Database;
import org.hsqldb.error.Error;

/**
 * New NIO version of ScaledRAFile. This class is used only for storing a CACHED
 * TABLE .data file and cannot be used for TEXT TABLE source files.
 *
 * Due to various issues with java.nio classes, this class will use a mapped
 * channel of fixed size. After reaching this size, the file and channel are
 * closed.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version  1.9.0
 * @since 1.8.0.5
 */
final class ScaledRAFileNIO implements ScaledRAInterface {

    private final Database   database;
    private final boolean    readOnly;
    private final long       bufferLength;
    private RandomAccessFile file;
    private MappedByteBuffer buffer;
    private FileChannel      channel;
    private boolean          bufferModified;

    // We are using persist.Logger-instance-specific FrameworkLogger
    // because it is Database-instance specific.
    // If add any static level logging, should instantiate a standard,
    // context-agnostic FrameworkLogger for that purpose.
    private final static String JVM_ERROR = "JVM threw unsupported Exception";

    ScaledRAFileNIO(Database database, String name, boolean readOnly,
                    int bufferLength) throws Throwable {

        this.database = database;

        long fileLength;

        if (bufferLength < 1 << 18) {
            bufferLength = 1 << 18;
        }

        try {
            file = new RandomAccessFile(name, readOnly ? "r"
                                                       : "rw");
        } catch (Throwable e) {
            throw e;
        }

        try {
            fileLength = file.length();
        } catch (Throwable e) {
            file.close();

            throw e;
        }

        if (fileLength > ScaledRAFile.MAX_NIO_LENGTH) {
            file.close();

            throw new IOException("length exceeds nio limit");
        }

        if (bufferLength < fileLength) {
            bufferLength = (int) fileLength;
        }

        bufferLength = newNIOBufferSize(bufferLength);

        if (readOnly) {
            bufferLength = (int) fileLength;
        }

        if (fileLength < bufferLength) {
            try {
                file.seek(bufferLength - 1);
                file.writeByte(0);
                file.getFD().sync();
                file.close();

                file = new RandomAccessFile(name, readOnly ? "r"
                                                           : "rw");
            } catch (Throwable e) {
                file.close();

                throw e;
            }
        }

        this.readOnly     = readOnly;
        this.bufferLength = bufferLength;
        this.channel      = file.getChannel();

        try {
            buffer = channel.map(readOnly ? FileChannel.MapMode.READ_ONLY
                                          : FileChannel.MapMode.READ_WRITE, 0,
                                          bufferLength);

            Error.printSystemOut("NIO file instance created. mode: "
                                 + readOnly);

            if (!readOnly) {
                long tempSize = bufferLength - fileLength;

                if (tempSize > 1 << 18) {
                    tempSize = 1 << 18;
                }

                byte[] temp = new byte[(int) tempSize];

                try {
                    long pos = fileLength;

                    for (; pos < bufferLength - tempSize; pos += tempSize) {
                        buffer.position((int) pos);
                        buffer.put(temp, 0, temp.length);
                    }

                    buffer.position((int) pos);
                    buffer.put(temp, 0, (int) (bufferLength - pos));
                    buffer.force();
                } catch (Throwable t) {
                    database.logger.logWarningEvent(JVM_ERROR + " "
                                                    + "length: "
                                                    + bufferLength, t);
                }

                buffer.position(0);
            }
        } catch (Throwable e) {
            Error.printSystemOut("NIO constructor failed:  " + bufferLength);

            buffer  = null;
            channel = null;

            file.close();

            // System.gc();
            throw e;
        }
    }

    public long length() throws IOException {

        try {
            return file.length();
        } catch (IOException e) {
            database.logger.logWarningEvent("nio", e);

            throw e;
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public void seek(long newPos) throws IOException {

        try {
            buffer.position((int) newPos);
        } catch (IllegalArgumentException e) {
            database.logger.logWarningEvent("nio", e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public long getFilePointer() throws IOException {

        try {
            return buffer.position();
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public int read() throws IOException {

        try {
            return buffer.get();
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public void read(byte[] b, int offset, int length) throws IOException {

        try {
            buffer.get(b, offset, length);
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public int readInt() throws IOException {

        try {
            return buffer.getInt();
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public long readLong() throws IOException {

        try {
            return buffer.getLong();
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public void write(byte[] b, int offset, int len) throws IOException {

        try {
            bufferModified = true;

            buffer.put(b, offset, len);
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public void writeInt(int i) throws IOException {

        try {
            bufferModified = true;

            buffer.putInt(i);
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public void writeLong(long i) throws IOException {

        try {
            bufferModified = true;

            buffer.putLong(i);
        } catch (Throwable e) {
            database.logger.logWarningEvent(JVM_ERROR, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public void close() throws IOException {

        try {
            Error.printSystemOut("NIO close() start - fileLength = "
                                 + bufferLength);

            if (buffer != null && bufferModified) {
                try {
                    buffer.force();
                } catch (Throwable t) {
                    try {
                        buffer.force();
                    } catch (Throwable t1) {
                        database.logger.logWarningEvent(JVM_ERROR + " "
                                                        + "length: "
                                                        + bufferLength, t);
                    }
                }
            }

            buffer  = null;
            channel = null;

            file.close();

            // System.gc();
        } catch (Throwable e) {
            database.logger.logWarningEvent("length: " + bufferLength, e);

            IOException io = new IOException(e.getMessage());

            try {
                io.initCause(e);
            } catch (Throwable e1) {}

            throw io;
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean wasNio() {
        return true;
    }

    public boolean canAccess(int length) {
        return buffer.position() + length <= bufferLength;
    }

    public boolean canSeek(long position) {
        return position <= bufferLength;
    }

    public Database getDatabase() {
        return null;
    }

    public void synch() {

        try {
            buffer.force();
        } catch (Throwable t) {}
    }

    static int newNIOBufferSize(int newSize) {

        int bufSize = 0;

        for (int scale = 20; scale < 30; scale++) {
            bufSize = 1 << scale;

            if (bufSize >= newSize) {
                break;
            }
        }

        return bufSize;
    }
}
