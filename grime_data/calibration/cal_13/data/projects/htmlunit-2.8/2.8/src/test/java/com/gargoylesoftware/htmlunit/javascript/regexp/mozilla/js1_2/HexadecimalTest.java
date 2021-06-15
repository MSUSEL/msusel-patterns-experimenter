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
 * Tests originally in '/js/src/tests/js1_2/regexp/hexadecimal.js'.
 *
 * @version $Revision: 5767 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HexadecimalTest extends WebDriverTestCase {

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    public void test1() throws Exception {
        final String initialScript = "testPattern = '\\\\x41\\\\x42\\\\x43\\\\x44\\\\x45\\\\x46\\\\x47\\\\x48\\\\x49"
            + "\\\\x4A\\\\x4B\\\\x4C\\\\x4D\\\\x4E\\\\x4F\\\\x50\\\\x51\\\\x52\\\\x53\\\\x54\\\\x55\\\\x56\\\\x57"
            + "\\\\x58\\\\x59\\\\x5A';"
            + "var testString = '12345ABCDEFGHIJKLMNOPQRSTUVWXYZ67890';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefghijklmnopqrstuvwxyz")
    public void test2() throws Exception {
        final String initialScript = "var testPattern = '\\\\x61\\\\x62\\\\x63\\\\x64\\\\x65\\\\x66\\\\x67\\\\x68"
            + "\\\\x69\\\\x6A\\\\x6B\\\\x6C\\\\x6D\\\\x6E\\\\x6F\\\\x70\\\\x71\\\\x72\\\\x73\\\\x74\\\\x75\\\\x76"
            + "\\\\x77\\\\x78\\\\x79\\\\x7A';"
            + "var testString = '12345AabcdefghijklmnopqrstuvwxyzZ67890';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" !\"#$%&'()*+,-./0123")
    public void test3() throws Exception {
        final String initialScript = "var testPattern = '\\\\x20\\\\x21\\\\x22\\\\x23\\\\x24\\\\x25\\\\x26\\\\x27"
            + "\\\\x28\\\\x29\\\\x2A\\\\x2B\\\\x2C\\\\x2D\\\\x2E\\\\x2F\\\\x30\\\\x31\\\\x32\\\\x33';"
            + "var testString = 'abc !\"#$%&\\'()*+,-./0123ZBC';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("456789:;<=>?@")
    public void test4() throws Exception {
        final String initialScript = "var testPattern = '\\\\x34\\\\x35\\\\x36\\\\x37\\\\x38\\\\x39\\\\x3A\\\\x3B"
            + "\\\\x3C\\\\x3D\\\\x3E\\\\x3F\\\\x40';"
            + "var testString = '123456789:;<=>?@ABC';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("{|}~")
    public void test5() throws Exception {
        final String initialScript = "var testPattern = '\\\\x7B\\\\x7C\\\\x7D\\\\x7E';"
            + "var testString = '1234{|}~ABC';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests 'canthisbeFOUND'.match(new RegExp('[A-\\x5A]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("FOUND")
    public void test6() throws Exception {
        test("'canthisbeFOUND'.match(new RegExp('[A-\\\\x5A]+'))");
    }

    /**
     * Tests 'canthisbeFOUND'.match(new RegExp('[\\x61-\\x7A]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("canthisbe")
    public void test7() throws Exception {
        test("'canthisbeFOUND'.match(new RegExp('[\\\\x61-\\\\x7A]+'))");
    }

    /**
     * Tests 'canthisbeFOUND'.match(/[\x61-\x7A]+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("canthisbe")
    public void test8() throws Exception {
        test("'canthisbeFOUND'.match(/[\\x61-\\x7A]+/)");
    }

    private void test(final String script) throws Exception {
        test(null, script);
    }

    private void test(final String initialScript, final String script) throws Exception {
        String html = "<html><head><title>foo</title><script>\n";
        if (initialScript != null) {
            html += initialScript + ";\n";
        }
        html += "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
