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

import java.util.EventObject;

/**
 * This is the event class for notifications about changes to the DOM structure.
 *
 * @version $Revision: 5726 $
 * @author Ahmed Ashour
 * @see DomChangeListener
 */
public class DomChangeEvent extends EventObject {

    private static final long serialVersionUID = -7301406882254534491L;

    private final DomNode changedNode_;

    /**
     * Constructs a new DomChangeEvent from the given parent node and a changed node.
     *
     * @param parentNode the parent of the node that was changed
     * @param changedNode the node that has been added or deleted
     */
    public DomChangeEvent(final DomNode parentNode, final DomNode changedNode) {
        super(parentNode);
        changedNode_ = changedNode;
    }

    /**
     * Returns the parent of the node that was changed.
     * @return the parent of the node that was changed
     */
    public DomNode getParentNode() {
        return (DomNode) getSource();
    }

    /**
     * Returns the node that has been added or deleted.
     * @return the node that has been added or deleted
     */
    public DomNode getChangedNode() {
        return changedNode_;
    }
}
