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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.ServerDialog;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class ModifyServerAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(ModifyServerAction.class, "CTL_ModifyServerAction");
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
        
        if (!(activatedNodes[0] instanceof ResourceNode)) return;
        
        ResourceNode node = (ResourceNode)activatedNodes[0];
        RepositoryFolder rf = ((ResourceNode)activatedNodes[0]).getRepositoryObject();
        
        JServer server = rf.getServer();
        ServerDialog sd = new ServerDialog(Misc.getMainFrame(), true);
        sd.setJServer( server );
        sd.setVisible(true);
        if (sd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            server.setName( sd.getJServer().getName());
            server.setUsername( sd.getJServer().getUsername() );
            server.setPassword( sd.getJServer().getPassword() );
            server.setUrl( sd.getJServer().getUrl() );

            // We should update the underline folder used to represent the root...
            rf.getDescriptor().setLabel(server.getName());
            JasperServerManager.getMainInstance().saveConfiguration();
            // Find the root of this server...
            
            Node theNode = activatedNodes[0];
            while (theNode.getParentNode() != null)
            {
                if (node instanceof ResourceNode)
                {
                    node = (ResourceNode)theNode;
                    if (node.getRepositoryObject().isRoot())
                    {
                        node.updateDisplayName();
                        break;
                    }
                }
            
                theNode = theNode.getParentNode();
            }
        }
     
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if ( activatedNodes[0] instanceof ResourceNode)
        {
            return true;
        }
        return false;
    }
}