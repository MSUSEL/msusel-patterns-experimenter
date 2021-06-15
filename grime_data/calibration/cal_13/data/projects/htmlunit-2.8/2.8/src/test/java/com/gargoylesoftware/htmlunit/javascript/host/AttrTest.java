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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link Attr}.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class AttrTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "true", "false" },
            FF = { "true", "exception thrown" })
    public void specified() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var s = document.getElementById('testSelect');\n"
            + "    var o1 = s.options[0];\n"
            + "    alert(o1.getAttributeNode('value').specified);\n"
            + "    var o2 = s.options[1];\n"
            + "    alert(o2.getAttributeNode('value').specified);\n"
            + "  } catch(e) {\n"
            + "    alert('exception thrown');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1' value='foo'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Trimming of "class" attributes during Firefox emulation was having the unintended side effect
     * of setting the attribute's "specified" attribute to "false".
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "true", "true" })
    public void specified2() throws Exception {
        final String html
            = "<html><body onload='test()'><div id='div' class='test'></div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.getElementById('div');\n"
            + "    alert(div.attributes.id.specified);\n"
            + "    alert(div.attributes.class.specified);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "undefined", FF = "[object HTMLOptionElement]")
    public void ownerElement() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var o1 = s.options[0];\n"
            + "  alert(o1.getAttributeNode('value').ownerElement);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1' value='foo'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "undefined" },
            FF2 = { "undefined", "undefined", "undefined" },
            FF3 = { "true", "false", "false" })
    public void isId() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var d = document.getElementById('d');\n"
            + "  alert(d.getAttributeNode('id').isId);\n"
            + "  alert(d.getAttributeNode('name').isId);\n"
            + "  alert(d.getAttributeNode('width').isId);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div iD='d' name='d' width='40'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "test()", IE = "undefined")
    public void textContent() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var a = document.body.getAttributeNode('onload');\n"
            + "  alert(a.textContent);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "[object]", "undefined", "[object]", "" }, FF = {"[object Attr]", "", "[object Attr]", "" })
    public void value() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var attr = document.createAttribute('hi');\n"
            + "    alert(attr);\n"
            + "    alert(attr.value)\n"
            + "    attr = createXmlDocument().createAttribute('hi');\n"
            + "    alert(attr);\n"
            + "    alert(attr.value)\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
