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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WaitingRefreshHandlerTest}.
 *
 * @version $Revision: 5301 $
 * @author Brad Clarke
 */
@RunWith(BrowserRunner.class)
public final class WaitingRefreshHandlerTest extends WebTestCase {

    /**
     * Trying to cause an interrupt on a JavaScript thread due to meta redirect navigation.
     * @throws Exception if the test fails
     */
    @Test
    public void testRefreshOnJavascriptThread() throws Exception {
        final String firstContent = " <html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "function doRedirect() {\n"
            + "    window.location.href='" + URL_SECOND + "';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='setTimeout(doRedirect, 1);'>first page body</body>\n"
            + "</html>";
        final String secondContent = "<html>\n"
            + "<head><title>Meta Redirect Page</title>\n"
            + "<meta http-equiv='Refresh' content='1; URL=" + URL_THIRD + "'>\n"
            + "</head>\n"
            + "<body>redirect page body</body>\n"
            + "</html>";
        final String thirdContent = "<html>\n"
            + "<head><title>Expected Last Page</title></head>\n"
            + "<body>Success!</body>\n"
            + "</html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setRefreshHandler(new WaitingRefreshHandler(0));

        client.getPage(URL_FIRST);
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        final HtmlPage pageAfterWait = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
        assertEquals("Expected Last Page", pageAfterWait.getTitleText());
    }
}
