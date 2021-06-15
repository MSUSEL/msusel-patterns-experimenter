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

package org.hsqldb.util;

import java.util.Vector;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

// sqlbob@users 20020401 - patch 1.7.0 by sqlbob (RMP) - enhancements
// deccles@users 20040412 - patch 933671 - various bug fixes

/** Simple table model to represent a grid of tuples.
 *
 * New class based on Hypersonic SQL original
 *
 * @author dmarshall@users
 * @version 1.7.2
 * @since 1.7.0
 */
class GridSwing extends AbstractTableModel {

    JTable   jtable = null;
    Object[] headers;
    Vector   rows;

    /**
     * Default constructor.
     */
    public GridSwing() {

        super();

        headers = new Object[0];    // initially empty
        rows    = new Vector();     // initially empty
    }

    /**
     * Get the name for the specified column.
     */
    public String getColumnName(int i) {
        return headers[i].toString();
    }

    public Class getColumnClass(int i) {

        if (rows.size() > 0) {
            Object o = getValueAt(0, i);

            if (o != null) {
                if ((o instanceof java.sql.Timestamp)
                    || (o instanceof java.sql.Time)) {
                    // This is a workaround for JTable's lack of a default
                    // renderer that displays times.
                    // Without this workaround, Timestamps (and similar
                    // classes) will be displayed as dates without times,
                    // since JTable will match these classes to their
                    // java.util.Date superclass.
                    return Object.class;  // renderer will draw .toString().
                }
                return o.getClass();
            }
        }

        return super.getColumnClass(i);
    }

    /**
     * Get the number of columns.
     */
    public int getColumnCount() {
        return headers.length;
    }

    /**
     * Get the number of rows currently in the table.
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Get the current column headings.
     */
    public Object[] getHead() {
        return headers;
    }

    /**
     * Get the current table data.
     *  Each row is represented as a <code>String[]</code>
     *  with a single non-null value in the 0-relative
     *  column position.
     *  <p>The first row is at offset 0, the nth row at offset n etc.
     */
    public Vector getData() {
        return rows;
    }

    /**
     * Get the object at the specified cell location.
     */
    public Object getValueAt(int row, int col) {

        if (row >= rows.size()) {
            return null;
        }

        Object[] colArray = (Object[]) rows.elementAt(row);

        if (col >= colArray.length) {
            return null;
        }

        return colArray[col];
    }

    /**
     * Set the name of the column headings.
     */
    public void setHead(Object[] h) {

        headers = new Object[h.length];

        // System.arraycopy(h, 0, headers, 0, h.length);
        for (int i = 0; i < h.length; i++) {
            headers[i] = h[i];
        }
    }

    /**
     * Append a tuple to the end of the table.
     */
    public void addRow(Object[] r) {

        Object[] row = new Object[r.length];

        // System.arraycopy(r, 0, row, 0, r.length);
        for (int i = 0; i < r.length; i++) {
            row[i] = r[i];

            if (row[i] == null) {

//                row[i] = "(null)";
            }
        }

        rows.addElement(row);
    }

    /**
     * Remove data from all cells in the table (without
     *  affecting the current headings).
     */
    public void clear() {
        rows.removeAllElements();
    }

    public void setJTable(JTable table) {
        jtable = table;
    }

    public void fireTableChanged(TableModelEvent e) {
        super.fireTableChanged(e);
        autoSizeTableColumns(jtable);
    }

    public static void autoSizeTableColumns(JTable table) {

        TableModel  model        = table.getModel();
        TableColumn column       = null;
        Component   comp         = null;
        int         headerWidth  = 0;
        int         maxCellWidth = Integer.MIN_VALUE;
        int         cellWidth    = 0;
        TableCellRenderer headerRenderer =
            table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            comp = headerRenderer.getTableCellRendererComponent(table,
                    column.getHeaderValue(), false, false, 0, 0);
            headerWidth  = comp.getPreferredSize().width + 10;
            maxCellWidth = Integer.MIN_VALUE;

            for (int j = 0; j < Math.min(model.getRowCount(), 30); j++) {
                TableCellRenderer r = table.getCellRenderer(j, i);

                comp = r.getTableCellRendererComponent(table,
                                                       model.getValueAt(j, i),
                                                       false, false, j, i);
                cellWidth = comp.getPreferredSize().width;

                if (cellWidth >= maxCellWidth) {
                    maxCellWidth = cellWidth;
                }
            }

            column.setPreferredWidth(Math.max(headerWidth, maxCellWidth)
                                     + 10);
        }
    }
}
