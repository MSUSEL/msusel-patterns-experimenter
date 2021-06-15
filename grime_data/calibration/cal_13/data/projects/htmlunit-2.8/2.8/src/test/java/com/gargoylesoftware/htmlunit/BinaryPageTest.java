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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for binary content.
 *
 * @version $Revision: 5834 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BinaryPageTest extends WebServerTestCase {

    private SimpleWebServer simpleWebServer_;

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void binary() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/big", BinaryServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final Page page = client.getPage("http://localhost:" + PORT + "/big");
        assertTrue(page instanceof UnexpectedPage);
    }

    /**
     * Servlet for {@link #binary()}.
     */
    public static class BinaryServlet extends HttpServlet {

        private static final long serialVersionUID = -7979736717576809489L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            final int length = 1000;
            response.setContentLength(length);
            final byte[] buffer = new byte[1024];
            final OutputStream out = response.getOutputStream();
            for (int i = length / buffer.length; i >= 0; i--) {
                out.write(buffer);
            }
            out.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void chunkedBigContent() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/bigChunked", ChunkedBigContentServlet.class);
        startWebServer("./", null, servlets);

        final WebClient client = getWebClient();

        final Page page = client.getPage("http://localhost:" + PORT + "/bigChunked");
        assertTrue(page instanceof UnexpectedPage);
    }

    /**
     * Servlet for {@link #chunkedBigContent()}.
     */
    public static class ChunkedBigContentServlet extends HttpServlet {

        private static final long serialVersionUID = 8341425725771441517L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setHeader("Transfer-Encoding", "chunked");
            final int length = 60 * 1024 * 1024;
            final byte[] buffer = new byte[1024];
            final OutputStream out = response.getOutputStream();
            for (int i = length / buffer.length; i >= 0; i--) {
                out.write(buffer);
            }
            out.close();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void chunked() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
            + "Transfer-Encoding: chunked\r\n\r\n"
            + "5\r\n"
            + "ABCDE\r\n"
            + "5\r\n"
            + "FGHIJ\r\n"
            + "5\r\n"
            + "KLMNO\r\n"
            + "5\r\n"
            + "PQRST\r\n"
            + "5\r\n"
            + "UVWXY\r\n"
            + "1\r\n"
            + "Z\r\n"
            + "0\r\n\r\n";

        simpleWebServer_ = new SimpleWebServer(PORT, response.getBytes());
        simpleWebServer_.start();
        final WebClient client = getWebClient();

        final TextPage page = client.getPage("http://localhost:" + PORT + "/chunked");
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", page.getContent());
    }

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void stopServer() throws Exception {
        if (simpleWebServer_ != null) {
            simpleWebServer_.stop();
        }
    }
}
