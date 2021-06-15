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
import com.jaspersoft.ireport.designer.ReportDesignerPanel;
import com.jaspersoft.ireport.designer.ReportObjectScene;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;

/**
 *
 * @version $Id: ShowBackgroundImageAction.java 0 2010-01-12 16:48:47 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ShowBackgroundImageAction extends CallableSystemAction
        implements Presenter.Menu, Presenter.Popup, LookupListener {


    private final Lookup lkp;
    private final Lookup.Result <? extends ReportDesignerPanel> result;
    JCheckBoxMenuItem menu = null;

    public ShowBackgroundImageAction()
    {
        this.lkp = Utilities.actionsGlobalContext();
        result = lkp.lookupResult(ReportDesignerPanel.class);
        result.addLookupListener(this);
        result.allItems();
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return getMenu();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return getMenu();
    }

    @Override
    public void performAction() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                    JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
                    BackgroundImageWidget w = BackgroundImageUtilities.getBackgroundImageWidget(view);

                    if (w != null)
                    {
                        w.setImageVisible( getMenu().isSelected());
                    }

                    BackgroundImageUtilities.notifyBackgroundOptionsChange();
                }
            });

    }

    protected boolean enable() {

        // Update status...
        getMenu().setSelected(false);
        getMenu().setEnabled(false);

        if (IReportManager.getInstance().getActiveVisualView() != null)
        {
            BackgroundImageWidget w = BackgroundImageUtilities.getBackgroundImageWidget(IReportManager.getInstance().getActiveVisualView());
            getMenu().setEnabled(w != null);
            getMenu().setSelected(w != null && w.isVisible());
        }
        
        return getMenu().isEnabled();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ShowBackgroundImageAction.class, "action.showBackgroundImage");
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

    protected JCheckBoxMenuItem getMenu()
    {
        if (menu == null)
        {
            menu = new JCheckBoxMenuItem(getName());
            menu.addActionListener(this);
            menu.setSelected(true);
            menu.setEnabled(false);
        }

        return menu;
    }

    public void resultChanged(LookupEvent le) {
        enable();
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }


}
