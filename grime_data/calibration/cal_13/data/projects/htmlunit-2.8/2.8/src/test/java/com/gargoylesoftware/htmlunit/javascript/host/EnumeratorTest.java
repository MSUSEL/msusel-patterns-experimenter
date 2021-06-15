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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link Enumerator}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class EnumeratorTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "false", "[object]", "undefined", "undefined", "true" })
    public void basicEnumerator() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var en = new Enumerator(document.forms);\n"
            + "  alert(en.atEnd());\n"
            + "  alert(en.item());\n"
            + "  alert(en.moveNext());\n"
            + "  alert(en.item());\n"
            + "  alert(en.atEnd());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form/>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Verifies that the enumerator constructor can take a form argument and then enumerate over the
     * form's input elements (bug 2645480).
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "f t1 t2", "t1", "t2" })
    public void formEnumerator() throws Exception {
        final String html
            = "<html><body><form id='f'><input type='text' name='t1' id='t1' />\n"
            + "<input type='text' name='t2' id='t2' /><div id='d'>d</div></form><script>\n"
            + "var f = document.forms.f, t1 = document.all.t1, t2 = document.all.t2;\n"
            + "alert(f.id + ' ' + t1.id + ' ' + t2.id);\n"
            + "var e = new Enumerator(f);\n"
            + "for( ; !e.atEnd(); e.moveNext()) alert(e.item().id);\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "undefined", "text" })
    public void item() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var form = document.forms.myForm;\n"
            + "  alert(form.elements.item(0).TyPe);\n"
            + "  alert(new Enumerator(form).item().TyPe);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form name='myForm'>\n"
            + "    <input name='myInput'>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
