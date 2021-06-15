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
package com.jaspersoft.ireport.components.table.undo;

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class TableCellResizeUndoableEdit extends ObjectPropertyUndoableEdit {

    private boolean main = false;
    private StandardTable table = null;
    private JasperDesign jasperDesign = null;
    
    @Override
    public String getPresentationName() {
        return "Table Row/Column resize";
    }
    
    public TableCellResizeUndoableEdit(StandardTable table, JasperDesign jasperDesign, Object object, String propertyName, Class propertyClass, Object oldValue,  Object newValue)
    {
        super(object, propertyName, propertyClass, oldValue,  newValue);
        this.table = table;
        this.jasperDesign = jasperDesign;
    }

    public

    boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        if (isMain() && table != null)
        {
             // This trick is used to force a document rebuild...
           table.getEventSupport().firePropertyChange( StandardTable.PROPERTY_COLUMNS , null, null);
           TableModelUtils.fixTableLayout(table, jasperDesign);
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if (isMain() && table != null)
        {
           // This trick is used to force a document rebuild...
           table.getEventSupport().firePropertyChange( StandardTable.PROPERTY_COLUMNS , null, null);
           TableModelUtils.fixTableLayout(table, jasperDesign);
        }
    }

}
