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

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Evaluator for IE conditional expressions.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms537512.aspx">MSDN documentation</a>
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
public final class IEConditionalCommentExpressionEvaluator {

    /**
     * Hide constructor of utility class
     */
    private IEConditionalCommentExpressionEvaluator() {
        // nothing
    }

    /**
     * Evaluates the condition.
     * @param condition the condition like "lt IE 7"
     * @param browserVersion the browser version. Note that currently it can only be an IE browser.
     * @return the evaluation result
     */
    public static boolean evaluate(String condition, final BrowserVersion browserVersion) {
        condition = condition.trim();
        if ("IE".equals(condition)) {
            return true;
        }
        else if ("true".equals(condition)) {
            return true;
        }
        else if ("false".equals(condition)) {
            return false;
        }
        else if (condition.contains("&")) {
            return evaluate(StringUtils.substringBefore(condition, "&"), browserVersion)
                && evaluate(StringUtils.substringAfter(condition, "&"), browserVersion);
        }
        else if (condition.contains("|")) {
            return evaluate(StringUtils.substringBefore(condition, "|"), browserVersion)
                || evaluate(StringUtils.substringAfter(condition, "|"), browserVersion);
        }
        else if (condition.startsWith("!")) {
            return !evaluate(condition.substring(1), browserVersion);
        }
        else if (condition.startsWith("IE")) {
            final String currentVersion = String.valueOf(browserVersion.getBrowserVersionNumeric());
            return currentVersion.startsWith(condition.substring(2).trim());
        }
        else if (condition.startsWith("lte IE")) {
            return browserVersion.getBrowserVersionNumeric() <= parseVersion(condition.substring(6));
        }
        else if (condition.startsWith("lt IE")) {
            return browserVersion.getBrowserVersionNumeric() < parseVersion(condition.substring(5));
        }
        else if (condition.startsWith("gt IE")) {
            return browserVersion.getBrowserVersionNumeric() > parseVersion(condition.substring(5));
        }
        else if (condition.startsWith("gte IE")) {
            return browserVersion.getBrowserVersionNumeric() >= parseVersion(condition.substring(6));
        }
        else if (condition.startsWith("lt")) {
            return true;
        }
        else if (condition.startsWith("gt")) {
            return false;
        }
        else if (condition.startsWith("(")) {
            // in fact not fully correct if () can be nested
            return evaluate(StringUtils.substringBetween(condition, "(", ")"), browserVersion);
        }
        else {
            return false;
        }
    }

    private static float parseVersion(final String s) {
        return Float.parseFloat(s);
    }
}
