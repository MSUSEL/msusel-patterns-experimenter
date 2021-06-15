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

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * An implementation of DomNodeList that is much less expensive for iteration.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
class SiblingDomNodeList extends AbstractSequentialList<DomNode> implements DomNodeList<DomNode> {

    private DomNode parent_;

    public SiblingDomNodeList(final DomNode parent) {
        parent_ = parent;
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        int length = 0;
        for (DomNode node = parent_.getFirstChild(); node != null; node = node.getNextSibling()) {
            length++;
        }
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return getLength();
    }

    /**
     * {@inheritDoc}
     */
    public Node item(final int index) {
        return get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode get(final int index) {
        int i = 0;
        for (DomNode node = parent_.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (i == index) {
                return node;
            }
            i++;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<DomNode> listIterator(final int index) {
        return new SiblingListIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SiblingDomNodeList[" + parent_ + "]";
    }

    private class SiblingListIterator implements ListIterator<DomNode> {
        private DomNode prev_;
        private DomNode next_;
        private int nextIndex_;

        public SiblingListIterator(final int index) {
            next_ = parent_.getFirstChild();
            nextIndex_ = 0;
            for (int i = 0; i < index; i++) {
                next();
            }
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return next_ != null;
        }

        /**
         * {@inheritDoc}
         */
        public DomNode next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prev_ = next_;
            next_ = next_.getNextSibling();
            nextIndex_++;
            return prev_;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasPrevious() {
            return prev_ != null;
        }

        public DomNode previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            next_ = prev_;
            prev_ = prev_.getPreviousSibling();
            nextIndex_--;
            return next_;
        }

        public int nextIndex() {
            return nextIndex_;
        }

        public int previousIndex() {
            return nextIndex_ - 1;
        }

        public void add(final DomNode e) {
            throw new UnsupportedOperationException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(final DomNode e) {
            throw new UnsupportedOperationException();
        }
    }
}
