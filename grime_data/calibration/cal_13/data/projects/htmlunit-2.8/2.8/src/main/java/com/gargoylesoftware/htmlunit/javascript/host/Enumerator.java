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

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;

/**
 * A JavaScript object for Enumerator.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/en-us/library/6ch9zb09.aspx">MSDN Documentation</a>
 */
public class Enumerator extends SimpleScriptable {

    private static final long serialVersionUID = -7030539919126620376L;

    private int index_;

    private HTMLCollection collection_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Enumerator() {
        // Empty.
    }

    /**
     * JavaScript constructor.
     * @param o the object to enumerate over
     */
    public void jsConstructor(final Object o) {
        if (o instanceof HTMLCollection) {
            collection_ = (HTMLCollection) o;
        }
        else if (o instanceof HTMLFormElement) {
            collection_ = ((HTMLFormElement) o).jsxGet_elements();
        }
        else {
            throw new IllegalArgumentException(String.valueOf(o));
        }
    }

    /**
     * Returns whether the enumerator is at the end of the collection or not.
     * @return whether the enumerator is at the end of the collection or not
     */
    public boolean jsxFunction_atEnd() {
        return index_ >= collection_.getLength();
    }

    /**
     * Returns the current item in the collection.
     * @return the current item in the collection
     */
    public Object jsxFunction_item() {
        if (!jsxFunction_atEnd()) {
            SimpleScriptable scriptable = (SimpleScriptable) collection_.get(index_, collection_);
            scriptable = scriptable.clone();
            scriptable.setCaseSensitive(false);
            return scriptable;
        }
        return Undefined.instance;
    }

    /**
     * Resets the current item in the collection to the first item.
     */
    public void jsxFunction_moveFirst() {
        index_ = 0;
    }

    /**
     * Moves the current item to the next item in the collection.
     */
    public void jsxFunction_moveNext() {
        index_++;
    }
}
