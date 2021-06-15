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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link Screen}.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535868.aspx">MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
@RunWith(BrowserRunner.class)
public class ScreenTest extends WebTestCase {

    /**
     * Test all desired properties on screen (easy to copy and test in a real browser).
     * @throws Exception on test failure
     */
    @Test
    @Alerts("16")
    public void testProperties() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var props = {\n"
            + "           availHeight: 768, \n"
            + "           availLeft: 0, \n"
            + "           availTop: 0, \n"
            + "           availWidth: 1024, \n"
            + "           bufferDepth: 24, \n"
            + "           deviceXDPI: 96, \n"
            + "           deviceYDPI: 96, \n"
            + "           fontSmoothingEnabled: true, \n"
            + "           height: 768, \n"
            + "           left: 0, \n"
            + "           logicalXDPI: 96, \n"
            + "           logicalYDPI: 96, \n"
            + "           pixelDepth: 24, \n"
            + "           top: 0, \n"
            + "           updateInterval: 0, \n"
            + "           width: 1024 \n"
            + "       };\n"
            + "       var nbTests = 0;\n"
            + "       for (var i in props) {\n"
            + "           var myExpr = 'window.screen.' + i;\n"
            + "           var result = eval(myExpr);\n"
            + "           if (props[i] != result) {\n"
            + "             alert(myExpr + ': ' + result + ' != ' + props[i]);\n"
            + "           }\n"
            + "           nbTests++;\n"
            + "       }\n"
            + "       alert(nbTests);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
