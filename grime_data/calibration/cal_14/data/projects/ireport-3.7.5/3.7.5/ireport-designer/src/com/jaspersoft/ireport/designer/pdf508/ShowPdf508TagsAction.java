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
package com.jaspersoft.ireport.designer.pdf508;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;

public final class ShowPdf508TagsAction extends CallableSystemAction
        implements Presenter.Menu, LookupListener {

    private static JCheckBoxMenuItem SHOW_TAGS_MENU;
    
    public void performAction() {

        IReportManager.getPreferences().putBoolean("showPDF508Tags", SHOW_TAGS_MENU.isSelected());
        if (IReportManager.getInstance().getActiveReport() != null)
        {
            IReportManager.getInstance().getActiveVisualView().getReportDesignerPanel().repaint();
        }
    }
    
    private ShowPdf508TagsAction() {

        SHOW_TAGS_MENU  = new JCheckBoxMenuItem(getName());
        SHOW_TAGS_MENU.addActionListener(this);
        setMenu();
    }
    
    public void resultChanged(LookupEvent e) {
        setMenu();
    }
    
    protected void setMenu(){
       SHOW_TAGS_MENU.setSelected(IReportManager.getPreferences().getBoolean("showPDF508Tags",false));
    }
    
    public String getName() {
        return I18n.getString("pdf508.showTags");
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
    
    public JMenuItem getMenuPresenter()
    {
        return SHOW_TAGS_MENU;
    }
       
}