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
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlFrame}.
 *
 * @version $Revision: 5569 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlFrameTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void srcOfBlankAndEmpty() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame src='' id='frame1'>\n"
            + "    <frame src='about:blank' id='frame2'>\n"
            + "</frameset></html>";
        final HtmlPage page = loadPage(html);

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        Assert.assertEquals("frame1", "", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        Assert.assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame id='frame1'>\n"
            + "    <frame onload='alert(this.tagName)' id='frame2'>\n"
            + "</frameset></html>";
        final String[] expectedAlerts = {"FRAME"};

        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        Assert.assertEquals("frame1", "", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        Assert.assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void documentWrite() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame src='' name='frame1' id='frame1'>\n"
            + "    <frame onload=\"frame1.document.open();frame1.document.write("
            + "'<html><head><title>generated</title></head><body>generated</body></html>');"
            + "frame1.document.close()\"  id='frame2'>\n"
            + "</frameset></html>";
        final HtmlPage page = loadPage(html);

        assertEquals("first", page.getTitleText());

        final HtmlFrame frame1 = page.getHtmlElementById("frame1");
        Assert.assertEquals("frame1", "generated", ((HtmlPage) frame1.getEnclosedPage()).getTitleText());

        final HtmlFrame frame2 = page.getHtmlElementById("frame2");
        Assert.assertEquals("frame2", "", ((HtmlPage) frame2.getEnclosedPage()).getTitleText());
    }

    /**
     * Tests that frames are correctly deregistered even if not HTML.
     * @throws Exception if the test fails
     */
    @Test
    public void deregisterNonHtmlFrame() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='100%'>\n"
            + "    <frame src='foo.txt'>\n"
            + "</frameset></html>";
        webConnection.setDefaultResponse("foo", 200, "OK", "text/plain");
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        // loads something else to trigger frame de-registration
        webClient.getPage(URL_SECOND);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void failingHttpStatusCodeException() throws Exception {
        final String failingHtml = "<html><head><body>Not found</body></html>";

        final String firstHtml
            = "<html><head><title>First</title></head>\n"
            + "<frameset cols='130,*'>\n"
            + "  <frame scrolling='no' name='left' src='" + "failing_url" + "' frameborder='1' />\n"
            + "  <frame scrolling='auto' name='right' src='" + URL_THIRD + "' frameborder='1' />\n"
            + "  <noframes>\n"
            + "    <body>Frames not supported</body>\n"
            + "  </noframes>\n"
            + "</frameset>\n"
            + "</html>";

        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";
        final String thirdHtml  = "<html><head><title>Third</title></head><body></body></html>";

        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(failingHtml, 404, "No Found", "text/html");
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);
        webConnection.setResponse(URL_THIRD, thirdHtml);

        webClient.setWebConnection(webConnection);

        try {
            webClient.getPage(URL_FIRST);
            fail("Expected FailingHttpStatusCodeException");
        }
        catch (final FailingHttpStatusCodeException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    /**
     * Regression test for bug 1518195.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1518195&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void frameScriptReplaceOtherFrame() throws Exception {
        final String html =
            "<html><head><title>frames</title></head>\n"
            + "<frameset cols='180,*'>\n"
            + "<frame name='f1' src='1.html'/>\n"
            + "<frame name='f2' src='2.html'/>\n"
            + "</frameset>\n"
            + "</html>";

        final String frame1 = "<html><head><title>1</title></head>\n"
            + "<body>1"
            + "<script>\n"
            + "   parent.frames['f2'].location.href = '3.html';\n"
            + "</script>\n"
            + "</body></html>";

        final String frame3 = "<html><head><title>page 3</title></head><body></body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        webClient.setWebConnection(conn);

        conn.setDefaultResponse("<html><head><title>default</title></head><body></body></html>");
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(new URL(URL_FIRST, "1.html"), frame1);
        conn.setResponse(new URL(URL_FIRST, "3.html"), frame3);

        final HtmlPage page = webClient.getPage(URL_FIRST);

        assertEquals("page 3", ((HtmlPage) page.getFrameByName("f2").getEnclosedPage()).getTitleText());
    }

}
