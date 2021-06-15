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
package org.lnicholls.galleon.gui;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
/**
 * JTable model used by GUI
 */
public class OptionsTableModel extends AbstractTableModel {

    private static Logger log = Logger.getLogger(OptionsTableModel.class.getName());

    public OptionsTableModel(ArrayList columnNames, ArrayList columnValues) {
        mColumnNames = columnNames;
        mColumnValues = columnValues;
    }

    public int getColumnCount() {
        return mColumnNames.size();
    }

    public int getRowCount() {
        return mColumnValues.size();
    }

    public String getColumnName(int col) {
        return (String) mColumnNames.get(col);
    }
    
    
    public Object getValueAt(int row, int col) {
        return getValueAt(row, col, true);
    }

    public Object getValueAt(int row, int col, boolean hideSensitive) {
        String name = getColumnName(col);
        if (hideSensitive && (name.toLowerCase().equals("password")))
            return "******";
        else
        {
            ArrayList values = (ArrayList) mColumnValues.get(row);                        Object object = values.get(col);                        return object;        }
    }

    public Class getColumnClass(int c) {
        Object value = getValueAt(0, c);
        if (value != null)
            return value.getClass();
        else
            return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        ArrayList values = null;
        if (row < mColumnValues.size())
            values = (ArrayList) mColumnValues.get(row);

        if (values == null) {
            values = new ArrayList();
            values.add(col, value);
            mColumnValues.add(row, values);
        } else {
            if (col >= values.size())
                values.add(col, value);
            else
                values.set(col, value);
        }
        fireTableStructureChanged();
    }

    public void removeRow(int row) {
        mColumnValues.remove(row);
        fireTableStructureChanged();
    }

    public ArrayList getValues() {
        return mColumnValues;
    }

    private ArrayList mColumnNames;

    private ArrayList mColumnValues;
}