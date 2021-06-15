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
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class DeleteAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(DeleteAction.class, "CTL_DeleteAction");
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

    public void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        List<String> destroiedUris = new ArrayList<String>();
        
        // Save the parents note before they are unassociated by the childrens...
        Node[] parents = new Node[activatedNodes.length];
        for (int i=0; i<activatedNodes.length; ++i)
        {
            parents[i] = activatedNodes[i].getParentNode();
        }



        for (int i=0; i<activatedNodes.length; ++i)
        {
            ResourceNode node = (ResourceNode)activatedNodes[i];
            RepositoryFolder rf = node.getRepositoryObject();
         
            // Check if we already destroied the uri or an his parent...
            for (String destroiedUri : destroiedUris)
            {
                if (rf.getDescriptor().getUriString().startsWith(destroiedUri))
                {
                    continue;
                }
            }
            
            String reportUnitUri = null; // this is set only if we are inside a RU...
            
            // Check if we are inside a ReportUnit...
            // This is done just checking the parent, looking if his descriptor is a RU...
            if (activatedNodes[i].getParentNode() != null &&
                activatedNodes[i].getParentNode() instanceof ResourceNode)
            {
                ResourceNode parentNode = (ResourceNode)activatedNodes[i].getParentNode();
                if (parentNode.getRepositoryObject() instanceof RepositoryReportUnit)
                {
                    reportUnitUri = parentNode.getResourceDescriptor().getUriString();
                }
            }
            
            // Set the confirmation message to a simple default....
            String msg = JasperServerManager.getFormattedString("repositoryExplorer.message.confirmDeleteResource", "Are you sure you want to remove the resource {0} ?", new Object[] {rf.getDescriptor().getLabel()});
            
            if (node.getResourceDescriptor().getWsType().equals( ResourceDescriptor.TYPE_FOLDER))
            {
                 msg = JasperServerManager.getFormattedString("repositoryExplorer.message.confirmDeleteFolder", "Are you sure you want to remove the folder {0}\n and all its contents?", new Object[] {rf.getDescriptor().getLabel()});
            }
            else if (node.getResourceDescriptor().getWsType().equals( ResourceDescriptor.TYPE_REPORTUNIT))
            {
                 msg = JasperServerManager.getFormattedString("repositoryExplorer.message.confirmDeleteReportUnit", "Are you sure you want to remove the report unit {0}\n and all its contents?", new Object[] {rf.getDescriptor().getLabel()});
            }
            else
            {
                // just use the default generic message....
                
                // Check if we are trying to delete special resources....
                if (reportUnitUri != null) // we are inside a RU...
                {
                    if ( RepositoryFolder.isDataSource(  rf.getDescriptor() ) )
                    {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),
                                    JasperServerManager.getString("repositoryExplorer.message.cannotDeleteRUDatasource",                            
                                    "You can not delete the report datasource.\nUse Report Unit properties menu item to modify the datasource type."),
                                    "",JOptionPane.ERROR_MESSAGE);
                            return;
                    }
                    else if ( rf.getDescriptor().isMainReport())
                    {
                        // This would be not valid but it is cought a server side...
                    }
                }
            }
            
            if (JOptionPane.showConfirmDialog(Misc.getMainFrame(),msg) == JOptionPane.YES_OPTION)
            {
                 try {
                        // Update the folder childrens...
                        ResourceNode parentResourceNode = (ResourceNode)parents[i];
                        rf.getServer().getWSClient().delete(rf.getDescriptor(), reportUnitUri);

                        if (parentResourceNode != null)
                        {
                            RepositoryFolder parentFolder = parentResourceNode.getRepositoryObject();
                            parentFolder.getDescriptor().getChildren().remove( rf.getDescriptor());
                            // update the RepositoryFolder too...
                            List children = parentFolder.getChildren();
                            for (int k=0; k<children.size(); ++k)
                            {
                                RepositoryFolder child = (RepositoryFolder)children.get(k);
                                if (child.getDescriptor().equals(rf.getDescriptor()))
                                {
                                    children.remove(child);
                                    break;
                                }
                            }
                            parentResourceNode.refreshChildrens(false);
                        }
                 
                } catch (Exception ex)
                {
                    String errorMessage = ex.getMessage();
                    if (errorMessage == null) errorMessage = "No reason reported.";
                    if (errorMessage.length() > 120)
                    {
                        String emsg = "";
                        while (errorMessage.length() > 120)
                        {
                            if (emsg.length() > 0)
                            {
                                emsg += "\n";
                            }
                            emsg += errorMessage.substring(0, 120);
                            errorMessage = errorMessage.substring(120);
                            if (errorMessage.length() <= 120)
                            {
                                emsg += "\n" + errorMessage;
                                break;
                            }
                        }

                        System.out.println("Message: " + emsg);
                        errorMessage = emsg;
                    }
                    JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {errorMessage}));
                    ex.printStackTrace();
                }
            }
            
        }
        
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length < 1) return false;
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof ResourceNode))
            {
                return false;
            }
            
            ResourceNode node = (ResourceNode)activatedNodes[i];
            if (node.getRepositoryObject().isRoot()) return false;
        }
        
        return true;
    }
}