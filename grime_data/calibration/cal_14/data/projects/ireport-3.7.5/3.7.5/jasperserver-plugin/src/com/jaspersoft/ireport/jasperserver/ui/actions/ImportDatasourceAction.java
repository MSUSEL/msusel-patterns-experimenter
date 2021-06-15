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

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class ImportDatasourceAction extends NodeAction {

    /**
     * When a Jrxml file is opened, the action listen for the loading of the jrxml.
     * When a JasperDesign appears in the lookup, it is analyzed to cache his images.
     * On the first resultChange the listener is removed.
     */
    private static List jrxmlListeners = new ArrayList();
    
    public String getName() {
        return NbBundle.getMessage(ImportDatasourceAction.class, "CTL_ImportDatasourceAction");
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
            final Node node = activatedNodes[i];
            ResourceDescriptor rd = ((ResourceNode)node).getResourceDescriptor();
            int ds_count = 0;
            if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC))
            {
                    boolean skipDs = false;
                    List<IReportConnection> conns = IReportManager.getInstance().getConnections();

                     int index = -1;
                     for (IReportConnection con : conns)
                     {
                         if (con.getName().equals( rd.getLabel() ))
                         {
                             if (JOptionPane.showConfirmDialog(Misc.getMainFrame(),
                                     JasperServerManager.getFormattedString("repositoryExplorer.message.duplicatedConnectionName",
                                     "A connection named {0} is already present.\nWould you like to replace the existing connection?",
                                     new Object[]{rd.getLabel()})) != JOptionPane.OK_OPTION)
                             {
                                 skipDs=true;
                                 break;
                             }
                             else
                             {
                                 IReportManager.getInstance().removeConnection(con);
                                 break;
                             }
                         }
                     }

                     if (skipDs) continue;

                     JDBCConnection jdbcConnection = new JDBCConnection();
                     jdbcConnection.setName(rd.getLabel() );
                     jdbcConnection.setUrl( rd.getConnectionUrl() );
                     jdbcConnection.setJDBCDriver( rd.getDriverClass() );
                     jdbcConnection.setUsername( rd.getUsername() );
                     jdbcConnection.setPassword( rd.getPassword() );

                     IReportManager.getInstance().addConnection(jdbcConnection);

                     IReportManager.getInstance().setDefaultConnection(jdbcConnection);
                     ds_count++;
            }

            if (ds_count > 0)
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), JasperServerManager.getFormattedString("repositoryExplorer.message.connectionImported",
                                     "{0} connection(s) succefully imported in iReport.", new Object[]{ds_count}),"",JOptionPane.INFORMATION_MESSAGE);
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
            if (node.getResourceDescriptor() == null ||
                !node.getResourceDescriptor().getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC))
            {
                return false;
            }
        }
        
        return true;
    }

    
}