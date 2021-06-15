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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a CSSRuleList.
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 */
public class CSSRuleList extends SimpleScriptable {

    private static final long serialVersionUID = 6068213884501456020L;

    private final CSSStyleSheet stylesheet_;
    private final org.w3c.dom.css.CSSRuleList rules_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSRuleList() {
        stylesheet_ = null;
        rules_ = null;
    }

    /**
     * Creates a new instance.
     * @param stylesheet the stylesheet
     */
    public CSSRuleList(final CSSStyleSheet stylesheet) {
        stylesheet_ = stylesheet;
        rules_ = stylesheet.getWrappedSheet().getCssRules();
        setParentScope(stylesheet.getParentScope());
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the length of this list.
     * @return the length of this list.
     */
    public int jsxGet_length() {
        if (rules_ != null) {
            return rules_.getLength();
        }
        return 0;
    }

    /**
     * Returns the item in the given index.
     * @param index the index
     * @return the item in the given index
     */
    public Object jsxFunction_item(final int index) {
        return null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Object[] getIds() {
        final List<String> idList = new ArrayList<String>();

        final int length = jsxGet_length();
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_21)) {
            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }

            idList.add("length");
            idList.add("item");
        }
        else {
            idList.add("length");

            for (int i = 0; i < length; i++) {
                idList.add(Integer.toString(i));
            }
        }
        return idList.toArray();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (name.equals("length") || name.equals("item")) {
            return true;
        }
        try {
            final int index = Integer.parseInt(name);
            final int length = jsxGet_length();
            if (index >= 0 && index < length) {
                return true;
            }
        }
        catch (final Exception e) {
            //ignore
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        return CSSRule.create(stylesheet_, rules_.item(index));
    }

}
