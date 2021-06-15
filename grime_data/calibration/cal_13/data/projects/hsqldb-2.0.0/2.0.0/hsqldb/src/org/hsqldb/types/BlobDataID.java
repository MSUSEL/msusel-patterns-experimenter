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
import org.hsqldb.error.Error;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultLob;

public class BlobDataID implements BlobData {

    long id;

    public BlobDataID(long id) {
        this.id = id;
    }

    public BlobData duplicate(SessionInterface session) {
        return null;
    }

    public void free() {}

    public InputStream getBinaryStream(SessionInterface session) {

        long length = length(session);

        return new BlobInputStream(session, this, 0, length);
    }

    public InputStream getBinaryStream(SessionInterface session, long pos,
                                       long length) {
        return new BlobInputStream(session, this, pos, length);
    }

    public byte[] getBytes() {
        return null;
    }

    public byte[] getBytes(SessionInterface session, long pos, int length) {

        ResultLob resultOut = ResultLob.newLobGetBytesRequest(id, pos, length);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw Error.error(resultIn);
        }

        return ((ResultLob) resultIn).getByteArray();
    }

    public BlobData getBlob(SessionInterface session, long pos, long length) {

        ResultLob resultOut = ResultLob.newLobGetRequest(id, pos, length);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw Error.error(resultIn);
        }

        long lobID = ((ResultLob) resultIn).getLobID();

        return new BlobDataID(lobID);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStreamBlockSize() {
        return 0;
    }

    public boolean isClosed() {
        return false;
    }

    public long length(SessionInterface session) {

        ResultLob resultOut = ResultLob.newLobGetLengthRequest(id);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return ((ResultLob) resultIn).getBlockLength();
    }

    public long bitLength(SessionInterface session) {
        return 0;
    }

    public boolean isBits() {
        return false;
    }

    public long position(SessionInterface session, BlobData pattern,
                         long start) {
        return 0L;
    }

    public long position(SessionInterface session, byte[] pattern,
                         long start) {

        ResultLob resultOut = ResultLob.newLobGetBytePatternPositionRequest(id,
            pattern, start);
        ResultLob resultIn = (ResultLob) session.execute(resultOut);

        return resultIn.getOffset();
    }

    public long nonZeroLength(SessionInterface session) {
        return 0;
    }

    public OutputStream setBinaryStream(SessionInterface session, long pos) {
        return null;
    }

    public int setBytes(SessionInterface session, long pos, byte[] bytes,
                        int offset, int len) {

        if (offset != 0 || len != bytes.length) {
            if (!BinaryData.isInLimits(bytes.length, offset, len)) {
                throw new IndexOutOfBoundsException();
            }

            byte[] newbytes = new byte[len];

            System.arraycopy(bytes, offset, newbytes, 0, len);

            bytes = newbytes;
        }

        ResultLob resultOut = ResultLob.newLobSetBytesRequest(id, pos, bytes);
        Result    resultIn  = (ResultLob) session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return bytes.length;
    }

    public int setBytes(SessionInterface session, long pos, byte[] bytes) {
        return setBytes(session, pos, bytes, 0, bytes.length);
    }

    public long setBinaryStream(SessionInterface session, long pos,
                                InputStream in) {
        return 0;
    }

    public void setSession(SessionInterface session) {}

    public void truncate(SessionInterface session, long len) {

        ResultLob resultOut = ResultLob.newLobTruncateRequest(id, len);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }
    }

    public boolean isBinary() {
        return true;
    }
}
