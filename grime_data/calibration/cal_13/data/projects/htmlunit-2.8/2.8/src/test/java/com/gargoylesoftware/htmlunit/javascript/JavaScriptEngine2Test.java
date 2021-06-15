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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Same scope as {@link JavaScriptEngineTest} but extending {@link WebDriverTestCase}.
 *
 * @version $Revision: 5580 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JavaScriptEngine2Test extends WebDriverTestCase {

    /**
     * All browsers except Opera seem to have a single JS execution thread for all windows,
     * but it is not the case of HtmlUnit-2.6!
     * Tested browsers (21.09.09):
     * - single JS thread: FF2, FF3.1, FF3.5, IE6 (and for info: Konqueror 4.2.2, Chrome Linux dev build)
     * - multiple JS threads: none of HtmlUnit's simulated browsers (for info Opera 10.00)
     * @throws Exception if the test fails
     */
    @Test
    public void jsRunSingleThreadedBrowserWide() throws Exception {
        final String html = "<html><head><script>"
            + "function test(prefix) {\n"
            + "  parent.document.getElementById('theArea').value += prefix + ' start\\n';\n"
            + "  var end = new Date().valueOf() + 1 * 1000;\n"
            + "  var t = [];\n"
            + "  while (new Date().valueOf() < end) {\n"
            + "    var x = document.createElement('iframe');\n"
            + "    t.push(x);\n"
            + "  }\n"
            + "  parent.document.getElementById('theArea').value += prefix + ' end\\n';\n"
            + "}\n"
            + "function checkResults() {\n"
            + "  var value = document.getElementById('theArea').value;\n"
            + "  var lines = value.split('\\n');\n"
            + "  if (lines.length < 5)\n"
            + "    setTimeout(checkResults, 100); // not yet ready, check later\n"
            + "  value = value.replace(/frame \\d /gi, '').replace(/\\W/gi, '');\n"
            + "  var singleThreaded = (value == 'startendstartend');\n"
            + "  document.getElementById('result').innerHTML = (singleThreaded ? 'single threaded' : 'in parallel');\n"
            + "}\n"
            + "function doTest() {\n"
            + "  parent.document.getElementById('theArea').value = '';\n"
            + "  document.getElementById('frame1').contentWindow.setTimeout(function() {test('frame 1'); }, 10);\n"
            + "  document.getElementById('frame2').contentWindow.setTimeout(function() {test('frame 2'); }, 10);\n"
            + "  setTimeout(checkResults, 1000);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe id='frame1' src='about:blank'></iframe>\n"
            + "<iframe id='frame2' src='about:blank'></iframe>\n"
            + "<textarea id='theArea' rows='5'></textarea>\n"
            + "script execution occured: <span id='result'></span>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("result"));

        // give time to the script to execute: normally ~2 seconds when scripts are run sequentially
        int nbWait = 0;
        while (element.getText().length() == 0) {
            Thread.sleep(100);
            if (nbWait++ > 50) {
                break;
            }
        }

        assertEquals("single threaded", element.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "false", "false", "true" })
    public void functionCaller() throws Exception {
        final String html = "<html><head><script>\n"
            + "function myFunc() {\n"
            + "  alert(myFunc.caller == null);\n"
            + "  alert(myFunc.caller == foo);\n"
            + "}\n"
            + "myFunc()\n"
            + "function foo() { myFunc() }\n"
            + "foo()\n"
            + "</script>\n"
            + "</head><body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    @NotYetImplemented
    public void undefined() throws Exception {
        final String html = "<html><head><script>\n"
            + "var undefined = 123;\n"
            + "alert(undefined);\n"
            + "</script>\n"
            + "</head><body></body></html>";

        loadPageWithAlerts2(html);
    }
}
