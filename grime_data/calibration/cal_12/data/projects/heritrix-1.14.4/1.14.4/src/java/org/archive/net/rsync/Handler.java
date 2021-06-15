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
package org.archive.net.rsync;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * A protocol handler that uses native rsync client to do copy.
 * You need to define the system property
 * <code>-Djava.protocol.handler.pkgs=org.archive.net</code> to add this handler
 * to the java.net.URL set.  Assumes rsync is in path.  Define
 * system property
 * <code>-Dorg.archive.net.rsync.RsyncUrlConnection.path=PATH_TO_RSYNC</code> to
 * pass path to rsync. Downloads to <code>java.io.tmpdir</code>.
 * @author stack
 */
public class Handler extends URLStreamHandler {
    protected URLConnection openConnection(URL u) {
        return new RsyncURLConnection(u);
    }

    /**
     * Main dumps rsync file to STDOUT.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)
    throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java java " +
                "-Djava.protocol.handler.pkgs=org.archive.net " +
                "org.archive.net.rsync.Handler RSYNC_URL");
            System.exit(1);
        }
        URL u = new URL(args[0]);
        URLConnection connect = u.openConnection();
        // Write download to stdout.
        final int bufferlength = 4096;
        byte [] buffer = new byte [bufferlength];
        InputStream is = connect.getInputStream();
        try {
            for (int count = is.read(buffer, 0, bufferlength);
                    (count = is.read(buffer, 0, bufferlength)) != -1;) {
                System.out.write(buffer, 0, count);
            }
            System.out.flush();
        } finally {
            is.close();
        }
    }
}