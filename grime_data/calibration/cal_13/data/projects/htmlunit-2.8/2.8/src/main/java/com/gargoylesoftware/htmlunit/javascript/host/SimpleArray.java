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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for simple array allowing access per key and index (like {@link MimeTypeArray}).
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
public class SimpleArray extends SimpleScriptable implements ScriptableWithFallbackGetter {
    private static final long serialVersionUID = 8025124211062703153L;
    private final List<Object> elements_ = new ArrayList<Object>();

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public SimpleArray() {
        // nothing
    }

    /**
     * Returns the item at the given index.
     * @param index the index
     * @return the item at the given position
     */
    public Object jsxFunction_item(final int index) {
        return elements_.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final Object response = jsxFunction_namedItem(name);
        if (response != null) {
            return response;
        }
        return Context.getUndefinedValue();
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the
     * index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        final SimpleArray array = (SimpleArray) start;
        final List<Object> elements = array.elements_;

        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        return null;
    }

    /**
     * Returns the item at the given index.
     * @param name the item name
     * @return the item with the given name
     */
    public Object jsxFunction_namedItem(final String name) {
        for (final Object element : elements_) {
            if (name.equals(getItemName(element))) {
                return element;
            }
        }
        return null;
    }

    /**
     * Gets the name of the element.
     * Should be abstract but current implementation of prototype configuration doesn't allow it.
     * @param element the array's element
     * @return the element's name
     */
    protected String getItemName(final Object element) {
        return null;
    }

    /**
     * Gets the array size.
     * @return the number elements
     */
    public int jsxGet_length() {
        return elements_.size();
    }

    /**
     * Adds an element.
     * @param element the element to add
     */
    void add(final Object element) {
        elements_.add(element);
    }
}
