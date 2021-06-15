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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLBRElement}.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLBRElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
        IE = { "", "left", "all", "right", "none", "", "", "!", "!", "!", "left", "none", "right", "all", "none",
               "", "" },
        FF = { "", "left", "all", "right", "none", "2", "foo", "left", "none", "right", "all", "2", "abc", "8" })
    public void clear() throws Exception {
        final String html
            = "<html><body>\n"
            + "<br id='br1'/>\n"
            + "<br id='br2' clear='left'/>\n"
            + "<br id='br3' clear='all'/>\n"
            + "<br id='br4' clear='right'/>\n"
            + "<br id='br5' clear='none'/>\n"
            + "<br id='br6' clear='2'/>\n"
            + "<br id='br7' clear='foo'/>\n"
            + "<script>\n"
            + "function set(br, value) {\n"
            + "  try {\n"
            + "    br.clear = value;\n"
            + "  } catch(e) {\n"
            + "    alert('!');\n"
            + "  }\n"
            + "}\n"
            + "var br1 = document.getElementById('br1');\n"
            + "var br2 = document.getElementById('br2');\n"
            + "var br3 = document.getElementById('br3');\n"
            + "var br4 = document.getElementById('br4');\n"
            + "var br5 = document.getElementById('br5');\n"
            + "var br6 = document.getElementById('br6');\n"
            + "var br7 = document.getElementById('br7');\n"
            + "alert(br1.clear);\n"
            + "alert(br2.clear);\n"
            + "alert(br3.clear);\n"
            + "alert(br4.clear);\n"
            + "alert(br5.clear);\n"
            + "alert(br6.clear);\n"
            + "alert(br7.clear);\n"
            + "set(br1, 'left');\n"
            + "set(br2, 'none');\n"
            + "set(br3, 'right');\n"
            + "set(br4, 'all');\n"
            + "set(br5, 2);\n"
            + "set(br6, 'abc');\n"
            + "set(br7, '8');\n"
            + "alert(br1.clear);\n"
            + "alert(br2.clear);\n"
            + "alert(br3.clear);\n"
            + "alert(br4.clear);\n"
            + "alert(br5.clear);\n"
            + "alert(br6.clear);\n"
            + "alert(br7.clear);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

}
