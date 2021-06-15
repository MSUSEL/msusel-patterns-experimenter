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
package net.sourceforge.ganttproject;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

class TreeTableCellEditorImpl  implements TableCellEditor {
	private TableCellEditor myProxiedEditor;

	TreeTableCellEditorImpl(TableCellEditor proxiedEditor){
		myProxiedEditor = proxiedEditor;
	}
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		final Component result = myProxiedEditor.getTableCellEditorComponent(arg0, arg1,arg2, arg3, arg4);
		if (result instanceof JTextComponent) {
			((JTextComponent)result).selectAll();
			result.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent arg0) {
					super.focusGained(arg0);
					((JTextComponent)result).selectAll();
					result.removeFocusListener(this);
				}

				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					super.focusLost(arg0);
				}
				
			});
		}
		return result;
	}

	public Object getCellEditorValue() {
		return myProxiedEditor.getCellEditorValue();
	}

	public boolean isCellEditable(EventObject arg0) {
		return myProxiedEditor.isCellEditable(arg0);
	}

	public boolean shouldSelectCell(EventObject arg0) {
		return myProxiedEditor.shouldSelectCell(arg0);
	}

	public boolean stopCellEditing() {
		return myProxiedEditor.stopCellEditing();
	}

	public void cancelCellEditing() {
		myProxiedEditor.cancelCellEditing();
	}

	public void addCellEditorListener(CellEditorListener arg0) {
		myProxiedEditor.addCellEditorListener(arg0);
	}
	public void removeCellEditorListener(CellEditorListener arg0) {
		myProxiedEditor.removeCellEditorListener(arg0);
	}
}
