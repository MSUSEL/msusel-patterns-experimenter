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

import org.hsqldb.HsqlException;
import org.hsqldb.SessionInterface;

/**
 * Interface for Character Large Object implementations.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public interface ClobData extends LobData {

    char[] getChars(SessionInterface session, final long position, int length);

    long length(SessionInterface session);

    String getSubString(SessionInterface session, final long pos,
                        final int length);

    ClobData getClob(SessionInterface session, final long pos,
                     final long length);

    void truncate(SessionInterface session, long len);

    Reader getCharacterStream(SessionInterface session);

    int setString(SessionInterface session, long pos, String str);

    int setString(SessionInterface session, long pos, String str, int offset,
                  int len);

    int setChars(SessionInterface session, long pos, char[] chars, int offset,
                 int len);

    public long setCharacterStream(SessionInterface session, long pos,
                                   Reader in);

    long position(SessionInterface session, String searchstr, long start);

    long position(SessionInterface session, ClobData searchstr, long start);

    long nonSpaceLength(SessionInterface session);

    Reader getCharacterStream(SessionInterface session, long pos, long length);

    long getId();

    void setId(long id);

    long getRightTrimSize(SessionInterface session);
}
