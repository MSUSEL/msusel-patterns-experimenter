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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.ReportDesignerPanel;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;

public final class SnapToGridAction extends CallableSystemAction
        implements Presenter.Menu, LookupListener {

    private static JCheckBoxMenuItem SNAP_TO_GRID_MENU;
    private final Lookup lkp;
    private final Lookup.Result <? extends ReportDesignerPanel> result;

    public void performAction() {
        
       // reportDesignerPanel
       Iterator<? extends ReportDesignerPanel> i = result.allInstances().iterator();
       if (i.hasNext())
       {
         ReportDesignerPanel rdp = i.next();
         rdp.setSnapToGrid(SNAP_TO_GRID_MENU.isSelected());
       }
    }
    
    public SnapToGridAction(){
            this (Utilities.actionsGlobalContext());
    }
    
    private SnapToGridAction(Lookup lkp) {
        
        
        SNAP_TO_GRID_MENU  = new JCheckBoxMenuItem(getName());
        this.lkp = lkp;
        result = lkp.lookupResult(ReportDesignerPanel.class);
        result.addLookupListener(this);
        result.allItems();
        SNAP_TO_GRID_MENU.addActionListener(this);
        setMenu();
    }
    
    public void resultChanged(LookupEvent e) {
        setMenu();
    }
    
    protected void setMenu(){
        
            Iterator<? extends ReportDesignerPanel> i = result.allInstances().iterator();
            SNAP_TO_GRID_MENU.setEnabled(i.hasNext());
            if (i.hasNext())
            {
                ReportDesignerPanel rdp = i.next();
                SNAP_TO_GRID_MENU.setSelected(rdp.isGridVisible());
            }
        }
    
    public String getName() {
        return NbBundle.getMessage(SnapToGridAction.class, "CTL_SnapToGridAction");
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
        return SNAP_TO_GRID_MENU;
    }
       
}