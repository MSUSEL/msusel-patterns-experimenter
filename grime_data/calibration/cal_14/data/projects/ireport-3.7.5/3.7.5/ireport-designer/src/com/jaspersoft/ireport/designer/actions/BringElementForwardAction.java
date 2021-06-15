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
import com.jaspersoft.ireport.designer.outline.nodes.ElementGroupNode;
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import java.util.Arrays;
import java.util.List;
import javax.swing.JMenuItem;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class BringElementForwardAction extends NodeAction {

    public String getName() {
        return I18n.getString("BringElementForwardAction.Name");
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/formatting/bringforward.png";
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.FALSE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {

        // 1. order the selected elements by index.
        Node parent = activatedNodes[0].getParentNode();

        Children childrens = parent.getChildren();
        int[] perms = new int[childrens.getNodesCount()];
        for (int i=0; i<perms.length; ++i)
        {
            perms[i] = i;
        }

        int[] indexes = new int[activatedNodes.length];

        List allNodes = Arrays.asList(childrens.getNodes());
        for (int i=0; i<activatedNodes.length; ++i)
        {
            indexes[i] = allNodes.indexOf(activatedNodes[i]);
        }

        Arrays.sort(indexes);

        for (int i=indexes.length-1; i>=0; --i)
        {
            swap(perms, indexes[i]+1, indexes[i]);
        }

        ((Index.KeysChildren)parent.getChildren()).getIndex().reorder(perms);
    }


    public void swap(int[] perms, int a, int b)
    {
        int indexOfA = -1;
        int indexOfB = -1;

        // find the original node ad position a...
        for (int i=0; i<perms.length; ++i)
        {
            if (perms[i] == a)
            {
                indexOfA = i;
            }
            if (perms[i] == b)
            {
                indexOfB = b;
            }
            if (indexOfA != -1 && indexOfB != -1)
            {
                perms[indexOfA] = b;
                perms[indexOfB] = a;
                return;
            }
        }
        // unable to find one of the indexes...
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        
        // Check if all the nodes are under the same parent...
        Node parent =  activatedNodes[0].getParentNode();
        
        if (parent == null) return false;
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i].getParentNode() != parent) 
            {
                return false;
            }

            if (activatedNodes[i].getParentNode() != null &&
                activatedNodes[i].getParentNode() instanceof ElementNode)
            {
                    JRDesignElement multiaxischart = ((ElementNode)activatedNodes[i].getParentNode()).getElement();
                    if (multiaxischart instanceof JRDesignChart &&
                        ((JRDesignChart)multiaxischart).getChartType() == JRDesignChart.CHART_TYPE_MULTI_AXIS)
                    {
                        return false;
                    }
            }

            if (activatedNodes[i] instanceof ElementNode)
            {
                JRDesignElement element = ((ElementNode)activatedNodes[i]).getElement();
                JRElementGroup group = element.getElementGroup();
                if (group.getChildren().indexOf(element) == group.getChildren().size()-1) return false;
            }
            else if (activatedNodes[i] instanceof ElementGroupNode)
            {
                JRElementGroup element = ((ElementGroupNode)activatedNodes[i]).getElementGroup();
                JRElementGroup group = element.getElementGroup();
                if (group.getChildren().indexOf(element) == group.getChildren().size()-1) return false;
            }
        }
        
        return true;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenuItem item = super.getPopupPresenter();
        item.setIcon(getIcon());
        return item;
    }
}