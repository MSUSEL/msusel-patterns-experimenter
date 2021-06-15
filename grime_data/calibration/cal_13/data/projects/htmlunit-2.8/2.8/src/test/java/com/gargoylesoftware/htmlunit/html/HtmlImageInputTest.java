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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlImageInput}.
 *
 * @version $Revision: 5663 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlImageInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_NoPosition() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='image' name='aButton' value='foo'/>\n"
            + "<input type='image' name='button' value='foo'/>\n"
            + "<input type='image' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPageWithAlerts(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage) imageInput.click();
        assertNotNull(secondPage);

        final List<NameValuePair> expectedPairs = new ArrayList<NameValuePair>();
        expectedPairs.add(new NameValuePair("button.x", "0"));
        expectedPairs.add(new NameValuePair("button.y", "0"));
        if (getBrowserVersion().isFirefox()) {
            expectedPairs.add(new NameValuePair("button", "foo"));
        }

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_NoPosition_NoValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<input type='image' name='button'>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPageWithAlerts(htmlContent);
        getMockConnection(page).setDefaultResponse(htmlContent);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage) imageInput.click();
        final String url = secondPage.getWebResponse().getWebRequest().getUrl().toExternalForm();
        assertTrue(url.endsWith("?button.x=0&button.y=0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_WithPosition() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='image' name='aButton' value='foo'/>\n"
            + "<input type='image' name='button' value='foo'/>\n"
            + "<input type='image' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage) imageInput.click(100, 200);
        assertNotNull(secondPage);

        final List<NameValuePair> expectedPairs = new ArrayList<NameValuePair>();
        expectedPairs.add(new NameValuePair("button.x", "100"));
        expectedPairs.add(new NameValuePair("button.y", "200"));
        if (getBrowserVersion().isFirefox()) {
            expectedPairs.add(new NameValuePair("button", "foo"));
        }

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters());
    }

    /**
     * If an image button without name is clicked, it should send only "x" and "y" parameters.
     * Regression test for bug 1118877.
     * @throws Exception if the test fails
     */
    @Test
    public void testNoNameClick_WithPosition() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='image' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = form.getInputByValue("foo");
        final HtmlPage secondPage = (HtmlPage) imageInput.click(100, 200);
        assertNotNull(secondPage);

        final List<NameValuePair> expectedPairs = Arrays.asList(new NameValuePair[]{
            new NameValuePair("x", "100"),
            new NameValuePair("y", "200")
        });

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOutsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='image' src='test.png' onclick='alert(1)'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlImageInput input = page.getHtmlElementById("myInput");
        input.click();

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug: http://sourceforge.net/tracker/index.php?func=detail&aid=2013891&group_id=47038&atid=448266.
     * @throws Exception if an error occurs
     */
    @Test
    public void testClickFiresOnMouseDown() throws Exception {
        final String s = "<html><body><input type='image' src='x.png' id='i' onmousedown='alert(1)'></body></html>";
        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), s, collectedAlerts);
        page.<HtmlElement>getHtmlElementById("i").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for bug: http://sourceforge.net/tracker/index.php?func=detail&aid=2013891&group_id=47038&atid=448266.
     * @throws Exception if an error occurs
     */
    @Test
    public void testClickFiresOnMouseUp() throws Exception {
        final String s = "<html><body><input type='image' src='x.png' id='i' onmouseup='alert(1)'></body></html>";
        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), s, collectedAlerts);
        page.<HtmlElement>getHtmlElementById("i").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

}
