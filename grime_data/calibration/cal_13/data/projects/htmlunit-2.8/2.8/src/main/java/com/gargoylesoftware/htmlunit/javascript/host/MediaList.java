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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * A JavaScript object for a MediaList.
 *
 * @version $Revision: 5618 $
 * @author Daniel Gredler
 */
public class MediaList extends SimpleScriptable {

    private static final long serialVersionUID = 1766588734264714462L;

    private final org.w3c.dom.stylesheets.MediaList wrappedList_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public MediaList() {
        wrappedList_ = null;
    }

    /**
     * Creates a new instance.
     * @param parent the parent style
     * @param wrappedList the wrapped media list that this host object exposes
     */
    public MediaList(final CSSStyleSheet parent, final org.w3c.dom.stylesheets.MediaList wrappedList) {
        wrappedList_ = wrappedList;
        setParentScope(parent);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the number of media in the list.
     * @return the number of media in the list
     */
    public int jsxGet_length() {
        return wrappedList_.getLength();
    }

}
