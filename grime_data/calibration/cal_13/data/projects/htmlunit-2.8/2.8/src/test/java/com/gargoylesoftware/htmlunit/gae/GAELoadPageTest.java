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
package com.gargoylesoftware.htmlunit.gae;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests to see if pages can be loaded using <a href="http://code.google.com/appengine/">Google App Engine</a>
 * support.
 *
 * @version $Revision: 5922 $
 * @author Amit Manjhi
 * @author Marc Guillemot
 */
@RunWith(GAETestRunner.class)
public class GAELoadPageTest {

    // can't use constants from {@link WebtestCase} as it forces to load forbidden classes
    private static final URL FIRST_URL;

    static {
        try {
            FIRST_URL = new URL("http://localhost:8080/");
        }
        catch (final MalformedURLException e) {
            throw new Error("Can't create test urls", e);
        }
    }

    /**
     * Tests asynchronous use of XMLHttpRequest, using Mozilla style object creation.
     * @throws Exception if the test fails
     */
    @Test
    public void testAsyncUse() throws Exception {
        final URL secondUrl = new URL(FIRST_URL, "/second/");

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <title>XMLHttpRequest Test</title>\n"
            + "    <script>\n"
            + "      var request;\n"
            + "      function testAsync() {\n"
            + "        if (window.XMLHttpRequest)\n"
            + "          request = new XMLHttpRequest();\n"
            + "        else if (window.ActiveXObject)\n"
            + "          request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "        request.onreadystatechange = onReadyStateChange;\n"
            + "        alert(request.readyState);\n"
            + "        request.open('GET', '/second/', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "      function onReadyStateChange() {\n"
            + "        alert(request.readyState);\n"
            + "        if (request.readyState == 4)\n"
            + "          alert(request.responseText);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='testAsync()'>\n"
            + "  </body>\n"
            + "</html>";

        final String xml =
              "<xml2>\n"
            + "<content2>sdgxsdgx</content2>\n"
            + "<content2>sdgxsdgx2</content2>\n"
            + "</xml2>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(FIRST_URL, html);
        conn.setResponse(secondUrl, xml, "text/xml");
        client.setWebConnection(conn);

        client.getPage(FIRST_URL);

        final int executedJobs = client.getJavaScriptEngine().pumpEventLoop(1000);
        final String[] alerts = {String.valueOf(XMLHttpRequest.STATE_UNINITIALIZED),
            String.valueOf(XMLHttpRequest.STATE_LOADING),
            String.valueOf(XMLHttpRequest.STATE_LOADING),
            String.valueOf(XMLHttpRequest.STATE_LOADED),
            String.valueOf(XMLHttpRequest.STATE_INTERACTIVE),
            String.valueOf(XMLHttpRequest.STATE_COMPLETED), xml};
        assertEquals(Arrays.asList(alerts).toString(), collectedAlerts.toString());
        assertEquals(1, executedJobs);
    }

    /**
     * Test that a JS job using setInterval is processed on GAE.
     * @throws IOException if fails to get page.
     * @throws FailingHttpStatusCodeException if fails to get page.
     */
    @Test
    public void setInterval() throws FailingHttpStatusCodeException, IOException {
        final String html = "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      setInterval(\"alert('hello')\", 100);"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);
        client.getPage(FIRST_URL);

        assertEquals(0, collectedAlerts.size());

        // pump but not long enough
        int executedJobs = client.getJavaScriptEngine().pumpEventLoop(50);
        assertEquals(0, collectedAlerts.size());

        // pump a bit more
        executedJobs = client.getJavaScriptEngine().pumpEventLoop(100);
        assertEquals(Arrays.asList("hello"), collectedAlerts);
        assertEquals(1, executedJobs);

        // pump even more
        executedJobs = client.getJavaScriptEngine().pumpEventLoop(250);
        assertEquals(Arrays.asList("hello", "hello", "hello"), collectedAlerts);
        assertEquals(2, executedJobs);
    }

    /**
     * Test that a JS job using setTimeout is processed on GAE.
     * @throws IOException if fails to get page.
     * @throws FailingHttpStatusCodeException if fails to get page.
     */
    @Test
    public void setTimeout() throws FailingHttpStatusCodeException, IOException {
        final String html = "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      setTimeout(\"alert('hello')\", 0);"
            + "      setTimeout(\"alert('hello again')\", 200);"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);
        client.getPage(FIRST_URL);

        int executedJobs = client.getJavaScriptEngine().pumpEventLoop(20);
        assertEquals(Arrays.asList("hello"), collectedAlerts);
        assertEquals(1, executedJobs);

        executedJobs = client.getJavaScriptEngine().pumpEventLoop(100);
        assertEquals(Arrays.asList("hello"), collectedAlerts);
        assertEquals(0, executedJobs);

        executedJobs = client.getJavaScriptEngine().pumpEventLoop(200);
        assertEquals(Arrays.asList("hello", "hello again"), collectedAlerts);
        assertEquals(1, executedJobs);
    }
}
