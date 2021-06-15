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

package org.hsqldb.scriptio;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.persist.Crypto;

/**
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @since 1.9.0
 * @version 1.9.0
 */
public class ScriptWriterEncode extends ScriptWriterText {

    Crypto                    crypto;
    HsqlByteArrayOutputStream byteOut;

    public ScriptWriterEncode(Database db, String file, boolean includeCached,
                              Crypto crypto) {

        super(db, file, includeCached, true, false);

        try {
            fileStreamOut = crypto.getOutputStream(fileStreamOut);
            fileStreamOut = new GZIPOutputStream(fileStreamOut);
        } catch (IOException e) {
            throw Error.error(e, ErrorCode.FILE_IO_ERROR,
                              ErrorCode.M_Message_Pair, new Object[] {
                e.getMessage(), outFile
            });
        }
    }

    public ScriptWriterEncode(Database db, String file, Crypto crypto) {

        super(db, file, false, false, false);

        this.crypto = crypto;
        byteOut     = new HsqlByteArrayOutputStream();
    }

    protected void finishStream() throws IOException {

        if (fileStreamOut instanceof GZIPOutputStream) {
            ((GZIPOutputStream) fileStreamOut).finish();
        }

        fileStreamOut.flush();
    }

    void writeRowOutToFile() throws IOException {

        synchronized (fileStreamOut) {
            if (byteOut == null) {
                super.writeRowOutToFile();

                return;
            }

            int count = crypto.getEncodedSize(rowOut.size());
            byteOut.ensureRoom(count + 4);

            count = crypto.encode(rowOut.getBuffer(), 0, rowOut.size(),
                                      byteOut.getBuffer(), 4);
            byteOut.setPosition(0);
            byteOut.writeInt(count);
            fileStreamOut.write(byteOut.getBuffer(), 0, count + 4);
        }
    }
}
