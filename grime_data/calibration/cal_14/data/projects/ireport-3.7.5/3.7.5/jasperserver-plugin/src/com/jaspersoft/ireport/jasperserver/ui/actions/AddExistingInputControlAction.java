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
import com.jaspersoft.ireport.jasperserver.ui.ResourceChooser;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ReportUnitInputControlsNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

public final class AddExistingInputControlAction extends NodeAction {

    public String getName() {
        return NbBundle.getMessage(AddExistingInputControlAction.class, "CTL_AddExistingInputControlAction");
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

        if (activatedNodes == null || activatedNodes.length != 1) return;

        if (activatedNodes[0] instanceof ReportUnitInputControlsNode)
        {
            ReportUnitInputControlsNode ruicn = (ReportUnitInputControlsNode)activatedNodes[0];
            RepositoryReportUnit ru = ruicn.getReportUnit();

            // In this case we choose an input control from the repository....
            ResourceChooser rc = new ResourceChooser();
            rc.setMultipleSelection(true);

            String reportUnitUri = null;
            reportUnitUri = ru.getDescriptor().getUriString();
            rc.setServer( ru.getServer() );

            if (rc.showDialog(Misc.getMainFrame(), null) == JOptionPane.OK_OPTION)
            {
                List<ResourceDescriptor> rds = rc.getSelectedDescriptors();
                if (rds.size() == 0)
                {
                    return;
                }

                for (ResourceDescriptor rd : rds)
                {
                    if (!rd.getWsType().equals( ResourceDescriptor.TYPE_INPUT_CONTROL))
                    {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),
                                JasperServerManager.getFormattedString("repositoryExplorer.message.invalidInputControl","{0} is not an Input Control!",new Object[]{rd.getName()}),
                                     "",JOptionPane.ERROR_MESSAGE);
                        continue;
                    }


                    ResourceDescriptor newRd = new ResourceDescriptor();
                    newRd.setWsType( ResourceDescriptor.TYPE_INPUT_CONTROL);
                    newRd.setIsReference(true);
                    newRd.setReferenceUri( rd.getUriString() );
                    newRd.setIsNew(true);
                    newRd.setUriString(reportUnitUri+"/<cotnrols>");
                    try {
                        newRd = ru.getServer().getWSClient().modifyReportUnitResource(reportUnitUri, newRd, null);

                        RepositoryFolder obj = RepositoryFolder.createRepositoryObject(ru.getServer(), newRd);
                        if (ruicn.getRepositoryObject().isLoaded())
                        {
                            ruicn.getResourceDescriptor().getChildren().add( newRd );
                            ruicn.getRepositoryObject().getChildren().add(obj);
                            ruicn.refreshChildrens(false);
                        }

                    } catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
                        ex.printStackTrace();
                    }
                }
            }

        }
        
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;


        if (!(activatedNodes[0] instanceof ReportUnitInputControlsNode))
        {
                return false;
        }
        
        return true;
    }

    
}