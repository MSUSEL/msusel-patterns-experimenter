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
 * Unit tests for {@link HTMLMenuElement}.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLMenuElementTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "false", "true", "true", "true", "null", "", "blah", "2",
                   "true", "false", "true", "false", "", "null", "", "null" },
        IE = { "false", "true", "true", "true", "false", "true", "true", "true",
               "true", "false", "true", "false", "true", "false", "true", "false" })
    public void compact() throws Exception {
        final String html = "<html><body>\n"
            + "<menu id='menu1'><li>a</li><li>b</li></menu>\n"
            + "<menu compact='' id='menu2'><li>a</li><li>b</li></menu>\n"
            + "<menu compact='blah' id='menu3'><li>a</li><li>b</li></menu>\n"
            + "<menu compact='2' id='menu4'><li>a</li><li>b</li></menu>\n"
            + "<script>\n"
            + "alert(document.getElementById('menu1').compact);\n"
            + "alert(document.getElementById('menu2').compact);\n"
            + "alert(document.getElementById('menu3').compact);\n"
            + "alert(document.getElementById('menu4').compact);\n"
            + "alert(document.getElementById('menu1').getAttribute('compact'));\n"
            + "alert(document.getElementById('menu2').getAttribute('compact'));\n"
            + "alert(document.getElementById('menu3').getAttribute('compact'));\n"
            + "alert(document.getElementById('menu4').getAttribute('compact'));\n"
            + "document.getElementById('menu1').compact = true;\n"
            + "document.getElementById('menu2').compact = false;\n"
            + "document.getElementById('menu3').compact = 'xyz';\n"
            + "document.getElementById('menu4').compact = null;\n"
            + "alert(document.getElementById('menu1').compact);\n"
            + "alert(document.getElementById('menu2').compact);\n"
            + "alert(document.getElementById('menu3').compact);\n"
            + "alert(document.getElementById('menu4').compact);\n"
            + "alert(document.getElementById('menu1').getAttribute('compact'));\n"
            + "alert(document.getElementById('menu2').getAttribute('compact'));\n"
            + "alert(document.getElementById('menu3').getAttribute('compact'));\n"
            + "alert(document.getElementById('menu4').getAttribute('compact'));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

}
