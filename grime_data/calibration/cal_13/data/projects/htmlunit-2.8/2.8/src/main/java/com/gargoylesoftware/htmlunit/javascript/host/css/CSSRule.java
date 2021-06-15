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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a CSSRule.
 *
 * @version $Revision: 5618 $
 * @author Ahmed Ashour
 */
public class CSSRule extends SimpleScriptable {

    /**
     * The rule is a <code>CSSUnknownRule</code>.
     */
    public static final short UNKNOWN_RULE              = org.w3c.dom.css.CSSRule.UNKNOWN_RULE;
    /**
     * The rule is a <code>CSSStyleRule</code>.
     */
    public static final short STYLE_RULE                = org.w3c.dom.css.CSSRule.STYLE_RULE;
    /**
     * The rule is a <code>CSSCharsetRule</code>.
     */
    public static final short CHARSET_RULE              = org.w3c.dom.css.CSSRule.CHARSET_RULE;
    /**
     * The rule is a <code>CSSImportRule</code>.
     */
    public static final short IMPORT_RULE               = org.w3c.dom.css.CSSRule.IMPORT_RULE;
    /**
     * The rule is a <code>CSSMediaRule</code>.
     */
    public static final short MEDIA_RULE                = org.w3c.dom.css.CSSRule.MEDIA_RULE;
    /**
     * The rule is a <code>CSSFontFaceRule</code>.
     */
    public static final short FONT_FACE_RULE            = org.w3c.dom.css.CSSRule.FONT_FACE_RULE;
    /**
     * The rule is a <code>CSSPageRule</code>.
     */
    public static final short PAGE_RULE                 = org.w3c.dom.css.CSSRule.PAGE_RULE;

    private static final long serialVersionUID = -8392071769970424557L;

    private final CSSStyleSheet stylesheet_;

    private final org.w3c.dom.css.CSSRule rule_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSRule() {
        stylesheet_ = null;
        rule_ = null;
    }

    /**
     * Creates a CSSRule according to the specified rule type.
     * @param stylesheet the Stylesheet of this rule
     * @param rule the wrapped rule
     * @return a CSSRule subclass according to the rule type
     */
    public static CSSRule create(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        switch (rule.getType()) {
            case STYLE_RULE:
                return new CSSStyleRule(stylesheet, rule);
            case IMPORT_RULE:
                return new CSSImportRule(stylesheet, rule);
            default:
                throw new UnsupportedOperationException("CSSRule "
                    + rule.getClass().getName() + " is not yet supported.");
        }
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        stylesheet_ = stylesheet;
        rule_ = rule;
        setParentScope(stylesheet);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the type of the rule.
     * @return the type of the rule.
     */
    public short jsxGet_type() {
        return rule_.getType();
    }

    /**
     * Returns the parsable textual representation of the rule.
     * This reflects the current state of the rule and not its initial value.
     * @return the parsable textual representation of the rule.
     */
    public String jsxGet_cssText() {
        return rule_.getCssText();
    }

    /**
     * Sets the parsable textual representation of the rule.
     * @param cssText the parsable textual representation of the rule
     */
    public void jsxSet_cssText(final String cssText) {
        rule_.setCssText(cssText);
    }

    /**
     * Returns the style sheet that contains this rule.
     * @return the style sheet that contains this rule.
     */
    public CSSStyleSheet jsxGet_parentStyleSheet() {
        return stylesheet_;
    }

    /**
     * If this rule is contained inside another rule (e.g. a style rule inside an @media block),
     * this is the containing rule. If this rule is not nested inside any other rules, this returns <code>null</code>.
     * @return the parent rule
     */
    public CSSRule jsxGet_parentRule() {
        final org.w3c.dom.css.CSSRule parentRule = rule_.getParentRule();
        if (parentRule != null) {
            return CSSRule.create(stylesheet_, parentRule);
        }
        return null;
    }

    /**
     * Returns the wrapped rule.
     * @return the wrapped rule.
     */
    protected org.w3c.dom.css.CSSRule getRule() {
        return rule_;
    }

}
