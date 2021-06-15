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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests of the source repository of <a href="http://code.google.com/webtoolkit">Google Web Toolkit</a>,
 * which are marked to fail with HtmlUnit.
 *
 * To generate the JavaScript, copy the test case to "Hello" GWT sample, compile it with "-style PRETTY"
 * by modifying "gwtc" target, and run "ant".  In generated ".nocache.js", search for "ie6" or "gecko1_8"
 * to know which JavaScript file corresponds to IE or FF respectively.
 *
 * @version $Revision: 5551 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GWTSourceTest extends WebDriverTestCase {

    /**
     * Test case for
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/core/client/impl/StackTraceCreatorTest.java">StackTraceCreatorTest</a>.
     *
     * Test case to be moved to {@link com.gargoylesoftware.htmlunit.javascript.SimpleScriptableTest}
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = "undefined", FF = "true")
    public void stack() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack)\n"
            + "      alert(e.stack.indexOf('test()@') != -1);\n"
            + "    else\n"
            + "      alert('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
