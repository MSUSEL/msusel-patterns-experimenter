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

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * An interface that represents one window in a browser. It could be a top level window or a frame.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 */
public interface WebWindow extends Serializable {

    /**
     * Returns the name of this window.
     *
     * @return the name of this window
     */
    String getName();

    /**
     * Sets the name of this window.
     *
     * @param name the new window name
     */
    void setName(final String name);

    /**
     * Returns the currently loaded page or null if no page has been loaded.
     *
     * @return the currently loaded page or null if no page has been loaded
     */
    Page getEnclosedPage();

    /**
     * Sets the currently loaded page.
     *
     * @param page the new page or null if there is no page (ie empty window)
     */
    void setEnclosedPage(final Page page);

    /**
     * Returns the window that contains this window. If this is a top
     * level window, then return this window.
     *
     * @return the parent window or this window if there is no parent
     */
    WebWindow getParentWindow();

    /**
     * Returns the top level window that contains this window. If this
     * is a top level window, then return this window.
     *
     * @return the top level window that contains this window or this
     * window if there is no parent.
     */
    WebWindow getTopWindow();

    /**
     * Returns the web client that "owns" this window.
     *
     * @return the web client or null if this window has been closed
     */
    WebClient getWebClient();

    /**
     * Returns this window's navigation history.
     *
     * @return this window's navigation history
     */
    History getHistory();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Sets the JavaScript object that corresponds to this element. This is not guaranteed
     * to be set even if there is a JavaScript object for this HTML element.
     *
     * @param scriptObject the JavaScript object
     */
    void setScriptObject(final Object scriptObject);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the JavaScript object that corresponds to this element.
     *
     * @return the JavaScript object that corresponds to this element
     */
    Object getScriptObject();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the job manager for this window.
     *
     * @return the job manager for this window
     */
    JavaScriptJobManager getJobManager();

    /**
     * Indicates if this window is closed. No action should be performed on a closed window.
     * @return <code>true</code> when the window is closed
     */
    boolean isClosed();

}
