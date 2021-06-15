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
package org.apache.hadoop.eclipse.server;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

public class HadoopPathPage implements IEditorPart {

  public IEditorInput getEditorInput() {
    // TODO Auto-generated method stub
    return null;
  }

  public IEditorSite getEditorSite() {
    // TODO Auto-generated method stub
    return null;
  }

  public void init(IEditorSite site, IEditorInput input)
      throws PartInitException {
    // TODO Auto-generated method stub

  }

  public void addPropertyListener(IPropertyListener listener) {
    // TODO Auto-generated method stub

  }

  public void createPartControl(Composite parent) {
    // TODO Auto-generated method stub

  }

  public void dispose() {
    // TODO Auto-generated method stub

  }

  public IWorkbenchPartSite getSite() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getTitle() {
    // TODO Auto-generated method stub
    return null;
  }

  public Image getTitleImage() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getTitleToolTip() {
    // TODO Auto-generated method stub
    return null;
  }

  public void removePropertyListener(IPropertyListener listener) {
    // TODO Auto-generated method stub

  }

  public void setFocus() {
    // TODO Auto-generated method stub

  }

  public Object getAdapter(Class adapter) {
    // TODO Auto-generated method stub
    return null;
  }

  public void doSave(IProgressMonitor monitor) {
    // TODO Auto-generated method stub

  }

  public void doSaveAs() {
    // TODO Auto-generated method stub

  }

  public boolean isDirty() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isSaveAsAllowed() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isSaveOnCloseNeeded() {
    // TODO Auto-generated method stub
    return false;
  }

}
