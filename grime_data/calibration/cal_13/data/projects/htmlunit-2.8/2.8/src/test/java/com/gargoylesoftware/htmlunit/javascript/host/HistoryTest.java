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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link History}.
 *
 * @version $Revision: 5658 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HistoryTest extends WebServerTestCase {

    /**
     * Starts the web server prior to test execution.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/com/gargoylesoftware/htmlunit/javascript/host");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void backAndForward() throws Exception {
        final WebClient client = getWebClient();
        final TopLevelWindow window = (TopLevelWindow) client.getCurrentWindow();
        final History history = window.getHistory();

        final String urlA = "http://localhost:" + PORT + "/HistoryTest_a.html";
        final String urlB = "http://localhost:" + PORT + "/HistoryTest_b.html";
        final String urlBX = "http://localhost:" + PORT + "/HistoryTest_b.html#x";
        final String urlC = "http://localhost:" + PORT + "/HistoryTest_c.html";

        HtmlPage page = client.getPage(urlA);
        assertEquals(1, history.getLength());
        assertEquals(0, history.getIndex());
        assertEquals(urlA, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("b").click();
        assertEquals(2, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("x").click();
        assertEquals(3, history.getLength());
        assertEquals(2, history.getIndex());
        assertEquals(urlBX, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("back").click();
        assertEquals(3, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("back").click();
        assertEquals(3, history.getLength());
        assertEquals(0, history.getIndex());
        assertEquals(urlA, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("forward").click();
        assertEquals(3, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("c").click();
        assertEquals(3, history.getLength());
        assertEquals(2, history.getIndex());
        assertEquals(urlC, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("back").click();
        assertEquals(3, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("forward").click();
        assertEquals(3, history.getLength());
        assertEquals(2, history.getIndex());
        assertEquals(urlC, page.getWebResponse().getWebRequest().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void go() throws Exception {
        final WebClient client = getWebClient();
        final TopLevelWindow window = (TopLevelWindow) client.getCurrentWindow();
        final History history = window.getHistory();

        final String urlA = "http://localhost:" + PORT + "/HistoryTest_a.html";
        final String urlB = "http://localhost:" + PORT + "/HistoryTest_b.html";
        final String urlBX = "http://localhost:" + PORT + "/HistoryTest_b.html#x";
        final String urlC = "http://localhost:" + PORT + "/HistoryTest_c.html";

        HtmlPage page = client.getPage(urlA);
        assertEquals(1, history.getLength());
        assertEquals(0, history.getIndex());
        assertEquals(urlA, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("b").click();
        assertEquals(2, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("x").click();
        assertEquals(3, history.getLength());
        assertEquals(2, history.getIndex());
        assertEquals(urlBX, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("minusTwo").click();
        assertEquals(3, history.getLength());
        assertEquals(0, history.getIndex());
        assertEquals(urlA, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("plusOne").click();
        assertEquals(3, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("c").click();
        assertEquals(3, history.getLength());
        assertEquals(2, history.getIndex());
        assertEquals(urlC, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("minusOne").click();
        assertEquals(3, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());

        page = page.getAnchorByName("plusTwo").click();
        assertEquals(3, history.getLength());
        assertEquals(1, history.getIndex());
        assertEquals(urlB, page.getWebResponse().getWebRequest().getUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "2", "3" })
    public void length() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));

        HtmlPage page = client.getPage("http://localhost:" + PORT + "/HistoryTest_a.html");
        page = page.getAnchorByName("length").click();

        page = page.getAnchorByName("b").click();
        page = page.getAnchorByName("length").click();

        page = page.getAnchorByName("x").click();
        page = page.getAnchorByName("length").click();

        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "error", IE = "undefined")
    public void previous() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/HistoryTest_a.html");
        page.getAnchorByName("previous").click();
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "error", IE = "undefined")
    public void current() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/HistoryTest_a.html");
        page.getAnchorByName("current").click();
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "error", IE = "undefined")
    public void next() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/HistoryTest_a.html");
        page.getAnchorByName("next").click();
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "error")
    public void item() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/HistoryTest_a.html");
        page.getAnchorByName("itemZero").click();
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "false", "false", "true", "true", "false", "false" },
            IE = { "false", "false", "false", "false", "false", "false" })
    public void byIndex() throws Exception {
        final WebClient client = getWebClient();
        final List<String> alerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(alerts));
        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/HistoryTest_a.html");
        page.getAnchorByName("hasNegativeOne").click();
        page.getAnchorByName("hasZero").click();
        page.getAnchorByName("hasPositiveOne").click();
        assertEquals(getExpectedAlerts(), alerts);
    }

}
