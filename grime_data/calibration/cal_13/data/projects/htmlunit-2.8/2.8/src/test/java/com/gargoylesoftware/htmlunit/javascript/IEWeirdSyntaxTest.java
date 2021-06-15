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
package com.gargoylesoftware.htmlunit.javascript;

import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Test for IE weird JavaScript syntax.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEWeirdSyntaxTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "2" })
    public void semicolon_before_finally() throws Exception {
        doTestTryCatchFinally("", ";");
        doTestTryCatchFinally("", "\n;\n");
        doTestTryCatchFinally("", "\n;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "2" })
    public void semicolon_before_catch() throws Exception {
        doTestTryCatchFinally(";", "");
        doTestTryCatchFinally("\n;\n", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "2" })
    public void semicolonAndComment_before_catchAndFinally() throws Exception {
        doTestTryCatchFinally("// comment\n;\n", "");
        doTestTryCatchFinally("", "// comment\n;\n");
        doTestTryCatchFinally("", "// comment\n // other comment\n;\n");
        doTestTryCatchFinally("", "// comment\n ; // other comment\n");
    }

    private void doTestTryCatchFinally(final String beforeCatch, final String beforeFinally) throws Exception {
        final String html = "<html><script>\n"
            + "try {\n"
            +  "alert('1');\n"
            +  "}" + beforeCatch
            +  "catch(e) {\n"
            +  "}" + beforeFinally
            +  "finally {\n"
            +  "alert('2');\n"
            +  "}\n"
            +  "</script></html>";
        doTestWithEvaluatorExceptionExceptForIE(html);
    }

    private void doTestWithEvaluatorExceptionExceptForIE(final String html) throws Exception {
        try {
            loadPageWithAlerts(html);
        }
        catch (final ScriptException e) {
            if (e.getCause() instanceof EvaluatorException && !getBrowserVersion().isIE()) {
                // this is normal
            }
            else {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    public void windowDotHandlerFunction() throws Exception {
        final String html = "<html><head><script>\n"
            + "function window.onload() {\n"
            + "  alert(1);\n"
            + "}\n"
            + "</script></head>"
            + "<body></body></html>";
        doTestWithEvaluatorExceptionExceptForIE(html);
    }
}
