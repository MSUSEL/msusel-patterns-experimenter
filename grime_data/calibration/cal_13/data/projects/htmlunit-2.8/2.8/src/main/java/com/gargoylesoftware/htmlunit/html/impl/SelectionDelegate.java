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
package com.gargoylesoftware.htmlunit.html.impl;

import java.io.Serializable;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <p>Contains standard selection-related functionality used by various input elements.</p>
 *
 * <p>From <a href="http://www.whatwg.org/specs/web-apps/current-work/#selection">the HTML5 spec</a>:</p>
 *
 * <blockquote>Mostly for historical reasons, in addition to the browsing context's selection, each
 * textarea and input element has an independent selection. These are the text field selections.</blockquote>
 *
 * @version $Revision: 5864 $
 * @author Daniel Gredler
 */
public class SelectionDelegate implements Serializable {

    private static final long serialVersionUID = -5611492671640559880L;

    /** The owner element. */
    private final SelectableTextInput element_;

    /** The field selection, which is independent of the browsing context's selection. */
    private final Range selection_;

    /**
     * Creates a new instance for the specified element.
     * @param element the owner element
     */
    public SelectionDelegate(final SelectableTextInput element) {
        element_ = element;
        selection_ = new SimpleRange(element, element.getText().length());
    }

    /**
     * Focuses the owner element and selects all of its text.
     */
    public void select() {
        element_.focus();
        setSelectionStart(0);
        setSelectionEnd(element_.getText().length());
    }

    /**
     * Returns the selected text in the owner element, or <tt>null</tt> if there is no selected text.
     * @return the selected text in the owner element, or <tt>null</tt> if there is no selected text
     */
    public String getSelectedText() {
        return selection_.toString();
    }

    /**
     * Returns the start position of the selected text in the owner element.
     * @return the start position of the selected text in the owner element
     */
    public int getSelectionStart() {
        return selection_.getStartOffset();
    }

    /**
     * Sets the start position of the selected text in the owner element.
     * @param selectionStart the start position of the selected text in the owner element
     */
    public void setSelectionStart(int selectionStart) {
        final int length = element_.getText().length();
        selectionStart = Math.max(0, Math.min(selectionStart, length));
        selection_.setStart(element_, selectionStart);
        if (selection_.getEndOffset() < selectionStart) {
            selection_.setEnd(element_, selectionStart);
        }
        makeThisTheOnlySelectionIfEmulatingIE();
    }

    /**
     * Returns the end position of the selected text in the owner element.
     * @return the end position of the selected text in the owner element
     */
    public int getSelectionEnd() {
        return selection_.getEndOffset();
    }

    /**
     * Sets the end position of the selected text in the owner element.
     * @param selectionEnd the end position of the selected text in the owner element
     */
    public void setSelectionEnd(int selectionEnd) {
        final int length = element_.getText().length();
        selectionEnd = Math.min(length, Math.max(selectionEnd, 0));
        selection_.setEnd(element_, selectionEnd);
        if (selection_.getStartOffset() > selectionEnd) {
            selection_.setStart(element_, selectionEnd);
        }
        makeThisTheOnlySelectionIfEmulatingIE();
    }

    private void makeThisTheOnlySelectionIfEmulatingIE() {
        final Page page = element_.getPage();
        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            if (htmlPage.getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_13)) {
                htmlPage.setSelectionRange(selection_);
            }
        }
    }

}
