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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link BoxObject}.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class BoxObjectTest extends WebDriverTestCase {

    /**
     * Tests box object attributes relating to HTML elements: firstChild, lastChild, previousSibling, etc.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "true", "true", "true", "true", "true" }, IE = "exception")
    public void testElementAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <span id='foo'>foo</span><div id='d'><span id='a'>a</span><span id='b'>b</span></div><span id='bar'>bar</span>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var div = document.getElementById('d');\n"
            + "          var spanFoo = document.getElementById('foo');\n"
            + "          var spanA = document.getElementById('a');\n"
            + "          var spanB = document.getElementById('b');\n"
            + "          var spanBar = document.getElementById('bar');\n"
            + "          var box = document.getBoxObjectFor(div);\n"
            + "          alert(box.element == div);\n"
            + "          alert(box.firstChild == spanA);\n"
            + "          alert(box.lastChild == spanB);\n"
            + "          alert(box.previousSibling == spanFoo);\n"
            + "          alert(box.nextSibling == spanBar);\n"
            + "        } catch (e) {alert('exception')}\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests box object attributes relating to position and size: x, y, screenX, screenY, etc.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "73-123", "73-244", "510-410" }, IE = "exception")
    public void testPositionAndSizeAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <style>#d { position:absolute; left:50px; top:100px; width:500px; height:400px; border:3px; padding: 5px; margin: 23px; }</style>\n"
            + "    <div id='d'>daniel</div>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        try {\n"
            + "          var div = document.getElementById('d');\n"
            + "          var box = document.getBoxObjectFor(div);\n"
            + "          alert(box.x + '-' + box.y);\n"
            + "          alert(box.screenX + '-' + box.screenY);\n"
            + "          alert(box.width + '-' + box.height);\n"
            + "        } catch (e) {alert('exception')}\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

}
