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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * A JavaScript object for a CSSStyleRule.
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class CSSStyleRule extends CSSRule {

    private static final long serialVersionUID = 207943879569003822L;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSStyleRule() {
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSStyleRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the textual representation of the selector for the rule set.
     * @return the textual representation of the selector for the rule set
     */
    public String jsxGet_selectorText() {
        String selectorText = ((org.w3c.dom.css.CSSStyleRule) getRule()).getSelectorText();
        final Pattern p = Pattern.compile("[\\.#]?[a-zA-Z]+");
        final Matcher m = p.matcher(selectorText);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String fixedName = m.group();
            // this should be handled with the right regex but...
            if (fixedName.startsWith(".") || fixedName.startsWith("#")) {
                // nothing
            }
            else if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_27)) {
                fixedName = fixedName.toUpperCase();
            }
            else {
                fixedName = fixedName.toLowerCase();
            }
            m.appendReplacement(sb, fixedName);
        }
        m.appendTail(sb);

        selectorText = sb.toString().replaceAll("\\*([\\.#])", "$1"); // ".foo" and not "*.foo"
        return selectorText;
    }

    /**
     * Sets the textual representation of the selector for the rule set.
     * @param selectorText the textual representation of the selector for the rule set
     */
    public void jsxSet_selectorText(final String selectorText) {
        ((org.w3c.dom.css.CSSStyleRule) getRule()).setSelectorText(selectorText);
    }

    /**
     * Returns the declaration-block of this rule set.
     * @return the declaration-block of this rule set
     */
    public CSSStyleDeclaration jsxGet_style() {
        return new CSSStyleDeclaration(getParentScope(), ((org.w3c.dom.css.CSSStyleRule) getRule()).getStyle());
    }
}
