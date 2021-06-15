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

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.JRViewerTopComponent;
import com.jaspersoft.ireport.jasperserver.ui.RepositoryTopComponent;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FileNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderChildren;
import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ReportUnitNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Exceptions;
import org.openide.util.actions.SystemAction;

/**
 * Given a repository folder node, the class opens the main jrxml.
 * Why do we need this class? When the report unit is published,
 * the repository view is not yet updated, but we may want to open
 * the main jrxml from the newly created report unit. This class
 * wait for the report unit to be loaded in the view, and then wait
 * for the FileResource node containing the jrxml be available.
 * When this happens, the jrxml is opened.
 *
 * @version $Id: NestedResourceOpener.java 0 2009-10-06 18:27:45 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class NestedResourceOpener implements NodeListener {

    FolderNode rootNode = null;
    ReportUnitNode reportUnitNode = null;
    boolean refreshedChildren = false;
    String ruUri = null;

    public void openFile()
    {

        rootNode.removeNodeListener(this);
        if (reportUnitNode == null)
        {
            

            Node[] nodes = rootNode.getChildren().getNodes();
            
            // Look for the report unit node...
            for (int i=0; i<nodes.length; ++i)
            {
                if (nodes[i] instanceof ReportUnitNode)
                {
                    ReportUnitNode ruNode = (ReportUnitNode)nodes[i];
                    if (ruNode.getResourceDescriptor().getUriString().equals(ruUri))
                    {
                        System.out.println("Report Unit found nthe folder nodes!");
                        System.out.flush();
                        reportUnitNode = ruNode;
                        break;
                    }
                }
            }
            
            if (reportUnitNode == null)
            {
                System.out.println("Report Unit not available yet...");
                System.out.flush();

                // listen for node changes...
                rootNode.addNodeListener(this);
                rootNode.refreshChildrens(true);
                return;
            }
        
        }

        if (reportUnitNode != null)
        {
            reportUnitNode.removeNodeListener(this);

            System.out.println("Report Unit available!");
            System.out.flush();

            // Look for report unit node in the root node...
            Node[] nodes = reportUnitNode.getChildren().getNodes();
            for (int i=0; nodes != null && i<nodes.length; ++i)
            {
                // Check if this is the report unit we are interested into...
                if (nodes[i] instanceof FileNode)
                {
                    final FileNode fileNode = (FileNode)nodes[i];
                    if (fileNode.getResourceDescriptor().isMainReport())
                    {
                        System.out.println("Opening filenode: " + fileNode);
                        System.out.flush();

                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        RepositoryTopComponent.findInstance().getExplorerManager().setSelectedNodes(new Node[]{fileNode});
                                    } catch (PropertyVetoException ex) {
                                        Exceptions.printStackTrace(ex);
                                    }
                                    SystemAction.get(OpenFileAction.class).performAction(new Node[]{fileNode});
                                }
                            });
                        
                        
                        return;
                    }
                }
            }

            reportUnitNode.addNodeListener(this);
            reportUnitNode.refreshChildrens(true);
        }

    }

    public NestedResourceOpener(FolderNode node, String ruUri)
    {
        this.rootNode = node;
        this.ruUri = ruUri;
    }

    public void childrenAdded(NodeMemberEvent evt) {
        if (reportUnitNode == null)
        {
            Node[] nodes = evt.getDelta();
            for (int i=0; i<nodes.length; ++i)
            {
                if (nodes[i] instanceof ReportUnitNode)
                {
                    ReportUnitNode ruNode = (ReportUnitNode)nodes[i];
                    if (ruNode.getResourceDescriptor().getUriString().equals(ruUri))
                    {
                        reportUnitNode = ruNode;
                        rootNode.removeNodeListener(this); // just in case...
                        break;
                    }
                }
            }
            openFile();
            return;
        }


        if (reportUnitNode != null)
        {
            Node[] nodes = evt.getDelta();
            for (int i=0; nodes != null && i<nodes.length; ++i)
            {
                // Check if this is the report unit we are interested into...
                if (nodes[i] instanceof FileNode)
                {
                    final FileNode fileNode = (FileNode)nodes[i];
                    if (fileNode.getResourceDescriptor().isMainReport())
                    {
                        System.out.println("Opening filenode: " + fileNode);
                        System.out.flush();

                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        RepositoryTopComponent.findInstance().getExplorerManager().setSelectedNodes(new Node[]{fileNode});
                                    } catch (PropertyVetoException ex) {
                                        Exceptions.printStackTrace(ex);
                                    }
                                    SystemAction.get(OpenFileAction.class).performAction(new Node[]{fileNode});
                                }
                            });


                        return;
                    }
                }
            }
        }
    }

    public void childrenRemoved(NodeMemberEvent arg0) {}

    public void childrenReordered(NodeReorderEvent arg0) {}

    public void nodeDestroyed(NodeEvent arg0) {}

    public void propertyChange(PropertyChangeEvent evt) {}

}
