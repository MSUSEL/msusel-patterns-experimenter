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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision: 5834 $
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionTest extends WebServerTestCase {

    /**
     * Assert that the two byte arrays are equal.
     * @param expected the expected value
     * @param actual the actual value
     */
    public static void assertEquals(final byte[] expected, final byte[] actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two byte arrays are equal.
     * @param message the message to display on failure
     * @param expected the expected value
     * @param actual the actual value
     */
    public static void assertEquals(final String message, final byte[] expected, final byte[] actual) {
        assertEquals(message, expected, actual, expected.length);
    }

    /**
     * Assert that the two byte arrays are equal.
     * @param message the message to display on failure
     * @param expected the expected value
     * @param actual the actual value
     * @param length How many characters at the beginning of each byte array will be compared
     */
    public static void assertEquals(
            final String message, final byte[] expected, final byte[] actual, final int length) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            Assert.fail(message);
        }
        if (expected.length < length || actual.length < length) {
            Assert.fail(message);
        }
        for (int i = 0; i < length; i++) {
            Assert.assertEquals(message, expected[i], actual[i]);
        }
    }

    /**
     * Assert that the two input streams are the same.
     * @param expected the expected value
     * @param actual the actual value
     * @throws IOException if an IO problem occurs during comparison
     */
    public static void assertEquals(final InputStream expected, final InputStream actual) throws IOException {
        assertEquals(null, expected, actual);
    }

    /**
     * Assert that the two input streams are the same.
     * @param message the message to display on failure
     * @param expected the expected value
     * @param actual the actual value
     * @throws IOException if an IO problem occurs during comparison
     */
    public static void assertEquals(final String message, final InputStream expected,
            final InputStream actual) throws IOException {

        if (expected == null && actual == null) {
            return;
        }

        if (expected == null || actual == null) {
            try {
                Assert.fail(message);
            }
            finally {
                try {
                    if (expected != null) {
                        expected.close();
                    }
                }
                finally {
                    if (actual != null) {
                        actual.close();
                    }
                }
            }
        }

        InputStream expectedBuf = null;
        InputStream actualBuf = null;
        try {
            expectedBuf = new BufferedInputStream(expected);
            actualBuf = new BufferedInputStream(actual);

            final byte[] expectedArray = new byte[2048];
            final byte[] actualArray = new byte[2048];

            int expectedLength = expectedBuf.read(expectedArray);
            while (true) {

                final int actualLength = actualBuf.read(actualArray);
                Assert.assertEquals(message, expectedLength, actualLength);

                if (expectedLength == -1) {
                    break;
                }

                assertEquals(message, expectedArray, actualArray, expectedLength);
                expectedLength = expectedBuf.read(expectedArray);
            }
        }
        finally {
            try {
                if (expectedBuf != null) {
                    expectedBuf.close();
                }
            }
            finally {
                if (actualBuf != null) {
                    actualBuf.close();
                }
            }
        }
    }

    /**
     * Tests creation of a web response.
     * @throws Exception if the test fails
     */
    @Test
    public void makeWebResponse() throws Exception {
        final URL url = new URL("http://htmlunit.sourceforge.net/");
        final String content = "<html><head></head><body></body></html>";
        final DownloadedContent downloadedContent = new DownloadedContent.InMemory(content.getBytes());
        final int httpStatus = HttpStatus.SC_OK;
        final long loadTime = 500L;

        final ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 0);
        final StatusLine statusLine = new BasicStatusLine(protocolVersion, HttpStatus.SC_OK, null);
        final HttpResponse httpResponse = new BasicHttpResponse(statusLine);

        final HttpEntity responseEntity = new StringEntity(content);
        httpResponse.setEntity(responseEntity);

        final HttpWebConnection connection = new HttpWebConnection(getWebClient());
        final Method method = connection.getClass().getDeclaredMethod("makeWebResponse",
                HttpResponse.class, WebRequest.class, DownloadedContent.class, long.class);
        method.setAccessible(true);
        final WebResponse response = (WebResponse) method.invoke(connection,
                httpResponse, new WebRequest(url), downloadedContent, new Long(loadTime));

        Assert.assertEquals(httpStatus, response.getStatusCode());
        Assert.assertEquals(url, response.getWebRequest().getUrl());
        Assert.assertEquals(loadTime, response.getLoadTime());
        Assert.assertEquals(content, response.getContentAsString());
        assertEquals(content.getBytes(), IOUtils.toByteArray(response.getContentAsStream()));
        assertEquals(new ByteArrayInputStream(content.getBytes()), response.getContentAsStream());
    }

    /**
     * Tests Jetty.
     * @throws Exception on failure
     */
    @Test
    public void jettyProofOfConcept() throws Exception {
        startWebServer("./");

        final WebClient client = getWebClient();
        final Page page = client.getPage("http://localhost:" + PORT + "/src/test/resources/event_coordinates.html");
        final WebConnection defaultConnection = client.getWebConnection();
        Assert.assertTrue(
                "HttpWebConnection should be the default",
                HttpWebConnection.class.isInstance(defaultConnection));
        Assert.assertTrue("Response should be valid HTML", HtmlPage.class.isInstance(page));
    }

    /**
     * Test for feature request 1438216: HttpWebConnection should allow extension to create the HttpClient.
     * @throws Exception if the test fails
     */
    @Test
    public void designedForExtension() throws Exception {
        startWebServer("./");

        final WebClient webClient = getWebClient();
        final boolean[] tabCalled = {false};
        final WebConnection myWebConnection = new HttpWebConnection(webClient) {
            @Override
            protected AbstractHttpClient createHttpClient() {
                tabCalled[0] = true;
                return new DefaultHttpClient();
            }
        };

        webClient.setWebConnection(myWebConnection);
        webClient.getPage("http://localhost:" + PORT + "/LICENSE.txt");
        Assert.assertTrue("createHttpClient has not been called", tabCalled[0]);
    }

    /**
     * Test that the right file part is built for a file that doesn't exist.
     * @throws Exception if the test fails
     */
    @Test
    public void buildFilePart() throws Exception {
        final String encoding = "ISO8859-1";
        final KeyDataPair pair = new KeyDataPair("myFile", new File("this/doesnt_exist.txt"), "text/plain", encoding);
        final InputStreamBody part = (InputStreamBody) new HttpWebConnection(
                getWebClient()).buildFilePart(pair, encoding);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        part.writeTo(baos);
        //FIMXE Last changes does not make it a very useful test...
        final String expected = "";
        Assert.assertEquals(expected, baos.toString(encoding));
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void unicode() throws Exception {
        startWebServer("./");
        final WebClient client = getWebClient();
        client.getPage("http://localhost:" + PORT + "/src/test/resources/event_coordinates.html?param=\u00F6");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyPut() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", EmptyPutServlet.class);
        startWebServer("./", null, servlets);

        final String[] expectedAlerts = {"1"};
        final WebClient client = getWebClient();
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage("http://localhost:" + PORT + "/test");
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Servlet for {@link #emptyPut()}.
     */
    public static class EmptyPutServlet extends ServletContentWrapper {
        private static final long serialVersionUID = -8674500186401667484L;

        /** Constructor. */
        public EmptyPutServlet() {
            super("<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var xhr = window.ActiveXObject?new ActiveXObject('Microsoft.XMLHTTP'):new XMLHttpRequest();\n"
                + "      xhr.open('PUT', '" + "http://localhost:" + PORT + "/test" + "', true);\n"
                + "      xhr.send();\n"
                + "      alert(1);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>");
        }
    }
}
