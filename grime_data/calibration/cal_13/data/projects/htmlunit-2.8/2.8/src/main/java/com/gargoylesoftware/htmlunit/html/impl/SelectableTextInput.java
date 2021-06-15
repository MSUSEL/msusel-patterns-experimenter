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

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.Page;

/**
 * Internal interface which defines an input element which contains selectable text. This interface just keeps
 * the various implementations in sync as to selection functionality, and provides a definition of the functionality
 * required by the {@link SelectionDelegate} so that it can do its job. This interface is not public because it is
 * an internal contract.
 *
 * @version $Revision: 5302 $
 * @author Daniel Gredler
 */
public interface SelectableTextInput extends Node {

    /**
     * Returns the page which contains this element.
     * @return the page which contains this element
     */
    Page getPage();

    /**
     * Focuses this element.
     */
    void focus();

    /**
     * Focuses this element and selects all of its text.
     */
    void select();

    /**
     * Returns all of the text in this element.
     * @return all of the text in this element
     */
    String getText();

    /**
     * Sets the text in this element.
     * @param text the text to put in this element
     */
    void setText(String text);

    /**
     * Returns the selected text in this element, or <tt>null</tt> if there is no selected text in this element.
     * @return the selected text in this element, or <tt>null</tt> if there is no selected text in this element
     */
    String getSelectedText();

    /**
     * Returns the start position of the selected text in this element.
     * @return the start position of the selected text in this element
     */
    int getSelectionStart();

    /**
     * Sets the start position of the selected text in this element.
     * @param selectionStart the start position of the selected text in this element
     */
    void setSelectionStart(int selectionStart);

    /**
     * Returns the end position of the selected text in this element.
     * @return the end position of the selected text in this element
     */
    int getSelectionEnd();

    /**
     * Sets the end position of the selected text in this element.
     * @param selectionEnd the end position of the selected text in this element
     */
    void setSelectionEnd(int selectionEnd);

}
