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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision: 5800 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SELECT")
    //TODO: WebDriver tests passes even with HtmlUnit direct usage fails!
    public void click() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  function init() {\n"
            + "    var s = document.getElementById('s');\n"
            + "    if (s.addEventListener) {\n"
            + "      s.addEventListener('click', handle, false);\n"
            + "    } else if (s.attachEvent) {\n"
            + "      s.attachEvent('onclick', handle);\n"
            + "    }\n"
            + "  }\n"
            + "  function handle(event) {\n"
            + "    if (event.target)\n"
            + "      alert(event.target.nodeName);\n"
            + "    else\n"
            + "      alert(event.srcElement.nodeName);\n"
            + "  }\n"
            + "</script></head><body onload='init()'>\n"
            + "  <select id='s'>\n"
            + "    <option value='a'>A</option>\n"
            + "    <option id='opb' value='b'>B</option>\n"
            + "    <option value='c'>C</option>\n"
            + "  </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void click2() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  function init() {\n"
            + "    var s = document.getElementById('s');\n"
            + "    if (s.addEventListener) {\n"
            + "      s.addEventListener('click', handle, false);\n"
            + "    } else if (s.attachEvent) {\n"
            + "      s.attachEvent('onclick', handle);\n"
            + "    }\n"
            + "  }\n"
            + "  function handle(event) {\n"
            + "    alert(s.options[s.selectedIndex].value);\n"
            + "  }\n"
            + "</script></head><body onload='init()'>\n"
            + "  <select id='s'>\n"
            + "    <option value='a'>A</option>\n"
            + "    <option id='opb' value='b'>B</option>\n"
            + "    <option value='c'>C</option>\n"
            + "  </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("opb")).setSelected();
        driver.findElement(By.id("s")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
