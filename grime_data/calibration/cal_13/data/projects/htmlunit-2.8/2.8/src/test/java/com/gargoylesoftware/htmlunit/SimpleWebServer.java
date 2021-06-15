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
package com.gargoylesoftware.htmlunit;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * A very simple implementation of a Web Server.
 * This covers some cases which are not possible with Jetty.
 *
 * @version $Revision: 5665 $
 * @author Ahmed Ashour
 */
public class SimpleWebServer extends WebTestCase {

    private final int port_;
    private final byte[] defaultResponse_;
    private ServerSocket server_;

    /**
     * Constructs a new SimpleWebServer.
     * @param port the port
     * @param defaultResponse the default response, must contain all bytes (to start with "HTTP/1.1 200 OK")
     */
    public SimpleWebServer(final int port, final byte[] defaultResponse) {
        port_ = port;
        defaultResponse_ = defaultResponse;
    }

    /**
     * Starts the server.
     * @throws IOException if an error occurs
     */
    public void start() throws IOException {
        server_ = new ServerSocket(port_);
        new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        final Socket socket = server_.accept();
                        final InputStream in = socket.getInputStream();
                        final CharArrayWriter writer = new CharArrayWriter();
                        int i;
                        while ((i = in.read()) != -1) {
                            writer.append((char) i);
                            if (i == '\n' && writer.toString().endsWith("\r\n\r\n")) {
                                break;
                            }
                        }
                        final OutputStream out = socket.getOutputStream();
                        out.write(defaultResponse_);
                        out.close();
                    }
                }
                catch (final SocketException e) {
                    //ignore
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Stops the server.
     * @throws IOException if an error occurs
     */
    public void stop() throws IOException {
        server_.close();
    }
}
