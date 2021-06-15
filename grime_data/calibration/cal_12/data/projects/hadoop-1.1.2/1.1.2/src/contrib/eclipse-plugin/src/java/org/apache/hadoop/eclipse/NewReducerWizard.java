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

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard for creating a new Reducer class (a class that runs the Reduce
 * portion of a MapReduce job). The class is pre-filled with a template.
 * 
 */

public class NewReducerWizard extends NewElementWizard implements
    INewWizard, IRunnableWithProgress {
  private Page page;

  public NewReducerWizard() {
    setWindowTitle("New Reducer");
  }

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

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    super.init(workbench, selection);

    page = new Page();
    addPage(page);
    page.setSelection(selection);
  }

  public static class Page extends NewTypeWizardPage {
    public Page() {
      super(true, "Reducer");

      setTitle("Reducer");
      setDescription("Create a new Reducer implementation.");
      setImageDescriptor(ImageLibrary.get("wizard.reducer.new"));
    }

    public void setSelection(IStructuredSelection selection) {
      initContainerPage(getInitialJavaElement(selection));
      initTypePage(getInitialJavaElement(selection));
    }

    @Override
    public void createType(IProgressMonitor monitor) throws CoreException,
        InterruptedException {
      super.createType(monitor);
    }

    @Override
    protected void createTypeMembers(IType newType, ImportsManager imports,
        IProgressMonitor monitor) throws CoreException {
      super.createTypeMembers(newType, imports, monitor);
      imports.addImport("java.io.IOException");
      imports.addImport("org.apache.hadoop.io.WritableComparable");
      imports.addImport("org.apache.hadoop.mapred.OutputCollector");
      imports.addImport("org.apache.hadoop.mapred.Reporter");
      imports.addImport("java.util.Iterator");
      newType
          .createMethod(
              "public void reduce(WritableComparable _key, Iterator values, OutputCollector output, Reporter reporter) throws IOException \n{\n"
                  + "\t// replace KeyType with the real type of your key\n"
                  + "\tKeyType key = (KeyType) _key;\n\n"
                  + "\twhile (values.hasNext()) {\n"
                  + "\t\t// replace ValueType with the real type of your value\n"
                  + "\t\tValueType value = (ValueType) values.next();\n\n"
                  + "\t\t// process value\n" + "\t}\n" + "}\n", null, false,
              monitor);
    }

    public void createControl(Composite parent) {
      // super.createControl(parent);

      initializeDialogUnits(parent);
      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 4;
      composite.setLayout(layout);

      createContainerControls(composite, 4);
      createPackageControls(composite, 4);
      createSeparator(composite, 4);
      createTypeNameControls(composite, 4);
      createSuperClassControls(composite, 4);
      createSuperInterfacesControls(composite, 4);
      // createSeparator(composite, 4);

      setControl(composite);

      setSuperClass("org.apache.hadoop.mapred.MapReduceBase", true);
      setSuperInterfaces(Arrays
          .asList(new String[] { "org.apache.hadoop.mapred.Reducer" }), true);

      setFocus();
      validate();
    }

    @Override
    protected void handleFieldChanged(String fieldName) {
      super.handleFieldChanged(fieldName);

      validate();
    }

    private void validate() {
      updateStatus(new IStatus[] { fContainerStatus, fPackageStatus,
          fTypeNameStatus, fSuperClassStatus, fSuperInterfacesStatus });
    }
  }

  @Override
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
  protected void finishPage(IProgressMonitor monitor)
      throws InterruptedException, CoreException {
    this.run(monitor);
  }

  @Override
  public IJavaElement getCreatedElement() {
    return (page.getCreatedType() == null) ? null : page.getCreatedType()
        .getPrimaryElement();
  }
}
