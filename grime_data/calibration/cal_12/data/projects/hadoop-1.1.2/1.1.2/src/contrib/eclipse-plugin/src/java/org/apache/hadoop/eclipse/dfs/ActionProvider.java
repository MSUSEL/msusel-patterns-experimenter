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
package org.apache.hadoop.eclipse.dfs;

import org.apache.hadoop.eclipse.ImageLibrary;
import org.apache.hadoop.eclipse.actions.DFSActionImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;

/**
 * Allows the user to delete and refresh items in the DFS tree
 */

public class ActionProvider extends CommonActionProvider {

  private static ICommonActionExtensionSite site;

  public ActionProvider() {
  }

  /* @inheritDoc */
  @Override
  public void init(ICommonActionExtensionSite site) {
    if (ActionProvider.site != null) {
      System.err.printf("%s: Multiple init()\n", this.getClass()
          .getCanonicalName());
      return;
    }
    super.init(site);
    ActionProvider.site = site;
  }

  /* @inheritDoc */
  @Override
  public void fillActionBars(IActionBars actionBars) {
    actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
        new DFSAction(DFSActions.DELETE));
    actionBars.setGlobalActionHandler(ActionFactory.REFRESH.getId(),
        new DFSAction(DFSActions.REFRESH));

    if (site == null)
      return;

    if ((site.getStructuredViewer().getSelection() instanceof IStructuredSelection)
        && (((IStructuredSelection) site.getStructuredViewer()
            .getSelection()).size() == 1)
        && (((IStructuredSelection) site.getStructuredViewer()
            .getSelection()).getFirstElement() instanceof DFSFile)) {

      actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN,
          new DFSAction(DFSActions.OPEN));
    }

    actionBars.updateActionBars();
  }

  /* @inheritDoc */
  @Override
  public void fillContextMenu(IMenuManager menu) {
    /*
     * Actions on multiple selections
     */
    menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, new DFSAction(
        DFSActions.DELETE));

    menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, new DFSAction(
        DFSActions.REFRESH));

    menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, new DFSAction(
        DFSActions.DOWNLOAD));

    if (site == null)
      return;

    ISelection isel = site.getStructuredViewer().getSelection();
    if (!(isel instanceof IStructuredSelection))
      return;

    /*
     * Actions on single selections only
     */

    IStructuredSelection issel = (IStructuredSelection) isel;
    if (issel.size() != 1)
      return;
    Object element = issel.getFirstElement();

    if (element instanceof DFSFile) {
      menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, new DFSAction(
          DFSActions.OPEN));

    } else if (element instanceof DFSFolder) {
      menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, new DFSAction(
          DFSActions.MKDIR));
      menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, new DFSAction(
          DFSActions.UPLOAD_FILES));
      menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, new DFSAction(
          DFSActions.UPLOAD_DIR));

    } else if (element instanceof DFSLocation) {
      menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, new DFSAction(
          DFSActions.RECONNECT));

    } else if (element instanceof DFSLocationsRoot) {
      menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, new DFSAction(
          DFSActions.DISCONNECT));
    }

  }

  /**
   * Representation of an action on a DFS entry in the browser
   */
  public static class DFSAction extends Action {

    private final String id;

    private final String title;

    private DFSActions action;

    public DFSAction(String id, String title) {
      this.id = id;
      this.title = title;
    }

    public DFSAction(DFSActions action) {
      this.id = action.id;
      this.title = action.title;
    }

    /* @inheritDoc */
    @Override
    public String getText() {
      return this.title;
    }

    /* @inheritDoc */
    @Override
    public ImageDescriptor getImageDescriptor() {
      return ImageLibrary.get(getActionDefinitionId());
    }

    /* @inheritDoc */
    @Override
    public String getActionDefinitionId() {
      return id;
    }

    /* @inheritDoc */
    @Override
    public void run() {
      DFSActionImpl action = new DFSActionImpl();
      action.setActivePart(this, PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow().getActivePage().getActivePart());
      action.selectionChanged(this, site.getStructuredViewer()
          .getSelection());
      action.run(this);
    }

    /* @inheritDoc */
    @Override
    public boolean isEnabled() {
      return true;
    }
  }
}
