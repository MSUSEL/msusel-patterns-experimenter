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
package org.apache.hadoop.eclipse.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.eclipse.servers.RunOnHadoopWizard;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaApplicationLaunchShortcut;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Add a shortcut "Run on Hadoop" to the Run menu
 */

public class HadoopApplicationLaunchShortcut extends
    JavaApplicationLaunchShortcut {

  static Logger log =
      Logger.getLogger(HadoopApplicationLaunchShortcut.class.getName());

  // private ActionDelegate delegate = new RunOnHadoopActionDelegate();

  public HadoopApplicationLaunchShortcut() {
  }

  /* @inheritDoc */
  @Override
  protected ILaunchConfiguration findLaunchConfiguration(IType type,
      ILaunchConfigurationType configType) {

    // Find an existing or create a launch configuration (Standard way)
    ILaunchConfiguration iConf =
        super.findLaunchConfiguration(type, configType);
    if (iConf == null) iConf = super.createConfiguration(type);
    ILaunchConfigurationWorkingCopy iConfWC;
    try {
      /*
       * Tune the default launch configuration: setup run-time classpath
       * manually
       */
      iConfWC = iConf.getWorkingCopy();

      iConfWC.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);

      List<String> classPath = new ArrayList<String>();
      IResource resource = type.getResource();
      IJavaProject project =
          (IJavaProject) resource.getProject().getNature(JavaCore.NATURE_ID);
      IRuntimeClasspathEntry cpEntry =
          JavaRuntime.newDefaultProjectClasspathEntry(project);
      classPath.add(0, cpEntry.getMemento());

      iConfWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classPath);

    } catch (CoreException e) {
      e.printStackTrace();
      // FIXME Error dialog
      return null;
    }

    /*
     * Update the selected configuration with a specific Hadoop location
     * target
     */
    IResource resource = type.getResource();
    if (!(resource instanceof IFile))
      return null;
    RunOnHadoopWizard wizard =
        new RunOnHadoopWizard((IFile) resource, iConfWC);
    WizardDialog dialog =
        new WizardDialog(Display.getDefault().getActiveShell(), wizard);

    dialog.create();
    dialog.setBlockOnOpen(true);
    if (dialog.open() != WizardDialog.OK)
      return null;

    try {
      iConfWC.doSave();

    } catch (CoreException e) {
      e.printStackTrace();
      // FIXME Error dialog
      return null;
    }

    return iConfWC;
  }

  /**
   * Was used to run the RunOnHadoopWizard inside and provide it a
   * ProgressMonitor
   */
  static class Dialog extends WizardDialog {
    public Dialog(Shell parentShell, IWizard newWizard) {
      super(parentShell, newWizard);
    }

    @Override
    public void create() {
      super.create();

      ((RunOnHadoopWizard) getWizard())
          .setProgressMonitor(getProgressMonitor());
    }
  }
}
