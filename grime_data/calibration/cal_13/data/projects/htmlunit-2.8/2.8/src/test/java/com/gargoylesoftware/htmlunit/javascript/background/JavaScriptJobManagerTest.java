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
package com.gargoylesoftware.htmlunit.javascript.background;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link JavaScriptJobManagerImpl} using the full HtmlUnit stack. Minimal unit tests
 * which do not use the full HtmlUnit stack go in {@link JavaScriptJobManagerMinimalTest}.
 *
 * @version $Revision: 5580 $
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JavaScriptJobManagerTest extends WebTestCase {
    private static final Log LOG = LogFactory.getLog(JavaScriptJobManagerTest.class);

    private long startTime_;

    private void startTimedTest() {
        startTime_ = System.currentTimeMillis();
    }

    private void assertMaxTestRunTime(final long maxRunTimeMilliseconds) {
        final long endTime = System.currentTimeMillis();
        final long runTime = endTime - startTime_;
        assertTrue("\nTest took too long to run and results may not be accurate. Please try again. "
            + "\n  Actual Run Time: "
            + runTime
            + "\n  Max Run Time: "
            + maxRunTimeMilliseconds, runTime < maxRunTimeMilliseconds);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setClearTimeoutUsesManager() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setTimeout(doAlert, 10000);\n"
            + "    }\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<a onclick='clearTimeout(threadID);' id='clickme'/>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        startTimedTest();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());
        final HtmlAnchor a = page.getHtmlElementById("clickme");
        a.click();
        jobManager.waitForJobs(7000);
        assertEquals(0, jobManager.getJobCount());
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertMaxTestRunTime(10000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setClearIntervalUsesManager() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      threadID = setInterval(doAlert, 100);\n"
            + "    }\n"
            + "    var iterationNumber=0;\n"
            + "    function doAlert() {\n"
            + "      alert('blah');\n"
            + "      if (++iterationNumber >= 3) {\n"
            + "        clearInterval(threadID);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        startTimedTest();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);
        assertEquals(1, jobManager.getJobCount());
        jobManager.waitForJobs(1000);
        assertEquals(0, jobManager.getJobCount());
        assertEquals(Collections.nCopies(3, "blah"), collectedAlerts);
        assertMaxTestRunTime(1000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void navigationStopThreadsInChildWindows() throws Exception {
        final String firstContent = "<html><head><title>First</title></head><body>\n"
            + "<iframe id='iframe1' src='"
            + URL_SECOND
            + "'>\n"
            + "<a href='"
            + URL_THIRD.toExternalForm()
            + "' id='clickme'>click me</a>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body>\n"
            + "<script>\n"
            + "setInterval('', 30000);\n"
            + "</script>\n"
            + "</body></html>";
        final String thirdContent = "<html><head><title>Third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlInlineFrame iframe = page.getHtmlElementById("iframe1");
        final JavaScriptJobManager mgr = iframe.getEnclosedWindow().getJobManager();
        Assert.assertEquals("inner frame should show child thread", 1, mgr.getJobCount());

        final HtmlAnchor anchor = page.getHtmlElementById("clickme");
        if (LOG.isDebugEnabled()) {
            LOG.debug("before click");
        }
        final HtmlPage newPage = anchor.click();
        if (LOG.isDebugEnabled()) {
            LOG.debug("after click");
        }

        Assert.assertEquals("new page should load", "Third", newPage.getTitleText());
        Assert.assertEquals("frame should be gone", 0, newPage.getFrames().size());

        mgr.waitForJobs(10000);
        Assert.assertEquals("job manager should have no jobs left", 0, mgr.getJobCount());
    }

    /**
     * Test for bug 1728883 that makes sure closing a window prevents a
     * recursive setTimeout from continuing forever.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void interruptAllWithRecursiveSetTimeout() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    var threadID;\n"
            + "    function test() {\n"
            + "      alert('ping');\n"
            + "      threadID = setTimeout(test, 5);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        final HtmlPage page = loadPage(content, collectedAlerts);
        final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
        assertNotNull(jobManager);

        // Not perfect, but 100 chances to start should be enough for a loaded system
        Thread.sleep(500);

        Assert.assertFalse("At least one alert should have fired by now", collectedAlerts.isEmpty());
        ((TopLevelWindow) page.getEnclosingWindow()).close();

        // 100 chances to stop
        jobManager.waitForJobs(500);

        final int finalValue = collectedAlerts.size();

        // 100 chances to fail
        jobManager.waitForJobs(500);

        Assert.assertEquals("No new alerts should have happened", finalValue, collectedAlerts.size());
    }
}
