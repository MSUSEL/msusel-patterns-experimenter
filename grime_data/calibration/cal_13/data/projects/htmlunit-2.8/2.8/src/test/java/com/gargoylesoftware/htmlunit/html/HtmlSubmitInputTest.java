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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlSubmitInput}.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlSubmitInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSubmit() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='submit' name='aButton' value='foo'/>\n"
            + "<input type='suBMit' name='button' value='foo'/>\n"
            + "<input type='submit' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";

        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement button = wd.findElement(By.name("button"));
        button.click();

        assertEquals("foo", wd.getTitle());

        assertEquals(Collections.singletonList(new NameValuePair("button", "foo")),
            getMockWebConnection().getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClick() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\"); return false;'>\n"
            + "    <input type='submit' name='button' value='foo' onClick='alert(\"foo\")'/>\n"
            + "</form></body></html>";

        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement button = wd.findElement(By.name("button"));
        button.click();

        final String[] expectedAlerts = {"foo", "bar"};
        assertEquals(expectedAlerts, getCollectedAlerts(wd));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClick_JavascriptReturnsTrue() throws Exception {
        final String html
            = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1' method='get' action='foo.html'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button1'"
            + "onclick='return true'/></form>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "foo.html"), secondHtml);

        final WebDriver wd = loadPageWithAlerts2(html);

        final WebElement button = wd.findElement(By.id("button1"));
        button.click();

        assertEquals("Second", wd.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "Submit Query", FF = "")
    public void testDefaultValue() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form action='foo.html'>\n"
            + "  <input type='submit' id='myId'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(page.asText().indexOf("Submit Query") > -1);
        assertFalse(page.asXml().indexOf("Submit Query") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void testEmptyValue() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input type='submit' id='myId' value=''>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertFalse(page.asText().indexOf("Submit Query") > -1);
        assertTrue(page.asXml().indexOf("value=\"\"") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOutsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='submit' onclick='alert(1)'>\n"
            + "</body></html>";

        final WebDriver wd = loadPageWithAlerts2(html);
        final String[] expectedAlerts = {"1"};
        final WebElement input = wd.findElement(By.id("myInput"));
        input.click();

        assertEquals(expectedAlerts, getCollectedAlerts(wd));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "1")
    public void onclick() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<form>\n"
            + "  <input id='myInput'>\n"
            + "  <input type='submit' onclick='alert(1)'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        page.<HtmlInput>getHtmlElementById("myInput").type('\n');

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doubleSubmission() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script type='text/javascript'>\n"
            + "    function submitForm() {\n"
            + "      document.deliveryChannelForm.submitBtn.disabled = true;\n"
            + "      document.deliveryChannelForm.submit();\n"
            + "    }\n"
            + "  </script>"
            + "</head>\n"
            + "<body>\n"
            + "  <form action='test' name='deliveryChannelForm'>\n"
            + "    <input name='submitBtn' type='submit' value='Save' title='Save' onclick='submitForm();'>\n"
            + "  </form>"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver wd = loadPageWithAlerts2(html);
        final WebElement input = wd.findElement(By.name("submitBtn"));
        input.click();

        assertEquals(2, getMockWebConnection().getRequestCount());
    }
}
