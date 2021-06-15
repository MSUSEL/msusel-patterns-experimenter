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
package com.jaspersoft.ireport.components.table.actions;

import com.jaspersoft.ireport.components.table.TableCell;
import com.jaspersoft.ireport.components.table.TableMatrix;
import com.jaspersoft.ireport.components.table.TableModelUtils;
import com.jaspersoft.ireport.components.table.nodes.TableNullCellNode;
import com.jaspersoft.ireport.components.table.undo.AddTableCellUndoableEdit;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class AddTableCellAction extends NodeAction {

    private static AddTableCellAction instance = null;
    
    public static synchronized AddTableCellAction getInstance()
    {
        if (instance == null)
        {
            instance = new AddTableCellAction();
        }
        
        return instance;
    }
    
    private AddTableCellAction()
    {
        super();
    }
    
    
    public String getName() {
        return NbBundle.getMessage(AddTableCellAction.class, "AddTableCellAction.Name.CTL_AddTableCellAction");
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
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof TableNullCellNode)
            {
                TableNullCellNode cellNode = (TableNullCellNode)activatedNodes[i];
                JasperDesign jd = cellNode.getLookup().lookup(JasperDesign.class);
                
                DesignCell cell = new DesignCell();

                TableMatrix tm = new TableMatrix(jd, cellNode.getTable());
 
                TableCell tc = tm.findTableCell(cellNode.getColumn(), cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                int h = tm.getCellBounds(tc).height;
                if (h == 0) h = 30;

                cell.setHeight(h);
                // TODO -> Add the default style...
                TableModelUtils.addCell(cellNode.getColumn(), cell, cellNode.getSection(), (cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null);

                System.out.println(" Added cell for group: " + cellNode.getSection() + "  " + ((cellNode.getGroup() != null) ? cellNode.getGroup().getName() : null));
                System.out.flush();

                AddTableCellUndoableEdit edit = new AddTableCellUndoableEdit(cellNode.getTable(), jd,cell, cellNode.getColumn(), cellNode.getSection(),cellNode.getGroup());
                IReportManager.getInstance().addUndoableEdit(edit);

                TableModelUtils.fixTableLayout(cellNode.getTable(), jd);
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof TableNullCellNode)) return false;
        }
        return true;
    }
}