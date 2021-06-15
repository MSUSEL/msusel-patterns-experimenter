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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLMetaElement}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLMetaElementTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "", "text/html; charset=utf-8", "Content-Type", "", "", "" },
            FF = { "undefined", "text/html; charset=utf-8", "Content-Type", "", "", "undefined" })
    public void name() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var meta = document.getElementsByTagName('meta')[0];\n"
            + "        alert(meta.charset);\n"
            + "        alert(meta.content);\n"
            + "        alert(meta.httpEquiv);\n"
            + "        alert(meta.name);\n"
            + "        alert(meta.scheme);\n"
            + "        alert(meta.url);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'></body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

}
