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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.openide.awt.UndoRedo;
import org.openide.util.Mutex;
import org.openide.util.MutexException;

/**
 *
 * @author gtoffoli
 * UndoRedoManager based on the undo manager of the FormModel in NetBeans
 */
public class UndoRedoManager extends UndoRedo.Manager {
    
        private Mutex.ExceptionAction<Object> runUndo = new Mutex.ExceptionAction<Object>() {
            public Object run() throws Exception {
                superUndo();
                return null;
            }
        };
        private Mutex.ExceptionAction<Object> runRedo = new Mutex.ExceptionAction<Object>() {
            public Object run() throws Exception {
                superRedo();
                return null;
            }
        };

        public void superUndo() throws CannotUndoException {
            super.undo();
        }
        public void superRedo() throws CannotRedoException {
            super.redo();
        }

        @Override
        public void undo() throws CannotUndoException {
            if (java.awt.EventQueue.isDispatchThread()) {
                superUndo();
            }
            else {
                try {
                    Mutex.EVENT.readAccess(runUndo);
                }
                catch (MutexException ex) {
                    Exception e = ex.getException();
                    if (e instanceof CannotUndoException)
                        throw (CannotUndoException) e;
                    else // should not happen, ignore
                        e.printStackTrace();
                }
            }
        }

        @Override
        public void redo() throws CannotRedoException {
            if (java.awt.EventQueue.isDispatchThread()) {
                superRedo();
            }
            else {
                try {
                    Mutex.EVENT.readAccess(runRedo);
                }
                catch (MutexException ex) {
                    Exception e = ex.getException();
                    if (e instanceof CannotRedoException)
                        throw (CannotRedoException) e;
                    else // should not happen, ignore
                        e.printStackTrace();
                }
            }
        }
        
        
    }