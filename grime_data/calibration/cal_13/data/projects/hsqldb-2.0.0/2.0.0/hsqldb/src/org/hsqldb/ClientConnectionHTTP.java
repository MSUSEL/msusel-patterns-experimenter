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

package org.hsqldb;

import java.io.IOException;
import java.io.OutputStream;

import org.hsqldb.lib.InOutUtil;
import org.hsqldb.result.Result;

/**
 * HTTP protocol session proxy implementation. Uses the updated HSQLDB HTTP
 * sub protocol.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class ClientConnectionHTTP extends ClientConnection {

    static final String ENCODING = "8859_1";
    static final int    IDLENGTH = 12;    // length of int + long for db and session IDs

    public ClientConnectionHTTP(String host, int port, String path,
                                String database, boolean isTLS, String user,
                                String password, int timeZoneSeconds) {
        super(host, port, path, database, isTLS, user, password,
              timeZoneSeconds);
    }

    protected void initConnection(String host, int port, boolean isTLS) {}

    public synchronized Result execute(Result r) {

        super.openConnection(host, port, isTLS);

        Result result = super.execute(r);

        super.closeConnection();

        return result;
    }

    protected void write(Result r) throws IOException, HsqlException {

        dataOutput.write("POST ".getBytes(ENCODING));
        dataOutput.write(path.getBytes(ENCODING));
        dataOutput.write(" HTTP/1.0\r\n".getBytes(ENCODING));
        dataOutput.write(
            "Content-Type: application/octet-stream\r\n".getBytes(ENCODING));
        dataOutput.write(("Content-Length: " + rowOut.size() + IDLENGTH
                          + "\r\n").getBytes(ENCODING));
        dataOutput.write("\r\n".getBytes(ENCODING));
        dataOutput.writeInt(r.getDatabaseId());
        dataOutput.writeLong(r.getSessionId());
        r.write(dataOutput, rowOut);
    }

    protected Result read() throws IOException, HsqlException {

        // fredt - for WebServer 4 lines should be skipped
        // for Servlet, number of lines depends on Servlet container
        // stop skipping after the blank line
        rowOut.reset();

        for (;;) {
            int count = InOutUtil.readLine(dataInput, (OutputStream) rowOut);

            if (count <= 2) {
                break;
            }
        }

        //
        Result result = Result.newResult(dataInput, rowIn);

        result.readAdditionalResults(this, dataInput, rowIn);

        return result;
    }

    protected void handshake() throws IOException {

        // We depend on the HTTP wrappings to assure end-to-end handshaking
    }
}
