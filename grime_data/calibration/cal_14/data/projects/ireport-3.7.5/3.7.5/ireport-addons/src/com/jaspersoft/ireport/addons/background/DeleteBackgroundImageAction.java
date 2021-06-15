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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.background;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: ShowBackgroundImageAction.java 0 2010-01-12 16:48:47 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DeleteBackgroundImageAction extends NodeAction {


    protected void performAction(Node[] activatedNodes) {

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();

                        ReportObjectScene scene = view.getReportDesignerPanel().getScene();

                        // Find the backgound image layer...
                        BackgroundImageLayer layer = BackgroundImageUtilities.getBackgroundImageLayer(view, false);

                        JasperDesign jd = view.getModel().getJasperDesign();
                        jd.removeProperty("ireport.background.image");
                        jd.removeProperty("ireport.background.image.properties");

                        if (layer != null)
                        {
                            layer.removeChildren();
                            scene.validate();
                            scene.revalidate(true);
                        }
                        ((ShowBackgroundImageAction)SystemAction.get(ShowBackgroundImageAction.class)).resultChanged(null);
                        firePropertyChange(NodeAction.PROP_ENABLED,true ,false);
                        IReportManager.getInstance().notifyReportChange();

                        BackgroundImageUtilities.notifyBackgroundOptionsChange();
                        setEnabled(false);
                    }
                });
    }

    public String getName() {
        return NbBundle.getMessage(BackgroundImageAction.class, "CTL_DeleteBackgroundImageAction");
    }


    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] nodes) {

        if (IReportManager.getInstance().getActiveReport() != null &&
                IReportManager.getInstance().getActiveVisualView() != null)
        {

            if (IReportManager.getInstance().getActiveReport().getProperty("ireport.background.image") != null)
            {
                return true;
            }
        }
        return false;
    }


}

