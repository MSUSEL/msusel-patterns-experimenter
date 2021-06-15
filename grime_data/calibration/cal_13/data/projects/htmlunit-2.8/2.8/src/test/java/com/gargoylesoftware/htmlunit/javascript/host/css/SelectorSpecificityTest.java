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

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;

/**
 * Tests for {@link SelectorSpecificity}.
 *
 * @version $Revision: 5618 $
 * @author Marc Guillemot
 */
public class SelectorSpecificityTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void selectorSpecifity() throws Exception {
        final SelectorSpecificity specificy0 = selectorSpecifity("*", "0,0,0,0");
        final SelectorSpecificity specificy1 = selectorSpecifity("li", "0,0,0,1");
        final SelectorSpecificity specificy2a = selectorSpecifity("li:first-line", "0,0,0,2");
        final SelectorSpecificity specificy2b = selectorSpecifity("ul li", "0,0,0,2");
        final SelectorSpecificity specificy3 = selectorSpecifity("ul ol+li", "0,0,0,3");
        final SelectorSpecificity specificy11 = selectorSpecifity("h1 + *[rel=up]", "0,0,1,1");
        final SelectorSpecificity specificy13 = selectorSpecifity("ul ol li.red", "0,0,1,3");
        final SelectorSpecificity specificy21 = selectorSpecifity("li.red.level", "0,0,2,1");
        final SelectorSpecificity specificy100 = selectorSpecifity("#x34y", "0,1,0,0");

        Assert.assertEquals(0, specificy0.compareTo(specificy0));
        Assert.assertTrue(specificy0.compareTo(specificy1) < 0);
        Assert.assertTrue(specificy0.compareTo(specificy2a) < 0);
        Assert.assertTrue(specificy0.compareTo(specificy13) < 0);

        Assert.assertEquals(0, specificy1.compareTo(specificy1));
        Assert.assertTrue(specificy1.compareTo(specificy0) > 0);
        Assert.assertTrue(specificy1.compareTo(specificy2a) < 0);
        Assert.assertTrue(specificy1.compareTo(specificy13) < 0);

        Assert.assertEquals(0, specificy2a.compareTo(specificy2b));
        Assert.assertTrue(specificy2a.compareTo(specificy0) > 0);
        Assert.assertTrue(specificy2a.compareTo(specificy3) < 0);
        Assert.assertTrue(specificy2a.compareTo(specificy11) < 0);
        Assert.assertTrue(specificy2a.compareTo(specificy13) < 0);
        Assert.assertTrue(specificy2a.compareTo(specificy100) < 0);

        Assert.assertEquals(0, specificy11.compareTo(specificy11));
        Assert.assertTrue(specificy11.compareTo(specificy0) > 0);
        Assert.assertTrue(specificy11.compareTo(specificy13) < 0);
        Assert.assertTrue(specificy11.compareTo(specificy21) < 0);
        Assert.assertTrue(specificy11.compareTo(specificy100) < 0);
    }

    private SelectorSpecificity selectorSpecifity(final String selector, final String expectedSpecificity)
        throws Exception {
        final String html =
            "<html><body id='b'><style></style>\n"
            + "<div id='d' class='foo bar'><span>x</span><span id='s'>a</span>b</div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.jsxGet_sheet();

        final Selector selectorObject = parseSelector(sheet, selector);
        final SelectorSpecificity specificity = new SelectorSpecificity(selectorObject);
        assertEquals(expectedSpecificity, specificity.toString());
        return specificity;
    }

    private Selector parseSelector(final CSSStyleSheet sheet, final String rule) {
        return sheet.parseSelectors(new InputSource(new StringReader(rule))).item(0);
    }
}
