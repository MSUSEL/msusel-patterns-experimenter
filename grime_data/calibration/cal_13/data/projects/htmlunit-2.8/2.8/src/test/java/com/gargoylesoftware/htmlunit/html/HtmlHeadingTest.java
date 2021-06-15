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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlHeading1} to {@link HtmlHeading6}.
 *
 * @version $Revision: 5346 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlHeadingTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLHeadingElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <h2 id='myId'>asdf</h2>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("myId")));
            assertTrue(element instanceof HtmlHeading2);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "begin"
            + "<h1>in h1</h1>after h1\n"
            + "<h2>in h2</h2>after h2\n"
            + "<h3>in h3</h3>after h3\n"
            + "<h4>in h4</h4>after h4\n"
            + "<h5>in h5</h5>after h5\n"
            + "<h6>in h6</h6>after h6\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final String expectedText = "begin" + LINE_SEPARATOR
            + "in h1" + LINE_SEPARATOR
            + "after h1" + LINE_SEPARATOR
            + "in h2" + LINE_SEPARATOR
            + "after h2" + LINE_SEPARATOR
            + "in h3" + LINE_SEPARATOR
            + "after h3" + LINE_SEPARATOR
            + "in h4" + LINE_SEPARATOR
            + "after h4" + LINE_SEPARATOR
            + "in h5" + LINE_SEPARATOR
            + "after h5" + LINE_SEPARATOR
            + "in h6" + LINE_SEPARATOR
            + "after h6";

        assertEquals(expectedText, page.asText());
    }
}
