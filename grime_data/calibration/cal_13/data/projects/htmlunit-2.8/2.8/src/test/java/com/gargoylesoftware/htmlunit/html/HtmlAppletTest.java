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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlApplet}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlAppletTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object HTMLAppletElement]", "[object HTMLAppletElement]" },
            IE = { "[object]", "[object]" })
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "    alert(document.applets[0]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <applet id='myId'></applet>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlApplet.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_appletDisabled() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "  <applet id='myId'>Your browser doesn't support applets</object>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlApplet appletNode = page.getHtmlElementById("myId");
        assertEquals("Your browser doesn't support applets", appletNode.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_appletEnabled() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "  <applet id='myId'>Your browser doesn't support applets</object>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        webClient.setWebConnection(connection);
        connection.setDefaultResponse(html);
        webClient.setAppletEnabled(true);
        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlApplet appletNode = page.getHtmlElementById("myId");
        assertEquals("", appletNode.asText()); // should we display something else?
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simpleInstantiation() throws Exception {
        final URL url = getClass().getResource("/applets/emptyApplet.html");

        final HtmlPage page = getWebClient().getPage(url);
        final HtmlApplet appletNode = page.getHtmlElementById("myApp");

        assertEquals("net.sourceforge.htmlunit.testapplets.EmptyApplet", appletNode.getApplet().getClass().getName());
    }
}
