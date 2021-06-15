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

import java.io.Reader;

import org.hsqldb.SessionInterface;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultLob;

/**
 * Implementation of CLOB for client and server.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class ClobDataID implements ClobData {

    long id;

    public ClobDataID(long id) {
        this.id = id;
    }

    public char[] getChars(SessionInterface session, long position,
                           int length) {

        ResultLob resultOut = ResultLob.newLobGetCharsRequest(id, position,
            length);
        Result resultIn = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return ((ResultLob) resultIn).getCharArray();
    }

    public long length(SessionInterface session) {

        ResultLob resultOut = ResultLob.newLobGetLengthRequest(id);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return ((ResultLob) resultIn).getBlockLength();
    }

    public String getSubString(SessionInterface session, long pos,
                               int length) {

        char[] chars = getChars(session, pos, length);

        return new String(chars);
    }

    public ClobData getClob(SessionInterface session, long position,
                            long length) {

        ResultLob resultOut = ResultLob.newLobGetRequest(id, position, length);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return new ClobDataID(((ResultLob) resultIn).getLobID());
    }

    public void truncate(SessionInterface session, long len) {
        ResultLob resultOut = ResultLob.newLobTruncateRequest(id, len);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }
    }

    public Reader getCharacterStream(SessionInterface session) {
        long length = length(session);
        return new ClobInputStream(session, this, 0, length);
    }

    public long setCharacterStream(SessionInterface session, long pos,
                                   Reader in) {
        return 0;
    }

    public int setString(SessionInterface session, long pos, String str) {

        ResultLob resultOut = ResultLob.newLobSetCharsRequest(id, pos,
            str.toCharArray());
        Result resultIn = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return str.length();
    }

    public int setString(SessionInterface session, long pos, String str,
                         int offset, int len) {

        if (!isInLimits(str.length(), offset, len)) {
            throw Error.error(ErrorCode.X_22001);
        }

        ResultLob resultOut = ResultLob.newLobSetCharsRequest(id, pos,
            str.substring(offset, len).toCharArray());
        Result resultIn = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return str.length();
    }

    public int setChars(SessionInterface session, long pos, char[] chars,
                        int offset, int len) {

        if (!isInLimits(chars.length, offset, len)) {
            throw Error.error(ErrorCode.X_22001);
        }

        char[] newChars = new char[len];

        System.arraycopy(chars, offset, newChars, 0, len);

        ResultLob resultOut = ResultLob.newLobSetCharsRequest(id, pos, chars);
        Result    resultIn  = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return len;
    }

    public long position(SessionInterface session, String searchstr,
                         long start) {

        ResultLob resultOut = ResultLob.newLobGetCharPatternPositionRequest(id,
            searchstr.toCharArray(), start);
        Result resultIn = session.execute(resultOut);

        if (resultIn.isError()) {
            throw resultIn.getException();
        }

        return ((ResultLob) resultIn).getOffset();
    }

    public long position(SessionInterface session, ClobData searchstr,
                         long start) {
        return 0L;
    }

    public long nonSpaceLength(SessionInterface session) {
        return 0;
    }

    public Reader getCharacterStream(SessionInterface session, long pos,
                                     long length) {
        return new ClobInputStream(session, this, pos, length);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRightTrimSize(SessionInterface session) {
        return 0;
    }

    static boolean isInLimits(long fullLength, long pos, long len) {
        return pos >= 0 && len >= 0 && pos + len <= fullLength;
    }

    public void setSession(SessionInterface session) {}

    public boolean isBinary() {
        return false;
    }
}
