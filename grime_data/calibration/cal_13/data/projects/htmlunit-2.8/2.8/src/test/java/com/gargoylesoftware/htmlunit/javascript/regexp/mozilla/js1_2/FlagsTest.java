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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla.js1_2;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/flags.js'.
 *
 * @version $Revision: 5766 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class FlagsTest extends WebDriverTestCase {

    /**
     * Tests 'aBCdEfGHijKLmno'.match(/fghijk/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fGHijK")
    public void test1() throws Exception {
        test("'aBCdEfGHijKLmno'.match(/fghijk/i)");
    }

    /**
     * Tests 'aBCdEfGHijKLmno'.match(new RegExp("fghijk","i")).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fGHijK")
    public void test2() throws Exception {
        test("'aBCdEfGHijKLmno'.match(new RegExp(\"fghijk\",\"i\"))");
    }

    /**
     * Tests 'xa xb xc xd xe xf'.match(/x./g).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,xb,xc,xd,xe,xf")
    public void test3() throws Exception {
        test("'xa xb xc xd xe xf'.match(/x./g)");
    }

    /**
     * Tests 'xa xb xc xd xe xf'.match(new RegExp('x.','g')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,xb,xc,xd,xe,xf")
    public void test4() throws Exception {
        test("'xa xb xc xd xe xf'.match(new RegExp('x.','g'))");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(/x./gi).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test5() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(/x./gi)");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(new RegExp('x.','gi')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test6() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(new RegExp('x.','gi'))");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(/x./ig).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test7() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(/x./ig)");
    }

    /**
     * Tests 'xa Xb xc xd Xe xf'.match(new RegExp('x.','ig')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa,Xb,xc,xd,Xe,xf")
    public void test8() throws Exception {
        test("'xa Xb xc xd Xe xf'.match(new RegExp('x.','ig'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
