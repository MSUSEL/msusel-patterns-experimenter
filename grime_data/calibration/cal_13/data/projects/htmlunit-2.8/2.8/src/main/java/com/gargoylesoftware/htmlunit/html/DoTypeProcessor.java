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

import java.io.Serializable;

abstract class DoTypeProcessor implements Serializable {

    private static final long serialVersionUID = -1857188321096014188L;

    void doType(final String currentValue, final int selectionStart, final int selectionEnd,
            final char c, final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {

        String newValue = currentValue;
        int cursorPosition = selectionStart;
        if (c == '\b') {
            if (selectionStart > 0) {
                newValue = currentValue.substring(0, selectionStart - 1) + currentValue.substring(selectionStart);
                cursorPosition = selectionStart - 1;
            }
        }
        else if (c >= '\uE000' && c <= '\uF8FF') {
            // nothing, this is private use area
            // see http://www.unicode.org/charts/PDF/UE000.pdf
        }
        else if (acceptChar(c)) {
            if (selectionStart != currentValue.length()) {
                newValue = currentValue.substring(0, selectionStart) + c + currentValue.substring(selectionEnd);
            }
            else {
                newValue += c;
            }
            cursorPosition++;
        }

        typeDone(newValue, cursorPosition);
    }

    /**
     * Indicates if the provided character can by "typed" in the text.
     * @param c the character
     * @return <code>true</code> if it is accepted
     */
    protected boolean acceptChar(final char c) {
        return (c == ' ' || !Character.isWhitespace(c));
    }

    abstract void typeDone(final String newValue, final int newCursorPosition);

}
