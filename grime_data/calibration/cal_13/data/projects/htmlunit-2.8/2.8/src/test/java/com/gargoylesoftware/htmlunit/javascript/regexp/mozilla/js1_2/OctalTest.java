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
 * Tests originally in '/js/src/tests/js1_2/regexp/octal.js'.
 *
 * @version $Revision: 5767 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class OctalTest extends WebDriverTestCase {

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    @NotYetImplemented
    public void test1() throws Exception {
        final String initialScript = "var testPattern = '\\\\101\\\\102\\\\103\\\\104\\\\105\\\\106\\\\107\\\\110"
            + "\\\\111\\\\112\\\\113\\\\114\\\\115\\\\116\\\\117\\\\120\\\\121\\\\122\\\\123\\\\124\\\\125\\\\126"
            + "\\\\127\\\\130\\\\131\\\\132';"
            + "var testString = '12345ABCDEFGHIJKLMNOPQRSTUVWXYZ67890';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefghijklmnopqrstuvwxyz")
    @NotYetImplemented
    public void test2() throws Exception {
        final String initialScript = "var testPattern = '\\\\141\\\\142\\\\143\\\\144\\\\145\\\\146\\\\147\\\\150"
            + "\\\\151\\\\152\\\\153\\\\154\\\\155\\\\156\\\\157\\\\160\\\\161\\\\162\\\\163\\\\164\\\\165\\\\166"
            + "\\\\167\\\\170\\\\171\\\\172';"
            + "var testString = '12345AabcdefghijklmnopqrstuvwxyzZ67890';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" !\"#$%&'()*+,-./0123")
    @NotYetImplemented
    public void test3() throws Exception {
        final String initialScript = "var testPattern = '\\\\40\\\\41\\\\42\\\\43\\\\44\\\\45\\\\46\\\\47\\\\50"
            + "\\\\51\\\\52\\\\53\\\\54\\\\55\\\\56\\\\57\\\\60\\\\61\\\\62\\\\63';"
            + "var testString = 'abc !\"#$%&\\'()*+,-./0123ZBC';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("456789:;<=>?@")
    @NotYetImplemented
    public void test4() throws Exception {
        final String initialScript = "var testPattern = '\\\\64\\\\65\\\\66\\\\67\\\\70\\\\71\\\\72\\\\73\\\\74\\\\75"
            + "\\\\76\\\\77\\\\100';"
            + "var testString = '123456789:;<=>?@ABC';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests testString.match(new RegExp(testPattern)).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("{|}~")
    @NotYetImplemented
    public void test5() throws Exception {
        final String initialScript = "var testPattern = '\\\\173\\\\174\\\\175\\\\176';"
            + "var testString = '1234{|}~ABC';";
        test(initialScript, "testString.match(new RegExp(testPattern))");
    }

    /**
     * Tests 'canthisbeFOUND'.match(new RegExp('[A-\\132]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("FOUND")
    @NotYetImplemented
    public void test6() throws Exception {
        test("'canthisbeFOUND'.match(new RegExp('[A-\\\\132]+'))");
    }

    /**
     * Tests 'canthisbeFOUND'.match(new RegExp('[\\141-\\172]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("canthisbe")
    @NotYetImplemented
    public void test7() throws Exception {
        test("'canthisbeFOUND'.match(new RegExp('[\\\\141-\\\\172]+'))");
    }

    /**
     * Tests 'canthisbeFOUND'.match(/[\141-\172]+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("canthisbe")
    @NotYetImplemented
    public void test8() throws Exception {
        test("'canthisbeFOUND'.match(/[\\141-\\172]+/)");
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
