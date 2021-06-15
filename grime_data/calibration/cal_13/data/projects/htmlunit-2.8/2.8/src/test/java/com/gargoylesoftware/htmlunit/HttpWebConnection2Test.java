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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision: 5867 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection2Test extends WebDriverTestCase {

    /**
     * Tests a simple POST request.
     * @throws Exception if the test fails
     */
    @Test
    public void post() throws Exception {
        final String html = "<html><body><form action='foo' method='post' accept-charset='iso-8859-1'>\n"
            + "<input name='text1' value='me &amp;amp; you'>\n"
            + "<textarea name='text2'>Hello\nworld!</textarea>\n"
            + "<input type='submit' id='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html, getDefaultUrl());
        driver.findElement(By.id("submit")).click();

        // better would be to look at the HTTP traffic directly
        // rather than to examine a request that has been rebuilt but...
        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();

        assertEquals("ISO-8859-1", lastRequest.getCharset());
        assertEquals(null, lastRequest.getProxyHost());
        assertEquals(null, lastRequest.getRequestBody());
        assertEquals(getDefaultUrl() + "foo", lastRequest.getUrl());
        final String expectedHeaders = "Connection: keep-alive\n"
            + "Content-Length: 48\n"
            + "Content-Type: application/x-www-form-urlencoded\n"
            + "Host: localhost:12345\n"
            + "Referer: http://localhost:12345/\n"
            + "User-Agent: " + getBrowserVersion().getUserAgent() + "\n";
        assertEquals(expectedHeaders, headersToString(lastRequest));
        assertEquals(FormEncodingType.URL_ENCODED, lastRequest.getEncodingType());
        assertEquals(HttpMethod.POST, lastRequest.getHttpMethod());
        assertEquals(0, lastRequest.getProxyPort());
        final List<NameValuePair> parameters = lastRequest.getRequestParameters();
        assertEquals(2, parameters.size());
        for (final NameValuePair pair : parameters) {
            if (pair.getName().equals("text1")) {
                assertEquals("me &amp; you", pair.getValue());
            }
            else {
                assertEquals("Hello\r\nworld!", pair.getValue());
            }
        }
    }

    private String headersToString(final WebRequest request) {
        // why doesn't HtmlUnit send these headers whereas Firefox does?
        final List<String> ignoredHeaders = Arrays.asList("accept", "accept-charset", "accept-encoding",
            "accept-language", "keep-alive");
        final List<String> caseInsensitiveHeaders = Arrays.asList("connection");

        // ensure ordering for comparison
        final Map<String, String> headers = new TreeMap<String, String>(request.getAdditionalHeaders());

        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            final String headerName = headerEntry.getKey();
            if (ignoredHeaders.contains(headerName.toLowerCase())) {
                continue;
            }
            sb.append(headerName);
            sb.append(": ");
            if (caseInsensitiveHeaders.contains(headerName.toLowerCase())) {
                sb.append(headerEntry.getValue().toLowerCase());
            }
            else {
                sb.append(headerEntry.getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "<body>host,user-agent,", IE = "<body>")
    public void headerOrder() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", HeaderOrderServlet.class);
        startWebServer("./", null, servlets);
        final WebDriver driver = getWebDriver();

        driver.get("http://localhost:" + PORT + "/test");
        final String body = driver.getPageSource().toLowerCase().replaceAll("\\s", "");
        assertTrue(body.contains(getExpectedAlerts()[0]));
    }

    /**
     * Servlet for {@link #headerOrder()}.
     */
    public static class HeaderOrderServlet extends HttpServlet {

        private static final long serialVersionUID = -6422333931337496616L;

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                writer.write(en.nextElement() + ",");
            }
            writer.close();
        }
    }
}
