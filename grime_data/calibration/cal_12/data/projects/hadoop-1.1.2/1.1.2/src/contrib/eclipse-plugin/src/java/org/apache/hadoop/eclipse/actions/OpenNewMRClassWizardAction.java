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
package org.apache.hadoop.eclipse.actions;

import java.util.logging.Logger;

import org.apache.hadoop.eclipse.NewDriverWizard;
import org.apache.hadoop.eclipse.NewMapperWizard;
import org.apache.hadoop.eclipse.NewReducerWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;


/**
 * Action to open a new MapReduce Class.
 */

public class OpenNewMRClassWizardAction extends Action implements
    ICheatSheetAction {

  static Logger log = Logger.getLogger(OpenNewMRClassWizardAction.class
      .getName());

  public void run(String[] params, ICheatSheetManager manager) {

    if ((params != null) && (params.length > 0)) {
      IWorkbench workbench = PlatformUI.getWorkbench();
      INewWizard wizard = getWizard(params[0]);
      wizard.init(workbench, new StructuredSelection());
      WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow().getShell(), wizard);
      dialog.create();
      dialog.open();

      // did the wizard succeed ?
      notifyResult(dialog.getReturnCode() == Window.OK);
    }
  }

  private INewWizard getWizard(String typeName) {
    if (typeName.equals("Mapper")) {
      return new NewMapperWizard();
    } else if (typeName.equals("Reducer")) {
      return new NewReducerWizard();
    } else if (typeName.equals("Driver")) {
      return new NewDriverWizard();
    } else {
      log.severe("Invalid Wizard requested");
      return null;
    }
  }

}
