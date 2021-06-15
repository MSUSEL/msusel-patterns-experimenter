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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * 
 * Handler for Local MapReduce job launches
 * 
 * TODO(jz) this may not be needed as we almost always deploy to a remote server
 * and not locally, where we do do it locally we may just be able to exec
 * scripts without going to java
 * 
 */
public class LocalMapReduceLaunchTabGroup extends
    AbstractLaunchConfigurationTabGroup {

  public LocalMapReduceLaunchTabGroup() {
    // TODO Auto-generated constructor stub
  }

  public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
    setTabs(new ILaunchConfigurationTab[] { new MapReduceLaunchTab(),
        new JavaArgumentsTab(), new JavaJRETab(), new JavaClasspathTab(),
        new CommonTab() });
  }

  public static class MapReduceLaunchTab extends AbstractLaunchConfigurationTab {
    private Text combinerClass;

    private Text reducerClass;

    private Text mapperClass;

    @Override
    public boolean canSave() {
      return true;
    }

    @Override
    public boolean isValid(ILaunchConfiguration launchConfig) {
      // todo: only if all classes are of proper types
      return true;
    }

    public void createControl(final Composite parent) {
      Composite panel = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout(3, false);
      panel.setLayout(layout);

      Label mapperLabel = new Label(panel, SWT.NONE);
      mapperLabel.setText("Mapper");
      mapperClass = new Text(panel, SWT.SINGLE | SWT.BORDER);
      createRow(parent, panel, mapperClass);

      Label reducerLabel = new Label(panel, SWT.NONE);
      reducerLabel.setText("Reducer");
      reducerClass = new Text(panel, SWT.SINGLE | SWT.BORDER);
      createRow(parent, panel, reducerClass);

      Label combinerLabel = new Label(panel, SWT.NONE);
      combinerLabel.setText("Combiner");
      combinerClass = new Text(panel, SWT.SINGLE | SWT.BORDER);
      createRow(parent, panel, combinerClass);

      panel.pack();
      setControl(panel);
    }

    private void createRow(final Composite parent, Composite panel,
        final Text text) {
      text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      Button button = new Button(panel, SWT.BORDER);
      button.setText("Browse...");
      button.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event arg0) {
          try {
            AST ast = AST.newAST(3);

            SelectionDialog dialog = JavaUI.createTypeDialog(parent.getShell(),
                new ProgressMonitorDialog(parent.getShell()), SearchEngine
                    .createWorkspaceScope(),
                IJavaElementSearchConstants.CONSIDER_CLASSES, false);
            dialog.setMessage("Select Mapper type (implementing )");
            dialog.setBlockOnOpen(true);
            dialog.setTitle("Select Mapper Type");
            dialog.open();

            if ((dialog.getReturnCode() == Window.OK)
                && (dialog.getResult().length > 0)) {
              IType type = (IType) dialog.getResult()[0];
              text.setText(type.getFullyQualifiedName());
              setDirty(true);
            }
          } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      });
    }

    public String getName() {
      return "Hadoop";
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
      try {
        mapperClass.setText(configuration.getAttribute(
            "org.apache.hadoop.eclipse.launch.mapper", ""));
        reducerClass.setText(configuration.getAttribute(
            "org.apache.hadoop.eclipse.launch.reducer", ""));
        combinerClass.setText(configuration.getAttribute(
            "org.apache.hadoop.eclipse.launch.combiner", ""));
      } catch (CoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        setErrorMessage(e.getMessage());
      }
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
      configuration.setAttribute("org.apache.hadoop.eclipse.launch.mapper",
          mapperClass.getText());
      configuration.setAttribute(
          "org.apache.hadoop.eclipse.launch.reducer", reducerClass
              .getText());
      configuration.setAttribute(
          "org.apache.hadoop.eclipse.launch.combiner", combinerClass
              .getText());
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {

    }
  }
}
