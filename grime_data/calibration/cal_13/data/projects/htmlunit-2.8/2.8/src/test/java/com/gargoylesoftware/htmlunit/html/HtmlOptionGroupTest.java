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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlOptionGroup}.
 *
 * @version $Revision: 5929 $
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HtmlOptionGroupTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLOptGroupElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <select>\n"
            + "    <optgroup id='myId' label='my label'>\n"
            + "      <option>My Option</option>\n"
            + "    </optgroup>\n"
            + "  </select>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlOptionGroup optionGroup = page.getHtmlElementById("myId");
        assertTrue(optionGroup.getEnclosingSelect() instanceof HtmlSelect);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "false", "true", "false", "true", "false", "false", "false" })
    public void testDisabled() throws Exception {
        final String html = "<html><body onload='test()'><form name='f'>\n"
            + "  <select name='s' id='s'>\n"
            + "    <optgroup id='g1' label='group 1'>\n"
            + "      <option value='o11' id='o11'>One</option>\n"
            + "      <option value='o12' id='o12'>Two</option>\n"
            + "    </optgroup>\n"
            + "    <optgroup id='g2' label='group 2' disabled='disabled'>\n"
            + "      <option value='o21' id='o21'>One</option>\n"
            + "      <option value='o22' id='o22'>Two</option>\n"
            + "    </optgroup>\n"
            + "  </select>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var g1 = document.getElementById('g1');\n"
            + "      var o11 = document.getElementById('o11');\n"
            + "      var g2 = document.getElementById('g2');\n"
            + "      var o21 = document.getElementById('o21');\n"
            + "      alert(g1.disabled);\n"
            + "      alert(o11.disabled);\n"
            + "      alert(g2.disabled);\n"
            + "      alert(o21.disabled);\n"
            + "      g1.disabled = true;\n"
            + "      g2.disabled = false;\n"
            + "      alert(g1.disabled);\n"
            + "      alert(o11.disabled);\n"
            + "      alert(g2.disabled);\n"
            + "      alert(o21.disabled);\n"
            + "    }\n"
            + "  </script>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final boolean disabled = getBrowserVersion().isFirefox();
        assertEquals(disabled, ((HtmlOptionGroup) page.getElementById("g1")).isDisabled());
        assertFalse(((HtmlOptionGroup) page.getElementById("g2")).isDisabled());
    }

}
