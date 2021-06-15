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

import static com.gargoylesoftware.htmlunit.html.IEConditionalCommentExpressionEvaluator.evaluate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link IEConditionalCommentExpressionEvaluator}.
 * Due to current implementation, conditional comment expressions get evaluated only when the simulated browser is IE.
 * @version $Revision: 5905 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEConditionalCommentExpressionEvaluatorTest extends WebTestCase {

    /**
     * Test for expression [if IE].
     */
    @Test
    @Alerts("true")
    public void IE() {
        doTest("IE");
    }

    /**
     * Test for expressions [if IE 5].
     */
    @Test
    @Alerts("false")
    public void IE_5() {
        doTest("IE 5");
    }

    /**
     * Test for expressions [if IE 6].
     */
    @Test
    @Alerts(IE6 = "true", IE = "false")
    public void IE_6() {
        doTest("IE 6");
    }

    /**
     * Test for expressions [if IE 7].
     */
    @Test
    @Alerts(IE7 = "true", IE = "false")
    public void IE_7() {
        doTest("IE 7");
    }

    /**
     * Test for expressions [if IE 8].
     */
    @Test
    @Alerts(IE8 = "true", IE = "false")
    public void IE_8() {
        doTest("IE 8");
    }

    /**
     * Test for expression [if !IE].
     */
    @Test
    @Alerts("false")
    public void notIE() {
        doTest("!IE");
    }

    /**
     * Test for expressions [if lt IE 5.5].
     */
    @Test
    @Alerts(IE = "false", FF = "true")
    public void lt_IE_5_5() {
        doTest("lt IE 5.5");
    }

    /**
     * Test for expressions [if lt IE 6].
     */
    @Test
    @Alerts("false")
    public void lt_IE_6() {
        doTest("lt IE 6");
    }

    /**
     * Test for expressions [if lt IE 7].
     */
    @Test
    @Alerts(IE6 = "true", IE = "false")
    public void lt_IE_7() {
        doTest("lt IE 7");
    }

    /**
     * Test for expressions [if lt IE 8].
     */
    @Test
    @Alerts(IE6 = "true", IE7 = "true", IE = "false")
    public void lt_IE_8() {
        doTest("lt IE 8");
    }

    /**
     * Test for expressions [if lt IE 9].
     */
    @Test
    @Alerts("true")
    public void lt_IE_9() {
        doTest("lt IE 9");
    }

    /**
     * Test for expressions [if gt IE 5.5].
     */
    @Test
    @Alerts("true")
    public void gt_IE_5_5() {
        doTest("gt IE 5.5");
    }

    /**
     * Test for expressions [if gt IE 6].
     */
    @Test
    @Alerts(IE6 = "false", IE = "true")
    public void gt_IE_6() {
        doTest("gt IE 6");
    }

    /**
     * Test for expressions [if gt IE 7].
     */
    @Test
    @Alerts(IE8 = "true", IE = "false")
    public void gt_IE_7() {
        doTest("gt IE 7");
    }

    /**
     * Test for expressions [if gt IE 8].
     */
    @Test
    @Alerts("false")
    public void gt_IE_8() {
        doTest("gt IE 8");
    }

    /**
     * Test for expressions [if gte IE 5.5].
     */
    @Test
    @Alerts("true")
    public void gte_IE_5_5() {
        doTest("gte IE 5.5");
    }

    /**
     * Test for expressions [if gte IE 6].
     */
    @Test
    @Alerts("true")
    public void gte_IE_6() {
        doTest("gte IE 6");
    }

    /**
     * Test for expressions [if gte IE 7].
     */
    @Test
    @Alerts(IE6 = "false", IE = "true")
    public void gte_IE_7() {
        doTest("gte IE 7");
    }

    /**
     * Test for expressions [if gte IE 8].
     */
    @Test
    @Alerts(IE6 = "false", IE7 = "false", IE = "true")
    public void gte_IE_8() {
        doTest("gte IE 8");
    }

    /**
     * Test for expressions [if !(IE 5)].
     */
    @Test
    @Alerts("true")
    public void parenthese_5() {
        doTest("!(IE 5)");
    }

    /**
     * Test for expressions [if !(IE 6)].
     */
    @Test
    @Alerts(IE6 = "false", IE = "true")
    public void parenthese_6() {
        doTest("!(IE 6)");
    }

    /**
     * Test for expressions [if !(IE 7)].
     */
    @Test
    @Alerts(IE7 = "false", IE = "true")
    public void parenthese_7() {
        doTest("!(IE 7)");
    }

    /**
     * Test for expressions [if !(IE 8)].
     */
    @Test
    @Alerts(IE8 = "false", IE = "true")
    public void parenthese_8() {
        doTest("!(IE 8)");
    }

    /**
     * Test for expressions [(gt IE 6)&(lt IE 8)].
     */
    @Test
    @Alerts(IE7 = "true", IE = "false")
    public void and() {
        doTest("(gt IE 6)&(lt IE 8)");
    }

    /**
     * Test for expressions [if (IE 6)|(IE 7)].
     */
    @Test
    @Alerts(IE6 = "true", IE7 = "true", IE = "false")
    public void or() {
        doTest("(IE 6)|(IE 7)");
    }

    /**
     * Test for expressions [if true].
     */
    @Test
    @Alerts("true")
    public void true_() {
        doTest("true");
    }

    /**
     * Test for expressions [if false].
     */
    @Test
    @Alerts("false")
    public void false_() {
        doTest("false");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("false")
    public void mso_1() {
        doTest("mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("false")
    public void mso_2() {
        doTest("gte mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("true")
    public void mso_3() {
        doTest("lt mso 9");
    }

    /**
     * Test for expressions with "mso" (HTML code generated by MS Office).
     */
    @Test
    @Alerts("true")
    public void mso_4() {
        doTest("lt mso 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("false")
    public void unknown_1() {
        doTest("foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("false")
    public void unknown_2() {
        doTest("gte foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("false")
    public void unknown_3() {
        doTest("gt foo 1");
    }

    /**
     * Test for expressions with unexpected identifier.
     */
    @Test
    @Alerts("true")
    public void unknown_4() {
        doTest("lt foo 1");
    }

    private void doTest(final String expression) {
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.isFirefox()) {
            return;
        }
        final String expected = getExpectedAlerts()[0];
        Assert.assertEquals(expression + " for " + browserVersion.getNickname(),
                expected, Boolean.toString(evaluate(expression, browserVersion)));
    }
}
