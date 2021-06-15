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
 * Created on 12.03.2005
 */
package net.sourceforge.ganttproject.undo;

import java.io.IOException;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.document.DocumentManager;
import net.sourceforge.ganttproject.parser.ParserFactory;

/**
 * @author bard
 */
public class UndoManagerImpl implements GPUndoManager {
    private UndoableEditSupport myUndoEventDispatcher;

    private UndoManager mySwingUndoManager;

    private DocumentManager myDocumentManager;

    private ParserFactory myParserFactory;

    private IGanttProject myProject;

    private UndoableEditImpl swingEditImpl;

    public UndoManagerImpl(IGanttProject project, ParserFactory parserFactory,
            DocumentManager documentManager) {
        myProject = project;
        myParserFactory = parserFactory;
        myDocumentManager = documentManager;
        mySwingUndoManager = new UndoManager();
        myUndoEventDispatcher = new UndoableEditSupport();

    }

    public void undoableEdit(String localizedName, Runnable editImpl) {

        try {
            swingEditImpl = new UndoableEditImpl(localizedName, editImpl, this);
            mySwingUndoManager.addEdit(swingEditImpl);
            fireUndoableEditHappened(swingEditImpl);
        } catch (IOException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
        }
    }

    private void fireUndoableEditHappened(UndoableEditImpl swingEditImpl) {
        myUndoEventDispatcher.postEdit(swingEditImpl);
    }

    private void fireUndoOrRedoHappened() {
        UndoableEditListener[] listeners = myUndoEventDispatcher
                .getUndoableEditListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((GPUndoListener) listeners[i]).undoOrRedoHappened();
        }
    }

    DocumentManager getDocumentManager() {
        return myDocumentManager;
    }

    protected ParserFactory getParserFactory() {
        return myParserFactory;
    }

    IGanttProject getProject() {
        return myProject;
    }

    public boolean canUndo() {
        return mySwingUndoManager.canUndo();
    }

    public boolean canRedo() {
        return mySwingUndoManager.canRedo();
    }

    public void undo() throws CannotUndoException {
        mySwingUndoManager.undo();
        fireUndoOrRedoHappened();
    }

    public void redo() throws CannotRedoException {
        mySwingUndoManager.redo();
        fireUndoOrRedoHappened();
    }

    public String getUndoPresentationName() {
        return mySwingUndoManager.getUndoPresentationName();
    }

    public String getRedoPresentationName() {
        return mySwingUndoManager.getRedoPresentationName();
    }

    public void addUndoableEditListener(GPUndoListener listener) {
        myUndoEventDispatcher.addUndoableEditListener(listener);
    }

    public void removeUndoableEditListener(GPUndoListener listener) {
        myUndoEventDispatcher.removeUndoableEditListener(listener);
    }

    public void die() {
        if (swingEditImpl != null)
            swingEditImpl.die();
        if (mySwingUndoManager != null)
            mySwingUndoManager.discardAllEdits();
    }
}
