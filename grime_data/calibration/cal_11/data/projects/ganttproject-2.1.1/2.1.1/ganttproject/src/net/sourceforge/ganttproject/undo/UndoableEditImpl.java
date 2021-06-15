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
package net.sourceforge.ganttproject.undo;

import java.io.File;
import java.io.IOException;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.io.GPSaver;

/**
 * @author bard
 */
class UndoableEditImpl extends AbstractUndoableEdit {
    private String myPresentationName;

    private Runnable myEditImpl;

    private Document myDocumentBefore;

    private Document myDocumentAfter;

    private UndoManagerImpl myManager;

    UndoableEditImpl(String localizedName, Runnable editImpl,
            UndoManagerImpl manager) throws IOException {
        // System.out.println ("UndoableEditImpl : " + localizedName);
        myManager = manager;
        myPresentationName = localizedName;
        myEditImpl = editImpl;
        myDocumentBefore = saveFile();
        editImpl.run();
        myDocumentAfter = saveFile();
    }

    private Document saveFile() throws IOException {
        File tempFile = createTemporaryFile();
        tempFile.deleteOnExit();
        Document doc = myManager.getDocumentManager().getDocument(
                tempFile.getAbsolutePath());
        doc.write();
        //GPSaver saver = myManager.getParserFactory().newSaver();
        //saver.save(doc.getOutputStream());
        return doc;
    }

    public boolean canUndo() {
        return myDocumentBefore.canRead();
    }

    public boolean canRedo() {
        return myDocumentAfter.canRead();
    }

    public void redo() throws CannotRedoException {
        try {
            restoreDocument(myDocumentAfter);
        } catch (IOException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
            throw new CannotRedoException();
        }
    }

    public void undo() throws CannotUndoException {
        try {
            restoreDocument(myDocumentBefore);
        } catch (IOException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
            throw new CannotRedoException();
        }
    }

    private void restoreDocument(Document document) throws IOException {
        Document projectDocument = myManager.getProject().getDocument(); 
		myManager.getProject().close();
        document.read();
        myManager.getProject().setDocument(projectDocument);
        
    }

    public String getPresentationName() {
        return myPresentationName;
    }

    File createTemporaryFile() throws IOException {
        return File.createTempFile("_GanttProject_qSave", ".gan");
    }

}
