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
 * Created on 14.03.2005
 */
package net.sourceforge.ganttproject.action;

import java.awt.event.ActionEvent;

import javax.swing.event.UndoableEditEvent;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.undo.GPUndoListener;
import net.sourceforge.ganttproject.undo.GPUndoManager;

/**
 * @author bard
 */
public class UndoAction extends GPAction implements GPUndoListener {
    private GPUndoManager myUndoManager;

    private final GanttProject appli;

    public UndoAction(GPUndoManager undoManager, String iconSize, GanttProject appli) {
        super(null, iconSize);
        myUndoManager = undoManager;
        myUndoManager.addUndoableEditListener(this);
        this.appli = appli;
        setEnabled(myUndoManager.canUndo());        
    }

    public void actionPerformed(ActionEvent e) {
    	appli.getUIFacade().setStatusText(GanttLanguage.getInstance().getText("undo"));
        myUndoManager.undo();
    }

    public void undoableEditHappened(UndoableEditEvent e) {
        setEnabled(myUndoManager.canUndo());
    }

    public void undoOrRedoHappened() {
        setEnabled(myUndoManager.canUndo());
    }

    protected String getIconFilePrefix() {
        return "undo_";
    }

    public void isIconVisible(boolean isNull) {
        setIconVisible(isNull);
    }

    protected String getLocalizedName() {
        return GanttProject.correctLabel(GanttLanguage.getInstance().getText(
                "undo"));
    }

}
