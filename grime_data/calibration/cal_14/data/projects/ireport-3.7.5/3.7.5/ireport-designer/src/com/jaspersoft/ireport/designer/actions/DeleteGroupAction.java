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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.GroupNode;
import com.jaspersoft.ireport.designer.undo.DeleteGroupUndoableEdit;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class DeleteGroupAction extends NodeAction {

    private static DeleteGroupAction instance = null;
    
    public static synchronized DeleteGroupAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteGroupAction();
        }
        
        return instance;
    }
    
    private DeleteGroupAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("DeleteGroupAction.Name.CTL_DeleteGroupAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        GroupNode groupNode = (GroupNode)activatedNodes[0];
        // Remove the group...
        JRDesignGroup grp = groupNode.getGroup();

        JRDesignDataset dataset = groupNode.getDataset();
        int index = dataset.getGroupsList().indexOf(grp);
        dataset.removeGroup(grp);

        // We should add an undo here...
        IReportManager.getInstance().notifyReportChange();
        
        DeleteGroupUndoableEdit edit = new DeleteGroupUndoableEdit(grp, dataset, index);
        IReportManager.getInstance().addUndoableEdit(edit);
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if ( activatedNodes[0] instanceof GroupNode &&
            ( ((GroupNode)activatedNodes[0]).getGroup() != null))
        {
            return true;
        }
        return false;
    }
}