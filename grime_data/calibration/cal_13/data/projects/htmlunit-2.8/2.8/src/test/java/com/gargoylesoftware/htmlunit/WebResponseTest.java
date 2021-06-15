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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link WebResponse}.
 *
 * @version $Revision: 5829 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebResponseTest extends WebDriverTestCase {

    /**
     * Verifies that when no encoding header is provided, encoding may be recognized with its Byte Order Mark.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u6211\u662F\u6211\u7684 \u064A\u0627 \u0623\u0647\u0644\u0627\u064B")
    public void recognizeBOM() throws Exception {
        recognizeBOM("UTF-8",    new byte[] {(byte) 0xef, (byte) 0xbb, (byte) 0xbf});
        recognizeBOM("UTF-16BE", new byte[] {(byte) 0xfe, (byte) 0xff});
        recognizeBOM("UTF-16LE", new byte[] {(byte) 0xff, (byte) 0xfe});
    }

    private void recognizeBOM(final String encoding, final byte[] markerBytes) throws Exception {
        final String html = "<html><head><script src='foo.js'></script></head><body></body></html>";

        // see http://en.wikipedia.org/wiki/Byte_Order_Mark
        final byte[] script = getModifiedContent("alert('" + getExpectedAlerts()[0]  + "');").getBytes(encoding);

        setWriteContentAsBytes_(true);
        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(ArrayUtils.addAll(markerBytes, script), 200, "OK", "text/javascript");
        webConnection.setResponse(URL_FIRST, html);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void encoding() throws Exception {
        final String title = "\u6211\u662F\u6211\u7684FOCUS";
        final String content =
            "<html><head>\n"
            + "<title>" + title + "</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        webConnection.setResponse(URL_FIRST, content.getBytes("UTF-8"), 200, "OK", "text/html", emptyList);
        client.setWebConnection(webConnection);
        final WebRequest request = new WebRequest(URL_FIRST);
        request.setCharset("UTF-8");
        final HtmlPage page = client.getPage(request);
        assertEquals(title, page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void quotedCharset() throws Exception {
        final String xml
            = "<books id='myId'>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        final List< ? extends NameValuePair> emptyList = Collections.emptyList();
        conn.setResponse(URL_FIRST, xml, HttpStatus.SC_OK, "OK", "text/xml; charset=\"ISO-8859-1\"", emptyList);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetInMetaTag() throws Exception {
        final String html
            = "<html>\n"
            + "<head><meta content='text/html; charset=utf-8' http-equiv='Content-Type'/></head>\n"
            + "<body>foo</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(html);
        assertEquals("utf-8", page.getWebResponse().getContentCharsetOrNull());
    }

    /**
     * Test that extracting charset from Content-Type header is forgiving.
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharset() throws Exception {
        illegalCharset("text/html; text/html; charset=ISO-8859-1;", "ISO-8859-1");
        illegalCharset("text/html; charset=UTF-8; charset=UTF-8", "UTF-8");
        illegalCharset("text/html; charset=#sda+s", TextUtil.DEFAULT_CHARSET);
        illegalCharset("text/html; charset=UnknownCharset", TextUtil.DEFAULT_CHARSET);
    }

    private void illegalCharset(final String cntTypeHeader, final String expectedCharset) throws Exception {
        final MockWebConnection conn = new MockWebConnection();
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Type", cntTypeHeader));
        conn.setDefaultResponse("<html/>", 200, "OK", "text/html", headers);
        final WebClient webClient = getWebClient();
        webClient.setWebConnection(conn);

        final Page page = webClient.getPage(URL_FIRST);
        assertEquals(expectedCharset, page.getWebResponse().getContentCharset());
        assertEquals(cntTypeHeader, page.getWebResponse().getResponseHeaderValue("Content-Type"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void responseHeaders() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", ResponseHeadersServlet.class);
        startWebServer("./", null, servlets);
        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        assertEquals("some_value", page.getWebResponse().getResponseHeaderValue("some_header"));
    }

    /**
     * Servlet for {@link #responseHeaders()}.
     */
    public static class ResponseHeadersServlet extends HttpServlet {

        private static final long serialVersionUID = -8815307540281233182L;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setHeader("some_header", "some_value");
            final Writer writer = response.getWriter();
            writer.write("<html/>");
            writer.close();
        }
    }

    /**
     * Stop the WebServer.
     * @throws Exception if it fails
     */
    @After
    public void stopServer() throws Exception {
        stopWebServer();
    }
}
