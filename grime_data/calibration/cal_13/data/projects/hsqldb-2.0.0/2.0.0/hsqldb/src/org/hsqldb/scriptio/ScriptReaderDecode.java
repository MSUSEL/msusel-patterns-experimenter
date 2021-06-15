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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.hsqldb.Database;
import org.hsqldb.Session;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.persist.Crypto;
import org.hsqldb.rowio.RowInputTextLog;

/**
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class ScriptReaderDecode extends ScriptReaderText {

    DataInputStream dataInput;
    Crypto          crypto;
    byte[]          buffer = new byte[256];

    public ScriptReaderDecode(Database db, String fileName,
                              Crypto crypto) throws IOException {

        super(db);

        InputStream d =
            database.logger.getFileAccess().openInputStreamElement(fileName);
        InputStream stream = crypto.getInputStream(d);

        stream       = new GZIPInputStream(stream);
        dataStreamIn = new BufferedReader(new InputStreamReader(stream));
        rowIn        = new RowInputTextLog();
    }

    public ScriptReaderDecode(Database db, String fileName, Crypto crypto,
                              boolean forLog) throws IOException {

        super(db);

        this.crypto = crypto;

        InputStream d =
            database.logger.getFileAccess().openInputStreamElement(fileName);

        dataInput = new DataInputStream(new BufferedInputStream(d));
        rowIn     = new RowInputTextLog();
    }

    public boolean readLoggedStatement(Session session) throws IOException {

        if (dataInput == null) {
            return super.readLoggedStatement(session);
        }

        int count;

        try {
            count = dataInput.readInt();

            if (count * 2 > buffer.length) {
                buffer = new byte[count * 2];
            }

            dataInput.readFully(buffer, 0, count);
        } catch (Throwable t) {
            return false;
        }

        count = crypto.decode(buffer, 0, count, buffer, 0);

        String s = new String(buffer, 0, count, "ISO-8859-1");

        lineCount++;

//        System.out.println(lineCount);
        statement = StringConverter.unicodeStringToString(s);

        if (statement == null) {
            return false;
        }

        processStatement(session);

        return true;
    }

    public void close() {

        try {
            if (dataStreamIn != null) {
                dataStreamIn.close();
            }

            if (dataInput != null) {
                dataInput.close();
            }
        } catch (Exception e) {}
    }
}
