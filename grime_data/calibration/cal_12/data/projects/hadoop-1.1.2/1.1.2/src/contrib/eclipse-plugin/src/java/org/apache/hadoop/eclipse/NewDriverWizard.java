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
package org.apache.hadoop.eclipse;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard for creating a new Driver class (a class that runs a MapReduce job).
 * 
 */

public class NewDriverWizard extends NewElementWizard implements INewWizard,
    IRunnableWithProgress {
  private NewDriverWizardPage page;

  /*
   * @Override public boolean performFinish() { }
   */
  public void run(IProgressMonitor monitor) {
    try {
      page.createType(monitor);
    } catch (CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public NewDriverWizard() {
    setWindowTitle("New MapReduce Driver");
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    super.init(workbench, selection);

    page = new NewDriverWizardPage();
    addPage(page);
    page.setSelection(selection);
  }

  @Override
  /**
   * Performs any actions appropriate in response to the user having pressed the
   * Finish button, or refuse if finishing now is not permitted.
   */
  public boolean performFinish() {
    if (super.performFinish()) {
      if (getCreatedElement() != null) {
        selectAndReveal(page.getModifiedResource());
        openResource((IFile) page.getModifiedResource());
      }

      return true;
    } else {
      return false;
    }
  }

  @Override
  /**
   * 
   */
  protected void finishPage(IProgressMonitor monitor)
      throws InterruptedException, CoreException {
    this.run(monitor);
  }

  @Override
  public IJavaElement getCreatedElement() {
    return page.getCreatedType().getPrimaryElement();
  }
}
