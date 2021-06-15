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
package com.jaspersoft.ireport.designer.crosstab.undo;

import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;

/**
 *
 * @author gtoffoli
 */
public class CellResizeUndoableEdit extends ObjectPropertyUndoableEdit {

    private boolean main = false;
    private JRDesignCrosstab crosstab = null;
    
    @Override
    public String getPresentationName() {
        return "Cell resize";
    }

    
    public CellResizeUndoableEdit(Object object, String propertyName, Class propertyClass, Object oldValue,  Object newValue)
    {
        super(object, propertyName, propertyClass, oldValue,  newValue);
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
        if (isMain() && crosstab != null) 
        {
            crosstab.preprocess();
            // This trick is used to force a document rebuild...
           crosstab.getEventSupport().firePropertyChange( JRDesignCrosstab.PROPERTY_CELLS , null, null);
        }
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if (isMain() && crosstab != null) 
        {
            crosstab.preprocess();
            // This trick is used to force a document rebuild...
           crosstab.getEventSupport().firePropertyChange( JRDesignCrosstab.PROPERTY_CELLS , null, null);
        }
    }

    public JRDesignCrosstab getCrosstab() {
        return crosstab;
    }

    public void setCrosstab(JRDesignCrosstab crosstab) {
        this.crosstab = crosstab;
    }
    
    
    
    
}
