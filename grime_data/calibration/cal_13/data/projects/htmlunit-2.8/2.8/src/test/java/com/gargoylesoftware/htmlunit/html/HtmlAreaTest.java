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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlArea}.
 *
 * @version $Revision: 5569 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlAreaTest extends WebTestCase {

    private WebClient createWebClient(final String onClick) {
        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<img src='/images/planets.gif' width='145' height='126' usemap='#planetmap'>\n"
            + "<map id='planetmap' name='planetmap'>\n"
            + "<area shape='rect' onClick=\"" + onClick + "\" coords='0,0,82,126' id='second' "
            + "href='" + URL_SECOND + "'>\n"
            + "<area shape='circle' coords='90,58,3' id='third' href='" + URL_THIRD + "'>\n"
            + "</map></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>third</title></head><body></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);
        return client;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick() throws Exception {
        final WebClient client = createWebClient("");

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("third");

        // Test that the correct value is being passed back up to the server
        final HtmlPage thirdPage = (HtmlPage) area.click();
        assertEquals("third", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onclickReturnsFalse() throws Exception {
        final WebClient client = createWebClient("alert('foo');return false;");
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("second");

        final HtmlPage thirdPage = (HtmlPage) area.click();
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals("first", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onclickReturnsTrue() throws Exception {
        final WebClient client = createWebClient("alert('foo');return true;");
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("second");

        final HtmlPage thirdPage = (HtmlPage) area.click();
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals("second", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(\"clicked\")' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlArea area = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = area.click();

        assertEquals(new String[] {"clicked"}, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_javascriptUrl_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(\"clicked\")' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final WebClient client = getWebClient();
        client.setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(getDefaultUrl());
        final HtmlArea area = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = area.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertSame(page, secondPage);
    }

    /**
     * In action "this" should be the window and not the area.
     * @throws Exception if the test fails
     */
    @Test
    public void testThisInJavascriptHref() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(this == window)' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"true"};
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final Page page2 = page.<HtmlArea>getHtmlElementById("a2").click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page, page2);
    }

}
