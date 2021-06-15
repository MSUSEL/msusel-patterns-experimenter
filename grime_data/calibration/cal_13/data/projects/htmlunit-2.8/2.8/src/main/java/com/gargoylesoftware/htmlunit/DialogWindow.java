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
package com.gargoylesoftware.htmlunit;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A window opened in JavaScript via either <tt>window.showModalDialog</tt> or <tt>window.showModelessDialog</tt>.
 * @version $Revision: 5726 $
 * @author Daniel Gredler
 */
public class DialogWindow extends WebWindowImpl {

    /** Serial version UID. */
    private static final long serialVersionUID = -8851612155741170131L;

    /** The arguments object exposed via the <tt>dialogArguments</tt> JavaScript property. */
    private Object arguments_;

    /**
     * Creates a new instance.
     * @param webClient the web client that "owns" this window
     * @param arguments the arguments object exposed via the <tt>dialogArguments</tt> JavaScript property
     */
    protected DialogWindow(final WebClient webClient, final Object arguments) {
        super(webClient);
        arguments_ = arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        return getScriptObject() == null;
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScriptObject(final Object scriptObject) {
        final ScriptableObject so = (ScriptableObject) scriptObject;
        if (so != null) {
            so.put("dialogArguments", so, arguments_);
        }
        super.setScriptObject(scriptObject);
    }

    /**
     * Closes this window.
     */
    public void close() {
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }

}
