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
package com.jaspersoft.ireport.designer.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class DeleteGroupUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignGroup group = null;
    private JRDesignDataset dataset = null;
    private int index = 0;
    
    public DeleteGroupUndoableEdit(JRDesignGroup group, JRDesignDataset dataset, int index)
    {
        this.group = group;
        this.dataset = dataset;
        this.index = index;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        try {

            getDataset().addGroup(getGroup());
        
            // We need to use a trick to put the group in the right position...
            getDataset().getGroupsList().remove(getGroup());
            getDataset().getGroupsList().add(index, getGroup());
            getDataset().getEventSupport().fireCollectionElementAddedEvent(JRDesignDataset.PROPERTY_GROUPS, group, getDataset().getGroupsList().size() - 1);
        
        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
            throw new CannotRedoException();
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        getDataset().removeGroup(getGroup());
        
    }
    
    @Override
    public String getPresentationName() {
        
        String groupName = "";
        if (getGroup() != null && getGroup().getName() != null)
        {
            groupName = getGroup().getName();
        }
        
        return "Delete group " + groupName;
        
    }

    public JRDesignGroup getGroup() {
        return group;
    }

    public void setGroup(JRDesignGroup group) {
        this.group = group;
    }

    public JRDesignDataset getDataset() {
        return dataset;
    }

    public void setDataset(JRDesignDataset dataset) {
        this.dataset = dataset;
    }
}
