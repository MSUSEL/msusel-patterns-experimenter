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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * A JavaScript object representing a TR.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLTableRowElement extends HTMLTableComponent {

    private static final long serialVersionUID = 3256441404401397812L;

    private HTMLCollection cells_; // has to be a member to have equality (==) working

    /**
     * Creates an instance.
     */
    public HTMLTableRowElement() {
        // Empty.
    }

    /**
     * Returns the index of the row within the parent table.
     * @return the index of the row within the parent table
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534377.aspx">MSDN Documentation</a>
     */
    public int jsxGet_rowIndex() {
        final HtmlTableRow row = (HtmlTableRow) getDomNodeOrDie();
        final HtmlTable table = row.getEnclosingTable();
        if (table == null) { // a not attached document.createElement('TR')
            return -1;
        }
        return table.getRows().indexOf(row);
    }

    /**
     * Returns the index of the row within the enclosing thead, tbody or tfoot.
     * @return the index of the row within the enclosing thead, tbody or tfoot
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534621.aspx">MSDN Documentation</a>
     * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-79105901">
     * DOM Level 1</a>
     */
    public int jsxGet_sectionRowIndex() {
        DomNode row = getDomNodeOrDie();
        final HtmlTable table = ((HtmlTableRow) row).getEnclosingTable();
        if (table == null) { // a not attached document.createElement('TR')
            if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_108)) {
                return -1;
            }
            // IE 6, 7 and 8 return really strange values: large integers that are not constants
            // as tests on different browsers give different results
            return 5461640;
        }
        int index = -1;
        while (row != null) {
            if (row instanceof HtmlTableRow) {
                ++index;
            }
            row = row.getPreviousSibling();
        }
        return index;
    }

    /**
     * Returns the cells in the row.
     * @return the cells in the row
     */
    public Object jsxGet_cells() {
        if (cells_ == null) {
            cells_ = new HTMLCollection(this);
            cells_.init(getDomNodeOrDie(), "./td|th");
        }
        return cells_;
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public String jsxGet_bgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param bgColor the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public void jsxSet_bgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Inserts a new cell at the specified index in the element's cells collection. If the index
     * is -1 or there is no index specified, then the cell is appended at the end of the
     * element's cells collection.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536455.aspx">MSDN Documentation</a>
     * @param index specifies where to insert the cell in the tr.
     *        The default value is -1, which appends the new cell to the end of the cells collection
     * @return the newly-created cell
     */
    public Object jsxFunction_insertCell(final Object index) {
        int position = -1;
        if (index != Undefined.instance) {
            position = (int) Context.toNumber(index);
        }
        final HtmlTableRow htmlRow = (HtmlTableRow) getDomNodeOrDie();

        final boolean indexValid = (position >= -1 && position <= htmlRow.getCells().size());
        if (indexValid) {
            final HtmlElement newCell = ((HtmlPage) htmlRow.getPage()).createElement("td");
            if (position == -1 || position == htmlRow.getCells().size()) {
                htmlRow.appendChild(newCell);
            }
            else {
                htmlRow.getCell(position).insertBefore(newCell);
            }
            return getScriptableFor(newCell);
        }
        throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
    }

    /**
     * Deletes the cell at the specified index in the element's cells collection. If the index
     * is -1 (or while simulating IE, when there is no index specified), then the last cell is deleted.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536406.aspx">MSDN Documentation</a>
     * @see <a href="http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109/html.html#ID-11738598">W3C DOM Level2</a>
     * @param index specifies the cell to delete.
     */
    public void jsxFunction_deleteCell(final Object index) {
        int position = -1;
        if (index != Undefined.instance) {
            position = (int) Context.toNumber(index);
        }
        else if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_172)) {
            throw Context.reportRuntimeError("No enough arguments");
        }

        final HtmlTableRow htmlRow = (HtmlTableRow) getDomNodeOrDie();

        if (position == -1) {
            position = htmlRow.getCells().size() - 1;
        }
        final boolean indexValid = (position >= -1 && position <= htmlRow.getCells().size());
        if (!indexValid) {
            throw Context.reportRuntimeError("Index or size is negative or greater than the allowed amount");
        }

        htmlRow.getCell(position).remove();
    }
}
