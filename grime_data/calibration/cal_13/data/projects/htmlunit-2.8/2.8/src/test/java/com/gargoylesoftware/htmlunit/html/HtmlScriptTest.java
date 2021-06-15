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

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlScript}.
 *
 * @version $Revision: 5905 $
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlScriptTest extends WebTestCase {

    /**
     * Verifies that a failing HTTP status code for a JavaScript file request (like a 404 response)
     * results in a {@link FailingHttpStatusCodeException}, depending on how the client has been
     * configured.
     *
     * @see WebClient#isThrowExceptionOnFailingStatusCode()
     * @throws Exception if an error occurs
     */
    @Test
    public void testBadExternalScriptReference() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script src='inexistent.js'></script>\n"
                + "</head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("inexistent", 404, "Not Found", "text/html");
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        try {
            client.getPage(URL_FIRST);
            fail("Should throw.");
        }
        catch (final FailingHttpStatusCodeException e) {
            final String url = URL_FIRST.toExternalForm();
            assertTrue("exception contains URL of failing script", e.getMessage().indexOf(url) > -1);
            assertEquals(404, e.getStatusCode());
            assertEquals("Not Found", e.getStatusMessage());
        }

        client.setThrowExceptionOnFailingStatusCode(false);

        try {
            client.getPage(URL_FIRST);
        }
        catch (final FailingHttpStatusCodeException e) {
            fail("Should not throw.");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><body><script id='s'>var foo = 132;</script></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlScript script = page.getHtmlElementById("s");
        assertEquals("", script.asText());
    }

    /**
     * Verifies that the weird script src attribute used by the jQuery JavaScript library is
     * ignored silently (bug 1695279).
     * @throws Exception if the test fails
     */
    @Test
    public void testInvalidJQuerySrcAttribute() throws Exception {
        loadPage("<html><body><script src='//:'></script></body></html>");
    }

    /**
     * Verifies that if a script element executes "window.location.href=someotherpage", then subsequent
     * script tags, and any onload handler for the original page do not run.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "First script executes", "Second page loading" })
    public void testChangingLocationSkipsFurtherScriptsOnPage() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body onload='alert(\"body onload executing but should be skipped\")'>\n"
            + "<script>alert('First script executes')</script>\n"
            + "<script>window.location.href='" + URL_SECOND + "'</script>\n"
            + "<script>alert('Third script executing but should be skipped')</script>\n"
            + "</body></html>";

        final String secondPage
            = "<html><head></head><body>\n"
            + "<script>alert('Second page loading')</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondPage);
        loadPageWithAlerts(html);
    }

    /**
     * Verifies that a script element is not run when it is cloned.
     * See bug 1707788.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("a")
    public void testScriptIsNotRunWhenCloned() throws Exception {
        final String html = "<html><body onload='document.body.cloneNode(true)'>\n"
            + "<script>alert('a')</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "deferred", "normal", "onload" }, IE = { "normal", "deferred", "onload" })
    public void testDefer() throws Exception {
        final String html = "<html><head>\n"
            + "<script defer>alert('deferred')</script>\n"
            + "<script>alert('normal')</script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onload\")'>test</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }
}
