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

import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOp;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportPopupMenuProvider implements PopupMenuProvider {

    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        
        Node[] nodes = null;
        
        try {
            nodes = OutlineTopComponent.getDefault().getExplorerManager().getSelectedNodes();
        } catch (Exception ex) { }
       
        if (nodes != null && nodes.length > 0)
        {
            //return NodeOp.findContextMenu(nodes);
            Action[] actions = NodeOp.findActions(nodes);
            if (actions != null && actions.length > 0)
            {
                ActionMap actionsMap = new ActionMap();
                for (int i=0; i<actions.length; ++i)
                {
                    if (actions[i] != null && actions[i].getValue( Action.ACTION_COMMAND_KEY) != null)
                        actionsMap.put( actions[i].getValue( Action.ACTION_COMMAND_KEY), actions[i]);
                }
                
                List<Lookup> allLookups = new ArrayList<Lookup>();
                for (int i=0; i<nodes.length; ++i)
                {
                    allLookups.add(nodes[i].getLookup());
                }
                allLookups.add(Utilities.actionsGlobalContext());
                allLookups.add(Lookups.singleton(actionsMap));
                Lookup lookup = new ProxyLookup(allLookups.toArray(new Lookup[allLookups.size()]));
                return Utilities.actionsToPopup(actions, lookup);
            }
        
        }
        
        return null;
    }
}
