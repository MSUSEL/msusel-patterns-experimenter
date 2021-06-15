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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "tr".
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlTableRow extends HtmlElement {

    private static final long serialVersionUID = 1296770669696592851L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "tr";

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that this element is contained within
     * @param attributes the initial attributes
     */
    HtmlTableRow(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * @return an Iterator over the all HtmlTableCell objects in this row
     */
    public CellIterator getCellIterator() {
        return new CellIterator();
    }

    /**
     * @return an immutable list containing all the HtmlTableCells held by this object
     * @see #getCellIterator
     */
    public List<HtmlTableCell> getCells() {
        final List<HtmlTableCell> result = new ArrayList<HtmlTableCell>();
        for (final CellIterator iterator = getCellIterator(); iterator.hasNext();) {
            result.add(iterator.next());
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * @param index the 0-based index
     * @return the cell at the given index
     * @throws IndexOutOfBoundsException if there is no cell at the given index
     */
    public HtmlTableCell getCell(final int index) throws IndexOutOfBoundsException {
        int count = 0;
        for (final CellIterator iterator = getCellIterator(); iterator.hasNext(); count++) {
            final HtmlTableCell next = iterator.nextCell();
            if (count == index) {
                return next;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns the value of the attribute "align". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "align"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttribute("align");
    }

    /**
     * Returns the value of the attribute "char". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "char"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharAttribute() {
        return getAttribute("char");
    }

    /**
     * Returns the value of the attribute "charoff". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "charoff"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCharoffAttribute() {
        return getAttribute("charoff");
    }

    /**
     * Returns the value of the attribute "valign". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "valign"
     * or an empty string if that attribute isn't defined.
     */
    public final String getValignAttribute() {
        return getAttribute("valign");
    }

    /**
     * Gets the table containing this row.
     * @return the table
     */
    public HtmlTable getEnclosingTable() {
        return (HtmlTable) getEnclosingElement("table");
    }

    /**
     * Returns the value of the attribute "bgcolor". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "bgcolor"
     * or an empty string if that attribute isn't defined.
     */
    public final String getBgcolorAttribute() {
        return getAttribute("bgcolor");
    }

    /**
     * An Iterator over the HtmlTableCells contained in this row. It will also dive
     * into nested forms, even though that is illegal HTML.
     */
    public class CellIterator implements Iterator<HtmlTableCell> {
        private HtmlTableCell nextCell_;
        private HtmlForm currentForm_;

        /** Creates an instance. */
        public CellIterator() {
            setNextCell(getFirstChild());
        }

        /** @return whether there is another cell available */
        public boolean hasNext() {
            return nextCell_ != null;
        }

        /**
         * @return the next cell
         * @throws NoSuchElementException if no cell is available
         */
        public HtmlTableCell next() throws NoSuchElementException {
            return nextCell();
        }

        /**
         * Removes the cell under the cursor from the current row.
         * @throws IllegalStateException if there is no current row
         */
        public void remove() throws IllegalStateException {
            if (nextCell_ == null) {
                throw new IllegalStateException();
            }
            final DomNode sibling = nextCell_.getPreviousSibling();
            if (sibling != null) {
                sibling.remove();
            }
        }

        /**
         * @return the next cell
         * @throws NoSuchElementException if no cell is available
         */
        public HtmlTableCell nextCell() throws NoSuchElementException {
            if (nextCell_ != null) {
                final HtmlTableCell result = nextCell_;
                setNextCell(nextCell_.getNextSibling());
                return result;
            }
            throw new NoSuchElementException();
        }

        /**
         * Sets the internal position to the next cell, starting at the given node
         * @param node the node to mark as the next cell; if this is not a cell, the
         *        next reachable cell will be marked.
         */
        private void setNextCell(final DomNode node) {
            nextCell_ = null;
            for (DomNode next = node; next != null; next = next.getNextSibling()) {
                if (next instanceof HtmlTableCell) {
                    nextCell_ = (HtmlTableCell) next;
                    return;
                }
                else if (currentForm_ == null && next instanceof HtmlForm) {
                    // Completely illegal HTML but some of the big sites (ie amazon) do this
                    currentForm_ = (HtmlForm) next;
                    setNextCell(next.getFirstChild());
                    return;
                }
            }
            if (currentForm_ != null) {
                final DomNode form = currentForm_;
                currentForm_ = null;
                setNextCell(form.getNextSibling());
            }
        }
    }
}
