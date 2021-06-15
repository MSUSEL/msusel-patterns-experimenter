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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/asterisk.js'.
 *
 * @version $Revision: 5766 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class AsteriskTest extends WebDriverTestCase {

    /**
     * Tests 'abcddddefg'.match(new RegExp('d*')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void test1() throws Exception {
        test("'abcddddefg'.match(new RegExp('d*'))");
    }

    /**
     * Tests 'abcddddefg'.match(new RegExp('cd*')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cdddd")
    public void test2() throws Exception {
        test("'abcddddefg'.match(new RegExp('cd*'))");
    }

    /**
     * Tests 'abcdefg'.match(new RegExp('cx*d')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cd")
    public void test3() throws Exception {
        test("'abcdefg'.match(new RegExp('cx*d'))");
    }

    /**
     * Tests 'xxxxxxx'.match(new RegExp('(x*)(x+)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xxxxxxx,xxxxxx,x")
    public void test4() throws Exception {
        test("'xxxxxxx'.match(new RegExp('(x*)(x+)'))");
    }

    /**
     * Tests '1234567890'.match(new RegExp('(\\d*)(\\d+)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234567890,123456789,0")
    public void test5() throws Exception {
        test("'1234567890'.match(new RegExp('(\\\\d*)(\\\\d+)'))");
    }

    /**
     * Tests '1234567890'.match(new RegExp('(\\d*)\\d(\\d+)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1234567890,12345678,0")
    public void test6() throws Exception {
        test("'1234567890'.match(new RegExp('(\\\\d*)\\\\d(\\\\d+)'))");
    }

    /**
     * Tests 'xxxxxxx'.match(new RegExp('(x+)(x*)')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xxxxxxx,xxxxxxx,")
    public void test7() throws Exception {
        test("'xxxxxxx'.match(new RegExp('(x+)(x*)'))");
    }

    /**
     * Tests 'xxxxxxyyyyyy'.match(new RegExp('x*y+$')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xxxxxxyyyyyy")
    public void test8() throws Exception {
        test("'xxxxxxyyyyyy'.match(new RegExp('x*y+$'))");
    }

    /**
     * Tests 'abcdef'.match(/[\d]*[\s]*bc./).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bcd")
    public void test9() throws Exception {
        test("'abcdef'.match(/[\\d]*[\\s]*bc./)");
    }

    /**
     * Tests 'abcdef'.match(/bc..[\d]*[\s]*\/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bcde")
    public void test10() throws Exception {
        test("'abcdef'.match(/bc..[\\d]*[\\s]*/)");
    }

    /**
     * Tests 'a1b2c3'.match(/.*\/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a1b2c3")
    public void test11() throws Exception {
        test("'a1b2c3'.match(/.*/)");
    }

    /**
     * Tests 'a0.b2.c3'.match(/[xyz]*1/.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    @NotYetImplemented
    public void test12() throws Exception {
        test("'a0.b2.c3'.match(/[xyz]*1/");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
