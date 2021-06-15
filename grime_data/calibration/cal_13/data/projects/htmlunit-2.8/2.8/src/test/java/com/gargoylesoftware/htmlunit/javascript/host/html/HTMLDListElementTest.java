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
 * Unit tests for {@link HTMLDListElement}.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLDListElementTest extends WebDriverTestCase {

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
            + "<dl id='dl1'><dt>a</dt><dd>b</dd></dl>\n"
            + "<dl compact='' id='dl2'><dt>a</dt><dd>b</dd></dl>\n"
            + "<dl compact='blah' id='dl3'><dt>a</dt><dd>b</dd></dl>\n"
            + "<dl compact='2' id='dl4'><dt>a</dt><dd>b</dd></dl>\n"
            + "<script>\n"
            + "alert(document.getElementById('dl1').compact);\n"
            + "alert(document.getElementById('dl2').compact);\n"
            + "alert(document.getElementById('dl3').compact);\n"
            + "alert(document.getElementById('dl4').compact);\n"
            + "alert(document.getElementById('dl1').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl2').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl3').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl4').getAttribute('compact'));\n"
            + "document.getElementById('dl1').compact = true;\n"
            + "document.getElementById('dl2').compact = false;\n"
            + "document.getElementById('dl3').compact = 'xyz';\n"
            + "document.getElementById('dl4').compact = null;\n"
            + "alert(document.getElementById('dl1').compact);\n"
            + "alert(document.getElementById('dl2').compact);\n"
            + "alert(document.getElementById('dl3').compact);\n"
            + "alert(document.getElementById('dl4').compact);\n"
            + "alert(document.getElementById('dl1').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl2').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl3').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl4').getAttribute('compact'));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
