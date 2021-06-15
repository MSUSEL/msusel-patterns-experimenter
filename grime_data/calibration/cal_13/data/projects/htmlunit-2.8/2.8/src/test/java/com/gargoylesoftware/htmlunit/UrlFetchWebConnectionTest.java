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

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Unit tests for {@link UrlFetchWebConnection}.
 *
 * @author Marc Guillemot
 * @version $Revision: 5879 $
 */
@RunWith(BrowserRunner.class)
public class UrlFetchWebConnectionTest extends WebDriverTestCase {
    /**
     * Tests a simple GET.
     * @throws Exception if the test fails
     */
    @Test
    public void get() throws Exception {
        doGetTest(getDefaultUrl());
    }

    /**
     * GET with query parameters.
     * @throws Exception if the test fails
     */
    @Test
    public void get_withQueryParameters() throws Exception {
        final URL url = new URL(getDefaultUrl() + "?a=b&c=d&e=f");
        doGetTest(url);
    }

    private void doGetTest(final URL url) throws Exception {
        // get with default WebConnection
        getMockWebConnection().setDefaultResponse("");
        loadPage2("", url);
        final WebRequest referenceRequest = getMockWebConnection().getLastWebRequest();

        // get with UrlFetchWebConnection
        final WebClient wc = getWebClient();
        wc.setWebConnection(new UrlFetchWebConnection(wc));
        wc.getPage(url);
        final WebRequest newRequest = getMockWebConnection().getLastWebRequest();

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    /**
     * Simple POST url-encoded.
     * @throws Exception if the test fails
     */
    @Test
    public void post() throws Exception {
        final WebRequest referenceRequest = getPostRequest(FormEncodingType.URL_ENCODED);

        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        final WebRequest newRequest = getPostRequest(FormEncodingType.URL_ENCODED);

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    private WebRequest getPostRequest(final FormEncodingType encoding) throws Exception {
        final String html = "<html><body><form action='foo' method='post' enctype='" + encoding.getName() + "'>\n"
            + "<input name='text1' value='me &amp;amp; you'>\n"
            + "<textarea name='text2'>Hello\nworld!</textarea>\n"
            + "<input type='submit' id='submit'>\n"
            + "</form></body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html, getDefaultUrl());
        driver.findElement(By.id("submit")).click();
        return getMockWebConnection().getLastWebRequest();
    }

    /**
     * Simple POST multipart.
     * This doesn't work currently and the test should be reworked as the boundary for the body varies.
     * @throws Exception if the test fails
     */
    @NotYetImplemented
    @Test
    public void post_multipart() throws Exception {
        final WebRequest referenceRequest = getPostRequest(FormEncodingType.MULTIPART);

        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        final WebRequest newRequest = getPostRequest(FormEncodingType.MULTIPART);

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    /**
     * Test a HEAD request with additional headers.
     * @throws Exception if the test fails
     */
    @Test
    public void head() throws Exception {
        final WebRequest referenceRequest = getHeadRequest();

        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        final WebRequest newRequest = getHeadRequest();

        // compare the two requests
        compareRequests(referenceRequest, newRequest);
    }

    private WebRequest getHeadRequest() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var request;\n"
            + "    if (window.XMLHttpRequest)\n"
            + "      request = new XMLHttpRequest();\n"
            + "    else if (window.ActiveXObject)\n"
            + "      request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "    request.open('HEAD', 'second.html', false);\n"
            + "    request.setRequestHeader('X-Foo', '123456');\n"
            + "    request.send('');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage2(html);

        return getMockWebConnection().getLastWebRequest();
    }

    private void compareRequests(final WebRequest referenceRequest, final WebRequest newRequest) {
        assertEquals(referenceRequest.getRequestBody(), newRequest.getRequestBody());
        assertEquals(referenceRequest.getCharset(), newRequest.getCharset());
        assertEquals(referenceRequest.getProxyHost(), newRequest.getProxyHost());
        assertEquals(referenceRequest.getUrl(), newRequest.getUrl());
        assertEquals(headersToString(referenceRequest), headersToString(newRequest));
        assertEquals(referenceRequest.getEncodingType(), newRequest.getEncodingType());
        assertEquals(referenceRequest.getHttpMethod(), newRequest.getHttpMethod());
        assertEquals(referenceRequest.getProxyPort(), newRequest.getProxyPort());
        assertEquals(referenceRequest.getRequestParameters().toString(), newRequest.getRequestParameters().toString());
    }

    private String headersToString(final WebRequest request) {
        final Set<String> caseInsensitiveHeaders = new HashSet<String>(Arrays.asList("Connection"));

        final StringBuilder sb = new StringBuilder();
        // ensure ordering for comparison
        final Map<String, String> headers = new TreeMap<String, String>(request.getAdditionalHeaders());
        for (final Entry<String, String> headerEntry : headers.entrySet()) {
            sb.append(headerEntry.getKey());
            sb.append(": ");
            if (caseInsensitiveHeaders.contains(headerEntry.getKey())) {
                sb.append(headerEntry.getValue().toLowerCase());
            }
            else {
                sb.append(headerEntry.getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
