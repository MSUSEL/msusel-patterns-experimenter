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

package org.hsqldb.types;

import java.io.InputStream;
import java.io.OutputStream;

import org.hsqldb.SessionInterface;

/**
 * Interface for Binary Large Object implementations.<p>
 *
 * All positions are 0 based
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public interface BlobData extends LobData {

    byte[] getBytes();

    byte[] getBytes(SessionInterface session, long pos, int length);

    BlobData getBlob(SessionInterface session, long pos, long length);

    InputStream getBinaryStream(SessionInterface session);

    InputStream getBinaryStream(SessionInterface session, long pos,
                                long length);

    long length(SessionInterface session);

    long bitLength(SessionInterface session);

    boolean isBits();

    int setBytes(SessionInterface session, long pos, byte[] bytes, int offset,
                 int len);

    int setBytes(SessionInterface session, long pos, byte[] bytes);

    public long setBinaryStream(SessionInterface session, long pos,
                                InputStream in);

    OutputStream setBinaryStream(SessionInterface session, long pos);

    void truncate(SessionInterface session, long len);

    BlobData duplicate(SessionInterface session);

    long position(SessionInterface session, byte[] pattern, long start);

    long position(SessionInterface session, BlobData pattern, long start);

    long nonZeroLength(SessionInterface session);

    long getId();

    void setId(long id);

    void free();

    boolean isClosed();

    void setSession(SessionInterface session);

    int getStreamBlockSize();
}
