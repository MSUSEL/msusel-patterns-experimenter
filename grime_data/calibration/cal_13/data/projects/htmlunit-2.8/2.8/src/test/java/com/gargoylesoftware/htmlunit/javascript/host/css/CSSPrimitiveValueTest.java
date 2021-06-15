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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link CSSPrimitiveValue}.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class CSSPrimitiveValueTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF2 = { "[CSSPrimitiveValue]", "012345678910111213141516171819202122232425" },
            FF3 = { "[object CSSPrimitiveValue]", "012345678910111213141516171819202122232425" },
            IE = { "exception" })
    public void test() throws Exception {
        final String html = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test(){\n"
            + "  try {\n"
            + "    alert(CSSPrimitiveValue);\n"
            + "    var props = ['CSS_UNKNOWN', 'CSS_NUMBER', 'CSS_PERCENTAGE', 'CSS_EMS', 'CSS_EXS', 'CSS_PX', "
            + "'CSS_CM', 'CSS_MM', 'CSS_IN', 'CSS_PT', 'CSS_PC', 'CSS_DEG', 'CSS_RAD', 'CSS_GRAD', 'CSS_MS', "
            + "'CSS_S', 'CSS_HZ', 'CSS_KHZ', 'CSS_DIMENSION', 'CSS_STRING', 'CSS_URI', 'CSS_IDENT', 'CSS_ATTR', "
            + "'CSS_COUNTER', 'CSS_RECT', 'CSS_RGBCOLOR'];\n"
            + "    var str = '';\n"
            + "    for (var i=0; i<props.length; ++i)\n"
            + "      str += CSSPrimitiveValue[props[i]];\n"
            + "    alert(str);\n"
            + "  } catch(e) { alert('exception') }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "rgb(0, 0, 255)", "0" })
    public void getPropertyCSSValue() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    var style = document.defaultView.getComputedStyle(oDiv1, null);\n"
            + "    var cssValue = style.getPropertyCSSValue('color');\n"
            + "    alert(cssValue.cssText);\n"
            + "    alert(style.getPropertyCSSValue('border-left-width').getFloatValue(CSSPrimitiveValue.CSS_PX));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='color: rgb(0, 0, 255)'>foo</div></body></html>";
        loadPageWithAlerts2(html);
    }
}
