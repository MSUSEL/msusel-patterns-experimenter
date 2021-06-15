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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptableProxy;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * Proxy for a {@link HTMLDocument} script object. In theory we could satisfy single-document requirements
 * without a proxy, by reusing (with appropriate cleanup and re-initialization) a single {@link HTMLDocument}
 * instance across various pages. However, we allow users to keep references to old pages as they navigate
 * across a series of pages, and all of these pages need to be usable -- so we can't just leave these old
 * pages without a <tt>window.document</tt> object.
 *
 * @version $Revision: 5565 $
 * @author Daniel Gredler
 */
public class HTMLDocumentProxy extends SimpleScriptableProxy<HTMLDocument> {

    private static final long serialVersionUID = -6997147276828365589L;

    private final WebWindow webWindow_;

    /**
     * Construct a proxy for the {@link HTMLDocument} of the {@link WebWindow}.
     * @param webWindow the window
     */
    public HTMLDocumentProxy(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLDocument getDelegee() {
        final Window w = (Window) webWindow_.getScriptObject();
        return w.getDocument();
    }

}
