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

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a static NodeList.
 *
 * @version $Revision: 5622 $
 * @author Ahmed Ashour
 */
public class StaticNodeList extends SimpleScriptable {

    private static final long serialVersionUID = -9198113809743670670L;

    private final List<Node> elements_;

    /**
     * Default constructor.
     */
    public StaticNodeList() {
        elements_ = new ArrayList<Node>();
    }

    /**
     * Constructor.
     * @param elements the elements
     * @param parentScope the parent scope
     */
    public StaticNodeList(final List<Node> elements, final ScriptableObject parentScope) {
        elements_ = elements;
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     */
    public Node jsxFunction_item(final int index) {
        if (index < 0 || index >= jsxGet_length()) {
            return null;
        }
        return elements_.get(index);
    }

    /**
     * Returns the length of this element array.
     * @return the length of this element array
     */
    public int jsxGet_length() {
        return elements_.size();
    }

}
