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

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;

/**
 * The web window for a frame or iframe.
 *
 * @version $Revision: 5726 $
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class FrameWindow extends WebWindowImpl {

    private static final long serialVersionUID = -4767759560108055220L;
    private final BaseFrame frame_;

    /**
     * Creates an instance for a given frame.
     */
    FrameWindow(final BaseFrame frame) {
        super(frame.getPage().getWebClient());
        frame_ = frame;
        final WebWindowImpl parent = (WebWindowImpl) getParentWindow();
        parent.addChildWindow(this);
    }

    /**
     * {@inheritDoc}
     * A FrameWindow shares it's name with it's containing frame.
     */
    @Override
    public String getName() {
        return frame_.getNameAttribute();
    }

    /**
     * {@inheritDoc}
     * A FrameWindow shares it's name with it's containing frame.
     */
    @Override
    public void setName(final String name) {
        frame_.setNameAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getParentWindow() {
        return frame_.getPage().getEnclosingWindow();
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getTopWindow() {
        return getParentWindow().getTopWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        return getScriptObject() == null
            || !(getEnclosedPage().getWebResponse() instanceof StringWebResponse);
        // TODO: find a better way to distinguish content written by document.open(),...
    }

    /**
     * Returns the HTML page in which the &lt;frame&gt; or &lt;iframe&gt; tag is contained
     * for this frame window.
     * This is a facility method for <code>(HtmlPage) (getParentWindow().getEnclosedPage())</code>.
     * @return the page in the parent window
     */
    public HtmlPage getEnclosingPage() {
        return (HtmlPage) frame_.getPage();
    }

    /**
     * Gets the DOM node of the (i)frame containing this window.
     * @return the DOM node
     */
    public BaseFrame getFrameElement() {
        return frame_;
    }

    /**
     * Gives a basic representation for debugging purposes.
     * @return a basic representation
     */
    @Override
    public String toString() {
        return "FrameWindow[name=\"" + getName() + "\"]";
    }
}
