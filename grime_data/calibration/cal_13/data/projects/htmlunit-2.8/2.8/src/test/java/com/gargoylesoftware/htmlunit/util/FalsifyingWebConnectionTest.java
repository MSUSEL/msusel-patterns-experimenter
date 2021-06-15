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
package com.gargoylesoftware.htmlunit.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link FalsifyingWebConnection}.
 *
 * @version $Revision: 5660 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class FalsifyingWebConnectionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void blockSomeRequests() throws Exception {
        final WebClient webClient = getWebClient();

        final String html = "<html><head>\n"
            + "<script src='http://www.google-analytics.com/ga.js'></script>\n"
            + "<script src='myJs.js'></script>\n"
            + "</head><body>\n"
            + "hello world!"
            + "<body></html>";

        final MockWebConnection mockConnection = new MockWebConnection();
        mockConnection.setResponse(URL_FIRST, html);
        mockConnection.setResponse(new URL(URL_FIRST.toExternalForm() + "myJs.js"), "alert('hello');");
        webClient.setWebConnection(mockConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // create a WebConnection that filters google-analytics scripts
        // c'tor configures connection on the web client
        new FalsifyingWebConnection(webClient) {
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                if ("www.google-analytics.com".equals(request.getUrl().getHost())) {
                    return createWebResponse(request, "", "application/javascript"); // -> empty script
                }
                return super.getResponse(request);
            }
        };

        webClient.getPage(URL_FIRST);

        assertEquals(2, mockConnection.getRequestCount());
        final String[] expectedAlerts = {"hello"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simulateHttpError() throws Exception {
        final WebClient webClient = getWebClient();

        final String html = "<html><head>\n"
            + "<script src='myJs.js'></script>\n"
            + "</head><body>\n"
            + "hello world!"
            + "<body></html>";

        final MockWebConnection mockConnection = new MockWebConnection();
        mockConnection.setResponse(URL_FIRST, html);
        mockConnection.setResponse(new URL(URL_FIRST.toExternalForm() + "myJs.js"), "alert('hello');");
        webClient.setWebConnection(mockConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // first test this "site" when everything is ok
        webClient.getPage(URL_FIRST);
        final String[] expectedAlerts = {"hello"};
        assertEquals(expectedAlerts, collectedAlerts);

        // now simulate some server problems

        // create a WebConnection that filters google-analytics scripts
        // c'tor configures connection on the web client
        new FalsifyingWebConnection(webClient) {
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                if (request.getUrl().getPath().endsWith(".js")) {
                    return createWebResponse(request, "", "text/html", 500, "Application Error");
                }
                return super.getResponse(request);
            }
        };

        try {
            webClient.getPage(URL_FIRST);
            Assert.fail("HTTP Exception expected!");
        }
        catch (final FailingHttpStatusCodeException e) {
            // that's fine
        }
    }
}
