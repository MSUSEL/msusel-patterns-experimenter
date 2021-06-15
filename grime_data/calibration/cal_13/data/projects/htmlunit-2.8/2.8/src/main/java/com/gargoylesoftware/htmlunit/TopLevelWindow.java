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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A window representing a top level browser window.
 *
 * @version $Revision: 5726 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 * @author Ahmed Ashour
 */
public class TopLevelWindow extends WebWindowImpl {

    private static final long serialVersionUID = 2448888802967514906L;

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(TopLevelWindow.class);

    /** The window which caused this window to be opened, if any. */
    private WebWindow opener_;

    /**
     * Creates an instance.
     * @param name the name of the new window
     * @param webClient the web client that "owns" this window
     */
    protected TopLevelWindow(final String name, final WebClient webClient) {
        super(webClient);
        WebAssert.notNull("name", name);
        setName(name);
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        final Page enclosedPage = getEnclosedPage();
        return getScriptObject() == null
            || enclosedPage.getWebResponse().getWebRequest().getUrl() == WebClient.URL_ABOUT_BLANK
            || !(enclosedPage.getWebResponse() instanceof StringWebResponse);
        // TODO: find a better way to distinguish content written by document.open(),...
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "TopLevelWindow[name=\"" + getName() + "\"]";
    }

    /**
     * Sets the opener property. This is the WebWindow that caused this new window to be opened.
     * @param opener the new opener
     */
    public void setOpener(final WebWindow opener) {
        opener_ = opener;
    }

    /**
     * Returns the opener property. This is the WebWindow that caused this new window to be opened.
     * @return the opener
     */
    public WebWindow getOpener() {
        return opener_;
    }

    /**
     * Closes this window.
     */
    public void close() {
        setClosed();
        final Page page = getEnclosedPage();
        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            if (!htmlPage.isOnbeforeunloadAccepted()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The registered OnbeforeunloadHandler rejected the window close event.");
                }
                return;
            }
            htmlPage.cleanUp();
        }
        getJobManager().shutdown();
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }

}
