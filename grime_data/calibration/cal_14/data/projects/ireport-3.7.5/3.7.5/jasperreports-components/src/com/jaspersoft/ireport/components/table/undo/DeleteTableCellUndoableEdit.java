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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.components.table.undo;

import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.designer.undo.AggregatedUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: AddTableCellUndoableEdit.java 0 2010-03-26 10:36:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeleteTableCellUndoableEdit extends AggregatedUndoableEdit {

    private DesignCell cell = null;
    private BaseColumn column = null;
    private byte section = 0;
    private String groupName = null;
    private StandardTable table = null;
    private JasperDesign jasperDesign = null;


    public DeleteTableCellUndoableEdit(StandardTable table, JasperDesign jasperDesign, DesignCell cell, BaseColumn column, byte section, JRGroup group)
    {
        this(table, jasperDesign, cell, column, section, (group == null) ? null : group.getName());
    }

    public DeleteTableCellUndoableEdit(StandardTable table, JasperDesign jasperDesign, DesignCell cell, BaseColumn column, byte section, String groupName)
    {
        this.cell = cell;
        this.column = column;
        this.section = section;
        this.groupName = groupName;
        this.table = table;
        this.jasperDesign = jasperDesign;
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();
        TableModelUtils.addCell(column, cell, section, groupName);
        TableModelUtils.fixTableLayout(table, jasperDesign);
    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();
        TableModelUtils.removeCell(column, section, groupName);
        TableModelUtils.fixTableLayout(table, jasperDesign);
    }

    @Override
    public String getPresentationName() {
        return "Delete cell";
    }


}
