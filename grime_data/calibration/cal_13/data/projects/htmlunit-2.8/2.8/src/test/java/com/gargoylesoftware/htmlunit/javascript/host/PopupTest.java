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
package com.gargoylesoftware.htmlunit.javascript.host;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Popup}.
 *
 * @version $Revision: 5658 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class PopupTest extends WebTestCase {

    /**
     * Just test that a standard use of popup works without exception.
     * @throws Exception if the test fails
     * TODO: it should fail when simulating FF as createPopup() is only for IE
     */
    @Test
    @Browsers(Browser.IE)
    public void testPopup() throws Exception {
        final String html = "<html><head><title>First</title><body>\n"
            + "<script>\n"
            + "var oPopup = window.createPopup();\n"
            + "var oPopupBody = oPopup.document.body;\n"
            + "oPopupBody.innerHTML = 'bla bla';\n"
            + "oPopup.show(100, 100, 200, 50, document.body);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that the opened window becomes the current one.
     * @throws Exception if the test fails
     */
    @Test
    public void testPopupWindowBecomesCurrent() throws Exception {
        final String content = "<html><head><title>First</title><body>\n"
            + "<span id='button' onClick='openPopup()'>Push me</span>\n"
            + "<SCRIPT>\n"
            + "function openPopup()  { \n "
            + "window.open('', '_blank', 'width=640, height=600, scrollbars=yes'); "
            + "alert('Pop-up window is Open');\n "
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        final HtmlElement button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();
        final String[] expectedAlerts = {"Pop-up window is Open"};
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("about:blank", secondPage.getWebResponse().getWebRequest().getUrl());
        assertSame(secondPage.getEnclosingWindow(), secondPage.getWebClient().getCurrentWindow());
    }
}
