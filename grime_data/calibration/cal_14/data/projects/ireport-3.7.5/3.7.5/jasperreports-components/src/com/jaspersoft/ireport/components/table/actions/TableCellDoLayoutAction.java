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

import com.jaspersoft.ireport.components.table.DefaultTableCellElementsLayout;
import com.jaspersoft.ireport.components.table.TableObjectScene;
import com.jaspersoft.ireport.components.table.nodes.TableCellNode;
import com.jaspersoft.ireport.designer.GenericDesignerPanel;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public abstract class TableCellDoLayoutAction extends NodeAction {


    public abstract int getLayoutType();
    
    public abstract String getName();


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
            TableCellNode cellNode =  null;
            if (activatedNodes[i] instanceof TableCellNode)
            {
                cellNode = (TableCellNode)activatedNodes[i];
            }
            else
            {
                Node parent = activatedNodes[i].getParentNode();
                while (parent != null)
                {
                    if (parent instanceof TableCellNode)
                    {
                        cellNode = (TableCellNode)parent;
                        break;
                    }
                    parent = parent.getParentNode();
                }
            }

            if (cellNode == null) continue;

            JasperDesign jd = cellNode.getLookup().lookup(JasperDesign.class);

            JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
            if (view != null)
            {
                GenericDesignerPanel panel = view.getReportDesignerPanel().getElementPanel( cellNode.getComponentElement()  );
                if (panel != null)
                {
                    DefaultTableCellElementsLayout.doLayout(cellNode.getCell(), (TableObjectScene)panel.getScene(), getLayoutType());

                }
            }
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            boolean isOk = false;
            if (activatedNodes[i] instanceof TableCellNode) isOk = true;

            if (!isOk)
            {
                Node parent = activatedNodes[0].getParentNode();
                while (parent != null)
                {
                    if (parent instanceof TableCellNode)
                    {
                        isOk = true;
                        break;
                    }
                    parent = parent.getParentNode();
                }
            }
            if (!isOk) return false;
        }
        return true;
    }
}